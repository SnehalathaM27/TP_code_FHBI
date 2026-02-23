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

public class TC_92_EntireTestcasesCode1 extends BaseClass{
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
        
        String destination = excelData.get("Destination");

        String travelClass = excelData.get("Class");
        
        int Adults = Integer.parseInt(excelData.get("Adults"));
        
        Thread.sleep(15000);
        
        String clickOnWardStops = excelData.get("clickOnWardStops");

        
        String roundTripClickReturnStops = excelData.get("roundTripClickReturnStops");
        

        int VerifyFlightsDetailsOnResultScreenForLocalIndex = Integer.parseInt(excelData.get("VerifyFlightsDetailsOnResultScreenForLocalIndex"));
        
        String selectAirLines = excelData.get("selectAirLines");
        
        int selectFromAndToFlightsBasedOnIndex = Integer.parseInt(excelData.get("selectFromAndToFlightsBasedOnIndex"));
        

        String onwardMealSelect = excelData.get("onwardMealSelect");
        String[] onwardMealSelectSplit = onwardMealSelect.split(",");

        
        String returnMealSelect = excelData.get("returnMealSelect");
        String[] returnMealSelectSplit = returnMealSelect.split(",");

        String selectCurrencyDropDownValues = excelData.get("selectCurrencyDropDownValues");

        String validateCurrencyRoundTrip = excelData.get("validateCurrencyRoundTrip");

        String clickAirlineCheckboxesRoundtripDomestic = excelData.get("clickAirlineCheckboxesRoundtripDomestic");
        
        int FlightCardIndex = Integer.parseInt(excelData.get("FlightCardIndex"));

        String validateFlightsStopsOnResultScreen = excelData.get("validateFlightsStopsOnResultScreen");
        

        String validateFlightsreturnStopsOnResultScreen = excelData.get("validateFlightsreturnStopsOnResultScreen");
        
        String flightNames=excelData.get("flightNames");
        
        int selectToFlightsBasedOnIndex = Integer.parseInt(excelData.get("selectToFlightsBasedOnIndex"));
        
        
        String OnwardBaggageSelect = excelData.get("onwardBaggageSelect");
        System.out.println(OnwardBaggageSelect);
        String[] OnwardBaggageSelectSplit = OnwardBaggageSelect.split(",");
        
        String ReturnBaggageSelect = excelData.get("returnBaggageSelect");
        System.out.println(ReturnBaggageSelect);
        String[] ReturnBaggageSelectSplit = ReturnBaggageSelect.split(",");


      
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

		                         //Slider
		double values[] =trs.defaultPriceRangeOfSlider(driver,Log,screenShots);
		double minValue=values[0];
		double minimumValue=minValue+3000;
		trs.adjustMinimumSliderToValue(driver,minimumValue);
		
		trs.verifyPriceRangeValuesOnResultScreen(Log,screenShots);
		
		System.out.println("MINIMUM SLIDER DONE");
		// -------------------------------------------------------------------------------------------------------

		Thread.sleep(3000);

                                    //Currency
		trs.defaultCurrencyValue(Log,screenShots);
		
		System.out.println("DEFAULT CURRENCY  DONE");

		
//		 //Function click On Currency Drop Down
  		tripgainresultspage.clickOnCurrencyDropDown();
//	
//		//Function to select Currency Drop Down Values
  		tripgainresultspage.selectCurrencyDropDownValues(driver,selectCurrencyDropDownValues);
//		
  		tripgainresultspage.validateCurrencyRoundTrip(validateCurrencyRoundTrip,Log,screenShots);
  		
		System.out.println("VALIDATE CURRENCY DONE");


  	// -------------------------------------------------------------------------------------------------------
  		Thread.sleep(3000);


	                                 //AirLines
		 String airlinename=trs.clickAirlineCheckboxRoundtripDomestic1(clickAirlineCheckboxesRoundtripDomestic);
	        
	     trs.validateAirlinesDomesticroundtrip(FlightCardIndex, Log, screenShots, airlinename);
	     Thread.sleep(2000);
	 //    tripgain_roundtrip_resultpage.selectAirLines(clickAirlineCheckboxesRoundtripDomestic);
        
			System.out.println("AIRLINES DONE");

// -------------------------------------------------------------------------------------------------------
	     
	     Thread.sleep(3000);

		trs.selectAirLines(selectAirLines);
		
// -------------------------------------------------------------------------------------------------------
		
		Thread.sleep(3000);

		
		//Method to click OnWardStops
		
		trs.roundTripClickOnWardStops(clickOnWardStops);  
		trs.validateFlightsStopsOnResultScreen(validateFlightsStopsOnResultScreen,Log, screenShots);
	
		System.out.println("ONWARD STOPS DONE");

// -------------------------------------------------------------------------------------------------------
		
		Thread.sleep(3000);

		
	                       //Method to click round Trip Click Return Stops
		
		  trs.roundTripClickReturnStops(roundTripClickReturnStops);
	        trs.validateFlightsreturnStopsOnResultScreen(validateFlightsreturnStopsOnResultScreen,Log, screenShots);
		
			System.out.println("RETURN STOPS DONE");

//	---------------------------------------------------------------------------------------------------------
	        
	
Thread.sleep(3000);

	        
		 //Function to validate Check In Baggage Functionality on Result Screen

	        tripgainresultspage.clickOnCheckInBaggage();

	        System.out.println(flightNames);
			 String[] names = flightNames.split(",");
	         String airlineName=trs.clickOnParticularAirline(names);
	         Thread.sleep(3000);
	         trs.selectFromAndToFlightsBasedOnIndex(selectFromAndToFlightsBasedOnIndex);
	         Thread.sleep(2000);
	         trs.validateAllBaggageDetailsForAllAirline(airlineName,Log,screenShots);
	         Thread.sleep(2000);
	         trs.closePopup1();
	         Thread.sleep(2000);
	         trs.selectToFlightsBasedOnIndex(selectToFlightsBasedOnIndex);
	         trs.validateAllBaggageDetailsForAllAirline(airlineName,Log,screenShots);
	         Thread.sleep(2000);
	         trs.closePopup1();
      
	 		System.out.println("CHECKIN BAGGAGE DONE");

	         Thread.sleep(3000);
 //------------------------------------------------------------------------------------------------------------
        trs.clickOnContinue();
        
        Thread.sleep(3000);
 
 //--------------------------------------------------------------------------------------------------------
        Thread.sleep(3000);

        
                                      //pick seat
         
        tripgainresultspage.selectSeatFormPickSeat(Log, screenShots);
        
		System.out.println("SEAT SELECTION DONE");

    	
 //-------------------------------------------------------------------------------------------------------------
        Thread.sleep(3000);

                                         //Meals
    	
        double selectMealsOnwardTotalPrice1 = trs.selectMealsOnward();
        double selectMealsOnwardTotalPrice2 = trs.selectMealsReturn();
        double totalMeal = trs.addTotalMealsPrice(Log, screenShots,selectMealsOnwardTotalPrice1,selectMealsOnwardTotalPrice2);

        trs.validateMealsPrice(Log, screenShots,totalMeal);
 
		System.out.println("MEALS DONE");

 //-------------------------------------------------------------------------------------------------------
        Thread.sleep(3000);

        
                          // Project details 
     /*   trs.selectDepartment();
        trs.selectProject();
        trs.selectCostcenter();
        
		System.out.println("PROJECT DETAILS  DONE");*/


        //Functions to Send Approval on Booking Page
        trs.clickOnSendApprovalButton();
        trs.validateSendApprovalToastMessage(Log, screenShots);
        
		System.out.println("SEND FOR APPROVAL DONE");

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