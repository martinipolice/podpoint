package ui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CashEntry;
import model.Transaction;
import model.User;
import util.FileHandler;

/***********************************************************
 * The cash screen. The Clerk records cash in and cash out
 * and sees the current balance of the cash box.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-08 
 ***********************************************************/

public class CashScreen extends VBox {

	private TextField descField;
	private TextField amountField;
	private Label balanceLabel;
	private Label message;
	private User user;

	// adds up the totals of the transactions that are not voided
	private double balanceOf(ArrayList<Transaction> list) {
		double balance = 0;
		for (Transaction t : list) {
			if (!t.isVoided()) {
				balance = balance + t.getTotal();
			}
		}
		return balance;
	}

	private void showBalance() {
		double balance = balanceOf(FileHandler.loadTransactions());
		balanceLabel.setText("Current balance: PHP " + String.format("%.2f", balance));
	}

	// records one cash entry (cashIn is true for money in, false for money out)
	private void recordEntry(boolean cashIn) {
		String description = descField.getText().trim();
		if (description.isEmpty()) {
			message.setText("Please type a description.");
			return;
		}
		try {
			double amount = Double.parseDouble(amountField.getText());

			// load first, so the new entry gets the next id
			ArrayList<Transaction> history = FileHandler.loadTransactions();
			double balance = balanceOf(history);
			if (!cashIn && amount > balance) {
				message.setText("Not enough cash. Balance is PHP " + String.format("%.2f", balance) + ".");
				return;
			}

			CashEntry entry = new CashEntry(user.getFullName(), description, amount, cashIn);
			history.add(entry);
			FileHandler.saveTransactions(history);

			showBalance();
			message.setText("Recorded entry #" + entry.getId() + ".");
			descField.clear();
			amountField.clear();
		} catch (NumberFormatException e) {
			message.setText("Amount must be a number.");
		} catch (IllegalArgumentException e) {
			message.setText(e.getMessage());
		}
	}

	public CashScreen(User user) {
		this.user = user;
		Label title = Parts.title("Cash");
		balanceLabel = Parts.highlight("");
		message = new Label("");

		descField = new TextField();
		descField.setPromptText("e.g. Starting cash, Delivery fee");
		descField.setPrefWidth(300);
		amountField = new TextField();
		amountField.setPromptText("Amount");
		amountField.setPrefWidth(300);

		Button cashInButton = Parts.primaryButton("Cash In");
		cashInButton.setOnAction(event -> recordEntry(true));
		Button cashOutButton = new Button("Cash Out");
		cashOutButton.setOnAction(event -> recordEntry(false));

		showBalance();

		HBox descRow = new HBox(10);
		descRow.getChildren().add(new Label("Description:"));
		descRow.getChildren().add(descField);

		HBox amountRow = new HBox(10);
		amountRow.getChildren().add(new Label("Amount (PHP):"));
		amountRow.getChildren().add(amountField);

		HBox buttonRow = new HBox(10);
		buttonRow.getChildren().add(cashInButton);
		buttonRow.getChildren().add(cashOutButton);

		setSpacing(10);
		setPadding(new Insets(10));
		getChildren().add(title);
		getChildren().add(balanceLabel);
		getChildren().add(descRow);
		getChildren().add(amountRow);
		getChildren().add(buttonRow);
		getChildren().add(message);
	}
}