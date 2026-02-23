package com.tripgain.collectionofpages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class ExpensesModule {
	WebDriver driver;

	public ExpensesModule(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	//Method to click expense button
	public void clickExpenseButton() throws InterruptedException {
		driver.findElement(By.xpath("//button[text()='Expenses']")).click();
		Thread.sleep(3000);
	}
	
//Method to click on add expense button
	public void clickAddExpenseButton() throws InterruptedException {
		driver.findElement(By.xpath("//button[text()='Add Expense']")).click();
		Thread.sleep(3000);
	}
	
	//Method to click Select Category dropdown values randomly

	public void clickOnSelectCategoryValues(Log log,ScreenShots ScreenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
		        JavascriptExecutor js = (JavascriptExecutor) driver;

		        //Click the dropdown
		        WebElement SelectCategoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//label[text()='Select Category']/following-sibling::div")));
		        SelectCategoryDropdown.click();
		        log.ReportEvent("PASS", "Clicked on the Select Category dropdown");
Thread.sleep(4000);
		        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
		            By.xpath("//*[contains(@class,'tg-select__option css-10wo9uf-option')]")));

		        // Picking a random option
		        Random rand = new Random();
		        int index = rand.nextInt(dropdownOptions.size());
		        WebElement selectedOption = dropdownOptions.get(index);
		        String selectedText = selectedOption.getText();

		        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
		        Thread.sleep(1000);

		        try {
		        	Thread.sleep(2000);
		            selectedOption.click(); 
		            log.ReportEvent("PASS", "Selected random Category : " + selectedText);
		        } catch (ElementClickInterceptedException ex) {
		            js.executeScript("arguments[0].click();", selectedOption);
		            log.ReportEvent("PASS", "Selected random Category: " + selectedText);
		        }

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to select Category. Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();

		    }
		}

	public void clickOnSelectCategoryValuesByUserPassed(Log log, ScreenShots ScreenShots, String categoryText) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        WebElement selectCategoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//label[text()='Select Category']/following-sibling::div")));
	        selectCategoryDropdown.click();
	        log.ReportEvent("PASS", "Clicked on the Select Category dropdown");

	        Thread.sleep(3000); 

	        wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//*[contains(@class,'tg-select__menu')]")));

	        WebElement scrollTarget = wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("//div[text()='" + categoryText + "']")));
	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", scrollTarget);

	        WebElement desiredOption = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//div[text()='" + categoryText + "']")));

	        String selectedText = desiredOption.getText(); 

	        try {
	            desiredOption.click();
	            log.ReportEvent("PASS", "Selected Category: " + selectedText);
	        } catch (ElementClickInterceptedException ex) {
	            js.executeScript("arguments[0].click();", desiredOption);
	            log.ReportEvent("PASS", "Selected Category via JS: " + selectedText);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to select category : " + categoryText + ". Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}

	
	
	//Method to click report values
	public void selectreportValues(Log log, ScreenShots ScreenShots, String value) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // Click the dropdown
	        WebElement SelectReportDropdown = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//label[text()='Report']/following-sibling::div")));
	        SelectReportDropdown.click();
	        log.ReportEvent("PASS", "Clicked on the Select Category dropdown");

	        Thread.sleep(4000);

	        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
	            By.xpath("//span[text()='" + value + "']")));

	        if (dropdownOptions.isEmpty()) {
	            log.ReportEvent("FAIL", "No matching report option found for value: " + value);
	            return;
	        }

	        WebElement selectedOption = dropdownOptions.get(0);
	        String selectedText = selectedOption.getText();

	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
	        Thread.sleep(1000);

	        try {
	            selectedOption.click(); 
	            log.ReportEvent("PASS", "Selected Report: " + selectedText);
	        } catch (ElementClickInterceptedException ex) {
	            js.executeScript("arguments[0].click();", selectedOption);
	            log.ReportEvent("PASS", "Selected Report: " + selectedText);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to select Report. Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }
	}

	
	public void selectVehicleType(Log log, ScreenShots ScreenShots, String vehicleName) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // Click the dropdown
	        WebElement SelectVehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//span[text()='Vehicle']/following-sibling::div")));
	        SelectVehicleDropdown.click();
	        log.ReportEvent("PASS", "Clicked on the Select Vehicle Type dropdown");

	        Thread.sleep(4000);

	        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
	            By.xpath("//span[text()='" + vehicleName + "']")));

	        if (dropdownOptions.isEmpty()) {
	            log.ReportEvent("FAIL", "No matching vehicle type found for: " + vehicleName);
	            return;
	        }

	        WebElement selectedOption = dropdownOptions.get(0);
	        String selectedText = selectedOption.getText();

	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
	        Thread.sleep(1000);

	        try {
	            Thread.sleep(2000);
	            selectedOption.click();
	            log.ReportEvent("PASS", "Selected Vehicle Type: " + selectedText);
	        } catch (ElementClickInterceptedException ex) {
	            js.executeScript("arguments[0].click();", selectedOption);
	            log.ReportEvent("PASS", "Selected Vehicle Type: " + selectedText);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to select Vehicle Type. Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }
	}




  	@FindBy(xpath="(//*[text()='Currency']//parent::div//parent::div//input)[1]")
    WebElement currencyDropDown;

	//Method to click On Currency DropDown
  	public void clickOnCurrencyDropDown() throws InterruptedException {
  	    JavascriptExecutor js = (JavascriptExecutor) driver;

  	    js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", currencyDropDown);

  	    Thread.sleep(1000);

  	    currencyDropDown.click();
  	}


			//Method to select Currency DropDown Values
		 public void selectCurrencyDropDownValues(WebDriver driver,String value,Log log) {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		        System.out.println(value);
		        WebElement CurrencyValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
		                By.xpath("//span[text()='"+value+"']")
		                ));

		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        js.executeScript("arguments[0].scrollIntoView(true);", CurrencyValue);

		        wait.until(ExpectedConditions.elementToBeClickable(CurrencyValue)).click();
	            log.ReportEvent("PASS", "Selected currency: " + value);

		    }
		 
		 
		 //Method to enter data on notes 
		 
		 public void enterNotes(String notesText,Log log,ScreenShots ScreenShots) throws TimeoutException {
			    try {

			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement notesInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//label[text()='Notes']/following::input[1]")
			        ));

			        notesInput.clear();
			        notesInput.sendKeys(notesText);

			        log.ReportEvent("PASS", "Successfully entered notes: " + notesText);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "Notes input field not found on the page.");
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while entering notes: " + e.getMessage());
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    }
			}


		 
		 
		 //Method to enter data on merchant name
		 
		 public void merchantName(String merchantName,Log log,ScreenShots ScreenShots) throws TimeoutException {
			    try {

			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement merchantInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//input[@name='merchant_name']")
			        ));

			        merchantInput.clear();
			        merchantInput.sendKeys(merchantName);

			        log.ReportEvent("PASS", "Successfully entered merchant name: " + merchantName);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "Merchant Name input field was not found on the page.");
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while entering merchant name: " + e.getMessage());
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    }
			}


		 //Method to select payment mode
	
		 public void selectPaymentMode(String paymentType,Log log,ScreenShots ScreenShots) throws TimeoutException {
			    try {
			        log.ReportEvent("INFO", "Attempting to select payment mode: " + paymentType);

				    WebElement paymentDropdown = driver.findElement(By.xpath("//span[text()='Payment Mode']/following-sibling::div"));
			        paymentDropdown.click();

			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//span[text()='" + paymentType + "']"))
			        );

			        option.click();
			        log.ReportEvent("PASS", "Successfully selected payment mode: " + paymentType);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "Payment dropdown or option '" + paymentType + "' not found on the page.");
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while selecting payment mode: " + e.getMessage());
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    }
			}
		 
		 //Method to select corporate card options
		 
		 public void selectorporateCardOptions(String CorpCard,Log log,ScreenShots ScreenShots) throws TimeoutException {
			    try {
			        log.ReportEvent("INFO", "Attempting to select CorpCard : " + CorpCard);

				    WebElement CorpDropdown = driver.findElement(By.xpath("//span[text()='Select Card']/following-sibling::div"));
				    CorpDropdown.click();

			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//span[text()='" + CorpCard + "']"))
			        );

			        option.click();
			        log.ReportEvent("PASS", "Successfully selected CorpCard Option: " + CorpCard);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "CorpCard dropdown or option '" + CorpCard + "' not found on the page.");
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while selecting payment mode: " + e.getMessage());
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    }
			}

		//Method to click on save expense button
		 public void clickSaveExpenseButton() {
			 driver.findElement(By.xpath("//button[text()='Save Expense']")).click();
		 }
		 
		//Method to click on Save and Add Another  button
		 public void clickSaveandAddAnotherButton() {
			 driver.findElement(By.xpath("//button[text()='Save and Add Another']")).click();
		 }
		 
		//Method to click on cancel button
		 public void clickCancelButton() {
			 driver.findElement(By.xpath("//button[text()='Cancel']")).click();
		 }
		 
		 //Method to click and upload receipt
		 public void uploadReceiptUnderExpense(String filePath) throws InterruptedException {
			    WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']/following-sibling::label"));
			    Thread.sleep(3000);
			    uploadInput.sendKeys(filePath); 
			}
		 
		 public void uploadReceiptUnderMileage(String filePath) throws InterruptedException {
			    WebElement uploadInput = driver.findElement(By.xpath("//div[contains(@class,'upload-button')]"));
			    Thread.sleep(3000);
			    uploadInput.sendKeys(filePath); 
			}
		 
		 //Method to click on Add Mileage button
		 public void clickAddMileage() {
			 driver.findElement(By.xpath("//button[text()='Add Mileage']")).click();
		 }
		
