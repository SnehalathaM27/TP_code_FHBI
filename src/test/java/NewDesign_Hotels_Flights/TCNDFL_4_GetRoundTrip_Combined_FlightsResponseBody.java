package NewDesign_Hotels_Flights;
import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
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
			Tripgain_HomePage_Flights tripgain_HomePage= new Tripgain_HomePage_Flights(driver);

	        NewDesignLogin.enterUserName(username);
	        NewDesignLogin.enterPasswordName(pwd);
	        NewDesignLogin.clickButton(); 
	  Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
	  Thread.sleep(2000);
		tripgain_HomePage.clickIfPresentCloseBtn();

	  NewDesignLogin.clickOnTravel();
	  
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
          System.out.println(" Roundtrip Flight search Body found."+body1);
          embedApiResponseInReport("Oneway_FlightSearchAPI", body1);

          // Capture the UI segments
          uiFlightDetails = tripgain_HomePage.getDynamicFlightDetailsForRoundTripCombined(selectFlightIndex);
          
          tripgain_HomePage.validateCombinedRoundTripSearchDataFromUIAndResponseBody(uiFlightDetails, body1, Log, screenShots);

          resetApiResponses();    
      } else {
          System.out.println(" failed to load response data: request content was evicted from inspector cache.");
          
      }      
      
      // --- FARE CARD SECTION ---
      
      tripgain_HomePage.clickViewFareByIndex(selectFlightIndex);
      Thread.sleep(3000); 

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
          resetApiResponses(); 

      }
       
       String[] fareDetails = tripgain_HomePage.clickOnSelectBasedOnFareType(fareType, Log, screenShots);
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

      tripgain_HomePage.handleReasonAndProceed(reason);
      Thread.sleep(1000);
      
   /*  String body3 = getResponseBodyByPartialUrl("opid=TGFS&authtoken", 60);
      System.out.println("Oneway Flight Booking Body found: " + body3);
      embedApiResponseInReport("Oneway_BookingSearchAPI", body3); */
      
      String urlIdentifier = "opid=TGFS&authtoken";
      Map<String, String> markers = new HashMap<>();
      markers.put("Booking", "ValidateSearchResponse");
      markers.put("SSR", "CarrierSSR");
      markers.put("SeatMap", "Status");
      
   // --- 3. FETCH AND CATEGORIZE (Dynamically) ---
      Map<String, String> responses = getCategorizedResponses(urlIdentifier, markers, 60);

      String bodyBooking = responses.getOrDefault("Booking", "");
      String bodySeatMap = responses.getOrDefault("SeatMap", "");
      String bodySSR = responses.getOrDefault("SSR", "");
      
      if (!bodyBooking.isEmpty()) {
    	    System.out.println(" Processing Booking Body."+bodyBooking);
    	    embedApiResponseInReport("Oneway_FlightValidateSearchAPI", bodyBooking);
    	    
    	    tripgain_BookingPage.validateBookingScreenIsDisplayed(Log, screenShots);
    	      List<Map<String, String>> uiFlightBookingDetails = tripgain_HomePage.getCombinedRoundTripBookingFlightDetails();
    	      tripgain_HomePage.validateCombinedRoundTripSearchDataFromUIAndResponseBody(uiFlightBookingDetails, bodyBooking, Log, screenShots);
    	} else {
    	    Assert.fail("❌ Booking Search API Response not found.");
    	}

    	if (!bodySeatMap.isEmpty()) {
    	    System.out.println(" Processing SeatMap Body." +bodySeatMap);
    	    embedApiResponseInReport("Oneway_GetSeatmapAPI", bodySeatMap);
    	}

    	if (!bodySSR.isEmpty()) {
    	    System.out.println(" Processing SSR Body."+bodySSR);
    	    embedApiResponseInReport("Oneway_GETSSRAPI", bodySSR); 
    	} 
      
  

      //Methods to Select the Add-on's and Validate
      
    	int addonsPricePrice = tripgain_BookingPage.selectAddOnsFlowForRTCombinedFts(Log, screenShots, bodySeatMap, bodySSR);      System.out.println("Total price after add-ons selection: ₹ " + addonsPricePrice);    
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

//	  
		// Function to Logout from Application
		// tripgainhomepage.logOutFromApplication(Log, screenShots);
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
 // 1. Method to fetch ALL responses matching the URL pattern
  /*  public List<String> getAllResponseBodiesByUrl(String userPassedName, int timeoutSeconds) {
        System.out.println("🔎 Searching for all API responses containing: " + userPassedName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        try {
            // Wait until at least one response is captured
            wait.until(d -> !allApiResponses.isEmpty());

            // Filter all matching URLs and return their bodies
            return allApiResponses.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(userPassedName))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("⚠️ Error fetching APIs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    */
    
     
   

