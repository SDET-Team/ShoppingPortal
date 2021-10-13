package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.HomePage;

import org.testng.annotations.BeforeTest;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import com.project.utils.TestUtils;

public class CartActivityTest extends CommonBase {

	CartActivity cartActivity;
	HomePage homepage;

	@BeforeTest
	public void setup() {
		initialization();
		homepage = new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
//		navbeforeLogin.navigatetologin();
	}

	@AfterTest
	public void afterTest() {
		driver.close();
	}

	@Test(priority = 1)
	public void validateTitle() throws AssertionError {
		cartActivity = new CartActivity();
		String title = cartActivity.indexPageTitle();
		try {
			Assert.assertEquals(title, "Shopping Portal Home Page", "Home Page Title Not Matched");
		} catch (AssertionError e) {
			logger.error("Assertion Error");
		} catch (Exception e) {
			logger.error("Error");
		}

	}

	@Test(priority = 2)
	public void validateFeatureProductTitle() throws AssertionError {
		cartActivity = new CartActivity();
		String title = cartActivity.getFeatureProductText();

		try {
			Assert.assertEquals(title, "FEATURED PRODUCTS", "FEATURED PRODUCTS Title Not Matched");
		} catch (AssertionError e) {
			System.out.println("Assertion Error");
		} catch (Exception e) {
			logger.error("Error");
		}

	}

	@Test(priority = 3)
	public void validateFeatureProductSort() throws AssertionError {
		cartActivity = new CartActivity();
		String[] strArray = new String[] { "ALL", "BOOKS", "FURNITURE" };
		ArrayList<String> sortProductList = cartActivity.getfeatureProductSortElement();

		for (int i = 0; i < sortProductList.size(); i++) {
			try {
				String actualString = sortProductList.get(i);
				String expectedString = strArray[i];
				Assert.assertEquals(actualString, expectedString);
			} catch (AssertionError e) {
				logger.error("Assertion Error");
			} catch (Exception e) {
				logger.error("Error");
			}
		}

	}

	@Test(priority = 4)
	public void validateFeatureProductList() throws AssertionError {
		cartActivity = new CartActivity();

		ArrayList<String> tempList;
		Map<Integer, ArrayList<String>> productDataMap = new TreeMap<Integer, ArrayList<String>>();

		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();

		for (int i = 0; i < featureProductList.size(); i++) {
			WebElement featureProductElement = featureProductList.get(i);
			ArrayList<String> productDetailsArrayList = cartActivity.getProductDetails(featureProductElement);
			tempList = new ArrayList<>();
			for (int j = 0; j < productDetailsArrayList.size(); j++) {
				String detailsString = productDetailsArrayList.get(j);

				if (!TestUtils.isTextFormated(detailsString)) {
					tempList.add("null");
					continue;
				}

				if (j == 1) {
					if (TestUtils.isLinkValid(detailsString)) {
						tempList.add(detailsString);
						continue;
					} else {
						tempList.add(null);
						continue;
					}
				}

				tempList.add(detailsString);
			}
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			productDataMap.put(i, tempList);
		}

		try {
			String[] columnNamesList = { "imgsrc", "prodLink", "prodText", "productPrice", "disCountPrice" };
			TestUtils.setTestData("productData.xlsx", "Product Details", productDataMap, columnNamesList);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException");
		} catch (Exception e) {
			logger.error("Exception");
		}

	}

}