//Method to enter data on merchant name
		 
		 public void addRemarks(String comment, Log log, ScreenShots screenShots) throws TimeoutException {
			    try {
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement commentInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//input[@name='comment']") 
			        ));
			        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", commentInput);
			        Thread.sleep(500);

			        commentInput.click();
			        commentInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
			        commentInput.sendKeys(comment);

			        log.ReportEvent("PASS", "Successfully entered comment: " + comment);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "Comment field was not found on the page.");
			        e.printStackTrace();
			        screenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while entering comment: " + e.getMessage());
			        e.printStackTrace();
			        screenShots.takeScreenShot1();
			    }
			}


		 @FindBy(xpath = "//label[contains(@class,'app-async-select_label')][contains(.,'From Location')]/following::input[@class='tg-async-select__input'][1]")
	       private WebElement enterFromLocation;
		 
		 public void enterfrom(String location) {
			    try {
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			        wait.until(ExpectedConditions.elementToBeClickable(enterFromLocation));
			        
			        try {
			            enterFromLocation.clear(); 
			        } catch (InvalidElementStateException e) {
			            System.out.println("Clear failed, trying JS fallback");
			            JavascriptExecutor js = (JavascriptExecutor) driver;
			            js.executeScript("arguments[0].value='';", enterFromLocation);
			        }

			        enterFromLocation.sendKeys(location);

			        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

			        selectlocations(location);
			    } catch (Exception e) {
			        System.out.println("Error entering location: " + e.getMessage());
			    }
			}

		 public void selectlocations(String location) throws TimeoutException {
		     try {
		         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		         // Wait for dropdown container to appear
		         wait.until(ExpectedConditions.visibilityOfElementLocated(
		             By.xpath("//*[@role='listbox']/parent::div")));

		         // Wait until options are loaded
		         wait.until(driver -> driver.findElements(By.xpath("//div[@role='option']")).size() > 0);

		         List<WebElement> initialOptions = driver.findElements(By.xpath("//div[@role='option']"));
		         int bestScore = Integer.MAX_VALUE;
		         String bestMatchText = null;

		         String input = location.trim().toLowerCase();

		         for (int i = 0; i < initialOptions.size(); i++) {
		             try {
		                 WebElement option = driver.findElements(By.xpath("//div[@role='option']")).get(i);
		                 String suggestion = option.getText().trim().toLowerCase();
		                 int score = levenshteinDistance(input, suggestion);

		                 if (score < bestScore) {
		                     bestScore = score;
		                     bestMatchText = option.getText().trim();
		                 }
		             } catch (StaleElementReferenceException e) {
		                 System.out.println("Stale element at index " + i + ", skipping.");
		             }
		         }

		         if (bestMatchText != null) {
		             // Retry clicking best match up to 3 times
		             int attempts = 0;
		             boolean clicked = false;
		             while (attempts < 3 && !clicked) {
		                 try {
		                     WebElement bestMatch = wait.until(ExpectedConditions.elementToBeClickable(
		                         By.xpath("//div[@role='option' and normalize-space(text())='" + bestMatchText + "']")));
		                     bestMatch.click();
		                     System.out.println("Selected best match: " + bestMatchText);
		                     clicked = true;
		                 } catch (StaleElementReferenceException e) {
		                     System.out.println("Stale element on click attempt " + (attempts + 1) + ", retrying...");
		                 }
		                 attempts++;
		             }

		             if (!clicked) {
		                 System.out.println("Failed to click the best match after retries.");
		             }

		         } else {
		             System.out.println("No suitable match found for input: " + location);
		         }

		     } catch (NoSuchElementException e) {
		         System.out.println("Input or dropdown not found: " + e.getMessage());
		     } catch (Exception e) {
		         System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
		     }
		 }

		 public int levenshteinDistance(String a, String b) {
		     int[][] dp = new int[a.length() + 1][b.length() + 1];

		     for (int i = 0; i <= a.length(); i++) {
		         for (int j = 0; j <= b.length(); j++) {
		             if (i == 0) {
		                 dp[i][j] = j;
		             } else if (j == 0) {
		                 dp[i][j] = i;
		             } else {
		                 int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
		                 dp[i][j] = Math.min(Math.min(
		                     dp[i - 1][j] + 1,       // deletion
		                     dp[i][j - 1] + 1),      // insertion
		                     dp[i - 1][j - 1] + cost // substitution
		                 );
		             }
		         }
		     }
		     return dp[a.length()][b.length()];
		 }
		 
		 @FindBy(xpath = "//label[contains(@class,'app-async-select_label')][contains(.,'To Location')]/following::input[@class='tg-async-select__input'][1]")
	       private WebElement enterToLocation;
		 
		 public void enterTo(String location) throws TimeoutException, InterruptedException {
			 Thread.sleep(1000);
			 enterToLocation.clear();
			 enterToLocation.sendKeys(location);
	            
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));
	            
	            selectlocations(location);
	        }
		 
		 @FindBy(xpath = "//*[contains(@id,'react-select-8-input')]")
	       private WebElement enterStop;
		 
		 public void enterStop(String location) throws TimeoutException, InterruptedException {
			 Thread.sleep(1000);
			 enterStop.clear();
			 enterStop.sendKeys(location);
	            
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));
	            
	            selectlocations(location);
	        }

		 @FindBy(xpath = "//label[text()='Expense Date']/following-sibling::div")
		    WebElement datePickerInput;		    
		    
		//Method to Select Check-In Date By Passing Two Paramenters(Date and MounthYear)
		            public void selectDate(String day, String MonthandYear) throws InterruptedException
		            {
		                JavascriptExecutor js = (JavascriptExecutor) driver;

		                // Method A: Using zoom
		                js.executeScript("document.body.style.zoom='80%'");
		                TestExecutionNotifier.showExecutionPopup();
		                datePickerInput.click();
		                String Date=driver.findElement(By.xpath("//h2[@class='react-datepicker__current-month']")).getText();
		                if(Date.contentEquals(MonthandYear))
		                {
		                    Thread.sleep(4000);
		                    driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
		                    
		                    Thread.sleep(4000);
		                }else {
		                    while(!Date.contentEquals(MonthandYear))
		                    {
		                        Thread.sleep(500);
		                        driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
		                        if(driver.findElement(By.xpath("(//div[@class='react-datepicker__header ']/child::h2)[1]")).getText().contentEquals(MonthandYear))
		                        {
		                            driver.findElement(By.xpath("//*[@class='react-datepicker__month-container']//*[text()='"+day+"']")).click();
		                            break;
		                        }

		                    }
		                }
		            }
		            
		            
	
	
	//Method for to click add trip number
	
	public void addTripNumber(Log log, ScreenShots ScreenShots, String tripnumber) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // Click the dropdown
	        WebElement SelectTripNumbereDropdown = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//label[text()='Report (Trip Number)']/following-sibling::div")));
	        SelectTripNumbereDropdown.click();
	        log.ReportEvent("PASS", "Clicked on the Select trip number dropdown");

	        Thread.sleep(4000);

	        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
	            By.xpath("//span[text()='" + tripnumber + "']")));

	        if (dropdownOptions.isEmpty()) {
	            log.ReportEvent("FAIL", "No matching trip number found for: " + tripnumber);
	            return;
	        }

	        WebElement selectedOption = dropdownOptions.get(0);
	        String selectedText = selectedOption.getText();

	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
	        Thread.sleep(1000);

	        try {
	            Thread.sleep(2000);
	            selectedOption.click();
	            log.ReportEvent("PASS", "Selected trip number: " + selectedText);
	        } catch (ElementClickInterceptedException ex) {
	            js.executeScript("arguments[0].click();", selectedOption);
	            log.ReportEvent("PASS", "Selected trip number: " + selectedText);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to select trip number. Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }
	}
	
	//get merchant name text from add expense 
	
	public void getMerchantNameText(Log log,ScreenShots ScreenShots) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        WebElement merchantText = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//label[text()='Merchant Name']/following-sibling::*//input")
	        ));
	        
	        String merchantName = merchantText.getAttribute("value");
	        
	        log.ReportEvent("INFO", "Merchant Name text is: " + merchantName);

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to get Merchant Name text: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}
	
	//Method to get amount text 
	
	public void getAmountText(Log log,ScreenShots ScreenShots) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        WebElement amountText = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//label[text()='Amount']/following-sibling::*//input")
	        ));
	        
	        String amountValue = amountText.getAttribute("value");
	        
	        log.ReportEvent("INFO", "amount text is: " + amountValue);

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to get amount Text: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}
	
