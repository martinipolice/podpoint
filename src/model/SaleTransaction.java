package model;

import java.util.ArrayList;

/***
 * One customer purchase. It is a Transaction that holds a list of SaleLines.
 * @author Charles Gian L. Santos
 * @created_date 2026-11-02 08:35
 ***/

public class SaleTransaction extends Transaction {
	private ArrayList<SaleLine> lines;

	public SaleTransaction(String clerkName) {
		super(clerkName);
		lines = new ArrayList<>();
	}

	public ArrayList<SaleLine> getLines() {
		return lines;
	}

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
}