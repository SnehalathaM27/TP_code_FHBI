package NewDesign_Trips;

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
import com.tripgain.collectionofpages.NewDesignHotelsSearchPage;
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

public class TCNDTR_1_CreateTripsForHotels extends BaseClass {
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
        
        System.out.println(username);
        System.out.println(pwd);
        number++;



		String origin = excelData.get("Origin");
		System.out.println(origin);
		String destination = excelData.get("Destination");
		System.out.println(destination);

		String tripName = excelData.get("TripName");

		String[] dates = GenerateDates.GenerateDatesToSelectFlights();
		String fromDate = dates[0];
		String fromMonthYear = dates[2];
		String returnDate = dates[1];
		String returnMonthYear = dates[3];

		// Login to application
		NewDesign_Login NewDesignLogin = new NewDesign_Login(driver);

		SkyTravelers_Hotels_Login SkyTravelersHotelsLogin = new SkyTravelers_Hotels_Login(driver);
		Tripgain_HomePage_Flights tripgain_HomePage = new Tripgain_HomePage_Flights(driver);

		NewDesignLogin.enterUserName(username);
		NewDesignLogin.enterPasswordName(pwd);
		NewDesignLogin.clickButton();
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		tripgain_HomePage.clickIfPresentCloseBtn();
		

		NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);
		NewDesign_Hotels_ResultsPage NewDesignHotelsResultsPage=new NewDesign_Hotels_ResultsPage(driver);
		NewDesign_Hotels_DescPage NewDesignHotels_DescPage = new NewDesign_Hotels_DescPage(driver);
		NewDesign_Hotels_BookingPage NewDesign_HotelsBookingPage=new NewDesign_Hotels_BookingPage(driver);
		


		NewDesignTrips.clcikOnTrips();
		NewDesignTrips.createTrip();
		Thread.sleep(3000);
		NewDesignTrips.enterNameThisTrip(tripName, Log);
		String origindetails = NewDesignTrips.enterfrom(origin);
		String destdetails = NewDesignTrips.enterTo(destination);
        Thread.sleep(1000);
         String journeydatedetails = NewDesignTrips.selectJourneyDate(fromDate, fromMonthYear);

		String returndatedetails = NewDesignTrips.selectReturnDate(returnDate, returnMonthYear);
		NewDesignTrips.selectTripTypeDropdown("Business Travel");

		// List<String> servicesdetails = Trip_Planner.selectServices("Flight","Hotel");
		List<String> servicesdetails = NewDesignTrips.selectServices("Bus","Hotel");

		NewDesignTrips.clickCreateTripButton();

		List<String> servicesTextFromPopup = NewDesignTrips.getSelectedServicesTextFromPopup();

		NewDesignTrips.validateSelectedServicesInSelectedAndPopup(servicesdetails, servicesTextFromPopup, Log,screenShots);
		
	//	String[] tripIdFromPop = NewDesignTrips.getTripIdFromPopup(Log);
	//	NewDesignTrips.clickOnContinueToAddServicesBtn();

		String[] TripIdFromNextPage = NewDesignTrips.getTripIdFromTripDetailsPage(Log);

		String[] TripDatesFromSearchPg = NewDesignTrips.getDatesFromTripDetailsPage();
		
		 List<String> selectedServicesFromDetailsPg = NewDesignTrips.getSelectedServicesTextFromDetailsPage();
		 NewDesignTrips.validateSelectedAndDetailsPageServices(servicesdetails, selectedServicesFromDetailsPg, Log, screenShots);

		 NewDesignTrips.clickOnServiceByText("Hotel", Log);
		 
		 String locationToSearch = NewDesignTrips.enterLocationForHotelsOndetailsPg("Mumbai");
		 String checkInDateFromDetailsPg = NewDesignTrips.selectJourneyDate(fromDate, fromMonthYear);
	   String checkOutDateFromDetailsPg = NewDesignTrips.selectReturnDate(returnDate, returnMonthYear);
	   NewDesignTrips.clickOnAddButton();
	   
	   String[] LocationNameAfterAdd = NewDesignTrips.getTripNameFromTripDetailsPageAfterAddForHotels();
	   String[] datesAfterAdd = NewDesignTrips.getCheckInAndOutDateFromTripDetailsPageForHotelsAfterAdd();
	   
	   NewDesignTrips.validateLocationFromsearchToAfterClickAddForHotels(locationToSearch, LocationNameAfterAdd, Log, screenShots);
	   NewDesignTrips.validateCheckInAndCheckOutDatesFromsearchToAdd(checkInDateFromDetailsPg, checkOutDateFromDetailsPg, datesAfterAdd, Log, screenShots);

	   NewDesignTrips.clickOnSearchHotelBut();
	   Thread.sleep(2000);
	   
	   String[] locNameFromTripResultPg = NewDesignTrips.getLocNAmeFromTripHotelResultsPg();
	   String[] checkindateResultPage = NewDesignHotelsResultsPage.getCheckInDateTextFromResultPage();
		String[] checkoutdateResultPage =NewDesignHotelsResultsPage.getCheckOutDateTextFromResultPage();
		
		NewDesignTrips.validateHotelLocationFromResultsAndAfterAddInDetailsPage(locNameFromTripResultPg, LocationNameAfterAdd, Log, screenShots);
		NewDesignTrips.validateCheckInAndOutDatesFromResultsAndDetailsAfterAdd(checkindateResultPage, checkoutdateResultPage, datesAfterAdd, Log, screenShots);
		
		Thread.sleep(3000);
	/*	NewDesignHotelsResultsPage.clickOnSortOption("Price: Low to High", Log);
		NewDesignHotelsResultsPage.validatePricesLowToHigh(Log);
		Thread.sleep(2000);
		
		NewDesignHotelsResultsPage.clickOnSortOption("Price: High to Low", Log);
		NewDesignHotelsResultsPage.validatePricesHighToLow(Log);
		Thread.sleep(2000);

//			NewDesignHotelsResultsPage.clickOnSortOption("Star Rating: Ascending", Log);
//			NewDesignHotelsResultsPage.clickOnSortOption("Star Rating: Descending", Log);
			NewDesignHotelsResultsPage.clickOnSortOption("Distance: Ascending", Log);
			NewDesignHotelsResultsPage.validateDistanceAscending(Log);
			Thread.sleep(2000);

			NewDesignHotelsResultsPage.clickOnSortOption("Distance: Descending", Log);
			NewDesignHotelsResultsPage.validateDistanceDescendingSimple(Log);
			Thread.sleep(2000);


			NewDesignHotelsResultsPage.selectCurrencyFromDropdown("TND", Log); 
			
			Thread.sleep(2000);
			*/

			
			
		//get all the hotel details from the selected hotel card
			String[] hotelDetails = NewDesignHotelsResultsPage.selectHotelAndGetDetails(2, Log);
			
			//get all the details from desc page 
			String[] hotelAddressFromDesc = NewDesignHotels_DescPage.getAddressFromDescPg();
			String[] hotelAmenitiesFromDesc = NewDesignHotels_DescPage.getAmenitiesFromDescPg();
			String[] checkInDateFromDesc = NewDesignHotels_DescPage.getCheckInAfterFromDescPg();
			String[] checkOutFromDesc = NewDesignHotels_DescPage.getCheckOutTimeFromDescPg();
			String[] hotelNameFromDesc = NewDesignHotels_DescPage.getHotelNameFromDescPg();
			NewDesignHotels_DescPage.getHotelRatingFromDescPg();
			String[] perNightPriceFromDesc = NewDesignHotels_DescPage.getPerNightPriceFromDescPg();
			String[] hotelPriceFromDesc = NewDesignHotels_DescPage.getPriceFromDescPg();
			String[] PolicyFromDesc = NewDesignHotels_DescPage.getPolicyFromDescPg();
			String[] OtherCurrencyInDesc=NewDesignHotels_DescPage.getOtherCountryPriceFromDescPg();
			//NewDesignHotels_DescPage.getStarRatingCount(null);
			
			//validations b/w desc to result pages 
		NewDesignHotels_DescPage.validateHotelNameFromDescToResultPage(hotelNameFromDesc, hotelDetails, Log, screenShots);
			NewDesignHotels_DescPage.validateHotelAddressFromDescToResultPage(hotelAddressFromDesc, hotelDetails, Log, screenShots);
			NewDesignHotels_DescPage.validateHotelPriceFromDescToResultPage(hotelPriceFromDesc, hotelDetails, Log, screenShots);
			NewDesignHotels_DescPage.validateHotelPolicyFromDescToResultPage(PolicyFromDesc, hotelDetails, Log, screenShots);
		//	NewDesignHotels_DescPage.validatePerNightPriceFromDescToResultPage(perNightPriceFromDesc, hotelDetails, Log, screenShots);
			NewDesignHotels_DescPage.validateAmenitiesFromDescToResultPage(hotelAmenitiesFromDesc, hotelDetails, Log, screenShots);
		//	NewDesignHotelsResultsPage.validateOtherCurrencyPriceFromDescToResultPage(OtherCurrencyInDesc, hotelDetails, Log, screenShots);
			
			
			String[] selectRooms = NewDesignHotels_DescPage.selectRoomsFromDescPg();
			
			Thread.sleep(3000);
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
			String[] BookingNightsStay = NewDesign_HotelsBookingPage.getHotelNightsStayFromBookingPg();
			
			NewDesign_HotelsBookingPage.validateCheckInAfterFromDescToBookingPage(checkInDateFromDesc, BookingPgcheckIn, Log, screenShots);
			NewDesign_HotelsBookingPage.validateCheckOutTimeFromDescToBookingPage(checkOutFromDesc, BookingPgCheckOut, Log, screenShots);
			NewDesign_HotelsBookingPage.validateHotelAddressFromDescToBookingPage(hotelAddressFromDesc, BookingPgAddress, Log, screenShots);
			NewDesign_HotelsBookingPage.validateHotelNameFromDescToBookingPage(hotelNameFromDesc, BookingPgHotelNm, Log, screenShots);
			NewDesign_HotelsBookingPage.validateLabelFromDescToBookingPage(selectRooms, BookingPgLabel, Log, screenShots);
			NewDesign_HotelsBookingPage.validateMealsTextFromDescToBookingPage(selectRooms, BookingPgMeals, Log, screenShots);
			NewDesign_HotelsBookingPage.validatePolicyTextFromDescToBookingPage(PolicyFromDesc, BookingPgPolicy, Log, screenShots);
			NewDesign_HotelsBookingPage.validateRefundableTextFromDescToBookingPage(selectRooms, BookingPgRefundable, Log, screenShots);
			NewDesign_HotelsBookingPage.validateSelectedRoomTextFromDescToBookingPage(selectRooms, BookingPgSelectedRoomText, Log, screenShots);
			//NewDesign_HotelsBookingPage.validatePriceFromDescWithBookingPage(selectRooms, BookingPgTotalfareAmount, Log, screenShots);
			NewDesign_HotelsBookingPage.validateCheckInDateBetweenResultAndBookingPage(checkindateResultPage, BookingCheckIndate, Log, screenShots);
			NewDesign_HotelsBookingPage.validateCheckOutDateBetweenResultAndBookingPage(checkoutdateResultPage, BookingCheckOutdate, Log, screenShots);
			//NewDesign_HotelsBookingPage.addTravellerDetails();
			
			NewDesignTrips.clickOnAddHotelToTrip(Log);
			Thread.sleep(3000);
			
			NewDesignTrips.clcikOnDownButtonInHotelsDesclaimer();
			Thread.sleep(1000);
			
			String[] HotelnameFromDetailspg = NewDesignTrips.getHotelNameTextFromTripDetailsPage();
			String[] HoteladdressFromDetailspg = NewDesignTrips.getHotelAddressTextFromTripDetailsPage();
			String[] HotelPolicyFromDetailspg = NewDesignTrips.getHotelPolicyTextFromTripDetailsPage();
			String[] HotelroomsFromDetailspg = NewDesignTrips.gethotelRoomTextFromTripDetailsPage();
			String[] HotelMealsFromDetailspg = NewDesignTrips.getHotelMealsTextFromTripDetailsPage();
			String[] HotelCheckInFromDetailspg = NewDesignTrips.getHotelCheckInafterTextFromTripDetailsPage();
			String[] HotelCheckOutFromDetailspg = NewDesignTrips.getHotelCheckOutBeforeTextFromTripDetailsPage();
			String[] HotelStayFromDetailspg = NewDesignTrips.getHotelDaysStayTextFromTripDetailsPage();
			
			NewDesignTrips.validateHotelNameFromTripDetailsToHotelsBookingPage(HotelnameFromDetailspg, BookingPgHotelNm, Log, screenShots);
			NewDesignTrips.validateHotelAddressFromTripDetailsToHotelBookingPage(HoteladdressFromDetailspg, BookingPgAddress, Log, screenShots);
			NewDesignTrips.validateHotelPolicyFromTripDetailsToBookingPage(HotelPolicyFromDetailspg, BookingPgPolicy, Log, screenShots);
			NewDesignTrips.validateSelectedRoomFromTripDetailsToBookingPage(HotelroomsFromDetailspg, BookingPgSelectedRoomText, Log, screenShots);
			NewDesignTrips.validateHotelMealsFromTripDetailsToBookingPage(HotelMealsFromDetailspg, BookingPgMeals, Log, screenShots);
			NewDesignTrips.validateHotelCheckInTimeFromTripDetailsToBookingPage(HotelCheckInFromDetailspg, BookingPgcheckIn, Log, screenShots);
			NewDesignTrips.validateHotelCheckOutTimeFromTripDetailsToBookingPage(HotelCheckOutFromDetailspg, BookingPgCheckOut, Log, screenShots);
			NewDesignTrips.validateDaysStayFromTripDetailsToBookingPage(HotelStayFromDetailspg, BookingNightsStay, Log, screenShots);
			
			
			
		
		// Function to Logout from Application
		// tripgainhomepage.logOutFromApplication(Log, screenShots);
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