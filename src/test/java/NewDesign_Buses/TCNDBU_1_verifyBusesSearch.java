package NewDesign_Buses;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.lang.reflect.Method;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tripgain.collectionofpages.NewDesignHotelsSearchPage;
import com.tripgain.collectionofpages.NewDesign_AwaitingApprovalScreen;
import com.tripgain.collectionofpages.NewDesign_Buses_BookingPage;
import com.tripgain.collectionofpages.NewDesign_Buses_ResultsPage;
import com.tripgain.collectionofpages.NewDesign_Buses_SerachPage;
import com.tripgain.collectionofpages.NewDesign_EmulateProcess;
import com.tripgain.collectionofpages.NewDesign_Hotels_BookingPage;
import com.tripgain.collectionofpages.NewDesign_Hotels_DescPage;
import com.tripgain.collectionofpages.NewDesign_Hotels_ResultsPage;
import com.tripgain.collectionofpages.NewDesign_Login;
import com.tripgain.collectionofpages.NewDesign_Trips;
import com.tripgain.collectionofpages.SiteChecker;
import com.tripgain.collectionofpages.SkyTravelers_Hotels_BookingPage;
import com.tripgain.collectionofpages.SkyTravelers_Hotels_DescriptionPage;
import com.tripgain.collectionofpages.SkyTravelers_Hotels_Login;
import com.tripgain.collectionofpages.SkyTravelers_Hotels_SearchPage;
import com.tripgain.collectionofpages.SkyTravelers_Hotels_confirmBookingPage;
import com.tripgain.collectionofpages.TripPlanner;
import com.tripgain.collectionofpages.Tripgain_FutureDates;
import com.tripgain.collectionofpages.Tripgain_HomePage_Flights;
import com.tripgain.collectionofpages.Tripgain_Login;
import com.tripgain.collectionofpages.Tripgain_RoundTripResultsScreen;
import com.tripgain.collectionofpages.Tripgain_homepage;
import com.tripgain.collectionofpages.Tripgain_resultspage;
import com.tripgain.collectionofpages.policyDates;
import com.tripgain.common.APIMonitor;
import com.tripgain.common.DataProviderUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.GenerateDates;
import com.tripgain.common.Getdata;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.common.SimpleLogAnalyzer; // ADD THIS IMPORT
import com.tripgain.testscripts.BaseClass;

//Add these imports if not already present
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.CapabilityType;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;

public class TCNDBU_1_verifyBusesSearch extends BaseClass {
	WebDriver driver;
	ExtentReports extent;
	ExtentTest test;
	String className = "";
	Log Log; // Declare Log object
	ScreenShots screenShots; // Declare Log object
	ExtantManager extantManager;

	// ThreadLocal to store Excel data per test thread
	static ThreadLocal<Map<String, String>> excelDataThread = new ThreadLocal<>();
	int number = 1;

