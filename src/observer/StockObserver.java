package observer;

import model.Item;

public interface StockObserver {
	void onLowStock(Item item);
}
