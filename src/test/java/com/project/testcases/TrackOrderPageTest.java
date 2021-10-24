package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterClass; 

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.TrackOrderPage;
import com.project.utils.TestUtils;

public class TrackOrderPageTest extends CommonBase {

	HomePage homepage;
	String filepath;
	TrackOrderPage trackorderpage;
	
	

/*!*******************************************************************
\file TrackOrderPageTest.java
\author Shivam Kushwaha
\date 12.10.2021	
\brief Test class for track order page functionalities. All test cases related to Track order will be executed from here.   

********************************************************************/

	
	
/*!********************
	\brief Constructor which will initialize the pre-requisites to execute test cases.
	
	\bug No known bugs
	
**********************/
	public TrackOrderPageTest() {
		super();
		
	}
	
	
	
/*!********************
	\brief function to initialize the properties of the Log4J logger
	
	\bug No known bugs
	
**********************/
	
	@BeforeSuite(groups="Log")
	public void loginit()
	{
		logConfig();
	}
	

	
/*!********************
	\brief function initialize the properties of the webdriver and will navigate to Track order page from the homepage of the AUT.
	@throws WebDriverException
	@throws Assertion error
	
**********************/
	@BeforeClass
	public void setup() {
		
	
		
		initialization();
		
		homepage = new HomePage(driver);
		trackorderpage=new TrackOrderPage(driver);
		
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
		navbeforeLogin.clickontrackorder();
	}

	
/*!*****************************************************************
    \brief DataProvider annotations for fetch test data from the excel sheet.
    @throws FileNotFoundException
*******************************************************************/
	
	@DataProvider(name = "testdata")
	public  Object[][] getPositiveData(Method m) throws IOException {
		String type="Negative";
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\TrackorderTestdata.xlsx";
		if (m.getName().equals("validatePositiveTrackorderOperation"))
			type="Positive";
		Object data[][] = TestUtils.getTestData(filepath,type);
		return data;
	}
	

	/*!*****************************************************************
    \brief ValidateTitle() will verify the title of Track order page. 
   
    @throws Assertion Error
*******************************************************************/

	@Test(priority=1)
	public  void validateTitle()
	{	log.info("Validating Loginpage Title");
		String title=trackorderpage.title();
		Assert.assertEquals(title, "Track Orders","Title Not Matched");
		log.info("Testcase Passed!");
		
	}
	
	
/*!*************************************************
	   \brief This method will validate whether user can track order by providing correct orderid and emailid.
	   \n Positive test cases.
	   \nTest data is fetched from a excel sheet.
	   \n File name "trackorderTestData.xlsx" Sheet name "Positive Testdata"
	   \n Columns names {"orderid","emailid"}
	   
	   @throws AssertionError
***************************************************/
	@Test(priority=2,dataProvider="testdata")
	public void validatePositiveTrackorderOperation(String orderid,String emailid)
	{
		log.info("Validating Tracking order operation by providing correct emailid and orderid");
		log.info("Order details: Orderid: "+orderid+" , Emailid: "+emailid);
		int col=trackorderpage.trackingOption(orderid, emailid);
		Assert.assertEquals(col,9,"Error message displayed for correct orderid and emailid!!!");
		log.info("Testcase Passed!");
		
	}
	

	/*!*************************************************
	   \brief This method will validate that user should not be able to change their password by providing incorrect EmailID or Password.
	   \n Negative test cases.
	   \nTest data is fetched from a excel sheet.
	   \n  File name "trackorderTestData.xlsx"  Sheet name "Negative Testdata"
	   \n Columns names Columns names {"orderid","emailid"}
	   
	   @throws AssertionError
***************************************************/
	@Test(priority=3,dataProvider="testdata")
	public  void validateNegativeTrackorderOperation(String orderid,String emailid)
	{
		log.info("Validating Tracking order operation by providing incorrect emailid or orderid");
		log.info("Order details: Orderid: {0} , Emailid: {1}");
		int col=trackorderpage.trackingOption(orderid, emailid);
		Assert.assertEquals(col,1,"No error message displayed for incorrect orderid or emailid!!");
		log.info("Testcase Passed!");
	}

	
/*!***********************************************
	  \brief Driver is closed.
*************************************************/
	
	@AfterClass
	public void tearDown() {
		log.info("Closing Browser");
		driver.close();
	}

}
