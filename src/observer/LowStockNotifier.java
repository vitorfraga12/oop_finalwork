package observer;

import java.util.List;

import java.util.ArrayList;
import model.Item;

public class LowStockNotifier {
	private List<StockObserver> observers;
	
	public LowStockNotifier() {
		this.observers = new ArrayList<>();
	}
	
	public void addObserver(StockObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("Observer cant be null");
		}
		observers.add(observer);
		
	}
	
	public void removeObserver(StockObserver observer) {
		observers.remove(observer);
	}
	
	
	public void notifyLowStock(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("Item cannot be null.");
		}

		for (StockObserver observer : observers) {
			observer.onLowStock(item);
		}
	}
	

}
