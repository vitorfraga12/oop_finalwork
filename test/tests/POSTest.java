package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import payment.BankCard;
import payment.POS;
import payment.PaymentResult;
import payment.SimulatedPaymentOutcome;

public class POSTest {

	@Test
	public void shouldAcceptValidPayment() {
		POS pos = new POS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		pos.registerCard(card);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.SUCCESS, result);
		assertEquals(70.0, card.getBalance());
	}

	@Test
	public void shouldRejectUnknownCard() {
		POS pos = new POS();

		PaymentResult result = pos.pay("9999", "1234", 30.0);

		assertEquals(PaymentResult.CARD_NOT_FOUND, result);
	}

	@Test
	public void shouldRejectWrongPin() {
		POS pos = new POS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		pos.registerCard(card);

		PaymentResult result = pos.pay("1111", "0000", 30.0);

		assertEquals(PaymentResult.PIN_WRONG, result);
	}

	@Test
	public void shouldRejectInsufficientFunds() {
		POS pos = new POS();
		BankCard card = new BankCard("1111", "1234", 20.0);

		pos.registerCard(card);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.INSUFFICIENT_FUNDS, result);
	}

	@Test
	public void shouldForceNextPaymentOutcome() {
		POS pos = new POS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		pos.registerCard(card);
		pos.simulateNextPayment(SimulatedPaymentOutcome.AUTH_DENIED);

		PaymentResult result = pos.pay("1111", "1234", 30.0);

		assertEquals(PaymentResult.AUTH_DENIED, result);
	}
}