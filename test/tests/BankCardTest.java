package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import payment.BankCard;

public class BankCardTest {

	@Test
	public void shouldCheckCorrectPin() {
		BankCard card = new BankCard("1111", "1234", 100.0);

		assertTrue(card.checkPin("1234"));
	}

	@Test
	public void shouldRejectWrongPin() {
		BankCard card = new BankCard("1111", "1234", 100.0);

		assertFalse(card.checkPin("0000"));
	}

	@Test
	public void shouldDebitAmount() {
		BankCard card = new BankCard("1111", "1234", 100.0);

		card.debit(30.0);

		assertEquals(70.0, card.getBalance());
	}

	@Test
	public void shouldRejectDebitWhenBalanceIsInsufficient() {
		BankCard card = new BankCard("1111", "1234", 20.0);

		assertThrows(IllegalArgumentException.class, () -> {
			card.debit(30.0);
		});
	}

	@Test
	public void shouldRejectNegativeBalance() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BankCard("1111", "1234", -10.0);
		});
	}
}