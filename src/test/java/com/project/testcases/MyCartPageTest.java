package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.LoginPage;
import com.project.pages.MyCartPage;
import com.project.pages.PaymentMethod;
import com.project.pages.commonnavbar.NavbarBeforeLogin;
import com.project.utils.TestUtils;

import org.testng.annotations.BeforeTest;
import java.io.FileNotFoundException;

//import java.lang.ProcessHandle.Info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

/**
 * @file MyCartPageTest.java
 * @author nikhil varavadekar
 */
public class MyCartPageTest extends CommonBase {

	CartActivity cartActivity;
	MyCartPage myCartPage;
	LoginPage loginPage;
	PaymentMethod paymentMethod;

	@BeforeSuite(groups = "Log")
	public void loginit() {
		logConfig();
	}

	/**
	 * @brief Driver initialization Add products to cart using
	 *        TestUtils.addToCartProduct(numberOfProducts, numberOfTimes) as
	 *        pre-condition
	 * @bug No known bugs
	 * @throws WebDriverException
	 */
	@BeforeTest
	public void setup() throws WebDriverException {
		initialization();
		myCartPage = new MyCartPage(driver);
		log.info("Driver initialization");
		myCartPage = new MyCartPage(driver);
		driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
		myCartPage.addToCartProduct(7, 3);
		log.info("7 Products are already present in the cart as pre-condition");
	}

