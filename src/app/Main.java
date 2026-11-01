package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Inventory;
import model.InventoryItem;
import model.Product;
import util.FileHandler;

public class Main extends Application {

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

	@Override
	public void start(Stage stage) {
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

		list.setPrefHeight(220);

		VBox root = new VBox(10);
		root.getChildren().add(title);
		root.getChildren().add(list);
		root.getChildren().add(amountField);
		root.getChildren().add(restockButton);
		root.getChildren().addAll(nameField, descField, sizeField, priceField, categoryField, qtyField);
		root.getChildren().add(addButton);
		root.getChildren().add(message);

		Scene scene = new Scene(root, 500, 700);
		stage.setTitle("Pod Point");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}