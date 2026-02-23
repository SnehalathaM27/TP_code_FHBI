package com.tripgain.common;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

//public class GenerateDates {
//	public static String[] GenerateDatesToSelectFlights() {
//	    LocalDate today = LocalDate.now();
//	    LocalDate futureDate = today.plusDays(8);
//	    LocalDate returnFutureDate = today.plusDays(10); // Add 5 days
//	    LocalDate inPolicyFlights = today.plusDays(10); // Add 5 days
//	    LocalDate diffdateFlights = today.plusDays(15); // Add 5 days
//
//	    LocalDate outOfPolicyFlights = today.plusDays(2); // Add 5 days
//	    
//	    LocalDate after15daysFlights = today.plusDays(20);
//
//	    // Get the day of the week and format it
//	    String dayOfWeekShort = returnFutureDate.getDayOfWeek()
//	            .getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH);
//
//	    int day = futureDate.getDayOfMonth();// e.g., 13
//	    String fromDate = String.valueOf(day);
//	    int returnDay = returnFutureDate.getDayOfMonth();// e.g., 13
//	    String returnDate = String.valueOf(returnDay);
//	    int inPolicyDay = inPolicyFlights.getDayOfMonth();// e.g., 13
//	    int diffdateFlightsDay = diffdateFlights.getDayOfMonth();// e.g., 13
//	    String inPolicyDate = String.valueOf(inPolicyDay);
//
//	    String diffdateFlightsDate = String.valueOf(diffdateFlightsDay);
//	   	    int outOfPolicyDay = outOfPolicyFlights.getDayOfMonth();// e.g., 13
//	   	    
//	   	    
//	   	    int after15daysFlightsDay = after15daysFlights.getDayOfMonth();// e.g., 13
//
//	    
//	    
//	    String outOfPolicyDate = String.valueOf(outOfPolicyDay);
//	    
//	    String after15daysFlightsDate = String.valueOf(after15daysFlightsDay);
//
//
//	    String month = futureDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // e.g., "May"
//	    int year = futureDate.getYear();
//	    String fromYear = String.valueOf(year);
//	    String returnMonth = returnFutureDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // e.g., "May"
//	    int returnYear = returnFutureDate.getYear();
//	    String returnYears = String.valueOf(returnYear);
//	    String inPolicyMonth = inPolicyFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // e.g., "May"
//	    int inPolicyYear = inPolicyFlights.getYear();
//	    String inPolicyYears = String.valueOf(inPolicyYear);
//	    String outOfPolicyMonth = outOfPolicyFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // e.g., "May"
//	    int outOfPolicyYear = outOfPolicyFlights.getYear();
//	    String outOfPolicyYears = String.valueOf(outOfPolicyYear);
//	    
//	    String after15daysFlightsMonth = after15daysFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // e.g., "May"
//	    int after15daysFlightsYear = after15daysFlights.getYear();
//	    String after15daysFlightsYears = String.valueOf(after15daysFlightsYear);
//	    
//
//	    String fromMonthYear = month + " " + year;
//	    String returnMonthYear = returnMonth + " " + returnYear;
//	    String inPolicyMonthYear = inPolicyMonth + " " + inPolicyYears;
//	    String outOfPolicyMonthYear = outOfPolicyMonth + " " + outOfPolicyYears;
//	    
//	    String returnafter15daysFlightsMonthYear = after15daysFlightsMonth + " " + after15daysFlightsYears;
//
//
//
//	    return new String[] {fromDate,returnDate,fromMonthYear, returnMonthYear,month,fromYear,returnMonth,returnYears,dayOfWeekShort,inPolicyDate,inPolicyMonthYear,outOfPolicyDate,outOfPolicyMonthYear,diffdateFlightsDate,after15daysFlightsDate,returnafter15daysFlightsMonthYear};
//
//	}


public class GenerateDates {

