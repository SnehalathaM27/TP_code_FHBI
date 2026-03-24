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
import com.tripgain.collectionofpages.NewDesign_Buses_BookingPage;
import com.tripgain.collectionofpages.NewDesign_Buses_ResultsPage;
import com.tripgain.collectionofpages.NewDesign_Buses_SerachPage;
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

public class TCNDTR_2_CreateTripsForBuses extends BaseClass {
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
		NewDesignLogin.enterUserName(username);
		NewDesignLogin.enterPasswordName(pwd);
		NewDesignLogin.clickButton();
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		Thread.sleep(4000);

		NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);
	
		NewDesign_Buses_ResultsPage NewDesignBusesResultsPage = new NewDesign_Buses_ResultsPage(driver);
		NewDesign_Buses_BookingPage NewDesignBusesBookingPage=new NewDesign_Buses_BookingPage(driver);


		NewDesignTrips.clcikOnTrips();
		NewDesignTrips.createTrip();
		NewDesignTrips.enterNameThisTrip(tripName, Log);
		String origindetails = NewDesignTrips.enterfrom(origin);
		String destdetails = NewDesignTrips.enterTo(destination);

		String journeydatedetails = NewDesignTrips.selectJourneyDate(fromDate, fromMonthYear);

		String returndatedetails = NewDesignTrips.selectReturnDate(returnDate, returnMonthYear);

		// List<String> servicesdetails = Trip_Planner.selectServices("Flight","Hotel");
		List<String> servicesdetails = NewDesignTrips.selectServices("Bus");

		NewDesignTrips.clickCreateTripButton();

		List<String> servicesTextFromPopup = NewDesignTrips.getSelectedServicesTextFromPopup();

		NewDesignTrips.validateSelectedServicesInSelectedAndPopup(servicesdetails, servicesTextFromPopup, Log,screenShots);
		String[] tripIdFromPop = NewDesignTrips.getTripIdFromPopup(Log);
		NewDesignTrips.clickOnContinueToAddServicesBtn();

		String[] TripIdFromNextPage = NewDesignTrips.getTripIdFromTripDetailsPage(Log);

		String[] TripDatesFromSearchPg = NewDesignTrips.getDatesFromTripDetailsPage();
		
		 List<String> selectedServicesFromDetailsPg = NewDesignTrips.getSelectedServicesTextFromDetailsPage();
		 NewDesignTrips.validateSelectedAndDetailsPageServices(servicesdetails, selectedServicesFromDetailsPg, Log, screenShots);

		 NewDesignTrips.clickOnServiceByText("Bus", Log);
		 
		 String enteredBusFromLOcSearch = NewDesignTrips.enterLocationForBusesOndetailsPg("Mumbai");
		 String enteredBusToLOcSearch = NewDesignTrips.enterToLocationForBusesOndetailsPg("Delhi");
		 String enteredBusDatesToSearch = NewDesignTrips.selectBusesJourneyDate(fromDate, fromMonthYear);
		 NewDesignTrips.clickOnAddButton();
		 Thread.sleep(2000);
		 
		 String[] BusLocAfterAdd = NewDesignTrips.getTripBusFromLocFromTripDetailsPageAfterAdd();
		 String[] BusDatesAfterAdd = NewDesignTrips.getTripBusDatesFromTripDetailsPageAfterAdd();
		 NewDesignTrips.clcikOnSearchBus();
		 
		 NewDesignTrips.validateBusFromLocationOnDetailsPage(enteredBusFromLOcSearch, BusLocAfterAdd, Log, screenShots);
		 NewDesignTrips.validateBusToLocationOnDetailsPage(enteredBusToLOcSearch, BusLocAfterAdd, Log, screenShots);
		 NewDesignTrips.validateBusJourneyDateFromDetailsPageToAdd(enteredBusDatesToSearch, BusDatesAfterAdd, Log, screenShots);
		 
		 
		 
			String[] checkindateResultPage = NewDesignBusesResultsPage.getCheckInDateTextFromResultPage();
			String[] ResultPgOrigin = NewDesignBusesResultsPage.getOriginFromresultPg();
			String[] ResultPgDest = NewDesignBusesResultsPage.getDestFromresultPg();
			
