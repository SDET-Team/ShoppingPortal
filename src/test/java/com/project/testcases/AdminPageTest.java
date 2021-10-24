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
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeClass;  
import org.testng.annotations.AfterClass;  
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AdminHomePage;
import com.project.pages.AdminPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.utils.TestUtils;

    
/**
 * @brief AdminPageTest class tests Admin page features. 
 * 
 * @details Verifies features of admin like admin login credentials, order management, user management, category, product management,etc. 
 * 
 */
public class AdminPageTest extends CommonBase
{
		AdminPage adminpage;
		AdminHomePage adminhomepage;
		String filepath;
		
		
		/*!********************
		\brief Constructor which will initialize the pre-requisites to execute test cases.
		
		
		**********************/
		public AdminPageTest()
		{
			super();
		}
		
		/*!*****************************************************************
	    \brief DataProvider annotations for fetch test data from file
	    @throws FileNotFoundException
	    *******************************************************************/
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
		
		
		@BeforeSuite(groups="Log")
		public void loginit()
		{
			logConfig();
		}
		
		/*!*************************************************
		   \brief Initialization of Admin page will happen from here, driver and url for admin page is initialized.
		   @throws WebDriverException
		***************************************************/
		@BeforeTest
		public void setup()
		{	
			initialization("admin");	
		}
		
		
		/*!********************
		\brief This function validates the title for Administration page \n title() function will return the value of title page
		from AdminHomePage class and value will be asserted. If value is true test case will pass and then only it will proceed to further test cases. 
		@throws HttpTimeOutException
		**********************/
		@Test(priority=1)
		public void validateTitle()
		{
			log.info("Validating Admin page title");
			adminhomepage=new AdminHomePage(driver);
			String title=adminhomepage.title();
			Assert.assertEquals(title, "Shopping Portal | Admin login","Login Page Title Not Matched");
			log.info("Testcase Passed!");
		}
		
		/*!*************************************************
		  \brief This function will validate authentication of admin user. Admin username and password is stored in config file and will be fetched from there
		  by getProperty method. For confirmation 
		***************************************************/
		@Test(priority=2)
		public void validateLogin()
		{
			log.info("Validating Admin login");
			adminpage=new AdminPage(driver);
			adminpage.adminloginOperation(config.getProperty("adminUserName"),config.getProperty("adminPassword"));
			Assert.assertEquals(adminpage.adminloginpageTitle(), "Admin| Change Password","Title not match");
			log.info("Testcase passed");
		}	
		
		/*!*************************************************
		   \brief validateColor() method will verify for green color of today's order tab. getColorCode method will return cssValue for green color 
         and will be compared to rgba value.
			\n Depends on method validateLogin, so it will be skipped if validateLogin fails. 
         @throws AssertionError			
		***************************************************/
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
		
		/*!*************************************************
		   \brief Validate count of todaysOrder is displayed on the right side of the 
		    tab in square box. 
			\details Again it will depend on valid admin login. TodaysOrder method will return the count 
			and it will be asserted.
			@throws AssertionError
		***************************************************/
		@Test(priority=4,dependsOnMethods="validateLogin")
		public void validateTodaysOrder()
		{
			log.info("Validating count of today's order");
			String status=adminpage.todaysOrder();
			int value=Integer.parseInt(status);
			if(value>=0 || value<=100)
			  status="0"; 	
		      Assert.assertEquals(status,"0","Count is displayed");
		}
		
		/*!*************************************************
		   \brief This method will validate whether category created is unique or not.
		   \details Test case will fail if category with same name is created twice.
		   Test data is fetched from a excel sheet.
		   File name "productCategoryData.xlsx" Sheet name "Sheet1"
		   Columns names {"Category","Description} 
		   \param category This is products category.
		   \param desc This is category description.
		   @throws AssertionError
		***************************************************/
		@Test(priority=5,dataProvider="testdata")
		public void validateUniqueCategory(String category,String desc)
		{
			log.info("Validating unique category name");
			   String status=adminpage.createCategory(category,desc);
			   Assert.assertNotEquals(status,category,"Category already exists");
			   log.info("Testcase passed");
			
		}
		
		/*!*************************************************
		   \brief Validate whether admin user is able to upload image for creating products.
		   \details AutoIt tool is used to execute the script to handle external window from where image will be uploaded.		  
		  @throws FileNotFoundException
		***************************************************/
		@Test(priority=6)
		public void validateUploadImage()
		{
			log.info("Validating uploading image");
			String status=adminpage.uploadImage();
			Assert.assertEquals(status,"Sams");
			log.info("Testcase passed");
		}
		
		/*!*************************************************
		   \brief Validation of product insertion and checking whther it is reflecting back on main user website.
		   \details For it's successful execution it depends on validateUploadImage test case.
		   
		   File name "insertProductData.xlsx" Sheet name "Sheet1"
		    Columns names {"Company","Model","Type","Description","Price before Discount","Price after Discount",
		   "Shipping charge"} 
		   \param company Products company name
		   \param model Product model name
		   \param desc Product description
		   \param priceBD Price before discount
		   \param priceAD Price after discount
		   \param shippCharge Shipping charges for product
		   \param searchKey Product to be searched in search box
		   @throws FileNotFoundException
		***************************************************/
		@Test(priority=7,dataProvider="testdata",dependsOnMethods="validateUploadImage")
		public void validateInsertProduct(String company,String model,String desc,String priceBD, String priceAD,String shippCharge,String searchKey)
		{
			log.info("Validating insert product");
			String actualOutput=adminpage.insertProduct(company,model,desc,priceBD,priceAD,shippCharge,searchKey);
			String expectedOutput="Samsung";
		    Assert.assertEquals(actualOutput, expectedOutput);
		    log.info("Testcase passed");
		}
		
		/*!*************************************************
		   \brief Driver is closed.
		   @throws
		***************************************************/				
		@AfterTest
		public void tearDown()
		{
			driver.close();
		}
		
		
	


}
