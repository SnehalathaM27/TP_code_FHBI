package NewDesign_Hotels_Flights;
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import com.tripgain.collectionofpages.NewDesignHotelsSearchPage;
import com.tripgain.collectionofpages.NewDesign_AwaitingApprovalScreen;
import com.tripgain.collectionofpages.NewDesign_EmulateProcess;
import com.tripgain.collectionofpages.NewDesign_Hotels_BookingPage;
import com.tripgain.collectionofpages.NewDesign_Hotels_DescPage;
import com.tripgain.collectionofpages.NewDesign_Hotels_ResultsPage;
import com.tripgain.collectionofpages.NewDesign_Login;
import com.tripgain.collectionofpages.NewDesign_Trips;
import com.tripgain.collectionofpages.NewDesign_getNetworkResponsesData;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.HasDevTools;
import java.util.ArrayList;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.openqa.selenium.devtools.v120.network.model.Response;
import org.openqa.selenium.devtools.v120.network.model.ResourceType;
import org.testng.annotations.Test;
import org.openqa.selenium.devtools.v120.network.model.RequestId;


public class TCND_2_GetHotelResponseBody extends BaseClass{
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
  
        String origin = excelData.get("Origin");
        System.out.println(origin);
        int hotelindex = Integer.parseInt(excelData.get("HotelIndex"));
        
        String searchby=excelData.get("SearchBy");
        String searchvalue=excelData.get("SearchValue");
        String remarks=excelData.get("Remarks");
        String status=excelData.get("Status");
        String travellerSearchValue=excelData.get("TravellerSearchValue");
        String travellerSearchValue2=excelData.get("TravellerSearchValue2");
        String status2=excelData.get("Status2");

  String[] dates=GenerateDates.GenerateDatesToSelectFlights();
  String fromDate = dates[0];
  String fromMonthYear = dates[2];
  String returnDate = dates[1];
  String returnMonthYear = dates[3];

         //Login to application
         NewDesign_Login NewDesignLogin= new NewDesign_Login(driver);
        SkyTravelers_Hotels_Login SkyTravelersHotelsLogin= new SkyTravelers_Hotels_Login(driver);
        NewDesignLogin.enterUserName(username);
        NewDesignLogin.enterPasswordName(pwd);
        NewDesignLogin.clickButton(); 
  Log.ReportEvent("PASS", "Enter UserName and Password is Successful");
  Thread.sleep(2000);
  NewDesignLogin.clickOnTravel();
  
  NewDesign_Hotels_ResultsPage NewDesignHotelsResultsPage=new NewDesign_Hotels_ResultsPage(driver);
  NewDesign_Hotels_DescPage NewDesignHotels_DescPage = new NewDesign_Hotels_DescPage(driver);
  NewDesignHotelsSearchPage NewDesign_HotelsSearchPage = new NewDesignHotelsSearchPage(driver);
  NewDesign_Hotels_BookingPage NewDesign_HotelsBookingPage=new NewDesign_Hotels_BookingPage(driver);
  NewDesign_AwaitingApprovalScreen NewDesign_Awaiting_ApprovalScreen=new NewDesign_AwaitingApprovalScreen(driver);
  NewDesign_EmulateProcess NewDesign_Emulate_Process=new NewDesign_EmulateProcess(driver);
  NewDesign_getNetworkResponsesData NewDesign_getNetwork_Responses_Data=new NewDesign_getNetworkResponsesData(driver);
  NewDesign_Trips NewDesignTrips = new NewDesign_Trips(driver);

