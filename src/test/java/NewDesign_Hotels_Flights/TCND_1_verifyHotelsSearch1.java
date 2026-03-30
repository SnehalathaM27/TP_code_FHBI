package NewDesign_Hotels_Flights;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Duration;
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
import com.tripgain.collectionofpages.NewDesignHotelsSearchPage;
import com.tripgain.collectionofpages.NewDesign_AwaitingApprovalScreen;
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
import com.tripgain.common.DataProviderUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.GenerateDates;
import com.tripgain.common.Getdata;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.testscripts.BaseClass;
import java.util.Optional;
import org.openqa.selenium.chrome.ChromeDriver;


public class TCND_1_verifyHotelsSearch1 extends BaseClass {
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
			int hotelindex = Integer.parseInt(excelData.get("HotelIndex"));

			String searchby = excelData.get("SearchBy");
			String searchvalue = excelData.get("SearchValue");
			String remarks = excelData.get("Remarks");
			String status = excelData.get("Status");
			String travellerSearchValue = excelData.get("TravellerSearchValue");
			String travellerSearchValue2 = excelData.get("TravellerSearchValue2");
			String status2 = excelData.get("Status2");

			String[] dates = GenerateDates.GenerateDatesToSelectFlights();
			String fromDate = dates[0];
			String fromMonthYear = dates[2];
			String returnDate = dates[1];
			String returnMonthYear = dates[3];

			
	         NewDesign_Login NewDesignLogin= new NewDesign_Login(driver);
			Tripgain_HomePage_Flights tripgain_HomePage= new Tripgain_HomePage_Flights(driver);

			
			NewDesignLogin.enterUserName(username);
			NewDesignLogin.enterPasswordName(pwd);
			NewDesignLogin.clickButton();
			Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
			tripgain_HomePage.clickIfPresentCloseBtn();
			

	  NewDesignLogin.clickOnTravel();
	  

			NewDesign_Hotels_ResultsPage NewDesignHotelsResultsPage = new NewDesign_Hotels_ResultsPage(driver);
			NewDesign_Hotels_DescPage NewDesignHotels_DescPage = new NewDesign_Hotels_DescPage(driver);
			NewDesignHotelsSearchPage NewDesign_HotelsSearchPage = new NewDesignHotelsSearchPage(driver);
			NewDesign_Hotels_BookingPage NewDesign_HotelsBookingPage = new NewDesign_Hotels_BookingPage(driver);
			NewDesign_AwaitingApprovalScreen NewDesign_Awaiting_ApprovalScreen = new NewDesign_AwaitingApprovalScreen(
					driver);
			NewDesign_EmulateProcess NewDesign_Emulate_Process = new NewDesign_EmulateProcess(driver);
			NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);

			NewDesign_HotelsSearchPage.clickOnHotels();
			Thread.sleep(3000);
			NewDesign_HotelsSearchPage.enterDestinationForHotels(origin, Log);
			Thread.sleep(3000);

			NewDesign_HotelsSearchPage.selectDate(fromDate, fromMonthYear, Log);
			NewDesign_HotelsSearchPage.selectReturnDate(returnDate, returnMonthYear, Log);
			
		 NewDesign_HotelsSearchPage.configureHotelBookingRooms("3", "2,3,2", "2,1,1", "5;7,8;9", driver);

			NewDesign_HotelsSearchPage.clickOnSearchHotelBut();
			Thread.sleep(8000);

			String[] checkindateResultPage = NewDesignHotelsResultsPage.getCheckInDateTextFromResultPage();
			String[] checkoutdateResultPage = NewDesignHotelsResultsPage.getCheckOutDateTextFromResultPage();
			String[] ResultPageRoomsAndGuests = NewDesignHotelsResultsPage.getRoomAndGuestTextFromResultPage();
			// String userRatingFromResultscreen =
			// NewDesignHotelsResultsPage.clickUserRating("Good: 3+", Log, screenShots);
			// NewDesignHotelsResultsPage.ApplyFilterBtn();
			// String perNightPrice = NewDesignHotelsResultsPage.clickPerNightPrice(1, Log);
