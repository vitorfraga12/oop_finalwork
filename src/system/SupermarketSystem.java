package system;

import java.util.HashMap;
import java.util.Map;

import checkout.Bill;
import checkout.CartLine;
import checkout.CheckoutSession;

import delivery.DeliveryPolicy;
import delivery.DeliveryRequest;
import delivery.StandardDeliveryPolicy;

import factory.PlanFactory;

import model.Cashier;
import model.Category;
import model.Customer;
import model.Item;
import model.Manager;
import model.User;

import observer.LowStockNotifier;
import observer.ManagerNotifier;

import payment.BankCard;
import payment.POS;
import payment.PaymentResult;
import payment.SimulatedPaymentOutcome;
import payment.TAS;
import pricing.CostumerPlan;
import pricing.NoCategoryDiscount;
import pricing.PercentageCategoryDiscount;

public class SupermarketSystem {

	private Map<String, User> users;
	private Map<String, Item> catalogue;
	private Map<String, Category> categories;

	private User currentUser;
	private CheckoutSession currentCheckout;

	private double revenue;

	private POS pos;
	private TAS tas;
	private PlanFactory planFactory;
	private DeliveryPolicy deliveryPolicy;
	private LowStockNotifier lowStockNotifier;
	private boolean configured;

	public SupermarketSystem() {
		this.users = new HashMap<>();
		this.catalogue = new HashMap<>();
		this.categories = new HashMap<>();

		this.currentUser = null;
		this.currentCheckout = null;
		this.revenue = 0.0;
		
		this.tas= new TAS();
		this.pos = new POS(tas);
		
		
		this.planFactory = new PlanFactory();
		this.deliveryPolicy = new StandardDeliveryPolicy();

		this.lowStockNotifier = new LowStockNotifier();
		this.lowStockNotifier.addObserver(new ManagerNotifier());
		
		this.configured = false;

		addUser(new Manager("CEO", "Manager", "ceo", "123456789"));
	}

	public void setup() {
		if (configured) {
			throw new IllegalStateException("Setup has already been loaded.");
		}
		users.clear();
		catalogue.clear();
		categories.clear();

		currentUser = null;
		currentCheckout = null;
		revenue = 0.0;
		
		this.tas= new TAS();
		pos = new POS(tas);

		addUser(new Manager("CEO", "Manager", "ceo", "123456789"));

		Category fruitAndVegetables = new Category("fruit-and-vegetables", true);
		Category dairy = new Category("dairy", true);
		Category meat = new Category("meat", true);

		categories.put(normalize(fruitAndVegetables.getName()), fruitAndVegetables);
		categories.put(normalize(dairy.getName()), dairy);
		categories.put(normalize(meat.getName()), meat);

		addUser(new Cashier("Default", "Cashier", "cashier", "1234"));

		Customer customer = new Customer("Default", "Customer", "customer", "1234", "near");
		addUser(customer);

		addItemAsSystem("apple", "fruit-and-vegetables", 2.0, 0.2, 100);
		addItemAsSystem("milk", "dairy", 2.5, 1.0, 20);
		addItemAsSystem("chicken", "meat", 8.0, 1.5, 15);

		tas.registerCard(new BankCard("1111", "1234", 1000.0));
		tas.registerCard(new BankCard("2222", "0000", 30.0));
		tas.registerCard(new BankCard("CARD" + customer.getId(), "0000", 1000.0));
		configured = true;
	}

