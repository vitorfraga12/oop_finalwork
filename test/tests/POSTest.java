package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import payment.BankCard;
import payment.POS;
import payment.PaymentResult;
import payment.SimulatedPaymentOutcome;
import payment.TAS;

public class POSTest {

	@Test
	public void testSuccessfulPayment() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.SUCCESS, result);
		assertEquals(70.0, card.getBalance(), 0.001);
	}

	@Test
	public void testPaymentWithWrongPin() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);

		PaymentResult result = pos.pay("1111", "0000", 30.0);

		assertEquals(PaymentResult.PIN_WRONG, result);
		assertEquals(100.0, card.getBalance(), 0.001);
	}

	@Test
	public void testPaymentWithUnknownCard() {
		TAS tas = new TAS();
		POS pos = new POS(tas);

		PaymentResult result = pos.pay("9999", "1234", 30.0);

		assertEquals(PaymentResult.CARD_NOT_FOUND, result);
	}

	@Test
	public void testPaymentWithInsufficientFunds() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 20.0);

		tas.registerCard(card);

		POS pos = new POS(tas);

		PaymentResult result = pos.pay("1111", "1234", 50.0);

		assertEquals(PaymentResult.INSUFFICIENT_FUNDS, result);
		assertEquals(20.0, card.getBalance(), 0.001);
	}

	@Test
	public void testPayWithNullCardNumberThrowsException() {
		TAS tas = new TAS();
		POS pos = new POS(tas);

		assertThrows(IllegalArgumentException.class, () -> {
			pos.pay(null, "1234", 30.0);
		});
	}

	@Test
	public void testPayWithEmptyPinThrowsException() {
		TAS tas = new TAS();
		POS pos = new POS(tas);

		assertThrows(IllegalArgumentException.class, () -> {
			pos.pay("1111", "", 30.0);
		});
	}

	@Test
	public void testPayWithNegativeAmountThrowsException() {
		TAS tas = new TAS();
		POS pos = new POS(tas);

		assertThrows(IllegalArgumentException.class, () -> {
			pos.pay("1111", "1234", -30.0);
		});
	}

	@Test
	public void testSimulatedPinWrong() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);
		pos.simulateNextPayment(SimulatedPaymentOutcome.PIN_WRONG);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.PIN_WRONG, result);
		assertEquals(100.0, card.getBalance(), 0.001);
	}

	@Test
	public void testSimulatedInsufficientFunds() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);
		pos.simulateNextPayment(SimulatedPaymentOutcome.INSUFFICIENT_FUNDS);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.INSUFFICIENT_FUNDS, result);
		assertEquals(100.0, card.getBalance(), 0.001);
	}

	@Test
	public void testSimulatedAuthDenied() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);
		pos.simulateNextPayment(SimulatedPaymentOutcome.AUTH_DENIED);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.AUTH_DENIED, result);
		assertEquals(100.0, card.getBalance(), 0.001);
	}

	@Test
	public void testSimulatedSuccessUsesNormalPaymentFlow() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);
		pos.simulateNextPayment(SimulatedPaymentOutcome.SUCCESS);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.SUCCESS, result);
		assertEquals(70.0, card.getBalance(), 0.001);
	}

	@Test
	public void testSimulationAppliesOnlyToNextPayment() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		POS pos = new POS(tas);

		pos.simulateNextPayment(SimulatedPaymentOutcome.PIN_WRONG);

		PaymentResult firstResult = pos.pay("1111", "1234", 30.0);
		PaymentResult secondResult = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.PIN_WRONG, firstResult);
		assertEquals(PaymentResult.SUCCESS, secondResult);
		assertEquals(70.0, card.getBalance(), 0.001);
	}

	@Test
	public void testPOSConstructorWithNullTASThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			new POS(null);
		});
	}

	@Test
	public void testSimulateNullOutcomeThrowsException() {
		TAS tas = new TAS();
		POS pos = new POS(tas);

		assertThrows(IllegalArgumentException.class, () -> {
			pos.simulateNextPayment(null);
		});
	}
}