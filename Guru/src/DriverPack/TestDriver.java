package DriverPack;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestDriver {
  
	private WebDriver driver;
	private String filePath =  ".\\src\\DriverPack\\";
	private String fileName = "TestSheet.xlsx";
	private String geckoDriverPath ="C:\\\\Users\\durga.suresh\\Desktop\\Automation\\geckodriver.exe";
	private String url = "http://127.0.0.1:8080/#/";
	private TestData data;
	private TestReport log = new TestReport();
	private ExtentReports report ;
	private ExtentTest logger ;
	private TestWait wait = new TestWait();
	private TestFunctions Function;
	private TestReport reporter = new TestReport();
	@BeforeTest
	//To create instance of TestReport class
	private ExtentReports setReport()
	{			
		if (report == null)
			try {
				report = new ExtentReports(log.reportPathGen());					
				}catch (Exception e) {
				e.printStackTrace();
			}return report;		
	}	
	
	@BeforeTest
	//To create instance of TestData class
	private  TestData Data() 
	{		
		if (data == null)
			try {
				data = new TestData(filePath, fileName, "TestRunner");						
				}catch (Exception e) {				
				e.printStackTrace();
				}return data;		
	}	  
	
	
	
	
	@Test  					
	private void executeAllTestCases() //To get testcase list from excel sheet and execute them 
	{		
		String currentTcID = null;		
		try {						
			//Get the row indexes which has Run_Mode as Yes
			Vector yesMode = Data().getRowIndexes("Yes", Data().getColIndex("RunMode"));	
						 
			//To get the list of Test cases having YES as Run Mode
			Vector testCases = Data().getResColList(yesMode, "TestCaseID");
			 
			//to run through the test case list with Yes and go to TestCase sheet to check Keywords
			for(int i=0;i<testCases.size();i++)		
			{
				//Current Test Case ID which needs to be executed
				currentTcID = (String) testCases.get(i);
				logger = report.startTest(currentTcID);
				executeTestCase(currentTcID,logger);				
				System.out.println("Status of TestCase "+currentTcID+" is "+logger.getRunStatus());	
				setReport().endTest(logger);				
			}
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			}
	}
	
	
	
			
	private void executeTestCase(String currentTcID, ExtentTest logger){
		try { 	
			
			//To Set driver with Gecko driver path
			System.setProperty("webdriver.gecko.driver",geckoDriverPath );			
			//To initiate the browser	
			driver = new FirefoxDriver();	
	 		Function = new TestFunctions(Data(), driver);
			
			//To set the sheet to the TestCase sheet to execute keyword list
			Data().setSheet("TestCase");
						
			//Get the array of row indexes in Test Case sheet which has Run_Mode as Yes
			Vector currentTcRow = Data().getRowIndexes(currentTcID, Data().getColIndex("TestCaseID"));
			int currentRowIndex = (int) currentTcRow.get(0);//---Current Test case row
						
			//To get the array of Keywords corresponding to each TestCase-------
			Vector KeywordList = Data().getKeywords(currentRowIndex);		
						
			//To execute the keywords for current test case			
			executeKeyword(KeywordList,currentTcID,logger);	
			
			} catch (IllegalArgumentException e) {
			reporter.LogFAIL(driver, logger, "Error in Execution of Test Case -"+currentTcID);
			//logger.log(LogStatus.FATAL, "Error in Execution of Test Case -"+currentTcID);
			e.printStackTrace();
			} 
	}
	
	private void executeKeyword(Vector KeywordList,String currentTcID, ExtentTest logger) {		
		
	System.out.println("Current execution is done on Test case - "+currentTcID);
	Boolean stopExe = true;		
			 		 
	Function = new TestFunctions(Data(), driver);
	 
	//To run through keyword list and execute them
	for(int j=0;j<KeywordList.size();j++)
	{
		
		if(stopExe)
		{
			String ctKeyword = (String) KeywordList.get(j);
			if(!ctKeyword.isEmpty())
			{
				//To retrieve the methods from function library to check for the expected keyword method as enetered in Testcase sheet
				Method method[]= Function.getClass().getDeclaredMethods();
				//To run through the keywords and execute them
				Boolean keywordExistence = false;
				for(int k=0;k<method.length;k++)
				{
					
					if (method[k].getName().equalsIgnoreCase(ctKeyword))
						{
							try
							{
								method[k].invoke(Function,currentTcID,logger);	
								keywordExistence = true;
								break;
								
							} catch (IllegalAccessException e) {
								e.printStackTrace();
								reporter.LogFAIL(driver, logger, "Error in execution of the keyword - "+ctKeyword);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
								reporter.LogFAIL(driver, logger, "Error in Arguments of the keyword -"+ctKeyword);
							} catch (InvocationTargetException e) {
								e.printStackTrace();
								reporter.LogFAIL(driver, logger, "Error in execution of "+ctKeyword+" keyword");								
								System.out.println("Error in execution of "+ctKeyword +" keyword");
								stopExe = false;
								break;	
							}catch (IllegalStateException e) {
								e.printStackTrace();
								reporter.LogFAIL(driver, logger, "Format numeric data in Excel");
							} 							
						}
						if((!keywordExistence)&(k==(method.length-1)))
						{
							reporter.LogFAIL(driver, logger,  "Keyword - "+ctKeyword+" is not available in Fucntion library");	
							break;
						}							
					
										
									}
								}
							}
				}
	//To close the browser session
	 driver.quit();	
	}

  @AfterTest
  private void tearDown() { 

	//To write to report
	setReport().flush();
	
  }

}
