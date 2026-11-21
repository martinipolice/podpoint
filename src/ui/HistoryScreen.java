package ui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Inventory;
import model.Transaction;
import model.User;
import util.FileHandler;
import util.PodSystem;

/***********************************************************
 * The transaction history screen. Shows every sale and cash entry,
 * the cash balance (voided transactions are not counted) and, 
 * for users who can void, a button to void a transaction.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-07
 ***********************************************************/

public class HistoryScreen extends VBox {

	private ArrayList<Transaction> history;
	private ListView<String> historyList;
	private Label balanceValue;
	private Label countValue;
	private Label voidedValue;
	private Transaction pendingVoid;   // the transaction waiting for the second (confirm) click

	// puts the Void button back to its normal look and forgets the pending void
	private void resetVoidButton(Button button) {
		pendingVoid = null;
		button.setText("Void Selected");
		button.getStyleClass().remove("danger-fill");
		if (!button.getStyleClass().contains("danger")) {
			button.getStyleClass().add("danger");
		}
	}

	// fills the list with one line per transaction and updates the tiles
	private void showHistory() {
		historyList.getItems().clear();
		int voided = 0;
		for (Transaction t : history) {
			historyList.getItems().add(Parts.transactionLine(t));
			if (t.isVoided()) {
				voided++;
			}
		}
		balanceValue.setText("PHP " + String.format("%.2f", PodSystem.balanceOf(history)));
		countValue.setText(String.valueOf(history.size()));
		voidedValue.setText(String.valueOf(voided));
	}

	public HistoryScreen(User user) {
		history = FileHandler.loadTransactions();

		Label title = Parts.title("Transaction History");
		historyList = new ListView<>();
		historyList.setPlaceholder(Parts.placeholder("No transactions yet."));
		Parts.statusRows(historyList);
		VBox.setVgrow(historyList, Priority.ALWAYS);

		balanceValue = new Label("");
		countValue = new Label("");
		voidedValue = new Label("");

		showHistory();

		Label message = Parts.message();

		Button voidButton = Parts.dangerButton("Void Selected");
		voidButton.setDisable(true);

		// the button works only for a selected transaction that is not voided yet.
		// Selecting another row also cancels a pending void.
		historyList.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
			resetVoidButton(voidButton);
			Parts.clear(message);
			int index = historyList.getSelectionModel().getSelectedIndex();
			voidButton.setDisable(index == -1 || history.get(index).isVoided());
		});

		voidButton.setOnAction(event -> {
			int index = historyList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				Parts.error(message, "Select a transaction first.");
				return;
			}
			Transaction t = history.get(index);
			// the rule lives in PodSystem: not already voided, and the balance stays 0 or more
			String problem = PodSystem.voidProblem(history, t);
			if (problem != null) {
				Parts.error(message, problem);
				return;
			}
			// first click: ask for a second click to confirm
			if (pendingVoid != t) {
				pendingVoid = t;
				voidButton.setText("Confirm Void #" + t.getId());
				voidButton.getStyleClass().remove("danger");
				voidButton.getStyleClass().add("danger-fill");
				Parts.error(message, "Click again to void #" + t.getId() + ". Select another row to cancel.");
				return;
			}
			// second click: back to the normal button, then mark it voided
			// and let the transaction undo its own effect on the stock
			Inventory inventory = FileHandler.loadInventory();
			resetVoidButton(voidButton);
			if (t.voidTransaction()) {
				t.undoEffect(inventory);
			}
			FileHandler.saveInventory(inventory);
			FileHandler.saveTransactions(history);
			showHistory();
			Parts.success(message, "Transaction #" + t.getId() + " voided.");
		});

		setSpacing(12);
		setPadding(new Insets(16));
		getChildren().add(title);
		getChildren().add(Parts.tileRow(
				Parts.primaryTile(balanceValue, "Cash balance"),
				Parts.tile(countValue, "Transactions"),
				Parts.tile(voidedValue, "Voided")));
		getChildren().add(historyList);

		// only the user who can void gets the bottom row (an empty row would
		// leave a gap that other screens do not have)
		if (user.canVoidTransactions()) {
			HBox bottomRow = new HBox(12);
			bottomRow.setAlignment(Pos.CENTER_LEFT);
			bottomRow.getChildren().add(voidButton);
			bottomRow.getChildren().add(message);
			getChildren().add(bottomRow);
		}
	}
}
