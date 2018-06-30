package TestPages;

import java.lang.reflect.Array;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import DriverPack.TestData;
import DriverPack.TestFunctions;
import DriverPack.TestWait;

public class AfterLoginPage {


	public TestWait wait = new  TestWait();	
	private SoftAssert softAssertion= new SoftAssert();
	private WebDriver driver;
	public AfterLoginPage(WebDriver Idriver)
	{
	this.driver = Idriver;	
	}
	
	//*********************************************************************

	@FindBy(xpath ="//*[@class='alert alert-success ng-scope ng-binding']")WebElement textWelcomeMsg;
	@FindBy(xpath = "//span[text()='Entities']") public WebElement dropdownEntities;
	@FindBy(xpath = "//span[text()='Account']") public WebElement dropdownAccount;
	@FindBy(xpath = "//*[text()='Log out']") public WebElement optionLogout;
	@FindBy(xpath = "//*[text()='Branch']") public WebElement optionBranch;
	@FindBy(xpath = "//*[text()='Staff']") public WebElement optionStaff;
	@FindBy(xpath = "//*[@class='btn btn-primary']") public WebElement buttonCreateBranchStaff;
	@FindBy(xpath = "//input[@name='name']") public WebElement editBranchStaffName;
	@FindBy(xpath = "//input[@name='code']") public WebElement editBranchStaffCode;
	@FindBy(xpath = "//button[@type='submit']") public WebElement buttonSave;
	@FindBy(xpath = "//select[@name='related_branch']") public WebElement dropdownBranchName;
	@FindBy(id = "searchQuery") public WebElement editQuery;
	@FindBy(xpath = "//button[@class='btn btn-info']") public WebElement buttonSearch;
	@FindBy(xpath = "//table[@class='table table-striped']") public WebElement tableBranchStaff;
	@FindBy(xpath = "//table[@class='table table-striped']//tbody/tr") public List<WebElement> rowsTable;
	@FindBy(xpath = "//tr[1]/td/button//span[text()='View']") public WebElement buttonView;
	@FindBy(xpath = "//tr[1]/td/button//span[text()='Edit']") public WebElement buttonEdit;
	@FindBy(xpath = "//tr[1]/td/button//span[text()='Delete']") public WebElement buttonDelete;
	@FindBy(xpath = "//form[@name='deleteForm']//button[@class='btn btn-danger']") public WebElement buttonpopupDelete;
	@FindBy(xpath = "//tr[1]/td/input[@class='input-sm form-control']") public WebElement editViewBranchName;
	@FindBy(xpath = "//select[@name='related_branch']") public WebElement dropdownRelatedBranch;
	
	//*********************************************************************
	
	
	//Verify if user is logged in
	public String GetLoginMsg()
	{
	return(textWelcomeMsg.getText());	
	}
	
	//Go to branch page
	public void GotoBranchPage(String url)
	{		
		
		driver.get(url+"branch");
		wait.WaitforPage(driver);
	}
	
	
	//Go to Staff page
	public void GotoStaffPage(String url)
	{			
		driver.get(url+"staff");
		wait.WaitforPage(driver);
					
	}
	
	//Create branch
	public boolean CreateBranch(String Name,String Code)
	{
		boolean flag = false;
		  boolean clicked = false;
		int beforeRowNos = tableRows();
		do{
            try {
            	if(buttonCreateBranchStaff.isDisplayed())
            	{
            		buttonCreateBranchStaff.click();
            	}else
            	{
            		wait.WaitforMinTime(driver, editQuery);
            	}
            } catch (WebDriverException e) {
                continue;
            } finally {
                clicked = true;
            }
        } while (!clicked);	  
		
		
		wait.WaitforMinTime(driver, editBranchStaffName);
		editBranchStaffName.sendKeys(Name);
		editBranchStaffCode.sendKeys(Code);
		wait.WaitforMinTime(driver, buttonSave);
		buttonSave.click();
		wait.WaitforPage(driver);
		wait.WaitforMaxTime(driver, buttonCreateBranchStaff);
		int afterRowNos = tableRows();
		int Nos[]={afterRowNos,beforeRowNos};
		if(Nos[0]==Nos[1]+1)
		{
			flag = true;
		}
		return(flag);
	}
	