//		String RatingOptions = NewDesignHotelsResultsPage.clickOnRatingOption(2, Log);
			// String Amenities = NewDesignHotelsResultsPage.clickAmenitiesOptions(2, Log);

			NewDesignHotelsResultsPage.validatePolicytext(Log, screenShots);
			// NewDesignHotelsResultsPage.clickOnSortOption("Name: Ascending", Log);
			// NewDesignHotelsResultsPage.validateSortNameAscending(Log);
//      	NewDesignHotelsResultsPage.clickOnSortOption("Name: Descending", Log);
//      	NewDesignHotelsResultsPage.validateSortNameDescending(Log);
//		NewDesignHotelsResultsPage.clcikOnPriceSlider();
//		NewDesignHotelsResultsPage.moveLeftThumbToRightByPercentage(40);
//		NewDesignHotelsResultsPage.moveRightThumbToLeftByPercentage(30);

			NewDesignHotelsResultsPage.clickOnSortOption("Price: Low to High", Log);
			NewDesignHotelsResultsPage.validatePricesLowToHigh(Log);
			Thread.sleep(2000);

			NewDesignHotelsResultsPage.clickOnSortOption("Price: High to Low", Log);
			NewDesignHotelsResultsPage.validatePricesHighToLow(Log);
			Thread.sleep(2000);

