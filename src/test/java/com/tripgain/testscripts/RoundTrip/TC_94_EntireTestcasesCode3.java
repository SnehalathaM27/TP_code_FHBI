package com.tripgain.testscripts.RoundTrip;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tripgain.collectionofpages.SiteChecker;
import com.tripgain.collectionofpages.Tripgain_FutureDates;
import com.tripgain.collectionofpages.Tripgain_Login;
import com.tripgain.collectionofpages.Tripgain_RoundTripResultsScreen;
import com.tripgain.collectionofpages.Tripgain_homepage;
import com.tripgain.collectionofpages.Tripgain_resultspage;
import com.tripgain.collectionofpages.policyDates;
import com.tripgain.common.DataProviderUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.GenerateDates;
import com.tripgain.common.Getdata;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.testscripts.BaseClass;

public class TC_94_EntireTestcasesCode3 extends BaseClass{
	WebDriver driver;    
	ExtentReports extent;
    ExtentTest test;
    String className = "";
    Log Log;  // Declare Log object
    ScreenShots screenShots;  // Declare Log object
    ExtantManager extantManager;
  
 // ThreadLocal to store Excel data per test thread
 	static ThreadLocal<Map<String, String>> excelDataThread = new ThreadLocal<>();
    int number=1;


    @Test(dataProvider = "sheetBasedData", dataProviderClass = DataProviderUtils.class)
    public void myTest(Map<String, String> excelData) throws InterruptedException, IOException, ParseException, TimeoutException {
        System.out.println("Running test with: " + excelData);
try {	    
        String username = excelData.get("UserName");
        String pwd = excelData.get("Password");
     
        
        String username1 = excelData.get("UserName1");
        String pwd1 = excelData.get("Password1");
        number++;

       
        String origin = excelData.get("Origin");
        System.out.println(origin);
        
        String destination = excelData.get("Destination");
        System.out.println(destination);

        String travelClass = excelData.get("Class");
        System.out.println(travelClass);
        
        int Adults = Integer.parseInt(excelData.get("Adults"));
        System.out.println(Adults);
        
        
        String times = excelData.get("Times");
        int flightStartHour = Integer.parseInt(excelData.get("FlightStartHour"));
        int flightStartMinute = Integer.parseInt(excelData.get("FlightStartMinute"));
        int flightEndHour = Integer.parseInt(excelData.get("FlightEndHour"));
        int flightEndMinute = Integer.parseInt(excelData.get("FlightEndMinute"));
        
        String returntimes = excelData.get("ReturnTimes");
        int returnflightStartHour = Integer.parseInt(excelData.get("ReturnFlightStartHour"));
        int returnflightStartMinute = Integer.parseInt(excelData.get("ReturnFlightStartMinute"));
        int returnflightEndHour = Integer.parseInt(excelData.get("ReturnFlightEndHour"));
        int returnflightEndMinute = Integer.parseInt(excelData.get("ReturnFlightEndMinute"));
        
        String Stops = excelData.get("Stops");
        String numberOfStops = excelData.get("NumberOfStops");

        String returnStops = excelData.get("ReturnStops");
        String returnNumberOfStops = excelData.get("ReturnNumberOfStops");
        
        String[] dates=GenerateDates.GenerateDatesToSelectFlights();
        String fromDate=dates[11];
        String fromMonthYear=dates[12];
        String returnDate=dates[1]; 
        String returnMonthYear=dates[3];
        
        
        // Login to TripGain Application
        Tripgain_Login tripgainLogin= new Tripgain_Login(driver);
SiteChecker Site_Checker=new SiteChecker(driver);
		
        Site_Checker.waitForSiteToBeUp(driver, "https://v3.tripgain.com/flights", 20, 180);
        tripgainLogin.enterUserName(username);
        tripgainLogin.enterPasswordName(pwd);
        tripgainLogin.clickButton(); 
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		Thread.sleep(2000);

		 
		//Functions to Search flights on Home Page  
		
        Tripgain_homepage tripgainhomepage = new Tripgain_homepage(driver);
        Tripgain_resultspage tripgainresultspage=new Tripgain_resultspage(driver);
        Tripgain_RoundTripResultsScreen trs=new Tripgain_RoundTripResultsScreen(driver);
		
        trs.printVersion(Log);
        Thread.sleep(2000);
        tripgainhomepage.Clickroundtrip();
        tripgainhomepage.searchFlightsOnHomePage(Log, screenShots,origin, destination,  fromDate, fromMonthYear, returnDate, returnMonthYear, travelClass, Adults);
        Thread.sleep(5000);

        trs.validateFlightsResultsForRoundTrip(Log, screenShots);
Thread.sleep(15000); 
		
	
// -------------------------------------------------------------------------------------------------------
//Function to click and validate onward arrival Time 
trs.selectOnWardArrivalTimeroundtrip(times);
trs.validateFlightsArrivalTimeroundtrip(flightStartHour, flightStartMinute, flightEndHour, flightEndMinute, Log, screenShots);

System.out.println("validate onward arrival Time done");
//----------------------------------------------------------------------------------------------------------


//Function to click and validate  on return arrival Time 
trs.selectReturnArrivalTimeRoundtrip(returntimes);
Thread.sleep(3000);     
trs.validatereturnFlightsArrivalTimeroundtrip(returnflightStartHour, returnflightStartMinute, returnflightEndHour, returnflightEndMinute, Log, screenShots);
System.out.println("validate return arrival Time done");


//----------------------------------------------------------------------------------------------------------

//Function to click on stops
trs.roundTripClickOnWardStops(Stops);  
trs.validateFlightsStopsOnResultScreen(numberOfStops,Log, screenShots);
System.out.println("validate stops done");

//----------------------------------------------------------------------------------------------------------

//Function to click on return stops

trs.roundTripClickReturnStops(returnStops);
trs.validateFlightsreturnStopsOnResultScreen(returnNumberOfStops,Log, screenShots);
System.out.println("validate return stops  done");

//----------------------------------------------------------------------------------------------------------

//Function to validate fare rule prices

trs.selectFromFaretypePrices(1, "StretchPlus", Log, screenShots);
trs.selectReturnFaretypePrices(1, "SME", Log, screenShots);
//----------------------------------------------------------------------------------------------------------

trs.clickContinueButton();

//Method to validate meals 
double selectMealsOnwardTotalPrice1 = trs.selectMealsOnward();
double selectMealsOnwardTotalPrice2 = trs.selectMealsReturn();
double totalMeal = trs.addTotalMealsPrice(Log, screenShots,selectMealsOnwardTotalPrice1,selectMealsOnwardTotalPrice2);
trs.validateMealsPrice(Log, screenShots,totalMeal);
System.out.println("validate meals done");

/*
//baggage
double selectBaggageOnwardTotal =trs.selectBaggageOnward();
double selectBaggageReturnTotal =trs.selectBaggageReturn();
double addTotalBaggagePrice1= trs.addTotalBaggagePrice(Log, screenShots, selectBaggageOnwardTotal,selectBaggageReturnTotal);
trs.validateBaggagePrice(addTotalBaggagePrice1);
 
        System.out.println("BAGGAGE DONE");  */


//----------------------------------------------------------------------------------------------------------
 
       //Function to Logout from Application
    		//tripgainhomepage.logOutFromApplication(Log, screenShots);
    		driver.quit();
         
	}catch (Exception e)
	{
		String errorMessage = "Exception occurred: " + e.toString();
		Log.ReportEvent("FAIL", errorMessage);
		screenShots.takeScreenShot();
		e.printStackTrace();  // You already have this, good for console logs
		Assert.fail(errorMessage);
	}
	 
	}


	@BeforeMethod(alwaysRun = true)
	@Parameters("browser")
	public void launchApplication(String browser, Method method, Object[] testDataObjects) {
	// Get test data passed from DataProvider
	@SuppressWarnings("unchecked")
	Map<String, String> testData = (Map<String, String>) testDataObjects[0];
	excelDataThread.set(testData);  // Set it early!

	String url = (testData != null && testData.get("URL") != null) ? testData.get("URL") : "https://defaulturl.com";

	extantManager = new ExtantManager();
	extantManager.setUpExtentReporter(browser);
	className = this.getClass().getSimpleName();
	String testName = className + "_" + number;
	extantManager.createTest(testName);
	test = ExtantManager.getTest();
	extent = extantManager.getReport();
	test.log(Status.INFO, "Execution Started Successfully");

	driver = launchBrowser(browser, url);
	Log = new Log(driver, test);
	screenShots = new ScreenShots(driver, test);
	}

	@AfterMethod
	public void tearDown() {
	if (driver != null) {
		driver.quit();
		extantManager.flushReport();
	}
	}




	}