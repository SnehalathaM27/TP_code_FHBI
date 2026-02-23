package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class NewDesign_Hotels_ResultsPage {
	WebDriver driver;

	public NewDesign_Hotels_ResultsPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to get the check in date text from result page
	/*
	 * public String[] getCheckInDateTextFromResultPage() { WebElement dateElement =
	 * driver.findElement(By.
	 * xpath("(//div[contains(@class,' tg-typography tg-typography_subtitle-6 ms-1 fw-600 tg-typography_default')])[2]"
	 * )); String fullText = dateElement.getText(); // e.g.
	 * "20th Sep, 2025 - 25th Sep, 2025" System.out.println("Full date text: " +
	 * fullText);
	 * 
	 * // Split to get the check-in part String checkIn = fullText.split(" - ")[0];
	 * // "20th Sep, 2025"
	 * System.out.println("Check-in part before removing suffix: " + checkIn);
	 * 
	 * // Remove ordinal suffixes like 'th', 'st', etc. checkIn =
	 * checkIn.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
	 * System.out.println("Check-in part after removing suffix: " + checkIn);
	 * 
	 * // Split into parts: [day, month, year] String[] parts =
	 * checkIn.trim().split(" "); // ["20", "Sep,", "2025"] for (String part :
	 * parts) { System.out.println("checkin dates"+ part); }
	 * 
	 * return parts; }
	 */

	public String[] getCheckInDateTextFromResultPage() {

	    WebElement dateElement = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions
	            .visibilityOfElementLocated(By.xpath("(//div[contains(@class,'search-header-date-width')])[1]")));

	    String fullText = dateElement.getAttribute("innerText").trim();
	    System.out.println("Full raw text: " + fullText);

	    // Split for any dash type
	    String[] dateRange = fullText.split("\\s[-–—]\\s");
	    if (dateRange.length < 1) {
	        throw new RuntimeException("Date format not recognized: " + fullText);
	    }

	    String checkIn = dateRange[0]; // left side
	    System.out.println("Check-in before suffix removal: " + checkIn);

	    // Remove ordinal suffixes like 'th', 'st', etc.
	    checkIn = checkIn.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
	    // FIX: Remove comma from month
	    checkIn = checkIn.replace(",", "");
	    System.out.println("Check-in after suffix removal: " + checkIn);

	    String[] parts = checkIn.trim().split("\\s+");
	    for (String p : parts) {
	        System.out.println("Part: " + p);
	    }

	    return parts;
	}

	public String[] getCheckOutDateTextFromResultPage() {
	    WebElement dateElement = driver
	            .findElement(By.xpath("(//div[contains(@class,'search-header-date-width')])[1]"));
	    String fullText = dateElement.getText(); // e.g. "20th Sep, 2025 - 25th Sep, 2025"
	    System.out.println("Full date text: " + fullText);

	    // Split to get the check-out part
	    String checkOut = fullText.split(" - ")[1]; // "25th Sep, 2025"
	    System.out.println("Check-out part before removing suffix: " + checkOut);

	    // Remove ordinal suffixes like 'th', 'st', etc.
	    checkOut = checkOut.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
	    // FIX: Remove comma from month
	    checkOut = checkOut.replace(",", "");
	    System.out.println("Check-out part after removing suffix: " + checkOut);

	    // Split into parts: [day, month, year]
	    String[] parts = checkOut.trim().split(" "); // ["25", "Sep", "2025"]

	    System.out.println("Split parts:");
	    for (String part : parts) {
	        System.out.println(part);
	    }

	    return parts;
	}
	
	
	public int getHotelCount(WebDriver driver, Log Log) {
		// Get the text from the paragraph
		String text = driver.findElement(By.xpath("//p[contains(text(),'We have found')]")).getText();

		int count = Integer.parseInt(text.replaceAll("\\D+", ""));

		Log.ReportEvent("INFO", "Total Hotels Found Count: " + count);

		return count;
	}

	// Method to get the room and guests text from result page

	public String[] getRoomAndGuestTextFromResultPage() {
		WebElement countElement = driver
				.findElement(By.xpath("(//div[contains(@class,'search-header-date-width')])[2]"));

		// Get full text like: "1 Room , 0 Adult , 0 Child"
		String fullText = countElement.getText();

		// Split by comma to get the individual counts
		String[] parts = fullText.split(",");

		// Trim whitespace from each part
		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].trim(); // Removes leading/trailing spaces
		}
		return parts; // parts[0] = "1 Room", parts[1] = "0 Adult", parts[2] = "0 Child"
	}

	// Method to click on user rating
	public String clickUserRating(String userText, Log Log, ScreenShots ScreenShots) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			driver.findElement(By.xpath("//span[text()='User Rating']")).click();

			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[@class='filter-section filter-section-active']")));
			WebElement ratingList = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='user-rating-list']")));

			WebElement ratingLabel = ratingList
					.findElement(By.xpath(".//span[@class='rating-label' and text()='" + userText + "']"));

			WebElement parentElement = ratingLabel.findElement(By.xpath("./..")); // get parent container

			WebElement checkbox = parentElement
					.findElement(By.xpath(".//span[contains(@class,'tg-checkbox-box primary')]"));

			checkbox.click();

			Log.ReportEvent("PASS", "Clicked on User Rating checkbox: " + userText);
		} catch (Exception e) {
			Log.ReportEvent("FAIL",
					"Failed to click on User Rating checkbox: " + userText + " | Exception: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			throw e;
		}

		return userText;
	}

	// Method to close the button in filters
	public void clickCloseBtnInFilters() {
		driver.findElement(By.xpath("//button[text()='Close']")).click();
	}

	// Method to click per night price
	public String clickPerNightPrice(int count, Log Log) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		driver.findElement(By.xpath("//span[text()='Per Night Price']")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='price-bucket-list']")));

		List<WebElement> priceOptions = driver
				.findElements(By.xpath("//div[@class='price-bucket-list']//div[@class='price-bucket-option ']"));

		if (priceOptions.isEmpty()) {
			throw new RuntimeException("No price bucket options found!");
		}

		// Validate count to not exceed available options
		if (count > priceOptions.size()) {
			throw new IllegalArgumentException(
					"Requested count (" + count + ") exceeds available options (" + priceOptions.size() + ")");
		}

		// Shuffle list to pick random unique options
		List<WebElement> shuffledOptions = new ArrayList<>(priceOptions);
		java.util.Collections.shuffle(shuffledOptions);

		StringBuilder selectedTexts = new StringBuilder();

		for (int i = 0; i < count; i++) {
			WebElement option = shuffledOptions.get(i);
			String optionText = option.getText().trim();
			option.click();

			Log.ReportEvent("PASS", "Clicked Per Night Price option: " + optionText);

			if (selectedTexts.length() > 0) {
				selectedTexts.append(", ");
			}
			selectedTexts.append(optionText);
		}

		return selectedTexts.toString();
	}

