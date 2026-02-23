package com.tripgain.testscripts.Expenses;

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
import com.tripgain.collectionofpages.ExpensesModule;
import com.tripgain.collectionofpages.NewDesign_AwaitingApprovalScreen;
import com.tripgain.collectionofpages.NewDesign_Buses_BookingPage;
import com.tripgain.collectionofpages.NewDesign_EmulateProcess;
import com.tripgain.collectionofpages.NewDesign_Login;
import com.tripgain.collectionofpages.NewDesign_Trips;
import com.tripgain.collectionofpages.SiteChecker;
import com.tripgain.collectionofpages.Tripgain_Login;
import com.tripgain.collectionofpages.Tripgain_RoundTripResultsScreen;
import com.tripgain.collectionofpages.Tripgain_homepage;
import com.tripgain.collectionofpages.Tripgain_resultspage;
import com.tripgain.common.DataProviderUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.GenerateDates;
import com.tripgain.common.Getdata;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.testscripts.BaseClass;

public class TCE_8_ExpenseFlowThroughPDTByUploadingReceipt extends BaseClass {
	WebDriver driver;
	ExtentReports extent;
	ExtentTest test;
	String className = "";
	Log Log; // Declare Log object
	ScreenShots screenShots; // Declare Log object
	ExtantManager extantManager;

	private WebDriverWait wait;

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

			String username1 = excelData.get("UserName1");
			String pwd1 = excelData.get("Password1");
			number++;

			String notes = excelData.get("Notes");

			String tripName = excelData.get("TripName");
			String origin = excelData.get("Origin");
			System.out.println(origin);
			String destination = excelData.get("Destination");
			System.out.println(destination);

			String[] dates = GenerateDates.GenerateDatesToSelectFlights();
			String fromDate = dates[0];
			String fromMonthYear = dates[2];
			String returnDate = dates[1];
			String returnMonthYear = dates[3];

			String searchby = excelData.get("SearchBy");
			String searchvalue = excelData.get("SearchValue");
			String remarks = excelData.get("Remarks");
			String status = excelData.get("Status");
			String travellerSearchValue2 = excelData.get("TravellerSearchValue2");
			String travellerSearchValue = excelData.get("TravellerSearchValue");

			// Login to TripGain Application
			Tripgain_Login tripgainLogin = new Tripgain_Login(driver);
			SiteChecker Site_Checker = new SiteChecker(driver);

			Site_Checker.waitForSiteToBeUp(driver, "https://v3.tripgain.com/flights", 20, 120);

			Thread.sleep(3000);
			tripgainLogin.enterUserName(username);
			tripgainLogin.enterPasswordName(pwd);
			tripgainLogin.clickButton();
			Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
			Thread.sleep(2000);

			// Functions to Search on Home Page
			Tripgain_RoundTripResultsScreen trs = new Tripgain_RoundTripResultsScreen(driver);
			NewDesign_Buses_BookingPage NewDesignBusesBookingPage = new NewDesign_Buses_BookingPage(driver);
			NewDesign_AwaitingApprovalScreen NewDesign_Awaiting_ApprovalScreen = new NewDesign_AwaitingApprovalScreen(
					driver);
			NewDesign_Login NewDesignLogin = new NewDesign_Login(driver);
			NewDesign_EmulateProcess NewDesign_Emulate_Process = new NewDesign_EmulateProcess(driver);

			trs.printVersion(Log);
			Thread.sleep(2000);

			// ---------------------------------------------------------------------------------------------

			NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);

			NewDesignTrips.clcikOnTrips();
			NewDesignTrips.createTrip();
			NewDesignTrips.clcikOnPastDatedBooking();

			NewDesignTrips.enterNameThisTrip(tripName, Log);
			String origindetails = NewDesignTrips.enterfrom(origin);
			String destdetails = NewDesignTrips.enterTo(destination);

			String journeydatedetails = NewDesignTrips.selectJourneyDate(fromDate, fromMonthYear);

			String returndatedetails = NewDesignTrips.selectReturnDate(returnDate, returnMonthYear);

			// List<String> servicesdetails = Trip_Planner.selectServices("Flight","Hotel");
			List<String> servicesdetails = NewDesignTrips.selectServices("Flight", "Hotel");

			NewDesignTrips.clickCreateTripButton();

			List<String> servicesTextFromPopup = NewDesignTrips.getSelectedServicesTextFromPopup();

			NewDesignTrips.validateSelectedServicesInSelectedAndPopup(servicesdetails, servicesTextFromPopup, Log,
					screenShots);
			String[] tripIdFromPop = NewDesignTrips.getTripIdFromPopup(Log);
			NewDesignTrips.clickOnContinueToAddServicesBtn();

			String[] TripIdFromNextPage = NewDesignTrips.getTripIdFromTripDetailsPage(Log);
			String[] TripDatesFromSearchPg = NewDesignTrips.getDatesFromTripDetailsPage();
			List<String> selectedServicesFromDetailsPg = NewDesignTrips.getSelectedServicesTextFromDetailsPage();
			NewDesignTrips.validateSelectedAndDetailsPageServices(servicesdetails, selectedServicesFromDetailsPg, Log,
					screenShots);
			NewDesignTrips.clickOnServiceByText("Flight", Log);
			NewDesignTrips.clickOnAddButton();
			NewDesignTrips.clickOnServiceByText("Hotel", Log);
			String locationToSearch = NewDesignTrips.enterLocationForHotelsOndetailsPg("Mumbai");
			NewDesignTrips.clickOnAddButton();
			NewDesignTrips.clickOnApprovalDetailsForCreateTrip();
			NewDesignBusesBookingPage.clickOnSubmitTripButton(Log);
			Thread.sleep(2000);
			NewDesign_Awaiting_ApprovalScreen.clickOnLogout();

			// emulate to approver screen through admin------------------------------------

			NewDesignLogin.enterUserName(username1);
			NewDesignLogin.enterPasswordName(pwd1);
			NewDesignLogin.clickButton();
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(searchvalue);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			Thread.sleep(1000);
			NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(TripIdFromNextPage, Log, screenShots);
			NewDesign_Emulate_Process.clcikOnProcessButton();
			String[] approverRemarks = NewDesign_Emulate_Process.enterRemarks(remarks);
			NewDesign_Emulate_Process.clickOnStatus("Approve");
			NewDesign_Emulate_Process.clickOnUpdateBtn();

			// ---------------If Two levels of approver---------------------------
			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue2);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnApprovalReqIn2ndApproverScreen();
			NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(TripIdFromNextPage, Log, screenShots);
			NewDesign_Emulate_Process.clcikOnProcessButton();
			String[] secondApproverRemarks = NewDesign_Emulate_Process.enterRemarks(remarks);
			NewDesign_Emulate_Process.clickOnStatus("Approve");
			NewDesign_Emulate_Process.clickOnUpdateBtn();
			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);

			// -----------------------------------------------------------------------------------------------------------------

			ExpensesModule Expenses_Module = new ExpensesModule(driver);
			Expenses_Module.clickExpenseButton();
			Thread.sleep(1000);
			Expenses_Module.SearchTripThroughSearchReportFieldUnderExpense(TripIdFromNextPage);
			Expenses_Module.openExpenseReport();

			Expenses_Module.getStatusFromExpenseReportDetailsPg(Log);

			Expenses_Module.clickAddExpenseButton();
			Thread.sleep(2000);
			Expenses_Module.clickOnSelectCategoryValuesThroughExpenseSearchTripIdFlow(Log, screenShots);
			Thread.sleep(2000);
			Expenses_Module.selectDateThroughExpenseSearchTripIdFlow("6");
			Thread.sleep(2000);
			Expenses_Module.clickOnCurrencyDropDown();
			// Expenses_Module.selectCurrencyDropDownValues(driver, "ANG", Log);
			Thread.sleep(2000);
			Expenses_Module.enterNotes(notes, Log, screenShots);
			Thread.sleep(2000);
			Expenses_Module.merchantName("TestTraveller", Log, screenShots);
			Thread.sleep(2000);
			Expenses_Module.selectPaymentMode("Corporate Card", Log, screenShots);
			Thread.sleep(2000);
			Expenses_Module.selectorporateCardOptions("Chase (98889879)", Log, screenShots);

			Expenses_Module.uploadReceiptUnderExpense(
					"‪D:\\OneDrive - tripgain.com\\Pictures\\Screenshots\\foodbillReceiptDummy.png");
			// Expenses_Module.EnterAmounthroughSearchReportFieldUnderExpense("1000");
			// Expenses_Module.clickOnSaveExpenseButt();
			Expenses_Module.clickOnSaveAndAnotherButt();

			Expenses_Module.clickOnMileageButtThroughExpenseSearchTripIdFlow();
			Expenses_Module.clickOnSelectCategoryValuesunderMileageThroughExpenseSearchTripIdFlow(Log, screenShots);
			Expenses_Module.selectDateThroughExpenseSearchTripIdFlow("6");
			Expenses_Module.merchantName("TestMileage", Log, screenShots);
			Expenses_Module.selectVehicleType(Log, screenShots, "bus (bus)");

			Expenses_Module.enterfrom("Rajajinagar");
			Thread.sleep(2000);
			Expenses_Module.enterTo("RR Nagar");
			Thread.sleep(2000);
			Expenses_Module.addRemarks("Test", Log, screenShots);
			Expenses_Module.uploadReceiptUnderMileage("‪D:\\OneDrive - tripgain.com\\Pictures\\Screenshots\\foodbillReceiptDummy.png");

			Expenses_Module.clickOnSaveExpenseButt();

			String[] ReportId = Expenses_Module.getReportIdFromExpenseReportDetailsPg(Log);
			Expenses_Module.clickOnSubmitReportBtnThroughExpenseSearchTripIdFlow();
			Expenses_Module.handleExpenseReportSubmission();

			Expenses_Module.SearchTripThroughSearchReportFieldUnderExpense(TripIdFromNextPage);
			Expenses_Module.openExpenseReport();
			Thread.sleep(2000);
			Expenses_Module.clcikOnLogBtn();
			String Firstemail = Expenses_Module.getApproverEmailByIndexPresentUnderLOgs(1);
			String secondemail = Expenses_Module.getApproverEmailByIndexPresentUnderLOgs(2);

			NewDesign_Awaiting_ApprovalScreen.clickOnLogout();

			// emulate to approver screen through admin------------------------------------

			NewDesignLogin.enterUserName(username1);
			NewDesignLogin.enterPasswordName(pwd1);
			NewDesignLogin.clickButton();
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(Firstemail);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			Thread.sleep(1000);
			NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ReportId, Log, screenShots);
			NewDesign_Emulate_Process.clcikOnProcessButton();
			NewDesign_Emulate_Process.enterRemarks(remarks);
			NewDesign_Emulate_Process.clickOnStatus(status);
			NewDesign_Emulate_Process.clickOnUpdateBtn();

			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(secondemail);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnApprovalReqIn2ndApproverScreen();
			NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ReportId, Log, screenShots);
			NewDesign_Emulate_Process.clcikOnProcessButton();
			NewDesign_Emulate_Process.enterRemarks(remarks);
			NewDesign_Emulate_Process.clickOnStatus("Approve");
			NewDesign_Emulate_Process.clickOnUpdateBtn();

			// roll back to traveller to check for status
			Thread.sleep(3000);
			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			Expenses_Module.clickExpenseButton();
			Thread.sleep(1000);
			Expenses_Module.SearchTripThroughSearchReportFieldUnderExpense(ReportId);
			Expenses_Module.openExpenseReport();
			Expenses_Module.getStatusFromExpenseReportDetailsPg(Log);

			
			//emulate to finance manager
			NewDesign_Emulate_Process.clickOnSwitchBack();
	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	        NewDesign_Emulate_Process.clcikOnAdmin();
	        NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	        NewDesign_Emulate_Process.clickSearchValueThroughUser("ramures@tripgain.com");
	        NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	        NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	        NewDesign_Emulate_Process.clcikOnAdmin();
	        Expenses_Module.clcikOnExpenseVerificationPendingBtn();
	        
	        Expenses_Module.SearchReportUnderExpenseVerificationPending(ReportId);
	        Expenses_Module.clcikOnProcessBtnUnderExpenseVerificationPending();
	        Expenses_Module.clcikOnStatusDropdownUnderFinanceMngr("Inprocess", Log, screenShots);
	        Expenses_Module.enterVerificationRemarksUnderExpenseVerificationPending("Inprocess");
	        Expenses_Module.clcikOnSubmitBtnUnderExpenseVerificationPending();
	        
	       
	        
	     // roll back to traveller to check for status
	     			Thread.sleep(3000);
	     			NewDesign_Emulate_Process.clickOnSwitchBack();
	     			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	     			NewDesign_Emulate_Process.clcikOnAdmin();
	     			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	     			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
	     			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	     			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	     			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	     			Expenses_Module.clickExpenseButton();
	     			Thread.sleep(1000);
	     			Expenses_Module.SearchTripThroughSearchReportFieldUnderExpense(ReportId);
	     			Expenses_Module.openExpenseReport();
	     			Expenses_Module.getStatusFromExpenseReportDetailsPg(Log);
	     			
	     			//emulate to finance manager
	    			NewDesign_Emulate_Process.clickOnSwitchBack();
	    	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	    	        NewDesign_Emulate_Process.clcikOnAdmin();
	    	        NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
	    	        NewDesign_Emulate_Process.clickSearchValueThroughUser("ramures@tripgain.com");
	    	        NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	    	        NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	    	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	    	        NewDesign_Emulate_Process.clcikOnAdmin();
			        NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ReportId, Log, screenShots);
			        NewDesign_Emulate_Process.clcikOnProcessButton();
			         NewDesign_Emulate_Process.enterRemarks(remarks);
			        NewDesign_Emulate_Process.clickOnStatus(status);
			        NewDesign_Emulate_Process.clickOnUpdateBtn();

	    	        
	     			
	        
			// Emulate to finance admin
			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser("testfinanceadmin98@tripgain.com");
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
		
			Expenses_Module.SearchReportUnderExpenseVerificationPending(ReportId);

			Expenses_Module.clcikOnReimburseBtnUnderExpenseVerificationPending();
			Expenses_Module.selectBankDuringReimbursed("45678903456789 - testing", Log, screenShots);
			Expenses_Module.enterBankReferenceText("QATesting");
			Expenses_Module.enterReimbursedRemarks("Reimbursed Done through Finance Admin");
			Expenses_Module.clcikOnSubmitBtnUnderExpenseVerificationPending();

		} catch (Exception e) {
			String errorMessage = "Exception occurred: " + e.toString();
			Log.ReportEvent("FAIL", errorMessage);
			screenShots.takeScreenShot();
			e.printStackTrace(); 
			Assert.fail(errorMessage);
		}

	}

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