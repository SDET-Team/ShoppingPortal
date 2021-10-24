package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;


/*!*******************************************************************
\file LoginPageTest.java
\author Shivam Kushwaha
\date 11.10.2021	
\brief Main class for User Login page functionalities. All test cases related to user login will be executed from here.   

********************************************************************/

public class LoginPageTest extends CommonBase {

	LoginPage loginpage;
	HomePage homepage;
	String filepath;
	String type, expected, actual, status, msg;
	
	/*!********************
	\brief Constructor which will initialize the pre-requisites to execute test cases.
	
	\bug No known bugs
	
**********************/

	public LoginPageTest() {
		super();
	}

	@BeforeSuite(groups = "Log")
	public void loginit() {
		logConfig();
	}

	/*!********************
	\brief Driver will be initialized and url for navigating to website will be fetched.
	\throws WebDriverException
**********************/
	@BeforeClass(groups = "BrowserActivity")
	public void setup() {

	
		initialization();
		homepage = new HomePage(driver);
		loginpage = new LoginPage(driver);
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
	}
	
	/*!*****************************************************************
    \brief DataProvider annotations for fetch test data from file
    @throws FileNotFoundException
*******************************************************************/

	@DataProvider(name = "testdata")
	public  Object[][] getpositivetestData(Method m) throws IOException {
		String type="Negative";
		if (m.getName().equals("validatecorrectLogin") || m.getName().equals("validateincorrectLogin"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\LoginTestdata.xlsx";
		else if (m.getName().equals("validatecorrectRegistration")
				|| m.getName().equals("validateincorrectRegistration"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\RegistrationTestdata.xlsx";
		if (m.getName().equals("validatecorrectLogin") || m.getName().equals("validatecorrectRegistration"))
			type = "Positive";
		Object data[][] = TestUtils.getTestData(filepath, type);
		return data;
	}
	
	

    /*!*****************************************************************
	    \brief ValidateTitle() will verify the title of login page. 		
	    @throws 
	*******************************************************************/

	@Test(priority = 1)
	public void validateTitle() {

		log.info("Validating Loginpage Title");
		String title = loginpage.loginpageTitle();
		Assert.assertEquals(title, "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched");
		log.info("Testcase Passed!");
	}
	
	/*!*************************************************
	   \brief This method will validate whether user can login using correct credentials.
	   \n Positive test cases.
	   \nTest data is fetched from a excel sheet.
	   \n File name "loginTestData.xlsx" Sheet name "Positive Testdata"
	   \n Columns names {"Email","Password"} 
	   
	   @throws AssertionError
***************************************************/

	@Test(priority = 2, dataProvider = "testdata", groups = "Login")
	public void validatecorrectLogin(String email, String password) {
		log.info("Validating Login operation using correct Credentials");
		log.info("Logging using email: " + email + " , Password: " + password);
		msg = loginpage.loginOperation(email, password);
		Assert.assertEquals(msg, "Welcome", "Authentication Failed!!!");
		log.info("Testcase Passed!");
		navafterLogin.logout();
		Assert.assertEquals(driver.getTitle(), "Shopping Portal Home Page", "Logout operation failed!!!");
		log.info("User logged out!!");
		navbeforeLogin.navigatetologin();

	}
	
	/*!*************************************************
	   \brief This method will validate whether user can login using incorrect credentials.
	   \n Negative test cases.
	   Test data is fetched from a excel sheet.
	   \n File name "loginTestData.xlsx" Sheet name "Negative Testdata"
	   \n Columns names {"Email","Password"} 
	   
	   @throws AssertionError
***************************************************/

	@Test(priority = 3, dataProvider = "testdata", groups = "Login")
	public void validateincorrectLogin(String email, String password) {
		log.info("Validating Login operation using incorrect Credentials");
		log.info("Logging using email: " + email + " , Password: " + password);
		msg = loginpage.loginOperation(email, password);

		if (msg.equals("Welcome")) {
			navafterLogin.logout();
			navbeforeLogin.navigatetologin();

		}
		Assert.assertEquals(msg, "Invalid email id or Password", "Logged in using incorrect credentials!!!!");
		log.info("Testcase Passed!");

	}
	
	 /*!*************************************************
	   \brief This method will validate whether forgot password page link is broken or not.
	   
	
***************************************************/ 

	@Test(priority = 4)
	public  void validateForgotpassNav() {
		log.info("Validating that link for forgot password page is not broken");
		loginpage.navigateToForgotpass();
		String title = loginpage.forgotpassTitle();
		Assert.assertEquals(title, "Shopping Portal | Forgot Password", "Forgot Password Page Title Not Matched");
		log.info("Testcase Passed!");
		navbeforeLogin.navigatetologin();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup",
				"Login Page Title Not Matched");
	}


	/*!*************************************************
	   \brief This method will validate whether user can register using correct credentials.
	   \n Positive test cases.
	   \nTest data is fetched from a excel sheet.
	   \n File name "ForgotPasswordTestData.xlsx" Sheet name "Positive Testdata"
	   \n Columns names {"email","contactno","newpass","confirmpass"} 
	   
	   @throws AssertionError
***************************************************/

	@Test(priority = 4, dataProvider = "testdata", groups = "Registration")
	public void validatecorrectRegistration(String fullname, String email, String contact, String newpass,String cpass) {
		log.info("Validating registration operation by providing all correct details");

		msg = loginpage.regOperation(fullname, email, contact, newpass, cpass);
		Assert.assertEquals(msg, "You are successfully register", msg);
		log.info("Testcase Passed!");
	}

	
	/*!*************************************************
	   \brief This method will validate whether user can register using invalid credentials.
	   \n Negative test cases.
	   \nTest data is fetched from a excel sheet.
	   \n File name "ForgotPasswordTestData.xlsx" Sheet name "Negative Testdata"
	   \n Columns names {"email","contactno","newpass","confirmpass"} 
	   
	   @throws AssertionError
***************************************************/
	
	@Test(priority=5, dataProvider="testdata",groups="Registration")
	public void  validateincorrectRegistration(String fullname,String email,String contact,String newpass,String cpass)
	{

		log.info("Validating registration operation by providing some incorrect details");
		msg = loginpage.regOperation(fullname, email, contact, newpass, cpass);

		if (TestUtils.isVisible(loginpage.duplicateEmailElement()))
			Assert.assertEquals(msg, "Email already exists .", msg);
		else if (!newpass.equals(cpass))
			Assert.assertEquals(msg, "Password and Confirm Password Field do not match  !!", msg);
		else if (contact.length() != 10)
			Assert.assertEquals(msg, "Contact no.length should be equal to 10", msg);
		

		log.info("Testcase Passed!");

	}
	

    /*!***********************************************
	  \brief Driver is closed.
	*************************************************/

	@AfterClass(groups = "BrowserActivity")
	public void tearDown() {

		log.info("Closing the Browser");
		driver.close();
	}

}