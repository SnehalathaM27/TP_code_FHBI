package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class NewDesign_AwaitingApprovalScreen {
	WebDriver driver;

	public NewDesign_AwaitingApprovalScreen (WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	//Method to get the details in awaiting approval screen 
	
	public void waitUntilAwaitingPageLoads(Log log,ScreenShots screenshots) {
	    try {
	        // Wait up to 20 seconds for the element to appear
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        WebElement awaitingPage = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("(//div[contains(@class,'trip_card__container')])[1]"))); // update your actual XPath here

	        // If element is found
	       // log.ReportEvent("PASS", "Awaiting Approval page is displayed.");
	        System.out.println("Awaiting page text: " + awaitingPage.getText());

	    } catch (Exception e) {
	        String screenText = driver.findElement(By.tagName("body")).getText();
	        log.ReportEvent("FAIL", "Awaiting page not loaded. Visible text: " + screenText);
	        screenshots.takeScreenShot1();
	        Assert.fail("Awaiting page not loaded");
	    }
	}
	
	public String[] getLocationDetailsFromAwaitingScreen() {
	    String text = driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')]//div[contains(@class,'trip_card__trip_info')]//div[contains(@class,'tg-start')])[1]")).getText();
	    System.out.println("Location names from Awaiting Page: " + text);

	    String[] parts = text.split("-");
	    String from = parts[0].trim();
	    String to = parts.length > 1 ? parts[1].trim() : "";

	    return new String[]{from, to};
	}
	
	public String[] getdateFromAwaitingPg() {
	    String date = driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')])[1]//div[contains(@class,'tg-typography_secondary-dark')]")).getText();
        System.out.println("date from awaiting Page: " + date);

	    return new String[]{date};
	}	
	
	public String[] getApproverIdFromAwaitingPg(Log log,String text) throws InterruptedException {
		WebElement search = driver.findElement(By.xpath("//input[@placeholder='Search']"));
		search.click();           
		search.clear();           
		search.sendKeys(text);  
		Thread.sleep(3000);
	    String approverid = driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')])[1]//div[contains(text(),'Approver ID:')]//div")).getText();
	    System.out.println("approverid from awaiting Page: " + approverid);
	    log.ReportEvent("INFO", "Approverid from awaiting Page:"+ approverid);
	    return new String[]{approverid};
	}	
	
	public String[] getApproverIdFromAwaitingPgForTrips(Log log, String text) throws InterruptedException {
	    // Capture Approver ID for the first record
	    String approverId = driver.findElement(
	        By.xpath("(//div[contains(@class,'trip_card__container')])[1]//div[contains(text(),'Approver ID:')]/div")
	    ).getText();

	    System.out.println("Approver ID from Awaiting Page: " + approverId);
	    log.ReportEvent("INFO", "Approver ID from Awaiting Page: " + approverId);

	    return new String[]{approverId};
	}

	
	
public void clickOnViewTripInAwaitingScreen() {
	driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')])[1]//button[text()='View Trip']")).click();
	
}
	

public String[] getLocationDetailsFromViewTripInAwaitingScreen() throws InterruptedException {
	Thread.sleep(3000);
    String text = driver.findElement(By.xpath("//div[contains(@class,' tg-typography tg-typography_subtitle-3 fw-600 tg-typography_default')]")).getText();
    System.out.println("Location names from view trip Page: " + text);

    String[] parts = text.split("-");
    String from = parts[0].trim();
    String to = parts.length > 1 ? parts[1].trim() : "";

    return new String[]{from, to};
}




public String[] getBusNameFromViewTripInAwaitingScreen() {
    String busname = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-operatorname')]")).getText();
    System.out.println("Bus name from view trip Page: " + busname);

    return new String[]{busname};
}	

public String[] getBustypeFromViewTripInAwaitingScreen() {
    String busType = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-type')]")).getText();
    System.out.println("Bus type from view trip Page: " + busType);

    return new String[]{busType};
}

public String[] getBusoriginLocFromViewTripInAwaitingScreen() {
    String origin = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-origin-location')]")).getText();
    System.out.println("Bus origin from view trip Page: " + origin);

    return new String[]{origin};
}


public String[] getBusDestLocFromViewTripInAwaitingScreen() {
    String dest = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-destination-location')]")).getText();
    System.out.println("Bus dest from view trip Page: " + dest);

    return new String[]{dest};
}


public String[] getBusArrTimeFromViewTripInAwaitingScreen() {
    String arrTime = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-arrtime')]")).getText();
    System.out.println("Bus arrTime from view trip Page: " + arrTime);

    return new String[]{arrTime};
}

public String[] getBusDepartTimeFromViewTripInAwaitingScreen() {
    String departTime = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-deptime')]")).getText();
    System.out.println("Bus departTime from view trip Page: " + departTime);

    return new String[]{departTime};
}

public String[] getBusDepartDateFromViewTripInAwaitingScreen() {
    String departDate = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-depdate')]")).getText();
    System.out.println("Bus departDate from view trip Page: " + departDate);

    return new String[]{departDate};
}

public String[] getBusDurationFromViewTripInAwaitingScreen() {
    String duration = driver.findElement(By.xpath("//div[contains(@class,'journey-duration')]//div")).getText();
    System.out.println("Bus duration from view trip Page: " + duration);

    return new String[]{duration};
}

public String[] getBusPolicyFromViewTripInAwaitingScreen() {
    String policy = driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText();
    System.out.println("Bus policy from view trip Page: " + policy);

    return new String[]{policy};
}