  NewDesign_HotelsSearchPage.clickOnHotels();
  Thread.sleep(3000);
  NewDesign_HotelsSearchPage.enterDestinationForHotels(origin, Log);
  Thread.sleep(3000);
  NewDesign_HotelsSearchPage.selectDate(fromDate, fromMonthYear, Log);
  NewDesign_HotelsSearchPage.selectReturnDate(returnDate, returnMonthYear, Log);
 // NewDesign_HotelsSearchPage.fillRoomDetails("3", "2,3,1", "1,2,0", "5;7,8;", Log);
  NewDesign_HotelsSearchPage.clickOnSearchHotelBut();
  Thread.sleep(8000);
  
//To get the hotel search response body based on url contains text 
String body1 = getResponseBodyByPartialUrl("hotelsearchv2", 60);
System.out.println("Hotel search Body found: " + body1);
embedApiResponseInReport("HotelSearchAPI", body1);

resetApiResponses();    

String[] checkindateResultPage = NewDesignHotelsResultsPage.getCheckInDateTextFromResultPage();
String[] checkoutdateResultPage = NewDesignHotelsResultsPage.getCheckOutDateTextFromResultPage();
String[] ResultPageRoomsAndGuests = NewDesignHotelsResultsPage.getRoomAndGuestTextFromResultPage();

// Get all the hotel details from the selected hotel card AND click it
String[] selectedHotelDetails = NewDesignHotelsResultsPage.selectHotelAndGetDetails(hotelindex, Log);
// ==============================================================

// =========== VALIDATE SELECTED HOTEL CARD AGAINST BACKEND ===========
if (body1 != null) {
    // Validate selected hotel details against backend response for the same index position
    NewDesignHotelsResultsPage.validateSelectedHotelWithBackend(
        selectedHotelDetails, 
        body1, 
        hotelindex, 
        Log, 
        screenShots
    );
    
    Log.ReportEvent("INFO", "Successfully validated Hotel Card #" + hotelindex + 
                    " UI details with Backend API response data");
}
// ====================================================================


  
  //get all the details from desc page 
  NewDesignHotels_DescPage.waitUntilDescriptionCardVisibleOrFail(driver, 30, Log);
  
  //fetch the hotel description page response body from the network console tab based on url text conatins
     String body2 = getResponseBodyByPartialUrl("TGHS&authtoken", 30);
     System.out.println("Hotel Desc Body found: " + body2);
     embedApiResponseInReport("HotelDescriptionAPI", body2);

     resetApiResponses();
     
  String[] hotelAddressFromDesc = NewDesignHotels_DescPage.getAddressFromDescPg();
  String[] hotelAmenitiesFromDesc = NewDesignHotels_DescPage.getAmenitiesFromDescPg();
  String[] checkInDateFromDesc = NewDesignHotels_DescPage.getCheckInAfterFromDescPg();
  String[] checkOutFromDesc = NewDesignHotels_DescPage.getCheckOutTimeFromDescPg();
  String[] hotelNameFromDesc = NewDesignHotels_DescPage.getHotelNameFromDescPg();
  
  String[] hotelPriceFromDesc = NewDesignHotels_DescPage.getPriceFromDescPg();
  String[] PolicyFromDesc = NewDesignHotels_DescPage.getPolicyFromDescPg();
 
  //validations b/w desc to result pages 
 NewDesignHotels_DescPage.validateHotelNameFromDescToResultPage(hotelNameFromDesc, selectedHotelDetails, Log, screenShots);
  NewDesignHotels_DescPage.validateHotelAddressFromDescToResultPage(hotelAddressFromDesc, selectedHotelDetails, Log, screenShots);
  NewDesignHotels_DescPage.validateHotelPolicyFromDescToResultPage(PolicyFromDesc, selectedHotelDetails, Log, screenShots);
  NewDesignHotels_DescPage.validateAmenitiesFromDescToResultPage(hotelAmenitiesFromDesc, selectedHotelDetails, Log, screenShots);
  
  String[] selectRooms = NewDesignHotels_DescPage.selectRoomsFromDescPg();
  
  Thread.sleep(3000);
  NewDesignHotels_DescPage.waitUntilHotelBookingPageDisplayed();
  
  //fetch the hotel booking page response body from the network console tab based on url text conatins
  String body3 = getResponseBodyByPartialUrl("TGHS&authtoken", 30);
  System.out.println("Hotel Booking Body found: " + body3);
  embedApiResponseInReport("HotelBookingAPI", body3);

        
  String[] BookingPgcheckIn = NewDesign_HotelsBookingPage.getCheckInAfterFromBookingPg();
  String[] BookingPgCheckOut = NewDesign_HotelsBookingPage.getCheckOutTimeFromBookingPg();
  String[] BookingPgAddress = NewDesign_HotelsBookingPage.getHotelAddressFromBookingPg();
  String[] BookingPgHotelNm = NewDesign_HotelsBookingPage.getHotelNameFromBookingPg();
  String[] BookingPgLabel = NewDesign_HotelsBookingPage.getLabelTextFromBookingPg();
  String[] BookingPgMeals = NewDesign_HotelsBookingPage.getMealsTextFromBookingPg();
  String[] BookingPgPolicy = NewDesign_HotelsBookingPage.getPolicyTextFromBookingPg();
  String[] BookingPgRefundable = NewDesign_HotelsBookingPage.getRefundableTextFromBookingPg();
  String[] BookingPgSelectedRoomText = NewDesign_HotelsBookingPage.getSelectedRoomTextFromBookingPg();
  String[] BookingPgTotalfareAmount = NewDesign_HotelsBookingPage.getTotalFareAmountFromBookingPg();
  String[] BookingCheckIndate = NewDesign_HotelsBookingPage.getCheckInDateFromBookingPg();
  String[] BookingCheckOutdate = NewDesign_HotelsBookingPage.getCheckOutDateFromBookingPg();
  
