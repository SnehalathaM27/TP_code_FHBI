package com.tripgain.collectionofpages;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONObject;
//import com.tripgain.common.TestExecutionNotifier;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tripgain_HomePage_Flights {
	WebDriver driver;

	public Tripgain_HomePage_Flights(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to Verify Trip Gain Home Page is Displayed
	public void verifyTripGainHomePageIsDisplayed(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			WebElement homePage = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Welcome')]")));
			TestExecutionNotifier.showExecutionPopup(); // ADD THIS LINE
			if (homePage.isDisplayed()) {
				Log.ReportEvent("PASS", "TripGain Home is displayed Successful");
			} else {
				Log.ReportEvent("FAIL", "TripGain Home Page is not displayed");
				ScreenShots.takeScreenShot();
				Assert.fail();
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "TripGain Home Page is not displayed");
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();

		}
	}

	public void clickIfPresentCloseBtn() {
	    // 1. Find elements (will wait up to 10s due to BaseClass implicit wait)
	    // Using contains(text()) is safer for modern web apps
	    List<WebElement> btn = driver.findElements(
	        By.xpath("//h2[text()='Travel Advisory']/ancestor::div[@role='dialog']//button[contains(text(),'Close')]")
	    );

	    if (!btn.isEmpty()) {
	        try {
	            WebElement element = btn.get(0);
	            JavascriptExecutor js = (JavascriptExecutor) driver;

	            // 2. Scroll the element into the CENTER of the view
	            // This ensures headers or footers don't block the click
	            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

	            // 3. Perform the click via JavaScript (ignores visibility/overlay blocks)
	            js.executeScript("arguments[0].click();", element);
	            
	            System.out.println("Successfully clicked the Close button using JS.");
	        } catch (Exception e) {
	            System.out.println("Failed to click Close button: " + e.getMessage());
	        }
	    } else {
	        System.out.println("Close button was not present, skipping.");
	    }
	}
	
	// Method to select trip type from dropdown
	public void selectTripType(String tripName) throws InterruptedException {
		// Click on the dropdown to open it
		Thread.sleep(800);
		WebElement dropdown = driver
				.findElement(By.xpath("(//div[contains(@class,'tg-select-box__value-container')])[1]"));
		dropdown.click();
		Thread.sleep(800);
		// Wait for the dropdown options to be visible (optional, but recommended)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement option = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + tripName + "']")));

		// Click on the desired option
		option.click();
	}

	// Method to select cabin class from dropdown
	public void selectCabinClass(String cabinClass) throws InterruptedException {
		// Click on the dropdown to open it
		Thread.sleep(800);
		WebElement dropdown = driver
				.findElement(By.xpath("(//div[contains(@class,'tg-select-box__value-container')])[2]"));
		dropdown.click();

		Thread.sleep(800);
		// Wait for the dropdown option with the specific cabin class to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//span[@class='tg-select-option-label' and text()='" + cabinClass + "']")));

		// Click on the desired option
		option.click();
	}

	public void clickOnRemoveReturnDate() {
		try {
			WebElement removeButton = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.elementToBeClickable(By.xpath(
							"//div[contains(@class,'MuiGrid2-grid-lg-1.9')]//*[local-name()='svg' and @data-testid='CloseIcon']")));

			removeButton.click();
			System.out.println("✅ Clicked on Remove button");

		} catch (Exception e) {
			System.out.println("❌ Failed: " + e.getMessage());
		}
	}

	// Method to select 'From' location by entering and selecting the first
	// suggestion
	public void selectFromLocation(String location) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Step 1: Find and click the 'From' input
		WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='from']")));
		fromInput.click();
		fromInput.clear();
		fromInput.sendKeys(location);
		Thread.sleep(800);

		// Step 2: Wait for at least one suggestion to be visible
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'airport-option')]")));

		// Step 3: Get all available suggestions
		List<WebElement> suggestions = wait.until(ExpectedConditions
				.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'airport-option')]")));

		// Step 4: Click the first visible and enabled suggestion
		for (WebElement suggestion : suggestions) {
			if (suggestion.isDisplayed() && suggestion.isEnabled()) {
				wait.until(ExpectedConditions.elementToBeClickable(suggestion)).click();
				return;
			}
		}

		// Step 5: If no clickable suggestion found, throw an exception
		throw new NoSuchElementException("No clickable airport suggestions found for: " + location);
	}

	public void selectDestLocation(String location) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Step 1: Find and click the 'From' input
		WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='to']")));
		fromInput.click();
		fromInput.clear();
		fromInput.sendKeys(location);
		Thread.sleep(800);

		// Step 2: Wait for at least one suggestion to be visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("(//div[@class='airport-menu d-flex justify-content-between'])[1]")));

		// Step 3: Get all available suggestions
		List<WebElement> suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("(//div[@class='airport-menu d-flex justify-content-between'])[1]")));

		// Step 4: Click the first visible and enabled suggestion
		for (WebElement suggestion : suggestions) {
			if (suggestion.isDisplayed() && suggestion.isEnabled()) {
				wait.until(ExpectedConditions.elementToBeClickable(suggestion)).click();
				return;
			}
		}

		// Step 5: If no clickable suggestion found, throw an exception
		throw new NoSuchElementException("No clickable airport suggestions found for: " + location);
	}

	// Method to select 'To' location by entering and selecting the first suggestion
	public void selectToLocation(String location) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Step 1: Find and click the 'From' input
		WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='to']")));
		fromInput.click();
		fromInput.clear();
		fromInput.sendKeys(location);
		Thread.sleep(800);

		// Step 2: Wait for at least one suggestion to be visible
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'airport-option')]")));

		// Step 3: Get all available suggestions
		List<WebElement> suggestions = wait.until(ExpectedConditions
				.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'airport-option')]")));

		// Step 4: Click the first visible and enabled suggestion
		for (WebElement suggestion : suggestions) {
			if (suggestion.isDisplayed() && suggestion.isEnabled()) {
				wait.until(ExpectedConditions.elementToBeClickable(suggestion)).click();
				return;
			}
		}

		// Step 5: If no clickable suggestion found, throw an exception
		throw new NoSuchElementException("No clickable airport suggestions found for: " + location);
	}

	public void selectDate(String day, String MonthAndYear) throws InterruptedException {

		TestExecutionNotifier.showExecutionPopup();

		driver.findElement(By.xpath("(//div[@class='tg-datepicker undefined'])[1]")).click();
		Thread.sleep(2000);

		String Date = driver.findElement(By.xpath("(//div[@class='pt-2'])[1]//div")).getText();

		if (Date.contentEquals(MonthAndYear)) {

			driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day
					+ "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();

		} else {

			while (!Date.contentEquals(MonthAndYear)) {

				driver.findElement(By.xpath("(//button[contains(@class, 'nav-arrow')])[2]")).click();
				Thread.sleep(1000);

				// 🔥 VERY IMPORTANT — update Date again
				Date = driver.findElement(By.xpath("(//div[@class='pt-2'])[1]//div")).getText();
			}

			driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day
					+ "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();
		}
	}

	@FindBy(xpath = "(//div[contains(@class,' min-date-width')])[1]")
	WebElement selectjourdate;

	public String selectJourneyDate(String returnDate, String MonthandYear) throws InterruptedException {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Zoom out (your logic kept)
		js.executeScript("document.body.style.zoom='80%'");

		// ===== CLICK DATE FIELD TO OPEN CALENDAR =====
		wait.until(ExpectedConditions.visibilityOf(selectjourdate));
		wait.until(ExpectedConditions.elementToBeClickable(selectjourdate));

		// Scroll to element
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", selectjourdate);
		Thread.sleep(500);

		// IMPORTANT: Click the actual clickable child inside wrapper
		js.executeScript("arguments[0].querySelector('.custom_datepicker_input_wrapper').click();", selectjourdate);

		// ===== WAIT FOR CALENDAR HEADER =====
		By monthYearHeader = By.xpath("//div[@class='custom-header']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));

		String currentMonthYear = driver.findElement(monthYearHeader).getText();

		// ===== NAVIGATE TO CORRECT MONTH =====
		while (!currentMonthYear.equals(MonthandYear)) {

			WebElement nextArrow = driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]"));
			js.executeScript("arguments[0].click();", nextArrow);

			wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));
			currentMonthYear = driver.findElement(monthYearHeader).getText();
		}

		// ===== SELECT DATE =====
		By dayLocator = By.xpath(
				"//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and not(contains(@class, 'disabled'))]//span[@class='day' and text()='"
						+ returnDate + "']");

		WebElement dayElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dayLocator));

		js.executeScript("arguments[0].scrollIntoView({block:'center'});", dayElement);
		Thread.sleep(500);

		js.executeScript("arguments[0].click();", dayElement);

		// Restore zoom
		js.executeScript("document.body.style.zoom='100%'");

		String rawDate = returnDate + " " + MonthandYear;
		return normalizeDate(rawDate);
	}

	@FindBy(xpath = "(//div[contains(@class,' min-date-width')])[2]")
	WebElement selectreturnjourdate;

	public String selectreturnJourneyDate(String returnDate, String MonthandYear) throws InterruptedException {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Zoom out (your logic kept)
		js.executeScript("document.body.style.zoom='80%'");

		// ===== CLICK DATE FIELD TO OPEN CALENDAR =====
		wait.until(ExpectedConditions.visibilityOf(selectreturnjourdate));
		wait.until(ExpectedConditions.elementToBeClickable(selectreturnjourdate));

		// Scroll to element
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", selectreturnjourdate);
		Thread.sleep(500);

		// IMPORTANT: Click the actual clickable child inside wrapper
		js.executeScript("arguments[0].querySelector('.custom_datepicker_input_wrapper').click();",
				selectreturnjourdate);

		// ===== WAIT FOR CALENDAR HEADER =====
		By monthYearHeader = By.xpath("//div[@class='custom-header']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));

		String currentMonthYear = driver.findElement(monthYearHeader).getText();

		// ===== NAVIGATE TO CORRECT MONTH =====
		while (!currentMonthYear.equals(MonthandYear)) {

			WebElement nextArrow = driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]"));
			js.executeScript("arguments[0].click();", nextArrow);

			wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));
			currentMonthYear = driver.findElement(monthYearHeader).getText();
		}

		// ===== SELECT DATE =====
		By dayLocator = By.xpath(
				"//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and not(contains(@class, 'disabled'))]//span[@class='day' and text()='"
						+ returnDate + "']");

		WebElement dayElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dayLocator));

		js.executeScript("arguments[0].scrollIntoView({block:'center'});", dayElement);
		Thread.sleep(500);

		js.executeScript("arguments[0].click();", dayElement);

		// Restore zoom
		js.executeScript("document.body.style.zoom='100%'");

		String rawDate = returnDate + " " + MonthandYear;
		return normalizeDate(rawDate);
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

		System.err.println("⚠ Could not normalize date: " + rawDate);
		return rawDate;
	}

	// Normalize location string by removing ", ..." suffix
	public String normalizeLocation(String location) {
		return location.split(",")[0].trim();
	}

	public void clickDate() {
		driver.findElement(By.xpath("(//div[contains(@class,' min-date-width')])[1]")).click();
	}

	public void selectDate(String returnDate, String returnMonthAndYear, Log Log) throws InterruptedException {
		clickDate();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='custom-header']")));

		String currentMonthYear = driver.findElement(By.xpath("//div[@class='custom-header']")).getText();
		System.out.println("Current calendar: " + currentMonthYear);

		// Navigate to correct month if needed
		while (!currentMonthYear.trim().equalsIgnoreCase(returnMonthAndYear.trim())) {
			driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@class='custom-header']"),
					returnMonthAndYear));
			currentMonthYear = driver.findElement(By.xpath("//div[@class='custom-header']")).getText();
		}

		driver.findElement(By.xpath(
				"//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and not(contains(@class, 'disabled'))]//span[@class='day' and text()='"
						+ returnDate + "']"))
				.click();

		Log.ReportEvent("INFO", "Selected date: " + returnDate + " " + returnMonthAndYear);
	}

	// Method to Select Return Date By Passing Two Paramenters(Date and MounthYear)
	public void selectReturnDate(String day, String MonthAndYear) throws InterruptedException {
		TestExecutionNotifier.showExecutionPopup();
		driver.findElement(By.xpath("(//div[@class='tg-datepicker undefined'])[2]")).click();
		Thread.sleep(5000);
		String Date = driver.findElement(By.xpath("((//div[@class='custom-header'])[1]//span)[1]")).getText();
		if (Date.contentEquals(MonthAndYear)) {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day
					+ "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();
			Thread.sleep(4000);

		} else {
			while (!Date.contentEquals(MonthAndYear)) {
				Thread.sleep(500);
				driver.findElement(By.xpath("(//button[contains(@class, 'nav-arrow')])[2]")).click();
				if (driver.findElement(By.xpath("((//div[@class='custom-header'])[1]//span)[1]")).getText()
						.contentEquals(MonthAndYear)) {
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='"
							+ day + "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]"))
							.click();
					break;
				}

			}
		}
	}

	public void selectTravellers(int desiredAdults) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Open traveller selection dropdown
		driver.findElement(By.xpath("//div[@class='traveller-button']")).click();
		Thread.sleep(3000);

		// Get current adult count
		WebElement adultCountElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='adultvalue'])[1]")));
		int currentAdults = Integer.parseInt(adultCountElement.getText().trim());

		// If desired count is greater, click plus icon (difference times)
		if (desiredAdults > currentAdults) {
			int clicksNeeded = desiredAdults - currentAdults;
			String plusIconXPath = "(//div[@class='plusminus']//*[@stroke='currentColor'])[2]";
			WebElement plusIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(plusIconXPath)));

			for (int i = 0; i < clicksNeeded; i++) {
				plusIcon.click();
				try {
					Thread.sleep(500); // allow UI to update
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		// Click 'Done' button
		WebElement doneButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Done']")));
		doneButton.click();
	}

	public void clickDoneButton() throws InterruptedException {
		Thread.sleep(500);
		driver.findElement(By.xpath("//button[text()='Done']"));
		Thread.sleep(600);
	}

	// Method: Click on Search Button with validations after clicking
	public void clickSearchFlightsButton(Log Log, ScreenShots ScreenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

		try {
			// Click the Search Flights button (always re-locate to avoid stale reference)
//			wait.until(ExpectedConditions.elementToBeClickable(
//					By.xpath("//button[text()='Search Flights']"))).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//button[text()='Search Flights']")).click();

			// After clicking, wait for results or messages
			WebDriverWait postClickWait = new WebDriverWait(driver, Duration.ofSeconds(40));

			// 1. Check for toast error message
			try {
				WebElement toastMessage = postClickWait.until(ExpectedConditions.refreshed(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='toast-title']"))));

				if (toastMessage.isDisplayed()) {
					String errorText = toastMessage.getText();
					Log.ReportEvent("FAIL", "Error after search: " + errorText);
					ScreenShots.takeScreenShot();
					Assert.fail("Search failed with error: " + errorText);
					return;
				}
			} catch (TimeoutException e) {
				// No toast message, continue
			}

			// 2. Check if flights are loaded
			try {
				WebElement flightsLoaded = postClickWait.until(ExpectedConditions.refreshed(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'tg-flightcarrier')])[1]"))));

				if (flightsLoaded.isDisplayed()) {
					Log.ReportEvent("PASS", "Flights are loaded successfully.");
					return;
				}
			} catch (TimeoutException e) {
				// Flights not loaded, continue
			}

			// 3. Check if 'flights not found' icon is displayed
			try {
				WebElement flightsNotFound = postClickWait.until(ExpectedConditions.refreshed(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-testid='AirplaneIcon']"))));

				if (flightsNotFound.isDisplayed()) {
					Log.ReportEvent("FAIL", "Flights are not found.");
					ScreenShots.takeScreenShot();
					Assert.fail("Flights not found after search.");
					return;
				}
			} catch (TimeoutException e) {
				// Continue to final failure
			}

			// 4. If none of the conditions are met
			Log.ReportEvent("FAIL",
					"Unexpected state after searching flights. No flights loaded or error message displayed.");
			ScreenShots.takeScreenShot();
			Assert.fail("No expected search results or error messages found.");

		} catch (StaleElementReferenceException e) {
			// Retry click once if element goes stale during action
			Log.ReportEvent("INFO", "Search button went stale, retrying click...");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Search Flights']"))).click();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	// Method to click on "View Fares" button by index
	// Inside Tripgain_HomePage_Flights.java
	public void clickViewFareByIndex(int index) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement viewFareBtn = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[text()='View Fares'])[" + index + "]")));

		// Use JS Click to avoid the "ElementClickInterceptedException"
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", viewFareBtn);
		js.executeScript("arguments[0].click();", viewFareBtn);

		System.out.println("✅ SOP: Clicked View Fares for index " + index + " using JS.");
	}

	// Method to get stop text dynamically
	public String getStopText(String stopType) {
		return "//div[normalize-space(text())='" + stopType + "']";
	}

	// Method to click on "View Fares" button by index
	public void clickViewFareByIndex(int index, Log Log, ScreenShots ScreenShots) {

		List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
		if (!noFlightsIcon.isEmpty()) {
			Log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
			ScreenShots.takeScreenShot();
			Assert.fail("No flights found, validation aborted.");
			return; // Exit method early
		}
		// XPath index starts from 1, so ensure your index is at least 1
		if (index < 1) {
			throw new IllegalArgumentException("Index must be 1 or greater.");
		}

		// Construct the XPath with the given index
		String xpath = "(//div[text()='View Fares'])[" + index + "]";

		// Wait for the element to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement viewFareButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

		// Click on the button
		viewFareButton.click();
	}

	// Method to click on Select Button based on Fare
	public String[] clickOnSelectBasedOnFareType(String fareType) throws InterruptedException {
		Thread.sleep(5000);
		TestExecutionNotifier.showExecutionPopup();
		String price = null;
		String fare = null;
		String supplierName = null;
		String cancelationData = null;
		String changeData = null;
		String cabinData = null;
		String checkInData = null;
		String policy = null;
		try {
			price = null;
			fare = null;
			List<WebElement> fareElements = driver.findElements(By.xpath(
					"//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType + "']"));

			if (!fareElements.isEmpty() && fareElements.get(0).isDisplayed()) {
				Thread.sleep(2000);
				WebElement continueButton = driver
						.findElement(By.xpath("//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div[@class='action']//button"));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);
				((JavascriptExecutor) driver).executeScript(
						"window.scrollTo({ top: arguments[0].getBoundingClientRect().top + window.scrollY - 100, behavior: 'smooth' });",
						continueButton);
				Thread.sleep(3000);
				price = driver.findElement(
						By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/following-sibling::div"))
						.getText();
				supplierName = driver
						.findElement(
								By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
										+ fareType + "']/parent::div/parent::div/following-sibling::div/img"))
						.getAttribute("alt");
				cancelationData = driver.findElement(
						By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[@data-tgflpenaltytype='CancelPenalty']/div"))
						.getText();
				changeData = driver.findElement(
						By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[@data-tgflpenaltytype='ChangePenalty']/div"))
						.getText();
				cabinData = driver.findElement(By
						.xpath("((//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[contains(@class,'MuiGrid2-container')])[3]//div[@data-tgflcheckinbaggage])[2]/div"))
						.getText();
				checkInData = driver.findElement(By
						.xpath("((//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[contains(@class,'MuiGrid2-container')])[3]//div[@data-tgflcabinbaggage])[2]/div"))
						.getText();
				policy = driver.findElement(
						By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div/div/div[contains(@class,'tg-policy')]"))
						.getAttribute("data-tginpolicy");

				Thread.sleep(2000);
				continueButton.click();
				fare = fareType;
			} else {
				Thread.sleep(2000);
				WebElement continueButton = driver.findElement(By.xpath("(//div[@class='action']//button)[1]"));
				((JavascriptExecutor) driver).executeScript(
						"window.scrollTo({ top: arguments[0].getBoundingClientRect().top + window.scrollY - 100, behavior: 'smooth' });",
						continueButton);
				Thread.sleep(3000);
				price = driver
						.findElement(By.xpath("(//div[@class='relative']//div[contains(@class,'tg-fare-price')])[1]"))
						.getText();
				supplierName = driver.findElement(By.xpath("(//div[@class='relative']//img)[1]")).getAttribute("alt");
				fare = driver
						.findElement(By.xpath("(//div[@class='relative']//div[contains(@class,'tg-fare-type')])[1]"))
						.getText();
				cancelationData = driver.findElement(By.xpath("(//div[@data-tgflpenaltytype='CancelPenalty']/div)[1]"))
						.getText();
				changeData = driver.findElement(By.xpath("(//div[@data-tgflpenaltytype='ChangePenalty']/div)[1]"))
						.getText();
				cabinData = driver
						.findElement(By.xpath(
								"(//div[contains(@class,'MuiGrid2-container')]//div[@data-tgflcheckinbaggage])[3]/div"))
						.getText();
				checkInData = driver
						.findElement(By.xpath(
								"(//div[contains(@class,'MuiGrid2-container')]//div[@data-tgflcabinbaggage])[3]/div"))
						.getText();
				policy = driver.findElement(By.xpath(
						"(//div[@class='relative']//div[contains(@class,'tg-fare-type')])[1]/parent::div//parent::div/following-sibling::div//div[contains(@class,'tg-policy')]"))
						.getAttribute("data-tginpolicy");

				Thread.sleep(2000);
				continueButton.click();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { price, fare, supplierName, cancelationData, changeData, cabinData, checkInData, policy };
	}

	public boolean isElementPresent(By locator) {
		return !driver.findElements(locator).isEmpty() && driver.findElements(locator).get(0).isDisplayed();
	}

	// Method to click on "Select" button by index
	public void clickSelectButtonByIndex(int index) {
		// Validate the index (XPath indices start at 1)
		if (index < 1) {
			throw new IllegalArgumentException("Index must be 1 or greater.");
		}

		// Construct the XPath with the provided index
		String xpath = "(//span[text()='Select'])[" + index + "]";

		// Wait for the element to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

		// Click the button
		selectButton.click();
	}

	// Method to click on View Flight Button by index
	public void clickViewFlightDetailsByIndex(int index) {
		// Validate the index (XPath indices start at 1)
		if (index < 1) {
			throw new IllegalArgumentException("Index must be 1 or greater.");
		}

		// Construct the XPath with the provided index
		String xpath = "(//div[text()='Show Flight Details'])[" + index + "]";

		// Wait for the element to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

		// Click the button
		selectButton.click();
	}

	// Method to click on View Flight Button by index
	public void clickShowFlightDetailsByIndex(int index) {
		// Validate the index (XPath indices start at 1)
		if (index < 1) {
			throw new IllegalArgumentException("Index must be 1 or greater.");
		}

		// Construct the XPath with the provided index
		String xpath = "(//button[@class='view-our-policy-button'])[" + index + "]";

		// Wait for the element to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

		// Click the button
		selectButton.click();
	}

	public void clickOnNextButton() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[text()='Select Next Flight']")).click();
	}

	// Method to click on View Flight Button by index
	public void clickSelectByIndex(int index) throws InterruptedException {
		// Validate the index (XPath indices start at 1)
		if (index < 1) {
			throw new IllegalArgumentException("Index must be 1 or greater.");
		}

		// Construct the XPath with the provided index
		String xpath = "(//span[text()='Select'])[" + index + "]";

		// Wait for the element to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

		// Click the button
		selectButton.click();
		Thread.sleep(3000);
	}

	// Method to click on View Flight Button by index
	public void clickViewFlightDetailsByIndexForInternational(int index) {
		// Validate the index (XPath indices start at 1)
		if (index < 1) {
			throw new IllegalArgumentException("Index must be 1 or greater.");
		}

		// Construct the XPath with the provided index
		String xpath = "(//button[text()='View Flight Details '])[" + index + "]";

		// Wait for the element to be clickable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

		// Click the button
		selectButton.click();
	}

	// Method to get Data from Flight Info Card
	public Map<String, String> getFareDetailsFromFlightInfo(Log Log, ScreenShots ScreenShots) {
		Map<String, String> fareDetails = new HashMap<>();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Fare price
			String farePrice = safeGetTextWithWait("//div[contains(@class,'tg-fare-price')]", "Fare Price", wait, Log,
					ScreenShots);
			fareDetails.put("FarePrice", farePrice);

			// Supplier name (from image alt)
			String supplierName = safeGetAttributeWithWait("(//img[contains(@class,'tg-supplier-img')])[1]", "alt",
					"Supplier Name", wait, Log, ScreenShots);
			fareDetails.put("Supplier", supplierName);

			// Fare type
			String fareType = safeGetTextWithWait("//div[contains(@class,'tg-fare-type')]", "Fare Type", wait, Log,
					ScreenShots);
			fareDetails.put("FareType", fareType);

			// Policy type (data attribute)
			String policy = safeGetTextWithWait(
					"//div[@class='flight-journey-info-card__container']//div[contains(@class,'tg-policy')]",
					"Policy Type", wait, Log, ScreenShots);
			fareDetails.put("policyType", policy);

			Log.ReportEvent("PASS", "Fare details fetched successfully.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to fetch fare details: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception in getFareDetailsFromFlightInfo: " + e.getMessage());
		}

		return fareDetails;
	}

	private String safeGetTextWithWait(String xpath, String label, WebDriverWait wait, Log Log,
			ScreenShots ScreenShots) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			return element.getText().trim();
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to get text for " + label + ": " + e.getMessage());
			ScreenShots.takeScreenShot();
			return "";
		}
	}

	private String safeGetAttributeWithWait(String xpath, String attribute, String label, WebDriverWait wait, Log Log,
			ScreenShots ScreenShots) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			String value = element.getAttribute(attribute);
			return value != null ? value.trim() : "";
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to get attribute '" + attribute + "' for " + label + ": " + e.getMessage());
			ScreenShots.takeScreenShot();
			return "";
		}
	}

	public Map<String, String> getFareDetailsFromFlightInfoForReturnFlights(Log Log, ScreenShots ScreenShots) {
		Map<String, String> fareDetails = new HashMap<>();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Fare price (2nd occurrence)
			String farePrice = safeGetTextWithWait("(//div[contains(@class,'tg-fare-price')])[1]", "Fare Price", wait,
					Log, ScreenShots);
			fareDetails.put("FarePrice", farePrice);

			// Supplier name from image alt attribute (2nd occurrence)
			String supplierName = safeGetAttributeWithWait("(//img[contains(@class,'tg-supplier-img')])[1]", "alt",
					"Supplier Name", wait, Log, ScreenShots);
			fareDetails.put("Supplier", supplierName);

			// Fare type (2nd occurrence)
			String fareType = safeGetTextWithWait("(//div[contains(@class,'tg-fare-type')])[1]", "Fare Type", wait, Log,
					ScreenShots);
			fareDetails.put("FareType", fareType);

			// Policy type (2nd occurrence)
			String policy = safeGetTextWithWait(
					"(//div[@class='flight-journey-info-card__container']//div[contains(@class,'tg-policy')])[1]",
					"Policy Type", wait, Log, ScreenShots);
			fareDetails.put("policyType", policy);

			Log.ReportEvent("PASS", "Return flight fare details fetched successfully.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to fetch return flight fare details: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception in getFareDetailsFromFlightInfoForReturnFlights: " + e.getMessage());
		}

		return fareDetails;
	}

	// Method to Validate Actual and Expected Data with Messages for Both Pass and
	// Fail
	public void ValidateActualAndExpectedValuesForFlights(String actual, String expected, String message, Log log,
			ScreenShots ScreenShots) {
		try {
			if (actual.contentEquals(expected)) {
				log.ReportEvent("PASS",
						String.format("%s | Actual: '%s', Expected: '%s' - Values match.", message, actual, expected));
			} else {
				log.ReportEvent("FAIL", String.format("%s | Actual: '%s', Expected: '%s' - Values do not match.",
						message, actual, expected));
				ScreenShots.takeScreenShot();
				Assert.fail("Validation Failed: " + message);
			}
		} catch (Exception e) {
			log.ReportEvent("FAIL", String.format("%s | Actual: '%s', Expected: '%s' - Exception during comparison.",
					message, actual, expected));
			e.printStackTrace();
			Assert.fail("Exception during validation: " + message);
		}
	}

	public void ValidateNumericValuesForFlights(String actual, String expected, String message, Log log,
			ScreenShots screenShots) {
		try {
			// Remove all non-digit characters (currency symbols, commas, spaces, etc)
			String actualNumbers = actual.replaceAll("[^0-9]", "");
			String expectedNumbers = expected.replaceAll("[^0-9]", "");

			if (actualNumbers.equals(expectedNumbers)) {
				log.ReportEvent("PASS", String.format("%s | Actual: '%s', Expected: '%s' - Numeric values match.",
						message, actual, expected));
			} else {
				log.ReportEvent("FAIL", String.format(
						"%s | Actual: '%s', Expected: '%s' - Numeric values do not match.", message, actual, expected));
				screenShots.takeScreenShot();
				Assert.fail("Validation Failed: " + message);
			}
		} catch (Exception e) {
			log.ReportEvent("FAIL",
					String.format("%s | Actual: '%s', Expected: '%s' - Exception during numeric comparison.", message,
							actual, expected));
			e.printStackTrace();
			Assert.fail("Exception during validation: " + message);
		}
	}

	// Method to Click on Continue Button.
	public void handleReasonAndProceed(String reasonText) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Step 1: Click the "Continue" button
		WebElement continueBtn = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Continue']")));
		continueBtn.click();

		// Step 2: Handle optional "Airport Change" flow
		try {
			WebElement airlineChange = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Airport Change']")));
			WebElement yesContinue = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Yes, Continue']")));
			yesContinue.click();
		} catch (TimeoutException e) {
			System.out.println("Airport Change dialog not shown. Continuing without it.");
		}

		// Step 3: Handle optional "Reason for Selection" popup
		try {
			WebElement reasonPopup = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Reason for Selection']")));

			WebElement reasonOption = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + reasonText + "']")));
			reasonOption.click();

			WebElement proceedBtn = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Proceed to Booking']")));
			proceedBtn.click();
		} catch (TimeoutException e) {
			System.out.println("Reason for Selection popup not displayed. Proceeding without it.");
		}
	}

	// Method to Get the data from Ui on Result Screen.
	public Map<String, List<String>> getDataFromUiForResultScreenByXPath() throws InterruptedException {
		TestExecutionNotifier.showExecutionPopup();
		Thread.sleep(1000);
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Map of keys and their corresponding XPaths to extract
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("fromDepartureTime",
				"//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromdeptime')]");
		xpathMap.put("fromOrigin",
				"//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-mb-fromorigin')]");
		xpathMap.put("fromArrivalTime",
				"//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromarrtime')]");
		xpathMap.put("fromDestination",
				"//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromdestination')]");
		xpathMap.put("cabinClass", "//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("totalDuration",
				"//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-totalduration')]");
		xpathMap.put("connectionLineText",
				"//div[@class='oneway-card__flight-info']//div[contains(@class,'oneway-card__connect_line_text')]");

		for (Map.Entry<String, String> entry : xpathMap.entrySet()) {
			String key = entry.getKey();
			String xpath = entry.getValue();

			List<String> values = new ArrayList<>();

			try {
				// Wait for presence of at least one visible element matching the XPath
				wait.until(driver -> {
					List<WebElement> elements = driver.findElements(By.xpath(xpath));
					return !elements.isEmpty() && elements.stream().anyMatch(el -> !el.getText().trim().isEmpty());
				});

				List<WebElement> elements = driver.findElements(By.xpath(xpath));
				for (WebElement el : elements) {
					String text = el.getText().trim();
					if (!text.isEmpty()) {
						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout waiting for elements with xpath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for xpath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("Departing Flight UI Data (by XPath): " + data);
		return data;
	}

	public List<String> getFlightDetailsCombined() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		String flightNameXPath = "//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-flightcarrier')]";
		String flightNumberXPath = "//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-flightnumber')]";

		List<String> flightNames = new ArrayList<>();
		List<String> flightNumbers = new ArrayList<>();
		List<String> combinedFlights = new ArrayList<>();

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(flightNameXPath)));
			for (WebElement el : driver.findElements(By.xpath(flightNameXPath))) {
				String name = el.getText().trim();
				if (!name.isEmpty()) {
					flightNames.add(name);
				}
			}
		} catch (TimeoutException e) {
			System.out.println("No flight names found.");
		}

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(flightNumberXPath)));
			for (WebElement el : driver.findElements(By.xpath(flightNumberXPath))) {
				String number = el.getText().trim();
				if (!number.isEmpty()) {
					flightNumbers.add(number);
				}
			}
		} catch (TimeoutException e) {
			System.out.println("No flight numbers found.");
		}

		// Combine name + number into one list
		int max = Math.max(flightNames.size(), flightNumbers.size());
		for (int i = 0; i < max; i++) {
			String name = i < flightNames.size() ? flightNames.get(i) : "<missing name>";
			String number = i < flightNumbers.size() ? flightNumbers.get(i) : "<missing number>";
			combinedFlights.add(name + " - " + number);
		}

		// Optional print
		System.out.println("---------- Combined Flight Details ----------");
		combinedFlights.forEach(System.out::println);
		System.out.println("---------------------------------------------");

		return combinedFlights;
	}

	public void clickIndividualFlightsButton(Log Log) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			WebElement individualFlightsBtn = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Individual Flights']")));

			// Scroll into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
					individualFlightsBtn);

			try {
				// Normal click first
				individualFlightsBtn.click();
				Log.ReportEvent("PASS", "Clicked on 'Individual Flights' button successfully.");
				Thread.sleep(8000);
			} catch (ElementClickInterceptedException e) {
				// Fallback: JavaScript click
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", individualFlightsBtn);
				Log.ReportEvent("PASS", "Clicked on 'Individual Flights' button using JavaScript (fallback).");
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to click on 'Individual Flights' button. Error: " + e.getMessage());
			throw e;
		}
	}

	public String getOnlyDayAndMonthFromDateLabel() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		By dateLabel = By.xpath("(//div[@class='flight-search-header__content_wrapper'])[2]//div");

		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(dateLabel));
			String fullText = element.getText().trim(); // e.g., "17th Sep, 2025 - Outbound"

			// ✅ Extract "17th Sep" from "17th Sep, 2025 - Outbound"
			String[] parts = fullText.split(",");
			if (parts.length > 0) {
				String dayMonth = parts[0].trim(); // "17th Sep"
				System.out.println("Extracted Date: " + dayMonth);
				return dayMonth;
			} else {
				System.out.println("Unexpected date format: " + fullText);
				return "";
			}

		} catch (Exception e) {
			System.out.println("Error while extracting date: " + e.getMessage());
			return "";
		}
	}

	public String getFormattedJourneyDate() {
		// Locate the element
		String raw = driver.findElement(By.xpath("(//div[contains(@class,'flight-stepper_tab')]/parent::div//span)[1]"))
				.getText();

		// Convert "DEL - BLR (15th Sep)" → "DEL - BLR - 15th Sep"
		String formatted = raw.replace("(", "- ").replace(")", "").trim();

		return formatted;
	}

	public String getFormattedReturnDate() {
		// Locate the element
		String raw = driver.findElement(By.xpath("(//div[contains(@class,'flight-stepper_tab')]/parent::div//span)[2]"))
				.getText();

		// Convert "DEL - BLR (15th Sep)" → "DEL - BLR - 15th Sep"
		String formatted = raw.replace("(", "- ").replace(")", "").trim();

		return formatted;
	}

	public String getOnlyDayAndMonthDepartingDateLabel() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		By dateLabel = By.xpath("(//div[@class='flight-stepper']//span)[1]");

		try {
			// ✅ Wait until element is visible AND text matches the expected pattern
			// "(dd...mmm)"
			WebElement element = wait.until(driver -> {
				WebElement el = driver.findElement(dateLabel);
				String text = el.getText().trim();
				// Example: "DUR - HYD (15th Nov)"
				if (text.matches(".*\\(\\d{1,2}(st|nd|rd|th)?\\s+[A-Za-z]{3,}\\).*")) {
					return el;
				}
				return null; // keep waiting
			});

			String fullText = element.getText().trim();
			System.out.println("ℹ️ Full text found: " + fullText);

			// ✅ Extract text inside parentheses — e.g., (15th Nov)
			Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
			Matcher matcher = pattern.matcher(fullText);

			if (matcher.find()) {
				String dayMonth = matcher.group(1).trim(); // -> "15th Nov"
				System.out.println("✅ Extracted Date: " + dayMonth);
				return dayMonth;
			} else {
				System.out.println("⚠️ No parentheses found in: " + fullText);
				return "";
			}

		} catch (Exception e) {
			System.out.println("❌ Error while extracting date: " + e.getMessage());
			return "";
		}
	}

	public String getOnlyDayAndMonthReturnDateLabel() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		By dateLabel = By.xpath("(//div[@class='flight-stepper']//span)[2]");

		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(dateLabel));
			String fullText = element.getText().trim();
			// Example: "BLR - CCU (22nd Nov)"

			// ✅ Extract date inside parentheses — e.g., (22nd Nov)
			Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
			Matcher matcher = pattern.matcher(fullText);

			if (matcher.find()) {
				String dayMonth = matcher.group(1).trim(); // -> "22nd Nov"
				System.out.println("✅ Extracted Return Date: " + dayMonth);
				return dayMonth;
			} else {
				System.out.println("⚠️ No parentheses found in: " + fullText);
				return "";
			}

		} catch (Exception e) {
			System.out.println("❌ Error while extracting return date: " + e.getMessage());
			return "";
		}
	}

	public void validateDepartureDateIsDisplayed(String expectedDate, Log log, ScreenShots screenShots)
			throws InterruptedException {
		Thread.sleep(5000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		By depDatesLocator = By
				.xpath("//div[@class='relative flight-search-result__paper']//div[contains(@class,'tg-depdate')]");

		try {
			// ✅ Wait for at least one date element
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(depDatesLocator));
			List<WebElement> dateElements = driver.findElements(depDatesLocator);

			List<String> actualDates = new ArrayList<>();
			for (WebElement el : dateElements) {
				String text = el.getText().trim();
				if (!text.isEmpty()) {
					actualDates.add(text);
				}
			}

			System.out.println("Actual dates found on UI: " + actualDates);

			if (actualDates.contains(expectedDate)) {
				log.ReportEvent("PASS", "Expected date '" + expectedDate + "' is displayed on the screen.");
			} else {
				log.ReportEvent("FAIL", "Expected date '" + expectedDate + "' not found in UI. Found: " + actualDates);
				screenShots.takeScreenShot();
				Assert.fail("Date validation failed. '" + expectedDate + "' not present.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception while validating departure date: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception during date validation.");
		}
	}

	public void validateDepartureDateIsDisplayedForCombinedFlights(String expectedDate, Log log,
			ScreenShots screenShots) throws InterruptedException {
		Thread.sleep(5000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		By depDatesLocator = By.xpath("//div[contains(@class,'tg-fromdepdate')]");

		try {
			// ✅ Wait for at least one date element
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(depDatesLocator));
			List<WebElement> dateElements = driver.findElements(depDatesLocator);

			List<String> actualDates = new ArrayList<>();
			for (WebElement el : dateElements) {
				String text = el.getText().trim();
				if (!text.isEmpty()) {
					actualDates.add(text);
				}
			}

			System.out.println("Actual dates found on UI: " + actualDates);

			if (actualDates.contains(expectedDate)) {
				log.ReportEvent("PASS", "Expected date '" + expectedDate + "' is displayed on the screen.");
			} else {
				log.ReportEvent("FAIL", "Expected date '" + expectedDate + "' not found in UI. Found: " + actualDates);
				screenShots.takeScreenShot();
				Assert.fail("Date validation failed. '" + expectedDate + "' not present.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception while validating departure date: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception during date validation.");
		}
	}

	public void validateDepartureDateIsDisplayedForInternational(String expectedDate, Log log,
			ScreenShots screenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		By depDatesLocator = By.className("tg-fromdepdate");

		try {
			// ✅ Wait for at least one date element
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(depDatesLocator));
			List<WebElement> dateElements = driver.findElements(depDatesLocator);

			List<String> actualDates = new ArrayList<>();
			for (WebElement el : dateElements) {
				String text = el.getText().trim();
				if (!text.isEmpty()) {
					actualDates.add(text);
				}
			}

			System.out.println("Actual dates found on UI: " + actualDates);

			if (actualDates.contains(expectedDate)) {
				log.ReportEvent("PASS", "Expected date '" + expectedDate + "' is displayed on the screen.");
			} else {
				log.ReportEvent("FAIL", "Expected date '" + expectedDate + "' not found in UI. Found: " + actualDates);
				screenShots.takeScreenShot();
				Assert.fail("Date validation failed. '" + expectedDate + "' not present.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception while validating departure date: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception during date validation.");
		}
	}

	// Method to Slide the Slider from Min to Max and Max to Min
	public double[] moveLeftThumbToRightByPercentage(double percentageFromLeft) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement leftThumbInput = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[@data-index='0']//input[@type='range']")));

		double minSliderValue = Double.parseDouble(leftThumbInput.getAttribute("aria-valuemin"));
		double maxSliderValue = Double.parseDouble(leftThumbInput.getAttribute("aria-valuemax"));

		// Target value for left thumb
		double leftTargetValue = minSliderValue + ((maxSliderValue - minSliderValue) * percentageFromLeft / 100.0);
		double leftTargetPercent = (leftTargetValue - minSliderValue) / (maxSliderValue - minSliderValue);

		WebElement sliderTrack = driver.findElement(By.xpath("//span[contains(@class, 'MuiSlider-track')]"));
		int trackWidth = sliderTrack.getSize().getWidth();
		int trackStartX = sliderTrack.getLocation().getX();

		int targetOffsetX = (int) (trackWidth * leftTargetPercent);
		WebElement leftThumb = driver.findElement(By.xpath("//span[@data-index='0']"));
		int currentThumbX = leftThumb.getLocation().getX();

		// Move left thumb
		new Actions(driver).clickAndHold(leftThumb).moveByOffset((trackStartX + targetOffsetX) - currentThumbX, 0)
				.release().perform();

		Thread.sleep(500);

		double updatedMin = Double.parseDouble(leftThumbInput.getAttribute("aria-valuenow"));
		System.out.println("✅ Left thumb moved. New Min: " + updatedMin);
		return new double[] { updatedMin, maxSliderValue };
	}

	public void clickOnFiltersButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By filtersButton = By.xpath("//span[text()='Filters']");

		try {
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(filtersButton));
			button.click();
			System.out.println("✅ Clicked on 'Filters' button.");
		} catch (Exception e) {
			System.out.println("❌ Failed to click on 'Filters' button: " + e.getMessage());
			Assert.fail("Unable to click on 'Filters' button.");
		}
	}

	public void clickSmeFareButton(Log log, ScreenShots screenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement smeButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='SME/Corporate Fare']")));

			// Scroll into view using JavaScript
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", smeButton);
			Thread.sleep(500); // small pause to ensure it's fully visible

			smeButton.click();
			log.ReportEvent("PASS", "Clicked on SME Fare button successfully.");

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Failed to click on SME Fare button: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Could not click on SME Fare button.");
		}
	}

	public double[] moveRightThumbToLeftByPercentage(double percentageFromRight) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement rightThumbInput = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[@data-index='1']//input[@type='range']")));

		double minSliderValue = Double.parseDouble(rightThumbInput.getAttribute("aria-valuemin"));
		double maxSliderValue = Double.parseDouble(rightThumbInput.getAttribute("aria-valuemax"));

		// Target value for right thumb
		double rightTargetValue = maxSliderValue - ((maxSliderValue - minSliderValue) * percentageFromRight / 100.0);
		double rightTargetPercent = (rightTargetValue - minSliderValue) / (maxSliderValue - minSliderValue);

		WebElement sliderTrack = driver.findElement(By.xpath("//span[contains(@class, 'MuiSlider-track')]"));
		int trackWidth = sliderTrack.getSize().getWidth();
		int trackStartX = sliderTrack.getLocation().getX();

		int targetOffsetX = (int) (trackWidth * rightTargetPercent);
		WebElement rightThumb = driver.findElement(By.xpath("//span[@data-index='1']"));
		int currentThumbX = rightThumb.getLocation().getX();

		// Move right thumb
		new Actions(driver).clickAndHold(rightThumb).moveByOffset((trackStartX + targetOffsetX) - currentThumbX, 0)
				.release().perform();

		Thread.sleep(500);

		double updatedMax = Double.parseDouble(rightThumbInput.getAttribute("aria-valuenow"));
		System.out.println("✅ Right thumb moved. New Max: " + updatedMax);
		return new double[] { minSliderValue, updatedMax };
	}

	public void clickOnApplyButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By applyButton = By.xpath("//button[text()='Apply']");

		try {
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(applyButton));
			button.click();
			System.out.println("✅ Clicked on 'Apply' button.");
		} catch (Exception e) {
			System.out.println("❌ Failed to click on 'Apply' button: " + e.getMessage());
			Assert.fail("Unable to click on 'Apply' button.");
		}
	}

	public void validatePricesInRange(double minPrice, double maxPrice, Log log, ScreenShots screenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Step 1: Check for "No Flights Found" icon
		List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
		if (!noFlightsIcon.isEmpty()) {
			log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
			screenShots.takeScreenShot();
			Assert.fail("No flights found, validation aborted.");
			return; // Exit method early
		}

		List<WebElement> priceElements = wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-price')]")));

		boolean allPricesValid = true;

		for (WebElement priceElement : priceElements) {
			String priceText = priceElement.getText().trim().replaceAll("[^0-9]", ""); // Remove ₹ and commas
			if (priceText.isEmpty())
				continue;

			try {
				double price = Double.parseDouble(priceText);

				if (price < minPrice || price > maxPrice) {
					allPricesValid = false;
					log.ReportEvent("FAIL",
							"❌ Price out of range: ₹ " + price + " | Allowed: ₹ " + minPrice + " - ₹ " + maxPrice);
					screenShots.takeScreenShot();
				}
			} catch (NumberFormatException e) {
				log.ReportEvent("FAIL", "❌ Could not parse price: " + priceText);
				screenShots.takeScreenShot();
				allPricesValid = false;
			}
		}

		if (allPricesValid) {
			log.ReportEvent("PASS", "✅ All prices are within range ₹ " + minPrice + " - ₹ " + maxPrice);
		} else {
			Assert.fail("❌ Price validation failed: One or more prices are outside the expected range.");
		}
	}

	public void validatePricesInRangeForInternational(double minPrice, double maxPrice, Log log,
			ScreenShots screenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Step 1: Check for "No Flights Found" icon
		List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
		if (!noFlightsIcon.isEmpty()) {
			log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
			screenShots.takeScreenShot();
			Assert.fail("No flights found, validation aborted.");
			return; // Exit method early
		}

		List<WebElement> priceElements = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("tg-fl-price")));

		boolean allPricesValid = true;

		for (WebElement priceElement : priceElements) {
			String priceText = priceElement.getText().trim().replaceAll("[^0-9]", ""); // Remove ₹ and commas
			if (priceText.isEmpty())
				continue;

			try {
				double price = Double.parseDouble(priceText);

				if (price < minPrice || price > maxPrice) {
					allPricesValid = false;
					log.ReportEvent("FAIL",
							"❌ Price out of range: ₹ " + price + " | Allowed: ₹ " + minPrice + " - ₹ " + maxPrice);
					screenShots.takeScreenShot();
				}
			} catch (NumberFormatException e) {
				log.ReportEvent("FAIL", "❌ Could not parse price: " + priceText);
				screenShots.takeScreenShot();
				allPricesValid = false;
			}
		}

		if (allPricesValid) {
			log.ReportEvent("PASS", "✅ All prices are within range ₹ " + minPrice + " - ₹ " + maxPrice);
		} else {
			Assert.fail("❌ Price validation failed: One or more prices are outside the expected range.");
		}
	}

	// Method to Selects Stops
	public void selectStops(String... stops) throws InterruptedException {
		Thread.sleep(500);
		for (String stop : stops) {
			driver.findElement(By.xpath("//div[@class='stops-grid']/div[text()='" + stop + "']")).click();
			Thread.sleep(500);
		}

	}

	// Method to Selects Departing Sort Time
	public void selectDepartingTime(String... departingTime) throws InterruptedException {
		Thread.sleep(500);
		for (String time : departingTime) {
			driver.findElement(By.xpath("//div[text()='Departure']/parent::div//div[text()='" + time + "']")).click();
			Thread.sleep(500);
		}

	}

	// Method to Selects Departing Sort Time
	public void selectDepartingTimeForInternationalCombinedFlights(String... departingTime)
			throws InterruptedException {
		Thread.sleep(500);
		for (String time : departingTime) {
			driver.findElement(
					By.xpath("(//div[text()='Departure'])[1]//parent::div/parent::div//div[text()='" + time + "']"))
					.click();
			Thread.sleep(500);
		}

	}

	// Method to Selects Departing Sort Time
	public void selectReturnDepartingTimeForInternationalCombinedFlights(String... departingTime)
			throws InterruptedException {
		Thread.sleep(500);
		for (String time : departingTime) {
			driver.findElement(
					By.xpath("(//div[text()='Departure'])[2]//parent::div/parent::div//div[text()='" + time + "']"))
					.click();
			Thread.sleep(500);
		}

	}

	// Method to Selects Departing Sort Time
	public void selectArrivalTimeForInternationalCombinedFlights(String... departingTime) throws InterruptedException {
		Thread.sleep(500);
		for (String time : departingTime) {
			driver.findElement(
					By.xpath("(//div[text()='Arrival'])[1]//parent::div/parent::div//div[text()='" + time + "']"))
					.click();
			Thread.sleep(500);
		}

	}

	// Method to Selects Departing Sort Time
	public void selectReturnArrivalTimeForInternationalCombinedFlights(String... departingTime)
			throws InterruptedException {
		Thread.sleep(500);
		for (String time : departingTime) {
			driver.findElement(
					By.xpath("(//div[text()='Arrival'])[2]//parent::div/parent::div//div[text()='" + time + "']"))
					.click();
			Thread.sleep(500);
		}

	}

	// Method to Selects Departing Sort Time
	public void selectArrivalTime(String... arrivalTime) throws InterruptedException {
		Thread.sleep(500);
		for (String time : arrivalTime) {
			driver.findElement(By.xpath("//div[text()='Arrival']/parent::div//div[text()='" + time + "']")).click();
			Thread.sleep(500);
		}

	}

	public void validateAllStopsMatchAnyExpected(List<String> expectedStops, Log log, ScreenShots screenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Step 1: Check for "No Flights Found" icon
		List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
		if (!noFlightsIcon.isEmpty()) {
			log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
			screenShots.takeScreenShot();
			Assert.fail("No flights found, validation aborted.");
			return; // Exit method early
		}
		List<WebElement> stopsElements = wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-stops')]")));

		boolean allMatch = true;
		StringBuilder mismatchInfo = new StringBuilder();

		for (int i = 0; i < stopsElements.size(); i++) {
			String actualStop = stopsElements.get(i).getText().trim();

			// Check if actualStop matches any expected stop (case-insensitive)
			boolean matchesAny = expectedStops.stream().anyMatch(expected -> expected.equalsIgnoreCase(actualStop));

			if (!matchesAny) {
				allMatch = false;
				mismatchInfo.append(String.format("Actual stop '%s' at index %d does not match any expected stops.%n",
						actualStop, i + 1));
			}
		}

		if (allMatch) {
			log.ReportEvent("PASS", "All actual stops matched at least one expected stop.");
		} else {
			log.ReportEvent("FAIL", "Some actual stops did not match expected stops:\n" + mismatchInfo.toString());
			screenShots.takeScreenShot();
			Assert.fail("Stops text validation failed - mismatches found.");
		}
	}

	public void validateAllStopsMatchAnyExpectedForCombinedFlights(List<String> expectedStops, Log log,
			ScreenShots screenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Step 1: Check for "No Flights Found" icon
		List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
		if (!noFlightsIcon.isEmpty()) {
			log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
			screenShots.takeScreenShot();
			Assert.fail("No flights found, validation aborted.");
			return; // Exit method early
		}
		List<WebElement> stopsElements = wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-tostops')]")));

		boolean allMatch = true;
		StringBuilder mismatchInfo = new StringBuilder();

		for (int i = 0; i < stopsElements.size(); i++) {
			String actualStop = stopsElements.get(i).getText().trim();

			// Check if actualStop matches any expected stop (case-insensitive)
			boolean matchesAny = expectedStops.stream().anyMatch(expected -> expected.equalsIgnoreCase(actualStop));

			if (!matchesAny) {
				allMatch = false;
				mismatchInfo.append(String.format("Actual stop '%s' at index %d does not match any expected stops.%n",
						actualStop, i + 1));
			}
		}

		if (allMatch) {
			log.ReportEvent("PASS", "All actual stops matched at least one expected stop.");
		} else {
			log.ReportEvent("FAIL", "Some actual stops did not match expected stops:\n" + mismatchInfo.toString());
			screenShots.takeScreenShot();
			Assert.fail("Stops text validation failed - mismatches found.");
		}
	}

	// Method to Validate Stops
	public void validateAllStopsMatchExpected(String expectedStop, Log log, ScreenShots screenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Step 1: Check for "No Flights Found" icon
			List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
			if (!noFlightsIcon.isEmpty()) {
				log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
				screenShots.takeScreenShot();
				Assert.fail("No flights found, validation aborted.");
				return; // Exit method early
			}

			// Step 2: Fetch all stop elements
			List<WebElement> stopsElements = wait.until(
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-stops')]")));

			boolean allMatch = true;
			StringBuilder mismatchInfo = new StringBuilder();

			// Step 3: Compare stops with expected value
			for (int i = 0; i < stopsElements.size(); i++) {
				String actualStop = stopsElements.get(i).getText().trim();

				if (!expectedStop.equalsIgnoreCase(actualStop)) {
					allMatch = false;
					mismatchInfo
							.append(String.format("Actual stop '%s' at index %d does not match expected stop '%s'.%n",
									actualStop, i + 1, expectedStop));
				}
			}

			// Step 4: Log results
			if (allMatch) {
				log.ReportEvent("PASS", "All actual stops matched the expected stop '" + expectedStop + "'.");
			} else {
				log.ReportEvent("FAIL", "Some actual stops did not match:\n" + mismatchInfo.toString());
				screenShots.takeScreenShot();
				Assert.fail("Stops text validation failed - mismatches found.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating stops: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating stops.");
		}
	}

	// Method to Validate Stops
	public void validateAllStopsMatchExpectedForCombinedFlights(String expectedStop, Log log, ScreenShots screenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Step 1: Check for "No Flights Found" icon
			List<WebElement> noFlightsIcon = driver.findElements(By.xpath("//*[@data-testid='AirplaneIcon']"));
			if (!noFlightsIcon.isEmpty()) {
				log.ReportEvent("FAIL", "No Flights Found - AirplaneIcon is displayed.");
				screenShots.takeScreenShot();
				Assert.fail("No flights found, validation aborted.");
				return; // Exit method early
			}

			// Step 2: Fetch all stop elements
			List<WebElement> stopsElements = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-tostops')]")));

			boolean allMatch = true;
			StringBuilder mismatchInfo = new StringBuilder();

			// Step 3: Compare stops with expected value
			for (int i = 0; i < stopsElements.size(); i++) {
				String actualStop = stopsElements.get(i).getText().trim();

				if (!expectedStop.equalsIgnoreCase(actualStop)) {
					allMatch = false;
					mismatchInfo
							.append(String.format("Actual stop '%s' at index %d does not match expected stop '%s'.%n",
									actualStop, i + 1, expectedStop));
				}
			}

			// Step 4: Log results
			if (allMatch) {
				log.ReportEvent("PASS", "All actual stops matched the expected stop '" + expectedStop + "'.");
			} else {
				log.ReportEvent("FAIL", "Some actual stops did not match:\n" + mismatchInfo.toString());
				screenShots.takeScreenShot();
				Assert.fail("Stops text validation failed - mismatches found.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating stops: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating stops.");
		}
	}

	public String getTotalCost() {
		// Locate the element just before "Total Cost"
		String raw = driver.findElement(By.xpath("//div[text()='Total Cost']/preceding-sibling::div")).getText();

		// Optional: Clean spaces/commas/currency symbols if needed
		String cost = raw.trim();

		return cost;
	}

	public String getTotalCostWhenOtherCountryCurrencySelected() {
		// Locate the element just before "Total Cost"
		String raw = driver.findElement(By.xpath("(//div[text()='Total Cost']/preceding-sibling::div)[1]")).getText();

		// Optional: Clean spaces/commas/currency symbols if needed
		String cost = raw.trim();

		return cost;
	}

	// Method to Get the data from Ui on Result Screen.
	public Map<String, List<String>> getDataFromUiForResultScreenByXPathForInternation() throws InterruptedException {
		TestExecutionNotifier.showExecutionPopup();
		Thread.sleep(1000);
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Map of keys and their corresponding XPaths to extract
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("fromDepartureTime",
				"//div[text()='Outbound Flight']/parent::div/following-sibling::div//div[contains(@class,'tg-fromdeptime')]");
		xpathMap.put("fromOrigin",
				"//div[text()='Outbound Flight']/parent::div/following-sibling::div//div[contains(@class,'tg-mb-fromorigin')]");
		xpathMap.put("fromArrivalTime",
				"//div[text()='Outbound Flight']/parent::div/following-sibling::div//div[contains(@class,'tg-fromarrtime')]");
		xpathMap.put("fromDestination",
				"//div[text()='Outbound Flight']/parent::div/following-sibling::div//div[contains(@class,'tg-fromdestination')]");
		xpathMap.put("cabinClass",
				"//div[text()='Outbound Flight']/parent::div/following-sibling::div//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("totalDuration",
				"//div[text()='Outbound Flight']/parent::div/following-sibling::div//div[contains(@class,' tg-totalduration')]");
		xpathMap.put("connectionLineText",
				"//div[text()='Outbound Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//span[@aria-label]");

		for (Map.Entry<String, String> entry : xpathMap.entrySet()) {
			String key = entry.getKey();
			String xpath = entry.getValue();

			List<String> values = new ArrayList<>();

			try {
				// Wait for presence of at least one visible element matching the XPath
				wait.until(driver -> {
					List<WebElement> elements = driver.findElements(By.xpath(xpath));
					return !elements.isEmpty() && elements.stream().anyMatch(el -> !el.getText().trim().isEmpty());
				});

				List<WebElement> elements = driver.findElements(By.xpath(xpath));
				for (WebElement el : elements) {
					String text = el.getText().trim();
					if (!text.isEmpty()) {
						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout waiting for elements with xpath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for xpath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("Departing Flight UI Data (by XPath): " + data);
		return data;
	}

	// Method to Get the data from Ui on Result Screen.
	public Map<String, List<String>> getDataFromUiForResultScreenByXPathForInternationReturnFlights()
			throws InterruptedException {
		TestExecutionNotifier.showExecutionPopup();
		Thread.sleep(1000);
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Map of keys and their corresponding XPaths to extract
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("fromDepartureTime",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-todeptime')]");
		xpathMap.put("fromOrigin",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-mb-toorigin')]");
		xpathMap.put("fromArrivalTime",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-toarrtime')]");
		xpathMap.put("fromDestination",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-todestination')]");
		xpathMap.put("cabinClass",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("totalDuration",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,' tg-totalduration')]");
		xpathMap.put("connectionLineText",
				"//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//span[@aria-label]");

		for (Map.Entry<String, String> entry : xpathMap.entrySet()) {
			String key = entry.getKey();
			String xpath = entry.getValue();

			List<String> values = new ArrayList<>();

			try {
				// Wait for presence of at least one visible element matching the XPath
				wait.until(driver -> {
					List<WebElement> elements = driver.findElements(By.xpath(xpath));
					return !elements.isEmpty() && elements.stream().anyMatch(el -> !el.getText().trim().isEmpty());
				});

				List<WebElement> elements = driver.findElements(By.xpath(xpath));
				for (WebElement el : elements) {
					String text = el.getText().trim();
					if (!text.isEmpty()) {
						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout waiting for elements with xpath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for xpath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("Return Flight UI Data (by XPath): " + data);
		return data;
	}

	public List<String> getFlightDetailsCombinedForDepartingFlights() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		String flightNameXPath = "//div[text()='Outbound Flight']//parent::div//parent::div[@class='intro-flight-card']//div[contains(@class,'tg-flightcarrier') and not(contains(@class,'tg-typography_subtitle-7'))]";
		String flightNumberXPath = "//div[text()='Outbound Flight']//parent::div//parent::div[@class='intro-flight-card']//div[contains(@class,'tg-flightnumber')]";

		List<String> flightNames = new ArrayList<>();
		List<String> flightNumbers = new ArrayList<>();
		List<String> combinedFlights = new ArrayList<>();

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(flightNameXPath)));
			for (WebElement el : driver.findElements(By.xpath(flightNameXPath))) {
				String name = el.getText().trim();
				if (!name.isEmpty()) {
					flightNames.add(name);
				}
			}
		} catch (TimeoutException e) {
			System.out.println("No flight names found.");
		}

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(flightNumberXPath)));
			for (WebElement el : driver.findElements(By.xpath(flightNumberXPath))) {
				String number = el.getText().trim();
				if (!number.isEmpty()) {
					flightNumbers.add(number);
				}
			}
		} catch (TimeoutException e) {
			System.out.println("No flight numbers found.");
		}

		// Combine name + number into one list
		int max = Math.max(flightNames.size(), flightNumbers.size());
		for (int i = 0; i < max; i++) {
			String name = i < flightNames.size() ? flightNames.get(i) : "<missing name>";
			String number = i < flightNumbers.size() ? flightNumbers.get(i) : "<missing number>";
			combinedFlights.add(name + " - " + number);
		}

		// Optional print
		System.out.println("---------- Combined Flight Details ----------");
		combinedFlights.forEach(System.out::println);
		System.out.println("---------------------------------------------");

		return combinedFlights;
	}

	public List<String> getFlightDetailsCombinedForReturnFlights() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		String flightNameXPath = "//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-flightcarrier') and not(contains(@class,'tg-typography_subtitle-7'))]";
		String flightNumberXPath = "//div[text()='Return Flight']//parent::div//parent::div[contains(@class,'intro-flight-card')]//div[contains(@class,'tg-flightnumber')]";

		List<String> flightNames = new ArrayList<>();
		List<String> flightNumbers = new ArrayList<>();
		List<String> combinedFlights = new ArrayList<>();

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(flightNameXPath)));
			for (WebElement el : driver.findElements(By.xpath(flightNameXPath))) {
				String name = el.getText().trim();
				if (!name.isEmpty()) {
					flightNames.add(name);
				}
			}
		} catch (TimeoutException e) {
			System.out.println("No flight names found.");
		}

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(flightNumberXPath)));
			for (WebElement el : driver.findElements(By.xpath(flightNumberXPath))) {
				String number = el.getText().trim();
				if (!number.isEmpty()) {
					flightNumbers.add(number);
				}
			}
		} catch (TimeoutException e) {
			System.out.println("No flight numbers found.");
		}

		// Combine name + number into one list
		int max = Math.max(flightNames.size(), flightNumbers.size());
		for (int i = 0; i < max; i++) {
			String name = i < flightNames.size() ? flightNames.get(i) : "<missing name>";
			String number = i < flightNumbers.size() ? flightNumbers.get(i) : "<missing number>";
			combinedFlights.add(name + " - " + number);
		}

		// Optional print
		System.out.println("---------- Combined Flight Details ----------");
		combinedFlights.forEach(System.out::println);
		System.out.println("---------------------------------------------");

		return combinedFlights;
	}

	public Map<String, String> getFareDetailsFromFlightInfoForInternational(Log Log, ScreenShots ScreenShots) {
		Map<String, String> fareDetails = new HashMap<>();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Fare price
			String farePrice = safeGetTextWithWait("//div[contains(@class,'tg-fare-price')]", "Fare Price", wait, Log,
					ScreenShots);
			fareDetails.put("FarePrice", farePrice);

			// Supplier name from image alt attribute
			String supplierName = safeGetAttributeWithWait("//img[contains(@class,'tg-supplier-img')]", "alt",
					"Supplier Name", wait, Log, ScreenShots);
			fareDetails.put("Supplier", supplierName);

			// Fare type
			String fareType = safeGetTextWithWait("//div[contains(@class,'tg-fare-type')]", "Fare Type", wait, Log,
					ScreenShots);
			fareDetails.put("FareType", fareType);

			// Policy type
			String policy = safeGetTextWithWait(
					"//div[@class='flight-journey-info-card__title']/parent::div//div[contains(@class,'tg-policy')]",
					"Policy Type", wait, Log, ScreenShots);
			fareDetails.put("policyType", policy);

			Log.ReportEvent("PASS", "International flight fare details fetched successfully.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to fetch international flight fare details: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception in getFareDetailsFromFlightInfoForInternational: " + e.getMessage());
		}

		return fareDetails;
	}

	// Safe method to check element presence
	private boolean isElementPresent(String xpath) {
		try {
			return !driver.findElements(By.xpath(xpath)).isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * public String[] clickOnSelectBasedOnFareType(String fareType, Log Log,
	 * ScreenShots ScreenShots) { String price = null; String fare = null; String
	 * supplierName = null; String cancelationData = null; String changeData = null;
	 * String cabinData = null; String checkInData = null; String policy = null;
	 * 
	 * try { Thread.sleep(5000); TestExecutionNotifier.showExecutionPopup();
	 * 
	 * List<WebElement> fareElements = driver.findElements(By.xpath(
	 * "//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType + "']"));
	 * 
	 * if (!fareElements.isEmpty() && fareElements.get(0).isDisplayed()) {
	 * WebElement continueButton = driver.findElement(By.xpath(
	 * "//div[contains(@class,'tg-fare-type') and text()='" + fareType +
	 * "']/parent::div/parent::div/following-sibling::div[@class='action']//button")
	 * ); scrollToElement(continueButton);
	 * 
	 * // Always present price =
	 * safeGetText("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType + "']/following-sibling::div", "Price", Log, ScreenShots);
	 * supplierName =
	 * safeGetAttribute("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType + "']/parent::div/following-sibling::div//img", "alt", "Supplier",
	 * Log, ScreenShots);
	 * 
	 * // Only fetch if present String cancelXpath =
	 * "//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType +
	 * "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[@data-tgflpenaltytype='CancelPenalty']/div";
	 * if (isElementPresent(cancelXpath)) { cancelationData =
	 * safeGetText(cancelXpath, "Cancelation", Log, ScreenShots); }
	 * 
	 * String changeXpath =
	 * "//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType +
	 * "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[@data-tgflpenaltytype='ChangePenalty']/div";
	 * if (isElementPresent(changeXpath)) { changeData = safeGetText(changeXpath,
	 * "Change", Log, ScreenShots); }
	 * 
	 * // cabinData =
	 * safeGetText("((//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType +
	 * "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[contains(@class,'MuiGrid2-container')])[3]//div[@data-tgflcheckinbaggage])[2]/div",
	 * "Cabin Baggage", Log, ScreenShots); // checkInData =
	 * safeGetText("((//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType +
	 * "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[contains(@class,'MuiGrid2-container')])[3]//div[@data-tgflcabinbaggage])[2]/div",
	 * "Check-In Baggage", Log, ScreenShots); policy =
	 * safeGetText("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
	 * + fareType +
	 * "']/parent::div/following-sibling::div//div[contains(@class,'tg-policy')]",
	 * "data-tginpolicy", Log, ScreenShots);
	 * 
	 * Thread.sleep(2000); continueButton.click(); fare = fareType;
	 * Log.ReportEvent("PASS", "Fare type '" + fareType +
	 * "' selected successfully.");
	 * 
	 * } else { Log.ReportEvent("INFO", "Fare type '" + fareType +
	 * "' not found, falling back to first available option."); WebElement
	 * continueButton =
	 * driver.findElement(By.xpath("(//div[@class='action']//button)[1]"));
	 * scrollToElement(continueButton);
	 * 
	 * price = safeGetText(
	 * "(//div[@class='relative']//div[contains(@class,'tg-fare-price')])[1]",
	 * "Default Price", Log, ScreenShots); supplierName =
	 * safeGetAttribute("(//div[@class='relative']//img)[1]", "alt",
	 * "Default Supplier", Log, ScreenShots); fare = safeGetText(
	 * "(//div[@class='relative']//div[contains(@class,'tg-fare-type')])[1]",
	 * "Default Fare Type", Log, ScreenShots);
	 * 
	 * // Only fetch if present String cancelXpath =
	 * "(//div[@data-tgflpenaltytype='CancelPenalty']/div)[1]"; if
	 * (isElementPresent(cancelXpath)) { cancelationData = safeGetText(cancelXpath,
	 * "Default Cancelation", Log, ScreenShots); }
	 * 
	 * String changeXpath = "(//div[@data-tgflpenaltytype='ChangePenalty']/div)[1]";
	 * if (isElementPresent(changeXpath)) { changeData = safeGetText(changeXpath,
	 * "Default Change", Log, ScreenShots); }
	 * 
	 * // cabinData = safeGetText(
	 * "(//div[contains(@class,'MuiGrid2-container')]//div[@data-tgflcheckinbaggage])[3]/div",
	 * "Default Cabin", Log, ScreenShots); // checkInData = safeGetText(
	 * "(//div[contains(@class,'MuiGrid2-container')]//div[@data-tgflcabinbaggage])[3]/div",
	 * "Default Check-In", Log, ScreenShots); policy = safeGetText(
	 * "(//div[@class='relative']//div[contains(@class,'tg-fare-type')])[1]/parent::div//parent::div/following-sibling::div//div[contains(@class,'tg-policy')]",
	 * "data-tginpolicy", Log, ScreenShots);
	 * 
	 * Thread.sleep(2000); continueButton.click(); Log.ReportEvent("PASS",
	 * "Default fare selected successfully."); }
	 * 
	 * } catch (Exception e) { handleBackendOrGenericFailure(e, Log, ScreenShots); }
	 * 
	 * return new String[]{price, fare, supplierName, cancelationData, changeData,
	 * cabinData, checkInData, policy}; }
	 */

	public String[] clickOnSelectBasedOnFareType(String fareType, Log Log, ScreenShots ScreenShots) {
		String price = null;
		String fare = null;
		String supplierName = null;
		String cancelationData = null;
		String changeData = null;
		String cabinData = null;
		String checkInData = null;
		String policy = null;
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			// Wait for results to settle
			Thread.sleep(5000);
			TestExecutionNotifier.showExecutionPopup();

			// --- STEP 1: INITIAL SCROLL TO START OF FARE SECTION ---
			// This ensures the 1st card is in context before searching for specific fare
			// types
			try {
				WebElement fareContainer = driver
						.findElement(By.xpath("//div[contains(@class,'fare-options-container')]"));
				js.executeScript("arguments[0].scrollIntoView({block: 'start', behavior: 'instant'});", fareContainer);
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Optional scroll to container failed, continuing search...");
			}

			List<WebElement> fareElements = driver.findElements(By.xpath(
					"//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType + "']"));

			if (!fareElements.isEmpty() && fareElements.get(0).isDisplayed()) {
				WebElement continueButton = driver
						.findElement(By.xpath("//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/parent::div/following-sibling::div[@class='action']//button"));

				// --- STEP 2: PROPER SCROLL TO TARGET BUTTON ---
				// Using center alignment to avoid sticky headers/footers
				js.executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", continueButton);
				Thread.sleep(1500);

				// Data extraction using your exact XPaths
				price = safeGetText("//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
						+ fareType + "']/following-sibling::div", "Price", Log, ScreenShots);
				supplierName = safeGetAttribute(
						"//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/following-sibling::div//img",
						"alt", "Supplier", Log, ScreenShots);

				String cancelXpath = "//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
						+ fareType
						+ "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[@data-tgflpenaltytype='CancelPenalty']/div";
				if (isElementPresent(cancelXpath)) {
					cancelationData = safeGetText(cancelXpath, "Cancelation", Log, ScreenShots);
				}

				String changeXpath = "//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='"
						+ fareType
						+ "']/parent::div/parent::div/following-sibling::div[@class='fare-card-body']//div[@data-tgflpenaltytype='ChangePenalty']/div";
				if (isElementPresent(changeXpath)) {
					changeData = safeGetText(changeXpath, "Change", Log, ScreenShots);
				}

				policy = safeGetText(
						"//div[@class='relative']//div[contains(@class,'tg-fare-type') and text()='" + fareType
								+ "']/parent::div/following-sibling::div//div[contains(@class,'tg-policy')]",
						"data-tginpolicy", Log, ScreenShots);

				Thread.sleep(2000);

				// --- STEP 3: ROBUST CLICK ---
				try {
					continueButton.click();
				} catch (Exception e) {
					// Fallback for intercepted click
					js.executeScript("arguments[0].click();", continueButton);
				}

				fare = fareType;
				Log.ReportEvent("PASS", "Fare type '" + fareType + "' selected successfully.");

			} else {
				Log.ReportEvent("INFO",
						"Fare type '" + fareType + "' not found, falling back to first available option.");
				WebElement continueButton = driver.findElement(By.xpath("(//div[@class='action']//button)[1]"));

				// Proper scroll for fallback button
				js.executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", continueButton);
				Thread.sleep(1500);

				price = safeGetText("(//div[@class='relative']//div[contains(@class,'tg-fare-price')])[1]",
						"Default Price", Log, ScreenShots);
				supplierName = safeGetAttribute("(//div[@class='relative']//img)[1]", "alt", "Default Supplier", Log,
						ScreenShots);
				fare = safeGetText("(//div[@class='relative']//div[contains(@class,'tg-fare-type')])[1]",
						"Default Fare Type", Log, ScreenShots);

				String cancelXpath = "(//div[@data-tgflpenaltytype='CancelPenalty']/div)[1]";
				if (isElementPresent(cancelXpath)) {
					cancelationData = safeGetText(cancelXpath, "Default Cancelation", Log, ScreenShots);
				}

				String changeXpath = "(//div[@data-tgflpenaltytype='ChangePenalty']/div)[1]";
				if (isElementPresent(changeXpath)) {
					changeData = safeGetText(changeXpath, "Default Change", Log, ScreenShots);
				}

				policy = safeGetText(
						"(//div[@class='relative']//div[contains(@class,'tg-fare-type')])[1]/parent::div//parent::div/following-sibling::div//div[contains(@class,'tg-policy')]",
						"data-tginpolicy", Log, ScreenShots);

				Thread.sleep(2000);

				try {
					continueButton.click();
				} catch (Exception e) {
					js.executeScript("arguments[0].click();", continueButton);
				}

				Log.ReportEvent("PASS", "Default fare selected successfully.");
			}

		} catch (Exception e) {
			handleBackendOrGenericFailure(e, Log, ScreenShots);
		}

		return new String[] { price, fare, supplierName, cancelationData, changeData, cabinData, checkInData, policy };
	}

	private void scrollToElement(WebElement element) throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		((JavascriptExecutor) driver).executeScript(
				"window.scrollTo({ top: arguments[0].getBoundingClientRect().top + window.scrollY - 100, behavior: 'smooth' });",
				element);
		Thread.sleep(1000);
	}

	private String safeGetText(String xpath, String label, Log Log, ScreenShots ScreenShots) {
		try {
			return driver.findElement(By.xpath(xpath)).getText();
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve " + label + ": " + e.getMessage());
			ScreenShots.takeScreenShot();
			return null;
		}
	}

	private String safeGetAttribute(String xpath, String attr, String label, Log Log, ScreenShots ScreenShots) {
		try {
			return driver.findElement(By.xpath(xpath)).getAttribute(attr);
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve " + label + " attribute: " + e.getMessage());
			ScreenShots.takeScreenShot();
			return null;
		}
	}

	private static AtomicInteger backEndIssueCount = new AtomicInteger(0);

	private void handleBackendOrGenericFailure(Exception e, Log Log, ScreenShots ScreenShots) {
		try {
			if (isElementPresent(By.xpath("//p[@class='toast-title']"))) {
				WebElement snackbar = driver.findElement(By.xpath("//p[@class='toast-title']"));
				String errorMessage = snackbar.getText().trim();
				int currentCount = backEndIssueCount.incrementAndGet();

				Log.ReportEvent("FAIL", "Issue from BackEnd: " + errorMessage + " | Count: " + currentCount);
				ScreenShots.takeScreenShot();
				Assert.fail("BackEnd Issue: " + errorMessage);
			} else {
				Log.ReportEvent("FAIL", "General failure: " + e.getMessage());
				ScreenShots.takeScreenShot();
				Assert.fail("Exception in clickOnSelectBasedOnFareType: " + e.getMessage());
			}
		} catch (Exception ex) {
			Log.ReportEvent("FAIL", "Error during error handling: " + ex.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Double failure in clickOnSelectBasedOnFareType");
		}
	}

	// Function to get List of Airlines
	public List<String> getAllAirlineNames(Log Log, ScreenShots ScreenShots) {
		List<String> airlineNames = new ArrayList<>();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> airlineElements = wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//span[@class='airline-name']")));

			for (WebElement element : airlineElements) {
				String name = element.getText().trim();
				if (!name.isEmpty()) {
					airlineNames.add(name);
				}
			}

			Log.ReportEvent("PASS", "Fetched airline names: " + airlineNames.toString());

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to fetch airline names: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}

		return airlineNames;
	}

	// Function to get List of Airlines
	public List<String> getAllLayoverNames(Log Log, ScreenShots ScreenShots) {
		List<String> airlineNames = new ArrayList<>();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> airlineElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
					"//div[text()='Layover Airports']/parent::div/parent::div/parent::div//span[@class='airline-name']")));

			for (WebElement element : airlineElements) {
				String name = element.getText().trim();
				if (!name.isEmpty()) {
					airlineNames.add(name);
				}
			}

			Log.ReportEvent("PASS", "Fetched airline names: " + airlineNames.toString());

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to fetch airline names: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}

		return airlineNames;
	}

	// Method to Select airlines
	public void clickOnAirlineByName(String airlineName) {
		try {
			String xpath = "//span[@class='airline-name' and text()='" + airlineName + "']";
			WebElement airlineElement = driver.findElement(By.xpath(xpath));
			airlineElement.click();
			System.out.println("Clicked on airline: " + airlineName);
		} catch (Exception e) {
			System.out.println("Failed to click on airline: " + airlineName + ". Error: " + e.getMessage());
		}
	}

	// Method to Select airlines
	public void clickOnLayoverByName(String layoverName) {
		try {
			String xpath = "//div[@data-tgflairport='" + layoverName + "']//label";
			WebElement airlineElement = driver.findElement(By.xpath(xpath));
			airlineElement.click();
			System.out.println("Clicked on airline: " + layoverName);
		} catch (Exception e) {
			System.out.println("Failed to click on airline: " + layoverName + ". Error: " + e.getMessage());
		}
	}

	// Method to Validate airline Names
	public void validateAirLinesListDisplayedBasedOnUserSearch(Log Log, ScreenShots ScreenShots,
			String... airlineNames) {
		TestExecutionNotifier.showExecutionPopup();
		try {
			// Get all airlines displayed on the result page
			List<WebElement> airlinesList = driver.findElements(By.className("tg-flightcarrier"));
			List<String> displayedAirlines = new ArrayList<>();
			for (WebElement airlines : airlinesList) {
				String airline = airlines.getText().trim();
				displayedAirlines.add(airline);
			}

			boolean allMatch = true;

			// Check each airline passed to the method
			for (String expectedAirline : airlineNames) {
				// Check if any displayed airline contains expected airline name
				// (case-insensitive)
				boolean found = displayedAirlines.stream().anyMatch(a -> a.equalsIgnoreCase(expectedAirline.trim())
						|| a.toLowerCase().contains(expectedAirline.trim().toLowerCase()));

				if (found) {
					Log.ReportEvent("PASS", "Expected airline is showing: " + expectedAirline);
				} else {
					Log.ReportEvent("FAIL", "Expected airline NOT found: " + expectedAirline);
					allMatch = false;
				}
			}

			if (allMatch) {
				Log.ReportEvent("PASS", "All selected airlines are correctly shown in the results screen.");
			} else {
				Log.ReportEvent("FAIL", "All selected airlines are not shown in the results screen.");
				Assert.fail();
			}
			// Print all displayed airlines
			System.out.println("Displayed Airlines:");
			displayedAirlines.forEach(System.out::println);

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while validating airlines list: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();
		}
	}

	// Method to Validate airline Names
	public void validateAirLinesListDisplayedBasedOnUserSearchForInternational(Log Log, ScreenShots ScreenShots,
			String... airlineNames) {
		TestExecutionNotifier.showExecutionPopup();
		try {
			// Get all airlines displayed on the result page
			List<WebElement> airlinesList = driver.findElements(By.className("tg-flightcarrier"));
			List<String> displayedAirlines = new ArrayList<>();
			for (WebElement airlines : airlinesList) {
				String airline = airlines.getText().trim();
				displayedAirlines.add(airline);
			}

			boolean allMatch = true;

			// Check each airline passed to the method
			for (String expectedAirline : airlineNames) {
				// Check if any displayed airline contains expected airline name
				// (case-insensitive)
				boolean found = displayedAirlines.stream().anyMatch(a -> a.equalsIgnoreCase(expectedAirline.trim())
						|| a.toLowerCase().contains(expectedAirline.trim().toLowerCase()));

				if (found) {
					Log.ReportEvent("PASS", "Expected airline is showing: " + expectedAirline);
				} else {
					Log.ReportEvent("FAIL", "Expected airline NOT found: " + expectedAirline);
					allMatch = false;
				}
			}

			if (allMatch) {
				Log.ReportEvent("PASS", "All selected airlines are correctly shown in the results screen.");
			} else {
				Log.ReportEvent("FAIL", "All selected airlines are not shown in the results screen.");
				Assert.fail();
			}
			// Print all displayed airlines
			System.out.println("Displayed Airlines:");
			displayedAirlines.forEach(System.out::println);

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while validating airlines list: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();
		}
	}

	// Method to Validate SME or Corporate Fare.
	public void validateFareTypesDisplayed(Log log, ScreenShots screenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Define allowed fare types
			List<String> allowedFareTypes = Arrays.asList("SME", "Corporate");
			boolean allValid = true;
			List<String> fareTypes = new ArrayList<>();

			// Fetch elements
			List<WebElement> fareTypeElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type')]")));

			for (int i = 0; i < fareTypeElements.size(); i++) {
				// Refetch element by index to avoid stale reference
				WebElement element = driver
						.findElements(By.xpath("//div[@class='relative']//div[contains(@class,'tg-fare-type')]"))
						.get(i);
				String text = element.getText().trim();
				if (!text.isEmpty()) {
					fareTypes.add(text);
					if (!allowedFareTypes.contains(text)) {
						allValid = false;
					}
				}
			}

			if (allValid && !fareTypes.isEmpty()) {
				log.ReportEvent("PASS", "Fare types displayed are valid: " + fareTypes);
			} else if (fareTypes.isEmpty()) {
				log.ReportEvent("FAIL", "No fare types are displayed on the page.");
				screenShots.takeScreenShot();
				Assert.fail("No fare types displayed on the page.");
			} else {
				log.ReportEvent("FAIL", "Invalid fare types displayed: " + fareTypes);
				screenShots.takeScreenShot();
				Assert.fail("Some fare types are not valid (allowed: SME or Corporate).");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating fare types: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating fare types.");
		}
	}

	// Method to click On Currency DropDown
	public void clickOnCurrencyDropdown(Log Log, ScreenShots ScreenShots) {
		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//			// Locate the currency dropdown using the given XPath
//			WebElement currencyDropdown = wait.until(
//					ExpectedConditions.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[1]"))
//			);
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[1]")).click();
			Thread.sleep(1000);

//			// Scroll into view and click
//			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", currencyDropdown);
//			Thread.sleep(500);  // optional small pause before clicking

//			currencyDropdown.click();

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to click on currency dropdown: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Unable to click on currency dropdown.");
		}
	}

	// Method to Select Currency Drop Down Value
	public void selectCurrencyDropDownValues(String currencyCode) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Wait for dropdown menu list
		WebElement menuList = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tg-select-box__menu-list")));

		// Find the currency option
		WebElement currencyOption = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//span[@class='tg-select-option-label' and text()='" + currencyCode + "']")));

		// Scroll INSIDE the dropdown container
		js.executeScript("arguments[0].scrollTop = arguments[1].offsetTop;", menuList, currencyOption);

		wait.until(ExpectedConditions.elementToBeClickable(currencyOption)).click();
	}

	// Method to Validate Selected Currency is Displayed on Result Screen
	public void validateCurrencyOnResultScreen(String currencyValue, Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(8000);
			List<WebElement> currencyTexts = driver.findElements(By.className("tg-other-price"));
			for (WebElement currencyText : currencyTexts) {
				System.out.println(currencyText.getText());
				currencyText.getText().contains(currencyValue);
			}
			String currencyData = currencyTexts.get(0).getText();
			String currencyCode = currencyData.substring(0, 3);
			System.out.println(currencyCode);
			if (currencyValue.contentEquals(currencyCode)) {
				Log.ReportEvent("PASS",
						"Currencies are Displayed Based on User Search " + "" + currencyCode + " " + "is Successful");

			} else {
				Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User" + " " + currencyCode);
				ScreenShots.takeScreenShot();
				Assert.fail();

			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User Search" + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();

		}
	}

	// Method to Validate Selected Currency is Displayed on Result Screen
	public void validateCurrencyOnResultScreenForInternational(String currencyValue, Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(6000);
			List<WebElement> currencyTexts = driver.findElements(By.className("other-currency-price"));
			for (WebElement currencyText : currencyTexts) {
				System.out.println(currencyText.getText());
				currencyText.getText().contains(currencyValue);
			}
			String currencyData = currencyTexts.get(0).getText();
			String currencyCode = currencyData.substring(0, 3);
			System.out.println(currencyCode);
			if (currencyValue.contentEquals(currencyCode)) {
				Log.ReportEvent("PASS",
						"Currencies are Displayed Based on User Search " + "" + currencyCode + " " + "is Successful");

			} else {
				Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User" + " " + currencyCode);
				ScreenShots.takeScreenShot();
				Assert.fail();

			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User Search" + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();

		}
	}

	// Method to Validate Selected Currency is Displayed on Result Screen
	public void validateCurrencyOnResultScreenForInterNational(String currencyValue, Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(6000);
			List<WebElement> currencyTexts = driver.findElements(By.className("other-currency-price"));
			for (WebElement currencyText : currencyTexts) {
				System.out.println(currencyText.getText());
				currencyText.getText().contains(currencyValue);
			}
			String currencyData = currencyTexts.get(0).getText();
			String currencyCode = currencyData.substring(0, 3);
			System.out.println(currencyCode);
			if (currencyValue.contentEquals(currencyCode)) {
				Log.ReportEvent("PASS",
						"Currencies are Displayed Based on User Search " + "" + currencyCode + " " + "is Successful");

			} else {
				Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User" + " " + currencyCode);
				ScreenShots.takeScreenShot();
				Assert.fail();

			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User Search" + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();

		}
	}

	// Method to click On Sort DropDown
	public void clickSortDropdown(Log Log, ScreenShots ScreenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Locate the currency dropdown using the given XPath
			WebElement currencyDropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[2]")));

//			// Scroll into view and click
//			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", currencyDropdown);
//			Thread.sleep(500);  // optional small pause before clicking

			currencyDropdown.click();

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to click on currency dropdown: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Unable to click on currency dropdown.");
		}
	}

	// Method to Select Sort Drop Down Value
	public void selectSortDropDownValues(String sortValue) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		WebElement currencyValue = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='" + sortValue + "']")));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		// 🛠️ FIXED: "argument" → "arguments"
		js.executeScript("arguments[0].scrollIntoView(true);", currencyValue);
		Thread.sleep(500);
		driver.findElement(By.xpath("//span[text()='" + sortValue + "']")).click();
