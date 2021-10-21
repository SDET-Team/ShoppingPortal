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
import org.testng.annotations.AfterClass; 

import com.project.base.CommonBase;
import com.project.pages.ForgotPasswordPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;

public class ForgotPasswordPageTest extends CommonBase {

	LoginPage loginpage;
	HomePage homepage;
	ForgotPasswordPage forgotpasspage;
	String filepath,msg;

	public ForgotPasswordPageTest() {
		super();
		
	}

	@DataProvider(name = "testdata")
	public Object[][] getPositiveData(Method m) throws IOException {
		String type="Negative";
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\ForgotPasswordTestdata.xlsx";
		if (m.getName().equals("validatepositiveForgotPassword"))
			type="Positive";
		Object data[][] = TestUtils.getTestData(filepath,type);
		return data;
	}
	
	
	@BeforeClass
	public void setup() {
		
		
		/*log=Logger.getLogger(ForgotPasswordPageTest.class);
		String timeStamp = new SimpleDateFormat(" yyyy.MM.dd.HH.mm.ss").format(new Date());
		String currDate=new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		dir1=currDate;
		dir2="TestLog "+timeStamp;
		String logFilename=ForgotPasswordPageTest.class.getSimpleName()+timeStamp+".log";
		System.setProperty("logfile.name",filePath+"\\src\\resources\\log\\"+dir1+"\\"+dir2+"\\"+logFilename);
		PropertyConfigurator.configure(logconfig);
		*/
		
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

	@Test(priority = 1)
	public void validateTitle() {
		log.info("Validating Loginpage Title");
		String title = forgotpasspage.title();
		Assert.assertEquals(title, "Shopping Portal | Forgot Password", "Forgot Password Page Title Not Matched");
		log.info("Testcase Passed!");
	}

	
	@Test(priority=2,dataProvider="testdata")
	public void validatepositiveForgotPassword(String email,String contact,String newpass, String cpass)
	{
		log.info("Validating Forgot password operation by providing valid emailid and contact number");
		log.info("User details: Email: "+email+" , ContactNo: "+contact);
		msg=forgotpasspage.changepassword(email, contact, newpass, cpass);
		
		Assert.assertEquals(msg,"Password Changed Successfully",msg);
		log.info("Testcase Passed!");
	}
	
	@Test(priority=3,dataProvider="testdata")
	public void validatenegativeForgotPassword(String email,String contact,String newpass, String cpass)
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



	@AfterClass
	public void tearDown() {
		log.info("Closing browser");

		driver.close();
	}

}
