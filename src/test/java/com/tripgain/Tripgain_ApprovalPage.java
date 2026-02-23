package com.tripgain;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

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

public class Tripgain_ApprovalPage {
WebDriver driver;
	

	public Tripgain_ApprovalPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	

	public void approvalPageValidation(String origin, String destination, String travels, Log Log, ScreenShots ScreenShots,String profile[]) throws InterruptedException {
		Thread.sleep(3000);
          //If the string ends with s, it gets removed; otherwise, it stays unchanged.
		travels = travels.replaceAll("s$", "");

		System.out.println(origin);
		System.out.println(destination);
		
	    String employeeCode = profile[0];
	    String approvalManager = profile[1];
	    String traveller = profile[2];
	    String travellerWithCode = traveller + " (" + employeeCode + ")";
	    String routeToFind = origin + " - " + destination;
	    System.out.println(routeToFind);

	    List<WebElement> cards = driver.findElements(By.cssSelector("div.MuiPaper-root"));

	    for (WebElement card : cards) {
	        try {
	            String route = card.findElement(By.xpath(".//span[text()='Origin - Destination']/following-sibling::h6")).getText().trim();
System.out.println(route);
	            if (route.equalsIgnoreCase(routeToFind)) {

	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", card);
	                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(card));

	                // 🔄 Index-based element access within card
	                String travelType = card.findElement(By.xpath("(.//h6[contains(@class,'MuiTypography-subtitle1')])[3]")).getText().trim();
	                String approverName = card.findElement(By.xpath("(.//h6[contains(@class,'MuiTypography-subtitle1')])[4]")).getText().trim();
	                String requestedBy = card.findElement(By.xpath("(.//h6[contains(@class,'MuiTypography-subtitle1')])[5]")).getText().trim();

	                System.out.println(travelType+" "+travels);
	                
	                System.out.println(approverName+" "+approvalManager);
	                System.out.println(requestedBy+" "+travellerWithCode);
	                
	                if (travelType.equalsIgnoreCase(travels)
	                        && approverName.equalsIgnoreCase(approvalManager)
	                        && requestedBy.equalsIgnoreCase(travellerWithCode)) {

	                    Log.ReportEvent("PASS", "✅ Sent approval details are correct:\n"
	                            + "Flight Type: " + travelType   + "\n"
	                            + "Approver Name: " + approverName + "\n"
	                            + "Requested By: " + requestedBy);

	                    ScreenShots.takeScreenShot1();
	                }
Thread.sleep(2000);
	                WebElement detailsBtn = card.findElement(By.xpath(".//button[text()='Details']"));
	                detailsBtn.click();

	                System.out.println("✅ Clicked Details for: " + routeToFind);
	                return;
	            }
	        } catch (NoSuchElementException e) {
	            // Handle missing data gracefully
	        }
	    }

	}
	
	public void validateDetailsNavBar1(String expected[], Log Log, ScreenShots ScreenShots) {
	    try {
	        String from = expected[0];
	        String to = expected[1];
	        String deptTime = expected[4];
	        String arrTime = expected[5];
	        String flightCodeExpected = expected[3];
	        String priceExpected = expected[9];
	        String deptDateExpected = expected[2];

	        System.out.println("Expected Flight Details:");
	        System.out.println("From: " + from);
	        System.out.println("To: " + to);
	        System.out.println("Date: " + deptDateExpected);
	        System.out.println("Flight Code: " + flightCodeExpected);
	        System.out.println("Departure Time: " + deptTime);
	        System.out.println("Arrival Time: " + arrTime);
	        System.out.println("Price: " + priceExpected);

	        WebElement detailsSection = driver.findElement(By.xpath("//h6[text()='Selected Flight Details']"));
	        if (!detailsSection.isDisplayed()) {
	            throw new RuntimeException("Flight details section not displayed.");
	        }

	        String origin = driver.findElement(By.xpath("(//h6[@class='MuiTypography-root MuiTypography-subtitle1 bold css-17jtg62'])[1]")).getText();
	        String destination = driver.findElement(By.xpath("(//h6[@class='MuiTypography-root MuiTypography-subtitle1 bold css-17jtg62'])[4]")).getText();
	        String departTime = driver.findElement(By.xpath("(//h6[@class='MuiTypography-root MuiTypography-subtitle1 bold css-17jtg62'])[2]")).getText();
	        String arrivalTime = driver.findElement(By.xpath("(//h6[@class='MuiTypography-root MuiTypography-subtitle1 bold css-17jtg62'])[5]")).getText();
	        String flightCodeActual = driver.findElement(By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 d-flex justify-content-center css-hhjtk']")).getText();
	        String priceActual = driver.findElement(By.xpath("//strong[@class='price']")).getText();
	        String departDate = driver.findElement(By.xpath("(//h6[@class='MuiTypography-root MuiTypography-subtitle1 bold css-17jtg62'])[1]/following-sibling::small")).getText();

	        origin = origin.replaceAll(".*\\((.*?)\\)", "$1").trim();
	        destination = destination.replaceAll(".*\\((.*?)\\)", "$1").trim();
	        flightCodeExpected = flightCodeExpected.replaceAll("[()]", "").trim();
	        flightCodeActual = flightCodeActual.replaceAll("[()]", "").trim();
	        priceActual = priceActual.replaceAll("[^0-9]", "");

	        departDate = departDate.replaceAll("on\\s*", "").replaceAll(",", "").trim();
	        String[] dateParts = departDate.split(" ");
	        String day = dateParts[0].replaceAll("\\D+", "");
	        String month = dateParts[1];
	        String monthNum = "";

	       
	        switch (month) {
	            case "Jan": monthNum = "01"; break;
	            case "Feb": monthNum = "02"; break;
	            case "Mar": monthNum = "03"; break;
	            case "Apr": monthNum = "04"; break;
	            case "May": monthNum = "05"; break;
	            case "Jun": monthNum = "06"; break;
	            case "Jul": monthNum = "07"; break;
	            case "Aug": monthNum = "08"; break;
	            case "Sep": monthNum = "09"; break;
	            case "Oct": monthNum = "10"; break;
	            case "Nov": monthNum = "11"; break;
	            case "Dec": monthNum = "12"; break;
	            default:
	                throw new IllegalArgumentException("Unknown month abbreviation: " + month);
	        }

	        if (day.length() == 1) day = "0" + day;
	        String currentYear = String.valueOf(java.time.LocalDate.now().getYear());
	        String departDateFormatted = currentYear + "-" + monthNum + "-" + day;

	        boolean allMatch = origin.equals(from) &&
	                           destination.equals(to) &&
	                           departTime.equals(deptTime) &&
	                           arrivalTime.equals(arrTime) &&
	                           flightCodeExpected.equals(flightCodeActual) &&
	                           priceActual.equals(priceExpected) &&
	                           departDateFormatted.equals(deptDateExpected);

	        if (allMatch) {
	            System.out.println("✅ All flight details match!");
	            Log.ReportEvent("PASS", "✅ Flight details match:\n"
	                    + "Origin: " + from + "\n"
	                    + "Destination: " + to + "\n"
	                    + "Date: " + deptDateExpected + "\n"
	                    + "Flight Code: " + flightCodeExpected + "\n"
	                    + "Departure Time: " + deptTime + "\n"
	                    + "Arrival Time: " + arrTime + "\n"
	                    + "Price: " + priceExpected);
	            ScreenShots.takeScreenShot1();
	        } else {
	            System.out.println("❌ Flight details mismatch.");
	            System.out.println("🔍 Expected vs Actual:");
	            System.out.println("From: " + from + " | " + origin);
	            System.out.println("To: " + to + " | " + destination);
	            System.out.println("Departure Time: " + deptTime + " | " + departTime);
	            System.out.println("Arrival Time: " + arrTime + " | " + arrivalTime);
	            System.out.println("Flight Code: " + flightCodeExpected + " | " + flightCodeActual);
	            System.out.println("Price: " + priceExpected + " | " + priceActual);
	            System.out.println("Date: " + deptDateExpected + " | " + departDateFormatted);

	            Log.ReportEvent("FAIL", "❌ Flight details mismatch.\n"
	                    + "Expected:\n"
	                    + "Origin: " + from + ", Destination: " + to + ", Date: " + deptDateExpected
	                    + ", Flight Code: " + flightCodeExpected + ", Departure: " + deptTime
	                    + ", Arrival: " + arrTime + ", Price: " + priceExpected + "\n"
	                    + "Actual:\n"
	                    + "Origin: " + origin + ", Destination: " + destination + ", Date: " + departDateFormatted
	                    + ", Flight Code: " + flightCodeActual + ", Departure: " + departTime
	                    + ", Arrival: " + arrivalTime + ", Price: " + priceActual);

	            ScreenShots.takeScreenShot1();
	            Assert.fail("Flight details validation failed.");
	        }

	    } catch (Exception e) {
	        System.out.println("❌ Exception during flight detail validation: " + e.getMessage());
	        e.printStackTrace();
	        Log.ReportEvent("ERROR", "❗ Exception occurred: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        Assert.fail("Test failed due to exception.");
	    }
	}

}
