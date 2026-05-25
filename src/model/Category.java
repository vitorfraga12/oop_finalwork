package model;

public class Category {
	
	private String name;
	private double discountPercent;
	
	public Category(String name) {
		this.name = name;
		this.discountPercent = 0.0;
	}
	
	public double applyDiscount(double price) {
		return price*(1-discountPercent/100);
	}
	
	// to confirme that the percent its correct
	public void setDiscountPercent(double discountPercent) {
		if (discountPercent == 0 || discountPercent >100) {
			throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
		}
		
		this.discountPercent = discountPercent;
	}
	
	public boolean isPerishable() {
		return name.equalsIgnoreCase("meat") || name.equalsIgnoreCase("dairy");
	}
	
	public String getName() {
		return name;
	}
	
	public double getDiscountPercent() {
		return discountPercent;
	}

}
