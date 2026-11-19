package util;

import javafx.scene.Scene;

/***********************************************************
 * Loads the CSS files of Pod Point into a Scene, so every
 * screen has the same look. The order matters: later files
 * win over earlier ones, so utilities.css is last.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-12
 ***********************************************************/
public class Theme {

	public static void apply(Scene scene) {
		String[] files = { "base.css", "buttons.css", "lists.css", "forms.css",
				"topbar.css", "login.css", "utilities.css" };
		for (String file : files) {
			scene.getStylesheets().add(Theme.class.getResource("/css/" + file).toExternalForm());
		}
	}
}
