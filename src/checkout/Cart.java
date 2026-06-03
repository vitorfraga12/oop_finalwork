package checkout;

import java.util.ArrayList;

import model.Item;

public class Cart {

	private ArrayList<CartLine> lines;

	public Cart() {
		this.lines = new ArrayList<>();
	}

	public void addItem(Item item, int quantity) {
		if (item == null) {
			throw new IllegalArgumentException("Item cannot be null.");
		}

		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be positive.");
		}

		if (!item.hasEnoughStock(quantity)) {
			throw new IllegalArgumentException("Not enough stock for item: " + item.getName());
		}

		CartLine existingLine = findLineByItem(item);

		if (existingLine == null) {
			lines.add(new CartLine(item, quantity));
		} else {
			int newQuantity = existingLine.getQuantity() + quantity;

			if (!item.hasEnoughStock(newQuantity)) {
				throw new IllegalArgumentException("Not enough stock for item: " + item.getName());
			}

			existingLine.increaseQuantity(quantity);
		}
	}

	public boolean isEmpty() {
		return lines.isEmpty();
	}

	public ArrayList<CartLine> getLines() {
		return lines;
	}

	public double computeRawTotal() {
		double total = 0.0;

		for (CartLine line : lines) {
			total += line.getRawPrice();
		}

		return total;
	}

	public double computeCategoryDiscountedTotal() {
		double total = 0.0;

		for (CartLine line : lines) {
			total += line.getDiscountedPrice();
		}

		return total;
	}

	public double computeTotalWeight() {
		double totalWeight = 0.0;

		for (CartLine line : lines) {
			totalWeight += line.getWeight();
		}

		return totalWeight;
	}

	public String displayCart() {
		if (lines.isEmpty()) {
			return "Cart is empty.";
		}

		String result = "";

		for (CartLine line : lines) {
			result += line.displayLine() + "\n";
		}

		return result;
	}

	private CartLine findLineByItem(Item item) {
		for (CartLine line : lines) {
			if (line.getItem() == item) {
				return line;
			}
		}

		return null;
	}
}