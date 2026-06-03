package checkout;

import delivery.DeliveryRequest;
import model.Customer;
import delivery.DeliveryPolicy;
import model.Item;

public class CheckoutSession {

	private Customer customer;
	private Cart cart;
	private DeliveryRequest deliveryRequest;
	private Bill bill;

	public CheckoutSession(Customer customer) {
		this(customer, null);
	}

	public CheckoutSession(Customer customer, DeliveryRequest deliveryRequest) {
		if (customer == null) {
			throw new IllegalArgumentException("Customer cannot be null.");
		}

		this.customer = customer;
		this.cart = new Cart();
		this.deliveryRequest = deliveryRequest;
		this.bill = null;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Cart getCart() {
		return cart;
	}

	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public boolean hasDelivery() {
		return deliveryRequest != null;
	}

	public Bill getBill() {
		return bill;
	}

	public void scanItem(Item item, int quantity) {
		cart.addItem(item, quantity);

		// The old bill is no longer valid after scanning a new item.
		bill = null;
	}

	public Bill computeBill(DeliveryPolicy deliveryPolicy) {
		if (cart.isEmpty()) {
			throw new IllegalStateException("Cannot compute bill for an empty cart.");
		}

		double rawItemsTotal = cart.computeRawTotal();

		double categoryDiscountedTotal = cart.computeCategoryDiscountedTotal();

		double planDiscountedTotal = customer.getPlan().costumerDiscount(categoryDiscountedTotal);

		double deliveryFee = 0.0;

		if (hasDelivery()) {
			if (deliveryPolicy == null) {
				throw new IllegalArgumentException("Delivery policy cannot be null.");
			}

			double totalWeight = this.cart.computeTotalWeight();
			double distanceKm = this.deliveryRequest.getDistanceKm();

			deliveryFee = deliveryPolicy.computeFee(
					totalWeight,
					distanceKm,
					customer.getPlan(),
					planDiscountedTotal
			);
		}

		bill = new Bill(
				rawItemsTotal,
				categoryDiscountedTotal,
				planDiscountedTotal,
				deliveryFee
		);

		return bill;
	}

	public String displayScannedItems() {
		return cart.displayCart();
	}
}
