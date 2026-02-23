package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

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
	
	public void selectBusDate(String returnDate, String returnMonthAndYear, Log Log) throws InterruptedException {
	    clickBusDate();

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
		
		
				//Method to add room, adt and child
		public void fillRoomDetails(String roomCountStr, String adultCountStr, String childCountStr, String childAgesStr, Log Log) {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    int roomCount = Integer.parseInt(roomCountStr.trim());
		    String[] adultArray = adultCountStr.split(",");
		    String[] childArray = (childCountStr != null && !childCountStr.isEmpty()) ? childCountStr.split(",") : null;
		    String[] roomChildAges = (childAgesStr != null && !childAgesStr.isEmpty()) ? childAgesStr.split(";") : null;

		    try {
		        // Try multiple approaches to click the expand icon
		        boolean dropdownOpened = false;

		        // Approach 1: JavaScript click with scroll into center view
		        try {
		            WebElement expandIcon = wait.until(ExpectedConditions.elementToBeClickable(
		                    By.xpath("//*[@data-testid='ExpandMoreIcon']")));
		            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", expandIcon);
		            Thread.sleep(500);
		            js.executeScript("arguments[0].click();", expandIcon);
		            dropdownOpened = true;
		        } catch (Exception e) {
		            Log.ReportEvent("WARN", "JavaScript click failed: " + e.getMessage());
		        }

		        // Approach 2: Selenium click if JS click failed
		        if (!dropdownOpened) {
		            try {
		                WebElement expandIcon = wait.until(ExpectedConditions.elementToBeClickable(
		                        By.xpath("//*[@data-testid='ExpandMoreIcon']")));
		                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", expandIcon);
		                Thread.sleep(500);
		                expandIcon.click();
		                dropdownOpened = true;
		            } catch (Exception e) {
		                Log.ReportEvent("WARN", "Selenium click failed: " + e.getMessage());
		            }
		        }

		        // Approach 3: Try clicking parent element if previous clicks failed
		        if (!dropdownOpened) {
		            try {
		                WebElement parentElement = wait.until(ExpectedConditions.elementToBeClickable(
		                        By.xpath("//*[@data-testid='ExpandMoreIcon']/..")));
		                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", parentElement);
		                Thread.sleep(500);
		                js.executeScript("arguments[0].click();", parentElement);
		                Log.ReportEvent("INFO", "Clicked parent element of expand icon");
		                dropdownOpened = true;
		            } catch (Exception e) {
		                Log.ReportEvent("WARN", "Parent click failed: " + e.getMessage());
		            }
		        }

		        if (!dropdownOpened) {
		            Log.ReportEvent("ERROR", "Could not open room dropdown");
		            return;
		        }

		        Thread.sleep(3000); // Wait for dropdown UI to load

		        // Get current room count
		        WebElement roomCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
		                By.xpath("//div[@class='room-plusminus']/span[@class='roomlength']")));
		        int currentRooms = Integer.parseInt(roomCountElement.getText());

		        // Add rooms if needed
		        if (roomCount > currentRooms) {
		            WebElement addRoomButton = wait.until(ExpectedConditions.elementToBeClickable(
		                    By.xpath("//div[@class='room-plusminus']/*[3]")));

		            Actions actions = new Actions(driver);

		            for (int i = currentRooms; i < roomCount; i++) {
		                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addRoomButton);
		                Thread.sleep(300);

		                try {
		                    actions.moveToElement(addRoomButton).pause(Duration.ofMillis(300)).click().perform();
		                    Log.ReportEvent("INFO", "Clicked using Actions for room: " + (i + 1));
		                } catch (Exception e) {
		                    Log.ReportEvent("WARN", "Actions click failed: " + e.getMessage());
		                }

		                Thread.sleep(1000);
		            }
		        }

		        // Process each room
		        for (int roomIndex = 0; roomIndex < roomCount; roomIndex++) {
		            int roomNumber = roomIndex + 1;
		            //Log.ReportEvent("INFO", "Processing Room " + roomNumber);

		            // Set adults for this room
		            int adultsNeeded = Integer.parseInt(adultArray[roomIndex].trim());
		            setAdultsForRoom(roomNumber, adultsNeeded, Log);

		            // Set children and ages for this room
		            if (childArray != null && roomIndex < childArray.length) {
		                int childrenNeeded = Integer.parseInt(childArray[roomIndex].trim());
		                String[] childAgesForRoom = null;

		                if (roomChildAges != null && roomIndex < roomChildAges.length) {
		                    childAgesForRoom = roomChildAges[roomIndex].split(",");
		                }

		                setChildrenForRoom(roomNumber, childrenNeeded, childAgesForRoom, Log);
		            }
		        }

		        // Click Done button
		        WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(
		                By.xpath("//button[contains(text(), 'Done')]")));
		        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", doneButton);
		        Thread.sleep(500);
		        js.executeScript("arguments[0].click();", doneButton);
		        Log.ReportEvent("INFO", "Clicked Done button successfully");

		    } catch (Exception e) {
		        Log.ReportEvent("ERROR", "Failed to fill room details: " + e.getMessage());
		    }
		}

		private void setAdultsForRoom(int roomNumber, int adultsNeeded, Log Log) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        Actions actions = new Actions(driver);

		        // Get current adult count - first adultvalue span in the room
		        WebElement adultCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("(//div[contains(@class,'rooms-list')]//div[contains(@class,'stepper-buttons')][" + roomNumber + "]//span[@class='adultvalue'])[1]")));

		        int currentAdults = Integer.parseInt(adultCountElement.getText().trim());
		        int clicksNeeded = adultsNeeded - currentAdults;

		        if (clicksNeeded == 0) {
		            Log.ReportEvent("INFO", "Room " + roomNumber + " already has " + adultsNeeded + " adults");
		            return;
		        }

		        // XPath for adult plus or minus button
		        String buttonXPath = clicksNeeded > 0 ? 
		            "(//div[contains(@class,'rooms-list')]//div[contains(@class,'stepper-buttons')][" + roomNumber + "]//div[contains(@class,'plusminus')]//svg[2])[1]" : // Plus button
		            "(//div[contains(@class,'rooms-list')]//div[contains(@class,'stepper-buttons')][" + roomNumber + "]//div[contains(@class,'plusminus')]//svg[1])[1]"; // Minus button

		        for (int i = 0; i < Math.abs(clicksNeeded); i++) {
		            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
		            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
		            actions.moveToElement(button).pause(Duration.ofMillis(300)).click().perform();
		            Thread.sleep(500);
		        }

		        Log.ReportEvent("INFO", "Room " + roomNumber + ": Set adults to " + adultsNeeded);

		    } catch (Exception e) {
		        Log.ReportEvent("ERROR", "Failed to set adults for room " + roomNumber + ": " + e.getMessage());
		    }
		}

		private void setChildrenForRoom(int roomNumber, int childrenNeeded, String[] childAges, Log Log) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        Actions actions = new Actions(driver);

		        // Get current child count - second adultvalue span in the room (for children)
		        WebElement childCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("(//div[contains(@class,'rooms-list')]//div[contains(@class,'stepper-buttons')][" + roomNumber + "]//span[@class='adultvalue'])[2]")));

		        int currentChildren = Integer.parseInt(childCountElement.getText().trim());
		        int clicksNeeded = childrenNeeded - currentChildren;

		        if (clicksNeeded <= 0) {
		            Log.ReportEvent("INFO", "Room " + roomNumber + " already has " + childrenNeeded + " children");
		            return;
		        }

		        // XPath for child plus button (second plusminus div, second SVG)
		        String buttonXPath = "(//div[contains(@class,'rooms-list')]//div[contains(@class,'stepper-buttons')][" + roomNumber + "]//div[contains(@class,'plusminus')]//svg[2])[2]";

		        for (int i = 0; i < clicksNeeded; i++) {
		            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
		            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addButton);
		            actions.moveToElement(addButton).pause(Duration.ofMillis(300)).click().perform();
		            Thread.sleep(1000);
		            
		            // Set child age if provided
		            if (childAges != null && i < childAges.length) {
		                setChildAge(roomNumber, currentChildren + i + 1, childAges[i], Log);
		            }
		        }

		        Log.ReportEvent("INFO", "Room " + roomNumber + ": Set children to " + childrenNeeded);

		    } catch (Exception e) {
		        Log.ReportEvent("ERROR", "Failed to set children for room " + roomNumber + ": " + e.getMessage());
		    }
		}

		private void setChildAge(int roomNumber, int childIndex, String age, Log Log) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        Actions actions = new Actions(driver);

		        // Find the age dropdown for the specific child
		        // This XPath might need adjustment based on how age dropdowns appear
		        WebElement ageDropdown = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("(//div[contains(@class,'rooms-list')]//div[contains(@class,'stepper-buttons')][" + roomNumber + "]//select)[" + childIndex + "]")));

		        // Select the age using Select class
		        Select select = new Select(ageDropdown);
		        select.selectByVisibleText(age);

		        Log.ReportEvent("INFO", "Room " + roomNumber + ": Set child " + childIndex + " age to " + age);

		    } catch (Exception e) {
		        Log.ReportEvent("ERROR", "Failed to set child age for room " + roomNumber + ": " + e.getMessage());
		    }
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
