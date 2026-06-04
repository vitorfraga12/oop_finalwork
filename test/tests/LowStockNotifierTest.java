package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Category;
import model.Item;
import observer.LowStockNotifier;
import observer.StockObserver;

public class LowStockNotifierTest {

	private class FakeObserver implements StockObserver {

		private boolean notified;

		public FakeObserver() {
			this.notified = false;
		}

		@Override
		public void onLowStock(Item item) {
			this.notified = true;
		}

		public boolean wasNotified() {
			return notified;
		}
	}

	@Test
	public void notifierShouldNotifyObserver() {
		LowStockNotifier notifier = new LowStockNotifier();
		FakeObserver observer = new FakeObserver();

		Category dairy = new Category("dairy", true);
		Item milk = new Item("milk", dairy, 2.5, 1.0, 1);

		notifier.addObserver(observer);
		notifier.notifyLowStock(milk);

		assertTrue(observer.wasNotified());
	}

	@Test
	public void notifierShouldRejectNullObserver() {
		LowStockNotifier notifier = new LowStockNotifier();

		assertThrows(IllegalArgumentException.class, () -> {
			notifier.addObserver(null);
		});
	}

	@Test
	public void notifierShouldRejectNullItem() {
		LowStockNotifier notifier = new LowStockNotifier();

		assertThrows(IllegalArgumentException.class, () -> {
			notifier.notifyLowStock(null);
		});
	}
}