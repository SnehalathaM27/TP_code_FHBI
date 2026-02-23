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

public class TCTP_02_VerifyCompleteTripCreationWithAllServicesBuses extends BaseClass{
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
		int boardingPointIndex = Integer.parseInt(excelData.get("BoardingPointIndex"));

        
        
        int seatCount = Integer.parseInt(excelData.get("SeatCount"));
		int dropingPointIndex = Integer.parseInt(excelData.get("DropingPointIndex"));
		int viewBus = Integer.parseInt(excelData.get("ViewBus"));
        String cityDestination = excelData.get("CityDestination");
        String policy = excelData.get("Policy");
        
        String seatType = excelData.get("SeatType");
        String[] seatTypes = seatType.split(",");
        System.out.println(seatTypes);
        
        String departTime = excelData.get("DepartTime");
        String[] departTimes = seatType.split(",");
        System.out.println(departTimes);
       
	     
			String[] dates=GenerateDates.GenerateDatesToSelectFlights();
	        String fromDate=dates[0];
	        String fromMonthYear=dates[2];
	        String returnDate=dates[1]; 
	        String returnMonthYear=dates[3];
	        String frombusDate=dates[0];
	        String frombusMonthYear=dates[2];
	        
        
        
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
         List<String> servicesdetails = Trip_Planner.selectServices("bus");

         
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
        
        Trip_Planner.clickBuses();
        Thread.sleep(2000);
        Trip_Planner.enterfromLocForBuses("Hyderabad");
        Trip_Planner.entertoLocForBuses("Tirupathi");
        Trip_Planner.selectJourneyDateForBuses(frombusDate, frombusMonthYear);
        Trip_Planner.clickAddToCartButtonForBuses();
        String[] sectordata = Trip_Planner.getSectorDataForBuses(Log, screenShots);
        Trip_Planner.clickSearchBusesButton();
        Thread.sleep(9000);
        String[] BusesResultPageData = Trip_Planner.getTripIdAndODLocInFlightsResultsScreenForBuses(Log, screenShots);
      
      //----------------------------------------------------------------------------------------------------------------------------------------------------------
      			
      			String clickOnPolicyFilter = Trip_Planner.clickOnPolicyFilter(policy,Log, screenShots);
      			Trip_Planner.PolicyValidation(clickOnPolicyFilter,Log, screenShots);
      		
      //------------------------------------------------------------------------------
      			//List<String> filtersToClickSeat= Arrays.asList("Seater", "Sleeper");
      			List<String> clickedSeatTypes = Trip_Planner.clickOnSeatType(seatTypes,Log, screenShots);
      			Trip_Planner.SeatTypeValidation(clickedSeatTypes,Log, screenShots);
      //--------------------------------------------------------------------------
      			
      			//List<String> filtersToClickDepart= Arrays.asList("Before 6 AM", "After 6 PM");
      			List<String> clickedDepartTime = Trip_Planner.clickOnDepartTime(departTimes,Log, screenShots);
      			Trip_Planner.ValidateDepartTime(clickedDepartTime,Log, screenShots);
      			
      //-------------------------------------------------------------------------------------------
      		    
      			Trip_Planner.DepartDescendingFilter(Log, screenShots);
      			Trip_Planner.TimeOrderCheckInDescendingForDepart(Log, screenShots);
      			Trip_Planner.DepartAscendingFilter(Log, screenShots);
      			Trip_Planner.TimeOrderCheckInAscendingForDepart(Log, screenShots);
      			
      			
      			
      		//-------------------------------------------------------------------------------------------



      			Trip_Planner.ArrivalDescendingFilter(Log, screenShots);
      			Trip_Planner.TimeOrderCheckInDescendingForArrival(Log, screenShots);
      			Trip_Planner.ArrivalAscendingFilter(Log, screenShots);
      			Trip_Planner.TimeOrderCheckInAscendingForArrival(Log, screenShots);
    			
    			
    //-------------------------------------------------------------------------------------------
    			
      			Trip_Planner.getOperatorsName(Log, screenShots);
      			Trip_Planner.clickOnViewSeatButton(Log, screenShots,viewBus);
      			Trip_Planner.clickOnBoardingPoint(boardingPointIndex,Log, screenShots);
      			Trip_Planner.clickOnDropingPoint(dropingPointIndex,Log, screenShots);
      			Trip_Planner.pickSeat(seatCount,Log, screenShots);
    			List<String> getSeatNames = (List<String>) Trip_Planner.getSeatNames(Log, screenShots);
    			String[] getTextInResultPage = Trip_Planner.getTextInResultPage();
    			String totalAmount = Trip_Planner.selectedTotalAmountPrice();

    			Trip_Planner.clickOnConfirmSeat(Log, screenShots);
    			Trip_Planner.reasonForSelectionPopUp1(Log, screenShots);
    			//booking page 
    			Trip_Planner.validateReviewYourTripPage(Log, screenShots);
    			List<String> fetchSeatInBookingPage = Trip_Planner.fetchSeatInBookingPage(Log, screenShots);
    			String[] getTextInBookingPage = Trip_Planner.getTextInBookingPage();
    			Trip_Planner.validateResultAndBookingPageData(getTextInResultPage,getTextInBookingPage,getSeatNames,fetchSeatInBookingPage,Log, screenShots);
    			Trip_Planner.validatePriceInBookingPage(totalAmount,Log, screenShots);
    			Trip_Planner.clickIdCardTypeForBuses("Voter ID");
    			Thread.sleep(1000);
    			Trip_Planner.enterIdcardNumberForBuses("12345678990887");
                Thread.sleep(1000);
                Trip_Planner.clickAddTripAndContinueButton();
                Thread.sleep(2000);
                
                
                
                
                
                
                
                
                
                
                
                Trip_Planner.clickSubmitTripButton();
                Thread.sleep(1000);
                Trip_Planner.clickYesSubmitButton();
                Thread.sleep(3000);
                Trip_Planner.getApprovalIdFromRequestScreen(viewPageTripId, Log, screenShots);
                Thread.sleep(1000);
                String[] reuestScreenViewPrice = Trip_Planner.getPriceFromRequestScreenView(Log, screenShots);
                
 
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