//Method to enter Violations text
	
	public void enterViolationsText(String violationText) {
		driver.findElement(By.xpath("//label[text()='Deviation Reason']/following-sibling::div/textarea")).sendKeys(violationText);
	}
	
	//Method to click proceed button
	
	public void clickProceedBtn() {
		driver.findElement(By.xpath("//button[text()='Proceed']")).click();
	}
	
	//Method to validate the report
	
	public void validateReportMessageText(Log log,ScreenShots ScreenShots) throws TimeoutException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement messageText = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//span[@id='client-snackbar']")
	        ));
	        
	        if (messageText.isDisplayed()) {
	            log.ReportEvent("PASS", "Report message is displayed: " + messageText.getText());
	        } else {
	            log.ReportEvent("FAIL", "Report message is NOT displayed.");
		        ScreenShots.takeScreenShot1();

	        }
	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error validating report message: " + e.getMessage());
	        ScreenShots.takeScreenShot1();

	    }
	}

	//get distance text
	
		public void getDistanceText(Log log,ScreenShots ScreenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        WebElement distanceText = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//label[text()='Distance in km']/following-sibling::div/input")
		        ));
		        
		        String distancevalue = distanceText.getAttribute("value");
		        
		        log.ReportEvent("INFO", "distance text is: " + distancevalue);

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to get distance text: " + e.getMessage());
		        ScreenShots.takeScreenShot1();
		        e.printStackTrace();
		    }
		}
		
		//get rate per km text
		
			public void getRateText(Log log,ScreenShots ScreenShots) {
			    try {
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement RateText = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//label[text()='Distance in km']/following-sibling::div/input")
			        ));
			        
			        String ratevalue = RateText.getAttribute("value");
			        
			        log.ReportEvent("INFO", "Rate per Km text is: " + ratevalue);

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Failed to get rate text: " + e.getMessage());
			        ScreenShots.takeScreenShot1();
			        e.printStackTrace();
			    }
			}

			//get amount text in add mileage
			
			public void getAmountTextFromAddMileage(Log log,ScreenShots ScreenShots) {
			    try {
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement amounttext = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//label[text()='Total Amount']/following-sibling::div/input")
			        ));
			        
			        String amount = amounttext.getAttribute("value");
			        
			        log.ReportEvent("INFO", "Amount text is: " + amount);

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Failed to get amount text: " + e.getMessage());
			        ScreenShots.takeScreenShot1();
			        e.printStackTrace();
			    }
			}
			
			 //Method to click and upload receipt in add mileage 
			 public void uploadReceiptInAddMileage(String filePath) throws InterruptedException {
				    WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']"));
				    Thread.sleep(3000);
				    uploadInput.sendKeys(filePath); 
				}
			 
			 
				//Method to click on Save  button on add mileage 
				 public void clickSaveButtonOnaddMileage() {
					 driver.findElement(By.xpath("//button[text()='Save']")).click();
				 }
				 
