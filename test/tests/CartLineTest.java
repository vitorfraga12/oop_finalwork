package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import checkout.CartLine;
import model.Category;
import model.Item;
import pricing.PercentageCategoryDiscount;

public class CartLineTest {

	@Test
	public void cartLineShouldComputeRawPrice() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		CartLine line = new CartLine(milk, 2);

		assertEquals(5.0, line.getRawPrice(), 0.0001);
	}

	@Test
	public void cartLineShouldComputeDiscountedPrice() {
		Category dairy = new Category("dairy", true);
		dairy.setCategoryDiscountPolicy(new PercentageCategoryDiscount(10.0));

		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);
		CartLine line = new CartLine(milk, 2);

		assertEquals(4.5, line.getDiscountedPrice(), 0.0001);
	}

	@Test
	public void cartLineShouldComputeWeight() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		CartLine line = new CartLine(milk, 3);

		assertEquals(3.0, line.getWeight(), 0.0001);
	}

	@Test
	public void cartLineShouldIncreaseQuantity() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		CartLine line = new CartLine(milk, 2);
		line.increaseQuantity(3);

		assertEquals(5, line.getQuantity());
	}

	@Test
	public void cartLineShouldRejectInvalidQuantity() {
		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 20);

		assertThrows(IllegalArgumentException.class, () -> {
			new CartLine(milk, 0);
		});
	}
}