//Method to clickOnRatingOption
	public String clickOnRatingOption(int optionNumber, Log Log) {
		driver.findElement(By.xpath("//span[text()='Rating']")).click();
		List<WebElement> ratingOptions = driver
				.findElements(By.xpath("//div[@class='star-rating-list']//div[@class='star-rating-option']"));

		// Validate user-passed index
		if (optionNumber < 1 || optionNumber > ratingOptions.size()) {
			throw new IllegalArgumentException("Invalid option number: " + optionNumber + ". Only "
					+ ratingOptions.size() + " options available.");
		}

		// Get the option based on index (adjusting for 0-based index)
		WebElement selectedOption = ratingOptions.get(optionNumber - 1);

		String selectedText = selectedOption.getText().trim();

		WebElement checkbox = selectedOption.findElement(By.xpath(".//span[@class='tg-checkbox-box primary']"));
		checkbox.click();

		Log.ReportEvent("PASS", "Clicked Rating Option: " + selectedText);
		return selectedText;
	}

	// Method to clcik on amenities
	public String clickAmenitiesOptions(int numberToSelect, Log Log) {
		Random random = new Random();

		// 1. Click on Amenities filter
		driver.findElement(By.xpath("//span[text()='Amenities']")).click();

		// 2. Get all amenity options
		List<WebElement> allOptions = driver.findElements(By.xpath("//div[@class='amenity-option ']"));

		// 3. Check input validity
		if (numberToSelect < 1 || numberToSelect > allOptions.size()) {
			throw new IllegalArgumentException("Invalid number to select: " + numberToSelect);
		}

		StringBuilder selectedOptionsText = new StringBuilder();
		Set<Integer> selectedIndexes = new HashSet<>();

		// 4. Select random unique options based on numberToSelect
		while (selectedIndexes.size() < numberToSelect) {
			int randomIndex = random.nextInt(allOptions.size());
			if (!selectedIndexes.contains(randomIndex)) {
				selectedIndexes.add(randomIndex);

				WebElement option = allOptions.get(randomIndex);
				String optionText = option.getText().trim();

				// Click checkbox inside option
				WebElement checkbox = option
						.findElement(By.xpath(".//span[contains(@class,'tg-checkbox-box primary')]"));
				checkbox.click();
				// Log the clicked option
				Log.ReportEvent("PASS", "Clicked Amenity option: " + optionText);

				// Collect text to return
				if (selectedOptionsText.length() > 0) {
					selectedOptionsText.append(", ");
				}
				selectedOptionsText.append(optionText);
			}
		}

		return selectedOptionsText.toString();
	}

	// Method to Scroll the Slider from max
	public String adjustMaximumSliderValueByPercentage(double percentageValue) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Wait for both thumbs to be visible
		WebElement minThumb = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".thumb.thumb-0")));
		WebElement maxThumb = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".thumb.thumb-1")));

		// Wait for the slider container
		WebElement slider = driver.findElement(By.xpath("//span[contains(@class,'MuiSlider-track css-1fw2xhf')]"));

		Thread.sleep(1000);

		// Get min and max range values from thumbs
		double minValue = Double.parseDouble(minThumb.getAttribute("aria-valuemin").trim());
		double maxValue = Double.parseDouble(maxThumb.getAttribute("aria-valuemax").trim());

		// Calculate the new max value based on the percentage (reduce from max)
		double targetValue = maxValue - ((maxValue - minValue) * (percentageValue / 100.0));
		targetValue = Math.max(minValue, Math.min(maxValue, targetValue));

		System.out.println("Min: " + minValue + ", Max: " + maxValue);
		System.out.println("Target Max Value After Reducing " + percentageValue + "%: " + targetValue);

		// Calculate slider pixel position
		int sliderLeft = slider.getLocation().getX();
		int sliderWidth = slider.getSize().getWidth();
		int thumbWidth = maxThumb.getSize().getWidth();
		int effectiveRange = sliderWidth - thumbWidth;

		double targetPercentage = (targetValue - minValue) / (maxValue - minValue);
		int targetPixelOffset = (int) (targetPercentage * effectiveRange);

		int currentThumbLeft = maxThumb.getLocation().getX();
		int currentOffset = currentThumbLeft - sliderLeft;

		int moveBy = targetPixelOffset - currentOffset;

		System.out.println("Slider Left X: " + sliderLeft);
		System.out.println("Slider Width: " + sliderWidth);
		System.out.println("Thumb Width: " + thumbWidth);
		System.out.println("Effective Range (pixels): " + effectiveRange);
		System.out.println("Current Thumb Offset: " + currentOffset);
		System.out.println("Target Pixel Offset: " + targetPixelOffset);
		System.out.println("Move By (pixels): " + moveBy);

		// Perform drag on max thumb
		new Actions(driver).clickAndHold(maxThumb).moveByOffset(moveBy, 0).release().perform();

		System.out.println("Slider max thumb moved to value corresponding to " + percentageValue + "%");

		Thread.sleep(2000); // Wait for UI update

		// Get the updated range text (similar to min method)
		List<WebElement> spanElements = driver
				.findElements(By.xpath("//*[@class='one-way-flight-filters_section_content']//span"));

		String finalText = "";

		for (WebElement el : spanElements) {
			String text = el.getText().trim();
			if (!text.isEmpty()) {
				finalText = text;
				break; // Assuming only one relevant span is needed
			}
		}

		System.out.println("Final Text after Max Slider Move: " + finalText);
		return finalText;
	}

	public void ApplyFilterBtn() {
		driver.findElement(By.xpath("//button[text()='Apply']")).click();
	}

	// method to validate policy text
	public void validatePolicytext(Log Log, ScreenShots ScreenShots) {
		try {
			// Get the text from the first element (e.g., "In-Policy Hotels")
			WebElement policyTextElement = driver.findElement(By.xpath("//div[contains(@class,'policy-text')]"));
			String policyText = policyTextElement.getText().trim();

			String HeaderPolicyText = policyText.replace("Hotels", "").replace("-", " ").trim(); // becomes "In Policy"

			// Get the text from the hotel card policy element
			WebElement hotelCardPolicyTextElement = driver
					.findElement(By.xpath("//div[@class='inpolicy tg-policy']/text()"));
			String hotelCardPolicyText = hotelCardPolicyTextElement.getText().trim();

			if (HeaderPolicyText.equalsIgnoreCase(hotelCardPolicyText)) {
				Log.ReportEvent("PASS", "Policy text matched: " + HeaderPolicyText);
				System.out.println("Pass policy text ");
			} else {
				Log.ReportEvent("FAIL", "Policy text mismatch! Expected: '" + HeaderPolicyText + "', but found: '"
						+ hotelCardPolicyText + "'");
				ScreenShots.takeScreenShot1();
			}
		} catch (Exception e) {
			Log.ReportEvent("ERROR", "Exception occurred during policy text validation: " + e.getMessage());
			ScreenShots.takeScreenShot1();
		}
	}

	// Method to select hotel from card
