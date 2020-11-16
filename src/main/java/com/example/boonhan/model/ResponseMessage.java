package com.example.boonhan.model;

import java.util.ArrayList;
import java.util.List;

import com.example.boonhan.enums.SubscriptionType;

public class ResponseMessage {
	private boolean success = false;
	private String message="";
	private double amount;
	private SubscriptionType subscriptionType = SubscriptionType.NOT_APPLICABLE;
	private List<String> invoiceDates = new ArrayList<String>();
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public SubscriptionType getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(SubscriptionType subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public List<String> getInvoiceDates() {
		return invoiceDates;
	}
	public void setInvoiceDates(List<String> invoiceDates) {
		this.invoiceDates = invoiceDates;
	}
	
}
