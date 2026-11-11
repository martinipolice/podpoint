package ui;

import app.Main;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
		Label title = new Label("Welcome to Pod Point");
		Label subtitle = new Label("Please log in");

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		usernameField.setMaxWidth(250);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(250);

		Label message = new Label("");

		Button loginButton = new Button("Log In");
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

		setSpacing(10);
		setPadding(new Insets(40));
		getChildren().add(title);
		getChildren().add(subtitle);
		getChildren().add(usernameField);
		getChildren().add(passwordField);
		getChildren().add(loginButton);
		getChildren().add(message);
	}
}