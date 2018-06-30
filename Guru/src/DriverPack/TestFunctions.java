package DriverPack;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.testng.asserts.SoftAssert;

import TestPages.AfterLoginPage;
import TestPages.BeforeLoginPage;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class TestFunctions{
	
	private TestData data;
	private WebDriver driver;
	private TestReport report = new TestReport();
	public TestWait wait = new  TestWait();
	private BeforeLoginPage blp;
	private AfterLoginPage alp;
	private String AppUrl = "http://192.168.2.6:8080/#/";	
	
	
	protected TestFunctions(TestData testData,WebDriver driver)
	{
		this.data = testData;	
		this.driver = driver; 
		this.blp = PageFactory.initElements(driver, BeforeLoginPage.class);
		this.alp = PageFactory.initElements(driver, AfterLoginPage.class);
	}
	
	//-------------------Assertion functions---------------------
	//-------------------------------------------------------------
		
	//Open Gurukula Portal
	public void LaunchApp(String tcId, ExtentTest logger) throws InterruptedException
	{
		driver.get(AppUrl);
		wait.WaitforMinTime(driver, blp.linkLogin);
		report.hardAssert("gurukula", driver.getTitle(), logger,"Gurukula portal has been Launched", "Failed to launch Gurukula Portal", driver);
	}	
	
	//LoginPage	
	public void Login(String tcId, ExtentTest logger)
	{
		blp.Login(data.getTestData(tcId, "LoginID"),data.getTestData(tcId, "Password"));	
		wait.WaitforMinTime(driver, alp.dropdownEntities);
		report.hardAssert("You are logged in as user \"admin\".", alp.GetLoginMsg(),logger,"User has Logged In successfully","User Failed to Login", driver);
	}
	
	//BranchPage
	public void ValidateBranch(String tcId, ExtentTest logger)
	{	
		String branchName = data.getTestData(tcId,"BranchName");
		String branchCode = data.getTestData(tcId,"BranchCode");
		//go to branch page
		alp.GotoBranchPage(AppUrl);
		report.hardAssert(driver.getCurrentUrl().contains("branch"),logger,
				"Branch Page has opened","Failed to open Branch Page", driver);
		//Create Branch
		if(alp.CreateBranch(branchName,branchCode))
		{
		report.LogPASSmsg(logger, "Branch with Name - "+branchName+" has been created");
		}
		//Query branch with branch name
		String valueB = alp.QueryBranchStaff(branchName,"Name");		
		report.hardAssert(branchName,valueB , logger,
				"Branch with Name - "+branchName+" has been fetched by Name query successfully ", "Failed to query the branch", driver);
		//Query branch with BranchCode		
		String valueC = alp.QueryBranchStaff(branchCode,"Code");
		report.softAssert(branchCode,valueC, logger, 
				"Branch with  Code -"+branchCode+" has been queried with Code and fetched successfully", "Failure in querying with Code", driver);
		//View created branch
		String viewBranch = alp.ViewBranchStaff();
		report.softAssert(!(viewBranch==null), logger,
				"Branch -"+viewBranch+" has been Viewed", "Failed to view branch", driver);
		alp.buttonSave.click();
		//Edit created branch		
		String editBranch = branchName+"edited";		
		String edited = alp.EditBranchStaff(branchName,editBranch);
		report.softAssert(editBranch,edited,logger, 
				"Branch -"+branchName+" has been Edited as -"+editBranch,"Failed to Edit the branch", driver);
		//Delete created branch
		boolean flag2 = alp.DeleteBranchStaff(editBranch);
		report.softAssert(flag2, logger, 
				"Branch -"+editBranch+" has been Deleted", "Branch was not deleted", driver);	
	}
	
	//StaffPage
	public void ValidateStaff(String tcId, ExtentTest logger) throws InterruptedException
	{
		
		String staffName =data.getTestData(tcId, "StaffName"); 
		//Create a branch to map it to staff
		String branchName = data.getTestData(tcId,"BranchName");
		String branchCode = data.getTestData(tcId,"BranchCode");
		//go to branch page//Create Branch
		alp.GotoBranchPage(AppUrl);		
		alp.CreateBranch(branchName,branchCode);
		report.LogPASSmsg(logger, "Branch - "+branchName+" has been created to be linked to staff ");
		String valueB = alp.QueryBranchStaff(branchName,"Name");
		report.hardAssert( branchName ,valueB , logger,
				"Branch - "+branchName+" has been fetched by query ", "Failed to quert Branch ", driver);
		//go to Staff page
		alp.GotoStaffPage(AppUrl);
		report.hardAssert(driver.getCurrentUrl().contains("staff"),logger,
				"Staff Page has opened","Failed to open Staff Page", driver);
		//Create Staff
		if(alp.CreateStaff(staffName, branchName))				
		{
		report.LogPASSmsg(logger, "Staff - "+staffName+" has been created ");
		}
		//Query branch with Staff name
		String valueS = alp.QueryBranchStaff(staffName,"Name");
		report.hardAssert(staffName,valueS , logger,
				"Staff - "+staffName+" has been fetched by Name query successfully ", "Failed to create Staff and query it", driver);
		//View created Staff
		String viewStaff = alp.ViewBranchStaff();
		report.softAssert(!(viewStaff==null), logger,
				"Staff -"+viewStaff+" has been Viewed", "Failed to view Staff", driver);
		alp.buttonSave.click();
		//Edit created Staff		
		String editStaff = staffName+"edited";		
		String edited = alp.EditBranchStaff(staffName,editStaff);
		report.softAssert(editStaff,edited,logger, 
				"Staff -"+staffName+" has been Edited as -"+editStaff,"Failed to Edit the Staff", driver);
		//Delete created Staff
		boolean flag = alp.DeleteBranchStaff(editStaff);
		report.softAssert(flag, logger, 
				"Staff -"+editStaff+" has been Deleted", "Staff was not deleted", driver);	
		
		//Delete created branch
		alp.GotoBranchPage(AppUrl);
		boolean flag1 = alp.DeleteBranchStaff(branchName);
		report.softAssert(flag1, logger, 
				"Branch -"+branchName+" has been Deleted", "Branch was not deleted", driver);	

	}
	
	//Logout	
	public void Logout(String tcId, ExtentTest logger)
	{
		alp.Logout();		
		report.softAssert(blp.beforeLoginPage(),logger,"User has Logged Out successfully","User Failed to Logout", driver);
	}
	
	
	public void CreateDuplicateStaffs(String tcId, ExtentTest logger)
	{
		String staffName = data.getTestData(tcId, "StaffName"); 
		String branchName = data.getTestData(tcId,"BranchName");
		String branchCode = data.getTestData(tcId,"BranchCode");
		//go to branch page//Create Branch
		alp.GotoBranchPage(AppUrl);		
		alp.CreateBranch(branchName,branchCode);
		report.LogPASSmsg(logger, "Branch - "+branchName+" has been created to be linked to staff ");
		//go to Staff page
		alp.GotoStaffPage(AppUrl);
		
		for(int i=0;i<5;i++)
		{
			//Create Staff
			if(alp.CreateStaff(staffName, branchName))				
			{
			report.LogPASSmsg(logger, "Staff - "+staffName+" has been created ");
			}
			String valueS = alp.QueryBranchStaff(staffName,"Name");
			report.softAssert(staffName,valueS , logger,
					"Staff - "+staffName+" has been fetched by Name query", "Failed to create Staff and query it", driver);
		}
	}
	
	public void CreateDuplicateBranches(String tcId, ExtentTest logger)
	
	{ 
		String branchName = data.getTestData(tcId,"BranchName");
		String branchCode = data.getTestData(tcId,"BranchCode");
		
		alp.GotoBranchPage(AppUrl);
		
		
		for(int i=0;i<5;i++)
		{
			//Create branch
			if(alp.CreateBranch(branchName,branchCode))				
			{
			report.LogPASSmsg(logger, "Branch - "+branchName+" has been created ");
			}
				
			String valueB = alp.QueryBranchStaff(branchName,"Name");
			report.softAssert(branchName,valueB , logger,
					"Branch "+branchName+" has been fetched by Name query ", "Failed to query ", driver);
		
		}
	}

	
	
}
