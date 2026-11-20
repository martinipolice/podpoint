package util;

import java.util.ArrayList;
import model.Clerk;
import model.Inventory;
import model.InventoryItem;
import model.Manager;
import model.Product;
import model.Transaction;
import model.User;

/***********************************************************
 * The System class of Pod Point. It holds the rules of the whole
 * system in one place, so the screens do not repeat them:
 * the fixed accounts and the login check, the starting catalog,
 * the cash balance, and the rule for voiding a transaction.
 * (It is not named "System" because Java already has java.lang.System.)
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-20
 ***********************************************************/

public class PodSystem {

	// ---------- accounts ----------

	// builds the list of accounts (one Manager and two Clerks)
	public static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<>();
		users.add(new Manager("manager", "1234", "Maria Manager"));
		users.add(new Clerk("clerk1", "1111", "Carlo Clerk"));
		users.add(new Clerk("clerk2", "2222", "Dana Clerk"));
		return users;
	}

	// only the accounts whose role is Clerk
	public static ArrayList<User> getClerks() {
		ArrayList<User> clerks = new ArrayList<>();
		for (User u : getUsers()) {
			if (u.getRole().equals("Clerk")) {
				clerks.add(u);
			}
		}
		return clerks;
	}

	// returns the matching User, or null if the username or password is wrong
	public static User login(String username, String password) {
		for (User u : getUsers()) {
			if (u.getUsername().equals(username) && u.checkPassword(password)) {
				return u;
			}
		}
		return null;
	}

	// ---------- starting catalog ----------

	// the 10 products of Cacao Culture, each with 20 in stock ("low" is 5 or fewer).
	// Used when the app starts and there is no saved inventory yet.
	public static Inventory defaultInventory() {
		ArrayList<Product> products = new ArrayList<Product>();
		products.add(new Product("Premium Chocolate Tablea (100% Pure)", "Unsweetened pure chocolate tablets, 6 pieces", "72g", 155.00, "Tablea"));
		products.add(new Product("Premium Chocolate Tablea (100% Pure)", "The same tablea in the larger pack", "144g", 290.00, "Tablea"));
		products.add(new Product("Premium Cocoa Powder (Pure, Unsweetened)", "Pure unsweetened cocoa powder", "100g", 155.00, "Baking and drinks"));
		products.add(new Product("Premium Cocoa Powder (Pure, Unsweetened)", "The same powder in the bulk pack", "1kg", 1395.00, "Baking and drinks"));
		products.add(new Product("Cocoa Butter Chunks (Pure and Unrefined)", "Pure, unrefined cocoa butter", "100g", 305.00, "Baking and drinks"));
		products.add(new Product("Almond Dark Chocolate", "Dark chocolate with almonds", "100g", 290.00, "Chocolate bar"));
		products.add(new Product("Chocolate Coated Peanut", "Peanuts coated in chocolate", "75g", 165.00, "Snacks"));
		products.add(new Product("Chocolate Coated Raisins", "Raisins coated in chocolate", "75g", 165.00, "Snacks"));
		products.add(new Product("Chocolate Crinkles", "Chocolate crinkle cookies", "140g", 160.00, "Snacks"));
		products.add(new Product("Dark Chocolate Covered Mango Strips", "Mango strips covered in dark chocolate", "100g", 240.00, "Snacks"));

		Inventory inventory = new Inventory();
		for (Product p : products) {
			inventory.addItem(new InventoryItem(p, 20, 5));
		}
		return inventory;
	}

	// ---------- cash ----------

	// adds up the totals of the transactions that are not voided
	public static double balanceOf(ArrayList<Transaction> history) {
		double balance = 0;
		for (Transaction t : history) {
			if (!t.isVoided()) {
				balance = balance + t.getTotal();
			}
		}
		return balance;
	}

	// ---------- voiding ----------

	// returns the reason why a transaction cannot be voided, or null if it can.
	// Voiding takes the transaction's total out of the balance, so it is refused
	// when that would leave the cash box with less than PHP 0.00.
	public static String voidProblem(ArrayList<Transaction> history, Transaction t) {
		if (t.isVoided()) {
			return "Transaction #" + t.getId() + " is already voided.";
		}
		double balanceAfter = balanceOf(history) - t.getTotal();
		if (balanceAfter < -0.005) {
			return "Cannot void #" + t.getId() + ": the balance would go below PHP 0.00.";
		}
		return null;
	}
}
