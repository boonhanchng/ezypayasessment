package com.example.boonhan.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.boonhan.enums.SubscriptionType;
import com.example.boonhan.model.ResponseMessage;
import com.example.boonhan.model.SubscriptionRequest;
import com.example.boonhan.service.SubscriptionService;

@RestController
public class SubscriptionController {
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	@Autowired
	private SubscriptionService subscriptionService;
	
	
	@PostMapping("/subscription")
	public ResponseMessage subscription(@RequestBody SubscriptionRequest request) {
		ResponseMessage response = new ResponseMessage();
		try {
			response.setAmount(Double.parseDouble(request.getAmount()));
			response.setSuccess(true);
		}catch(Exception e) {
			response.setMessage("Invalid value for amount. Only NUMERIC allowed. ");
		}
		
		if(response.isSuccess()) {
			try {
				response.setSubscriptionType(SubscriptionType.valueOf(request.getSubscriptionType()));
				response.setSuccess(true);
			}catch(Exception e) {
				response.setSuccess(false);
				response.setMessage("Invalid value for subscriptionType. eg: DAILY, WEEKLY, MONTHLY");
			}			
		}
		
		if(response.isSuccess()) {
			if(response.getSubscriptionType().equals(SubscriptionType.DAILY)) {
				//Do nothing
			}else if(response.getSubscriptionType().equals(SubscriptionType.WEEKLY)) {
				try {
					DayOfWeek.valueOf(request.getDayOfWeek());
				}catch(Exception e) {
					response.setSuccess(false);
					response.setMessage("Invalid value for dayOfWeek. eg: TUESDAY");
				}
			}else if(response.getSubscriptionType().equals(SubscriptionType.MONTHLY)) {
				try {
					int date = Integer.parseInt(request.getDayOfMonth());
					if(date<1 || date>31) {
						response.setSuccess(false);
						response.setMessage("Invalid value for dayOfMonth. eg: 20");
					}
				}catch(Exception e) {
					response.setSuccess(false);
					response.setMessage("Invalid value for dayOfMonth. eg: 20");
				}
			}
		}
		if(response.isSuccess()) {
			LocalDate startDate = null;
			LocalDate endDate = null;
			try {
				startDate = LocalDate.parse(request.getStartDate(), formatter);
			}catch(Exception e) {
				response.setSuccess(false);
				response.setMessage("Invalid value for startDate. (dd/MM/yyyy)");				
			}
			if(response.isSuccess()) {
				try {
					endDate = LocalDate.parse(request.getEndDate(), formatter);
				}catch(Exception e) {
					response.setSuccess(false);
					response.setMessage("Invalid value for endDate. (dd/MM/yyyy)");				
				}
			}
			if(startDate!=null && endDate!=null) {
				Period diff = Period.between(startDate,endDate.plusDays(1));
				if(diff.getMonths()>3) {
					response.setSuccess(false);
					response.setMessage("Maximum duration of subscriptions is 3 months. ");		
				}
			}
		}
		if(response.isSuccess()) {
			List<String> invoiceDates = subscriptionService.generateInvoiceDateList(request);
			response.setInvoiceDates(invoiceDates);
			response.setSuccess(true);
		}
		return response;
	}
}
