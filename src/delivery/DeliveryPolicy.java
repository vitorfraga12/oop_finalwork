package delivery;

import pricing.CostumerPlan;

public interface DeliveryPolicy {
		double computeFee(double weight, double distanceKm, CostumerPlan plan, double itemsTotal);
	
}
