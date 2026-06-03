package observer;

import model.Item;

public class ManagerNotifier implements StockObserver {
	
	@Override
	public void onLowStock(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("Item cant be null");
		}

		System.out.println("[LOW STOCK ALERT] Item: " + item.getName()
				+ " | Current stock: " + item.getStock());
	}

}