//			NewDesignBusesResultsPage.selectSortOption("Price: Low to High", Log);
//			NewDesignBusesResultsPage.validatePricesLowToHigh(Log);
//			Thread.sleep(2000);
//			NewDesignBusesResultsPage.selectSortOption("Price: High to Low", Log);
//			Thread.sleep(1000);
//			NewDesignBusesResultsPage.validatePricesHighToLow(Log);
//			
		//	NewDesignBusesResultsPage.selectSortOption("Departure Time", Log);
			
//			NewDesignBusesResultsPage.selectSortOption("Arrival Time", Log);
//			NewDesignBusesResultsPage.selectSortOption("Duration", Log);
		//	NewDesignBusesResultsPage.selectSortOption("Sort Recommended", Log);

		/*	NewDesignBusesResultsPage.filterByPriceAndValidate(Log, screenShots);
			NewDesignBusesResultsPage.clickAndValidatePolicyDropdown(Log, screenShots);*/
		//	NewDesignBusesResultsPage.clickAndValidateBusTypeOptions(Log, screenShots);
//		NewDesignBusesResultsPage.clickAndValidateSeatType(Log, screenShots);
		//	NewDesignBusesResultsPage.getAndValidatePolicyText(Log, screenShots);
			NewDesignBusesResultsPage.clickAndValidateSearchOperator(Log, screenShots);
		
			String[] Busdetails = NewDesignBusesResultsPage.getBusDetailsFromListingByIndex(0);
			
			String[] SeatType = NewDesignBusesResultsPage.getSeatTypeTextFromresultPgAfterSelect();
			String[] BoardingDetails = NewDesignBusesResultsPage.selectBoardingPoints(Log, screenShots);
			String[] DroppingPoint = NewDesignBusesResultsPage.selectDroppingPoint(Log, screenShots);
		//	NewDesignBusesResultsPage.clcikLowerBirth(Log, screenShots);
			Thread.sleep(1000);
			NewDesignBusesResultsPage.clickUpperBerth(Log, screenShots);
			String SeatSelectionPrice = NewDesignBusesResultsPage.getPriceAfterSeatSelection(Log, screenShots);
			NewDesignBusesResultsPage.clcikOnConfirmSeat();
			
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
			String[] BookingPgSeat = NewDesignBusesBookingPage.getSelectedSeatFromBokingPg();
			
			
			NewDesignBusesBookingPage.validateOriginFromResultToBookingpage(BookingPgorigin, ResultPgOrigin, Log, screenShots);
			NewDesignBusesBookingPage.validateDestFromResultToBookingPage(BookingDest, ResultPgDest, Log, screenShots);
			NewDesignBusesBookingPage.validateBusOperatorNameFromListingToBookingPage(Busdetails, BookingPgBusName, Log, screenShots);
			NewDesignBusesBookingPage.validateBoardingPointLocationFromListingToBookingPage(BoardingDetails, BookingPgBoardingPoint, Log, screenShots);
			NewDesignBusesBookingPage.validateDroppingPointLocationFromListingToBookingPage(DroppingPoint, BookingPgdroppingPoint, Log, screenShots);
			NewDesignBusesBookingPage.validateSeaterTypeFromListingToBookingPage(SeatType, BookingPgSeater, Log, screenShots);
		//	NewDesignBusesBookingPage.validateCheckInDateBetweenResultAndBookingPages(checkindateResultPage, BookingPgDate, Log, screenShots);
			NewDesignBusesBookingPage.validateDepartureTimeFromListingToBookingPage(Busdetails, BookingPgDepartTime, Log, screenShots);
			NewDesignBusesBookingPage.validateArrivalTimeFromListingToBookingPage(Busdetails, BookingPgArrival, Log, screenShots);
			NewDesignBusesBookingPage.validateDurationFromListingToBookingPage(Busdetails, bookingPgDuration, Log, screenShots);
			NewDesignBusesBookingPage.validatePolicyFromListingToBookingPage(Busdetails, BookingPgPolicy, Log, screenShots);
			NewDesignBusesBookingPage.validatePriceFromresultToBookingPage(BookingPgPrice, SeatSelectionPrice, Log, screenShots);
			
		//	NewDesignBusesBookingPage.enterEmailForBuses("abc@gmail.com");
