package com.tripgain.testscripts.Expenses;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
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

public class TCE_1_VerifyExpensesModule extends BaseClass{
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

        
     String currencyValue = excelData.get("CurrencyValue");
          String merchantName = excelData.get("MerchantName");
          String notes = excelData.get("Notes");
          String paymentType = excelData.get("PaymentType");

       
        
        // Login to TripGain Application
        Tripgain_Login tripgainLogin= new Tripgain_Login(driver);
SiteChecker Site_Checker=new SiteChecker(driver);
		
        Site_Checker.waitForSiteToBeUp(driver, "https://v3.tripgain.com/flights", 20, 120);
        		
        
        Thread.sleep(3000);
        tripgainLogin.enterUserName(username);
        tripgainLogin.enterPasswordName(pwd);
        tripgainLogin.clickButton(); 
		Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
		Thread.sleep(2000);

        
        //Functions to Search on Home Page     
    
        Tripgain_RoundTripResultsScreen trs=new Tripgain_RoundTripResultsScreen(driver);

        trs.printVersion(Log);
        Thread.sleep(2000);
        
        ExpensesModule Expenses_Module=new ExpensesModule(driver);
        Expenses_Module.clickExpenseButton();
        Expenses_Module.clickAddExpenseButton();
        Thread.sleep(2000);
        Expenses_Module.addTripNumber(Log, screenShots, "Test Expense (nontravel)");
        Thread.sleep(2000);
        Expenses_Module.clickOnSelectCategoryValues(Log, screenShots);
        Thread.sleep(2000);
        Expenses_Module.selectDate("3", "July 2025");
        Thread.sleep(2000);
        Expenses_Module.clickOnCurrencyDropDown();
        Expenses_Module.selectCurrencyDropDownValues(driver, currencyValue, Log);
        Thread.sleep(2000);
        Expenses_Module.enterNotes(notes, Log, screenShots);
        Thread.sleep(2000);
        Expenses_Module.merchantName(merchantName, Log, screenShots);
        Thread.sleep(2000);
        Expenses_Module.selectPaymentMode(paymentType, Log, screenShots);
        Thread.sleep(2000);
        Expenses_Module.uploadReceiptUnderExpense("C:\\Users\\LENOVO\\Downloads\\Screenshot 2025-07-28 182232.pdf");
        
       //Function to Logout from Application
    		//tripgainhomepage.logOutFromApplication(Log, screenShots);
    	//	driver.quit();
         
       
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