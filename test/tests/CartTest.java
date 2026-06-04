package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import checkout.Cart;
import model.Category;
import model.Item;
import pricing.PercentageCategoryDiscount;

public class CartTest {

	@Test
	public void cartShouldAddItem() {
		Cart cart = new Cart();

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		cart.addItem(milk, 2);

		assertFalse(cart.isEmpty());
		assertEquals(1, cart.getLines().size());
	}

	@Test
	public void cartShouldMergeSameItemInSameLine() {
		Cart cart = new Cart();

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		cart.addItem(milk, 2);
		cart.addItem(milk, 3);

		assertEquals(1, cart.getLines().size());
		assertEquals(5, cart.getLines().get(0).getQuantity());
	}

	@Test
	public void cartShouldComputeRawTotal() {
		Cart cart = new Cart();

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);
		Item yogurt = new Item("yogurt", dairy, 3.0, 0.5, 20);

		cart.addItem(milk, 2);
		cart.addItem(yogurt, 1);

		assertEquals(8.0, cart.computeRawTotal(), 0.0001);
	}

	@Test
	public void cartShouldComputeCategoryDiscountedTotal() {
		Cart cart = new Cart();

		Category dairy = new Category("dairy", true);
		dairy.setCategoryDiscountPolicy(new PercentageCategoryDiscount(10.0));

		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		cart.addItem(milk, 2);

		assertEquals(4.5, cart.computeCategoryDiscountedTotal(), 0.0001);
	}

	@Test
	public void cartShouldComputeTotalWeight() {
		Cart cart = new Cart();

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		cart.addItem(milk, 3);

		assertEquals(3.0, cart.computeTotalWeight(), 0.0001);
	}

	@Test
	public void cartShouldRejectItemWithNotEnoughStock() {
		Cart cart = new Cart();

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 2);

		assertThrows(IllegalArgumentException.class, () -> {
			cart.addItem(milk, 3);
		});
	}
}