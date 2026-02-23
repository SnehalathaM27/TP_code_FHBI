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

public class Ph_17_Philips_Trips_Flights_RoundTrip_International_Individual_Before15days extends BaseClass {
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
			 String fromDate=dates[11];
	         String returnDate=dates[0];
	         String fromMonthYear=dates[12];
	         String returnMonthYear=dates[2];


		// Login to application
		NewDesign_Login NewDesignLogin = new NewDesign_Login(driver);

		SkyTravelers_Hotels_Login SkyTravelersHotelsLogin = new SkyTravelers_Hotels_Login(driver);
		NewDesignLogin.enterUserName("admin@Philipscorp.com");
		NewDesignLogin.enterPasswordName("EMUL-20260213-TGIW$99826@");
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
		String EnteredtripName = NewDesignTrips.enterNameThisTrip("Flight_RoundTrip_Individual_International1_OOP", Log);
		String origindetails = NewDesignTrips.enterfrom("Delhi");
		String destdetails = NewDesignTrips.enterTo("Singapore");

		String journeydatedetails = NewDesignTrips.selectJourneyDate(fromDate, fromMonthYear);
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
			            tripgain_TripPlannerCreateTripPage.waitForFlightTagForInternational(Log, screenShots);
			           
			            tripgain_HomePage.clickIndividualFlightsButton(Log);
			            Thread.sleep(5000);
			            tripgain_TripPlannerCreateTripPage.waitForFlightTag(Log, screenShots);

//			            String actualFromDate = tripgain_HomePage.getOnlyDayAndMonthFromDateLabel();
//			            tripgain_HomePage.validateDepartureDateIsDisplayed(actualFromDate,Log, screenShots);

//			            String departingDateAndSectors=tripgain_HomePage.getFormattedJourneyDate();
//			            String returnDateAndSectors=tripgain_HomePage.getFormattedReturnDate();

			            String[] OnwardDateText = tripgain_HomePage.getOnwardDateTextFromResultPage();
			            String[] ReturnDateText = tripgain_HomePage.getReturnDateTextFromResultPage();

			            Implementation_Corporate_Travellers_Page.validateFlightPolicyAgainstDaysForInternational(OnwardDateText, Log, screenShots);



			            tripgain_HomePage.clickViewFlightDetailsByIndex(1);
			            Map<String, List<String>> flightDepartingDataFromResultScreen = tripgain_HomePage.getDataFromUiForResultScreenByXPath();

			            List<String> flightCarrierDetails = tripgain_HomePage.getFlightDetailsCombined();
			            // ✅ Use the data directly
			            for (String detail : flightCarrierDetails) {
			                System.out.println("From test: " + detail);
			                Log.ReportEvent("INFO", "Departing Flight Name and Number:" + detail);           }

			            tripgain_HomePage.clickViewFareByIndex(1);
			            String[] fareDetails=tripgain_HomePage.clickOnSelectBasedOnFareType("Flexi",Log, screenShots);

			            Map<String, String> fareInfo = tripgain_HomePage.getFareDetailsFromFlightInfo(Log, screenShots);
			            System.out.println("Fare Price: " + fareInfo.get("FarePrice"));
			            System.out.println("Supplier: " + fareInfo.get("Supplier"));
			            System.out.println("Fare Type: " + fareInfo.get("FareType"));
			            System.out.println("Policy Type: " + fareInfo.get("policyType"));

			            Log.ReportEvent("INFO", "Validating the Departing Flight Fare Details from Fare Card to Flight info Card");


			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[0],fareInfo.get("FarePrice"),"Departing Flight Fare Price from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[2],fareInfo.get("Supplier"),"Departing Flight Supplier Name from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[1],fareInfo.get("FareType"),"Departing Flight Fare Name from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[7],fareInfo.get("policyType"),"Departing Flight Policy Type from Fare Card to Flight Info",Log, screenShots);

//			            String actualReturnDate = tripgain_HomePage.getOnlyDayAndMonthReturnDateLabel();
//			            tripgain_HomePage.validateDepartureDateIsDisplayed(actualReturnDate,Log, screenShots);
			            tripgain_HomePage.clickOutboundFlightButton();


