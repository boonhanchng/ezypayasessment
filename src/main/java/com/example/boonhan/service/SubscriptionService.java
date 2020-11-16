package com.example.boonhan.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.boonhan.enums.SubscriptionType;
import com.example.boonhan.model.SubscriptionRequest;

@Service
public class SubscriptionService {
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public List<String> generateInvoiceDateList(SubscriptionRequest request){
		LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
		LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
		SubscriptionType type = SubscriptionType.valueOf(request.getSubscriptionType());
		if(type.equals(SubscriptionType.DAILY)) {
			List<String> dates = Stream.iterate(startDate, date -> date.plusDays(1))
				    .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
				    .map(date -> formatter.format(date))
				    .collect(Collectors.toList());
			return dates;
		}else if(type.equals(SubscriptionType.WEEKLY)) {
			List<LocalDate> dates = new ArrayList<>();
			LocalDate nextOrSameDay = startDate.with ( TemporalAdjusters.nextOrSame ( DayOfWeek.valueOf(request.getDayOfWeek()) ) );
			while ( ( null != nextOrSameDay ) & (  ! nextOrSameDay.isAfter ( endDate ) ) ) {
			    dates.add ( nextOrSameDay ); 
			    nextOrSameDay = nextOrSameDay.plusWeeks ( 1 ); 
			}
			return dates.stream()
					.map(date -> formatter.format(date))
					.collect(Collectors.toList());
		}else  if(type.equals(SubscriptionType.MONTHLY)) {
			List<LocalDate> dates = new ArrayList<>();
			YearMonth month = YearMonth.from(startDate);
			LocalDate firstDate = month.atDay(Integer.parseInt(request.getDayOfMonth()));
			LocalDate nextOrSameDate = firstDate;
			int i =1;
			while ( ( null != nextOrSameDate ) & (  ! nextOrSameDate.isAfter ( endDate ) ) ) {
			    dates.add ( nextOrSameDate ); 
			    nextOrSameDate = firstDate.plusMonths(i++); 
			}
			return dates.stream()
					.map(date -> formatter.format(date))
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
}
