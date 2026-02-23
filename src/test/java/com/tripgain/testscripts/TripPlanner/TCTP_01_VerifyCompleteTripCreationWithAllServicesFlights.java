package com.tripgain.testscripts.TripPlanner;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.List;
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
import com.tripgain.collectionofpages.TripPlanner;
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

public class TCTP_01_VerifyCompleteTripCreationWithAllServicesFlights extends BaseClass{
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
                String destination= excelData.get("Destination");
                System.out.println(destination);

               String dropDownValue= excelData.get("DropDownValue");
        String tripName= excelData.get("TripName");
        String services= excelData.get("Services");
        
        
    	int index = Integer.parseInt(excelData.get("Index"));
		// int returnfareIndex = Integer.parseInt(excelData.get("ReturnfareIndex"));

	        String returnFare = excelData.get("ReturnFare");

	    //    int departFareIndex = Integer.parseInt(excelData.get("DepartFareIndex"));

	        String departFare = excelData.get("DepartFare");
	        
	        String clickOnWardStops = excelData.get("clickOnWardStops");
	        
	        String validateFlightsStopsOnResultScreen = excelData.get("validateFlightsStopsOnResultScreen");

	        String times = excelData.get("Times");
	        int flightStartHour = Integer.parseInt(excelData.get("FlightStartHour"));
	        int flightStartMinute = Integer.parseInt(excelData.get("FlightStartMinute"));
	        int flightEndHour = Integer.parseInt(excelData.get("FlightEndHour"));
	        int flightEndMinute = Integer.parseInt(excelData.get("FlightEndMinute"));
	        
			int ReturnIndex = Integer.parseInt(excelData.get("returnIndex"));
		   
			int adults = Integer.parseInt(excelData.get("Adults"));
			System.out.println(adults);

	        
	        String title1 = excelData.get("title");

       
	     
			String[] dates=GenerateDates.GenerateDatesToSelectFlights();
	        String fromDate=dates[0];
	        String fromMonthYear=dates[2];
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
        TripPlanner Trip_Planner=new TripPlanner(driver);
		
        trs.printVersion(Log);
        Trip_Planner.clickTripPlannerDropdown();
        Trip_Planner.clickDropdownValue(dropDownValue);
        Trip_Planner.createTrip();
        Trip_Planner.enterNameThisTrip(tripName);
         String origindetails = Trip_Planner.enterfrom(origin);
         String destdetails = Trip_Planner.enterTo(destination);

        String journeydatedetails = Trip_Planner.selectJourneyDate(fromDate, fromMonthYear);

        String returndatedetails = Trip_Planner.selectReturnDate(returnDate, returnMonthYear);
        Trip_Planner.selectDepartment();
        Trip_Planner.selectCostCenter();
        Trip_Planner.selectProject();

        // List<String> servicesdetails = Trip_Planner.selectServices("Flight","Hotel");
         List<String> servicesdetails = Trip_Planner.selectServices("Flight");

         
        Trip_Planner.clickAddTripButton();
        Thread.sleep(1000);

     //   String[] viewpagedetails = Trip_Planner.getTripViewPageDetails(Log, screenShots);
       
        Trip_Planner.validateTripDetailsFromCreateTripToViewTrip(
        	    origindetails,
        	    destdetails,
        	    journeydatedetails,
        	    returndatedetails,
        	    servicesdetails, Log, screenShots
        	);
        
        String viewPageTripId = Trip_Planner.getViewPageId(Log, screenShots);     
        Thread.sleep(1000);
        Trip_Planner.clickFlightsOnline();
        Trip_Planner.selectClasses("Premium Economy");
        Thread.sleep(1000);
        String[] flightInputData = Trip_Planner.getFlightsOnlineOrigin(Log);
        Trip_Planner.clickAddToCartButton();
        Thread.sleep(1000);
  //      String[] getAddCartData = Trip_Planner.getDataForvalidateFlightsOnlineOriginData(Log, screenShots);
        Trip_Planner.validateFlightsOnlineDataToSearchFlightsData(Log, screenShots);
        Thread.sleep(1000);
        Trip_Planner.clickSearchFlightsButton();
        String[] resultsData = Trip_Planner.getTripIdAndODLocInFlightsResultsScreen(Log);
        Thread.sleep(8000);
        
        Trip_Planner.validateTripIdAndODLocInFlightsResultsScreen(flightInputData, viewPageTripId, resultsData, Log, screenShots);
         
        //--------------------------------------------------------------------------------------------------
        trs.selectReturnDepartTimeroundtrip(times);
        Thread.sleep(2000);
        //trs.validateroundtripreturnDepartureTimeIsSelected(Log, screenShots, "06 - 12");
        trs.validatereturnFlightsDepartureTimeOnResultScreen(flightStartHour, flightStartMinute, flightEndHour, flightEndMinute, Log, screenShots);

        System.out.println("RETURN Depart Time DONE");

        //Method to click OnWardStops

        trs.roundTripClickOnWardStops(clickOnWardStops);  
        trs.validateFlightsStopsOnResultScreen(validateFlightsStopsOnResultScreen,Log, screenShots);

        System.out.println("ONWARD STOPS DONE");

     //  String[] flightResultDetails = trs.getDepartFlightResultCardDetails(index, Log); 
     //   String departStopsText = flightResultDetails[10];  // stops info from results page

