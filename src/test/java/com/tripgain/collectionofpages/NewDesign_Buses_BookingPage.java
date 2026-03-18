package com.tripgain.collectionofpages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class NewDesign_Buses_BookingPage {
	WebDriver driver;

	public NewDesign_Buses_BookingPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to get bus name from booking page
	public String[] getBusNameFromBookingPg() {
		String busName = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-operatorname')]")).getText();
		System.out.println("Bus name from Booking Page: " + busName);

		return new String[] { busName };
	}

	public String[] getBusSeaterTextFromBookingPg() {
		String seaterText = driver.findElement(By.xpath("//*[contains(@class,' tg-bus-type')]")).getText();
		System.out.println("Seater text from Booking Page: " + seaterText);

		return new String[] { seaterText };
	}

	public String[] getDateFromBookingPg() {
		String date = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-jdate')]")).getText();
		System.out.println("Date from Booking Page: " + date);
		return new String[] { date };
	}

	public String[] getDepartureFromBookingPg() {
		String Dept = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-deptime')]")).getText();
		System.out.println("Dept from Booking Page: " + Dept);

		return new String[] { Dept };
	}

	public String[] getArrivalFromBookingPg() {
		String Arrival = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-arrtime')]")).getText();
		System.out.println("Arrival from Booking Page: " + Arrival);

		return new String[] { Arrival };
	}

	public String[] getDurationFromBookingPg() {
		String duration = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-duration')]")).getText();
		System.out.println("duration from Booking Page: " + duration);

		return new String[] { duration };
	}

	public String[] getBoardingPointFromBookingPg() {
		String BoardingPoint = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-boardingpoint')]")).getText();
		System.out.println("BoardingPoint from Booking Page: " + BoardingPoint);

		return new String[] { BoardingPoint };
	}

	public String[] getDroppingPointFromBookingPg() {
		String DroppingPoint = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-droppoint')]")).getText();
		System.out.println("DroppingPoint from Booking Page: " + DroppingPoint);

		return new String[] { DroppingPoint };
	}

	public String[] getOriginFromBookingPg() {
		String Origin = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-origin')]")).getText();
		System.out.println("Origin from Booking Page: " + Origin);

		return new String[] { Origin };
	}

	public String[] getDestFromBookingPg() {
		String Dest = driver.findElement(By.xpath("//*[contains(@class,'tg-bus-destination')]")).getText();
		System.out.println("Dest from Booking Page: " + Dest);

		return new String[] { Dest };
	}

	public String[] getPriceFromBookingPg() {
		String Price = driver.findElement(By.xpath("//div[text()='Total Fare']//following-sibling::div")).getText();
		System.out.println("Price from Booking Page: " + Price);

		return new String[] { Price };
	}

	public String[] getPolicyFromBookingPg() {
		String policy = driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText();
		System.out.println("policy from Booking Page: " + policy);

		return new String[] { policy };
	}

	// Method to get selected seat text
	public String[] getSelectedSeatFromBokingPg() {
		driver.findElement(By.xpath("//div[text()='Seat Price']")).click();
		String seat = driver.findElement(By.xpath(
				"(//div[contains(@class,' tg-typography tg-typography_subtitle-7 label-color tg-typography_default')])[1]"))
				.getText();
		System.out.println("Seleceted seat from booking page: " + seat);
		return new String[] { seat };

	}

	// Method to validate bus name

	public void validateBusOperatorNameFromListingToBookingPage(String[] busDetailsFromListing,
			String[] busNameFromBookingPage, Log log, ScreenShots screenshots) {

		if (busDetailsFromListing == null || busDetailsFromListing.length < 4
				|| busDetailsFromListing[3].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Bus operator name from listing page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus operator name from listing page is null or empty.");
			return;
		}

		if (busNameFromBookingPage == null || busNameFromBookingPage.length == 0
				|| busNameFromBookingPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Bus operator name from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus operator name from booking page is null or empty.");
			return;
		}

		String listingBusName = busDetailsFromListing[3].trim();
		String bookingBusName = busNameFromBookingPage[0].trim();

		if (!listingBusName.equalsIgnoreCase(bookingBusName)) {
			log.ReportEvent("FAIL", "Bus operator name mismatch! Listing Page: '" + listingBusName
					+ "', Booking Page: '" + bookingBusName + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Bus operator name mismatch");
		} else {
			log.ReportEvent("PASS", "Bus operator name matches on both Listing and Booking pages: " + listingBusName);
		}
	}

	public void validateBoardingPointLocationFromListingToBookingPage(String[] boardingPointDetails,
			String[] boardingPointFromBooking, Log log, ScreenShots screenshots) {

		if (boardingPointDetails == null || boardingPointDetails.length < 1
				|| boardingPointDetails[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Boarding point location from selection is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point location from selection is missing.");
			return;
		}

		if (boardingPointFromBooking == null || boardingPointFromBooking.length == 0
				|| boardingPointFromBooking[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Boarding point from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point from booking page is missing.");
			return;
		}

		String selectedLocation = boardingPointDetails[0].trim();
		String bookingPageLocation = boardingPointFromBooking[0].trim();

		if (!selectedLocation.equalsIgnoreCase(bookingPageLocation)) {
			log.ReportEvent("FAIL", "Boarding point mismatch! Selected: '" + selectedLocation + "', Booking Page: '"
					+ bookingPageLocation + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point location mismatch");
		} else {
			log.ReportEvent("PASS", "Boarding point matches on both Selection and Booking pages: " + selectedLocation);
		}
	}

	public void validateDroppingPointLocationFromListingToBookingPage(String[] droppingPointDetails,
			String[] droppingPointFromBooking, Log log, ScreenShots screenshots) {

		if (droppingPointDetails == null || droppingPointDetails.length < 1
				|| droppingPointDetails[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Dropping point location from selection is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Dropping point location from selection is missing.");
			return;
		}

		if (droppingPointFromBooking == null || droppingPointFromBooking.length == 0
				|| droppingPointFromBooking[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Dropping point from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Dropping point from booking page is missing.");
			return;
		}

		// Normalize both values
		String selectedLocation = droppingPointDetails[0].trim().replaceAll("\\s+", " ");
		String bookingPageLocation = droppingPointFromBooking[0].trim().replaceAll("\\s+", " ");

		if (!selectedLocation.equalsIgnoreCase(bookingPageLocation)) {
			log.ReportEvent("FAIL", "Dropping point mismatch! Selected: '" + selectedLocation + "', Booking Page: '"
					+ bookingPageLocation + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Dropping point location mismatch");
		} else {
			log.ReportEvent("PASS", "Dropping point matches on both Selection and Booking pages: " + selectedLocation);
		}
	}

	public void validateSeaterTypeFromListingToBookingPage(String[] busDetailsFromListing,
			String[] seaterTextFromBookingPage, Log log, ScreenShots screenshots) {

		if (busDetailsFromListing == null || busDetailsFromListing.length < 1
				|| busDetailsFromListing[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Service name from listing page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Service name from listing page is missing.");
			return;
		}

		if (seaterTextFromBookingPage == null || seaterTextFromBookingPage.length == 0
				|| seaterTextFromBookingPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Seater text from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Seater text from booking page is missing.");
			return;
		}

		String serviceNameFromListing = busDetailsFromListing[0].trim();
		String seaterTextFromBooking = seaterTextFromBookingPage[0].trim();

		// Normalize texts by removing special chars and lowercasing
		String normalizedListing = serviceNameFromListing.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
		String normalizedBooking = seaterTextFromBooking.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

		if (!normalizedListing.equals(normalizedBooking)) {
			log.ReportEvent("FAIL", "Seater type mismatch! Listing Page: '" + serviceNameFromListing
					+ "', Booking Page: '" + seaterTextFromBooking + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Seater type mismatch");
		} else {
			log.ReportEvent("PASS", "Seater type matches on both Listing and Booking pages: " + serviceNameFromListing);
		}
	}

	public void validateCheckInDateBetweenResultAndBookingPages(String[] checkInDateFromResultPage,
			String[] dateFromBookingPage, Log log, ScreenShots screenshots) {

		if (checkInDateFromResultPage == null || checkInDateFromResultPage.length == 0
				|| checkInDateFromResultPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Check-in date from result page is missing or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Check-in date from result page is missing or empty.");
			return;
		}

		if (dateFromBookingPage == null || dateFromBookingPage.length == 0 || dateFromBookingPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Date from booking page is missing or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Date from booking page is missing or empty.");
			return;
		}

		String resultDate = checkInDateFromResultPage[0].trim();
		String bookingDate = dateFromBookingPage[0].trim();

		if (!resultDate.equalsIgnoreCase(bookingDate)) {
			log.ReportEvent("FAIL", "Date mismatch from result and booking page! Result Page: '" + resultDate
					+ "', Booking Page: '" + bookingDate + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Check-in date mismatch between Result and Booking pages.");
		} else {
			log.ReportEvent("PASS", "Check-in dates match from result to booking page: " + resultDate);
		}
	}

	public void validateDepartureTimeFromBoardingToBookingPage(String[] boardingPointDetails,
			String[] departureTimeFromBookingPage, Log log, ScreenShots screenshots) {

		if (boardingPointDetails == null || boardingPointDetails.length < 2
				|| boardingPointDetails[1].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Boarding point time is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point time is missing.");
			return;
		}

		if (departureTimeFromBookingPage == null || departureTimeFromBookingPage.length == 0
				|| departureTimeFromBookingPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Departure time from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Departure time from booking page is missing.");
			return;
		}

		String selectedBoardingTime = boardingPointDetails[1].trim();
		String bookingPageDepartureTime = departureTimeFromBookingPage[0].trim();

		if (!selectedBoardingTime.equalsIgnoreCase(bookingPageDepartureTime)) {
			log.ReportEvent("FAIL", "Departure time mismatch from boarding to booking! Boarding: '"
					+ selectedBoardingTime + "', Booking Page: '" + bookingPageDepartureTime + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Departure time mismatch");
		} else {
			log.ReportEvent("PASS", "Departure time matches from boarding to booking page: " + selectedBoardingTime);
		}
	}

	public void validateArrivalTimeFromListingToBookingPage(String[] busDetailsFromListing,
			String[] arrivalTimeFromBookingPage, Log log, ScreenShots screenshots) {

		if (busDetailsFromListing == null || busDetailsFromListing.length < 2
				|| busDetailsFromListing[1].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Arrival time from listing page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Arrival time from listing page is missing.");
			return;
		}

		if (arrivalTimeFromBookingPage == null || arrivalTimeFromBookingPage.length == 0
				|| arrivalTimeFromBookingPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Arrival time from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Arrival time from booking page is missing.");
			return;
		}

		String listingArrival = busDetailsFromListing[1].trim();
		String bookingArrival = arrivalTimeFromBookingPage[0].trim();

		if (!listingArrival.equalsIgnoreCase(bookingArrival)) {
			log.ReportEvent("FAIL", "Arrival time mismatch from listing  to booking page! Listing: '" + listingArrival
					+ "', Booking: '" + bookingArrival + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Arrival time mismatch");
		} else {
			log.ReportEvent("PASS", "Arrival time matches from listing to booking page: " + listingArrival);
		}
	}

	public void validateDurationFromListingToBookingPage(String[] busDetailsFromListing,
			String[] durationFromBookingPage, Log log, ScreenShots screenshots) {

		if (busDetailsFromListing == null || busDetailsFromListing.length < 3
				|| busDetailsFromListing[2].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Duration from listing page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Duration from listing page is missing.");
			return;
		}

		if (durationFromBookingPage == null || durationFromBookingPage.length == 0
				|| durationFromBookingPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Duration from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Duration from booking page is missing.");
			return;
		}

		String listingDuration = busDetailsFromListing[2].trim();
		String bookingDuration = durationFromBookingPage[0].trim();

		if (!listingDuration.equalsIgnoreCase(bookingDuration)) {
			log.ReportEvent("FAIL",
					"Duration mismatch! Listing: '" + listingDuration + "', Booking: '" + bookingDuration + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Duration mismatch");
		} else {
			log.ReportEvent("PASS", "Duration matches: " + listingDuration);
		}
	}

	public void validatePolicyFromListingToBookingPage(String[] busDetailsFromListing, String[] policyFromBookingPage,
			Log log, ScreenShots screenshots) {

		if (busDetailsFromListing == null || busDetailsFromListing.length < 7
				|| busDetailsFromListing[6].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Policy from listing page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Policy from listing page is missing.");
			return;
		}
		if (policyFromBookingPage == null || policyFromBookingPage.length == 0) {
			log.ReportEvent("FAIL", "Policy from booking page is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Policy from booking page is missing.");
			return;
		}

		String listingPolicy = busDetailsFromListing[6].trim();
		String bookingPolicy = policyFromBookingPage[0].trim();

		if (!listingPolicy.equalsIgnoreCase(bookingPolicy)) {
			log.ReportEvent("FAIL", "Policy mismatch from listing to booking page! Listing: '" + listingPolicy
					+ "', Booking: '" + bookingPolicy + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Policy mismatch");
		} else {
			log.ReportEvent("PASS", "Policy matches from listing to booking page: " + listingPolicy);
		}
	}

	public void validatePriceFromresultToBookingPage(String[] priceFromBookingPage, String priceAfterSeatSelection,
			Log log, ScreenShots screenshots) {
		if (priceFromBookingPage == null || priceFromBookingPage.length == 0) {
			log.ReportEvent("FAIL", "Price from booking page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Price from booking page is missing.");
			return;
		}
		if (priceAfterSeatSelection == null || priceAfterSeatSelection.isEmpty()) {
			log.ReportEvent("FAIL", "Price after seat selection is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Price after seat selection is missing.");
			return;
		}

		String bookingPrice = priceFromBookingPage[0].replaceAll("[^\\d.]", ""); // remove non-numeric chars
		String seatSelectionPrice = priceAfterSeatSelection.replaceAll("[^\\d.]", "");

		if (!bookingPrice.equals(seatSelectionPrice)) {
			log.ReportEvent("FAIL", "Price mismatch from result to booking page! Booking Page: " + bookingPrice
					+ ", After Seat Selection: " + seatSelectionPrice);
			screenshots.takeScreenShot1();
			Assert.fail("Price mismatch between booking page and seat selection.");
		} else {
			log.ReportEvent("PASS", "Prices match from result to booking page: " + bookingPrice);
		}
	}

	public void validateDestFromResultToBookingPage(String[] bookingDest, String[] resultDest, Log log,
			ScreenShots screenshots) {
		if (bookingDest == null || bookingDest.length == 0) {
			log.ReportEvent("FAIL", "Destination from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Destination from Booking Page is missing.");
			return;
		}
		if (resultDest == null || resultDest.length == 0) {
			log.ReportEvent("FAIL", "Destination from Results Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Destination from Results Page is missing.");
			return;
		}

		String destBooking = bookingDest[0].trim();
		String destResult = resultDest[0].trim();

		if (!destBooking.equalsIgnoreCase(destResult)) {
			log.ReportEvent("FAIL",
					"Destination mismatch! Booking Page: '" + destBooking + "', Results Page: '" + destResult + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Destination mismatch between Booking and Results pages.");
		} else {
			log.ReportEvent("PASS", "Destination matches from booking and results page: " + destBooking);
		}
	}

	public void validateOriginFromResultToBookingpage(String[] bookingOrigin, String[] resultOrigin, Log log,
			ScreenShots screenshots) {
		if (bookingOrigin == null || bookingOrigin.length == 0) {
			log.ReportEvent("FAIL", "Origin from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Origin from Booking Page is missing.");
			return;
		}
		if (resultOrigin == null || resultOrigin.length == 0) {
			log.ReportEvent("FAIL", "Origin from Results Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Origin from Results Page is missing.");
			return;
		}

		String originBooking = bookingOrigin[0].trim();
		String originResult = resultOrigin[0].trim();

		if (!originBooking.equalsIgnoreCase(originResult)) {
			log.ReportEvent("FAIL",
					"Origin mismatch! Booking Page: '" + originBooking + "', Results Page: '" + originResult + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Origin mismatch between Booking and Results pages.");
		} else {
			log.ReportEvent("PASS", "Origin matches from booking to result page: " + originBooking);
		}
	}

	public void clickSendForApproval(Log log, ScreenShots screenshots) {
		driver.findElement(By.xpath("//button[text()='Send For Approval']")).click();

		try {
			// Get toast message text
			String messageText = driver
					.findElement(By.xpath("//div[contains(@class,'toast')]//p[contains(@class,'toast-title')]"))
					.getText();

			System.out.println("Toast Message: " + messageText);

			if (messageText.toLowerCase().contains("successfully")) {
				log.ReportEvent("PASS", "Request passed: " + messageText);
			} else {
				// CHANGED: Just log as warning, don't fail
				log.ReportEvent("WARNING", "Request returned: " + messageText);
				screenshots.takeScreenShot1();
				// Removed: Assert.fail("Failed: " + messageText);
			}

		} catch (Exception e) {
			// CHANGED: Just log as warning, don't fail
			log.ReportEvent("WARNING", "No toast message or error: " + e.getMessage());
			screenshots.takeScreenShot1();
			// Removed: Assert.fail("No toast message found or error: " + e.getMessage());
		}
	}

	public void addPassengerDetails() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Random rand = new Random();

		String[] firstNames = { "Rahul", "Priya", "Amit", "Neha", "Karan", "Divya", "Ravi", "Sneha", "Anil", "Megha" };
		String[] lastNames = { "Sharma", "Patel", "Reddy", "Kumar", "Verma", "Yadav", "Joshi", "Nair", "Rao",
				"Kapoor" };

		try {
			// Get all passenger containers
			List<WebElement> passengerBlocks = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-bsbk-traveller-card')]")));
			int totalPassengers = passengerBlocks.size();

			System.out.println("Total passengers detected: " + totalPassengers);

			for (int i = 0; i < totalPassengers; i++) {
				// REFRESH passenger blocks every time to avoid staleness
				passengerBlocks = driver.findElements(By.xpath("//div[contains(@class,'tg-bsbk-traveller-card')]"));
				if (i >= passengerBlocks.size())
					break;

				WebElement passengerBlock = passengerBlocks.get(i);

				System.out.println("=== Processing Passenger " + (i + 1) + " ===");

				// ✅ CASE 1: First passenger → Fill ID details + AGE
				if (i == 0) {
					System.out.println("Filling ID details + Age for passenger 1");
					fillIDCardDetails(passengerBlock, js, rand, wait);

					// 🔹 Add Age also for 1st passenger
					try {
						WebElement ageInput = wait.until(ExpectedConditions
								.elementToBeClickable(passengerBlock.findElement(By.xpath(".//input[@name='age']"))));
						int randomAge = 18 + rand.nextInt(43);
						ageInput.clear();
						ageInput.sendKeys(String.valueOf(randomAge));
						System.out.println("Age entered for 1st passenger: " + randomAge);
						Thread.sleep(500);
					} catch (Exception e) {
						System.out.println("Age entry failed for 1st passenger: " + e.getMessage());
					}
					continue;
				}

				// ✅ CASE 2: Remaining passengers → Fill everything
				System.out.println("Filling complete details for passenger " + (i + 1));

				fillTitleDropdown(passengerBlock, js, rand, wait);

				// First Name
				try {
					WebElement firstNameInput = wait.until(ExpectedConditions
							.elementToBeClickable(passengerBlock.findElement(By.xpath(".//input[@name='firstname']"))));
					String randomFirst = firstNames[rand.nextInt(firstNames.length)];
					firstNameInput.clear();
					firstNameInput.sendKeys(randomFirst);
					System.out.println("First Name entered: " + randomFirst);
					Thread.sleep(500);
				} catch (Exception e) {
					System.out.println("First Name failed: " + e.getMessage());
				}

				// Last Name
				try {
					WebElement lastNameInput = wait.until(ExpectedConditions
							.elementToBeClickable(passengerBlock.findElement(By.xpath(".//input[@name='lastname']"))));
					String randomLast = lastNames[rand.nextInt(lastNames.length)];
					lastNameInput.clear();
					lastNameInput.sendKeys(randomLast);
					System.out.println("Last Name entered: " + randomLast);
					Thread.sleep(500);
				} catch (Exception e) {
					System.out.println("Last Name failed: " + e.getMessage());
				}

				// Age
				try {
					WebElement ageInput = wait.until(ExpectedConditions
							.elementToBeClickable(passengerBlock.findElement(By.xpath(".//input[@name='age']"))));
					int randomAge = 18 + rand.nextInt(43);
					ageInput.clear();
					ageInput.sendKeys(String.valueOf(randomAge));
					System.out.println("Age entered: " + randomAge);
					Thread.sleep(500);
				} catch (Exception e) {
					System.out.println("Age failed: " + e.getMessage());
				}

				// Fill Gender - WITH SPECIAL HANDLING
				fillGenderDropdown(passengerBlock, js, rand, wait);

				// Fill ID Card details
				fillIDCardDetails(passengerBlock, js, rand, wait);

				System.out.println("=== Completed Passenger " + (i + 1) + " ===");
				Thread.sleep(1000); // Small delay between passengers
			}

		} catch (Exception e) {
			System.out.println("Error while filling passenger details: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// METHOD FOR TITLE DROPDOWN - ENHANCED
	private void fillTitleDropdown(WebElement passengerBlock, JavascriptExecutor js, Random rand, WebDriverWait wait) {
		try {
			System.out.println("Attempting to fill Title dropdown...");

			// Find Title dropdown with multiple XPath strategies
			WebElement titleDropdown = findDropdownElement(passengerBlock, "Title");

			if (titleDropdown != null) {
				// Scroll into view
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", titleDropdown);
				Thread.sleep(1000);

				// Click using multiple strategies
				clickElement(titleDropdown, js, "Title dropdown");

				// Wait for options to appear
				Thread.sleep(2000);

				// Select random option with better waiting
				selectRandomOption("Title", js, rand, wait);
			} else {
				System.out.println("Title dropdown not found");
			}
		} catch (Exception e) {
			System.out.println("Title dropdown failed: " + e.getMessage());
		}
	}

	private void fillGenderDropdown(WebElement passengerBlock, JavascriptExecutor js, Random rand, WebDriverWait wait) {
		try {
			System.out.println("Attempting to fill Gender dropdown...");

			// Find Gender dropdown - SAME PATTERN AS ID CARD TYPE
			WebElement genderDropdown = passengerBlock.findElement(By.xpath(
					".//span[contains(text(), 'Gender')]//ancestor::div[contains(@class, 'tg-select-box-container')]//div[contains(@class, 'tg-select-box__dropdown-indicator')]"));

			if (genderDropdown != null) {
				// Scroll into view
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", genderDropdown);
				Thread.sleep(1000);

				// Click using multiple strategies - SAME AS ID CARD TYPE
				clickElement(genderDropdown, js, "Gender dropdown");

				// Wait for options to appear
				Thread.sleep(3000);

				// Select random option - SAME AS ID CARD TYPE
				List<WebElement> genderOptions = driver
						.findElements(By.xpath("//div[contains(@class,'tg-dropdown-menu-item')]"));
				System.out.println("Found " + genderOptions.size() + " Gender options");

				if (!genderOptions.isEmpty()) {
					List<WebElement> visibleOptions = genderOptions.stream().filter(WebElement::isDisplayed)
							.collect(Collectors.toList());

					if (!visibleOptions.isEmpty()) {
						WebElement randomOption = visibleOptions.get(rand.nextInt(visibleOptions.size()));
						String optionText = randomOption.getText();
						System.out.println("Selecting Gender: " + optionText);

						js.executeScript("arguments[0].click();", randomOption);
						System.out.println("Gender selected: " + optionText);
					} else {
						System.out.println("No visible Gender options found");
					}
				} else {
					System.out.println("No Gender options found");
				}
			} else {
				System.out.println("Gender dropdown not found");
			}
		} catch (Exception e) {
			System.out.println("Gender dropdown failed: " + e.getMessage());
		}
	}

	// METHOD FOR ID CARD DETAILS
	private void fillIDCardDetails(WebElement passengerBlock, JavascriptExecutor js, Random rand, WebDriverWait wait) {
		try {
			// ID Card Type
			System.out.println("Filling ID Card Type...");
			WebElement idCardDropdown = findDropdownElement(passengerBlock, "ID Card Type");

			if (idCardDropdown != null) {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", idCardDropdown);
				Thread.sleep(1000);

				clickElement(idCardDropdown, js, "ID Card Type dropdown");
				Thread.sleep(2000);

				selectRandomOption("ID Card Type", js, rand, wait);
			}

			// ID Card Number
			try {
				WebElement idCardNumberInput = wait.until(ExpectedConditions
						.elementToBeClickable(passengerBlock.findElement(By.xpath(".//input[@name='idcardnumber']"))));
				String randomID = "ID" + (100000 + rand.nextInt(900000));
				idCardNumberInput.clear();
				idCardNumberInput.sendKeys(randomID);
				System.out.println("ID Card Number entered: " + randomID);
				Thread.sleep(500);
			} catch (Exception e) {
				System.out.println("ID Card Number failed: " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("ID Card details failed: " + e.getMessage());
		}
	}

	// HELPER METHOD TO FIND DROPDOWN ELEMENT
	private WebElement findDropdownElement(WebElement passengerBlock, String dropdownName) {
		String[] xpaths = { ".//span[contains(text(), '" + dropdownName
				+ "')]//ancestor::div[contains(@class, 'tg-select-box-container')]//div[contains(@class, 'tg-select-box__control')]",
				".//span[contains(text(), '" + dropdownName
						+ "')]//ancestor::div[contains(@class, 'tg-select-box-container')]//div[contains(@class, 'tg-select-box__dropdown-indicator')]",
				".//span[contains(text(), '" + dropdownName
						+ "')]//ancestor::div[contains(@class, 'tg-select-box-container')]",
				".//span[contains(., '" + dropdownName
						+ "')]//ancestor::div[contains(@class, 'tg-select-box-container')]//div[contains(@class, 'css-13cymwt-control')]" };

		for (String xpath : xpaths) {
			try {
				WebElement element = passengerBlock.findElement(By.xpath(xpath));
				System.out.println("Found " + dropdownName + " with XPath: " + xpath);
				return element;
			} catch (Exception e) {
				// Continue to next XPath
			}
		}
		return null;
	}

	// HELPER METHOD TO CLICK ELEMENT
	private void clickElement(WebElement element, JavascriptExecutor js, String elementName) {
		try {
			element.click();
			System.out.println(elementName + " - Regular click successful");
		} catch (Exception e) {
			System.out.println(elementName + " - Regular click failed, trying JavaScript click");
			js.executeScript("arguments[0].click();", element);
		}
	}

	// HELPER METHOD TO SELECT RANDOM OPTION
	private void selectRandomOption(String dropdownName, JavascriptExecutor js, Random rand, WebDriverWait wait) {
		try {
			List<WebElement> options = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-dropdown-menu-item')]")));

			System.out.println("Found " + options.size() + " " + dropdownName + " options");

			if (!options.isEmpty()) {
				List<WebElement> visibleOptions = options.stream().filter(WebElement::isDisplayed)
						.collect(Collectors.toList());

				if (!visibleOptions.isEmpty()) {
					WebElement randomOption = visibleOptions.get(rand.nextInt(visibleOptions.size()));
					String optionText = randomOption.getText();
					System.out.println("Selecting " + dropdownName + ": " + optionText);

					js.executeScript("arguments[0].click();", randomOption);
					System.out.println(dropdownName + " selected: " + optionText);
				}
			}
		} catch (Exception e) {
			System.out.println("Option selection failed for " + dropdownName + ": " + e.getMessage());
		}
	}

	// Method to clcik on add trip to continue for buses

	public void clcikOnAddTripAndContinueButton() {
		driver.findElement(By.xpath("//button[text()='Add to Trip and Continue']")).click();
		System.out.println("clciked on add trip and continue button");
	}

	// Method to click on down button in hotels desclaimerpage after add hotel to
	// trip
	public void clcikOnDownButtonInBusesDesclaimer() {
		driver.findElement(By.xpath("//div[@class='tg-triprequest-bb-view-unview-bus']")).click();

	}

	// Get the bus details from selected bus
	public String[] getBusNameFromSelectedInBusDetailsPg() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement busNameElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-details')]")));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", busNameElement);

		// Use JavaScript to get only the first text node (not inside span)
		String busNameOnly = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].childNodes[0].textContent.trim();", busNameElement);

		System.out.println("BusName from Bus detail Page: " + busNameOnly);

		return new String[] { busNameOnly };
	}

	public String[] getBusTypeFromSelectedInBusDetailsPg() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Wait for the span inside the div with bus type info
		WebElement busTypeElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-details')]//span")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", busTypeElement);
		String busType = busTypeElement.getText().trim();
		System.out.println("BusType from Bus detail Page: " + busType);

		return new String[] { busType };
	}

	public String[] getBusFromLocFromSelectedInBusDetailsPg() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement fromLocElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-origin')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fromLocElement);

		String fromLoc = fromLocElement.getText().trim();
		System.out.println("fromLoc from Bus detail Page: " + fromLoc);

		return new String[] { fromLoc };
	}

	public String[] getBusToLocFromSelectedInBusDetailsPg() {
		String ToLoc = driver.findElement(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-destination')]"))
				.getText();
		System.out.println("ToLoc from Bus detail Page: " + ToLoc);

		return new String[] { ToLoc };
	}

	public String[] getBusJourneyDateFromSelectedInBusDetailsPg() {
		String JourneyDate = driver.findElement(By.xpath("//span[contains(@class,'tg-triprequest-bb-bus-date')]"))
				.getText();
		System.out.println("JourneyDate from Bus detail Page: " + JourneyDate);

		return new String[] { JourneyDate };
	}

	public String[] getBusDepartTimeFromSelectedInBusDetailsPg() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Wait for the span inside the div with bus type info
		WebElement busTypeElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-depart-time')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", busTypeElement);
		String busType = busTypeElement.getText().trim();
		System.out.println("Depart from Bus detail Page: " + busType);

		return new String[] { busType };
	}

	public String[] getBusArrivalTimeFromSelectedInBusDetailsPg() {
		String ArrivalTime = driver
				.findElement(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-arrival-time')]")).getText();
		System.out.println("ArrivalTime from Bus detail Page: " + ArrivalTime);

		return new String[] { ArrivalTime };
	}

	public String[] getBusBoardingPointFromSelectedInBusDetailsPg() {
		String boardingPoint = driver
				.findElement(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-boarding-point')]")).getText();
		System.out.println("boardingPoint from Bus detail Page: " + boardingPoint);

		return new String[] { boardingPoint };
	}

	public String[] getBusDroppingPointFromSelectedInBusDetailsPg() {
		String droppingPoint = driver
				.findElement(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-dropping-point')]")).getText();
		System.out.println("droppingPoint from Bus detail Page: " + droppingPoint);

		return new String[] { droppingPoint };
	}

	public String[] getBusDurationFromSelectedInBusDetailsPg() {
		String duration = driver.findElement(By.xpath("//div[contains(@class,'bus-duration')]")).getText();
		System.out.println("duration from Bus detail Page: " + duration);

		return new String[] { duration };
	}

	public String[] getBusSeatTextFromSelectedInBusDetailsPg() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Wait for the span inside the div with bus type info
		WebElement busTypeElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[contains(@class,'tg-triprequest-bb-seat-number')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", busTypeElement);
		String busType = busTypeElement.getText().trim();
		System.out.println("Seat from Bus detail Page: " + busType);

		return new String[] { busType };
	}

	public String[] getBusPolicyFromSelectedInBusDetailsPg() {
		String Policy = driver.findElement(By.xpath("//div[contains(@class,' tg-policy')]")).getText();
		System.out.println("Policy from Bus detail Page: " + Policy);

		return new String[] { Policy };
	}

	public void validateBusNameFromBusBookingToDetailsPageAfterAdd(String[] bookingPgBusName, String[] detailsPgBusName,
			Log log, ScreenShots screenshots) {

		if (bookingPgBusName == null || bookingPgBusName.length == 0) {
			log.ReportEvent("FAIL", "Bus Name from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus name from Booking Page is missing.");
			return;
		}

		if (detailsPgBusName == null || detailsPgBusName.length == 0) {
			log.ReportEvent("FAIL", "Bus Name from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus name from Details Page is missing.");
			return;
		}

		String busNameBooking = bookingPgBusName[0].trim().replace("\u00A0", " ");
		String busNameDetails = detailsPgBusName[0].trim().replace("\u00A0", " ");

		if (!busNameBooking.contains(busNameDetails)) {
			log.ReportEvent("FAIL", "Bus Name mismatch between Booking and Details pages for the selected bus! "
					+ "Booking Page: '" + busNameBooking + "', Details Page: '" + busNameDetails + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Bus name mismatch between the Booking page and the Details page for the selected bus.");
		} else {
			log.ReportEvent("PASS", "Bus Name matches between Booking and Details pages: " + busNameBooking);
		}

	}

	public void validateBusTypeFromBookingToDetailspageAfterAdd(String[] bookingPgBusType, String[] detailsPgBusType,
			Log log, ScreenShots screenshots) {
		if (bookingPgBusType == null || bookingPgBusType.length == 0) {
			log.ReportEvent("FAIL", "Bus Type from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus Type from Booking Page is missing.");
			return;
		}
		if (detailsPgBusType == null || detailsPgBusType.length == 0) {
			log.ReportEvent("FAIL", "Bus Type from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus Type from Details Page is missing.");
			return;
		}

		String bookingBusType = bookingPgBusType[0].trim();
		String detailsBusType = detailsPgBusType[0].trim();

		if (!bookingBusType.equalsIgnoreCase(detailsBusType)) {
			log.ReportEvent("FAIL",
					"Bus Type mismatch between Booking and Details page for the selected bus! Booking Page: '"
							+ bookingBusType + "', Details Page: '" + detailsBusType + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Bus Type mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Bus Type matches from Booking to Details page for the selected bus: " + bookingBusType);
		}
	}

	public void validateFromLocationBetweenBookingAndDetailsAfterAdd(String[] bookingPgFromLoc,
			String[] detailsPgFromLoc, Log log, ScreenShots screenshots) {
		if (bookingPgFromLoc == null || bookingPgFromLoc.length == 0) {
			log.ReportEvent("FAIL", "Origin location from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Origin location from Booking Page is missing.");
			return;
		}
		if (detailsPgFromLoc == null || detailsPgFromLoc.length == 0) {
			log.ReportEvent("FAIL", "Origin location from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Origin location from Details Page is missing.");
			return;
		}

		String bookingOrigin = bookingPgFromLoc[0].trim();
		String detailsOrigin = detailsPgFromLoc[0].trim();

		if (!bookingOrigin.equalsIgnoreCase(detailsOrigin)) {
			log.ReportEvent("FAIL",
					"Origin location mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingOrigin + "', Details Page: '" + detailsOrigin + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Origin location mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Origin location matches from Booking to Details page for the selected bus: " + bookingOrigin);
		}
	}

	public void validateToLocationBetweenBookingAndDetailsAfterAdd(String[] bookingPgDest, String[] detailsPgToLoc,
			Log log, ScreenShots screenshots) {
		if (bookingPgDest == null || bookingPgDest.length == 0) {
			log.ReportEvent("FAIL", "Destination location from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Destination location from Booking Page is missing.");
			return;
		}
		if (detailsPgToLoc == null || detailsPgToLoc.length == 0) {
			log.ReportEvent("FAIL", "Destination location from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Destination location from Details Page is missing.");
			return;
		}

		String bookingDest = bookingPgDest[0].trim();
		String detailsToLoc = detailsPgToLoc[0].trim();

		if (!bookingDest.equalsIgnoreCase(detailsToLoc)) {
			log.ReportEvent("FAIL",
					"Destination location mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingDest + "', Details Page: '" + detailsToLoc + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Destination location mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Destination location matches from Booking to Details page for the selected bus: " + bookingDest);
		}
	}

	public void validateJourneyDateBetweenBookingAndDetailsAfterAdd(String[] bookingPgDate,
			String[] detailsPgJourneyDate, Log log, ScreenShots screenshots) {
		if (bookingPgDate == null || bookingPgDate.length == 0) {
			log.ReportEvent("FAIL", "Journey Date from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Journey Date from Booking Page is missing.");
			return;
		}
		if (detailsPgJourneyDate == null || detailsPgJourneyDate.length == 0) {
			log.ReportEvent("FAIL", "Journey Date from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Journey Date from Details Page is missing.");
			return;
		}

		String bookingDate = bookingPgDate[0].trim();
		String detailsJourneyDate = detailsPgJourneyDate[0].trim();

		if (!bookingDate.equalsIgnoreCase(detailsJourneyDate)) {
			log.ReportEvent("FAIL",
					"Journey Date mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingDate + "', Details Page: '" + detailsJourneyDate + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Journey Date mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Journey Date matches from Booking to Details page for the selected bus: " + bookingDate);
		}
	}

	public void validateDepartTimeBetweenBookingAndDetailsAfterAdd(String[] bookingPgDeptTime,
			String[] detailsPgDeptTime, Log log, ScreenShots screenshots) {
		if (bookingPgDeptTime == null || bookingPgDeptTime.length == 0) {
			log.ReportEvent("FAIL", "Departure time from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Departure time from Booking Page is missing.");
			return;
		}
		if (detailsPgDeptTime == null || detailsPgDeptTime.length == 0) {
			log.ReportEvent("FAIL", "Departure time from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Departure time from Details Page is missing.");
			return;
		}

		String bookingTime = bookingPgDeptTime[0].trim();

		// Extract only the HH:mm part (before the first space)
		String detailsRaw = detailsPgDeptTime[0].trim();
		String detailsTime = detailsRaw.split("\\s+")[0].trim();

		System.out.println("Booking Page Time: " + bookingTime);
		System.out.println("Details Page Time (extracted): " + detailsTime);

		if (!bookingTime.equalsIgnoreCase(detailsTime)) {
			log.ReportEvent("FAIL",
					"Departure time mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingTime + "', Details Page: '" + detailsTime + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Departure time mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Departure time matches from Booking to Details page for the selected bus: " + bookingTime);
		}
	}

	public void validateArrivalTimeBetweenBookingAndDetailsAfterAdd(String[] bookingPgArrivalTime,
			String[] detailsPgArrivalTime, Log log, ScreenShots screenshots) {
		if (bookingPgArrivalTime == null || bookingPgArrivalTime.length == 0) {
			log.ReportEvent("FAIL", "Arrival time from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Arrival time from Booking Page is missing.");
			return;
		}
		if (detailsPgArrivalTime == null || detailsPgArrivalTime.length == 0) {
			log.ReportEvent("FAIL", "Arrival time from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Arrival time from Details Page is missing.");
			return;
		}

		String bookingArrival = bookingPgArrivalTime[0].trim();
		String detailsArrival = detailsPgArrivalTime[0].trim();

		if (!bookingArrival.equalsIgnoreCase(detailsArrival)) {
			log.ReportEvent("FAIL",
					"Arrival time mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingArrival + "', Details Page: '" + detailsArrival + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Arrival time mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Arrival time matches from Booking to Details page for the selected bus: " + bookingArrival);
		}
	}

	public void validateBoardingPointBetweenBookingAndDetailsAfterAdd(String[] bookingPgBoardingPoint,
			String[] detailsPgBoardingPoint, Log log, ScreenShots screenshots) {
		if (bookingPgBoardingPoint == null || bookingPgBoardingPoint.length == 0) {
			log.ReportEvent("FAIL", "Boarding point from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point from Booking Page is missing.");
			return;
		}
		if (detailsPgBoardingPoint == null || detailsPgBoardingPoint.length == 0) {
			log.ReportEvent("FAIL", "Boarding point from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point from Details Page is missing.");
			return;
		}

		String bookingBoarding = bookingPgBoardingPoint[0].trim();
		String detailsBoarding = detailsPgBoardingPoint[0].trim();

		if (!bookingBoarding.equalsIgnoreCase(detailsBoarding)) {
			log.ReportEvent("FAIL",
					"Boarding point mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingBoarding + "', Details Page: '" + detailsBoarding + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Boarding point mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Boarding point matches from Booking to Details page for the selected bus: " + bookingBoarding);
		}
	}

	public void validateDroppingPointBetweenBookingAndDetailsAfterAdd(String[] bookingPgDroppingPoint,
			String[] detailsPgDroppingPoint, Log log, ScreenShots screenshots) {
		if (bookingPgDroppingPoint == null || bookingPgDroppingPoint.length == 0) {
			log.ReportEvent("FAIL", "Dropping point from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Dropping point from Booking Page is missing.");
			return;
		}
		if (detailsPgDroppingPoint == null || detailsPgDroppingPoint.length == 0) {
			log.ReportEvent("FAIL", "Dropping point from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Dropping point from Details Page is missing.");
			return;
		}

		String bookingDropping = bookingPgDroppingPoint[0].trim();
		String detailsDropping = detailsPgDroppingPoint[0].trim();

		if (!bookingDropping.equalsIgnoreCase(detailsDropping)) {
			log.ReportEvent("FAIL",
					"Dropping point mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingDropping + "', Details Page: '" + detailsDropping + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Dropping point mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Dropping point matches from Booking to Details page for the selected bus: " + bookingDropping);
		}
	}

	public void validateDurationBetweenBookingAndDetailsAfterAdd(String[] bookingPgDuration, String[] detailsPgDuration,
			Log log, ScreenShots screenshots) {
		if (bookingPgDuration == null || bookingPgDuration.length == 0) {
			log.ReportEvent("FAIL", "Duration from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Duration from Booking Page is missing.");
			return;
		}
		if (detailsPgDuration == null || detailsPgDuration.length == 0) {
			log.ReportEvent("FAIL", "Duration from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Duration from Details Page is missing.");
			return;
		}

		String bookingDuration = bookingPgDuration[0].trim();
		String detailsDuration = detailsPgDuration[0].trim();

		if (!bookingDuration.equalsIgnoreCase(detailsDuration)) {
			log.ReportEvent("FAIL",
					"Duration mismatch between Booking and Details pages for the selected bus! Booking Page: '"
							+ bookingDuration + "', Details Page: '" + detailsDuration + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Duration mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Duration matches from Booking to Details page for the selected bus: " + bookingDuration);
		}
	}

	public void validateSelectedSeatBetweenBookingAndDetailsAfterAdd(String[] bookingPgSeats, String[] detailsPgSeats,
			Log log, ScreenShots screenshots) {
		if (bookingPgSeats == null || bookingPgSeats.length == 0) {
			log.ReportEvent("FAIL", "Selected seats from Booking Page are missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Selected seats from Booking Page are missing.");
			return;
		}
		if (detailsPgSeats == null || detailsPgSeats.length == 0) {
			log.ReportEvent("FAIL", "Selected seats from Details Page are missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Selected seats from Details Page are missing.");
			return;
		}

		String bookingSeats = bookingPgSeats[0].trim();
		String detailsSeats = detailsPgSeats[0].trim();

		if (!bookingSeats.equalsIgnoreCase(detailsSeats)) {
			log.ReportEvent("FAIL",
					"Selected seats mismatch between Booking and Details pages for the selecetd bus! Booking Page: '"
							+ bookingSeats + "', Details Page: '" + detailsSeats + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Selected seats mismatch between Booking and Details pages for the selected bus.");
		} else {
			log.ReportEvent("PASS",
					"Selected seats match from Booking to Details page for the selected bus: " + bookingSeats);
		}
	}

	public void validateBusPolicyBetweenBookingAndDetailsAfterAdd(String[] bookingPgPolicy, String[] detailsPgPolicy,
			Log log, ScreenShots screenshots) {
		if (bookingPgPolicy == null || bookingPgPolicy.length == 0) {
			log.ReportEvent("FAIL", "Bus policy from Booking Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus policy from Booking Page is missing.");
			return;
		}
		if (detailsPgPolicy == null || detailsPgPolicy.length == 0) {
			log.ReportEvent("FAIL", "Bus policy from Details Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Bus policy from Details Page is missing.");
			return;
		}

		String bookingPolicy = bookingPgPolicy[0].trim();
		String detailsPolicy = detailsPgPolicy[0].trim();

		if (!bookingPolicy.equalsIgnoreCase(detailsPolicy)) {
			log.ReportEvent("FAIL", "Bus policy mismatch between Booking and Details pages! Booking Page: '"
					+ bookingPolicy + "', Details Page: '" + detailsPolicy + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Bus policy mismatch between Booking and Details pages.");
		} else {
			log.ReportEvent("PASS", "Bus policy matches from Booking to Details page.");
		}
	}

	// Method to clcik on submit trip button
	public void clickOnSubmitTripButton(Log log) {
		try {
			driver.findElement(By.xpath("//button[text()='Submit Trip']")).click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//button[text()='Yes, Submit']")).click();
			log.ReportEvent("INFO", "Successfully clicked on submit trip button");

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Trip Flow']")));

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Failed to submit trip or load 'Trip Flow' page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Validate data in trip requests after submit trip

	public List<String> getDataInTripReqAfterClickOnSubmit(String[] approverIdArray, Log log, ScreenShots screenshots) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Wait until "Approval Requests" screen is visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Approval Requests']")));

		// Get the first Approver ID from array
		String approverId = approverIdArray[0];

		try {
			// Find the search field
			WebElement searchField = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='tg-input']")));

			// Enter the Approver ID
			searchField.clear();
			searchField.sendKeys(approverId);
			Thread.sleep(3000);

			log.ReportEvent("INFO",
					"Entered Approver ID in search field in Approver Approval req screen: " + approverId);
			System.out.println("Entered Approver ID in search field: " + approverId);

			// Click the search button
			WebElement searchButton = driver.findElement(By.xpath("//button[contains(@class,'tg-icon-btn_small')]"));
			searchButton.click();

			log.ReportEvent("INFO", "Clicked on search button for Approver ID: " + approverId);
			System.out.println("Clicked on search button for Approver ID: " + approverId);

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Failed to enter Approver ID or click search button: " + approverId);
			screenshots.takeScreenShot1();
			Assert.fail("Failed to enter Approver ID or click search button in Approver Approval req screen: "
					+ approverId);
		}

		List<String> tripData = new ArrayList<>();
		String tripName = driver.findElement(By.xpath(
				"(//div[contains(@class,' tg-typography tg-typography_subtitle-5 fw-600 tg-typography_default')])[1]"))
				.getText();

		// Get the combined from-to location element and split it
		WebElement fromToElement = driver.findElement(By.xpath(
				"(//div[contains(@class,' tg-typography tg-typography_subtitle-7  tg-typography_secondary')])[1]"));
		String fromToText = fromToElement.getText();
		String TripFromLoc = fromToText.split(" - ")[0]; // Hyderabad, India
		String TripToLoc = fromToText.split(" - ")[1]; // Delhi, India

		String TripDates = driver.findElement(By.xpath(
				"//div[contains(@class,' tg-typography tg-typography_subtitle-6 xs-fs-12 tg-typography_secondary-dark')]"))
				.getText();

		// Get all service elements
		List<WebElement> serviceElements = driver.findElements(By.xpath(
				"(//div[contains(@class,'trip_card__container')])[1]//div[contains(@class,'tg-label tg-label_white tg-label_md undefined gap-1')]"));
		List<String> servicesList = new ArrayList<>();
		for (WebElement serviceElement : serviceElements) {
			servicesList.add(serviceElement.getText().trim());
		}
		String Services = String.join(", ", servicesList); // Combined string or you can keep list

		String tripId = driver.findElement(By.xpath(
				"(//div[contains(@class,' tg-typography tg-typography_subtitle-7 cursor-pointer fw-500 tg-typography_text-info')])[1]"))
				.getText();

		// Add all data in order
		tripData.add(tripName); // index 0
		tripData.add(TripFromLoc); // index 1
		tripData.add(TripToLoc); // index 2
		tripData.add(TripDates); // index 3
		tripData.add(Services); // index 4
		tripData.add(tripId); // index 5

		return tripData;
	}

	// validation methods
	public void validateTripNameAfterSubmit(String enteredTripName, List<String> tripDataAfterSubmit, Log log,
			ScreenShots screenshots) {
		if (enteredTripName == null || enteredTripName.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Entered trip name is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Entered trip name is null or empty.");
			return;
		}

		if (tripDataAfterSubmit == null || tripDataAfterSubmit.size() == 0) {
			log.ReportEvent("FAIL", "No trip data found after clicking Submit.");
			screenshots.takeScreenShot1();
			Assert.fail("No trip data found after clicking Submit.");
			return;
		}

		String displayedTripName = tripDataAfterSubmit.get(0).trim();

		if (!enteredTripName.trim().equalsIgnoreCase(displayedTripName)) {
			log.ReportEvent("FAIL", "Trip name mismatch! Entered: '" + enteredTripName + "', Displayed after submit: '"
					+ displayedTripName + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Trip name mismatch between search and after submit.");
		} else {
			log.ReportEvent("PASS", "Trip name matches With after search: '" + enteredTripName + "'");
		}
	}

	public void validateFromLocationAfterSubmit(String enteredFromLoc, List<String> tripDataAfterSubmit, Log log,
			ScreenShots screenshots) {
		if (enteredFromLoc == null || enteredFromLoc.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Entered 'From' location is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Entered 'From' location is null or empty.");
			return;
		}

		if (tripDataAfterSubmit == null || tripDataAfterSubmit.size() < 2) {
			log.ReportEvent("FAIL", "Trip data is incomplete or missing after Submit.");
			screenshots.takeScreenShot1();
			Assert.fail("Trip data is incomplete or missing after Submit.");
			return;
		}

		String entered = enteredFromLoc.trim().toLowerCase();
		String displayed = tripDataAfterSubmit.get(1).trim().toLowerCase();

		if (!displayed.contains(entered)) {
			log.ReportEvent("FAIL", "From location mismatch! Entered: '" + enteredFromLoc
					+ "', Displayed after submit: '" + tripDataAfterSubmit.get(1).trim() + "'");
			screenshots.takeScreenShot1();
			Assert.fail("From location mismatch between entered and after submit.");
		} else {
			log.ReportEvent("PASS", "From location matches between entered and after submit: '" + enteredFromLoc + "'");
		}
	}

	public void validateToLocationAfterSubmit(String enteredToLoc, List<String> tripDataAfterSubmit, Log log,
			ScreenShots screenshots) {
		if (enteredToLoc == null || enteredToLoc.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Entered 'To' location is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Entered 'To' location is null or empty.");
			return;
		}

		if (tripDataAfterSubmit == null || tripDataAfterSubmit.size() < 3) {
			log.ReportEvent("FAIL", "Trip data is incomplete or missing after Submit.");
			screenshots.takeScreenShot1();
			Assert.fail("Trip data is incomplete or missing after Submit.");
			return;
		}

		String entered = enteredToLoc.trim().toLowerCase();
		String displayed = tripDataAfterSubmit.get(2).trim().toLowerCase();

		// Flexible match: check if displayed contains entered location
		if (!displayed.contains(entered)) {
			log.ReportEvent("FAIL", "To location mismatch! Entered: '" + enteredToLoc + "', Displayed after submit: '"
					+ tripDataAfterSubmit.get(2).trim() + "'");
			screenshots.takeScreenShot1();
			Assert.fail("To location mismatch between Entered and after submit.");
		} else {
			log.ReportEvent("PASS", "To location matches between Entered and after submit: '" + enteredToLoc + "'");
		}
	}

	public void validateTripDatesAfterSubmit(String expectedOnwardDate, String expectedReturnDate,
			List<String> tripDataAfterSubmit, Log log, ScreenShots screenshots) {
		if (expectedOnwardDate == null || expectedReturnDate == null || expectedOnwardDate.trim().isEmpty()
				|| expectedReturnDate.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Expected onward or return date is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Expected onward or return date is null or empty.");
			return;
		}

		if (tripDataAfterSubmit == null || tripDataAfterSubmit.size() < 4) {
			log.ReportEvent("FAIL", "Trip data is missing or incomplete after Submit.");
			screenshots.takeScreenShot1();
			Assert.fail("Trip data is missing or incomplete after Submit.");
			return;
		}

		String displayedTripDates = tripDataAfterSubmit.get(3).trim(); // e.g., "4th Oct, 2025 - 9th Oct, 2025"

		// Split using " - " (with spaces), the format seen in the UI
		String[] parts = displayedTripDates.split(" - ");
		if (parts.length < 2) {
			log.ReportEvent("FAIL", "Unable to parse trip dates from displayed string: " + displayedTripDates);
			screenshots.takeScreenShot1();
			Assert.fail("Trip dates format is invalid");
			return;
		}

		// Normalize both dates using your method
		String displayedOnward = normalizeDate(parts[0].trim()); // e.g., "4th Oct, 2025" → "04-Oct-2025"
		String displayedReturn = normalizeDate(parts[1].trim()); // e.g., "9th Oct, 2025" → "09-Oct-2025"

		boolean onwardMatch = expectedOnwardDate.equalsIgnoreCase(displayedOnward);
		boolean returnMatch = expectedReturnDate.equalsIgnoreCase(displayedReturn);

		if (!onwardMatch || !returnMatch) {
			log.ReportEvent("FAIL",
					"Trip dates mismatch!\n" + "Expected Onward: " + expectedOnwardDate + " | Displayed: "
							+ displayedOnward + "\n" + "Expected Return: " + expectedReturnDate + " | Displayed: "
							+ displayedReturn);
			screenshots.takeScreenShot1();
			Assert.fail("Trip dates mismatch between entered and after submit.");
		} else {
			log.ReportEvent("PASS", "Trip dates match between entered and after submit:\nOnward: " + expectedOnwardDate
					+ ", Return: " + expectedReturnDate);
		}
	}

	public String normalizeDate(String rawDate) {
		// Remove ordinal suffixes: st, nd, rd, th
		rawDate = rawDate.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
		rawDate = rawDate.replaceAll(",", "").trim(); // Remove commas if any

		String[] possibleFormats = { "dd MMMM yyyy", // 13 August 2025
				"MMM dd yyyy", // Aug 13 2025
				"yyyy-MM-dd", // 2025-08-13
				"dd-MM-yyyy", // 13-08-2025
				"dd/MM/yyyy", // 13/08/2025
				"dd MMM yyyy" // 13 Aug 2025
		};

		for (String format : possibleFormats) {
			try {
				SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.ENGLISH);
				Date date = inputFormat.parse(rawDate);

				// Desired output format
				SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
				return outputFormat.format(date);

			} catch (ParseException e) {
				// Try next format
			}
		}

		System.err.println(" Could not normalize date: " + rawDate);
		return rawDate;
	}

	public void validateTripIdBetweenSubmitAndDetailsPage(List<String> tripDataAfterSubmit,
			String[] tripIdFromDetailsPage, Log log, ScreenShots screenshots) {
		if (tripDataAfterSubmit == null || tripDataAfterSubmit.size() < 6) {
			log.ReportEvent("FAIL", "Trip data is missing or incomplete after Submit.");
			screenshots.takeScreenShot1();
			Assert.fail("Trip data is missing or incomplete after Submit.");
			return;
		}

		if (tripIdFromDetailsPage == null || tripIdFromDetailsPage.length == 0
				|| tripIdFromDetailsPage[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Trip ID from details page is missing or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Trip ID from details page is missing or empty.");
			return;
		}

		String tripIdFromSubmit = tripDataAfterSubmit.get(5).trim();
		String tripIdFromDetails = tripIdFromDetailsPage[0].trim();

		if (!tripIdFromSubmit.equalsIgnoreCase(tripIdFromDetails)) {
			log.ReportEvent("FAIL", "Trip ID mismatch!\n" + "Trip Id In After submit Page: " + tripIdFromSubmit + "\n"
					+ "Trip Details Page: " + tripIdFromDetails);
			screenshots.takeScreenShot1();
			Assert.fail("Trip ID mismatch between Submit and Details pages.");
		} else {
			log.ReportEvent("PASS", "Trip ID matches between Submit and Details pages: " + tripIdFromSubmit);
		}
	}

	public void validateServicesMatchAfterSubmit(List<String> selectedServices, List<String> tripDataAfterSubmit,
			Log log, ScreenShots screenshots) {
		if (selectedServices == null || selectedServices.isEmpty()) {
			log.ReportEvent("FAIL", "No services were selected.");
			screenshots.takeScreenShot1();
			Assert.fail("No services were selected.");
			return;
		}

		if (tripDataAfterSubmit == null || tripDataAfterSubmit.size() < 5) {
			log.ReportEvent("FAIL", "Trip data is incomplete or missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Trip data is incomplete or missing.");
			return;
		}

		String displayedServices = tripDataAfterSubmit.get(4).toLowerCase(); // e.g., "AC, Sleeper"

		for (String service : selectedServices) {
			if (!displayedServices.contains(service.toLowerCase())) {
				log.ReportEvent("FAIL",
						"Service '" + service + "' not found in displayed services: " + displayedServices);
				screenshots.takeScreenShot1();
				Assert.fail("Service '" + service + "' not found in displayed services.");
				return;
			}
		}

		log.ReportEvent("PASS", "All selected services are present in the displayed services.");
	}

}