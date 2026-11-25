package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import model.Product;
import model.Inventory;
import model.Transaction;

/***********************************************************
 * Saves and loads the products, the inventory and the transaction history
 * using serialization. Two small helpers, write() and read(), do the file
 * work, so each save and load method below stays short.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-25
 ***********************************************************/

public class FileHandler {

	// the files are saved inside the project folder
	private static final Path PRODUCTS_PATH = Paths.get("data/products.dat");
	private static final Path INVENTORY_PATH = Paths.get("data/inventory.dat");
	private static final Path TRANSACTIONS_PATH = Paths.get("data/transactions.dat");


	// writes any serializable object into a file.
	// try ( ... ) closes the file by itself, even when an error happens.
	private static void write(Path path, Object data) {
		try {
			// makes the data folder if it does not exist yet
			Files.createDirectories(path.getParent());
			try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
				out.writeObject(data);
			}
		} catch (IOException e) {
			System.out.println("Could not save " + path + ": " + e);
		}
	}

	// reads an object back from a file.
	// Returns null if there is no file yet, or if the file cannot be read.
	private static Object read(Path path) {
		if (!Files.exists(path)) {
			return null;
		}
		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
			return in.readObject();
		} catch (IOException e) {
			System.out.println("Could not read " + path + ": " + e);
			setAside(path);
		} catch (ClassNotFoundException e) {
			System.out.println("Unknown class in " + path + ": " + e);
			setAside(path);
		}
		return null;
	}

	// renames a file that cannot be read to "<name>.bad", so the next save
	// does not overwrite it and nothing is lost
	private static void setAside(Path path) {
		try {
			Files.move(path, Paths.get(path.toString() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Could not set aside " + path + ": " + e);
		}
	}

	// ---------- products (used by the console tests) ----------

	// writes the whole list into the file
	public static void saveProducts(ArrayList<Product> products) {
		write(PRODUCTS_PATH, products);
	}

	// reads the list back, returns an empty list if there is no file yet
	@SuppressWarnings("unchecked")
	public static ArrayList<Product> loadProducts() {
		Object data = read(PRODUCTS_PATH);
		if (data == null) {
			return new ArrayList<Product>();
		}
		return (ArrayList<Product>) data;
	}

	// ---------- inventory ----------

	// writes the whole inventory into the file
	public static void saveInventory(Inventory inventory) {
		write(INVENTORY_PATH, inventory);
	}

	// reads the inventory back. On the first run (or when the file could not
	// be read) it starts with the Pod Point catalog and saves it.
	public static Inventory loadInventory() {
		Object data = read(INVENTORY_PATH);
		if (data == null) {
			Inventory inventory = PodSystem.defaultInventory();
			saveInventory(inventory);
			return inventory;
		}
		return (Inventory) data;
	}

	// ---------- transactions ----------

	// writes the whole transaction history into the file
	public static void saveTransactions(ArrayList<Transaction> transactions) {
		write(TRANSACTIONS_PATH, transactions);
	}

	// reads the history back; returns an empty list if there is no file yet
	@SuppressWarnings("unchecked")
	public static ArrayList<Transaction> loadTransactions() {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		Object data = read(TRANSACTIONS_PATH);
		if (data != null) {
			transactions = (ArrayList<Transaction>) data;
		}

		// new transactions continue numbering after the highest saved id
		int highest = 0;
		for (Transaction t : transactions) {
			if (t.getId() > highest) {
				highest = t.getId();
			}
		}
		Transaction.setNextId(highest + 1);
		return transactions;
	}
}