//		NewDesignHotelsResultsPage.clickOnSortOption("Star Rating: Ascending", Log);
//		NewDesignHotelsResultsPage.clickOnSortOption("Star Rating: Descending", Log);
//		NewDesignHotelsResultsPage.clickOnSortOption("Distance: Ascending", Log);
//		NewDesignHotelsResultsPage.validateDistanceAscending(Log);
//		Thread.sleep(2000);
//
//		NewDesignHotelsResultsPage.clickOnSortOption("Distance: Descending", Log);
//		NewDesignHotelsResultsPage.validateDistanceDescendingSimple(Log);
//		Thread.sleep(2000);

			// NewDesignHotelsResultsPage.selectCurrencyFromDropdown("TND", Log);

			Thread.sleep(2000);
			// get all the hotel details from the selected hotel card
			String[] hotelDetails = NewDesignHotelsResultsPage.selectHotelAndGetDetails(hotelindex, Log);

			// get all the details from desc page
			NewDesignHotels_DescPage.waitUntilDescriptionCardVisibleOrFail(driver, 30, Log);
			String[] hotelAddressFromDesc = NewDesignHotels_DescPage.getAddressFromDescPg();
			String[] hotelAmenitiesFromDesc = NewDesignHotels_DescPage.getAmenitiesFromDescPg();
			String[] checkInDateFromDesc = NewDesignHotels_DescPage.getCheckInAfterFromDescPg();
			String[] checkOutFromDesc = NewDesignHotels_DescPage.getCheckOutTimeFromDescPg();
			String[] hotelNameFromDesc = NewDesignHotels_DescPage.getHotelNameFromDescPg();
			// NewDesignHotels_DescPage.getHotelRatingFromDescPg();
			// String[] perNightPriceFromDesc =
			// NewDesignHotels_DescPage.getPerNightPriceFromDescPg();
			String[] hotelPriceFromDesc = NewDesignHotels_DescPage.getPriceFromDescPg();
			String[] PolicyFromDesc = NewDesignHotels_DescPage.getPolicyFromDescPg();
			// String[]
			// OtherCurrencyInDesc=NewDesignHotels_DescPage.getOtherCountryPriceFromDescPg();
			// NewDesignHotels_DescPage.getStarRatingCount(null);

			// validations b/w desc to result pages
			NewDesignHotels_DescPage.validateHotelNameFromDescToResultPage(hotelNameFromDesc, hotelDetails, Log,
					screenShots);
			NewDesignHotels_DescPage.validateHotelAddressFromDescToResultPage(hotelAddressFromDesc, hotelDetails, Log,
					screenShots);
			// NewDesignHotels_DescPage.validateHotelPriceFromDescToResultPage(hotelPriceFromDesc,
			// hotelDetails, Log, screenShots);
			NewDesignHotels_DescPage.validateHotelPolicyFromDescToResultPage(PolicyFromDesc, hotelDetails, Log,
					screenShots);
			// NewDesignHotels_DescPage.validatePerNightPriceFromDescToResultPage(perNightPriceFromDesc,
			// hotelDetails, Log, screenShots);
			NewDesignHotels_DescPage.validateAmenitiesFromDescToResultPage(hotelAmenitiesFromDesc, hotelDetails, Log,
					screenShots);
			// NewDesignHotelsResultsPage.validateOtherCurrencyPriceFromDescToResultPage(OtherCurrencyInDesc,
			// hotelDetails, Log, screenShots);

			String[] selectRooms = NewDesignHotels_DescPage.selectRoomsFromDescPg();

			Thread.sleep(3000);
			NewDesignHotels_DescPage.waitUntilHotelBookingPageDisplayed();
			String[] BookingPgcheckIn = NewDesign_HotelsBookingPage.getCheckInAfterFromBookingPg();
			String[] BookingPgCheckOut = NewDesign_HotelsBookingPage.getCheckOutTimeFromBookingPg();
			String[] BookingPgAddress = NewDesign_HotelsBookingPage.getHotelAddressFromBookingPg();
			String[] BookingPgHotelNm = NewDesign_HotelsBookingPage.getHotelNameFromBookingPg();
			String[] BookingPgLabel = NewDesign_HotelsBookingPage.getLabelTextFromBookingPg();
			String[] BookingPgMeals = NewDesign_HotelsBookingPage.getMealsTextFromBookingPg();
			String[] BookingPgPolicy = NewDesign_HotelsBookingPage.getPolicyTextFromBookingPg();
			String[] BookingPgRefundable = NewDesign_HotelsBookingPage.getRefundableTextFromBookingPg();
			String[] BookingPgSelectedRoomText = NewDesign_HotelsBookingPage.getSelectedRoomTextFromBookingPg();
			String[] BookingPgTotalfareAmount = NewDesign_HotelsBookingPage.getTotalFareAmountFromBookingPg();
			String[] BookingCheckIndate = NewDesign_HotelsBookingPage.getCheckInDateFromBookingPg();
			String[] BookingCheckOutdate = NewDesign_HotelsBookingPage.getCheckOutDateFromBookingPg();

			NewDesign_HotelsBookingPage.validateCheckInAfterFromDescToBookingPage(checkInDateFromDesc, BookingPgcheckIn,Log, screenShots);
			NewDesign_HotelsBookingPage.validateCheckOutTimeFromDescToBookingPage(checkOutFromDesc, BookingPgCheckOut,Log, screenShots);
			NewDesign_HotelsBookingPage.validateHotelAddressFromDescToBookingPage(hotelAddressFromDesc,BookingPgAddress, Log, screenShots);
			NewDesign_HotelsBookingPage.validateHotelNameFromDescToBookingPage(hotelNameFromDesc, BookingPgHotelNm, Log,screenShots);
			NewDesign_HotelsBookingPage.validateLabelFromDescToBookingPage(selectRooms, BookingPgLabel, Log,screenShots);
			NewDesign_HotelsBookingPage.validateMealsTextFromDescToBookingPage(selectRooms, BookingPgMeals, Log,screenShots);
			NewDesign_HotelsBookingPage.validatePolicyTextFromDescToBookingPage(PolicyFromDesc, BookingPgPolicy, Log,screenShots);
			NewDesign_HotelsBookingPage.validateRefundableTextFromDescToBookingPage(selectRooms, BookingPgRefundable,Log, screenShots);
			NewDesign_HotelsBookingPage.validateSelectedRoomTextFromDescToBookingPage(selectRooms,BookingPgSelectedRoomText, Log, screenShots);
			NewDesign_HotelsBookingPage.validateCheckInDateBetweenResultAndBookingPage(checkindateResultPage,BookingCheckIndate, Log, screenShots);
			NewDesign_HotelsBookingPage.validateCheckOutDateBetweenResultAndBookingPage(checkoutdateResultPage,BookingCheckOutdate, Log, screenShots);
			NewDesign_HotelsBookingPage.validatePriceFromDescWithBookingPage(selectRooms, BookingPgTotalfareAmount, Log,screenShots);
			Thread.sleep(3000);
			// NewDesign_HotelsBookingPage.addTravellerDetails();
			NewDesign_HotelsBookingPage.clickSendForApproval(Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.waitUntilAwaitingPageLoads(Log, screenShots);

			NewDesign_Awaiting_ApprovalScreen.getHotelNameFromAwaitingPg();
			NewDesign_Awaiting_ApprovalScreen.getHotelDateFromAwaitingPg();
			String[] ApproveridFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getApproverIdFromAwaitingPg(Log,"Hotel");
			Thread.sleep(1000);
			NewDesign_Awaiting_ApprovalScreen.clickOnViewTripInAwaitingScreen();
			Thread.sleep(2000);

			String[] hotelNameFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getHotelNameFromViewTripInAwaitingScreen();
			String[] hoteladdressFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getHotelAddressFromViewTripInAwaitingScreen();
			String[] hotelCheckInFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getCheckInAfterFromViewTripInAwaitingScreen();
			String[] hotelCheckOutTimeFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getCheckOutTimeFromViewTripInAwaitingScreen();
			String[] hotelCheckInDateFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getCheckInDateFromViewTripInAwaitingScreen();
			String[] hotelCheckOutDateFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getCheckOutDateFromViewTripInAwaitingScreen();
			String[] selectedRoomFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getSelectedRoomTextFromViewTripInAwaitingScreen();
			String[] labelFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getLabelTextFromViewTripInAwaitingScreen();
			String[] MealsFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getMealsTextFromViewTripInAwaitingScreen();
			String[] PolicyFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getPolicyTextFromFromViewTripInAwaitingScreen();
			String[] RefundableFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getRefundableTextFromViewTripInAwaitingScreen();
			String[] priceFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getpriceTextFromViewTripInAwaitingScreen();
			NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);
			String[] ApproverIdFromAwaitingScreen = NewDesign_Awaiting_ApprovalScreen.getApproverIdTextFromViewTripInAwaitingScreen();

			NewDesign_Awaiting_ApprovalScreen.validateCheckInAfterFromBookingToAwaitingPageForHotels(BookingPgcheckIn,hotelCheckInFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateCheckOutTimeFromBookingPageToAwaitingPage(BookingPgCheckOut,hotelCheckOutTimeFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateHotelAddressFromBookingToAwaitingPage(BookingPgAddress,hoteladdressFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateHotelAddressFromBookingToAwaitingPage(BookingPgHotelNm,hotelNameFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateLabelFromBookingToAwaitingPage(BookingPgLabel,labelFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateMealsTextFromBookingToAwaitingPage(BookingPgMeals,MealsFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validatePolicyTextFromBookingToAwaitingPage(BookingPgPolicy,PolicyFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateRefundableTextFromBookingToAwaitingPage(BookingPgRefundable,RefundableFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateSelectedRoomTextFromBookingToAwaitingPage(BookingPgSelectedRoomText, selectedRoomFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateCheckInDateBetweenBookingToAwaitingPage(BookingCheckIndate,hotelCheckInDateFromAwaitingScreen, Log, screenShots);
			NewDesign_Awaiting_ApprovalScreen.validateCheckOutDateBetweenBookingAndAwaitingPage(BookingCheckOutdate,hotelCheckOutDateFromAwaitingScreen, Log, screenShots);
			// NewDesign_Awaiting_ApprovalScreen.validatePriceFromDescWithBookingPage(BookingPgTotalfareAmount,priceFromAwaitingScreen,
			// Log, screenShots);

			NewDesign_Awaiting_ApprovalScreen.clickApprovalDetailsButtonInViewtrip();
			String[] ApproverNames = NewDesign_Awaiting_ApprovalScreen.getApproverNameInAwaitingScreen(Log);

			// -------------------logout---------------------------------------------------------
			NewDesign_Awaiting_ApprovalScreen.clickOnLogout();

			// emulate to approver screen through admin------------------------------------

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
			NewDesign_Emulate_Process.clcikOnAdmin();
			Thread.sleep(1000);
			NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ApproverIdFromAwaitingScreen, Log,screenShots);

			// -----------------------get all the details from the approver screen--------------------------
			Thread.sleep(2000);

			String[] hotelNameFromApproverScreen = NewDesign_Emulate_Process.getHotelNamefromApproverScreen();
			String[] hoteladdressFromApproverScreen = NewDesign_Emulate_Process.getHotelAddressFromApproverScreen();
			String[] hotelCheckInFromApproverScreen = NewDesign_Emulate_Process.getCheckInAfterFromApprovergScreen();
			String[] hotelCheckOutTimeFromApproverScreen = NewDesign_Emulate_Process.getCheckOutTimeFromApproverScreen();
			String[] hotelCheckInDateFromApproverScreen = NewDesign_Emulate_Process.getCheckInDateFromApproverScreen();
			String[] hotelCheckOutDateFromApproverScreen = NewDesign_Emulate_Process.getCheckOutDateFromApproverScreen();
			String[] selectedRoomFromApproverScreen = NewDesign_Emulate_Process.getSelectedRoomTextFromApproverScreen();
			String[] labelFromApproverScreen = NewDesign_Emulate_Process.getLabelTextFromApproverScreen();
			String[] MealsApproverScreen = NewDesign_Emulate_Process.getMealsTextFromApproverScreen();
			String[] PolicyFromApproverScreen = NewDesign_Emulate_Process.getPolicyTextFromFromApproverScreen();
			String[] RefundableFromApproverScreen = NewDesign_Emulate_Process.getRefundableTextFromApproverScreen();
			String[] priceFromApproverScreen = NewDesign_Emulate_Process.getpriceTextFromApproverScreen();
			NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);
			String[] ApproverIdFromApproverScreen = NewDesign_Emulate_Process.getApproverIdTextFromApproverScreen();

			NewDesign_Emulate_Process.validateCheckInAfterFromAwaitingToApproverScreenForHotels(hotelCheckInFromAwaitingScreen, hotelCheckInFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateCheckOutTimeFromAwaitingToApproverScreenForHotels(hotelCheckOutTimeFromAwaitingScreen, hotelCheckOutTimeFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateHotelAddressFromAwaitingToApproverScreenForHotels(hoteladdressFromAwaitingScreen, hoteladdressFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateHotelNameFromFromAwaitingToApproverScreenForHotels(hotelNameFromAwaitingScreen, hotelNameFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateLabelFromAwaitingToApproverScreenForHotels(labelFromAwaitingScreen,labelFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateMealsTextFromAwaitingToApproverScreenForHotels(MealsFromAwaitingScreen,MealsApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validatePolicyTextFromAwaitingToApproverScreenForHotels(PolicyFromAwaitingScreen,PolicyFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateRefundableTextFromAwaitingToApproverScreenForHotels(RefundableFromAwaitingScreen, RefundableFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateSelectedRoomTextFromAwaitingToApproverScreenForHotels(selectedRoomFromAwaitingScreen, selectedRoomFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateCheckInDateFromAwaitingToApproverScreenForHotels(hotelCheckInDateFromAwaitingScreen, hotelCheckInDateFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateCheckOutDateFromAwaitingToApproverScreenForHotels(hotelCheckOutDateFromAwaitingScreen, hotelCheckOutDateFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validatePriceFromAwaitingToApproverScreenForHotels(priceFromAwaitingScreen,priceFromApproverScreen, Log, screenShots);
			NewDesign_Emulate_Process.validateApproverIdFromViewTripToApproverScreenForHotels(ApproverIdFromAwaitingScreen, ApproverIdFromApproverScreen, Log, screenShots);

			NewDesign_Emulate_Process.clcikOnProcessButton();
			NewDesign_Emulate_Process.enterRemarks(remarks);
			NewDesign_Emulate_Process.clickOnStatus(status);
			NewDesign_Emulate_Process.clickOnUpdateBtn();
			NewDesign_Emulate_Process.clickOnErrorMsgAppearsIfAnyOneOOPolicyExixtsInTrip();

			// ----------------switch back to traveller-------------------------------------------
			Thread.sleep(3000);
			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);

			NewDesignTrips.clcikOnTrips();
			NewDesignTrips.clickOnAwaitingApproval();
			Thread.sleep(2000);
			NewDesignTrips.waitUntilDivDisplayed(driver);

			 NewDesignTrips.selectStatusFromDropdownUnderAwaitingscreen("All");

			NewDesignTrips.clickOnsearchTripsInAwaitingApprovalPg(ApproveridFromAwaitingScreen, Log, screenShots);
			Thread.sleep(2000);
			String[] travellerStatus = NewDesignTrips.getStatusInAwaitingApprovalForHotels(Log);
			NewDesign_Awaiting_ApprovalScreen.clickOnViewTripInAwaitingScreen();
			NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);

			// ---------------If Two levels of approver---------------------------
			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);

			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue2);

			// NewDesign_Emulate_Process.enterSearchValueByIndex(ApproverNames, 1, Log);

			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);

			NewDesign_Emulate_Process.clcikOnAdmin();
			Thread.sleep(1000);
			NewDesign_Emulate_Process.clickOnApprovalReqIn2ndApproverScreen();
			Thread.sleep(1000);
			NewDesign_Emulate_Process.searchApproverIdInApprovalReqScreen(ApproveridFromAwaitingScreen, Log,
					screenShots);
			Thread.sleep(1000);

			String[] ApproverStatus = NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);

			// NewDesignTrips.clickArrowToOpenTrip();
			NewDesign_Emulate_Process.clcikOnProcessButton();
			NewDesign_Emulate_Process.enterRemarks(remarks);
			NewDesign_Emulate_Process.clickOnStatus(status2);
			NewDesign_Emulate_Process.clickOnUpdateBtn();

			NewDesign_Emulate_Process.clickOnSwitchBack();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
			Thread.sleep(2000);

			NewDesign_Emulate_Process.clcikOnAdmin();
			NewDesign_Emulate_Process.clickOnSearchByThroughUser(searchby);
			NewDesign_Emulate_Process.clickSearchValueThroughUser(travellerSearchValue);
			NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
			NewDesign_Emulate_Process.clickOnEmulmateUserOption();
			NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);

			NewDesignTrips.clcikOnTrips();
			NewDesignTrips.clickOnAwaitingApproval();
			Thread.sleep(2000);
			NewDesignTrips.waitUntilDivDisplayed(driver);
			 NewDesignTrips.selectStatusFromDropdownUnderAwaitingscreen("All");


			NewDesignTrips.clickOnsearchTripsInAwaitingApprovalPg(ApproveridFromAwaitingScreen, Log, screenShots);
			Thread.sleep(2000);
			NewDesignTrips.getStatusInAwaitingApprovalForHotels(Log);
			NewDesign_Awaiting_ApprovalScreen.clickOnViewTripInAwaitingScreen();

			NewDesign_Awaiting_ApprovalScreen.getStatusFromViewTripInViewTripScreen(Log);

			System.out.println("Completed");

			// Function to Logout from Application
			// tripgainhomepage.logOutFromApplication(Log, screenShots);

		} catch (Exception e) {
			String errorMessage = "Exception occurred: " + e.toString();
			Log.ReportEvent("FAIL", errorMessage);
			screenShots.takeScreenShot();
			e.printStackTrace(); // You already have this, good for console logs
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

		// Use your existing launchBrowser method
		driver = launchBrowser(browser, url);

		Log = new Log(driver, test);
		screenShots = new ScreenShots(driver, test);

		// Add this simple JavaScript injection for API logging
	//	injectApiLogger();
	}

	

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			extantManager.flushReport();
		}
	}
}