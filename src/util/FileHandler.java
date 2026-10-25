package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import model.Product;

/***********************************************************
 * Saves and loads the product list using serialization.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-25 
 ***********************************************************/

public class FileHandler {

	// the file is saved inside the project folder
	private static final Path PRODUCTS_PATH = Paths.get("data/products.dat");

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
}