package com.project.testcases;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.ForgotPasswordPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;

public class FogotPasswordPageTest extends CommonBase {
	
	
	LoginPage loginpage;
	HomePage homepage;
	ForgotPasswordPage forgotpasspage;
	String filepath;
	
	public FogotPasswordPageTest()
	{
		super();
	}
	
	@DataProvider(name="testdata")
	public Object[][] getloginData(Method m) throws IOException{
		
		filepath=System.getProperty("user.dir")+"\\src\\resources\\testdata\\forgotpassTestdata.xlsx";
		Object data[][] = TestUtils.getTestData(filepath); 
		return data; 
	}
	
	
	@BeforeTest
	public void setup()
	{	
		initialization();
		homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page","Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
		loginpage=new LoginPage();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup","Login Page Title Not Matched");
		loginpage.navigateToForgotpass();
	}
	
	@Test(priority=1)
	public void validateTitle()
	{
	 forgotpasspage=new ForgotPasswordPage();
	 String title=forgotpasspage.title();
	 Assert.assertEquals(title, "Shopping Portal | Forgot Password","Forgot Password Page Title Not Matched");
		
	}
	
	@Test(priority=2,dataProvider="testdata")
	public void validateForgotPassword(String email,String contact,String newpass, String cpass,String remarks)
	{
		boolean status=forgotpasspage.changepassword(email, contact, newpass, cpass,remarks);
		Assert.assertTrue(status);
	}
	
	@AfterTest
	public void tearDown()
	{
		
		driver.close();
	}

}
