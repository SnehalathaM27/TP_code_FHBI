package TestScripts_Oneway_RoundTrip;
import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tripgain.collectionofpages.Tripgain_Login;
import com.tripgain.collectionofpages.Tripgain_RoundTripResultsScreen;
import com.tripgain.collectionofpages.Tripgain_homepage;
import com.tripgain.collectionofpages.Tripgain_resultspage;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.common.getDataFromExcel;
import com.tripgain.testscripts.BaseClass;
import com.tripgain.common.DataProviderUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.GenerateDates;
import com.tripgain.common.Getdata;

@Listeners(com.tripgain.common.TestListener.class)
public class TC_06 extends BaseClass{
	  WebDriver driver;    
	    ExtentReports extent;
	    ExtentTest test;
	    String className = "";
	    Log Log;  // Declare Log object
	    ScreenShots screenShots;  // Declare Log object
	    ExtantManager extantManager;
	  

	    private WebDriverWait wait;
	    
	    //ThreadLocal to store Excel data per test thread
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
        
        String[] dates=GenerateDates.GenerateDatesToSelectFlights();
        String fromDate=dates[0];
        String fromMonthYear=dates[2];
        String returnDate=dates[1]; 
        String returnMonthYear=dates[3];
        
        
       // Map<String, String> excelData = getDataFromExcel.getExcelData("TC_06");
        String origin = excelData.get("Origin");
        String destination = excelData.get("Destination");
        String fromDates = excelData.get("FromDate");
        String fromMonthYears = excelData.get("FromMonthYear");
        
        String travelClass = excelData.get("Class");
        int adults = Integer.parseInt(excelData.get("Adults"));
        int selectflightbasedindex = Integer.parseInt(excelData.get("SelectFlightBasedIndex"));


        // Login to TripGain Application
        Tripgain_Login tripgainLogin= new Tripgain_Login(driver);
        tripgainLogin.enterUserName(username);
        tripgainLogin.enterPasswordName(pwd);
        tripgainLogin.clickButton(); 
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		Thread.sleep(2000);
		screenShots.takeScreenShot1();

        
		  //Functions to Search on Home Page     
        Tripgain_homepage tripgainhomepage = new Tripgain_homepage(driver);
        Tripgain_resultspage tripgainresultspage=new Tripgain_resultspage(driver);
        Tripgain_RoundTripResultsScreen trs=new Tripgain_RoundTripResultsScreen(driver);
        Thread.sleep(3000); 

        tripgainhomepage.searchFlightsOnHomePage(origin, destination,  fromDates, fromMonthYears, travelClass , adults, Log, screenShots);
       
        Thread.sleep(3000); 
        wait = new WebDriverWait(driver,Duration.ofSeconds(20));

        

        Thread.sleep(5000); 
        tripgainresultspage.validateFlightsResults(Log, screenShots);
        Thread.sleep(3000); 
      //Function to validate Check In Baggage Functionality on Result Screen
      		tripgainresultspage.clickOnCheckInBaggage();
            Thread.sleep(3000); 
      		tripgainresultspage.selectFlightBasedOnIndex(selectflightbasedindex);
            Thread.sleep(3000); 
      		tripgainresultspage.validateCheckInBaggageFlightsOnResultScreen(Log, screenShots);
      		
       
  		
  		//tripgainhomepage.logOutFromApplication(Log, screenShots);
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

