package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import factory.PlanFactory;
import pricing.CostumerPlan;
import pricing.NormalPlan;
import pricing.PrimePlan;
import pricing.PlatinumPlan;

public class PlanFactoryTest {

	@Test
	public void shouldCreateNormalPlan() {
		PlanFactory factory = new PlanFactory();

		CostumerPlan plan = factory.createPlan("normal");

		assertTrue(plan instanceof NormalPlan);
	}

	@Test
	public void shouldCreatePrimePlan() {
		PlanFactory factory = new PlanFactory();

		CostumerPlan plan = factory.createPlan("prime");

		assertTrue(plan instanceof PrimePlan);
	}

	@Test
	public void shouldCreatePlatinumPlan() {
		PlanFactory factory = new PlanFactory();

		CostumerPlan plan = factory.createPlan("platinum");

		assertTrue(plan instanceof PlatinumPlan);
	}

	@Test
	public void shouldAcceptUppercasePlanName() {
		PlanFactory factory = new PlanFactory();

		CostumerPlan plan = factory.createPlan("PRIME");

		assertTrue(plan instanceof PrimePlan);
	}

	@Test
	public void shouldRejectUnknownPlan() {
		PlanFactory factory = new PlanFactory();

		assertThrows(IllegalArgumentException.class, () -> {
			factory.createPlan("gold");
		});
	}

	@Test
	public void shouldRejectNullPlanName() {
		PlanFactory factory = new PlanFactory();

		assertThrows(IllegalArgumentException.class, () -> {
			factory.createPlan(null);
		});
	}
}