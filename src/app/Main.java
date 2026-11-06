package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.InventoryScreen;

/***********************************************************
 * Starting point of Pod Point. Opens the window and shows a screen.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-22
 ***********************************************************/
public class Main extends Application {

	@Override
	public void start(Stage stage) {
		InventoryScreen inventoryScreen = new InventoryScreen();

		Scene scene = new Scene(inventoryScreen, 500, 780);
		stage.setTitle("Pod Point");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}