package DriverPack;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author - Durga Suresh
 * Description - Functions supporting the creation of report.
 *
 */
public class TestReport {	
	
	public  String testCaseID;
	public  ExtentReports report;
	public  ExtentTest logger;
	public WebDriver driver;
	public String filePath =  ".\\src\\DriverPack\\";
	public String fileName = "TestSheet.xlsx";
	public String reportName;
	public String screenShotName;
	
	
	//(@Author:Durga)To generate time stamp
	public String timeStampGenerator()
	{
		DateFormat dateForm = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss.SS");
		Date date = new Date();
		String Date = dateForm.format(date);
		return Date;		
	}
	
	//(@Author:Durga)To generate report name based on time of execution
	public String reportNameGenerator()
	{
		String reportName = "AutomationReport-"+timeStampGenerator();
		return reportName;		
	}
	
	//(@Author:Durga)To generate Screenshot name based on time of execution
	public String screenShotNameGenerator()
	{
		String screenShotName = "Image-"+timeStampGenerator();
		return screenShotName;		
	}
	
	//(@Author:Durga)To generate report path based on time stamp
	public String reportPathGen()
	{
		//String reportPath = "C:/Users/DSuresh/Desktop/AutomationResult/Log/"+reportNameGenerator()+".html"; //-- To store in local
		String reportPath = "./Report/Logs/"+reportNameGenerator()+".html";	//-- Stores in Framework	
		return reportPath;		
	}
	
	//(@Author:Durga)To generate image path based on time stamp	
	public String imagePathGen()
	{
		//String imagePath = "C:/Users/DSuresh/Desktop/AutomationResult/Screenshot/"+screenShotNameGenerator()+".png"; //-- To store in local
		String imagePath = "./Report/Screenshots/"+screenShotNameGenerator()+".png";	//-- Stores in Framework	
		return imagePath;		
	}
	
	
	//(@Author:Durga;@Param:driver;@Return:Image Path)To capture screen shot and return the absolute path of the same with Webdriver as parameter
	public String CaptureScreenshot(WebDriver driver)
	{	String absolutePath = null;		
		try {			
			String screenshotPath = imagePathGen();
			TakesScreenshot Capture = ((TakesScreenshot)driver);		
			File source = Capture.getScreenshotAs(OutputType.FILE);
			File capturePath = new File(screenshotPath);		
			FileUtils.copyFile(source,capturePath); 			
			absolutePath = capturePath.getCanonicalPath();//to get the absolute path of the screenshot		
			} catch (Exception e) {				
			//e.printStackTrace();
		}return absolutePath;		
	}
	
	
	//(@Author:Durga;@Param:driver;@Return:Image Path)To scroll and capture screen shot and return the absolute path of the same 
		public String ScrollCaptureScreenshot(WebDriver driver,WebElement element)
		{	String absolutePath = null;		
			try {			
				String screenshotPath = imagePathGen();
				Thread.sleep(3000L);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				int yPosition = element.getLocation().getY();
				js.executeScript("window.scroll (0, " + yPosition + ") ");       
				Thread.sleep(3000L);   
				TakesScreenshot Capture = ((TakesScreenshot)driver);		
				File source = Capture.getScreenshotAs(OutputType.FILE);
				File capturePath = new File(screenshotPath);		
				FileUtils.copyFile(source,capturePath); 			
				absolutePath = capturePath.getCanonicalPath();//to get the absolute path of the screenshot	
				
				} catch (Exception e) {				
				//e.printStackTrace();
			}return absolutePath;		
		}
		
	
	//(@Author:Durga;@Param:driver,logger,message)To report as PASS
	public void LogPASS(WebDriver driver,ExtentTest logger,String message)
	{
		String screenCapturePath = CaptureScreenshot(driver);
		logger.log(LogStatus.PASS, message + logger.addScreenCapture(screenCapturePath));			
	}
	
	//(@Author:Durga;@Param:driver,logger,message)To report as FAIL
	public void LogFAIL(WebDriver driver,ExtentTest logger,String message)
	{
		String screenCapturePath = CaptureScreenshot(driver);
		logger.log(LogStatus.FAIL, message + logger.addScreenCapture(screenCapturePath));	
	}

	//(@Author:Durga;@Param:driver,logger,message)To report as INFO
	public void LogINFO(ExtentTest logger,String message)
	{
		logger.log(LogStatus.INFO, message);			
	}
	
	//(@Author:Durga;@Param:logger,message)To report as PASS without IMG
	public void LogPASSmsg(ExtentTest logger,String message)
	{
		
		logger.log(LogStatus.PASS, message );			
	}
	
	//(@Author:Durga;@Param:logger,message)To report as FAIL without IMG
	public void LogFAILmsg(ExtentTest logger,String message)
	{
		
		logger.log(LogStatus.FAIL, message );	
	}
	
	//(@Author:Durga;@Param:driver,logger,message)To report as PASS with scrolled screenshot
	public void LogPASSSS(WebDriver driver,ExtentTest logger,String message,WebElement element)
	{
		String screenCapturePath = ScrollCaptureScreenshot(driver, element);
		logger.log(LogStatus.PASS, message + logger.addScreenCapture(screenCapturePath));			
	}
	
	//(@Author:Durga;@Param:driver,logger,message)To report as FAIL with scrolled screenshot
	public void LogFAILSS(WebDriver driver,ExtentTest logger,String message,WebElement element)
	{
		String screenCapturePath = ScrollCaptureScreenshot(driver, element);
		logger.log(LogStatus.FAIL, message + logger.addScreenCapture(screenCapturePath));	
	}
	
	//Hard AssertFunction
	public void hardAssert(String expected,String actual,ExtentTest logger,String passMsg,String failMsg,WebDriver driver)
	{		
		try {
			Assert.assertEquals(expected,actual);
			LogPASS(driver,logger,passMsg);
		} catch (Exception e) {
			e.printStackTrace();
			LogFAIL(driver,logger,failMsg);
			driver.quit();
			
		}
	}
	public void hardAssert(Boolean condition,ExtentTest logger,String passMsg,String failMsg,WebDriver driver)
	{
		try {
			Assert.assertTrue(condition);
			LogPASS(driver,logger,passMsg);
		} catch (Exception e) {
			e.printStackTrace();
			LogFAIL(driver,logger,failMsg);
			driver.quit();
			
		}
	}
	//Soft AsserFunction
	public void softAssert(Boolean condition,ExtentTest logger,String passMsg,String failMsg,WebDriver driver)
    {
		if(condition)
		{	
			LogPASS(driver,logger,passMsg);
		}else
		{
			LogFAIL(driver,logger,failMsg);
		}		
	}
	public void softAssert(String expected,String actual,ExtentTest logger,String passMsg,String failMsg,WebDriver driver)
	{
		if(expected.contains(actual))
		{	
			LogPASS(driver,logger,passMsg);
		}else
		{
			LogFAIL(driver,logger,failMsg);				
		}
	}
		
	

	
}



