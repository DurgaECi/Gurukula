
package DriverPack;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class TestWait {
	
	public WebDriverWait wait;
	int minTimeMs = 20;
	int maxTimeMs = 50;
	
	public void WaitforMinTime(WebDriver driver,WebElement object)
	{
	
	wait = new WebDriverWait(driver, minTimeMs);
	wait.until(ExpectedConditions.elementToBeClickable(object));
	
	}
	
	public void WaitforMaxTime(WebDriver driver,WebElement object)
	{
	
	wait = new WebDriverWait(driver, maxTimeMs);
	wait.until(ExpectedConditions.elementToBeClickable(object));
	}
	
	public void Wait(WebDriver driver) throws InterruptedException 
	{
		wait = new WebDriverWait(driver, maxTimeMs);
		wait.wait(minTimeMs);
		
	}
	  public void WaitforPage(WebDriver driver) 
	  {
		  driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	  }
}
