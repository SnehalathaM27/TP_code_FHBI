package com.tripgain.collectionofpages;

import static org.testng.Assert.expectThrows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class NewDesign_Buses_ResultsPage {
	WebDriver driver;

	public NewDesign_Buses_ResultsPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to get the check in date text from result page
	public String[] getCheckInDateTextFromResultPage() {
		String checkIndate = driver.findElement(By.xpath(
				"(//div[contains(@class,' tg-typography tg-typography_subtitle-6 hotel-price fw-600 tg-typography_default')])[3]"))
				.getText();
		System.out.println("check in date text from Results Page: " + checkIndate);
		return new String[] { checkIndate };
	}

	// Method to select sort recommended dropdown
	public void selectSortOption(String sortOptionText, Log log) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement sortContainer = driver
					.findElement(By.xpath("(//div[contains(@class,'tg-select-box__indicators css-1wy0on6')])[1]"));

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortContainer);
			Thread.sleep(500); // Let it stabilize

			// Always reopen the dropdown before every selection
			try {
				sortContainer.click();
			} catch (ElementClickInterceptedException e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortContainer);
			}

			// Optional wait for dropdown to render
			Thread.sleep(500);

			// Now wait for the desired option and click
			WebElement desiredOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//span[@class='tg-select-option-label' and normalize-space(text())='" + sortOptionText + "']")));

			try {
				desiredOption.click();
			} catch (Exception e) {
				// Fallback to JS click in case of overlay
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", desiredOption);
			}

			System.out.println(" Selected sort option: " + sortOptionText);
			log.ReportEvent("PASS", "Selected sort option: " + sortOptionText);

		} catch (Exception e) {
			System.out.println(" Failed to select sort option: " + sortOptionText);
			log.ReportEvent("FAIL", "Failed to select sort option: " + sortOptionText);
			e.printStackTrace();
		}
	}

	// Method to valiadte price sort Low to high
	public void validatePricesLowToHigh(Log log) {
		List<WebElement> priceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-bus-fare')]"));

		List<Integer> prices = new ArrayList<>();

		for (WebElement el : priceElements) {
			String text = el.getText();
			String number = text.replaceAll("[^0-9]", ""); // Remove ₹ and commas
			if (!number.isEmpty()) {
				prices.add(Integer.parseInt(number));
			}
		}

		// Check if the list is sorted in ascending order
		for (int i = 0; i < prices.size() - 1; i++) {
			if (prices.get(i) > prices.get(i + 1)) {
				System.out.println("FAIL: Prices are not sorted in Low to High order.");
				System.out.println("Prices: " + prices);
				return;
			}
		}

		System.out.println("PASS: Prices are sorted in Low to High order.");
		log.ReportEvent("PASS", " Prices are sorted in Low to High order. ");

	}

	public void validatePricesHighToLow(Log log) {
		List<WebElement> priceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-bus-fare')]"));
		List<Integer> prices = new ArrayList<>();

		for (WebElement el : priceElements) {
			String text = el.getText();
			String number = text.replaceAll("[^0-9]", "");
			if (!number.isEmpty()) {
				try {
					prices.add(Integer.parseInt(number));
				} catch (NumberFormatException e) {
					System.out.println("⚠️ Skipping invalid price: " + number);
				}
			}
		}

		if (prices.isEmpty()) {
			System.out.println(" No prices found to validate.");
			log.ReportEvent("FAIL", "No prices found to validate High to Low order");
			return;
		}

		boolean sorted = true;
		for (int i = 0; i < prices.size() - 1; i++) {
			if (prices.get(i) < prices.get(i + 1)) {
				sorted = false;
				break;
			}
		}

		if (sorted) {
			System.out.println(" PASS: Prices are sorted in High to Low order.");
			log.ReportEvent("PASS", "Prices are sorted in High to Low order");
		} else {
			System.out.println(" FAIL: Prices are not sorted in High to Low order.");
			System.out.println("Prices: " + prices);
			log.ReportEvent("FAIL", "Prices are not sorted in High to Low order. Prices: " + prices);
		}
	}

	// Method to clcik on policy
	public void clickAndValidatePolicyDropdown(Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			// Step 1: Open the policy dropdown
			WebElement dropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[3]")));
			dropdown.click();

			// Step 2: Fetch all options and pick one randomly
			List<WebElement> options = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//span[@class='tg-select-option-label']")));

			List<String> validOptions = new ArrayList<>();
			for (WebElement option : options) {
				String optText = option.getText().trim();
				if (optText.equalsIgnoreCase("All") || optText.equalsIgnoreCase("In Policy")
						|| optText.equalsIgnoreCase("Out of Policy")) {
					validOptions.add(optText);
				}
			}

			if (validOptions.isEmpty()) {
				log.ReportEvent("FAIL", "No valid policy filter options found.");
				screenshots.takeScreenShot1();
				Assert.fail("No valid policy options available.");
				return;
			}

			// Randomly select one
			Random rand = new Random();
			String selectedOptionText = validOptions.get(rand.nextInt(validOptions.size()));
			WebElement selectedOptionElement = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//span[@class='tg-select-option-label' and normalize-space(text())='" + selectedOptionText
							+ "']")));
			selectedOptionElement.click();

			log.ReportEvent("INFO", "Randomly selected policy filter: " + selectedOptionText);

			// Step 3: Toggle the checkbox if applicable
			try {
				WebElement checkbox = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='tg-checkbox-box primary']")));
				if (!checkbox.isSelected()) {
					checkbox.click();
				}
			} catch (Exception e) {
				log.ReportEvent("WARN", "Policy checkbox not found or already selected.");
			}

			// Step 4: Wait for results to update
			Thread.sleep(1500); // Optional: Replace with proper loader wait if available

			// Step 5: Collect all policy labels from results
			List<WebElement> policyLabels = driver
					.findElements(By.xpath("//div[contains(@class,'tg-policy pointer')]"));

			if (policyLabels.isEmpty()) {
				log.ReportEvent("FAIL", "No policy labels found in the results.");
				screenshots.takeScreenShot1();
				Assert.fail("No policies found after applying filter.");
				return;
			}

			// Step 6: Validate policy labels
			boolean allMatch = true;
			List<String> mismatchedLabels = new ArrayList<>();

			for (WebElement el : policyLabels) {
				String resultText = el.getText().trim();
				if (selectedOptionText.equalsIgnoreCase("All")) {
					// For "All", allow both In Policy and Out of Policy
					if (!(resultText.equalsIgnoreCase("In Policy") || resultText.equalsIgnoreCase("Out of Policy"))) {
						allMatch = false;
						mismatchedLabels.add(resultText);
					}
				} else {
					// For In/Out of Policy, must match exactly
					if (!resultText.equalsIgnoreCase(selectedOptionText)) {
						allMatch = false;
						mismatchedLabels.add(resultText);
					}
				}
			}

			// Step 7: Final report
			if (allMatch) {
				log.ReportEvent("PASS", "All policy results match the selected option: " + selectedOptionText);
			} else {
				log.ReportEvent("FAIL", "Mismatch in policy results. Expected: " + selectedOptionText + " but found: "
						+ mismatchedLabels);
				screenshots.takeScreenShot1();
				Assert.fail("Policy filter validation failed.");
			}

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Exception in clickRandomAndValidatePolicyDropdown: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Exception occurred during policy filter validation.");
		}
	}

	// Method to clcik and validate price from dropdown
	public void filterByPriceAndValidate(Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			// Step 1: Click the dropdown
			WebElement dropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[2]")));
			dropdown.click();

			// Step 2: Fetch all filter options (except "All Prices")
			List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.xpath("//span[@class='tg-select-option-label' and not(contains(text(),'All Prices'))]")));

			if (options.isEmpty()) {
				log.ReportEvent("FAIL", "No price filter options found.");
				screenshots.takeScreenShot1();
				Assert.fail("No filter options to select.");
				return;
			}

			// Step 3: Select a random option
			Random rand = new Random();
			WebElement selectedOption = options.get(rand.nextInt(options.size()));
			String optionText = selectedOption.getText().trim();
			selectedOption.click();

			log.ReportEvent("INFO", "Selected price filter: " + optionText);

			// Step 4: Determine price range
			double min = 0, max = Double.MAX_VALUE;
			if (optionText.toLowerCase().contains("below")) {
				max = Double.parseDouble(optionText.replaceAll("[^0-9]", ""));
			} else if (optionText.toLowerCase().contains("above")) {
				min = Double.parseDouble(optionText.replaceAll("[^0-9]", ""));
			} else if (optionText.contains("-")) {
				String[] parts = optionText.replaceAll("[^0-9\\-]", "").split("-");
				if (parts.length == 2) {
					min = Double.parseDouble(parts[0].trim());
					max = Double.parseDouble(parts[1].trim());
				}
			}

			// Step 5: Wait for results to update
			Thread.sleep(1500); // Use better waits if needed

			// Step 6: Fetch all prices displayed
			List<WebElement> priceEls = driver.findElements(By.xpath("//*[contains(@class,'tg-bus-fare')]"));
			if (priceEls.isEmpty()) {
				log.ReportEvent("FAIL", "No prices found after filter applied.");
				screenshots.takeScreenShot1();
				Assert.fail("No prices displayed to validate.");
				return;
			}

			// Step 7: Validate each price
			boolean allValid = true;
			for (WebElement el : priceEls) {
				String raw = el.getText().replaceAll(",", "").trim();
				Matcher matcher = Pattern.compile("(\\d{2,6})").matcher(raw);
				if (matcher.find()) {
					double price = Double.parseDouble(matcher.group(1));
					if (price < min || price > max) {
						allValid = false;
						log.ReportEvent("FAIL", "Invalid price: ₹" + price + " not in range ₹" + min + " - ₹" + max);
						break;
					}
				} else {
					log.ReportEvent("WARN", "Unable to extract number from: " + raw);
				}
			}

			// Step 8: Final result
			if (allValid) {
				log.ReportEvent("PASS", "All prices are valid within range: " + optionText);
			} else {
				screenshots.takeScreenShot1();
				Assert.fail("Some prices are outside the selected range.");
			}

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Exception in filterRandomPriceAndValidate: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Exception occurred during validation.");
		}
	}

	// Method to clcik bus type
	public String clickAndValidateBusTypeOptions(Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // increased time

		try {
			// Open the "Bus Type" dropdown
			WebElement dropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[4]")));
			dropdown.click();
			log.ReportEvent("INFO", "Clicked Bus Type dropdown");

			// Wait for all options to be visible
			List<WebElement> options = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-select-option')]")));
			if (options.isEmpty()) {
				log.ReportEvent("FAIL", "No dropdown options found for 'Bus Type'");
				screenshots.takeScreenShot1();
				return "";
			}

			// Pick a random option and get its label text BEFORE clicking to avoid stale
			// element issues
			Random rand = new Random();
			WebElement chosenOption = options.get(rand.nextInt(options.size()));
			WebElement labelSpan = chosenOption.findElement(By.xpath(".//span[@class='tg-select-option-label']"));
			String clickedOptionText = labelSpan.getText().trim();

			// Scroll into view and click
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chosenOption);
			Thread.sleep(500); // brief wait for UI stability

			try {
				chosenOption.click();
			} catch (Exception e) {
				log.ReportEvent("WARN", "Normal click failed. Trying JS click for Bus Type option.");
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", chosenOption);
			}

			log.ReportEvent("INFO", "Clicked Bus Type option: '" + clickedOptionText + "'");

			// Wait for results to load - optionally replace with wait for loader disappear
			// if exists
			Thread.sleep(2000);

			// Fetch bus service name elements
			List<WebElement> serviceNameElements = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-buscard-servicename')]")));

			if (serviceNameElements.isEmpty()) {
				log.ReportEvent("FAIL", "After selecting '" + clickedOptionText + "', found no service name elements.");
				screenshots.takeScreenShot1();
				return clickedOptionText;
			}

			// Collect all service names to log and validate
			List<String> serviceNames = new ArrayList<>();
			for (WebElement service : serviceNameElements) {
				String sName = service.getText().trim();
				serviceNames.add(sName);
			}
			log.ReportEvent("INFO", "Service names present: " + serviceNames);

			// Validate: at least one service matches the selected Bus Type
			boolean matchFound = false;
			for (String sName : serviceNames) {
				if (clickedOptionText.equalsIgnoreCase("All Bus Types") || sName.equalsIgnoreCase(clickedOptionText)
						|| sName.toLowerCase().contains(clickedOptionText.toLowerCase())) {
					matchFound = true;
					break;
				}
			}

			if (matchFound) {
				log.ReportEvent("PASS",
						"At least one service matches the selected Bus Type: '" + clickedOptionText + "'");
			} else {
				log.ReportEvent("FAIL", "No service matches the selected Bus Type: '" + clickedOptionText
						+ "'. Found services: " + serviceNames);
				screenshots.takeScreenShot1();
			}

			return clickedOptionText;

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Error in clickAndValidateBusTypeOptions: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
			return "";
		}
	}

	// Method to clcik and validate seat type
	public void clickAndValidateSeatType(Log log, ScreenShots screenshots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			// 1. Open the dropdown
			WebElement dropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[5]")));
			dropdown.click();
			log.ReportEvent("INFO", "Clicked on 'Seat Type' dropdown");

			// 2. Get all dropdown options
			List<WebElement> options = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-select-option')]")));

			if (options.isEmpty()) {
				log.ReportEvent("FAIL", "No seat type options found.");
				screenshots.takeScreenShot1();
				return;
			}

			// 3. Pick a random option
			Random rand = new Random();
			WebElement chosenOption = options.get(rand.nextInt(options.size()));

			// Scroll into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chosenOption);
			Thread.sleep(500);

			// 4. Get option label
			WebElement labelSpan = chosenOption.findElement(By.xpath(".//span[@class='tg-select-option-label']"));
			String selectedSeatType = labelSpan.getText().trim();

			// 5. Click the entire option (instead of just checkbox)
			try {
				chosenOption.click();
			} catch (Exception e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", chosenOption);
			}

			log.ReportEvent("INFO", "Clicked seat type option: " + selectedSeatType);
			screenshots.takeScreenShot1();

			// 6. Validate based on selected type
			if ("All Seat Types".equalsIgnoreCase(selectedSeatType)) {
				log.ReportEvent("PASS", "All seat types selected. No specific validation needed.");
			} else {
				// Wait for services to refresh
				List<WebElement> serviceNameElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//div[contains(@class,'tg-buscard-servicename')]")));

				boolean matchFound = false;
				for (WebElement serviceNameElement : serviceNameElements) {
					String serviceName = serviceNameElement.getText().trim();
					if (serviceName.equalsIgnoreCase(selectedSeatType)) {
						matchFound = true;
						break;
					}
				}

				if (matchFound) {
					log.ReportEvent("PASS", selectedSeatType + " service found as expected.");
				} else {
					log.ReportEvent("FAIL", selectedSeatType + " service not found.");
					screenshots.takeScreenShot1();
				}
			}

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Error in clickAndValidateSeatType: " + e.getMessage());
			screenshots.takeScreenShot1();
		}
	}

	// Method to clcik and validate search operator

	public void clickAndValidateSearchOperator(Log log, ScreenShots screenshots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

			// 1) Open dropdown
			WebElement dropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//div[contains(@class,'tg-select-box__indicators')])[6]")));
			dropdown.click();
			log.ReportEvent("INFO", "Clicked on 'Search Operator' dropdown");

			// 2) Wait for dropdown options to load
			List<WebElement> options = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-select-option')]")));

			if (options.isEmpty()) {
				log.ReportEvent("FAIL", "No operator options found.");
				screenshots.takeScreenShot1();
				return;
			}

			// 3) Pick a random option
			Random rand = new Random();
			WebElement chosenOption = options.get(rand.nextInt(options.size()));

			// Scroll to view and click the full option
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chosenOption);
			Thread.sleep(500); // optional wait

			// 4) Get the option text (label inside option)
			WebElement labelSpan = chosenOption.findElement(By.xpath(".//span[@class='tg-select-option-label']"));
			String selectedOptionText = labelSpan.getText().trim();

			// 5) Click using fallback methods
			try {
				chosenOption.click(); // normal click
			} catch (Exception e) {
				log.ReportEvent("WARN", "Normal click failed, trying JavaScript click.");
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", chosenOption);
			}

			log.ReportEvent("INFO", "Selected operator option: " + selectedOptionText);

			// 6) Close the dropdown
			dropdown.click();

			// 7) Wait for results to refresh
			List<WebElement> displayedOperators = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-bsrs-operatorname')]")));

			List<String> displayedTexts = new ArrayList<>();
			for (WebElement op : displayedOperators) {
				displayedTexts.add(op.getText().trim());
			}

			// 8) Validate results
			if (selectedOptionText.equalsIgnoreCase("All Operators")) {
				if (displayedTexts.size() > 1) {
					log.ReportEvent("PASS", "'All Operators' selected - multiple operators are visible.");
				} else {
					log.ReportEvent("FAIL", "'All Operators' selected but only one or none are visible.");
					screenshots.takeScreenShot1();
				}
			} else {
				boolean allMatch = displayedTexts.stream().allMatch(text -> text.contains(selectedOptionText));
				if (allMatch) {
					log.ReportEvent("PASS", "All displayed operators match selected: " + selectedOptionText);
				} else {
					log.ReportEvent("FAIL", "Some displayed operators do not match selected: " + selectedOptionText
							+ ". Found: " + displayedTexts);
					screenshots.takeScreenShot1();
				}
			}

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Error during operator filter validation: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
		}
	}

	// Method to get and valiadte policy text
	public void getAndValidatePolicyText(Log log, ScreenShots screenshots) {
		try {
			// Find and click main policy label
			WebElement policyText = driver.findElement(By.xpath("//div[contains(@class,'tg-typography_subtitle-5')]"));
			String mainPolicyText = policyText.getText().trim();
			policyText.click();
			log.ReportEvent("INFO", "Clicked main policy text: " + mainPolicyText);

			// Clean the main policy text
			String cleanedMainText = cleanPolicyText(mainPolicyText);

			// Get all card-level policy labels
			List<WebElement> allPolicyTextElements = driver
					.findElements(By.xpath("//div[contains(@class,'tg-policy pointer')]"));

			if (allPolicyTextElements.isEmpty()) {
				log.ReportEvent("FAIL", "No policy text elements found.");
				screenshots.takeScreenShot1();
				return;
			}

			boolean allMatch = true;

			for (WebElement element : allPolicyTextElements) {
				String actualText = element.getText().trim();
				String cleanedActualText = cleanPolicyText(actualText);

				if (!cleanedActualText.equalsIgnoreCase(cleanedMainText)) {
					allMatch = false;
					log.ReportEvent("FAIL", "Policy text mismatch found. Main: '" + cleanedMainText + "' | Found: '"
							+ cleanedActualText + "'");
					screenshots.takeScreenShot1();
					break;
				}
			}

			if (allMatch) {
				log.ReportEvent("PASS", "All policy texts match: " + cleanedMainText);
			}

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Exception in getAndValidatePolicyText: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
		}
	}

	private String cleanPolicyText(String text) {
		if (text == null || text.isEmpty())
			return "";

		// Remove "Bus" (case-insensitive)
		String noBus = text.replaceAll("(?i)\\bbus\\b", "");

		// Replace hyphens with spaces to normalize both formats
		String noHyphens = noBus.replaceAll("-", " ");

		// Normalize spaces (trim and replace multiple spaces with single space)
		return noHyphens.trim().replaceAll("\\s+", " ");
	}

	// Method to select boarding points

	public String[] selectBoardingPoints(Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			driver.findElement(By.xpath("//button[text()='Boarding/Dropping Point']")).click();

			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[contains(@class,'tg-select-box__indicators css-1wy0on6')])[7]")));
			dropdown.click();

			WebElement listBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(@id,'react-select-') and contains(@id,'-listbox')]")));

			List<WebElement> boardingOptions = listBox
					.findElements(By.xpath(".//div[contains(@class,'tg-dropdown-menu-item')]"));

			if (boardingOptions.isEmpty()) {
				log.ReportEvent("FAIL", "No boarding options found in listbox");
				screenshots.takeScreenShot1();
				return null;
			}

			Random rand = new Random();
			int randomIndex = rand.nextInt(boardingOptions.size());
			WebElement optionToClick = boardingOptions.get(randomIndex);
			String location = "";
			String time = "";

			try {
				// get location text
				location = optionToClick
						.findElement(By.xpath(".//span[contains(@class,'tg-select-option-label')]//div//span[1]"))
						.getText().trim();
			} catch (Exception e) {
				log.ReportEvent("WARN", "Could not find location span for the option");
			}

			try {
				// get time text
				time = optionToClick
						.findElement(By.xpath(".//span[contains(@class,'tg-select-option-label')]//div//span[2]"))
						.getText().trim();
			} catch (Exception e) {
				log.ReportEvent("WARN", "Could not find time span for the option");
			}

			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						optionToClick);
				Thread.sleep(500); // small pause to ensure scroll completes
			} catch (Exception e) {

			}

			try {
				optionToClick.click();
			} catch (Exception e) {
				// fallback to JS click if normal click fails
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionToClick);
			}

			log.ReportEvent("PASS", "Selected boarding point: " + location + ", time: " + time);

			return new String[] { location, time };

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Error selecting boarding point: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
			return null;
		}
	}

	// Method for select dropping point
	public String[] selectDroppingPoint(Log log, ScreenShots screenshots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			// 1) Open the "Select dropping point" dropdown
			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[contains(@class,'tg-select-box__indicators css-1wy0on6')])[8]")));
			dropdown.click();
			log.ReportEvent("INFO", "Clicked on 'Select dropping point' dropdown");

			// 2) Wait for the listbox to be visible - generalized XPath based on pattern
			WebElement listBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(@id,'react-select-') and contains(@id,'-listbox')]")));

			// 3) Find all dropping options inside the listbox
			List<WebElement> droppingOptions = listBox
					.findElements(By.xpath(".//div[contains(@class,'tg-dropdown-menu-item')]"));

			if (droppingOptions.isEmpty()) {
				log.ReportEvent("FAIL", "No dropping options found in listbox");
				screenshots.takeScreenShot1();
				return null;
			}

			// 4) Pick a random option
			Random rand = new Random();
			int randomIndex = rand.nextInt(droppingOptions.size());
			WebElement optionToClick = droppingOptions.get(randomIndex);

			// 5) Extract location and time based on inner div structure
			String location = "";
			String time = "";

			try {
				location = optionToClick.findElement(By.xpath(".//div[contains(@class,'d-flex')]/span[1]")).getText()
						.trim();
			} catch (Exception e) {
				log.ReportEvent("WARN", "Could not find location span for the option");
			}

			try {
				time = optionToClick.findElement(By.xpath(".//div[contains(@class,'d-flex')]/span[2]")).getText()
						.trim();
			} catch (Exception e) {
				log.ReportEvent("WARN", "Could not find time span for the option");
			}

			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						optionToClick);
				Thread.sleep(500); // small pause to ensure scroll completes
			} catch (Exception e) {
				// log.ReportEvent("WARN", "Scroll to boarding point failed: " +
				// e.getMessage());
			}
			try {
				optionToClick.click();
			} catch (Exception e) {
				// fallback to JS click if normal click fails
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionToClick);
			}

			log.ReportEvent("PASS", "Selected dropping point: " + location + ", time: " + time);

			return new String[] { location, time };

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Error selecting dropping point: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
			return null;
		}
	}

	// Method to pick seat
	public String[] clcikLowerBirth(Log Log, ScreenShots screenshots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Wait for the "Lower Berth" element to be clickable
			WebElement lowerBerth = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space(text())='Lower Berth']")));

			System.out.println("Attempting to click Lower Berth...");
			lowerBerth.click();
			System.out.println("Clicked Lower Berth.");

			WebElement seatContainer = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-bsrs-seatavailable')]")));

			List<WebElement> availableSeats = driver
					.findElements(By.xpath("//div[contains(@class,'tg-bsrs-seatavailable')]"));

			if (availableSeats.isEmpty()) {
				throw new RuntimeException("No available seats found in Lower Berth.");
			}

			int randomIndex = new Random().nextInt(availableSeats.size());
			WebElement selectedSeat = availableSeats.get(randomIndex);
			selectedSeat.click();

			WebElement selectedSeatInfo = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[contains(@class,'tg-bus-selected-seat-price')]")));

			String fullText = selectedSeatInfo.getText().trim();

			if (!fullText.contains(" - ₹")) {
				throw new RuntimeException("Unexpected seat info format: " + fullText);
			}

			String[] seatData = fullText.split(" - ₹");
			String seatNumber = seatData[0].trim();
			String seatPrice = "₹" + seatData[1].trim();

			Log.ReportEvent("PASS", "Selected Seat: " + seatNumber + ", Price: " + seatPrice);

			return new String[] { seatNumber, seatPrice };

		} catch (Exception e) {
			Log.ReportEvent("ERROR", "Error selecting Lower Berth seat: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
			return null;
		}
	}

	public String[] clickUpperBerth(Log Log, ScreenShots screenshots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Step 1: Click on "Upper Berth"
			driver.findElement(By.xpath("//div[text()=' Upper Berth']")).click();

			// Step 2: Wait for upper berth seat layout
			List<WebElement> availableSeats = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-bsrs-seatavailable')]")));

			if (availableSeats.isEmpty()) {
				throw new RuntimeException("No available seats found in Upper Berth.");
			}

			// Step 3: Click a random seat
			int randomIndex = new Random().nextInt(availableSeats.size());
			WebElement selectedSeat = availableSeats.get(randomIndex);
			selectedSeat.click();

			// Step 4: Wait and fetch seat + price text from 3rd matching element
			WebElement seatInfoElement = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[contains(@class,'tg-bus-selected-seat-price')]")));

			String fullText = seatInfoElement.getText().trim(); // e.g., "10 - ₹ 734"

			// Step 5: Split into seat number and price
			String[] parts = fullText.split(" - ₹");
			String seatNumber = parts[0].trim();
			String seatPrice = "₹" + parts[1].trim();

			Log.ReportEvent("PASS", "Selected Upper Berth Seat: " + seatNumber + ", Price: " + seatPrice);
			// screenshots.takeScreenShot1();

			return new String[] { seatNumber, seatPrice };

		} catch (Exception e) {
			Log.ReportEvent("ERROR", "Error selecting Upper Berth seat: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
			return null;
		}
	}

	public String[] selectAvailableBerth(Log Log, ScreenShots screenshots) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        String[] seatData = null;

	        // Step 1: Try to click Lower Berth first
	        try {
	            System.out.println("Checking for Lower Berth...");
	            WebElement lowerBerth = wait.until(
	                    ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space(text())='Lower Berth']")));
	            
	            // If found, click it and run your Lower Berth seat picking logic
	            lowerBerth.click();
	            System.out.println("Lower Berth clicked. Selecting seat...");
	            seatData = pickRandomSeatFromLayout(Log, screenshots, "Lower Berth");
	            
	        } catch (Exception e) {
	            System.out.println("Lower Berth not found or not clickable. Switching to Upper Berth...");
	            
	            // Step 2: Fallback to Upper Berth
	            WebElement upperBerth = wait.until(
	                    ExpectedConditions.elementToBeClickable(By.xpath("//div[text()=' Upper Berth']")));
	            
	            upperBerth.click();
	            System.out.println("Upper Berth clicked. Selecting seat...");
	            seatData = pickRandomSeatFromLayout(Log, screenshots, "Upper Berth");
	        }

	        return seatData;

	    } catch (Exception e) {
	        Log.ReportEvent("ERROR", "No berths (Lower or Upper) could be selected: " + e.getMessage());
	        screenshots.takeScreenShot1();
	        return null;
	    }
	}

	// Helper method to pick the seat once the Berth is clicked
	private String[] pickRandomSeatFromLayout(Log Log, ScreenShots screenshots, String berthType) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    
	    // Wait for seat layout
	    List<WebElement> availableSeats = wait.until(ExpectedConditions
	            .visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'tg-bsrs-seatavailable')]")));

	    if (availableSeats.isEmpty()) {
	        throw new RuntimeException("No available seats found in " + berthType);
	    }

	    // Click a random seat
	    int randomIndex = new java.util.Random().nextInt(availableSeats.size());
	    WebElement selectedSeat = availableSeats.get(randomIndex);
	    selectedSeat.click();

	    // Fetch price info
	    WebElement seatInfoElement = wait.until(ExpectedConditions
	            .visibilityOfElementLocated(By.xpath("//*[contains(@class,'tg-bus-selected-seat-price')]")));

	    String fullText = seatInfoElement.getText().trim(); 
	    String[] parts = fullText.split(" - ₹");
	    String seatNumber = parts[0].trim();
	    String seatPrice = "₹" + parts[1].trim();

	    Log.ReportEvent("PASS", "Selected " + berthType + " Seat: " + seatNumber + ", Price: " + seatPrice);
	    return new String[] { seatNumber, seatPrice };
	}
	// Method to click on price after selecting seat

	public String getPriceAfterSeatSelection(Log Log, ScreenShots screenshots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			WebElement priceElement = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//h6[contains(@class,'tg-bsrs-totalseatprice')]")));

			String priceText = priceElement.getText().trim();

			Log.ReportEvent("PASS", "Total seat price after selection: " + priceText);
			// screenshots.takeScreenShot1();

			return priceText;

		} catch (Exception e) {
			Log.ReportEvent("ERROR", "Failed to get price after seat selection: " + e.getMessage());
			screenshots.takeScreenShot1();
			e.printStackTrace();
			return null;
		}
	}

	// Method to clcik on confirm seat
	public void clcikOnConfirmSeat() {
		driver.findElement(By.xpath("//button[text()='Confirm Seat']")).click();
	}

	// Method to close reason For Selection PopUp
	public void reasonForSelectionPopUp() throws InterruptedException, TimeoutException {
		String value = "Personal Preference";

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
		Thread.sleep(8000);
		WebElement popup = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@id='alert-dialog-title']")));

		if (popup.isDisplayed()) {
			WebElement reasonOption = driver.findElement(By.xpath("//span[text()='" + value + "']//parent::label"));
			reasonOption.click();
			// click on Proceed to Booking
			driver.findElement(By.xpath("//button[text()='Proceed to Booking']")).click();
			System.out.println("Clicked on reason for selection popup");
			Thread.sleep(3000);

		}
	}

	public String[] getBusDetailsFromListingByIndex(int index) {
		String[] busDetails = new String[7];

		try {
			List<WebElement> busCards = driver.findElements(By.xpath("//div[contains(@class,'buscard-root')]"));

			if (index < 0 || index >= busCards.size()) {
				throw new IllegalArgumentException("Index out of range. Available bus cards: " + busCards.size());
			}

			WebElement selectedBusCard = busCards.get(index);

			// Click the Select button inside the selected bus card
			WebElement selectButton = selectedBusCard.findElement(By.xpath(".//span[text()='Select']"));
			selectButton.click();

			// Extract required fields as Strings
			String departureTime = selectedBusCard.findElement(By.xpath(".//div[contains(@class,' tg-bus-deptime')]"))
					.getText().trim();
			String arrivalTime = selectedBusCard.findElement(By.xpath(".//div[contains(@class,'tg-bsrs-arrtime')]"))
					.getText().trim();
			String duration = selectedBusCard.findElement(By.xpath(".//div[contains(@class,'tg-bus-duration')]"))
					.getText().trim();
			String busName = selectedBusCard.findElement(By.xpath(".//div[contains(@class,'tg-bsrs-operatorname')]"))
					.getText().trim();
			String serviceName = selectedBusCard
					.findElement(By.xpath(".//div[contains(@class,'tg-buscard-servicename')]")).getText().trim();
			String amount = selectedBusCard.findElement(By.xpath(".//div[contains(@class,'tg-bus-fare')]")).getText()
					.trim();
			String policy = selectedBusCard.findElement(By.xpath(".//*[contains(@class,'tg-policy')]")).getText()
					.trim();

			// Store values in array
			busDetails = new String[] { departureTime, arrivalTime, duration, busName, serviceName, amount, policy };

		} catch (Exception e) {
			// Optional: Log or handle exception
			e.printStackTrace();
			throw e;
		}

		return busDetails;
	}

	public String[] getDestFromresultPg() {
		String Dest = driver.findElement(By.xpath("(//*[contains(@class,'hotel-price ')])[2]")).getText();
		System.out.println("Dest from Results Page: " + Dest);

		return new String[] { Dest };
	}

	public String[] getOriginFromresultPg() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement originElement = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'hotel-price')])[1]")));

		String origin = originElement.getText().trim();
		System.out.println("Origin from Results Page: " + origin);

		return new String[] { origin };
	}

	public String[] getSeatTypeTextFromresultPgAfterSelect() {
		String seatTypeText = driver.findElement(By.xpath(
				"//div[contains(@class,' tg-typography tg-typography_subtitle-8 fw-400 label-color tg-typography_default')]"))
				.getText();
		System.out.println("Seat type text from Results Page: " + seatTypeText);

		return new String[] { seatTypeText };
	}

	public void clcikOnAddTripAndContinueForBuses() {
		driver.findElement(By.xpath("//button[text()='Add to Trip and Continue']")).click();
	}

}
