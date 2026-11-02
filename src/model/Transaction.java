package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/***
 * Parent class of every transaction (a sale or a cash entry).
 * It keeps what they share: id, date, clerk and whether it was voided.
 * @author Charles Gian L. Santos
 * @created_date 2026-11-02 08:30
 ***/

public abstract class Transaction implements Serializable {
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

	// marks this transaction as cancelled (it stays in the history)
	public void voidTransaction() {
		voided = true;
	}

	// each kind of transaction works out its own total
	public abstract double getTotal();
}