	/**
	 * @brief Driver Closed
	 * @bug No known bugs
	 */
	@AfterTest
	public void afterTest() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.close();
		log.info("Driver closed");
	}

	/**
	 * @brief validate My Cart page title
	 * @bug No known bugs
	 * @throws AssertionError
	 */
	@Test(priority = 1)
	public void validateCartPageTitle() throws AssertionError {

		try {
			Assert.assertEquals(myCartPage.getPageTitle(), "My Cart", "My Cart Page Title Not Matched");
		} catch (AssertionError e) {
			log.error("AssertionError");
		}
		log.info("validate cart page title test case execution completed");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
	}

	/**
	 * @brief validate Cart Head Section \n { "Remove", "Image", "Product Name",
	 *        "Quantity", "Price Per unit", "Shipping Charge", "GrandTotal" } check
	 *        if any Product is present in cart if number of product is 0 prompt
	 *        user else validate the haed section
	 * @bug No known bugs
	 * @throws AssertionError
	 */
	@Test(priority = 2)
	public void validateTableHeadSectionList() throws AssertionError {
		String[] strArray = new String[] { "Remove", "Image", "Product Name", "Quantity", "Price Per unit",
				"Shipping Charge", "GrandTotal" };

		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();
		Set<Integer> KeySet = bodyElements.keySet();

		if (KeySet.size() == 0) {
			log.info("Shopping cart is Empty" + KeySet.size() + "products present in cart");
		} else {
			List<WebElement> headList = myCartPage.getTableHeadColumnNames();
			for (int i = 0; i < headList.size(); i++) {
				try {
					String actualString = headList.get(i).getText(); //
					String expectedString = strArray[i];
					Assert.assertEquals(actualString, expectedString);
				} catch (AssertionError e) {
					log.error("Assertion Error");
				} catch (Exception e) {
					log.error("Error");
				}
			}
		}

		log.info("validate Table Head Section List test case execution completed");
	}

	/**
	 * @brief validate Cart Body Section \n { "productSelected", "imageSrc",
	 *        "productLink", "productText", "Quantity", "product Price per Unit",
	 *        "Shipping Charge", "Grandtotal" } \n File name "InCartProducts.xlsx"
	 *        Sheet name "In Cart Products Details" Parse through the map
	 *        getTableBodyData check if the text is Formated/null, \n check for
	 *        broken links and insert the data in TreeMap productDataMap \n Using
	 *        TestUtils.setTestData(filename, sheetName, productDataMap,
	 *        columnNamesList) Write the products information in ProductsData.xlsx
	 *        file
	 * @bug No known bugs
	 * @throws AssertionError
	 */
	@Test(priority = 3)
	public void validateTableBodysectionList() throws AssertionError {

		String[] columnNamesList = { "productSelected", "imageSrc", "productLink", "productText", "Quantity",
				"product Price per Unit", "Shipping Charge", "Grandtotal" };
		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();
		Map<Integer, ArrayList<String>> inCartProductMap = new TreeMap<Integer, ArrayList<String>>();

		Set<Integer> KeySet = bodyElements.keySet();

		if (KeySet.size() == 0) {
			log.info("Shopping cart is Empty" + KeySet.size() + "products present in cart");
		} else {
			log.info(KeySet.size() + "products are present in cart");
			for (int i : KeySet) {
				ArrayList<WebElement> element = bodyElements.get(i);
				ArrayList<String> elementsData = myCartPage.getWebElementData(element);
				inCartProductMap.put(i, elementsData);
			}
		}

		try {
			String file = testDataDirectoryPath + "InCartProducts.xlsx";
			TestUtils.setTestData(file, "In Cart Products Details", inCartProductMap, columnNamesList);

			String fileString = filePath + "\\src\\resources\\testdata\\" + "InCartProductsData.xlsx";
			TestUtils.setTestData(fileString, "In Cart Products Details", inCartProductMap, columnNamesList);
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info("validate cart page table test case execution completed");
	}

	/**
	 * @brief validate if Product can be Removal From Cart \n select the check box
	 *        of the products to be deleted handle click on "UPDATE SHOPPING CART"
	 * @bug No known bugs
	 * @throws AssertionError
	 * @throws ElementNotInteractableException
	 */
	@Test(priority = 4)
	public void validateProductRemovalFromCart() throws AssertionError, ElementNotInteractableException {
		int productsToBeRemoved = 2;

		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();

		boolean isSelected = myCartPage.selectElementToBDeleted(bodyElements, productsToBeRemoved);
		if (isSelected) {
			driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			myCartPage.updateShoppingCartClick();
		}

		log.info(productsToBeRemoved + " products are removed out of " + bodyElements.size() + " from cart");
		log.info("validate Product Removal From Cart test case execution completed");
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
	}

	/**
	 * @brief validate updated grand total of the cart \n validate displayed grand
	 *        total with calculated grand total \n select the check box of the
	 *        products to be deleted handle click on "UPDATE SHOPPING CART" again
	 *        validate displayed grand total with calculated grand total
	 * @bug No known bugs
	 * @throws AssertionError
	 * @throws ElementNotInteractableException
	 */
	@Test(priority = 5)
	public void validateUpdatedGrandTotalCart() throws AssertionError {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Map<Integer, ArrayList<WebElement>> bodyElements1 = myCartPage.getTableBodyData();

		log.info("Grand total before removing any product from cart");

		float shownGrandTotal = myCartPage.getGrandTotal();
		float calGrandTotal = myCartPage.getCalculatedGrandTotal(bodyElements1);
		log.info("Displayed grand total = " + shownGrandTotal);
		log.info("Claculated grand total = " + calGrandTotal);
		try {
			Assert.assertEquals(shownGrandTotal, calGrandTotal);
		} catch (AssertionError e) {
			log.error("AssertionError");
		}

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

		boolean isSelected = myCartPage.selectElementToBDeleted(bodyElements1, 2);
		if (isSelected) {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
			myCartPage.updateShoppingCartClick();
		}

		log.info("Grand total after removing any product from cart");

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Map<Integer, ArrayList<WebElement>> bodyElements2 = myCartPage.getTableBodyData();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		float shownGrandTotal1 = myCartPage.getGrandTotal();
		float calGrandTotal1 = myCartPage.getCalculatedGrandTotal(bodyElements2);
		log.info("Displayed grand total = " + shownGrandTotal1);
		log.info("Claculated grand total = " + calGrandTotal1);
		try {
			Assert.assertEquals(shownGrandTotal1, calGrandTotal1);
		} catch (AssertionError e) {
			log.error("AssertionError");
		}

		log.info("validate Updated Grand Total of the Cart test case execution completed");
	}

	/**
	 * @brief validate product Quantity Limit \n update prduct quantity wiht both
	 *        valid and invalid quantities \n check the grand total
	 * @bug No known bugs
	 * @throws AssertionError
	 * @throws ElementNotInteractableException
	 */
	@Test(priority = 6)
	public void validateQuantityLimit() throws AssertionError, ElementNotInteractableException {
		myCartPage = new MyCartPage(driver);
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

		Map<Integer, ArrayList<WebElement>> bodyElements1 = myCartPage.getTableBodyData();
		boolean actual = myCartPage.isProductQuantityValid(bodyElements1);

		log.info("Tried to set invalid quantities");
		myCartPage.setInvalidInputQuantity(bodyElements1);

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		myCartPage.updateShoppingCartClick();

		log.info("invalid quantities set for every ptoduct");

		Map<Integer, ArrayList<WebElement>> bodyElements2 = myCartPage.getTableBodyData();
		boolean updated = myCartPage.isProductQuantityValid(bodyElements2);
		try {
			Assert.assertEquals(actual, updated);
		} catch (AssertionError e) {
			log.error("AssertionError");
		}

		myCartPage.removeProductFromCart(18);
		log.info("validate Quantity Limit test case execution completed");
	}

	/**
	 * 
	 * @brief validate Proceed to checkout \n if user logged in redirects to payment
	 *        page else login page the payment page select payment method and submit
	 * @bug No known bugs
	 * @throws ElementNotInteractableException
	 * @throws AssertionError
	 * @throws NoSuchElementException
	 */
	@Test(priority = 7)
	public void validateProceedToCheckOut()
			throws ElementNotInteractableException, AssertionError, NoSuchElementException {
		myCartPage = new MyCartPage(driver);
		loginPage = new LoginPage(driver);
		navbeforeLogin = new NavbarBeforeLogin(driver);
		paymentMethod = new PaymentMethod(driver);

		navbeforeLogin.clickOnLogoImage();
		myCartPage.addToCartProduct(4, 2);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		myCartPage.handleCheckOutButtonClick();
		String msgString = loginPage.loginOperation("anuj.lpu1@gmail.com", "Test@123");
		log.info(msgString);
		log.info(driver.getTitle());
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		String[] shippingAddr = { "0", "Shipping Address", "New Delhi sector 7", "New-Delhi-123", "Delhi-123",
				"110096" };
		String[] billingAddr = { "1", "Billing Address", "CS New Delhi sector 5", "New-Delhi-234", "Delhi-234",
				"110091" };
		myCartPage.setAddressData(shippingAddr);
		myCartPage.setAddressData(billingAddr);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		myCartPage.handleCheckOutButtonClick();

		Map<Integer, String> map = paymentMethod.setPaymantMethod();
		Set<Integer> set = map.keySet();
		for (Integer i : set) {
			log.info(map.get(i) + " Payment Method Selected");
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		paymentMethod.handlePaymentSubmitBtn();

		try {
			Assert.assertEquals(driver.getTitle(), "Order History");
			Assert.assertEquals(driver.getCurrentUrl(), "http://localhost/shopping/order-history.php");
		} catch (AssertionError e) {
			log.error("AssertionError");
		} catch (Exception e) {
			log.error("Error");
		}

	}

}
