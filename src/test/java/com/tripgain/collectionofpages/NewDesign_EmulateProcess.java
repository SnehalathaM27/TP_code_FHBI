package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
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

public class NewDesign_EmulateProcess {
	WebDriver driver;

	public NewDesign_EmulateProcess (WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	//Method to clcik on admin button
	public void clcikOnAdmin() {
	    // Wait up to 10 seconds until the element is clickable
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
	    WebElement adminElement = wait.until(
	            ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']"))
	    );

	    adminElement.click();
	}
	
	//Method to clcik on search by 
	public void clickOnSearchByThroughUser(String optionToSelect) throws InterruptedException {
		Thread.sleep(2000);
	    driver.findElement(By.xpath(".//div[contains(text(), 'Search By')]//ancestor::div[contains(@class, 'tg-select-box-container')]//div[contains(@class, 'tg-select-box__control')]")).click();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	        By.xpath("//div[contains(@class,'g-select-box__menu-list')]")
	    ));
	    List<WebElement> options = driver.findElements(
	        By.xpath("//div[contains(@class,'tg-dropdown-menu-item')]//span[contains(@class,'tg-select-option-label')]")
	    );

	    for (WebElement opt : options) {
	        if (opt.getText().equalsIgnoreCase(optionToSelect)) {
	            opt.click();
	            break;
	        }
	    }
	}

	

	//Method to clcik on search value
	public void clickSearchValueThroughUser(String searchValue) {
	    WebElement searchField = driver.findElement(By.xpath("//input[@name='searchValue']"));
	    searchField.clear();
	    searchField.sendKeys(searchValue);
	}
	
	
	public void enterSearchValueByIndex(String[] searchValues, int index, Log log) {

	    if (searchValues == null || searchValues.length == 0) {
	        System.out.println("No search values provided.");
	        return;
	    }

	    if (index < 0 || index >= searchValues.length) {
	        System.out.println("Invalid index: " + index);
	        return;
	    }

	    WebElement searchField = driver.findElement(By.xpath("//input[@name='searchValue']"));
	    searchField.clear();
	    searchField.sendKeys(searchValues[index]);

	    log.ReportEvent("INFO", "Entered approver details for: " + searchValues[index]);

	    System.out.println("Entered search value at index " + index + ": " + searchValues[index]);
	}


	public void enterSearchValueByIndexInFinanceApproverPg(String[] searchValues, int index) {
	    if (searchValues == null || searchValues.length == 0) {
	        System.out.println("No search values provided.");
	        return;
	    }

	    if (index < 0 || index >= searchValues.length) {
	        System.out.println("Invalid index: " + index);
	        return;
	    }

	    WebElement searchField = driver.findElement(By.xpath("//input[@placeholder='Search']"));
	    searchField.clear();
	    searchField.sendKeys(searchValues[index]);

	    System.out.println("Entered search value at index " + index + ": " + searchValues[index]);
	}

	
	//Method to search corporate traveller
	public void clcikOnCorpTravellerSearchButton() {
		driver.findElement(By.xpath("//button[@aria-label='Search Corporate Traveller']")).click();
	}
	
	//Method to clcik on emulate user option
	public void clickOnEmulmateUserOption() {
		driver.findElement(By.xpath("//span[text()='Emulate']")).click();
		
	}
	
	//wait until approver screen should display
	
	public void waitUntilApproverScreenDisplay(Log log,ScreenShots screenshots) {
	    String messageText = "Approver screen not displayed or page still loading";

	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
	        wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//div[contains(text(),'Welcome')]")));
	        
	        System.out.println("Approver screen displayed successfully.");
	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Approver Screen Not displayed : " + messageText);
	        screenshots.takeScreenShot1();
	        Assert.fail("Failed: " + messageText);
	    }
	}
	
	
	//Method to clcik on search in approval request screen in test approver id
	
	public void searchApproverIdInApprovalReqScreen(String[] approverIdArray, Log log, ScreenShots screenshots) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));

	    // Wait until "Approval Requests" screen is visible
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	        By.xpath("//div[text()='Approval Requests']")
	    ));

	    // Get the first Approver ID from array
	    String approverId = approverIdArray[0];

	    try {
	        // Find the search field
//	        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(
//	            By.xpath("//input[@class='tg-input']")
//	        ));
	        
	        WebElement searchField = driver.findElement(By.xpath("//input[contains(@class,'tg-input')]"));
	        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", searchField);
	        searchField.sendKeys(approverId);


//	        // Enter the Approver ID
//	        searchField.clear();
//	        searchField.sendKeys(approverId);
        Thread.sleep(3000);

	        log.ReportEvent("INFO", "Entered Approver ID in search field in Approver Approval req screen: " + approverId);
	        System.out.println("Entered Approver ID in search field: " + approverId);

	        // Click the search button
	        WebElement searchButton = driver.findElement(By.xpath("//button[contains(@class,'tg-icon-btn_small')]"));
	        searchButton.click();

	        log.ReportEvent("INFO", "Clicked on search button for Approver ID: " + approverId);
	        System.out.println("Clicked on search button for Approver ID: " + approverId);

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to enter Approver ID or click search button: " + approverId);
	        screenshots.takeScreenShot1();
	        Assert.fail("Failed to enter Approver ID or click search button in Approver Approval req screen: " + approverId);
	    }
	}

	
	//Method to get the details after clcik on view arrow 
	
	public String[] getLocationDetailsFromApproverApprovalReqScreen() {
	  //  String text = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-location')]")).getText();
		    String text = driver.findElement(By.xpath("//div[contains(@class,' tg-typography tg-typography_subtitle-3 fw-600 tg-typography_default')]")).getText();

	    System.out.println("Location names from Approver Approval Req Screen: " + text);

	    String[] parts = text.split("-");
	    String from = parts[0].trim();
	    String to = parts.length > 1 ? parts[1].trim() : "";

	    return new String[]{from, to};
	}

	public String[] getBusNameFromApproverApprovalReqScreen() {
	    String busname = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-operatorname')]")).getText();
	    System.out.println("Bus name from Approver Approval Req Screen: " + busname);

	    return new String[]{busname};
	}	

	public String[] getBustypeFromApproverApprovalReqScreen() {
	    String busType = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-type')]")).getText();
	    System.out.println("Bus type from Approver Approval Req Screen: " + busType);

	    return new String[]{busType};
	}

	public String[] getBusoriginLocFromApproverApprovalReqScreen() {
	    String origin = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-origin-location')]")).getText();
	    System.out.println("Bus origin from Approver Approval Req Screen: " + origin);

	    return new String[]{origin};
	}


	public String[] getBusDestLocFromApproverApprovalReqScreen() {
	    String dest = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-destination-location')]")).getText();
	    System.out.println("Bus dest from Approver Approval Req Screen: " + dest);

	    return new String[]{dest};
	}


	public String[] getBusArrTimeFromApproverApprovalReqScreen() {
	    String arrTime = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-arrtime')]")).getText();
	    System.out.println("Bus arrTime from Approver Approval Req Screen: " + arrTime);

	    return new String[]{arrTime};
	}

	public String[] getBusDepartTimeFromApproverApprovalReqScreen() {
	    String departTime = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-deptime')]")).getText();
	    System.out.println("Bus departTime from Approver Approval Req Screen: " + departTime);

	    return new String[]{departTime};
	}

	public String[] getBusDepartDateFromApproverApprovalReqScreen() {
	    String departDate = driver.findElement(By.xpath("//div[contains(@class,'tg-bus-depdate')]")).getText();
	    System.out.println("Bus departDate from Approver Approval Req Screen: " + departDate);

	    return new String[]{departDate};
	}

	public String[] getBusDurationFromApproverApprovalReqScreen() {
	    String duration = driver.findElement(By.xpath("//div[contains(@class,'journey-duration')]//div")).getText();
	    System.out.println("Bus duration from Approver Approval Req Screen: " + duration);

	    return new String[]{duration};
	}

	public String[] getBusPolicyFromApproverApprovalReqScreen() {
	    String policy = driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText();
	    System.out.println("Bus policy from Approver Approval Req Screen: " + policy);

	    return new String[]{policy};
	}

	public String[] getApproverIdFromApproverApprovalReqScreen() {
	    String approverId = driver.findElement(By.xpath("//div[text()='Approver ID']//span")).getText();
	    System.out.println("Approver Id from Approver Approval Req Screen: " + approverId);

	    return new String[]{approverId};
	}

	public String[] getPriceFromApproverApprovalReqScreen() {
	    String price = driver.findElement(By.xpath("//div[text()='Requested Trip Cost']/following-sibling::div")).getText();
	    System.out.println("price from Approver Approval Req Screen: " + price);

	    return new String[]{price};
	}

	public String[] getStatusFromApproverApprovalReqScreen(Log log) {
	    String status = driver.findElement(By.xpath("(//div[contains(@class,'tg-label')])[2]")).getText();
	    System.out.println("status from view trip Page: " + status);
	    log.ReportEvent("INFO", "service status from Approver Approval Req Screen."+ status);


	    return new String[]{status};
	}

	//Method to clcik on process button
	public void clcikOnProcessButton() {
		driver.findElement(By.xpath("//button[contains(@class,'tg-approval-requests-book-button')]")).click();
	}
	
	public void validateLocationsDetailsFromViewTripToApproverScreenForBuses(String[] ViewTripData,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Location details from Approver Details Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Location details from Approver Details Page is missing.");
	        return;
	    }
	    if (ViewTripData == null || ViewTripData.length == 0) {
	        log.ReportEvent("FAIL", "Location details from View trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Location details from view trip Page is missing.");
	        return;
	    }

	    String AwaitingLoc = ApproverDetails[0].trim();
	    String ViewTripLoc = ViewTripData[0].trim();

	    if (!AwaitingLoc.equalsIgnoreCase(ViewTripLoc)) {
	        log.ReportEvent("FAIL", "location details mismatch! view trip Page: '" + ViewTripLoc + "', Approver Details Page: '" + ApproverDetails + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Location details mismatch between viewtrip and Approver Details pages.");
	    } else {
	        log.ReportEvent("PASS", "Location details matches from viewtrip page to Approver Details page: " + ViewTripLoc);
	    }
	}

	public void validateApproverIdFromViewTripToApproverScreenForBuses(String[] ViewTripData,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Approver Id details from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Approver Id from approver Page is missing.");
	        return;
	    }
	    if (ViewTripData == null || ViewTripData.length == 0) {
	        log.ReportEvent("FAIL", "Approver Id details from View trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Approver Id from view trip Page is missing.");
	        return;
	    }

	    String AwaitingLoc = ApproverDetails[0].trim();
	    String ViewTripLoc = ViewTripData[0].trim();

	    if (!AwaitingLoc.equalsIgnoreCase(ViewTripLoc)) {
	        log.ReportEvent("FAIL", "Approver Id mismatch! view trip Page: '" + ViewTripLoc + "', Approver Details Page: '" + ApproverDetails + "'");

	        screenshots.takeScreenShot1();
	        Assert.fail("Approver Id  mismatch between viewtrip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Approver Id  matches from viewtrip page to approver page: " + ViewTripLoc);
	    }
	}

	public void validateOriginFromViewTripToApproverScreen(String[] viewTripOrigin, String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Origin from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Origin from approver Page is missing.");
	        return;
	    }
	    if (viewTripOrigin == null || viewTripOrigin.length == 0) {
	        log.ReportEvent("FAIL", "Origin from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Origin from view trip Page is missing.");
	        return;
	    }

	    String originApprover = ApproverDetails[0].trim();
	    String originViewtrip = viewTripOrigin[0].trim();

	    if (!originApprover.equalsIgnoreCase(originViewtrip)) {
	        log.ReportEvent("FAIL", "Origin mismatch! view trip Page: '" + originViewtrip + "', Approver Page: '" + originApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Origin mismatch between view trip and Approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Origin matches from view trip to approver page: " + originApprover);
	    }
	}

	public void validateDestFromViewTripToApproverScreenForBuses(String[] viewTripdest,String[] ApproverDetails,  Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Destination from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Destination from approver Page is missing.");
	        return;
	    }
	    if (viewTripdest == null || viewTripdest.length == 0) {
	        log.ReportEvent("FAIL", "Destination from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Destination from view trip Page is missing.");
	        return;
	    }

	    String DestApprover = ApproverDetails[0].trim();
	    String destViewtrip = viewTripdest[0].trim();

	    if (!DestApprover.equalsIgnoreCase(destViewtrip)) {
	        log.ReportEvent("FAIL", "Destination mismatch! view trip Page: '" + destViewtrip + "', approver Page: '" + DestApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Destination mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Destination matches from view trip and approver pages: " + DestApprover);
	    }
	}

	public void validateBusNameFromViewTripToApproverScreenForBuses(String[] viewTripBusName,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Name from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Name from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusName == null || viewTripBusName.length == 0) {
	        log.ReportEvent("FAIL", "Bus Name from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Name from view trip Page is missing.");
	        return;
	    }

	    String BusNameApprover = ApproverDetails[0].trim();
	    String BusNameViewtrip = viewTripBusName[0].trim();

	    if (!BusNameApprover.equalsIgnoreCase(BusNameViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Name mismatch! view trip Page: '" + BusNameViewtrip + "',approver Page: '" + BusNameApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Name mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Bus Name matches from view trip and approver pages: " + BusNameApprover);
	    }
	}

	public void validateBusTypeFromViewTripToApproverScreenForBuses(String[] viewTripBusType, String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Type from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Type from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusType == null || viewTripBusType.length == 0) {
	        log.ReportEvent("FAIL", "Bus Type from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Type from view trip Page is missing.");
	        return;
	    }

	    String BusTypeApprover = ApproverDetails[0].trim();
	    String BusTypeViewtrip = viewTripBusType[0].trim();

	    if (!BusTypeApprover.equalsIgnoreCase(BusTypeViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Type mismatch! view trip Page: '" + BusTypeViewtrip + "', approver Page: '" + BusTypeApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Type mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Bus Type matches from view trip to approver page: " + BusTypeApprover);
	    }
	}

	public void validateBusOriginLocFromViewTripToApproverScreenForBuses(String[] viewTripBusType, String[] ApproverDetails,Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Type from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Type from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusType == null || viewTripBusType.length == 0) {
	        log.ReportEvent("FAIL", "Bus origin from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus origin from view trip Page is missing.");
	        return;
	    }
	    String BusoriginApprover = ApproverDetails[0].trim();
	    String BusOriginViewtrip = viewTripBusType[0].trim();

	    if (!BusoriginApprover.equalsIgnoreCase(BusOriginViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Origin mismatch! view trip Page: '" + BusOriginViewtrip + "',approver Page: '" + BusoriginApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus origin mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Bus origin matches from view trip to approver page: " + BusoriginApprover);
	    }
	}

	public void validateBusDepartTimeFromViewTripToApproverScreenForBuses(String[] viewTripBusDepartTime,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Depart time from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Depart time from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusDepartTime == null || viewTripBusDepartTime.length == 0) {
	        log.ReportEvent("FAIL", "Bus Depart time from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Depart time from view trip Page is missing.");
	        return;
	    }

	    String BusDepartTimeApprover = ApproverDetails[0].trim();
	    String BusDepartTimeViewtrip = viewTripBusDepartTime[0].trim();

	    if (!BusDepartTimeApprover.equalsIgnoreCase(BusDepartTimeViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Depart time mismatch! view trip Page: '" + BusDepartTimeViewtrip + "', approver Page: '" + BusDepartTimeApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Depart time mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Depart Time matches from view trip to approver page: " + BusDepartTimeApprover);
	    }
	}

	public void validateBusArrivalTimeFromViewTripToApproverScreenForBuses(String[] viewTripBusArrivalTime,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Arrival time from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Arrival time from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusArrivalTime == null || viewTripBusArrivalTime.length == 0) {
	        log.ReportEvent("FAIL", "Bus Arrival time from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Arrival time from view trip Page is missing.");
	        return;
	    }

	    String BusArrivalTimeApprover = ApproverDetails[0].trim();
	    String BusArrivalTimeViewtrip = viewTripBusArrivalTime[0].trim();

	    if (!BusArrivalTimeApprover.equalsIgnoreCase(BusArrivalTimeViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Arrival time mismatch! view trip Page: '" + BusArrivalTimeViewtrip + "', approver Page: '" + BusArrivalTimeApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Arrival time mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Arrival Time matches from view trip to approver page: " + BusArrivalTimeApprover);
	    }
	}

	public void validateBusdurationFromViewTripToApproverScreenForBuses(String[] viewTripBusDuration,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Duration from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Duration from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusDuration == null || viewTripBusDuration.length == 0) {
	        log.ReportEvent("FAIL", "Bus Duration from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Duration from view trip Page is missing.");
	        return;
	    }

	    String BusDurationApprover = ApproverDetails[0].trim();
	    String BusDurationViewtrip = viewTripBusDuration[0].trim();

	    if (!BusDurationApprover.equalsIgnoreCase(BusDurationViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Duration mismatch! view trip Page: '" + BusDurationViewtrip + "', approver Page: '" + BusDurationApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Duration mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Duration matches from view trip to approver page: " + BusDurationApprover);
	    }
	}

	public void validateBusPolicyFromViewTripToApproverScreenForBuses(String[] viewTripBusPolicy,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus Policy from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Policy from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusPolicy == null || viewTripBusPolicy.length == 0) {
	        log.ReportEvent("FAIL", "Bus Policy from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Policy from view trip Page is missing.");
	        return;
	    }

	    String BusPolicyApprover = ApproverDetails[0].trim();
	    String BusPolicyViewtrip = viewTripBusPolicy[0].trim();

	    if (!BusPolicyApprover.equalsIgnoreCase(BusPolicyViewtrip)) {
	        log.ReportEvent("FAIL", "Bus Policy mismatch! view trip Page: '" + BusPolicyViewtrip + "', approver Page: '" + BusPolicyApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus Policy mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Policy matches from view trip to booking page: " + BusPolicyApprover);
	    }
	}

	public void validateBusPriceFromViewTripToApproverScreenForBuses(String[] viewTripBusprice,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Bus price from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus price from approver Page is missing.");
	        return;
	    }
	    if (viewTripBusprice == null || viewTripBusprice.length == 0) {
	        log.ReportEvent("FAIL", "Bus price from view trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus price from view trip Page is missing.");
	        return;
	    }

	    String BusPriceApprover = ApproverDetails[0].trim();
	    String BusPriceViewtrip = viewTripBusprice[0].trim();

	    if (!BusPriceApprover.equalsIgnoreCase(BusPriceViewtrip)) {
	        log.ReportEvent("FAIL", "Bus price mismatch! view trip Page: '" + BusPriceViewtrip + "' , approver Page: '" + BusPriceApprover + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Bus price mismatch between view trip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "price matches from view trip to approver page: " + BusPriceApprover);
	    }
	}

	
	//Method to enetr remarks 
