

package com.tripgain.collectionofpages;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
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


public class Tripgain_TripPlannerCreateTripPage_Flights {
	WebDriver driver;

	public Tripgain_TripPlannerCreateTripPage_Flights(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	//Method to Verify create Trip Page is Displayed
	public void verifyCreateTripPageIsDisplayed(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			WebElement homePageLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Create Your Trip']")));
			TestExecutionNotifier.showExecutionPopup(); // ADD THIS LINE
			if (homePageLogo.isDisplayed()) {
				Log.ReportEvent("PASS", "Create Trip Form is loaded Successful");
				Thread.sleep(5000);
			} else {
				Log.ReportEvent("FAIL", "Create Trip Form is not loaded");
				ScreenShots.takeScreenShot();
				Assert.fail();
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Create Trip Form is not loaded");
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail();

		}
	}

	//Method to Select Date By Passing Two Paramenters(Date and MounthYear)
	public void selectDate(String day, String MonthAndYear) throws InterruptedException {
		TestExecutionNotifier.showExecutionPopup();
		driver.findElement(By.xpath("(//input[@class='custom_datepicker_input'])[1]")).click();
		Thread.sleep(4000);
		String Date = driver.findElement(By.xpath("((//div[@class='custom-header'])[1]//span)[1]")).getText();
		if (Date.contentEquals(MonthAndYear)) {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day + "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();
			Thread.sleep(4000);

		} else {
			while (!Date.contentEquals(MonthAndYear)) {
				Thread.sleep(500);
				driver.findElement(By.xpath("(//button[contains(@class, 'nav-arrow')])[2]")).click();
				if (driver.findElement(By.xpath("((//div[@class='custom-header'])[1]//span)[1]")).getText().contentEquals(MonthAndYear)) {
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day + "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();
					break;
				}

			}
		}
	}

	//Method to Select Return Date By Passing Two Paramenters(Date and MounthYear)
	public void selectReturnDate(String day, String MonthAndYear) throws InterruptedException {
		TestExecutionNotifier.showExecutionPopup();
		driver.findElement(By.xpath("(//input[@class='custom_datepicker_input'])[2]")).click();
		Thread.sleep(4000);
		String Date = driver.findElement(By.xpath("((//div[@class='custom-header'])[1]//span)[1]")).getText();
		if (Date.contentEquals(MonthAndYear)) {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day + "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();
			Thread.sleep(4000);

		} else {
			while (!Date.contentEquals(MonthAndYear)) {
				Thread.sleep(500);
				driver.findElement(By.xpath("(//button[contains(@class, 'nav-arrow')])[2]")).click();
				if (driver.findElement(By.xpath("((//div[@class='custom-header'])[1]//span)[1]")).getText().contentEquals(MonthAndYear)) {
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[1]//div//div//span[text()='" + day + "']//parent::div//parent::div[@role='option' and not(@aria-disabled='true')]")).click();
					break;
				}

			}
		}
	}


	// Method to Click on Travel Icon
	public void clickOnTripsIcon() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Wait until the "Travel" icon is visible and clickable
		WebElement travelIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Trips']")));

		travelIcon.click();
	}

	// Method to Click on Travel Icon
	public void clickOnCreateTrip() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Wait until the "Travel" icon is visible and clickable
		WebElement travelIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Create Trip']")));

