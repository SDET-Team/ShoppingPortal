package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.HomePage;
import com.project.pages.common.NavbarBeforeLogin;

import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.ElementNotInteractableException;
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
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.close();
//		System.out.println("driver.close()");
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
			} finally {

			}
		}

	}

	@Test(priority = 4)
	public void validateFeatureProductList() throws AssertionError {
		cartActivity = new CartActivity();
		String[] columnNamesList = { "imageSrc", "productLink", "productText", "productPrice", "discountPrice",
				"Add-to-Cart" };
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
			String fileString = filePath + testDataDirectoryPath + "\\" + "ProductsData.xlsx";
			TestUtils.setTestData(fileString, "Featured Products Details", productDataMap, columnNamesList);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException");
		} catch (Exception e) {
			logger.error("Exception");
		}

	}

	@Test(priority = 5)
	public void validateOtherProductList() throws AssertionError {
		cartActivity = new CartActivity();
		int key = 0;
		String[] sectionList = { "Smart Phone", "Laptops" };
		String[] columnNamesList = { "Section", "imageSrc", "productLink", "productText", "productPrice",
				"discountPrice", "Add-to-Cart" };
		ArrayList<String> tempList;
		Map<Integer, ArrayList<String>> productDataMap = new TreeMap<Integer, ArrayList<String>>();

		List<WebElement> otherSectionElementsList = cartActivity.getOtherSectionListElement();

		for (int i = 0; i < otherSectionElementsList.size(); i++) {

			WebElement webElement = otherSectionElementsList.get(i);
			List<WebElement> divElements = cartActivity.getWebElements("className", "owl-item", webElement);

			for (int j = 0; j < divElements.size(); j++) {
				WebElement div = divElements.get(j);
				tempList = new ArrayList<>();
				tempList.add(sectionList[i]);
				ArrayList<String> productDetailsArrayList = cartActivity.getProductDetails(div);

				for (int k = 0; k < productDetailsArrayList.size(); k++) {
					String detailsString = productDetailsArrayList.get(k);
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
				driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				productDataMap.put(key++, tempList);
			}
		}

		try {
			String fileString = filePath + testDataDirectoryPath + "\\" + "ProductsData.xlsx";
			TestUtils.setTestData(fileString, "Other Products Details", productDataMap, columnNamesList);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException");
		} catch (Exception e) {
			logger.error("Exception");
		}

	}

	@Test(priority = 6)
	public void validateAddToCartButton() {
		cartActivity = new CartActivity();
		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();
		for (int i = 1; i < featureProductList.size() - 1; i++) {
			driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
			WebElement firstElement = featureProductList.get(i);
			String actualString = cartActivity.handleClickActionOnWebElement(firstElement, i);
			try {
				Assert.assertEquals(actualString, "ADD TO CART");
			} catch (AssertionError e) {
				logger.error("AssertionError");
			}

			try {
				navbeforeLogin.clickOnLogoImage();
			} catch (ElementNotInteractableException e) {
				logger.error("ElementNotInteractableException");
			}
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		navbeforeLogin.clickOnMyCartImage();
//		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
//		TestUtils.removalProductFromCart(featureProductList.size());
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		navbeforeLogin.clickOnLogoImage();
	}

	@Test(priority = 7)
	public void validateAddToCartForSameProduct() {
		cartActivity = new CartActivity();
		int productNumber = 1, numOfTimes = 4;
		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();

		for (int i = 1; i < numOfTimes; i++) {
			driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
			WebElement firstElement = featureProductList.get(productNumber);
			String actualString = cartActivity.handleClickActionOnWebElement(firstElement, productNumber);

			try {
				Assert.assertEquals(actualString, "ADD TO CART");
			} catch (AssertionError e) {
				logger.error("AssertionError");
//				System.out.println("AssertionError");
			}

			try {
				navbeforeLogin.clickOnLogoImage();
			} catch (ElementNotInteractableException e) {
				logger.error("ElementNotInteractableException");
//				System.out.println("ElementNotInteractableException");
			}

		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		navbeforeLogin.clickOnMyCartImage();

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		TestUtils.removalProductFromCart(featureProductList.size());

	}

}
