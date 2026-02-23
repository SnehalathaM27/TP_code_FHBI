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




public class Ph_8_Philips_NF_Hotels_Domestic_After7Days extends BaseClass{
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
        int hotelindex = Integer.parseInt(excelData.get("HotelIndex"));
        
        String searchby=excelData.get("SearchBy");
        String searchvalue=excelData.get("SearchValue");
        String remarks=excelData.get("Remarks");
        String status=excelData.get("Status");
        String travellerSearchValue=excelData.get("TravellerSearchValue");
        String grade=excelData.get("Grade");

      //after 7days
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
	        String[] selectedGradeFromDropdown = Implementation_Corporate_Travellers_Page.clickOnSelectGrade(grade, Log);

	        Implementation_Corporate_Travellers_Page.clickOnSaveBtn();
	        Thread.sleep(3000);
	        NewDesign_Emulate_Process.clickOnEmulmateUserOption();
	        NewDesign_Emulate_Process.waitUntilApproverScreenDisplay(Log, screenShots);
	        
		NewDesignLogin.clickOnTravel();

		NewDesign_Hotels_ResultsPage NewDesignHotelsResultsPage=new NewDesign_Hotels_ResultsPage(driver);
		NewDesign_Hotels_DescPage NewDesignHotels_DescPage = new NewDesign_Hotels_DescPage(driver);
		NewDesignHotelsSearchPage NewDesign_HotelsSearchPage = new NewDesignHotelsSearchPage(driver);
		NewDesign_Hotels_BookingPage NewDesign_HotelsBookingPage=new NewDesign_Hotels_BookingPage(driver);
		NewDesign_AwaitingApprovalScreen NewDesign_Awaiting_ApprovalScreen=new NewDesign_AwaitingApprovalScreen(driver);


		NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);
		NewDesign_HotelsSearchPage.clickOnHotels();
		Thread.sleep(3000);
		NewDesign_HotelsSearchPage.enterDestinationForHotels("Delhi", Log);
		Thread.sleep(3000);
		NewDesign_HotelsSearchPage.selectDate(fromDate, fromMonthYear, Log);
		NewDesign_HotelsSearchPage.selectReturnDate(returnDate, returnMonthYear, Log);
	//	NewDesign_HotelsSearchPage.fillRoomDetails("3", "2,3,1", "1,2,0", "5;7,8;", Log);
		NewDesign_HotelsSearchPage.clickOnSearchHotelBut();
		Thread.sleep(8000);
		
		  String[] checkindateResultPage = NewDesignHotelsResultsPage.getCheckInDateTextFromResultPage();
			String[] checkoutdateResultPage =NewDesignHotelsResultsPage.getCheckOutDateTextFromResultPage();
		   
			Implementation_Corporate_Travellers_Page.validateHotelPolicyBasedOnGradeOrPriceForDomestic(selectedGradeFromDropdown, checkindateResultPage, Log, screenShots);
		   
			//get all the hotel details from the selected hotel card
			String[] hotelDetails = NewDesignHotelsResultsPage.selectHotelAndGetDetails(2, Log);
			
			//get all the details from desc page 
			
			String[] PolicyFromDesc = NewDesignHotels_DescPage.getPolicyFromDescPg();
			
			//validations b/w desc to result pages 
			NewDesignHotels_DescPage.validateHotelPolicyFromDescToResultPage(PolicyFromDesc, hotelDetails, Log, screenShots);
			
			
			String[] selectRooms = NewDesignHotels_DescPage.selectRoomsFromDescPg();
			
			Thread.sleep(3000);
			String[] BookingPgPolicy = NewDesign_HotelsBookingPage.getPolicyTextFromBookingPg();
			
			NewDesign_HotelsBookingPage.validatePolicyTextFromDescToBookingPage(PolicyFromDesc, BookingPgPolicy, Log, screenShots);
			//NewDesign_HotelsBookingPage.addTravellerDetails();
			NewDesign_HotelsBookingPage.enterLastNameForHotels("traveller");

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