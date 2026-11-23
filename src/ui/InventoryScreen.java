package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Inventory;
import model.InventoryItem;
import model.Product;
import model.User;
import util.FileHandler;

/***********************************************************
 * The Inventory screen. Everyone sees the stock list and the three
 * tiles. The Manager also gets the forms on the right to restock,
 * change prices and add new products.
 * The title is on top, and under it are two columns that start at the
 * same height: the tiles and list on the left, the forms on the right.
 * It is a VBox, so it can be placed in any window (window switching).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-06 08:55
 ***********************************************************/
public class InventoryScreen extends VBox {

	private Inventory inventory;
	private ListView<String> list;
	private Label productsValue;
	private Label lowValue;
	private Label unitsValue;
	private VBox lowTile;
	private Label selectedTitle;
	private TextField editDescField = new TextField();
	private TextField editCategoryField = new TextField();
	private TextField editPriceField = new TextField();

	// fills the list box with one line per inventory item and updates the tiles
	private void showItems() {
		list.getItems().clear();
		int low = 0;
		int units = 0;
		for (InventoryItem item : inventory.getItems()) {
			if (item.isLow()) {
				low++;
			}
			units = units + item.getQuantity();
			list.getItems().add(Parts.productLine(item));
		}
		productsValue.setText(String.valueOf(inventory.getItems().size()));
		lowValue.setText(String.valueOf(low));
		unitsValue.setText(String.valueOf(units));
		// the Low stock tile gets an amber stripe when something needs restocking
		Parts.setAlert(lowTile, low > 0);
	}

