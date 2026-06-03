package cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import checkout.Bill;
import model.User;
import payment.PaymentResult;
import system.SupermarketSystem;

public class SupermarketCLI {

	private SupermarketSystem system;
	private Scanner scanner;

	public SupermarketCLI() {
		this.system = new SupermarketSystem();
		this.scanner = new Scanner(System.in);
	}

	public static void main(String[] args) {
		SupermarketCLI cli = new SupermarketCLI();
		cli.start();
	}

	public void start() {
		printWelcomeMessage();

		boolean running = true;

		while (running) {
			System.out.print("> ");
			String line = scanner.nextLine().trim();

			if (line.isEmpty()) {
				continue;
			}

			if (line.equalsIgnoreCase("STOP") || line.equalsIgnoreCase("EXIT")) {
				System.out.println("Exiting the program...");
				running = false;
			} else {
				executeCommand(line);
			}
		}

		scanner.close();
	}

	private void executeCommand(String line) {
		String[] parts = line.split("\\s+");
		String command = parts[0].toLowerCase();

		try {
			switch (command) {
				case "login":
					handleLogin(parts);
					break;

				case "logout":
					handleLogout(parts);
					break;

				case "setup":
					handleSetup(parts);
					break;

				case "registercashier":
					handleRegisterCashier(parts);
					break;

				case "registercustomer":
					handleRegisterCustomer(parts);
					break;

				case "additem":
					handleAddItem(parts);
					break;

				case "restock":
					handleRestock(parts);
					break;

				case "setcategorydiscount":
					handleSetCategoryDiscount(parts);
					break;

				case "subscribetoplan":
					handleSubscribeToPlan(parts);
					break;

				case "startcheckout":
					handleStartCheckout(parts);
					break;

				case "scanitem":
					handleScanItem(parts);
					break;

				case "computebill":
					handleComputeBill(parts);
					break;

				case "requestdelivery":
					handleRequestDelivery(parts);
					break;

				case "pay":
					handlePay(parts);
					break;

				case "simulatepayment":
					handleSimulatePayment(parts);
					break;

				case "showinventory":
					handleShowInventory(parts);
					break;

				case "showrevenue":
					handleShowRevenue(parts);
					break;

				case "runtest":
					handleRunTest(parts);
					break;

				case "help":
					printHelp();
					break;

				default:
					System.out.println("Unknown command: " + parts[0]);
					System.out.println("Type help for available commands.");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void handleLogin(String[] parts) {
		checkNumberOfArguments(parts, 3, "Usage: login <username> <password>");

		User user = system.login(parts[1], parts[2]);
		System.out.println("Logged in as: " + user.getUsername());
	}

	private void handleLogout(String[] parts) {
		checkNumberOfArguments(parts, 1, "Usage: logout");

		system.logout();
		System.out.println("Logged out.");
	}

	private void handleSetup(String[] parts) {
		checkNumberOfArguments(parts, 1, "Usage: setup");

		system.setup();
		System.out.println("Default setup loaded.");
	}

	private void handleRegisterCashier(String[] parts) {
		checkNumberOfArguments(parts, 5,
				"Usage: registerCashier <firstName> <lastName> <username> <password>");

		system.registerCashier(parts[1], parts[2], parts[3], parts[4]);
		System.out.println("Cashier registered: " + parts[3]);
	}

	private void handleRegisterCustomer(String[] parts) {
		checkNumberOfArguments(parts, 6,
				"Usage: registerCustomer <firstName> <lastName> <username> <address> <password>");

		system.registerCustomer(parts[1], parts[2], parts[3], parts[4], parts[5]);
		System.out.println("Customer registered: " + parts[3]);
		System.out.println("Auto-generated test card: CARD<customerId>, PIN: 0000");
	}

	private void handleAddItem(String[] parts) {
		checkNumberOfArguments(parts, 6,
				"Usage: addItem <itemName> <categoryName> <unitPrice> <weight> <initialStock>");

		String itemName = parts[1];
		String categoryName = parts[2];
		double unitPrice = Double.parseDouble(parts[3]);
		double weight = Double.parseDouble(parts[4]);
		int initialStock = Integer.parseInt(parts[5]);

		system.addItem(itemName, categoryName, unitPrice, weight, initialStock);
		System.out.println("Item added: " + itemName);
	}

	private void handleRestock(String[] parts) {
		checkNumberOfArguments(parts, 3,
				"Usage: restock <itemName> <quantity>");

		String itemName = parts[1];
		int quantity = Integer.parseInt(parts[2]);

		system.restock(itemName, quantity);
		System.out.println("Item restocked: " + itemName);
	}

	private void handleSetCategoryDiscount(String[] parts) {
		checkNumberOfArguments(parts, 3,
				"Usage: setCategoryDiscount <categoryName> <discountPercent>");

		String categoryName = parts[1];
		double discountPercent = Double.parseDouble(parts[2]);

		system.setCategoryDiscount(categoryName, discountPercent);
		System.out.println("Category discount updated: " + categoryName + " = " + discountPercent + "%");
	}

	private void handleSubscribeToPlan(String[] parts) {
		checkNumberOfArguments(parts, 2,
				"Usage: subscribeToPlan <planName>");

		system.subscribeToPlan(parts[1]);
		System.out.println("Subscribed to plan: " + parts[1]);
	}

	private void handleStartCheckout(String[] parts) {
		checkNumberOfArguments(parts, 2,
				"Usage: startCheckout <customerUsername>");

		system.startCheckout(parts[1]);
		System.out.println("Checkout started for customer: " + parts[1]);
	}

	private void handleScanItem(String[] parts) {
		checkNumberOfArguments(parts, 3,
				"Usage: scanItem <itemName> <quantity>");

		String itemName = parts[1];
		int quantity = Integer.parseInt(parts[2]);

		system.scanItem(itemName, quantity);
		System.out.println("Scanned: " + itemName + " x" + quantity);
	}

	private void handleComputeBill(String[] parts) {
		checkNumberOfArguments(parts, 1,
				"Usage: computeBill");

		Bill bill = system.computeBill();
		System.out.println(bill.summary());
	}

	private void handleRequestDelivery(String[] parts) {
		checkNumberOfArguments(parts, 2,
				"Usage: requestDelivery <address>");

		system.requestDelivery(parts[1]);
		System.out.println("Delivery requested for next purchase: " + parts[1]);
		System.out.println("Available delivery zones: near, medium, far");
	}

	private void handlePay(String[] parts) {
		checkNumberOfArguments(parts, 3,
				"Usage: pay <cardNumber> <pin>");

		PaymentResult result = system.pay(parts[1], parts[2]);
		System.out.println("Payment result: " + result);
	}

	private void handleSimulatePayment(String[] parts) {
		checkNumberOfArguments(parts, 2,
				"Usage: simulatePayment <SUCCESS|INSUFFICIENT_FUNDS|PIN_WRONG|AUTH_DENIED>");

		system.simulatePayment(parts[1]);
		System.out.println("Next payment outcome forced to: " + parts[1].toUpperCase());
	}

	private void handleShowInventory(String[] parts) {
		checkNumberOfArguments(parts, 1,
				"Usage: showInventory");

		System.out.print(system.showInventory());
	}

	private void handleShowRevenue(String[] parts) {
		checkNumberOfArguments(parts, 1,
				"Usage: showRevenue");

		System.out.println("Total revenue: " + system.showRevenue());
	}

	private void handleRunTest(String[] parts) {
		checkNumberOfArguments(parts, 2,
				"Usage: runTest <testScenario-file>");

		runTest(parts[1]);
	}

	private void runTest(String fileName) {
		File file = new File(fileName);

		try (Scanner fileScanner = new Scanner(file)) {
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine().trim();

				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}

				System.out.println("> " + line);
				executeCommand(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: test scenario file not found: " + fileName);
		}
	}

	private void checkNumberOfArguments(String[] parts, int expected, String usageMessage) {
		if (parts.length != expected) {
			throw new IllegalArgumentException(usageMessage);
		}
	}

	private void printWelcomeMessage() {
		System.out.println("Supermarket Checkout System");
		System.out.println("Type stop or exit to quit.");
		System.out.println();
		printHelp();
	}

	private void printHelp() {
		System.out.println("Available commands:");
		System.out.println("  login username password");
		System.out.println("  logout");
		System.out.println("  setup");
		System.out.println("  registerCashier firstName lastName username password");
		System.out.println("  registerCustomer firstName lastName username address password");
		System.out.println("  addItem itemName categoryName unitPrice weight initialStock");
		System.out.println("  restock itemName quantity");
		System.out.println("  setCategoryDiscount categoryName discountPercent");
		System.out.println("  subscribeToPlan planName");
		System.out.println("  startCheckout customerUsername");
		System.out.println("  scanItem itemName quantity");
		System.out.println("  computeBill");
		System.out.println("  requestDelivery address");
		System.out.println("      address must be one of: near, medium, far");
		System.out.println("  pay cardNumber pin");
		System.out.println("  simulatePayment SUCCESS|INSUFFICIENT_FUNDS|PIN_WRONG|AUTH_DENIED");
		System.out.println("  showInventory");
		System.out.println("  showRevenue");
		System.out.println("  runTest testScenario-file");
		System.out.println("  help");
		System.out.println("  stop");
	}
}