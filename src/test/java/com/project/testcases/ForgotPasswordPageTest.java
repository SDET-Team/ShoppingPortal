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
import com.project.pages.ForgotPasswordPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;





/*!*******************************************************************
\file ForgotpassPageTest.java
\author Shivam Kushwaha
\date 12.10.2021	
\brief Test class for Forgot password page functionalities. All test cases related to forgot password functionalities will be executed from here.   

********************************************************************/

public class ForgotPasswordPageTest extends CommonBase {

	LoginPage loginpage;
	HomePage homepage;
	ForgotPasswordPage forgotpasspage;
	String filepath,msg;
	
	
/*!********************
	\brief Constructor which will initialize the pre-requisites to execute test cases.
	
	\bug No known bugs
	
**********************/

	public ForgotPasswordPageTest() {
		super();
		
	}
	
/*!*****************************************************************
    \brief DataProvider annotations for fetch test data from the excel sheet.
    @throws FileNotFoundException
*******************************************************************/

	@DataProvider(name = "testdata")
	public   Object[][] getPositiveData(Method m) throws IOException {
		String type="Negative";
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\ForgotPasswordTestdata.xlsx";
		if (m.getName().equals("validatepositiveForgotPassword"))
			type="Positive";
		Object data[][] = TestUtils.getTestData(filepath,type);
		return data;
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
		\brief function initialize the properties of the webdriver and will navigate to forgot password page from the homepage of the AUT.
		@throws WebDriverException
		@throws Assertion error
		
**********************/
	
	@BeforeClass
	public void setup() {
		
		
		initialization();
		
		homepage = new HomePage(driver);
		loginpage=new LoginPage(driver);
		forgotpasspage=new ForgotPasswordPage(driver);
		
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
		
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup",
				"Login Page Title Not Matched");
		loginpage.navigateToForgotpass();
	}
	

/*!*****************************************************************
    \brief ValidateTitle() will verify the title of forgot password page. 
   
    @throws Assertion Error
*******************************************************************/

	@Test(priority = 1)
	public void validateTitle() {
		log.info("Validating Loginpage Title");
		String title = forgotpasspage.title();
		Assert.assertEquals(title, "Shopping Portal | Forgot Password", "Forgot Password Page Title Not Matched");
		log.info("Testcase Passed!");
	}

	
	
/*!*************************************************
	   \brief This method will validate whether user can change their password by providing correct EmailID and Password.
	   \n Positive test cases.
	   \nTest data is fetched from a excel sheet.
	   \n File name "forgotpassTestData.xlsx" Sheet name "Positive Testdata"
	   \n Columns names {"email","contactno","newpass","confirmpass"} 
	   
	   @throws AssertionError
***************************************************/
	
	@Test(priority=2,dataProvider="testdata")
	public  void validatepositiveForgotPassword(String email,String contact,String newpass, String cpass)
	{
		log.info("Validating Forgot password operation by providing valid emailid and contact number");
		log.info("User details: Email: "+email+" , ContactNo: "+contact);
		msg=forgotpasspage.changepassword(email, contact, newpass, cpass);
		
		Assert.assertEquals(msg,"Password Changed Successfully",msg);
		log.info("Testcase Passed!");
	}
	
/*!*************************************************
	   \brief This method will validate that user should not be able to change their password by providing incorrect EmailID or Password.
	   \n Negative test cases.
	   \nTest data is fetched from a excel sheet.
	   \n File name "forgotpassTestData.xlsx" Sheet name "Negative Testdata"
	   \n Columns names {"email","contactno","newpass","confirmpass"} 
	   
	   @throws AssertionError
***************************************************/
	
	@Test(priority=3,dataProvider="testdata")
	public  void validatenegativeForgotPassword(String email,String contact,String newpass, String cpass)
	{
		log.info("Validating Forgot password operation by providing invalid emailid or contact number");
		log.info("User details: Email: "+email+" , ContactNo: "+contact);
		msg=forgotpasspage.changepassword(email, contact, newpass, cpass);
		
		
		if(TestUtils.isVisible(forgotpasspage.invaliduserElement()))
			Assert.assertEquals(msg,"Invalid email id or Contact no",msg);
		else if(!newpass.equals(cpass))
			Assert.assertEquals(msg,"New password and Confirm password should be same!!",msg);
		else
			Assert.assertEquals(msg, "Invalid User or Password not match",msg);
		log.info("Testcase Passed!");
	
	}



    /*!***********************************************
	  \brief Driver is closed.
	*************************************************/
	@AfterClass
	public  void tearDown() {
		log.info("Closing browser");

		driver.close();
	}

}