//Method to click on add stop on add mileage
	public void addStop() {
		driver.findElement(By.xpath("//button[text()='Add Stop']")).click();
	}
	
	//method to click on create report
	
	public void clickOnCreateReportButton() {
		driver.findElement(By.xpath("//button[text()='Create Report']")).click();
	}
	
	public void clickOnReportPurpose(Log log, ScreenShots ScreenShots, String reportPurposeText) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        WebElement selectReportPurposeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//label[text()='Report Purpose']/following-sibling::div")));
	        selectReportPurposeDropdown.click();
	        log.ReportEvent("PASS", "Clicked on the Report Purpose dropdown");

	        Thread.sleep(3000); 

	        ;

	        WebElement scrollTarget = wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("//li[text()='" + reportPurposeText + "']")));
	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", scrollTarget);

	        WebElement desiredOption = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[text()='" + reportPurposeText + "']")));

	        String selectedText = desiredOption.getText(); 

	        try {
	            desiredOption.click();
	            log.ReportEvent("PASS", "Selected report Purpose Text: " + selectedText);
	        } catch (ElementClickInterceptedException ex) {
	            js.executeScript("arguments[0].click();", desiredOption);
	            log.ReportEvent("PASS", "Selected report Purpose Text: " + selectedText);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Failed to select report Purpose Text : " + reportPurposeText + ". Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}

	 
	 
	 public void ReportNameOnCreateReport(String reportname,Log log,ScreenShots ScreenShots) throws TimeoutException {
		    try {

		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        WebElement reportnm = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//label[text()='Report Name']/following-sibling::div/input")
		        ));

		        reportnm.clear();
		        reportnm.sendKeys(reportname);

		        log.ReportEvent("PASS", "Successfully entered report name: " + reportname);

		    } catch (NoSuchElementException e) {
		        log.ReportEvent("FAIL", "report name input field was not found on the page.");
		        e.printStackTrace();
		        ScreenShots.takeScreenShot1();

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Unexpected error while entering report name: " + e.getMessage());
		        e.printStackTrace();
		        ScreenShots.takeScreenShot1();

		    }
		}
	 
	 @FindBy(xpath = "//label[text()='From Date']/following-sibling::div")
	    WebElement fromDateonCreateReport;
	    
	    
	//Method to Select from date
	            public void selectFromDateOnCreateReport(String day, String MonthandYear) throws InterruptedException
	            {
	                JavascriptExecutor js = (JavascriptExecutor) driver;

	                // Method A: Using zoom
	                js.executeScript("document.body.style.zoom='80%'");
	                TestExecutionNotifier.showExecutionPopup();
	                fromDateonCreateReport.click();
	                String Date=driver.findElement(By.xpath("//h2[@class='react-datepicker__current-month']")).getText();
	                if(Date.contentEquals(MonthandYear))
	                {
	                    Thread.sleep(4000);
	                    driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
	                    
	                    Thread.sleep(4000);
	                }else {
	                    while(!Date.contentEquals(MonthandYear))
	                    {
	                        Thread.sleep(500);
	                        driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
	                        if(driver.findElement(By.xpath("(//div[@class='react-datepicker__header ']/child::h2)[1]")).getText().contentEquals(MonthandYear))
	                        {
	                            driver.findElement(By.xpath("//*[@class='react-datepicker__month-container']//*[text()='"+day+"']")).click();
	                            break;
	                        }

	                    }
	                }
	            }
	            
	            @FindBy(xpath = "//label[text()='To Date']/following-sibling::div")
	    	    WebElement toDateonCreateReport;
	    	    
	    	    
	    	//Method to Select from date
	    	            public void selectToDateOnCreateReport(String day, String MonthandYear) throws InterruptedException
	    	            {
	    	                JavascriptExecutor js = (JavascriptExecutor) driver;

	    	                // Method A: Using zoom
	    	                js.executeScript("document.body.style.zoom='80%'");
	    	                TestExecutionNotifier.showExecutionPopup();
	    	                toDateonCreateReport.click();
	    	                String Date=driver.findElement(By.xpath("//h2[@class='react-datepicker__current-month']")).getText();
	    	                if(Date.contentEquals(MonthandYear))
	    	                {
	    	                    Thread.sleep(4000);
	    	                    driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
	    	                    
	    	                    Thread.sleep(4000);
	    	                }else {
	    	                    while(!Date.contentEquals(MonthandYear))
	    	                    {
	    	                        Thread.sleep(500);
	    	                        driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
	    	                        if(driver.findElement(By.xpath("(//div[@class='react-datepicker__header ']/child::h2)[1]")).getText().contentEquals(MonthandYear))
	    	                        {
	    	                            driver.findElement(By.xpath("//*[@class='react-datepicker__month-container']//*[text()='"+day+"']")).click();
	    	                            break;
	    	                        }

	    	                    }
	    	                }
	    	            }
	    
