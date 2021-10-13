package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;

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
	public LoginPageTest()
	{
		super();
	}
	
	@BeforeTest
	public void setup()
	{	
		initialization();
		homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page","Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
	}
	
	
	@DataProvider(name="testdata")
	public Object[][] getloginData(Method m) throws IOException{
		if(m.getName().equals("validateLogin"))
			filepath=System.getProperty("user.dir")+"\\src\\resources\\testdata\\loginTestdata.xlsx";
		else if(m.getName().equals("validateRegistration"))
			filepath=System.getProperty("user.dir")+"\\src\\resources\\testdata\\regTestdata.xlsx";
		Object data[][] = TestUtils.getTestData(filepath); 
		return data; 
	}
	
	
	@Test(priority=1)
	public void validateTitle()
	{
		loginpage=new LoginPage();
		String title=loginpage.loginpageTitle();
		Assert.assertEquals(title, "Shopping Portal | Signi-in | Signup","Login Page Title Not Matched");
		
	}
	
	@Test(priority=2,dataProvider="testdata")
	public void validateLogin(String email,String password,String expected) 
	{	
		
		boolean status=loginpage.loginOperation(email, password,expected);
		if(expected.equals("pass"))
		{
			Assert.assertFalse(status);
			navafterLogin.logout();
			Assert.assertEquals(homepage.title(),"Shopping Portal Home Page","Home Page Title Not Matched");
			navbeforeLogin.navigatetologin();
			Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup","Login Page Title Not Matched");
		}
		else
			Assert.assertTrue(status);
		
	}
	
	@Test(priority=3)
	public void validateForgotpassNav()
	{
		loginpage.navigateToForgotpass();
		String title=loginpage.forgotpassTitle();
		Assert.assertEquals(title, "Shopping Portal | Forgot Password","Forgot Password Page Title Not Matched");
		navbeforeLogin.navigatetologin();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup","Login Page Title Not Matched");
	}
	
	@Test(priority=4, dataProvider="testdata")
	public void validateRegistration(String fullname,String email,String contact,String newpass,String cpass,String expected)
	{
		boolean status=loginpage.regOperation(fullname, email, contact, newpass, cpass,expected);
		Assert.assertTrue(status);
		
	
	}
	
	@AfterTest
	public void tearDown()
	{
		driver.close();
	}
	
	
}
