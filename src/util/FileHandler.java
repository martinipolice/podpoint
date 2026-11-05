package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import model.Product;
import model.Inventory;
import model.Transaction;

/***********************************************************
 * Saves and loads the product list using serialization.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-25 
 ***********************************************************/

public class FileHandler {

	// the file is saved inside the project folder
	private static final Path PRODUCTS_PATH = Paths.get("data/products.dat");
	private static final Path INVENTORY_PATH = Paths.get("data/inventory.dat");
	private static final Path TRANSACTIONS_PATH = Paths.get("data/transactions.dat");

	// writes the whole list into the file
	public static void saveProducts(ArrayList<Product> products) {
		try {
			// makes the data folder if it does not exist yet
			Files.createDirectories(PRODUCTS_PATH.getParent());
			ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(PRODUCTS_PATH));
			out.writeObject(products);
			out.close();
		} catch (IOException e) {
			System.out.println("Could not save products: " + e);
		}
	}

	// reads the list back, returns an empty list if there is no file yet
	@SuppressWarnings("unchecked")
	public static ArrayList<Product> loadProducts() {
		ArrayList<Product> products = new ArrayList<Product>();
		if (!Files.exists(PRODUCTS_PATH)) {
			return products;
		}
		try {
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(PRODUCTS_PATH));
			products = (ArrayList<Product>) in.readObject();
			in.close();
		} catch (IOException e) {
			System.out.println("Could not read products: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Product class not found: " + e);
		}
		return products;
	}
	
	// writes the whole inventory into the file
	public static void saveInventory(Inventory inventory) {
		try {
			Files.createDirectories(INVENTORY_PATH.getParent());
			ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(INVENTORY_PATH));
			out.writeObject(inventory);
			out.close();
		} catch (IOException e) {
			System.out.println("Could not save inventory: " + e);
		}
	}

	// reads the inventory back; returns an empty inventory if there is no file yet
	public static Inventory loadInventory() {
		Inventory inventory = new Inventory();
		if (!Files.exists(INVENTORY_PATH)) {
			return inventory;
		}
		try {
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(INVENTORY_PATH));
			inventory = (Inventory) in.readObject();
			in.close();
		} catch (IOException e) {
			System.out.println("Could not read inventory: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Inventory class not found: " + e);
		}
		return inventory;
	}
	
	// writes the whole transaction history into the file
	public static void saveTransactions(ArrayList<Transaction> transactions) {
		try {
			Files.createDirectories(TRANSACTIONS_PATH.getParent());
			ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(TRANSACTIONS_PATH));
			out.writeObject(transactions);
			out.close();
		} catch (IOException e) {
			System.out.println("Could not save transactions: " + e);
		}
	}

	// reads the history back; returns an empty list if there is no file yet
	@SuppressWarnings("unchecked")
	public static ArrayList<Transaction> loadTransactions() {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		if (!Files.exists(TRANSACTIONS_PATH)) {
			return transactions;
		}
		try {
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(TRANSACTIONS_PATH));
			transactions = (ArrayList<Transaction>) in.readObject();
			in.close();
		} catch (IOException e) {
			System.out.println("Could not read transactions: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Transaction class not found: " + e);
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