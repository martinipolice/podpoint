package model;

import java.io.Serializable;

/***********************************************************
 * One line of a sale: a product, how many were bought,
 * and the price at the time of the sale.
 * 
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-02 
 ***********************************************************/

public class SaleLine implements Serializable {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;

	private Product product;
	private int quantity;
	private double unitPrice;

	public SaleLine(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
		// remember the price now, so a later price change won't change old sales
		this.unitPrice = product.getPrice();
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	// price times quantity for this line
	public double getSubtotal() {
		return unitPrice * quantity;
	}
}