	public static String[] GenerateDatesToSelectFlights() {
	    LocalDate today = LocalDate.now();

	    // Your existing date logic
	    LocalDate futureDate = today.plusDays(8); // selected date
	    LocalDate returnFutureDate = today.plusDays(9); // 1-day gap return date (e.g., 8–9)
	    LocalDate inPolicyFlights = today.plusDays(10);
	    LocalDate diffdateFlights = today.plusDays(15);
	    LocalDate outOfPolicyFlights = today.plusDays(12);
	    LocalDate after15daysFlights = today.plusDays(20);

	    // ✅ New dynamic selection dates
	    LocalDate selectAfter7Days = today.plusDays(8);  // 8th day from today
	    LocalDate selectAfter15Days = today.plusDays(16); // 16th day from today

	    // Get day names and details
	    String dayOfWeekShort = returnFutureDate.getDayOfWeek()
	            .getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH);

	    // Convert to day values
	    String fromDate = String.valueOf(futureDate.getDayOfMonth());
	    String returnDate = String.valueOf(returnFutureDate.getDayOfMonth());
	    String inPolicyDate = String.valueOf(inPolicyFlights.getDayOfMonth());
	    String diffdateFlightsDate = String.valueOf(diffdateFlights.getDayOfMonth());
	    String outOfPolicyDate = String.valueOf(outOfPolicyFlights.getDayOfMonth());
	    String after15daysFlightsDate = String.valueOf(after15daysFlights.getDayOfMonth());

	    // ✅ New selection days
	    String selectAfter7DaysDate = String.valueOf(selectAfter7Days.getDayOfMonth());
	    String selectAfter15DaysDate = String.valueOf(selectAfter15Days.getDayOfMonth());

	    // Month and Year details
	    String month = futureDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String fromYear = String.valueOf(futureDate.getYear());

	    String returnMonth = returnFutureDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String returnYears = String.valueOf(returnFutureDate.getYear());

	    String inPolicyMonth = inPolicyFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String inPolicyYears = String.valueOf(inPolicyFlights.getYear());

	    String diffdateFlightsMonth = diffdateFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String diffdateFlightsYears = String.valueOf(diffdateFlights.getYear());

	    String outOfPolicyMonth = outOfPolicyFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String outOfPolicyYears = String.valueOf(outOfPolicyFlights.getYear());

	    String after15daysFlightsMonth = after15daysFlights.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String after15daysFlightsYears = String.valueOf(after15daysFlights.getYear());

	    // ✅ New selection month-year
	    String selectAfter7DaysMonthYear = selectAfter7Days.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
	                                        + " " + selectAfter7Days.getYear();
	    String selectAfter15DaysMonthYear = selectAfter15Days.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
	                                         + " " + selectAfter15Days.getYear();

	    // Month-Year combinations
	    String fromMonthYear = month + " " + fromYear;
	    String returnMonthYear = returnMonth + " " + returnYears;
	    String inPolicyMonthYear = inPolicyMonth + " " + inPolicyYears;
	    String outOfPolicyMonthYear = outOfPolicyMonth + " " + outOfPolicyYears;
	    String returnafter15daysFlightsMonthYear = after15daysFlightsMonth + " " + after15daysFlightsYears;

	    // Print for reference
	    System.out.println("Selected Date: " + fromDate + " " + fromMonthYear);
	    System.out.println("Return Date (1-day gap): " + returnDate + " " + returnMonthYear);
	    System.out.println("Select After 7 Days: " + selectAfter7DaysDate + " " + selectAfter7DaysMonthYear);
	    System.out.println("Select After 15 Days: " + selectAfter15DaysDate + " " + selectAfter15DaysMonthYear);

	    return new String[] {
	        fromDate,                       // Departure date
	        returnDate,                     // Return date (1-day gap)
	        fromMonthYear,                  // Departure Month-Year
	        returnMonthYear,                // Return Month-Year
	        month, 
	        fromYear,
	        returnMonth, 
	        returnYears,
	        dayOfWeekShort,
	        inPolicyDate, inPolicyMonthYear,
	        outOfPolicyDate, outOfPolicyMonthYear,
	        diffdateFlightsDate,
	        after15daysFlightsDate,
	        returnafter15daysFlightsMonthYear,
	        // ✅ New selection dates
	        selectAfter7DaysDate,
	        selectAfter7DaysMonthYear,
	        selectAfter15DaysDate,
	        selectAfter15DaysMonthYear
	    };
	}

    

    // Example test
    public static void main(String[] args) {
        String[] dates = GenerateDatesToSelectFlights();
        for (String d : dates) {
            System.out.println(d);
        }
    }
}