//			public String[] selectHotelAndGetDetails(int hotelIndex) {
//			    try {
//			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//			        // Validate user input (1-based index)
//			        if (hotelIndex <= 0) {
//			            System.out.println("Invalid hotel index: " + hotelIndex);
//			            return new String[]{};
//			        }
//
//			        // Construct dynamic XPath for the specific card
//			        String cardXPath = "(//div[contains(@class,'hcard')])[" + hotelIndex + "]";
//
//			        // Wait for the specific hotel card to be visible
//			        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cardXPath)));
//
//			        // ✅ Extract hotel details (adjust these based on actual class names or structure)
//			        String[] details = new String[]{
//			            getText(card, ".//*[contains(@class,'tg-hl-hotel-name')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-star')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-address')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-distance')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-rating')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-supplier')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-pernight-price')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-price')]"),
//			            getText(card, ".//*[contains(@class,'tg-policy')]")
//			        };
//
//			        // ✅ Click the Select button inside this card
//			        WebElement selectBtn = card.findElement(By.xpath(".//button[contains(@class,'tg-hl-select-hotel')]"));
//			        selectBtn.click();
//
//			        // Optional: log details
//			        System.out.println("Hotel card #" + hotelIndex + " details:");
//			        for (String detail : details) {
//			            System.out.println(" - " + detail);
//			        }
//
//			        return details;
//
//			    } catch (Exception e) {
//			        System.out.println("Error selecting hotel and extracting details: " + e.getMessage());
//			        return new String[]{};
//			    }
//			}
//
//			private String getText(WebElement parent, String xpath) {
//			    try {
//			        WebElement el = parent.findElement(By.xpath(xpath));
//			        String text = el.getText().trim();
//			        return text.isEmpty() ? "N/A" : text;
//			    } catch (Exception e) {
//			        return "N/A";
//			    }
//			}
//

	// Method to select hotel from card
