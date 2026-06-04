package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import checkout.Bill;
import checkout.CheckoutSession;
import delivery.DeliveryRequest;
import delivery.StandardDeliveryPolicy;
import model.Category;
import model.Customer;
import model.Item;
import pricing.PrimePlan;

public class CheckoutSessionTest {

	@Test
	public void checkoutSessionShouldScanItem() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		CheckoutSession session = new CheckoutSession(customer, null);

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		session.scanItem(milk, 2);

		assertEquals(1, session.getCart().getLines().size());
	}

	@Test
	public void checkoutSessionShouldComputeBillWithoutDelivery() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		CheckoutSession session = new CheckoutSession(customer, null);

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		session.scanItem(milk, 2);

		Bill bill = session.computeBill(new StandardDeliveryPolicy());

		assertEquals(5.0, bill.getFinalTotal(), 0.0001);
	}

	@Test
	public void checkoutSessionShouldComputeBillWithDelivery() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		DeliveryRequest delivery = new DeliveryRequest("near", 5.0);

		CheckoutSession session = new CheckoutSession(customer, delivery);

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		session.scanItem(milk, 2);

		Bill bill = session.computeBill(new StandardDeliveryPolicy());

		assertEquals(20.0, bill.getFinalTotal(), 0.0001);
	}

	@Test
	public void checkoutSessionShouldApplyCustomerPlan() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		customer.subscribeToPlan(new PrimePlan());

		CheckoutSession session = new CheckoutSession(customer, null);

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 30);

		session.scanItem(milk, 20);

		Bill bill = session.computeBill(new StandardDeliveryPolicy());

		assertEquals(40.0, bill.getFinalTotal(), 0.0001);
	}

	@Test
	public void checkoutSessionShouldRejectEmptyCartBillComputation() {
		Customer customer = new Customer("John", "Doe", "john", "1234", "near");
		CheckoutSession session = new CheckoutSession(customer, null);

		assertThrows(IllegalStateException.class, () -> {
			session.computeBill(new StandardDeliveryPolicy());
		});
	}
}