package payment;

import java.util.HashMap;
import java.util.Map;

public class TAS {

	private Map<String, BankCard> registeredCards;
	private boolean connectionOpen;

	public TAS() {
		this.registeredCards = new HashMap<>();
		this.connectionOpen = false;
	}

	public void registerCard(BankCard card) {
		if (card == null) {
			throw new IllegalArgumentException("Card cant be null");
		}

		registeredCards.put(card.getCardNumber(), card);
	}

	public void openConnection() {
		connectionOpen = true;
	}

	public void closeConnection() {
		connectionOpen = false;
	}

	public BankCard findCard(String cardNumber) {
		if (cardNumber == null || cardNumber.isBlank()) {
			throw new IllegalArgumentException("Card number cant be empty");
		}

		return registeredCards.get(cardNumber);
	}

	public PaymentResult authorizeTransaction(String cardNumber, double amount) {
		if (!connectionOpen) {
			throw new IllegalStateException("TAS connection is not open.");
		}

		if (cardNumber == null || cardNumber.isBlank()) {
			throw new IllegalArgumentException("Card number cant be empty");
		}

		if (amount < 0) {
			throw new IllegalArgumentException("Amount cant be negative");
		}

		BankCard card = registeredCards.get(cardNumber);

		if (card == null) {
			return PaymentResult.CARD_NOT_FOUND;
		}

		if (!card.hasEnoughBalance(amount)) {
			return PaymentResult.INSUFFICIENT_FUNDS;
		}

		card.debit(amount);
		return PaymentResult.SUCCESS;
	}
}