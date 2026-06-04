package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import pricing.CategoryDiscountPolicy;
import pricing.NoCategoryDiscount;
import pricing.PercentageCategoryDiscount;

public class CategoryDiscountPolicyTest {

	@Test
	public void noCategoryDiscountShouldReturnSamePrice() {
		CategoryDiscountPolicy policy = new NoCategoryDiscount();

		assertEquals(100.0, policy.categoryDiscount(100.0));
	}

	@Test
	public void percentageCategoryDiscountShouldApplyReduction() {
		CategoryDiscountPolicy policy = new PercentageCategoryDiscount(10.0);

		assertEquals(90.0, policy.categoryDiscount(100.0));
		assertEquals(45.0, policy.categoryDiscount(50.0));
	}

	@Test
	public void percentageCategoryDiscountShouldRejectNegativePercent() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PercentageCategoryDiscount(-5.0);
		});
	}

	@Test
	public void percentageCategoryDiscountShouldRejectPercentAbove100() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PercentageCategoryDiscount(120.0);
		});
	}
}