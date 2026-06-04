package payment;

public class POS {

	private TAS tas;
	private SimulatedPaymentOutcome nextForcedOutcome;

	public POS(TAS tas) {
		if (tas == null) {
			throw new IllegalArgumentException("TAS cant be null");
		}

		this.tas = tas;
		this.nextForcedOutcome = SimulatedPaymentOutcome.NONE;
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
			nextForcedOutcome = SimulatedPaymentOutcome.NONE;

			if (forcedResult == PaymentResult.SUCCESS) {
				return payNormally(cardNumber, pin, amount);
			}

			return forcedResult;
		}

		return payNormally(cardNumber, pin, amount);
	}

	private PaymentResult payNormally(String cardNumber, String pin, double amount) {
		BankCard card = tas.findCard(cardNumber);

		if (card == null) {
			return PaymentResult.CARD_NOT_FOUND;
		}

		if (!card.checkPin(pin)) {
			return PaymentResult.PIN_WRONG;
		}

		tas.openConnection();

		try {
			return tas.authorizeTransaction(cardNumber, amount);
		} finally {
			tas.closeConnection();
		}
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