//	public void enterRemarks(String remarks) {
//	    WebElement remarksField = driver.findElement(By.xpath("//input[@name='remarks']"));
//	    remarksField.clear();
//	    remarksField.sendKeys(remarks);
//	}
	
	public String[] enterRemarks(String remarks) {
	    WebElement remarksField = driver.findElement(By.xpath("//input[@name='remarks']"));
	    remarksField.clear();
	    remarksField.sendKeys(remarks);
	    System.out.println("Entered Remarks: " + remarks);
	    return new String[]{remarks};
	}

	
//Method to clcik on update button 
	public void clickOnUpdateBtn() {
		driver.findElement(By.xpath("//button[text()='Update']")).click();
	}
	
	public void clickOnStatus(String statusToSelect) {
	    driver.findElement(By.xpath("//div[contains(@class,'tg-select-box__dropdown-indicator')]")).click();

	    driver.findElement(By.xpath("//span[@class='tg-select-option-label' and text()='" + statusToSelect + "']")).click();

	    System.out.println("Selected status: " + statusToSelect);
	}

	
	//Method to clcik on switch back
	public void clickOnSwitchBack() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
	    WebElement switchBackButton = wait.until(
	            ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Switch Back to Profile']"))
	    );

	    try {
	        switchBackButton.click(); 
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", switchBackButton);
	    }

	    System.out.println("Clicked on Switch Back button.");
	}

	
	//Emulate to finance means 2nd approver screen 
	public void clickOnApprovalReqIn2ndApproverScreen() {
		driver.findElement(By.xpath("//span[text()='Approval Requests']")).click();
		
	}
	
	//get hotel data from approver screen

	public String[] getHotelNamefromApproverScreen() {
	    String hotelName = driver.findElement(By.xpath("//div[contains(@class,' tg-hb-hotelname')]")).getText();
	    System.out.println("Hotel Name from view trip Page: " + hotelName);

	    return new String[]{hotelName};
	}	

	public String[] getHotelAddressFromApproverScreen() {
	    String Address = driver.findElement(By.xpath("//div[contains(@class,' tg-hb-hoteladdress')]")).getText();
	    System.out.println("Hotel Address from view trip Page: " + Address);

	    return new String[]{Address};
	}

	public String[] getCheckInAfterFromApprovergScreen() throws InterruptedException {
		Thread.sleep(3000);
	    String checkInAfter = driver.findElement(By.xpath("(//div[text()='Check In after']/following-sibling::div)[1]")).getText();
	    System.out.println("checkInAfter from view trip Page: " + checkInAfter);
	    return new String[]{checkInAfter};
	}	

	public String[] getCheckOutTimeFromApproverScreen() {
	    String checkOutTime = driver.findElement(By.xpath("(//div[text()='Check Out Before']/following-sibling::div)[1]")).getText();
	    System.out.println("hotel checkOutTime from view trip Page: " + checkOutTime);

	    return new String[]{checkOutTime};
	}

	public String[] getCheckInDateFromApproverScreen() {
	    String checkIndate = driver.findElement(By.xpath(".//div[contains(@class,'tg-hb-checkindate')]")).getText();
	    System.out.println("checkIndate Text from view trip Page: " + checkIndate);

	    return new String[]{checkIndate};
	}	

	public String[] getCheckOutDateFromApproverScreen() {
	    String checkOutdate = driver.findElement(By.xpath(".//div[contains(@class,'tg-hb-checkoutdate')]")).getText();
	    System.out.println("checkOutdate Text from view trip Page: " + checkOutdate);

	    return new String[]{checkOutdate};
	}

	public String[] getSelectedRoomTextFromApproverScreen() {
	    String SelectedroomName = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-roomname')]")).getText();
	    System.out.println("selecetd hotel name Text from view trip Page: " + SelectedroomName);

	    return new String[]{SelectedroomName};
	}	

	public String[] getLabelTextFromApproverScreen() {
	    String label = driver.findElement(By.xpath("//div[contains(@class,'g-hb-faretype')]")).getText();
	    System.out.println("hotel label Text from view trip Page: " + label);

	    return new String[]{label};
	}	

	public String[] getMealsTextFromApproverScreen() {
	    String meals = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-meals')]")).getText();
	    System.out.println("hotel meals Text from view trip Page: " + meals);

	    return new String[]{meals};
	}	

	public String[] getPolicyTextFromFromApproverScreen() {
	    String PolicyText = driver.findElement(By.xpath("//div[contains(@class,'tg-policy')]")).getText();
	    System.out.println("hotel PolicyText from view trip Page: " + PolicyText);

	    return new String[]{PolicyText};
	}	

	public String[] getRefundableTextFromApproverScreen() {
	    String Refundable = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-refundablity')]")).getText();
	    System.out.println("hotel Refundable Text from view trip Page: " + Refundable);

	    return new String[]{Refundable};
	}


	public String[] getpriceTextFromApproverScreen() {
	    String price = driver.findElement(By.xpath("//div[text()='Requested Trip Cost']/following-sibling::div")).getText();
	    System.out.println("price from view trip Page: " + price);

	    return new String[]{price};
	}

	public String[] getStatusTextFromApproverScreen(Log log) throws InterruptedException {
		Thread.sleep(2000);
	    String status = driver.findElement(By.xpath("//div[contains(@class,'tg-hb-status')]")).getText();
	    System.out.println("Hotel status from view trip Page: " + status);
	   // log.ReportEvent("INFO", "service status from View trip Page."+ status);


	    return new String[]{status};
	}
	
	
	public String[] getApproverIdTextFromApproverScreen() {
	    String id = driver.findElement(By.xpath("//span[contains(@class,'tg-hb-approvalid')]")).getText();
	    System.out.println("Approver id from Approver Page: " + id);

	    return new String[]{id};
	}

	
	//validations betweeen approval screen to approver screen
	public void validateApproverIdFromViewTripToApproverScreenForHotels(String[] ViewTripData,String[] ApproverDetails, Log log, ScreenShots screenshots) {
	    if (ApproverDetails == null || ApproverDetails.length == 0) {
	        log.ReportEvent("FAIL", "Approver Id details from approver Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Approver Id from approver Page is missing.");
	        return;
	    }
	    if (ViewTripData == null || ViewTripData.length == 0) {
	        log.ReportEvent("FAIL", "Approver Id details from View trip Page is missing.");
	        screenshots.takeScreenShot1();
	        Assert.fail("Approver Id from view trip Page is missing.");
	        return;
	    }

	    String AwaitingLoc = ApproverDetails[0].trim();
	    String ViewTripLoc = ViewTripData[0].trim();

	    if (!AwaitingLoc.equalsIgnoreCase(ViewTripLoc)) {
	        log.ReportEvent("FAIL", "Approver Id mismatch! view trip Page: '" + ViewTripLoc + "', Approver Details Page: '" + ApproverDetails + "'");

	        screenshots.takeScreenShot1();
	        Assert.fail("Approver Id  mismatch between viewtrip and approver pages.");
	    } else {
	        log.ReportEvent("PASS", "Approver Id  matches from viewtrip page to approver page: " + ViewTripLoc);
	    }
	}

	//validations between booking to view trip 
	public void validateCheckInAfterFromAwaitingToApproverScreenForHotels(
	        String[] checkInAfterFromAwaiting,
	        String[] checkInAfterFromApproverPage,

	        Log log,
	        ScreenShots screenshots) {

	    // Null or empty checks
	    if (checkInAfterFromAwaiting == null || checkInAfterFromAwaiting.length == 0 || checkInAfterFromAwaiting[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Check-In After from Awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (checkInAfterFromApproverPage == null || checkInAfterFromApproverPage.length == 0 || checkInAfterFromApproverPage[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Check-In After from approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String AwaitingCheckInAfter = checkInAfterFromAwaiting[0].trim();
	    String ApproverCheckInAfter = checkInAfterFromApproverPage[0].trim();

	    if (!AwaitingCheckInAfter.equalsIgnoreCase(ApproverCheckInAfter)) {
	        log.ReportEvent("FAIL", "Check-In After mismatch! AwaitingPage: '" + AwaitingCheckInAfter + "', ApproverPage: '" + ApproverCheckInAfter + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Check-In After mismatch");
	    } else {
	        log.ReportEvent("PASS", "Check-In After matches on both awaiting and approver pages: '" + ApproverCheckInAfter + "'");
	    }
	}


	public void validateCheckOutTimeFromAwaitingToApproverScreenForHotels(
	        String[] checkOutTimeFromAwaiting,
	        String[] checkOutTimeFromApproverPage,


	        Log log,
	        ScreenShots screenshots) {

	    if (checkOutTimeFromAwaiting == null || checkOutTimeFromAwaiting.length == 0 || checkOutTimeFromAwaiting[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Check-Out Time from awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (checkOutTimeFromApproverPage == null || checkOutTimeFromApproverPage.length == 0 || checkOutTimeFromApproverPage[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Check-Out Time from Approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String awaitingCheckOutTime = checkOutTimeFromAwaiting[0].trim();
	    String approverCheckOutTime = checkOutTimeFromApproverPage[0].trim();

	  
	    if (!awaitingCheckOutTime.equalsIgnoreCase(approverCheckOutTime)) {
	        log.ReportEvent("FAIL", "Check-Out Time mismatch! AwaitingPage: '" + awaitingCheckOutTime + "', ApproverPage: '" + approverCheckOutTime + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Check-Out Time mismatch");
	    } else {
	        log.ReportEvent("PASS", "Check-Out Time matches on both awaiting and approver pages: '" + approverCheckOutTime + "'");
	    }
	}

	public void validateHotelAddressFromAwaitingToApproverScreenForHotels(
	        String[] addressFromAwaitingPage,
	        String[] addressFromApproverPage,

	        Log log,
	        ScreenShots screenshots) {

	    // Null or empty checks
	    if (addressFromAwaitingPage == null || addressFromAwaitingPage.length == 0 || addressFromAwaitingPage[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Address from awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (addressFromApproverPage == null || addressFromApproverPage.length == 0 || addressFromApproverPage[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Address from Booking Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String awaitingAddress = addressFromAwaitingPage[0].trim().toLowerCase();
	    String approverAddress = addressFromApproverPage[0].trim().toLowerCase();

	    // Allow partial match in either direction
	    if (approverAddress.contains(awaitingAddress) || awaitingAddress.contains(approverAddress)) {
	        log.ReportEvent("PASS", "Hotel address matches between awaiting and approver pages.\n" +
	                "AwaitingPage: '" + awaitingAddress + "'\nApproverPage: '" + approverAddress + "'");
	    } else {
	        log.ReportEvent("FAIL", "Hotel address mismatch!\nAwaitingAdress: '" + awaitingAddress + "'\nApproverPage: '" + approverAddress + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Hotel address mismatch");
	    }
	}


	public void validateHotelNameFromFromAwaitingToApproverScreenForHotels(
	        String[] hotelNameFromAwaitingPage,
	        String[] hotelNameFromApproverPage,


	        Log log,
	        ScreenShots screenshots) {

	    if (hotelNameFromAwaitingPage == null || hotelNameFromAwaitingPage.length == 0 || hotelNameFromAwaitingPage[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Hotel name from awaiting page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (hotelNameFromApproverPage == null || hotelNameFromApproverPage.length == 0 || hotelNameFromApproverPage[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Hotel name from approver page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String AwaitingHotelName = hotelNameFromAwaitingPage[0].trim();
	    String approverHotelName = hotelNameFromApproverPage[0].trim();

	    if (!AwaitingHotelName.equalsIgnoreCase(approverHotelName)) {
	        log.ReportEvent("FAIL", "Hotel name mismatch! AwaitingPage: '" + AwaitingHotelName + "', ApproverPage: '" + approverHotelName + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Hotel name mismatch");
	    } else {
	        log.ReportEvent("PASS", "Hotel name matches on both awaiting and approver pages: " + AwaitingHotelName);
	    }
	}

	public void validateLabelFromAwaitingToApproverScreenForHotels(
	        String[] AwaitingPageData,
	        String[] approverPageLabelData,


	        Log log,
	        ScreenShots screenshots) {

	    // Null or empty checks
	    if (AwaitingPageData == null || AwaitingPageData.length == 0 || AwaitingPageData[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Label text from Awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (approverPageLabelData == null || approverPageLabelData.length == 0 || approverPageLabelData[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Label text from approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String AwaitingLabel = AwaitingPageData[0].trim();  // Changed to index 3
	    String ApproverLabel = approverPageLabelData[0].trim();

	    // Compare label texts (case-insensitive)
	    if (!AwaitingLabel.equalsIgnoreCase(ApproverLabel)) {
	        log.ReportEvent("FAIL", "Label mismatch! Awaiting Page Label: '" + AwaitingLabel + "', Approver page label '" + ApproverLabel + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Label mismatch");
	    } else {
	        log.ReportEvent("PASS", "Label matches on both awaiting and approver pages. Label: '" + AwaitingLabel + "'");
	    }
	}

	public void validateMealsTextFromAwaitingToApproverScreenForHotels(
	        String[] AwaitingPageMealData,
	        String[] ApproverPageMealsData,

	        Log log,
	        ScreenShots screenshots) {

	    if (AwaitingPageMealData == null || AwaitingPageMealData.length == 0 || AwaitingPageMealData[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Meals text from awaiting Page is missing or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    // Validate Booking Page meal text (from index 0, because getMealsTextFromBookingPg returns String[]{meals})
	    if (ApproverPageMealsData == null || ApproverPageMealsData.length == 0 || ApproverPageMealsData[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Meals text from Approver Page is missing.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String AwaitingMeals = AwaitingPageMealData[0].trim();   
	    String ApproverMeals = ApproverPageMealsData[0].trim();

	    // Compare meal texts (case-insensitive)
	    if (!AwaitingMeals.equalsIgnoreCase(ApproverMeals)) {
	        log.ReportEvent("FAIL", "Meals text mismatch! Awaiting Page: '" + AwaitingMeals + "', Approver Page: '" + ApproverMeals + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Meals text mismatch between awaiting and approver Pages.");
	    } else {
	        log.ReportEvent("PASS", "Meals text matches on both awaiting and approver pages. Meals: '" + AwaitingMeals + "'");
	    }
	}



	public void validatePolicyTextFromAwaitingToApproverScreenForHotels(
	        String[] awaitingPagePolicy,
	        String[] approverPagePolicy,


	        Log log,
	        ScreenShots screenshots) {

	    // Null or empty checks
	    if (awaitingPagePolicy == null || awaitingPagePolicy.length == 0 || awaitingPagePolicy[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Policy text from Awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (approverPagePolicy == null || approverPagePolicy.length == 0 || approverPagePolicy[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Policy text from Approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String awaitingPolicy = awaitingPagePolicy[0].trim();
	    String approverPolicy = approverPagePolicy[0].trim();

	    if (!awaitingPolicy.toLowerCase().contains(approverPolicy.toLowerCase())) {
	        log.ReportEvent("FAIL", "Policy text mismatch! AwaitingPage: '" + awaitingPolicy + "', ApproverPage: '" + approverPolicy + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Policy text mismatch");
	    } else {
	        log.ReportEvent("PASS", "Policy text from Awaiting Page is present in Approver Page.");
	    }
	}
	public void validateRefundableTextFromAwaitingToApproverScreenForHotels(
	        String[] awaitingPageRefundable,
	        String[] approveerPageRefundable,

	        Log log,
	        ScreenShots screenshots) {

	    if (awaitingPageRefundable == null || awaitingPageRefundable.length == 0 || awaitingPageRefundable[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Refundable text from Awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (approveerPageRefundable == null || approveerPageRefundable.length == 0 || approveerPageRefundable[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Refundable text from Approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String AwaitingRefundable = awaitingPageRefundable[0].trim();  // Corrected index here
	    String ApproverRefundable = approveerPageRefundable[0].trim();

	    if (!AwaitingRefundable.equalsIgnoreCase(ApproverRefundable)) {
	        log.ReportEvent("FAIL", "Refundable text mismatch! AwaitingPage: '" + AwaitingRefundable + "', ApproverPage: '" + ApproverRefundable + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Refundable text mismatch");
	    } else {
	        log.ReportEvent("PASS", "Refundable text matches on both awaiting and approver pages.");
	    }
	}


	public void validateSelectedRoomTextFromAwaitingToApproverScreenForHotels(
	        String[] awaitingPageRoomDetails,
	        String[] ApproverPageRoomText,

	        Log log,
	        ScreenShots screenshots) {

	    if (awaitingPageRoomDetails == null || awaitingPageRoomDetails.length == 0 || awaitingPageRoomDetails[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Selected Room Text from awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (ApproverPageRoomText == null || ApproverPageRoomText.length == 0 || ApproverPageRoomText[0] == null || ApproverPageRoomText[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Selected Room Text from Approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String awaitingSelectedRoomText = awaitingPageRoomDetails[0].trim();
	    String approverSelectedRoomText = ApproverPageRoomText[0].trim();

	    // Compare room texts ignoring case
	    if (!awaitingSelectedRoomText.equalsIgnoreCase(approverSelectedRoomText)) {
	        log.ReportEvent("FAIL", "Selected Room Text mismatch!\n" +
	                "   Awaiting Page: '" + awaitingSelectedRoomText + "'\n" +
	                "   Approver Page: '" + approverSelectedRoomText + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Selected Room Text mismatch between awaiting and approver Page.");
	    } else {
	        log.ReportEvent("PASS", "Selected Room Text matches between awaiting and approver pages.\n" +
	                "   Value: '" + awaitingSelectedRoomText + "'");
	    }
	}





	public void validatePriceFromAwaitingToApproverScreenForHotels(
	        String[] awaitingPageTotalFare,
	        String[] approverPageTotalFare,

	        Log log,
	        ScreenShots screenshots) {

	    if (awaitingPageTotalFare == null || awaitingPageTotalFare.length == 0 || awaitingPageTotalFare[0].isEmpty()) {
	        log.ReportEvent("FAIL", "Price from Awaiting Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (approverPageTotalFare == null || approverPageTotalFare.length == 0 || approverPageTotalFare[0] == null || approverPageTotalFare[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Total Fare from approver Page is null or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    String awaitingPriceRaw = awaitingPageTotalFare[0].trim();
	    String bookingPriceRaw = approverPageTotalFare[0].trim();

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
	        log.ReportEvent("FAIL", "Failed to extract price parts. Approver Page: '" + bookingPriceRaw + "', Awaiting Page: '" + awaitingPriceRaw + "'");
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
	            log.ReportEvent("PASS", "Price matches between Approver and awaiting pages. Value: " + bookingPricePart);
	        } else {
	            log.ReportEvent("FAIL", "Price mismatch! Approver Page Fare: '" + bookingPricePart +
	                    "', Awaiting Page Fare: '" + descPricePart + "'");
	            screenshots.takeScreenShot1();
	            Assert.fail("Total fare mismatch");
	        }

	    } catch (NumberFormatException e) {
	        log.ReportEvent("FAIL", "Failed to parse price values. Approver Page: '" + bookingPricePart + "', Awaiting Page: '" + descPricePart + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Invalid price format");
	    }
	}



	public void validateCheckInDateFromAwaitingToApproverScreenForHotels(
	        String[] awaitingPageDateText,
	        String[] approverPageDateText,
	        Log log,
	        ScreenShots screenshots) {

	    if (awaitingPageDateText == null || awaitingPageDateText.length == 0) {
	        log.ReportEvent("FAIL", "Check-in date from Awaiting Page is missing or not in expected format.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (approverPageDateText == null || approverPageDateText.length == 0 || approverPageDateText[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Check-in date from Approver Page is missing or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    //  Combine Awaiting date parts (handles both ["20", "Sep,", "2025"] and ["20 Sep 2025"])
	    String awaitingDateRaw = String.join(" ", awaitingPageDateText).trim();

	    //  Clean both dates (remove suffixes like st/nd/rd/th, commas, and trim)
	    String awaitingDate = awaitingDateRaw.replaceAll("(?<=\\d)(st|nd|rd|th)", "")
	                                         .replace(",", "")
	                                         .trim();
	    String approverDate = approverPageDateText[0].replaceAll("(?<=\\d)(st|nd|rd|th)", "")
	                                                 .replace(",", "")
	                                                 .trim();

	    if (!awaitingDate.equalsIgnoreCase(approverDate)) {
	        log.ReportEvent("FAIL", "Check-in date mismatch! Awaiting Page: '" + awaitingDate +
	                                "', Approver Page: '" + approverDate + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Check-in date mismatch between Awaiting and Approver Pages.");
	    } else {
	        log.ReportEvent("PASS", "Check-in date matches on both Awaiting and Approver pages: '" + awaitingDate + "'");
	    }
	}

	public void validateCheckOutDateFromAwaitingToApproverScreenForHotels(
	        String[] awaitingPageDateParts,
	        String[] approverPageDateText,
	        Log log,
	        ScreenShots screenshots) {

	    if (awaitingPageDateParts == null || awaitingPageDateParts.length == 0) {
	        log.ReportEvent("FAIL", "Check-out date from Awaiting Page is missing or not in expected format.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    if (approverPageDateText == null || approverPageDateText.length == 0 || approverPageDateText[0].trim().isEmpty()) {
	        log.ReportEvent("FAIL", "Check-out date from Approver Page is missing or empty.");
	        screenshots.takeScreenShot1();
	        return;
	    }

	    // . Combine date parts (handles both ["25", "Sep,", "2025"] and ["25 Sep 2025"])
	    String awaitingDateRaw = String.join(" ", awaitingPageDateParts).trim();

	    //  Clean both dates (remove suffixes, commas, and extra spaces)
	    String awaitingDate = awaitingDateRaw.replaceAll("(?<=\\d)(st|nd|rd|th)", "")
	                                         .replace(",", "")
	                                         .trim();
	    String approverDate = approverPageDateText[0].replaceAll("(?<=\\d)(st|nd|rd|th)", "")
	                                                 .replace(",", "")
	                                                 .trim();

	    if (!awaitingDate.equalsIgnoreCase(approverDate)) {
	        log.ReportEvent("FAIL", "Check-out date mismatch! Awaiting Page: '" + awaitingDate +
	                                "', Approver Page: '" + approverDate + "'");
	        screenshots.takeScreenShot1();
	        Assert.fail("Check-out date mismatch between Awaiting and Approver Pages.");
	    } else {
	        log.ReportEvent("PASS", "Check-out date matches on both Awaiting and Approver pages: '" + awaitingDate + "'");
	    }
	}

	public String[] getApproverIdFromAwaitingPg(Log log) {
	    String approverid = driver.findElement(By.xpath("(//div[contains(@class,'trip_card__container')])[1]//div[contains(text(),'Approver ID:')]//div")).getText();
	    System.out.println("approverid from awaiting Page: " + approverid);
	    log.ReportEvent("INFO", "Approverid from awaiting Page:"+ approverid);
	    return new String[]{approverid};
	}	
	
	   public void validateSelectedServicesInSelectedAndApprovalScreen(
 	           List<String> selectedServices,
 	           List<String> ApprovalServices,
 	           Log log,
 	           ScreenShots screenshots) {

 	       if (selectedServices == null || selectedServices.isEmpty()) {
 	           log.ReportEvent("FAIL", "Selected services list is null or empty.");
 	           screenshots.takeScreenShot1();
 	           Assert.fail("Selected services list is empty.");
 	           return;
 	       }

 	       if (ApprovalServices == null || ApprovalServices.isEmpty()) {
 	           log.ReportEvent("FAIL", "Services in Approval screen are null or empty.");
 	           screenshots.takeScreenShot1();
 	           Assert.fail("Popup services list is empty.");
 	           return;
 	       }

 	       // Sort both lists to compare irrespective of order
 	       List<String> sortedSelected = new ArrayList<>(selectedServices);
 	       List<String> sortedPopup = new ArrayList<>(ApprovalServices);

 	       Collections.sort(sortedSelected, String.CASE_INSENSITIVE_ORDER);
 	       Collections.sort(sortedPopup, String.CASE_INSENSITIVE_ORDER);

 	       if (!sortedSelected.equals(sortedPopup)) {
 	           log.ReportEvent("FAIL", "Mismatch in selected services.\nExpected: " + selectedServices + "\nActual: " + ApprovalServices);
 	           screenshots.takeScreenShot1();
 	           Assert.fail("Selected services and Approval screen services do not match.");
 	       } else {
 	           log.ReportEvent("PASS", "Selected services match successfully in the Approval screen: " + ApprovalServices);
 	       }
 	   }
	
	   public void validateHotelNameFromTripDetailsToApprovalScreen(
		        String[] hotelNameFromTripDetailsPage,
		        String[] hotelNameFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (hotelNameFromTripDetailsPage == null || hotelNameFromTripDetailsPage.length == 0 || hotelNameFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel name from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (hotelNameFromApprovalScreen == null || hotelNameFromApprovalScreen.length == 0 || hotelNameFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel name from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsHotelName = hotelNameFromTripDetailsPage[0].trim();
		    String approvalHotelName = hotelNameFromApprovalScreen[0].trim();

		    if (!tripDetailsHotelName.equalsIgnoreCase(approvalHotelName)) {
		        log.ReportEvent("FAIL", "Hotel name mismatch from trip details to hotel Approval page! Trip Details Page: '" + tripDetailsHotelName + "', Approval Page: '" + approvalHotelName + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel name mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel name matches on both Trip Details and hotel Approval pages: " + tripDetailsHotelName);
		    }
		}

	  public void validateHotelAddressFromTripDetailsToApprovalScreen(
		        String[] hotelAddressFromTripDetailsPage,
		        String[] hotelAddressFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (hotelAddressFromTripDetailsPage == null || hotelAddressFromTripDetailsPage.length == 0 || hotelAddressFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel address from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (hotelAddressFromApprovalScreen == null || hotelAddressFromApprovalScreen.length == 0 || hotelAddressFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel address from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsHotelAddress = hotelAddressFromTripDetailsPage[0].trim();
		    String approvalHotelAddress = hotelAddressFromApprovalScreen[0].trim();

		    if (!tripDetailsHotelAddress.equalsIgnoreCase(approvalHotelAddress)) {
		        log.ReportEvent("FAIL", "Hotel address mismatch from both Trip Details and Approval pages! Trip Details Page: '" + tripDetailsHotelAddress + "', Approval Page: '" + approvalHotelAddress + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel address mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel address matches on both Trip Details and Approval pages: " + tripDetailsHotelAddress);
		    }
		}

	  public void validateHotelPolicyFromTripDetailsToApprovalScreen(
		        String[] hotelPolicyFromTripDetailsPage,
		        String[] hotelPolicyFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (hotelPolicyFromTripDetailsPage == null || hotelPolicyFromTripDetailsPage.length == 0 || hotelPolicyFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel policy from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (hotelPolicyFromApprovalScreen == null || hotelPolicyFromApprovalScreen.length == 0 || hotelPolicyFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel policy from APproval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsPolicy = hotelPolicyFromTripDetailsPage[0].trim();
		    String ApprovalPagePolicy = hotelPolicyFromApprovalScreen[0].trim();

		    if (!tripDetailsPolicy.equalsIgnoreCase(ApprovalPagePolicy)) {
		        log.ReportEvent("FAIL", "Hotel policy mismatch between Trip Details and Approval pages! Trip Details Page: '" + tripDetailsPolicy + "', Approval Page: '" + ApprovalPagePolicy + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel policy mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel policy matches on both Trip Details and Approval pages: " + tripDetailsPolicy);
		    }
		}

	  public void validateSelectedRoomFromTripDetailsToApprovalScreen(
		        String[] roomTextFromTripDetailsPage,
		        String[] selectedRoomTextFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (roomTextFromTripDetailsPage == null || roomTextFromTripDetailsPage.length == 0 || roomTextFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Room text from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (selectedRoomTextFromApprovalScreen == null || selectedRoomTextFromApprovalScreen.length == 0 || selectedRoomTextFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Selected room text from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsRoomText = roomTextFromTripDetailsPage[0].trim();
		    String approvalPageRoomText = selectedRoomTextFromApprovalScreen[0].trim();

		    if (!tripDetailsRoomText.equalsIgnoreCase(approvalPageRoomText)) {
		        log.ReportEvent("FAIL", "Room name mismatch between Trip Details and Approval pages! Trip Details Page: '" + tripDetailsRoomText + "', Approval Page: '" + approvalPageRoomText + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Room name mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Room name matches on both Trip Details and Approval pages: " + tripDetailsRoomText);
		    }
		}

	  public void validateHotelMealsFromTripDetailsToApprovalScreen(
		        String[] mealsTextFromTripDetailsPage,
		        String[] mealsTextFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (mealsTextFromTripDetailsPage == null || mealsTextFromTripDetailsPage.length == 0 || mealsTextFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Meals text from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (mealsTextFromApprovalScreen == null || mealsTextFromApprovalScreen.length == 0 || mealsTextFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Meals text from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsMeals = mealsTextFromTripDetailsPage[0].trim().toLowerCase();
		    String approvalPageMeals = mealsTextFromApprovalScreen[0].trim().toLowerCase();

		    if (!tripDetailsMeals.contains(approvalPageMeals) && !approvalPageMeals.contains(tripDetailsMeals)) {
		        log.ReportEvent("FAIL", "Meals mismatch between trip details and approval pages! Trip Details Page: '" + tripDetailsMeals + "', Approval Page: '" + approvalPageMeals + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Meal plan mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Meals match on both Trip Details and Approval pages: '" + approvalPageMeals + "'");
		    }
		}

	  //Method to validate check in d=time 
	  public void validateHotelCheckInTimeFromTripDetailsToApprovalScreen(
		        String[] checkInTimeFromTripDetailsPage,
		        String[] checkInTimeFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (checkInTimeFromTripDetailsPage == null || checkInTimeFromTripDetailsPage.length == 0 || checkInTimeFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "hotel Check-In time from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (checkInTimeFromApprovalScreen == null || checkInTimeFromApprovalScreen.length == 0 || checkInTimeFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "hotel Check-In time from Booking page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsCheckIn = checkInTimeFromTripDetailsPage[0].trim();
		    String approvalPageCheckIn = checkInTimeFromApprovalScreen[0].trim();

		    if (!tripDetailsCheckIn.equalsIgnoreCase(approvalPageCheckIn)) {
		        log.ReportEvent("FAIL", "hotel Check-In time mismatch between Trip Details and Approval pages! Trip Details: '" + tripDetailsCheckIn + "', Approval Page: '" + approvalPageCheckIn + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("hotel Check-In time mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent(" PASS", "hotel Check-In time matches between Trip Details and Approval pages: " + tripDetailsCheckIn);
		    }
		}

	//Method to validate hotel check out time    
	  
	  public void validateHotelCheckOutTimeFromTripDetailsToApprovalScreen(
		        String[] checkOutTimeFromTripDetailsPage,
		        String[] checkOutTimeFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (checkOutTimeFromTripDetailsPage == null || checkOutTimeFromTripDetailsPage.length == 0 || checkOutTimeFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "hotel Check-Out time from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (checkOutTimeFromApprovalScreen == null || checkOutTimeFromApprovalScreen.length == 0 || checkOutTimeFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "hotel Check-Out time from Approver page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String tripDetailsCheckOut = checkOutTimeFromTripDetailsPage[0].trim();
		    String arrivalPageCheckOut = checkOutTimeFromApprovalScreen[0].trim();

		    if (!tripDetailsCheckOut.equalsIgnoreCase(arrivalPageCheckOut)) {
		        log.ReportEvent("FAIL", "hotel Check-Out time mismatch from trip details to Approver page! Trip Details: '" + tripDetailsCheckOut + "', Approver Page: '" + arrivalPageCheckOut + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel Check-Out time mismatch between Trip Details and Approver pages.");
		    } else {
		        log.ReportEvent("PASS", "hotel Check-Out time matches on both trip details and Approver pages: " + tripDetailsCheckOut);
		    }
		}

	  public void validateDaysStayFromTripDetailsToApprovalScreen(
		        String[] daysStayFromTripDetailsPage,
		        String[] nightsStayFromApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (daysStayFromTripDetailsPage == null || daysStayFromTripDetailsPage.length == 0 || daysStayFromTripDetailsPage[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Days stay from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (nightsStayFromApprovalScreen == null || nightsStayFromApprovalScreen.length == 0 || nightsStayFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Nights stay from Booking page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    // Get only the numbers
		    String daysNumber = daysStayFromTripDetailsPage[0].replaceAll("[^0-9]", "");
		    String nightsNumber = nightsStayFromApprovalScreen[0].replaceAll("[^0-9]", "");

		    if (!daysNumber.equals(nightsNumber)) {
		        log.ReportEvent("FAIL", "Stay duration mismatch! Trip Details: '" + daysNumber + "', Approval Page: '" + nightsNumber + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Stay duration mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Stay duration matches on both pages: " + daysNumber + " nights/days");
		    }
		}

	//Method to validate for buses in approval screen
	  public void validateBusNameFromDetailsToApprovalPg(String[] detailsPgBusName, String[] ApprovalPgBusName, Log log, ScreenShots screenshots) {
		    if (ApprovalPgBusName == null || ApprovalPgBusName.length == 0) {
		        log.ReportEvent("FAIL", "Bus Name from Approavl Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus name from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgBusName == null || detailsPgBusName.length == 0) {
		        log.ReportEvent("FAIL", "Bus Name from details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Name from details Page is missing.");
		        return;
		    }

		    String BusNmApproval = ApprovalPgBusName[0].trim();
		    String BusNmDetailsPg = detailsPgBusName[0].trim();

		    if (!BusNmDetailsPg.equalsIgnoreCase(BusNmApproval)) {
		        log.ReportEvent("FAIL", "Bus Name mismatch from details and Approval pg for the selected bus! Details Page: '" + BusNmDetailsPg + "', Approval Page: '" + BusNmApproval + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("\"Bus name mismatch between the Details page and the Approval page for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Bus Name matches from details to Approval page for the selected bus: " + BusNmApproval);
		    }
		}

	  public void validateBusTypeFromDetailsToApprovalPg(String[] detailsPgBusType, String[] ApprovalPgBusType, Log log, ScreenShots screenshots) {
		    if (ApprovalPgBusType == null || ApprovalPgBusType.length == 0) {
		        log.ReportEvent("FAIL", "Bus Type from approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Type from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgBusType == null || detailsPgBusType.length == 0) {
		        log.ReportEvent("FAIL", "Bus Type from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Type from Details Page is missing.");
		        return;
		    }

		    String approvalBusType = ApprovalPgBusType[0].trim();
		    String detailsBusType = detailsPgBusType[0].trim();

		    if (!detailsBusType.equalsIgnoreCase(approvalBusType)) {
		        log.ReportEvent("FAIL", "Bus Type mismatch between Details and Approval page for the selected bus! Details Page: '" + detailsBusType + "', Approval Page: '" + approvalBusType + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Type mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Bus Type matches from Details to Approval page for the selected bus: " + approvalBusType);
		    }
		}

	  
	  public void validateFromLocationBetweenDetailsToApprovalPg(String[] detailsPgFromLoc, String[] approvalPgFromLoc, Log log, ScreenShots screenshots) {
		    if (approvalPgFromLoc == null || approvalPgFromLoc.length == 0) {
		        log.ReportEvent("FAIL", "Origin location from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Origin location from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgFromLoc == null || detailsPgFromLoc.length == 0) {
		        log.ReportEvent("FAIL", "Origin location from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Origin location from Details Page is missing.");
		        return;
		    }

		    String approvalOrigin = approvalPgFromLoc[0].trim();
		    String detailsOrigin = detailsPgFromLoc[0].trim();

		    if (!detailsOrigin.equalsIgnoreCase(approvalOrigin)) {
		        log.ReportEvent("FAIL", "Origin location mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsOrigin + "', Approval Page: '" + approvalOrigin + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Origin location mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Origin location matches from Details to Approval page for the selected bus: " + approvalOrigin);
		    }
		}

	  public void validateToLocationBetweenDetailsToApprovalPg(String[] detailsPgToLoc, String[] approvalPgToLoc, Log log, ScreenShots screenshots) {
		    if (approvalPgToLoc == null || approvalPgToLoc.length == 0) {
		        log.ReportEvent("FAIL", "Destination location from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Destination location from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgToLoc == null || detailsPgToLoc.length == 0) {
		        log.ReportEvent("FAIL", "Destination location from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Destination location from Details Page is missing.");
		        return;
		    }

		    String approvalDest = approvalPgToLoc[0].trim();
		    String detailsToLoc = detailsPgToLoc[0].trim();

		    if (!detailsToLoc.equalsIgnoreCase(approvalDest)) {
		        log.ReportEvent("FAIL", "Destination location mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsToLoc + "', Approval Page: '" + approvalDest + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Destination location mismatch between Details and approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Destination location matches from Details to Approval page for the selected bus: " + approvalDest);
		    }
		}

	  
	  public void validateJourneyDateBetweenDetailsToApprovalPg(String[] detailsPgJourneyDate, String[] approvalPgJourneyDate, Log log, ScreenShots screenshots) {
		    if (approvalPgJourneyDate == null || approvalPgJourneyDate.length == 0) {
		        log.ReportEvent("FAIL", "Journey Date from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Journey Date from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgJourneyDate == null || detailsPgJourneyDate.length == 0) {
		        log.ReportEvent("FAIL", "Journey Date from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Journey Date from Details Page is missing.");
		        return;
		    }

		    String approvaalPgDate = approvalPgJourneyDate[0].trim();
		    String detailsJourneyDate = detailsPgJourneyDate[0].trim();

		    if (!detailsJourneyDate.equalsIgnoreCase(approvaalPgDate)) {
		        log.ReportEvent("FAIL", "Journey Date mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsJourneyDate + "', Approval Page: '" + approvaalPgDate + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Journey Date mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Journey Date matches from Details to Approval page for the selected bus: " + approvaalPgDate);
		    }
		}

	  public void validateDepartTimeBetweenDetailsToApprovalPg(String[] detailsPgDeptTime, String[] approvalPgDeptTime, Log log, ScreenShots screenshots) {
		    if (approvalPgDeptTime == null || approvalPgDeptTime.length == 0) {
		        log.ReportEvent("FAIL", "Departure time from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Departure time from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgDeptTime == null || detailsPgDeptTime.length == 0) {
		        log.ReportEvent("FAIL", "Departure time from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Departure time from Details Page is missing.");
		        return;
		    }

		    String ApprovalTime = approvalPgDeptTime[0].trim();
		    String detailsTime = detailsPgDeptTime[0].trim();

		    if (!detailsTime.equalsIgnoreCase(ApprovalTime)) {
		        log.ReportEvent("FAIL", "Departure time mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsTime + "', Approval Page: '" + ApprovalTime + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Departure time mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Departure time matches from Details to Approval page for the selected bus: " + ApprovalTime);
		    }
		}

	  public void validateArrivalTimeBetweenDetailsToApprovalPg(String[] detailsPgArrivalTime, String[] approvalPgArrivalTime, Log log, ScreenShots screenshots) {
		    if (approvalPgArrivalTime == null || approvalPgArrivalTime.length == 0) {
		        log.ReportEvent("FAIL", "Arrival time from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Arrival time from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgArrivalTime == null || detailsPgArrivalTime.length == 0) {
		        log.ReportEvent("FAIL", "Arrival time from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Arrival time from Details Page is missing.");
		        return;
		    }

		    String approvalArrival = approvalPgArrivalTime[0].trim();
		    String detailsArrival = detailsPgArrivalTime[0].trim();

		    if (!detailsArrival.equalsIgnoreCase(approvalArrival)) {
		        log.ReportEvent("FAIL", "Arrival time mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsArrival + "', Approval Page: '" + detailsArrival + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Arrival time mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Arrival time matches from Details to Approval page for the selected bus: " + approvalArrival);
		    }
		}

	  public void validateBoardingPointBetweenDetailsToApprovalPg(String[] detailsPgBoardingPoint, String[] ApprovalPgBoardingPoint, Log log, ScreenShots screenshots) {
		    if (ApprovalPgBoardingPoint == null || ApprovalPgBoardingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Boarding point from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Boarding point from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgBoardingPoint == null || detailsPgBoardingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Boarding point from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Boarding point from Details Page is missing.");
		        return;
		    }

		    String approverBoarding = ApprovalPgBoardingPoint[0].trim();
		    String detailsBoarding = detailsPgBoardingPoint[0].trim();

		    if (!detailsBoarding.equalsIgnoreCase(approverBoarding)) {
		        log.ReportEvent("FAIL", "Boarding point mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsBoarding + "', Approval Page: '" + approverBoarding + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Boarding point mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Boarding point matches from Details to Approval page for the selected bus: " + approverBoarding);
		    }
		}
	  
	  public void validateDroppingPointBetweenDetailsToApprovalPg(String[] detailsPgDroppingPoint, String[] approvalPgDroppingPoint, Log log, ScreenShots screenshots) {
		    if (approvalPgDroppingPoint == null || approvalPgDroppingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Dropping point from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Dropping point from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgDroppingPoint == null || detailsPgDroppingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Dropping point from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Dropping point from Details Page is missing.");
		        return;
		    }

		    String approvalDropping = approvalPgDroppingPoint[0].trim();
		    String detailsDropping = detailsPgDroppingPoint[0].trim();

		    if (!detailsDropping.equalsIgnoreCase(approvalDropping)) {
		        log.ReportEvent("FAIL", "Dropping point mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsDropping + "', Approval Page: '" + approvalDropping + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Dropping point mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Dropping point matches from Details to Approval page for the selected bus: " + approvalDropping);
		    }
		}
	  
	  public void validateDurationBetweenDetailsToApprovalPg(String[] detailsPgDuration, String[] approvalPgDuration, Log log, ScreenShots screenshots) {
		    if (approvalPgDuration == null || approvalPgDuration.length == 0) {
		        log.ReportEvent("FAIL", "Duration from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Duration from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgDuration == null || detailsPgDuration.length == 0) {
		        log.ReportEvent("FAIL", "Duration from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Duration from Details Page is missing.");
		        return;
		    }

		    String approvalDuration = approvalPgDuration[0].trim();
		    String detailsDuration = detailsPgDuration[0].trim();

		    if (!detailsDuration.equalsIgnoreCase(approvalDuration)) {
		        log.ReportEvent("FAIL", "Duration mismatch between Details and Approval pages for the selected bus! Details Page: '" + detailsDuration + "', Approval Page: '" + approvalDuration + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Duration mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Duration matches from Details to Approval page for the selected bus: " + approvalDuration);
		    }
		}

	  public void validateSelectedSeatBetweenDetailsToApprovalPg(String[] detailsPgSeats, String[] approvalPgSeats, Log log, ScreenShots screenshots) {
		    if (approvalPgSeats == null || approvalPgSeats.length == 0) {
		        log.ReportEvent("FAIL", "Selected seats from Approval Page are missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Selected seats from Approval Page are missing.");
		        return;
		    }
		    if (detailsPgSeats == null || detailsPgSeats.length == 0) {
		        log.ReportEvent("FAIL", "Selected seats from Details Page are missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Selected seats from Details Page are missing.");
		        return;
		    }

		    String approvalSeats = approvalPgSeats[0].trim();
		    String detailsSeats = detailsPgSeats[0].trim();

		    if (!detailsSeats.equalsIgnoreCase(approvalSeats)) {
		        log.ReportEvent("FAIL", "Selected seats mismatch between Details and Approval pages for the selecetd bus! Details Page: '" + detailsSeats + "', ApprovaL Page: '" + approvalSeats + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Selected seats mismatch between Details and Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Selected seats match from Details to Approval page for the selected bus: " + approvalSeats);
		    }
		}

	  public void validateBusPolicyBetweenDetailsToApprovalPg(String[] detailsPgPolicy, String[] approvalPgPolicy, Log log, ScreenShots screenshots) {
		    if (approvalPgPolicy == null || approvalPgPolicy.length == 0) {
		        log.ReportEvent("FAIL", "Bus policy from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus policy from Approval Page is missing.");
		        return;
		    }
		    if (detailsPgPolicy == null || detailsPgPolicy.length == 0) {
		        log.ReportEvent("FAIL", "Bus policy from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus policy from Details Page is missing.");
		        return;
		    }

		    String approvalPolicy = approvalPgPolicy[0].trim();
		    String detailsPolicy = detailsPgPolicy[0].trim();

		    if (!detailsPolicy.equalsIgnoreCase(approvalPolicy)) {
		        log.ReportEvent("FAIL", "Bus policy mismatch between Details and Approval pages! Details Page: '" + detailsPolicy + "', Approval Page: '" + approvalPolicy + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus policy mismatch between Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Bus policy matches from Details to Approval page.");
		    }
		}

//---------------2nd approver validation with 1st approver details
	  
	  
	  
	  
	  
	  public void validateBusNameFromApprovalToSecondApprverPg(String[] ApprovalPgBusName, String[] secondApprovalPgBusName, Log log, ScreenShots screenshots) {
		    if (ApprovalPgBusName == null || ApprovalPgBusName.length == 0) {
		        log.ReportEvent("FAIL", "Bus Name from Approavl Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus name from Approval Page is missing.");
		        return;
		    }
		    if (secondApprovalPgBusName == null || secondApprovalPgBusName.length == 0) {
		        log.ReportEvent("FAIL", "Bus Name from second Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Name from second Approval Page is missing.");
		        return;
		    }

		    String BusNmApproval = ApprovalPgBusName[0].trim();
		    String BusNmDetailsPg = secondApprovalPgBusName[0].trim();

		    if (!BusNmApproval.equalsIgnoreCase(BusNmDetailsPg)) {
		        log.ReportEvent("FAIL", "Bus Name mismatch from Approval and Second Approval pg for the selected bus! Details Page: '" + BusNmDetailsPg + "', Approval Page: '" + BusNmApproval + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("\"Bus name mismatch between the Approval page and the Second Approval page for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Bus Name matches from Approval to Second Approval page for the selected bus: " + BusNmApproval);
		    }
		}

	  public void validateBusTypeFromFromApprovalToSecondApprverPg(String[] ApprovalPgBusType, String[] secondApprovalPgBusName, Log log, ScreenShots screenshots) {
		    if (ApprovalPgBusType == null || ApprovalPgBusType.length == 0) {
		        log.ReportEvent("FAIL", "Bus Type from approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Type from Approval Page is missing.");
		        return;
		    }
		    if (secondApprovalPgBusName == null || secondApprovalPgBusName.length == 0) {
		        log.ReportEvent("FAIL", "Bus Type from second Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Type from second Approval ils Page is missing.");
		        return;
		    }

		    String approvalBusType = ApprovalPgBusType[0].trim();
		    String detailsBusType = secondApprovalPgBusName[0].trim();

		    if (!approvalBusType.equalsIgnoreCase(detailsBusType)) {
		        log.ReportEvent("FAIL", "Bus Type mismatch between Approval and Second Approval page for the selected bus! Details Page: '" + detailsBusType + "', Approval Page: '" + approvalBusType + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus Type mismatch between Approval and Second Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Bus Type matches from Approval to Second Approval page for the selected bus: " + approvalBusType);
		    }
		}

	  
	  public void validateFromLocationFromApprovalToSecondApprverPg(String[] approvalPgFromLoc, String[] SecondapprovalPgFromLoc, Log log, ScreenShots screenshots) {
		    if (approvalPgFromLoc == null || approvalPgFromLoc.length == 0) {
		        log.ReportEvent("FAIL", "Origin location from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Origin location from Approval Page is missing.");
		        return;
		    }
		    if (SecondapprovalPgFromLoc == null || SecondapprovalPgFromLoc.length == 0) {
		        log.ReportEvent("FAIL", "Origin location from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Origin location from Details Page is missing.");
		        return;
		    }

		    String approvalOrigin = approvalPgFromLoc[0].trim();
		    String detailsOrigin = SecondapprovalPgFromLoc[0].trim();

		    if (!approvalOrigin.equalsIgnoreCase(detailsOrigin)) {
		        log.ReportEvent("FAIL", "Origin location mismatch between approval and Second approval pages for the selected bus! Details Page: '" + detailsOrigin + "', Approval Page: '" + approvalOrigin + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Origin location mismatch between Approval and second Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Origin location matches from Approval to second Approval page for the selected bus: " + approvalOrigin);
		    }
		}

	  public void validateToLocationFromApprovalToSecondApprverPg(String[] approvalPgToLoc, String[] SecondapprovalPgToLoc, Log log, ScreenShots screenshots) {
		    if (approvalPgToLoc == null || approvalPgToLoc.length == 0) {
		        log.ReportEvent("FAIL", "Destination location from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Destination location from Approval Page is missing.");
		        return;
		    }
		    if (SecondapprovalPgToLoc == null || SecondapprovalPgToLoc.length == 0) {
		        log.ReportEvent("FAIL", "Destination location from Details Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Destination location from Details Page is missing.");
		        return;
		    }

		    String approvalDest = approvalPgToLoc[0].trim();
		    String detailsToLoc = SecondapprovalPgToLoc[0].trim();

		    if (!approvalDest.equalsIgnoreCase(detailsToLoc)) {
		        log.ReportEvent("FAIL", "Destination location mismatch between Approval and second Approval pages for the selected bus! Details Page: '" + detailsToLoc + "', Approval Page: '" + approvalDest + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Destination location mismatch between Approval and second approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Destination location matches from Approval to second Approval page for the selected bus: " + approvalDest);
		    }
		}

	  
	  public void validateJourneyDateFromApprovalToSecondApprverPg(String[] approvalPgJourneyDate, String[] SecondapprovalPgJourneyDate, Log log, ScreenShots screenshots) {
		    if (approvalPgJourneyDate == null || approvalPgJourneyDate.length == 0) {
		        log.ReportEvent("FAIL", "Journey Date from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Journey Date from Approval Page is missing.");
		        return;
		    }
		    if (SecondapprovalPgJourneyDate == null || SecondapprovalPgJourneyDate.length == 0) {
		        log.ReportEvent("FAIL", "Journey Date from Second approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Journey Date from Second aproval Page is missing.");
		        return;
		    }

		    String approvaalPgDate = approvalPgJourneyDate[0].trim();
		    String detailsJourneyDate = SecondapprovalPgJourneyDate[0].trim();

		    if (!approvaalPgDate.equalsIgnoreCase(detailsJourneyDate)) {
		        log.ReportEvent("FAIL", "Journey Date mismatch between Approval and Second Approval pages for the selected bus! Details Page: '" + detailsJourneyDate + "', Approval Page: '" + approvaalPgDate + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Journey Date mismatch between Approval and Second Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Journey Date matches from Approval to Second Approval page for the selected bus: " + approvaalPgDate);
		    }
		}

	  public void validateDepartTimeFromApprovalToSecondApprverPg(String[] approvalPgDeptTime, String[] secondapprovalPgDeptTime, Log log, ScreenShots screenshots) {
		    if (approvalPgDeptTime == null || approvalPgDeptTime.length == 0) {
		        log.ReportEvent("FAIL", "Departure time from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Departure time from Approval Page is missing.");
		        return;
		    }
		    if (secondapprovalPgDeptTime == null || secondapprovalPgDeptTime.length == 0) {
		        log.ReportEvent("FAIL", "Departure time from second approval  Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Departure time from second approval Page is missing.");
		        return;
		    }

		    String ApprovalTime = approvalPgDeptTime[0].trim();
		    String detailsTime = secondapprovalPgDeptTime[0].trim();

		    if (!ApprovalTime.equalsIgnoreCase(detailsTime)) {
		        log.ReportEvent("FAIL", "Departure time mismatch between Approval and second Approval pages for the selected bus! Details Page: '" + detailsTime + "', Approval Page: '" + ApprovalTime + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Departure time mismatch between Approval and second Approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Departure time matches from Approval to second Approval page for the selected bus: " + ApprovalTime);
		    }
		}

	  public void validateArrivalTimeFromApprovalToSecondApprverPg(String[] approvalPgArrivalTime, String[] SecondapprovalPgArrivalTime, Log log, ScreenShots screenshots) {
		    if (approvalPgArrivalTime == null || approvalPgArrivalTime.length == 0) {
		        log.ReportEvent("FAIL", "Arrival time from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Arrival time from Approval Page is missing.");
		        return;
		    }
		    if (SecondapprovalPgArrivalTime == null || SecondapprovalPgArrivalTime.length == 0) {
		        log.ReportEvent("FAIL", "Arrival time from Second approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Arrival time from Second approval  Page is missing.");
		        return;
		    }

		    String approvalArrival = approvalPgArrivalTime[0].trim();
		    String detailsArrival = SecondapprovalPgArrivalTime[0].trim();

		    if (!approvalArrival.equalsIgnoreCase(detailsArrival)) {
		        log.ReportEvent("FAIL", "Arrival time mismatch between approval and Second approval  pages for the selected bus! Details Page: '" + detailsArrival + "', Approval Page: '" + detailsArrival + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Arrival time mismatch between approval and Second approval  pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Arrival time matches from approval to Second approval  page for the selected bus: " + approvalArrival);
		    }
		}

	  public void validateBoardingPointFromApprovalToSecondApprverPg(String[] ApprovalPgBoardingPoint, String[] secondApprovalPgBoardingPoint, Log log, ScreenShots screenshots) {
		    if (ApprovalPgBoardingPoint == null || ApprovalPgBoardingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Boarding point from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Boarding point from Approval Page is missing.");
		        return;
		    }
		    if (secondApprovalPgBoardingPoint == null || secondApprovalPgBoardingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Boarding point from Second approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Boarding point from Second approval Page is missing.");
		        return;
		    }

		    String approverBoarding = ApprovalPgBoardingPoint[0].trim();
		    String detailsBoarding = secondApprovalPgBoardingPoint[0].trim();

		    if (!approverBoarding.equalsIgnoreCase(detailsBoarding)) {
		        log.ReportEvent("FAIL", "Boarding point mismatch between approval and Second approval pages for the selected bus! Details Page: '" + detailsBoarding + "', Approval Page: '" + approverBoarding + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Boarding point mismatch between approval and Second approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Boarding point matches from approval to Second approval page for the selected bus: " + approverBoarding);
		    }
		}
	  
	  public void validateDroppingPointFromApprovalToSecondApprverPg(String[] approvalPgDroppingPoint, String[] secondapprovalPgDroppingPoint, Log log, ScreenShots screenshots) {
		    if (approvalPgDroppingPoint == null || approvalPgDroppingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Dropping point from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Dropping point from Approval Page is missing.");
		        return;
		    }
		    if (secondapprovalPgDroppingPoint == null || secondapprovalPgDroppingPoint.length == 0) {
		        log.ReportEvent("FAIL", "Dropping point from Second approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Dropping point from Second approval Page is missing.");
		        return;
		    }

		    String approvalDropping = approvalPgDroppingPoint[0].trim();
		    String detailsDropping = secondapprovalPgDroppingPoint[0].trim();

		    if (!approvalDropping.equalsIgnoreCase(detailsDropping)) {
		        log.ReportEvent("FAIL", "Dropping point mismatch between approval and Second approval pages for the selected bus! Details Page: '" + detailsDropping + "', Approval Page: '" + approvalDropping + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Dropping point mismatch between approval and Second approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Dropping point matches from approval to Second approval page for the selected bus: " + approvalDropping);
		    }
		}
	  
	  public void validateDurationFromApprovalToSecondApprverPg(String[] approvalPgDuration, String[] secondapprovalPgDuration, Log log, ScreenShots screenshots) {
		    if (approvalPgDuration == null || approvalPgDuration.length == 0) {
		        log.ReportEvent("FAIL", "Duration from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Duration from Approval Page is missing.");
		        return;
		    }
		    if (secondapprovalPgDuration == null || secondapprovalPgDuration.length == 0) {
		        log.ReportEvent("FAIL", "Duration from Second approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Duration from Second approval Page is missing.");
		        return;
		    }

		    String approvalDuration = approvalPgDuration[0].trim();
		    String detailsDuration = secondapprovalPgDuration[0].trim();

		    if (!approvalDuration.equalsIgnoreCase(detailsDuration)) {
		        log.ReportEvent("FAIL", "Duration mismatch between approval and Second approval pages for the selected bus! Details Page: '" + detailsDuration + "', Approval Page: '" + approvalDuration + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Duration mismatch between approval and Second approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Duration matches from approval to Second approval page for the selected bus: " + approvalDuration);
		    }
		}

	  public void validateSelectedSeatFromApprovalToSecondApprverPg(String[] approvalPgSeats, String[] secondapprovalPgSeats, Log log, ScreenShots screenshots) {
		    if (approvalPgSeats == null || approvalPgSeats.length == 0) {
		        log.ReportEvent("FAIL", "Selected seats from Approval Page are missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Selected seats from Approval Page are missing.");
		        return;
		    }
		    if (secondapprovalPgSeats == null || secondapprovalPgSeats.length == 0) {
		        log.ReportEvent("FAIL", "Selected seats from Second approval Page are missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Selected seats from Second approval Page are missing.");
		        return;
		    }

		    String approvalSeats = approvalPgSeats[0].trim();
		    String detailsSeats = secondapprovalPgSeats[0].trim();

		    if (!approvalSeats.equalsIgnoreCase(detailsSeats)) {
		        log.ReportEvent("FAIL", "Selected seats mismatch between approval and Second approval pages for the selecetd bus! Details Page: '" + detailsSeats + "', ApprovaL Page: '" + approvalSeats + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Selected seats mismatch between approval and Second approval pages for the selected bus.");
		    } else {
		        log.ReportEvent("PASS", "Selected seats match from approval to Second approval page for the selected bus: " + approvalSeats);
		    }
		}

	  public void validateBusPolicyFromApprovalToSecondApprverPg(String[] approvalPgPolicy, String[] secondapprovalPgPolicy, Log log, ScreenShots screenshots) {
		    if (approvalPgPolicy == null || approvalPgPolicy.length == 0) {
		        log.ReportEvent("FAIL", "Bus policy from Approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus policy from Approval Page is missing.");
		        return;
		    }
		    if (secondapprovalPgPolicy == null || secondapprovalPgPolicy.length == 0) {
		        log.ReportEvent("FAIL", "Bus policy from Second approval Page is missing.");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus policy from Second approval Page is missing.");
		        return;
		    }

		    String approvalPolicy = approvalPgPolicy[0].trim();
		    String detailsPolicy = secondapprovalPgPolicy[0].trim();

		    if (!approvalPolicy.equalsIgnoreCase(detailsPolicy)) {
		        log.ReportEvent("FAIL", "Bus policy mismatch between approval and Second approval pages! Details Page: '" + detailsPolicy + "', Approval Page: '" + approvalPolicy + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Bus policy mismatch between approval and Second approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Bus policy matches from approval to Second approval page.");
		    }
		}
	  
	  public void clickOnErrorMsgAppearsIfAnyOneOOPolicyExixtsInTrip() {
		    // Check if the 'Approve' button exists
		    if (!driver.findElements(By.xpath("//button[text()='Approve']")).isEmpty()) {
		        driver.findElement(By.xpath("//button[text()='Approve']")).click();
		        System.out.println("Clicked on Approve button.");
		    } else {
		        System.out.println("Approve button not found. Skipping...");
		    }

		    System.out.println("Continuing with next steps...");
		}
	  
	  public void clickOnRejectErrorMsgAppearsWhnRejectTheTrip() {
		    // Check if the 'Approve' button exists
		    if (!driver.findElements(By.xpath("//button[text()='Reject']")).isEmpty()) {
		        driver.findElement(By.xpath("//button[text()='Reject']")).click();
		        System.out.println("Clicked on Reject button.");
		    } else {
		        System.out.println("reject button not found. Skipping...");
		    }

		    System.out.println("Continuing with next steps...");
		}
	  
	   public void validateHotelNameFromApprovalTosecondScreen(
		        String[] hotelNameFromApprovalScreen,
		        String[] hotelNameFromSecondApprovalScreen,

		        Log log,
		        ScreenShots screenshots) {

		    if (hotelNameFromSecondApprovalScreen == null || hotelNameFromSecondApprovalScreen.length == 0 || hotelNameFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel name from Trip Details page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (hotelNameFromApprovalScreen == null || hotelNameFromApprovalScreen.length == 0 || hotelNameFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel name from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondapprovalHotelName = hotelNameFromSecondApprovalScreen[0].trim();
		    String approvalHotelName = hotelNameFromApprovalScreen[0].trim();

		    if (!approvalHotelName.equalsIgnoreCase(secondapprovalHotelName)) {
		        log.ReportEvent("FAIL", "Hotel name mismatch from trip hotel Approval to second approver page! Approval Page: '" + approvalHotelName + "',second Approval Page: '" + secondapprovalHotelName + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel name mismatch between Trip Details and Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel name matches on both  hotel Approval and second approval pages: " + secondapprovalHotelName);
		    }
		}

	   public void validateHotelAddressFromApprovalTosecondScreen(
		        String[] hotelAddressFromApprovalScreen,
		        String[] hotelAddressFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (hotelAddressFromSecondApprovalScreen == null || hotelAddressFromSecondApprovalScreen.length == 0 || hotelAddressFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel address from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (hotelAddressFromApprovalScreen == null || hotelAddressFromApprovalScreen.length == 0 || hotelAddressFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel address from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondApprovalHotelAddress = hotelAddressFromSecondApprovalScreen[0].trim();
		    String approvalHotelAddress = hotelAddressFromApprovalScreen[0].trim();

		    if (!approvalHotelAddress.equalsIgnoreCase(secondApprovalHotelAddress)) {
		        log.ReportEvent("FAIL", "Hotel address mismatch from Approval to Second Approval pages! Approval Page: '" 
		            + approvalHotelAddress + "', Second Approval Page: '" + secondApprovalHotelAddress + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel address mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel address matches on both Approval and Second Approval pages: " + secondApprovalHotelAddress);
		    }
		}

	   public void validateHotelPolicyFromApprovalTosecondScreen(
		        String[] hotelPolicyFromApprovalScreen,
		        String[] hotelPolicyFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (hotelPolicyFromSecondApprovalScreen == null || hotelPolicyFromSecondApprovalScreen.length == 0 || hotelPolicyFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel policy from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (hotelPolicyFromApprovalScreen == null || hotelPolicyFromApprovalScreen.length == 0 || hotelPolicyFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel policy from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondApprovalPolicy = hotelPolicyFromSecondApprovalScreen[0].trim();
		    String approvalPolicy = hotelPolicyFromApprovalScreen[0].trim();

		    if (!approvalPolicy.equalsIgnoreCase(secondApprovalPolicy)) {
		        log.ReportEvent("FAIL", "Hotel policy mismatch between Approval and Second Approval pages! "
		            + "Approval Page: '" + approvalPolicy + "', Second Approval Page: '" + secondApprovalPolicy + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel policy mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel policy matches on both Approval and Second Approval pages: " + secondApprovalPolicy);
		    }
		}


	   public void validateSelectedRoomFromApprovalTosecondScreen(
		        String[] selectedRoomTextFromApprovalScreen,
		        String[] selectedRoomTextFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (selectedRoomTextFromSecondApprovalScreen == null || selectedRoomTextFromSecondApprovalScreen.length == 0 || selectedRoomTextFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Selected room text from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (selectedRoomTextFromApprovalScreen == null || selectedRoomTextFromApprovalScreen.length == 0 || selectedRoomTextFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Selected room text from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondApprovalRoomText = selectedRoomTextFromSecondApprovalScreen[0].trim();
		    String approvalRoomText = selectedRoomTextFromApprovalScreen[0].trim();

		    if (!approvalRoomText.equalsIgnoreCase(secondApprovalRoomText)) {
		        log.ReportEvent("FAIL", "Room name mismatch between Approval and Second Approval pages! "
		            + "Approval Page: '" + approvalRoomText + "', Second Approval Page: '" + secondApprovalRoomText + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Room name mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Room name matches on both Approval and Second Approval pages: " + secondApprovalRoomText);
		    }
		}


	   public void validateHotelMealsFromApprovalTosecondScreen(
		        String[] mealsTextFromApprovalScreen,
		        String[] mealsTextFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (mealsTextFromSecondApprovalScreen == null || mealsTextFromSecondApprovalScreen.length == 0 || mealsTextFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Meals text from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (mealsTextFromApprovalScreen == null || mealsTextFromApprovalScreen.length == 0 || mealsTextFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Meals text from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondApprovalMeals = mealsTextFromSecondApprovalScreen[0].trim().toLowerCase();
		    String approvalMeals = mealsTextFromApprovalScreen[0].trim().toLowerCase();

		    if (!secondApprovalMeals.contains(approvalMeals) && !approvalMeals.contains(secondApprovalMeals)) {
		        log.ReportEvent("FAIL", "Meals mismatch between Approval and Second Approval pages! "
		            + "Approval Page: '" + approvalMeals + "', Second Approval Page: '" + secondApprovalMeals + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Meal plan mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Meals match on both Approval and Second Approval pages: '" + secondApprovalMeals + "'");
		    }
		}


	  //Method to validate check in d=time 
	   public void validateHotelCheckInTimeFromApprovalTosecondScreen(
		        String[] checkInTimeFromApprovalScreen,
		        String[] checkInTimeFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (checkInTimeFromSecondApprovalScreen == null || checkInTimeFromSecondApprovalScreen.length == 0 || checkInTimeFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel Check-In time from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (checkInTimeFromApprovalScreen == null || checkInTimeFromApprovalScreen.length == 0 || checkInTimeFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel Check-In time from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondApprovalCheckIn = checkInTimeFromSecondApprovalScreen[0].trim();
		    String approvalCheckIn = checkInTimeFromApprovalScreen[0].trim();

		    if (!approvalCheckIn.equalsIgnoreCase(secondApprovalCheckIn)) {
		        log.ReportEvent("FAIL", "Hotel Check-In time mismatch between Approval and Second Approval pages! "
		            + "Approval Page: '" + approvalCheckIn + "', Second Approval Page: '" + secondApprovalCheckIn + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel Check-In time mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel Check-In time matches on both Approval and Second Approval pages: " + secondApprovalCheckIn);
		    }
		}

	//Method to validate hotel check out time    
	  
	   public void validateHotelCheckOutTimeFromApprovalTosecondScreen(
		        String[] checkOutTimeFromApprovalScreen,
		        String[] checkOutTimeFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (checkOutTimeFromSecondApprovalScreen == null || checkOutTimeFromSecondApprovalScreen.length == 0 || checkOutTimeFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel Check-Out time from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (checkOutTimeFromApprovalScreen == null || checkOutTimeFromApprovalScreen.length == 0 || checkOutTimeFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Hotel Check-Out time from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    String secondApprovalCheckOut = checkOutTimeFromSecondApprovalScreen[0].trim();
		    String approvalCheckOut = checkOutTimeFromApprovalScreen[0].trim();

		    if (!approvalCheckOut.equalsIgnoreCase(secondApprovalCheckOut)) {
		        log.ReportEvent("FAIL", "Hotel Check-Out time mismatch between Approval and Second Approval pages! "
		            + "Approval Page: '" + approvalCheckOut + "', Second Approval Page: '" + secondApprovalCheckOut + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Hotel Check-Out time mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Hotel Check-Out time matches on both Approval and Second Approval pages: " + secondApprovalCheckOut);
		    }
		}

	   public void validateDaysStayFromApprovalTosecondScreen(
		        String[] daysStayFromApprovalScreen,
		        String[] nightsStayFromSecondApprovalScreen,
		        Log log,
		        ScreenShots screenshots) {

		    if (nightsStayFromSecondApprovalScreen == null || nightsStayFromSecondApprovalScreen.length == 0 || nightsStayFromSecondApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Stay duration from Second Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    if (daysStayFromApprovalScreen == null || daysStayFromApprovalScreen.length == 0 || daysStayFromApprovalScreen[0].isEmpty()) {
		        log.ReportEvent("FAIL", "Stay duration from Approval page is null or empty.");
		        screenshots.takeScreenShot1();
		        return;
		    }

		    // Extract numeric values (days/nights count)
		    String approvalDaysNumber = daysStayFromApprovalScreen[0].replaceAll("[^0-9]", "");
		    String secondApprovalNightsNumber = nightsStayFromSecondApprovalScreen[0].replaceAll("[^0-9]", "");

		    if (!approvalDaysNumber.equals(secondApprovalNightsNumber)) {
		        log.ReportEvent("FAIL", "Stay duration mismatch between Approval and Second Approval pages! "
		            + "Approval Page: '" + approvalDaysNumber + "', Second Approval Page: '" + secondApprovalNightsNumber + "'");
		        screenshots.takeScreenShot1();
		        Assert.fail("Stay duration mismatch between Approval and Second Approval pages.");
		    } else {
		        log.ReportEvent("PASS", "Stay duration matches on both Approval and Second Approval pages: " 
		            + approvalDaysNumber + " nights/days");
		    }
		}
	   
	   


	  
}
 



