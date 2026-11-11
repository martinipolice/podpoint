package ui;

import app.Main;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.User;

/***********************************************************
 * The dashboard shown after login. The menu at the top depends
 * on the kind of user, and the screen in the middle is switched
 * with setCenter (window switching).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 
 ***********************************************************/

public class Dashboard extends BorderPane {

	public Dashboard(Main main, User user) {
		Button inventoryButton = new Button("Inventory");
		Button salesButton = new Button("Sales");
		Button historyButton = new Button("History");
		Button cashButton = new Button("Cash");
		Button logoutButton = new Button("Logout");

		// each click puts a fresh screen in the middle of the window
		inventoryButton.setOnAction(event -> setCenter(new InventoryScreen()));
		salesButton.setOnAction(event -> setCenter(new SalesScreen(user)));
		historyButton.setOnAction(event -> setCenter(new HistoryScreen()));
		cashButton.setOnAction(event -> setCenter(new CashScreen(user)));
		logoutButton.setOnAction(event -> main.showLogin());

		Label whoLabel = new Label("Logged in as " + user.getFullName() + " (" + user.getRole() + ")");

		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10));

		if (user.canManageProducts()) {
			// Manager menu: manages products and reviews the history
			menu.getChildren().add(inventoryButton);
			menu.getChildren().add(historyButton);
			setCenter(new InventoryScreen());
		} else {
			// Clerk menu: records sales and cash, checks inventory, voids in History
			menu.getChildren().add(salesButton);
			menu.getChildren().add(cashButton);
			menu.getChildren().add(historyButton);
			menu.getChildren().add(inventoryButton);
			setCenter(new SalesScreen(user));
		}

		menu.getChildren().add(logoutButton);
		menu.getChildren().add(whoLabel);
		setTop(menu);
	}
}