package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ui.HistoryScreen;
import ui.InventoryScreen;
import ui.SalesScreen;
import ui.CashScreen;

/***********************************************************
 * Starting point of Pod Point. Opens the window with a menu
 * at the top and switches the screen shown in the middle.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-22
 ***********************************************************/

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		BorderPane root = new BorderPane();

		Button inventoryButton = new Button("Inventory");
		Button salesButton = new Button("Sales");
		Button historyButton = new Button("History");
		Button cashButton = new Button("Cash");
		cashButton.setOnAction(event -> root.setCenter(new CashScreen()));

		// each click puts a fresh screen in the middle of the window (window switching)
		inventoryButton.setOnAction(event -> root.setCenter(new InventoryScreen()));
		salesButton.setOnAction(event -> root.setCenter(new SalesScreen()));
		historyButton.setOnAction(event -> root.setCenter(new HistoryScreen()));

		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10));
		menu.getChildren().add(inventoryButton);
		menu.getChildren().add(salesButton);
		menu.getChildren().add(historyButton);
		menu.getChildren().add(cashButton);

		root.setTop(menu);
		root.setCenter(new SalesScreen());

		Scene scene = new Scene(root, 650, 820);
		stage.setTitle("Pod Point");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}