//		NewDesignBusesBookingPage.enterLastNameForBuses("traveller");
//			//NewDesignBusesBookingPage.enterPhNoForBuses("98765432190");
//			NewDesignBusesBookingPage.enetrIdCardNumber("1234567");
//			NewDesignBusesBookingPage.clickOnIdCardType();
			NewDesignBusesBookingPage.clcikOnAddTripAndContinueButton();
			Thread.sleep(3000);
			NewDesignBusesBookingPage.clcikOnDownButtonInBusesDesclaimer();
			
			String[] BusNameAfterSelected = NewDesignBusesBookingPage.getBusNameFromSelectedInBusDetailsPg();
			String[] BusTypeAfterSelected = NewDesignBusesBookingPage.getBusTypeFromSelectedInBusDetailsPg();
			String[] BusFromLocAfterSelected = NewDesignBusesBookingPage.getBusFromLocFromSelectedInBusDetailsPg();
			String[] BusToLocAfterSelected = NewDesignBusesBookingPage.getBusToLocFromSelectedInBusDetailsPg();
			String[] BusJourneyDateAfterSelected = NewDesignBusesBookingPage.getBusJourneyDateFromSelectedInBusDetailsPg();
			String[] BusDepartTimeAfterSelected = NewDesignBusesBookingPage.getBusDepartTimeFromSelectedInBusDetailsPg();
			String[] BusArrivalTimeAfterSelected = NewDesignBusesBookingPage.getBusArrivalTimeFromSelectedInBusDetailsPg();
			String[] BusBoardingPointAfterSelected = NewDesignBusesBookingPage.getBusBoardingPointFromSelectedInBusDetailsPg();
			String[] BusDroppoingPOintAfterSelected = NewDesignBusesBookingPage.getBusDroppingPointFromSelectedInBusDetailsPg();
			String[] BusDurationAfterSelected = NewDesignBusesBookingPage.getBusDurationFromSelectedInBusDetailsPg();
			String[] BusSeatAfterSelected = NewDesignBusesBookingPage.getBusSeatTextFromSelectedInBusDetailsPg();
			String[] BusPolicyAfterSelected = NewDesignBusesBookingPage.getBusPolicyFromSelectedInBusDetailsPg();
			
			
			NewDesignBusesBookingPage.validateBusNameFromBusBookingToDetailsPageAfterAdd(BookingPgBusName, BusNameAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateBusTypeFromBookingToDetailspageAfterAdd(BookingPgSeater, BusTypeAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateFromLocationBetweenBookingAndDetailsAfterAdd(BookingPgorigin, BusFromLocAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateToLocationBetweenBookingAndDetailsAfterAdd(BookingDest, BusToLocAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateJourneyDateBetweenBookingAndDetailsAfterAdd(BookingPgDate, BusJourneyDateAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateDepartTimeBetweenBookingAndDetailsAfterAdd(BookingPgDepartTime, BusDepartTimeAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateArrivalTimeBetweenBookingAndDetailsAfterAdd(BookingPgArrival, BusArrivalTimeAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateBoardingPointBetweenBookingAndDetailsAfterAdd(BookingPgBoardingPoint, BusBoardingPointAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateDroppingPointBetweenBookingAndDetailsAfterAdd(BookingPgdroppingPoint, BusDroppoingPOintAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateDurationBetweenBookingAndDetailsAfterAdd(bookingPgDuration, BusDurationAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateSelectedSeatBetweenBookingAndDetailsAfterAdd(BookingPgSeat, BusSeatAfterSelected, Log, screenShots);
			NewDesignBusesBookingPage.validateBusPolicyBetweenBookingAndDetailsAfterAdd(BookingPgPolicy, BusPolicyAfterSelected, Log, screenShots);
			
			
			
	
		// Function to Logout from Application
		// tripgainhomepage.logOutFromApplication(Log, screenShots);
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