package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import delivery.DeliveryRequest;
import model.Customer;
import pricing.CostumerPlan;
import pricing.PrimePlan;
import pricing.NormalPlan;

public class CustomerTest {

	@Test
	public void newCustomerShouldHaveNormalPlanByDefault() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");

		assertTrue(customer.getPlan() instanceof NormalPlan);
	}

	@Test
	public void customerShouldSubscribeToPlan() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		CostumerPlan prime = new PrimePlan();

		customer.subscribeToPlan(prime);

		assertTrue(customer.getPlan() instanceof PrimePlan);
	}

	@Test
	public void customerShouldStorePendingDelivery() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		DeliveryRequest delivery = new DeliveryRequest("near", 5.0);

		customer.requestDelivery(delivery);

		assertTrue(customer.hasWaitingDelivery());
		assertEquals(delivery, customer.consumeWaitingDelivery());
	}

	@Test
	public void customerShouldConsumePendingDelivery() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		DeliveryRequest delivery = new DeliveryRequest("near", 5.0);

		customer.requestDelivery(delivery);

		DeliveryRequest consumedDelivery = customer.consumeWaitingDelivery();

		assertEquals(delivery, consumedDelivery);
		assertFalse(customer.hasWaitingDelivery());
		assertNull(customer.getWaitingDelivery());
	}

	@Test
	public void customerShouldRejectNullPlan() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");

		assertThrows(IllegalArgumentException.class, () -> {
			customer.subscribeToPlan(null);
		});
	}

	@Test
	public void customerShouldRejectNullDeliveryRequest() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");

		assertThrows(IllegalArgumentException.class, () -> {
			customer.requestDelivery(null);
		});
	}
}