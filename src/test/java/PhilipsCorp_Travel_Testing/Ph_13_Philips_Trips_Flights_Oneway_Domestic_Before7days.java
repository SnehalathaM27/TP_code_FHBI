package PhilipsCorp_Travel_Testing;
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
import com.tripgain.collectionofpages.Implementation_Corporate_TravellersPage;
import com.tripgain.collectionofpages.NewDesignHotelsSearchPage;
import com.tripgain.collectionofpages.NewDesign_AwaitingApprovalScreen;
import com.tripgain.collectionofpages.NewDesign_Buses_BookingPage;
import com.tripgain.collectionofpages.NewDesign_Buses_ResultsPage;
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
import com.tripgain.collectionofpages.Tripgain_BookingPage_Flights;
import com.tripgain.collectionofpages.Tripgain_FutureDates;
import com.tripgain.collectionofpages.Tripgain_HomePage_Flights;
import com.tripgain.collectionofpages.Tripgain_Login;
import com.tripgain.collectionofpages.Tripgain_RoundTripResultsScreen;
import com.tripgain.collectionofpages.Tripgain_TripPlannerCreateTripPage_Flights;
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

public class Ph_13_Philips_Trips_Flights_Oneway_Domestic_Before7days extends BaseClass {
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
        
        
        String username1 = excelData.get("UserName1");
        String pwd1 = excelData.get("Password1");
        number++;


		String origin = excelData.get("Origin");
		System.out.println(origin);
		String destination = excelData.get("Destination");
		System.out.println(destination);
		
		String Busorigin = excelData.get("BusOrigin");
		System.out.println(Busorigin);
		String Busdestination = excelData.get("BusDestination");
		System.out.println(Busdestination);
		
		String hotelOrigin = excelData.get("HotelOrigin");
		System.out.println(hotelOrigin);

		String tripName = excelData.get("TripName");
		
		
		 String searchby=excelData.get("SearchBy");
	        String searchvalue=excelData.get("SearchValue");
	        String remarks=excelData.get("Remarks");
	        String status=excelData.get("Status");
	        String travellerSearchValue=excelData.get("TravellerSearchValue");
	        String travellerSearchValue2=excelData.get("TravellerSearchValue2");
	        String status2=excelData.get("Status2");

//select 2 days 

			String[] dates = GenerateDates.GenerateDatesToSelectFlights();
			String fromDate = dates[0];
			String fromMonthYear = dates[2];
			String returnDate = dates[1];
			String returnMonthYear = dates[3];


		// Login to application
		NewDesign_Login NewDesignLogin = new NewDesign_Login(driver);

		SkyTravelers_Hotels_Login SkyTravelersHotelsLogin = new SkyTravelers_Hotels_Login(driver);
		NewDesignLogin.enterUserName("admin@Philipscorp.com");
		NewDesignLogin.enterPasswordName("EMUL-20260214-TGIW$99826@");
		NewDesignLogin.clickButton();
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		Thread.sleep(2000);

		NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);
		NewDesign_Hotels_ResultsPage NewDesignHotelsResultsPage=new NewDesign_Hotels_ResultsPage(driver);
		NewDesign_Hotels_DescPage NewDesignHotels_DescPage = new NewDesign_Hotels_DescPage(driver);
		NewDesign_Hotels_BookingPage NewDesign_HotelsBookingPage=new NewDesign_Hotels_BookingPage(driver);
		
		NewDesign_Buses_ResultsPage NewDesignBusesResultsPage = new NewDesign_Buses_ResultsPage(driver);
		NewDesign_Buses_BookingPage NewDesignBusesBookingPage=new NewDesign_Buses_BookingPage(driver);

		NewDesign_AwaitingApprovalScreen NewDesign_Awaiting_ApprovalScreen=new NewDesign_AwaitingApprovalScreen(driver);
		NewDesign_EmulateProcess NewDesign_Emulate_Process=new NewDesign_EmulateProcess(driver);
		Implementation_Corporate_TravellersPage Implementation_Corporate_Travellers_Page=new Implementation_Corporate_TravellersPage(driver);

		
		 NewDesign_Emulate_Process.clcikOnAdmin();
	        NewDesign_Emulate_Process.clickOnSearchByThroughUser("Email");
	        NewDesign_Emulate_Process.clickSearchValueThroughUser("traveler@philips");
	        NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	        
	        Implementation_Corporate_Travellers_Page.clickOnViewDetailBtnInCorpTravellers("traveler@philips");
	        Implementation_Corporate_Travellers_Page.clcikOnEditBtn();
	        
	        //select grade ----------------------
	        String[] selectedGradeFromDropdown = Implementation_Corporate_Travellers_Page.clickOnSelectGrade("E-5 - ", Log);

	        Implementation_Corporate_Travellers_Page.clickOnSaveBtn();
	        Thread.sleep(3000);
	        NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);

		NewDesignTrips.clcikOnTrips();
		NewDesignTrips.createTrip();
		String EnteredtripName = NewDesignTrips.enterNameThisTrip("Flight_Domestic1_OOP", Log);
		String origindetails = NewDesignTrips.enterfrom("Delhi");
		Thread.sleep(2000);
		String destdetails = NewDesignTrips.enterTo("Mumbai");
		Thread.sleep(2000);

		String journeydatedetails = NewDesignTrips.selectJourneyDate(fromDate, fromMonthYear);
		Thread.sleep(2000);

		String returndatedetails = NewDesignTrips.selectReturnDate(returnDate, returnMonthYear);

		 List<String> servicesdetails = NewDesignTrips.selectServices("Flight");
