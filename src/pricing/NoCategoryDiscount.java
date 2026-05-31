package pricing;

public class NoCategoryDiscount implements CategoryDiscountPolicy{

	@Override
	public double categoryDiscount(double total) {
		// TODO Auto-generated method stub
		return total;
	}

}
