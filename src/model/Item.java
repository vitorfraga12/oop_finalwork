package model;

public class Item {
	private String name;
	private Category category;
	private double unitPrice;
	private double weight;
	private int stock;
	private int threshold;
	
	public Item(String name, Category category, double unitPrice, double weight, int stock) {
		if (unitPrice < 0) {
			throw new IllegalArgumentException("unit price cannot be negative.");
		}
		
		if (weight < 0) {
			throw new IllegalArgumentException("weight cannot be negative.");
		}
		
		if (stock < 0) {
			throw new IllegalArgumentException("stock cannot be negative.");
		}
		
		this.name = name;
		this.category = category;
		this.unitPrice = unitPrice;
		this.weight = weight;
		this.stock = stock;
		this.threshold = 2;
	}
	
	public double priceForQuantity(int quantity){
		validateQuantity(quantity);
		return unitPrice * quantity;
	}
	
	public double discountedPriceForQuantity(int quantity) {
		double total = priceForQuantity(quantity);
		return category.applyDiscount(total);
	}
	
	public double weightForQuantity(int quantity) {
		validateQuantity(quantity);
		return weight*quantity;
	}
	
	public void decreaseStock(int quantity) {
		validateQuantity(quantity);
		if (quantity > stock) {
			throw new IllegalArgumentException("not enough stock for item: " + name);
		}
		
		stock -= quantity;
	}
	
	public void restock(int quantity) {
		validateQuantity(quantity);
		stock += quantity;
	}
	
	public boolean isBelowThreshold() {
		return category.isPerishable() && stock <= threshold;
	}
	
	private void validateQuantity(int quantity) {
		if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public int getStock() {
		return stock;
	}
	
	public int getThreshold() {
		return threshold;
	}
	
	public void setThreshold(int threshold) {
		if (threshold < 0) {
			throw new IllegalArgumentException("low stock threshold cannot negative");
		}
		
		this.threshold = threshold;
	}
	

}
