package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/***********************************************************
 * Parent class of every transaction (a sale or a cash entry).
 * It keeps what they share: id, date, clerk and whether it was voided.
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-02 
 ***********************************************************/

public abstract class Transaction implements Serializable {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;

	// shared by all transactions, gives each one a new id (like the counter in your handout)
	private static int nextId = 1;

	private int id;
	private LocalDateTime date;
	private String clerkName;
	private boolean voided;

	public Transaction(String clerkName) {
		this.id = nextId;
		nextId = nextId + 1;
		this.date = LocalDateTime.now();
		this.clerkName = clerkName;
		this.voided = false;
	}

	// used after loading saved data, so new ids continue from the last one
	public static void setNextId(int value) {
		nextId = value;
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getClerkName() {
		return clerkName;
	}

	public boolean isVoided() {
		return voided;
	}

	// marks this transaction as cancelled (it stays in the history).
	// Returns false if it was already voided, so the caller does not undo it twice.
	public boolean voidTransaction() {
		if (voided) {
			return false;
		}
		voided = true;
		return true;
	}

	// each kind of transaction works out its own total
	public abstract double getTotal();
	
	// each kind of transaction describes itself in one short text
	public abstract String getDescription();
	
	// what happens to the stock when this is voided (nothing by default)
	public void undoEffect(Inventory inventory) {
	}
	
	// a plain Transaction is not a sale; SaleTransaction says yes
	public boolean isSale() {
		return false;
	}
}