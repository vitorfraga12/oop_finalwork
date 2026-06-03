package checkout;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import model.Item;

public class Cart {
	private List<CartLine> lines;
	
	public Cart() {
		this.lines = new ArrayList<>();
	}
	
	public void addItem(Item item, int quantity) {
		if (item == null) {
			throw new IllegalArgumentException("Item cant be null");
		}
		
		if (quantity <= 0) {
			throw new IllegalArgumentException("Item's quantity must be positive.");
		}
		
		if (!item.hasEnoughStock(quantity)) {
			throw new IllegalArgumentException("Not enough stock for item: " + item.getName());
		}
		
		CartLine existingLine = findLineByItem(item);
		
		if (existingLine != null) {
			int newQuantity = existingLine.getQuantity() + quantity;

			if (!item.hasEnoughStock(newQuantity)) {
				throw new IllegalArgumentException("Not enough stock for item: " + item.getName());
			}

			existingLine.increaseQuantity(quantity);
		} else {
			lines.add(new CartLine(item, quantity));
		}
		
	}
	
	public double computeRawTotal() {
		double total = 0;
		
		for(CartLine line: lines) {
			total += line.computeRawPrice();
		}
		return total;
	}
	
	public double computeDiscountedItemsTotal() {
		double total = 0;

		for (CartLine line : lines) {
			total += line.computeDiscountedPrice();
		}

		return total;
	}
	
	public double computeTotalWeight() {
		double totalWeight = 0;

		for (CartLine line : lines) {
			totalWeight += line.computeWeight();
		}

		return totalWeight;
	}
	
	public boolean isEmpty() {
		return lines.isEmpty();
	}
	
	public List<CartLine> getLines() {
		return Collections.unmodifiableList(lines);
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