	//Create staff
	public boolean CreateStaff(String staffName,String branchName)
	{
		int beforeRowNos = tableRows();
		wait.WaitforMaxTime(driver, buttonCreateBranchStaff);
		buttonCreateBranchStaff.click();
		editBranchStaffName.sendKeys(staffName);
		selectRelatedBranch(branchName);
		buttonSave.click();
		wait.WaitforPage(driver);
		wait.WaitforMaxTime(driver, buttonCreateBranchStaff);
		int afterRowNos = tableRows();
		int Nos[]={afterRowNos,beforeRowNos};
		boolean flag = false;
		if(Nos[0]==Nos[1]+1)
		{
			flag = true;
		}
		return(flag);
	}
	
	//Query with Name/code
	public String QueryBranchStaff(String nameCode,String coloumnName) 
	{
		wait.WaitforMaxTime(driver, editQuery);
		editQuery.clear();
		editQuery.sendKeys(nameCode);		
		  boolean clicked = false;
	        do{
	            try {
	            	if(buttonSearch.isDisplayed())
	            	{
	        		buttonSearch.click();
	            	}else
	            	{
	            		wait.WaitforMinTime(driver, editQuery);
	            	}
	            } catch (WebDriverException e) {
	                continue;
	            } finally {
	                clicked = true;
	            }
	        } while (!clicked);	        
		
		wait.WaitforPage(driver);
		
		String value = coloumnValue(coloumnName);
		return value;
	}
	
	//To return the coloumn value of first Row and Column name[Name/Code] from branch/staff table
	public String coloumnValue(String Name)
	{
		
		List<WebElement> coloumnHeaders = tableBranchStaff.findElements(By.xpath("//tr/th"));
		String coloumnValue = null;
		for(int i=0;i<coloumnHeaders.size();i++)
		{
			if(((coloumnHeaders.get(i).getText()).equals(Name)))
			{
				if(tableRows()>0)
				{
				coloumnValue = tableBranchStaff.findElement(By.xpath("//tr[1]/td["+(i+1)+"]")).getText();
				}
				break;
						
			}
		}
		return coloumnValue;	
	}
	
	//To return the number of table rows
	public int tableRows()
	{		
		int rowNos = rowsTable.size();			
		return rowNos;	
	}

	//View branch/code from first row
	public String ViewBranchStaff()
	{		
		buttonView.click();
		String value=editViewBranchName.getAttribute("value"); 
		return(value);
		
	}
	
	//Edit branch/code from first row
	public String EditBranchStaff(String branchName, String editBranch)
	{		
		QueryBranchStaff(branchName,"Name");
		buttonEdit.click();
		editBranchStaffName.clear();
		editBranchStaffName.sendKeys(editBranch);
		buttonSave.click();
		wait.WaitforMinTime(driver, buttonSearch);
		QueryBranchStaff(editBranch,"Name");
		return coloumnValue("Name");		
	}
	
	//Delete branch/code from first row
	public boolean DeleteBranchStaff(String branchName)
	{	QueryBranchStaff(branchName,"Name");
		int beforeRowNos = tableRows();
		buttonDelete.click(); 	
		wait.WaitforMaxTime(driver, buttonpopupDelete);
		buttonpopupDelete.click();
		QueryBranchStaff(branchName,"Name");
		int afterRowNos = tableRows();
		int Nos[]={afterRowNos,beforeRowNos};
		boolean flag = false;
		if(Nos[0]==Nos[1]-1)
		{
			flag = true;
		}
		return(flag);
				
	}
	
	//select the branch from dropdown in creation of staff
	public void selectRelatedBranch(String branchName)
	{
		Select branches = new Select(dropdownRelatedBranch);
		branches.selectByVisibleText(branchName);
	}
	
	//Logout
	public void Logout()
	{
		driver.navigate().refresh();
		wait.WaitforMinTime(driver, dropdownAccount);
		dropdownAccount.click();
		optionLogout.click();
	}
	
	
	
	
	
	
	

}
