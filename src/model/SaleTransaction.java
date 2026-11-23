package model;

import java.util.ArrayList;

/***********************************************************
 * One customer purchase. It is a Transaction that holds a list of SaleLines.
 * 
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-02
  ***********************************************************/

public class SaleTransaction extends Transaction {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;

	private ArrayList<SaleLine> lines;

	public SaleTransaction(String clerkName) {
		super(clerkName);
		lines = new ArrayList<>();
	}

	public ArrayList<SaleLine> getLines() {
		return lines;
	}

	// adds one product line to this sale
	public void addLine(SaleLine line) {
		lines.add(line);
	}

	// the total is the sum of every line's subtotal
	@Override
	public double getTotal() {
		double total = 0;
		for (SaleLine line : lines) {
			total = total + line.getSubtotal();
		}
		return total;
	}
	
	@Override
	public String getDescription() {
		return "Sale (" + lines.size() + " product(s))";
	}
	
	// voiding a sale puts the sold items back into stock.
	// The stock is added directly (not through restock), so it is always put back.
	@Override
	public void undoEffect(Inventory inventory) {
		for (SaleLine line : lines) {
			Product p = line.getProduct();
			InventoryItem item = inventory.findItem(p.getName(), p.getSize());
			if (item != null) {
				item.setQuantity(item.getQuantity() + line.getQuantity());
			}
		}
	}

	// a SaleTransaction is a sale
	@Override
	public boolean isSale() {
		return true;
	}
}