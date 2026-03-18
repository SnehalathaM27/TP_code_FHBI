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
import com.tripgain.common.SimpleLogAnalyzer;  // ADD THIS IMPORT
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

public class TCNDBUNEW_3 extends BaseClass{
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
            String dest = excelData.get("Destination");
            
     
            String[] dates=GenerateDates.GenerateDatesToSelectFlights();
            String fromDate=dates[0];
            String fromMonthYear=dates[2];
            String returnDate=dates[1]; 
            String returnMonthYear=dates[3];
	
            
            //Login to application
            NewDesign_Login NewDesignLogin= new NewDesign_Login(driver);
            
            NewDesignLogin.enterUserName(username);
            NewDesignLogin.enterPasswordName(pwd);
            NewDesignLogin.clickButton(); 
            
      
        
            
            NewDesignLogin.clickOnTravel();
            
            NewDesign_Buses_SerachPage NewDesignBusesSerachPage=new NewDesign_Buses_SerachPage(driver);
            NewDesign_Buses_ResultsPage NewDesignBusesResultsPage = new NewDesign_Buses_ResultsPage(driver);
            NewDesign_Buses_BookingPage NewDesignBusesBookingPage=new NewDesign_Buses_BookingPage(driver);
            NewDesignHotelsSearchPage NewDesign_HotelsSearchPage = new NewDesignHotelsSearchPage(driver);

            NewDesignBusesSerachPage.clickOnBuses();
            Thread.sleep(3000);
            NewDesignBusesSerachPage.enterfromLocForBuses(origin, Log);
            Thread.sleep(2000);
            NewDesignBusesSerachPage.enterToLocForBuses(dest, Log);
			NewDesign_HotelsSearchPage.selectBusJourneyDate(fromDate, fromMonthYear);
            NewDesignBusesSerachPage.SearchBus(Log, screenShots);
            Thread.sleep(3000);
                       
            String[] Busdetails = NewDesignBusesResultsPage.getBusDetailsFromListingByIndex(0);
           

            
            NewDesignBusesResultsPage.clickUpperBerth(Log, screenShots);
            String[] BoardingDetails = NewDesignBusesResultsPage.selectBoardingPoints(Log, screenShots);
            String[] DroppingPoint = NewDesignBusesResultsPage.selectDroppingPoint(Log, screenShots);
            Thread.sleep(1000);
            
           
            NewDesignBusesResultsPage.clcikOnConfirmSeat();
            Thread.sleep(2000);
            
                      
            NewDesignBusesResultsPage.reasonForSelectionPopUp();
            
            NewDesignBusesBookingPage.addPassengerDetails();
            
               NewDesignBusesBookingPage.clickSendForApproval(Log, screenShots);
            
            
        } catch (Exception e) {
            // Even on exception, check console
            Log.checkConsoleErrorsWithFormat("After Exception");
            throw e;
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

        // Use your existing launchBrowser method
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
	}}