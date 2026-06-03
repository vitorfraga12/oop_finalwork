package payment;

import java.util.HashMap;
import java.util.Map;

public class POS {
	private Map<String, BankCard> registeredCards;
	private SimulatedPaymentOutcome nextForcedOutcome;
		
	public POS() {
		this.registeredCards = new HashMap<>();
		this.nextForcedOutcome = SimulatedPaymentOutcome.NONE;
	}
	
	public void registerCard(BankCard card) {
		if (card == null) {
			throw new IllegalArgumentException("Card cant be null");
		}
		registeredCards.put(card.getCardNumber(), card);
	}
	
	public void simulateNextPayment(SimulatedPaymentOutcome outcome) {
		if (outcome == null) {
			throw new IllegalArgumentException("Outcome cant be null");
		}

		this.nextForcedOutcome = outcome;
	}
	
	public PaymentResult pay(String cardNumber, String pin, double amount) {
		if (cardNumber == null || cardNumber.isBlank()) {
			throw new IllegalArgumentException("Card number cant be empty");
		}

		if (pin == null || pin.isBlank()) {
			throw new IllegalArgumentException("PIN cant be empty");
		}

		if (amount < 0) {
			throw new IllegalArgumentException("Amount cant be negative");
		}

		if (nextForcedOutcome != SimulatedPaymentOutcome.NONE) {
			PaymentResult forcedResult = convertForcedOutcome(nextForcedOutcome);

			if (forcedResult == PaymentResult.SUCCESS) {
				BankCard card = registeredCards.get(cardNumber);

				if (card != null && card.hasEnoughBalance(amount)) {
					card.debit(amount);
				}
			}

			nextForcedOutcome = SimulatedPaymentOutcome.NONE;
			return forcedResult;
		}

		BankCard card = registeredCards.get(cardNumber);

		if (card == null) {
			return PaymentResult.CARD_NOT_FOUND;
		}

		if (!card.checkPin(pin)) {
			return PaymentResult.PIN_WRONG;
		}

		if (!card.hasEnoughBalance(amount)) {
			return PaymentResult.INSUFFICIENT_FUNDS;
		}

		card.debit(amount);
		return PaymentResult.SUCCESS;
	}
	
	private PaymentResult convertForcedOutcome(SimulatedPaymentOutcome outcome) {
		switch (outcome) {
			case SUCCESS:
				return PaymentResult.SUCCESS;

			case INSUFFICIENT_FUNDS:
				return PaymentResult.INSUFFICIENT_FUNDS;

			case PIN_WRONG:
				return PaymentResult.PIN_WRONG;

			case AUTH_DENIED:
				return PaymentResult.AUTH_DENIED;

			case NONE:
			default:
				throw new IllegalArgumentException("Can't convert NONE forced outcome.");
		}
	}

}
