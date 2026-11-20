package util;

/***********************************************************
 * Thrown when someone tries to sell more than what is in stock.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-28
 ***********************************************************/
public class InsufficientStockException extends Exception {

	// fixed version number of this class (exceptions are serializable too)
	private static final long serialVersionUID = 1L;

	// message explains what went wrong; getMessage() returns it later
	public InsufficientStockException(String message) {
		super(message);
	}
}