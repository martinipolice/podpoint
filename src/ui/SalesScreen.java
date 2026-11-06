package ui;

import java.util.ArrayList;
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
import model.SaleLine;
import util.FileHandler;

/***********************************************************
 * The sales screen. The Clerk picks products, types a quantity
 * and adds them to a cart. (Checkout comes in the next step.)
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-06 
 ***********************************************************/

public class SalesScreen extends VBox {

	private Inventory inventory;
	private ArrayList<SaleLine> cart;
	private ListView<String> productList;
	private ListView<String> cartList;
	private Label totalLabel;

	// fills the product list with one line per inventory item
	private void showProducts() {
		productList.getItems().clear();
		for (InventoryItem item : inventory.getItems()) {
			Product p = item.getProduct();
			productList.getItems().add(p.getName() + " (" + p.getSize() + ") - PHP "
					+ String.format("%.2f", p.getPrice()) + " | Stock: " + item.getQuantity());
		}
	}

	// fills the cart list and updates the total
	private void showCart() {
		cartList.getItems().clear();
		double total = 0;
		for (SaleLine line : cart) {
			Product p = line.getProduct();
			cartList.getItems().add(line.getQuantity() + " x " + p.getName() + " (" + p.getSize()
					+ ") = PHP " + String.format("%.2f", line.getSubtotal()));
			total = total + line.getSubtotal();
		}
		totalLabel.setText("Total: PHP " + String.format("%.2f", total));
	}

	public SalesScreen() {
		inventory = FileHandler.loadInventory();
		cart = new ArrayList<SaleLine>();

		Label title = new Label("Pod Point - New Sale");
		productList = new ListView<>();
		productList.setPrefHeight(180);
		cartList = new ListView<>();
		cartList.setPrefHeight(130);
		totalLabel = new Label("Total: PHP 0.00");
		Label message = new Label("");

		TextField qtyField = new TextField();
		qtyField.setPromptText("Quantity");

		Button addButton = new Button("Add to Cart");
		addButton.setOnAction(event -> {
			int index = productList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				message.setText("Select a product first.");
				return;
			}
			try {
				int qty = Integer.parseInt(qtyField.getText());
				InventoryItem item = inventory.getItems().get(index);
				if (qty <= 0) {
					message.setText("Quantity must be more than 0.");
					return;
				}
				if (qty > item.getQuantity()) {
					message.setText("Only " + item.getQuantity() + " left of "
							+ item.getProduct().getName() + ".");
					return;
				}
				cart.add(new SaleLine(item.getProduct(), qty));
				showCart();
				message.setText("Added to cart.");
				qtyField.clear();
			} catch (NumberFormatException e) {
				message.setText("Please type a whole number.");
			}
		});

		Button clearButton = new Button("Clear Cart");
		clearButton.setOnAction(event -> {
			cart.clear();
			showCart();
			message.setText("Cart cleared.");
		});

		showProducts();
		showCart();

		HBox qtyRow = new HBox(10);
		qtyRow.getChildren().add(new Label("Quantity:"));
		qtyRow.getChildren().add(qtyField);
		qtyRow.getChildren().add(addButton);

		setSpacing(10);
		setPadding(new Insets(10));
		getChildren().add(title);
		getChildren().add(new Label("Products"));
		getChildren().add(productList);
		getChildren().add(qtyRow);
		getChildren().add(new Label("Cart"));
		getChildren().add(cartList);
		getChildren().add(totalLabel);
		getChildren().add(clearButton);
		getChildren().add(message);
	}
}