package com.project.testcases;


import org.apache.log4j.Logger;
import static org.testng.Assert.assertEquals;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.utils.TestUtils;

public class HomePageTest extends CommonBase {
	HomePage homepage;
	
	
			
	public HomePageTest() {
		super();
		//log=Logger.getLogger(HomePageTest.class);
	}

	@BeforeTest
	public void setup() {
		initialization();
		homepage = new HomePage();

	}


	
	@Test(priority = 1)
	public void validateTitle(){
		log.info("Validating Homepage Title");
		String title = homepage.title();
		Assert.assertEquals(title, "Shopping Portal Home Page", "Home Page Title Not Matched");
		log.info("Validation Testcase Passed");
	}


	@Test(priority = 2)
	public void validateHomePageDropDwnElements() throws AssertionError {
		homepage = new HomePage();
		String[] expectedStrArray = new String[] { "HOME", "BOOKS", "ELECTRONICS", "FURNITURE", "FASHION" };
		List<WebElement> list = homepage.getHomePageDropDwnElements();
		for (int i = 0; i < list.size(); i++) {
			WebElement webElement = list.get(i);
			String link = homepage.getAnchorTagLink(webElement);
			if (TestUtils.isLinkValid(link)) {
				log.info(link);
			} else {
				log.info(" INVALID " + link);

			}

			String actualString = webElement.getText();
			try {
				assertEquals(actualString, expectedStrArray[i]);
			} catch (AssertionError e) {
				log.error("AssertionError");
			}

			actions = new Actions(driver);
			Action action = actions.moveToElement(webElement).build();
			action.perform();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			
		}
	}

	@Test(priority = 3)
	public void validateBrandElementLink() {
		homepage = new HomePage();
		List<WebElement> brandList = homepage.getBrandWebElements();
		for (WebElement webElement : brandList) {
			String link = homepage.getAnchorTagLink(webElement);
			if (TestUtils.isLinkValid(link)) {
				log.info(link);
			} else {
				log.info(" INVALID " + link);

			}
			actions = new Actions(driver);
			Action action = actions.moveToElement(webElement).build();
			action.perform();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}
	}

	@Test(priority = 4)
	public void validateSocialMediaIconElementsLink() {
		homepage = new HomePage();
		javascriptExecutor = (JavascriptExecutor) driver;
		List<WebElement> brandList = homepage.getsocialMediaIconElements();
		for (WebElement webElement : brandList) {
			javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);
			String link = webElement.getAttribute("href");
			if (TestUtils.isLinkValid(link)) {
				log.info(link);
//				System.out.println(link);
			} else {
				log.info(" INVALID " + link);
//				System.out.println(" INVALID " + link);
			}
			
			actions = new Actions(driver);
			Action action = actions.moveToElement(webElement).build();
			action.perform();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}

	}
	
	
	@AfterTest
	public void tearDown() {
		log.info("Closing Browser");
		driver.close();
	}


}