	public InventoryScreen(User user) {
		inventory = FileHandler.loadInventory();

		Label title = Parts.title("Inventory");
		list = new ListView<>();
		Parts.statusRows(list);
		VBox.setVgrow(list, Priority.ALWAYS);
		Label message = Parts.message();

		productsValue = new Label("");
		lowValue = new Label("");
		unitsValue = new Label("");
		lowTile = Parts.tile(lowValue, "Low stock");

		// the heading of the Manager card shows which product is selected
		selectedTitle = Parts.section("Select a product from the list");
		list.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				selectedTitle.setText("Select a product from the list");
				editDescField.clear();
				editCategoryField.clear();
				editPriceField.clear();
			} else {
				Product p = inventory.getItems().get(index).getProduct();
				selectedTitle.setText(p.getName() + " (" + p.getSize() + ")");
				// the edit boxes show the current details of the product
				editDescField.setText(p.getDescription());
				editCategoryField.setText(p.getCategory());
				editPriceField.setText(String.format("%.2f", p.getPrice()));
			}
		});

		// ----- restock the selected product -----
		TextField amountField = new TextField();
		amountField.setPromptText("Amount to add");

		Button restockButton = Parts.primaryButton("Restock");
		restockButton.setMinWidth(120);
		// the button is disabled while no product is selected
		restockButton.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
		restockButton.setOnAction(event -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				Parts.error(message, "Select a product first.");
				return;
			}
			try {
				int amount = Integer.parseInt(amountField.getText());
				InventoryItem item = inventory.getItems().get(index);
				Product p = item.getProduct();
				if (inventory.restock(p.getName(), p.getSize(), amount)) {
					FileHandler.saveInventory(inventory);
					showItems();
					list.getSelectionModel().select(index);
					Parts.success(message, "Restocked " + p.getName() + " by " + amount + ".");
					amountField.clear();
				} else {
					Parts.error(message, "Amount must be above 0, and the stock cannot go above "
							+ Inventory.MAX_STOCK + ".");
				}
			} catch (NumberFormatException e) {
				Parts.error(message, "Please type a whole number.");
			}
		});

		// ----- edit the details of the selected product -----
		editDescField.setPromptText("Short description");
		editCategoryField.setPromptText("e.g. Snacks");
		editPriceField.setPromptText("0.00");

		Button saveButton = Parts.primaryButton("Save Changes");
		saveButton.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
		saveButton.setOnAction(event -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				Parts.error(message, "Select a product first.");
				return;
			}
			String desc = editDescField.getText().trim();
			String category = editCategoryField.getText().trim();
			if (category.isEmpty()) {
				Parts.error(message, "Category is required.");
				return;
			}
			try {
				double newPrice = Double.parseDouble(editPriceField.getText());
				if (newPrice <= 0 || Double.isNaN(newPrice) || Double.isInfinite(newPrice)) {
					Parts.error(message, "Price must be above 0.");
					return;
				}
				Product p = inventory.getItems().get(index).getProduct();
				p.setDescription(desc);
				p.setCategory(category);
				p.setPrice(newPrice);
				FileHandler.saveInventory(inventory);
				showItems();
				list.getSelectionModel().select(index);
				Parts.success(message, "Saved the changes to " + p.getName() + " (" + p.getSize() + ").");
			} catch (NumberFormatException e) {
				Parts.error(message, "Price must be a number.");
			}
		});

		// ----- form to add a new product -----
		TextField nameField = new TextField();
		nameField.setPromptText("e.g. Cacao Nibs");
		TextField descField = new TextField();
		descField.setPromptText("Short description");
		TextField sizeField = new TextField();
		sizeField.setPromptText("e.g. 100g");
		TextField priceField = new TextField();
		priceField.setPromptText("0.00");
		TextField categoryField = new TextField();
		categoryField.setPromptText("e.g. Snacks");
		TextField qtyField = new TextField();
		qtyField.setPromptText("0");

		Button addButton = Parts.primaryButton("Add Product");
		addButton.setOnAction(event -> {
			String name = nameField.getText().trim();
			String desc = descField.getText().trim();
			String size = sizeField.getText().trim();
			String category = categoryField.getText().trim();

			if (name.isEmpty() || size.isEmpty() || category.isEmpty()) {
				Parts.error(message, "Name, size and category are required.");
				return;
			}
			try {
				double price = Double.parseDouble(priceField.getText());
				int qty = Integer.parseInt(qtyField.getText());
				if (price <= 0 || Double.isNaN(price) || Double.isInfinite(price)
						|| qty < 0 || qty > Inventory.MAX_STOCK) {
					Parts.error(message, "Price must be above 0 and stock must be from 0 to "
							+ Inventory.MAX_STOCK + ".");
					return;
				}
				if (inventory.findItem(name, size) != null) {
					Parts.error(message, "That product and size already exists.");
					return;
				}
				Product p = new Product(name, desc, size, price, category);
				inventory.addItem(new InventoryItem(p, qty, 5));
				FileHandler.saveInventory(inventory);
				showItems();
				Parts.success(message, "Added " + name + " (" + size + ").");
				nameField.clear();
				descField.clear();
				sizeField.clear();
				priceField.clear();
				categoryField.clear();
				qtyField.clear();
			} catch (NumberFormatException e) {
				Parts.error(message, "Price and stock must be numbers.");
			}
		});

		showItems();

		// ----- left column: tiles (main tile first) and the list -----
		VBox left = new VBox(12);
		left.getChildren().add(Parts.tileRow(
				Parts.primaryTile(unitsValue, "Units in stock"),
				Parts.tile(productsValue, "Products"),
				lowTile));
		left.getChildren().add(list);
		HBox.setHgrow(left, Priority.ALWAYS);

		// the two columns sit side by side, under the title
		HBox body = new HBox(16);
		body.getChildren().add(left);
		VBox.setVgrow(body, Priority.ALWAYS);

		// ----- right column: forms and their message, only for the Manager -----
		if (user.canManageProducts()) {
			HBox restockRow = new HBox(10);
			HBox.setHgrow(amountField, Priority.ALWAYS);
			restockRow.getChildren().add(amountField);
			restockRow.getChildren().add(restockButton);

			VBox categoryEditBox = Parts.fieldWithCaption("Category", editCategoryField);
			VBox priceEditBox = Parts.fieldWithCaption("Price (PHP)", editPriceField);
			HBox.setHgrow(categoryEditBox, Priority.ALWAYS);
			HBox.setHgrow(priceEditBox, Priority.ALWAYS);
			HBox categoryPriceRow = new HBox(10);
			categoryPriceRow.getChildren().add(categoryEditBox);
			categoryPriceRow.getChildren().add(priceEditBox);

			VBox selectedCard = Parts.card();
			selectedCard.getChildren().add(selectedTitle);
			selectedCard.getChildren().add(restockRow);
			selectedCard.getChildren().add(Parts.fieldWithCaption("Description", editDescField));
			selectedCard.getChildren().add(categoryPriceRow);
			selectedCard.getChildren().add(saveButton);

			VBox sizeBox = Parts.fieldWithCaption("Size", sizeField);
			VBox priceBox = Parts.fieldWithCaption("Price (PHP)", priceField);
			HBox.setHgrow(sizeBox, Priority.ALWAYS);
			HBox.setHgrow(priceBox, Priority.ALWAYS);
			HBox sizePriceRow = new HBox(10);
			sizePriceRow.getChildren().add(sizeBox);
			sizePriceRow.getChildren().add(priceBox);

			VBox categoryBox = Parts.fieldWithCaption("Category", categoryField);
			VBox qtyBox = Parts.fieldWithCaption("Starting stock", qtyField);
			HBox.setHgrow(categoryBox, Priority.ALWAYS);
			HBox.setHgrow(qtyBox, Priority.ALWAYS);
			HBox categoryQtyRow = new HBox(10);
			categoryQtyRow.getChildren().add(categoryBox);
			categoryQtyRow.getChildren().add(qtyBox);

			VBox addCard = Parts.card();
			addCard.getChildren().add(Parts.section("Add a new product"));
			addCard.getChildren().add(Parts.fieldWithCaption("Name", nameField));
			addCard.getChildren().add(Parts.fieldWithCaption("Description", descField));
			addCard.getChildren().add(sizePriceRow);
			addCard.getChildren().add(categoryQtyRow);
			addCard.getChildren().add(addButton);

			// the two forms scroll when the window is short; the message stays under them
			VBox forms = new VBox(12);
			forms.getChildren().add(selectedCard);
			forms.getChildren().add(addCard);

			ScrollPane scroll = new ScrollPane(forms);
			scroll.setFitToWidth(true);
			scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			VBox.setVgrow(scroll, Priority.ALWAYS);

			VBox right = new VBox(12);
			right.setPrefWidth(385);
			right.setMinWidth(385);
			right.getChildren().add(scroll);
			right.getChildren().add(message);
			body.getChildren().add(right);
		} else {
			left.getChildren().add(Parts.emptyHint("View only. Only the Manager can change products."));
		}

		setSpacing(12);
		setPadding(new Insets(16));
		getChildren().add(title);
		getChildren().add(body);
	}
}