//Method to click on add button
 public void clickOnAddButton() {
	 driver.findElement(By.xpath("//button[text()='Add']")).click();
 }
	
//Method to click on view button
public void clickOnViewButton() {
	 driver.findElement(By.xpath("(//span[text()='View'])[1]")).click();
}

//Method to click on add expense in viewreport
public void clickAddExpenseInViewButton() {
	driver.findElement(By.xpath("//span[text()='Add Expense']")).click();
}
	            
//get Report Number text 

public void getReportNumberText(Log log, ScreenShots screenShots) {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement reportNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//label[text()='Report (Trip Number)']/following-sibling::div")
        ));
        
        String report = reportNumber.getText();
        
        log.ReportEvent("INFO", "Report Number text is: " + report);

    } catch (Exception e) {
        log.ReportEvent("FAIL", "Failed to get Report Number text: " + e.getMessage());
        screenShots.takeScreenShot1();
        e.printStackTrace();
    }
}

	
	 //Method to select corporate card 
	
	 public void selectCorporateCard(String corporateCard,Log log,ScreenShots ScreenShots) throws TimeoutException {
		    try {
		        log.ReportEvent("INFO", "Attempting to select corporateCard : " + corporateCard);

			    WebElement corporateCardDropdown = driver.findElement(By.xpath("//label[text()='Select Card']/following-sibling::div"));
			    corporateCardDropdown.click();

		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//span[text()='" + corporateCard + "']"))
		        );

		        option.click();
		        log.ReportEvent("PASS", "Successfully selected corporateCard : " + corporateCard);

		    } catch (NoSuchElementException e) {
		        log.ReportEvent("FAIL", "corporateCard or option '" + corporateCard + "' not found on the page.");
		        e.printStackTrace();
		        ScreenShots.takeScreenShot1();

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Unexpected error while selecting corporateCard : " + e.getMessage());
		        e.printStackTrace();
		        ScreenShots.takeScreenShot1();

		    }
		} 
		
		public void getSelectYearTextFromMARR(Log log, ScreenShots screenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        
		        WebElement selectyear = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//label[text()='Select Year']/following-sibling::div")
		        ));
		        
		        // Get visible text
		        String yearText = selectyear.getText();

		        log.ReportEvent("INFO", "selected year is: " + yearText);

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to get selected year text: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        e.printStackTrace();
		    }
		}
		
		
		public void getSelectMonthTextFromMARR(Log log, ScreenShots screenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        
		        // Adjusted XPath to find the actual text-containing div
		        WebElement selectmonth = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//label[text()='Select Month']/following-sibling::div/div")
		        ));
		        
		        // Get visible text
		        String monthText = selectmonth.getText();

		        log.ReportEvent("INFO", "select month text  is: " + monthText);

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to get selected month text: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        e.printStackTrace();
		    }
		}
		
		public void getSeletedFromDateTextFromMARR(Log log, ScreenShots screenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        
		        WebElement fromDate = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//label[text()='From Date']/following-sibling::div/input")
		        ));
		        
		        String fromdateText = fromDate.getText();

		        log.ReportEvent("INFO", "selected from date text  is: " + fromdateText);

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to get selected from date text: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        e.printStackTrace();
		    }
		}

		public void getSeletedtoDateTextFromMARR(Log log, ScreenShots screenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        
		        WebElement toDate = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//label[text()='To Date']/following-sibling::div/input")
		        ));
		        
		        String todateText = toDate.getText();

		        log.ReportEvent("INFO", "selected to date text  is: " + todateText);

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to get selected to date text: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        e.printStackTrace();
		    }
		}
		
		
		// method to search through trip on search report fields
		
		public void SearchTripThroughSearchReportFieldUnderExpense(String... searchValues) {
		    WebElement searchField = driver.findElement(
		        By.xpath("//input[@placeholder='Search Reports']")
		    );

		    for (String value : searchValues) {
		        searchField.clear();
		        searchField.sendKeys(value);
		    }
		}

		public void openExpenseReport() {
			driver.findElement(By.xpath("//tr[@class='clickable']")).click();
		}

		
		public void clickOnSelectCategoryValuesThroughExpenseSearchTripIdFlow(Log log,ScreenShots ScreenShots) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
		        JavascriptExecutor js = (JavascriptExecutor) driver;

		        //Click the dropdown
		        WebElement SelectCategoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//span[text()='Category']/following-sibling::div")));
		        SelectCategoryDropdown.click();
		        
		       // log.ReportEvent("PASS", "Clicked on the Select Category dropdown");