		travelIcon.click();
	}

   //Method to enter trip name.
	public void enterTripName(String tripName) {
		WebElement tripNameField = driver.findElement(By.xpath("//input[@name='tripname']"));
		tripNameField.clear(); // optional - clears any existing text
		tripNameField.sendKeys(tripName); // enters the provided value
	}


	// Method to select 'From' location by entering and selecting the first suggestion
	public void selectFromLocation(String location) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Step 1: Find and click the 'From' input
		WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='origin']")));
		fromInput.click();
		fromInput.clear();
		fromInput.sendKeys(location);

		// Step 2: Wait for at least one suggestion to be visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//div[contains(@role, 'option')]")));

		// Step 3: Get all available suggestions
		List<WebElement> suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//div[contains(@role, 'option')]")));

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
	public void selectToLocation(String location) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Step 1: Find and click the 'From' input
		WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@class='tg-async-select__input'])[2]")));
		fromInput.click();
		fromInput.clear();
		fromInput.sendKeys(location);

		// Step 2: Wait for at least one suggestion to be visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//div[contains(@role, 'option')]")));

		// Step 3: Get all available suggestions
		List<WebElement> suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//div[contains(@role, 'option')]")));

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

    //Method to select Services
	public void selectServices(String... serviceOptions) {
		for(String serviceOption: serviceOptions)
		{
			WebElement flightTab = driver.findElement(By.xpath("//span[text()='"+serviceOption+"']"));
			flightTab.click();
		}

	}

	// Method to click on Create Trip button and handle error message if displayed
	public void clickCreateTripAndCaptureError(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Wait for and click the "Create Trip" button
			WebElement createTripButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Create  Trip']"))
			);
			createTripButton.click();

			// Wait briefly to see if an error message (toast) appears
			try {
				WebElement errorToast = wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='toast-title']"))
				);

				if (errorToast.isDisplayed()) {
					String errorMessage = errorToast.getText().trim();
					Log.ReportEvent("FAIL", "Error message displayed after clicking 'Create Trip': " + errorMessage);
					ScreenShots.takeScreenShot();
					System.out.println("❗ Error message displayed: " + errorMessage);
					Assert.fail("Error message displayed: " + errorMessage);
				}

			} catch (TimeoutException te) {
				Log.ReportEvent("PASS", "'Create Trip' button clicked successfully — no error message displayed.");
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while clicking 'Create Trip': " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred while clicking 'Create Trip'.");
		}
	}

	// Method to verify Trip Created Successfully message is displayed
	public void verifyTripCreatedSuccessfully(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Wait for the success message to be visible
			WebElement successMessage = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Trip Created Successfully!']"))
			);

			if (successMessage.isDisplayed()) {
				String messageText = successMessage.getText().trim();
				Log.ReportEvent("PASS", "Trip creation successful. Message displayed: " + messageText);
				System.out.println("✅ Trip created successfully: " + messageText);
			} else {
				Log.ReportEvent("FAIL", "Trip creation success message not displayed.");
				ScreenShots.takeScreenShot();
				Assert.fail("Trip creation success message not displayed.");
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "Trip creation success message not found within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("Trip creation success message not found within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception while verifying trip creation success message: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception while verifying trip creation success message.");
		}
	}

	public String getTripId(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		String tripId = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Locate the element containing the full message
			WebElement tripInfo = wait.until(
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[text()='Trip Created Successfully!']/parent::div//div[contains(@class,'tg-typography_text-info')]")
					)
			);

			// Get the text, e.g., "Your Trip ID is TGTR20252710RY8SLN"
			String fullText = tripInfo.getText().trim();
			System.out.println("Full message text: " + fullText);

			// Extract only the Trip ID using regex
			if (fullText.contains("Your Trip ID is")) {
				tripId = fullText.replace("Your Trip ID is", "").trim();
			} else {
				// fallback regex if format changes slightly
				java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([A-Z0-9]{10,})").matcher(fullText);
				if (matcher.find()) {
					tripId = matcher.group(1);
				}
			}

			// Log and take screenshot
			if (!tripId.isEmpty()) {
				Log.ReportEvent("PASS", "Trip ID extracted successfully: " + tripId);
				System.out.println("✅ Trip ID: " + tripId);
			} else {
				Log.ReportEvent("FAIL", "Trip ID not found in message: " + fullText);
				ScreenShots.takeScreenShot();
				Assert.fail("Trip ID not found.");
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "Trip ID message not displayed within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("Trip ID message not displayed within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception while extracting Trip ID: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception while extracting Trip ID.");
		}

		return tripId;
	}

	// Method to click on Continue to Add Services and validate Trip ID
	public void validateTripIdAfterContinue(String expectedTripId, Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Step 1: Wait for and click "Continue to Add Services" button
			WebElement continueButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Continue to Add Services']"))
			);
			continueButton.click();
			Log.ReportEvent("INFO", "Clicked on 'Continue to Add Services' button.");

			// Step 2: Wait for Trip ID to appear on next page/section
			WebElement tripIdElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//span[text()='Trip ID ']/parent::div//div[contains(@class,'tg-typography_text-info')]")
					)
			);

			// Step 3: Extract Trip ID text (e.g., "TGTR20252710RY8SLN")
			String actualTripId = tripIdElement.getText().trim();
			System.out.println("Fetched Trip ID after Continue: " + actualTripId);

			// Step 4: Validate both IDs match
			if (actualTripId.equalsIgnoreCase(expectedTripId)) {
				Log.ReportEvent("PASS", "Trip ID validation successful. Expected: " + expectedTripId + ", Actual: " + actualTripId);
				System.out.println("✅ Trip ID validation successful.");
			} else {
				Log.ReportEvent("FAIL", "Trip ID mismatch! Expected: " + expectedTripId + ", Actual: " + actualTripId);
				ScreenShots.takeScreenShot();
				Assert.fail("Trip ID mismatch! Expected: " + expectedTripId + ", Actual: " + actualTripId);
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "Trip ID not found after clicking 'Continue to Add Services' within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("Trip ID not found within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while validating Trip ID: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred while validating Trip ID.");
		}
	}
	public String[] getInputValues() {
		String[] inputValues = new String[3]; // Array to hold 3 input values

		try {
			// 1st datepicker input
			WebElement dateInput1 = driver.findElement(By.xpath("(//input[@class='custom_datepicker_input'])[1]"));
			inputValues[0] = dateInput1.getAttribute("value").trim();

			// 2nd datepicker input
			WebElement dateInput2 = driver.findElement(By.xpath("(//input[@class='custom_datepicker_input'])[2]"));
			inputValues[1] = dateInput2.getAttribute("value").trim();

			// tripname input
			WebElement tripNameInput = driver.findElement(By.xpath("//input[@name='tripname']"));
			inputValues[2] = tripNameInput.getAttribute("value").trim();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while fetching input values: " + e.getMessage());
		}

		return inputValues;
	}

	public void validateFields(String expectedStartDate, String expectedEndDate, String expectedValue, Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// 1st input date
			WebElement startDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("(//input[@class='custom_datepicker_input'])[1]")));
			String actualStartDate = startDateInput.getAttribute("value").trim();

			// 2nd input date
			WebElement endDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("(//input[@class='custom_datepicker_input'])[2]")));
			String actualEndDate = endDateInput.getAttribute("value").trim();

			// 3rd div text
			WebElement thirdDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("(//div[contains(@class,'tg-start')])[4]/child::div[contains(@class,'tg-typography_default')]")));
			String actualValue = thirdDiv.getText().trim();

			// Validate all three fields
			boolean allMatch = true;

			if (actualStartDate.equals(expectedStartDate)) {
				Log.ReportEvent("PASS", "Start date validated successfully: " + actualStartDate);
			} else {
				Log.ReportEvent("FAIL", "Start date mismatch! Expected: " + expectedStartDate + ", Actual: " + actualStartDate);
				allMatch = false;
			}

			if (actualEndDate.equals(expectedEndDate)) {
				Log.ReportEvent("PASS", "End date validated successfully: " + actualEndDate);
			} else {
				Log.ReportEvent("FAIL", "End date mismatch! Expected: " + expectedEndDate + ", Actual: " + actualEndDate);
				allMatch = false;
			}

			if (actualValue.equals(expectedValue)) {
				Log.ReportEvent("PASS", "Trip Name validated successfully: " + actualValue);
			} else {
				Log.ReportEvent("FAIL", "Trip Name mismatch! Expected: " + expectedValue + ", Actual: " + actualValue);
				allMatch = false;
			}

			if (!allMatch) {
				Assert.fail("One or more field validations failed. Check logs for details.");
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "One or more elements not found within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("Elements not found within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred during field validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred during field validation.");
		}
	}

	// Method to click the button and validate success message
	public void clickButtonAndValidateMessage(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Step 1: Wait for and click the button
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//button[contains(@class,'tg-icon-btn_md')])[4]")));
			button.click();
			Log.ReportEvent("INFO", "Clicked the Plus icon button.");

			// Step 2: Wait for the success message to appear
			WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//p[text()='Request Item added successfully to Trip.']")));
			if (successMsg.isDisplayed()) {
				String message = successMsg.getText().trim();
				Log.ReportEvent("PASS", "Success message displayed: " + message);
				System.out.println("✅ Success message displayed: " + message);
			} else {
				Log.ReportEvent("FAIL", "Success message not displayed after clicking the button.");
				ScreenShots.takeScreenShot();
				Assert.fail("Success message not displayed after clicking the button.");
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "Success message not found within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("Success message not found within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while validating success message: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred while validating success message.");
		}
	}

	// Method to click on "Search" button/span
	public void clickSearch(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Wait for the "Search" element to be clickable
			WebElement searchButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Search']"))
			);
			searchButton.click();

			Log.ReportEvent("PASS", "'Search' button clicked successfully.");
			System.out.println("✅ Clicked on 'Search'.");

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "'Search' button not found or not clickable within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("'Search' button not found or not clickable within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while clicking 'Search': " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred while clicking 'Search'.");
		}
	}

	// Method to wait until the flight one-way content tag is displayed
	public void waitForFlightTag(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			WebElement flightTag = wait.until(
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[@class='flight-oneway__content_tag']")
					)
			);

			if (flightTag.isDisplayed()) {
				Log.ReportEvent("PASS", "'Flight loaded Successfully");
				System.out.println("✅ Flight one-way content tag is displayed.");
			} else {
				Log.ReportEvent("FAIL", "'Flight are Not Loaded");
				ScreenShots.takeScreenShot();
				Assert.fail("'Flight One-way Content Tag' is not displayed.");
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "'Flight was not displayed within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("'Flight was not displayed within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while waiting for flight tag: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred while waiting for flight tag.");
		}
	}

	public void waitForFlightTagForInternational(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			WebElement flightTag = wait.until(
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("(//div[contains(@class,'flight-oneway__card')])[1]")
					)
			);

			if (flightTag.isDisplayed()) {
				Log.ReportEvent("PASS", "'Flight loaded Successfully");
				System.out.println("✅ Flight one-way content tag is displayed.");
			} else {
				Log.ReportEvent("FAIL", "'Flight are Not Loaded");
				ScreenShots.takeScreenShot();
				Assert.fail("'Flight One-way Content Tag' is not displayed.");
			}

		} catch (TimeoutException te) {
			Log.ReportEvent("FAIL", "'Flight was not displayed within timeout.");
			ScreenShots.takeScreenShot();
			Assert.fail("'Flight was not displayed within timeout.");
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while waiting for flight tag: " + e.getMessage());
			ScreenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Exception occurred while waiting for flight tag.");
		}
	}

	
	public void clcikonAddTripToContinueBtn() {
		driver.findElement(By.xpath("//button[text()='Add to Trip and Continue']")).click();
	}


}


