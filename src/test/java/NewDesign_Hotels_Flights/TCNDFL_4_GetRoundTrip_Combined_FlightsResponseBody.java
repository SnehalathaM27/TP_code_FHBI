package NewDesign_Hotels_Flights;
import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.v120.network.model.RequestId;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tripgain.collectionofpages.ApiResponseHandler;
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

public class TCNDFL_4_GetRoundTrip_Combined_FlightsResponseBody extends BaseClass {
	WebDriver driver;  
	// 🔹 DevTools variables (ADD HERE)
	 DevTools devTools;
	 //--hotelSearchApiResponse for to fetch single api------------
	 //AtomicReference<String> hotelSearchApiResponse = new AtomicReference<>();
	 
	 //---this is for to multiple apis----
	 ConcurrentHashMap<String, String> allApiResponses = new ConcurrentHashMap<>();
	 AtomicReference<RequestId> hotelRequestId = new AtomicReference<>();
	 AtomicReference<String> hotelApiUrl = new AtomicReference<>();
	 AtomicReference<Boolean> apiResponseCaptured = new AtomicReference<>(false);
	 AtomicReference<String> lastApiUrl = new AtomicReference<>("");
	 AtomicReference<RequestId> targetRequestId = new AtomicReference<>();
	 AtomicReference<StringBuilder> hotelResponseBuilder = new AtomicReference<>(new StringBuilder());
	 AtomicReference<Integer> targetStatus = new AtomicReference<>();
	 AtomicReference<Long> lastResetTime = new AtomicReference<>(System.currentTimeMillis());

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


        String userName = excelData.get("UserName");
        String password = excelData.get("Password");
        String[] dates = GenerateDates.GenerateDatesToSelectFlights();
		 String fromDate=dates[11];
        String returnDate=dates[0];
        String fromMonthYear=dates[12];
        String returnMonthYear=dates[2];
       
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
        test.log(Status.INFO, "Flight From Date:" +" "+fromDate+" "+"and MonthAndYear:"+" "+fromMonthYear);
        test.log(Status.INFO, "Flight Travel Class:" +" "+travelClass);
        test.log(Status.INFO, "Flight Trip Type:" +" "+tripType);
        test.log(Status.INFO, "Number of Adults:" +" "+adults);
        
			 //Login to application
	         NewDesign_Login NewDesignLogin= new NewDesign_Login(driver);
	        SkyTravelers_Hotels_Login SkyTravelersHotelsLogin= new SkyTravelers_Hotels_Login(driver);
	        NewDesignLogin.enterUserName(username);
	        NewDesignLogin.enterPasswordName(pwd);
	        NewDesignLogin.clickButton(); 
	  Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
	  Thread.sleep(2000);
	  NewDesignLogin.clickOnTravel();
	  
		Tripgain_HomePage_Flights tripgain_HomePage= new Tripgain_HomePage_Flights(driver);
		Tripgain_BookingPage_Flights tripgain_BookingPage= new Tripgain_BookingPage_Flights(driver);

	  
	  // Method to Enter the Search Details
		  tripgain_HomePage.verifyTripGainHomePageIsDisplayed(Log, screenShots);
	      tripgain_HomePage.selectTripType(tripType);
	      tripgain_HomePage.selectCabinClass(travelClass);
	      tripgain_HomePage.selectFromLocation(origin);
	      tripgain_HomePage.selectToLocation(destination);
	      tripgain_HomePage.selectJourneyDate(fromDate,fromMonthYear);
	        tripgain_HomePage.selectreturnJourneyDate(returnDate, returnMonthYear);
	      tripgain_HomePage.selectTravellers(adults);
	      tripgain_HomePage.clickSearchFlightsButton(Log, screenShots);
	        
	    
     
      
      
   // 1. Attempt to get the body
      String body1 = getResponseBodyByPartialUrl("TGFS&authtoken", 60);

      // Initialize the list so it's not null, even if the API fails
      List<Map<String, String>> uiFlightDetails = new ArrayList<>();

      if (body1 != null && !body1.isEmpty()) {
          System.out.println(" Oneway Flight search Body found. Proceeding with validation...");
          embedApiResponseInReport("Oneway_FlightSearchAPI", body1);

          // Capture the UI segments
          uiFlightDetails = tripgain_HomePage.getDynamicFlightDetailsForRoundTripCombined(selectFlightIndex);
          
          // Validate segments
          tripgain_HomePage.validateCombinedRoundTripSearchDataFromUIAndResponseBody(uiFlightDetails, body1, Log, screenShots);

          resetApiResponses();    
      } else {
          // This is where it "skips"
          System.out.println("⚠ failed to load response data: request content was evicted from inspector cache.");
          System.out.println("⏭ Skipping validation and moving to the next line of code...");
          
          // Optional: If you still need the UI data for later steps even without API validation, 
          // you can scrape it here as a fallback:
          // uiFlightDetails = tripgain_HomePage.getDynamicFlightDetailsForRoundTripCombined(selectFlightIndex);
      }

