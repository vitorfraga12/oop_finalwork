package delivery;

import pricing.CostumerPlan;

public class StandardDeliveryPolicy implements DeliveryPolicy{
	private double lightDeliveryMaxWeight;
	private double lightDeliveryMaxDistance;
	private double fixedFee;
	private double mediumDeliveryPercentage;
	private double maxSupportedWeight;

	public StandardDeliveryPolicy() {
		this(10.0, 30.0, 15.0, 0.05, 50.0);
	}

	public StandardDeliveryPolicy(double lightDeliveryMaxWeight, double lightDeliveryMaxDistance,
			double fixedFee, double mediumDeliveryPercentage, double maxSupportedWeight) {

		if (lightDeliveryMaxWeight < 0) {
			throw new IllegalArgumentException("Light delivery max weight cannot be negative.");
		}

		if (lightDeliveryMaxDistance < 0) {
			throw new IllegalArgumentException("Light delivery max distance cannot be negative.");
		}

		if (fixedFee < 0) {
			throw new IllegalArgumentException("Fixed fee cannot be negative.");
		}

		if (mediumDeliveryPercentage < 0) {
			throw new IllegalArgumentException("Medium delivery percentage cannot be negative.");
		}

		if (maxSupportedWeight < 0) {
			throw new IllegalArgumentException("Max supported weight cannot be negative.");
		}

		this.lightDeliveryMaxWeight = lightDeliveryMaxWeight;
		this.lightDeliveryMaxDistance = lightDeliveryMaxDistance;
		this.fixedFee = fixedFee;
		this.mediumDeliveryPercentage = mediumDeliveryPercentage;
		this.maxSupportedWeight = maxSupportedWeight;
	}

	@Override
	public double computeFee(double weight, double distanceKm, CostumerPlan plan, double itemsTotal) {
		if (plan == null) {
			throw new IllegalArgumentException("Plan cannot be null.");
		}

		if (weight < 0) {
			throw new IllegalArgumentException("Weight cannot be negative.");
		}

		if (distanceKm < 0) {
			throw new IllegalArgumentException("Distance cannot be negative.");
		}

		if (itemsTotal < 0) {
			throw new IllegalArgumentException("Items total cannot be negative.");
		}

		if (weight > maxSupportedWeight) {
			throw new IllegalArgumentException("Delivery is not supported for more than "
					+ maxSupportedWeight + " kg.");
		}

		double fee;

		if (weight < lightDeliveryMaxWeight && distanceKm <= lightDeliveryMaxDistance) {
			fee = fixedFee;
		} else {
			fee = fixedFee + itemsTotal * mediumDeliveryPercentage;
		}

		return plan.deliveryDiscount(fee);
	}

}