	public User login(String username, String password) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty.");
		}

		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be empty.");
		}

		User user = users.get(normalize(username));

		if (user == null || !user.checkPassword(password)) {
			throw new IllegalArgumentException("Invalid username or password.");
		}

		currentUser = user;
		return currentUser;
	}

	public void logout() {
		requireLoggedUser();
		currentUser = null;
	}

	public void registerCashier(String firstName, String lastName, String username, String password) {
		requireManager();

		Cashier cashier = new Cashier(firstName, lastName, username, password);
		addUser(cashier);
	}

	public void registerCustomer(String firstName, String lastName, String username, String address, String password) {
		requireManager();

		Customer customer = new Customer(firstName, lastName, username, password, address);
		addUser(customer);

		tas.registerCard(new BankCard("CARD" + customer.getId(), "0000", 1000.0));
	}

	public void addItem(String itemName, String categoryName, double unitPrice, double weight, int initialStock) {
		requireManager();

		addItemAsSystem(itemName, categoryName, unitPrice, weight, initialStock);
	}

	public void restock(String itemName, int quantity) {
		requireManager();

		Item item = findItem(itemName);
		item.restock(quantity);
	}

	public void setCategoryDiscount(String categoryName, double discountPercent) {
		requireManager();

		if (discountPercent < 0 || discountPercent > 100) {
			throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
		}

		Category category = findOrCreateCategory(categoryName);

		if (discountPercent == 0) {
			category.setCategoryDiscountPolicy(new NoCategoryDiscount());
		} else {
			category.setCategoryDiscountPolicy(new PercentageCategoryDiscount(discountPercent));
		}
	}

	public void subscribeToPlan(String planName) {
		Customer customer = getCurrentCustomer();

		CostumerPlan plan = planFactory.createPlan(planName);
		customer.subscribeToPlan(plan);

		revenue += plan.getAnnualFee();
	}

	public void requestDelivery(String address) {
		Customer customer = getCurrentCustomer();

		double distanceKm = estimateDistanceKm(address);
		DeliveryRequest deliveryRequest = new DeliveryRequest(address, distanceKm);

		customer.requestDelivery(deliveryRequest);
	}

	public void startCheckout(String customerUsername) {
		requireCashier();

		if (currentCheckout != null) {
			throw new IllegalStateException("A checkout session is already open.");
		}

		User user = users.get(normalize(customerUsername));

		if (user == null) {
			throw new IllegalArgumentException("Unknown customer: " + customerUsername);
		}

		if (!(user instanceof Customer)) {
			throw new IllegalArgumentException(customerUsername + " is not a customer.");
		}

		Customer customer = (Customer) user;
		DeliveryRequest delivery = customer.consumeWaitingDelivery();

		currentCheckout = new CheckoutSession(customer, delivery);
	}

	public void scanItem(String itemName, int quantity) {
		requireCashier();
		requireOpenCheckout();

		Item item = findItem(itemName);

		if (!item.hasEnoughStock(quantity)) {
			throw new IllegalArgumentException("Not enough stock for item: " + itemName);
		}

		currentCheckout.scanItem(item, quantity);
	}

	public Bill computeBill() {
		requireCashier();
		requireOpenCheckout();

		return currentCheckout.computeBill(deliveryPolicy);
	}

	public PaymentResult pay(String cardNumber, String pin) {
		requireCashier();
		requireOpenCheckout();

		Bill bill = currentCheckout.getBill();

		if (bill == null) {
			throw new IllegalStateException("Bill must be computed before payment.");
		}

		PaymentResult result = pos.pay(cardNumber, pin, bill.getFinalTotal());

		if (result == PaymentResult.SUCCESS) {
			revenue += bill.getFinalTotal();

			for (CartLine line : currentCheckout.getCart().getLines()) {
				Item item = line.getItem();

				item.decreaseStock(line.getQuantity());

				if (item.isBelowThreshold()) {
					lowStockNotifier.notifyLowStock(item);
				}
			}

			printReceipt(bill);
			currentCheckout = null;
		}

		return result;
	}

	public void simulatePayment(String outcomeName) {
		requireCashier();

		if (outcomeName == null || outcomeName.isBlank()) {
			throw new IllegalArgumentException("Outcome cannot be empty.");
		}

		try {
			SimulatedPaymentOutcome outcome = SimulatedPaymentOutcome.valueOf(outcomeName.toUpperCase());
			pos.simulateNextPayment(outcome);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Invalid payment outcome. Use: SUCCESS, INSUFFICIENT_FUNDS, PIN_WRONG or AUTH_DENIED."
			);
		}
	}

	public String showInventory() {
		requireManager();

		if (catalogue.isEmpty()) {
			return "Inventory is empty.";
		}

		String result = "";

		for (Item item : catalogue.values()) {
			result += item.getName()
					+ " | category: " + item.getCategory().getName()
					+ " | stock: " + item.getStock();

			if (item.isBelowThreshold()) {
				result += " | LOW STOCK";
			}

			result += "\n";
		}

		return result;
	}

	public double showRevenue() {
		requireManager();
		return revenue;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public CheckoutSession getCurrentCheckout() {
		return currentCheckout;
	}

	private void addItemAsSystem(String itemName, String categoryName, double unitPrice, double weight, int initialStock) {
		if (itemName == null || itemName.isBlank()) {
			throw new IllegalArgumentException("Item name cannot be empty.");
		}

		if (catalogue.containsKey(normalize(itemName))) {
			throw new IllegalArgumentException("Item already exists: " + itemName);
		}

		Category category = findOrCreateCategory(categoryName);
		Item item = new Item(itemName, category, unitPrice, weight, initialStock);

		catalogue.put(normalize(itemName), item);
	}

	private void addUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null.");
		}

		String username = normalize(user.getUsername());

		if (users.containsKey(username)) {
			throw new IllegalArgumentException("Username already exists: " + user.getUsername());
		}

		users.put(username, user);
	}

	private Category findOrCreateCategory(String categoryName) {
		if (categoryName == null || categoryName.isBlank()) {
			throw new IllegalArgumentException("Category name cannot be empty.");
		}

		String key = normalize(categoryName);
		Category category = categories.get(key);

		if (category == null) {
			category = new Category(categoryName, isPerishableCategory(categoryName));
			categories.put(key, category);
		}

		return category;
	}

	private Item findItem(String itemName) {
		if (itemName == null || itemName.isBlank()) {
			throw new IllegalArgumentException("Item name cannot be empty.");
		}

		Item item = catalogue.get(normalize(itemName));

		if (item == null) {
			throw new IllegalArgumentException("Unknown item: " + itemName);
		}

		return item;
	}

	private boolean isPerishableCategory(String categoryName) {
		String normalized = normalize(categoryName);

		return normalized.equals("dairy")
				|| normalized.equals("meat")
				|| normalized.equals("fruit-and-vegetables");
	}

	private double estimateDistanceKm(String address) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("Address cannot be empty.");
		}

		String zone = normalize(address);

		switch (zone) {
			case "near":
				return 5.0;

			case "medium":
				return 25.0;

			case "far":
				return 45.0;

			default:
				throw new IllegalArgumentException("Unknown delivery zone. Use: near, medium or far.");
		}
	}

	private void printReceipt(Bill bill) {
		System.out.println("----- RECEIPT -----");
		System.out.println(bill.summary());
		System.out.println("-------------------");
	}

	private void requireOpenCheckout() {
		if (currentCheckout == null) {
			throw new IllegalStateException("No checkout session is currently open.");
		}
	}

	private void requireLoggedUser() {
		if (currentUser == null) {
			throw new IllegalStateException("You must be logged in.");
		}
	}

	private Manager getCurrentManager() {
		requireLoggedUser();

		if (!(currentUser instanceof Manager)) {
			throw new IllegalStateException("Only a manager can perform this command.");
		}

		return (Manager) currentUser;
	}

	private Cashier getCurrentCashier() {
		requireLoggedUser();

		if (!(currentUser instanceof Cashier)) {
			throw new IllegalStateException("Only a cashier can perform this command.");
		}

		return (Cashier) currentUser;
	}

	private Customer getCurrentCustomer() {
		requireLoggedUser();

		if (!(currentUser instanceof Customer)) {
			throw new IllegalStateException("Only a customer can perform this command.");
		}

		return (Customer) currentUser;
	}

	private void requireManager() {
		getCurrentManager();
	}

	private void requireCashier() {
		getCurrentCashier();
	}

	private String normalize(String text) {
		return text.toLowerCase();
	}
}