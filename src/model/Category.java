package model;

import pricing.CategoryDiscountPolicy;
import pricing.NoCategoryDiscount;

public class Category {
	
	private String name;
	private CategoryDiscountPolicy policy;
	private boolean perishable;
	
	public Category(String name, boolean perishable) {
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
		this.policy = policy;
	}

	public CategoryDiscountPolicy getDiscountPolicy() {
		return policy;
	}
	
	public boolean isPerishable(){
		return perishable;
	}
	}