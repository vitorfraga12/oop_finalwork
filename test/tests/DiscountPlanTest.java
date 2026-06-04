package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import pricing.CostumerPlan;
import pricing.NormalPlan;
import pricing.PrimePlan;
import pricing.PlatinumPlan;

public class DiscountPlanTest {

	@Test
	public void normalPlanShouldNotApplyDiscount() {
		CostumerPlan plan = new NormalPlan();

		assertEquals(100.0, plan.costumerDiscount(100.0));
		assertEquals(0.0, plan.getAnnualFee());
	}

	@Test
	public void primePlanShouldApplyDiscountWhenTotalIsAtLeast50() {
		CostumerPlan plan = new PrimePlan();

		assertEquals(40.0, plan.costumerDiscount(50.0));
		assertEquals(80.0, plan.costumerDiscount(100.0));
	}

	@Test
	public void primePlanShouldNotApplyDiscountWhenTotalIsBelow50() {
		CostumerPlan plan = new PrimePlan();

		assertEquals(40.0, plan.costumerDiscount(40.0));
	}

	@Test
	public void primePlanShouldHaveAnnualFee() {
		CostumerPlan plan = new PrimePlan();

		assertEquals(50.0, plan.getAnnualFee());
	}

	@Test
	public void platinumPlanShouldAlwaysApplyDiscount() {
		CostumerPlan plan = new PlatinumPlan();

		assertEquals(70.0, plan.costumerDiscount(100.0));
		assertEquals(35.0, plan.costumerDiscount(50.0));
	}

	@Test
	public void platinumPlanShouldHaveAnnualFee() {
		CostumerPlan plan = new PlatinumPlan();

		assertEquals(200.0, plan.getAnnualFee());
	}
}