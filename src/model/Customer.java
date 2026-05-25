package model;

import delivery.DeliveryRequest;
import payment.BankCard;
import pricing.DiscountPlan;

public class Customer extends User{
	private String address;
	private DiscountPlan plan;
	private DeliveryRequest pendingDelivery;
	private BankCard bankCard;
	
	public Customer(String firstName, String lastName, String username, String password, String address) {
		super(firstName, lastName, username, password);
		this.address = address;
		this.pendingDelivery = null;
		this.bankCard = null;
			
	}
	
	public void subscribeToPlan(DiscountPlan plan) {
		this.plan = plan;
	}
	
	public boolean hasPlan() {
		return plan != null;
	}
	
	public void requestToDelivery(String address) {
		this.pendingDelivery = new DeliveryRequest(address);
	}
	
	public boolean hasPendingDelivery() {
		return pendingDelivery != null;
	}
	
	public DeliveryRequest consumePendingDelivery() {
		DeliveryRequest delivery = pendingDelivery;
        pendingDelivery = null;
        return delivery;
	}
	
	public void setBankCard(BankCard bankCard) {
		this.bankCard = bankCard;
	}
	
	public BankCard getBankCard() {
		return bankCard;
	}
	
	@Override
	public String getRole() {
		return "customer";
	}
	
	public String getAddress() {
		return address;
	}
	
	public DiscountPlan getPlan() {
		return plan;
	}
	
	public DeliveryRequest getPendingDelivery() {
		return pendingDelivery;
	}
	
	
	

}
