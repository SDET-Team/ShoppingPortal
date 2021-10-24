package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.SearchResult;
import com.project.utils.TestUtils;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

/**
 * @file SearchResultTest.java
 * @author nikhil varavadekar
 *
 */
public class SearchResultTest extends CommonBase {

	HomePage homePage;
	SearchResult searchResult;

	public SearchResultTest() {
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
	@BeforeTest
	public void setup() throws WebDriverException {
		initialization();
		log.info("driver initialization");
		homePage = new HomePage(driver);
		homePage.setSearchData(" ");
	}

	/**
	 * @brief Driver Closed
	 * @bug No known bugs
	 */
	@AfterTest
	public void tearDown() {
		log.info("Closing Browser");
		driver.close();
	}

	/**
	 * @param method
	 * @return 2D object
	 * @throws IOException
	 */
	@DataProvider(name = "searchTestData")
	public Object[][] getSearchTestData(Method method) throws IOException {
		String type = "Positive";
		String filepath = null;
		if (method.getName().equals("valitateSearchReuslts"))
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\searchTestData.xlsx";

		Object data[][] = TestUtils.getTestData(filepath, type);
		return data;
	}

	/**
	 * @param data
	 * @throws ElementNotVisibleException
	 * @throws NoSuchElementException
	 */
	@Test(priority = 1, dataProvider = "searchTestData")
	public void valitateSearchReuslts(String data) throws ElementNotVisibleException, NoSuchElementException {
		homePage = new HomePage(driver);
		searchResult = new SearchResult(driver);
		if (data != null) {
			homePage.setSearchData(data);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

			List<WebElement> searchResultList = searchResult.getSearchResultList();
			for (int i = 0; i < searchResultList.size(); i++) {
				WebElement webElement = searchResultList.get(i);
				ArrayList<String> productDetailsList = searchResult.getProductDetails(webElement);
				productDetailsList.set(0, data);
				for (String dataString : productDetailsList) {
					log.info(dataString);
				}

			}

		}

	}

}
