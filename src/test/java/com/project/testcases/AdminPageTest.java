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

import jdk.internal.org.jline.utils.Log;
public class AdminPageTest extends CommonBase
{
		AdminPage adminpage;
		AdminHomePage adminhomepage;
		String filepath;
		public AdminPageTest()
		{
			super();
		}
		
		@DataProvider(name = "testdata")
		public Object[][] getpositivetestData(Method m) throws IOException {
			String type="Positive";
			if (m.getName().equals("validateUniqueCategory"))
				filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\productCategoryData.xlsx";
			else if (m.getName().equals("validateInsertProduct"))
				filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\insertProductData.xlsx";
	
			Object data[][] = TestUtils.getTestData(filepath,type);
			return data;
		}
		
		@BeforeTest
		public void setup()
		{	
			initialization("admin");	
		}
		
		
		
		@Test(priority=1)
		public void validateTitle()
		{
			log.info("Validating Admin page title");
			adminhomepage=new AdminHomePage();
			String title=adminhomepage.title();
			Assert.assertEquals(title, "Shopping Portal | Admin login","Login Page Title Not Matched");
			log.info("Testcase Passed!");
		}
		
		@Test(priority=2)
		public void validateLogin()
		{
			log.info("Validating Admin login");
			adminpage=new AdminPage();
			adminpage.adminloginOperation(config.getProperty("adminUserName"),config.getProperty("adminPassword"));
			Assert.assertEquals(adminpage.adminloginpageTitle(), "Admin| Change Password","Title not match");
			log.info("Testcase passed");
		}	
		
		@Test(priority=3,dependsOnMethods="validateLogin")
		public void validateColor()
		{
		    log.info("Validating color of square button in order management tab");
			boolean status=adminpage.orderManagement();
			
			Assert.assertTrue(status);
			
			String colorCode=adminpage.getColorCode();
			String expectedColor="rgba(255, 255, 255, 1)";
			Assert.assertEquals(colorCode, expectedColor,"Green color is verified");
		}	
		
		@Test(priority=4,dependsOnMethods="validateLogin")
		public void validateTodaysOrder()
		{
			log.info("Validating count of today's order");
			String status=adminpage.todaysOrder();
		      Assert.assertEquals(status,"0","Count is displayed");
		}
		
		@Test(priority=5,dataProvider="testdata")
		public void validateUniqueCategory(String category,String desc)
		{
			log.info("Validating unique category name");
			   String status=adminpage.createCategory(category,desc);
			   Assert.assertNotEquals(status,category,"Category already exists");
			   log.info("Testcase passed");
			
		}
		
		@Test(priority=6)
		public void validateUploadImage()
		{
			log.info("Validating uploading image");
			String status=adminpage.uploadImage();
			Assert.assertEquals(status,"SamsungTVFrontView.jpg");
			log.info("Testcase passed");
		}
		
		@Test(priority=7,dataProvider="testdata",dependsOnMethods="validateUploadImage")
		public void validateInsertProduct(String company,String model,String desc,String priceBD, String priceAD,String shippCharge,String searchKey)
		{
			log.info("Validating insert product");
			String actualOutput=adminpage.insertProduct(company,model,desc,priceBD,priceAD,shippCharge,searchKey);
			String expectedOutput="Samsung";
		    Assert.assertEquals(actualOutput, expectedOutput);
		    log.info("Testcase passed");
		}
		
				
		@AfterTest
		public void tearDown()
		{
			driver.close();
		}
		
		
	


}
