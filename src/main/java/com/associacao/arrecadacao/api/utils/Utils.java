package com.associacao.arrecadacao.api.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
	
	public static String dateFormat(Date date) {
		
		LocalDate dateFormat = convertToLocalDate(date);
		DateTimeFormatter formatador = 
		  DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return dateFormat.format(formatador);
	}
	
	public static LocalDate convertToLocalDate(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime())
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
}
