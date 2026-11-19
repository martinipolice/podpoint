package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;
import ui.Dashboard;
import ui.LoginScreen;
import util.Theme;

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
		stage.getIcons().add(new Image("file:resources/logo.png"));
		stage.setMinWidth(900);
		stage.setMinHeight(640);
		showLogin();
		stage.show();
	}

	// the Welcome/Login window
	public void showLogin() {
		Scene scene = new Scene(new LoginScreen(this), 1000, 700);
		Theme.apply(scene);
		stage.setScene(scene);
	}

	// the main window after a correct login
	public void showDashboard(User user) {
		Scene scene = new Scene(new Dashboard(this, user), 1000, 700);
		Theme.apply(scene);
		stage.setScene(scene);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
