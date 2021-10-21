package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.HomePage;
import com.project.pages.commonnavbar.NavbarBeforeLogin;

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

import org.apache.log4j.LogManager;
import org.bouncycastle.asn1.eac.Flags;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.project.utils.TestUtils;

public class CartActivityTest extends CommonBase {

	CartActivity cartActivity;
	HomePage homepage;
	
	public CartActivityTest()
	{
		super();
	}

	

	@BeforeSuite(groups="Log")
	public void loginit()
	{
		logConfig();
	}
	
	@BeforeTest
	public void setup() {
		
		initialization();

		log.info("driver initialization");

		homepage = new HomePage(driver);
		cartActivity = new CartActivity(driver);
		try {
			Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
		} catch (AssertionError e) {
			log.error("Assertion Error");
		} catch (Exception e) {
			log.error("Error");
		}
	}

	@AfterTest
	public void afterTest() {
		log.info("driver closed");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.close();
	}

	@Test(priority = 1)
	public void validateTitle() throws AssertionError {
		
		String title = cartActivity.indexPageTitle();
		try {
			Assert.assertEquals(title, "Shopping Portal Home Page", "Home Page Title Not Matched");
		} catch (AssertionError e) {
			log.error("Assertion Error");
		} catch (Exception e) {
			log.error("Error");

		}

		log.info("Validate Page Title test case execution completed");

	}

	@Test(priority = 2)
	public void validateFeatureProductTitle() throws AssertionError {
	
		String title = cartActivity.getFeatureProductText();

		try {
			Assert.assertEquals(title, "FEATURED PRODUCTS", "FEATURED PRODUCTS Title Not Matched");
		} catch (AssertionError e) {
			log.error("Assertion Error");
		} catch (Exception e) {
			log.error("Error");
		}

		log.info("Validate Feature Product list test case execution completed");

	}

	@Test(priority = 3)
	public void validateFeatureProductSort() throws AssertionError {

		String[] strArray = new String[] { "ALL", "BOOKS", "FURNITURE" };
		ArrayList<String> sortProductList = cartActivity.getfeatureProductSortElement();

		for (int i = 0; i < sortProductList.size(); i++) {
			try {
				String actualString = sortProductList.get(i);
				String expectedString = strArray[i];
				Assert.assertEquals(actualString, expectedString);
			} catch (AssertionError e) {
				log.error("Assertion Error");
			} catch (Exception e) {
				log.error("Error");
			}
		}

		log.info("validate Feature Product Sort function test case execution completed");

	}

	@Test(priority = 4)
	public void validateFeatureProductList() throws AssertionError {

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
				if (j == 1 || j == 2) {
					if (TestUtils.isLinkValid(detailsString,driver)) {
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

			String fileString = filePath +"\\src\\resources\\testdata\\" + "ProductsData.xlsx";
			TestUtils.setTestData(fileString, "Featured Products Details", productDataMap, columnNamesList);

		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info("validate Feature Product List test case execution completed");

	}

	@Test(priority = 5)
	public void validateOtherProductList() throws AssertionError {
	
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
					if (k == 1 || k == 2) {
						if (TestUtils.isLinkValid(detailsString,driver)) {
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

			String fileString = filePath + "\\src\\resources\\testdata\\" + "ProductsData.xlsx";
			TestUtils.setTestData(fileString, "Other Products Details", productDataMap, columnNamesList);

		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info("validateFeatureProductList test case execution completed");

	}

	@Test(priority = 6)
	public void validateAddToCartButton() {

		ArrayList<String> tempList;
		String[] columnNamesList = { "imageSrc", "productLink", "productText", "productPrice", "discountPrice",
				"Add-to-Cart" };
		Map<Integer, ArrayList<String>> productDataMap = new TreeMap<Integer, ArrayList<String>>();

		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();
		for (int i = 1; i < featureProductList.size(); i++) {
			driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
			WebElement firstElement = featureProductList.get(i);
			ArrayList<String> productDetailsArrayList = cartActivity.getProductDetails(firstElement);

			tempList = new ArrayList<>();
			for (int j = 0; j < productDetailsArrayList.size(); j++) {
				String detailsString = productDetailsArrayList.get(j);

				if (!TestUtils.isTextFormated(detailsString)) {
					tempList.add("null");
					continue;
				}
				if (j == 1 || j == 2) {
					if (TestUtils.isLinkValid(detailsString,driver)) {
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

			cartActivity.handleClickActionOnWebElement(firstElement, i);
			try {
				navbeforeLogin.clickOnLogoImage();
			} catch (ElementNotInteractableException e) {
				log.error("ElementNotInteractableException");
			}
		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		navbeforeLogin.clickOnMyCartImage();

		try {
			String file = System.getProperty("user.dir")+"\\src\\resources\\testdata\\" + "InCartProducts.xlsx";
			TestUtils.setTestData(file, "Products Details", productDataMap, columnNamesList);
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info(featureProductList.size() + " products added to cart");
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
		TestUtils.removeProductFromCart(18);
		log.info(featureProductList.size() + " products removed from cart");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		navbeforeLogin.clickOnLogoImage();

		log.info("validate add to cart button for products test case execution completed");

	}

	@Test(priority = 7)
	public void validateAddToCartForSameProduct() {
		int productNumber = 1, numOfTimes = 4;
		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();

		for (int i = 1; i < numOfTimes; i++) {
			driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
			WebElement firstElement = featureProductList.get(productNumber);
			cartActivity.handleClickActionOnWebElement(firstElement, productNumber);

			try {
				navbeforeLogin.clickOnLogoImage();
			} catch (ElementNotInteractableException e) {
				log.error("ElementNotInteractableException");
			}

		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		navbeforeLogin.clickOnMyCartImage();

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		TestUtils.removeProductFromCart(featureProductList.size() - 1);

		log.info("validate add to cart button for the same products test case execution completed");

	}

}