	@Test(dataProvider = "sheetBasedData", dataProviderClass = DataProviderUtils.class)
	public void myTest(Map<String, String> excelData)
			throws InterruptedException, IOException, ParseException, TimeoutException {
		System.out.println("Running test with: " + excelData);
		try {
			String username = excelData.get("UserName");
			String pwd = excelData.get("Password");

			System.out.println(username);
			System.out.println(pwd);

			String username1 = excelData.get("UserName1");
			String pwd1 = excelData.get("Password1");
			number++;

			String origin = excelData.get("Origin");
			System.out.println(origin);
			String dest = excelData.get("Destination");
			System.out.println(dest);

			String searchby = excelData.get("SearchBy");
			String searchvalue = excelData.get("ApproverSearchValue");
			String remarks = excelData.get("Remarks");
			String status = excelData.get("Status");
			String travellerSearchValue = excelData.get("TravellerSearchValue");
			String approverSearchValue2 = excelData.get("ApproverSearchValue2");
			String status2 = excelData.get("Status2");

			String[] dates = GenerateDates.GenerateDatesToSelectFlights();
			String fromDate = dates[0];
			String fromMonthYear = dates[2];
			String returnDate = dates[1];
			String returnMonthYear = dates[3];

			// Login to application
			// Login to application
			NewDesign_Login NewDesignLogin = new NewDesign_Login(driver);
			Tripgain_HomePage_Flights tripgain_HomePage = new Tripgain_HomePage_Flights(driver);

			NewDesignLogin.enterUserName(username);
			NewDesignLogin.enterPasswordName(pwd);
			NewDesignLogin.clickButton();
			Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
			Thread.sleep(2000);
			tripgain_HomePage.clickIfPresentCloseBtn();

		//	Log.checkConsoleErrorsWithFormat("After Login");

			NewDesignLogin.clickOnTravel();

			NewDesign_Buses_SerachPage NewDesignBusesSerachPage = new NewDesign_Buses_SerachPage(driver);
			NewDesign_Buses_ResultsPage NewDesignBusesResultsPage = new NewDesign_Buses_ResultsPage(driver);
			NewDesign_Buses_BookingPage NewDesignBusesBookingPage = new NewDesign_Buses_BookingPage(driver);
			NewDesign_AwaitingApprovalScreen NewDesign_Awaiting_ApprovalScreen = new NewDesign_AwaitingApprovalScreen(
					driver);
			NewDesign_EmulateProcess NewDesign_Emulate_Process = new NewDesign_EmulateProcess(driver);
			NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);

			NewDesign_Hotels_ResultsPage NewDesignHotelsResultsPage = new NewDesign_Hotels_ResultsPage(driver);
			NewDesign_Hotels_DescPage NewDesignHotels_DescPage = new NewDesign_Hotels_DescPage(driver);
			NewDesign_Hotels_BookingPage NewDesign_HotelsBookingPage = new NewDesign_Hotels_BookingPage(driver);
			NewDesignHotelsSearchPage NewDesign_HotelsSearchPage = new NewDesignHotelsSearchPage(driver);

			NewDesignBusesSerachPage.clickOnBuses();
			Thread.sleep(3000);
			NewDesignBusesSerachPage.enterfromLocForBuses(origin, Log);
			Thread.sleep(2000);
			NewDesignBusesSerachPage.enterToLocForBuses(dest, Log);
			NewDesign_HotelsSearchPage.selectBusJourneyDate(fromDate, fromMonthYear);
			NewDesignBusesSerachPage.SearchBus(Log, screenShots);
			Thread.sleep(3000);

			// 📊 CHECK 2: Check APIs after search
			// Log.captureNetworkCalls("AFTER SEARCH");

		//	Log.checkConsoleErrorsWithFormat("After Search");

		//	Log.injectApiLogger();

			String[] checkindateResultPage = NewDesignBusesResultsPage.getCheckInDateTextFromResultPage();
			String[] ResultPgOrigin = NewDesignBusesResultsPage.getOriginFromresultPg();
			String[] ResultPgDest = NewDesignBusesResultsPage.getDestFromresultPg();

			NewDesignBusesResultsPage.clickAndValidateSearchOperator(Log, screenShots);

			String[] Busdetails = NewDesignBusesResultsPage.getBusDetailsFromListingByIndex(0);

		//	Log.checkConsoleErrorsWithFormat("After bus selected");

//			Log.injectApiLogger();

			String[] SeatType = NewDesignBusesResultsPage.getSeatTypeTextFromresultPgAfterSelect();
			NewDesignBusesResultsPage.selectAvailableBerth(Log, screenShots);
			String[] BoardingDetails = NewDesignBusesResultsPage.selectBoardingPoints(Log, screenShots);
			String[] DroppingPoint = NewDesignBusesResultsPage.selectDroppingPoint(Log, screenShots);
			Thread.sleep(1000);

			String SeatSelectionPrice = NewDesignBusesResultsPage.getPriceAfterSeatSelection(Log, screenShots);
			NewDesignBusesResultsPage.clcikOnConfirmSeat();
			Thread.sleep(2000);

			NewDesignBusesResultsPage.reasonForSelectionPopUp();

			String[] BookingPgArrival = NewDesignBusesBookingPage.getArrivalFromBookingPg();
			String[] BookingPgBoardingPoint = NewDesignBusesBookingPage.getBoardingPointFromBookingPg();
			String[] BookingPgBusName = NewDesignBusesBookingPage.getBusNameFromBookingPg();
			String[] BookingPgSeater = NewDesignBusesBookingPage.getBusSeaterTextFromBookingPg();
			String[] BookingPgDate = NewDesignBusesBookingPage.getDateFromBookingPg();
			String[] BookingPgDepartTime = NewDesignBusesBookingPage.getDepartureFromBookingPg();
			String[] BookingDest = NewDesignBusesBookingPage.getDestFromBookingPg();
			String[] BookingPgdroppingPoint = NewDesignBusesBookingPage.getDroppingPointFromBookingPg();
			String[] bookingPgDuration = NewDesignBusesBookingPage.getDurationFromBookingPg();
			String[] BookingPgorigin = NewDesignBusesBookingPage.getOriginFromBookingPg();
			String[] BookingPgPolicy = NewDesignBusesBookingPage.getPolicyFromBookingPg();
			String[] BookingPgPrice = NewDesignBusesBookingPage.getPriceFromBookingPg();

			// 📊 CHECK 5: Check APIs on booking page
			// Log.captureNetworkCalls("Booking screen");
		//	Log.checkConsoleErrorsWithFormat("booking screen");

			//Log.injectApiLogger();

			NewDesignBusesBookingPage.validateOriginFromResultToBookingpage(BookingPgorigin, ResultPgOrigin, Log,screenShots);
			NewDesignBusesBookingPage.validateDestFromResultToBookingPage(BookingDest, ResultPgDest, Log, screenShots);
			NewDesignBusesBookingPage.validateBusOperatorNameFromListingToBookingPage(Busdetails, BookingPgBusName, Log,screenShots);
			NewDesignBusesBookingPage.validateBoardingPointLocationFromListingToBookingPage(BoardingDetails,
					BookingPgBoardingPoint, Log, screenShots);
			NewDesignBusesBookingPage.validateDroppingPointLocationFromListingToBookingPage(DroppingPoint,
					BookingPgdroppingPoint, Log, screenShots);
			NewDesignBusesBookingPage.validateSeaterTypeFromListingToBookingPage(SeatType, BookingPgSeater, Log,
					screenShots);
			NewDesignBusesBookingPage.validateCheckInDateBetweenResultAndBookingPages(checkindateResultPage,
					BookingPgDate, Log, screenShots);
	//	NewDesignBusesBookingPage.validateDepartureTimeFromBoardingToBookingPage(BoardingDetails,BookingPgDepartTime, Log, screenShots);
			NewDesignBusesBookingPage.validateArrivalTimeFromListingToBookingPage(Busdetails, BookingPgArrival, Log,
					screenShots);
			NewDesignBusesBookingPage.validateDurationFromListingToBookingPage(Busdetails, bookingPgDuration, Log,
					screenShots);
			NewDesignBusesBookingPage.validatePolicyFromListingToBookingPage(Busdetails, BookingPgPolicy, Log,
					screenShots);
			NewDesignBusesBookingPage.validatePriceFromresultToBookingPage(BookingPgPrice, SeatSelectionPrice, Log,
					screenShots);

		//	NewDesignBusesBookingPage.addPassengerDetails();

		//	Log.ReportEvent("info", "=== CHECK BEFORE APPROVAL ===");

			NewDesignBusesBookingPage.clickSendForApproval(Log, screenShots);
			Thread.sleep(3000);
			
		// ========== CRITICAL CONSOLE CHECK AFTER APPROVAL ==========

		/*	Log.ReportEvent("info", "Console After Approval");

			// 4. COMPREHENSIVE console check
			Log.checkConsoleErrorsWithFormat("After Approval Click");

			// Check if we have console errors
			boolean hasConsoleIssues = Log.hasConsoleErrors("Final Console Check");

			if (hasConsoleIssues) {
				Log.ReportEvent("fail", "❌ TEST FAILING: Console errors detected");

			} else {
				// Log.ReportEvent("pass", "✅ No console errors");
			} */

		//	showAllConsoleEntries(); 

	  NewDesign_Awaiting_ApprovalScreen.waitUntilAwaitingPageLoads(Log,screenShots);
	 
	 //--------------------------------Awaiting page-------------------------------------------
	
	  String[] LocationFromAwaitingScreen=NewDesign_Awaiting_ApprovalScreen.getLocationDetailsFromAwaitingScreen();
	  String[] DateFromAwaitingScreen =NewDesign_Awaiting_ApprovalScreen.getdateFromAwaitingPg(); String[]
       ApproveridFromAwaitingScreen =NewDesign_Awaiting_ApprovalScreen.getApproverIdFromAwaitingPg(Log, "bus");
	 
	  NewDesign_Awaiting_ApprovalScreen.clickOnViewTripInAwaitingScreen(); 
	  String[] LocationFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen. getLocationDetailsFromViewTripInAwaitingScreen(); 
	  String[] BusNameFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen.getBusNameFromViewTripInAwaitingScreen();
	  String[] BusTypeFromViewtripScreen =NewDesign_Awaiting_ApprovalScreen.getBustypeFromViewTripInAwaitingScreen();
	 String[] BusOriginFromViewtripScreen =NewDesign_Awaiting_ApprovalScreen.getBusoriginLocFromViewTripInAwaitingScreen();
	 String[] BusDestFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen.getBusDestLocFromViewTripInAwaitingScreen() ;
	 String[] BusDepartTimeFromViewtripScreen =NewDesign_Awaiting_ApprovalScreen.getBusDepartTimeFromViewTripInAwaitingScreen(); 
	 String[] BusArrivalTimeFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen.getBusArrTimeFromViewTripInAwaitingScreen();
	 
	 String[] BusDepartDateFromViewtripScreen =NewDesign_Awaiting_ApprovalScreen.getBusDepartDateFromViewTripInAwaitingScreen(); String[]
	 BusDurationFromViewtripScreen =NewDesign_Awaiting_ApprovalScreen.getBusDurationFromViewTripInAwaitingScreen();
	String[] BusPolicyFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen.getBusPolicyFromViewTripInAwaitingScreen();
	String[] BusApproverIdFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen.getApproverIdFromViewTripInViewTripScreen();
	  String[] BusPriceFromViewtripScreen = NewDesign_Awaiting_ApprovalScreen.getPriceFromViewTripInViewTripScreen();
	 NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);
	 
