package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import checkout.Bill;

public class BillTest {

	@Test
	public void billShouldComputeFinalTotal() {
		Bill bill = new Bill(100.0, 90.0, 80.0, 15.0);

		assertEquals(95.0, bill.getFinalTotal(), 0.0001);
	}

	@Test
	public void billShouldStoreIntermediateValues() {
		Bill bill = new Bill(100.0, 90.0, 80.0, 15.0);

		assertEquals(100.0, bill.getRawItemsTotal(), 0.0001);
		assertEquals(90.0, bill.getCategoryDiscountedItemsTotal(), 0.0001);
		assertEquals(80.0, bill.getPlanDiscountedItemsTotal(), 0.0001);
		assertEquals(15.0, bill.getDeliveryFee(), 0.0001);
	}

	@Test
	public void billShouldRejectNegativeRawItemsTotal() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Bill(-1.0, 90.0, 80.0, 15.0);
		});
	}

	@Test
	public void billShouldRejectNegativeCategoryDiscountedTotal() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Bill(100.0, -1.0, 80.0, 15.0);
		});
	}

	@Test
	public void billShouldRejectNegativePlanDiscountedTotal() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Bill(100.0, 90.0, -1.0, 15.0);
		});
	}

	@Test
	public void billShouldRejectNegativeDeliveryFee() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Bill(100.0, 90.0, 80.0, -1.0);
		});
	}

	@Test
	public void billSummaryShouldContainImportantValues() {
		Bill bill = new Bill(100.0, 90.0, 80.0, 15.0);

		String summary = bill.summary();

		assertTrue(summary.contains("Raw items total: 100.0"));
		assertTrue(summary.contains("After category discounts: 90.0"));
		assertTrue(summary.contains("After customer plan: 80.0"));
		assertTrue(summary.contains("Delivery fee: 15.0"));
		assertTrue(summary.contains("Final total: 95.0"));
	}
}