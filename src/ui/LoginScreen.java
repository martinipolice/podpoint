package ui;

import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.User;
import util.Accounts;

/***********************************************************
 * The Welcome/Login screen. When the login is correct, it asks
 * Main to show the dashboard of that user (window switching).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 11:55
 ***********************************************************/
public class LoginScreen extends VBox {

	public LoginScreen(Main main) {
		// the logo is loaded from the resources folder of the project
		ImageView logo = new ImageView(new Image("file:resources/logo_text.png", 180, 0, true, true));

		Label title = Parts.title("Log in");

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		usernameField.setMaxWidth(250);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(250);

		Label message = new Label("");
		message.getStyleClass().add("text-danger");

		Button loginButton = Parts.primaryButton("Log In");
		loginButton.setOnAction(event -> {
			String username = usernameField.getText().trim();
			String password = passwordField.getText();

			if (username.isEmpty() || password.isEmpty()) {
				message.setText("Type your username and password.");
				return;
			}

			User user = Accounts.login(username, password);
			if (user == null) {
				message.setText("Wrong username or password.");
				passwordField.clear();
			} else {
				main.showDashboard(user);
			}
		});

		getStyleClass().add("login");
		setAlignment(Pos.CENTER);
		setSpacing(10);
		setPadding(new Insets(30));
		getChildren().add(logo);
		getChildren().add(title);
		getChildren().add(usernameField);
		getChildren().add(passwordField);
		getChildren().add(loginButton);
		getChildren().add(message);
	}
}
