package pricing;

public interface CostumerPlan {
	String getName();
	
	double getAnnualFee();

	double costumerDiscount(double total);

	double deliveryDiscount(double deliveryFee);
}