//		List<String> servicesdetails = NewDesignTrips.selectServices("Hotel");

		NewDesignTrips.clickCreateTripButton();

		List<String> servicesTextFromPopup = NewDesignTrips.getSelectedServicesTextFromPopupAfterTripCreated();

		NewDesignTrips.validateSelectedServicesInSelectedAndPopup(servicesdetails, servicesTextFromPopup, Log,screenShots);
		String[] tripIdFromPop = NewDesignTrips.getTripIdFromPopup(Log);
		NewDesignTrips.clickOnContinueToAddServicesBtn();

		String[] TripIdFromNextPage = NewDesignTrips.getTripIdFromTripDetailsPage(Log);

		String[] TripDatesFromSearchPg = NewDesignTrips.getDatesFromTripDetailsPage();
		
		 List<String> selectedServicesFromDetailsPg = NewDesignTrips.getSelectedServicesTextFromDetailsPage();
		 NewDesignTrips.validateSelectedAndDetailsPageServices(servicesdetails, selectedServicesFromDetailsPg, Log, screenShots);
					//--------------------Flights --------------------------------
					
					Tripgain_HomePage_Flights tripgain_HomePage= new Tripgain_HomePage_Flights(driver);
					Tripgain_BookingPage_Flights tripgain_BookingPage= new Tripgain_BookingPage_Flights(driver);
					Tripgain_TripPlannerCreateTripPage_Flights tripgain_TripPlannerCreateTripPage= new Tripgain_TripPlannerCreateTripPage_Flights(driver);

					 NewDesignTrips.clickOnServiceByText("Flight", Log);
					 
			            tripgain_HomePage.selectCabinClass("Economy");
			            tripgain_HomePage.clickOnRemoveReturnDate();

			     //       tripgain_TripPlannerCreateTripPage.clickButtonAndValidateMessage(Log, screenShots);
			     	   NewDesignTrips.clickOnAddButton();

			            tripgain_TripPlannerCreateTripPage.clickSearch(Log, screenShots);
			            tripgain_TripPlannerCreateTripPage.waitForFlightTag(Log, screenShots);
			            
			            

			            String[] OnwardDateText = tripgain_HomePage.getOnwardDateTextFromResultPage();

			            Implementation_Corporate_Travellers_Page.validateFlightPolicyAgainstDaysForDomestic(OnwardDateText, Log, screenShots);

			            //Methods to Select Flight get the Flight Date form Result Screen
			            tripgain_HomePage.clickViewFlightDetailsByIndex(1);
			            Map<String, List<String>> flightDataFromResultScreen = tripgain_HomePage.getDataFromUiForResultScreenByXPath();
			            List<String> flightCarrierDetails = tripgain_HomePage.getFlightDetailsCombined();
			            // ✅ Use the data directly
			            for (String detail : flightCarrierDetails) {
			                System.out.println("From test: " + detail);
			                Log.ReportEvent("INFO", "Flight Name and Number:" + detail);          }


			            //Methods to get the Fare Details from Fare Care and Validate with Flight Info
			            tripgain_HomePage.clickViewFareByIndex(1);
			            String[] fareDetails=tripgain_HomePage.clickOnSelectBasedOnFareType("Flexi",Log, screenShots);

			            Map<String, String> fareInfo = tripgain_HomePage.getFareDetailsFromFlightInfo(Log, screenShots);
			            System.out.println("Fare Price: " + fareInfo.get("FarePrice"));
			            System.out.println("Supplier: " + fareInfo.get("Supplier"));
			            System.out.println("Fare Type: " + fareInfo.get("FareType"));
			            System.out.println("Policy Type: " + fareInfo.get("policyType"));

			            Log.ReportEvent("INFO", "Validating the Fare Details from Fare Card to Flight info Card");

			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[0],fareInfo.get("FarePrice"),"Fare Price from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[2],fareInfo.get("Supplier"),"Supplier Name from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[1],fareInfo.get("FareType"),"Fare Name from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[7],fareInfo.get("policyType"),"Policy Type from Fare Card to Flight Info",Log, screenShots);

			            tripgain_HomePage.handleReasonAndProceed("Better Flight Time");

			            //Method to Validate Booking Screen is Displayed
			            tripgain_BookingPage.validateBookingScreenIsDisplayed(Log, screenShots);

			            //Methods to get Flight data from Booking Screen and Validate
			            Map<String, List<String>> bookingData = tripgain_BookingPage.clickIfPresentAndGetBookingData();

			            Log.ReportEvent("INFO", "Validating the Flight Details from Result to Booking Screen");
			            tripgain_BookingPage.validateSearchAndBookingDataByXPath(flightDataFromResultScreen,bookingData,Log, screenShots);

			            Map<String, String> flightDetails=tripgain_BookingPage.getDataFromBookingPage();
			            System.out.println("Supplier: " + flightDetails.get("Supplier"));
			            System.out.println("Fare Type: " + flightDetails.get("FareName"));
			            System.out.println("Policy Type: " + flightDetails.get("PolicyType"));
			            String fareName=fareInfo.get("FareType");
			            String actualFareName=fareName +" "+"Fare";

			            Log.ReportEvent("INFO", "Validating the Fare Details from Flight info Card to Booking Screen");

			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("Supplier"),fareInfo.get("Supplier"),"Supplier Name from Flight Info and Booking Screen",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("FareName"),actualFareName,"Fare Name from Flight Info and Booking Screen",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("PolicyType"),fareInfo.get("policyType"),"Policy Type from Flight Info and Booking Screen",Log, screenShots);

			            Log.ReportEvent("INFO", "Validating the Final fare from Flight info Card to Booking Screen");

			            String totalFare=tripgain_BookingPage.getTotalFare();
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareInfo.get("FarePrice"),totalFare,"Total Cost from Result to Booking Screen",Log, screenShots);

//			            String[] titles = title.split(",");
//			            tripgain_BookingPage.enterAdultDetailsForDomestic(titles,adults,Log, screenShots);
			//
		        //    tripgain_BookingPage.clickProceedBookingAndValidateToast(Log, screenShots);
			            
			            tripgain_TripPlannerCreateTripPage.clcikonAddTripToContinueBtn();
		
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