Thread.sleep(4000);
		        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
		            By.xpath("//*[contains(@class,'tg-select-option-label')]")));

		        // Picking a random option
		        Random rand = new Random();
		        int index = rand.nextInt(dropdownOptions.size());
		        WebElement selectedOption = dropdownOptions.get(index);
		        String selectedText = selectedOption.getText();

		        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
		        Thread.sleep(1000);

		        try {
		        	Thread.sleep(2000);
		            selectedOption.click(); 
		            log.ReportEvent("PASS", "Selected random Category : " + selectedText);
		        } catch (ElementClickInterceptedException ex) {
		            js.executeScript("arguments[0].click();", selectedOption);
		            log.ReportEvent("PASS", "Selected random Category: " + selectedText);
		        }

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Failed to select Category. Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();

		    }
		}
		    
		
		 @FindBy(xpath = "//*[@class='custom_datepicker_input']")
		    WebElement datePicker;
	

		 // Method to select date (simplest version)
		 public void selectDateThroughExpenseSearchTripIdFlow(String day) throws InterruptedException {
		     // 1. Click on date picker to open calendar
		     datePicker.click();
		     Thread.sleep(2000);
		     
		     // 2. Click on the day directly
		     String dayXpath = "//div[contains(@class,'react-datepicker__month-container')]//span[text()='" + day + "']";
		     driver.findElement(By.xpath(dayXpath)).click();
		     Thread.sleep(2000);
		 }
		 
		 public void EnterAmounthroughSearchReportFieldUnderExpense(String value) {
			    WebElement searchField = driver.findElement(
			        By.xpath("//input[@name='total_amount']"));
			        searchField.clear();
			        searchField.sendKeys(value);
			    }
		    
		 //Method to clcik on save expense 
		 
		 public void clickOnSaveExpenseButt() {
			 driver.findElement(By.xpath("//button[text()='Save Expense']")).click();
		 }
		 
		
		 public void clickOnSaveAndAnotherButt() {
			 driver.findElement(By.xpath("//button[text()='Save and Add Another']")).click();
		 }
		 
		 public void clickOnMileageButtThroughExpenseSearchTripIdFlow() {
			 driver.findElement(By.xpath("//button[text()='Mileage']")).click();
		 }
		 
		 
		 public void clickOnSelectCategoryValuesunderMileageThroughExpenseSearchTripIdFlow(Log log,ScreenShots ScreenShots) {
			    try {
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
			        JavascriptExecutor js = (JavascriptExecutor) driver;

			        //Click the dropdown
			        WebElement SelectCategoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//span[text()='Select Category']/following-sibling::div")));
			        SelectCategoryDropdown.click();
			        
			       // log.ReportEvent("PASS", "Clicked on the Select Category dropdown");
	Thread.sleep(4000);
			        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
			            By.xpath("//*[contains(@class,'tg-select-option-label')]")));

			        // Picking a random option
			        Random rand = new Random();
			        int index = rand.nextInt(dropdownOptions.size());
			        WebElement selectedOption = dropdownOptions.get(index);
			        String selectedText = selectedOption.getText();

			        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
			        Thread.sleep(1000);

			        try {
			        	Thread.sleep(2000);
			            selectedOption.click(); 
			            log.ReportEvent("PASS", "Selected random Category : " + selectedText);
			        } catch (ElementClickInterceptedException ex) {
			            js.executeScript("arguments[0].click();", selectedOption);
			            //log.ReportEvent("PASS", "Selected random Category: " + selectedText);
			        }

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Failed to select Category under mileage. Exception: " + e.getMessage());
		        ScreenShots.takeScreenShot1();

			    }
			}
		 
		 public String[] getReportIdFromExpenseReportDetailsPg(Log log) {
	 		    String reportid = driver.findElement(By.xpath("//span[text()='Report ID']/following-sibling::span")).getText();
	 	        System.out.println("Report id from Expense report details page : " + reportid);
	 		    log.ReportEvent("INFO", "reportid from Expense report details page :"+ reportid);

	 		    return new String[]{reportid};
	 		      
	 		}
		 
		 public void clickOnSubmitReportBtnThroughExpenseSearchTripIdFlow() {
			 driver.findElement(By.xpath("//button[text()='Submit Report']")).click();
		 }
		 

		 
		 public void clickOnSubmitBtnFromExpenseReportSubmissionWarningpopup() {
			 driver.findElement(By.xpath("//button[text()='Submit']")).click();
		 }
		 
		 
		 public void handleExpenseReportSubmission() {
			    try {
			        // 1️⃣ Check if warning/violation message is displayed
			        WebElement warningDiv = driver.findElement(
			            By.xpath("//div[contains(@class,'tg-typography_error')]/text()[normalize-space()]")
			        );

			        if (warningDiv.isDisplayed()) {
			            System.out.println("Warning notification is displayed: " + warningDiv.getText().trim());

			            // Click checkbox inside the warning popup
			            WebElement checkboxInWarning = driver.findElement(
			                By.xpath("//div[text()='Expense Report Submission']//following::input[@type='checkbox']/following-sibling::span")
			            );
			            checkboxInWarning.click();

			            // Click Submit button in warning popup
			            clickOnSubmitBtnFromExpenseReportSubmissionWarningpopup();
			        }

			    } catch (NoSuchElementException e) {
			        // Warning not displayed → skip popup
			        System.out.println("No warning popup. Proceeding with normal flow.");

			        // 2️⃣ Enter remarks
			        WebElement remarksField = driver.findElement(By.xpath("//input[@name='remarks']"));
			        remarksField.sendKeys("Automated remarks");

			        // 3️⃣ Click checkbox outside popup
			        WebElement checkbox = driver.findElement(
			            By.xpath("//div[text()='Expense Report Submission']//following::input[@type='checkbox']/following-sibling::span")
			        );
			        checkbox.click();

			        // 4️⃣ Click Submit
			        clickOnSubmitBtnFromExpenseReportSubmissionWarningpopup();
			    }
			}

		 //Method to clcik on logs button
		 public void clcikOnLogBtn() {
			 driver.findElement(By.xpath("//button[text()='Logs']")).click();
		 }
		 
	
		 public String getApproverEmailByIndexPresentUnderLOgs(int index) {
		     // Fetch all email elements dynamically
		     List<WebElement> emailElements = driver.findElements(
		         By.xpath("//div[contains(@class,'tg-approver-email')]")
		     );

		     // Return email at index (1-based)
		     if (index > 0 && index <= emailElements.size()) {
		         return emailElements.get(index - 1).getText().trim();
		     }

		     // Return empty string if index is too high
		     return "";
		 }

		 
		 public void clcikOnExpenseVerificationPendingBtn() {
			 driver.findElement(By.xpath("//span[text()='Expense Verification Pending']")).click();
		 }
		 
		
		 
		 public void SearchReportUnderExpenseVerificationPending(String... reportIds) {

			    WebElement searchField = driver.findElement(
			        By.xpath("//input[@placeholder='Search Reports']"));

			    for (String reportid : reportIds) {
			        searchField.clear();
			        searchField.sendKeys(reportid);
			    }
			}

		
		 public void clcikOnStatusDropdownUnderFinanceMngr(String statusDropdown,Log log,ScreenShots ScreenShots) throws TimeoutException {
			    try {
			      //  log.ReportEvent("INFO", "Attempting to select status Dropdown : " + statusDropdown);

				    WebElement Dropdown = driver.findElement(By.xpath("//div[contains(@class,'tg-select-box__dropdown')]"));
				    Dropdown.click();

			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//span[text()='" + statusDropdown + "']"))
			        );

			        option.click();
			        log.ReportEvent("PASS", "Successfully selected status Dropdown Under Finance manager: " + statusDropdown);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "Status dropdown or option '" + statusDropdown + "' not found on the page.");
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while selecting status dropdown " + e.getMessage());
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    }
			}

		 
		 public void clcikOnProcessBtnUnderExpenseVerificationPending() {
			 driver.findElement(By.xpath("//button[text()='Process']")).click();
		 }
		 
		 public String[] enterVerificationRemarksUnderExpenseVerificationPending(String comment) {
			    WebElement remarksField = driver.findElement(By.xpath("//input[@name='reimbursementref']"));
			    remarksField.clear();
			    remarksField.sendKeys(comment);
			    System.out.println("Entered comment: " + comment);
			    return new String[]{comment};
			}
		 
		 public void clcikOnSubmitBtnUnderExpenseVerificationPending() {
			 driver.findElement(By.xpath("//button[text()='Submit']")).click();
		 }
		 
		 public void clcikOnReimburseBtnUnderExpenseVerificationPending() {
			 driver.findElement(By.xpath("//button[text()='Reimburse']")).click();
		 }
		
		 public void selectBankDuringReimbursed(String bankType,Log log,ScreenShots ScreenShots) throws TimeoutException {
			    try {
			        log.ReportEvent("INFO", "Attempting to select payment mode: " + bankType);

				    WebElement bankTypedropdown = driver.findElement(By.xpath("//div[contains(@class,'tg-select-box__indicators css-1wy0on6')]"));
				    bankTypedropdown.click();

			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//span[text()='" + bankType + "']"))
			        );

			        option.click();
			        log.ReportEvent("PASS", "Successfully selected bankType: " + bankType);

			    } catch (NoSuchElementException e) {
			        log.ReportEvent("FAIL", "bankType dropdown or option '" + bankType + "' not found on the page.");
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    } catch (Exception e) {
			        log.ReportEvent("FAIL", "Unexpected error while selecting bankType : " + e.getMessage());
			        e.printStackTrace();
			        ScreenShots.takeScreenShot1();

			    }
			}
		 
			public String[] enterBankReferenceText(String BankRefText) {
			    WebElement BankRef = driver.findElement(By.xpath("//input[@name='bankRef']"));
			    BankRef.clear();
			    BankRef.sendKeys(BankRefText);
			    System.out.println("Entered BankRefText: " + BankRefText);
			    return new String[]{BankRefText};
			} 
		 
			public String[] enterReimbursedRemarks(String ReimRemarks) {
			    WebElement ReimbursedRemarks = driver.findElement(By.xpath("//input[@name='reimbursementref']"));
			    ReimbursedRemarks.clear();
			    ReimbursedRemarks.sendKeys(ReimRemarks);
			    System.out.println("Entered ReimbursedRemarks: " + ReimRemarks);
			    return new String[]{ReimRemarks};
			} 
			
			
			public String[] getStatusFromExpenseReportDetailsPg(Log log) {
			    String Status = driver.findElement(By.xpath("(//div[contains(@class,'tg-label')])[2]")).getText();
			    System.out.println("Expense Status : " + Status);
		        log.ReportEvent("PASS", "Expense Status : " + Status);    
			    return new String[]{Status};
			}	
		 
