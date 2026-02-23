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

public class TC_95_EntireTestcasesCode4 extends BaseClass{
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
        
        
        String title1 = excelData.get("title");
		String OnwardMealsSelect = excelData.get("onwardMealsSelect");
		String[] OnwardMealsSelectSplit = OnwardMealsSelect.split(",");
		String ReturnMealsSelect = excelData.get("returnMealsSelect");
		String[] ReturnMealsSelectSplit = ReturnMealsSelect.split(",");
		int adults = Integer.parseInt(excelData.get("Adults"));
		String OnwardBaggageSelect = excelData.get("onwardBaggageSelect");
		String[] OnwardBaggageSelectSplit = OnwardBaggageSelect.split(",");
		String ReturnBaggageSelect = excelData.get("returnBaggageSelect");
		String[] ReturnBaggageSelectSplit = ReturnBaggageSelect.split(",");
		
		int index = Integer.parseInt(excelData.get("Index"));
		 int returnfareIndex = Integer.parseInt(excelData.get("ReturnfareIndex"));

	        String returnFare = excelData.get("ReturnFare");

	        int departFareIndex = Integer.parseInt(excelData.get("DepartFareIndex"));

	        String departFare = excelData.get("DepartFare");
	        
	        String clickOnWardStops = excelData.get("clickOnWardStops");
	        String validateFlightsStopsOnResultScreen = excelData.get("validateFlightsStopsOnResultScreen");

	        String times = excelData.get("Times");
	        int flightStartHour = Integer.parseInt(excelData.get("FlightStartHour"));
	        int flightStartMinute = Integer.parseInt(excelData.get("FlightStartMinute"));
	        int flightEndHour = Integer.parseInt(excelData.get("FlightEndHour"));
	        int flightEndMinute = Integer.parseInt(excelData.get("FlightEndMinute"));
	        
			int ReturnIndex = Integer.parseInt(excelData.get("returnIndex"));

        
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
        Thread.sleep(10000);

        trs.validateFlightsResultsForRoundTrip(Log, screenShots);
Thread.sleep(5000); 

String[] topBarDetails = trs.getTopBarFlightDetails();



////Slider
//double values[] =trs.defaultPriceRangeOfSlider(driver,Log,screenShots);
//double minValue=values[0];
//double minimumValue=minValue+3000;
//trs.adjustMinimumSliderToValue(driver,minimumValue);
//
//trs.verifyPriceRangeValuesOnResultScreen(Log,screenShots);
//
//System.out.println("MINIMUM SLIDER DONE");

trs.selectReturnDepartTimeroundtrip(times);
Thread.sleep(2000);
//trs.validateroundtripreturnDepartureTimeIsSelected(Log, screenShots, "06 - 12");
trs.validatereturnFlightsDepartureTimeOnResultScreen(flightStartHour, flightStartMinute, flightEndHour, flightEndMinute, Log, screenShots);

System.out.println("RETURN Depart Time DONE");


String[] flightResultDetails = trs.getDepartFlightResultCardDetails(index, Log); 
String departStopsText = flightResultDetails[11];  // stops info from results page


//trs.validateFlightDetailsfromTopbarToDeprtflightIndex(topBarDetails, flightResultDetails, Log, screenShots);

//fare prices 

String[] fareAndBaggageDetails = trs.getDepartfareTypeAndFarePriceAndBaggage(departFare, Log, screenShots);

                   //-----------------return -----------------
String[] flightReturnResultDetails = trs.getReturnFlightResultCardDetails(ReturnIndex, Log);
String returnStopsText = flightReturnResultDetails[1];

//trs.validatetopbarwithReturnFlightDetails(topBarDetails, flightReturnResultDetails, Log, screenShots);

String[] ReturnfareAndBaggageDetails = trs.getReturnfareTypeAndFarePriceAndBaggage(returnFare, Log, screenShots);

String bottomBarTotalPrice = trs.compareSumOfPopupFaresWithBottomBarTotal(fareAndBaggageDetails, ReturnfareAndBaggageDetails, Log, screenShots);


//Method to click OnWardStops

trs.roundTripClickOnWardStops(clickOnWardStops);  
trs.validateFlightsStopsOnResultScreen(validateFlightsStopsOnResultScreen,Log, screenShots);

System.out.println("ONWARD STOPS DONE");


trs.clickOnContinue();

Thread.sleep(5000);

String[] bookingPageDepartDetails = trs.getBookingPageFlightDepartCardDetails(departStopsText);
trs.validateFlightDetailsFromResultToBooking(flightResultDetails, bookingPageDepartDetails, Log, screenShots);


String[] bookingPageReturnDetails =trs.getBookingPageFlightReturnCardDetails(returnStopsText);
trs.validateReturnFlightDetailsFromResultToBooking(flightReturnResultDetails, bookingPageReturnDetails, Log, screenShots);


trs.validateDepartFareAndBaggageDetails(bookingPageDepartDetails, fareAndBaggageDetails, Log, screenShots);

trs.validateReturnFareAndBaggagetillBooking(bookingPageReturnDetails, ReturnfareAndBaggageDetails, Log, screenShots);

trs.compareBottomBarTotalWithBookingPageTotal(bottomBarTotalPrice, Log, screenShots);


// -------------------------------------------------------------------------------------------------------
/*  tripgain_resultspage.selectFromAndToFlightsBasedOnIndex(1);
trs.validateDepatureFaretypeToBookingPg(departFareIndex, departFare, Log, screenShots);
Thread.sleep(3000);
System.out.println("DEPART FARE TYPE DONE");

         trs.clickBackToSearchResults();
	System.out.println("BACK TO RESULTS BUTTON CLICKED");

Thread.sleep(5000);
trs.validateReturnFaretypeToBookingPg(returnfareIndex, returnFare, Log, screenShots);

System.out.println("FARE DONE");*/
//----------------------------------------------------------------------------------------

String[] titles = title1.split(",");
trs.enterAdultDetailsForInterNational(titles,adults,Log,screenShots);

System.out.println("ADULTS DONE");

//pick seat

tripgainresultspage.selectSeatFormPickSeat(Log, screenShots);

System.out.println("SEAT SELECTION DONE");


//----------------------------------------------------------------------------------------s


double selectBaggageOnwardTotal =trs.selectBaggageOnward();
double selectBaggageReturnTotal =trs.selectBaggageReturn();
double addTotalBaggagePrice1= trs.addTotalBaggagePrice(Log, screenShots, selectBaggageOnwardTotal,selectBaggageReturnTotal);
trs.validateBaggagePrice(addTotalBaggagePrice1);
 
        System.out.println("BAGGAGE DONE");

//----------------------------------------------------------------------------------------

    
    		 
        
        
        
        

/*trs.selectDepartment();
trs.selectProject();
trs.selectCostcenter();*/


System.out.println("APPROVAL DONE");
//----------------------------------------------------------------------------------------------------------
 
       //Function to Logout from Application
    		//tripgainhomepage.logOutFromApplication(Log, screenShots);
    		//driver.quit();
         
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