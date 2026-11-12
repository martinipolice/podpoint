package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Inventory;
import model.InventoryItem;
import model.Product;
import model.User;
import util.FileHandler;

/***********************************************************
 * The Inventory screen. The Manager can view stock, restock,
 * add new products and change prices.
 * It is a VBox, so it can be placed in any Scene (window switching).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-06 
 ***********************************************************/

public class InventoryScreen extends VBox {

	private Inventory inventory;
	private ListView<String> list;

	// fills the list box with one line per inventory item
	private void showItems() {
		list.getItems().clear();
		for (InventoryItem item : inventory.getItems()) {
			Product p = item.getProduct();
			String line = p.getName() + " (" + p.getSize() + ") - PHP "
					+ String.format("%.2f", p.getPrice()) + " | Stock: " + item.getQuantity();
			if (item.isLow()) {
				line = line + "  [LOW]";
			}
			list.getItems().add(line);
		}
	}

	// reusable piece: a label placed next to its text box
	private HBox labeledField(String labelText, TextField field) {
		Label label = new Label(labelText);
		label.setPrefWidth(110);
		field.setPrefWidth(300);

		HBox row = new HBox(10);
		row.getChildren().add(label);
		row.getChildren().add(field);
		return row;
	}

	public InventoryScreen(User user) {
		inventory = FileHandler.loadInventory();

		Label title = new Label("Pod Point - Inventory");
		list = new ListView<>();
		Label message = new Label("");
		TextField amountField = new TextField();
		amountField.setPromptText("Amount to add");

		Button restockButton = new Button("Restock");
		restockButton.setOnAction(event -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				message.setText("Select a product first.");
				return;
			}
			try {
				int amount = Integer.parseInt(amountField.getText());
				InventoryItem item = inventory.getItems().get(index);
				Product p = item.getProduct();
				if (inventory.restock(p.getName(), p.getSize(), amount)) {
					FileHandler.saveInventory(inventory);
					showItems();
					message.setText("Restocked " + p.getName() + " by " + amount + ".");
				} else {
					message.setText("Amount must be more than 0.");
				}
			} catch (NumberFormatException e) {
				message.setText("Please type a whole number.");
			}
		});

		// ----- change the price of the selected product -----
		TextField newPriceField = new TextField();
		newPriceField.setPromptText("New price");

		Button priceButton = new Button("Update Price");
		priceButton.setOnAction(event -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				message.setText("Select a product first.");
				return;
			}
			try {
				double newPrice = Double.parseDouble(newPriceField.getText());
				if (newPrice <= 0) {
					message.setText("Price must be above 0.");
					return;
				}
				Product p = inventory.getItems().get(index).getProduct();
				p.setPrice(newPrice);
				FileHandler.saveInventory(inventory);
				showItems();
				message.setText("Price of " + p.getName() + " (" + p.getSize() + ") is now PHP "
						+ String.format("%.2f", newPrice) + ".");
				newPriceField.clear();
			} catch (NumberFormatException e) {
				message.setText("Price must be a number.");
			}
		});

		// ----- form to add a new product -----
		TextField nameField = new TextField();
		nameField.setPromptText("Product name");
		TextField descField = new TextField();
		descField.setPromptText("Description");
		TextField sizeField = new TextField();
		sizeField.setPromptText("Size (e.g. 100g)");
		TextField priceField = new TextField();
		priceField.setPromptText("Price");
		TextField categoryField = new TextField();
		categoryField.setPromptText("Category");
		TextField qtyField = new TextField();
		qtyField.setPromptText("Starting stock");

		Button addButton = new Button("Add Product");
		addButton.setOnAction(event -> {
			String name = nameField.getText().trim();
			String desc = descField.getText().trim();
			String size = sizeField.getText().trim();
			String category = categoryField.getText().trim();

			if (name.isEmpty() || size.isEmpty() || category.isEmpty()) {
				message.setText("Name, size and category are required.");
				return;
			}
			try {
				double price = Double.parseDouble(priceField.getText());
				int qty = Integer.parseInt(qtyField.getText());
				if (price <= 0 || qty < 0) {
					message.setText("Price must be above 0 and stock cannot be negative.");
					return;
				}
				if (inventory.findItem(name, size) != null) {
					message.setText("That product and size already exists.");
					return;
				}
				Product p = new Product(name, desc, size, price, category);
				inventory.addItem(new InventoryItem(p, qty, 5));
				FileHandler.saveInventory(inventory);
				showItems();
				message.setText("Added " + name + " (" + size + ").");
				nameField.clear();
				descField.clear();
				sizeField.clear();
				priceField.clear();
				categoryField.clear();
				qtyField.clear();
			} catch (NumberFormatException e) {
				message.setText("Price and stock must be numbers.");
			}
		});

		showItems();

		list.setPrefHeight(160);

		setSpacing(10);
		setPadding(new Insets(10));
		getChildren().add(title);
		getChildren().add(list);

		if (user.canManageProducts()) {
			getChildren().add(new Label("Restock selected product"));
			getChildren().add(labeledField("Amount to add:", amountField));
			getChildren().add(restockButton);
			getChildren().add(new Label("Change price of selected product"));
			getChildren().add(labeledField("New price:", newPriceField));
			getChildren().add(priceButton);

			getChildren().add(new Label("Add a new product"));
			getChildren().add(labeledField("Name:", nameField));
			getChildren().add(labeledField("Description:", descField));
			getChildren().add(labeledField("Size:", sizeField));
			getChildren().add(labeledField("Price (PHP):", priceField));
			getChildren().add(labeledField("Category:", categoryField));
			getChildren().add(labeledField("Starting stock:", qtyField));
			getChildren().add(addButton);
		} else {
			getChildren().add(new Label("View only. Only the Manager can change products."));
		}

		getChildren().add(message);
	}
}