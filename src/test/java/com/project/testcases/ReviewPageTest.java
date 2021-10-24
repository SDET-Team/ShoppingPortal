package com.project.testcases;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.ReviewPage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.utils.TestUtils;

/**
 * @brief ReviewPageTest class tests My Account page features. 
 * 
 * @details Verifies features of Review page like adding review ,user name in review,etc.
 */

public class ReviewPageTest extends CommonBase
{
	LoginPage loginpage;
	HomePage homepage;
	ReviewPage reviewpage;
	String filepath;
	
	/*!*****************************************************************
	\brief DataProvider annotations for fetch test data from file
	@throws FileNotFoundException
	*******************************************************************/
	@DataProvider(name = "testdata")
	public Object[][] getpositivetestData(Method m) throws IOException {
		String type="AddReview";
		if (m.getName().equals("validateReview"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\productReviewData.xlsx";
		
		else if(m.getName().equals("validateReviewUserNameFormat"))
		{
			  filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\productReviewData.xlsx";
			  type="ValidateUserName";
		}
		else if(m.getName().equals("validateAutoFillUserName"))
		{
			  filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\productReviewData.xlsx";
			  type="ReviewAutoFillName";
		}
		     
		Object data[][] = TestUtils.getTestData(filepath,type);
		return data;
	}
	
	/**
	 * @brief Initialize log4j log configuration to print testcase logs to specific file. 
	 * 
	 */
	@BeforeSuite(groups="Log")
	public void loginit()
	{
		logConfig();
	}
	
	/*!*****************************************************************
	\brief Driver initialization and pre-requisites for running test cases.
	@throws WebDriverException
	*******************************************************************/
	@BeforeTest
	public void setup()
	{	
		initialization();
		homepage=new HomePage(driver);
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page","Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
	}
	
	/*!*************************************************
	   \brief This method will validate format of user name.
	   \details Test case will fail if user's name contains character other than alphabets.
	   Test data is fetched from a excel sheet.
	    File name "productReviewData.xlsx" Sheet name "Sheet2"
	    Columns names {"User Name","Summary","Review","searchKey","Expected User Name"} 
	   
	   @throws AssertionError
       @throws FileNotFoundException
    ***************************************************/
	@Test(priority=1,dataProvider="testdata")
	public void validateReviewUserNameFormat(String userName,String summary,String review,String searchKey,String expUserName)
	{
		log.info("Validating user name format in review");
		reviewpage=new ReviewPage(driver);
		String name=reviewpage.verifyUserName(userName,summary,review,searchKey,expUserName);
		Assert.assertEquals(name,expUserName);
		log.info("test case passed");
	}
	
	/*!*************************************************
	   \brief This method will validate whether logged in user's name is auto filled.
	   \details Test case will fail if we click on submit button and will propmt to type name.
	   Test data is fetched from a excel sheet.
	    File name "productReviewData.xlsx" Sheet name "Sheet2"
	    Columns names {"User Name","Summary","Review","searchKey","Expected User Name"} 
	   
	   @throws AssertionError
       @throws FileNotFoundException
***************************************************/
	@Test(priority=2,dataProvider="testdata")
	public void validateAutoFillUserName(String userName,String summary,String review,String searchKey,String expUserName)
	{
		log.info("Validating autofill in username");
		reviewpage=new ReviewPage(driver);
		boolean status=reviewpage.verifyAutoFillUserName(userName,summary,review,searchKey,expUserName);
		Assert.assertTrue(status);
		log.info("test case passed");
	}
	
	/*!*****************************************************************
    \brief This method will validate whether user can add review on purchased product.
	 \details End to end test case as it shows functioning of website from user page to admin page.
	   Test data is fetched from a excel sheet.
	    File name "productReviewData.xlsx" Sheet name "Sheet1"
	    Columns names {"EmailId","Password","Order Status Description","Name","Summary","Review"} 
	   
	   @throws AssertionError
       @throws FileNotFoundException
*******************************************************************/
	@Test(priority=3,dataProvider="testdata")
	public void validateReview(String emailId,String pwd,String orderStatus,String name,String summary,String review)
	{
		log.info("Validating review");
		reviewpage=new ReviewPage(driver);
		reviewpage.addReview(emailId,pwd,orderStatus,name,summary,review);
		log.info("Testcase passed");
	}
	
	/*!*****************************************************************
	\brief Driver is closed.
	@throws
	*******************************************************************/
	@AfterTest
	public void tearDown()
	{
		driver.close();
	}

}
