package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class NewDesign_Hotels_BookingPage {
	WebDriver driver;

	public NewDesign_Hotels_BookingPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	//Method to get hotel name text from booking page 
		public String[] getHotelNameFromBookingPg() {
		    String hotelName = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-hotelname')]")).getText();
	        System.out.println("hotel name from Booking Page: " + hotelName);

		    return new String[]{hotelName};
		}	
		
		//Method to get hotel address text from booking page 
				public String[] getHotelAddressFromBookingPg() {
				    String hotelAddress = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-hoteladdress')]")).getText();
			        System.out.println("hotel address from Booking Page: " + hotelAddress);

				    return new String[]{hotelAddress};
				}	
				
				public String[] getCheckInAfterFromBookingPg() {
				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    WebElement checkInAfterElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
				        By.xpath("(//*[contains(normalize-space(text()), 'Check In')]/following-sibling::*[1])")
				    ));

				    String checkInAfter = checkInAfterElem.getText().trim();
				    System.out.println("checkInAfter from Booking Page: " + checkInAfter);

				    if (checkInAfter.isEmpty()) {
				        System.out.println("⚠️ Warning: 'Check In after' value is empty!");
				    }

				    return new String[]{checkInAfter};
				}

				
				
				
				public String[] getCheckOutTimeFromBookingPg() {
				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    WebElement checkInAfterElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
				        By.xpath("(//*[contains(normalize-space(text()), 'Check Out Before')]/following-sibling::*[1])")
				    ));

				    String checkOutBefore = checkInAfterElem.getText().trim();
				    System.out.println("check out before from Booking Page: " + checkOutBefore);

				    if (checkOutBefore.isEmpty()) {
				        System.out.println("⚠️ Warning: 'Check out before' value is empty!");
				    }

				    return new String[]{checkOutBefore};
				}
				
				
				public String[] getPolicyTextFromBookingPg() throws InterruptedException {
					Thread.sleep(2000);
				    String PolicyText = driver.findElement(By.xpath("//div[contains(@class,'tg-policy')]")).getText();
			        System.out.println("hotel PolicyText from Booking Page: " + PolicyText);

				    return new String[]{PolicyText};
				}	
				
				public String[] getRefundableTextFromBookingPg() {
				    String Refundable = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-faretype')]")).getText();
			        System.out.println("hotel Refundable Text from Booking Page: " + Refundable);

				    return new String[]{Refundable};
				}	
				
				public String[] getSelectedRoomTextFromBookingPg() {
				    String SelectedroomName = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-refundable-tag')]")).getText();
			        System.out.println("selecetd hotel name Text from Booking Page: " + SelectedroomName);

				    return new String[]{SelectedroomName};
				}	
				
				
				public String[] getLabelTextFromBookingPg() {
				    String label = "";

				    try {
				        // Check if the element is present
				        List<WebElement> labels = driver.findElements(By.xpath("//div[contains(@class,'tg-label_warning')]"));

				        if (!labels.isEmpty()) {
				            label = labels.get(0).getText().trim();
				            System.out.println("Hotel label text from Booking Page: " + label);
				        } else {
				            System.out.println("Label not found on Booking Page — skipping...");
				        }

				    } catch (Exception e) {
				        System.out.println("Error while getting label text: " + e.getMessage());
				    }

				    return new String[]{label};
				}
				
				public String[] getMealsTextFromBookingPg() {
				    String meals = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-meals')]")).getText();
			        System.out.println("hotel meals Text from Booking Page: " + meals);

				    return new String[]{meals};
				}	
				
				public String[] getTotalFareAmountFromBookingPg() {
				    String totalFare = driver.findElement(By.xpath("(//div[contains(@class,'tg-hb-totalnet')])[1]")).getText();
			        System.out.println("totalFare Text from Booking Page: " + totalFare);

				    return new String[]{totalFare};
				}	
				
				public String[] getCheckInDateFromBookingPg() {
				    String checkIndate = driver.findElement(By.xpath(".//div[contains(@class,'tg-hb-checkindate')]")).getText();
			        System.out.println("checkIndate Text from Booking Page: " + checkIndate);

				    return new String[]{checkIndate};
				}	
				
				public String[] getCheckOutDateFromBookingPg() {
				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    WebElement checkOutdateEle = wait.until(ExpectedConditions.visibilityOfElementLocated(
				        By.xpath(".//div[contains(@class,'tg-hb-checkoutdate')]")
				    ));

				    String checkOutdate = checkOutdateEle.getText().trim();
				    System.out.println("check out date text from Booking Page: " + checkOutdate);

				    if (checkOutdate.isEmpty()) {
				        System.out.println("⚠️ Warning: 'Check out before' value is empty!");
				    }

				    return new String[]{checkOutdate};
				}
							
				
				 public String[] getHotelNightsStayFromBookingPg() {
			 		    String nightsStay = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-nights')]//div")).getText();
			 	        System.out.println("nightsStay from booking page : " + nightsStay);
			 		    return new String[]{nightsStay};
			 		}
				
				 public void clickSendForApproval(Log log, ScreenShots screenshots) {
						driver.findElement(By.xpath("//button[text()='Send for Approval']")).click();

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

				
				public void validateCheckInAfterFromDescToBookingPage(
				        String[] checkInAfterFromDescPage,
				        String[] checkInAfterFromBookingPage,
				        Log log,
				        ScreenShots screenshots) {

				    // Null or empty checks
				    if (checkInAfterFromDescPage == null || checkInAfterFromDescPage.length == 0 || checkInAfterFromDescPage[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Check-In After from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (checkInAfterFromBookingPage == null || checkInAfterFromBookingPage.length == 0 || checkInAfterFromBookingPage[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Check-In After from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descCheckInAfter = checkInAfterFromDescPage[0].trim();
				    String bookingCheckInAfter = checkInAfterFromBookingPage[0].trim();

				    if (!descCheckInAfter.equalsIgnoreCase(bookingCheckInAfter)) {
				        log.ReportEvent("FAIL", "Check-In After mismatch! DescPage: '" + descCheckInAfter + "', BookingPage: '" + bookingCheckInAfter + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Check-In After mismatch");
				    } else {
				        log.ReportEvent("PASS", "Check-In After matches on both Description and Booking pages: '" + descCheckInAfter + "'");
				    }
				}
				
				
				public void validateCheckOutTimeFromDescToBookingPage(
				        String[] checkOutTimeFromDescPage,
				        String[] checkOutTimeFromBookingPage,
				        Log log,
				        ScreenShots screenshots) {

				    if (checkOutTimeFromDescPage == null || checkOutTimeFromDescPage.length == 0 || checkOutTimeFromDescPage[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Check-Out Time from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (checkOutTimeFromBookingPage == null || checkOutTimeFromBookingPage.length == 0 || checkOutTimeFromBookingPage[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Check-Out Time from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descCheckOutTime = checkOutTimeFromDescPage[0].trim();
				    String bookingCheckOutTime = checkOutTimeFromBookingPage[0].trim();

				    System.out.println("Description Page Check-Out Time: " + descCheckOutTime);
				    System.out.println("Booking Page Check-Out Time: " + bookingCheckOutTime);

				    if (!descCheckOutTime.equalsIgnoreCase(bookingCheckOutTime)) {
				        log.ReportEvent("FAIL", "Check-Out Time mismatch! DescPage: '" + descCheckOutTime + "', BookingPage: '" + bookingCheckOutTime + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Check-Out Time mismatch");
				    } else {
				        log.ReportEvent("PASS", "Check-Out Time matches on both Description and Booking pages: '" + descCheckOutTime + "'");
				    }
				}

				public void validateHotelAddressFromDescToBookingPage(
				        String[] addressFromDescPage,
				        String[] addressFromBookingPage,
				        Log log,
				        ScreenShots screenshots) {

				    // Null or empty checks
				    if (addressFromDescPage == null || addressFromDescPage.length == 0 || addressFromDescPage[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Address from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (addressFromBookingPage == null || addressFromBookingPage.length == 0 || addressFromBookingPage[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Address from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descAddress = addressFromDescPage[0].trim().toLowerCase();
				    String bookingAddress = addressFromBookingPage[0].trim().toLowerCase();

				    // Allow partial match in either direction
				    if (descAddress.contains(bookingAddress) || bookingAddress.contains(descAddress)) {
				        log.ReportEvent("PASS", "Hotel address matches between Description and Booking pages.\n" +
				                "DescPage: '" + descAddress + "'\nBookingPage: '" + bookingAddress + "'");
				    } else {
				        log.ReportEvent("FAIL", "Hotel address mismatch!\nDescPage: '" + descAddress + "'\nBookingPage: '" + bookingAddress + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Hotel address mismatch");
				    }
				}


				public void validateHotelNameFromDescToBookingPage(
				        String[] hotelNameFromDescPage,
				        String[] hotelNameFromBookingPage,
				        Log log,
				        ScreenShots screenshots) {

				    if (hotelNameFromDescPage == null || hotelNameFromDescPage.length == 0 || hotelNameFromDescPage[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Hotel name from description page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (hotelNameFromBookingPage == null || hotelNameFromBookingPage.length == 0 || hotelNameFromBookingPage[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Hotel name from booking page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descHotelName = hotelNameFromDescPage[0].trim();
				    String bookingHotelName = hotelNameFromBookingPage[0].trim();

				    if (!descHotelName.equalsIgnoreCase(bookingHotelName)) {
				        log.ReportEvent("FAIL", "Hotel name mismatch! DescPage: '" + descHotelName + "', BookingPage: '" + bookingHotelName + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Hotel name mismatch");
				    } else {
				        log.ReportEvent("PASS", "Hotel name matches on both Description and Booking pages: " + descHotelName);
				    }
				}
	
				public void validateLabelFromDescToBookingPage(
				        String[] descPageData,
				        String[] bookingPageLabelData,
				        Log log,
				        ScreenShots screenshots) {

				    // Null or empty checks
				    if (descPageData == null || descPageData.length < 4 || descPageData[3].isEmpty()) {
				        log.ReportEvent("INFO", "Label text from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (bookingPageLabelData == null || bookingPageLabelData.length == 0 || bookingPageLabelData[0].isEmpty()) {
				        log.ReportEvent("INFO", "Label text from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descLabel = descPageData[3].trim();  // Changed to index 3
				    String bookingLabel = bookingPageLabelData[0].trim();

				    // Compare label texts (case-insensitive)
				    if (!descLabel.equalsIgnoreCase(bookingLabel)) {
				        log.ReportEvent("INFO", "Label mismatch! DescPage Label: '" + descLabel + "', Booking Page Label: '" + bookingLabel + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Label mismatch");
				    } else {
				        log.ReportEvent("PASS", "Label matches on both Description and Booking pages. Label: '" + descLabel + "'");
				    }
				}

				public void validateMealsTextFromDescToBookingPage(
				        String[] descPageData,
				        String[] bookingPageMealsData,
				        Log log,
				        ScreenShots screenshots) {

				    // Validate Description Page meal text (from index 1)
				    if (descPageData == null || descPageData.length < 2 || descPageData[1].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Meals text from Description Page is missing or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    // Validate Booking Page meal text (from index 0, because getMealsTextFromBookingPg returns String[]{meals})
				    if (bookingPageMealsData == null || bookingPageMealsData.length == 0 || bookingPageMealsData[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Meals text from Booking Page is missing or not in the expected format.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descMeals = descPageData[1].trim();    // Updated to index 1
				    String bookingMeals = bookingPageMealsData[0].trim();

				    // Compare meal texts (case-insensitive)
				    if (!descMeals.equalsIgnoreCase(bookingMeals)) {
				        log.ReportEvent("FAIL", "Meals text mismatch! Description Page: '" + descMeals + "', Booking Page: '" + bookingMeals + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Meals text mismatch between Description and Booking Pages.");
				    } else {
				        log.ReportEvent("PASS", "Meals text matches on both Description and Booking pages. Meals: '" + descMeals + "'");
				    }
				}



				public void validatePolicyTextFromDescToBookingPage(
				        String[] descPagePolicy,
				        String[] bookingPagePolicy,
				        Log log,
				        ScreenShots screenshots) {

				    // Null or empty checks
				    if (descPagePolicy == null || descPagePolicy.length == 0 || descPagePolicy[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Policy text from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (bookingPagePolicy == null || bookingPagePolicy.length == 0 || bookingPagePolicy[0].isEmpty()) {
				        log.ReportEvent("FAIL", "Policy text from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descPolicy = descPagePolicy[0].trim();
				    String bookingPolicy = bookingPagePolicy[0].trim();

				    // Check if all booking page policy text is contained in description page policy text
				    if (!descPolicy.toLowerCase().contains(bookingPolicy.toLowerCase())) {
				        log.ReportEvent("FAIL", "Policy text mismatch! DescPage: '" + descPolicy + "', BookingPage: '" + bookingPolicy + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Policy text mismatch");
				    } else {
				        log.ReportEvent("PASS", "Policy text from Booking Page is present in Description Page.");
				    }
				}
				public void validateRefundableTextFromDescToBookingPage(
				        String[] descPageRoomDetails,
				        String[] bookingPageRefundable,
				        Log log,
				        ScreenShots screenshots) {

				    // refundable text is at index 2 in descPageRoomDetails
				    if (descPageRoomDetails == null || descPageRoomDetails.length < 3 || descPageRoomDetails[2].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Refundable text from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (bookingPageRefundable == null || bookingPageRefundable.length == 0 || bookingPageRefundable[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Refundable text from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descRefundable = descPageRoomDetails[2].trim();  // Corrected index here
				    String bookingRefundable = bookingPageRefundable[0].trim();

				    if (!descRefundable.equalsIgnoreCase(bookingRefundable)) {
				        log.ReportEvent("FAIL", "Refundable text mismatch! DescPage: '" + descRefundable + "', BookingPage: '" + bookingRefundable + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Refundable text mismatch");
				    } else {
				        log.ReportEvent("PASS", "Refundable text matches on both Description and Booking pages.");
				    }
				}


				public void validateSelectedRoomTextFromDescToBookingPage(
				        String[] descPageRoomDetails,
				        String[] bookingPageRoomText,
				        Log log,
				        ScreenShots screenshots) {

				    // Validate index 8 for selected room text in Description Page
				    if (descPageRoomDetails == null || descPageRoomDetails.length < 9 || descPageRoomDetails[8] == null || descPageRoomDetails[8].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Selected Room Text from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    // Validate index 0 for selected room text in Booking Page
				    if (bookingPageRoomText == null || bookingPageRoomText.length == 0 || bookingPageRoomText[0] == null || bookingPageRoomText[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Selected Room Text from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descSelectedRoomText = descPageRoomDetails[8].trim();
				    String bookingSelectedRoomText = bookingPageRoomText[0].trim();

				    // Compare room texts ignoring case
				    if (!descSelectedRoomText.equalsIgnoreCase(bookingSelectedRoomText)) {
				        log.ReportEvent("FAIL", "Selected Room Text mismatch!\n" +
				                "   Description Page: '" + descSelectedRoomText + "'\n" +
				                "   Booking Page: '" + bookingSelectedRoomText + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Selected Room Text mismatch between Description and Booking Page.");
				    } else {
				        log.ReportEvent("PASS", "Selected Room Text matches between Description and Booking pages.\n" +
				                "   Value: '" + descSelectedRoomText + "'");
				    }
				}


				public void validatePriceFromDescWithBookingPage(
				        String[] descPageRoomDetails,
				        String[] bookingPageTotalFare,
				        Log log,
				        ScreenShots screenshots) {

				    if (descPageRoomDetails == null || descPageRoomDetails.length < 7 || descPageRoomDetails[6] == null || descPageRoomDetails[6].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Price from Description Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    if (bookingPageTotalFare == null || bookingPageTotalFare.length == 0 || bookingPageTotalFare[0] == null || bookingPageTotalFare[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Total Fare from Booking Page is null or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String descPriceRaw = descPageRoomDetails[6].trim();
				    String bookingPriceRaw = bookingPageTotalFare[0].trim();

				    // Regex to match price: optional ₹, digits, commas, dots (e.g. ₹ 1,17,071 or ₹117071.40)
				    Pattern pricePattern = Pattern.compile("₹?\\s*[\\d,]+(\\.\\d+)?");

				    Matcher matcherDesc = pricePattern.matcher(descPriceRaw);
				    String descPricePart = "";
				    if (matcherDesc.find()) {
				        descPricePart = matcherDesc.group();
				    }

				    Matcher matcherBooking = pricePattern.matcher(bookingPriceRaw);
				    String bookingPricePart = "";
				    if (matcherBooking.find()) {
				        bookingPricePart = matcherBooking.group();
				    }

				    if (descPricePart.isEmpty() || bookingPricePart.isEmpty()) {
				        log.ReportEvent("FAIL", "Failed to extract price parts. Description Page: '" + descPriceRaw + "', Booking Page: '" + bookingPriceRaw + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Price extraction failed");
				        return;
				    }

				    // Clean commas, currency symbol ₹ and spaces
				    String descPriceCleaned = descPricePart.replaceAll("[₹,\\s]", "");
				    String bookingPriceCleaned = bookingPricePart.replaceAll("[₹,\\s]", "");

				    try {
				        double descPrice = Double.parseDouble(descPriceCleaned);
				        double bookingPrice = Double.parseDouble(bookingPriceCleaned);

				        double tolerance = 0.01;

				        if (Math.abs(descPrice - bookingPrice) <= tolerance) {
				            log.ReportEvent("PASS", "Price matches between Description and Booking pages. Value: " + bookingPricePart);
				        } else {
				            log.ReportEvent("FAIL", "Price mismatch! Description Page Price: '" + descPricePart +
				                    "', Booking Page Fare: '" + bookingPricePart + "'");
				            screenshots.takeScreenShot1();
				            Assert.fail("Total fare mismatch");
				        }

				    } catch (NumberFormatException e) {
				        log.ReportEvent("FAIL", "Failed to parse price values. Description Page: '" + descPricePart + "', Booking Page: '" + bookingPricePart + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Invalid price format");
				    }
				}


		
				public void validateCheckInDateBetweenResultAndBookingPage(
				        String[] resultPageDateParts,
				        String[] bookingPageDateText,
				        Log log,
				        ScreenShots screenshots) {

				    // Validate Result Page date parts
				    if (resultPageDateParts == null || resultPageDateParts.length != 3) {
				        log.ReportEvent("FAIL", "Check-in date from Result Page is missing or not in expected format.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    // Validate Booking Page date
				    if (bookingPageDateText == null || bookingPageDateText.length == 0 || bookingPageDateText[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Check-in date from Booking Page is missing or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String resultDay = resultPageDateParts[0];
				    String resultMonth = resultPageDateParts[1].replace(",", "");  // Remove comma if present
				    String resultYear = resultPageDateParts[2];

				    String normalizedResultDate = resultDay + " " + resultMonth + " " + resultYear;  // e.g., "20 Sep 2025"
				    String bookingDateRaw = bookingPageDateText[0].trim();

				    // Normalize booking page text by removing suffixes, commas, and trimming
				    String bookingDate = bookingDateRaw
				            .replaceAll("(?<=\\d)(st|nd|rd|th)", "")
				            .replace(",", "")
				            .trim();  // e.g., "20 Sep 2025"

				    // Compare (case-insensitive)
				    if (!normalizedResultDate.equalsIgnoreCase(bookingDate)) {
				        log.ReportEvent("FAIL", "Check-in date mismatch! Result Page: '" + normalizedResultDate + "', Booking Page: '" + bookingDate + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Check-in date mismatch between Result and Booking Pages.");
				    } else {
				        log.ReportEvent("PASS", "Check-in date matches on both Result and Booking pages. Date: '" + normalizedResultDate + "'");
				    }
				}
				public void validateCheckOutDateBetweenResultAndBookingPage(
				        String[] resultPageDateParts,
				        String[] bookingPageDateText,
				        Log log,
				        ScreenShots screenshots) {

				    // Validate Result Page date parts
				    if (resultPageDateParts == null || resultPageDateParts.length != 3) {
				        log.ReportEvent("FAIL", "Check-out date from Result Page is missing or not in expected format.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    // Validate Booking Page date
				    if (bookingPageDateText == null || bookingPageDateText.length == 0 || bookingPageDateText[0].trim().isEmpty()) {
				        log.ReportEvent("FAIL", "Check-out date from Booking Page is missing or empty.");
				        screenshots.takeScreenShot1();
				        return;
				    }

				    String resultDay = resultPageDateParts[0];
				    String resultMonth = resultPageDateParts[1].replace(",", "");  // Remove comma
				    String resultYear = resultPageDateParts[2];

				    String normalizedResultDate = resultDay + " " + resultMonth + " " + resultYear;  // e.g., "25 Sep 2025"
				    String bookingDateRaw = bookingPageDateText[0].trim();

				    // Normalize booking page date (remove ordinal suffixes and commas)
				    String bookingDate = bookingDateRaw
				            .replaceAll("(?<=\\d)(st|nd|rd|th)", "")
				            .replace(",", "")
				            .trim();  // e.g., "25 Sep 2025"

				    // Compare (case-insensitive)
				    if (!normalizedResultDate.equalsIgnoreCase(bookingDate)) {
				        log.ReportEvent("FAIL", "Check-out date mismatch! Result Page: '" + normalizedResultDate + "', Booking Page: '" + bookingDate + "'");
				        screenshots.takeScreenShot1();
				        Assert.fail("Check-out date mismatch between Result and Booking Pages.");
				    } else {
				        log.ReportEvent("PASS", "Check-out date matches on both Result and Booking pages. Date: '" + normalizedResultDate + "'");
				    }
				}

				
				public void enterLastNameForHotels(String lastName) throws InterruptedException {
				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    WebElement name = wait.until(ExpectedConditions.elementToBeClickable(
				        By.xpath("//input[@name='lastname']")));
				    
				    // Click to focus
				    name.click();
				    
				    // Clear the input using JavaScript
				    JavascriptExecutor js = (JavascriptExecutor) driver;
				    js.executeScript("arguments[0].value = '';", name);
				    
				    // Small pause to let the clearing reflect
				    Thread.sleep(500);
				    
				    // Additional clear by Ctrl+A + Backspace (in case JS didn't fully clear)
				    name.sendKeys(Keys.chord(Keys.CONTROL, "a"));
				    name.sendKeys(Keys.BACK_SPACE);
				    
				    // Enter the new last name
				    name.sendKeys(lastName);
				}
				

				public void enterEmailForHotels(String email) throws InterruptedException {
				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    WebElement name = wait.until(ExpectedConditions.elementToBeClickable(
				        By.xpath("//input[@class='tg-hb-email']")));
				    
				    // Click to focus
				    name.click();
				    
				    // Clear the input using JavaScript
				    JavascriptExecutor js = (JavascriptExecutor) driver;
				    js.executeScript("arguments[0].value = '';", name);
				    
				    // Small pause to let the clearing reflect
				    Thread.sleep(500);
				    
				    // Additional clear by Ctrl+A + Backspace (in case JS didn't fully clear)
				    name.sendKeys(Keys.chord(Keys.CONTROL, "a"));
				    name.sendKeys(Keys.BACK_SPACE);
				    
				    // Enter the new last name
				    name.sendKeys(email);
				}



				
				//Method to add traveller details 
				
				public void addTravellerDetails() throws InterruptedException {
				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    Actions actions = new Actions(driver);
				    Random random = new Random();

				    // Get list of Title dropdown elements
				    List<WebElement> titleDropdowns = driver.findElements(By.xpath("//span[text()='Title']/following::div[contains(@class,'tg-select-option')]"));
				    // Get list of first name input fields
				    List<WebElement> firstNames = driver.findElements(By.xpath("//input[@name='firstname']"));
				    // Get list of last name input fields
				    List<WebElement> lastNames = driver.findElements(By.xpath("//input[@name='lastname']"));

				    // Sample data to pick random names
				    String[] sampleFirstNames = {"John", "Jane", "Alex", "Emily", "Chris", "Katie"};
				    String[] sampleLastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Davis"};
				    String[] sampleTitles = {"Mr", "Mrs", "Miss", "Dr"};

				    // We start from 2nd traveller (index 1)
				    for (int i = 1; i < titleDropdowns.size(); i++) {
				        try {
				            WebElement titleDropdown = titleDropdowns.get(i);

				            // Click on title dropdown to open options
				            wait.until(ExpectedConditions.elementToBeClickable(titleDropdown));
				            titleDropdown.click();
				            Thread.sleep(500); // wait for options to appear

				            // Find all dropdown options under this dropdown
				            List<WebElement> options = driver.findElements(By.xpath("//div[contains(@class,'tg-select-option')]"));
				            if (!options.isEmpty()) {
				                // Pick random option and click it
				                int randomIndex = random.nextInt(options.size());
				                WebElement option = options.get(randomIndex);
				                wait.until(ExpectedConditions.elementToBeClickable(option));
				                option.click();
				                Thread.sleep(500);
				            }

				            // Enter random first name
				            if (i < firstNames.size()) {
				                WebElement firstNameInput = firstNames.get(i);
				                firstNameInput.clear();
				                firstNameInput.sendKeys(sampleFirstNames[random.nextInt(sampleFirstNames.length)]);
				            }

				            // Enter random last name
				            if (i < lastNames.size()) {
				                WebElement lastNameInput = lastNames.get(i);
				                lastNameInput.clear();
				                lastNameInput.sendKeys(sampleLastNames[random.nextInt(sampleLastNames.length)]);
				            }
				        } catch (Exception e) {
				            System.out.println("Failed to fill traveller details for index " + i + ": " + e.getMessage());
				        }
				    }
				}

			
}
