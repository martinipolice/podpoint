package ui;

import javafx.scene.control.ListCell;

/***********************************************************
 * A row of a list box that shows its text and adds a style class
 * when the text has a status: [LOW] for low stock and [VOIDED]
 * for a voided transaction. The CSS gives each status its color.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-14 
 ***********************************************************/
public class StatusCell extends ListCell<String> {

	@Override
	protected void updateItem(String text, boolean empty) {
		super.updateItem(text, empty);

		// rows are reused, so clear the old status first
		getStyleClass().remove("row-low");
		getStyleClass().remove("row-voided");

		if (empty || text == null) {
			setText(null);
			return;
		}

		setText(text);
		if (text.contains("[LOW]")) {
			getStyleClass().add("row-low");
		}
		if (text.contains("[VOIDED]")) {
			getStyleClass().add("row-voided");
		}
	}
}