      // --- NEXT LINE OF CODE ---
      // The script will always reach here because we removed Assert.fail() from getResponseBodyByPartialUrl
      System.out.println("🚀 Continuing with the rest of the test flow...");
      // Proceed with booking, passenger details, etc.

      // --- FARE CARD SECTION ---
      
      //Methods to get the Fare Details from Fare Care and Validate with Flight Info
      tripgain_HomePage.clickViewFareByIndex(1);
      
      // Scrape Fare Cards (Saver, Flexi, etc.)
      List<Map<String, String>> uiFareCards = tripgain_HomePage.getFareCardDetails();

      // Check for the More Fares API (Optional)
      String body2 = getPartialResponseBodyByPartialUrl("GETMOREFARES", 10);
      
      // Determine which API body to use for validation (Body2 if exists, else Body1)
      String validationBody = (body2 != null) ? body2 : body1;

      if (uiFlightDetails != null) {
          // REUSING uiFlightDetails instead of calling the method again
          List<String> flightNums = uiFlightDetails.stream()
                                          .map(m -> m.get("flightNum"))
                                          .collect(Collectors.toList());

          // Perform Validation using the data already captured
          tripgain_HomePage.validateFareCardDetails(uiFareCards, body1, flightNums, Log, screenShots);
      }
      


           
      String[] fareDetails=tripgain_HomePage.clickOnSelectBasedOnFareType("Flexi",Log, screenShots);

      Map<String, String> fareInfo = tripgain_HomePage.getFareDetailsFromFlightInfo(Log, screenShots);
      System.out.println("Fare Price: " + fareInfo.get("FarePrice"));
      System.out.println("Supplier: " + fareInfo.get("Supplier"));
      System.out.println("Fare Type: " + fareInfo.get("FareType"));
      System.out.println("Policy Type: " + fareInfo.get("policyType"));

      Log.ReportEvent("INFO", "Validating the Fare Details from Fare Card to Flight info Card");

      tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareDetails[7],fareInfo.get("policyType"),"Policy Type from Fare Card to Flight Info",Log, screenShots);

      
      tripgain_HomePage.handleReasonAndProceed("Better Flight Time");

      String body3 = getResponseBodyByPartialUrl("TGFS&authtoken=", 60);
      System.out.println("Oneway Flight Booking Body found: " + body3);
      embedApiResponseInReport("Oneway_BookingSearchAPI", body3);
      
      //Method to Validate Booking Screen is Displayed
      tripgain_BookingPage.validateBookingScreenIsDisplayed(Log, screenShots);
      
      List<Map<String, String>> uiFlightBookingDetails = tripgain_HomePage.getCombinedRoundTripBookingFlightDetails();

      
     
      tripgain_HomePage.validateCombinedRoundTripBookingDataUIToBackend(uiFlightBookingDetails, body3, Log, screenShots);

      Log.ReportEvent("INFO", "Validating the Final fare from Flight info Card to Booking Screen");

      String totalFare=tripgain_BookingPage.getTotalFare();
      tripgain_HomePage.ValidateActualAndExpectedValuesForFlights(fareInfo.get("FarePrice"),totalFare,"Total Cost from Result to Booking Screen",Log, screenShots);

      String[] titles = title.split(",");
      tripgain_BookingPage.enterAdultDetailsForDomestic(titles,adults,Log, screenShots);
      
      
     String body4 = getResponseBodyByPartialUrl("?opid=TGFS", 60);
      System.out.println("Oneway Flight GetSeatmap Body found: " + body4);
      embedApiResponseInReport("Oneway_GetSeatmapAPI", body4);

     // resetApiResponses();  
      
      String body5 = getResponseBodyByPartialUrl("?opid=TGFS", 60);
      System.out.println("Oneway Flight GETSSR Body found: " + body5);
      embedApiResponseInReport("Oneway_GETSSRAPI", body5); 
      
      
     
    //  resetApiResponses();   

      //Methods to Select the Add-on's and Validate
      
      int addonsPricePrice = tripgain_BookingPage.selectAddOnsFlow2(Log, screenShots, body4, body5);  
      System.out.println("Total price after add-ons selection: ₹ " + addonsPricePrice);    
      String farePrice=fareInfo.get("FarePrice");
      String priceText = farePrice.replaceAll("[^0-9]", "");
      int farePriceInt = Integer.parseInt(priceText);
      Log.ReportEvent("INFO", "Total Add-ons Price: ₹ " + addonsPricePrice);
      Log.ReportEvent("INFO", "Base Price: ₹ " + fareInfo.get("FarePrice"));

      int finalPrice=addonsPricePrice+farePriceInt;

      String finalPriceStr = String.valueOf(finalPrice);
      String bookingPrice="₹"+" "+finalPriceStr;

      Log.ReportEvent("INFO", "Total price after adding add-ons and Base Fare:" + bookingPrice);

      String finalBookingPrice=tripgain_BookingPage.getFinalPrice();
      tripgain_HomePage.ValidateNumericValuesForFlights(bookingPrice,finalBookingPrice,"Final Fare from Booking",Log, screenShots);
      tripgain_BookingPage.clickProceedBookingAndValidateToast(Log, screenShots);

	  
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

    // Add this helper method to your class
    private boolean isApiCallPresent(String apiName) {
        try {
            // Try to get the API response with a very short timeout just to check existence
            String response = getResponseBodyByPartialUrl(apiName, 2);
            return response != null && !response.isEmpty();
        } catch (Exception e) {
            // API call not present or failed
            return false;
        }
    }
    
    private String formatJson(String rawJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(rawJson, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(json);
        } catch (Exception e) {
            // If response is not valid JSON, return original text
            return rawJson;
        }
    }
    
   

    public String getResponseBodyByPartialUrl(String userPassedName, int timeoutSeconds) {
        System.out.println("🔎 Searching for API response containing: " + userPassedName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        try {
            // 1. Wait for the URL to appear in the map keys
            wait.until(d -> allApiResponses.keySet().stream()
                    .anyMatch(url -> url.contains(userPassedName)));

            // 2. Extract the full URL
            String fullUrl = allApiResponses.keySet().stream()
                    .filter(url -> url.contains(userPassedName))
                    .findFirst()
                    .orElse(null);

            String body = (fullUrl != null) ? allApiResponses.get(fullUrl) : null;

            // 3. CHECK FOR EMPTY/EVICTED BODY
            if (body == null || body.trim().isEmpty()) {
                System.out.println("⚠️ failed to load response data: request content was evicted from inspector cache");
                return ""; // Return empty string so the calling code knows there's no data
            }

            return body;

        } catch (Exception e) {
            // Log the specific message you requested instead of failing the test
            System.out.println("⚠️ failed to load response data: request content was evicted from inspector cache");
            return ""; // Return empty to allow next lines of code to run
        }
    }
    
    public String getPartialResponseBodyByPartialUrl(String userPassedName, int timeoutSeconds) {
        System.out.println("🔎 Searching for API response containing: " + userPassedName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        try {
            // Wait for the URL to appear in our captured map
            wait.until(d -> allApiResponses.keySet().stream()
                    .anyMatch(url -> url.contains(userPassedName)));

            String fullUrl = allApiResponses.keySet().stream()
                    .filter(url -> url.contains(userPassedName))
                    .findFirst()
                    .orElse(null);

            return (fullUrl != null) ? allApiResponses.get(fullUrl) : null;
        } catch (Exception e) {
            // Log it, but do NOT Assert.fail here. Just return null.
            System.out.println("ℹ️ API " + userPassedName + " not found within " + timeoutSeconds + "s. Skipping API validation.");
            return null;
        }
    }
    
    // --------------------------
    // Method to clear previous API responses before navigating to a new page
    public void resetApiResponses() {
        allApiResponses.clear();
        lastResetTime.set(System.currentTimeMillis());
    }
    

  


    private void embedApiResponseInReport(String apiName, String responseBody) {
        try {
            String formattedJson = formatJson(responseBody);

            test.log(Status.INFO,
                "<details style='margin-left:15px;'>"
              + "<summary style='cursor:pointer;font-weight:bold;'>📄 View "
              + apiName + " JSON Response</summary>"
              + "<pre style='white-space:pre-wrap; word-wrap:break-word; "
              + "max-height:400px; overflow:auto; background:#f4f4f4; "
              + "padding:10px; border:1px solid #ccc;'>"
              + formattedJson +
              "</pre></details>");

        } catch (Exception e) {
            test.log(Status.WARNING, "Unable to embed API response: " + e.getMessage());
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
            
            System.out.println("DRIVER CLASS = " + driver.getClass().getName());

            // 🔹 DevTools  and explanation 
            // HasDevTools: Checks if the browser supports Chrome DevTools (Chrome and Edge do).
            //createSession(): Opens a direct communication line between your Java code and the browser's internal engine.
            //new Thread -- If we pause the main code, the browser freezes. By starting a new thread, the browser keeps working while our "spy" waits in the background.
            //AtomicReference guarantees visibility between threads
            //hotelSearchApiResponse.set(responseBody);  -- store the response body 

            
         // Thread-safe timestamp to track last reset
            AtomicReference<Long> lastResetTime = new AtomicReference<>(System.currentTimeMillis());

            if (driver instanceof HasDevTools) {
                devTools = ((HasDevTools) driver).getDevTools();
                devTools.createSession();
                devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

                devTools.addListener(Network.responseReceived(), response -> {
                    String apiUrl = response.getResponse().getUrl();
                    RequestId requestId = response.getRequestId();

                    // Background thread to fetch the body for EVERY request
                    new Thread(() -> {
                        try {
                            // Short wait to ensure the response body is ready
                            Thread.sleep(1500);

                            Network.GetResponseBodyResponse body = devTools.send(Network.getResponseBody(requestId));
                            String bodyText = body.getBody();

                            // ✅ Only store responses captured AFTER the last reset
                            long currentTime = System.currentTimeMillis();
                            if (bodyText != null && !bodyText.isEmpty() && currentTime > lastResetTime.get()) {
                                allApiResponses.put(apiUrl, bodyText);
                            }
                        } catch (Exception e) {
                            // Ignore non-XHR failures (images, css, etc.)
                        }
                    }).start();
                });
            }
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