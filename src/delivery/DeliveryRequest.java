package delivery;

public class DeliveryRequest {
	private String address;
	private double distanceKm;

	public DeliveryRequest(String address, double distanceKm) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("Address cannot be empty.");
		}

		if (distanceKm < 0) {
			throw new IllegalArgumentException("Distance cannot be negative.");
		}

		this.address = address;
		this.distanceKm = distanceKm;
	}

	public String getAddress() {
		return address;
	}

	public double getDistanceKm() {
		return distanceKm;
	}

}
