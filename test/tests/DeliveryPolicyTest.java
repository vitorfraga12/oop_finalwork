package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import delivery.DeliveryPolicy;
import delivery.StandardDeliveryPolicy;
import pricing.NormalPlan;
import pricing.PrimePlan;
import pricing.PlatinumPlan;

public class DeliveryPolicyTest {

	@Test
	public void lightDeliveryShouldHaveFixedFee() {
		DeliveryPolicy policy = new StandardDeliveryPolicy();

		double fee = policy.computeFee(5.0, 10.0, new NormalPlan(), 100.0);

		assertEquals(15.0, fee);
	}

	@Test
	public void primePlanShouldPayHalfDeliveryFee() {
		DeliveryPolicy policy = new StandardDeliveryPolicy();

		double fee = policy.computeFee(5.0, 10.0, new PrimePlan(), 100.0);

		assertEquals(7.5, fee);
	}

	@Test
	public void platinumPlanShouldHaveFreeDelivery() {
		DeliveryPolicy policy = new StandardDeliveryPolicy();

		double fee = policy.computeFee(5.0, 10.0, new PlatinumPlan(), 100.0);

		assertEquals(0.0, fee);
	}

	@Test
	public void heavierDeliveryShouldUseFixedFeePlusPercentage() {
		DeliveryPolicy policy = new StandardDeliveryPolicy();

		double fee = policy.computeFee(20.0, 10.0, new NormalPlan(), 100.0);

		assertEquals(20.0, fee);
	}

	@Test
	public void deliveryAbove50KgShouldBeRejected() {
		DeliveryPolicy policy = new StandardDeliveryPolicy();

		assertThrows(IllegalArgumentException.class, () -> {
			policy.computeFee(60.0, 10.0, new NormalPlan(), 100.0);
		});
	}
}