    	trs.clickOnDepartFlightBasedOnIndex(Log, "2");
    	Thread.sleep(3000);

		Map<String, List<String>> departingData = trs.getDataFromUiForDepartingFlightForDomestic();
		Thread.sleep(2000);

                                                    //fare prices 
        String[] fareAndBaggageDetails = trs.getDepartfareTypeAndFarePriceAndBaggage(departFare, Log, screenShots);

                           //-----------------return -----------------
        
        trs.roundTripClickReturnStops(clickOnWardStops);
        trs.validateFlightsreturnStopsOnResultScreen(validateFlightsStopsOnResultScreen,Log, screenShots);
        
    //  String[] flightReturnResultDetails = trs.getReturnFlightResultCardDetails(ReturnIndex, Log);
      //  String returnStopsText = flightReturnResultDetails[10];
        
    	trs.clickOnReturnFlightBasedOnIndex(Log, "2");
    	Thread.sleep(3000);
		Map<String, List<String>> returnDepartingData = trs.getDataFromUiForReturnFlightForDomestic();
		Thread.sleep(4000);

        //trs.validatetopbarwithReturnFlightDetails(topBarDetails, flightReturnResultDetails, Log, screenShots);

        String[] ReturnfareAndBaggageDetails = trs.getReturnfareTypeAndFarePriceAndBaggage(returnFare, Log, screenShots);

                                             //bottom bar prices validation
        String bottomBarTotalPrice = trs.compareSumOfPopupFaresWithBottomBarTotal(fareAndBaggageDetails, ReturnfareAndBaggageDetails, Log, screenShots);

        trs.clickOnContinue();
        Thread.sleep(3000);

                                             //booking page details
        
		trs.validateBookingScreenIsDisplayed(Log,screenShots);

      // String[] bookingPageDepartDetails = trs.getBookingPageFlightDepartCardDetails(departStopsText);
       // trs.validateFlightDetailsFromResultToBooking(flightResultDetails, bookingPageDepartDetails, Log, screenShots);

		Map<String, List<String>> fbDepartingData = trs.getDataFromUiForFbDepartingFlightForDomesticForBookingScreen();
		Thread.sleep(1000);

		String[] bookingPageFareandCabinDetails = trs.getBookingPageFareAndBaggageDetails();
		Thread.sleep(1000);

		trs.validateDepartingAndFbDepartingDataForDomestic(departingData, fbDepartingData, Log, screenShots);



        //String[] bookingPageReturnDetails =trs.getBookingPageFlightReturnCardDetails(returnStopsText);
    //    trs.validateReturnFlightDetailsFromResultToBooking(flightReturnResultDetails, bookingPageReturnDetails, Log, screenShots);

		
		Map<String, List<String>> returnFbDepartingData = trs.getDataFromUiForFbReturnFlightForDomesticForBookingScreen();
		Thread.sleep(1000);
		//baggage and fare details 
		String[] bookingPageReturnFareandCabinDetails = trs.getBookingPageReturnFareAndBaggageDetails();
		Thread.sleep(1000);

		trs.validateReturnAndFbReturnDataForDomestic(returnDepartingData, returnFbDepartingData, Log, screenShots);


        trs.validateDepartFareAndBaggageDetails(bookingPageFareandCabinDetails, fareAndBaggageDetails, Log, screenShots);

        trs.validateReturnFareAndBaggagetillBooking(bookingPageReturnFareandCabinDetails, ReturnfareAndBaggageDetails, Log, screenShots);

        trs.compareBottomBarTotalWithBookingPageTotal(bottomBarTotalPrice, Log, screenShots);

                                         //adults
//        String[] titles = title1.split(",");
//        trs.enterAdultDetailsForInterNational(titles,adults,Log,screenShots);
//
//        System.out.println("ADULTS DONE");

                                                //pick seat

        tripgainresultspage.selectSeatFormPickSeat(Log, screenShots);
        System.out.println("SEAT SELECTION DONE");

                                            //Baggage details
        double selectBaggageOnwardTotal =trs.selectBaggageOnward();
        double selectBaggageReturnTotal =trs.selectBaggageReturn();
        double addTotalBaggagePrice1= trs.addTotalBaggagePrice(Log, screenShots, selectBaggageOnwardTotal,selectBaggageReturnTotal);
        trs.validateBaggagePrice(addTotalBaggagePrice1);
                System.out.println("BAGGAGE DONE");

                String bookingGrandTotal  = trs.getGrandTotalPriceFromBookingPage(Log, screenShots);
      
//---------------------------------------------------------------------------------------------------------
                
                Trip_Planner.clickAddTripAndContinueButton();
                Thread.sleep(2000);
                Trip_Planner.clickSubmitTripButton();
                Thread.sleep(1000);
                Trip_Planner.clickYesSubmitButton();
                Thread.sleep(3000);
                Trip_Planner.getApprovalIdFromRequestScreen(viewPageTripId, Log, screenShots);
                Thread.sleep(1000);
                String[] reuestScreenViewPrice = Trip_Planner.getPriceFromRequestScreenView(Log, screenShots);
                
                Trip_Planner.validatePriceFromFlightsBookingPageToRequestViewPage(reuestScreenViewPrice, bookingGrandTotal, Log, screenShots);
                
                
            
                
                
                
                
    		


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