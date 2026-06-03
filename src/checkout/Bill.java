package checkout;

public class Bill {
	private double rawItemsTotal;
	private double categoryDiscountedItemsTotal;
	private double planDiscountedItemsTotal;
	private double deliveryFee;
	private double finalTotal;
	
	public Bill(double rawItemsTotal, double categoryDiscountedItemsTotal, double planDiscountedItemsTotal, double deliveryFee) {

		if (rawItemsTotal < 0 || categoryDiscountedItemsTotal < 0 || planDiscountedItemsTotal < 0 || deliveryFee < 0) {
			throw new IllegalArgumentException("Bill values cant be negative");
		}
		this.rawItemsTotal = rawItemsTotal;
		this.categoryDiscountedItemsTotal = categoryDiscountedItemsTotal;
		this.planDiscountedItemsTotal = planDiscountedItemsTotal;
		this.deliveryFee = deliveryFee;
		this.finalTotal = planDiscountedItemsTotal + deliveryFee;
	}
	
	public double getRawItemsTotal() {
		return this.rawItemsTotal;
	}
	
	public double getCategoryDiscountedItemsTotal() {
		return this.categoryDiscountedItemsTotal;
	}
	
	public double getPlanDiscountedItemsTotal() {
		return this.planDiscountedItemsTotal;
	}
	
	public double getDeliveryFee() {
		return this.deliveryFee;
	}
	
	public double getFinalTotal() {
		return this.finalTotal;
	}
	
	
	public String summary() {
		return "Raw items total: " + rawItemsTotal + "\nAfter category discounts: " + categoryDiscountedItemsTotal
	+ "\nAfter customer plan: " + planDiscountedItemsTotal + "\nDelivery fee: " + deliveryFee + "\nFinal total: " + finalTotal;
	}
	
	

}
