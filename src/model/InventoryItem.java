package model;

import java.io.Serializable;

/***********************************************************
 * One product in the inventory, with how many are in stock.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-26
 ***********************************************************/

public class InventoryItem implements Serializable {
	// this item HAS a product (composition)
	private Product product;
	private int quantity;
	// when quantity falls to this number or lower, the item is "low stock"
	private int reorderLevel;

	public InventoryItem(Product product, int quantity, int reorderLevel) {
		this.product = product;
		this.quantity = quantity;
		this.reorderLevel = reorderLevel;
	}

	public Product getProduct() { return product; }
	public int getQuantity() { return quantity; }
	public int getReorderLevel() { return reorderLevel; }

	public void setQuantity(int quantity) { this.quantity = quantity; }
	public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

	// true when the stock is at or below the reorder level
	public boolean isLow() {
		return quantity <= reorderLevel;
	}
}