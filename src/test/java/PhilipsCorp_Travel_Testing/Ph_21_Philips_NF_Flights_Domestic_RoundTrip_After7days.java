package PhilipsCorp_Travel_Testing;

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
import com.tripgain.collectionofpages.Implementation_Corporate_TravellersPage;
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
import com.tripgain.collectionofpages.Tripgain_BookingPage_Flights;
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
import java.util.concurrent.atomic.AtomicReference;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.HasDevTools;
import java.util.Date;
import org.openqa.selenium.devtools.v120.network.model.Response;
import org.openqa.selenium.devtools.v120.network.model.ResourceType;
import org.testng.annotations.Test;
import org.openqa.selenium.devtools.v120.network.model.RequestId;




public class Ph_21_Philips_NF_Flights_Domestic_RoundTrip_After7days extends BaseClass{
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
        String destination = excelData.get("Destination");
        String travelClass = excelData.get("Class");
        String tripType = excelData.get("TripType");
        String title=excelData.get("Title");

        int adults = (int) Double.parseDouble(excelData.get("Adults"));
        int selectFlightIndex = (int) Double.parseDouble(excelData.get("SelectFlightIndex"));
        String fareType = excelData.get("FareType");
        String reason = excelData.get("Reason");

        test.log(Status.INFO, "Flight Sector Origin:" +" "+origin+" "+"and Destination:"+" "+destination);
        test.log(Status.INFO, "Flight Travel Class:" +" "+travelClass);
        test.log(Status.INFO, "Flight Trip Type:" +" "+tripType);
        test.log(Status.INFO, "Number of Adults:" +" "+adults);

    
       
     
        String[] dates = GenerateDates.GenerateDatesToSelectFlights();
		 String fromDate=dates[16];  //8th day from today
        String fromMonthYear=dates[17];

        String returnDate=dates[0];
        String returnMonthYear=dates[2];
	
	        //Login to application
	        NewDesign_Login NewDesignLogin= new NewDesign_Login(driver);
	        
        SkyTravelers_Hotels_Login SkyTravelersHotelsLogin= new SkyTravelers_Hotels_Login(driver);
        NewDesignLogin.enterUserName("admin@Philipscorp.com");
		NewDesignLogin.enterPasswordName("EMUL-20260212-TGIW$99826@");
		NewDesignLogin.clickButton();
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		Thread.sleep(2000);
		
		
		
		NewDesign_EmulateProcess NewDesign_Emulate_Process=new NewDesign_EmulateProcess(driver);
		Implementation_Corporate_TravellersPage Implementation_Corporate_Travellers_Page=new Implementation_Corporate_TravellersPage(driver);

		
		 NewDesign_Emulate_Process.clcikOnAdmin();
	        NewDesign_Emulate_Process.clickOnSearchByThroughUser("Email");
	        NewDesign_Emulate_Process.clickSearchValueThroughUser("traveler@philips");
	        NewDesign_Emulate_Process.clcikOnCorpTravellerSearchButton();
	        
	        Implementation_Corporate_Travellers_Page.clickOnViewDetailBtnInCorpTravellers("traveler@philips");
	        Implementation_Corporate_Travellers_Page.clcikOnEditBtn();
	        
	        //select grade ----------------------
	        String[] selectedGradeFromDropdown = Implementation_Corporate_Travellers_Page.clickOnSelectGrade("C-5 - ", Log);

	        Implementation_Corporate_Travellers_Page.clickOnSaveBtn();
	        Thread.sleep(3000);
	        NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	        
		NewDesignLogin.clickOnTravel();

		Tripgain_HomePage_Flights tripgain_HomePage= new Tripgain_HomePage_Flights(driver);
		Tripgain_BookingPage_Flights tripgain_BookingPage= new Tripgain_BookingPage_Flights(driver);


		NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);
		
      
		
		 // Method to Enter the Search Details
		 // Method to Enter the Search Details
        tripgain_HomePage.verifyTripGainHomePageIsDisplayed(Log, screenShots);
        tripgain_HomePage.selectTripType(tripType);
        tripgain_HomePage.selectCabinClass(travelClass);
        tripgain_HomePage.selectFromLocation(origin);
        tripgain_HomePage.selectDestLocation(destination);
        tripgain_HomePage.selectJourneyDate(fromDate,fromMonthYear);
        tripgain_HomePage.selectreturnJourneyDate(returnDate, returnMonthYear);
        
        tripgain_HomePage.selectTravellers(adults);
        tripgain_HomePage.clickSearchFlightsButton(Log, screenShots);
         Thread.sleep(3000);
        String[] OnwardDateText = tripgain_HomePage.getOnwardDateTextFromResultPage();
        String[] ReturnDateText = tripgain_HomePage.getReturnDateTextFromResultPage();
        
        
        tripgain_HomePage.getAllPolicyTextsFromFlightCards();
        
        Implementation_Corporate_Travellers_Page.validateFlightPolicyAgainstDaysForDomestic(OnwardDateText, Log, screenShots);
        //Methods to Select Flight get the Flight Date form Result Screen
        tripgain_HomePage.clickViewFlightDetailsByIndex(selectFlightIndex); 
        String flightcardPolicyText = tripgain_HomePage.getFlightCardPolicyByIndex(selectFlightIndex);
        
        Map<String, List<String>> flightDataFromResultScreen = tripgain_HomePage.getDataFromUiForResultScreenByXPath();
        List<String> flightCarrierDetails = tripgain_HomePage.getFlightDetailsCombined();
        // ✅ Use the data directly
        for (String detail : flightCarrierDetails) {
            System.out.println("From test: " + detail);
            Log.ReportEvent("INFO", "Flight Name and Number:" + detail);          }


        //Methods to get the Fare Details from Fare Care and Validate with Flight Info
        tripgain_HomePage.clickViewFareByIndex(selectFlightIndex);
        
        Implementation_Corporate_Travellers_Page.getAllPolicyTextsFromFlightFareCards(driver);

    //    Implementation_Corporate_Travellers_Page.validateSelectedFlightCardPolicyAgainstFareCards(selectFlightIndex, Log, screenShots);
        String[] fareDetails=tripgain_HomePage.clickOnSelectBasedOnFareType(fareType,Log, screenShots);

        Map<String, String> fareInfo = tripgain_HomePage.getFareDetailsFromFlightInfo(Log, screenShots);
        System.out.println("Fare Price: " + fareInfo.get("FarePrice"));
        System.out.println("Supplier: " + fareInfo.get("Supplier"));
        System.out.println("Fare Type: " + fareInfo.get("FareType"));
        System.out.println("Policy Type: " + fareInfo.get("policyType"));

        Log.ReportEvent("INFO", "Validating the Fare Details from Fare Card to Flight info Card");

              tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[7],fareInfo.get("policyType"),"Policy Type from Fare Card to Flight Info",Log, screenShots);

        tripgain_HomePage.clickOnNextButton();
        tripgain_HomePage.getAllPolicyTextsFromFlightCards();
        Implementation_Corporate_Travellers_Page.validateFlightPolicyAgainstDaysForDomestic(OnwardDateText, Log, screenShots);
        tripgain_HomePage.clickViewFlightDetailsByIndex(selectFlightIndex); 
        String returnflightcardPolicyText = tripgain_HomePage.getFlightCardPolicyByIndex(selectFlightIndex);
        tripgain_HomePage.clickViewFareByIndex(selectFlightIndex);
        
        Implementation_Corporate_Travellers_Page.getAllPolicyTextsFromFlightFareCards(driver);

      //  Implementation_Corporate_Travellers_Page.validateSelectedFlightCardPolicyAgainstFareCards(selectFlightIndex, Log, screenShots);
             
        String[] retrunfareDetails=tripgain_HomePage.clickOnSelectBasedOnFareType(fareType,Log, screenShots);
        Map<String, String> returnfareInfo = tripgain_HomePage.getFareDetailsFromFlightInfo(Log, screenShots);
        
        tripgain_HomePage.handleReasonAndProceed(reason);

        //Method to Validate Booking Screen is Displayed
        tripgain_BookingPage.validateBookingScreenIsDisplayed(Log, screenShots);

        //Methods to get Flight data from Booking Screen and Validate
        Map<String, List<String>> bookingData = tripgain_BookingPage.clickIfPresentAndGetBookingData();

        Log.ReportEvent("INFO", "Validating the Flight Details from Result to Booking Screen");
     //   tripgain_BookingPage.validateSearchAndBookingDataByXPath(flightDataFromResultScreen,bookingData,Log, screenShots);

        Map<String, String> flightDetails=tripgain_BookingPage.getDataFromBookingPage();
        
        System.out.println("Supplier: " + flightDetails.get("Supplier"));
        System.out.println("Fare Type: " + flightDetails.get("FareName"));
        System.out.println("Policy Type: " + flightDetails.get("PolicyType"));
        String fareName=fareInfo.get("FareType");
        String actualFareName=fareName +" "+"Fare";

        Log.ReportEvent("INFO", "Validating the Fare Details from Flight info Card to Booking Screen");

        tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(flightDetails.get("PolicyType"),fareInfo.get("policyType"),"Policy Type from Flight Info and Booking Screen",Log, screenShots);

        
        
        Map<String, String> returnflightDetails=tripgain_BookingPage.getDataFromBookingPageForReturnFlights();
        System.out.println("Supplier: " + flightDetails.get("Supplier"));
        System.out.println("Policy Type: " + flightDetails.get("PolicyType"));

        Log.ReportEvent("INFO", "Validating the Fare Details from Return Flight info Card to Booking Screen");

        tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(returnflightDetails.get("PolicyType"),fareInfo.get("policyType"),"Policy Type from Flight Info and Booking Screen",Log, screenShots);

        
        
        
       
        String[] titles = title.split(",");
        tripgain_BookingPage.enterAdultDetailsForDomestic(titles,adults,Log, screenShots);

        //Methods to Select the Add-on's and Validate
        int addonsPricePrice = tripgain_BookingPage.selectAddOnsNormalFlow(Log, screenShots);
        System.out.println("Total price after add-ons selection: ₹ " + addonsPricePrice);
        String farePrice=fareInfo.get("FarePrice");
        String priceText = farePrice.replaceAll("[^0-9]", "");
        int farePriceInt = Integer.parseInt(priceText);
        Log.ReportEvent("INFO", "Total Add-ons Price: ₹ " + addonsPricePrice);
        Log.ReportEvent("INFO", "Base Price: ₹ " + fareInfo.get("FarePrice"));

        int finalPrice=addonsPricePrice+farePriceInt;

        String finalPriceStr = String.valueOf(finalPrice);
        String bookingPrice="₹"+" "+finalPriceStr;
        
        


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