//    public String getResponseBodyByPartialUrl(String userPassedName, int timeoutSeconds) {
//        System.out.println("🔎 Searching for API response containing: " + userPassedName);
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
//
//        try {
//            // Wait until an entry in our map contains the user's string in its URL key
//            wait.until(d -> allApiResponses.keySet().stream()
//                    .anyMatch(url -> url.contains(userPassedName)));
//
//            // Find the full URL key that matches the user's string
//            String fullUrl = allApiResponses.keySet().stream()
//                    .filter(url -> url.contains(userPassedName))
//                    .findFirst()
//                    .get();
//
//            return allApiResponses.get(fullUrl);
//        } catch (Exception e) {
//            Assert.fail("❌ Could not find any captured API response containing: " + userPassedName);
//            return null;
//        }
//    }
    
    public String getResponseBodyByPartialUrl(String userPassedName, int timeoutSeconds) throws TimeoutException {
        System.out.println("🔎 Searching for API response containing: " + userPassedName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        try {
            // 1. Wait for the URL to appear in the captured keys
            wait.until(d -> allApiResponses.keySet().stream()
                    .anyMatch(url -> url.contains(userPassedName)));

            // 2. Find the full URL key
            String fullUrl = allApiResponses.keySet().stream()
                    .filter(url -> url.contains(userPassedName))
                    .findFirst()
                    .orElse(null);

            if (fullUrl != null) {
                String body = allApiResponses.get(fullUrl);
                
                // 3. CHECK IF BODY IS EMPTY (Evicted or missing)
                if (body == null || body.trim().isEmpty()) {
                    System.out.println("⚠️ failed to load response data: request content was evicted from inspector cache");
                    return ""; // Return empty string to signal "nothing to validate"
                }
                return body;
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error during API capture: " + e.getMessage());
        }

        // 4. LOG THE SPECIFIC EVICTION MESSAGE AND CONTINUE
        System.out.println("⚠️ failed to load response data: request content was evicted from inspector cache");
        return ""; // Return empty instead of Assert.fail to allow code to continue
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
    
    /**
     * Fetches all responses for a URL pattern and categorizes them based on unique content markers.
     */
    public Map<String, String> getCategorizedResponses(String urlIdentifier, Map<String, String> contentMarkers, int waitSeconds) {
        System.out.println("🔎 Waiting " + waitSeconds + " seconds to capture responses for: " + urlIdentifier);
        
        try {
            // Wait for asynchronous calls to complete
            Thread.sleep(waitSeconds * 1000); 

            // Filter all matching URLs from the map
            Map<String, String> filteredResponses = allApiResponses.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(urlIdentifier))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            System.out.println("Total responses captured for " + urlIdentifier + ": " + filteredResponses.size());

            Map<String, String> categorizedBodies = new ConcurrentHashMap<>();

            // Categorize based on content markers
            for (String responseBody : filteredResponses.values()) {
                for (Map.Entry<String, String> marker : contentMarkers.entrySet()) {
                    if (responseBody.contains(marker.getValue())) {
                        categorizedBodies.put(marker.getKey(), responseBody);
                        System.out.println("🎯 Identified Response: " + marker.getKey());
                    }
                }
            }
            return categorizedBodies;

        } catch (Exception e) {
            System.out.println("⚠️ Error fetching APIs: " + e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }
    
 


//    private void embedApiResponseInReport(String apiName, String responseBody) {
//        try {
//            String formattedJson = formatJson(responseBody);
//
//            test.log(Status.INFO,
//                "<details style='margin-left:15px;'>"
//              + "<summary style='cursor:pointer;font-weight:bold;'>📄 View "
//              + apiName + " JSON Response</summary>"
//              + "<pre style='white-space:pre-wrap; word-wrap:break-word; "
//              + "max-height:400px; overflow:auto; background:#f4f4f4; "
//              + "padding:10px; border:1px solid #ccc;'>"
//              + formattedJson +
//              "</pre></details>");
//
//        } catch (Exception e) {
//            test.log(Status.WARNING, "Unable to embed API response: " + e.getMessage());
//        }
//    }

    private void embedApiResponseInReport(String apiName, String responseBody) {
        try {
            String formattedJson = formatJson(responseBody);
            // We use a unique ID to ensure the script copies the correct text if there are multiple logs
            String uniqueId = "copy_" + System.currentTimeMillis();

            test.log(Status.INFO,
                "<details style='margin-left:15px; border:1px solid #ddd; padding:5px; border-radius:4px;'>"
              + "<summary style='cursor:pointer; font-weight:bold;'>📄 View " + apiName + " JSON Response</summary>"
              + "<div style='position:relative; margin-top:10px;'>"
              + "<button onclick=\"copyToClipboard('" + uniqueId + "')\" style='position:absolute; right:10px; top:10px; z-index:1; cursor:pointer; padding:5px 10px; background:#007bff; color:white; border:none; border-radius:3px; font-size:12px;'>Copy</button>"
              + "<pre id='" + uniqueId + "' style='white-space:pre-wrap; word-wrap:break-word; "
              + "max-height:400px; overflow:auto; background:#f4f4f4; "
              + "padding:10px; border:1px solid #ccc; font-family:monospace;'>"
              + formattedJson + "</pre>"
              + "</div>"
              + "<script>"
              + "function copyToClipboard(elementId) {"
              + "  var text = document.getElementById(elementId).innerText;"
              + "  var elem = document.createElement('textarea');"
              + "  document.body.appendChild(elem);"
              + "  elem.value = text;"
              + "  elem.select();"
              + "  document.execCommand('copy');"
              + "  document.body.removeChild(elem);"
              + "  alert('Response copied to clipboard!');"
              + "}"
              + "</script>"
              + "</details>");

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

    driver = launchBrowser1(browser, url);
    
    System.out.println("DRIVER CLASS = " + driver.getClass().getName());

    // 🔹 DevTools  and explanation 
    // HasDevTools: Checks if the browser supports Chrome DevTools (Chrome and Edge do).
    //createSession(): Opens a direct communication line between your Java code and the browser's internal engine.
    //new Thread -- If we pause the main code, the browser freezes. By starting a new thread, the browser keeps working while our "spy" waits in the background.
    //AtomicReference guarantees visibility between threads
    //hotelSearchApiResponse.set(responseBody);  -- store the response body 

    
 // Thread-safe timestamp to track last reset
    AtomicReference<Long> lastResetTime = new AtomicReference<>(System.currentTimeMillis());

//    if (driver instanceof HasDevTools) {
//        devTools = ((HasDevTools) driver).getDevTools();
//        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
//
//        devTools.addListener(Network.responseReceived(), response -> {
//            String apiUrl = response.getResponse().getUrl();
//            RequestId requestId = response.getRequestId();
//
//            // Background thread to fetch the body for EVERY request
//            new Thread(() -> {
//                try {
//                    // Short wait to ensure the response body is ready
//                    Thread.sleep(1500);
//
//                    Network.GetResponseBodyResponse body = devTools.send(Network.getResponseBody(requestId));
//                    String bodyText = body.getBody();
//
//                    // ✅ Only store responses captured AFTER the last reset
//                    long currentTime = System.currentTimeMillis();
//                    if (bodyText != null && !bodyText.isEmpty() && currentTime > lastResetTime.get()) {
//                        allApiResponses.put(apiUrl, bodyText);
//                    }
//                } catch (Exception e) {
//                    // Ignore non-XHR failures (images, css, etc.)
//                }
//            }).start();
//        });
//    }
    
 // --- UPDATE THIS PART INSIDE launchApplication ---
    if (driver instanceof HasDevTools) {
        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.responseReceived(), response -> {
            String apiUrl = response.getResponse().getUrl();
            RequestId requestId = response.getRequestId();

            // Background thread to fetch the body
            new Thread(() -> {
                try {
                    // Short wait to ensure the response body is ready
                    Thread.sleep(1000);

                    Network.GetResponseBodyResponse body = devTools.send(Network.getResponseBody(requestId));
                    String bodyText = body.getBody();

                    // ✅ Store the response if it matches the URL pattern
                    if (apiUrl.contains("opid=TGFS") && bodyText != null && !bodyText.isEmpty()) {
                        // Use a unique key (URL + Timestamp) to prevent overwriting
                        allApiResponses.put(apiUrl + "_" + System.currentTimeMillis(), bodyText);
                        System.out.println("✅ Captured TGFS API: " + apiUrl);
                    }
                } catch (Exception e) {
                    // Ignore failures (images, css, etc.)
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
     //driver.quit();
     extantManager.flushReport();
    }
    }
    }




