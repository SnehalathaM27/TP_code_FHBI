package com.tripgain.collectionofpages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.tripgain.common.Log;


public class NewDesign_getNetworkResponsesData {
	WebDriver driver;

	public NewDesign_getNetworkResponsesData (WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	
	    public static void validateHotelNamesUIvsAPI(List<String> uiHotelNames, String body1, Log Log) {
	        if (body1 != null) {
	            System.out.println("---HOTEL NAME VALIDATION BOTH UI AND IN API-----");
	            
	            // Limit to first 10 hotels to keep the validation focused
	            int limit = Math.min(uiHotelNames.size(), 10);
	            String responseLower = body1.toLowerCase();
	            
	            for (int i = 0; i < limit; i++) {
	                String uiName = uiHotelNames.get(i).trim();
	                String uiNameLower = uiName.toLowerCase();
	                
	                System.out.println("Checking Hotel " + (i + 1) + "...");

	                if (responseLower.contains(uiNameLower)) {
	                    // Find the exact text match in the API response to show its version
	                    int startIndex = responseLower.indexOf(uiNameLower);
	                    int endIndex = startIndex + uiName.length();
	                    String apiName = body1.substring(startIndex, endIndex);

	                    System.out.println("✅ MATCH FOUND:");
	                    System.out.println("   UI Hotel Name  : " + uiName);
	                    System.out.println("   API Hotel Name : " + apiName);
	                    
	                } else {
	                    System.out.println("❌ NO HOTEL FOUND:");
	                    System.out.println("   UI Hotel Name  : " + uiName);
	                    System.out.println("   API Status     : Not present in response body");
	                    
	                    Log.ReportEvent("FAIL", "UI Hotel [" + uiName + "] was not found in the API response data.");
	                }
	            }
	        }
	    }
	    
	    
	    public static void validateHotelAddressesUIvsAPI(
	            List<String> uiAddresses,
	            String apiResponse,
	            Log Log) {

	        if (apiResponse == null) return;

	        System.out.println("--- HOTEL ADDRESS VALIDATION UI vs API ---");

	        String apiText = apiResponse.toLowerCase();
	        int limit = Math.min(uiAddresses.size(), 10);

	        for (int i = 0; i < limit; i++) {

	            String uiAddress = uiAddresses.get(i)
	                    .toLowerCase()
	                    .replace("...", "")
	                    .replace(",", "")
	                    .replace(".", "");

	            String[] words = uiAddress.split(" ");

	            int totalValidWords = 0;
	            int matchedWords = 0;

	            for (String word : words) {

	                // ignore very small words
	                if (word.length() < 3) {
	                    continue;
	                }

	                totalValidWords++;

	                if (apiText.contains(word)) {
	                    matchedWords++;
	                }
	            }

	            System.out.println("Checking Address " + (i + 1) + "...");

	            if (totalValidWords > 0 && matchedWords >= (totalValidWords / 2)) {

	                System.out.println("✅ ADDRESS MATCHED (50% rule)");
	                System.out.println("   UI Address : " + uiAddresses.get(i));
	                System.out.println("   Matched    : " + matchedWords + "/" + totalValidWords);

	            } else {

	                System.out.println("❌ ADDRESS NOT MATCHED");
	                System.out.println("   UI Address : " + uiAddresses.get(i));
	                System.out.println("   Matched    : " + matchedWords + "/" + totalValidWords);

	                Log.ReportEvent(
	                        "FAIL",
	                        "Address match below 50%: " + uiAddresses.get(i)
	                );
	            }
	        }
	    }

	    
	    
	    
	    
	    
	    
	    
	}
	  

	


