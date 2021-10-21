package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.HomePage;
import com.project.pages.MyCartPage;
import com.project.pages.commonnavbar.NavbarBeforeLogin;

import org.testng.annotations.BeforeTest;

import java.io.FileNotFoundException;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.project.utils.TestUtils;

/**
 * @file CartActivity.java
 * @author Nikhil varavadekar
 */
public class CartActivityTest extends CommonBase {

	static CartActivity cartActivity;
	MyCartPage myCartPage;
	HomePage homepage;

	public CartActivityTest() {
		super();
	}

	/**
	 * @brief log configuration
	 */
	@BeforeSuite(groups = "Log")
	public void loginit() {
		logConfig();
	}

	/**
	 * 
	 * @brief Driver initialization
	 * @bug No known bugs
	 * @throws WebDriverException
	 * @throws AssertionError
	 */
	@BeforeTest
	public void setup() throws WebDriverException, AssertionError {
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

	/**
	 * 
	 * @brief Driver Closed @bug No known bugs
	 */
	@AfterTest
	public void afterTest() {
		log.info("driver closed");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.close();
	}

	/**
	 * 
	 * @brief validate Home Page title
	 * 
	 * @bug No known bugs
	 * 
	 * @throws AssertionError
	 */
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

	/**
	 * 
	 * @brief Validate Feature Product Text
	 * 
	 * @bug No known bugs
	 * 
	 * @throws AssertionError
	 */
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

	/**
	 * @file CartActivity.java
	 * @brief Validate Feature Product Sort Option text \n { "ALL", "BOOKS",
	 *        "FURNITURE" }
	 * 
	 * @bug No known bugs
	 * 
	 * @throws AssertionError
	 */
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

	/**
	 * @brief Validate Displayed Feature Products \n Columns names { "imageSrc",
	 *        "productLink", "productText", "productPrice", "discountPrice",
	 *        "Add-to-Cart" } \n File name "ProductsData.xlsx" Sheet name "Featured
	 *        Products Details"
	 * 
	 *        Parse through the list getfeatureProductListElement check if the text
	 *        is Formated/null, \n check for broken links and insert the data in
	 *        TreeMap productDataMap
	 * 
	 *        Using TestUtils.setTestData(filename, sheetName, productDataMap,
	 *        columnNamesList) \n Write the products information in
	 *        ProductsData.xlsx file
	 * 
	 * @bug No known bugs
	 * 
	 * @throws AssertionError
	 * @throws HttpTimeoutException
	 * @throws NullPointerException
	 */
	@Test(priority = 4)
	public void validateFeatureProductList() throws AssertionError, NullPointerException, HttpTimeoutException {
		cartActivity = new CartActivity(driver);

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

			String fileString = filePath + "\\src\\resources\\testdata\\" + "ProductsData.xlsx";
			TestUtils.setTestData(fileString, "Featured Products Details", productDataMap, columnNamesList);

		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info("validate Feature Product List test case execution completed");

	}

	/**
	 * @brief Validate Other Products Section \b section names { "Smart Phone",
	 *        "Laptop" } Columns names { "imageSrc", "productLink", "productText",
	 *        "productPrice", "discountPrice", "Add-to-Cart" } \n File name
	 *        "ProductsData.xlsx" Sheet name "Other Featured Products Details"
	 * 
	 *        Parse through the list getfeatureProductListElement check if the text
	 *        is Formated/null, \n check for broken links and insert the data in
	 *        TreeMap productDataMap
	 * 
	 *        Using TestUtils.setTestData(filename, sheetName, productDataMap,
	 *        columnNamesList) \n Write the products information in
	 *        ProductsData.xlsx file
	 * 
	 * @bug No known bugs
	 * 
	 * @throws AssertionError
	 * @throws HttpTimeoutException
	 * @throws NullPointerException
	 */
	@Test(priority = 5)
	public void validateOtherProductList() throws AssertionError, NullPointerException, HttpTimeoutException {
		cartActivity = new CartActivity(driver);
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

			String fileString = filePath + "\\src\\resources\\testdata\\" + "ProductsData.xlsx";
			TestUtils.setTestData(fileString, "Other Products Details", productDataMap, columnNamesList);

		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info("validateFeatureProductList test case execution completed");
	}

	/**
	 * @brief Validate Add to cart button for Displayed Feature Products \n Columns
	 *        names { "imageSrc", "productLink", "productText", "productPrice",
	 *        "discountPrice", "Add-to-Cart" } \n File name "ProductsData.xlsx"
	 *        Sheet name "ProductsDetailsInCart"
	 * 
	 *        Parse through the list getfeatureProductListElement check if the text
	 *        is Formated/null, \n check for broken links and insert the data in
	 *        TreeMap productDataMap
	 * 
	 *        handle Click Action On WebElement "ADD TO CART" handle Click Action On
	 *        WebElement "CONTINUE SHOPPING"
	 * 
	 *        Using TestUtils.setTestData(filename, sheetName, productDataMap,
	 *        columnNamesList) \n Write the products information in
	 *        ProductsData.xlsx file
	 * 
	 * @bug No known bugs
	 * 
	 * @throws HttpTimeoutException
	 * @throws NullPointerException
	 * @throws AssertionError
	 */
	@Test(priority = 6)
	public void validateAddToCartButton() throws NullPointerException, HttpTimeoutException {
		int i;
		cartActivity = new CartActivity(driver);
		myCartPage = new MyCartPage(driver);

		ArrayList<String> tempList;
		String[] columnNamesList = { "imageSrc", "productLink", "productText", "productPrice", "discountPrice",
				"Add-to-Cart" };
		Map<Integer, ArrayList<String>> productDataMap = new TreeMap<Integer, ArrayList<String>>();

		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();
		for (i = 1; i < featureProductList.size(); i++) {
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
			String file = testDataDirectoryPath + "InCartProducts.xlsx";
			TestUtils.setTestData(file, "ProductsDetailsInCart", productDataMap, columnNamesList);
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info(i - 1 + " products added to cart");
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
		myCartPage.removeProductFromCart(18);
		log.info(i - 1 + " products removed from cart");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		navbeforeLogin.clickOnLogoImage();

		log.info("validate add to cart button for products test case execution completed");
	}

	/**
	 * 
	 * @brief validate add to cart button for the same products \n handle Click
	 *        Action On WebElement "ADD TO CART" handle Click Action On WebElement
	 *        "CONTINUE SHOPPING" handle Alert
	 * 
	 *        In Cart Product Quantity increases by 1
	 * 
	 * @bug No known bugs
	 * @throws ElementNotInteractableException
	 */
	@Test(priority = 7)
	public void validateAddToCartForSameProduct() throws ElementNotInteractableException {
		cartActivity = new CartActivity(driver);
		myCartPage = new MyCartPage(driver);
		int numberOfproducts = 4, numberOfTimes = 4;

		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();

		for (int i = 1; i <= numberOfproducts + 1; i++) {
			for (int j = 1; j <= numberOfTimes; j++) {
				driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
				WebElement firstElement = featureProductList.get(i);
				cartActivity.handleClickActionOnWebElement(firstElement, i);

				try {
					navbeforeLogin.clickOnLogoImage();
				} catch (ElementNotInteractableException e) {
					log.error("ElementNotInteractableException");
				}

			}

		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		navbeforeLogin.clickOnMyCartImage();

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		myCartPage.removeProductFromCart(featureProductList.size() - 1);

		log.info("validate add to cart button for the same products test case execution completed");

	}

}
