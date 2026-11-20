package ui;

import java.util.ArrayList;
import app.Main;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import model.User;

/***********************************************************
 * The dashboard shown after login. The menu at the top depends
 * on the kind of user, and the screen in the middle is switched
 * with setCenter (window switching). The Manager has every screen
 * and can also open the dashboard of a clerk (then a "Back to Manager"
 * button is shown).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11
 ***********************************************************/

public class Dashboard extends BorderPane {

	private ArrayList<Button> tabs = new ArrayList<>();

	// shows a screen in the middle and marks its button as the active one
	private void show(Node screen, Button button) {
		setCenter(screen);
		for (Button b : tabs) {
			b.getStyleClass().remove("active");
		}
		button.getStyleClass().add("active");
	}

	// a normal dashboard, after a login
	public Dashboard(Main main, User user) {
		this(main, user, null);
	}

	// manager is the Manager who opened this clerk's dashboard; null after a normal login
	public Dashboard(Main main, User user, User manager) {
		Button inventoryButton = new Button("Inventory");
		Button salesButton = new Button("Sales");
		Button historyButton = new Button("History");
		Button cashButton = new Button("Cash");
		Button clerkSalesButton = new Button("Clerk Sales");
		Button logoutButton = new Button("Log Out");

		tabs.add(inventoryButton);
		tabs.add(salesButton);
		tabs.add(historyButton);
		tabs.add(cashButton);
		tabs.add(clerkSalesButton);

		// each click puts a fresh screen in the middle of the window
		inventoryButton.setOnAction(event -> show(new InventoryScreen(user), inventoryButton));
		salesButton.setOnAction(event -> show(new SalesScreen(user), salesButton));
		historyButton.setOnAction(event -> show(new HistoryScreen(user), historyButton));
		cashButton.setOnAction(event -> show(new CashScreen(user), cashButton));
		clerkSalesButton.setOnAction(event -> show(new ClerkSalesScreen(main, user), clerkSalesButton));
		logoutButton.setOnAction(event -> main.showLogin());

		// small logo on a cream badge, so it can be seen on the dark bar
		ImageView logo = new ImageView(new Image("file:resources/logo.png", 30, 30, true, true));
		HBox logoBadge = new HBox();
		logoBadge.getStyleClass().add("logo-badge");
		logoBadge.getChildren().add(logo);

		Label whoLabel = new Label(user.getFullName());
		Label roleBadge = new Label(user.getRole());
		roleBadge.getStyleClass().add("role-badge");

		// an empty space that pushes the name and Log Out to the right
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		HBox menu = new HBox(10);
		menu.getStyleClass().add("top-bar");
		// 10 above and below, 16 on the sides like the padding of every screen
		menu.setPadding(new Insets(10, 16, 10, 16));
		menu.getChildren().add(logoBadge);

		if (user.canManageProducts()) {
			// Manager menu: full access, so every screen is here
			menu.getChildren().add(inventoryButton);
			menu.getChildren().add(salesButton);
			menu.getChildren().add(cashButton);
			menu.getChildren().add(historyButton);
			menu.getChildren().add(clerkSalesButton);
			show(new InventoryScreen(user), inventoryButton);
		} else {
			// Clerk menu: records sales and cash, checks inventory, voids in History
			menu.getChildren().add(salesButton);
			menu.getChildren().add(cashButton);
			menu.getChildren().add(historyButton);
			menu.getChildren().add(inventoryButton);
			show(new SalesScreen(user), salesButton);
		}

		menu.getChildren().add(spacer);
		menu.getChildren().add(whoLabel);
		menu.getChildren().add(roleBadge);
		if (manager != null) {
			// the Manager is looking at a clerk's dashboard: a way back
			Button backButton = new Button("Back to " + manager.getRole());
			backButton.setOnAction(event -> main.showDashboard(manager));
			menu.getChildren().add(backButton);
		}
		menu.getChildren().add(logoutButton);
		setTop(menu);
	}
}