	 NewDesign_Awaiting_ApprovalScreen.validateLocationsDetailsFromAwaitingApprovalToViewTrippage(LocationFromAwaitingScreen, LocationFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateApproverIdFromAwaitingApprovalToViewTrippage(ApproveridFromAwaitingScreen, BusApproverIdFromViewtripScreen, Log,screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateOriginFromBusBookingToViewTrippage(BookingPgorigin, BusOriginFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateDestFromBusBookingToViewTrippage(BookingDest, BusDestFromViewtripScreen, Log, screenShots);
     NewDesign_Awaiting_ApprovalScreen.validateBusNameFromBusBookingToViewTrippage(BookingPgBusName, BusNameFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateBusTypeFromBusBookingToViewTrippage(BookingPgSeater, BusTypeFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateBusDepartTimeFromBusBookingToViewTrippage(Busdetails,BusDepartTimeFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateBusArrivalTimeFromBusBookingToViewTrippage(Busdetails,BusArrivalTimeFromViewtripScreen, Log, screenShots);
	NewDesign_Awaiting_ApprovalScreen.validateBusdurationFromBusBookingToViewTrippage(bookingPgDuration,BusDurationFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateBusPolicyFromBusBookingToViewTrippage(BookingPgPolicy,BusPolicyFromViewtripScreen, Log, screenShots);
	 NewDesign_Awaiting_ApprovalScreen.validateBusPriceFromBusBookingToViewTrippage(BookingPgPrice,BusPriceFromViewtripScreen, Log, screenShots);
	  
	  NewDesign_Awaiting_ApprovalScreen.clickApprovalDetailsButtonInViewtrip();
	 
	  String[] ApproverNames= NewDesign_Awaiting_ApprovalScreen.getApproverNameInAwaitingScreen(Log);
	 
	 
	//  NewDesign_Awaiting_ApprovalScreen.getApproverTimeInAwaitingScreen(Log);
	//div[contains(@class,'tg-approver-email')]
	 
	  //-------------------logout--------------------------------------------------
	  NewDesign_Awaiting_ApprovalScreen.clickOnLogout();
	  
	  //emulate to approver screen through admin------------------------------------
	 
	  NewDesignLogin.enterUserName(username1);
	  NewDesignLogin.enterPasswordName(pwd1);
	  
	  NewDesignLogin.clickButton();
		
		tripgain_HomePage.clickIfPresentCloseBtn();

	  
	  NewDesign_Emulate_Process.clcikOnAdmin();
	  NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	  
	 NewDesign_Emulate_Process.clickSearchValueThroughUser(searchvalue);
	  
	  NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	  Thread.sleep(2000); 
	  NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	 NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	 NewDesign_Emulate_Process.clcikOnAdmin(); Thread.sleep(1000);
	  NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ApproveridFromAwaitingScreen, Log, screenShots);
	  
	  //-----------------------get all the details from the approver screen-------------------------- 
	  Thread.sleep(2000); 
	  String[]LocationFromApproverScreen =NewDesign_Emulate_Process.getLocationDetailsFromApproverApprovalReqScreen();
	  String[] BusNameFromApproverScreen = NewDesign_Emulate_Process.getBusNameFromApproverApprovalReqScreen();
	  String[] BusTypeFromApproverScreen =NewDesign_Emulate_Process.getBustypeFromApproverApprovalReqScreen(); 
	  String[] BusDepartTimeFromApproverScreen =NewDesign_Emulate_Process.getBusDepartTimeFromApproverApprovalReqScreen();
	 String[] BusArrTimeFromApproverScreen =NewDesign_Emulate_Process.getBusArrTimeFromApproverApprovalReqScreen();
	 String[] BusDepartdateFromApproverScreen = NewDesign_Emulate_Process.getBusDepartDateFromApproverApprovalReqScreen();
	   String[] BusoriginFromApproverScreen =NewDesign_Emulate_Process.getBusoriginLocFromApproverApprovalReqScreen();
	 String[] BusDestFromApproverScreen = NewDesign_Emulate_Process.getBusDestLocFromApproverApprovalReqScreen();
	  String[] BusDurationFromApproverScreen = NewDesign_Emulate_Process.getBusDurationFromApproverApprovalReqScreen();
	 String[] BusPolicyFromApproverScreen = NewDesign_Emulate_Process.getBusPolicyFromApproverApprovalReqScreen();
	 String[] BusPriceFromApproverScreen = NewDesign_Emulate_Process.getPriceFromApproverApprovalReqScreen(); String[]
	 BusStatusFromApproverScreen = NewDesign_Emulate_Process.getStatusFromApproverApprovalReqScreen(Log);
	  String[] ApproverIdFromApproverScreen =NewDesign_Emulate_Process.getApproverIdFromApproverApprovalReqScreen();
	 
	  
	  NewDesign_Emulate_Process.validateLocationsDetailsFromViewTripToApproverScreenForBuses(LocationFromViewtripScreen, LocationFromApproverScreen, Log, screenShots);
	 NewDesign_Emulate_Process.validateApproverIdFromViewTripToApproverScreenForBuses(BusApproverIdFromViewtripScreen, ApproverIdFromApproverScreen, Log, screenShots);
	 NewDesign_Emulate_Process.validateBusOriginLocFromViewTripToApproverScreenForBuses(BusOriginFromViewtripScreen, BusoriginFromApproverScreen, Log, screenShots);
	 NewDesign_Emulate_Process.validateDestFromViewTripToApproverScreenForBuses(BusDestFromViewtripScreen, BusDestFromApproverScreen, Log, screenShots);
	  NewDesign_Emulate_Process.validateBusNameFromViewTripToApproverScreenForBuses(BusNameFromViewtripScreen, BusNameFromApproverScreen, Log, screenShots);
	 NewDesign_Emulate_Process.validateBusTypeFromViewTripToApproverScreenForBuses(BusTypeFromViewtripScreen, BusTypeFromApproverScreen, Log, screenShots);
	 NewDesign_Emulate_Process.validateBusDepartTimeFromViewTripToApproverScreenForBuses(BusDepartTimeFromViewtripScreen, BusDepartTimeFromApproverScreen, Log,screenShots);
	 NewDesign_Emulate_Process.validateBusArrivalTimeFromViewTripToApproverScreenForBuses(BusArrivalTimeFromViewtripScreen, BusArrTimeFromApproverScreen, Log,screenShots); 
	 NewDesign_Emulate_Process.validateBusdurationFromViewTripToApproverScreenForBuses(BusDurationFromViewtripScreen, BusDurationFromApproverScreen, Log,screenShots); 
	 NewDesign_Emulate_Process.validateBusPolicyFromViewTripToApproverScreenForBuses(BusPolicyFromViewtripScreen, BusPolicyFromApproverScreen, Log, screenShots);
	 NewDesign_Emulate_Process.validateBusPriceFromViewTripToApproverScreenForBuses(BusPriceFromViewtripScreen, BusPriceFromApproverScreen, Log, screenShots);
	 
	NewDesign_Emulate_Process.clcikOnProcessButton();
	 NewDesign_Emulate_Process.enterRemarks(remarks);
	 NewDesign_Emulate_Process.clickOnStatus(status);
	NewDesign_Emulate_Process.clickOnUpdateBtn();
	  NewDesign_Emulate_Process.clickOnErrorMsgAppearsIfAnyOneOOPolicyExixtsInTrip();
	 
	  
	  //----------------switch back to traveller -------------------------------------------
	  Thread.sleep(3000);
	 NewDesign_Emulate_Process.clickOnSwitchBack();
	  NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	  NewDesign_Emulate_Process.clcikOnAdmin();
	  NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	  NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
	  NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	  Thread.sleep(3000); 
	  NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	 NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	  
	  NewDesignTrips.clcikOnTrips();
	 NewDesignTrips.clickOnAwaitingApproval();
	 Thread.sleep(3000); 
	 NewDesignTrips.waitUntilDivDisplayed(driver);
	 
	 NewDesignTrips.selectStatusFromDropdownUnderAwaitingscreen("All");
	 
	 
	  NewDesignTrips.clickOnsearchTripsInAwaitingApprovalPg(ApproveridFromAwaitingScreen, Log, screenShots); 
	  Thread.sleep(2000); 
	  String[] travellerStatus = NewDesignTrips.getStatusInAwaitingApprovalForHotels(Log);
	 NewDesign_Awaiting_ApprovalScreen.clickOnViewTripInAwaitingScreen();
	 NewDesignTrips.getStatusInAwaitingApprovalForBusesInPendingStatys(Log);
	  
	  //---------------If Two levels of approver---------------------------
	  NewDesign_Emulate_Process.clickOnSwitchBack();
	  NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	  NewDesign_Emulate_Process.clcikOnAdmin();
	  NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	  
	  NewDesign_Emulate_Process.clickSearchValueThroughUser(approverSearchValue2);
	  
	  NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	  NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	  NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	  
	  NewDesign_Emulate_Process.clcikOnAdmin();
	  NewDesign_Emulate_Process.clickOnApprovalReqIn2ndApproverScreen();
	  NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ApproveridFromAwaitingScreen, Log, screenShots);
	  String[] ApproverStatus =NewDesignTrips.getStatusInAwaitingApprovalForBusesInPendingStatys(Log); 
	  NewDesignTrips.clickArrowToOpenTrip();
	  Thread.sleep(2000);
	  NewDesign_Emulate_Process.clcikOnProcessButton();
	  NewDesign_Emulate_Process.enterRemarks(remarks);
	 NewDesign_Emulate_Process.clickOnStatus(status2);
	 NewDesign_Emulate_Process.clickOnUpdateBtn();
	  
	  
	  NewDesign_Emulate_Process.clickOnSwitchBack();
	  NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	  NewDesign_Emulate_Process.clcikOnAdmin();
	  NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	  NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
	  NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	  Thread.sleep(2000); 
	  NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	 NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	  
	  
	  NewDesignTrips.clcikOnTrips();
	  NewDesignTrips.clickOnAwaitingApproval();
	  NewDesignTrips.waitUntilDivDisplayed(driver);
		 NewDesignTrips.selectStatusFromDropdownUnderAwaitingscreen("All");

	  NewDesignTrips.clickOnsearchTripsInAwaitingApprovalPg(ApproveridFromAwaitingScreen, Log, screenShots);
	  NewDesignTrips.getStatusInAwaitingApprovalForHotels(Log);
	  NewDesign_Awaiting_ApprovalScreen.clickOnViewTripInAwaitingScreen();
	  NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);
	  
	  System.out.println("Completed");
	  
	

		} catch (Exception e) {
			// Even on exception, check console
			//Log.checkConsoleErrorsWithFormat("After Exception");
			throw e;
		}
	} 

	private void showAllConsoleEntries() {
		try {
			LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
			int count = 0;

			for (LogEntry entry : logs) {
				count++;
				if (count > 20)
					break; // Show only last 20

				String level = entry.getLevel().toString();
				String message = entry.getMessage();

				// Clean the message
				String cleanMsg = message;
				if (cleanMsg.contains("[")) {
					int start = cleanMsg.indexOf("[");
					int end = cleanMsg.indexOf("]", start);
					if (end > start && end < 50) {
						cleanMsg = cleanMsg.substring(end + 2);
					}
				}

				// Shorten if too long
				if (cleanMsg.length() > 150) {
					cleanMsg = cleanMsg.substring(0, 147) + "...";
				}

				String levelIcon = "";
				switch (level) {
				case "SEVERE":
					levelIcon = "🔴";
					break;
				case "ERROR":
					levelIcon = "❌";
					break;
				case "WARNING":
					levelIcon = "⚠️";
					break;
				case "INFO":
					levelIcon = "ℹ️";
					break;
				default:
					levelIcon = "⚪";
				}

				Log.ReportEvent("info", levelIcon + " [" + level + "] " + cleanMsg);
			}

			if (count == 0) {
				Log.ReportEvent("warning", "⚠️ No console entries found");
			}

		} catch (Exception e) {
			Log.ReportEvent("warning", "Cannot read console: " + e.getMessage());
		}
	}

	// Function to Logout from Application
	// tripgainhomepage.logOutFromApplication(Log, screenShots);
	// driver.quit();

	@BeforeMethod(alwaysRun = true)
	@Parameters("browser")
	public void launchApplication(String browser, Method method, Object[] testDataObjects) {
		// Get test data passed from DataProvider
		@SuppressWarnings("unchecked")
		Map<String, String> testData = (Map<String, String>) testDataObjects[0];
		excelDataThread.set(testData); // Set it early!

		String url = (testData != null && testData.get("URL") != null) ? testData.get("URL") : "https://defaulturl.com";

		extantManager = new ExtantManager();
		extantManager.setUpExtentReporter(browser);
		className = this.getClass().getSimpleName();
		String testName = className + "_" + number;
		extantManager.createTest(testName);
		test = ExtantManager.getTest();
		extent = extantManager.getReport();
		test.log(Status.INFO, "Execution Started Successfully");

		// Use your existing launchBrowser method
		driver = launchBrowser(browser, url);

		Log = new Log(driver, test);
		screenShots = new ScreenShots(driver, test);

		// Add this simple JavaScript injection for API logging
	//	injectApiLogger();
	}

	// Add this method to inject JavaScript that forces API logging
	private void injectApiLogger() {
		try {
			String script = "// Simple API logger\n" + "console.log('[API-MONITOR] JavaScript logger injected');\n"
					+ "\n" + "// Store API calls\n" + "window._apiLogs = [];\n" + "\n" + "// Override fetch\n"
					+ "const originalFetch = window.fetch;\n" + "window.fetch = function(...args) {\n"
					+ "    const url = args[0];\n" + "    const method = args[1]?.method || 'GET';\n" + "    \n"
					+ "    // Log to console\n" + "    console.log('[API-CALL] ' + method + ' ' + url);\n" + "    \n"
					+ "    return originalFetch.apply(this, args)\n" + "        .then(response => {\n"
					+ "            // Clone to read response\n" + "            const clone = response.clone();\n"
					+ "            return clone.text().then(text => {\n" + "                // Store in array\n"
					+ "                window._apiLogs.push({\n" + "                    url: url,\n"
					+ "                    method: method,\n" + "                    status: response.status,\n"
					+ "                    response: text.substring(0, 500), // Limit size\n"
					+ "                    time: new Date().toLocaleTimeString()\n" + "                });\n"
					+ "                \n" + "                // Log status\n"
					+ "                if (response.status >= 200 && response.status < 300) {\n"
					+ "                    console.log('[API-SUCCESS] ' + response.status + ' ' + url);\n"
					+ "                } else {\n"
					+ "                    console.error('[API-ERROR] ' + response.status + ' ' + url);\n"
					+ "                    console.error('[API-ERROR-DATA] ' + text.substring(0, 200));\n"
					+ "                }\n" + "                \n" + "                return response;\n"
					+ "            });\n" + "        })\n" + "        .catch(error => {\n"
					+ "            console.error('[API-FAILED] ' + url + ' - ' + error.message);\n"
					+ "            throw error;\n" + "        });\n" + "};\n" + "\n"
					+ "console.log('[API-MONITOR] Ready to capture API calls');";

			((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
			test.log(Status.INFO, "✅ JavaScript API logger injected");

		} catch (Exception e) {
			test.log(Status.WARNING, "⚠️ Could not inject API logger: " + e.getMessage());
		}
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			extantManager.flushReport();
		}
	}
}