package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import checkout.Bill;
import payment.PaymentResult;
import system.SupermarketSystem;

public class SupermarketSystemTest {

	@Test
	public void systemShouldRunBasicCheckoutSuccessfully() {
		SupermarketSystem system = new SupermarketSystem();

		system.login("ceo", "123456789");
		system.setup();

		system.login("cashier", "1234");
		system.startCheckout("customer");
		system.scanItem("milk", 2);

		Bill bill = system.computeBill();

		assertEquals(5.0, bill.getFinalTotal(), 0.0001);

		PaymentResult result = system.pay("1111", "1234");

		assertEquals(PaymentResult.SUCCESS, result);

		system.login("ceo", "123456789");

		assertTrue(system.showInventory().contains("milk"));
		assertTrue(system.showInventory().contains("18"));
		assertEquals(5.0, system.showRevenue(), 0.0001);
	}

	@Test
	public void systemShouldApplyDeliveryToNextCheckout() {
		SupermarketSystem system = new SupermarketSystem();

		system.login("ceo", "123456789");
		system.setup();

		system.login("customer", "1234");
		system.requestDelivery("near");
		system.logout();

		system.login("cashier", "1234");
		system.startCheckout("customer");
		system.scanItem("milk", 2);

		Bill bill = system.computeBill();

		assertEquals(20.0, bill.getFinalTotal(), 0.0001);
	}

	@Test
	public void systemShouldApplyCategoryDiscount() {
		SupermarketSystem system = new SupermarketSystem();

		system.login("ceo", "123456789");
		system.setup();

		system.login("ceo", "123456789");
		system.setCategoryDiscount("dairy", 10.0);

		system.login("cashier", "1234");
		system.startCheckout("customer");
		system.scanItem("milk", 2);

		Bill bill = system.computeBill();

		assertEquals(4.5, bill.getFinalTotal(), 0.0001);
	}

	@Test
	public void systemShouldRejectScanItemWithoutCheckout() {
		SupermarketSystem system = new SupermarketSystem();

		system.login("ceo", "123456789");
		system.setup();

		system.login("cashier", "1234");

		assertThrows(IllegalStateException.class, () -> {
			system.scanItem("milk", 2);
		});
	}

	@Test
	public void systemShouldRejectManagerCommandFromCustomer() {
		SupermarketSystem system = new SupermarketSystem();

		system.login("ceo", "123456789");
		system.setup();

		system.login("customer", "1234");

		assertThrows(IllegalStateException.class, () -> {
			system.addItem("bread", "dairy", 1.0, 0.2, 10);
		});
	}

	@Test
	public void systemShouldRejectWrongPinPayment() {
		SupermarketSystem system = new SupermarketSystem();

		system.login("ceo", "123456789");
		system.setup();

		system.login("cashier", "1234");
		system.startCheckout("customer");
		system.scanItem("milk", 2);
		system.computeBill();

		PaymentResult result = system.pay("1111", "9999");

		assertEquals(PaymentResult.PIN_WRONG, result);
	}
}