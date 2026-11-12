package ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import model.Transaction;
import model.User;
import util.Accounts;
import util.FileHandler;

/***********************************************************
 * The Clerk Sales screen, for the Manager. Shows each clerk with
 * the number and total of their sales, and the sale history of the
 * selected clerk. Voided sales are marked and not counted.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-12
 ***********************************************************/

public class ClerkSalesScreen extends VBox {

	private ArrayList<Transaction> history;
	private ArrayList<User> clerks;
	private ListView<String> clerkList;
	private ListView<String> salesList;

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
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
		for (Transaction t : history) {
			if (t.isSale() && t.getClerkName().equals(clerk.getFullName())) {
				String line = "#" + t.getId() + " | " + t.getDescription() + " | "
						+ t.getDate().format(format) + " | PHP " + String.format("%.2f", t.getTotal());
				if (t.isVoided()) {
					line = line + "  [VOIDED]";
				}
				salesList.getItems().add(line);
			}
		}
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

		Label title = Parts.title("Clerk Sales");
		clerkList = new ListView<>();
		clerkList.setPrefHeight(120);
		salesList = new ListView<>();
		salesList.setPlaceholder(Parts.emptyHint("Select a clerk and click Show Sales."));
		salesList.setPrefHeight(250);
		Label message = new Label("");

		showClerks();

		Button showButton = Parts.primaryButton("Show Sales");
		showButton.setOnAction(event -> {
			int index = clerkList.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				message.setText("Select a clerk first.");
				return;
			}
			User clerk = clerks.get(index);
			showSalesOf(clerk);
			message.setText("Showing the sales of " + clerk.getFullName() + ".");
		});

		setSpacing(10);
		setPadding(new Insets(10));
		getChildren().add(title);
		getChildren().add(Parts.section("Clerks"));
		getChildren().add(clerkList);
		getChildren().add(showButton);
		getChildren().add(Parts.section("Sales of the selected clerk"));
		getChildren().add(salesList);
		getChildren().add(message);
	}
}