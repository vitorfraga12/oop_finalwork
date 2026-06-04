package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Category;
import model.Item;
import pricing.PercentageCategoryDiscount;

public class ItemTest {

	@Test
	public void itemShouldComputePriceForQuantity() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertEquals(5.0, milk.priceForQuantity(2), 0.0001);
	}

	@Test
	public void itemShouldComputeDiscountedPriceForQuantity() {
		Category dairy = new Category("dairy", true);
		dairy.setCategoryDiscountPolicy(new PercentageCategoryDiscount(10.0));

		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertEquals(4.5, milk.discountedPriceForQuantity(2), 0.0001);
	}

	@Test
	public void itemShouldComputeWeightForQuantity() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertEquals(3.0, milk.weightForQuantity(3), 0.0001);
	}

	@Test
	public void itemShouldCheckEnoughStock() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertTrue(milk.hasEnoughStock(10));
		assertFalse(milk.hasEnoughStock(30));
	}

	@Test
	public void itemShouldDecreaseStock() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		milk.decreaseStock(5);

		assertEquals(15, milk.getStock());
	}

	@Test
	public void itemShouldRestock() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		milk.restock(5);

		assertEquals(25, milk.getStock());
	}

	@Test
	public void itemShouldRejectDecreaseAboveStock() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertThrows(IllegalArgumentException.class, () -> {
			milk.decreaseStock(30);
		});
	}

	@Test
	public void itemShouldRejectInvalidQuantityForPrice() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertThrows(IllegalArgumentException.class, () -> {
			milk.priceForQuantity(0);
		});
	}

	@Test
	public void itemShouldRejectInvalidQuantityForRestock() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertThrows(IllegalArgumentException.class, () -> {
			milk.restock(0);
		});
	}

	@Test
	public void perishableItemShouldBeBelowThresholdWhenStockIsLow() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 3);

		milk.decreaseStock(2);

		assertTrue(milk.isBelowThreshold());
	}

	@Test
	public void nonPerishableItemShouldNotBeBelowThresholdEvenWhenStockIsLow() {
		Category cleaning = new Category("cleaning", false);
		Item soap = new Item("soap", cleaning, 2.0, 0.5, 3);

		soap.decreaseStock(2);

		assertFalse(soap.isBelowThreshold());
	}

	@Test
	public void itemShouldAllowChangingThreshold() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 10);

		milk.setThreshold(10);

		assertEquals(10, milk.getThreshold());
		assertTrue(milk.isBelowThreshold());
	}

	@Test
	public void itemShouldRejectNegativeThreshold() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 10);

		assertThrows(IllegalArgumentException.class, () -> {
			milk.setThreshold(-1);
		});
	}

	@Test
	public void itemShouldRejectNegativeUnitPrice() {
		Category dairy = new Category("dairy", true);

		assertThrows(IllegalArgumentException.class, () -> {
			new Item("milk", dairy, -2.5, 1.0, 20);
		});
	}

	@Test
	public void itemShouldRejectNegativeWeight() {
		Category dairy = new Category("dairy", true);

		assertThrows(IllegalArgumentException.class, () -> {
			new Item("milk", dairy, 2.5, -1.0, 20);
		});
	}

	@Test
	public void itemShouldRejectNegativeStock() {
		Category dairy = new Category("dairy", true);

		assertThrows(IllegalArgumentException.class, () -> {
			new Item("milk", dairy, 2.5, 1.0, -20);
		});
	}
}