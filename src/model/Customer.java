package model;

import delivery.DeliveryRequest;
import payment.BankCard;
import pricing.CostumerPlan;
import pricing.NormalPlan;

public class Customer extends User{
	private String address;
	private CostumerPlan plan;
	private DeliveryRequest waitingDelivery;
	private int id;
	private static int ID = 0;
	
	public Customer(String firstName, String lastName, String username, String password, String address) {
		super(firstName, lastName, username, password);
		this.address = address;
		this.waitingDelivery = null;
		ID+=1;
		this.id=ID;
		this.plan = new NormalPlan();
	}
	
	public int getId() {
		return id;
	}
	
	public void subscribeToPlan(CostumerPlan plan) {
		if (plan == null) {
			throw new IllegalArgumentException("Plan cannot be null.");
		}

		this.plan = plan;
	}
	
	public boolean hasPlan() {
		return plan != null;
	}
	
	public CostumerPlan getPlan() {
	    return plan;
	}
	
	public void requestDelivery(DeliveryRequest deliveryRequest) {
		if (deliveryRequest == null) {
			throw new IllegalArgumentException("Delivery request cannot be null.");
		}

		this.waitingDelivery = deliveryRequest;
	}
	
	public boolean hasWaitingDelivery() {
		return waitingDelivery != null;
	}
	
	public DeliveryRequest consumeWaitingDelivery() {
		DeliveryRequest delivery = waitingDelivery;
		this.waitingDelivery = null;
        return delivery;
	}
	
	public DeliveryRequest getWaitingDelivery() {
		return waitingDelivery;
	}
	
	
	

}