//			public String[] selectHotelAndGetDetails(int hotelIndex) {
//			    try {
//			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//			        // Validate user input (1-based index)
//			        if (hotelIndex <= 0) {
//			            System.out.println("Invalid hotel index: " + hotelIndex);
//			            return new String[]{};
//			        }
//
//			        // Construct dynamic XPath for the specific card
//			        String cardXPath = "(//div[contains(@class,'hcard')])[" + hotelIndex + "]";
//
//			        // Wait for the specific hotel card to be visible
//			        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cardXPath)));
//
//			        String[] details = new String[]{
//			            getText(card, ".//*[contains(@class,'tg-hl-hotel-name')]"),
//
//			            //  Replaced this line with class-based star extraction
//			            extractStarRatingFromClass(card),
//
//			            getText(card, ".//*[contains(@class,'tg-hl-address')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-distance')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-rating')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-supplier')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-pernight-price')]"),
//			            getText(card, ".//*[contains(@class,'tg-hl-price')]"),
//			            getText(card, ".//*[contains(@class,'tg-policy')]"),
//			            getText(card, "//span[contains(@class,'tg-hl-amenities-list')]//span[@class='hotel-amenity']"),
//			            getText(card, ".//span[@class='other-currency-price']")
//			        };
//
//			        WebElement selectBtn = card.findElement(By.xpath(".//button[contains(@class,'tg-hl-select-hotel')]"));
//			        selectBtn.click();
//
//			        System.out.println("Hotel card #" + hotelIndex + " details:");
//			        for (String detail : details) {
//			            System.out.println(" - " + detail);
//			        }
//
//			        return details;
//
//			    } catch (Exception e) {
//			        System.out.println("Error selecting hotel and extracting details: " + e.getMessage());
//			        return new String[]{};
//			    }
//			}

	public String[] selectHotelAndGetDetails(int hotelIndex, Log log) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Validate user input (1-based index)
			if (hotelIndex <= 0) {
				System.out.println("Invalid hotel index: " + hotelIndex);
				return new String[] {};
			}

			// Construct dynamic XPath for the specific card
			// String cardXPath = "(//div[contains(@class,'hcard')])[" + hotelIndex + "]";
			String cardXPath = "(//div[contains(@class,'hcard ')])[" + hotelIndex + "]";

			// Wait for the specific hotel card to be visible
			WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cardXPath)));

			// Extract details
			String hotelName = getText(card, ".//*[contains(@class,'tg-hl-hotel-name')]");
			String starRating = extractStarRatingFromClass(card);
			String address = getText(card, ".//*[contains(@class,'tg-hl-address')]");
			String distance = getText(card, ".//*[contains(@class,'tg-hl-distance')]");
			String rating = getText(card, ".//*[contains(@class,'tg-hl-rating')]");
			String supplier = getText(card, ".//*[contains(@class,'tg-hl-supplier')]");
			String perNight = getText(card, ".//*[contains(@class,'tg-hl-pernight-price')]");
			String totalPrice = getText(card, ".//*[contains(@class,'tg-hl-price')]");
			String policy = getText(card, ".//*[contains(@class,'tg-policy')]");
			String amenities = getText(card,
					"//span[contains(@class,'tg-hl-amenities-list')]//span[@class='hotel-amenity']");
			String otherPrice = getText(card, ".//span[@class='other-currency-price']");

			String[] details = { hotelName, starRating, address, distance, rating, supplier, perNight, totalPrice,
					policy, amenities, otherPrice };

			// Log supplier info to report
			log.ReportEvent("INFO", "Supplier: " + supplier);

			// Click the select button
			WebElement selectBtn = card.findElement(By.xpath(".//button[contains(@class,'tg-hl-select-hotel')]"));
			selectBtn.click();

			// Print all details in console (optional)
			System.out.println("Hotel card #" + hotelIndex + " details:");
			for (String detail : details) {
				System.out.println(" - " + detail);
			}

			return details;

		} catch (Exception e) {
			System.out.println("Error selecting hotel and extracting details: " + e.getMessage());
			log.ReportEvent("ERROR", "Failed to extract hotel details: " + e.getMessage());
			return new String[] {};
		}
	}

	// Extracts the number from class like 'tg-hl-star-4'
	private String extractStarRatingFromClass(WebElement card) {
		try {
			WebElement starElement = card.findElement(By.xpath(".//*[contains(@class,'tg-hl-star-')]"));
			String classAttr = starElement.getAttribute("class");

			Pattern pattern = Pattern.compile("tg-hl-star-(\\d+)");
			Matcher matcher = pattern.matcher(classAttr);

			if (matcher.find()) {
				return matcher.group(1); // e.g., "2", "3", "4"
			}
		} catch (Exception ignored) {
		}
		return "N/A";
	}

	private String getText(WebElement parent, String xpath) {
		try {
			WebElement el = parent.findElement(By.xpath(xpath));
			String text = el.getText().trim();
			return text.isEmpty() ? "N/A" : text;
		} catch (Exception e) {
			return "N/A";
		}
	}

	// Method to clcik on sort recommended
	public void clickOnSortOption(String optionText, Log log) {
		try {
			// Click on the sort dropdown
			WebElement sortDropdown = driver.findElement(By.xpath("//div[contains(@class,'tg-hl-sort-recommended')]"));
			sortDropdown.click();

			// Click on the desired option by visible text
			WebElement option = driver.findElement(By
					.xpath("//span[@class='tg-select-option-label' and normalize-space(text())='" + optionText + "']"));
			option.click();
			log.ReportEvent("INFO", "Clicked on sort option: " + optionText);
			System.out.println("Clicked on sort option: " + optionText);
		} catch (NoSuchElementException e) {
			System.out.println("Sort option not found: " + optionText);
			throw e;
		} catch (Exception e) {
			System.out.println("Error while selecting sort option: " + e.getMessage());
			throw e;
		}
	}

	// Method to validate sort -- name ascending filter
	public void validateSortNameAscending(Log log) {
		// Get all hotel name elements
		List<WebElement> hotelNameElements = driver
				.findElements(By.xpath("//div[contains(@class,'tg-hl-hotel-name')]"));

		// Extract the text into a list
		List<String> hotelNames = new ArrayList<>();
		for (WebElement el : hotelNameElements) {
			hotelNames.add(el.getText().trim());
		}

		// Make a copy and sort it
		List<String> sortedHotelNames = new ArrayList<>(hotelNames);
		sortedHotelNames.sort(String.CASE_INSENSITIVE_ORDER);

		// Compare original vs sorted list
		if (hotelNames.equals(sortedHotelNames)) {
			System.out.println("PASS: Hotel names are sorted in ascending order (A-Z).");
			log.ReportEvent("PASS", "Hotel names are sorted in ascending order.");

		} else {
			System.out.println("FAIL: Hotel names are not sorted in ascending order.");
			System.out.println("Actual: " + hotelNames);
			System.out.println("Expected: " + sortedHotelNames);
		}
	}

	public void validateSortNameDescending(Log log) {
		List<WebElement> hotelNameElements = driver
				.findElements(By.xpath("//div[contains(@class,'tg-hl-hotel-name')]"));

		List<String> hotelNames = new ArrayList<>();
		for (WebElement el : hotelNameElements) {
			hotelNames.add(el.getText().trim());
		}

		List<String> sortedHotelNames = new ArrayList<>(hotelNames);
		sortedHotelNames.sort(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER)); // Z → A

		if (hotelNames.equals(sortedHotelNames)) {
			System.out.println("PASS: Hotel names are sorted in descending order (Z–A).");
			log.ReportEvent("PASS", "Hotel names are sorted in descending order.");

		} else {
			System.out.println("FAIL: Hotel names are NOT sorted in descending order.");
			System.out.println("Actual: " + hotelNames);
			System.out.println("Expected: " + sortedHotelNames);
		}
	}

	public void validatePricesLowToHigh(Log log) {
		List<WebElement> priceElements = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-price')]"));

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
		List<WebElement> priceElements = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-price')]"));

		List<Integer> prices = new ArrayList<>();

		for (WebElement el : priceElements) {
			String text = el.getText();
			String number = text.replaceAll("[^0-9]", "");
			if (!number.isEmpty()) {
				prices.add(Integer.parseInt(number));
			}
		}

		// Check if the list is sorted in descending order
		for (int i = 0; i < prices.size() - 1; i++) {
			if (prices.get(i) < prices.get(i + 1)) {
				System.out.println("FAIL: Prices are not sorted in High to Low order.");
				System.out.println("Prices: " + prices);
				return;
			}
		}

		System.out.println("PASS: Prices are sorted in High to Low order.");
		log.ReportEvent("PASS", " Prices are sorted in High to Low order ");

	}