  NewDesign_HotelsBookingPage.validateCheckInAfterFromDescToBookingPage(checkInDateFromDesc, BookingPgcheckIn, Log, screenShots);
  NewDesign_HotelsBookingPage.validateCheckOutTimeFromDescToBookingPage(checkOutFromDesc, BookingPgCheckOut, Log, screenShots);
  NewDesign_HotelsBookingPage.validateHotelAddressFromDescToBookingPage(hotelAddressFromDesc, BookingPgAddress, Log, screenShots);
  NewDesign_HotelsBookingPage.validateHotelNameFromDescToBookingPage(hotelNameFromDesc, BookingPgHotelNm, Log, screenShots);
  NewDesign_HotelsBookingPage.validateLabelFromDescToBookingPage(selectRooms, BookingPgLabel, Log, screenShots);
  NewDesign_HotelsBookingPage.validateMealsTextFromDescToBookingPage(selectRooms, BookingPgMeals, Log, screenShots);
  NewDesign_HotelsBookingPage.validatePolicyTextFromDescToBookingPage(PolicyFromDesc, BookingPgPolicy, Log, screenShots);
  NewDesign_HotelsBookingPage.validateRefundableTextFromDescToBookingPage(selectRooms, BookingPgRefundable, Log, screenShots);
  NewDesign_HotelsBookingPage.validateSelectedRoomTextFromDescToBookingPage(selectRooms, BookingPgSelectedRoomText, Log, screenShots);
  NewDesign_HotelsBookingPage.validateCheckInDateBetweenResultAndBookingPage(checkindateResultPage, BookingCheckIndate, Log, screenShots);
  NewDesign_HotelsBookingPage.validateCheckOutDateBetweenResultAndBookingPage(checkoutdateResultPage, BookingCheckOutdate, Log, screenShots);
 NewDesign_HotelsBookingPage.validatePriceFromDescWithBookingPage(selectRooms, BookingPgTotalfareAmount, Log, screenShots);

  //NewDesign_HotelsBookingPage.addTravellerDetails();
 // NewDesign_HotelsBookingPage.clickSendForApprovalBtn(Log);
 
     }catch (Exception e)
     {
      String errorMessage = "Exception occurred: " + e.toString();
      Log.ReportEvent("FAIL", errorMessage);
      screenShots.takeScreenShot();
      e.printStackTrace();  
      Assert.fail(errorMessage);
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
            // Wait until an entry in our map contains the user's string in its URL key
            wait.until(d -> allApiResponses.keySet().stream()
                    .anyMatch(url -> url.contains(userPassedName)));

            // Find the full URL key that matches the user's string
            String fullUrl = allApiResponses.keySet().stream()
                    .filter(url -> url.contains(userPassedName))
                    .findFirst()
                    .get();

            return allApiResponses.get(fullUrl);
        } catch (Exception e) {
            Assert.fail("❌ Could not find any captured API response containing: " + userPassedName);
            return null;
        }
    }
    
    // --------------------------
    // Method to clear previous API responses before navigating to a new page
    public void resetApiResponses() {
        allApiResponses.clear();
        lastResetTime.set(System.currentTimeMillis());
    }
    

    //to make like file format -- but accesible in only my laptop not others
//    private void saveAndAttachApiResponse(String apiName, String responseBody) {
//        try {
//            // 🔹 Report base directory
//            String reportDir = System.getProperty("user.dir")
//                    + "/src/test/resources/Reports";
//
//            String apiDir = reportDir + "/api-responses";
//            Files.createDirectories(Paths.get(apiDir));
//
//            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
//                    .format(new Date());
//
//            String fileName = apiName + "_" + timestamp + ".json";
//            String fullFilePath = apiDir + "/" + fileName;
//
//            String formattedJson = formatJson(responseBody);
//            Files.write(Paths.get(fullFilePath), formattedJson.getBytes());
//
//            // 🔹 RELATIVE path for HTML
//            String relativePath = "api-responses/" + fileName;
//
//            test.log(Status.INFO,
//                "<a href='" + relativePath + "' target='_blank'>📄 View "
//                + apiName + " JSON Response</a>");
//
//        } catch (Exception e) {
//            test.log(Status.WARNING, "Unable to save API response: " + e.getMessage());
//        }
//    }


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