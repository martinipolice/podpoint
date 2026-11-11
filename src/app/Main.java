package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import ui.Dashboard;
import ui.LoginScreen;

/***********************************************************
 * Starting point of Pod Point. Opens the Login screen first,
 * then the Dashboard of the user who logged in (window switching).
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

	// the main window after a correct login
	public void showDashboard(User user) {
		stage.setScene(new Scene(new Dashboard(this, user), 650, 820));
	}

	public static void main(String[] args) {
		launch(args);
	}
}