package TestPages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import DriverPack.TestWait;


//objects and methods for pages before login
public class BeforeLoginPage {
	
		
		WebDriver driver;	
		public TestWait wait = new  TestWait();
		public BeforeLoginPage(WebDriver Idriver)
		{
		this.driver = Idriver;	
		}
		
		//*********************************************************************
		@FindBy(xpath = "//a[text()='login']") public WebElement linkLogin;
		@FindBy(xpath = "//*[@href='#/register']") public WebElement linkRegister;	
		@FindBy(xpath = "//*[@id='username']") public WebElement editUsername;
		@FindBy(xpath = "//*[@id='password']") public WebElement editPassword;
		@FindBy(xpath = "//*[@class='btn btn-primary ng-scope']")public WebElement buttonAuthenticate;
		
		//*********************************************************************
	
		//Go to Login page
		public void gotoLoginPage()
		{
			linkLogin.click();			
		}
		
		//Go to register page
		public void gotoRegisterPage(String userID,String passWord)
		{
			linkRegister.click();			
		}
		
		//Login
		public void Login(String username,String password)
		{
			gotoLoginPage();
			wait.WaitforMaxTime(driver, editUsername);
			editUsername.sendKeys(username);
			editPassword.sendKeys(password);
			buttonAuthenticate.click();		
		}
		//check beforelogin page
		public Boolean beforeLoginPage()
		{
			Boolean flag = false;
			if(linkLogin.isDisplayed())
			{
				flag =true;
			}
			return flag;
		}
			
		}
	
		
