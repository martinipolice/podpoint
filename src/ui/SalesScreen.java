package ui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import model.SaleLine;
import model.SaleTransaction;
import model.Transaction;
import model.User;
import util.FileHandler;
import util.InsufficientStockException;

/***********************************************************
 * The sales screen. The title is on top. Under it, the Clerk picks
 * products in the left card, types a quantity and adds them to the
 * cart in the right card, then checks out. Both cards start and end
 * at the same height.
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
			productList.getItems().add(Parts.productLine(item));
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

	public SalesScreen(User user) {
		inventory = FileHandler.loadInventory();
		cart = new ArrayList<SaleLine>();

		Label title = Parts.title("New Sale");
		productList = new ListView<>();
		Parts.statusRows(productList);
		VBox.setVgrow(productList, Priority.ALWAYS);
		cartList = new ListView<>();
		cartList.setPlaceholder(Parts.placeholder("Your cart is empty. Add a product to start a sale."));
		VBox.setVgrow(cartList, Priority.ALWAYS);
		totalLabel = Parts.highlight("Total: PHP 0.00");
		Label message = Parts.message();

		TextField qtyField = new TextField();
		qtyField.setPromptText("Quantity");
		qtyField.setPrefWidth(120);

		Button addButton = new Button("Add to Cart");
		// the button is disabled while no product is selected
		addButton.disableProperty().bind(productList.getSelectionModel().selectedItemProperty().isNull());
		addButton.setOnAction(event -> {
			int index = productList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				Parts.error(message, "Select a product first.");
				return;
			}
			try {
				int qty = Integer.parseInt(qtyField.getText());
				InventoryItem item = inventory.getItems().get(index);
				if (qty <= 0) {
					Parts.error(message, "Quantity must be more than 0.");
					return;
				}
				if (qty > item.getQuantity()) {
					Parts.error(message, "Only " + item.getQuantity() + " left of "
							+ item.getProduct().getName() + ".");
					return;
				}
				cart.add(new SaleLine(item.getProduct(), qty));
				showCart();
				Parts.success(message, "Added to cart.");
				qtyField.clear();
			} catch (NumberFormatException e) {
				Parts.error(message, "Please type a whole number.");
			}
		});

		// Enter in the quantity box adds to the cart. A disabled button does
		// nothing when fired, so we say what is missing ourselves.
		qtyField.setOnAction(event -> {
			if (productList.getSelectionModel().getSelectedIndex() == -1) {
				Parts.error(message, "Select a product first.");
			} else {
				addButton.fire();
			}
		});

		Button removeButton = new Button("Remove");
		removeButton.disableProperty().bind(cartList.getSelectionModel().selectedItemProperty().isNull());
		removeButton.setOnAction(event -> {
			int index = cartList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				Parts.error(message, "Select a cart line first.");
				return;
			}
			cart.remove(index);
			showCart();
			Parts.success(message, "Line removed.");
		});

		Button clearButton = new Button("Clear Cart");
		clearButton.setOnAction(event -> {
			cart.clear();
			showCart();
			Parts.success(message, "Cart cleared.");
		});

		Button checkoutButton = Parts.primaryButton("Checkout");
		checkoutButton.setOnAction(event -> {
			if (cart.isEmpty()) {
				Parts.error(message, "Cart is empty.");
				return;
			}
			// take the stock out for every line in the cart
			try {
				for (SaleLine line : cart) {
					Product p = line.getProduct();
					InventoryItem item = inventory.findItem(p.getName(), p.getSize());
					item.removeStock(line.getQuantity());
				}
			} catch (InsufficientStockException e) {
				// undo: reload the inventory from the file, which still has the old stock
				inventory = FileHandler.loadInventory();
				showProducts();
				Parts.error(message, "Sale cancelled. " + e.getMessage());
				return;
			}

			// load the history first, so the new sale gets the next id
			ArrayList<Transaction> history = FileHandler.loadTransactions();
			SaleTransaction sale = new SaleTransaction(user.getFullName());
			for (SaleLine line : cart) {
				sale.addLine(line);
			}
			history.add(sale);

			FileHandler.saveTransactions(history);
			FileHandler.saveInventory(inventory);

			Parts.success(message, "Sale #" + sale.getId() + " complete. Total: PHP "
					+ String.format("%.2f", sale.getTotal()));
			cart.clear();
			showCart();
			showProducts();
		});

		showProducts();
		showCart();

		// left card: the products and the quantity
		HBox qtyRow = new HBox(10);
		qtyRow.setAlignment(Pos.CENTER_LEFT);
		qtyRow.getChildren().add(new Label("Quantity:"));
		qtyRow.getChildren().add(qtyField);
		qtyRow.getChildren().add(addButton);

		VBox leftCard = Parts.card();
		HBox.setHgrow(leftCard, Priority.ALWAYS);
		leftCard.getChildren().add(Parts.section("Products"));
		leftCard.getChildren().add(productList);
		leftCard.getChildren().add(qtyRow);

		// right card: the cart
		HBox buttonRow = new HBox(10);
		buttonRow.getChildren().add(checkoutButton);
		buttonRow.getChildren().add(removeButton);
		buttonRow.getChildren().add(clearButton);

		VBox rightCard = Parts.card();
		rightCard.setPrefWidth(340);
		rightCard.setMinWidth(340);
		rightCard.getChildren().add(Parts.section("Cart"));
		rightCard.getChildren().add(cartList);
		rightCard.getChildren().add(totalLabel);
		rightCard.getChildren().add(buttonRow);
		rightCard.getChildren().add(message);

		// the two cards sit side by side, under the title
		HBox body = new HBox(16);
		body.getChildren().add(leftCard);
		body.getChildren().add(rightCard);
		VBox.setVgrow(body, Priority.ALWAYS);

		setSpacing(12);
		setPadding(new Insets(16));
		getChildren().add(title);
		getChildren().add(body);
	}
}
