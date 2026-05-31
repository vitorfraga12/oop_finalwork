package pricing;

public class PrimePlan implements CostumerPlan{
	private static double FEE;
	private static double MINIMUM_TOTAL;
	private static double DISCOUNT_RATE;
	private static double DELIVERY_RATE;
	
	public PrimePlan() {
		FEE = 50.0;
		MINIMUM_TOTAL = 50.0;
		DISCOUNT_RATE = 0.20;
		DELIVERY_RATE = 0.50;
	}
	
	public PrimePlan(double fee, double minimum, double discount, double delivery) {
		FEE = fee;
		MINIMUM_TOTAL = minimum;
		DISCOUNT_RATE = discount;
		DELIVERY_RATE = delivery;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prime";
	}

	@Override
	public double getAnnualFee() {
		// TODO Auto-generated method stub
		return FEE;
	}

	@Override
	public double costumerDiscount(double total) {
		// TODO Auto-generated method stub
		if (total >= MINIMUM_TOTAL) {
			return total * (1.0 - DISCOUNT_RATE);
		}

		return total;
	}

	@Override
	public double deliveryDiscount(double deliveryFee) {
		// TODO Auto-generated method stub
		return deliveryFee * (1.0 - DELIVERY_RATE);
	}

}
