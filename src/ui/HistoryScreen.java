package ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import model.Inventory;
import model.Transaction;
import model.User;
import util.FileHandler;

/***********************************************************
 * The transaction history screen. Shows every sale and cash entry
 * and the cash balance (voided transactions are not counted).
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-07 
 ***********************************************************/

public class HistoryScreen extends VBox {

	private ArrayList<Transaction> history;
	private ListView<String> historyList;
	private Label balanceLabel;

	// fills the list with one line per transaction and updates the balance
	private void showHistory() {
		historyList.getItems().clear();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
		double balance = 0;
		for (Transaction t : history) {
			String line = "#" + t.getId() + " | " + t.getDescription() + " | " + t.getClerkName()
					+ " | " + t.getDate().format(format)
					+ " | PHP " + String.format("%.2f", t.getTotal());
			if (t.isVoided()) {
				line = line + "  [VOIDED]";
			} else {
				balance = balance + t.getTotal();
			}
			historyList.getItems().add(line);
		}
		balanceLabel.setText("Balance: PHP " + String.format("%.2f", balance));
	}
	
	

	public HistoryScreen(User user) {
		history = FileHandler.loadTransactions();

		Label title = new Label("Pod Point - Transaction History");
		historyList = new ListView<>();
		historyList.setPrefHeight(300);
		balanceLabel = new Label("");

		showHistory();
		
		Label message = new Label("");

		Button voidButton = new Button("Void Selected");
		voidButton.setOnAction(event -> {
			int index = historyList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				message.setText("Select a transaction first.");
				return;
			}
			Transaction t = history.get(index);
			if (t.isVoided()) {
				message.setText("Transaction #" + t.getId() + " is already voided.");
				return;
			}
			// mark it voided and let the transaction undo its own effect on the stock
			Inventory inventory = FileHandler.loadInventory();
			t.voidTransaction();
			t.undoEffect(inventory);
			FileHandler.saveInventory(inventory);
			FileHandler.saveTransactions(history);
			showHistory();
			message.setText("Transaction #" + t.getId() + " voided.");
		});

		setSpacing(10);
		setPadding(new Insets(10));
		getChildren().add(title);
		getChildren().add(historyList);
		getChildren().add(balanceLabel);
		if (user.canVoidTransactions()) {
			getChildren().add(voidButton);
		}
		getChildren().add(message);
	}
}