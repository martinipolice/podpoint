package model;

import java.io.Serializable;

/***********************************************************
 * Represents one product sold by the shop.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-25
 ***********************************************************/

public class Product implements Serializable {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;


	// private attributes
	private String name;
	private String description;
	private String size;
	private double price;
	private String category;

	// constructor
	public Product(String name, String description, String size, double price, String category) {
		this.name = name;
		this.description = description;
		this.size = size;
		this.price = price;
		this.category = category;
	}

	// getters
	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getSize() { return size; }
	public double getPrice() { return price; }
	public String getCategory() { return category; }

	// setters
	public void setName(String name) { this.name = name; }
	public void setDescription(String description) { this.description = description; }
	public void setSize(String size) { this.size = size; }
	public void setPrice(double price) { this.price = price; }
	public void setCategory(String category) { this.category = category; }
}