//			public void validateDistanceAscending() {
//			    List<WebElement> distanceElements = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-distance')]"));
//
//			    List<Double> distances = new ArrayList<>();
//
//			    for (WebElement el : distanceElements) {
//			        String text = el.getText(); 
//
//			        String numberStr = "";
//			        Matcher m = Pattern.compile("\\d+(\\.\\d+)?").matcher(text);
//			        if (m.find()) {
//			            numberStr = m.group();
//			        }
//
//			        if (!numberStr.isEmpty()) {
//			            distances.add(Double.parseDouble(numberStr));
//			        }
//			    }
//
//			    // Check if distances list is ascending
//			    for (int i = 0; i < distances.size() - 1; i++) {
//			        if (distances.get(i) > distances.get(i + 1)) {
//			            System.out.println("FAIL: Distances are not sorted in ascending order.");
//			            System.out.println("Distances: " + distances);
//			            return;
//			        }
//			    }
//
//			    System.out.println("PASS: Distances are sorted in ascending order.");
//			}
	public void validateDistanceAscending(Log log) {
		List<WebElement> distanceElements = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-distance')]"));

		List<Double> distances = new ArrayList<>();

		for (WebElement el : distanceElements) {
			String text = el.getText(); // Example: ">3.8 km from RR Nagar"

			// Find the first number in the text:
			// We'll loop through the characters and build the number until we hit a
			// non-number char
			String numberStr = "";
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if ((c >= '0' && c <= '9') || c == '.') {
					numberStr += c; // add digits and decimal point
				} else if (!numberStr.isEmpty()) {
					// We already found the number, break now
					break;
				}
			}

			if (!numberStr.isEmpty()) {
				distances.add(Double.parseDouble(numberStr));
			}
		}

		// Check if distances are sorted ascending
		for (int i = 0; i < distances.size() - 1; i++) {
			if (distances.get(i) > distances.get(i + 1)) {
				System.out.println("FAIL: Distances are NOT sorted ascending.");
				System.out.println("Distances found: " + distances);
				return;
			}
		}

		System.out.println("PASS: Distances are sorted ascending.");
		log.ReportEvent("PASS", " Distances are sorted ascending.");

	}

	public void validateDistanceDescendingSimple(Log log) {
		List<WebElement> distanceElements = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-distance')]"));

		List<Double> distances = new ArrayList<>();

		for (WebElement el : distanceElements) {
			String text = el.getText(); // Example: ">3.8 km from RR Nagar"

			// Extract the number from the string
			String numberStr = "";
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if ((c >= '0' && c <= '9') || c == '.') {
					numberStr += c; // add digits and decimal point
				} else if (!numberStr.isEmpty()) {
					break; // number extraction done
				}
			}

			if (!numberStr.isEmpty()) {
				distances.add(Double.parseDouble(numberStr));
			}
		}

		// Check if distances are sorted descending (each number >= next number)
		for (int i = 0; i < distances.size() - 1; i++) {
			if (distances.get(i) < distances.get(i + 1)) {
				System.out.println("FAIL: Distances are NOT sorted descending.");
				System.out.println("Distances found: " + distances);
				return;
			}
		}

		System.out.println("PASS: Distances are sorted descending.");
		log.ReportEvent("PASS", "Distances are sorted descending.");

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

	// Method to click on price
	public void clcikOnPriceSlider() {
		driver.findElement(By.xpath("//span[text()='Price']")).click();
	}

	// Method to select currency from dropdown
