package ui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.CashEntry;
import model.Transaction;
import model.User;
import util.FileHandler;
import util.PodSystem;

/***********************************************************
 * The cash screen. The title is on top. Under it, the Clerk records cash
 * in and cash out on the left and sees the current balance of the cash
 * box. The card on the right shows the recent cash entries.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-08
 ***********************************************************/

public class CashScreen extends VBox {

	private TextField descField;
	private TextField amountField;
	private Label balanceValue;
	private Label message;
	private ListView<String> entryList;
	private User user;

	// updates the balance and the list of cash entries (newest first)
	private void refresh() {
		ArrayList<Transaction> history = FileHandler.loadTransactions();
		balanceValue.setText("PHP " + String.format("%.2f", PodSystem.balanceOf(history)));

		entryList.getItems().clear();
		for (int i = history.size() - 1; i >= 0; i--) {
			Transaction t = history.get(i);
			if (!t.isSale()) {
				entryList.getItems().add(Parts.transactionLine(t));
			}
		}
	}

	// records one cash entry (cashIn is true for money in, false for money out)
	private void recordEntry(boolean cashIn) {
		String description = descField.getText().trim();
		if (description.isEmpty()) {
			Parts.error(message, "Please type a description.");
			return;
		}
		try {
			double amount = Double.parseDouble(amountField.getText());

			// load first, so the new entry gets the next id
			ArrayList<Transaction> history = FileHandler.loadTransactions();
			double balance = PodSystem.balanceOf(history);
			if (!cashIn && amount > balance) {
				Parts.error(message, "Not enough cash. Balance is PHP " + String.format("%.2f", balance) + ".");
				return;
			}

			CashEntry entry = new CashEntry(user.getFullName(), description, amount, cashIn);
			history.add(entry);
			FileHandler.saveTransactions(history);

			refresh();
			Parts.success(message, "Recorded entry #" + entry.getId() + ".");
			descField.clear();
			amountField.clear();
		} catch (NumberFormatException e) {
			Parts.error(message, "Amount must be a number.");
		} catch (IllegalArgumentException e) {
			Parts.error(message, e.getMessage());
		}
	}

	public CashScreen(User user) {
		this.user = user;
		Label title = Parts.title("Cash");
		balanceValue = new Label("");
		message = Parts.message();

		descField = new TextField();
		descField.setPromptText("e.g. Starting cash, Delivery fee");
		amountField = new TextField();
		amountField.setPromptText("0.00");

		Button cashInButton = Parts.primaryButton("Cash In");
		cashInButton.setOnAction(event -> recordEntry(true));
		Button cashOutButton = new Button("Cash Out");
		cashOutButton.setOnAction(event -> recordEntry(false));

		entryList = new ListView<>();
		entryList.setPlaceholder(Parts.placeholder("No cash in or out yet. The balance so far comes from sales."));
		Parts.statusRows(entryList);
		VBox.setVgrow(entryList, Priority.ALWAYS);

		refresh();

		HBox buttonRow = new HBox(10);
		buttonRow.getChildren().add(cashInButton);
		buttonRow.getChildren().add(cashOutButton);

		VBox formCard = Parts.card();
		formCard.getChildren().add(Parts.section("Record cash"));
		formCard.getChildren().add(Parts.fieldWithCaption("Description", descField));
		formCard.getChildren().add(Parts.fieldWithCaption("Amount (PHP)", amountField));
		formCard.getChildren().add(buttonRow);
		formCard.getChildren().add(message);

		// left column: the balance (main tile) and the form
		VBox left = new VBox(12);
		left.setPrefWidth(380);
		left.setMinWidth(380);
		left.getChildren().add(Parts.primaryTile(balanceValue, "Current balance"));
		left.getChildren().add(formCard);

		// right column: the recent cash entries in a card
		VBox right = Parts.card();
		right.getChildren().add(Parts.section("Recent cash entries"));
		right.getChildren().add(entryList);
		HBox.setHgrow(right, Priority.ALWAYS);

		// the two columns sit side by side, under the title
		HBox body = new HBox(16);
		body.getChildren().add(left);
		body.getChildren().add(right);
		VBox.setVgrow(body, Priority.ALWAYS);

		setSpacing(12);
		setPadding(new Insets(16));
		getChildren().add(title);
		getChildren().add(body);
	}
}
