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
 * @created_date 2026-11-11
 ***********************************************************/
public class LoginScreen extends VBox {

	public LoginScreen(Main main) {
		// the logo is loaded from the resources folder of the project
		ImageView logo = new ImageView(new Image("file:resources/logo.png", 130, 130, true, true));

		Label wordmark = new Label("pod point");
		wordmark.getStyleClass().add("wordmark");

		Label tagline = new Label("Sales and inventory for Cacao Culture");
		tagline.getStyleClass().add("text-muted");

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");

		Label message = Parts.message();

		Button loginButton = Parts.primaryButton("Log In");
		loginButton.setMaxWidth(Double.MAX_VALUE);
		loginButton.setOnAction(event -> {
			String username = usernameField.getText().trim();
			String password = passwordField.getText();

			if (username.isEmpty() || password.isEmpty()) {
				Parts.error(message, "Type your username and password.");
				return;
			}

			User user = Accounts.login(username, password);
			if (user == null) {
				Parts.error(message, "Wrong username or password.");
				passwordField.clear();
			} else {
				main.showDashboard(user);
			}
		});

		// Enter in the username box moves on, Enter in the password box logs in
		usernameField.setOnAction(event -> passwordField.requestFocus());
		passwordField.setOnAction(event -> loginButton.fire());

		VBox card = Parts.card();
		card.setMaxWidth(340);
		card.getStyleClass().add("shadow");
		card.getChildren().add(Parts.title("Log in"));
		card.getChildren().add(Parts.fieldWithCaption("Username", usernameField));
		card.getChildren().add(Parts.fieldWithCaption("Password", passwordField));
		card.getChildren().add(loginButton);
		card.getChildren().add(message);

		getStyleClass().add("login");
		setAlignment(Pos.CENTER);
		setSpacing(8);
		setPadding(new Insets(30));
		getChildren().add(logo);
		getChildren().add(wordmark);
		getChildren().add(tagline);
		getChildren().add(card);
		VBox.setMargin(card, new Insets(14, 0, 0, 0));
	}
}
