package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;


import static org.testng.Assert.assertEquals;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterClass;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.utils.TestUtils;

/**
 * @file TestUtils.java @author nikhil varavadekar @author shivam k @author
 *       nishant nair @author nikhil pawar
 */
public class HomePageTest extends CommonBase {

	HomePage homepage;

	public HomePageTest() {
		super();

	}

	@BeforeSuite(groups = "Log")
	public void loginit() {
		logConfig();
	}

	/**
	 * @brief Driver initialization
	 * @bug No known bugs
	 * @throws WebDriverException
	 */

	@BeforeClass
	public void setup() {

		/*
		 * log=Logger.getLogger(HomePageTest.class); String timeStamp = new
		 * SimpleDateFormat(" yyyy.MM.dd.HH.mm.ss").format(new Date()); String
		 * currDate=new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		 * dir1=currDate; dir2="TestLog "+timeStamp; String
		 * logFilename=HomePageTest.class.getSimpleName()+timeStamp+".log";
		 * System.setProperty("logfile.name",filePath+"\\src\\resources\\log\\"+dir1+"\\
		 * "+dir2+"\\"+logFilename); PropertyConfigurator.configure(logconfig);
		 */

		initialization();
		log.info("driver initialization");
		homepage = new HomePage(driver);
	}

	/**
	 * @brief Driver Closed
	 * @bug No known bugs
	 */
	@AfterClass
	public void tearDown() {
		log.info("Closing Browser");
		driver.close();
	}

	/**
	 * @brief validate Home Page title
	 * @bug No known bugs
	 * @throws AssertionError
	 */
	@Test(priority = 1)
	public void validateTitle() throws AssertionError {
		log.info("Validating Homepage Title");
		String title = homepage.title();
		Assert.assertEquals(title, "Shopping Portal Home Page", "Home Page Title Not Matched");
		log.info("Validate title test case execution completed");
	}

	/**
	 * @brief validate Products category list \n { "HOME", "BOOKS", "ELECTRONICS",
	 *        "FURNITURE", "FASHION" } \n check for broken link or invalid links
	 *        perform moveToElement (hover) from Actions Class
	 * 
	 * @bug No known bugs
	 * @throws AssertionError
	 * @throws HttpTimeoutException
	 * @throws NullPointerException
	 */

	@Test(priority = 2)
	public void validateHomePageDropDwnElements() throws AssertionError {

		String[] expectedStrArray = new String[] { "HOME", "BOOKS", "ELECTRONICS", "FURNITURE", "FASHION" };
		List<WebElement> list = homepage.getHomePageDropDwnElements();
		for (int i = 0; i < list.size() - 2; i++) {
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

	/**
	 * @brief validate OUR BRANDS category \n check for broken link or invalid links
	 *        \n perform moveToElement (hover) from Actions Class \n perform
	 *        javascriptExecutor scroll from JavascriptExecutor Interface \n
	 * 
	 * @bug No known bugs
	 * 
	 * @throws HttpTimeoutException
	 * @throws NullPointerException
	 * @throws AssertionError
	 */
	@Test(priority = 3)
	public void validateBrandElementLink() throws NullPointerException {

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

	/**
	 * @brief validate SOCIAL MEDIA LINKS check for broken link or invalid links
	 *        perform moveToElement (hover) from Actions Class
	 * @bug No known bugs
	 * @throws HttpTimeoutException
	 * @throws NullPointerException
	 * @throws AssertionError
	 */
	@Test(priority = 4)
	public void validateSocialMediaIconElementsLink() throws NullPointerException {
		javascriptExecutor = (JavascriptExecutor) driver;
		List<WebElement> brandList = homepage.getsocialMediaIconElements();
		for (WebElement webElement : brandList) {
			javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);
			String link = webElement.getAttribute("href");

			if (TestUtils.isLinkValid(link)) {
				log.info(link);
			}
			if (TestUtils.isLinkValid(link)) {
				log.info(link);
			} else {
				log.info(" INVALID " + link);
			}

			actions = new Actions(driver);
			Action action = actions.moveToElement(webElement).build();
			action.perform();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}

	}

	/**
	 * @brief validate Search Functionality
	 * @param data
	 * @throws ElementNotVisibleException
	 * @throws ElementNotInteractableException
	 */
	@Test(priority = 5, dataProvider = "searchTestData")
	public void validateSearchFunctionality(String data)
			throws ElementNotVisibleException, ElementNotInteractableException {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		homepage = new HomePage(driver);
		homepage.setSearchData(data);
		System.out.println(data);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	/**
	 * @brief DataProvider returns 2D Object
	 * @param method
	 * @return Object[][]
	 * @throws IOException
	 */
	@DataProvider(name = "searchTestData")
	public Object[][] getSearchTestData(Method method) throws IOException {
		String type = "Positive";
		String filepath = null;
		if (method.getName().equals("validateSearchFunctionality"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\searchTestData.xlsx";

		Object data[][] = TestUtils.getTestData(filepath, type);
		return data;
	}
}
