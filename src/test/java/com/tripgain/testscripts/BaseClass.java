package com.tripgain.testscripts;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions; // ADD THIS IMPORT
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentTest;
import com.tripgain.common.EmailUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.ScreenShots;
import com.tripgain.common.TestResultTracker;

public class BaseClass{

      
		 WebDriver driver;
		public WebDriver launchBrowser1(String browser,String url)
		{
			if (browser.equalsIgnoreCase("chrome")) {
				// Set the path to the ChromeDriver executable (optional if already set in system PATH)
				
				// ADD THESE 3 LINES FOR CHROME LOGGING
				ChromeOptions options = new ChromeOptions();
				options.setCapability("goog:loggingPrefs", java.util.Map.of("browser", "ALL"));
				driver = new ChromeDriver(options); // CHANGED THIS LINE
				
				// REMOVED THIS LINE: driver = new ChromeDriver();

			} else if (browser.equalsIgnoreCase("firefox")) {
				// Set the path to the GeckoDriver executable (optional if already set in system PATH)
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("edge")) {
				// Set the path to the EdgeDriver executable (optional if already set in system PATH)
				driver = new EdgeDriver();
			} else {
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}
			driver.get(url);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			return driver;
		}
		
		public WebDriver launchBrowser(String browser,String url)
		{
			if (browser.equalsIgnoreCase("chrome")) {
				// Set the path to the ChromeDriver executable (optional if already set in system PATH)
				driver = new ChromeDriver();

			} else if (browser.equalsIgnoreCase("firefox")) {
				// Set the path to the GeckoDriver executable (optional if already set in system PATH)
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("edge")) {
				// Set the path to the EdgeDriver executable (optional if already set in system PATH)
				driver = new EdgeDriver();
			} else {
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}
			driver.get(url);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			return driver;
		}


		@AfterSuite
		public void afterSuite() throws InterruptedException {
			ExtantManager extantManager = new ExtantManager();
			String reportPath = extantManager.getReportFilePath();
			int passed = TestResultTracker.passedTests;
			int failed = TestResultTracker.failedTests;
			int total = passed + failed;

			if (reportPath != null) {
				String toEmail = "harshitha@tripgain.com";
				String[] ccEmails = {
					"harshitha@tripgain.com"
						//	"ranga@tripgain.com","ashutosh@tripgain.com","ramu.achala@tripgain.com","manish@tripgain.com","rajashekar@tripgain.com","arun@tripgain.com"
				};
				EmailUtils.sendReportByEmail(reportPath, toEmail, ccEmails, total, passed, failed);
			} else {
				System.out.println("❌ Report not generated. Skipping email.");
			}
		}


//			@AfterClass
//			public void tearDown()
//			{
//				ExtantManager extantManager=new ExtantManager();
//				extantManager.finalizeExtentReport();
//				extantManager.flushReport();
//				}
	

}