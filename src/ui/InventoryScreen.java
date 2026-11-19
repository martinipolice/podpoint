package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

	// fills the list box with one line per inventory item and updates the tiles
	private void showItems() {
		list.getItems().clear();
		int low = 0;
		int units = 0;
		for (InventoryItem item : inventory.getItems()) {
			Product p = item.getProduct();
			String line = p.getName() + " (" + p.getSize() + ") - PHP "
					+ String.format("%.2f", p.getPrice()) + " | Stock: " + item.getQuantity();
			if (item.isLow()) {
				line = line + "  [LOW]";
				low++;
			}
			units = units + item.getQuantity();
			list.getItems().add(line);
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
			} else {
				Product p = inventory.getItems().get(index).getProduct();
				selectedTitle.setText(p.getName() + " (" + p.getSize() + ")");
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
					Parts.error(message, "Amount must be more than 0.");
				}
			} catch (NumberFormatException e) {
				Parts.error(message, "Please type a whole number.");
			}
		});

		// ----- change the price of the selected product -----
		TextField newPriceField = new TextField();
		newPriceField.setPromptText("New price (PHP)");

		Button priceButton = Parts.primaryButton("Update Price");
		priceButton.setMinWidth(120);
		priceButton.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
		priceButton.setOnAction(event -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				Parts.error(message, "Select a product first.");
				return;
			}
			try {
				double newPrice = Double.parseDouble(newPriceField.getText());
				if (newPrice <= 0 || Double.isNaN(newPrice) || Double.isInfinite(newPrice)) {
					Parts.error(message, "Price must be above 0.");
					return;
				}
				Product p = inventory.getItems().get(index).getProduct();
				p.setPrice(newPrice);
				FileHandler.saveInventory(inventory);
				showItems();
				list.getSelectionModel().select(index);
				Parts.success(message, "Price of " + p.getName() + " (" + p.getSize() + ") is now PHP "
						+ String.format("%.2f", newPrice) + ".");
				newPriceField.clear();
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
				if (price <= 0 || Double.isNaN(price) || Double.isInfinite(price) || qty < 0) {
					Parts.error(message, "Price must be above 0 and stock cannot be negative.");
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

			HBox priceRow = new HBox(10);
			HBox.setHgrow(newPriceField, Priority.ALWAYS);
			priceRow.getChildren().add(newPriceField);
			priceRow.getChildren().add(priceButton);

			VBox selectedCard = Parts.card();
			selectedCard.getChildren().add(selectedTitle);
			selectedCard.getChildren().add(restockRow);
			selectedCard.getChildren().add(priceRow);

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

			// the message is under the forms, next to the buttons that were clicked
			VBox right = new VBox(12);
			right.setPrefWidth(360);
			right.setMinWidth(360);
			right.getChildren().add(selectedCard);
			right.getChildren().add(addCard);
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
