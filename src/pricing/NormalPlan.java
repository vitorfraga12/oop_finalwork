package pricing;

public class NormalPlan implements CostumerPlan {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "normal";
	}

	@Override
	public double costumerDiscount(double total) {
		// TODO Auto-generated method stub
		
		return total;
	}

	@Override
	public double deliveryDiscount(double deliveryFee) {
		// TODO Auto-generated method stub
		return deliveryFee;
	}

	@Override
	public double getAnnualFee() {
		// TODO Auto-generated method stub
		return 0.0;
	}

}
