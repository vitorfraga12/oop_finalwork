package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Category;
import pricing.PercentageCategoryDiscount;

public class CategoryTest {

	@Test
	public void categoryShouldApplyNoDiscountByDefault() {
		Category category = new Category("dairy", true);

		assertEquals(100.0, category.applyDiscount(100.0), 0.0001);
	}

	@Test
	public void categoryShouldApplyPercentageDiscountAfterPolicyChange() {
		Category category = new Category("dairy", true);

		category.setCategoryDiscountPolicy(new PercentageCategoryDiscount(10.0));

		assertEquals(90.0, category.applyDiscount(100.0), 0.0001);
	}

	@Test
	public void categoryShouldKnowIfItIsPerishable() {
		Category dairy = new Category("dairy", true);
		Category cleaning = new Category("cleaning", false);

		assertTrue(dairy.isPerishable());
		assertFalse(cleaning.isPerishable());
	}

	@Test
	public void categoryShouldRejectNullPolicy() {
		Category category = new Category("dairy", true);

		assertThrows(IllegalArgumentException.class, () -> {
			category.setCategoryDiscountPolicy(null);
		});
	}

	@Test
	public void categoryShouldRejectEmptyName() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Category("", true);
		});
	}
}