			            //Method to get the data from Return Flight
			            Implementation_Corporate_Travellers_Page.validateFlightPolicyAgainstDaysForInternational(OnwardDateText, Log, screenShots);

			            tripgain_HomePage.clickViewFlightDetailsByIndex(1);
			            Map<String, List<String>> flightReturnDataFromResultScreen = tripgain_HomePage.getDataFromUiForResultScreenByXPath();

			            List<String> returnFlightCarrierDetails = tripgain_HomePage.getFlightDetailsCombined();
			            // ✅ Use the data directly
			            for (String detail : returnFlightCarrierDetails) {
			                System.out.println("From test: " + detail);
			                Log.ReportEvent("INFO", "Return Flight Name and Number:" + detail);           }


			            tripgain_HomePage.clickViewFareByIndex(1);
			            String[] returnFareDetails=tripgain_HomePage.clickOnSelectBasedOnFareType("Flexi",Log, screenShots);

			            Map<String, String> returnFareInfo = tripgain_HomePage.getFareDetailsFromFlightInfoForReturnFlights(Log, screenShots);
			            System.out.println("Fare Price: " + returnFareInfo.get("FarePrice"));
			            System.out.println("Supplier: " + returnFareInfo.get("Supplier"));
			            System.out.println("Fare Type: " + returnFareInfo.get("FareType"));
			            System.out.println("Policy Type: " + returnFareInfo.get("policyType"));

			            Log.ReportEvent("INFO", "Validating the Return Flight Fare Details from Fare Card to Flight info Card");

			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(returnFareDetails[0],returnFareInfo.get("FarePrice"),"Return Flight Fare Price from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(returnFareDetails[2],returnFareInfo.get("Supplier"),"Return Flight Supplier Name from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(returnFareDetails[1],returnFareInfo.get("FareType"),"Return Flight Fare Name from Fare Card to Flight Info",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(returnFareDetails[7],returnFareInfo.get("policyType"),"Return Flight Policy Type from Fare Card to Flight Info",Log, screenShots);

			            String totalCost = tripgain_HomePage.getTotalCost();


			            tripgain_HomePage.handleReasonAndProceed("Better Flight Time");
			            tripgain_BookingPage.validateBookingScreenIsDisplayed(Log, screenShots);

			            Log.ReportEvent("INFO", "Validating the Flight Details from Result to Booking Screen");

//			            String[] journeys = tripgain_BookingPage.getJourneyDetails();
//			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(departingDateAndSectors,journeys[0],"Departing Flight Sector and Date from Result to Booking Screen",Log, screenShots);
//			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(returnDateAndSectors,journeys[1],"Return Flight Sector and Date from Result to Booking Screen",Log, screenShots);

			            Map<String, List<String>> bookingData = tripgain_BookingPage.clickIfPresentAndGetBookingData();
			            tripgain_BookingPage.validateSearchAndBookingDataByXPath(flightDepartingDataFromResultScreen,bookingData,Log, screenShots);

			            Map<String, List<String>> returnFlightBookingData = tripgain_BookingPage.clickIfPresentAndGetBookingDataForReturnFlight();
			            tripgain_BookingPage.validateSearchAndBookingDataByXPath(flightReturnDataFromResultScreen,returnFlightBookingData,Log, screenShots);

			            Map<String, String> flightDetails=tripgain_BookingPage.getDataFromBookingPage();
			            System.out.println("Supplier: " + flightDetails.get("Supplier"));
			            System.out.println("Fare Type: " + flightDetails.get("FareName"));
			            System.out.println("Policy Type: " + flightDetails.get("PolicyType"));
			            String fareName=fareInfo.get("FareType");
			            String actualFareName=fareName +" "+"Fare";
			            Log.ReportEvent("INFO", "Validating the Departing Flight Fare Details from Flight info Card to Booking Screen");

			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("Supplier"),fareInfo.get("Supplier"),"Departing Flight: Supplier Name from Flight Info and Booking Screen",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("FareName"),actualFareName,"Departing Flight: Fare Name from Flight Info and Booking Screen",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("PolicyType"),fareInfo.get("policyType"),"Departing Flight: Policy Type from Flight Info and Booking Screen",Log, screenShots);

			            Map<String, String> flightDetailsForReturnFlights=tripgain_BookingPage.getDataFromBookingPageForReturnFlights();
			            System.out.println("Supplier: " + flightDetailsForReturnFlights.get("Supplier"));
			            System.out.println("Fare Type: " + flightDetailsForReturnFlights.get("FareName"));
			            System.out.println("Policy Type: " + flightDetailsForReturnFlights.get("PolicyType"));

			            String fareName1=returnFareInfo.get("FareType");
			            String actualReturnFareName=fareName1 +" "+"Fare";
			            Log.ReportEvent("INFO", "Validating the Return Flight Fare Details from Flight info Card to Booking Screen");

			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetailsForReturnFlights.get("Supplier"),returnFareInfo.get("Supplier"),"Return Flight: Supplier Name from Flight Info and Booking Screen",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetailsForReturnFlights.get("FareName"),actualReturnFareName,"Return Flight: Fare Name from Flight Info and Booking Screen",Log, screenShots);
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetailsForReturnFlights.get("PolicyType"),returnFareInfo.get("policyType"),"Return Flight: Policy Type from Flight Info and Booking Screen",Log, screenShots);


			            Log.ReportEvent("INFO", "Validating the Final fare from Flight info Card to Booking Screen");
			            String totalFare=tripgain_BookingPage.getTotalFare();
			            tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(totalCost,totalFare,"Total Cost from Result to Booking Screen",Log, screenShots);

