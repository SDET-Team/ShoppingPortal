package com.project.testcases;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.ReviewPage;

import java.io.IOException;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.project.utils.TestUtils;

public class ReviewPageTest extends CommonBase
{
	LoginPage loginpage;
	HomePage homepage;
	ReviewPage reviewpage;
	String filepath;
	@BeforeTest
	public void setup()
	{	
		initialization();
		homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page","Home Page Title Not Matched");
		navbeforeLogin.navigatetologin();
	}
	
	@Test(priority=1)
	public void validateReview()
	{
		reviewpage=new ReviewPage();
		reviewpage.addReview();
	}
	
	@Test(priority=2)
	public void validateReviewUserNameFormat()
	{
		reviewpage=new ReviewPage();
		String name=reviewpage.verifyUserName();
		Assert.assertEquals(name,"Anuj");
		
	
	}
	@Test(priority=3)
	public void validateAutoFillUserName()
	{
		reviewpage=new ReviewPage();
		boolean status=reviewpage.verifyAutoFillUserName();
		Assert.assertTrue(status);
	}
	/*
	@AfterTest
	public void tearDown()
	{
		driver.close();
	}
*/
}
