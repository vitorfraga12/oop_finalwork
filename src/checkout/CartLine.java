package checkout;

import model.Item;

public class CartLine {

	private Item item;
	private int quantity;

	public CartLine(Item item, int quantity) {
		if (item == null) {
			throw new IllegalArgumentException("Item cannot be null.");
		}

		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be positive.");
		}

		this.item = item;
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void increaseQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be positive.");
		}

		this.quantity += quantity;
	}

	public double getRawPrice() {
		return item.priceForQuantity(quantity);
	}

	public double getDiscountedPrice() {
		return item.discountedPriceForQuantity(quantity);
	}

	public double getWeight() {
		return item.weightForQuantity(quantity);
	}

	public String getLabel() {
		return item.getName() + " x" + quantity;
	}

	public String displayLine() {
		return getLabel()
				+ " | unit price: " + item.getUnitPrice()
				+ " | raw total: " + getRawPrice()
				+ " | after category discount: " + getDiscountedPrice();
	}
}