//			            String[] titles = title.split(",");
//			            tripgain_BookingPage.selectAdultToEnterDetails(Log, screenShots);
//			            tripgain_BookingPage.enterPassportNumber(Log, screenShots);
//			            tripgain_BookingPage.selectExpireDate(expireYear,expireMonth,expireDay,1);
//			            tripgain_BookingPage.selectIssueDate(issueYear,issueMonth,issueDay,1);
//			            tripgain_BookingPage.selectPassportIssuedCountry(countryCode,Log, screenShots);
//			            tripgain_BookingPage.enterAdultDetailsForInterNational(titles,countryCode,2,birthYear,birthMonth,birthDay,expireYear,expireMonth,expireDay,issueYear,issueMonth,issueDay,Log, screenShots);
			//
//			            int addonsPricePrice = tripgain_BookingPage.selectAddOnsFlow2(Log, screenShots);
//			            System.out.println("Total price after add-ons selection: ₹ " + addonsPricePrice);
//			            String priceText = totalFare.replaceAll("[^0-9]", "");
//			            int farePriceInt = Integer.parseInt(priceText);
//			            Log.ReportEvent("INFO", "Total Add-ons Price: ₹ " + addonsPricePrice);
//			            Log.ReportEvent("INFO", "Base Price: ₹ " + totalFare);
			//
//			            int finalPrice=addonsPricePrice+farePriceInt;
			//
//			            String finalPriceStr = String.valueOf(finalPrice);
//			            String bookingPrice="₹"+" "+finalPriceStr;
			//
//			            Log.ReportEvent("INFO", "Total price after adding add-ons and Base Fare:" + bookingPrice);
			//
//			            String finalBookingPrice=tripgain_BookingPage.getFinalPrice();
//			            tripgain_HomePage.ValidateNumericValuesForFlights(bookingPrice,finalBookingPrice,"Final Fare from Booking",Log, screenShots);
//			            tripgain_BookingPage.clickProceedBookingAndValidateToast(Log, screenShots);

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