//			 public void handleExpenseSubmissionAfterAddingExpense() {
//				    try {
//				        // 1️⃣ Check if warning/violation message is displayed
//				        WebElement warningDiv = driver.findElement(
//				            By.xpath("//div[contains(@class,'tg-typography_error')]/text()[normalize-space()]")
//				        );
//
//				        if (warningDiv.isDisplayed()) {
//				            System.out.println("Warning notification is displayed: " + warningDiv.getText().trim());
//
//				            // Click checkbox inside the warning popup
//				            WebElement checkboxInWarning = driver.findElement(
//				                By.xpath("//div[text()='Expense Submission']//following::input[@type='checkbox']/following-sibling::span")
//				            );
//				            checkboxInWarning.click();
//
//				            // Click Submit button in warning popup
//				            clickOnSubmitBtnFromExpenseReportSubmissionWarningpopup();
//				        }
//
//				    } catch (NoSuchElementException e) {
//				        // Warning not displayed → skip popup
//				        System.out.println("No warning popup. Proceeding with normal flow.");
//
//				        // 2️⃣ Enter remarks
//				        WebElement remarksField = driver.findElement(By.xpath("//input[@name='remarks']"));
//				        remarksField.sendKeys("Automated remarks");
//
//				        // 3️⃣ Click checkbox outside popup
//				        WebElement checkbox = driver.findElement(
//				            By.xpath("//div[text()='Expense Report Submission']//following::input[@type='checkbox']/following-sibling::span")
//				        );
//				        checkbox.click();
//
//				        // 4️⃣ Click Submit
//				        clickOnSubmitBtnFromExpenseReportSubmissionWarningpopup();
//				    }
//				}

		 

}
