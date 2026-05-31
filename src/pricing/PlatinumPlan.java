package pricing;

public class PlatinumPlan implements CostumerPlan {
	private static double FEE;
	private static double DISCOUNT_RATE;
	
	public PlatinumPlan() {
		FEE = 200.0;
		DISCOUNT_RATE = 0.30;
	}
	
	public PlatinumPlan(double fee, double discount) {
		FEE = fee;
		DISCOUNT_RATE = discount;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "platinum";
	}

	@Override
	public double getAnnualFee() {
		// TODO Auto-generated method stub
		return FEE;
	}

	@Override
	public double costumerDiscount(double total) {
		// TODO Auto-generated method stub
		return total * (1.0 - DISCOUNT_RATE);
	}

	@Override
	public double deliveryDiscount(double deliveryFee) {
		// TODO Auto-generated method stub
		return 0.0;
	}

}
