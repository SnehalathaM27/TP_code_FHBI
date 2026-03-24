package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class NewDesign_Hotels_DescPage {
	WebDriver driver;

	public NewDesign_Hotels_DescPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to get hotel name text from desc page
	public String[] getHotelNameFromDescPg() {
		String hotelName = driver.findElement(By.xpath("//div[contains(@class,'tg-hl-hotalname')]")).getText();
		System.out.println("hotel name from Description Page: " + hotelName);

		return new String[] { hotelName };
	}

	public void waitUntilDescriptionCardVisibleOrFail(WebDriver driver, int timeoutSeconds, Log log) {
		String xpath = "//*[contains(@class,'tg-hl-description-card')]";
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			// Element is visible, proceed
		} catch (Exception e) {
			log.ReportEvent("FAIL", "Page is not loaded");
			throw new RuntimeException("Test failed: Element with xpath '" + xpath + "' not visible after timeout.", e);
		}
	}

	public String[] getAddressFromDescPg() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement addressElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-hl-addr')]")));
			String address = addressElement.getText();
			System.out.println("hotel address from Description Page: " + address);
			return new String[] { address };
		} catch (Exception e) {
			System.out.println("Failed to get hotel address from description page: " + e.getMessage());
			return new String[] { "N/A" };
		}
	}

	public String[] getHotelRatingFromDescPg() {
		String rating = driver.findElement(By.xpath("//div[contains(@class,'tg-hl-rating-2')]")).getText();
		System.out.println("hotel rating from Description Page: " + rating);

		return new String[] { rating };
	}

	public String[] getPriceFromDescPg() {
		WebElement priceElement = driver.findElement(By.xpath(
				"//div[contains(@class,' tg-typography tg-typography_subtitle-4 d-flex flex-column align-items-end tg-typography_default')]"));
		String fullText = priceElement.getText();
		String mainPrice = fullText.replaceAll("\\(.*?\\)", "").trim();

		System.out.println("hotel price from Description Page: " + mainPrice);

		return new String[] { mainPrice };
	}

	
	
	public String[] getOtherCountryPriceFromDescPg() {
	    String Othercurrency = "";
	    
	    List<WebElement> otherCountryPrice = driver.findElements(By.xpath("//span[contains(@class,'tg-hl-othercurrency')]"));

	    if (!otherCountryPrice.isEmpty()) {
	    	Othercurrency = otherCountryPrice.get(0).getText();
	        System.out.println("Hotel Other currency price found: " + Othercurrency);
	    } else {
	        System.out.println("Other currency price found not present, skipping to next step...");
	    }

	    return new String[] { Othercurrency };
	}

	private String getStarRatingCount(WebElement element) {
		try {
			String classAttr = element.getAttribute("class");
			Pattern pattern = Pattern.compile("tg-hl-stars-(\\d+)");
			Matcher matcher = pattern.matcher(classAttr);
			if (matcher.find()) {
				return matcher.group(1);
			}
		} catch (Exception e) {
		}
		return "N/A";
	}

	public String[] getCheckInAfterFromDescPg() throws InterruptedException {
		Thread.sleep(3000);
		String checkInAfter = "";
		List<WebElement> elements = driver
				.findElements(By.xpath("//div[text()='Check In after']/following-sibling::div"));

		if (!elements.isEmpty()) {
			checkInAfter = driver.findElement(By.xpath("//div[text()='Check In after']/following-sibling::div"))
					.getText();
			System.out.println("checkInAfter from Description Page: " + checkInAfter);
		} else {
			System.out.println("'Check In after' element not found. Skipping...");
		}

		return new String[] { checkInAfter };
	}

	public void goToTop() throws InterruptedException {
		// Scroll to top
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
		Thread.sleep(2000);

	}

	public String[] getCheckOutTimeFromDescPg() {
		String checkOutTime = "";
		List<WebElement> elements = driver
				.findElements(By.xpath("//div[text()='Check Out Before']/following-sibling::div"));

		if (!elements.isEmpty()) {
			checkOutTime = driver.findElement(By.xpath("//div[text()='Check Out Before']/following-sibling::div"))
					.getText();
			System.out.println("hotel checkOutTime from Description Page: " + checkOutTime);
		} else {
			System.out.println("'Check Out Before' element not found. Skipping...");
		}

		return new String[] { checkOutTime };
	}

	public String[] getPerNightPriceFromDescPg() {
	    String perNightprice = "";
	    
	    List<WebElement> priceElements = driver.findElements(By.xpath("//p[contains(@class,'tg-hl-pricefornight')]"));

	    if (!priceElements.isEmpty()) {
	        perNightprice = priceElements.get(0).getText();
	        System.out.println("Hotel perNightprice found: " + perNightprice);
	    } else {
	        System.out.println("Per night price not present, skipping to next step...");
	    }

	    return new String[] { perNightprice };
	}

	public String[] getAmenitiesFromDescPg() { // Find all individual amenity elements inside the amenities list
		List<WebElement> amenityElements = driver.findElements(By.xpath(
				"//div[contains(@class,'d-flex tg-hl-amenities-list')]//div[contains(@class,'tg-hl-amenities')]"));

		// Initialize a list to hold the amenities
		List<String> amenitiesList = new ArrayList<>();

		// Loop through each element and add its text
		for (WebElement amenity : amenityElements) {
			String amenityText = amenity.getText().trim();
			if (!amenityText.isEmpty()) {
				amenitiesList.add(amenityText);
			}
		}

		System.out.println("Hotel amenities from Description Page: " + amenitiesList);

		return amenitiesList.toArray(new String[0]);
	}

	public String[] getPolicyFromDescPg() {
		List<WebElement> elements = driver.findElements(By.xpath("(//div[contains(@class,'tg-policy')])[1]"));
		StringBuilder amenities = new StringBuilder();

		for (WebElement el : elements) {
			amenities.append(el.getText()).append(" ");
		}

		String result = amenities.toString().trim();
		System.out.println("hotel policy from Description Page: " + result);
		return new String[] { result };
	}

	public void validateHotelNameFromDescToResultPage(String[] hotelNameFromDescPage, String[] hotelNameFromResultsPage,
			Log log, ScreenShots screenshots) {

		// Basic null/empty checks
		if (hotelNameFromDescPage == null || hotelNameFromDescPage.length == 0 || hotelNameFromDescPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel name from description page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (hotelNameFromResultsPage == null || hotelNameFromResultsPage.length == 0
				|| hotelNameFromResultsPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Hotel name from results page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String descHotelName = hotelNameFromDescPage[0].trim();
		String resultHotelName = hotelNameFromResultsPage[0].trim();

		if (!descHotelName.equalsIgnoreCase(resultHotelName)) {
			log.ReportEvent("FAIL",
					"Hotel name mismatch! DescPage: '" + descHotelName + "', ResultsPage: '" + resultHotelName + "'");
			screenshots.takeScreenShot1();
			Assert.fail();
		} else {
			log.ReportEvent("PASS", "Hotel name matches on both Description and Result pages: '" + descHotelName + "'");
		}
	}

	public void validateHotelAddressFromDescToResultPage(String[] addressFromDescPage, String[] detailsFromResultsPage,
			Log log, ScreenShots screenshots) {

		if (addressFromDescPage == null || addressFromDescPage.length == 0 || addressFromDescPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Address from description page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (detailsFromResultsPage == null || detailsFromResultsPage.length <= 2
				|| detailsFromResultsPage[2].isEmpty()) {
			log.ReportEvent("FAIL", "Address from results page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String descAddress = addressFromDescPage[0].trim().toLowerCase();
		String resultAddress = detailsFromResultsPage[2].trim().toLowerCase();

		boolean match;

		if (resultAddress.contains("...")) {
			// Get only the visible part before the "..."
			String visiblePart = resultAddress.substring(0, resultAddress.indexOf("...")).trim();
			match = descAddress.contains(visiblePart);
		} else {
			// Full address: use contains or exact match
			match = descAddress.equals(resultAddress) || descAddress.contains(resultAddress)
					|| resultAddress.contains(descAddress);
		}

		if (match) {
			log.ReportEvent("PASS", "Hotel address matches (partial allowed):\nDesc: '" + descAddress + "'\nResult: '"
					+ resultAddress + "'");
		} else {
			log.ReportEvent("FAIL",
					"Hotel address mismatch!\nDesc: '" + descAddress + "'\nResult: '" + resultAddress + "'");
			screenshots.takeScreenShot1();
			Assert.fail();
		}
	}

	public void validateHotelPriceFromDescToResultPage(String[] priceFromDescPage, String[] detailsFromResultsPage,
			Log log, ScreenShots screenshots) {

		if (priceFromDescPage == null || priceFromDescPage.length == 0 || priceFromDescPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Price from description page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (detailsFromResultsPage == null || detailsFromResultsPage.length <= 7
				|| detailsFromResultsPage[7].isEmpty()) {
			log.ReportEvent("FAIL", "Price from results page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String descPrice = priceFromDescPage[0].trim();
		String resultPrice = detailsFromResultsPage[7].trim(); // price is at index 7

		if (!descPrice.equals(resultPrice)) {
			log.ReportEvent("FAIL", "Hotel price mismatch! DescPage: " + descPrice + ", ResultsPage: " + resultPrice);
			screenshots.takeScreenShot1();
			Assert.fail("Price mismatch");
		} else {
			log.ReportEvent("PASS", "Hotel price matches on Results and Description Page: '" + descPrice + "'");
		}
	}

	public void validateHotelPolicyFromDescToResultPage(String[] policyFromDescPage, String[] detailsFromResultsPage,
			Log log, ScreenShots screenshots) {

		if (policyFromDescPage == null || policyFromDescPage.length == 0 || policyFromDescPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Policy from description page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (detailsFromResultsPage == null || detailsFromResultsPage.length <= 8
				|| detailsFromResultsPage[8].isEmpty()) {
			log.ReportEvent("FAIL", "Policy from results page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String descPolicy = policyFromDescPage[0].trim().toLowerCase();
		String resultPolicy = detailsFromResultsPage[8].trim().toLowerCase();

		if (descPolicy.contains(resultPolicy)) {
			log.ReportEvent("PASS", "Hotel policy matches on Results and Description Page: '" + descPolicy + "'");
		} else {
			log.ReportEvent("FAIL",
					"Hotel policy mismatch! DescPage: " + descPolicy + ", ResultsPage: " + resultPolicy);
			screenshots.takeScreenShot1();
			Assert.fail("Policy mismatch");
		}
	}

	public void validatePerNightPriceFromDescToResultPage(String[] perNightFromDescPage,
			String[] detailsFromResultsPage, Log log, ScreenShots screenshots) {

		if (perNightFromDescPage == null || perNightFromDescPage.length == 0 || perNightFromDescPage[0].isEmpty()) {
			log.ReportEvent("FAIL", "Per Night Price from Description Page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		if (detailsFromResultsPage == null || detailsFromResultsPage.length <= 6
				|| detailsFromResultsPage[6].isEmpty()) {
			log.ReportEvent("FAIL", "Per Night Price from Results Page is null or empty.");
			screenshots.takeScreenShot1();
			return;
		}

		String descPerNight = perNightFromDescPage[0].trim();
		String resultPerNight = detailsFromResultsPage[6].trim();

		// Extract only digits from the string, remove commas
		String descPriceDigits = descPerNight.replaceAll("[^0-9]", "");
		String resultPriceDigits = resultPerNight.replaceAll("[^0-9]", "");

		if (!descPriceDigits.equals(resultPriceDigits)) {
			log.ReportEvent("FAIL",
					"Mismatch in Per Night Price! DescPage: " + descPerNight + ", ResultsPage: " + resultPerNight);
			screenshots.takeScreenShot1();
			Assert.fail("Per Night Price mismatch");
		} else {
			log.ReportEvent("PASS", "Per Night Price matches on Results and Description Page : '" + descPerNight + "'");
		}
	}

	public void validateAmenitiesFromDescToResultPage(String[] amenitiesFromDescPage, String[] resultPageDetails,
			Log log, ScreenShots screenshots) {

		if (amenitiesFromDescPage == null || amenitiesFromDescPage.length == 0) {
			log.ReportEvent("FAIL", "Amenities from Description Page are missing.");
			screenshots.takeScreenShot1();
			return;
		}

		if (resultPageDetails == null || resultPageDetails.length < 10 || resultPageDetails[9].isEmpty()) {
			log.ReportEvent("FAIL", "Amenities from Results Page are missing.");
			screenshots.takeScreenShot1();
			return;
		}

		String descAmenityText = amenitiesFromDescPage[0].toLowerCase().trim();
		String resultAmenityText = resultPageDetails[9].toLowerCase().trim(); // assuming amenities is at index 9

		// Split the result amenities by common separators: comma, semicolon, pipe
		String[] resultAmenities = resultAmenityText.split("[,;|]");

		boolean allAmenitiesPresent = true;
		StringBuilder missingAmenities = new StringBuilder();

		for (String amenity : resultAmenities) {
			String trimmedAmenity = amenity.trim();

			// Skip empty or "n/a"
			if (trimmedAmenity.isEmpty() || trimmedAmenity.equals("n/a")) {
				continue;
			}

			if (!descAmenityText.contains(trimmedAmenity)) {
				allAmenitiesPresent = false;
				missingAmenities.append(trimmedAmenity).append(", ");
			}
		}

		if (allAmenitiesPresent) {
			log.ReportEvent("PASS", "All amenities from Results Page are found in Description Page.");
		} else {
			log.ReportEvent("FAIL", "Amenities from Results Page missing in Description Page: "
					+ missingAmenities.toString().replaceAll(", $", ""));
			screenshots.takeScreenShot1();
			Assert.fail("Amenities mismatch.");
		}
	}

	public void waitUntilHotelBookingPageDisplayed() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[contains(@class,'hotel-booking-page')]")));
	}

	// method to select the rooms
	public String[] selectRoomsFromDescPg() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			String hotelName = "";
			try {

				Thread.sleep(2000);
				WebElement hotelNameElement = driver.findElement(By.xpath("//div[contains(@class,'tg-hl-meals')]"));
				hotelName = hotelNameElement.getText().trim();
			} catch (Exception e) {
				System.out.println("⚠️ Hotel name not found: " + e.getMessage());
			}

			List<WebElement> groupedSections = driver.findElements(By.xpath("//div[@class='grouped-room-section']"));
			if (groupedSections.isEmpty()) {
				System.out.println("❌ No grouped-room-section found.");
				return new String[0];
			}
			WebElement firstGroup = groupedSections.get(0);

			String selectedRoomText = "";
			try {
				WebElement roomNameElement = firstGroup.findElement(
						By.xpath(".//div[contains(@class,'tg-rm-name') and contains(@class,'group-heading')]"));
				selectedRoomText = roomNameElement.getText().trim();
				System.out.println("✅ Found room name from group heading: " + selectedRoomText);
			} catch (Exception e) {
				System.out.println("⚠️ Room name not found in group heading: " + e.getMessage());
			}

			List<WebElement> roomCards = firstGroup
					.findElements(By.xpath(".//div[contains(@class,'tg-hl-rooms-card')]"));
			if (roomCards.isEmpty()) {
				System.out.println("❌ No room cards inside the first grouped-room-section.");
				return new String[0];
			}
			System.out.println("✅ Found " + roomCards.size() + " room cards in the first group");

			// Step 5: Filter cards that have "Select Room" button
			List<WebElement> selectableCards = new ArrayList<>();
			for (WebElement card : roomCards) {
				List<WebElement> btns = card.findElements(By.xpath(".//button[text()='Select Room']"));
				if (!btns.isEmpty()) {
					selectableCards.add(card);
				}
			}
			if (selectableCards.isEmpty()) {
				System.out.println("❌ No Select Room button found in any card of first group.");
				return new String[0];
			}
			System.out.println("✅ Found " + selectableCards.size() + " selectable cards in the first group");

			// Step 6: Select a random card
			Random random = new Random();
			int randomIndex = random.nextInt(selectableCards.size());
			WebElement selectedCard = selectableCards.get(randomIndex);

			// Scroll into view and wait a bit
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selectedCard);
			Thread.sleep(1000);

			// Step 7: Extract details from the individual room card
			String meals = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-hl-meals')]", "Meals");
			String refundableText = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-label_sm')]",
					"Refundable text");
			String labelText = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-label_md')]", "Label text");
			String roomType = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-hl-roomtype')]", "Room type");
			String policyText = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-policy')]", "Policy");
			String priceText = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-hl-roomprice')]", "Price")
					.replaceAll("\\n.*", "").trim();
			String perNightPriceText = getTextFromCard(selectedCard,
					".//div[contains(@class,'tg-hl-roomprice') and contains(@class,'subtitle')]", "Per Night Price");

			if (selectedRoomText.isEmpty()) {
				selectedRoomText = getTextFromCard(selectedCard, ".//div[contains(@class,'tg-rm-name')]",
						"Selected Room Text");
			}

			try {
				WebElement selectRoomBtn = selectedCard.findElement(By.xpath(".//button[text()='Select Room']"));
				selectRoomBtn.click();
				System.out.println("✅ Clicked Select Room button");
			} catch (Exception e) {
				System.out.println("⚠️ Select Room button not found or could not be clicked: " + e.getMessage());
			}

			System.out.println("\n Random room selected from first group (" + (randomIndex + 1) + "/"
					+ selectableCards.size() + "):");
			System.out.println(" - Hotel Name: " + hotelName);
			System.out.println(" - Meals: " + meals);
			System.out.println(" - Refundable: " + refundableText);
			System.out.println(" - Label: " + labelText);
			System.out.println(" - Room Type: " + roomType);
			System.out.println(" - Policy: " + policyText);
			System.out.println(" - Price: " + priceText);
			System.out.println(" - Per Night Price: " + perNightPriceText);
			System.out.println(" - Selected Room Text: " + selectedRoomText);

			return new String[] { hotelName, meals, refundableText, labelText, roomType, policyText, priceText,
					perNightPriceText, selectedRoomText };

		} catch (Exception e) {
			System.out.println("❌ Error in selectRoomsFromDescPg: " + e.getMessage());
			e.printStackTrace();
			return new String[0];
		}
	}

	private String getTextFromCard(WebElement card, String xpath, String label) {
		try {
			WebElement element = card.findElement(By.xpath(xpath));
			String text = element.getText().trim();
			if (text.isEmpty()) {
				System.out.println("⚠️ " + label + " found but text is empty.");
			}
			return text;
		} catch (Exception e) {
			System.out.println("⚠️ " + label + " not found. (" + xpath + ")");
			return "";
		}
	}
}
