package ui;

import java.time.format.DateTimeFormatter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.InventoryItem;
import model.Product;
import model.Transaction;

/***********************************************************
 * Reusable UI parts. Every screen builds its titles, headings,
 * buttons, cards and tiles here, so they look the same everywhere
 * and each style is written in only one place.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-12 
 ***********************************************************/
public class Parts {

	// ---------- text ----------

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

	// a large bold line for totals
	public static Label highlight(String text) {
		Label label = new Label(text);
		label.getStyleClass().addAll("text-lg", "font-bold", "text-cocoa");
		return label;
	}

	// a small gray caption, for example above a text box
	public static Label caption(String text) {
		Label label = new Label(text);
		label.getStyleClass().addAll("text-sm", "text-muted");
		return label;
	}

	// a plain gray line of text, for example a hint under a list
	public static Label emptyHint(String text) {
		Label label = new Label(text);
		label.getStyleClass().add("text-muted");
		label.setWrapText(true);
		return label;
	}

	// the text shown inside an empty list: centered, wrapped, with space around it
	public static Label placeholder(String text) {
		Label label = new Label(text);
		label.getStyleClass().addAll("text-muted", "p-4");
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.CENTER);
		return label;
	}

	// ---------- messages (green for success, red for errors) ----------

	// an empty message label that wraps long text.
	// It is hidden (and takes no space) until success() or error() shows it.
	public static Label message() {
		Label label = new Label("");
		label.setWrapText(true);
		label.setVisible(false);
		label.setManaged(false);
		return label;
	}

	public static void success(Label label, String text) {
		label.getStyleClass().remove("text-danger");
		if (!label.getStyleClass().contains("text-leaf")) {
			label.getStyleClass().add("text-leaf");
		}
		label.setText(text);
		label.setVisible(true);
		label.setManaged(true);
	}

	public static void error(Label label, String text) {
		label.getStyleClass().remove("text-leaf");
		if (!label.getStyleClass().contains("text-danger")) {
			label.getStyleClass().add("text-danger");
		}
		label.setText(text);
		label.setVisible(true);
		label.setManaged(true);
	}

	// hides a message label again and removes its text
	public static void clear(Label label) {
		label.setText("");
		label.setVisible(false);
		label.setManaged(false);
	}

	// ---------- buttons ----------

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

	// ---------- containers ----------

	// a white box with a border and rounded corners
	public static VBox card() {
		VBox box = new VBox(10);
		box.getStyleClass().addAll("bg-card", "border", "rounded-lg", "p-4");
		return box;
	}

	// Tile hierarchy: the first tile of a screen is the main one (primaryTile),
	// the others support it (tile). A tile can also show a warning (setAlert).

	// a supporting tile: a value with a gray caption under it
	public static VBox tile(Label valueLabel, String captionText) {
		valueLabel.getStyleClass().addAll("tile-value", "text-xl", "font-bold", "text-cocoa");
		return buildTile(valueLabel, captionText);
	}

	// the main tile of a screen: a bigger number on a cream background
	public static VBox primaryTile(Label valueLabel, String captionText) {
		valueLabel.getStyleClass().addAll("tile-value", "text-2xl", "font-bold", "text-cocoa");
		VBox box = buildTile(valueLabel, captionText);
		box.getStyleClass().add("tile-primary");
		return box;
	}

	// turns the amber warning stripe of a tile on or off
	public static void setAlert(VBox tile, boolean on) {
		tile.getStyleClass().remove("tile-alert");
		if (on) {
			tile.getStyleClass().add("tile-alert");
		}
	}

	// the box that tile() and primaryTile() share
	private static VBox buildTile(Label valueLabel, String captionText) {
		VBox box = new VBox(2);
		box.getStyleClass().addAll("tile", "bg-card", "border", "rounded-lg", "p-3");
		box.getChildren().add(valueLabel);
		box.getChildren().add(caption(captionText));
		return box;
	}

	// puts tiles side by side, all with the same width
	public static HBox tileRow(VBox... tiles) {
		HBox row = new HBox(12);
		for (VBox tile : tiles) {
			tile.setPrefWidth(100);
			tile.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(tile, Priority.ALWAYS);
			row.getChildren().add(tile);
		}
		return row;
	}

	// a text box with a small caption above it
	public static VBox fieldWithCaption(String captionText, TextField field) {
		field.setMaxWidth(Double.MAX_VALUE);
		VBox box = new VBox(3);
		box.getChildren().add(caption(captionText));
		box.getChildren().add(field);
		return box;
	}

	// ---------- lists ----------

	// makes rows with [LOW] amber and rows with [VOIDED] gray
	public static void statusRows(ListView<String> list) {
		list.setCellFactory(view -> new StatusCell());
	}

	// one line of text for a product in a list: name, size, price and stock
	// (with [LOW] when it is running out). Used by Inventory and Sales.
	public static String productLine(InventoryItem item) {
		Product p = item.getProduct();
		String line = p.getName() + " (" + p.getSize() + ") - PHP "
				+ String.format("%.2f", p.getPrice()) + " | Stock: " + item.getQuantity();
		if (item.isLow()) {
			line = line + "  [LOW]";
		}
		return line;
	}

	// one line of text for a transaction (used by History, Cash and Clerk Sales).
	// The amount comes first, so it is easy to scan down the list.
	public static String transactionLine(Transaction t) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
		String line = "PHP " + String.format("%.2f", t.getTotal())
				+ " | #" + t.getId() + " " + t.getDescription() + " | " + t.getClerkName()
				+ " | " + t.getDate().format(format);
		if (t.isVoided()) {
			line = line + "  [VOIDED]";
		}
		return line;
	}
}
