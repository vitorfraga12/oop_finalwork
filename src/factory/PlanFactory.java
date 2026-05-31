package factory;

import pricing.CostumerPlan;
import pricing.NormalPlan;
import pricing.PlatinumPlan;
import pricing.PrimePlan;

public class PlanFactory {
	public CostumerPlan createPlan(String plan) {
		if (plan == null) {
			throw new IllegalArgumentException("Plan null");
		}

		String planName = plan.toLowerCase();

		switch (planName) {
			case "normal":
				return new NormalPlan();

			case "prime":
				return new PrimePlan();

			case "platinum":
				return new PlatinumPlan();

			default:
				throw new IllegalArgumentException("Plan: " + planName + " does not exist");
		}
	}

}