//			public void selectCurrencyFromDropdown(String currencyCode, Log log) {
//			    try {
//			    	Thread.sleep(3000);
//			    	driver.findElement(By.xpath("//div[contains(@class,'tg-currency-change')]//div[@class='tg-select-box__indicators css-1wy0on6']")).click();
//
//			        WebElement currencyOption = driver.findElement(
//			            By.xpath("//span[@class='tg-select-option-label' and text()='" + currencyCode + "']")
//			        );
//
//			        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", currencyOption);
//			        currencyOption.click();
//
//			        String priceText = driver.findElement(
//			            By.xpath("//span[@class='other-currency-price']")
//			        ).getText();
//
//			        System.out.println("Raw price text: " + priceText);
//
//			        String priceCurrencyCode = priceText.split(" ")[0].replaceAll("[^A-Z]", "");
//
//			        System.out.println("Extracted currency code: " + priceCurrencyCode);
//
//			        if (priceCurrencyCode.equals(currencyCode)) {
//			            System.out.println("Currency code matches: " + priceCurrencyCode);
//			            log.ReportEvent("PASS", "Currency code matches: " + priceCurrencyCode);
//			        } else {
//			            System.err.println("Currency code does NOT match! Expected: " + currencyCode + ", but found: " + priceCurrencyCode);
//			            log.ReportEvent("FAIL", "Currency code does NOT match! Expected: " + currencyCode + ", but found: " + priceCurrencyCode);
//			        }
//
//			    } catch (Exception e) {
//			        System.err.println("Exception occurred while selecting currency: " + e.getMessage());
//			        log.ReportEvent("ERROR", "Exception occurred: " + e.getMessage());
//			    }
//			}

	// Method to select currency from dropdown
	public void selectCurrencyFromDropdown(String currencyCode, Log log) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Locate dropdown using your same XPath
			WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@class,'tg-currency-change')]//div[@class='tg-select-box__indicators css-1wy0on6']")));

			// --- TRY 1: Normal click ---
			try {
				dropdown.click();
			} catch (Exception e1) {

				// --- TRY 2: Actions click ---
				try {
					new Actions(driver).moveToElement(dropdown).click().perform();
				} catch (Exception e2) {

					// --- TRY 3: JavaScript click (guaranteed) ---
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
				}
			}

			// Wait for option and click it (your same XPath)
			WebElement currencyOption = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//span[@class='tg-select-option-label' and text()='" + currencyCode + "']")));

			// Scroll + click option
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", currencyOption);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", currencyOption);

			// Validate selected currency
			String priceText = driver.findElement(By.xpath("//span[@class='other-currency-price']")).getText();

			System.out.println("Raw price text: " + priceText);

			String priceCurrencyCode = priceText.split(" ")[0].replaceAll("[^A-Z]", "");

			if (priceCurrencyCode.equals(currencyCode)) {
				log.ReportEvent("PASS", "Currency code matches: " + priceCurrencyCode);
			} else {
				log.ReportEvent("FAIL", "Expected: " + currencyCode + ", but found: " + priceCurrencyCode);
			}

		} catch (Exception e) {
			log.ReportEvent("ERROR", "Exception occurred: " + e.getMessage());
		}
	}

	public void validateOtherCurrencyPriceFromDescToResultPage(String[] otherCurrencyPriceFromDescPage,
			String[] resultsPageDetails, Log log, ScreenShots screenshots) {

		// Validate description page price
		if (otherCurrencyPriceFromDescPage == null || otherCurrencyPriceFromDescPage.length == 0
				|| otherCurrencyPriceFromDescPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Other currency price from description page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		// Validate results page details array length and the expected index 10
		int otherCurrencyIndex = 10;
		if (resultsPageDetails == null || resultsPageDetails.length <= otherCurrencyIndex
				|| resultsPageDetails[otherCurrencyIndex] == null || resultsPageDetails[otherCurrencyIndex].isEmpty()) {
			log.ReportEvent("FAIL", "Other currency price from results page is null or empty or missing.");
			screenshots.takeScreenShot1();
			return;
		}

		String descPrice = otherCurrencyPriceFromDescPage[0].trim();
		String resultPrice = resultsPageDetails[otherCurrencyIndex].trim();

		if (!descPrice.equalsIgnoreCase(resultPrice)) {
			log.ReportEvent("FAIL",
					"Other currency price mismatch! DescPage: '" + descPrice + "', ResultsPage: '" + resultPrice + "'");
			screenshots.takeScreenShot1();
			Assert.fail();
		} else {
			log.ReportEvent("PASS", "Other currency price matches: '" + descPrice + "'");
		}
	}

	// fetch all the hotel names text fom ui

	public List<String> getAllHotelNamesFromResults(Log Log) {
		// Update the selector to match your actual hotel name element
		List<WebElement> names = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-hotel-name')]"));
		List<String> hotelNamesList = new ArrayList<>();
		for (WebElement element : names) {
			hotelNamesList.add(element.getText());
		}
		return hotelNamesList;
	}

	public List<String> getAllHotelAddressFromResults(Log Log) {
		// Update the selector to match your actual hotel name element
		List<WebElement> names = driver.findElements(By.xpath("//div[contains(@class,'tg-hl-address')]"));
		List<String> hotelAddressList = new ArrayList<>();
		for (WebElement element : names) {
			hotelAddressList.add(element.getText());
		}
		return hotelAddressList;
	}

	public void validateSelectedHotelWithBackend(String[] uiHotelDetails, String apiResponseBody, int hotelIndex,
			Log log, ScreenShots screenShots) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(apiResponseBody);

// Navigate to HotelRecommendation array
			JsonNode hotelRecommendations = rootNode.path("HotelRecommendation");

			if (hotelRecommendations.isArray() && hotelRecommendations.size() >= hotelIndex) {
// Get the specific hotel at index (adjust for 0-based array)
				JsonNode selectedHotel = hotelRecommendations.get(hotelIndex - 1);

// Extract backend values - only the ones we need
				String apiHotelName = selectedHotel.path("hn").asText();
				String apiAddress = selectedHotel.path("addr").asText();
				String apiSupplier = selectedHotel.path("supname").asText();

// Handle price - ONLY take "mp" field, nothing else
				String apiTotalPrice = "";
				if (selectedHotel.has("mp")) {
					JsonNode priceNode = selectedHotel.path("mp");
					apiTotalPrice = priceNode.asText();
				}

				boolean allValidationsPassed = true;

// Validate Hotel Name (index 0 in uiHotelDetails array) - using contains for partial match
				if (uiHotelDetails.length > 0 && uiHotelDetails[0] != null && !uiHotelDetails[0].isEmpty()) {
					if (uiHotelDetails[0].toLowerCase().contains(apiHotelName.toLowerCase())
							|| apiHotelName.toLowerCase().contains(uiHotelDetails[0].toLowerCase())) {
						log.ReportEvent("PASS", "Hotel Card #" + hotelIndex
								+ " - Hotel Name validation passed: UI contains/partially matches API");
					} else {
						allValidationsPassed = false;
						log.ReportEvent("FAIL",
								"Hotel Card #" + hotelIndex + " - Hotel Name mismatch (partial match failed): UI='"
										+ uiHotelDetails[0] + "', API='" + apiHotelName + "'");
						screenShots.takeScreenShot();
					}
				}

// Validate Address (index 2 in uiHotelDetails array) - using contains for partial match
				if (uiHotelDetails.length > 2 && uiHotelDetails[2] != null && !uiHotelDetails[2].isEmpty()) {
					if (uiHotelDetails[2].toLowerCase().contains(apiAddress.toLowerCase())
							|| apiAddress.toLowerCase().contains(uiHotelDetails[2].toLowerCase())) {
						log.ReportEvent("PASS", "Hotel Card #" + hotelIndex
								+ " - Address validation passed: UI contains/partially matches API");
					} else {
						allValidationsPassed = false;
						log.ReportEvent("FAIL",
								"Hotel Card #" + hotelIndex + " - Address mismatch (partial match failed): UI='"
										+ uiHotelDetails[2] + "', API='" + apiAddress + "'");
						screenShots.takeScreenShot();
					}
				}

// Validate Supplier (index 5 in uiHotelDetails array)
				if (uiHotelDetails.length > 5 && uiHotelDetails[5] != null && !uiHotelDetails[5].isEmpty()) {
					if (uiHotelDetails[5].equalsIgnoreCase(apiSupplier)) {
						log.ReportEvent("PASS", "Hotel Card #" + hotelIndex + " - Supplier validation passed");
					} else {
						allValidationsPassed = false;
						log.ReportEvent("FAIL", "Hotel Card #" + hotelIndex + " - Supplier mismatch: UI='"
								+ uiHotelDetails[5] + "', API='" + apiSupplier + "'");
						screenShots.takeScreenShot();
					}
				}

// Validate Total Price (index 7 in uiHotelDetails array) - ONLY from mp field
				if (uiHotelDetails.length > 7 && uiHotelDetails[7] != null && !uiHotelDetails[7].isEmpty()
						&& !apiTotalPrice.isEmpty()) {
					String cleanedUiPrice = uiHotelDetails[7].replaceAll("[^0-9.]", "");
					String cleanedApiPrice = apiTotalPrice.replaceAll("[^0-9.]", "");

					if (cleanedUiPrice.equals(cleanedApiPrice)) {
						log.ReportEvent("PASS", "Hotel Card #" + hotelIndex + " - Total Price validation passed");
					} else {
						allValidationsPassed = false;
						log.ReportEvent("FAIL", "Hotel Card #" + hotelIndex + " - Total Price mismatch: UI='"
								+ uiHotelDetails[7] + "', API='" + apiTotalPrice + "'");
						screenShots.takeScreenShot();
					}
				}

// Validate Per Night Price (index 6 in uiHotelDetails array) - ONLY if present in UI
				if (uiHotelDetails.length > 6 && uiHotelDetails[6] != null && !uiHotelDetails[6].isEmpty()) {
// Check if Per Night price exists in UI and is not placeholder text
					String uiPerNightText = uiHotelDetails[6].trim();
					if (!uiPerNightText.isEmpty() && !uiPerNightText.equalsIgnoreCase("N/A")
							&& !uiPerNightText.equalsIgnoreCase("-") && !uiPerNightText.equals("0")) {

						String apiPerNightPrice = "";
						if (selectedHotel.has("Price") && selectedHotel.path("Price").has("PerNight")) {
							apiPerNightPrice = selectedHotel.path("Price").path("PerNight").asText();

							if (!apiPerNightPrice.isEmpty()) {
								String cleanedUiPerNight = uiPerNightText.replaceAll("[^0-9.]", "");
								String cleanedApiPerNight = apiPerNightPrice.replaceAll("[^0-9.]", "");

								if (cleanedUiPerNight.equals(cleanedApiPerNight)) {
									log.ReportEvent("PASS",
											"Hotel Card #" + hotelIndex + " - Per Night Price validation passed");
								} else {
									log.ReportEvent("WARNING",
											"Hotel Card #" + hotelIndex + " - Per Night Price mismatch: UI='"
													+ uiPerNightText + "', API='" + apiPerNightPrice + "'");
								}
							}
						}
					} else {
// Skip validation if UI doesn't have valid Per Night price
						log.ReportEvent("INFO", "Hotel Card #" + hotelIndex
								+ " - Per Night Price not present in UI or has placeholder value, skipping validation");
					}
				}

				if (allValidationsPassed) {
					log.ReportEvent("PASS", "✓ SUCCESS: Hotel Card #" + hotelIndex
							+ " fully validated with backend response (Hotel Name, Address, Supplier, Total Price)");
					Assert.assertTrue(true, "Hotel card #" + hotelIndex + " validation passed");
				} else {
					Assert.fail("Hotel card #" + hotelIndex + " validation failed - check logs above");
				}

			} else {
				log.ReportEvent("FAIL", "Hotel index #" + hotelIndex + " is out of range. Total hotels in response: "
						+ (hotelRecommendations.isArray() ? hotelRecommendations.size() : 0));
				screenShots.takeScreenShot();
				Assert.fail("Hotel index #" + hotelIndex + " not found in API response");
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL",
					"Exception while validating hotel #" + hotelIndex + " with backend: " + e.getMessage());
			screenShots.takeScreenShot();
			e.printStackTrace();
			Assert.fail("Validation failed: " + e.getMessage());
		}
	}

}