public String[] getApproverIdFromViewTripInViewTripScreen() {
    String approverId = driver.findElement(By.xpath("//div[text()='Approver ID']//span")).getText();
    System.out.println("Approver Id from view trip Page: " + approverId);

    return new String[]{approverId};
}

public String[] getPriceFromViewTripInViewTripScreen() {
    String price = driver.findElement(By.xpath("//div[text()='Requested Trip Cost']/following-sibling::div")).getText();
    System.out.println("price from view trip Page: " + price);

    return new String[]{price};
}

public String[] getStatusFromViewTripInViewTripScreen(Log log) throws InterruptedException {
	Thread.sleep(2000);
   // String status = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-status')]")).getText();
	    String status = driver.findElement(By.xpath("(//div[contains(@class,'tg-label')])[2]")).getText();
    System.out.println("status from view trip Page: " + status);
   // log.ReportEvent("INFO", "service status from View trip Page."+ status);


    return new String[]{status};
}



//Validation methods for these 
public void validateLocationsDetailsFromAwaitingApprovalToViewTrippage(String[] AwaitingDetails, String[] ViewTripData, Log log, ScreenShots screenshots) {
    if (AwaitingDetails == null || AwaitingDetails.length == 0) {
        log.ReportEvent("FAIL", "Location details from Awaiting Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Location details from Awaiting Page is missing.");
        return;
    }
    if (ViewTripData == null || ViewTripData.length == 0) {
        log.ReportEvent("FAIL", "Location details from View trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Location details from view trip Page is missing.");
        return;
    }

    String AwaitingLoc = AwaitingDetails[0].trim();
    String ViewTripLoc = ViewTripData[0].trim();

    if (!AwaitingLoc.equalsIgnoreCase(ViewTripLoc)) {
        log.ReportEvent("FAIL", "location details mismatch! Awaiting Page: '" + AwaitingLoc + "', view trip Page: '" + ViewTripLoc + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Location details mismatch between Awaiting and viewtrip pages.");
    } else {
        log.ReportEvent("PASS", "Location details matches from Awaiting to viewtrip page: " + ViewTripLoc);
    }
}

public void validateApproverIdFromAwaitingApprovalToViewTrippage(String[] AwaitingDetails, String[] ViewTripData, Log log, ScreenShots screenshots) {
    if (AwaitingDetails == null || AwaitingDetails.length == 0) {
        log.ReportEvent("FAIL", "Approver Id details from Awaiting Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Approver Id from Awaiting Page is missing.");
        return;
    }
    if (ViewTripData == null || ViewTripData.length == 0) {
        log.ReportEvent("FAIL", "Approver Id details from View trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Approver Id from view trip Page is missing.");
        return;
    }

    String AwaitingLoc = AwaitingDetails[0].trim();
    String ViewTripLoc = ViewTripData[0].trim();

    if (!AwaitingLoc.equalsIgnoreCase(ViewTripLoc)) {
        log.ReportEvent("FAIL", "Approver Id  mismatch! Awaiting Page: '" + AwaitingLoc + "', view trip Page: '" + ViewTripLoc + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Approver Id  mismatch between Awaiting and viewtrip pages.");
    } else {
        log.ReportEvent("PASS", "Approver Id  matches from Awaiting to viewtrip page: " + ViewTripLoc);
    }
}

public void validateOriginFromBusBookingToViewTrippage(String[] bookingOrigin, String[] viewTripOrigin, Log log, ScreenShots screenshots) {
    if (bookingOrigin == null || bookingOrigin.length == 0) {
        log.ReportEvent("FAIL", "Origin from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Origin from Booking Page is missing.");
        return;
    }
    if (viewTripOrigin == null || viewTripOrigin.length == 0) {
        log.ReportEvent("FAIL", "Origin from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Origin from view trip Page is missing.");
        return;
    }

    String originBooking = bookingOrigin[0].trim();
    String originViewtrip = viewTripOrigin[0].trim();

    if (!originBooking.equalsIgnoreCase(originViewtrip)) {
        log.ReportEvent("FAIL", "Origin mismatch! Booking Page: '" + originBooking + "', view trip Page: '" + originViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Origin mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Origin matches from booking to view trip page: " + originBooking);
    }
}

public void validateDestFromBusBookingToViewTrippage(String[] bookingDest, String[] viewTripdest, Log log, ScreenShots screenshots) {
    if (bookingDest == null || bookingDest.length == 0) {
        log.ReportEvent("FAIL", "Destination from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Destination from Booking Page is missing.");
        return;
    }
    if (viewTripdest == null || viewTripdest.length == 0) {
        log.ReportEvent("FAIL", "Destination from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Destination from view trip Page is missing.");
        return;
    }

    String DestBooking = bookingDest[0].trim();
    String destViewtrip = viewTripdest[0].trim();

    if (!DestBooking.equalsIgnoreCase(destViewtrip)) {
        log.ReportEvent("FAIL", "Destination mismatch! Booking Page: '" + DestBooking + "', view trip Page: '" + destViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Destination mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Destination matches from booking to view trip page: " + DestBooking);
    }
}

public void validateBusNameFromBusBookingToViewTrippage(String[] bookingBusName, String[] viewTripBusName, Log log, ScreenShots screenshots) {
    if (bookingBusName == null || bookingBusName.length == 0) {
        log.ReportEvent("FAIL", "Bus Name from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Name from Booking Page is missing.");
        return;
    }
    if (viewTripBusName == null || viewTripBusName.length == 0) {
        log.ReportEvent("FAIL", "Bus Name from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Name from view trip Page is missing.");
        return;
    }

    String BusNameBooking = bookingBusName[0].trim();
    String BusNameViewtrip = viewTripBusName[0].trim();

    if (!BusNameBooking.equalsIgnoreCase(BusNameViewtrip)) {
        log.ReportEvent("FAIL", "Bus Name mismatch! Booking Page: '" + BusNameBooking + "', view trip Page: '" + BusNameViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Name mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Bus Name matches from booking to view trip page: " + BusNameBooking);
    }
}

public void validateBusTypeFromBusBookingToViewTrippage(String[] bookingBusType, String[] viewTripBusType, Log log, ScreenShots screenshots) {
    if (bookingBusType == null || bookingBusType.length == 0) {
        log.ReportEvent("FAIL", "Bus Type from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Type from Booking Page is missing.");
        return;
    }
    if (viewTripBusType == null || viewTripBusType.length == 0) {
        log.ReportEvent("FAIL", "Bus Type from ViewTrip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Type from ViewTrip Page is missing.");
        return;
    }

    String BusTypeBooking = bookingBusType[0].trim().toLowerCase();
    String BusTypeViewtrip = viewTripBusType[0].trim().toLowerCase();

    if (BusTypeBooking.contains(BusTypeViewtrip) || BusTypeViewtrip.contains(BusTypeBooking)) {
        log.ReportEvent("PASS", "Bus Type matches (including partial match) from booking to view trip page: " + bookingBusType[0]);
    } else {
        log.ReportEvent("FAIL", "Bus Type mismatch! Booking Page: '" + bookingBusType[0] + "', ViewTrip Page: '" + viewTripBusType[0] + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Type mismatch between Booking and ViewTrip pages.");
    }
}


public void validateBusOriginLocFromBusBookingToViewTrippage(String[] bookingBusType, String[] viewTripBusType, Log log, ScreenShots screenshots) {
    if (bookingBusType == null || bookingBusType.length == 0) {
        log.ReportEvent("FAIL", "Bus Type from Buscard  is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Type from Buscard  is missing.");
        return;
    }
    if (viewTripBusType == null || viewTripBusType.length == 0) {
        log.ReportEvent("FAIL", "Bus Type from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Type from view trip Page is missing.");
        return;
    }

    String BusTypeBooking = bookingBusType[0].trim();
    String BusTypeViewtrip = viewTripBusType[0].trim();

    if (!BusTypeBooking.equalsIgnoreCase(BusTypeViewtrip)) {
        log.ReportEvent("FAIL", "Bus Type mismatch! Booking Page: '" + BusTypeBooking + "', view trip Page: '" + BusTypeViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Type mismatch between Buscard and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Bus Type matches from Buscard to view trip page: " + BusTypeBooking);
    }
}



public void validateBusDepartTimeFromBusBookingToViewTrippage(String[] bookingBusDepart, String[] viewTripBusDepart, Log log, ScreenShots screenshots) {
    if (bookingBusDepart == null || bookingBusDepart.length == 0) {
        log.ReportEvent("FAIL", "Bus Duration from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Duration from Booking Page is missing.");
        return;
    }
    if (viewTripBusDepart == null || viewTripBusDepart.length == 0) {
        log.ReportEvent("FAIL", "Bus Duration from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Duration from view trip Page is missing.");
        return;
    }

    String BusDepartBooking = bookingBusDepart[0].trim();
    String BusDepartViewtrip = viewTripBusDepart[0].trim();

    if (!BusDepartBooking.equalsIgnoreCase(BusDepartViewtrip)) {
        log.ReportEvent("FAIL", "Bus Duration mismatch! Booking Page: '" + BusDepartBooking + "', view trip Page: '" + BusDepartViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Depart mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Depart matches from booking to view trip page: " + BusDepartBooking);
    }
}

public void validateBusArrivalTimeFromBusBookingToViewTrippage(String[] BusCardDetails, String[] viewTripBusArrivalTime, Log log, ScreenShots screenshots) {
    if (BusCardDetails == null || BusCardDetails.length == 0) {
        log.ReportEvent("FAIL", "Bus Arrival time from BusCard is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Arrival time from BusCard is missing.");
        return;
    }
    if (viewTripBusArrivalTime == null || viewTripBusArrivalTime.length == 0) {
        log.ReportEvent("FAIL", "Bus Arrival time from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Arrival time from view trip Page is missing.");
        return;
    }

    String BusArrivalTime = BusCardDetails[1].trim();
    String BusArrivalTimeViewtrip = viewTripBusArrivalTime[0].trim();

    if (!BusArrivalTime.equalsIgnoreCase(BusArrivalTimeViewtrip)) {
        log.ReportEvent("FAIL", "Bus Arrival time mismatch! Booking Page: '" + BusArrivalTime + "', view trip Page: '" + BusArrivalTimeViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Arrival time mismatch between BusCard and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Arrival Time matches from BusCard to view trip page: " + BusArrivalTime);
    }
}



public void validateBusdurationFromBusBookingToViewTrippage(String[] bookingBusDuration, String[] viewTripBusDuration, Log log, ScreenShots screenshots) {
    if (bookingBusDuration == null || bookingBusDuration.length == 0) {
        log.ReportEvent("FAIL", "Bus Duration from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Duration from Booking Page is missing.");
        return;
    }
    if (viewTripBusDuration == null || viewTripBusDuration.length == 0) {
        log.ReportEvent("FAIL", "Bus Duration from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Duration from view trip Page is missing.");
        return;
    }

    String BusDurationBooking = bookingBusDuration[0].trim();
    String BusDurationViewtrip = viewTripBusDuration[0].trim();

    if (!BusDurationBooking.equalsIgnoreCase(BusDurationViewtrip)) {
        log.ReportEvent("FAIL", "Bus Duration mismatch! Booking Page: '" + BusDurationBooking + "', view trip Page: '" + BusDurationViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Duration mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Duration matches from booking to view trip page: " + BusDurationBooking);
    }
}

public void validateBusPolicyFromBusBookingToViewTrippage(String[] bookingBusPolicy, String[] viewTripBusPolicy, Log log, ScreenShots screenshots) {
    if (bookingBusPolicy == null || bookingBusPolicy.length == 0) {
        log.ReportEvent("FAIL", "Bus Policy from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Policy from Booking Page is missing.");
        return;
    }
    if (viewTripBusPolicy == null || viewTripBusPolicy.length == 0) {
        log.ReportEvent("FAIL", "Bus Policy from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Policy from view trip Page is missing.");
        return;
    }

    String BusPolicyBooking = bookingBusPolicy[0].trim();
    String BusPolicyViewtrip = viewTripBusPolicy[0].trim();

    if (!BusPolicyBooking.equalsIgnoreCase(BusPolicyViewtrip)) {
        log.ReportEvent("FAIL", "Bus Policy mismatch! Booking Page: '" + BusPolicyBooking + "', view trip Page: '" + BusPolicyViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus Policy mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "Policy matches from booking to view trip page: " + BusPolicyBooking);
    }
}

public void validateBusPriceFromBusBookingToViewTrippage(String[] bookingBusprice, String[] viewTripBusprice, Log log, ScreenShots screenshots) {
    if (bookingBusprice == null || bookingBusprice.length == 0) {
        log.ReportEvent("FAIL", "Bus price from Booking Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus price from Booking Page is missing.");
        return;
    }
    if (viewTripBusprice == null || viewTripBusprice.length == 0) {
        log.ReportEvent("FAIL", "Bus price from view trip Page is missing.");
        screenshots.takeScreenShot1();
        Assert.fail("Bus price from view trip Page is missing.");
        return;
    }

    String BusPriceBooking = bookingBusprice[0].trim();
    String BusPriceViewtrip = viewTripBusprice[0].trim();

    if (!BusPriceBooking.equalsIgnoreCase(BusPriceViewtrip)) {
        log.ReportEvent("FAIL", "Bus price mismatch! Booking Page: '" + BusPriceBooking + "', view trip Page: '" + BusPriceViewtrip + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Bus price mismatch between Booking and view trip pages.");
    } else {
        log.ReportEvent("PASS", "price matches from booking to view trip page: " + BusPriceBooking);
    }
}

//get data from Approval Details in view trip screen

public String[] getApproverNameInAwaitingScreen1(Log log) {
    List<WebElement> approverElements = driver.findElements(By.xpath("//div[contains(@class,'tg-approver-name')]"));
    
    if (approverElements.isEmpty()) {
        log.ReportEvent("INFO", "No approver names found on the Awaiting screen.");
        return new String[0]; // return empty array if none found
    }

    String[] approverNames = new String[approverElements.size()];
    for (int i = 0; i < approverElements.size(); i++) {
        approverNames[i] = approverElements.get(i).getText().trim();
        System.out.println("Approver Name [" + i + "]: " + approverNames[i]);
    }

    log.ReportEvent("INFO", "Approver Names from Awaiting Screen: " + String.join(", ", approverNames));
    return approverNames;
}

public String[] getApproverNameInAwaitingScreen(Log log) {
    // Locate all approver name elements on the Awaiting screen
    List<WebElement> approverElements = driver.findElements(
        By.xpath("//div[contains(@class,'tg-approver-name')]")
    );

    // If no approvers found
    if (approverElements.isEmpty()) {
        log.ReportEvent("INFO", "No approver names found on the Awaiting screen.");
        return new String[0]; // empty array
    }

    String[] approverNames = new String[approverElements.size()];
    for (int i = 0; i < approverElements.size(); i++) {
        approverNames[i] = approverElements.get(i).getText().trim();
    }

    log.ReportEvent("INFO", approverNames.length + " approver names found on Awaiting screen: " 
        + String.join(", ", approverNames));

    return approverNames;
}

public void clickApprovalDetailsButtonInViewtrip() {
	driver.findElement(By.xpath("//button[text()='Approval Details']")).click();
	
}

public String[] getApproverTimeInAwaitingScreen(Log log) {
    String approverTime = driver.findElement(By.xpath("//div[text()='Approval Time :']//following-sibling::span")).getText();
    System.out.println("Approver Time from view trip Page: " + approverTime);
    log.ReportEvent("INFO", "Approver Time from view trip Page: "+approverTime);

    return new String[]{approverTime};
}

//Method to logout 
public void clickOnLogout() throws InterruptedException {
	driver.findElement(By.xpath("//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-15eml5n']")).click();
	Thread.sleep(1000);
	driver.findElement(By.xpath("//span[text()='Logout']")).click();
}

//----------------------------------------Hotels-------------------------------------------------------

//Method to get data of hotels
public String[] getHotelNameFromAwaitingPg() {
    String name = driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')])[1]//div[contains(@class,'tg-start')]")).getText();
    System.out.println("Hotel name from awaiting Page: " + name);

    return new String[]{name};
}	

public String[] getHotelDateFromAwaitingPg() {
    String date = driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')])[1]//div[contains(@class,'tg-typography_secondary-dark')]")).getText();
    System.out.println("Hotel date from awaiting Page: " + date);

    return new String[]{date};
}

public String[] getHotelNameFromViewTripInAwaitingScreen() {
    String hotelName = driver.findElement(By.xpath("//div[contains(@class,' tg-hb-hotelname')]")).getText();
    System.out.println("Hotel Name from view trip Page: " + hotelName);

    return new String[]{hotelName};
}	

public String[] getHotelAddressFromViewTripInAwaitingScreen() {
    String Address = driver.findElement(By.xpath("//div[contains(@class,' tg-hb-hoteladdress')]")).getText();
    System.out.println("Hotel Address from view trip Page: " + Address);

    return new String[]{Address};
}

public String[] getCheckInAfterFromViewTripInAwaitingScreen() throws InterruptedException {
    String checkInAfter = driver.findElement(By.xpath("(//div[text()='Check In after']/following-sibling::div)[1]")).getText();
    System.out.println("checkInAfter from view trip Page: " + checkInAfter);
    return new String[]{checkInAfter};
}	

public String[] getCheckOutTimeFromViewTripInAwaitingScreen() {
    String checkOutTime = driver.findElement(By.xpath("(//div[text()='Check Out Before']/following-sibling::div)[1]")).getText();
    System.out.println("hotel checkOutTime from view trip Page: " + checkOutTime);

    return new String[]{checkOutTime};
}

public String[] getCheckInDateFromViewTripInAwaitingScreen() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Wait for the check-in date element to be visible
    WebElement checkInElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath(".//div[contains(@class,'tg-hb-checkindate')]")));

    String checkIndate = checkInElement.getText().trim();
    System.out.println("Check-in date text from Awaiting Page: " + checkIndate);

    if (checkIndate.isEmpty()) {
        throw new RuntimeException("Check-in date is missing from Awaiting Page.");
    }

    // Normalize text (remove ordinal suffixes like 1st, 2nd, etc.)
    checkIndate = checkIndate.replaceAll("(?<=\\d)(st|nd|rd|th)", "").trim();

    // Split by spaces — e.g. "20 Sep 2025" → ["20", "Sep", "2025"]
    String[] dateParts = checkIndate.split("\\s+");

    // If format is not 3 parts, try alternative cleanup
    if (dateParts.length != 3) {
        System.out.println("Unexpected date format for Awaiting Page date: " + checkIndate);
        // Attempt to extract digits/month/year from a messy string
        dateParts = checkIndate.replaceAll("[,]", "").split("\\s+");
    }

    return dateParts;
}


public String[] getCheckOutDateFromViewTripInAwaitingScreen() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    WebElement checkOutDateElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath(".//div[contains(@class,'tg-hb-checkoutdate')]")
    ));

    String checkOutDate = checkOutDateElem.getText().trim();
    System.out.println("Check-out date text from Awaiting (View Trip) Page: " + checkOutDate);

    if (checkOutDate.isEmpty()) {
        System.out.println("⚠️ Warning: 'Check-out date' value from Awaiting Page is empty!");
    }

    return new String[]{checkOutDate};
}


public String[] getSelectedRoomTextFromViewTripInAwaitingScreen() {
    String SelectedroomName = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-roomname')]")).getText();
    System.out.println("selecetd hotel name Text from view trip Page: " + SelectedroomName);

    return new String[]{SelectedroomName};
}	

public String[] getLabelTextFromViewTripInAwaitingScreen() {
    String label = driver.findElement(By.xpath("//div[contains(@class,'g-hb-faretype')]")).getText();
    System.out.println("hotel label Text from view trip Page: " + label);

    return new String[]{label};
}	

public String[] getMealsTextFromViewTripInAwaitingScreen() {
    String meals = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-meals')]")).getText();
    System.out.println("hotel meals Text from view trip Page: " + meals);

    return new String[]{meals};
}	

public String[] getPolicyTextFromFromViewTripInAwaitingScreen() {
    String PolicyText = driver.findElement(By.xpath("//div[contains(@class,'tg-policy')]")).getText();
    System.out.println("hotel PolicyText from view trip Page: " + PolicyText);

    return new String[]{PolicyText};
}	

public String[] getRefundableTextFromViewTripInAwaitingScreen() {
    String Refundable = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-refundablity')]")).getText();
    System.out.println("hotel Refundable Text from view trip Page: " + Refundable);

    return new String[]{Refundable};
}


public String[] getpriceTextFromViewTripInAwaitingScreen() {
    String price = driver.findElement(By.xpath("//div[text()='Requested Trip Cost']/following-sibling::div")).getText();
    System.out.println("price from view trip Page: " + price);

    return new String[]{price};
}

public String[] getApproverIdTextFromViewTripInAwaitingScreen() {
    String id = driver.findElement(By.xpath("//span[contains(@class,'tg-hb-approvalid')]")).getText();
    System.out.println("Approver id from view trip Page: " + id);

    return new String[]{id};
}

public String[] getStatusTextFromViewTripInAwaitingScreen(Log log) throws InterruptedException {
	Thread.sleep(2000);
    String status = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-status')]")).getText();
    System.out.println("Hotel status from view trip Page: " + status);
   // log.ReportEvent("INFO", "service status from View trip Page."+ status);


    return new String[]{status};
}
//validations between booking to view trip 
public void validateCheckInAfterFromBookingToAwaitingPageForHotels(
        String[] checkInAfterFromBookingPage,
        String[] checkInAfterFromAwaiting,
        Log log,
        ScreenShots screenshots) {

    // Null or empty checks
    if (checkInAfterFromAwaiting == null || checkInAfterFromAwaiting.length == 0 || checkInAfterFromAwaiting[0].isEmpty()) {
        log.ReportEvent("FAIL", "Check-In After from Awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (checkInAfterFromBookingPage == null || checkInAfterFromBookingPage.length == 0 || checkInAfterFromBookingPage[0].isEmpty()) {
        log.ReportEvent("FAIL", "Check-In After from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String AwaitingCheckInAfter = checkInAfterFromAwaiting[0].trim();
    String bookingCheckInAfter = checkInAfterFromBookingPage[0].trim();

    if (!bookingCheckInAfter.equalsIgnoreCase(AwaitingCheckInAfter)) {
        log.ReportEvent("FAIL", "Check-In After mismatch! BookingPage: '" + bookingCheckInAfter + "', AwaitingPage: '" + AwaitingCheckInAfter + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Check-In After mismatch");
    } else {
        log.ReportEvent("PASS", "Check-In After matches on both Booking and awaiting pages: '" + AwaitingCheckInAfter + "'");
    }
}


public void validateCheckOutTimeFromBookingPageToAwaitingPage(
        String[] checkOutTimeFromBookingPage,
        String[] checkOutTimeFromAwaiting,

        Log log,
        ScreenShots screenshots) {

    if (checkOutTimeFromAwaiting == null || checkOutTimeFromAwaiting.length == 0 || checkOutTimeFromAwaiting[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Check-Out Time from awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (checkOutTimeFromBookingPage == null || checkOutTimeFromBookingPage.length == 0 || checkOutTimeFromBookingPage[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Check-Out Time from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String awaitingCheckOutTime = checkOutTimeFromAwaiting[0].trim();
    String bookingCheckOutTime = checkOutTimeFromBookingPage[0].trim();

  
    if (!bookingCheckOutTime.equalsIgnoreCase(awaitingCheckOutTime)) {
        log.ReportEvent("FAIL", "Check-Out Time mismatch! BookingPage: '" + bookingCheckOutTime + "', AwaitingPage: '" + awaitingCheckOutTime + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Check-Out Time mismatch");
    } else {
        log.ReportEvent("PASS", "Check-Out Time matches on both Booking and awaiting pages: '" + awaitingCheckOutTime + "'");
    }
}

public void validateHotelAddressFromBookingToAwaitingPage(
        String[] addressFromBookingPage,
        String[] addressFromAwaitingPage,

        Log log,
        ScreenShots screenshots) {

    // Null or empty checks
    if (addressFromAwaitingPage == null || addressFromAwaitingPage.length == 0 || addressFromAwaitingPage[0].isEmpty()) {
        log.ReportEvent("FAIL", "Address from awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (addressFromBookingPage == null || addressFromBookingPage.length == 0 || addressFromBookingPage[0].isEmpty()) {
        log.ReportEvent("FAIL", "Address from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String awaitingAddress = addressFromAwaitingPage[0].trim().toLowerCase();
    String bookingAddress = addressFromBookingPage[0].trim().toLowerCase();

    // Allow partial match in either direction
    if (bookingAddress.contains(awaitingAddress) || awaitingAddress.contains(bookingAddress)) {
        log.ReportEvent("PASS", "Hotel address matches between Booking and awaiting pages.\n" +
                "BookingPage: '" + bookingAddress + "'\nAwaitingPage: '" + awaitingAddress + "'");
    } else {
        log.ReportEvent("FAIL", "Hotel address mismatch!\nBookingPage: '" + bookingAddress + "'\nAwaitingPage: '" + awaitingAddress + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Hotel address mismatch");
    }
}


public void validateHotelNameFromBookingToToAwaitingPage(
        String[] hotelNameFromBookingPage,
        String[] hotelNameFromAwaitingPage,

        Log log,
        ScreenShots screenshots) {

    if (hotelNameFromAwaitingPage == null || hotelNameFromAwaitingPage.length == 0 || hotelNameFromAwaitingPage[0].isEmpty()) {
        log.ReportEvent("FAIL", "Hotel name from awaiting page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (hotelNameFromBookingPage == null || hotelNameFromBookingPage.length == 0 || hotelNameFromBookingPage[0].isEmpty()) {
        log.ReportEvent("FAIL", "Hotel name from booking page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String AwaitingHotelName = hotelNameFromAwaitingPage[0].trim();
    String bookingHotelName = hotelNameFromBookingPage[0].trim();

    if (!bookingHotelName.equalsIgnoreCase(AwaitingHotelName)) {
        log.ReportEvent("FAIL", "Hotel name mismatch! BookingPage: '" + bookingHotelName + "', AwaitingPage: '" + AwaitingHotelName + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Hotel name mismatch");
    } else {
        log.ReportEvent("PASS", "Hotel name matches on both Booking and awaiting pages: " + AwaitingHotelName);
    }
}

public void validateLabelFromBookingToAwaitingPage(
        String[] bookingPageLabelData,
        String[] AwaitingPageData,

        Log log,
        ScreenShots screenshots) {

    // Null or empty checks
    if (AwaitingPageData == null || AwaitingPageData.length == 0 || AwaitingPageData[0].isEmpty()) {
        log.ReportEvent("FAIL", "Label text from Awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (bookingPageLabelData == null || bookingPageLabelData.length == 0 || bookingPageLabelData[0].isEmpty()) {
        log.ReportEvent("FAIL", "Label text from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String AwaitingLabel = AwaitingPageData[0].trim();  // Changed to index 3
    String bookingLabel = bookingPageLabelData[0].trim();

    // Compare label texts (case-insensitive)
    if (!bookingLabel.equalsIgnoreCase(AwaitingLabel)) {
        log.ReportEvent("FAIL", "Label mismatch! Booking Page Label: '" + bookingLabel + "', Awaiting page label '" + AwaitingLabel + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Label mismatch");
    } else {
        log.ReportEvent("PASS", "Label matches on both Booking and awaiting pages. Label: '" + AwaitingLabel + "'");
    }
}

public void validateMealsTextFromBookingToAwaitingPage(
        String[] bookingPageMealsData,
        String[] AwaitingPageMealData,

        Log log,
        ScreenShots screenshots) {

    if (AwaitingPageMealData == null || AwaitingPageMealData.length == 0 || AwaitingPageMealData[0].isEmpty()) {
        log.ReportEvent("FAIL", "Meals text from awaiting Page is missing or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    // Validate Booking Page meal text (from index 0, because getMealsTextFromBookingPg returns String[]{meals})
    if (bookingPageMealsData == null || bookingPageMealsData.length == 0 || bookingPageMealsData[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Meals text from Booking Page is missing or not in the expected format.");
        screenshots.takeScreenShot1();
        return;
    }

    String AwaitingMeals = AwaitingPageMealData[0].trim();   
    String bookingMeals = bookingPageMealsData[0].trim();

    // Compare meal texts (case-insensitive)
    if (!bookingMeals.equalsIgnoreCase(AwaitingMeals)) {
        log.ReportEvent("FAIL", "Meals text mismatch! Booking Page: '" + bookingMeals + "', Awaiting Page: '" + AwaitingMeals + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Meals text mismatch between Booking and awaiting Pages.");
    } else {
        log.ReportEvent("PASS", "Meals text matches on both Booking and awaiting pages. Meals: '" + AwaitingMeals + "'");
    }
}



public void validatePolicyTextFromBookingToAwaitingPage(
        String[] bookingPagePolicy,
        String[] awaitingPagePolicy,

        Log log,
        ScreenShots screenshots) {

    // Null or empty checks
    if (awaitingPagePolicy == null || awaitingPagePolicy.length == 0 || awaitingPagePolicy[0].isEmpty()) {
        log.ReportEvent("FAIL", "Policy text from Awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (bookingPagePolicy == null || bookingPagePolicy.length == 0 || bookingPagePolicy[0].isEmpty()) {
        log.ReportEvent("FAIL", "Policy text from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String awaitingPolicy = awaitingPagePolicy[0].trim();
    String bookingPolicy = bookingPagePolicy[0].trim();

    if (!bookingPolicy.toLowerCase().contains(awaitingPolicy.toLowerCase())) {
        log.ReportEvent("FAIL", "Policy text mismatch! BookingPage: '" + bookingPolicy + "', AwaitingPage: '" + awaitingPolicy + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Policy text mismatch");
    } else {
        log.ReportEvent("PASS", "Policy text from Booking Page is present in Awaiting Page.");
    }
}
public void validateRefundableTextFromBookingToAwaitingPage(
        String[] bookingPageRefundable,
        String[] awaitingPageRefundable,

        Log log,
        ScreenShots screenshots) {

    if (awaitingPageRefundable == null || awaitingPageRefundable.length == 0 || awaitingPageRefundable[0].isEmpty()) {
        log.ReportEvent("FAIL", "Refundable text from Awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (bookingPageRefundable == null || bookingPageRefundable.length == 0 || bookingPageRefundable[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Refundable text from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String AwaitingRefundable = awaitingPageRefundable[0].trim();  // Corrected index here
    String bookingRefundable = bookingPageRefundable[0].trim();

    if (!bookingRefundable.equalsIgnoreCase(AwaitingRefundable)) {
        log.ReportEvent("FAIL", "Refundable text mismatch! BookingPage: '" + bookingRefundable + "', AwaitingPage: '" + AwaitingRefundable + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Refundable text mismatch");
    } else {
        log.ReportEvent("PASS", "Refundable text matches on both Booking and awaiting pages.");
    }
}


public void validateSelectedRoomTextFromBookingToAwaitingPage(
        String[] bookingPageRoomText,
        String[] awaitingPageRoomDetails,

        Log log,
        ScreenShots screenshots) {

    if (awaitingPageRoomDetails == null || awaitingPageRoomDetails.length == 0 || awaitingPageRoomDetails[0].isEmpty()) {
        log.ReportEvent("FAIL", "Selected Room Text from awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (bookingPageRoomText == null || bookingPageRoomText.length == 0 || bookingPageRoomText[0] == null || bookingPageRoomText[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Selected Room Text from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String awaitingSelectedRoomText = awaitingPageRoomDetails[0].trim();
    String bookingSelectedRoomText = bookingPageRoomText[0].trim();

    // Compare room texts ignoring case
    if (!bookingSelectedRoomText.equalsIgnoreCase(awaitingSelectedRoomText)) {
        log.ReportEvent("FAIL", "Selected Room Text mismatch!\n" +
                "   Booking Page: '" + bookingSelectedRoomText + "'\n" +
                "   Awaiting Page: '" + awaitingSelectedRoomText + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Selected Room Text mismatch between Booking and awaiting Page.");
    } else {
        log.ReportEvent("PASS", "Selected Room Text matches between Bookingand awaiting pages.\n" +
                "   Value: '" + awaitingSelectedRoomText + "'");
    }
}





public void validatePriceFromBookingToAwaitingPage(
        String[] bookingPageTotalFare,
        String[] awaitingPageTotalFare,

        Log log,
        ScreenShots screenshots) {

    if (awaitingPageTotalFare == null || awaitingPageTotalFare.length == 0 || awaitingPageTotalFare[0].isEmpty()) {
        log.ReportEvent("FAIL", "Price from Awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    if (bookingPageTotalFare == null || bookingPageTotalFare.length == 0 || bookingPageTotalFare[0] == null || bookingPageTotalFare[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Total Fare from Booking Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String awaitingPriceRaw = awaitingPageTotalFare[0].trim();
    String bookingPriceRaw = bookingPageTotalFare[0].trim();

    // Regex to match price: optional ₹, digits, commas, dots (e.g. ₹ 1,17,071 or ₹117071.40)
    Pattern pricePattern = Pattern.compile("₹?\\s*[\\d,]+(\\.\\d+)?");

    Matcher matcherDesc = pricePattern.matcher(awaitingPriceRaw);
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
        log.ReportEvent("FAIL", "Failed to extract price parts. Booking Page: '" + bookingPriceRaw + "', Awaiting Page: '" + awaitingPriceRaw + "'");
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
            log.ReportEvent("PASS", "Price matches between Booking and awaiting pages. Value: " + bookingPricePart);
        } else {
            log.ReportEvent("FAIL", "Price mismatch! Booking Page Fare: '" + bookingPricePart +
                    "', Awaiting Page Fare: '" + descPricePart + "'");
            screenshots.takeScreenShot1();
            Assert.fail("Total fare mismatch");
        }

    } catch (NumberFormatException e) {
        log.ReportEvent("FAIL", "Failed to parse price values. Booking Page: '" + bookingPricePart + "', Awaiting Page: '" + descPricePart + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Invalid price format");
    }
}


public void validateCheckInDateBetweenBookingToAwaitingPage(
        String[] bookingPageDateText,
        String[] awaitingPageDateText,
        Log log,
        ScreenShots screenshots) {

    // ---- Validation for Awaiting Page ----
    if (awaitingPageDateText == null || awaitingPageDateText.length != 3) {
        log.ReportEvent("FAIL", "Check-in date from Awaiting Page is missing or not in expected format.");
        screenshots.takeScreenShot1();
        return;
    }

    // ---- Validation for Booking Page ----
    if (bookingPageDateText == null || bookingPageDateText.length == 0 || bookingPageDateText[0].trim().isEmpty()) {
        log.ReportEvent("FAIL", "Check-in date from Booking Page is missing or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String resultDay = awaitingPageDateText[0].replaceAll("[^0-9]", ""); // remove suffixes like 20th → 20
    String resultMonth = awaitingPageDateText[1].replace(",", "");
    String resultYear = awaitingPageDateText[2];

    String normalizedAwaitingDate = resultDay + " " + resultMonth + " " + resultYear;  // e.g. "20 Sep 2025"

    // Normalize Booking page text
    String bookingDateRaw = bookingPageDateText[0].trim();
    String bookingDate = bookingDateRaw
            .replaceAll("(?<=\\d)(st|nd|rd|th)", "")
            .replace(",", "")
            .trim();

    // ---- Compare normalized values ----
    if (!bookingDate.equalsIgnoreCase(normalizedAwaitingDate)) {
        log.ReportEvent("FAIL", "Check-in date mismatch! Booking Page: '" + bookingDate +
                "', Awaiting Page: '" + normalizedAwaitingDate + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Check-in date mismatch between Booking and Awaiting Pages.");
    } else {
        log.ReportEvent("PASS", "Check-in date matches on both Booking and Awaiting Pages. Date: '" + normalizedAwaitingDate + "'");
    }
}


public void validateCheckOutDateBetweenBookingAndAwaitingPage(
        String[] bookingPageDateText,
        String[] awaitingPageDateText,
        Log log,
        ScreenShots screenshots) {

    // --- Defensive check ---
    if (awaitingPageDateText == null || awaitingPageDateText.length == 0) {
        log.ReportEvent("FAIL", "Check-out date from Awaiting Page is null or empty.");
        screenshots.takeScreenShot1();
        return;
    }

    String rawAwaitingDate = awaitingPageDateText[0].trim();
    System.out.println("Raw Awaiting Date Text: " + rawAwaitingDate);

    // --- Clean up possible prefixes and commas ---
    rawAwaitingDate = rawAwaitingDate
            .replace("Check-out:", "")
            .replace("Check out:", "")
            .replace(",", "")
            .trim();

    // Now split
    String[] awaitingParts = rawAwaitingDate.split("\\s+"); // Split by one or more spaces

    if (awaitingParts.length != 3) {
        log.ReportEvent("FAIL", "Check-out date from Awaiting Page is missing or not in expected format. Raw value: " + rawAwaitingDate);
        screenshots.takeScreenShot1();
        return;
    }

    // ---- Proceed with your existing comparison logic ----
    String awaitingDay = awaitingParts[0].replaceAll("[^0-9]", "");
    String awaitingMonth = awaitingParts[1];
    String awaitingYear = awaitingParts[2];
    String normalizedAwaitingDate = awaitingDay + " " + awaitingMonth + " " + awaitingYear;

    // Normalize Booking page text
    String bookingDateRaw = bookingPageDateText[0].trim();
    String bookingDate = bookingDateRaw
            .replaceAll("(?<=\\d)(st|nd|rd|th)", "")
            .replace(",", "")
            .trim();

    if (!bookingDate.equalsIgnoreCase(normalizedAwaitingDate)) {
        log.ReportEvent("FAIL", "Check-out date mismatch! Booking Page: '" + bookingDate +
                "', Awaiting Page: '" + normalizedAwaitingDate + "'");
        screenshots.takeScreenShot1();
        Assert.fail("Check-out date mismatch between Booking and Awaiting Pages.");
    } else {
        log.ReportEvent("PASS", "Check-out date matches on both Booking and Awaiting Pages. Date: '" + normalizedAwaitingDate + "'");
    }
}


public void waitForTripCardToAppearUnderAwaitingScreen() {
    String tripCardXpath = "(//div[contains(@class,'trip_card__container')])[1]";
    
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    
    try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tripCardXpath)));
        System.out.println("Trip Card container is now displayed");
    } catch (Exception e) {
        Assert.fail("Test Failed: Trip Card did not appear");
    }
}

  
}
