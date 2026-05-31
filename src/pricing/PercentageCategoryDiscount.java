package pricing;

public class PercentageCategoryDiscount implements CategoryDiscountPolicy {
	private double discountPercent;
	
	public PercentageCategoryDiscount(double percent) {
		if (discountPercent < 0 || discountPercent > 100) {
			throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
		}
		this.discountPercent= percent;
	}

	@Override
	public double categoryDiscount(double total) {
		// TODO Auto-generated method stub
		return total * (1.0 - discountPercent / 100.0);
	}
	
}
