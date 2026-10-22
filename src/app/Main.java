package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	// start() is the JavaFX version of main(). JavaFX calls it for us.
	// The Stage is the window, the Scene is what is shown inside it.
	@Override
	public void start(Stage stage) {
		Label label = new Label("Hello, Agri DMS!");
		StackPane root = new StackPane(label);
		Scene scene = new Scene(root, 400, 300);

		stage.setTitle("Agri DMS");
		stage.setScene(scene);
		stage.show();
	}

	// main() just hands control to JavaFX through launch()
	public static void main(String[] args) {
		launch(args);
	}
}