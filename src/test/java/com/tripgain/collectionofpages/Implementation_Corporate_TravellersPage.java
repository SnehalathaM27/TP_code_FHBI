package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class Implementation_Corporate_TravellersPage {
	WebDriver driver;

	public Implementation_Corporate_TravellersPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	// Method to clcik on view detail button in Corporate Travellers page
	public void clickOnViewDetailBtnInCorpTravellers(String email) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement viewDetailBtn = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space(text())='" + email+ "']/ancestor::div[contains(@class,'corporate_traveller__detail_container')]//button[normalize-space(text())='View Detail']")));
		viewDetailBtn.click();
		System.out.println(" Clicked on 'View Detail' for: " + email);
	}
	public String[] clickOnTravellerProfile() throws InterruptedException {
	    Thread.sleep(1000);

	    driver.findElement(By.xpath("//div[@class='MuiAvatar-root MuiAvatar-circular MuiAvatar-colorDefault css-1vhh6nc']/following-sibling::button")).click();
	    Thread.sleep(1000);

	    driver.findElement(By.xpath("//span[normalize-space(text())='Profile']")).click();
	    Thread.sleep(3000);

	    WebElement gradeTextElement = driver.findElement(By.xpath("//div[normalize-space(text())='Grade']/following-sibling::div"));
	    String gradeText = gradeTextElement.getText().trim();

	    System.out.println("Grade from Traveller Profile: " + gradeText);
	    return new String[]{gradeText};
	}

	public void clcikOnEditBtn() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		try {
			// Don't wait for overlays to disappear - just find the button and click it
			WebElement editBtn = wait.until(ExpectedConditions
					.presenceOfElementLocated(By.cssSelector("div.profile__action_btn_align button")));

			// Scroll to the button first
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editBtn);
			Thread.sleep(500);

			// Force click using JavaScript
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);

			System.out.println("✓ Successfully clicked on 'Edit' button using JavaScript");

		} catch (Exception e) {
			System.out.println("✗ Failed to click Edit button: " + e.getMessage());
			throw e;
		}
	}

	// Method to clcik on select grade dropdown

	public String[] clickOnSelectGrade(String gradeOption, Log log) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			// CLICK TO OPEN DROPDOWN - USING YOUR XPATH
			WebElement gradeBox = driver.findElement(By.xpath(
					"(//div[@class='tg-select-box__value-container tg-select-box__value-container--has-value css-hlgwow'])[4]"));
			gradeBox.click();
			Thread.sleep(1000);

			// WAIT FOR DROPDOWN MENU
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[contains(@class,'tg-select-box__menu-list')]")));

			// FIND OPTION BASED ON USER VALUE AND SCROLL TO IT
			String optionXpath = "//span[@class='tg-select-option-label' and contains(text(), '" + gradeOption + "')]";
			WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(optionXpath)));

			// SCROLL TO THE OPTION AND CLICK
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
			Thread.sleep(500);
			option.click();

			System.out.println("✅ Selected grade option: " + gradeOption);
			log.ReportEvent("PASS", "Selected grade option: " + gradeOption);

			return new String[] { gradeOption };

		} catch (Exception e) {
			System.out.println("❌ Failed to select grade: " + e.getMessage());
			log.ReportEvent("FAIL", "Failed to select grade: " + e.getMessage());
			throw e;
		}
	}

	public void clickOnSaveBtn() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		try {
			WebElement saveBtn = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Save']")));

			// Try regular click first, fallback to JavaScript
			try {
				saveBtn.click();
			} catch (Exception e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
			}

			System.out.println("✓ Clicked on Save button");
		} catch (Exception e) {
			System.out.println("✗ Failed to click Save button: " + e.getMessage());
			throw e;
		}
	}
	// Method to validate grade from selecetd to traveller profile

	public void validateGradesFromSelectedToTravellerProfile(String[] selectedGrades, String[] travellerProfileGrade,
			Log log, ScreenShots screenshots) {
		if (travellerProfileGrade == null || travellerProfileGrade.length == 0) {
			log.ReportEvent("FAIL", "traveller Profile Grade is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("traveller Profile Grade is missing.");
			return;
		}
		if (selectedGrades == null || selectedGrades.length == 0) {
			log.ReportEvent("FAIL", "selecetd grade  is missing.");
			screenshots.takeScreenShot1();
			Assert.fail("selecetd grade  is missing.");
			return;
		}

		String travellerGrade = travellerProfileGrade[0].trim();
		String selectedgrade = selectedGrades[0].trim();

		if (!selectedgrade.equalsIgnoreCase(travellerGrade)) {
			log.ReportEvent("FAIL", "Grade mismatch! selected grade: '" + selectedgrade
					+ "', traveller Profile grade: '" + travellerGrade + "'");
			screenshots.takeScreenShot1();
			Assert.fail("grades mismatch between selected to traveller profile grade section");
		} else {
			log.ReportEvent("PASS", "grades matches between selected to traveller profile " + selectedgrade);
		}
	}

	public void validateHotelPricesAgainstGradePolicy(String[] selectedGrades, int priceLimit, Log log,
			ScreenShots screenshots) {
		try {
			log.ReportEvent("INFO", "Starting hotel price validation for grade - Price limit: " + priceLimit);

			// Keep scrolling until no more hotels load
			JavascriptExecutor js = (JavascriptExecutor) driver;
			int previousCount = 0;
			int currentCount = 0;
			int scrollCount = 0;

			do {
				previousCount = currentCount;

				// Scroll to bottom
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(3000);

				// Get current hotel count
				List<WebElement> hotelCards = driver.findElements(By.xpath("//div[@class='hcard']"));
				currentCount = hotelCards.size();
				scrollCount++;

				// Stop if no new hotels loaded or after 20 scrolls
				if (currentCount == previousCount) {
					break;
				}

			} while (scrollCount < 20);

			// Final hotel cards
			List<WebElement> hotelCards = driver.findElements(By.xpath("//div[@class='hcard']"));
			log.ReportEvent("INFO", "Final total hotels: " + hotelCards.size());

			boolean allCorrect = true;

			// Check each hotel card
			for (int i = 0; i < hotelCards.size(); i++) {
				WebElement hotelCard = hotelCards.get(i);

				// Get hotel price
				WebElement priceElement = hotelCard.findElement(By.xpath(".//div[contains(@class,'tg-hl-price')]"));
				String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();
				int hotelPrice = Integer.parseInt(priceText);

				// Get policy text
				WebElement policyElement = hotelCard.findElement(By.xpath(".//div[contains(@class,'tg-policy')]"));
				String actualPolicy = policyElement.getText();

				// Convert to lowercase for case-insensitive comparison
				String actualPolicyLower = actualPolicy.toLowerCase();

				// Check if policy is correct - Only log failures
				if (hotelPrice <= priceLimit) {
					// Price is within limit - should be IN POLICY
					if (!actualPolicyLower.contains("in policy")) {
						log.ReportEvent("FAIL", "Hotel " + (i + 1) + " - Price: " + hotelPrice
								+ " - Should be IN POLICY but found: " + actualPolicy);
						allCorrect = false;
					}
				} else {
					// Price exceeds limit - should be OUT OF POLICY
					if (!actualPolicyLower.contains("out of policy")) {
						log.ReportEvent("FAIL", "Hotel " + (i + 1) + " - Price: " + hotelPrice
								+ " - Should be OUT OF POLICY but found: " + actualPolicy);
						allCorrect = false;
					}
				}
			}

			// Final result
			if (allCorrect) {
				log.ReportEvent("PASS", "All " + hotelCards.size()
						+ " hotels follow grade policy correctly! Price limit: " + priceLimit);
			} else {
				log.ReportEvent("FAIL", "Some hotels don't follow grade policy! Price limit: " + priceLimit);
				screenshots.takeScreenShot1();
				Assert.fail("Hotel policy validation failed - Some hotels don't follow grade policy with limit: "
						+ priceLimit);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Error during hotel validation: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Error during hotel validation: " + e.getMessage());
		}
	}

	public void validateBusesPricesAgainstGradePolicy(String[] selectedGrades, int priceLimit, Log log,
			ScreenShots screenshots) {
		try {
			log.ReportEvent("INFO", "Starting hotel price validation for grade - Price limit: " + priceLimit);

			// Keep scrolling until no more hotels load
			JavascriptExecutor js = (JavascriptExecutor) driver;
			int previousCount = 0;
			int currentCount = 0;
			int scrollCount = 0;

			do {
				previousCount = currentCount;

				// Scroll to bottom
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(3000);

				// Get current hotel count
				List<WebElement> busCards = driver.findElements(By.xpath("//div[contains(@class,'buscard-root')]"));
				currentCount = busCards.size();
				scrollCount++;

				// Stop if no new hotels loaded or after 20 scrolls
				if (currentCount == previousCount) {
					break;
				}

			} while (scrollCount < 20);

			// Final hotel cards
			List<WebElement> busCards = driver.findElements(By.xpath("//div[contains(@class,'buscard-root')]"));
			log.ReportEvent("INFO", "Final total hotels: " + busCards.size());

			boolean allCorrect = true;

			// Check each hotel card
			for (int i = 0; i < busCards.size(); i++) {
				WebElement busCard = busCards.get(i);

				// Get hotel price
				WebElement priceElement = busCard.findElement(By.xpath(".//div[contains(@class,'tg-bus-fare')]"));
				String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();
				int busPrice = Integer.parseInt(priceText);

				// Get policy text
				WebElement policyElement = busCard.findElement(By.xpath(".//div[contains(@class,'tg-policy')]"));
				String actualPolicy = policyElement.getText();

				// Convert to lowercase for case-insensitive comparison
				String actualPolicyLower = actualPolicy.toLowerCase();

				// Check if policy is correct - Only log failures
				if (busPrice <= priceLimit) {
					// Price is within limit - should be IN POLICY
					if (!actualPolicyLower.contains("in policy")) {
						log.ReportEvent("FAIL", "Bus " + (i + 1) + " - Price: " + busPrice
								+ " - Should be IN POLICY but found: " + actualPolicy);
						allCorrect = false;
					}
				} else {
					// Price exceeds limit - should be OUT OF POLICY
					if (!actualPolicyLower.contains("out of policy")) {
						log.ReportEvent("FAIL", "Bus " + (i + 1) + " - Price: " + busPrice
								+ " - Should be OUT OF POLICY but found: " + actualPolicy);
						allCorrect = false;
					}
				}
			}

			// Final result
			if (allCorrect) {
				log.ReportEvent("PASS",
						"All " + busCards.size() + " Bus follow grade policy correctly! Price limit: " + priceLimit);
			} else {
				log.ReportEvent("FAIL", "Some Buses don't follow grade policy! Price limit: " + priceLimit);
				screenshots.takeScreenShot1();
				Assert.fail("Bus policy validation failed - Some hotels don't follow grade policy with limit: "
						+ priceLimit);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Error during bus validation: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Error during bus validation: " + e.getMessage());
		}
	}

	// ------------------------------Philips Corp Testing

	public void validateHotelPolicyAgainstDaysForDomestic(String[] checkindateResultPage,
			String[] checkoutdateResultPage, Log log, ScreenShots screenshots) {
		try {
			// ✅ FIX: Construct full date strings using all parts
			String checkInDay = checkindateResultPage[0]; // "20"
			String checkInMonth = checkindateResultPage[1]; // "Feb"
			String checkInYear = checkindateResultPage[2]; // "2026"

			String checkOutDay = checkoutdateResultPage[0]; // "21"
			String checkOutMonth = checkoutdateResultPage[1]; // "Feb"
			String checkOutYear = checkoutdateResultPage[2]; // "2026"

			// Create full date strings
			String checkInDateStr = checkInDay + " " + checkInMonth + " " + checkInYear;
			String checkOutDateStr = checkOutDay + " " + checkOutMonth + " " + checkOutYear;

			System.out.println("Check-in full date: " + checkInDateStr);
			System.out.println("Check-out full date: " + checkOutDateStr);

			// Adjust the date pattern according to your UI format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

			LocalDate checkInDate = LocalDate.parse(checkInDateStr, formatter);
			LocalDate checkOutDate = LocalDate.parse(checkOutDateStr, formatter);

			int userDays = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
			System.out.println("Calculated days between: " + userDays);

			log.ReportEvent("INFO", "Starting hotel policy validation based on days: " + userDays);

			List<WebElement> hotelCards = driver.findElements(By.xpath("//div[@class='hcard ']"));
			log.ReportEvent("INFO", "Total hotels found: " + hotelCards.size());

			boolean allCorrect = true;

			// ✅ Your existing policy rule
			String expectedPolicy;
			if (userDays <= 7) { // <= 7 → OUT OF POLICY
				expectedPolicy = "out of policy";
			} else { // > 7 → IN POLICY
				expectedPolicy = "in policy";
			}

			for (int i = 0; i < hotelCards.size(); i++) {

				WebElement hotelCard = hotelCards.get(i);

				WebElement policyElement = hotelCard.findElement(By.xpath(".//div[contains(@class,'tg-policy')]"));

				String actualPolicy = policyElement.getText().toLowerCase();

				if (!actualPolicy.contains(expectedPolicy)) {
					log.ReportEvent("FAIL", "Hotel " + (i + 1) + " - Days: " + userDays + " | Expected Policy: "
							+ expectedPolicy + " but found: " + actualPolicy);

					allCorrect = false;
				}
			}

			if (allCorrect) {
				log.ReportEvent("PASS", "All hotels correctly show policy based on days rule. Days: " + userDays
						+ " | Applied Policy: " + expectedPolicy);
			} else {
				screenshots.takeScreenShot1();
				Assert.fail("Hotel policy validation failed for days: " + userDays + " | Expected Policy: "
						+ expectedPolicy);
			}

		} catch (Exception e) {
			log.ReportEvent("FAIL", "Error during validation: " + e.getMessage());
			screenshots.takeScreenShot1();
			Assert.fail("Error during validation: " + e.getMessage());
		}
	}

	
	
	public void validateHotelDaysPolicyForInternational(String[] checkindateResultPage, Log log, ScreenShots screenshots) {
	    try {
	        // Construct check-in date string
	        String checkInDay = checkindateResultPage[0];
	        String checkInMonth = checkindateResultPage[1];
	        String checkInYear = checkindateResultPage[2];

	        String checkInDateStr = checkInDay + " " + checkInMonth + " " + checkInYear;
	        System.out.println("Check-in full date: " + checkInDateStr);

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
	        LocalDate checkInDate = LocalDate.parse(checkInDateStr, formatter);

	        // Calculate days from TODAY to CHECK-IN (inclusive)
	        LocalDate today = LocalDate.now();
	        int daysUntilCheckIn = (int) ChronoUnit.DAYS.between(today, checkInDate) + 1;

	        log.ReportEvent("INFO", "Starting INTERNATIONAL hotel policy validation. Days until check-in (inclusive): " + daysUntilCheckIn);

	        List<WebElement> hotelCards = driver.findElements(By.xpath("//div[@class='hcard ']"));
	        log.ReportEvent("INFO", "Total international hotels found: " + hotelCards.size());

	        boolean allCorrect = true;

	        for (int i = 0; i < hotelCards.size(); i++) {
	            WebElement hotelCard = hotelCards.get(i);

	            // Get policy text
	            WebElement policyElement = hotelCard.findElement(By.xpath(".//div[contains(@class,'tg-policy')]"));
	            String actualPolicy = policyElement.getText().toLowerCase();
	            
	            // Get hotel price
	            int hotelPrice;
	            
	            // Try to get price element
	            try {
	                WebElement priceElement = hotelCard.findElement(By.xpath(".//*[contains(@class,'tg-hl-price')]"));
	                String priceText = priceElement.getText().replace("$", "").replace(",", "").trim();
	                if (priceText.contains("₹")) {
	                    priceText = priceText.replace("₹", "").replace(",", "").trim();
	                }
	                hotelPrice = Integer.parseInt(priceText);
	            } catch (Exception e) {
	                // Try alternative price locator
	                WebElement priceElement = hotelCard.findElement(By.xpath(".//*[contains(@class,'price')]"));
	                String priceText = priceElement.getText().replaceAll("[^0-9]", "").trim();
	                hotelPrice = Integer.parseInt(priceText);
	            }

	            // STEP 1: First validate DAYS-BASED policy
	            String expectedDaysPolicy = (daysUntilCheckIn <= 15) ? "out of policy" : "in policy";
	            
	            // STEP 2: Then validate AMOUNT-BASED policy (0-13600 = in policy, above = out of policy)
	            String expectedAmountPolicy = (hotelPrice >= 0 && hotelPrice <= 13600) ? "in policy" : "out of policy";
	            
	            // The final expected policy should match BOTH conditions
	            // But we need to know how these two combine
	            // For now, I'll check both separately
	            
	            log.ReportEvent("INFO", "Hotel " + (i + 1) + " | Days: " + daysUntilCheckIn + " | Expected Days Policy: " + expectedDaysPolicy);
	            log.ReportEvent("INFO", "Hotel " + (i + 1) + " | Price: " + hotelPrice + " | Expected Amount Policy: " + expectedAmountPolicy);
	            
	            // Check if actual policy contains both expected policies
	            boolean daysPolicyMatch = actualPolicy.contains(expectedDaysPolicy);
	            boolean amountPolicyMatch = actualPolicy.contains(expectedAmountPolicy);
	            
	            if (!daysPolicyMatch) {
	                log.ReportEvent("FAIL", "Hotel " + (i + 1) + " | DAYS POLICY MISMATCH | Days: " + daysUntilCheckIn 
	                        + " | Expected: " + expectedDaysPolicy + " | Found: " + actualPolicy);
	                allCorrect = false;
	            }
	            
	            if (!amountPolicyMatch) {
	                log.ReportEvent("FAIL", "Hotel " + (i + 1) + " | AMOUNT POLICY MISMATCH | Price: " + hotelPrice 
	                        + " | Expected: " + expectedAmountPolicy + " | Found: " + actualPolicy);
	                allCorrect = false;
	            }
	            
	            if (daysPolicyMatch && amountPolicyMatch) {
	                log.ReportEvent("PASS", "Hotel " + (i + 1) + " | BOTH POLICIES CORRECT | Days: " + daysUntilCheckIn 
	                        + " | Price: " + hotelPrice + " | Policy: " + actualPolicy);
	            }
	        }

	        if (allCorrect) {
	            log.ReportEvent("PASS", "✓ ALL INTERNATIONAL HOTELS VALIDATED SUCCESSFULLY | Days until check-in: " + daysUntilCheckIn);
	        } else {
	            screenshots.takeScreenShot1();
	            Assert.fail("International hotel policy validation failed. Days until check-in: " + daysUntilCheckIn);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error during international hotel validation: " + e.getMessage());
	        screenshots.takeScreenShot1();
	        Assert.fail("Error during international hotel validation: " + e.getMessage());
	    }
	}
	public void validateHotelPolicyBasedOnGradeOrPriceForDomestic(String[] selectedGradeFromDropdown,
	        String[] checkindateResultPage, Log log, ScreenShots screenshots) {

	    try {
	        // Construct check-in date string
	        String checkInDay = checkindateResultPage[0];
	        String checkInMonth = checkindateResultPage[1];
	        String checkInYear = checkindateResultPage[2];

	        String checkInDateStr = checkInDay + " " + checkInMonth + " " + checkInYear;
	        System.out.println("Check-in full date: " + checkInDateStr);

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
	        LocalDate checkInDate = LocalDate.parse(checkInDateStr, formatter);

	        // Calculate days from TODAY to CHECK-IN (inclusive)
	        LocalDate today = LocalDate.now();
	        int daysUntilCheckIn = (int) ChronoUnit.DAYS.between(today, checkInDate) + 1;

	        log.ReportEvent("INFO", "Starting hotel policy validation. Days until check-in (inclusive): " + daysUntilCheckIn 
	                + ", Selected Grade: " + Arrays.toString(selectedGradeFromDropdown));

	        List<WebElement> hotelCards = driver.findElements(By.xpath("//div[@class='hcard ']"));
	        log.ReportEvent("INFO", "Total hotels found: " + hotelCards.size());

	        boolean allCorrect = true;

	        // Grades that use DAYS-based policy
	        List<String> daysPolicyGrades = Arrays.asList("M-7 - ", "M-8 - ", "C-1 - ", "C-2 - ", "C-3 - ",
	                "C-4 - Senior Vice President", "C-5 - ");

	        // Check if selected grade matches days-based policy grades
	        boolean isDaysPolicyGrade = Arrays.stream(selectedGradeFromDropdown).anyMatch(daysPolicyGrades::contains);

	        for (int i = 0; i < hotelCards.size(); i++) {
	            WebElement hotelCard = hotelCards.get(i);

	            WebElement policyElement = hotelCard.findElement(By.xpath(".//div[contains(@class,'tg-policy')]"));
	            String actualPolicy = policyElement.getText().toLowerCase();

	            if (isDaysPolicyGrade) {
	                // ✅ DAYS-BASED POLICY (for specific grades)
	                // If days until check-in ≤ 7 → out of policy
	                // If days until check-in > 7 → in policy
	                String expectedPolicy = (daysUntilCheckIn <= 7) ? "out of policy" : "in policy";

	                if (!actualPolicy.contains(expectedPolicy)) {
	                    log.ReportEvent("FAIL", "Hotel " + (i + 1) + " | DAYS POLICY | Days until check-in: " + daysUntilCheckIn 
	                            + " | Expected: " + expectedPolicy + " | Found: " + actualPolicy);
	                    allCorrect = false;
	                } else {
	                    log.ReportEvent("PASS", "Hotel " + (i + 1) + " | DAYS POLICY | Correct: " + actualPolicy 
	                            + " for " + daysUntilCheckIn + " days until check-in");
	                }

	            } else {
	                // ✅ PRICE-BASED POLICY (for all other grades)
	                // Get hotel price
	                int hotelPrice;
	                
	                // Try to get price element
	                WebElement priceElement = hotelCard.findElement(By.xpath(".//*[contains(@class,'tg-hl-price')]"));
	                String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();
	                hotelPrice = Integer.parseInt(priceText);

	                // If price between 0-4000 → in policy, else → out of policy
	                String expectedPolicy = (hotelPrice >= 0 && hotelPrice <= 4000) ? "in policy" : "out of policy";

	                if (!actualPolicy.contains(expectedPolicy)) {
	                    log.ReportEvent("FAIL", "Hotel " + (i + 1) + " | PRICE POLICY | Price: ₹" + hotelPrice 
	                            + " | Expected: " + expectedPolicy + " | Found: " + actualPolicy);
	                    allCorrect = false;
	                } else {
	                    log.ReportEvent("PASS", "Hotel " + (i + 1) + " | PRICE POLICY | Correct: " + actualPolicy 
	                            + " for price: ₹" + hotelPrice);
	                }
	            }
	        }

	        if (allCorrect) {
	            log.ReportEvent("PASS", "✓ ALL HOTELS VALIDATED SUCCESSFULLY | Selected Grade: "
	                    + Arrays.toString(selectedGradeFromDropdown) + " | Days until check-in: " + daysUntilCheckIn);
	        } else {
	            screenshots.takeScreenShot1();
	            Assert.fail("Hotel policy validation failed. Selected Grade: " + Arrays.toString(selectedGradeFromDropdown));
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error during validation: " + e.getMessage());
	        screenshots.takeScreenShot1();
	        Assert.fail("Error during validation: " + e.getMessage());
	    }
	}

	public void validateHoteldaysPolicyForInternational(String[] checkindateResultPage, Log log, ScreenShots screenshots) {
	    try {
	        // Construct check-in date string
	        String checkInDay = checkindateResultPage[0];
	        String checkInMonth = checkindateResultPage[1];
	        String checkInYear = checkindateResultPage[2];

	        String checkInDateStr = checkInDay + " " + checkInMonth + " " + checkInYear;
	        System.out.println("Check-in full date: " + checkInDateStr);

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
	        LocalDate checkInDate = LocalDate.parse(checkInDateStr, formatter);

	        // Calculate days from TODAY to CHECK-IN (inclusive)
	        LocalDate today = LocalDate.now();
	        int daysUntilCheckIn = (int) ChronoUnit.DAYS.between(today, checkInDate) + 1;

	        log.ReportEvent("INFO", "Starting INTERNATIONAL hotel policy validation. Days until check-in (inclusive): " + daysUntilCheckIn);

	        List<WebElement> hotelCards = driver.findElements(By.xpath("//div[@class='hcard ']"));
	        log.ReportEvent("INFO", "Total international hotels found: " + hotelCards.size());

	        boolean allCorrect = true;

	        for (int i = 0; i < hotelCards.size(); i++) {
	            WebElement hotelCard = hotelCards.get(i);

	            // Get policy text
	            WebElement policyElement = hotelCard.findElement(By.xpath(".//div[contains(@class,'tg-policy')]"));
	            String actualPolicy = policyElement.getText().toLowerCase();
	            
	            // Get hotel price
	            int hotelPrice;
	            
	            // Try to get price element (international hotels might have different price format)
	            try {
	                WebElement priceElement = hotelCard.findElement(By.xpath(".//*[contains(@class,'tg-hl-price')]"));
	                String priceText = priceElement.getText().replace("$", "").replace(",", "").trim(); // For USD
	                // If it's in other currencies, adjust accordingly
	                if (priceText.contains("₹")) {
	                    priceText = priceText.replace("₹", "").replace(",", "").trim();
	                }
	                hotelPrice = Integer.parseInt(priceText);
	            } catch (Exception e) {
	                // Try alternative price locator
	                WebElement priceElement = hotelCard.findElement(By.xpath(".//*[contains(@class,'price')]"));
	                String priceText = priceElement.getText().replaceAll("[^0-9]", "").trim();
	                hotelPrice = Integer.parseInt(priceText);
	            }

	            // LOGIC: First calculate days, then check amount
	            // If amount is 0-13600 → in policy, else → out of policy
	            // (Days calculation is already done above for logging purposes)
	            
	            String expectedPolicy = (hotelPrice >= 0 && hotelPrice <= 13600) ? "in policy" : "out of policy";

	            log.ReportEvent("INFO", "Hotel " + (i + 1) + " | Days until check-in: " + daysUntilCheckIn 
	                    + " | Price: " + hotelPrice + " | Expected: " + expectedPolicy);

	            if (!actualPolicy.contains(expectedPolicy)) {
	                log.ReportEvent("FAIL", "Hotel " + (i + 1) + " | Days until check-in: " + daysUntilCheckIn 
	                        + " | Price: " + hotelPrice 
	                        + " | Expected Policy: " + expectedPolicy + " | Found: " + actualPolicy);
	                allCorrect = false;
	            } else {
	                log.ReportEvent("PASS", "Hotel " + (i + 1) + " | Correct policy: " + actualPolicy 
	                        + " for price: " + hotelPrice + " | Days until check-in: " + daysUntilCheckIn);
	            }
	        }

	        if (allCorrect) {
	            log.ReportEvent("PASS", "✓ ALL INTERNATIONAL HOTELS VALIDATED SUCCESSFULLY | Days until check-in: " + daysUntilCheckIn);
	        } else {
	            screenshots.takeScreenShot1();
	            Assert.fail("International hotel policy validation failed. Days until check-in: " + daysUntilCheckIn);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error during international hotel validation: " + e.getMessage());
	        screenshots.takeScreenShot1();
	        Assert.fail("Error during international hotel validation: " + e.getMessage());
	    }
	}

	
	// ---------------flights -------------------
	
	public List<String> getAllPolicyTextsFromFlightCards() {

	    By policyLocator = By.xpath("//div[contains(@class,'tg-policy')]");

	    List<WebElement> elements = driver.findElements(policyLocator);

	    List<String> texts = new ArrayList<>();

	    for (WebElement element : elements) {
	        texts.add(element.getText());
	    }

	    return texts;
	}



	
	
	public String getFlightCardPolicyByIndex(int index) {
	    try {
	        // Wait until at least one policy element is visible
	        List<WebElement> policyElements = new WebDriverWait(driver, Duration.ofSeconds(10))
	                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
	                        By.xpath("//div[contains(@class,'tg-policy')]")
	                ));

	        // Check index is valid
	        if (index < 0 || index >= policyElements.size()) {
	            throw new RuntimeException("Invalid index: " + index 
	                    + ". Available policies count: " + policyElements.size());
	        }

	        // Return trimmed text
	        return policyElements.get(index).getText().trim();

	    } catch (Exception e) {
	        throw new RuntimeException("Error getting flight card policy at index " + index + ": " + e.getMessage(), e);
	    }
	}
	
	//---method to get flights policy text
		public List<String> getAllPolicyTextsFromFlightFareCards(WebDriver driver) {
		    By policyLocator = By.xpath("//div[contains(@class,'fare-options-container')]//div[contains(@class,'tg-policy')]");
		    List<WebElement> elements = driver.findElements(policyLocator);
		    
		    List<String> texts = new ArrayList<>();
		    for (WebElement element : elements) {
		        texts.add(element.getText());
		    }
		    
		    return texts;
		}
	
		public void validateSelectedFlightCardPolicyAgainstFareCards(int userSelectedCardNumber, Log log, ScreenShots screenshots) {
		    try {
		        log.ReportEvent("INFO", "Validating for Card #" + userSelectedCardNumber);
		        
		        // Convert user card number (1-based) to code index (0-based)
		        int codeIndex = userSelectedCardNumber - 1;
		        
		        // Get policy from selected flight card using your existing method
		        String flightCardPolicy = getFlightCardPolicyByIndex(codeIndex);
		        log.ReportEvent("INFO", "Flight Card #" + userSelectedCardNumber + " Policy: '" + flightCardPolicy + "'");
		        
		        // Get all fare card policies using your existing method
		        List<String> fareCardPolicies = getAllPolicyTextsFromFlightFareCards(driver);
		        log.ReportEvent("INFO", "Fare Card Policies: " + fareCardPolicies);
		        
		        // Check if flight card policy matches ANY fare card policy
		        boolean matchFound = false;
		        for (String farePolicy : fareCardPolicies) {
		            if (flightCardPolicy.equalsIgnoreCase(farePolicy)) {
		                matchFound = true;
		                log.ReportEvent("PASS", "✓ Match found! Flight policy '" + flightCardPolicy + 
		                                       "' matches fare policy '" + farePolicy + "'");
		                break;
		            }
		        }
		        
		        // Final result
		        if (matchFound) {
		            log.ReportEvent("PASS", "✓ Card #" + userSelectedCardNumber + " validation PASSED");
		        } else {
		            log.ReportEvent("FAIL", "✗ Card #" + userSelectedCardNumber + " policy '" + 
		                                   flightCardPolicy + "' does not match any fare policy: " + fareCardPolicies);
		            screenshots.takeScreenShot1();
		            Assert.fail("Policy mismatch for Card #" + userSelectedCardNumber);
		        }
		        
		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Error validating flight card policy: " + e.getMessage());
		        screenshots.takeScreenShot1();
		        Assert.fail("Error validating flight card policy: " + e.getMessage());
		    }
		}
		
		public String[] getOnwardDateTextFromResultPage() {

	    WebElement dateElement = new WebDriverWait(driver, Duration.ofSeconds(10))
	            .until(ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("(//div[@class=' tg-typography tg-typography_subtitle-6 fw-600 tg-typography_default'])[2]")));

	    String fullText = dateElement.getText().trim();
	    System.out.println("Raw text: " + fullText);

	    // Remove ordinal suffix (st, nd, rd, th)
	    fullText = fullText.replaceAll("(?<=\\d)(st|nd|rd|th)", "");

	    // Remove comma
	    fullText = fullText.replace(",", "");

	    System.out.println("Cleaned text: " + fullText);

	    return fullText.split("\\s+");
	}


		public void validateFlightPolicyAgainstDaysForDomestic(String[] onwardDate, Log log, ScreenShots screenshots) {
		    try {
		        // Get current date
		        LocalDate currentDate = LocalDate.now();
		        
		        // Construct onward date string from user selection
		        String onwardDateStr = onwardDate[0] + " " + onwardDate[1] + " " + onwardDate[2];
		        
		        System.out.println("Current Date: " + currentDate);
		        System.out.println("Selected Onward Date: " + onwardDateStr);
		        
		        // Parse the selected onward date
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		        LocalDate selectedDate = LocalDate.parse(onwardDateStr, formatter);
		        
		        // Calculate days INCLUDING both start and end dates
		        long daysBetween = ChronoUnit.DAYS.between(currentDate, selectedDate);
		        int daysCount = (int) daysBetween + 1;  // +1 to make it inclusive
		        
		        System.out.println("Days from today to travel (inclusive): " + daysCount);
		        log.ReportEvent("INFO", "Current Date: " + currentDate + ", Selected Date: " + selectedDate + 
		                               ", Total Days (inclusive): " + daysCount);
		        
		        // Determine expected policy based on days count
		        String expectedPolicy;
		        if (daysCount <= 7) {
		            expectedPolicy = "out of policy";  // If total days (inclusive) ≤ 7 = out of policy
		        } else {
		            expectedPolicy = "in policy";       // If total days > 7 = in policy
		        }
		        
		        log.ReportEvent("INFO", "Expected policy based on " + daysCount + " total days (inclusive): " + expectedPolicy);
		        
		        // Get all flight card policy texts
		        List<String> flightPolicies = getAllPolicyTextsFromFlightCards();
		        log.ReportEvent("INFO", "Total flight cards found: " + flightPolicies.size());
		        
		        if (flightPolicies.isEmpty()) {
		            log.ReportEvent("WARNING", "No flight policies found to validate");
		            screenshots.takeScreenShot1();
		            return;
		        }
		        
		        boolean allCorrect = true;
		        
		        // Validate each flight card
		        for (int i = 0; i < flightPolicies.size(); i++) {
		            String actualPolicy = flightPolicies.get(i).toLowerCase().trim();
		            
		            if (actualPolicy.contains(expectedPolicy)) {
		                log.ReportEvent("PASS", "Flight " + (i + 1) + " correctly shows: " + actualPolicy);
		            } else {
		                log.ReportEvent("FAIL", "Flight " + (i + 1) + " | Total days (inclusive): " + daysCount 
		                        + " | Expected: " + expectedPolicy + " | Found: " + actualPolicy);
		                allCorrect = false;
		            }
		        }
		        
		        // Final result
		        if (allCorrect) {
		            log.ReportEvent("PASS", "✓ ALL FLIGHT CARDS VALIDATED - Total days (inclusive): " + daysCount 
		                    + ", Applied Policy: " + expectedPolicy);
		        } else {
		            log.ReportEvent("FAIL", "✗ POLICY VALIDATION FAILED - Total days (inclusive): " + daysCount);
		            screenshots.takeScreenShot1();
		            Assert.fail("Flight policy validation failed. Expected: " + expectedPolicy + " for " + daysCount + " total days (inclusive)");
		        }
		        
		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Error during flight validation: " + e.getMessage());
		        screenshots.takeScreenShot1();
		        Assert.fail("Error during flight validation: " + e.getMessage());
		    }
		}
		
		public void validateFlightPolicyAgainstDaysForInternational(String[] onwardDate, Log log, ScreenShots screenshots) {
		    try {
		        // Get current date
		        LocalDate currentDate = LocalDate.now();
		        
		        // Construct onward date string from user selection
		        String onwardDateStr = onwardDate[0] + " " + onwardDate[1] + " " + onwardDate[2];
		        
		        System.out.println("Current Date: " + currentDate);
		        System.out.println("Selected Onward Date: " + onwardDateStr);
		        
		        // Parse the selected onward date
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		        LocalDate selectedDate = LocalDate.parse(onwardDateStr, formatter);
		        
		        // Calculate days INCLUDING both start and end dates
		        long daysBetween = ChronoUnit.DAYS.between(currentDate, selectedDate);
		        int daysCount = (int) daysBetween + 1;  // +1 to make it inclusive
		        
		        System.out.println("Days from today to travel (inclusive): " + daysCount);
		        log.ReportEvent("INFO", "Current Date: " + currentDate + ", Selected Date: " + selectedDate + 
		                               ", Total Days (inclusive): " + daysCount);
		        
		        // Determine expected policy based on days count
		        String expectedPolicy;
		        if (daysCount <= 15) {
		            expectedPolicy = "out of policy";  // If total days (inclusive) ≤ 7 = out of policy
		        } else {
		            expectedPolicy = "in policy";       // If total days > 7 = in policy
		        }
		        
		        log.ReportEvent("INFO", "Expected policy based on " + daysCount + " total days (inclusive): " + expectedPolicy);
		        
		        // Get all flight card policy texts
		        List<String> flightPolicies = getAllPolicyTextsFromFlightCards();
		        log.ReportEvent("INFO", "Total flight cards found: " + flightPolicies.size());
		        
		        if (flightPolicies.isEmpty()) {
		            log.ReportEvent("WARNING", "No flight policies found to validate");
		            screenshots.takeScreenShot1();
		            return;
		        }
		        
		        boolean allCorrect = true;
		        
		        // Validate each flight card
		        for (int i = 0; i < flightPolicies.size(); i++) {
		            String actualPolicy = flightPolicies.get(i).toLowerCase().trim();
		            
		            if (actualPolicy.contains(expectedPolicy)) {
		                log.ReportEvent("PASS", "Flight " + (i + 1) + " correctly shows: " + actualPolicy);
		            } else {
		                log.ReportEvent("FAIL", "Flight " + (i + 1) + " | Total days (inclusive): " + daysCount 
		                        + " | Expected: " + expectedPolicy + " | Found: " + actualPolicy);
		                allCorrect = false;
		            }
		        }
		        
		        // Final result
		        if (allCorrect) {
		            log.ReportEvent("PASS", "✓ ALL FLIGHT CARDS VALIDATED - Total days (inclusive): " + daysCount 
		                    + ", Applied Policy: " + expectedPolicy);
		        } else {
		            log.ReportEvent("FAIL", "✗ POLICY VALIDATION FAILED - Total days (inclusive): " + daysCount);
		            screenshots.takeScreenShot1();
		            Assert.fail("Flight policy validation failed. Expected: " + expectedPolicy + " for " + daysCount + " total days (inclusive)");
		        }
		        
		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Error during flight validation: " + e.getMessage());
		        screenshots.takeScreenShot1();
		        Assert.fail("Error during flight validation: " + e.getMessage());
		    }
		}
}
