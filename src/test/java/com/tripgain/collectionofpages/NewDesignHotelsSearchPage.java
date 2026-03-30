package com.tripgain.collectionofpages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;

public class NewDesignHotelsSearchPage {
	WebDriver driver;

	public NewDesignHotelsSearchPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}


	//Method to click on hotels 
	
	public void clickOnHotels() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[text()='Hotel']")).click();
	}

//	public void enterDestinationForHotels(String city, Log log) {
//	    try {
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//	        js.executeScript("document.body.style.zoom='80%'");
//
//	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
//	        WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(
//	            By.xpath("//*[contains(@class,'tg-async-select__input')]//input")));
//
//	        searchField.clear();
//	        searchField.sendKeys(city);
//
//	        WebElement firstOption = wait.until(ExpectedConditions.elementToBeClickable(
//	            By.xpath("//*[contains(@id,'react-select') and contains(@id,'option-0')]")));
//
//	        js.executeScript("arguments[0].scrollIntoView(true);", firstOption);
//	        js.executeScript("arguments[0].click();", firstOption);
//
//	        String selectedLocation = firstOption.getText();
//	        Thread.sleep(1000); // Optional short wait
//
//	        log.ReportEvent("PASS", "Selected Location: " + selectedLocation);
//
//	    } catch (Exception e) {
//	        log.ReportEvent("ERROR", "Failed to select a location: " + e.getMessage());
//	    }
//	}
	
	public void enterDestinationForHotels(String city, Log Log) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	        WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//*[contains(@class,'tg-async-select__input')]//input")));

	        searchField.click();
	        searchField.clear();
	        searchField.sendKeys(city);

	        // ✅ Wait until dropdown options appear
	        List<WebElement> options = wait.until(
	                ExpectedConditions.presenceOfAllElementsLocatedBy(
	                        By.xpath("//div[contains(@id,'react-select') and contains(@id,'option')]")));

	        // ✅ Click first VISIBLE option using JS (most reliable)
	        WebElement firstOption = options.get(0);

	        String selectedLocation = firstOption.getText();

	        ((JavascriptExecutor) driver)
	                .executeScript("arguments[0].scrollIntoView(true);", firstOption);

	        ((JavascriptExecutor) driver)
	                .executeScript("arguments[0].click();", firstOption);

	        Log.ReportEvent("PASS", "Selected Location: " + selectedLocation);

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "Failed to select location: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@FindBy(xpath = "(//*[@class='custom_datepicker_input'])[1]")
	WebElement datePickerInput;
	
	public void clickDate() {
		driver.findElement(By.xpath("(//div[contains(@class,' min-date-width')])[1]")).click();
	}
	
	public void clickBusDate() {
		//driver.findElement(By.xpath("//div[contains(@class,'field date-range')]")).click();
		WebElement el = driver.findElement(By.xpath("//div[contains(@class,'field date-range')]"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", el);

	}
	
	//Method to Select Check-In Date By Passing Two Paramenters(Date and MounthYear)
//	public void selectDate(String day, String MonthandYear,Log Log) throws InterruptedException {
//	    TestExecutionNotifier.showExecutionPopup();
//
//	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	    datePickerInput.click();
//
//	    // Wait until the calendar is visible and get the current month & year
//	    wait.until(ExpectedConditions.visibilityOfElementLocated(
//	        By.xpath("//div[@class='custom-header']")
//	    ));
//
//	    String currentMonthYear = driver.findElement(
//	        By.xpath("//div[@class='custom-header']")
//	    ).getText();
//	    System.out.println(currentMonthYear);
//
//	    // Loop to navigate to the desired month
//	    while (!currentMonthYear.equals(MonthandYear)) {
//	        driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
//	        wait.until(ExpectedConditions.textToBePresentInElementLocated(
//	            By.xpath("//*[@class='custom-header']"),
//	            MonthandYear
//	        ));
//
//	        currentMonthYear = driver.findElement(
//	            By.xpath("//*[@class='custom-header']")
//	        ).getText();
//	    }
//
//	    // Once the correct month is displayed, select the day
//	    
//	    
//	     WebElement dateElement = driver.findElement(By.xpath("//span[text()='" + day + "']"));
//	     dateElement.click();
////	    WebElement dateElement = wait.until(ExpectedConditions.elementToBeClickable(
////	        By.xpath("(//div[@class='react-datepicker'])[1]//span[text()='" + day + "']")
////	    ));
//
////	    try {
////	        Actions actions = new Actions(driver);
////	        actions.moveToElement(dateElement).pause(Duration.ofMillis(200)).click().perform();
////	    } catch (ElementClickInterceptedException e) {
////	        dateElement = driver.findElement(By.xpath("(//div[@class='react-datepicker'])[1]//span[text()='" + day + "']"));
////	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateElement);
////	    }
//
//
//
//	    
////	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dateElement);
////	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateElement);
//
//	    Log.ReportEvent("INFO", "Selected Date: " + day + " " + MonthandYear);
//	}
//	
	
	public void selectDate(String returnDate, String returnMonthAndYear, Log Log) throws InterruptedException {
	    clickDate();

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='custom-header']")));

	    String currentMonthYear = driver.findElement(By.xpath("//div[@class='custom-header']")).getText();
	    System.out.println("Current calendar: " + currentMonthYear);

	    // Navigate to correct month if needed
	    while (!currentMonthYear.trim().equalsIgnoreCase(returnMonthAndYear.trim())) {
	        driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
	        wait.until(ExpectedConditions.textToBePresentInElementLocated(
	            By.xpath("//div[@class='custom-header']"),
	            returnMonthAndYear
	        ));
	        currentMonthYear = driver.findElement(By.xpath("//div[@class='custom-header']")).getText();
	    }

        driver.findElement(By.xpath("//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and not(contains(@class, 'disabled'))]//span[@class='day' and text()='" + returnDate + "']")).click();


	    Log.ReportEvent("INFO", "Selected date: " + returnDate + " " + returnMonthAndYear);
	}
	
	
	@FindBy(xpath = "//div[contains(@class,'field date-range tg-fs-dp')]")
	WebElement selectjourdate;

	public String selectBusJourneyDate(String returnDate, String MonthandYear) throws InterruptedException {

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




		//Method to Click on Check-Out  Date
		public void clickOnReturnDate()
		{
			driver.findElement(By.xpath("(//div[contains(@class,' min-date-width')])[2]")).click();
		}
		
		//Method to Select Return Date By Passing Two Paramenters(Date and MounthYear)
		public void selectReturnDate(String returnDate, String returnMonthAndYear,Log Log) throws InterruptedException {
		    // Log the attempt to select the return date
		    Log.ReportEvent("INFO", "Attempting to select return date: " + returnDate + " " + returnMonthAndYear);

		    clickOnReturnDate();
		    String currentMonthYear = driver.findElement(By.xpath("//div[@class='custom-header']")).getText();

		    // Log the current month and year displayed in the date picker

		    if (currentMonthYear.contentEquals(returnMonthAndYear)) {
		    	// Select date that is NOT from outside month and NOT disabled
		        driver.findElement(By.xpath("//div[@class='react-datepicker__month']//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'disabled'))]//span[@class='day' and text()='" + returnDate + "']")).click();

		        // Log the selected return date
		    } else {
		        while (!currentMonthYear.contentEquals(returnMonthAndYear)) {
		            Thread.sleep(500);
		            driver.findElement(By.xpath("(//button[contains(@class,'nav-arrow')])[2]")).click();
		            currentMonthYear = driver.findElement(By.xpath("//div[@class='custom-header']")).getText();

		            // Log the navigation to the next month
		          //  Log.ReportEvent("INFO", "Navigated to: " + currentMonthYear);
		        }

		     // Select date that is NOT from outside month and NOT disabled
		        driver.findElement(By.xpath("//div[@class='react-datepicker__month']//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'disabled'))]//span[@class='day' and text()='" + returnDate + "']")).click();
		        // Log the selected return date
		        Log.ReportEvent("INFO", "Selected return date: " + returnDate + " " + returnMonthAndYear);
		    }
		}
		
//			public static void configureHotelBookingRooms(String totalRooms, String adultsStr, String childrenStr, String agesStr, WebDriver driver) {
//		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//		        // 1. Open Guest Section
//		        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'tg-hl-guests-section')]"))).click();
//
//		        // 2. Add Rooms
//		        int targetRooms = Integer.parseInt(totalRooms);
//		        WebElement roomCountSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='room-plusminus']/span[@class='roomlength']")));
//		        int currentRooms = Integer.parseInt(roomCountSpan.getText());
//
//		        while (currentRooms < targetRooms) {
//		            driver.findElement(By.xpath("//div[contains(@class, 'room-plusminus')]/*[3]")).click();
//		            currentRooms++;
//		        }
//
//		        // 3. Parse comma-separated strings into arrays
//		        String[] adultsArr = adultsStr.split(",");
//		        String[] childrenArr = childrenStr.split(",");
//
//		        // 4. Loop through each room
//		        for (int i = 0; i < targetRooms; i++) {
//		            int roomNum = i + 1;
//		            int targetAdults = Integer.parseInt(adultsArr[i]);
//		            int targetChildren = Integer.parseInt(childrenArr[i]);
//
//		            // Select Adults (Default is 1, so click + target-1 times)
//		            String adultPlusXpath = "//span[text()='Rooms']/following-sibling::span[text()='" + roomNum + "']/ancestor::div[contains(@class, 'MuiGrid2-grid-xs-12')]//p[text()='Adults']/ancestor::div[contains(@class, 'stepper-buttons')]//*[local-name()='svg'][2]";
//		            for (int a = 0; a < (targetAdults - 1); a++) {
//		                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(adultPlusXpath))).click();
//		            }
//
//		            // Select Children (Default is 0, so click + target times)
//		            String childPlusXpath = "//span[text()='Rooms']/following-sibling::span[text()='" + roomNum + "']/ancestor::div[contains(@class, 'MuiGrid2-grid-xs-12')]//p[text()='Children']/ancestor::div[contains(@class, 'stepper-buttons')]//*[local-name()='svg'][2]";
//		            for (int c = 0; c < targetChildren; c++) {
//		                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(childPlusXpath))).click();
//		            }
//		        }
//
//		        // 5. Done
//		        driver.findElement(By.xpath("//button[text()='Done']")).click();
//		    }
		
	
	
	
		    public static void configureHotelBookingRooms(String totalRooms,
		                                                  String adultsStr,
		                                                  String childrenStr,
		                                                  String agesStr,
		                                                  WebDriver driver) {

		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        Random random = new Random();

		        // 1️⃣ Open Guest Section
		        WebElement guestSection = wait.until(ExpectedConditions.elementToBeClickable(
		                By.xpath("//button[contains(@class,'tg-hl-guests-section')]")
		        ));
		        js.executeScript("arguments[0].click();", guestSection);

		        // 2️⃣ Add Rooms
		        int targetRooms = Integer.parseInt(totalRooms);
		        WebElement roomCountSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(
		                By.xpath("//span[contains(@class,'roomlength')]")
		        ));

		        int currentRooms = Integer.parseInt(roomCountSpan.getText());
		        while (currentRooms < targetRooms) {
		            WebElement addRoomBtn = wait.until(ExpectedConditions.elementToBeClickable(
		                    By.xpath("//div[contains(@class,'room-plusminus')]//*[name()='svg'][last()]")
		            ));
		            js.executeScript("arguments[0].click();", addRoomBtn);
		            currentRooms++;
		            try { Thread.sleep(500); } catch (Exception e) {} 
		        }

		        String[] adultsArr = adultsStr.split(",");
		        String[] childrenArr = childrenStr.split(",");

		        // 3️⃣ Loop each room
		        for (int i = 0; i < targetRooms; i++) {
		            int roomNum = i + 1;
		            int targetAdults = Integer.parseInt(adultsArr[i]);
		            int targetChildren = Integer.parseInt(childrenArr[i]);

		            // 👉 Room container - Scroll into center so Room 2/3 are visible
		            By roomBy = By.xpath("(//div[contains(@class,'rooms-list')]/div)[" + roomNum + "]");
		            WebElement room = wait.until(ExpectedConditions.presenceOfElementLocated(roomBy));
		            js.executeScript("arguments[0].scrollIntoView({block:'center'});", room);
		            try { Thread.sleep(500); } catch (Exception e) {}

		            // --- ADULTS ---
		            String adultPlusXpath = "(//div[contains(@class,'rooms-list')]/div)[" + roomNum + "]//p[text()='Adults']/ancestor::div[contains(@class,'stepper-buttons')]//*[name()='svg'][last()]";
		            for (int a = 1; a < targetAdults; a++) {
		                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(adultPlusXpath))).click();
		            }

		            // --- CHILDREN ---
		            String childPlusXpath = "(//div[contains(@class,'rooms-list')]/div)[" + roomNum + "]//p[text()='Children']/ancestor::div[contains(@class,'stepper-buttons')]//*[name()='svg'][last()]";
		            for (int c = 0; c < targetChildren; c++) {
		                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(childPlusXpath))).click();
		                try { Thread.sleep(400); } catch (Exception e) {} // Give UI time to generate age dropdown
		            }

		            // --- CHILD AGE DROPDOWNS ---
		            if (targetChildren > 0) {
		                // FIXED XPATH: Removed the backslashes and extra quotes
		                String ageDropdownXpath = "(//div[contains(@class,'rooms-list')]/div)[" + roomNum + "]//input[@name='childAge']/following-sibling::*[local-name()='svg']";

		                // Wait for dropdowns to appear in DOM
		                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(ageDropdownXpath)));
		                List<WebElement> dropdowns = driver.findElements(By.xpath(ageDropdownXpath));

		                for (int k = 0; k < dropdowns.size(); k++) {
		                    WebElement dropdownSVG = dropdowns.get(k);

		                    // Scroll specifically to the SVG
		                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdownSVG);
		                    try { Thread.sleep(500); } catch (Exception e) {}

		                    // 💥 CLICK ON DROPDOWN: Using MouseEvent to force click the SVG
		                    js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));", dropdownSVG);

		                    // Wait for the popup menu
		                    WebElement listbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));

		                    // 🎲 Random age (1–11)
		                    int randomAge = random.nextInt(11) + 1;
		                    WebElement ageOption = wait.until(ExpectedConditions.elementToBeClickable(
		                            By.xpath("//li[@role='option' and @data-value='" + randomAge + "']")));
		                    
		                    // Click the age value
		                    js.executeScript("arguments[0].click();", ageOption);

		                    // Wait for menu to close before moving to next child
		                    wait.until(ExpectedConditions.invisibilityOf(listbox));
		                    try { Thread.sleep(600); } catch (InterruptedException e) {}
		                }
		            }
		        }

		        // 4️⃣ Click Done
		        WebElement doneBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Done']")));
		        js.executeScript("arguments[0].click();", doneBtn);
		    }
		
		       
		
		
		
		 public void clickOnSearchHotelBut() {
			    try {
			        // Click the "Search Hotel" button
			        driver.findElement(By.xpath("//button[text()='Search Hotels']")).click();

			        // Wait for the expected result section to appear
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			        wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//div[contains(@class,'MuiGrid2-root MuiGrid2-direction-xs-row MuiGrid2-grid-lg-8 MuiGrid2-grid-md-8 MuiGrid2-grid-sm-8 MuiGrid2-grid-xs-12 h-100 css-1fteupx')]")
			        ));

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
		  
		
	
}
