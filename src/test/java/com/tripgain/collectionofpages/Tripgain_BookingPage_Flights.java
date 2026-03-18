
package com.tripgain.collectionofpages;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
///import com.tripgain.common.TestExecutionNotifier;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class Tripgain_BookingPage_Flights {
	WebDriver driver;

	public Tripgain_BookingPage_Flights(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	private static AtomicInteger backEndIssueCount = new AtomicInteger(0);

	public void validateBookingScreenIsDisplayed(Log Log, ScreenShots ScreenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Booking Summary')]")));
			Log.ReportEvent("PASS", "Review Your Flight Page is Displayed");
			Thread.sleep(8000);
		} catch (Exception e) {
			if (isElementPresent(By.xpath("//p[@class='toast-title']"))) {
				WebElement snackbar = driver.findElement(By.xpath("//p[@class='toast-title']"));
				String errorMessage = snackbar.getText().trim(); // Get the actual text

				int currentCount = backEndIssueCount.incrementAndGet();

				Log.ReportEvent("FAIL", "Issue from BackEnd: " + errorMessage + " | Count: " + currentCount);
				ScreenShots.takeScreenShot();
				Assert.fail("BackEnd Issue: " + errorMessage);
			} else {
				Log.ReportEvent("FAIL", "Review Your Flight Page is Not Displayed: " + e.getMessage());
				ScreenShots.takeScreenShot();
				Assert.fail("Review Your Flight Page is Not Displayed: " + e.getMessage());
			}
		}
	}

	// Your existing method for element presence check
	private boolean isElementPresent(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void selectFirstAvailableSeat(Log Log) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		List<WebElement> rows = driver.findElements(By.cssSelector(".flight-seat-map_row"));
		String[] seatLetters = { "A", "B", "C", "D", "E", "F" };

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		for (WebElement row : rows) {
			List<WebElement> rowNumberElems = row
					.findElements(By.cssSelector(".flight-seat-map_seat_aisle_row-number"));
			String rowNumber = rowNumberElems.size() > 0 ? rowNumberElems.get(0).getText().trim() : "";

			List<WebElement> seats = row.findElements(By.cssSelector(".flight-seat-map_seat"));

			for (int i = 0; i < seats.size(); i++) {
				WebElement seat = seats.get(i);

				WebElement img;
				try {
					img = seat.findElement(By.tagName("img"));
				} catch (Exception e) {
					// No image tag — invalid or blank seat
					continue;
				}

				String src = img.getAttribute("src");
				if (src != null && (src.contains("/images/seat-free.svg") || src.contains("/images/seat-lowmid.svg")
						|| src.contains("/images/seat-secondary.svg") || src.contains("/images/seat-premium.svg"))) {
					try {
						// Scroll into view
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", seat);

						// Wait for seat to be clickable
						wait.until(ExpectedConditions.elementToBeClickable(seat));

						// Click using Actions
						Actions actions = new Actions(driver);
						actions.moveToElement(seat).click().perform();

						// ✅ Check for toast message
						try {
							WebElement toast = wait.until(
									ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='toast-title']")));
							if (toast.isDisplayed()) {
								String toastMessage = toast.getText().trim();
								Log.ReportEvent("FAIL", "Seat selection failed. Toast message: " + toastMessage);
								throw new RuntimeException("Seat selection failed with toast: " + toastMessage);
							}
						} catch (TimeoutException te) {
							// No toast → proceed with seat confirmation
						}

						// ✅ Wait for selection indication
						try {
							wait.until(ExpectedConditions.attributeContains(seat, "class", "selected"));
							System.out.println(
									"Selected seat: " + rowNumber + (i < seatLetters.length ? seatLetters[i] : ""));
							Log.ReportEvent("PASS",
									"Selected seat: " + rowNumber + (i < seatLetters.length ? seatLetters[i] : ""));
							return;
						} catch (TimeoutException e) {
							System.out.println("Seat clicked, but no confirmation of selection.");
							continue;
						}

					} catch (Exception e) {
						System.out.println("Failed to click seat at: " + rowNumber
								+ (i < seatLetters.length ? seatLetters[i] : "") + " - " + e.getMessage());
						continue;
					}
				}
			}
		}

		Log.ReportEvent("FAIL", "No available seats found.");
		System.out.println("No available seats found.");
	}

	public void selectFirstAvailableMeal() {
		// Find all "Add" buttons for meals
		List<WebElement> addButtons = driver.findElements(By.cssSelector(".meal-selection_meal-tab_info_btn"));

		// Click the first available one
		if (!addButtons.isEmpty()) {
			addButtons.get(0).click();
			System.out.println("Clicked on the first available meal's 'Add' button.");
		} else {
			System.out.println("No meal 'Add' buttons found.");
		}
	}

	public void selectFirstAvailableSsr() {
		// Find all "Add" buttons for meals
		List<WebElement> addButtons = driver.findElements(By.xpath("//button[text()='Add']"));

		// Click the first available one
		if (!addButtons.isEmpty()) {
			addButtons.get(0).click();
			System.out.println("Clicked on the first available ssr 'Add' button.");
		} else {
			System.out.println("No ssr 'Add' buttons found.");
		}
	}

	public void selectFirstAvailableBaggage() {
		// Find all "Add" buttons for baggage options
		List<WebElement> addButtons = driver.findElements(By.cssSelector(".meal-selection_meal-tab button"));

		// Click the first available one
		if (!addButtons.isEmpty()) {
			addButtons.get(0).click();
			System.out.println("Clicked on the first available baggage's 'Add' button.");
		} else {
			System.out.println("No baggage 'Add' buttons found.");
		}
	}

	public int sumAddOnPrices() {
		int totalPrice = 0;

		// Find all price elements
		List<WebElement> priceElements = driver
				.findElements(By.xpath("//div[@class='special-service-request_add-on_text']/div"));

		for (WebElement priceElem : priceElements) {
			String priceText = priceElem.getText().trim(); // Example: "₹ 0", "₹ 10", "₹ 40"

			// Remove currency symbol and commas, then parse to int
			// Using regex to extract digits only, assuming format "₹ 1,000" or "₹ 1000"
			String numericPart = priceText.replaceAll("[^0-9]", "");

			if (!numericPart.isEmpty()) {
				int price = Integer.parseInt(numericPart);
				totalPrice += price;
			}
		}

		System.out.println("Total Add-on price: ₹ " + totalPrice);
		return totalPrice;
	}

	public void clickConfirmButton() {
		try {
			WebElement confirmButton = driver.findElement(By.xpath("//button[text()='Confirm']"));
			confirmButton.click();
			System.out.println("Clicked on Confirm button.");
		} catch (NoSuchElementException e) {
			System.out.println("Confirm button not found.");
		}
	}

	public void clickSelectAddOns() {
		driver.findElement(By.xpath("//*[text()='Select Add-Ons']")).click();
	}

	// Method to get the Final Price
	public String getFinalPrice() {
		// Locate the element using the provided XPath
		WebElement priceElement = driver.findElement(By.xpath("//div[text()='Total Fare']/following-sibling::div"));

		// Get the text from the element
		String priceText = priceElement.getText();

		return priceText;
	}

	public void clickOnShowConnectedFlightsButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		try {
			// Wait for the button to be visible and clickable
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[@class='flight-booking-page_flight-details_content_action-btn']")));

			// Click the button
			button.click();

			System.out.println("Clicked on 'Show Connected Flights' button.");
		} catch (TimeoutException e) {
			System.out.println("Timeout: 'Show Connected Flights' button not found or not clickable.");
		} catch (Exception e) {
			System.out.println("Error while clicking 'Show Connected Flights' button: " + e.getMessage());
		}
	}

	public Map<String, List<String>> clickIfPresentAndGetBookingData() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		By showConnectedFlightsBtn = By
				.xpath("//div[contains(@class,'flight-booking-page_flight-details flight_0')]//button/span");

		try {
			List<WebElement> buttons = driver.findElements(showConnectedFlightsBtn);

			if (!buttons.isEmpty()) {
				WebElement button = buttons.get(0);

				// Wait for it to be visible and enabled
				wait.until(ExpectedConditions.visibilityOf(button));
				wait.until(ExpectedConditions.elementToBeClickable(button));

				try {
					// Try clicking normally
					button.click();
					System.out.println("Clicked on 'Show Connected Flights' button.");
				} catch (Exception e) {
					// Fallback: click using JavaScript if normal click fails
					System.out.println("Standard click failed, trying JavaScript click...");
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
					System.out.println("Clicked using JavaScript.");
				}

				// Optionally wait for data to load after click
				Thread.sleep(2000); // Adjust or replace with explicit wait for loaded content
			} else {
				System.out.println("'Show Connected Flights' button not found.");
			}
		} catch (Exception e) {
			System.out.println("Exception during click: " + e.getMessage());
		}

		return getDataFromBookingScreenByXPath();
	}

	public Map<String, List<String>> clickIfPresentAndGetBookingDataForReturnFlight() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		By showConnectedFlightsBtn = By
				.xpath("//div[contains(@class,'flight-booking-page_flight-details flight_1')]//button/span");

		try {
			List<WebElement> buttons = driver.findElements(showConnectedFlightsBtn);

			if (!buttons.isEmpty()) {
				WebElement button = buttons.get(0);

				// Wait for it to be visible and enabled
				wait.until(ExpectedConditions.visibilityOf(button));
				wait.until(ExpectedConditions.elementToBeClickable(button));

				try {
					// Try clicking normally
					button.click();
					System.out.println("Clicked on 'Show Connected Flights' button.");
				} catch (Exception e) {
					// Fallback: click using JavaScript if normal click fails
					System.out.println("Standard click failed, trying JavaScript click...");
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
					System.out.println("Clicked using JavaScript.");
				}

				// Optionally wait for data to load after click
				Thread.sleep(2000); // Adjust or replace with explicit wait for loaded content
			} else {
				System.out.println("'Show Connected Flights' button not found.");
			}
		} catch (Exception e) {
			System.out.println("Exception during click: " + e.getMessage());
		}

		return getDataFromBookingScreenByXPathForReturnFlight();
	}

	// Method to Get the Data from Booking Screen.
	public Map<String, List<String>> getDataFromBookingScreenByXPathForReturnFlight() {
		TestExecutionNotifier.showExecutionPopup();
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		// Map of labels to XPaths for the booking screen (return flight)
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("departureTime",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-deptime')]");
		xpathMap.put("originFullName",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-origin-fullname')]");
		xpathMap.put("arrivalTime",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-arrtime')]");
		xpathMap.put("destinationFullName",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-destination-fullname')]");
		xpathMap.put("cabinClass",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("duration",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-mb-duration')]");
		xpathMap.put("layoverText",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//span[@aria-label]");

		for (Map.Entry<String, String> entry : xpathMap.entrySet()) {
			String key = entry.getKey();
			String xpath = entry.getValue();

			List<String> values = new ArrayList<>();

			try {
				// Wait until at least one visible element with non-empty text is found
				wait.until(driver -> {
					List<WebElement> elements = driver.findElements(By.xpath(xpath));
					return !elements.isEmpty() && elements.stream().anyMatch(el -> !el.getText().trim().isEmpty());
				});

				// Fetch and extract visible text elements
				List<WebElement> elements = driver.findElements(By.xpath(xpath));
				for (WebElement el : elements) {
					String text = el.getText().trim();
					if (!text.isEmpty()) {

						// Special handling for origin and destination full names
						if (key.equals("originFullName") || key.equals("destinationFullName")) {
							// Handle different dash types: -, –, —
							String[] parts = text.split("\\s*[–—-]\\s*");
							text = parts[0].trim();

							// If no dash is present, but airport code exists, trim after ')'
							int closingParenIndex = text.indexOf(")");
							if (closingParenIndex != -1) {
								text = text.substring(0, closingParenIndex + 1).trim();
							}
						}

						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout: No visible elements found for XPath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for XPath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("Return Flight Booking Screen UI Data (by XPath): " + data);
		return data;
	}

	// Method to Get the Data from Booking Screen.
// Method to Get the Data from Booking Screen.
	public Map<String, List<String>> getDataFromBookingScreenByXPath() {
		TestExecutionNotifier.showExecutionPopup();
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Map of labels to XPaths for the booking screen
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("departureTime",
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-deptime')]");
		xpathMap.put("originFullName",
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-origin-fullname')]");
		xpathMap.put("arrivalTime",
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-arrtime')]");
		xpathMap.put("destinationFullName",
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-destination-fullname')]");
		xpathMap.put("cabinClass",
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("duration",
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-mb-duration')]");
		xpathMap.put("layoverText", "//div[contains(@class,' oneway-card__connect_line_text')]//span");

		for (Map.Entry<String, String> entry : xpathMap.entrySet()) {
			String key = entry.getKey();
			String xpath = entry.getValue();

			List<String> values = new ArrayList<>();

			try {
				// Wait until at least one visible element with non-empty text is found
				wait.until(driver -> {
					List<WebElement> elements = driver.findElements(By.xpath(xpath));
					return !elements.isEmpty() && elements.stream().anyMatch(el -> !el.getText().trim().isEmpty());
				});

				// Fetch and extract visible text elements
				List<WebElement> elements = driver.findElements(By.xpath(xpath));
				for (WebElement el : elements) {
					String text = el.getText().trim();
					if (!text.isEmpty()) {

						// Special handling for origin and destination full names
						if (key.equals("originFullName") || key.equals("destinationFullName")) {
							// Handle dashes (–, —, -) if present
							String[] parts = text.split("\\s*[–—-]\\s*");
							text = parts[0].trim();

							// If no dash, trim everything after closing parenthesis ')'
							int closingParenIndex = text.indexOf(")");
							if (closingParenIndex != -1) {
								text = text.substring(0, closingParenIndex + 1).trim();
							}
						}

						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout: No visible elements found for XPath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for XPath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("Booking Screen UI Data (by XPath): " + data);
		return data;
	}

	// Method to Validate Flight Details From Result to Booking Screen.
	public void validateSearchAndBookingDataByXPath(Map<String, List<String>> searchData,
			Map<String, List<String>> bookingData, Log log, ScreenShots screenShots) {
		TestExecutionNotifier.showExecutionPopup();

		// Friendly field name -> [searchDataKey, bookingDataKey]
		Map<String, String[]> fieldMappings = new LinkedHashMap<>();
		fieldMappings.put("Departure Time", new String[] { "fromDepartureTime", "departureTime" });
		fieldMappings.put("Origin", new String[] { "fromOrigin", "originFullName" });
		fieldMappings.put("Arrival Time", new String[] { "fromArrivalTime", "arrivalTime" });
		fieldMappings.put("Destination", new String[] { "fromDestination", "destinationFullName" });
		fieldMappings.put("Cabin Class", new String[] { "cabinClass", "cabinClass" });
		fieldMappings.put("Duration", new String[] { "totalDuration", "duration" });
		fieldMappings.put("Layover Info", new String[] { "connectionLineText", "layoverText" });

		boolean allMatch = true;

		for (Map.Entry<String, String[]> entry : fieldMappings.entrySet()) {
			String label = entry.getKey();
			String searchKey = entry.getValue()[0];
			String bookingKey = entry.getValue()[1];

			List<String> searchList = searchData.getOrDefault(searchKey, Collections.emptyList());
			List<String> bookingList = bookingData.getOrDefault(bookingKey, Collections.emptyList());

			int maxSize = Math.max(searchList.size(), bookingList.size());

			for (int i = 0; i < maxSize; i++) {
				String searchVal = i < searchList.size() ? searchList.get(i).trim() : "<missing>";
				String bookingVal = i < bookingList.size() ? bookingList.get(i).trim() : "<missing>";

				if (!searchVal.equals(bookingVal)) {
					allMatch = false;
					log.ReportEvent("FAIL", String.format("Mismatch in '%s' at index %d: Search = '%s', Booking = '%s'",
							label, i + 1, searchVal, bookingVal));
					screenShots.takeScreenShot();
					Assert.fail(); // Optional: remove this if you want to collect all mismatches before failing
				}
			}
		}

		if (allMatch) {
			log.ReportEvent("PASS", "All flight details match between Search and Booking screens.");
		} else {
			Assert.fail("Validation failed: Mismatch found between Search and Booking data.");
		}
	}

	// Method to Get the Data from Booking Screen
	public Map<String, String> getDataFromBookingPage() {
		Map<String, String> fareDetails = new HashMap<>();
		// Wait setup (optional but recommended)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Get fare price text
		WebElement fareNameElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("(//span[@class='tg-origin-destination']//span)[3]")));
		String fareName = fareNameElement.getText().trim();
		;
		fareDetails.put("FareName", fareName.replaceFirst("^\\|\\s*", "").trim());

		// Get supplier name from img alt attribute
		WebElement supplierImg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//img[contains(@class,'tg-supplier-img')]")));
		String supplierName = supplierImg.getAttribute("alt");
		fareDetails.put("Supplier", supplierName);

		WebElement policyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//div[contains(@class,'tg-policy')]")));
		fareDetails.put("PolicyType", policyElement.getText().trim());

		return fareDetails;
	}

	// Method to Get the Data from Booking Screen
	public Map<String, String> getDataFromBookingPageForReturnFlights() {
		Map<String, String> fareDetails = new HashMap<>();
		// Wait setup (optional but recommended)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Get fare price text
		WebElement fareNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"(//div[contains(@class,'flight-booking-page_flight-details flight_1')]//span[@class='tg-origin-destination']/span)[3]")));
		String fareName = fareNameElement.getText().trim();
		;
		fareDetails.put("FareName", fareName.replaceFirst("^\\|\\s*", "").trim());

		// Get supplier name from img alt attribute
		WebElement supplierImg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//img[contains(@class,'tg-supplier-img')]")));
		String supplierName = supplierImg.getAttribute("alt");
		fareDetails.put("Supplier", supplierName);

		WebElement policyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//div[contains(@class,'tg-policy')]")));
		fareDetails.put("PolicyType", policyElement.getText().trim());

		return fareDetails;
	}

	// Method to Click on Proceed Booking.
	public void clickProceedBookingAndValidateToast(Log log, ScreenShots screenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		By proceedBookingBtn = By.xpath("//button[contains(@class,'tg-fbbook-btn')]");
		By toastLocator = By.xpath("//div[contains(@class,'toast')]");
		By toastMessageLocator = By.xpath("//p[@class='toast-title']");

		try {
			// ✅ Step 1: Click the "Proceed Booking" button
			wait.until(ExpectedConditions.elementToBeClickable(proceedBookingBtn)).click();
			log.ReportEvent("INFO", "Clicked on 'Proceed Booking' button.");
			System.out.println("Clicked on 'Proceed Booking' button.");

			// ✅ Step 2: Wait for toast to appear
			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
			WebElement toastMessageElement = toast.findElement(toastMessageLocator);
			String toastMessage = toastMessageElement.getText().trim();

			// ✅ Step 3: Check if toast has 'error' class
			String toastClass = toast.getAttribute("class");
			boolean isError = toastClass.contains("error");

			if (isError) {
				// ❌ Toast is an error — fail the test
				log.ReportEvent("FAIL", "Error Toast Message: " + toastMessage);
				screenShots.takeScreenShot();
				Assert.fail("Booking failed due to error toast: " + toastMessage);
			} else {
				// ✅ Toast is success/info
				log.ReportEvent("PASS", "Toast Message after booking: " + toastMessage);
				System.out.println("Toast Message: " + toastMessage);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Exception while clicking or fetching toast message: " + e.getMessage());
			screenShots.takeScreenShot();
			Assert.fail("Exception occurred during booking.");
		}
	}

	public String[] getJourneyDetails() {
		String[] journeys = new String[2];

		// XPaths for flight_0 and flight_1
		String[] xpaths = {
				"//div[contains(@class,'flight-booking-page_flight-details flight_0')]//span[@class='tg-origin-destination']",
				"//div[contains(@class,'flight-booking-page_flight-details flight_1')]//span[@class='tg-origin-destination']" };

		for (int i = 0; i < xpaths.length; i++) {
			String raw = driver.findElement(By.xpath(xpaths[i])).getText();
			// Convert "(date)" → "- date"
			journeys[i] = raw.replace("(", "- ").replace(")", "").trim();
		}

		return journeys;
	}

	public String getTotalFare() {
		// Locate the element after "Total Fare"
		String raw = driver.findElement(By.xpath("//div[text()='Total Fare']/following-sibling::div")).getText();

		// Trim to remove extra spaces
		return raw.trim();
	}

	public Map<String, List<String>> clickIfPresentAndGetBookingDataForInternationDepartingFlight() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By showConnectedFlightsBtn = By
				.xpath("(//div[contains(@class,'flight-booking-page_flight-details')])[1]//button/span");

		try {
			List<WebElement> buttons = driver.findElements(showConnectedFlightsBtn);

			if (!buttons.isEmpty()) {
				WebElement button = buttons.get(0);

				// Wait for it to be visible and enabled
				wait.until(ExpectedConditions.visibilityOf(button));
				wait.until(ExpectedConditions.elementToBeClickable(button));

				try {
					// Try clicking normally
					button.click();
					System.out.println("Clicked on 'Show Connected Flights' button.");
				} catch (Exception e) {
					// Fallback: click using JavaScript if normal click fails
					System.out.println("Standard click failed, trying JavaScript click...");
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
					System.out.println("Clicked using JavaScript.");
				}

				// Optionally wait for data to load after click
				Thread.sleep(2000); // Adjust or replace with explicit wait for loaded content
			} else {
				System.out.println("'Show Connected Flights' button not found.");
			}
		} catch (Exception e) {
			System.out.println("Exception during click: " + e.getMessage());
		}

		return getDataFromBookingScreenByXPathForInterNationalDepartingFlight();
	}

	// Method to Get the Data from Booking Screen.
	public Map<String, List<String>> getDataFromBookingScreenByXPathForInterNationalDepartingFlight() {
		TestExecutionNotifier.showExecutionPopup();
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Map of labels to XPaths for the booking screen
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("departureTime",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-deptime')]");
		xpathMap.put("originFullName",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-origin-fullname')]");
		xpathMap.put("arrivalTime",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-arrtime')]");
		xpathMap.put("destinationFullName",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-destination-fullname')]");
		xpathMap.put("cabinClass",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("duration",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-mb-duration')]");
		xpathMap.put("layoverText",
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'oneway-card__connect_line_text')]//span");

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

						// Special handling for origin and destination full names
						if (key.equals("originFullName") || key.equals("destinationFullName")) {
							// Handle dashes (–, —, -) if present
							String[] parts = text.split("\\s*[–—-]\\s*");
							text = parts[0].trim();

							// If no dash, trim everything after closing parenthesis ')'
							int closingParenIndex = text.indexOf(")");
							if (closingParenIndex != -1) {
								text = text.substring(0, closingParenIndex + 1).trim();
							}
						}

						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout: No visible elements found for XPath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for XPath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("Booking Screen UI Data (by XPath): " + data);
		return data;
	}

	public Map<String, List<String>> clickIfPresentAndGetBookingDataForInternationReturnFlight() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(18));
		By showConnectedFlightsBtn = By
				.xpath("(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//button/span");

		try {
			List<WebElement> buttons = driver.findElements(showConnectedFlightsBtn);

			if (!buttons.isEmpty()) {
				WebElement button = buttons.get(0);

				// Wait for it to be visible and enabled
				wait.until(ExpectedConditions.visibilityOf(button));
				wait.until(ExpectedConditions.elementToBeClickable(button));

				try {
					// Try clicking normally
					button.click();
					System.out.println("Clicked on 'Show Connected Flights' button.");
				} catch (Exception e) {
					// Fallback: click using JavaScript if normal click fails
					System.out.println("Standard click failed, trying JavaScript click...");
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
					System.out.println("Clicked using JavaScript.");
				}

				// Optionally wait for data to load after click
				Thread.sleep(2000); // Adjust or replace with explicit wait for loaded content
			} else {
				System.out.println("'Show Connected Flights' button not found.");
			}
		} catch (Exception e) {
			System.out.println("Exception during click: " + e.getMessage());
		}

		return getDataFromBookingScreenByXPathForInterNationalReturnFlight();
	}

	// Method to Get the Data from Booking Screen.
	public Map<String, List<String>> getDataFromBookingScreenByXPathForInterNationalReturnFlight() {
		TestExecutionNotifier.showExecutionPopup();
		Map<String, List<String>> data = new LinkedHashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		// Map of labels to XPaths for the international return flight booking screen
		Map<String, String> xpathMap = new LinkedHashMap<>();
		xpathMap.put("departureTime",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'tg-deptime')]");
		xpathMap.put("originFullName",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'tg-origin-fullname')]");
		xpathMap.put("arrivalTime",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'tg-arrtime')]");
		xpathMap.put("destinationFullName",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'tg-destination-fullname')]");
		xpathMap.put("cabinClass",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'tg-cabinclass')]");
		xpathMap.put("duration",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'tg-mb-duration')]");
		xpathMap.put("layoverText",
				"(//div[contains(@class,'flight-booking-page_flight-details_content')])[2]//div[contains(@class,'oneway-card__connect_line_text')]//span");

		for (Map.Entry<String, String> entry : xpathMap.entrySet()) {
			String key = entry.getKey();
			String xpath = entry.getValue();

			List<String> values = new ArrayList<>();

			try {
				// Wait until at least one visible element with non-empty text is found
				wait.until(driver -> {
					List<WebElement> elements = driver.findElements(By.xpath(xpath));
					return !elements.isEmpty() && elements.stream().anyMatch(el -> !el.getText().trim().isEmpty());
				});

				// Fetch and extract visible text elements
				List<WebElement> elements = driver.findElements(By.xpath(xpath));
				for (WebElement el : elements) {
					String text = el.getText().trim();
					if (!text.isEmpty()) {

						// Special handling for origin and destination full names
						if (key.equals("originFullName") || key.equals("destinationFullName")) {
							// Handle different dash types: -, –, —
							String[] parts = text.split("\\s*[–—-]\\s*");
							text = parts[0].trim();

							// If no dash is present, but airport code exists, trim after ')'
							int closingParenIndex = text.indexOf(")");
							if (closingParenIndex != -1) {
								text = text.substring(0, closingParenIndex + 1).trim();
							}
						}

						values.add(text);
					}
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout: No visible elements found for XPath: " + xpath);
			} catch (Exception e) {
				System.out.println("Error fetching data for XPath: " + xpath + " - " + e.getMessage());
			}

			data.put(key, values);
		}

		System.out.println("International Return Flight Booking Screen UI Data (by XPath): " + data);
		return data;
	}

	// Method to Get the Data from Booking Screen
	public Map<String, String> getDataFromBookingPageForInterNational() {
		Map<String, String> fareDetails = new HashMap<>();
		// Wait setup (optional but recommended)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Get fare price text
		WebElement fareNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"((//div[contains(@class,'flight-booking-page_flight-details')])[1]//span[@class='tg-origin-destination']/span)[3]")));
		String fareName = fareNameElement.getText().trim();
		;
		fareDetails.put("FareName", fareName.replaceFirst("^\\|\\s*", "").trim());

		// Get supplier name from img alt attribute
		WebElement supplierImg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//img[contains(@class,'tg-supplier-img')]")));
		String supplierName = supplierImg.getAttribute("alt");
		fareDetails.put("Supplier", supplierName);

		WebElement policyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"(//div[contains(@class,'flight-booking-page_flight-details')])[1]//div[contains(@class,'tg-policy')]")));
		fareDetails.put("PolicyType", policyElement.getText().trim());

		return fareDetails;
	}

	// Arun main code
	public int selectAddOnsNormalFlow(Log Log, ScreenShots ScreenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		int totalAddOnPrice = 0;

		List<WebElement> addOnsElements = driver.findElements(By.xpath("//*[text()='Select Add-Ons']"));
		Log.ReportEvent("INFO", "Number of 'Select Add-Ons' elements found: " + addOnsElements.size());

		for (int addonIndex = 0; addonIndex < addOnsElements.size(); addonIndex++) {
			Log.ReportEvent("INFO", "Processing Select Add-Ons #" + (addonIndex + 1));

			try {
				WebElement addOnButton = wait
						.until(ExpectedConditions.elementToBeClickable(addOnsElements.get(addonIndex)));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						addOnButton);
				addOnButton.click();

				// Always start with Seat Selection tab
				processTabnrml("Seat Selection", () -> selectFirstAvailableSeat(Log), Log, ScreenShots);

				// Then Meal Preferences
				processTabnrml("Meal Preferences", () -> selectFirstAvailableMeal(), Log, ScreenShots);

				// Then Baggage
				processTabnrml("Baggage", () -> selectFirstAvailableBaggage(), Log, ScreenShots);

				// Special SSR
				processTabnrml("Special Requests", () -> selectFirstAvailableSsr(), Log, ScreenShots);

				// ---------------- PRICE CALCULATION ----------------
				try {
					int currentAddOnPrice = sumAddOnPrices();
					Log.ReportEvent("INFO", "Total price for current add-on: ₹ " + currentAddOnPrice);

					totalAddOnPrice += currentAddOnPrice;
					Log.ReportEvent("PASS", "Running total price so far: ₹ " + totalAddOnPrice);
				} catch (Exception e) {
					Log.ReportEvent("FAIL", "Price calculation failed: " + e.getMessage());
					ScreenShots.takeScreenShot();
					Assert.fail("Price calculation failed.");
				}

				// ---------------- CONFIRM ----------------
				try {
					clickConfirmButton();
					Log.ReportEvent("PASS", "Confirm button clicked successfully.");
				} catch (Exception e) {
					Log.ReportEvent("FAIL", "Confirm button click failed: " + e.getMessage());
					ScreenShots.takeScreenShot();
					Assert.fail("Confirm button click failed.");
				}

				// Re-fetch "Select Add-Ons" list if more remain
				if (addonIndex + 1 < addOnsElements.size()) {
					addOnsElements = driver.findElements(By.xpath("//*[text()='Select Add-Ons']"));
				}

			} catch (Exception e) {
				Log.ReportEvent("FAIL", "Add-On processing failed at index " + addonIndex + ": " + e.getMessage());
				ScreenShots.takeScreenShot();
				Assert.fail("Add-On processing failed.");
			}
		}

		Log.ReportEvent("PASS", "Final total price of all add-ons: ₹ " + totalAddOnPrice);
		return totalAddOnPrice;
	}

	/**
	 * Generic method to process a tab (Seat, Meal, Baggage).
	 */
	private void processTab(String tabName, Runnable selectionAction, Log Log, ScreenShots ScreenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		try {
			WebElement tab = driver.findElement(By.xpath("//div[text()='" + tabName + "']"));
			tab.click();

			// Wait for either sectors or not-found message
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("(//div[contains(@class,'special-service-request_sector-tabs')])[2]/div")),
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[@class='special-service-request_preferences_not-found_primary-text']"))));

			if (isNotFoundMessageDisplayed()) {
				Log.ReportEvent("INFO", tabName + " not available. Skipping...");
				return;
			}

			List<WebElement> sectors = driver
					.findElements(By.xpath("(//div[contains(@class,'special-service-request_sector-tabs')])[2]/div"));

			if (sectors.isEmpty()) {
				Log.ReportEvent("INFO", "No sectors found for " + tabName + ". Skipping...");
				return;
			}

			for (int i = 0; i < sectors.size(); i++) {
				try {
					Log.ReportEvent("INFO", "Selecting " + tabName + " for sector " + (i + 1));
					selectionAction.run();
				} catch (Exception ex) {
					Log.ReportEvent("INFO", tabName + " not selectable for sector " + (i + 1) + ". Moving on...");
				}
			}

			Log.ReportEvent("PASS", tabName + " selection completed.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", tabName + " processing failed: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}
	}

// ---aruns actual code --------
	private void processTabnrml(String tabName, Runnable selectionAction, Log Log, ScreenShots ScreenShots) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		try {
			// Click main tab
			WebElement tab = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + tabName + "']")));
			tab.click();

			// Wait for either sectors or not-found message
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("(//div[contains(@class,'special-service-request_sector-tab')])[6]/div")),
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[@class='special-service-request_preferences_not-found_primary-text']"))));

			// Check not found message
			if (isNotFoundMessageDisplayed()) {
				Log.ReportEvent("INFO", tabName + " not available. Skipping...");
				return;
			}

			// Get total sectors count
			List<WebElement> sectors = driver
					.findElements(By.xpath("(//div[contains(@class,'special-service-request_sector-tab')])[6]/div"));

			if (sectors.isEmpty()) {
				Log.ReportEvent("INFO", "No sectors found for " + tabName + ". Skipping...");
				return;
			}

			// Loop through sectors by index (IMPORTANT for SSR)
			for (int i = 1; i <= sectors.size(); i++) {
				try {
					Log.ReportEvent("INFO", "Selecting " + tabName + " for sector " + i);

					// 👇 Re-locate sector each time to avoid stale element issues
					WebElement sector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
							"((//div[contains(@class,'special-service-request_sector-tab')])[6]/div)[" + i + "]")));

					sector.click();

					// Small wait for content refresh (SSR UI is async)
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath("//div[contains(@class,'special-service-request')]")));

					// Perform selection
					selectionAction.run();

				} catch (Exception ex) {
					Log.ReportEvent("INFO", tabName + " not selectable for sector " + i + ". Moving on...");
				}
			}

			Log.ReportEvent("PASS", tabName + " selection completed.");

		} catch (Exception e) {
			Log.ReportEvent("FAIL", tabName + " processing failed: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}
	}

	public int selectAddOnsFlow2(Log Log, ScreenShots ScreenShots, String body4, String body5) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		int totalAddOnPrice = 0;

		List<WebElement> addOnsElements = driver.findElements(By.xpath("//*[text()='Select Add-Ons']"));

		for (int addonIndex = 0; addonIndex < addOnsElements.size(); addonIndex++) {
			try {
				WebElement addOnButton = wait
						.until(ExpectedConditions.elementToBeClickable(addOnsElements.get(addonIndex)));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						addOnButton);
				addOnButton.click();

				// Use body4 for Seats
				processTab1("Seat Selection", () -> selectFirstAvailableSeat(Log), Log, ScreenShots, body4);

				// Use body5 for everything else
				processTab1("Meal Preferences", () -> selectFirstAvailableMeal(), Log, ScreenShots, body5);
				processTab1("Baggage", () -> selectFirstAvailableBaggage(), Log, ScreenShots, body5);
				processTab1("Special Requests", () -> selectFirstAvailableSsr(), Log, ScreenShots, body5);

				totalAddOnPrice += sumAddOnPrices();
				clickConfirmButton();

				if (addonIndex + 1 < addOnsElements.size()) {
					addOnsElements = driver.findElements(By.xpath("//*[text()='Select Add-Ons']"));
				}
			} catch (Exception e) {
				Log.ReportEvent("FAIL", "Add-On processing failed: " + e.getMessage());
			}
		}
		return totalAddOnPrice;
	}

	private void processTab1(String tabName, Runnable selectionAction, Log Log, ScreenShots ScreenShots,
			String responseBody) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		Tripgain_HomePage_Flights homePage = new Tripgain_HomePage_Flights(driver);

		try {
			// 1. Click the main Tab (Seats, Meals, etc.)
			WebElement tab = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + tabName + "']")));
			tab.click();

			if (isNotFoundMessageDisplayed()) {
				Log.ReportEvent("INFO", tabName + " not available. Skipping...");
				return;
			}

			String sectorBaseXpath = "(//div[contains(@class,'special-service-request_sector-tabs')])[2]/div";
			List<WebElement> sectors = driver.findElements(By.xpath(sectorBaseXpath));

			for (int i = 1; i <= sectors.size(); i++) {
				// 2. Click the specific Sector
				WebElement sector = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("(" + sectorBaseXpath + ")[" + i + "]")));
				sector.click();

				// 3. DYNAMIC WAIT: Wait for the specific data cards to appear
				String itemCardXpath = tabName.equals("Seat Selection")
						? "//div[contains(@class,'flight-seat-map_seat')]"
						: "//div[contains(@class,'meal-selection_meal-tab_info')]";

				try {
					// We use a short wait here. If it fails, the sector is truly empty.
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(itemCardXpath)));

					List<WebElement> itemCards = driver.findElements(By.xpath(itemCardXpath));
					List<Map<String, String>> uiData = new ArrayList<>();

					// 4. Scrape & Validate
					if (tabName.equals("Seat Selection")) {
						uiData = homePage.getOnewayBookingpgAllSeatData(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageSeatData(uiData, responseBody, Log, ScreenShots);
					} else if (tabName.equals("Meal Preferences")) {
						uiData = homePage.getOnewayBookingpgAllAvailableMeals(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageMealData(uiData, responseBody, Log, ScreenShots);
					} else if (tabName.equals("Baggage")) {
						uiData = homePage.getOnewayBookingpgAllAvailableBaggage(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageBaggageData(uiData, responseBody, Log, ScreenShots);
					} else if (tabName.equals("Special Requests")) {
						uiData = homePage.getOnewayBookingpgAllAvailableSpecialRequests(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageSpecialRequests(uiData, responseBody, Log, ScreenShots);
					}

					// 5. Select the first item
					// Log.ReportEvent("INFO", "Performing selection for " + tabName + " in Sector "
					// + i);
					selectionAction.run();

				} catch (Exception e) {
					// Log.ReportEvent("INFO", "No items found for " + tabName + " in Sector " + i +
					// " after waiting.");
				}
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", tabName + " processing failed: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}
	}

	public List<Map<String, String>> getOnewayBookingpgAllAvailableBaggage(Log log, ScreenShots ScreenShots,
			int sectorIndex) {
		List<Map<String, String>> baggageList = new ArrayList<>();
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

				data.put("sector", currentSector);
				data.put("mealName", name);
				data.put("mealPrice", price);
				baggageList.add(data);
			}
		} catch (Exception e) {
			log.ReportEvent("INFO", "Baggage scrape failed for sector " + sectorIndex);
		}
		return baggageList;
	}

	/**
	 * Helper method to check if "Not Found" message is displayed.
	 */
	private boolean isNotFoundMessageDisplayed() {
		List<WebElement> notFoundElements = driver
				.findElements(By.xpath("//div[@class='special-service-request_preferences_not-found_primary-text']"));
		return !notFoundElements.isEmpty() && notFoundElements.get(0).isDisplayed();
	}

	public void validateTotalPriceMatches(String resultMainPrice, String resultOtherCurrencyPrice, Log Log,
			ScreenShots ScreenShots) {
		try {
			// Combine result screen prices as per booking screen format
			String expectedBookingPrice = resultMainPrice.trim() + "\n" + resultOtherCurrencyPrice.trim();

			// Get actual value from booking screen
			String actualBookingPrice = driver.findElement(By.xpath("//div[text()='Total Fare']/following-sibling::h6"))
					.getText().trim();

			if (expectedBookingPrice.equals(actualBookingPrice)) {
				Log.ReportEvent("PASS", "Booking screen total price matches expected result screen price.");
			} else {
				Log.ReportEvent("FAIL",
						"Price mismatch!\nExpected: " + expectedBookingPrice + "\nActual: " + actualBookingPrice);
				ScreenShots.takeScreenShot();
				Assert.fail("Booking price does not match result screen price.");
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Error validating booking screen price: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail("Exception during price validation.");
		}
	}

	public static String generateRandomDigits(int length) {
		String digits = "0123456789";
		Random rng = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(digits.charAt(rng.nextInt(digits.length())));
		}
		return sb.toString();
	}

	// Method to Enter Adult Details.
	public void enterAdultDetailsForInterNational(String[] title, String countryCode, int adults, String birthYear,
			String birthMonth, String birthDay, String expireYear, String expireMonth, String expireDay,
			String issueYear, String issueMonth, String issueDay, Log Log, ScreenShots ScreenShots)
			throws InterruptedException {
		try {
			TestExecutionNotifier.showExecutionPopup();

			if (adults > 1) {
				for (int i = 0; i < title.length; i++) {
					try {
						String firstName = generateRandomString(5);
						String lastName = generateRandomString(5);
						String first = "Appu" + firstName;
						String last = "Kumar" + lastName;

						String titleNames = title[i];
						int xpathIndex = i + 2;
						Thread.sleep(800);

						// Select Adult dropdown
//						try {
//							String xpath = "(//div[contains(@class,'tg-label_white tg-label_sm') and text()='Adult " + xpathIndex +" "+"']/parent::div/following-sibling::div)[2]/*";
//							WebElement element = driver.findElement(By.xpath(xpath));
//							WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//							element = wait.until(ExpectedConditions.elementToBeClickable(element));
//							((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
//							Thread.sleep(300);
//							Actions actions = new Actions(driver);
//							actions.moveToElement(element).click().perform();
//						} catch (Exception e) {
//							Log.ReportEvent("FAIL", "Failed to click Adult dropdown for index " + xpathIndex);
//							throw e;
//						}

						// Select Title
						try {
							WebElement titleDropDown = driver.findElement(By
									.xpath("(//span[text()='Title']/parent::div//input[@class='tg-select-box__input'])["
											+ xpathIndex + "]"));
							titleDropDown.click();
							WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
							WebElement option = wait.until(ExpectedConditions
									.elementToBeClickable(By.xpath("//span[text()='" + titleNames + "']")));
							((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
							Actions actions = new Actions(driver);
							actions.moveToElement(option).click().perform();
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to select Title '" + titleNames + "' for Adult " + (i + 1));
							throw e;
						}

						// Enter First Name and Last Name
						try {
							WebElement firstNameField = driver
									.findElement(By.xpath("(//input[@name='firstname'])[" + xpathIndex + "]"));
							firstNameField.clear();
							firstNameField.sendKeys(first);
							WebElement lastNameField = driver
									.findElement(By.xpath("(//input[@name='lastname'])[" + xpathIndex + "]"));
							lastNameField.clear();
							lastNameField.sendKeys(last);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to enter first or last name for Adult " + (i + 1));
							throw e;
						}

						// Select Date of Birth
						try {
							selectDateOfBirthDate(birthYear, birthMonth, birthDay, xpathIndex);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to select Date of Birth for Adult " + (i + 1));
							throw e;
						}

						// Enter Passport Number
						try {
							WebElement passportNumber = driver
									.findElement(By.xpath("(//input[@name='passportnumber'])[" + xpathIndex + "]"));
							passportNumber.clear();
							String randomNumber = generateRandomDigits(6);
							System.out.println("Random Number: " + randomNumber);
							passportNumber.sendKeys(randomNumber);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to enter Passport Number for Adult " + (i + 1));
							throw e;
						}

						// Select Expiry Date
						try {
							selectExpireDate(expireYear, expireMonth, expireDay, xpathIndex);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to select Expiry Date for Adult " + (i + 1));
							throw e;
						}

						// Select Issue Date
						try {
							selectIssueDate(issueYear, issueMonth, issueDay, xpathIndex);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to select Issue Date for Adult " + (i + 1));
							throw e;
						}

						// Select Passport Issue Country
						try {
							WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
							JavascriptExecutor js = (JavascriptExecutor) driver;
							Actions actions = new Actions(driver);

							WebElement titleDropDownForPassportIssue = wait
									.until(ExpectedConditions.elementToBeClickable(By.xpath(
											"(//span[text()='Passport Issue Country']/parent::div//div[contains(@class,'tg-select-box__dropdown-indicator')])["
													+ xpathIndex + "]")));
							js.executeScript("arguments[0].scrollIntoView({block: 'center'});",
									titleDropDownForPassportIssue);
							Thread.sleep(200);
							wait.until(ExpectedConditions.elementToBeClickable(titleDropDownForPassportIssue));
							actions.moveToElement(titleDropDownForPassportIssue).click().perform();

							WebElement countryElement = wait.until(ExpectedConditions
									.presenceOfElementLocated(By.xpath("//span[text()='" + countryCode + "']")));
							js.executeScript("arguments[0].scrollIntoView({block: 'center'});", countryElement);
							wait.until(ExpectedConditions.elementToBeClickable(countryElement));
							actions.moveToElement(countryElement).click().perform();
							Thread.sleep(1000);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to select Passport Issue Country '" + countryCode
									+ "' for Adult " + (i + 1));
							throw e;
						}
					} catch (Exception e) {
						// Already logged specific failure above, rethrow to outer catch
						throw e;
					}
				}

			} else {
				System.out.println("One Adult had been Selected");
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Enter Adult Details is UnSuccessful");
			e.printStackTrace();
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	public void selectAdultToEnterDetails(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		// Select Adult dropdown
		try {
			String xpath = "(//div[contains(@class,'tg-label_white tg-label_sm') and text()='Adult " + "1" + " "
					+ "']/parent::div/following-sibling::div)[2]/*";
			WebElement element = driver.findElement(By.xpath(xpath));
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			element = wait.until(ExpectedConditions.elementToBeClickable(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
			Thread.sleep(300);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().perform();
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to click Adult Edit Button for First Adult");
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	public void enterPassportNumber(Log Log, ScreenShots ScreenShots) {
		try {
			WebElement passportNumber = driver.findElement(By.xpath("//input[@name='passportnumber']"));
			passportNumber.clear();
			String randomNumber = generateRandomDigits(6);
			System.out.println("Random Number: " + randomNumber);
			passportNumber.sendKeys(randomNumber);
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to enter Passport Number for Adult 1");
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	public void selectPassportIssuedCountry(String countryCode, Log Log, ScreenShots ScreenShots) {
		// Select Passport Issue Country
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Actions actions = new Actions(driver);

			WebElement titleDropDownForPassportIssue = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//span[text()='Passport Issue Country']/parent::div//div[contains(@class,'tg-select-box__dropdown-indicator')]")));
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", titleDropDownForPassportIssue);
			Thread.sleep(200);
			wait.until(ExpectedConditions.elementToBeClickable(titleDropDownForPassportIssue));
			actions.moveToElement(titleDropDownForPassportIssue).click().perform();

			WebElement countryElement = wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + countryCode + "']")));
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", countryElement);
			wait.until(ExpectedConditions.elementToBeClickable(countryElement));
			actions.moveToElement(countryElement).click().perform();
			Thread.sleep(1000);
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to select Passport Issue Country '" + countryCode + "' for Adult 1");
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	// Method to generate random numbers
	public static String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rng = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(rng.nextInt(characters.length())));
		}
		return sb.toString();
	}

	public void selectDateOfBirthDate(String year, String month, String day, int xpathIndex)
			throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);

		WebElement dobInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"(//label[text()='Date of Birth']/parent::div/parent::div//input[@class='custom_datepicker_input'])["
						+ xpathIndex + "]")));

		js.executeScript("arguments[0].scrollIntoView(true);", dobInput);
		Thread.sleep(300);

		js.executeScript("arguments[0].click();", dobInput);
		// 1. Click on the year dropdown
		WebElement yearDropdown = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@class='react-datepicker']//div[contains(@class,'tg-select-box__indicators')]")));
		yearDropdown.click();

		// 2. Scroll to and click the year (e.g., "1953")
		WebElement yearElement = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + year + "']")));
		js.executeScript("arguments[0].scrollIntoView(true);", yearElement);
		actions.moveToElement(yearElement).click().perform();

		// 3. Check if the desired month is visible, if not click next until it is
		boolean monthVisible = false;
		int maxTries = 12; // Avoid infinite loop

		for (int i = 0; i < maxTries; i++) {
			try {
				WebElement monthElement = driver.findElement(By.xpath("//span[text()='" + month + "']"));
				if (monthElement.isDisplayed()) {
					monthVisible = true;
					break;
				}
			} catch (Exception e) {
				// Month not found, click "Next"
				WebElement nextButton = driver.findElement(By.xpath("(//div[@class='custom-header']/button)[1]"));
				nextButton.click();
				Thread.sleep(400); // Small delay for calendar to update
			}
		}

		if (!monthVisible) {
			throw new RuntimeException("Month '" + month + "' was not found after navigating.");
		}

		// 4. Select the day (e.g., "16")
		WebElement dayElement = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + day + "']")));
		js.executeScript("arguments[0].scrollIntoView(true);", dayElement);
		actions.moveToElement(dayElement).click().perform();
	}

	public void selectExpireDate(String year, String month, String day, int xpathIndex) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);

		WebElement dobInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"(//label[text()='Expiry Date']/parent::div/parent::div//input[@class='custom_datepicker_input'])["
						+ xpathIndex + "]")));

		js.executeScript("arguments[0].scrollIntoView(true);", dobInput);
		Thread.sleep(300);

		js.executeScript("arguments[0].click();", dobInput);
		// 1. Click on the year dropdown
		WebElement yearDropdown = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@class='react-datepicker']//div[contains(@class,'tg-select-box__indicators')]")));
		yearDropdown.click();

		// 2. Scroll to and click the year (e.g., "1953")
		WebElement yearElement = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + year + "']")));
		js.executeScript("arguments[0].scrollIntoView(true);", yearElement);
		actions.moveToElement(yearElement).click().perform();

		// 3. Check if the desired month is visible, if not click next until it is
		boolean monthVisible = false;
		int maxTries = 12; // Avoid infinite loop

		for (int i = 0; i < maxTries; i++) {
			try {
				WebElement monthElement = driver.findElement(By.xpath("//span[text()='" + month + "']"));
				if (monthElement.isDisplayed()) {
					monthVisible = true;
					break;
				}
			} catch (Exception e) {
				// Month not found, click "Next"
				WebElement nextButton = driver.findElement(By.xpath("(//div[@class='custom-header']/button)[2]"));
				nextButton.click();
				Thread.sleep(400); // Small delay for calendar to update
			}
		}

		if (!monthVisible) {
			throw new RuntimeException("Month '" + month + "' was not found after navigating.");
		}

		// 4. Select the day (e.g., "16")
		WebElement dayElement = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + day + "']")));
		js.executeScript("arguments[0].scrollIntoView(true);", dayElement);
		actions.moveToElement(dayElement).click().perform();
	}

	public void selectIssueDate(String year, String month, String day, int xpathIndex) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);

		WebElement dobInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"(//label[text()='Issue Date']/parent::div/parent::div//input[@class='custom_datepicker_input'])["
						+ xpathIndex + "]")));

		js.executeScript("arguments[0].scrollIntoView(true);", dobInput);
		Thread.sleep(300);

		js.executeScript("arguments[0].click();", dobInput);
		// 1. Click on the year dropdown
		WebElement yearDropdown = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@class='react-datepicker']//div[contains(@class,'tg-select-box__indicators')]")));
		yearDropdown.click();

		// 2. Scroll to and click the year (e.g., "1953")
		WebElement yearElement = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + year + "']")));
		js.executeScript("arguments[0].scrollIntoView(true);", yearElement);
		actions.moveToElement(yearElement).click().perform();

		// 3. Check if the desired month is visible, if not click next until it is
		boolean monthVisible = false;
		int maxTries = 12; // Avoid infinite loop

		for (int i = 0; i < maxTries; i++) {
			try {
				WebElement monthElement = driver.findElement(By.xpath("//span[text()='" + month + "']"));
				if (monthElement.isDisplayed()) {
					monthVisible = true;
					break;
				}
			} catch (Exception e) {
				// Month not found, click "Next"
				WebElement nextButton = driver.findElement(By.xpath("(//div[@class='custom-header']/button)[1]"));
				nextButton.click();
				Thread.sleep(400); // Small delay for calendar to update
			}
		}

		if (!monthVisible) {
			throw new RuntimeException("Month '" + month + "' was not found after navigating.");
		}

		// 4. Select the day (e.g., "16")
		WebElement dayElement = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + day + "']")));
		js.executeScript("arguments[0].scrollIntoView(true);", dayElement);
		actions.moveToElement(dayElement).click().perform();
	}

	// Method to Enter Adult Details.
	public void enterAdultDetailsForDomestic(String[] title, int adults, Log Log, ScreenShots ScreenShots)
			throws InterruptedException {
		try {
			TestExecutionNotifier.showExecutionPopup();

			if (adults > 1) {
				for (int i = 0; i < title.length; i++) {
					try {
						String firstName = generateRandomString(5);
						String lastName = generateRandomString(5);
						String first = "Appu" + firstName;
						String last = "Kumar" + lastName;

						String titleNames = title[i];
						int xpathIndex = i + 2;
						Thread.sleep(800);

						// Select Adult dropdown
//						try {
//							String xpath = "(//div[contains(@class,'tg-label_white tg-label_sm') and text()='Adult " + xpathIndex + " "+"']/parent::div/following-sibling::div)[2]/*";
//							WebElement element = driver.findElement(By.xpath(xpath));
//							WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//							element = wait.until(ExpectedConditions.elementToBeClickable(element));
//							((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
//							Thread.sleep(300);
//							Actions actions = new Actions(driver);
//							actions.moveToElement(element).click().perform();
//						} catch (Exception e) {
//							Log.ReportEvent("FAIL", "Failed to click Adult dropdown for index " + xpathIndex);
//							throw e;
//						}

						// Select Title
						try {
							WebElement titleDropDown = driver.findElement(By
									.xpath("(//span[text()='Title']/parent::div//input[@class='tg-select-box__input'])["
											+ xpathIndex + "]"));
							titleDropDown.click();
							WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
							WebElement option = wait.until(ExpectedConditions
									.elementToBeClickable(By.xpath("//span[text()='" + titleNames + "']")));
							((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
							Actions actions = new Actions(driver);
							actions.moveToElement(option).click().perform();
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to select Title '" + titleNames + "' for Adult " + (i + 1));
							throw e;
						}

						// Enter First Name and Last Name
						try {
							WebElement firstNameField = driver
									.findElement(By.xpath("(//input[@name='firstname'])[" + xpathIndex + "]"));
							firstNameField.clear();
							firstNameField.sendKeys(first);
							WebElement lastNameField = driver
									.findElement(By.xpath("(//input[@name='lastname'])[" + xpathIndex + "]"));
							lastNameField.clear();
							lastNameField.sendKeys(last);
						} catch (Exception e) {
							Log.ReportEvent("FAIL", "Failed to enter first or last name for Adult " + (i + 1));
							throw e;
						}
					} catch (Exception e) {
						// Already logged specific failure above, rethrow to outer catch
						throw e;
					}
				}

			} else {
				System.out.println("One Adult had been Selected");
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Enter Adult Details is UnSuccessful");
			e.printStackTrace();
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	public int selectAddOnsFlowForRTCombinedFts(Log Log, ScreenShots ScreenShots, String body4, String body5) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		int totalAddOnPrice = 0;

		List<WebElement> addOnsElements = driver.findElements(By.xpath("//*[text()='Select Add-Ons']"));

		for (int addonIndex = 0; addonIndex < addOnsElements.size(); addonIndex++) {
			try {
				WebElement addOnButton = wait
						.until(ExpectedConditions.elementToBeClickable(addOnsElements.get(addonIndex)));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						addOnButton);
				addOnButton.click();

				// Use body4 for Seats
				processTabForCombinedRT("Seat Selection", () -> selectFirstAvailableSeat(Log), Log, ScreenShots, body4);

				// Use body5 for everything else
				processTabForCombinedRT("Meal Preferences", () -> selectFirstAvailableMeal(), Log, ScreenShots, body5);
				processTabForCombinedRT("Baggage", () -> selectFirstAvailableBaggage(), Log, ScreenShots, body5);
				processTabForCombinedRT("Special Requests", () -> selectFirstAvailableSsr(), Log, ScreenShots, body5);

				totalAddOnPrice += sumAddOnPrices();
				clickConfirmButton();

				if (addonIndex + 1 < addOnsElements.size()) {
					addOnsElements = driver.findElements(By.xpath("//*[text()='Select Add-Ons']"));
				}
			} catch (Exception e) {
				Log.ReportEvent("FAIL", "Add-On processing failed: " + e.getMessage());
			}
		}
		return totalAddOnPrice;
	}

	private void processTabForCombinedRT(String tabName, Runnable selectionAction, Log Log, ScreenShots ScreenShots,
			String responseBody) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		Tripgain_HomePage_Flights homePage = new Tripgain_HomePage_Flights(driver);

		try {
			// 1. Click the main Tab (Seats, Meals, etc.)
			WebElement tab = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + tabName + "']")));
			tab.click();

			if (isNotFoundMessageDisplayed()) {
				Log.ReportEvent("INFO", tabName + " not available. Skipping...");
				return;
			}

			String sectorBaseXpath = "(//div[contains(@class,'special-service-request_sector-tabs')])[2]/div";
			List<WebElement> sectors = driver.findElements(By.xpath(sectorBaseXpath));

			for (int i = 1; i <= sectors.size(); i++) {
				// 2. Click the specific Sector
				WebElement sector = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("(" + sectorBaseXpath + ")[" + i + "]")));
				sector.click();

				// 3. DYNAMIC WAIT: Wait for the specific data cards to appear
				String itemCardXpath = tabName.equals("Seat Selection")
						? "//div[contains(@class,'flight-seat-map_seat')]"
						: "//div[contains(@class,'meal-selection_meal-tab_info')]";

				try {
					// We use a short wait here. If it fails, the sector is truly empty.
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(itemCardXpath)));

					List<WebElement> itemCards = driver.findElements(By.xpath(itemCardXpath));
					List<Map<String, String>> uiData = new ArrayList<>();

					// 4. Scrape & Validate
					if (tabName.equals("Seat Selection")) {
						uiData = homePage.getOnewayBookingpgAllSeatData(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateRTCombinedBookingPageSeatData(uiData, responseBody, Log, ScreenShots);
					} else if (tabName.equals("Meal Preferences")) {
						uiData = homePage.getOnewayBookingpgAllAvailableMeals(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageMealData(uiData, responseBody, Log, ScreenShots);
					} else if (tabName.equals("Baggage")) {
						uiData = homePage.getOnewayBookingpgAllAvailableBaggage(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageBaggageData(uiData, responseBody, Log, ScreenShots);
					} else if (tabName.equals("Special Requests")) {
						uiData = homePage.getOnewayBookingpgAllAvailableSpecialRequests(Log, ScreenShots, i);
						if (!uiData.isEmpty())
							homePage.validateOnewayBookingPageSpecialRequests(uiData, responseBody, Log, ScreenShots);
					}

					// 5. Select the first item
					// Log.ReportEvent("INFO", "Performing selection for " + tabName + " in Sector "
					// + i);
					selectionAction.run();

				} catch (Exception e) {
					// Log.ReportEvent("INFO", "No items found for " + tabName + " in Sector " + i +
					// " after waiting.");
				}
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", tabName + " processing failed: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}
	}

}
