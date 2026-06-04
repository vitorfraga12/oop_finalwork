package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import delivery.DeliveryRequest;

public class DeliveryRequestTest {

	@Test
	public void shouldCreateDeliveryRequest() {
		DeliveryRequest request = new DeliveryRequest("near", 5.0);

		assertEquals("near", request.getAddress());
		assertEquals(5.0, request.getDistanceKm());
	}

	@Test
	public void shouldRejectEmptyAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			new DeliveryRequest("", 5.0);
		});
	}

	@Test
	public void shouldRejectNegativeDistance() {
		assertThrows(IllegalArgumentException.class, () -> {
			new DeliveryRequest("near", -1.0);
		});
	}
}