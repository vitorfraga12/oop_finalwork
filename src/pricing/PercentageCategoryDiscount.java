package pricing;

public class PercentageCategoryDiscount implements CategoryDiscountPolicy {
	private double discountPercent;

	public PercentageCategoryDiscount(double discountPercent) {
		if (discountPercent < 0 || discountPercent > 100) {
			throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
		}

		this.discountPercent = discountPercent;
	}

	@Override
	public double categoryDiscount(double price) {
		return price * (1.0 - discountPercent / 100.0);
	}

	public double getDiscountPercent() {
		return discountPercent;
	}
}