package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

/***********************************************************
 * Reusable UI parts. Every screen builds its titles, section
 * headings and buttons here, so they look the same everywhere
 * and the style is written in only one place.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-12 13:00
 ***********************************************************/
public class Parts {

	// the big heading at the top of a screen
	public static Label title(String text) {
		Label label = new Label(text);
		label.getStyleClass().add("page-title");
		return label;
	}

	// a small bold heading above a list or a group of fields
	public static Label section(String text) {
		Label label = new Label(text);
		label.getStyleClass().addAll("font-bold", "text-cocoa");
		return label;
	}

	// a large bold line for totals and balances
	public static Label highlight(String text) {
		Label label = new Label(text);
		label.getStyleClass().addAll("text-lg", "font-bold", "text-cocoa");
		return label;
	}

	// the main action of a screen or section (terracotta)
	public static Button primaryButton(String text) {
		Button button = new Button(text);
		button.getStyleClass().add("primary");
		return button;
	}

	// an action that undoes something (red outline)
	public static Button dangerButton(String text) {
		Button button = new Button(text);
		button.getStyleClass().add("danger");
		return button;
	}

	// the text shown inside an empty list
	public static Label emptyHint(String text) {
		Label label = new Label(text);
		label.getStyleClass().add("text-muted");
		return label;
	}
}
