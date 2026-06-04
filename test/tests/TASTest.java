package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import payment.BankCard;
import payment.PaymentResult;
import payment.TAS;

public class TASTest {
	@Test
	public void testRegisterAndFindCard() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		assertEquals(card, tas.findCard("1111"));
	}

	@Test
	public void testFindUnknownCardReturnsNull() {
		TAS tas = new TAS();

		assertNull(tas.findCard("9999"));
	}

	@Test
	public void testRegisterNullCardThrowsException() {
		TAS tas = new TAS();

		assertThrows(IllegalArgumentException.class, () -> {
			tas.registerCard(null);
		});
	}

	@Test
	public void testAuthorizeWithoutOpenConnectionThrowsException() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);

		assertThrows(IllegalStateException.class, () -> {
			tas.authorizeTransaction("1111", 20.0);
		});
	}

	@Test
	public void testAuthorizeSuccessfulTransactionDebitsCard() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 100.0);

		tas.registerCard(card);
		tas.openConnection();

		PaymentResult result = tas.authorizeTransaction("1111", 30.0);

		tas.closeConnection();

		assertEquals(PaymentResult.SUCCESS, result);
		assertEquals(70.0, card.getBalance(), 0.001);
	}

	@Test
	public void testAuthorizeUnknownCardReturnsCardNotFound() {
		TAS tas = new TAS();

		tas.openConnection();

		PaymentResult result = tas.authorizeTransaction("9999", 30.0);

		tas.closeConnection();

		assertEquals(PaymentResult.CARD_NOT_FOUND, result);
	}

	@Test
	public void testAuthorizeInsufficientFundsReturnsInsufficientFunds() {
		TAS tas = new TAS();
		BankCard card = new BankCard("1111", "1234", 20.0);

		tas.registerCard(card);
		tas.openConnection();

		PaymentResult result = tas.authorizeTransaction("1111", 50.0);

		tas.closeConnection();

		assertEquals(PaymentResult.INSUFFICIENT_FUNDS, result);
		assertEquals(20.0, card.getBalance(), 0.001);
	}

	@Test
	public void testAuthorizeNegativeAmountThrowsException() {
		TAS tas = new TAS();

		tas.openConnection();

		assertThrows(IllegalArgumentException.class, () -> {
			tas.authorizeTransaction("1111", -10.0);
		});

		tas.closeConnection();
	}
}
