package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AdminHomePage;
import com.project.pages.AdminPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;

public class AdminPageTest extends CommonBase
{
		AdminPage adminpage;
		AdminHomePage adminhomepage;
		
		public AdminPageTest()
		{
			super();
		}
		
		@BeforeTest
		public void setup()
		{	
			initialization("admin");	
		}
		
		
		@Test(priority=1)
		public void validateTitle()
		{
			adminhomepage=new AdminHomePage();
			String title=adminhomepage.title();
			Assert.assertEquals(title, "Shopping Portal | Admin login","Login Page Title Not Matched");
			
		}
		
		@Test(priority=2)
		public void validateLogin()
		{
			adminpage=new AdminPage();
			adminpage.adminloginOperation(config.getProperty("adminUserName"),config.getProperty("adminPassword"));
			Assert.assertEquals(adminpage.adminloginpageTitle(), "Admin| Change Password","Title not match");
		}	
		
		@Test(priority=3,dependsOnMethods="validateLogin")
		public void validateColor()
		{
		    
			boolean status=adminpage.orderManagement();
			
			Assert.assertTrue(status);
			
			String colorCode=adminpage.getColorCode();
			String expectedColor="rgba(255, 255, 255, 1)";
			Assert.assertEquals(colorCode, expectedColor);
		}	
		
		@Test(priority=4,dependsOnMethods="validateLogin")
		public void validateTodaysOrder()
		{
			List<WebElement> elements=driver.findElements(By.xpath("//*[@id=\"DataTables_Table_0\"]/thead/tr/td[1]"));
			Assert.assertEquals(0,elements.size());
			
		}
		
		
		
				
		@AfterTest
		public void tearDown()
		{
			driver.close();
		}
		
}
		
		
	
