package util;

/***********************************************************
 * Thrown when someone tries to sell more than what is in stock.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-28
 ***********************************************************/
public class InsufficientStockException extends Exception {

	// message explains what went wrong; getMessage() returns it later
	public InsufficientStockException(String message) {
		super(message);
	}
}