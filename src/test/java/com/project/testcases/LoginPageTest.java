package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;

public class LoginPageTest extends CommonBase {

	LoginPage loginpage;
	HomePage homepage;
	String filepath;
	String type,expected,actual,status,msg;
	

	public LoginPageTest() {
		super();
	}

	@BeforeTest
	public void setup() {
		initialization();
		homepage = new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
	}
	
	@DataProvider(name = "testdata")
	public Object[][] getpositivetestData(Method m) throws IOException {
		String type="Negative";
		if (m.getName().equals("validatecorrectLogin") || m.getName().equals("validateincorrectLogin"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\LoginTestdata.xlsx";
		else if (m.getName().equals("validatecorrectRegistration") || m.getName().equals("validateincorrectRegistration"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\RegistrationTestdata.xlsx";
		if (m.getName().equals("validatecorrectLogin") || m.getName().equals("validatecorrectRegistration"))
			type="Positive";
		Object data[][] = TestUtils.getTestData(filepath,type);
		return data;
	}
	


	@Test(priority = 1)
	public void validateTitle() {
		
		log.info("Validating Loginpage Title");
		loginpage = new LoginPage();
		String title = loginpage.loginpageTitle();
		Assert.assertEquals(title, "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched");
		log.info("Testcase Passed!");
	}
	
	@Test(priority=2,dataProvider="testdata")
	public void validatecorrectLogin(String email,String password) 
	{	log.info("Validating Login operation using correct Credentials");
		log.info("Logging using Username:{0} , Password:{1}");
		msg=loginpage.loginOperation(email, password);
		Assert.assertEquals(msg,"Welcome","Authentication Failed!!!");
		log.info("Testcase Passed!");
		navafterLogin.logout();
		Assert.assertEquals(driver.getTitle(),"Shopping Portal Home Page","Logout operation failed!!!");
		log.info("User logged out!!");
		navbeforeLogin.navigatetologin();
		
	}
	@Test(priority=3,dataProvider="testdata")
	public void validateincorrectLogin(String email,String password) 
	{	log.info("Validating Login operation using incorrect Credentials");
		log.info("Logging using Username:{0} , Password: {1}");
		msg=loginpage.loginOperation(email, password);
	
		if(msg.equals("Welcome"))
		{
			navafterLogin.logout();
			navbeforeLogin.navigatetologin();
			
		}
		Assert.assertEquals(msg,"Invalid email id or Password","Logged in using incorrect credentials!!!!");
		log.info("Testcase Passed!");

	}

	@Test(priority = 4)
	public void validateForgotpassNav() {
		log.info("Validating that link for forgot password page is not broken");
		loginpage.navigateToForgotpass();
		String title = loginpage.forgotpassTitle();
		Assert.assertEquals(title, "Shopping Portal | Forgot Password", "Forgot Password Page Title Not Matched");
		log.info("Testcase Passed!");
		navbeforeLogin.navigatetologin();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup",
				"Login Page Title Not Matched");
	}

	
	@Test(priority=4, dataProvider="testdata")
	public void validatecorrectRegistration(String fullname,String email,String contact,String newpass,String cpass)
	{	
		log.info("Validating registration operation by providing all correct details");

		msg=loginpage.regOperation(fullname, email, contact, newpass, cpass);
		Assert.assertEquals(msg,"You are successfully register",msg);
		
		log.info("Testcase Passed!");
	}
	
	@Test(priority=5, dataProvider="testdata")
	public void validateincorrectRegistration(String fullname,String email,String contact,String newpass,String cpass)
	{
		log.info("Validating registration operation by providing some incorrect details");
		msg=loginpage.regOperation(fullname, email, contact, newpass, cpass);
		
		
		if(TestUtils.isVisible(loginpage.duplicateEmailElement()))
			Assert.assertEquals(msg, "Email already exists .",msg);
		else if(contact.length()!=10)
			Assert.assertEquals(msg, "Contact no.length should be equal to 10",msg);
		else if(!newpass.equals(cpass))
			Assert.assertEquals(msg,"Password and Confirm Password Field do not match  !!",msg);
		
		log.info("Testcase Passed!");

	}

	@AfterTest
	public void tearDown() {
		log.info("Closing the Browser");
		driver.close();
	}

}
