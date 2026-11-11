package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.User;
import ui.CashScreen;
import ui.HistoryScreen;
import ui.InventoryScreen;
import ui.LoginScreen;
import ui.SalesScreen;

/***********************************************************
 * Starting point of Pod Point. Opens the Login screen first,
 * then the main window with a menu at the top and the screen
 * shown in the middle (window switching).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-22
 ***********************************************************/

public class Main extends Application {

	private Stage stage;

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("Pod Point");
		showLogin();
		stage.show();
	}

	// the Welcome/Login window
	public void showLogin() {
		stage.setScene(new Scene(new LoginScreen(this), 650, 820));
	}

	// the main window after a correct login (same menu for everyone for now)
	public void showDashboard(User user) {
		BorderPane root = new BorderPane();

		Button inventoryButton = new Button("Inventory");
		Button salesButton = new Button("Sales");
		Button historyButton = new Button("History");
		Button cashButton = new Button("Cash");
		Button logoutButton = new Button("Logout");

		// each click puts a fresh screen in the middle of the window (window switching)
		inventoryButton.setOnAction(event -> root.setCenter(new InventoryScreen()));
		salesButton.setOnAction(event -> root.setCenter(new SalesScreen()));
		historyButton.setOnAction(event -> root.setCenter(new HistoryScreen()));
		cashButton.setOnAction(event -> root.setCenter(new CashScreen()));
		logoutButton.setOnAction(event -> showLogin());

		Label whoLabel = new Label("Logged in as " + user.getFullName() + " (" + user.getRole() + ")");

		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10));
		menu.getChildren().add(inventoryButton);
		menu.getChildren().add(salesButton);
		menu.getChildren().add(historyButton);
		menu.getChildren().add(cashButton);
		menu.getChildren().add(logoutButton);
		menu.getChildren().add(whoLabel);

		root.setTop(menu);
		root.setCenter(new SalesScreen());

		stage.setScene(new Scene(root, 650, 820));
	}

	public static void main(String[] args) {
		launch(args);
	}
}