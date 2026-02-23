package com.tripgain.collectionofpages;

import java.util.HashMap;
import java.util.Map;


public class ApiResponseHandler {
	
	 // This map stores the responses for each URL. The key is the URL, and the value is a list of responses.
    private Map<String, Integer> urlCallCount = new HashMap<>();
    private Map<String, String> apiResponses = new HashMap<>();

    // Method to simulate getting a response from an API (with the same URL, but calling it multiple times)
    public String getResponseBodyByPartialUrl(String urlPart, int timeout) {
        // Increment the call count for this URL
        int callCount = urlCallCount.getOrDefault(urlPart, 0) + 1;
        urlCallCount.put(urlPart, callCount);  // Update the call count for the specific URL

        // Simulate fetching the response based on the call count
        String responseBody = fetchResponse(urlPart, timeout, callCount);

        // Store the response for this specific call number (e.g., "Call1", "Call2", etc.)
        apiResponses.put(urlPart + "_Call" + callCount, responseBody);

        return responseBody;
    }

    // Simulate fetching a response (e.g., making an HTTP request)
    private String fetchResponse(String urlPart, int timeout, int callCount) {
        // Just return a mock response based on the call count and URL part
        return "Response for URL: " + urlPart + " (Call #" + callCount + ")";
    }

    // Method to print all stored responses (for debugging)
    public void printAllResponses() {
        for (Map.Entry<String, String> entry : apiResponses.entrySet()) {
            System.out.println("Stored response for " + entry.getKey() + ": " + entry.getValue());
        }
    }


}
