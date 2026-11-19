package app;

import model.Inventory;
import model.InventoryItem;
import model.SaleLine;
import model.SaleTransaction;
import model.Transaction;
import util.FileHandler;

/***********************************************************
 * Console test for SaleLine, Transaction and SaleTransaction.
 * @author Charles Gian L. Santos
 * @created_date 2026-11-02 
 ***********************************************************/

public class SaleTest {
	public static void main(String[] args) {
		Inventory inventory = FileHandler.loadInventory();
		InventoryItem tablea = inventory.findItem("Premium Chocolate Tablea (100% Pure)", "72g");
		InventoryItem cocoa = inventory.findItem("Premium Cocoa Powder (Pure, Unsweetened)", "100g");

		// a sale with two lines
		SaleTransaction sale = new SaleTransaction("Clerk1");
		sale.addLine(new SaleLine(tablea.getProduct(), 2));
		sale.addLine(new SaleLine(cocoa.getProduct(), 1));

		for (SaleLine line : sale.getLines()) {
			System.out.println(line.getQuantity() + " x " + line.getProduct().getName()
					+ " (" + line.getProduct().getSize() + ") = " + line.getSubtotal());
		}
		System.out.println("Sale #" + sale.getId() + " total: " + sale.getTotal());
		System.out.println("Voided? " + sale.isVoided());

		// a Transaction variable can hold a SaleTransaction (polymorphism)
		Transaction t = sale;
		System.out.println("Total through Transaction: " + t.getTotal());
		t.voidTransaction();
		System.out.println("Voided? " + sale.isVoided());
	}
}