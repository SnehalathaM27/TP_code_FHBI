package com.tripgain.collectionofpages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class NewDesign_Trips {
	WebDriver driver;

	public NewDesign_Trips(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to clik on trips
	public void clcikOnTrips() {
		driver.findElement(By.xpath("//span[text()='Trips']")).click();
	}

	// Method to click on create trip
	public void createTrip() {
		driver.findElement(By.xpath("//button[text()='Create Trip']")).click();
	}

	// Method to clcik and enter craete trip values

	// Method to enter name the trip
	public String enterNameThisTrip(String tripName, Log log) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

			WebElement inputField = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='tripname']")));

			// Scroll into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inputField);
			Thread.sleep(300);

			// Click to focus
			inputField.click();
			Thread.sleep(200);

			// Best clearing method for React Inputs
			inputField.sendKeys(Keys.CONTROL, "a");
			inputField.sendKeys(Keys.DELETE);

			// Enter text
			inputField.sendKeys(tripName);

			log.ReportEvent("INFO", "Entered trip name: " + tripName);
			return tripName;

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Failed to enter trip name: " + e.getMessage());
			return null;
		}
	}

	@FindBy(xpath = "//*[contains(@id,'origin')]")
	private WebElement enterLocation;

	public String enterfrom(String location) throws TimeoutException {
		enterLocation.clear();
		enterLocation.sendKeys(location);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

		selectCityto(location);

		return location; // return the input location
	}

	public void selectlocations(String location) throws TimeoutException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role='listbox']/parent::div")));

			wait.until(driver -> driver.findElements(By.xpath("//div[@role='option']")).size() > 0);

			List<WebElement> options = driver.findElements(By.xpath("(//div[@role='option'])[1]"));
			int bestScore = Integer.MAX_VALUE;
			String bestMatchText = null;

			String input = location.trim().toLowerCase();

			for (int i = 0; i < options.size(); i++) {
				try {
					WebElement option = options.get(i);
					String suggestion = option.getText().trim().toLowerCase();
					int score = levenshteinDistance(input, suggestion);

					if (score < bestScore) {
						bestScore = score;
						bestMatchText = option.getText().trim();
					}
				} catch (StaleElementReferenceException e) {
					System.out.println("Stale element at index " + i + ", skipping.");
				}
			}

			if (bestMatchText != null) {
				int attempts = 0;
				boolean clicked = false;
				while (attempts < 3 && !clicked) {
					try {
						// Re-fetch the element each attempt to avoid stale references
						WebElement bestMatch = wait.until(ExpectedConditions.elementToBeClickable(
								By.xpath("//div[@role='option' and normalize-space(text())='" + bestMatchText + "']")));
						bestMatch.click();
						System.out.println("Selected best match: " + bestMatchText);
						clicked = true;
					} catch (StaleElementReferenceException e) {
						System.out.println("Stale element on click attempt " + (attempts + 1) + ", retrying...");
					}
					attempts++;
				}

				if (!clicked) {
					System.out.println("Failed to click the best match after retries.");
				}

			} else {
				System.out.println("No suitable match found for input: " + location);
			}

		} catch (NoSuchElementException e) {
			System.out.println("Input or dropdown not found: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
		}
	}

	public int levenshteinDistance(String a, String b) {
		int[][] dp = new int[a.length() + 1][b.length() + 1];

		for (int i = 0; i <= a.length(); i++) {
			for (int j = 0; j <= b.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
					dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, // deletion
							dp[i][j - 1] + 1), // insertion
							dp[i - 1][j - 1] + cost // substitution
					);
				}
			}
		}
		return dp[a.length()][b.length()];
	}

	@FindBy(xpath = "(//input[contains(@class,'tg-async-select__input')])[2]")
	private WebElement entertoLocation;

	public String enterTo(String location) throws TimeoutException {
		entertoLocation.clear();
		entertoLocation.sendKeys(location);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

		selectCityto(location);

		return location; // returning the input location
	}

	public void selectCityto(String location) throws TimeoutException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Wait for dropdown container to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role='listbox']/parent::div")));

			// Wait until options are loaded
			wait.until(driver -> driver.findElements(By.xpath("//div[@role='option']")).size() > 0);

			List<WebElement> initialOptions = driver.findElements(By.xpath("(//div[@role='option'])[1]"));
			int bestScore = Integer.MAX_VALUE;
			String bestMatchText = null;

			String input = location.trim().toLowerCase();

			for (int i = 0; i < initialOptions.size(); i++) {
				try {
					WebElement option = driver.findElements(By.xpath("//div[@role='option']")).get(i);
					String suggestion = option.getText().trim().toLowerCase();
					int score = levenshteinDistance(input, suggestion);

					if (score < bestScore) {
						bestScore = score;
						bestMatchText = option.getText().trim();
					}
				} catch (StaleElementReferenceException e) {
					System.out.println("Stale element at index " + i + ", skipping.");
				}
			}

			if (bestMatchText != null) {
				// Retry clicking best match up to 3 times
				int attempts = 0;
				boolean clicked = false;
				while (attempts < 3 && !clicked) {
					try {
						WebElement bestMatch = wait.until(ExpectedConditions.elementToBeClickable(
								By.xpath("//div[@role='option' and normalize-space(text())='" + bestMatchText + "']")));
						bestMatch.click();
						System.out.println("Selected best match: " + bestMatchText);
						clicked = true;
					} catch (StaleElementReferenceException e) {
						System.out.println("Stale element on click attempt " + (attempts + 1) + ", retrying...");
					}
					attempts++;
				}

				if (!clicked) {
					System.out.println("Failed to click the best match after retries.");
				}

			} else {
				System.out.println("No suitable match found for input: " + location);
			}

		} catch (NoSuchElementException e) {
			System.out.println("Input or dropdown not found: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
		}
	}

	@FindBy(xpath = "(//input[contains(@class,'custom_datepicker_input')])[1]")
	WebElement selectjourdate;

	public String selectJourneyDate(String day, String MonthandYear) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Zoom out
		js.executeScript("document.body.style.zoom='80%'");

		// FIX: Use JavaScript click to bypass blocking element
		try {
			wait.until(ExpectedConditions.elementToBeClickable(selectjourdate));
			js.executeScript("arguments[0].click();", selectjourdate);
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", selectjourdate);
		}

		By monthYearHeader = By.xpath(
				"//div[contains(@class,'MuiGrid2-root MuiGrid2-direction-xs-row MuiGrid2-grid-lg-12 css-19gnu0v')]//span");
		wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));

		String currentMonthYear = driver.findElement(monthYearHeader).getText();

		if (currentMonthYear.equals(MonthandYear)) {
			By dayLocator = By.xpath(
					"//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and not(contains(@class, 'disabled'))]//span[@class='day' and text()='"
							+ day + "']");
			WebElement dayElement = wait.until(ExpectedConditions.presenceOfElementLocated(dayLocator));

			// Scroll to the day element
			js.executeScript("arguments[0].scrollIntoView(true);", dayElement);
			Thread.sleep(500);

			// Click using JavaScript
			js.executeScript("arguments[0].click();", dayElement);

		} else {
			while (!currentMonthYear.equals(MonthandYear)) {
				driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
				wait.until(ExpectedConditions.textToBe(monthYearHeader, MonthandYear));
				currentMonthYear = driver.findElement(monthYearHeader).getText();
			}
			By dayLocator = By.xpath(
					"//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and not(contains(@class, 'disabled'))]//span[@class='day' and text()='"
							+ day + "']");
			WebElement dayElement = wait.until(ExpectedConditions.presenceOfElementLocated(dayLocator));

			// Scroll and click using JavaScript
			js.executeScript("arguments[0].scrollIntoView(true);", dayElement);
			Thread.sleep(500);
			js.executeScript("arguments[0].click();", dayElement);
		}

		js.executeScript("document.body.style.zoom='100%'");

		String rawDate = day + " " + MonthandYear;
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

	// Method to Click on Check-Out Date
	public void clickOnReturnDate() {
		driver.findElement(By.xpath("(//input[contains(@class,'custom_datepicker_input')])[2]")).click();
	}

	// Method to Select Return Date By Passing Two Paramenters(Date and MounthYear)
	public String selectReturnDate(String returnDate, String returnMonthAndYear) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		clickOnReturnDate();

		By monthYearHeader = By.xpath(
				"//div[contains(@class,'MuiGrid2-root MuiGrid2-direction-xs-row MuiGrid2-grid-lg-12 css-19gnu0v')]//span");
		wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));

		String currentMonthYear = driver.findElement(monthYearHeader).getText();

		while (!currentMonthYear.equals(returnMonthAndYear)) {
			driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
			wait.until(ExpectedConditions.textToBe(monthYearHeader, returnMonthAndYear));
			currentMonthYear = driver.findElement(monthYearHeader).getText();
		}

		By dayLocator = By.xpath(
				"//div[@class='react-datepicker__month']//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'disabled'))]//span[@class='day' and text()='"
						+ returnDate + "']");
		WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(dayLocator));
		dayElement.click();

		String rawDate = returnDate + " " + returnMonthAndYear; // e.g., "18 August 2025"
		return normalizeDate(rawDate);
	}

	// Method to selct services
	public List<String> selectServices(String... services) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'tg-label_lg')])[2]")));

		List<String> selectedServices = new ArrayList<>();

		for (String service : services) {
			String serviceText = "//span[@class='text' and text()='" + service + "']";
			WebElement serviceOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(serviceText)));
			serviceOption.click();

			selectedServices.add(service); // Add to the list of selected services
		}

		return selectedServices;
	}

	// Method to click on add trip button
	public void clickCreateTripButton() {
		WebElement addTripButton = driver.findElement(By.xpath("//button[text()='Create  Trip']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addTripButton);
		addTripButton.click();
	}

	// get the selected services text from your trip crerated succesfully popup
	public List<String> getSelectedServicesTextFromPopup() {
		List<WebElement> serviceElements = driver.findElements(
				By.xpath("//div[contains(@class, 'triplayout_service-tab')]//div[contains(@class, 'tg-start')]"));

		List<String> serviceTexts = new ArrayList<>();
		for (WebElement element : serviceElements) {
			serviceTexts.add(element.getText().trim());
		}

		return serviceTexts;
	}

	public List<String> getSelectedServicesTextFromPopupAfterTripCreated() {
		// Wait for the success container to be visible first
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[contains(@class,'create-trip_success-conatiner')]")));

		List<WebElement> serviceElements = driver.findElements(By.xpath(
				"//div[contains(@class,'create-trip_success-conatiner')]//button[contains(@class,'selected-service')]//span[@class='text']"));

		List<String> serviceTexts = new ArrayList<>();
		for (WebElement element : serviceElements) {
			serviceTexts.add(element.getText().trim());
		}

		return serviceTexts;
	}

//Method for to clcik continue to add services btn
	public void clickOnContinueToAddServicesBtn() {
		driver.findElement(By.xpath("//button[text()='Continue to Add Services']")).click();
	}

	// Method to validae services from clciked to poup
	public void validateSelectedServicesInSelectedAndPopup(List<String> selectedServices, List<String> popupServices,
			Log log, ScreenShots screenshots) {

		if (selectedServices == null || selectedServices.isEmpty()) {
			log.ReportEvent("FAIL", "Selected services list is null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Selected services list is empty.");
			return;
		}

		if (popupServices == null || popupServices.isEmpty()) {
			log.ReportEvent("FAIL", "Services in popup are null or empty.");
			screenshots.takeScreenShot1();
			Assert.fail("Popup services list is empty.");
			return;
		}

		// Sort both lists to compare irrespective of order
		List<String> sortedSelected = new ArrayList<>(selectedServices);
		List<String> sortedPopup = new ArrayList<>(popupServices);

		Collections.sort(sortedSelected, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(sortedPopup, String.CASE_INSENSITIVE_ORDER);

		if (!sortedSelected.equals(sortedPopup)) {
			log.ReportEvent("FAIL",
					"Mismatch in selected services.\nExpected: " + selectedServices + "\nActual: " + popupServices);
			screenshots.takeScreenShot1();
			Assert.fail("Selected services and popup services do not match.");
		} else {
			log.ReportEvent("PASS", "Selected services match successfully in the popup: " + popupServices);
		}
	}

	public String[] getTripIdFromPopup(Log log) {
		String tripid = driver
				.findElement(By.xpath("//div[text()='Trip Created Successfully!']/following-sibling::div")).getText();
		System.out.println("tripid from popup : " + tripid);
		log.ReportEvent("INFO", "Tripid : " + tripid);

		return new String[] { tripid };
	}
	
	public void selectTripTypeDropdown(String value) {
	    driver.findElement(By.xpath("//span[text()='Trip Type']/following::div[contains(@class,'tg-select-box__dropdown-indicator')][1]")).click();
	    new WebDriverWait(driver, Duration.ofSeconds(5))
	        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-select-box__menu css-1nmdiq5-menu')]")));
	    driver.findElement(By.xpath("//span[text()='" + value + "']")).click();
	}

	public String[] getTripIdFromTripDetailsPage(Log log) {
		String tripid = driver.findElement(By.xpath("//span[text()='Trip ID ']/following-sibling::div")).getText();
		System.out.println("tripid from Nextpage : " + tripid);
		log.ReportEvent("INFO", "tripid :" + tripid);

		return new String[] { tripid };

	}

	// Method to get dates from
	public String[] getDatesFromTripDetailsPage() {
		String tripDates = driver.findElement(By.xpath(
				"(//div[contains(@class,' tg-typography tg-typography_subtitle-6  tg-typography_secondary-dark')])[3]"))
				.getText();
		System.out.println("tripDates from Nextpage : " + tripDates);
		return new String[] { tripDates };
	}

	// Method to get the selected services text from details page
	public List<String> getSelectedServicesTextFromDetailsPage() {
		List<WebElement> serviceElements = driver.findElements(By.xpath(
				"//div[contains(@class,'tg-typography_ triplayout_service-tab')]//div[contains(@class,'tg-start')]"));
		List<String> services = new ArrayList<>();

		for (WebElement el : serviceElements) {
			String text = el.getText().trim();
			if (!text.isEmpty()) {
				services.add(text);
			}
		}

		System.out.println("Services text from details page: " + services);
		return services;
	}

	// Method to validate selected services and displayed services from details page
	public void validateSelectedAndDetailsPageServices(List<String> selectedServices,
			List<String> selectedFromDetailsPage, Log log, ScreenShots screenshots) {

		// Check if the first list (selected from popup) is empty
		if (selectedServices == null || selectedServices.isEmpty()) {
			log.ReportEvent("FAIL", "No services were selected from the popup.");
			screenshots.takeScreenShot1();
			return;
		}

		// Check if the second list (from details page) is empty
		if (selectedFromDetailsPage == null || selectedFromDetailsPage.isEmpty()) {
			log.ReportEvent("FAIL", "No services were found on the details page.");
			screenshots.takeScreenShot1();
			return;
		}

		// Convert both lists to lowercase and sort (ignore case and order)
		List<String> sortedPopup = selectedServices.stream().map(String::toLowerCase).map(String::trim).sorted()
				.collect(Collectors.toList());

		List<String> sortedDetailsPage = selectedFromDetailsPage.stream().map(String::toLowerCase).map(String::trim)
				.sorted().collect(Collectors.toList());

		// Compare both lists
		if (sortedPopup.equals(sortedDetailsPage)) {
			log.ReportEvent("PASS", "Selected services match with details page services: " + sortedDetailsPage);
		} else {
			log.ReportEvent("FAIL", "Mismatch in selected and details page services.\n" + "Expected (Selected): "
					+ sortedPopup + "\n" + "Actual (Details Page): " + sortedDetailsPage);
			screenshots.takeScreenShot1();
		}
	}

	// Method to clcik on services to search
	public void clickOnServiceByText(String serviceName, Log log) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			// XPATH: Find service ONLY under Selected Services section
			String xpath = "//div[text()='Selected Services']/following::div[contains(@class,'triplayout_service-tab')]//div[contains(@class,'tg-start') and contains(.,'"
					+ serviceName + "')]";

			WebElement serviceElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

			// Scroll and click
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", serviceElement);
			Thread.sleep(500);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", serviceElement);

			System.out.println("✅ Clicked on service: " + serviceName + " from Selected Services");
			log.ReportEvent("PASS", "Clicked on service: " + serviceName + " from Selected Services");

		} catch (Exception e) {
			System.out.println("❌ Failed to click service '" + serviceName + "': " + e.getMessage());
			log.ReportEvent("FAIL", "Failed to click service: " + e.getMessage());
		}
	}

	@FindBy(xpath = "//label[normalize-space()='Search Location or Property']/ancestor::div[contains(@class,'app-async-select')]//input")
	private WebElement enterHotelLocation;

	public String enterLocationForHotelsOndetailsPg(String location) throws TimeoutException {
		enterHotelLocation.clear();
		enterHotelLocation.sendKeys(location);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

		selectCitytoo(location);

		return location; // return the input location
	}

	public void selectCitytoo(String location) throws TimeoutException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Wait for dropdown container to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role='option']/parent::div")));

			// Wait until options are loaded
			wait.until(driver -> driver.findElements(By.xpath("//div[@role='option']")).size() > 0);

			List<WebElement> initialOptions = driver.findElements(By.xpath("(//div[@role='option'])[1]"));
			int bestScore = Integer.MAX_VALUE;
			String bestMatchText = null;

			String input = location.trim().toLowerCase();

			for (int i = 0; i < initialOptions.size(); i++) {
				try {
					WebElement option = driver.findElements(By.xpath("//div[@role='option']")).get(i);
					String suggestion = option.getText().trim().toLowerCase();
					int score = levenshteinDistance(input, suggestion);

					if (score < bestScore) {
						bestScore = score;
						bestMatchText = option.getText().trim();
					}
				} catch (StaleElementReferenceException e) {
					System.out.println("Stale element at index " + i + ", skipping.");
				}
			}

			if (bestMatchText != null) {
				// Retry clicking best match up to 3 times
				int attempts = 0;
				boolean clicked = false;
				while (attempts < 3 && !clicked) {
					try {
						WebElement bestMatch = wait.until(ExpectedConditions.elementToBeClickable(
								By.xpath("//div[@role='option' and normalize-space(text())='" + bestMatchText + "']")));
						bestMatch.click();
						System.out.println("Selected best match: " + bestMatchText);
						clicked = true;
					} catch (StaleElementReferenceException e) {
						System.out.println("Stale element on click attempt " + (attempts + 1) + ", retrying...");
					}
					attempts++;
				}

				if (!clicked) {
					System.out.println("Failed to click the best match after retries.");
				}

			} else {
				System.out.println("No suitable match found for input: " + location);
			}

		} catch (NoSuchElementException e) {
			System.out.println("Input or dropdown not found: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
		}
	}

	// Method to clcik on add button from details page for hotels
	public void clickOnAddButton() {
		driver.findElement(By.xpath("(//button[contains(@class,'tg-icon-btn_md ')])[3]")).click();
	}

	public String[] getTripNameFromTripDetailsPageAfterAddForHotels() {
		String tripHotelname = driver.findElement(By.xpath("//div[contains(@class,'hotel-place-name')]")).getText();
		System.out.println("tripHotelName from Nextpage : " + tripHotelname);
		return new String[] { tripHotelname };
	}

	public String[] getTripCityTxtFromTripsDetailPg(Log log) {
		String TripCityText = driver.findElement(By.xpath("//div[text()='Trip City :']/following-sibling::div"))
				.getText();
		System.out.println("Trip City Text from trips detail Page: " + TripCityText);
		log.ReportEvent("INFO", "Trip City Text from trips detail Page: " + TripCityText);

		return new String[] { TripCityText };
	}

	public String[] getCheckInAndOutDateFromTripDetailsPageForHotelsAfterAdd() {
		try {
			// Get the full date string from the page
			String tripDates = driver
					.findElement(By.xpath(
							"(//div[contains(@class,'tg-typography tg-typography_subtitle-7 fw-400 label-color')])[1]"))
					.getText().trim();

			System.out.println("Dates from Next Page: " + tripDates);

			// Split using hyphen
			String[] parts = tripDates.split("-");
			if (parts.length == 2) {
				String checkIn = parts[0].trim(); // e.g., "27th Sep, 2025"
				String checkOut = parts[1].trim(); // e.g., "2nd Oct, 2025"
				return new String[] { checkIn, checkOut };
			} else {
				System.out.println("Unexpected date format: " + tripDates);
				return new String[] { "", "" };
			}
		} catch (Exception e) {
			System.out.println("Failed to get check-in/out dates: " + e.getMessage());
			return new String[] { "", "" };
		}
	}

	// method to validate location names
	public void validateLocationFromsearchToAfterClickAddForHotels(String enteredLocation,
			String[] tripDetailsLocationAfterAdd, Log log, ScreenShots screenshots) {

		if (tripDetailsLocationAfterAdd == null || tripDetailsLocationAfterAdd.length == 0) {
			log.ReportEvent("FAIL", "Trip details location is empty or null.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsLocation = tripDetailsLocationAfterAdd[0].trim().toLowerCase();
		String entered = enteredLocation.trim().toLowerCase();

		if (tripDetailsLocation.contains(entered) || entered.contains(tripDetailsLocation)) {
			log.ReportEvent("PASS", "Hotel location matches from search to after add: " + enteredLocation + " == "
					+ tripDetailsLocationAfterAdd[0]);
		} else {
			log.ReportEvent("FAIL", "Hotel location does not match between search and Add. Entered: " + enteredLocation
					+ ", Found on Trip Details page: " + tripDetailsLocationAfterAdd[0]);
			screenshots.takeScreenShot1();
		}
	}

	// Method to validate dates
	public void validateCheckInAndCheckOutDatesFromsearchToAdd(String expectedCheckIn, // from selectJourneyDate(...)
			String expectedCheckOut, // from selectReturnDate(...)
			String[] actualDatesFromTripDetails, // from getCheckInAndOutDateFromTripDetailsPageForHotelsAfterAdd()
			Log log, ScreenShots screenshots) {

		if (actualDatesFromTripDetails == null || actualDatesFromTripDetails.length < 2) {
			log.ReportEvent("FAIL", "Check-in or Check-out date not found on Trip Details page.");
			screenshots.takeScreenShot1();
			return;
		}

		String actualCheckIn = normalizeDate(actualDatesFromTripDetails[0]);
		String actualCheckOut = normalizeDate(actualDatesFromTripDetails[1]);

		if (expectedCheckIn.equalsIgnoreCase(actualCheckIn) && expectedCheckOut.equalsIgnoreCase(actualCheckOut)) {
			log.ReportEvent("PASS", "Check-in and Check-out dates match from search to add. \n" + "Check-in: "
					+ actualCheckIn + " | Check-out: " + actualCheckOut);
		} else {
			log.ReportEvent("FAIL",
					"Date mismatch between search To Add! \nExpected Check-in: " + expectedCheckIn + ", Actual: "
							+ actualCheckIn + "\nExpected Check-out: " + expectedCheckOut + ", Actual: "
							+ actualCheckOut);
			screenshots.takeScreenShot1();
		}
	}

	// Method to clcik on search hotel
	public void clickOnSearchHotelBut() {
		try {
			// Click the "Search Hotel" button
			driver.findElement(By.xpath("//span[text()='Search Hotel']")).click();

			// Wait for the expected result section to appear
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='hcard ']")));

		} catch (Exception e) {
			String errorMessage;
			try {
				WebElement body = driver.findElement(By.tagName("body"));
				errorMessage = body.getText();
			} catch (Exception ex) {
				errorMessage = "Could not retrieve page text.";
			}

			Assert.fail("Expected search results were not displayed. Page message: \n" + errorMessage);
		}
	}

	public String[] getLocNAmeFromTripHotelResultsPg() {
		String TRipLocNm = driver.findElement(By.xpath(
				"(//div[contains(@id,'hotel-search-header-id')]//div[@class=' tg-typography tg-typography_subtitle-6 ms-1 fw-600 tg-typography_default'])[1]"))
				.getText();
		System.out.println("location name from results page: " + TRipLocNm);
		return new String[] { TRipLocNm };
	}

	// Method to validate loction name from search to result pg
	public void validateHotelLocationFromResultsAndAfterAddInDetailsPage(String[] locationFromResultsPage,
			String[] hotelNameFromTripDetailsPage, Log log, ScreenShots screenshots) {

		if (locationFromResultsPage == null || locationFromResultsPage.length == 0
				|| hotelNameFromTripDetailsPage == null || hotelNameFromTripDetailsPage.length == 0) {

			log.ReportEvent("FAIL", "One or both location values are missing from the pages.");
			screenshots.takeScreenShot1();
			return;
		}

		String resultPageLocation = normalizeLocation(locationFromResultsPage[0]);
		String detailsPageHotelName = normalizeLocation(hotelNameFromTripDetailsPage[0]);

		if (resultPageLocation.equalsIgnoreCase(detailsPageHotelName)) {
			log.ReportEvent("PASS",
					"Hotel location matches between results and details pages \nLocation: " + resultPageLocation);
		} else {
			log.ReportEvent("FAIL",
					"Hotel location does not match between results and After add Loc name in details page  \n"
							+ "Results Page: " + resultPageLocation + "\n" + "Trip Details Page: "
							+ detailsPageHotelName);
			screenshots.takeScreenShot1();
		}
	}

	public void validateCheckInAndOutDatesFromResultsAndDetailsAfterAdd(String[] checkInPartsFromResults,
			String[] checkOutPartsFromResults, String[] checkInOutFromDetailsPage, Log log, ScreenShots screenshots) {

		if (checkInPartsFromResults == null || checkOutPartsFromResults == null || checkInOutFromDetailsPage == null) {
			log.ReportEvent("FAIL", "One or more date values are null.");
			screenshots.takeScreenShot1();
			return;
		}

		if (checkInPartsFromResults.length < 3 || checkOutPartsFromResults.length < 3
				|| checkInOutFromDetailsPage.length < 2) {
			log.ReportEvent("FAIL", "Incomplete date format detected in inputs.");
			screenshots.takeScreenShot1();
			return;
		}

		String expectedCheckInRaw = checkInPartsFromResults[0] + " " + checkInPartsFromResults[1].replace(",", "") + " "
				+ checkInPartsFromResults[2];
		String expectedCheckOutRaw = checkOutPartsFromResults[0] + " " + checkOutPartsFromResults[1].replace(",", "")
				+ " " + checkOutPartsFromResults[2];

		String actualCheckIn = checkInOutFromDetailsPage[0].replaceAll("(?<=\\d)(st|nd|rd|th)", "").replace(",", "")
				.trim();
		String actualCheckOut = checkInOutFromDetailsPage[1].replaceAll("(?<=\\d)(st|nd|rd|th)", "").replace(",", "")
				.trim();

		// Normalize both expected and actual
		String expectedCheckIn = normalizeDate(expectedCheckInRaw);
		String expectedCheckOut = normalizeDate(expectedCheckOutRaw);
		String normalizedCheckIn = normalizeDate(actualCheckIn);
		String normalizedCheckOut = normalizeDate(actualCheckOut);

		System.out.println("🔍 Expected Check-In: " + expectedCheckIn);
		System.out.println("🔍 Expected Check-Out: " + expectedCheckOut);
		System.out.println("📄 Actual Check-In from Details Page: " + normalizedCheckIn);
		System.out.println("📄 Actual Check-Out from Details Page: " + normalizedCheckOut);

		if (expectedCheckIn.equalsIgnoreCase(normalizedCheckIn)
				&& expectedCheckOut.equalsIgnoreCase(normalizedCheckOut)) {
			log.ReportEvent("PASS", " Check-in and Check-out dates match from results pg and after add.\nCheck-in: "
					+ normalizedCheckIn + "\nCheck-out: " + normalizedCheckOut);
		} else {
			log.ReportEvent("FAIL", " Date mismatch from results and after add.\nExpected: " + expectedCheckIn + " - "
					+ expectedCheckOut + "\nActual: " + normalizedCheckIn + " - " + normalizedCheckOut);
			screenshots.takeScreenShot1();
		}
	}

	// Method to clik on add hotel to trips
	public void clickOnAddHotelToTrip(Log log) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			By addButtonLocator = By.xpath("//button[text()='Add Hotel to Trip']");
			WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addButton);
			Thread.sleep(500); // Optional: replace with wait if needed
			addButton.click();
			log.ReportEvent("INFO", "Clicked on 'Add Hotel to Trip' button.");

			// Short wait to check for toast message after clicking
			WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			try {
				WebElement toastMessage = toastWait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//div[@class='toast error']//p[@class='toast-title']")));
				String errorText = toastMessage.getText();
				log.ReportEvent("FAIL", "Toast Error Message displayed: " + errorText);
				System.out.println("Error Message from Toast: " + errorText);
				Assert.fail("Error Message displayed: " + errorText);
			} catch (Exception toastNotFound) {
				// No toast message found — continue normally
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Failed to click 'Add Hotel to Trip' button: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Method to click on down button in hotels desclaimerpage after add hotel to
	// trip
	public void clcikOnDownButtonInHotelsDesclaimer() {
		driver.findElement(By.xpath("//div[@class='tg-triprequest-hb-view-unview-hotel-details']")).click();

	}

	// Method to get the hotel details data from tripn details page
	public String[] getHotelNameTextFromTripDetailsPage() {
		String Hotelname = driver.findElement(By.xpath("//div[contains(@class,'tg-triprequest-hb-selected-hotel')]"))
				.getText();
		System.out.println("Hotelname from trip details page : " + Hotelname);
		return new String[] { Hotelname };
	}

	public String[] getHotelAddressTextFromTripDetailsPage() {
		String HotelAddress = driver.findElement(By.xpath("//div[contains(@class,'tg-triprequest-hb-hotel-address')]"))
				.getText();
		System.out.println("HotelAddress from trip details page : " + HotelAddress);
		return new String[] { HotelAddress };
	}

	public String[] getHotelPolicyTextFromTripDetailsPage() {
		String HotelPolicy = driver.findElement(By.xpath("//div[contains(@class,'tg-policy')]")).getText();
		System.out.println("HotelPolicy from trip details page : " + HotelPolicy);
		return new String[] { HotelPolicy };
	}

	public String[] gethotelRoomTextFromTripDetailsPage() {
		String roomText = driver.findElement(By.xpath("//div[contains(@class,'tg-triprequest-hb-room-details')]"))
				.getText();
		System.out.println("roomText from trip details page : " + roomText);
		return new String[] { roomText };
	}

	public String[] getHotelMealsTextFromTripDetailsPage() {
		String mealsText = driver.findElement(By.xpath("//div[contains(@class,'tg-triprequest-hb-meal-details')]"))
				.getText();
		System.out.println("mealsText from trip details page : " + mealsText);
		return new String[] { mealsText };
	}

	public String[] getHotelCheckInafterTextFromTripDetailsPage() {
		String CheckIn = driver.findElement(By.xpath("//div[text()='Check In after']//span")).getText();
		System.out.println("CheckIn from trip details page : " + CheckIn);
		return new String[] { CheckIn };
	}

	public String[] getHotelCheckOutBeforeTextFromTripDetailsPage() {
		String CheckOut = driver.findElement(By.xpath("//div[text()='Check Out Before']//span")).getText();
		System.out.println("CheckOut from trip details page : " + CheckOut);
		return new String[] { CheckOut };
	}

	public String[] getHotelDaysStayTextFromTripDetailsPage() {
		String daysStay = driver.findElement(By.xpath("//div[contains(@class,' days-stay')]")).getText();
		System.out.println("daysStay from trip details page : " + daysStay);
		return new String[] { daysStay };
	}

	// validation methods for hotels for hotel booking page to trip details page

	public void validateHotelNameFromTripDetailsToHotelsBookingPage(String[] hotelNameFromTripDetailsPage,
			String[] hotelNameFromBookingPage, Log log, ScreenShots screenshots) {

		if (hotelNameFromTripDetailsPage == null || hotelNameFromTripDetailsPage.length == 0
				|| hotelNameFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel name from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (hotelNameFromBookingPage == null || hotelNameFromBookingPage.length == 0
				|| hotelNameFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel name from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsHotelName = hotelNameFromTripDetailsPage[0].trim();
		String bookingHotelName = hotelNameFromBookingPage[0].trim();

		if (!tripDetailsHotelName.equalsIgnoreCase(bookingHotelName)) {
			log.ReportEvent("FAIL", "Hotel name mismatch from trip details to hotel booking page! Trip Details Page: '"
					+ tripDetailsHotelName + "', Booking Page: '" + bookingHotelName + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Hotel name mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS",
					"Hotel name matches on both Trip Details and hotel Booking pages: " + tripDetailsHotelName);
		}
	}

	public void validateHotelAddressFromTripDetailsToHotelBookingPage(String[] hotelAddressFromTripDetailsPage,
			String[] hotelAddressFromBookingPage, Log log, ScreenShots screenshots) {

		if (hotelAddressFromTripDetailsPage == null || hotelAddressFromTripDetailsPage.length == 0
				|| hotelAddressFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel address from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (hotelAddressFromBookingPage == null || hotelAddressFromBookingPage.length == 0
				|| hotelAddressFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel address from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsHotelAddress = hotelAddressFromTripDetailsPage[0].trim();
		String bookingHotelAddress = hotelAddressFromBookingPage[0].trim();

		if (!tripDetailsHotelAddress.equalsIgnoreCase(bookingHotelAddress)) {
			log.ReportEvent("FAIL",
					"Hotel address mismatch from both Trip Details and Booking pages! Trip Details Page: '"
							+ tripDetailsHotelAddress + "', Booking Page: '" + bookingHotelAddress + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Hotel address mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS",
					"Hotel address matches on both Trip Details and Booking pages: " + tripDetailsHotelAddress);
		}
	}

	public void validateHotelPolicyFromTripDetailsToBookingPage(String[] hotelPolicyFromTripDetailsPage,
			String[] hotelPolicyFromBookingPage, Log log, ScreenShots screenshots) {

		if (hotelPolicyFromTripDetailsPage == null || hotelPolicyFromTripDetailsPage.length == 0
				|| hotelPolicyFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel policy from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (hotelPolicyFromBookingPage == null || hotelPolicyFromBookingPage.length == 0
				|| hotelPolicyFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel policy from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsPolicy = hotelPolicyFromTripDetailsPage[0].trim();
		String bookingPagePolicy = hotelPolicyFromBookingPage[0].trim();

		if (!tripDetailsPolicy.equalsIgnoreCase(bookingPagePolicy)) {
			log.ReportEvent("FAIL", "Hotel policy mismatch between Trip Details and Booking pages! Trip Details Page: '"
					+ tripDetailsPolicy + "', Booking Page: '" + bookingPagePolicy + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Hotel policy mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS",
					"Hotel policy matches on both Trip Details and Booking pages: " + tripDetailsPolicy);
		}
	}

	public void validateSelectedRoomFromTripDetailsToBookingPage(String[] roomTextFromTripDetailsPage,
			String[] selectedRoomTextFromBookingPage, Log log, ScreenShots screenshots) {

		if (roomTextFromTripDetailsPage == null || roomTextFromTripDetailsPage.length == 0
				|| roomTextFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Room text from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (selectedRoomTextFromBookingPage == null || selectedRoomTextFromBookingPage.length == 0
				|| selectedRoomTextFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Selected room text from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsRoomText = roomTextFromTripDetailsPage[0].trim();
		String bookingPageRoomText = selectedRoomTextFromBookingPage[0].trim();

		if (!tripDetailsRoomText.equalsIgnoreCase(bookingPageRoomText)) {
			log.ReportEvent("FAIL", "Room name mismatch between Trip Details and Booking pages! Trip Details Page: '"
					+ tripDetailsRoomText + "', Booking Page: '" + bookingPageRoomText + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Room name mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS", "Room name matches on both Trip Details and Booking pages: " + tripDetailsRoomText);
		}
	}

	public void validateHotelMealsFromTripDetailsToBookingPage(String[] mealsTextFromTripDetailsPage,
			String[] mealsTextFromBookingPage, Log log, ScreenShots screenshots) {

		if (mealsTextFromTripDetailsPage == null || mealsTextFromTripDetailsPage.length == 0
				|| mealsTextFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Meals text from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (mealsTextFromBookingPage == null || mealsTextFromBookingPage.length == 0
				|| mealsTextFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Meals text from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsMeals = mealsTextFromTripDetailsPage[0].trim().toLowerCase();
		String bookingPageMeals = mealsTextFromBookingPage[0].trim().toLowerCase();

		if (!tripDetailsMeals.contains(bookingPageMeals) && !bookingPageMeals.contains(tripDetailsMeals)) {
			log.ReportEvent("FAIL", "Meals mismatch between trip details and booking pages! Trip Details Page: '"
					+ tripDetailsMeals + "', Booking Page: '" + bookingPageMeals + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Meal plan mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS", "Meals match on both Trip Details and Booking pages: '" + bookingPageMeals + "'");
		}
	}

	// Method to validate check in d=time
	public void validateHotelCheckInTimeFromTripDetailsToBookingPage(String[] checkInTimeFromTripDetailsPage,
			String[] checkInTimeFromBookingPage, Log log, ScreenShots screenshots) {

		if (checkInTimeFromTripDetailsPage == null || checkInTimeFromTripDetailsPage.length == 0
				|| checkInTimeFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "hotel Check-In time from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (checkInTimeFromBookingPage == null || checkInTimeFromBookingPage.length == 0
				|| checkInTimeFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "hotel Check-In time from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsCheckIn = checkInTimeFromTripDetailsPage[0].trim();
		String bookingPageCheckIn = checkInTimeFromBookingPage[0].trim();

		if (!tripDetailsCheckIn.equalsIgnoreCase(bookingPageCheckIn)) {
			log.ReportEvent("FAIL",
					"hotel Check-In time mismatch between Trip Details and Booking pages! Trip Details: '"
							+ tripDetailsCheckIn + "', Booking Page: '" + bookingPageCheckIn + "'");
			screenshots.takeScreenShot1();
			Assert.fail("hotel Check-In time mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent(" PASS",
					"hotel Check-In time matches between Trip Details and Booking pages: " + tripDetailsCheckIn);
		}
	}

	// Method to validate hotel check out time

	public void validateHotelCheckOutTimeFromTripDetailsToBookingPage(String[] checkOutTimeFromTripDetailsPage,
			String[] checkOutTimeFromBookingPage, Log log, ScreenShots screenshots) {

		if (checkOutTimeFromTripDetailsPage == null || checkOutTimeFromTripDetailsPage.length == 0
				|| checkOutTimeFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "hotel Check-Out time from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (checkOutTimeFromBookingPage == null || checkOutTimeFromBookingPage.length == 0
				|| checkOutTimeFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "hotel Check-Out time from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String tripDetailsCheckOut = checkOutTimeFromTripDetailsPage[0].trim();
		String bookingPageCheckOut = checkOutTimeFromBookingPage[0].trim();

		if (!tripDetailsCheckOut.equalsIgnoreCase(bookingPageCheckOut)) {
			log.ReportEvent("FAIL", "hotel Check-Out time mismatch from trip details to booking page! Trip Details: '"
					+ tripDetailsCheckOut + "', Booking Page: '" + bookingPageCheckOut + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Hotel Check-Out time mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS",
					"hotel Check-Out time matches on both trip details and booking pages: " + tripDetailsCheckOut);
		}
	}

	public void validateDaysStayFromTripDetailsToBookingPage(String[] daysStayFromTripDetailsPage,
			String[] nightsStayFromBookingPage, Log log, ScreenShots screenshots) {

		if (daysStayFromTripDetailsPage == null || daysStayFromTripDetailsPage.length == 0
				|| daysStayFromTripDetailsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Days stay from Trip Details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (nightsStayFromBookingPage == null || nightsStayFromBookingPage.length == 0
				|| nightsStayFromBookingPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Nights stay from Booking page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		// Get only the numbers
		String daysNumber = daysStayFromTripDetailsPage[0].replaceAll("[^0-9]", "");
		String nightsNumber = nightsStayFromBookingPage[0].replaceAll("[^0-9]", "");

		if (!daysNumber.equals(nightsNumber)) {
			log.ReportEvent("FAIL",
					"Stay duration mismatch! Trip Details: '" + daysNumber + "', Booking Page: '" + nightsNumber + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Stay duration mismatch between Trip Details and Booking pages.");
		} else {
			log.ReportEvent("PASS", "Stay duration matches on both pages: " + daysNumber + " nights/days");
		}
	}

	@FindBy(xpath = "//div[text()='From']/following-sibling::div//input")
	private WebElement enterBusLocation;

	public String enterLocationForBusesOndetailsPg(String location) throws TimeoutException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.elementToBeClickable(enterBusLocation)).click();
		enterBusLocation.clear();
		enterBusLocation.sendKeys(location);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

		selectCitytoo(location);

		return location;
	}

	@FindBy(xpath = "//div[text()='To']/following-sibling::div//input")
	private WebElement enterBusToLocation;

	public String enterToLocationForBusesOndetailsPg(String location) throws TimeoutException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.elementToBeClickable(enterBusToLocation)).click();
		enterBusToLocation.clear();
		enterBusToLocation.sendKeys(location);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

		selectCitytoo(location);

		return location;
	}

	// Method to select journey date
	@FindBy(xpath = "(//input[contains(@class,'custom_datepicker_input')])[1]")
	WebElement selectBusjourdates;

	public String selectBusesJourneyDate(String day, String MonthandYear) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		js.executeScript("document.body.style.zoom='80%'");
		wait.until(ExpectedConditions.elementToBeClickable(selectBusjourdates)).click();

		By monthYearHeader = By.xpath(
				"//div[contains(@class,'MuiGrid2-root MuiGrid2-direction-xs-row MuiGrid2-grid-lg-12 css-19gnu0v')]//span");
		wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearHeader));

		String currentMonthYear = driver.findElement(monthYearHeader).getText();

		if (currentMonthYear.equals(MonthandYear)) {
			By dayLocator = By.xpath("//div[@class='react-datepicker__month-container']//span[text()='" + day + "']");
			wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
		} else {
			while (!currentMonthYear.equals(MonthandYear)) {
				driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
				wait.until(ExpectedConditions.textToBe(monthYearHeader, MonthandYear));
				currentMonthYear = driver.findElement(monthYearHeader).getText();
			}
			By dayLocator = By.xpath("//div[@class='react-datepicker__month-container']//span[text()='" + day + "']");
			wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
		}

		js.executeScript("document.body.style.zoom='100%'");

		String rawDate = day + " " + MonthandYear; // e.g., "13 August 2025"
		return normalizeDate(rawDate);
	}

	public String[] getTripBusFromLocFromTripDetailsPageAfterAdd() {
		String fullLocation = driver.findElement(By.xpath("//div[contains(@class,'hotel-place-name')]")).getText();
		String[] parts = fullLocation.split(" - ");
		return parts;
	}

	public String[] getTripBusDatesFromTripDetailsPageAfterAdd() {
		String busDate = driver.findElement(By.xpath("//div[contains(@class,'tg-triprequest-bb-bus-booking-date')]"))
				.getText();
		System.out.println("Bus date from details page : " + busDate);
		return new String[] { busDate };
	}

	public void clcikOnSearchBus() {
		driver.findElement(By.xpath("//span[text()='Search Bus']")).click();
	}

	public void validateBusFromLocationOnDetailsPage(String enteredLocation, String[] tripLocationsAfterAdd, Log log,
			ScreenShots screenshots) {

		if (enteredLocation == null || enteredLocation.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Entered location is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}
		if (tripLocationsAfterAdd == null || tripLocationsAfterAdd.length < 1
				|| tripLocationsAfterAdd[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Trip 'From' location from summary is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}
		String fromLocationDisplayed = tripLocationsAfterAdd[0].trim();
		String expectedLocation = enteredLocation.trim();
		if (!fromLocationDisplayed.equalsIgnoreCase(expectedLocation)) {
			log.ReportEvent("FAIL", "Mismatch in 'From' location from Search To Add. Entered: '" + expectedLocation
					+ "', Displayed: '" + fromLocationDisplayed + "'");
			screenshots.takeScreenShot1();
			Assert.fail();
		} else {
			log.ReportEvent("PASS", "Entered 'From' location matches from search to add : '" + expectedLocation + "'");
		}
	}

	public void validateBusToLocationOnDetailsPage(String enteredToLocation, String[] tripLocationsAfterAdd, Log log,
			ScreenShots screenshots) {

		if (enteredToLocation == null || enteredToLocation.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Entered To location is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}
		if (tripLocationsAfterAdd == null || tripLocationsAfterAdd.length < 2
				|| tripLocationsAfterAdd[1].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Trip 'To' location from summary is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}
		String ToLocationDisplayed = tripLocationsAfterAdd[1].trim();
		String expectedLocation = enteredToLocation.trim();
		if (!ToLocationDisplayed.equalsIgnoreCase(expectedLocation)) {
			log.ReportEvent("FAIL", "Mismatch in 'To' location from Search To Add. Entered: '" + expectedLocation
					+ "', Displayed: '" + ToLocationDisplayed + "'");
			screenshots.takeScreenShot1();
			Assert.fail();
		} else {
			log.ReportEvent("PASS", "Entered 'To' location matches from search to add : '" + expectedLocation + "'");
		}
	}

	public void validateBusJourneyDateFromDetailsPageToAdd(String selectedDate, String[] displayedDateAfterAdd, Log log,
			ScreenShots screenshots) {
		if (selectedDate == null || selectedDate.trim().isEmpty()) {
			log.ReportEvent("FAIL", "Selected journey date is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (displayedDateAfterAdd == null || displayedDateAfterAdd.length == 0
				|| displayedDateAfterAdd[0].trim().isEmpty()) {
			log.ReportEvent("FAIL", "Displayed journey date from trip details page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		// Convert displayed date to match selected date format
		String normalizedDisplayed = convertToSelectedFormat(displayedDateAfterAdd[0].trim());
		String selected = selectedDate.trim();

		if (!selected.equalsIgnoreCase(normalizedDisplayed)) {
			log.ReportEvent("FAIL", "Mismatch in journey date from search To Add. Selected: '" + selected
					+ "', Displayed: '" + displayedDateAfterAdd[0] + "'");
			screenshots.takeScreenShot1();
			Assert.fail();
		} else {
			log.ReportEvent("PASS", "Journey date matches from search To add: '" + selected + "'");
		}
	}

	private String convertToSelectedFormat(String displayedDate) {
		try {
			// Remove ordinal indicators (st, nd, rd, th) and comma
			String cleaned = displayedDate.replaceAll("(\\d+)(st|nd|rd|th)", "$1").replace(",", "").trim();

			// Parse the displayed format (1 Oct 2025)
			SimpleDateFormat inputFormat = new SimpleDateFormat("d MMM yyyy");
			Date date = inputFormat.parse(cleaned);

			// Format to match selected format (01-Oct-2025)
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
			return outputFormat.format(date);
		} catch (ParseException e) {
			return displayedDate; // Return original if parsing fails
		}
	}

	// Method to clcik on awaiting approval
	public void clickOnAwaitingApproval() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));

		// Click on "Awaiting Approval" tab/button
		WebElement awaitingTab = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Awaiting Approval']")));
		awaitingTab.click();

		// Wait until the "Awaiting Approval" heading or section is visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Awaiting Approval']")));
	}

	public void waitUntilDivDisplayed(WebDriver driver) {
		By locator = By.xpath("//div[@class='mb-16']");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void selectStatusFromDropdownUnderAwaitingscreen(String value) {

	    // click dropdown (2nd one)
	    driver.findElement(By.xpath("(//div[contains(@class,'tg-select-box__dropdown-indicator')])[2]")).click();

	    // wait for list to appear
	    new WebDriverWait(driver, Duration.ofSeconds(5))
	        .until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//div[contains(@class,'tg-select-box__menu-list')]")));

	    // click dynamic option
	    driver.findElement(By.xpath("//span[text()='" + value + "']")).click();
	}
	// Method to enetr data in search in awaiting apporoval page

	public void clickOnsearchTripsInAwaitingApprovalPg(String[] searchText, Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		try {
			// Wait for search field
			WebElement searchField = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));

			// Convert array to single string
			String searchString = String.join("", searchText); // or String.join(" ", searchText) if you want spaces

			// Clear and enter text
			searchField.clear();
			searchField.sendKeys(searchString);
			Thread.sleep(3000);

			log.ReportEvent("INFO", "Entered search text in Awaiting Approval page: " + searchString);
			System.out.println("Entered search text in Awaiting Approval page: " + searchString);

		} catch (Exception e) {
			String searchString = String.join("", searchText);
			log.ReportEvent("FAIL", "Failed to enter search text in Awaiting Approval page: " + searchString);
			screenshots.takeScreenShot1();
			Assert.fail("Failed to enter search text in Awaiting Approval page: " + searchString);
		}
	}

	public void clickOnsearchForCreateTripsInAwaitingApprovalPg(String searchText, Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		try {
			// Wait for search field
			WebElement searchField = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search Trips']")));

			// Clear and enter text
			searchField.clear();
			searchField.sendKeys(searchText);
			Thread.sleep(3000);

			// log.ReportEvent("INFO", "Entered search text in Awaiting Approval page: " +
			// searchText);
			System.out.println("Entered search text in Awaiting Approval page: " + searchText);

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Failed to enter search text in Awaiting Approval page: " + searchText);
			screenshots.takeScreenShot1();
			Assert.fail("Failed to enter search text in Awaiting Approval page: " + searchText);
		}
	}

	// Method to get the ststaus

	public String[] getStatusInAwaitingApprovalForBuses(Log log) {
		String status = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-status')]")).getText();
		System.out.println("status from Awaiting Approval Page: " + status);
		log.ReportEvent("INFO", "status from Awaiting Approval Screen." + status);

		return new String[] { status };
	}

	public String[] getStatusInAwaitingApprovalForBusesInPendingStatys(Log log) {
		String status = driver.findElement(By.xpath("(//div[contains(@class,'tg-label')])[2]")).getText();
		System.out.println("status from Awaiting Approval Page: " + status);
		log.ReportEvent("INFO", "status from Awaiting Approval Screen." + status);

		return new String[] { status };
	}

	public String[] getStatusInAwaitingApprovalForHotels(Log log) {
		String status = driver.findElement(By.xpath("//div[contains(@class,'tg-label')]")).getText();
		System.out.println("status from Awaiting Approval Page: " + status);
		log.ReportEvent("INFO", "status from Awaiting Approval Screen for hotels." + status);

		return new String[] { status };
	}

	public String[] getStatusInSecondApproverForHotels(Log log) {
		String status = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-status')]")).getText();
		System.out.println("status from Awaiting Approval Page: " + status);
		log.ReportEvent("INFO", "status from second Approver  Screen for hotels." + status);

		return new String[] { status };
	}

	public void clickOnApprovalDetailsForCreateTrip() {
		driver.findElement(By.xpath("//div[text()='Approval Details']")).click();
	}

	public String[] getApproverRemarksInTripsPage(Log log) {
		String remarks = driver.findElement(By.xpath("(//div[contains(text(), 'Remarks :')]//div)[1]")).getText();
		System.out.println("Approver remarks from trips Page: " + remarks);
		// log.ReportEvent("INFO", "status from Awaiting Approval Screen for hotels."+
		// status);

		return new String[] { remarks };
	}

	public String[] getApproverEmailsFromtravellerScreen(Log log) {
		// Locate all email cells in the table
		List<WebElement> emailElements = driver
				.findElements(By.xpath("//table[contains(@class,'MuiTable-root')]//tbody//tr//td[3]"));

		// If no emails found
		if (emailElements.isEmpty()) {
			log.ReportEvent("INFO", "No approver emails found in the approval table.");
			return new String[0]; // empty array
		}

		// Extract text from all email cells
		String[] emails = new String[emailElements.size()];
		for (int i = 0; i < emailElements.size(); i++) {
			emails[i] = emailElements.get(i).getText().trim();
		}

		log.ReportEvent("INFO", emails.length + " approver emails found: " + String.join(", ", emails));
		return emails;
	}

	public String[] getSecondApproverRemarksInTripsPage(Log log) {
		String remarks = driver.findElement(By.xpath("(//div[contains(text(), 'Remarks :')]//div)[2]")).getText();
		System.out.println("Approver remarks from trips Page: " + remarks);
		// log.ReportEvent("INFO", "status from Awaiting Approval Screen for hotels."+
		// status);

		return new String[] { remarks };
	}

	// Method to validate status from traveller to approver
	public void validateStatusFromTravellerToApprover(String[] travellerStatus, String[] approverStatus, Log log,
			ScreenShots screenshots) {
		if (approverStatus == null || approverStatus.length == 0) {
			log.ReportEvent("FAIL", "Status from Approver Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Status from Approver Page is missing.");
			return;
		}
		if (travellerStatus == null || travellerStatus.length == 0) {
			log.ReportEvent("FAIL", "Status from View Trip Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Status from View Trip Page is missing.");
			return;
		}

		// Trim values
		String statusApprover = approverStatus[0].trim();
		String statusTraveller = travellerStatus[0].trim();

		// Compare
		if (!statusApprover.equalsIgnoreCase(statusTraveller)) {
			log.ReportEvent("FAIL", "Status mismatch! Travellers Page: '" + statusTraveller + "' , Approver Page: '"
					+ statusApprover + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Status mismatch between Travellers and Approver pages.");
		} else {
			log.ReportEvent("PASS", "Status matches from Travellers to Approver page: " + statusApprover);
		}
	}

	public void validateStatusFromApproverToSecondApprover(String[] approverStatus, String[] secondapproverStatus,
			Log log, ScreenShots screenshots) {
		if (approverStatus == null || approverStatus.length == 0) {
			log.ReportEvent("FAIL", "Status from Approver Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Status from Approver Page is missing.");
			return;
		}
		if (secondapproverStatus == null || secondapproverStatus.length == 0) {
			log.ReportEvent("FAIL", "Status from View Trip Page is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("Status from secondapprover is missing.");
			return;
		}

		// Trim values
		String statusApprover = approverStatus[0].trim();
		String statusSecondApprover = secondapproverStatus[0].trim();

		// Compare
		if (!statusApprover.equalsIgnoreCase(statusSecondApprover)) {
			log.ReportEvent("FAIL", "Status mismatch! Approver Page: '" + statusApprover + "' , second Approver Page: '"
					+ statusSecondApprover + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Status mismatch between Approver and second Approver pages.");
		} else {
			log.ReportEvent("PASS", "Status matches from Approver to second Approver page: " + statusSecondApprover);
		}
	}

	// Method to open trip
//	public void clickArrowToOpenTrip() {
//		driver.findElement(By.xpath("//button[contains(@class,'tg-icon-btn_small')]")).click();
//	}
//	
	public void clickArrowToOpenTrip() {
	    try {
	        WebElement arrow = driver.findElement(By.xpath("//button[contains(@class,'tg-icon-btn_small')]"));

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].click();", arrow);

	        System.out.println("Arrow clicked successfully (JS).");
	        
	    } catch (Exception e) {
	        System.out.println("Ignoring minor click error as page is loading: " + e.getMessage());
	    }
	}

	// Method to clcik on summary in awaiting page '
	public void clcikOnApprovalDetailsonAwaitingPageForcraeteTrips() {
		driver.findElement(By.xpath("//div[text()='Approval Details']")).click();
	}

	// Method to validate remarks
	public void validateRemarksFromFirstApproverToTravellerpg(String[] approverRemarks, String[] travellerPgRemarks,

			Log log, ScreenShots screenshots) {

		if (travellerPgRemarks == null || travellerPgRemarks.length == 0 || travellerPgRemarks[0].isEmpty()) {
			log.ReportEvent("FAIL", "Traveller page remarks are null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (approverRemarks == null || approverRemarks.length == 0 || approverRemarks[0].isEmpty()) {
			log.ReportEvent("FAIL", "Approver remarks on Trips Page are null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String traveller = travellerPgRemarks[0].trim().toLowerCase();
		String approver = approverRemarks[0].trim().toLowerCase();

		// Compare remarks (allowing partial match either way)
		if (approver.equals(traveller) || approver.contains(traveller) || traveller.contains(approver)) {
			log.ReportEvent("PASS", "Remarks match between approver and traveller Trips Page.\n" + "Approver: '"
					+ approver + "'\nTraveller Page: '" + traveller + "'");
		} else {
			log.ReportEvent("FAIL",
					"Remarks mismatch!\nTraveller page: '" + traveller + "'\nApprover: '" + approver + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Remarks mismatch between approver and traveller Trips Page.");
		}
	}

	// Method to validate remarks
	public void validateRemarksFromFirstApproverToSecondApprover(String[] approverRemarks,
			String[] secondapproverRemarks,

			Log log, ScreenShots screenshots) {

		if (secondapproverRemarks == null || secondapproverRemarks.length == 0 || secondapproverRemarks[0].isEmpty()) {
			log.ReportEvent("FAIL", "Traveller page remarks are null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (approverRemarks == null || approverRemarks.length == 0 || approverRemarks[0].isEmpty()) {
			log.ReportEvent("FAIL", "Approver remarks on Trips Page are null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String secondApprover = secondapproverRemarks[0].trim().toLowerCase();
		String approver = approverRemarks[0].trim().toLowerCase();

		// Compare remarks (allowing partial match either way)
		if (approver.equals(secondApprover) || approver.contains(secondApprover) || secondApprover.contains(approver)) {
			log.ReportEvent("PASS", "Remarks match between approver and traveller Trips Page.\n" + "Approver: '"
					+ approver + "'\nSecond approver Page: '" + secondApprover + "'");
		} else {
			log.ReportEvent("FAIL",
					"Remarks mismatch!\nsecond approver page: '" + secondApprover + "'\nApprover: '" + approver + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Remarks mismatch between approver and second approver Trips Page.");
		}
	}

	// Method to validate remarks
	public void validateRemarksFromSecondApproverToTravellerpg(String[] secondapproverRemarks,
			String[] travellerPgRemarks,

			Log log, ScreenShots screenshots) {

		if (travellerPgRemarks == null || travellerPgRemarks.length == 0 || travellerPgRemarks[0].isEmpty()) {
			log.ReportEvent("FAIL", "Traveller page remarks are null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (secondapproverRemarks == null || secondapproverRemarks.length == 0 || secondapproverRemarks[0].isEmpty()) {
			log.ReportEvent("FAIL", "Second Approver remarks on Trips Page are null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String traveller = travellerPgRemarks[0].trim().toLowerCase();
		String approver = secondapproverRemarks[0].trim().toLowerCase();

		// Compare remarks (allowing partial match either way)
		if (approver.equals(traveller) || approver.contains(traveller) || traveller.contains(approver)) {
			log.ReportEvent("PASS", "Remarks match between second approver and traveller Trips Page.\n"
					+ "second Approver: '" + approver + "'\nTraveller Page: '" + traveller + "'");
		} else {
			log.ReportEvent("FAIL",
					"Remarks mismatch!\nTraveller page: '" + traveller + "'\nsecond Approver: '" + approver + "'");
			screenshots.takeScreenShot1();
			Assert.fail("Remarks mismatch between second approver and traveller Trips Page.");
		}
	}

	// Method to click on past data booking
	public void clcikOnPastDatedBooking() {
		driver.findElement(By.xpath("(//span[@class='tg-checkbox-box primary'])[1]")).click();
	}

}
