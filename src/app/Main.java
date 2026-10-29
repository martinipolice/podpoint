package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

		Button restockButton = new Button("Restock +10");
		// runs when the button is clicked
		restockButton.setOnAction(event -> {
			int index = list.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				message.setText("Select a product first.");
			} else {
				InventoryItem item = inventory.getItems().get(index);
				item.setQuantity(item.getQuantity() + 10);
				FileHandler.saveInventory(inventory);
				showItems();
				message.setText("Restocked " + item.getProduct().getName() + " by 10.");
			}
		});

		showItems();

		VBox root = new VBox(10);
		root.getChildren().add(title);
		root.getChildren().add(list);
		root.getChildren().add(restockButton);
		root.getChildren().add(message);

		Scene scene = new Scene(root, 500, 450);
		stage.setTitle("Pod Point");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}