package app;

import java.util.ArrayList;
import model.Product;
import util.FileHandler;

/***********************************************************
 * Quick test of the Product class. Not part of the final app.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-25
 ***********************************************************/

public class ProductTest {

	public static void main(String[] args) {
		// an ArrayList that can only hold Product objects
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

		// save the list to data/products.dat
		FileHandler.saveProducts(products);

		// load it into a brand new list, to prove the file works
		ArrayList<Product> loaded = FileHandler.loadProducts();

		for (Product p : loaded) {
			System.out.println(p.getName() + " (" + p.getSize() + ") - PHP " + p.getPrice());
		}
		System.out.println("Loaded products: " + loaded.size());
	}
}