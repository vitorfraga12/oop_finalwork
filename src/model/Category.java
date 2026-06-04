package model;

import pricing.CategoryDiscountPolicy;
import pricing.NoCategoryDiscount;

public class Category {
	
	private String name;
	private CategoryDiscountPolicy policy;
	private boolean perishable;
	
	public Category(String name, boolean perishable) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Category name cannot be empty.");
		}
		
		this.name = name;
		this.policy = new NoCategoryDiscount();
		this.perishable = perishable;
	}
	
	public String getName() {
		return name;
	}
	
	public double applyDiscount(double price) {
		return policy.categoryDiscount(price);
	}

	public void setCategoryDiscountPolicy(CategoryDiscountPolicy policy) {
		if (policy == null) {
			throw new IllegalArgumentException("Discount policy cannot be null.");
		}
		this.policy = policy;
	}

	public CategoryDiscountPolicy getDiscountPolicy() {
		return policy;
	}
	
	public boolean isPerishable(){
		return perishable;
	}
	}