//		wait.until(ExpectedConditions.elementToBeClickable(currencyValue)).click();
	}

	// Method to Select Sort Drop Down Value
	public void selectFilter(String sortValue) throws InterruptedException {
		Thread.sleep(500);
		driver.findElement(By.xpath("//span[text()='" + sortValue + "']")).click();
//		wait.until(ExpectedConditions.elementToBeClickable(currencyValue)).click();
	}

	// Method to Validate Price Sort Functionality (Descending Order, duplicates
	// allowed)
	public void validatePricesAreNonIncreasing(Log Log, ScreenShots ScreenShots) {
		try {
			List<WebElement> priceElements = driver.findElements(By.className("tg-price"));

			if (priceElements.size() < 2) {
				Log.ReportEvent("FAIL", "Less than 2 price elements found. Found: " + priceElements.size());
				ScreenShots.takeScreenShot();
				Assert.fail("Insufficient price elements for validation.");
			}

			List<Integer> prices = new ArrayList<>();

			// Extract and convert each price to int
			for (int i = 0; i < priceElements.size(); i++) {
				String priceText = priceElements.get(i).getText().replaceAll("[^\\d]", "");
				if (priceText.isEmpty()) {
					Log.ReportEvent("FAIL", "Price text was empty or invalid at index " + i);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid price format at index " + i);
				}

				int priceValue = Integer.parseInt(priceText);
				prices.add(priceValue);
				Log.ReportEvent("INFO", "Price " + (i + 1) + ": ₹" + priceValue);
			}

			// Validate that prices are in non-increasing (descending) order
			for (int i = 1; i < prices.size(); i++) {
				if (prices.get(i) > prices.get(i - 1)) {
					Log.ReportEvent("FAIL", "Price at position " + (i + 1) + " (" + prices.get(i)
							+ ") is greater than previous price (" + prices.get(i - 1) + ")");
					ScreenShots.takeScreenShot();
					Assert.fail("Price validation failed: price at position " + (i + 1)
							+ " is greater than previous price.");
				}
			}

			Log.ReportEvent("PASS", "All prices are in non-increasing (descending) order: " + prices);

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during price validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception occurred during price validation.");
		}
	}

	// Method to Sort Price in ascending order
	public void validatePricesInAscendingOrder(Log Log, ScreenShots ScreenShots) {
		try {
			List<WebElement> priceElements = driver.findElements(By.className("tg-price"));
			if (priceElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No price elements found with class 'tg-fl-price'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No price elements found.");
			}

			List<Integer> prices = new ArrayList<>();
			for (WebElement elem : priceElements) {
				String priceText = elem.getText().replaceAll("[^0-9]", ""); // Remove currency symbols, commas etc.
				if (priceText.isEmpty()) {
					Log.ReportEvent("FAIL", "Price text is empty or invalid: '" + elem.getText() + "'");
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid price text.");
				}
				prices.add(Integer.parseInt(priceText));
			}

			for (int i = 1; i < prices.size(); i++) {
				if (prices.get(i) < prices.get(i - 1)) {
					Log.ReportEvent("FAIL", "Price at position " + (i + 1) + " (" + prices.get(i)
							+ ") is less than previous price (" + prices.get(i - 1) + ")");
					ScreenShots.takeScreenShot();
					Assert.fail(
							"Price validation failed: price at position " + (i + 1) + " is less than previous price.");
				}
			}

			Log.ReportEvent("PASS", "Prices are in ascending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during price ascending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	// Method to Validate Duration Order in Ascending Order
	public void validateDurationsInAscendingOrder(Log Log, ScreenShots ScreenShots) {
		try {

			List<WebElement> durationElements = driver.findElements(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-totalduration')]"));
			if (durationElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No duration elements found with class 'tg-totalduration'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No duration elements found.");
			}

			List<Integer> durationsInMinutes = new ArrayList<>();
			for (WebElement elem : durationElements) {
				String durationText = elem.getText().trim();
				int totalMinutes = convertDurationToMinutes(durationText);
				if (totalMinutes == -1) {
					Log.ReportEvent("FAIL", "Invalid duration format: " + durationText);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid duration format detected: " + durationText);
				}
				durationsInMinutes.add(totalMinutes);
				Log.ReportEvent("INFO", "Parsed duration '" + durationText + "' as " + totalMinutes + " minutes.");
			}

			for (int i = 1; i < durationsInMinutes.size(); i++) {
				if (durationsInMinutes.get(i) < durationsInMinutes.get(i - 1)) {
					Log.ReportEvent("FAIL", "Duration at position " + (i + 1) + " (" + durationsInMinutes.get(i)
							+ " min) is less than previous (" + durationsInMinutes.get(i - 1) + " min).");
					ScreenShots.takeScreenShot();
					Assert.fail("Duration ascending order validation failed.");
				}
			}

			Log.ReportEvent("PASS", "Durations are in ascending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during duration ascending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	private int convertDurationToMinutes(String duration) {
		try {
			int hours = 0;
			int minutes = 0;

			String[] parts = duration.split(" ");
			for (String part : parts) {
				if (part.endsWith("h")) {
					hours = Integer.parseInt(part.replace("h", "").trim());
				} else if (part.endsWith("m")) {
					minutes = Integer.parseInt(part.replace("m", "").trim());
				}
			}

			return hours * 60 + minutes;
		} catch (Exception e) {
			return -1; // invalid format
		}
	}

	// Method to Validate Duration Order in Descending Order
	public void validateDurationsInDescendingOrder(Log Log, ScreenShots ScreenShots) {
		try {

			List<WebElement> durationElements = driver.findElements(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-totalduration')]"));
			if (durationElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No duration elements found with class 'tg-totalduration'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No duration elements found.");
			}

			List<Integer> durationsInMinutes = new ArrayList<>();
			for (WebElement elem : durationElements) {
				String durationText = elem.getText().trim();
				int totalMinutes = convertDurationToMinutes(durationText);
				if (totalMinutes == -1) {
					Log.ReportEvent("FAIL", "Invalid duration format: " + durationText);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid duration format detected: " + durationText);
				}
				durationsInMinutes.add(totalMinutes);
				Log.ReportEvent("INFO", "Parsed duration '" + durationText + "' as " + totalMinutes + " minutes.");
			}

			for (int i = 1; i < durationsInMinutes.size(); i++) {
				if (durationsInMinutes.get(i) > durationsInMinutes.get(i - 1)) {
					Log.ReportEvent("FAIL", "Duration at position " + (i + 1) + " (" + durationsInMinutes.get(i)
							+ " min) is greater than previous (" + durationsInMinutes.get(i - 1) + " min).");
					ScreenShots.takeScreenShot();
					Assert.fail("Duration descending order validation failed.");
				}
			}

			Log.ReportEvent("PASS", "Durations are in descending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during duration descending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	// Method to Validate DepartureTime in Ascending order
	public void validateDepartureTimesInAscendingOrder(Log Log, ScreenShots ScreenShots) {
		try {

			List<WebElement> timeElements = driver.findElements(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-deptime')]"));
			if (timeElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No departure time elements found with class 'tg-deptime'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No departure time elements found.");
			}

			List<Integer> timesInMinutes = new ArrayList<>();
			for (WebElement elem : timeElements) {
				String timeText = elem.getText().trim();
				int totalMinutes = convertTimeToMinutes(timeText);
				if (totalMinutes == -1) {
					Log.ReportEvent("FAIL", "Invalid time format: " + timeText);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid time format detected: " + timeText);
				}
				timesInMinutes.add(totalMinutes);
				Log.ReportEvent("INFO",
						"Parsed time '" + timeText + "' as " + totalMinutes + " minutes from midnight.");
			}

			for (int i = 1; i < timesInMinutes.size(); i++) {
				if (timesInMinutes.get(i) < timesInMinutes.get(i - 1)) {
					Log.ReportEvent("FAIL", "Departure time at position " + (i + 1) + " (" + timesInMinutes.get(i)
							+ " min) is less than previous (" + timesInMinutes.get(i - 1) + " min).");
					ScreenShots.takeScreenShot();
					Assert.fail("Departure times ascending order validation failed.");
				}
			}

			Log.ReportEvent("PASS", "Departure times are in ascending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during departure times ascending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	private int convertTimeToMinutes(String time) {
		try {
			String[] parts = time.split(":");
			int hours = Integer.parseInt(parts[0].trim());
			int minutes = Integer.parseInt(parts[1].trim());
			return hours * 60 + minutes;
		} catch (Exception e) {
			// If parsing fails, return -1 to indicate invalid time
			return -1;
		}
	}

	// Method to Sort Departure time in Descending order
	public void validateDepartureTimesInDescendingOrder(Log Log, ScreenShots ScreenShots) {
		try {
			List<WebElement> timeElements = driver.findElements(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-deptime')]"));
			if (timeElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No departure time elements found with class 'tg-deptime'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No departure time elements found.");
			}

			List<Integer> timesInMinutes = new ArrayList<>();
			for (WebElement elem : timeElements) {
				String timeText = elem.getText().trim();
				int totalMinutes = convertTimeToMinutes(timeText);
				if (totalMinutes == -1) {
					Log.ReportEvent("FAIL", "Invalid time format: " + timeText);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid time format detected: " + timeText);
				}
				timesInMinutes.add(totalMinutes);
				Log.ReportEvent("INFO",
						"Parsed time '" + timeText + "' as " + totalMinutes + " minutes from midnight.");
			}

			for (int i = 1; i < timesInMinutes.size(); i++) {
				if (timesInMinutes.get(i) > timesInMinutes.get(i - 1)) {
					Log.ReportEvent("FAIL", "Departure time at position " + (i + 1) + " (" + timesInMinutes.get(i)
							+ " min) is greater than previous (" + timesInMinutes.get(i - 1) + " min).");
					ScreenShots.takeScreenShot();
					Assert.fail("Departure times descending order validation failed.");
				}
			}

			Log.ReportEvent("PASS", "Departure times are in descending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during departure times descending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	// Method to Validate ArrivalTime in Ascending order
	public void validateArrivalTimesInAscendingOrder(Log Log, ScreenShots ScreenShots) {
		try {
			List<WebElement> timeElements = driver.findElements(By.className("tg-arrtime"));
			if (timeElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No Arrival time elements found with class 'tg-deptime'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No Arrival time elements found.");
			}

			List<Integer> timesInMinutes = new ArrayList<>();
			for (WebElement elem : timeElements) {
				String timeText = elem.getText().trim();
				int totalMinutes = convertTimeToMinutes(timeText);
				if (totalMinutes == -1) {
					Log.ReportEvent("FAIL", "Invalid time format: " + timeText);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid time format detected: " + timeText);
				}
				timesInMinutes.add(totalMinutes);
				Log.ReportEvent("INFO",
						"Parsed time '" + timeText + "' as " + totalMinutes + " minutes from midnight.");
			}

			for (int i = 1; i < timesInMinutes.size(); i++) {
				if (timesInMinutes.get(i) < timesInMinutes.get(i - 1)) {
					Log.ReportEvent("FAIL", "Arrival time at position " + (i + 1) + " (" + timesInMinutes.get(i)
							+ " min) is less than previous (" + timesInMinutes.get(i - 1) + " min).");
					ScreenShots.takeScreenShot();
					Assert.fail("Arrival times ascending order validation failed.");
				}
			}

			Log.ReportEvent("PASS", "Arrival times are in ascending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during Arrival times ascending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	// Method to Sort Arrival time in Descending order
	public void validateArrivalTimesInDescendingOrder(Log Log, ScreenShots ScreenShots) {
		try {
			List<WebElement> timeElements = driver.findElements(By.className("tg-arrtime"));
			if (timeElements.isEmpty()) {
				Log.ReportEvent("FAIL", "No Arrival time elements found with class 'tg-deptime'.");
				ScreenShots.takeScreenShot();
				Assert.fail("No Arrival time elements found.");
			}

			List<Integer> timesInMinutes = new ArrayList<>();
			for (WebElement elem : timeElements) {
				String timeText = elem.getText().trim();
				int totalMinutes = convertTimeToMinutes(timeText);
				if (totalMinutes == -1) {
					Log.ReportEvent("FAIL", "Invalid time format: " + timeText);
					ScreenShots.takeScreenShot();
					Assert.fail("Invalid time format detected: " + timeText);
				}
				timesInMinutes.add(totalMinutes);
				Log.ReportEvent("INFO",
						"Parsed time '" + timeText + "' as " + totalMinutes + " minutes from midnight.");
			}

			for (int i = 1; i < timesInMinutes.size(); i++) {
				if (timesInMinutes.get(i) > timesInMinutes.get(i - 1)) {
					Log.ReportEvent("FAIL", "Arrival time at position " + (i + 1) + " (" + timesInMinutes.get(i)
							+ " min) is greater than previous (" + timesInMinutes.get(i - 1) + " min).");
					ScreenShots.takeScreenShot();
					Assert.fail("Arrival times descending order validation failed.");
				}
			}

			Log.ReportEvent("PASS", "Arrival times are in descending order or equal as expected.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during Arrival times descending order validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	// Method to get other country price value
	public String getOtherCountryPriceByIndex(int index, Log Log, ScreenShots ScreenShots) {
		try {
			// XPath with the given index (1-based)
			String xpath = "(//div[contains(@class,'tg-other-price')])[" + index + "]";

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			String priceText = priceElement.getText().trim();
			Log.ReportEvent("PASS", "Price at index " + index + " retrieved: " + priceText);
			return priceText;

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve price at index " + index + ": " + e.getMessage());
			ScreenShots.takeScreenShot();
			return ""; // or return null depending on your preference
		}
	}

	// Method to get other country price value
	public String getOtherCountryPriceByIndexForInternational(int index, Log Log, ScreenShots ScreenShots) {
		try {
			// XPath with the given index (1-based)
			String xpath = "(//span[@class='other-currency-price bold othercurrency'])[" + index + "]";

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			String priceText = priceElement.getText().trim();
			Log.ReportEvent("PASS", "Price at index " + index + " retrieved: " + priceText);
			return priceText;

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve price at index " + index + ": " + e.getMessage());
			ScreenShots.takeScreenShot();
			return ""; // or return null depending on your preference
		}
	}

	// Method to get other country price value
	public String getOtherCountryPriceByIndexForInternational(Log Log, ScreenShots ScreenShots) {
		try {
			// XPath with the given index (1-based)
			String xpath = "//div[contains(@class,'other-currency-price')]";

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			String priceText = priceElement.getText().trim();
			Log.ReportEvent("PASS", "Price retrieved for Other Currency Value: " + priceText);
			return priceText;

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve other currency price" + e.getMessage());
			ScreenShots.takeScreenShot();
			return ""; // or return null depending on your preference
		}
	}

	public String getOtherCurrencyPriceForTotalCost(Log Log, ScreenShots ScreenShots) {
		try {
			String xpath = "//div[text()='Total Cost']//preceding-sibling::div[contains(@class,'other-currency-price')]";
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			String priceText = priceElement.getText().trim();
			return priceText;
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve other currency price near Total Cost: " + e.getMessage());
			ScreenShots.takeScreenShot();
			return "";
		}
	}

	public String addCurrencyValues(String value1, String value2) {
		try {
			// Extract currency prefix (assumed same for both)
			String currency = value1.split(" ")[0];

			// Extract numeric parts and parse to double
			double num1 = Double.parseDouble(value1.split(" ")[1]);
			double num2 = Double.parseDouble(value2.split(" ")[1]);

			// Sum the values
			double sum = num1 + num2;

			// Format sum with 2 decimals and prepend currency
			String result = String.format("%s %.2f", currency, sum);
			return result;
		} catch (Exception e) {
			// Handle error gracefully
			System.err.println("Error parsing or adding currency values: " + e.getMessage());
			return "";
		}
	}

	public String getOtherCurrencyPriceForTotalCostFromBookingPage(Log Log, ScreenShots ScreenShots) {
		try {
			String xpath = "//span[contains(@class,'other-currency-price')]";
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			String priceText = priceElement.getText().trim();
			return priceText;
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve other currency price near Total Cost: " + e.getMessage());
			ScreenShots.takeScreenShot();
			return "";
		}
	}

	// Method to Validate Departing Time Range Filter.
	public void validateDepartingTimeRange(Log log, ScreenShots screenShots, int startHour, int endHour) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-deptime')]")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour > endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All departing times are within the selected range: " + startHour + " to "
						+ endHour + " hours.");
			} else {
				log.ReportEvent("FAIL", "Some departing times are outside the selected range: " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Departing time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating departing times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating departing times.");
		}
	}

	public void validateDepartingTimeRange(Log log, ScreenShots screenShots, String rangeLabel) {
		try {
			int startHour = 0;
			int endHour = 0;

			// Map range label to start and end hours
			switch (rangeLabel) {
			case "00-06":
				startHour = 0;
				endHour = 6;
				break;
			case "06-12":
				startHour = 6;
				endHour = 12;
				break;
			case "12-18":
				startHour = 12;
				endHour = 18;
				break;
			case "18-24":
				startHour = 18;
				endHour = 24;
				break;
			default:
				throw new IllegalArgumentException("Invalid range label: " + rangeLabel);
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-deptime')]")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour >= endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All departing times are within the selected range: " + rangeLabel);
			} else {
				log.ReportEvent("FAIL",
						"Some departing times are outside the selected range " + rangeLabel + ": " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Departing time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating departing times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating departing times.");
		}
	}

	public void validateDepartingTimeRangeForDepartingFlights(Log log, ScreenShots screenShots, String rangeLabel) {
		try {
			int startHour = 0;
			int endHour = 0;

			// Map range label to start and end hours
			switch (rangeLabel) {
			case "00-06":
				startHour = 0;
				endHour = 6;
				break;
			case "06-12":
				startHour = 6;
				endHour = 12;
				break;
			case "12-18":
				startHour = 12;
				endHour = 18;
				break;
			case "18-24":
				startHour = 18;
				endHour = 24;
				break;
			default:
				throw new IllegalArgumentException("Invalid range label: " + rangeLabel);
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("tg-fromdeptime")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour >= endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All departing times are within the selected range: " + rangeLabel);
			} else {
				log.ReportEvent("FAIL",
						"Some departing times are outside the selected range " + rangeLabel + ": " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Departing time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating departing times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating departing times.");
		}
	}

	public void validateDepartingTimeRangeForReturnFlights(Log log, ScreenShots screenShots, String rangeLabel) {
		try {
			int startHour = 0;
			int endHour = 0;

			// Map range label to start and end hours
			switch (rangeLabel) {
			case "00-06":
				startHour = 0;
				endHour = 6;
				break;
			case "06-12":
				startHour = 6;
				endHour = 12;
				break;
			case "12-18":
				startHour = 12;
				endHour = 18;
				break;
			case "18-24":
				startHour = 18;
				endHour = 24;
				break;
			default:
				throw new IllegalArgumentException("Invalid range label: " + rangeLabel);
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("tg-todeptime")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour >= endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All departing times are within the selected range: " + rangeLabel);
			} else {
				log.ReportEvent("FAIL",
						"Some departing times are outside the selected range " + rangeLabel + ": " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Departing time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating departing times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating departing times.");
		}
	}

	public void validateArrivalTimeRange(Log log, ScreenShots screenShots, String rangeLabel) {
		try {
			int startHour = 0;
			int endHour = 0;

			// Map range label to start and end hours
			switch (rangeLabel) {
			case "00-06":
				startHour = 0;
				endHour = 6;
				break;
			case "06-12":
				startHour = 6;
				endHour = 12;
				break;
			case "12-18":
				startHour = 12;
				endHour = 18;
				break;
			case "18-24":
				startHour = 18;
				endHour = 24;
				break;
			default:
				throw new IllegalArgumentException("Invalid range label: " + rangeLabel);
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					By.xpath("//div[@class='flight-oneway__content_tag']//div[contains(@class,'tg-arrtime')]")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour >= endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All Arrival times are within the selected range: " + rangeLabel);
			} else {
				log.ReportEvent("FAIL",
						"Some Arrival times are outside the selected range " + rangeLabel + ": " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Arrival time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating Arrival times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating Arrival times.");
		}
	}

	public void validateArrivalTimeRangeForDepartingFlights(Log log, ScreenShots screenShots, String rangeLabel) {
		try {
			int startHour = 0;
			int endHour = 0;

			// Map range label to start and end hours
			switch (rangeLabel) {
			case "00-06":
				startHour = 0;
				endHour = 6;
				break;
			case "06-12":
				startHour = 6;
				endHour = 12;
				break;
			case "12-18":
				startHour = 12;
				endHour = 18;
				break;
			case "18-24":
				startHour = 18;
				endHour = 24;
				break;
			default:
				throw new IllegalArgumentException("Invalid range label: " + rangeLabel);
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("tg-fromarrtime")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour >= endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All Arrival times are within the selected range: " + rangeLabel);
			} else {
				log.ReportEvent("FAIL",
						"Some Arrival times are outside the selected range " + rangeLabel + ": " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Arrival time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating Arrival times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating Arrival times.");
		}
	}

	public void validateReturnArrivalTimeRangeForDepartingFlights(Log log, ScreenShots screenShots, String rangeLabel) {
		try {
			int startHour = 0;
			int endHour = 0;

			// Map range label to start and end hours
			switch (rangeLabel) {
			case "00-06":
				startHour = 0;
				endHour = 6;
				break;
			case "06-12":
				startHour = 6;
				endHour = 12;
				break;
			case "12-18":
				startHour = 12;
				endHour = 18;
				break;
			case "18-24":
				startHour = 18;
				endHour = 24;
				break;
			default:
				throw new IllegalArgumentException("Invalid range label: " + rangeLabel);
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> timeElements = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("tg-toarrtime")));

			boolean allValid = true;
			List<String> invalidTimes = new ArrayList<>();

			for (WebElement element : timeElements) {
				String timeText = element.getText().trim(); // e.g., "05:45"
				if (!timeText.isEmpty()) {
					String[] parts = timeText.split(":");
					int hour = Integer.parseInt(parts[0]);

					if (hour < startHour || hour >= endHour) {
						allValid = false;
						invalidTimes.add(timeText);
					}
				}
			}

			if (allValid) {
				log.ReportEvent("PASS", "All Arrival times are within the selected range: " + rangeLabel);
			} else {
				log.ReportEvent("FAIL",
						"Some Arrival times are outside the selected range " + rangeLabel + ": " + invalidTimes);
				screenShots.takeScreenShot();
				Assert.fail("Arrival time validation failed for times: " + invalidTimes);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception occurred while validating Arrival times: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred while validating Arrival times.");
		}
	}

	public String[] extractAirportCodesFromElements(String xpath) {
		List<WebElement> elements = driver.findElements(By.xpath(xpath));
		List<String> codes = new ArrayList<>();

		for (WebElement element : elements) {
			String text = element.getText();
			if (text.contains("(") && text.contains(")")) {
				String code = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
				codes.add(code);
			}
		}

		return codes.toArray(new String[0]);
	}

	public boolean isAirportCodePresent(String[] allCodes, String expectedCode, Log Log, ScreenShots ScreenShots) {
		try {
			for (String code : allCodes) {
				if (code.equals(expectedCode)) {
					Log.ReportEvent("PASS", "Expected airport code found: " + expectedCode);
					return true;
				}
			}
			Log.ReportEvent("FAIL", "Expected airport code not found: " + expectedCode);
			ScreenShots.takeScreenShot();
			return false;
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Error while validating airport code: " + e.getMessage());
			ScreenShots.takeScreenShot();
			return false;
		}
	}

	public void clickOutboundFlightButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By locator = By.xpath("//button[text()='Select Next Flight']");

		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locator));
		btn.click();
	}

	// ---method to get flights policy text

	public List<String> getAllPolicyTextsFromFlightCards() {
		try {
			// Wait for flight card policy elements
			List<WebElement> policyElements = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
							By.xpath("//div[contains(@class, 'flight-card')]//div[contains(@class, 'tg-policy')]")));

			List<String> policies = new ArrayList<>();
			for (WebElement element : policyElements) {
				policies.add(element.getText().trim().toLowerCase());
			}

			System.out.println("Found " + policies.size() + " flight card policies");
			return policies;

		} catch (Exception e) {
			System.out.println("Error getting flight card policies: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public String[] getOnwardDateTextFromResultPage() {

		WebElement dateElement = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
						"(//div[@class=' tg-typography tg-typography_subtitle-6 fw-600 tg-typography_default'])[2]")));

		String fullText = dateElement.getText().trim();
		System.out.println("Raw text: " + fullText);

		// Remove ordinal suffix (st, nd, rd, th)
		fullText = fullText.replaceAll("(?<=\\d)(st|nd|rd|th)", "");

		// Remove comma
		fullText = fullText.replace(",", "");

		System.out.println("Cleaned text: " + fullText);

		return fullText.split("\\s+");
	}

	public String[] getReturnDateTextFromResultPage() {

		WebElement dateElement = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
						"(//div[@class=' tg-typography tg-typography_subtitle-6 fw-600 tg-typography_default'])[3]")));

		String fullText = dateElement.getText().trim();
		System.out.println("Raw text: " + fullText);

		// Remove ordinal suffix (st, nd, rd, th)
		fullText = fullText.replaceAll("(?<=\\d)(st|nd|rd|th)", "");

		// Remove comma
		fullText = fullText.replace(",", "");

		System.out.println("Cleaned text: " + fullText);

		return fullText.split("\\s+");
	}

	public void validateFlightCardPolicyWithFlightFareCardPolicyText(List<String> flightFareCardPolicies,
			List<String> flightCardPolicies, Log log, ScreenShots screenshots) {

		try {

			if (flightFareCardPolicies == null || flightFareCardPolicies.isEmpty()) {
				log.ReportEvent("FAIL", "Flight Fare Card policies are empty.");
				screenshots.takeScreenShot1();
				Assert.fail("Flight Fare Card policies are empty.");
			}

			if (flightCardPolicies == null || flightCardPolicies.isEmpty()) {
				log.ReportEvent("FAIL", "Flight Card policies are empty.");
				screenshots.takeScreenShot1();
				Assert.fail("Flight Card policies are empty.");
			}

			// Optional: Size validation
			if (flightFareCardPolicies.size() != flightCardPolicies.size()) {
				log.ReportEvent("FAIL", "Policy count mismatch. Flight Fare Card count: "
						+ flightFareCardPolicies.size() + " | Flight Card count: " + flightCardPolicies.size());
				screenshots.takeScreenShot1();
				Assert.fail("Policy count mismatch.");
			}

			boolean allPoliciesMatch = true;

			for (int i = 0; i < flightCardPolicies.size(); i++) {

				String flightFareCardPolicyText = flightFareCardPolicies.get(i).trim().toLowerCase();

				String flightCardPolicyText = flightCardPolicies.get(i).trim().toLowerCase();

				if (!flightFareCardPolicyText.equals(flightCardPolicyText)) {

					log.ReportEvent("FAIL", "Policy mismatch at index " + i + " | Flight Fare Card Policy: "
							+ flightFareCardPolicyText + " | Flight Card Policy: " + flightCardPolicyText);

					allPoliciesMatch = false;
				}
			}

			if (allPoliciesMatch) {
				log.ReportEvent("PASS", "All Flight Card policies match with Flight Fare Card policies.");
			} else {
				screenshots.takeScreenShot1();
				Assert.fail("Flight policy validation failed.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Error during flight policy validation: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Exception during validation.");
		}
	}

	public String getFlightCardPolicyByIndex(int index) {
		try {
			// Wait until at least one policy element is visible
			List<WebElement> policyElements = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-policy')]")));

			// Check index is valid
			if (index < 0 || index >= policyElements.size()) {
				throw new RuntimeException(
						"Invalid index: " + index + ". Available policies count: " + policyElements.size());
			}

			// Return trimmed text
			return policyElements.get(index).getText().trim();

		} catch (Exception e) {
			throw new RuntimeException("Error getting flight card policy at index " + index + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Validates that each Flight Card policy contains all Flight Fare Card policies
	 *
	 * @param flightCardPolicies     List of all Flight Card policies
	 * @param flightFareCardPolicies List of all Flight Fare Card policies
	 * @param log                    Logging object
	 * @param screenshots            Screenshot helper
	 */
	public void validateFlightCardPoliciesWithAllFareCardPolicies(List<String> flightCardPolicies,
			List<String> flightFareCardPolicies, Log log, ScreenShots screenshots) {

		try {

			if (flightCardPolicies == null || flightCardPolicies.isEmpty()) {
				log.ReportEvent("FAIL", "No Flight Card policies found.");
				screenshots.takeScreenShot1();
				Assert.fail("No Flight Card policies found.");
			}

			if (flightFareCardPolicies == null || flightFareCardPolicies.isEmpty()) {
				log.ReportEvent("FAIL", "No Flight Fare Card policies found.");
				screenshots.takeScreenShot1();
				Assert.fail("No Flight Fare Card policies found.");
			}

			boolean allCardsValid = true;

			// Loop through each Flight Card policy
			for (int i = 0; i < flightCardPolicies.size(); i++) {

				String flightCardPolicyText = flightCardPolicies.get(i).trim().toLowerCase();
				boolean allFarePoliciesMatched = true;

				// Check that this flight card policy contains all fare card policies
				for (String farePolicy : flightFareCardPolicies) {
					if (!flightCardPolicyText.contains(farePolicy.trim().toLowerCase())) {
						log.ReportEvent("FAIL", "Flight Card policy at index " + i
								+ " does NOT contain Fare Card policy: '" + farePolicy + "'");
						allFarePoliciesMatched = false;
					}
				}

				if (!allFarePoliciesMatched) {
					allCardsValid = false;
				}
			}

			// Final result
			if (allCardsValid) {
				log.ReportEvent("PASS", "All Flight Card policies contain all Flight Fare Card policies.");
			} else {
				screenshots.takeScreenShot1();
				Assert.fail("Some Flight Card policies did not match all Fare Card policies.");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception during Flight Card policy validation: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Exception during Flight Card policy validation.");
		}
	}

//------------------------------------------------------------

	public Map<String, String> getnewFlightDetails(int index) {
		Map<String, String> flightData = new HashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// 1. Click to expand flight details first
		clickViewFlightDetailsByIndex(index);

		// 2. Determine the stop type by checking which element exists
		String stopType = "";
		if (isElementPresent("//div[normalize-space(text())='Direct Flight']")) {
			stopType = "Direct Flight";
		} else if (isElementPresent("//div[normalize-space(text())='1 stops']")) {
			stopType = "1 stops";
		} else if (isElementPresent("//div[normalize-space(text())='2 stops']")) {
			stopType = "2 stops";
		}

		// 3. Extract data based on stopType
		if (stopType.equals("Direct Flight")) {
			flightData.put("fromDepartureTime",
					getText("//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromdeptime')]"));
			flightData.put("fromOrigin",
					getText("//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-mb-fromorigin')]"));
			flightData.put("fromArrivalTime",
					getText("//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromarrtime')]"));
			flightData.put("fromDestination",
					getText("//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromdestination')]"));
			flightData.put("cabinClass",
					getText("//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-cabinclass')]"));
			flightData.put("totalDuration",
					getText("//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-totalduration')]"));
			flightData.put("connectionLineText", getText(
					"//div[@class='oneway-card__flight-info']//div[contains(@class,'oneway-card__connect_line_text')]"));
		} else if (stopType.contains("stops")) {
			// Extract the number from "1 stops" or "2 stops"
			int stopCount = Integer.parseInt(stopType.replaceAll("[^0-9]", ""));
			int segments = stopCount + 1; // 1 stop = 2 flight segments

			String[] prefixes = { "from", "second", "third", "fourth" };

			for (int i = 1; i <= segments; i++) {
				String p = (i <= prefixes.length) ? prefixes[i - 1] : "segment" + i;

				flightData.put(p + "Origin",
						getText("(//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-mb-fromorigin')])["
								+ i + "]"));
				flightData.put(p + "Destination", getText(
						"(//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromdestination')])[" + i
								+ "]"));
				flightData.put(p + "DepartureTime",
						getText("(//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromdeptime')])["
								+ i + "]"));
				flightData.put(p + "ArrivalTime",
						getText("(//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-fromarrtime')])["
								+ i + "]"));
				flightData.put(p + "Class",
						getText("(//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-cabinclass')])[" + i
								+ "]"));
				flightData.put(p + "Duration",
						getText("(//div[@class='oneway-card__flight-info']//div[contains(@class,'tg-totalduration')])["
								+ i + "]"));

				// Airline number logic (index usually starts from 2 for segments)
				flightData.put(p + "airlinenumber",
						getText("(//div[contains(@class,'tg-flightnumber')])[" + (i + 1) + "]"));
			}
		}

		return flightData;
	}

	// Helper method to simplify getting text
	private String getText(String xpath) {
		try {
			return driver.findElement(By.xpath(xpath)).getText().trim();
		} catch (Exception e) {
			return "NOT_FOUND";
		}
	}

	// Helper to check if stop type exists
	private boolean isElementPresentnew(String xpath) {
		return driver.findElements(By.xpath(xpath)).size() > 0;
	}

	// --------------------------------------------------------------------------
	// --------------------------------------------------------------------------

	public List<Map<String, String>> getDynamicFlightDetails(int index) {
		List<Map<String, String>> allSegments = new ArrayList<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// 1. Define the main card path
		String cardPath = "(//div[contains(@class,'tg-flight-card')])[" + index + "]";

		try {
			// --- STEP 1: SCROLL TO MAIN CARD ---
			WebElement mainCard = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cardPath)));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", mainCard);
			Thread.sleep(1000);

			// --- STEP 2: EXPAND & SCROLL TO DETAILS ---
			clickViewFlightDetailsByIndex(index);

			// Wait for the segment container to appear in the DOM
			By originXpath = By.xpath(cardPath + "//div[contains(@class,'tg-mb-fromorigin')]");
			WebElement firstSegment = wait.until(ExpectedConditions.presenceOfElementLocated(originXpath));

			// 🔥 SECOND SCROLL: Move specifically to the expanded details
			System.out.println("🖱️ SOP: Scrolling to expanded segments...");
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstSegment);

			// Wait for visibility and animation
			wait.until(ExpectedConditions.visibilityOf(firstSegment));
			Thread.sleep(1500);

			// 3. EXTRACT SEGMENT LISTS
			List<WebElement> origins = driver
					.findElements(By.xpath(cardPath + "//div[contains(@class,'tg-mb-fromorigin')]"));
			List<WebElement> destinations = driver
					.findElements(By.xpath(cardPath + "//div[contains(@class,'tg-fromdestination')]"));
			List<WebElement> depTimes = driver
					.findElements(By.xpath(cardPath + "//div[contains(@class,'tg-fromdeptime')]"));
			List<WebElement> arrTimes = driver
					.findElements(By.xpath(cardPath + "//div[contains(@class,'tg-fromarrtime')]"));
			List<WebElement> cabins = driver
					.findElements(By.xpath(cardPath + "//div[contains(@class,'tg-cabinclass')]"));
			List<WebElement> durations = driver.findElements(By.xpath(cardPath
					+ "//div[contains(@class,'oneway-card__flight-info')]//div[contains(@class,'tg-totalduration')]"));
			List<WebElement> flights = driver.findElements(By.xpath(cardPath
					+ "//div[contains(@class,'oneway-card__flight-info')]//div[contains(@class,'tg-flightnumber')]"));

			int segmentCount = origins.size();
			System.out.println("📊 Detected " + segmentCount + " segments for card " + index);

			for (int i = 0; i < segmentCount; i++) {
				Map<String, String> segmentData = new HashMap<>();
				segmentData.put("origin", origins.get(i).getText().trim().split(" \\(")[0]);
				segmentData.put("destination", destinations.get(i).getText().trim().split(" \\(")[0]);
				segmentData.put("depTime", depTimes.get(i).getText().trim());
				segmentData.put("arrTime", arrTimes.get(i).getText().trim());
				segmentData.put("cabin", cabins.get(i).getText().trim());
				segmentData.put("duration", i < durations.size() ? durations.get(i).getText().trim() : "N/A");

				String rawFlight = (i < flights.size()) ? flights.get(i).getText().trim() : "";
				String cleanFlightNum = rawFlight.contains("-") ? rawFlight.split("-")[1].trim()
						: rawFlight.replaceAll("[^0-9]", "");
				segmentData.put("flightNum", cleanFlightNum);

				allSegments.add(segmentData);
			}
		} catch (Exception e) {
			System.out.println("❌ ERROR: Failed to process flight at index " + index + ". Error: " + e.getMessage());
		}

		return allSegments;
	}

	public void validateOnewaySearchDataFromUIAndResponseBody(List<Map<String, String>> uiSegments, String responseBody,
			Log log, ScreenShots ScreenShots) {
		System.out.println("\n" + "=".repeat(60));
		System.out.println(" STARTING FLIGHT DATA VALIDATION");
		System.out.println("=".repeat(60));

		// 1. Get clean flight numbers from UI
		List<String> uiFlightNums = uiSegments.stream().map(m -> m.get("flightNum")).collect(Collectors.toList());

		System.out.println("Step 1: UI Flight Sequence to find: " + uiFlightNums);

		// 2. Parse and Search
		JSONObject jsonResponse = new JSONObject(responseBody);
		JSONObject matchedJourney = findJourneyByFlightNumbers(jsonResponse, uiFlightNums);

		if (matchedJourney == null) {
			System.out.println("❌ ERROR: Could not find " + uiFlightNums + " anywhere in the response body.");
			log.ReportEvent("FAIL", "Flight sequence " + uiFlightNums + " not present in API response.");
			Assert.fail("Flight sequence " + uiFlightNums + " not present in API response.");
		}

		System.out.println(" Step 2: Journey found in API. Comparing fields now...");

		// 3. Compare Segments
		JSONArray apiLegs = matchedJourney.getJSONArray("flightlegs");
		for (int i = 0; i < uiSegments.size(); i++) {
			Map<String, String> ui = uiSegments.get(i);
			JSONObject api = apiLegs.getJSONObject(i);

			System.out.println("\n--- Segment #" + (i + 1) + " [" + ui.get("flightNum") + "] ---");

			// Track status for Reporting
			boolean pass = true;

			// Detailed SOPs for each field
			pass &= compare("Origin", ui.get("origin"), api.getString("origin_name"));
			pass &= compare("Destination", ui.get("destination"), api.getString("destination_name"));
			pass &= compare("Cabin", ui.get("cabin"), api.getString("cabinclass"));

			// Time match (Stripping ':' from UI e.g., 14:40 -> 1440)
			pass &= compare("Departure Time", ui.get("depTime").replace(":", ""), api.getString("deptime"));
			pass &= compare("Arrival Time", ui.get("arrTime").replace(":", ""), api.getString("arrtime"));

			// Duration (Converts e.g., 95 to "1h 35m")
			// 1. Compare individual leg duration
			String apiLegDuration = convertMinutesToUiFormat(api.getInt("journeyduration"));
			pass &= compare("Leg Duration", ui.get("duration"), apiLegDuration);

			// --- Log ReportEvent ---
			if (pass) {
				log.ReportEvent("PASS", "Flight Segment #" + (i + 1) + " [" + ui.get("flightNum")
						+ "] validation successful. All fields match.");
			} else {
				log.ReportEvent("FAIL", "Flight Segment #" + (i + 1) + " [" + ui.get("flightNum")
						+ "] validation failed. Check console for details.");
				ScreenShots.takeScreenShot();

			}
		}
		System.out.println("\n" + "=".repeat(60) + "\n");
	}

	private boolean compare(String field, String uiVal, String apiVal) {
		String ui = (uiVal == null) ? "NOT_FOUND" : uiVal.trim();
		String api = (apiVal == null) ? "NOT_FOUND" : apiVal.trim();

		// Flexible comparison (case insensitive and handles partial matches)
		if (ui.equalsIgnoreCase(api) || ui.contains(api) || api.contains(ui)) {
			System.out.println("    " + field + " Match: " + ui);
			return true;
		} else {
			System.out.println("    " + field + " MISMATCH! UI: [" + ui + "] | API: [" + api + "]");
			return false;
		}
	}

	private JSONObject findJourneyByFlightNumbers(Object json, List<String> targetNums) {
		if (json instanceof JSONObject) {
			JSONObject obj = (JSONObject) json;
			if (obj.has("flightlegs")) {
				JSONArray legs = obj.getJSONArray("flightlegs");
				List<String> currentApiNums = new ArrayList<>();
				for (int i = 0; i < legs.length(); i++) {
					currentApiNums.add(legs.getJSONObject(i).optString("flightnumber"));
				}
				// Check if the sequence matches exactly
				if (currentApiNums.equals(targetNums)) {
					return obj;
				}
			}
			// Recurse through keys
			for (String key : obj.keySet()) {
				JSONObject found = findJourneyByFlightNumbers(obj.get(key), targetNums);
				if (found != null)
					return found;
			}
		} else if (json instanceof JSONArray) {
			JSONArray array = (JSONArray) json;
			for (int i = 0; i < array.length(); i++) {
				JSONObject found = findJourneyByFlightNumbers(array.get(i), targetNums);
				if (found != null)
					return found;
			}
		}
		return null;
	}

	private String convertMinutesToUiFormat(int totalMinutes) {
		int hours = totalMinutes / 60;
		int minutes = totalMinutes % 60;
		// Handles case where UI might be "2h 5m" or "0h 45m"
		return hours + "h " + minutes + "m";
	}

	public List<Map<String, String>> getFareCardDetails() {
		List<Map<String, String>> allFares = new ArrayList<>();
		Actions actions = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		String cardContainerXpath = "//div[contains(@class,'fare-options-container')]//div[contains(@class, 'tg-fare-card')] | //div[contains(@class,'fare-options-container')]/div";
		List<WebElement> fareCards = driver.findElements(By.xpath(cardContainerXpath));

		System.out.println("\n📊 TOTAL CARDS FOUND: " + fareCards.size());

		for (int i = 1; i <= fareCards.size(); i++) {
			Map<String, String> fareData = new HashMap<>();
			String base = "(" + cardContainerXpath + ")[" + i + "]";

			try {
				WebElement currentCard = driver.findElement(By.xpath(base));
				js.executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'instant'});", currentCard);
				Thread.sleep(800);

				String fName = "Unknown";
				try {
					fName = currentCard.findElement(By.xpath(".//*[contains(@class,'tg-fare-type')]")).getText().trim();
				} catch (Exception e) {
				}
				fareData.put("fareName", fName);
				System.out.println("👉 Processing: [" + fName + "]");

				// 1. HOVERS - Using the safe fixed method below
				fareData.put("cancellation", hoverAndCapture(base, "Cancellation", actions, wait));
				fareData.put("reschedule", hoverAndCapture(base, "Reschedule", actions, wait));

				// 2. BAGGAGE & MEALS - Using exact text matching to avoid "N/A"
				fareData.put("cabinBaggage", getPolicyText(base, "Cabin Baggage"));
				fareData.put("checkinBaggage", getPolicyText(base, "Check-in Baggage"));
				fareData.put("meals", getPolicyText(base, "Meals"));
				fareData.put("seats", getPolicyText(base, "Seats"));

				// 3. POLICY
				String policy = "N/A";
				try {
					policy = currentCard.findElement(By.xpath(".//*[contains(@class,'tg-policy')]")).getText().trim();
				} catch (Exception e) {
				}
				fareData.put("policyStatus", policy);

				System.out.println(
						"    🎒 Baggage: " + fareData.get("cabinBaggage") + " | " + fareData.get("checkinBaggage"));
				System.out.println("    🍱 Meals: " + fareData.get("meals") + " | Seats: " + fareData.get("seats"));

				allFares.add(fareData);

			} catch (Exception e) {
				System.out.println("❌ Error on Card " + i + ": " + e.getMessage());
			}
		}
		return allFares;
	}

	private String getPolicyText(String base, String label) {
		try {
			// Using your specific XPath logic, constrained by the 'base' card container
			String xpath = base + "//div[contains(text(),'" + label
					+ "')]/following::div[contains(@class,'flight-oneway__policy_tag_text')][1]";

			WebElement element = driver.findElement(By.xpath(xpath));
			String text = element.getText().trim();

			return text.isEmpty() ? "N/A" : text;
		} catch (Exception e) {
			return "N/A";
		}
	}

	private String hoverAndCapture(String base, String label, Actions actions, WebDriverWait wait) {
		try {
			// Use your specific XPath
			String iconXpath = base + "//div[contains(text(),'" + label + "')]/span//*[local-name()='svg']";
			WebElement icon = driver.findElement(By.xpath(iconXpath));

			// 1. Move to icon and pause to let the UI 'register' the mouse
			actions.moveToElement(icon).pause(Duration.ofMillis(500)).perform();

			// 2. Click and Hold is the 'Nuclear Option' - it forces MuiTooltips to stay
			// open
			actions.clickAndHold(icon).pause(Duration.ofMillis(1200)).build().perform();

			// 3. Capture Tooltip
			WebElement tooltip = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@role='tooltip'] | //div[contains(@class, 'MuiTooltip-tooltip')]")));

			String text = tooltip.getText().trim();

			// 4. RESET: Release the mouse and move to a neutral spot on the SAME card
			// This prevents the 'Out of Bounds' error and clears the tooltip for the next
			// field
			actions.release()
					.moveToElement(driver.findElement(By.xpath(base + "//div[contains(@class,'tg-fare-type')]")))
					.perform();
			Thread.sleep(500);

			return text;
		} catch (Exception e) {
			actions.release().perform();
			System.out.println("   ⚠️ Hover failed for " + label);
			return "N/A";
		}
	}

//	private String hoverAndCapture(String base, String label, Actions actions, WebDriverWait wait) {
//	    try {
//	        // Use your provided specific XPath logic
//	        String iconXpath = base + "//div[contains(text(),'" + label + "')]/span//*[local-name()='svg']";
//	        WebElement icon = driver.findElement(By.xpath(iconXpath));
//
//	        // 1. Force Scroll to center and wait for UI to stabilize
//	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", icon);
//	        Thread.sleep(1000); 
//
//	        // 2. Clear any stuck actions and move to element
//	        actions.moveToElement(icon).perform();
//	        Thread.sleep(200);
//
//	        // 3. THE FIX: clickAndHold is much more stable for MuiTooltips on complex cards
//	        actions.clickAndHold(icon).pause(Duration.ofMillis(1500)).build().perform();
//
//	        // 4. Capture Tooltip - Increased wait time for Exclusive cards
//	        WebElement tooltip = wait.until(ExpectedConditions.visibilityOfElementLocated(
//	            By.xpath("//div[@role='tooltip'] | //div[contains(@class, 'MuiTooltip-tooltip')] | //div[contains(@class,'tooltip')]")
//	        ));
//	        
//	        String text = tooltip.getText().trim();
//	        System.out.println("      ✅ Captured " + label + ": " + text.replace("\n", " "));
//
//	        // 5. Clean up: Release and move away to clear the tooltip for the next capture
//	        actions.release().moveByOffset(-250, 0).build().perform();
//	        Thread.sleep(500); 
//
//	        return text;
//	    } catch (Exception e) {
//	        // Safe exit: ensure mouse isn't "stuck" holding an invisible icon
//	        actions.release().perform();
//	        System.out.println("      ⚠️ " + label + " hover failed.");
//	        return "N/A";
//	    }
//	}

	/**
	 * Helper method to find the value next to a specific label within a fare card
	 */
	private String getPolicyValue(String base, String label) {
		try {
			// This XPath finds the label text, goes to the parent container, and finds the
			// sibling value
			String xpath = base + "//*[contains(text(),'" + label + "')]/following-sibling::*" + " | " + base
					+ "//*[contains(text(),'" + label + "')]/..//div[last()]";

			WebElement val = driver.findElement(By.xpath(xpath));
			String result = val.getText().trim();

			return result.isEmpty() ? "Not Mentioned" : result;
		} catch (Exception e) {
			return "Not Mentioned";
		}
	}

	public void validateFareCardDetails(List<Map<String, String>> uiFares, String responseBody,
			List<String> targetFlightNums, Log log, ScreenShots ScreenShots) {
		System.out.println("\n" + "=".repeat(60));
		System.out.println(" STARTING FARE CARDS VALIDATION");
		System.out.println("=".repeat(60));

		JSONObject jsonResponse = new JSONObject(responseBody);
		JSONObject matchedFlight = findJourneyByFlightNumbers(jsonResponse, targetFlightNums);

		if (matchedFlight == null) {
			log.ReportEvent("FAIL", "Flight sequence " + targetFlightNums + " not found in API.");
			Assert.fail("Flight not found.");
		}

		JSONArray apiFares = matchedFlight.getJSONArray("flightfares");

		for (Map<String, String> uiFare : uiFares) {
			String uiFareName = uiFare.get("fareName");
			JSONObject apiFare = null;

			// Match UI card to API fare object
			for (int i = 0; i < apiFares.length(); i++) {
				if (apiFares.getJSONObject(i).getString("faretype").equalsIgnoreCase(uiFareName)) {
					apiFare = apiFares.getJSONObject(i);
					break;
				}
			}

			if (apiFare != null) {
				System.out.println("\n--- Validating Fare Card: [" + uiFareName + "] ---");
				boolean pass = true;

				// 1. Validate Baggage
				pass &= compareFare("Cabin Baggage", uiFare.get("cabinBaggage"), apiFare.getString("freehandbaggage"));
				pass &= compareFare("Check-in Baggage", uiFare.get("checkinBaggage"), apiFare.getString("freebaggage"));

				// 2. Validate Penalties
				JSONArray apiPenalties = apiFare.getJSONArray("penalties");
				pass &= validatePenaltyTooltip("Cancellation", uiFare.get("cancellation"), apiPenalties,
						"CancelPenalty");
				pass &= validatePenaltyTooltip("Reschedule", uiFare.get("reschedule"), apiPenalties, "ChangePenalty");

				// 3. NEW: Validate Policy Status (inpolicy: "true"/"false")
				String apiInPolicy = apiFare.optString("inpolicy", "true"); // Defaulting to true if field missing
				String uiPolicyText = uiFare.get("policyStatus").toLowerCase();

				System.out.println("Checking Compliance Policy...");
				if (apiInPolicy.equalsIgnoreCase("true")) {
					if (uiPolicyText.contains("in policy")) {
						System.out.println("  Match: API (In-Policy) == UI (" + uiFare.get("policyStatus") + ")");
					} else {
						System.out
								.println("   Mismatch: API says In-Policy but UI says: " + uiFare.get("policyStatus"));
						pass = false;
					}
				} else {
					if (uiPolicyText.contains("out") || uiPolicyText.contains("out of policy")) {
						System.out.println("   Match: API (Out-of-Policy) == UI (" + uiFare.get("policyStatus") + ")");
					} else {
						System.out.println(
								"    Mismatch: API says Out-of-Policy but UI says: " + uiFare.get("policyStatus"));
						pass = false;
					}
				}

				// Reporting
				if (pass) {
					log.ReportEvent("PASS", "Fare Card [" + uiFareName + "] matches Backend perfectly.");
				} else {
					log.ReportEvent("FAIL", "Fare Card [" + uiFareName + "] has data mismatches.");
					ScreenShots.takeScreenShot();

				}
			}
		}

	}

	private boolean validatePenaltyTooltip(String type, String uiTooltipText, JSONArray apiPenalties,
			String penaltyType) {
		boolean allMatched = true;
		System.out.println("   🔍 Checking " + type + " breakdown...");

		for (int i = 0; i < apiPenalties.length(); i++) {
			JSONObject penalty = apiPenalties.getJSONObject(i);

			if (penalty.getString("penaltyType").equals(penaltyType)) {
				String amount = penalty.get("amount").toString(); // e.g., "4999.0"
				String duration = penalty.getString("penaltyApplies"); // e.g., "0-24 hrs"

				// Check if the UI tooltip text contains the amount from the API
				if (uiTooltipText.contains(amount.split("\\.")[0])) { // Matches "4999"
					System.out.println("      ✅ Match Found: " + duration + " -> " + amount);
				} else {
					System.out.println("      ❌ Mismatch: " + duration + " amount [" + amount + "] not in UI tooltip!");
					allMatched = false;
				}
			}
		}
		return allMatched;
	}

	private boolean compareFare(String field, String uiVal, String apiVal) {
		String ui = (uiVal == null) ? "" : uiVal.trim();
		String api = (apiVal == null) ? "" : apiVal.trim();

		if (ui.equalsIgnoreCase(api) || ui.contains(api) || api.contains(ui)) {
			System.out.println("    " + field + " Match: " + ui);
			return true;
		} else {
			System.out.println("    " + field + " MISMATCH! UI: [" + ui + "] | API: [" + api + "]");
			return false;
		}
	}

	/*
	 * public List<Map<String, String>> getOnewayBookingFlightDetails() {
	 * List<Map<String, String>> allSegments = new ArrayList<>(); WebDriverWait wait
	 * = new WebDriverWait(driver, Duration.ofSeconds(10)); JavascriptExecutor js =
	 * (JavascriptExecutor) driver;
	 * 
	 * try { TestExecutionNotifier.showExecutionPopup();
	 * 
	 * // --- STEP 1: EXPAND DETAILS IF BUTTON EXISTS --- try { WebDriverWait
	 * shortWait = new WebDriverWait(driver, Duration.ofSeconds(3)); String
	 * expandBtnXpath =
	 * "//div[contains(@class,'flight-booking-page_flight-details flight_0')]//button/span"
	 * ; WebElement expandBtn =
	 * shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath(
	 * expandBtnXpath)));
	 * js.executeScript("arguments[0].scrollIntoView({block: 'center'});",
	 * expandBtn); js.executeScript("arguments[0].click();", expandBtn);
	 * Thread.sleep(1500); } catch (Exception e) {
	 * System.out.println("ℹ️ Expand button not found/already expanded."); }
	 * 
	 * // --- STEP 2: EXTRACT ELEMENT LISTS --- List<WebElement> origins =
	 * driver.findElements(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-origin-fullname')]"
	 * )); List<WebElement> destinations = driver.findElements(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-destination-fullname')]"
	 * )); List<WebElement> depTimes = driver.findElements(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-deptime')]"
	 * )); List<WebElement> arrTimes = driver.findElements(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-arrtime')]"
	 * )); List<WebElement> cabins = driver.findElements(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-cabinclass')]"
	 * )); List<WebElement> durations = driver.findElements(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-mb-duration')]"
	 * )); List<WebElement> layovers = driver.findElements(By.xpath(
	 * "//div[contains(@class,'oneway-card__connect_line_text')]//span"));
	 * 
	 * // --- CHANGE THIS LINE IN YOUR SCRAPER --- List<WebElement>
	 * flightNumElements = driver.findElements(By.xpath(
	 * "//div[contains(@class,'tg-carrier-name')]/following-sibling::div")); // New
	 * Baggage XPaths List<WebElement> checkinBaggage =
	 * driver.findElements(By.xpath("//div[contains(@class,'tg-checkinbaggage')]"));
	 * List<WebElement> cabinBaggage =
	 * driver.findElements(By.xpath("//div[contains(@class,'tg-cabinbaggage')]"));
	 * 
	 * int segmentCount = origins.size(); for (int i = 0; i < segmentCount; i++) {
	 * Map<String, String> segmentData = new HashMap<>();
	 * 
	 * // Core Details segmentData.put("origin",
	 * origins.get(i).getText().trim().split("\\s*[–—-]\\s*")[0].split(" \\(")[0].
	 * trim()); segmentData.put("destination",
	 * destinations.get(i).getText().trim().split("\\s*[–—-]\\s*")[0].split(" \\(")[
	 * 0].trim()); segmentData.put("depTime", depTimes.get(i).getText().trim());
	 * segmentData.put("arrTime", arrTimes.get(i).getText().trim());
	 * segmentData.put("cabin", cabins.get(i).getText().trim());
	 * segmentData.put("duration", i < durations.size() ?
	 * durations.get(i).getText().trim() : "N/A");
	 * 
	 * // --- UPDATED: Regex added to keep only numbers from flight number string
	 * --- if (i < flightNumElements.size()) { String fullFlightNum =
	 * flightNumElements.get(i).getText().trim(); // Keeps only digits from
	 * "EY 1017" -> "1017" segmentData.put("flightNum",
	 * fullFlightNum.replaceAll("[^0-9]", "")); } else {
	 * segmentData.put("flightNum", "N/A"); } // Baggage Data
	 * segmentData.put("checkin", i < checkinBaggage.size() ?
	 * checkinBaggage.get(i).getText().trim() : "N/A"); segmentData.put("cabinBag",
	 * i < cabinBaggage.size() ? cabinBaggage.get(i).getText().trim() : "N/A");
	 * 
	 * // Policy Data String policy = "N/A"; try { policy =
	 * driver.findElement(By.xpath(
	 * "//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-policy')]"
	 * )).getText().trim(); } catch (Exception e) {} segmentData.put("policyStatus",
	 * policy);
	 * 
	 * // Layover Parsing if (i < layovers.size()) { String rawLayover =
	 * layovers.get(i).getText().trim(); try { segmentData.put("layoverDuration",
	 * rawLayover.split(" Layover")[0].trim()); segmentData.put("layoverLocation",
	 * rawLayover.split("In ")[1].split(",")[0].trim()); } catch (Exception e) {
	 * segmentData.put("layoverDuration", "N/A"); segmentData.put("layoverLocation",
	 * "N/A"); } } else { segmentData.put("layoverDuration", "Direct");
	 * segmentData.put("layoverLocation", "Direct"); } allSegments.add(segmentData);
	 * } } catch (Exception e) { System.out.println("❌ Extraction Error: " +
	 * e.getMessage()); } return allSegments; }
	 */

	/*
	 * public void validateOnewayBookingDataUIToBackend(List<Map<String, String>>
	 * uiSegments, String responseBody, Log log, ScreenShots ScreenShots) {
	 * System.out.println("\n" + "=".repeat(60));
	 * System.out.println("  STARTING FULL BOOKING SCREEN VALIDATION");
	 * System.out.println("=".repeat(60));
	 * 
	 * JSONObject jsonResponse = new JSONObject(responseBody); JSONArray journeys;
	 * 
	 * try { JSONObject mainResponse =
	 * jsonResponse.getJSONObject("ValidateSearchResponse"); JSONArray
	 * recommendations = mainResponse.getJSONArray("flightrecommendations");
	 * JSONObject firstRecommendation =
	 * recommendations.getJSONObject(0).getJSONObject("FlightRecommendation");
	 * journeys = firstRecommendation.getJSONArray("flights"); } catch (Exception e)
	 * { log.ReportEvent("FAIL", "JSON path error: " + e.getMessage());
	 * Assert.fail("Missing ValidateSearchResponse path."); return; }
	 * 
	 * JSONObject matchedJourney = null;
	 * 
	 * // --- 1. NORMALIZE UI VALUES FOR MATCHING --- // UI: "01:00" -> "0100"
	 * String uiFirstDep = uiSegments.get(0).get("depTime").replace(":", "").trim();
	 * String uiFirstOrigin = uiSegments.get(0).get("origin").trim(); String
	 * uiFirstFlightNum = uiSegments.get(0).get("flightNum").trim();
	 * 
	 * // --- 2. FIND MATCHING JOURNEY --- for (int j = 0; j < journeys.length();
	 * j++) { JSONObject journey =
	 * journeys.getJSONObject(j).getJSONObject("FlightObject");
	 * 
	 * // Handle FlightLeg (Object vs Array) JSONObject legContainer =
	 * journey.getJSONArray("flightlegs").getJSONObject(0); JSONObject firstLeg =
	 * (legContainer.get("FlightLeg") instanceof JSONArray) ?
	 * legContainer.getJSONArray("FlightLeg").getJSONObject(0) :
	 * legContainer.getJSONObject("FlightLeg");
	 * 
	 * // Convert API values to String for safe comparison String apiFlightNum =
	 * String.valueOf(firstLeg.get("flightnumber")).trim(); String apiDepTime =
	 * String.valueOf(firstLeg.get("deptime")).trim(); String apiOrigin =
	 * firstLeg.getString("origin_name");
	 * 
	 * // Logic: Check if API deptime matches UI deptime (e.g., "0100" == "0100") if
	 * (apiDepTime.equals(uiFirstDep) &&
	 * apiOrigin.toLowerCase().contains(uiFirstOrigin.toLowerCase()) &&
	 * apiFlightNum.equals(uiFirstFlightNum)) { matchedJourney = journey;
	 * System.out.println("✅ Found Matching Journey in API: " + apiFlightNum);
	 * break; } }
	 * 
	 * if (matchedJourney == null) { log.ReportEvent("FAIL",
	 * "Selected Journey not found in API. Searching for: " + uiFirstFlightNum +
	 * " at " + uiFirstDep);
	 * Assert.fail("API Matching Error: No journey found matching UI details."); }
	 * 
	 * // --- 3. VALIDATE EVERY SEGMENT --- JSONObject fareObject =
	 * matchedJourney.getJSONArray("flightfares").getJSONObject(0).getJSONObject(
	 * "FlightFare");
	 * 
	 * // Extract All Legs for multi-stop validation List<JSONObject> legsToValidate
	 * = new ArrayList<>(); JSONObject legWrapper =
	 * matchedJourney.getJSONArray("flightlegs").getJSONObject(0); if
	 * (legWrapper.get("FlightLeg") instanceof JSONArray) { JSONArray arr =
	 * legWrapper.getJSONArray("FlightLeg"); for(int k=0; k<arr.length(); k++)
	 * legsToValidate.add(arr.getJSONObject(k)); } else {
	 * legsToValidate.add(legWrapper.getJSONObject("FlightLeg")); }
	 * 
	 * for (int i = 0; i < uiSegments.size(); i++) { Map<String, String> ui =
	 * uiSegments.get(i); JSONObject api = legsToValidate.get(i); boolean
	 * segmentPass = true;
	 * 
	 * System.out.println("\n🔍 VALIDATING SEGMENT #" + (i + 1) + " [" +
	 * ui.get("flightNum") + "]");
	 * 
	 * // Flight Details segmentPass &= compare("Origin", ui.get("origin"),
	 * api.getString("origin_name"), log); segmentPass &= compare("Destination",
	 * ui.get("destination"), api.getString("destination_name"), log); segmentPass
	 * &= compare("Flight Number", ui.get("flightNum"),
	 * String.valueOf(api.get("flightnumber")), log);
	 * 
	 * // Compare "01:00" (UI) vs "0100" (API) by stripping colon from UI
	 * segmentPass &= compare("Departure Time", ui.get("depTime").replace(":", ""),
	 * String.valueOf(api.get("deptime")), log); segmentPass &=
	 * compare("Arrival Time", ui.get("arrTime").replace(":", ""),
	 * String.valueOf(api.get("arrtime")), log);
	 * 
	 * // Baggage (From the flightfares section) segmentPass &=
	 * compare("Check-in Baggage", ui.get("checkin"),
	 * fareObject.getString("freebaggage"), log); segmentPass &=
	 * compare("Cabin Baggage", ui.get("cabinBag"),
	 * fareObject.getString("freehandbaggage"), log);
	 * 
	 * // Corporate Policy String apiPolicy = matchedJourney.getBoolean("inpolicy")
	 * ? "In Policy" : "Out of Policy"; segmentPass &= compare("Policy Compliance",
	 * ui.get("policyStatus"), apiPolicy, log);
	 * 
	 * if (segmentPass) { log.ReportEvent("PASS", "Segment #" + (i + 1) + " [" +
	 * ui.get("flightNum") + "] validated."); } else { ScreenShots.takeScreenShot();
	 * log.ReportEvent("FAIL", "Segment #" + (i + 1) + " validation failed."); } }
	 * System.out.println("\n" + "=".repeat(60) + "\n"); }
	 */

	public List<Map<String, String>> getOnewayBookingFlightDetails() {
		List<Map<String, String>> allSegments = new ArrayList<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			TestExecutionNotifier.showExecutionPopup();

			// --- STEP 1: EXPAND DETAILS ---
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
				String expandBtnXpath = "//div[contains(@class,'flight-booking-page_flight-details flight_0')]//button/span";
				WebElement expandBtn = shortWait
						.until(ExpectedConditions.elementToBeClickable(By.xpath(expandBtnXpath)));
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", expandBtn);
				js.executeScript("arguments[0].click();", expandBtn);
				Thread.sleep(1500);
			} catch (Exception e) {
				System.out.println("ℹ️ Expand button not found/already expanded.");
			}

			// --- STEP 2: EXTRACT ELEMENT LISTS ---
			List<WebElement> origins = driver.findElements(By.xpath(
					"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-origin-fullname')]"));
			List<WebElement> destinations = driver.findElements(By.xpath(
					"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-destination-fullname')]"));
			List<WebElement> depTimes = driver.findElements(By.xpath(
					"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-deptime')]"));
			List<WebElement> arrTimes = driver.findElements(By.xpath(
					"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-arrtime')]"));
			List<WebElement> cabins = driver.findElements(By.xpath(
					"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-cabinclass')]"));
			List<WebElement> durations = driver.findElements(By.xpath(
					"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-mb-duration')]"));
			List<WebElement> layovers = driver
					.findElements(By.xpath("//div[contains(@class,'oneway-card__connect_line_text')]//span"));

			// --- FIXED XPATH: Only grabs flight numbers from inside the detailed segment
			// blocks ---
			List<WebElement> flightNumElements = driver.findElements(By.xpath(
					"//div[contains(@class,'tg-flightnumber') and not(contains(text(), ',')) and not(contains(text(), '('))]"));
			List<WebElement> checkinBaggage = driver
					.findElements(By.xpath("//div[contains(@class,'tg-checkinbaggage')]"));
			List<WebElement> cabinBaggage = driver.findElements(By.xpath("//div[contains(@class,'tg-cabinbaggage')]"));

			int segmentCount = origins.size();
			for (int i = 0; i < segmentCount; i++) {
				Map<String, String> segmentData = new HashMap<>();

				segmentData.put("origin",
						origins.get(i).getText().trim().split("\\s*[–—-]\\s*")[0].split(" \\(")[0].trim());
				segmentData.put("destination",
						destinations.get(i).getText().trim().split("\\s*[–—-]\\s*")[0].split(" \\(")[0].trim());
				segmentData.put("depTime", depTimes.get(i).getText().trim());
				segmentData.put("arrTime", arrTimes.get(i).getText().trim());
				segmentData.put("cabin", cabins.get(i).getText().trim());
				segmentData.put("duration", i < durations.size() ? durations.get(i).getText().trim() : "N/A");
				if (i < flightNumElements.size()) {
					String rawFlight = flightNumElements.get(i).getText().trim();
					// Keeps only digits: "EY 1017" -> "1017"
					segmentData.put("flightNum", rawFlight.replaceAll("[^0-9]", ""));
				} else {
					segmentData.put("flightNum", "N/A");
				}

				segmentData.put("checkin", i < checkinBaggage.size() ? checkinBaggage.get(i).getText().trim() : "N/A");
				segmentData.put("cabinBag", i < cabinBaggage.size() ? cabinBaggage.get(i).getText().trim() : "N/A");

				String policy = "N/A";
				try {
					policy = driver.findElement(By.xpath(
							"//div[contains(@class,'flight-booking-page_flight-details')]//div[contains(@class,'tg-policy')]"))
							.getText().trim();
				} catch (Exception e) {
				}
				segmentData.put("policyStatus", policy);

				if (i < layovers.size()) {
					String rawLayover = layovers.get(i).getText().trim();
					try {
						segmentData.put("layoverDuration", rawLayover.split(" Layover")[0].trim());
						segmentData.put("layoverLocation", rawLayover.split("In ")[1].split(",")[0].trim());
					} catch (Exception e) {
						segmentData.put("layoverDuration", "N/A");
						segmentData.put("layoverLocation", "N/A");
					}
				} else {
					segmentData.put("layoverDuration", "Direct");
					segmentData.put("layoverLocation", "Direct");
				}
				allSegments.add(segmentData);
			}
		} catch (Exception e) {
			System.out.println("❌ Extraction Error: " + e.getMessage());
		}
		return allSegments;
	}

	public void validateOnewayBookingDataUIToBackend(List<Map<String, String>> uiSegments, String responseBody, Log log,
			ScreenShots ScreenShots) {
		System.out.println("\n" + "=".repeat(60));
		System.out.println("  STARTING FULL BOOKING SCREEN VALIDATION");
		System.out.println("=".repeat(60));

		JSONObject jsonResponse = new JSONObject(responseBody);
		JSONArray journeys;

		try {
			// Correct path based on your JSON: ValidateSearchResponse ->
			// flightrecommendations[0] -> FlightRecommendation -> flights
			JSONObject validateResp = jsonResponse.getJSONObject("ValidateSearchResponse");
			JSONArray recommendations = validateResp.getJSONArray("flightrecommendations");
			JSONObject flightRec = recommendations.getJSONObject(0).getJSONObject("FlightRecommendation");
			journeys = flightRec.getJSONArray("flights");
		} catch (Exception e) {
			log.ReportEvent("FAIL", "JSON path error: " + e.getMessage());
			Assert.fail("Failed to parse the ValidateSearchResponse path.");
			return;
		}

		JSONObject matchedJourney = null;

		// --- 1. NORMALIZE UI VALUES WITH NULL CHECKS ---
		Map<String, String> firstSegment = uiSegments.get(0);
		String uiFirstDep = firstSegment.containsKey("depTime") ? firstSegment.get("depTime").replace(":", "").trim()
				: "";
		String uiFirstOrigin = firstSegment.containsKey("origin") ? firstSegment.get("origin").trim() : "";
		String uiFirstFlightNum = firstSegment.containsKey("flightNum") ? firstSegment.get("flightNum").trim() : "";

		// --- 2. FIND MATCHING JOURNEY ---
		for (int j = 0; j < journeys.length(); j++) {
			JSONObject journey = journeys.getJSONObject(j).getJSONObject("FlightObject");

			// Handle nesting: flightlegs -> [0] -> FlightLeg
			JSONObject legWrapper = journey.getJSONArray("flightlegs").getJSONObject(0);
			JSONObject firstLeg = (legWrapper.get("FlightLeg") instanceof JSONArray)
					? legWrapper.getJSONArray("FlightLeg").getJSONObject(0)
					: legWrapper.getJSONObject("FlightLeg");

			String apiFlightNum = String.valueOf(firstLeg.get("flightnumber")).trim();
			String apiDepTime = String.valueOf(firstLeg.get("deptime")).trim();
			String apiOrigin = firstLeg.getString("origin_name").toLowerCase();

			// MATCHING LOGIC: We check if UI flight number contains the API number
			// to handle cases like UI: "5151" vs API: "151"
			boolean match = apiDepTime.equals(uiFirstDep) && apiOrigin.contains(uiFirstOrigin.toLowerCase())
					&& (uiFirstFlightNum.contains(apiFlightNum) || apiFlightNum.contains(uiFirstFlightNum));

			if (match) {
				matchedJourney = journey;
				System.out.println("✅ Found Matching Journey in API: " + apiFlightNum);
				break;
			}
		}

		if (matchedJourney == null) {
			log.ReportEvent("FAIL",
					"Selected Journey not found in API. Searching for: " + uiFirstFlightNum + " at " + uiFirstDep);
			Assert.fail("API Matching Error: No journey found matching UI details.");
		}

		// --- 3. VALIDATE EVERY SEGMENT WITH NULL CHECKS ---
		// According to your JSON, baggage is in flightfares -> [0] -> FlightFare
		JSONObject fareObject = matchedJourney.getJSONArray("flightfares").getJSONObject(0).getJSONObject("FlightFare");

		// Extract legs for multi-stop validation
		List<JSONObject> legsToValidate = new ArrayList<>();
		JSONArray flightlegs = matchedJourney.getJSONArray("flightlegs");
		for (int k = 0; k < flightlegs.length(); k++) {
			JSONObject legWrapper = flightlegs.getJSONObject(k);
			if (legWrapper.get("FlightLeg") instanceof JSONArray) {
				JSONArray arr = legWrapper.getJSONArray("FlightLeg");
				for (int l = 0; l < arr.length(); l++)
					legsToValidate.add(arr.getJSONObject(l));
			} else {
				legsToValidate.add(legWrapper.getJSONObject("FlightLeg"));
			}
		}

		for (int i = 0; i < uiSegments.size(); i++) {
			Map<String, String> ui = uiSegments.get(i);
			JSONObject api = legsToValidate.get(i);
			boolean segmentPass = true;

			// Get UI values with null checks
			String uiOrigin = ui.containsKey("origin") ? ui.get("origin") : "N/A";
			String uiDestination = ui.containsKey("destination") ? ui.get("destination") : "N/A";
			String uiFlightNum = ui.containsKey("flightNum") ? ui.get("flightNum") : "N/A";
			String uiDepTime = ui.containsKey("depTime") ? ui.get("depTime").replace(":", "") : "";
			String uiArrTime = ui.containsKey("arrTime") ? ui.get("arrTime").replace(":", "") : "";
			String uiCheckin = ui.containsKey("checkin") ? ui.get("checkin") : "N/A";
			String uiCabinBag = ui.containsKey("cabinBag") ? ui.get("cabinBag") : "N/A";
			String uiPolicyStatus = ui.containsKey("policyStatus") ? ui.get("policyStatus") : "N/A";

			System.out.println("\n🔍 VALIDATING SEGMENT #" + (i + 1) + " [" + uiFlightNum + "]");

			segmentPass &= compare("Origin", uiOrigin, api.getString("origin_name"), log);
			segmentPass &= compare("Destination", uiDestination, api.getString("destination_name"), log);

			// Use contains to allow for partial matches (151 vs 5151)
			String apiFlight = String.valueOf(api.get("flightnumber"));
			boolean flightMatch = uiFlightNum.contains(apiFlight) || apiFlight.contains(uiFlightNum);
			if (!flightMatch) {
				log.ReportEvent("FAIL", "Flight Number mismatch: UI [" + uiFlightNum + "] API [" + apiFlight + "]");
				segmentPass = false;
			}

			segmentPass &= compare("Departure Time", uiDepTime, String.valueOf(api.get("deptime")), log);
			segmentPass &= compare("Arrival Time", uiArrTime, String.valueOf(api.get("arrtime")), log);

			// Baggage
			segmentPass &= compare("Check-in Baggage", uiCheckin, fareObject.getString("freebaggage"), log);
			segmentPass &= compare("Cabin Baggage", uiCabinBag, fareObject.getString("freehandbaggage"), log);

			// Policy
			String apiPolicy = matchedJourney.getBoolean("inpolicy") ? "In Policy" : "Out of Policy";
			segmentPass &= compare("Policy Compliance", uiPolicyStatus, apiPolicy, log);

			if (segmentPass) {
				log.ReportEvent("PASS", "Segment #" + (i + 1) + " validated.");
			} else {
				ScreenShots.takeScreenShot();
				log.ReportEvent("FAIL", "Segment #" + (i + 1) + " validation failed.");
			}
		}
	}

	/**
	 * 
	 * Flexible comparison to handle casing and partial matches (e.g., "Kochi" vs
	 * "Kochi, India")
	 */
	private boolean compare(String field, String uiVal, String apiVal, Log log) {
		if (uiVal == null || apiVal == null)
			return false;

		boolean match = uiVal.equalsIgnoreCase(apiVal) || apiVal.toLowerCase().contains(uiVal.toLowerCase())
				|| uiVal.toLowerCase().contains(apiVal.toLowerCase());

		System.out.println(
				(match ? "✅ " : "❌ ") + String.format("%-18s", field) + ": UI[" + uiVal + "] | API[" + apiVal + "]");
		return match;
	}

	/**
	 * Helper to match API minutes (e.g., 105) to UI text (e.g., "1h 45m")
	 */
//	private String convertMinutesToUiFormat(int totalMinutes) {
//	    int hrs = totalMinutes / 60;
//	    int mins = totalMinutes % 60;
//	    return hrs + "h " + mins + "m";
//	}
//

	// get all the meals data
	public List<Map<String, String>> getOnewayBookingpgAllAvailableMeals(Log log, ScreenShots ScreenShots,
			int sectorIndex) {
		List<Map<String, String>> mealList = new ArrayList<>();
		try {
			// Grab the name of the current active sector tab
			String sectorNameXpath = "((//div[contains(@class,'special-service-request_sector-tabs')])[2]/div)["
					+ sectorIndex + "]";
			String currentSector = driver.findElement(By.xpath(sectorNameXpath)).getText().trim();

			List<WebElement> mealCards = driver
					.findElements(By.xpath("//div[contains(@class,'meal-selection_meal-tab_info')]"));

			for (WebElement card : mealCards) {
				Map<String, String> mealData = new HashMap<>();
				String name = card.findElement(By.xpath(".//div[@class='fw-600'][1]")).getText().trim();
				String price = card
						.findElement(By.xpath(".//div[contains(@class,'align-items-end')]//div[@class='fw-600']"))
						.getText().trim();

				mealData.put("Mealsector", currentSector);
				mealData.put("mealName", name);
				mealData.put("mealPrice", price);
				mealList.add(mealData);
			}
		} catch (Exception e) {
			log.ReportEvent("INFO", "Could not scrape meals for sector index: " + sectorIndex);
		}
		return mealList;
	}

	// get all the seats data
	/*
	 * public List<Map<String, String>> getOnewayBookingpgAllSeatData(Log log,
	 * ScreenShots ScreenShots, int sectorIndex) { List<Map<String, String>>
	 * seatList = new ArrayList<>(); Actions action = new Actions(driver); //
	 * Increased wait slightly for stability, but we will catch it gracefully
	 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2)); int
	 * missingTooltipCount = 0;
	 * 
	 * try { String sectorXpath =
	 * "((//div[contains(@class,'special-service-request_sector-tabs')])[2]/div)[" +
	 * sectorIndex + "]"; String currentSector =
	 * driver.findElement(By.xpath(sectorXpath)).getText().trim();
	 * 
	 * List<WebElement> seatContainers =
	 * driver.findElements(By.xpath("//div[contains(@class,'flight-seat-map_seat')]"
	 * ));
	 * 
	 * for (WebElement seat : seatContainers) { Map<String, String> seatData = new
	 * HashMap<>(); String classAttr = seat.getAttribute("class").toLowerCase();
	 * 
	 * // 1. Enhanced "Closed/Occupied" Check // Checks for blocked class,
	 * aria-disabled, or the specific blocked SVG boolean isBlocked =
	 * classAttr.contains("blocked") || classAttr.contains("closed") ||
	 * classAttr.contains("occupied") ||
	 * !seat.findElements(By.xpath(".//img[contains(@src, 'seat-blocked')]")).
	 * isEmpty();
	 * 
	 * if (isBlocked) { String designator = seat.getAttribute("title"); if
	 * (designator == null || designator.isEmpty()) designator = "N/A";
	 * 
	 * seatData.put("SeatSector", currentSector); seatData.put("SeatStatus",
	 * "Closed"); seatData.put("SeatDesignator", designator);
	 * seatData.put("SeatPrice", "0"); } else { // 2. Hover Logic for Open Seats try
	 * { action.moveToElement(seat).perform(); WebElement tooltip =
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(
	 * By.xpath("//div[contains(@class, 'tooltip') or contains(@class, 'popover')]")
	 * ));
	 * 
	 * String tooltipText = tooltip.getText(); // Robust extraction logic String
	 * designator = tooltipText.split("\\s+")[0].trim(); String price =
	 * tooltipText.contains("₹") ?
	 * tooltipText.split("₹")[1].split("\\n")[0].replaceAll("[^0-9]", "") : "0";
	 * 
	 * seatData.put("SeatSector", currentSector); seatData.put("SeatStatus",
	 * "Open"); seatData.put("SeatDesignator", designator);
	 * seatData.put("SeatPrice", price); } catch (Exception e) {
	 * missingTooltipCount++; } } if(seatData.containsKey("SeatDesignator"))
	 * seatList.add(seatData); }
	 * 
	 * // 3. Summarized Logging (Prevents Spam) if (missingTooltipCount > 0) {
	 * log.ReportEvent("INFO", "Sector " + sectorIndex +
	 * ": Successfully scraped seats. Note: " + missingTooltipCount +
	 * " tooltips failed to load."); }
	 * 
	 * } catch (Exception e) { log.ReportEvent("FAIL",
	 * "Critical failure scraping Sector " + sectorIndex + ": " + e.getMessage()); }
	 * return seatList; }
	 */

	public List<Map<String, String>> getOnewayBookingpgAllSeatData(Log log, ScreenShots ScreenShots, int sectorIndex) {
		List<Map<String, String>> seatList = new ArrayList<>();
		Actions action = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

		try {
			String sectorXpath = "((//div[contains(@class,'special-service-request_sector-tabs')])[2]/div)["
					+ sectorIndex + "]";
			String currentSector = driver.findElement(By.xpath(sectorXpath)).getText().trim();

			List<WebElement> seatContainers = driver
					.findElements(By.xpath("//div[contains(@class,'flight-seat-map_seat')]"));
			int limit = Math.min(seatContainers.size(), 20);

			for (int i = 0; i < limit; i++) {
				WebElement seat = seatContainers.get(i);
				Map<String, String> seatData = new HashMap<>();
				String classAttr = seat.getAttribute("class").toLowerCase();

				boolean isClosed = classAttr.contains("blocked") || classAttr.contains("occupied")
						|| classAttr.contains("unavailable")
						|| !seat.findElements(By.xpath(".//*[local-name()='svg']")).isEmpty();

				if (isClosed) {
					String designator = seat.getAttribute("title");
					if (designator == null || designator.isEmpty())
						designator = seat.getAttribute("aria-label");
					if (designator == null || designator.isEmpty())
						designator = "N/A";

					seatData.put("SeatSector", currentSector);
					seatData.put("SeatStatus", "Closed");
					seatData.put("SeatDesignator", designator.replaceAll("[^a-zA-Z0-9]", ""));
					seatData.put("SeatPrice", "0");
				} else {
					try {
						action.moveToElement(seat).perform();
						WebElement tooltip = wait.until(ExpectedConditions.visibilityOfElementLocated(
								By.xpath("//div[contains(@class, 'tooltip') or contains(@class, 'popover')]")));

						String tooltipText = tooltip.getText();
						String designator = tooltipText.split("\\s+")[0].trim();

						// Extracts only the numeric price following INR or ₹
						String price = "0";
						java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?:INR|₹)\\s*([0-9,]+)");
						java.util.regex.Matcher m = p.matcher(tooltipText);
						if (m.find()) {
							price = m.group(1).replaceAll(",", "");
						}

						seatData.put("SeatSector", currentSector);
						seatData.put("SeatStatus", "Open");
						seatData.put("SeatDesignator", designator);
						seatData.put("SeatPrice", price);
					} catch (Exception e) {
						String altDesignator = seat.getAttribute("title");
						if (altDesignator != null) {
							seatData.put("SeatSector", currentSector);
							seatData.put("SeatStatus", "Open");
							seatData.put("SeatDesignator", altDesignator);
							seatData.put("SeatPrice", "0");
						}
					}
				}
				if (seatData.containsKey("SeatDesignator") && !seatData.get("SeatDesignator").equals("N/A")) {
					seatList.add(seatData);
				}
			}
			log.ReportEvent("INFO", "Sector " + sectorIndex + ": Scraped " + seatList.size() + " seats.");
		} catch (Exception e) {
			log.ReportEvent("FAIL", "Scraping failure: " + e.getMessage());
		}
		return seatList;
	}

	// get all the baggage data
	public List<Map<String, String>> getOnewayBookingpgAllAvailableBaggage(Log log, ScreenShots ScreenShots,
			int sectorIndex) {
		List<Map<String, String>> baggageList = new ArrayList<>();
		try {
			String sectorNameXpath = "((//div[contains(@class,'special-service-request_sector-tabs')])[2]/div)["
					+ sectorIndex + "]";
			String currentSector = driver.findElement(By.xpath(sectorNameXpath)).getText().trim();

			List<WebElement> cards = driver
					.findElements(By.xpath("//div[contains(@class,'meal-selection_meal-tab_info')]"));

			for (WebElement card : cards) {
				Map<String, String> data = new HashMap<>();
				// Only taking Text and Price
				String name = card.findElement(By.xpath("(.//div[@class='fw-600'])[1]")).getText().trim();
				String price = card.findElement(By.xpath("(.//div[@class='fw-600'])[2]")).getText().trim();

				data.put("Baggagesector", currentSector);
				data.put("baggageName", name);
				data.put("baggagePrice", price);
				baggageList.add(data);
			}
		} catch (Exception e) {
			log.ReportEvent("INFO", "Baggage scrape failed for sector " + sectorIndex);
		}
		return baggageList;
	}

	// get all the special req data
	public List<Map<String, String>> getOnewayBookingpgAllAvailableSpecialRequests(Log log, ScreenShots ScreenShots,
			int sectorIndex) {
		List<Map<String, String>> specialRequestList = new ArrayList<>();
		try {
			// Find the sector name currently selected
			String sectorNameXpath = "((//div[contains(@class,'special-service-request_sector-tabs')])[2]/div)["
					+ sectorIndex + "]";
			String currentSector = driver.findElement(By.xpath(sectorNameXpath)).getText().trim();

			// Find all cards (same container class as meals)
			List<WebElement> cards = driver
					.findElements(By.xpath("//div[contains(@class,'meal-selection_meal-tab_info')]"));

			for (WebElement card : cards) {
				Map<String, String> data = new HashMap<>();

				// Relative XPaths inside the 'card'
				// Text: "Extra Baggage 3KG"
				String name = card.findElement(By.xpath("(.//div[@class='fw-600'])[1]")).getText().trim();
				// Price: "₹ 1,800"
				String price = card.findElement(By.xpath("(.//div[@class='fw-600'])[2]")).getText().trim();

				data.put("SpecialReqsector", currentSector);
				data.put("SpecialReqName", name);
				data.put("SpecialReqPrice", price);
				specialRequestList.add(data);
			}
		} catch (Exception e) {
			log.ReportEvent("INFO", "Baggage scrape failed for sector " + sectorIndex);
		}
		return specialRequestList;
	}

	/*
	 * public void validateOnewayBookingPageSeatData(List<Map<String, String>>
	 * uiSeats, String responseBody, Log log, ScreenShots ScreenShots) { if
	 * (uiSeats.isEmpty()) return;
	 * 
	 * try { JSONObject jsonResponse = new JSONObject(responseBody); JSONObject
	 * result = jsonResponse.getJSONObject("Root").getJSONObject("Result");
	 * JSONArray seatMapArray =
	 * result.getJSONObject("list").getJSONArray("SeatMap"); String currentSector =
	 * uiSeats.get(0).get("SeatSector");
	 * 
	 * log.ReportEvent("INFO", "--- Validating Seat Map for Sector: " +
	 * currentSector + " ---"); List<String> mismatches = new ArrayList<>();
	 * 
	 * for (Map<String, String> uiSeat : uiSeats) { String uiDesignator =
	 * uiSeat.get("SeatDesignator"); String uiStatus = uiSeat.get("SeatStatus");
	 * String uiPrice = uiSeat.get("SeatPrice");
	 * 
	 * if (uiDesignator.equals("Blocked") || uiDesignator.equals("N/A")) continue;
	 * 
	 * boolean found = false; for (int i = 0; i < seatMapArray.length(); i++) {
	 * JSONArray seatRows =
	 * seatMapArray.getJSONObject(i).getJSONObject("seatrows").getJSONArray(
	 * "SeatRow"); for (int j = 0; j < seatRows.length(); j++) { JSONObject apiSeat
	 * = seatRows.getJSONObject(j);
	 * 
	 * if (apiSeat.getString("seatdesignator").equalsIgnoreCase(uiDesignator)) {
	 * found = true; String apiStatus = apiSeat.getString("seatstatus"); String
	 * apiPrice = String.valueOf((int)
	 * Double.parseDouble(apiSeat.getString("seatprice")));
	 * 
	 * if (!apiStatus.equalsIgnoreCase(uiStatus)) {
	 * mismatches.add("STATUS MISMATCH for Seat " + uiDesignator + ": UI [" +
	 * uiStatus + "] API [" + apiStatus + "]"); } if
	 * (uiStatus.equalsIgnoreCase("Open") && !uiPrice.equals(apiPrice)) {
	 * mismatches.add("PRICE MISMATCH for Seat " + uiDesignator + ": UI [" + uiPrice
	 * + "] API [" + apiPrice + "]"); } break; } } if (found) break; } if (!found)
	 * mismatches.add("API MISSING: Seat " + uiDesignator + " not found in API."); }
	 * 
	 * if (mismatches.isEmpty()) { log.ReportEvent("PASS", "SUCCESS: All seats for "
	 * + currentSector + " validated against API."); } else { for (String error :
	 * mismatches) log.ReportEvent("FAIL", error); ScreenShots.takeScreenShot(); } }
	 * catch (Exception e) { log.ReportEvent("FAIL", "Seat Validation Logic Error: "
	 * + e.getMessage()); } }
	 */

//	public void validateOnewayBookingPageSeatData(List<Map<String, String>> uiSeats, String responseBody, Log log, ScreenShots ScreenShots) {
//	    if (uiSeats.isEmpty()) return;
//
//	    try {
//	        JSONObject jsonResponse = new JSONObject(responseBody);
//	        JSONObject seatMapObj = jsonResponse.getJSONObject("Root")
//	                                            .getJSONObject("Result")
//	                                            .getJSONObject("list")
//	                                            .getJSONObject("SeatMap");
//	        
//	        JSONArray seatRowArray = seatMapObj.getJSONObject("seatrows").getJSONArray("SeatRow");
//	        
//	        Map<String, JSONObject> apiSeatMap = new HashMap<>();
//	        for (int i = 0; i < seatRowArray.length(); i++) {
//	            JSONObject seat = seatRowArray.getJSONObject(i);
//	            apiSeatMap.put(seat.getString("seatdesignator").toUpperCase(), seat);
//	        }
//
//	        List<String> mismatches = new ArrayList<>();
//	        for (Map<String, String> uiSeat : uiSeats) {
//	            String uiDesignator = uiSeat.get("SeatDesignator").toUpperCase();
//	            
//	            if (apiSeatMap.containsKey(uiDesignator)) {
//	                JSONObject apiSeat = apiSeatMap.get(uiDesignator);
//	                String apiStatus = apiSeat.getString("seatstatus");
//	                String apiPrice = String.valueOf((int) Double.parseDouble(apiSeat.getString("seatprice")));
//	                String uiPrice = uiSeat.get("SeatPrice").replaceAll("[^0-9]", "");
//
//	                if (!apiStatus.equalsIgnoreCase(uiSeat.get("SeatStatus"))) {
//	                    mismatches.add("STATUS MISMATCH [" + uiDesignator + "]: UI=" + uiSeat.get("SeatStatus") + " API=" + apiStatus);
//	                }
//	                
//	                if (uiSeat.get("SeatStatus").equalsIgnoreCase("Open") && !uiPrice.equals(apiPrice)) {
//	                    mismatches.add("PRICE MISMATCH [" + uiDesignator + "]: UI=" + uiPrice + " API=" + apiPrice);
//	                }
//	            }
//	        }
//
//	        if (mismatches.isEmpty()) {
//	            log.ReportEvent("PASS", "SUCCESS: Seat data matches API.");
//	        } else {
//	            for (String error : mismatches) log.ReportEvent("FAIL", error);
//	            ScreenShots.takeScreenShot();
//	        }
//	    } catch (Exception e) {
//	        log.ReportEvent("FAIL", "Validation Logic Error: " + e.getMessage());
//	    }
//	}

	public void validateOnewayBookingPageSeatData(List<Map<String, String>> uiSeats, String responseBody, Log log,
			ScreenShots ScreenShots) {
		if (uiSeats.isEmpty())
			return;

		try {
			JSONObject jsonResponse = new JSONObject(responseBody);
			JSONObject seatMapObj = jsonResponse.getJSONObject("Root").getJSONObject("Result").getJSONObject("list")
					.getJSONObject("SeatMap");

			JSONArray seatRowArray = seatMapObj.getJSONObject("seatrows").getJSONArray("SeatRow");

			Map<String, JSONObject> apiSeatMap = new HashMap<>();
			for (int i = 0; i < seatRowArray.length(); i++) {
				JSONObject seat = seatRowArray.getJSONObject(i);
				apiSeatMap.put(seat.getString("seatdesignator").toUpperCase(), seat);
			}

			// --- Added Sector Info Log ---
			String currentSector = uiSeats.get(0).get("SeatSector");
			log.ReportEvent("INFO", "--- Validating Seat Map for Sector: " + currentSector + " ---");

			List<String> mismatches = new ArrayList<>();
			for (Map<String, String> uiSeat : uiSeats) {
				String uiDesignator = uiSeat.get("SeatDesignator").toUpperCase();

				if (apiSeatMap.containsKey(uiDesignator)) {
					JSONObject apiSeat = apiSeatMap.get(uiDesignator);
					String apiStatus = apiSeat.getString("seatstatus");
					String apiPrice = String.valueOf((int) Double.parseDouble(apiSeat.getString("seatprice")));
					String uiPrice = uiSeat.get("SeatPrice").replaceAll("[^0-9]", "");

					if (!apiStatus.equalsIgnoreCase(uiSeat.get("SeatStatus"))) {
						mismatches.add("STATUS MISMATCH [" + uiDesignator + "]: UI=" + uiSeat.get("SeatStatus")
								+ " API=" + apiStatus);
					}

					if (uiSeat.get("SeatStatus").equalsIgnoreCase("Open") && !uiPrice.equals(apiPrice)) {
						mismatches.add("PRICE MISMATCH [" + uiDesignator + "]: UI=" + uiPrice + " API=" + apiPrice);
					}
				}
			}

			if (mismatches.isEmpty()) {
				log.ReportEvent("PASS", "SUCCESS: Seat data matches API for " + currentSector);
			} else {
				for (String error : mismatches)
					log.ReportEvent("FAIL", error);
				ScreenShots.takeScreenShot();
			}
		} catch (Exception e) {
			log.ReportEvent("FAIL", "Validation Logic Error: " + e.getMessage());
		}
	}

	public void validateRTCombinedBookingPageSeatData(List<Map<String, String>> uiSeats, String responseBody, Log log,
			ScreenShots ScreenShots) {
		if (uiSeats == null || uiSeats.isEmpty())
			return;

		try {
			JSONObject jsonResponse = new JSONObject(responseBody);
			JSONArray seatMapArray = jsonResponse.getJSONObject("Root").getJSONObject("Result").getJSONObject("list")
					.getJSONArray("SeatMap");

			// UI Sector name (e.g., "ADD - FIH" -> Clean it to "ADD-FIH")
			String rawUiSector = uiSeats.get(0).get("SeatSector").toUpperCase().trim();
			String cleanUiSector = rawUiSector.replaceAll("\\s+", ""); // Removes all spaces
			log.ReportEvent("INFO", "--- Validating Sector: " + cleanUiSector + " ---");

			JSONObject matchedApiSector = null;

			// 1. Loop through the array to find the object containing our Origin and
			// Destination
			for (int i = 0; i < seatMapArray.length(); i++) {
				JSONObject currentMap = seatMapArray.getJSONObject(i);

				// optString is safer if keys are missing
				String apiOrigin = currentMap.optString("origin", "").trim();
				String apiDest = currentMap.optString("destination", "").trim();
				String apiSectorKey = apiOrigin + "-" + apiDest;

				if (cleanUiSector.equalsIgnoreCase(apiSectorKey)) {
					matchedApiSector = currentMap;
					log.ReportEvent("INFO", "Found API SeatMap for Flight: " + currentMap.optString("flightnumber"));
					break;
				}
			}

			if (matchedApiSector == null) {
				log.ReportEvent("FAIL", "API SeatMap does not contain data for sector: " + cleanUiSector);
				return;
			}

			// 2. Map the Seats only for THIS specific sector object
			JSONArray seatRowArray = matchedApiSector.getJSONObject("seatrows").getJSONArray("SeatRow");
			Map<String, JSONObject> apiSeatLookup = new HashMap<>();

			for (int i = 0; i < seatRowArray.length(); i++) {
				JSONObject seat = seatRowArray.getJSONObject(i);
				// Convert "21-A" to "21A" to match UI
				String designator = seat.getString("seatdesignator").toUpperCase().replace("-", "").trim();
				apiSeatLookup.put(designator, seat);
			}

			// 3. Compare UI data
			List<String> mismatches = new ArrayList<>();
			for (Map<String, String> uiSeat : uiSeats) {
				String uiDesignator = uiSeat.get("SeatDesignator").toUpperCase().replace("-", "").trim();

				if (apiSeatLookup.containsKey(uiDesignator)) {
					JSONObject apiSeat = apiSeatLookup.get(uiDesignator);

					// Status Validation
					String apiStatus = apiSeat.getString("seatstatus"); // e.g., "Closed"
					String uiStatus = uiSeat.get("SeatStatus"); // e.g., "Closed" or "Occupied"

					// Normalize: UI 'Occupied' or 'Closed' should match API 'Closed'
					boolean isUiUnavailable = uiStatus.equalsIgnoreCase("Closed")
							|| uiStatus.equalsIgnoreCase("Occupied");
					boolean isApiUnavailable = apiStatus.equalsIgnoreCase("Closed")
							|| apiStatus.equalsIgnoreCase("Occupied");

					if (isUiUnavailable != isApiUnavailable) {
						mismatches.add("STATUS MISMATCH [" + uiDesignator + "]: UI=" + uiStatus + " API=" + apiStatus);
					}

					// Price Validation (Only if seat is Open)
					if (!isUiUnavailable) {
						String uiPrice = uiSeat.get("SeatPrice").replaceAll("[^0-9]", "");
						// Handle "0.0" or "1500.0"
						double priceVal = Double.parseDouble(apiSeat.getString("seatprice"));
						String apiPrice = String.valueOf((int) priceVal);

						if (!uiPrice.equals(apiPrice)) {
							mismatches.add("PRICE MISMATCH [" + uiDesignator + "]: UI=" + uiPrice + " API=" + apiPrice);
						}
					}
				}
			}

			if (mismatches.isEmpty()) {
				log.ReportEvent("PASS", "Success: All " + uiSeats.size() + " seats match for sector " + cleanUiSector);
			} else {
				for (String error : mismatches)
					log.ReportEvent("FAIL", error);
				ScreenShots.takeScreenShot();
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Seat Validation Exception: " + e.getMessage());
		}
	}

	public void validateOnewayBookingPageMealData(List<Map<String, String>> uiMeals, String responseBody, Log log,
			ScreenShots ScreenShots) {
		if (uiMeals == null || uiMeals.isEmpty()) {
			log.ReportEvent("INFO", "Meal Validation: No UI data found to validate for this sector.");
			return;
		}

		try {
			JSONObject jsonResponse = new JSONObject(responseBody);
			JSONArray apiSSR = jsonResponse.getJSONArray("list").getJSONObject(0).getJSONArray("CarrierSSR");
			String currentSector = uiMeals.get(0).get("Mealsector");

			log.ReportEvent("INFO", "--- Validating Meal Preferences for Sector: " + currentSector + " ---");
			List<String> mismatches = new ArrayList<>();

			for (Map<String, String> uiMeal : uiMeals) {
				// Trim UI name and remove any weird characters
				String uiName = uiMeal.get("mealName").replace("\u00a0", " ").trim();
				String uiPrice = uiMeal.get("mealPrice").replaceAll("[^0-9]", "");

				boolean nameFoundInApi = false;
				String apiPriceFound = "";

				for (int i = 0; i < apiSSR.length(); i++) {
					JSONObject api = apiSSR.getJSONObject(i);

					// 1. Clean the API name just like the UI name
					String apiName = api.getString("ssrname").replace("\u00a0", " ").trim();
					String apiRoute = api.getString("depstation") + " - " + api.getString("arrstation");

					// 2. Compare using cleaned names
					if (apiRoute.equalsIgnoreCase(currentSector) && apiName.equalsIgnoreCase(uiName)) {
						nameFoundInApi = true;
						apiPriceFound = String.valueOf(api.getInt("chargeableamount"));
						break;
					}
				}

				if (!nameFoundInApi) {
					mismatches.add(
							"API MISSING: '" + uiName + "' found on UI but missing in API (Check for hidden spaces).");
				} else if (!uiPrice.equals(apiPriceFound)) {
					mismatches.add(
							"PRICE MISMATCH for '" + uiName + "': UI [" + uiPrice + "] vs API [" + apiPriceFound + "]");
				}
			}

			if (mismatches.isEmpty()) {
				log.ReportEvent("PASS", "SUCCESS: All meals and prices match API for " + currentSector);
			} else {
				for (String error : mismatches) {
					log.ReportEvent("FAIL", "SECTOR [" + currentSector + "] ERROR: " + error);
				}
				ScreenShots.takeScreenShot();
			}
		} catch (Exception e) {
			log.ReportEvent("FAIL", "Meal Validation Exception: " + e.getMessage());
		}
	}

	public void validateOnewayBookingPageBaggageData(List<Map<String, String>> uiBaggage, String responseBody, Log log,
			ScreenShots ScreenShots) {
		if (uiBaggage == null || uiBaggage.isEmpty())
			return;

		try {
			JSONObject jsonResponse = new JSONObject(responseBody);
			JSONArray apiSSRArray = jsonResponse.getJSONArray("list").getJSONObject(0).getJSONArray("CarrierSSR");
			String currentSector = uiBaggage.get(0).get("Baggagesector");

			log.ReportEvent("INFO", "--- Validating Baggage for Sector: " + currentSector + " ---");

			// 1. Create a local copy of API items for this sector to track "Used" status
			List<JSONObject> apiSectorItems = new ArrayList<>();
			for (int i = 0; i < apiSSRArray.length(); i++) {
				JSONObject item = apiSSRArray.getJSONObject(i);
				String apiRoute = item.getString("depstation") + " - " + item.getString("arrstation");
				if (apiRoute.equalsIgnoreCase(currentSector)) {
					apiSectorItems.add(item);
				}
			}

			// To keep track of which API index we have already "matched" with a UI row
			Set<Integer> matchedApiIndices = new HashSet<>();
			List<String> mismatches = new ArrayList<>();

			// 2. Iterate through each Baggage item found on the UI
			for (Map<String, String> uiBag : uiBaggage) {
				String uiName = uiBag.get("baggageName").replace("\u00a0", " ").trim();
				String uiPrice = uiBag.get("baggagePrice").replaceAll("[^0-9]", "");

				boolean foundMatch = false;

				// 3. Search through the API items for this sector
				for (int j = 0; j < apiSectorItems.size(); j++) {
					// Skip if this specific API record was already linked to a previous UI row
					if (matchedApiIndices.contains(j))
						continue;

					JSONObject apiItem = apiSectorItems.get(j);
					String apiName = apiItem.getString("ssrname").replace("\u00a0", " ").trim();
					String apiPrice = String.valueOf(apiItem.getInt("chargeableamount"));

					// Check if BOTH Name and Price match
					if (apiName.equalsIgnoreCase(uiName) && apiPrice.equals(uiPrice)) {
						foundMatch = true;
						matchedApiIndices.add(j); // Mark this API index as "Used" for this specific UI row
						break;
					}
				}

				if (!foundMatch) {
					mismatches.add("NOT FOUND: UI Item '" + uiName + "' with Price [" + uiPrice
							+ "] has no corresponding available match in API.");
				}
			}

			// 4. Reporting
			if (mismatches.isEmpty()) {
				log.ReportEvent("PASS", "SUCCESS: All baggage items matched correctly with API.");
			} else {
				for (String error : mismatches)
					log.ReportEvent("FAIL", "BAGGAGE ERROR: " + error);
				ScreenShots.takeScreenShot();
			}
		} catch (Exception e) {
			log.ReportEvent("FAIL", "Baggage Validation Error: " + e.getMessage());
		}
	}

	public void validateOnewayBookingPageSpecialRequests(List<Map<String, String>> uiSpecialReqs, String responseBody,
			Log log, ScreenShots ScreenShots) {
		if (uiSpecialReqs == null || uiSpecialReqs.isEmpty()) {
			log.ReportEvent("FAIL", "UI DATA MISSING: No special requests found on UI.");
			return;
		}

		try {
			JSONObject jsonResponse = new JSONObject(responseBody);
			JSONArray apiSSR = jsonResponse.getJSONArray("list").getJSONObject(0).getJSONArray("CarrierSSR");
			String currentSector = uiSpecialReqs.get(0).get("SpecialReqsector");
			List<String> mismatches = new ArrayList<>();

			for (Map<String, String> uiReq : uiSpecialReqs) {
				String uiName = uiReq.get("SpecialReqName");
				String uiPrice = uiReq.get("SpecialReqPrice").replaceAll("[^0-9]", "");

				boolean nameFoundInApi = false;
				String apiPriceFound = "";

				for (int i = 0; i < apiSSR.length(); i++) {
					JSONObject api = apiSSR.getJSONObject(i);
					String apiRoute = api.getString("depstation") + " - " + api.getString("arrstation");

					if (apiRoute.equalsIgnoreCase(currentSector) && api.getString("ssrname").equalsIgnoreCase(uiName)) {
						nameFoundInApi = true;
						apiPriceFound = String.valueOf(api.getInt("chargeableamount"));
						break;
					}
				}

				if (!nameFoundInApi) {
					mismatches.add("API MISSING: Special Request '" + uiName + "' not found in API.");
				} else if (!uiPrice.equals(apiPriceFound)) {
					mismatches.add(
							"PRICE MISMATCH for '" + uiName + "': UI [" + uiPrice + "] vs API [" + apiPriceFound + "]");
				}
			}

			if (mismatches.isEmpty()) {
				log.ReportEvent("PASS", "ALL SPECIAL REQUESTS VALIDATED: All items match for Sector: " + currentSector);
			} else {
				for (String error : mismatches) {
					log.ReportEvent("FAIL", "SPECIAL REQUEST ERROR [" + currentSector + "]: " + error);
				}
				ScreenShots.takeScreenShot();
			}
		} catch (Exception e) {
			log.ReportEvent("FAIL", "Special Request Validation Error: " + e.getMessage());
		}
	}

	// -----------------round trip
	// ---------------------------------------------------------

	public List<Map<String, String>> getDynamicFlightDetailsForRoundTripCombined(int index) {
		List<Map<String, String>> allSegments = new ArrayList<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Base card path
		String cardPath = "(//div[contains(@class,'tg-flight-card')])[" + index + "]";

		try {
			WebElement mainCard = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cardPath)));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", mainCard);

			// Open details to make elements visible
			clickShowFlightDetailsByIndex(index);

			// --- 🛫 OUTBOUND (2 Segments) ---
			String outboundBase = cardPath + "//div[div[contains(text(),'Outbound Flight')]]/following-sibling::div";
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(outboundBase + "//div[contains(@class,'tg-mb-fromorigin')]")));

			List<WebElement> outOrigins = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-mb-fromorigin')]"));
			List<WebElement> outDests = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-fromdestination')]"));
			List<WebElement> outDeps = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-fromdeptime')]"));
			List<WebElement> outArrs = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-fromarrtime')]"));
			List<WebElement> outFlights = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-flightnumber')]"));
			List<WebElement> outCabins = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-cabinclass')]"));
			List<WebElement> outDurs = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'tg-mb-fromduration')]"));

			// Layovers usually appear between flight rows
			List<WebElement> outLayovers = driver
					.findElements(By.xpath(outboundBase + "//div[contains(@class,'oneway-card__connect_line_text')]"));

			for (int i = 0; i < outOrigins.size(); i++) {
				Map<String, String> seg = new HashMap<>();
				seg.put("direction", "Outbound");
				seg.put("origin", outOrigins.get(i).getText().trim().split(" \\(")[0]);
				seg.put("destination", outDests.get(i).getText().trim().split(" \\(")[0]);
				seg.put("depTime", outDeps.get(i).getText().trim());
				seg.put("arrTime", outArrs.get(i).getText().trim());
				seg.put("flightNum", outFlights.get(i).getText().trim().replaceAll("[^0-9]", ""));
				seg.put("cabin", i < outCabins.size() ? outCabins.get(i).getText().trim() : "N/A");
				seg.put("duration", i < outDurs.size() ? outDurs.get(i).getText().trim() : "N/A");

				// Assign layover to the segment if it exists (usually after the first leg)
				seg.put("layover", (i < outLayovers.size()) ? outLayovers.get(i).getText().trim() : "None");

				allSegments.add(seg);
			}

			// --- 🛬 RETURN (3 Segments) ---
			// FIX: Removed the extra "]" from cardPath concatenation
			String returnBase = cardPath
					+ "//div[contains(@class, 'sub-card')]//div[div[contains(text(),'Return Flight')]]/following-sibling::div";

			WebElement returnTrigger = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(returnBase + "//div[contains(@class,'tg-mb-toorigin')]")));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", returnTrigger);

			List<WebElement> retOrigins = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-mb-toorigin')]"));
			List<WebElement> retDests = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-todestination')]"));
			List<WebElement> retDeps = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-todeptime')]"));
			List<WebElement> retArrs = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-toarrtime')]"));
			List<WebElement> retFlights = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-flightnumber')]"));
			List<WebElement> retCabins = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-cabinclass')]"));
			List<WebElement> retDurs = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'tg-mb-toduration')]"));
			List<WebElement> retLayovers = driver
					.findElements(By.xpath(returnBase + "//div[contains(@class,'oneway-card__connect_line_text')]"));

			for (int i = 0; i < retOrigins.size(); i++) {
				Map<String, String> seg = new HashMap<>();
				seg.put("direction", "Return");
				seg.put("origin", retOrigins.get(i).getText().trim().split(" \\(")[0]);
				seg.put("destination", retDests.get(i).getText().trim().split(" \\(")[0]);
				seg.put("depTime", retDeps.get(i).getText().trim());
				seg.put("arrTime", retArrs.get(i).getText().trim());
				seg.put("flightNum", retFlights.get(i).getText().trim().replaceAll("[^0-9]", ""));
				seg.put("cabin", i < retCabins.size() ? retCabins.get(i).getText().trim() : "N/A");
				seg.put("duration", i < retDurs.size() ? retDurs.get(i).getText().trim() : "N/A");

				// Assign layover to the segment if it exists
				seg.put("layover", (i < retLayovers.size()) ? retLayovers.get(i).getText().trim() : "None");

				allSegments.add(seg);
			}
		} catch (Exception e) {
			System.out.println("❌ UI Scraper Error: " + e.getMessage());
			e.printStackTrace();
		}
		return allSegments;
	}

	private boolean validateFlightSegment(Map<String, String> uiSegment, JSONObject apiLeg, Log log) {
		boolean isMatch = true;

		// Extract UI data
		String uiFlightNum = uiSegment.get("flightNum").replaceAll("[^0-9]", "");
		String uiOrigin = uiSegment.get("origin").toLowerCase().trim();
		String uiDest = uiSegment.get("destination").toLowerCase().trim();
		int uiDur = convertHhMmToMinutes(uiSegment.get("duration"));

		// Extract API data
		String apiFlightNum = String.valueOf(apiLeg.get("flightnumber")).replaceAll("[^0-9]", "");
		String apiOrigin = apiLeg.getString("origin_name").toLowerCase();
		String apiDest = apiLeg.getString("destination_name").toLowerCase();
		int apiDur = apiLeg.getInt("sectorduration");

		log.ReportEvent("INFO",
				">>> Validating Segment: " + uiOrigin + " to " + uiDest + " (Flight: " + uiFlightNum + ") <<<");

		// Validate Origin
		if (!apiOrigin.contains(uiOrigin) && !uiOrigin.contains(apiOrigin)) {
			log.ReportEvent("FAIL", "Origin Mismatch: UI[" + uiOrigin + "] vs API[" + apiOrigin + "]");
			isMatch = false;
		}

		// Validate Destination
		if (!apiDest.contains(uiDest) && !uiDest.contains(apiDest)) {
			log.ReportEvent("FAIL", "Destination Mismatch: UI[" + uiDest + "] vs API[" + apiDest + "]");
			isMatch = false;
		}

		// Validate Duration (within 5 mins)
		if (Math.abs(uiDur - apiDur) > 5) {
			log.ReportEvent("FAIL", "Duration Mismatch: UI[" + uiDur + " mins] vs API[" + apiDur + " mins]");
			isMatch = false;
		}

		// Validate Layover if present
		if (uiSegment.containsKey("layover") && !uiSegment.get("layover").equalsIgnoreCase("None")) {
			int uiLayover = convertHhMmToMinutes(uiSegment.get("layover"));
			String apiLayover = String.valueOf(apiLeg.get("layovertime"));

			if (!String.valueOf(uiLayover).equals(apiLayover)) {
				log.ReportEvent("FAIL",
						"Layover Time Mismatch: UI[" + uiLayover + " mins] vs API[" + apiLayover + " mins]");
				isMatch = false;
			}
		}

		if (isMatch) {
			log.ReportEvent("PASS", "Segment validated: " + uiOrigin + " → " + uiDest);
		}

		return isMatch;
	}

	private boolean compare2(String field, String uiVal, String apiVal, Log log) {
		if (uiVal.trim().equals(apiVal.trim())) {
			log.ReportEvent("PASS", field + " Match: " + uiVal + " mins");
			return true;
		} else {
			log.ReportEvent("FAIL", field + " Mismatch: UI[" + uiVal + "] vs API[" + apiVal + "]");
			return false;
		}
	}

	// 2. THE HELPER METHOD (Place it here, at the bottom of the class)
	private int convertHhMmToMinutes(String timeStr) {
		if (timeStr == null || timeStr.isEmpty() || timeStr.equalsIgnoreCase("None"))
			return 0;
		int total = 0;
		try {
			String workStr = timeStr.toLowerCase();
			// Case 1: Handle Hours (e.g., "1h 30m" -> extract "1")
			if (workStr.contains("h")) {
				String hPart = workStr.split("h")[0].replaceAll("[^0-9]", "").trim();
				if (!hPart.isEmpty())
					total += Integer.parseInt(hPart) * 60;
			}
			// Case 2: Handle Minutes (e.g., "1h 30m" -> extract "30")
			if (workStr.contains("m")) {
				String mPart = "";
				if (workStr.contains("h")) {
					// Get text between 'h' and 'm'
					mPart = workStr.split("h")[1].split("m")[0].replaceAll("[^0-9]", "").trim();
				} else {
					// No 'h', just get everything before 'm'
					mPart = workStr.split("m")[0].replaceAll("[^0-9]", "").trim();
				}
				if (!mPart.isEmpty())
					total += Integer.parseInt(mPart);
			}
		} catch (Exception e) {
			System.out.println("Error parsing time string: " + timeStr);
			return 0;
		}
		return total;
	}

	public List<Map<String, String>> getCombinedRoundTripBookingFlightDetails() {
		List<Map<String, String>> allSegments = new ArrayList<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			TestExecutionNotifier.showExecutionPopup();

			// --- STEP 1: EXTRACT THE GLOBAL POLICY ---
			// Since there is only one policy for the whole combined trip
			String globalPolicy = "N/A";
			try {
				WebElement policyEl = driver.findElement(By.xpath("//div[contains(@class,'tg-policy')]"));
				globalPolicy = policyEl.getText().trim();
				System.out.println("Policy Detected: " + globalPolicy);
			} catch (Exception e) {
				System.out.println(" Policy element not found.");
			}

			// --- STEP 2: IDENTIFY OUTBOUND AND RETURN BLOCKS ---
			List<WebElement> detailBlocks = driver
					.findElements(By.xpath("//div[@class='flight-booking-page_flight-details']"));

			for (int i = 1; i <= detailBlocks.size(); i++) {
				String blockPath = "(//div[@class='flight-booking-page_flight-details'])[" + i + "]";
				String direction = (i == 1) ? "Outbound" : "Return";
				System.out.println(" Processing " + direction + " Flight (Block " + i + ")");

				// --- STEP 3: DYNAMIC EXPANSION (CLICK CONNECTING FLIGHTS) ---
				try {
					By expandBtnXpath = By.xpath(blockPath + "//button//span");
					List<WebElement> buttons = driver.findElements(expandBtnXpath);

					if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
						WebElement button = buttons.get(0);
						js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
						js.executeScript("arguments[0].click();", button);
						System.out.println("✅ Expanded " + direction + " segments.");
						Thread.sleep(1500);
					}
				} catch (Exception e) {
					System.out.println("ℹ️ " + direction + " is direct or already expanded.");
				}

				// --- STEP 4: SCOPED SEGMENT EXTRACTION ---
				List<WebElement> origins = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-origin-fullname')]"));
				List<WebElement> destinations = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-destination-fullname')]"));
				List<WebElement> depTimes = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-deptime')]"));
				List<WebElement> arrTimes = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-arrtime')]"));
				List<WebElement> cabins = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-cabinclass')]"));
				List<WebElement> durations = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-mb-duration')]"));
				List<WebElement> flightNums = driver
						.findElements(By.xpath(blockPath + "//div[contains(@class,'tg-flightnumber')]"));
				List<WebElement> checkinBaggage = driver
						.findElements(By.xpath(blockPath + "//strong[contains(@class,'tg-checkinbaggage')]"));
				List<WebElement> cabinBaggage = driver
						.findElements(By.xpath(blockPath + "//span[contains(@class,'tg-cabinbaggage')]"));

				for (int j = 0; j < origins.size(); j++) {
					Map<String, String> segmentData = new HashMap<>();

					segmentData.put("direction", direction);
					segmentData.put("origin", origins.get(j).getText().trim().split(" \\(")[0]);
					segmentData.put("destination", destinations.get(j).getText().trim().split(" \\(")[0]);
					segmentData.put("depTime", depTimes.get(j).getText().trim());
					segmentData.put("arrTime", arrTimes.get(j).getText().trim());
					segmentData.put("cabin", j < cabins.size() ? cabins.get(j).getText().trim() : "N/A");
					segmentData.put("duration", j < durations.size() ? durations.get(j).getText().trim() : "N/A");

					// Flight Number cleaning (e.g., CX-489 -> 489)
					String rawFlight = (j < flightNums.size()) ? flightNums.get(j).getText().trim() : "";
					String cleanFlightNum = rawFlight.contains("-") ? rawFlight.split("-")[1].trim()
							: rawFlight.replaceAll("[^0-9]", "");
					segmentData.put("flightNum", cleanFlightNum);

					// Apply the Global Policy extracted earlier
					segmentData.put("policy", globalPolicy);

					// Baggage (Check-in = strong, Cabin = span)
					segmentData.put("checkin",
							j < checkinBaggage.size() ? checkinBaggage.get(j).getText().trim() : "N/A");
					segmentData.put("cabinBag", j < cabinBaggage.size() ? cabinBaggage.get(j).getText().trim() : "N/A");

					allSegments.add(segmentData);
				}
			}
		} catch (Exception e) {
			System.out.println("❌ Extraction Error: " + e.getMessage());
		}

		return allSegments;
	}

	/*
	 * public void
	 * validateCombinedRoundTripSearchDataFromUIAndResponseBody(List<Map<String,
	 * String>> uiSegments, String responseBody, Log log, ScreenShots ScreenShots) {
	 * JSONObject jsonResponse = new JSONObject(responseBody);
	 * 
	 * // Standard path for your GA.json JSONArray recommendations =
	 * jsonResponse.getJSONArray("flightjourneys") .getJSONObject(0)
	 * .getJSONArray("recommendations");
	 * 
	 * log.ReportEvent("INFO", "Starting validation for " + uiSegments.size() +
	 * " UI segments.");
	 * 
	 * // We loop through the UI segments one by one for (Map<String, String> ui :
	 * uiSegments) { String uiFlightNum = ui.get("flightNum").replaceAll("[^0-9]",
	 * ""); String uiDep = normalizeUiTime(ui.get("depTime")); String uiArr =
	 * normalizeUiTime(ui.get("arrTime")); String direction = ui.get("direction");
	 * 
	 * boolean legMatched = false;
	 * 
	 * // CRITICAL: We search EVERY recommendation to find the one that matches your
	 * UI selection // In your case, Recommendation [2] contains all 5 segments for
	 * (int i = 0; i < recommendations.length(); i++) { JSONArray flights =
	 * recommendations.getJSONObject(i).getJSONArray("flights");
	 * 
	 * // Look inside every flight object (Onward cluster, Return cluster, etc.) for
	 * (int j = 0; j < flights.length(); j++) { JSONArray apiLegs =
	 * flights.getJSONObject(j).getJSONArray("flightlegs");
	 * 
	 * // Check every individual leg for (int k = 0; k < apiLegs.length(); k++) {
	 * JSONObject apiLeg = apiLegs.getJSONObject(k);
	 * 
	 * String apiFlightNum =
	 * String.valueOf(apiLeg.get("flightnumber")).replaceAll("[^0-9]", ""); String
	 * apiDep = apiLeg.getString("deptime").trim(); String apiArr =
	 * apiLeg.getString("arrtime").trim();
	 * 
	 * // --- THE EXACT MATCH CHECK --- // We match by Number + Dep Time + Arr Time
	 * to ensure we don't pick the wrong duplicate if
	 * (uiFlightNum.equals(apiFlightNum) && uiDep.equals(apiDep) &&
	 * uiArr.equals(apiArr)) {
	 * 
	 * log.ReportEvent("INFO", ">>> Validating Leg: " + uiFlightNum + " [" + uiDep +
	 * " -> " + uiArr + "] <<<");
	 * 
	 * boolean isMatch = true; isMatch &= compareField("Origin", ui.get("origin"),
	 * apiLeg.getString("origin_name"), log); isMatch &= compareField("Destination",
	 * ui.get("destination"), apiLeg.getString("destination_name"), log);
	 * 
	 * // Duration Check int uiDur = convertHhMmToMinutes(ui.get("duration")); int
	 * apiDur = apiLeg.getInt("sectorduration"); isMatch &= compareField("Duration",
	 * String.valueOf(uiDur), String.valueOf(apiDur), log);
	 * 
	 * // Layover Check if (ui.containsKey("layover") &&
	 * !ui.get("layover").equalsIgnoreCase("None")) { int uiLay =
	 * convertHhMmToMinutes(ui.get("layover")); int apiLay =
	 * apiLeg.optInt("layovertime", 0); isMatch &= compareField("Layover",
	 * String.valueOf(uiLay), String.valueOf(apiLay), log); }
	 * 
	 * if (isMatch) { log.ReportEvent("PASS", direction + " segment " + uiFlightNum
	 * + " matches API."); } else { log.ReportEvent("FAIL", direction + " segment "
	 * + uiFlightNum + " data mismatch."); ScreenShots.takeScreenShot(); }
	 * 
	 * legMatched = true; break; } } if (legMatched) break; } if (legMatched) break;
	 * }
	 * 
	 * if (!legMatched) { log.ReportEvent("FAIL", direction + " Flight " +
	 * uiFlightNum + " (" + uiDep + "-" + uiArr +
	 * ") NOT FOUND in any API recommendation."); ScreenShots.takeScreenShot(); } }
	 * }
	 */

	public void validateCombinedRoundTripSearchDataFromUIAndResponseBody(List<Map<String, String>> uiSegments,
			String responseBody, Log log, ScreenShots ScreenShots) {
		JSONObject jsonResponse = new JSONObject(responseBody);

		// Extract recommendations - the pool where all flight legs live
		JSONArray recommendations = jsonResponse.getJSONArray("flightjourneys").getJSONObject(0)
				.getJSONArray("recommendations");

		log.ReportEvent("INFO", "Starting validation for " + uiSegments.size() + " total UI segments.");

		// Loop through each segment from the UI
		for (Map<String, String> ui : uiSegments) {
			String uiFlightNum = ui.get("flightNum").replaceAll("[^0-9]", "");
			String uiDep = normalizeUiTime(ui.get("depTime"));
			String uiArr = normalizeUiTime(ui.get("arrTime"));
			String direction = ui.get("direction");

			boolean legMatched = false;

			// Search through EVERY recommendation in the API pool
			searchLoop: for (int i = 0; i < recommendations.length(); i++) {
				JSONArray flights = recommendations.getJSONObject(i).getJSONArray("flights");

				for (int j = 0; j < flights.length(); j++) {
					JSONArray apiLegs = flights.getJSONObject(j).getJSONArray("flightlegs");

					for (int k = 0; k < apiLegs.length(); k++) {
						JSONObject apiLeg = apiLegs.getJSONObject(k);

						String apiFlightNum = String.valueOf(apiLeg.get("flightnumber")).replaceAll("[^0-9]", "");
						String apiDep = apiLeg.getString("deptime").trim();
						String apiArr = apiLeg.getString("arrtime").trim();

						// Identification Check: Flight Number + Departure Time + Arrival Time
						if (uiFlightNum.equals(apiFlightNum) && uiDep.equals(apiDep) && uiArr.equals(apiArr)) {

							log.ReportEvent("INFO",
									">>> Validating Leg: " + uiFlightNum + " [" + uiDep + " -> " + uiArr + "] <<<");

							boolean isMatch = true;

							// 1. Origin/Destination Match
							isMatch &= compareField("Origin", ui.get("origin"), apiLeg.getString("origin_name"), log);
							isMatch &= compareField("Destination", ui.get("destination"),
									apiLeg.getString("destination_name"), log);

							// 2. Duration Match
							int uiDur = convertHhMmToMinutes(ui.get("duration"));
							int apiDur = apiLeg.getInt("sectorduration");
							isMatch &= compareField("Duration", String.valueOf(uiDur), String.valueOf(apiDur), log);

							// 3. Layover Match (if applicable)
							if (ui.containsKey("layover") && !ui.get("layover").equalsIgnoreCase("None")) {
								int uiLay = convertHhMmToMinutes(ui.get("layover"));
								int apiLay = apiLeg.optInt("layovertime", 0);
								isMatch &= compareField("Layover", String.valueOf(uiLay), String.valueOf(apiLay), log);
							}

							// Final Segment Decision
							if (isMatch) {
								log.ReportEvent("PASS", direction + " segment " + uiFlightNum + " matches API.");
							} else {
								log.ReportEvent("FAIL",
										direction + " segment " + uiFlightNum + " has data mismatches.");
								ScreenShots.takeScreenShot();
							}

							legMatched = true;
							break searchLoop;
						}
					}
				}
			}
		}
	}

	private String normalizeUiTime(String time) {
		if (time == null)
			return "";
		String clean = time.replace(":", "").trim();
		if (clean.length() == 3)
			clean = "0" + clean;
		return clean;
	}

	private boolean compareField(String field, String uiVal, String apiVal, Log log) {
		// Flexible matching for names (e.g., "Maya Maya" vs "Maya Maya Airport")
		if (apiVal.toLowerCase().contains(uiVal.toLowerCase().trim()) || uiVal.trim().equalsIgnoreCase(apiVal.trim())) {
			log.ReportEvent("PASS", field + " Match: " + apiVal);
			return true;
		} else {
			log.ReportEvent("FAIL", field + " Mismatch: UI[" + uiVal + "] vs API[" + apiVal + "]");
			return false;
		}
	}

	private boolean compare1(String field, String expected, String actual, Log log) {
		if (expected != null && expected.equalsIgnoreCase(actual)) {
			return true;
		} else {
			log.ReportEvent("FAIL", field + " mismatch: Expected [" + expected + "], but found [" + actual + "]");
			return false;
		}
	}

	public void clickEmulBtnInMbView() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("(//div[contains(@class,'tg-avatar_rounded undefined')])[5]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", closeBtn);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
			System.out.println(" 'Emul' button clicked via JavaScript.");
		} catch (Exception e) {
			System.out.println(" 'Emul' button not clickable or not found, skipping...");
		}
	}

}