package payment;

public class BankCard {
	private String cardNumber;
	private String pin;
	private double balance;
	
	public BankCard(String cardNumber, String pin, double balance) {
		if (cardNumber == null || cardNumber.isBlank()) {
			throw new IllegalArgumentException("Card number cant be empty");
		}

		if (pin == null || pin.isBlank()) {
			throw new IllegalArgumentException("PIN cant be empty");
		}

		if (balance < 0) {
			throw new IllegalArgumentException("Balance cant be negative");
		}

		this.cardNumber = cardNumber;
		this.pin = pin;
		this.balance = balance;
	}
	
	public String getCardNumber() {
		return this.cardNumber;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public boolean checkPin(String pin) {
		return this.pin.equals(pin);
	}
	
	public boolean hasEnoughBalance(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cant be negative");
		}

		return balance >= amount;
	}
	
	
	public void debit(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cant be negative");
		}

		if (!hasEnoughBalance(amount)) {
			throw new IllegalArgumentException("Not enough balance");
		}

		balance -= amount;
	}

}
