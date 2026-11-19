package ui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Transaction;
import model.User;
import util.Accounts;
import util.FileHandler;

/***********************************************************
 * The Clerk Sales screen, for the Manager. The tiles show the sales of
 * all clerks. The list on the left shows each clerk with the number and
 * total of their sales; clicking a clerk shows their sale history on the
 * right. Voided sales are marked and not counted.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-12 
 ***********************************************************/
public class ClerkSalesScreen extends VBox {

	private ArrayList<Transaction> history;
	private ArrayList<User> clerks;
	private ListView<String> clerkList;
	private ListView<String> salesList;
	private Label salesHint;

	// one line per clerk: name, number of sales and total (voided sales not counted)
	private void showClerks() {
		clerkList.getItems().clear();
		for (User c : clerks) {
			int count = 0;
			double total = 0;
			for (Transaction t : history) {
				if (t.isSale() && !t.isVoided() && t.getClerkName().equals(c.getFullName())) {
					count++;
					total = total + t.getTotal();
				}
			}
			clerkList.getItems().add(c.getFullName() + " | " + count + " sale(s) | PHP "
					+ String.format("%.2f", total));
		}
	}

	// shows every sale of one clerk, voided ones are marked
	private void showSalesOf(User clerk) {
		salesList.getItems().clear();
		for (Transaction t : history) {
			if (t.isSale() && t.getClerkName().equals(clerk.getFullName())) {
				salesList.getItems().add(Parts.transactionLine(t));
			}
		}
		salesHint.setText(clerk.getFullName() + " has no sales yet.");
	}

	public ClerkSalesScreen() {
		history = FileHandler.loadTransactions();

		// the clerks are the accounts whose role is Clerk
		clerks = new ArrayList<User>();
		for (User u : Accounts.getUsers()) {
			if (u.getRole().equals("Clerk")) {
				clerks.add(u);
			}
		}

		// numbers for the tiles (sales of all clerks)
		int saleCount = 0;
		int voidedCount = 0;
		double saleTotal = 0;
		for (Transaction t : history) {
			if (t.isSale()) {
				if (t.isVoided()) {
					voidedCount++;
				} else {
					saleCount++;
					saleTotal = saleTotal + t.getTotal();
				}
			}
		}

		Label title = Parts.title("Clerk Sales");

		clerkList = new ListView<>();
		VBox.setVgrow(clerkList, Priority.ALWAYS);
		salesList = new ListView<>();
		salesHint = Parts.placeholder("Select a clerk to see their sales.");
		salesList.setPlaceholder(salesHint);
		Parts.statusRows(salesList);
		VBox.setVgrow(salesList, Priority.ALWAYS);

		showClerks();

		// selecting a clerk (with the mouse or the arrow keys) shows the sales of that clerk
		clerkList.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
			int index = clerkList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				return;
			}
			showSalesOf(clerks.get(index));
		});

		// the first clerk is selected when the screen opens, so the right card is never empty
		if (clerks.size() > 0) {
			clerkList.getSelectionModel().select(0);
		}

		// two cards with the same layout: a heading and a list
		VBox left = Parts.card();
		left.setPrefWidth(340);
		left.setMinWidth(340);
		left.getChildren().add(Parts.section("Clerks"));
		left.getChildren().add(clerkList);

		VBox right = Parts.card();
		right.getChildren().add(Parts.section("Sales of the selected clerk"));
		right.getChildren().add(salesList);
		HBox.setHgrow(right, Priority.ALWAYS);

		HBox body = new HBox(16);
		body.getChildren().add(left);
		body.getChildren().add(right);
		VBox.setVgrow(body, Priority.ALWAYS);

		setSpacing(12);
		setPadding(new Insets(16));
		getChildren().add(title);
		getChildren().add(Parts.tileRow(
				Parts.primaryTile(new Label("PHP " + String.format("%.2f", saleTotal)), "Total sales"),
				Parts.tile(new Label(String.valueOf(saleCount)), "Sales made"),
				Parts.tile(new Label(String.valueOf(voidedCount)), "Voided sales")));
		getChildren().add(body);
	}
}
