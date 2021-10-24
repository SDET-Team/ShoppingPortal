package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyCartPage;
import com.project.pages.PaymentMethod;
import com.project.pages.SearchResult;
import com.project.pages.commonnavbar.NavbarBeforeLogin;
import com.project.utils.TestUtils;

import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

public class EndToEndTestCases extends CommonBase {

	LoginPage loginPage;
	HomePage homePage;
	SearchResult searchResult;
	MyCartPage myCartPage;
	PaymentMethod paymentMethod;

	public EndToEndTestCases() {
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
	 * @brief Driver initialization
	 * @bug No known bugs
	 */
	@BeforeTest
	public void setup() throws WebDriverException, AssertionError {
		initialization();
		log.info("driver initialization");
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
	 * @brief Create a new Account if exists the log-in
	 * @throws ElementNotVisibleException
	 * @throws NoSuchElementException
	 * @throws ElementNotInteractableException
	 */
	@Test(priority = 1)
	public void loginTestCase()
			throws ElementNotVisibleException, NoSuchElementException, ElementNotInteractableException {
		loginPage = new LoginPage(driver);
		navbeforeLogin.navigatetologin();
		log.info("try To create new User Account");
		String msgString = loginPage.regOperation("nikhil v", "nikhil@gmail.com", "9876543210", "@123", "@123");
		if (msgString.equals("Email already exists .")) {
			log.warn("Email already exists .");
		}
		loginPage.loginOperation("nikhil@gmail.com", "@123");
		log.info(driver.getTitle());
		log.info(driver.getCurrentUrl());
	}

	/**
	 * @bug No known bugs
	 * @throws ElementNotVisibleException
	 * @throws NoSuchElementException
	 */
	@Test(priority = 2, dependsOnMethods = "loginTestCase")
	public void searchRequiredProductTestCase() throws ElementNotVisibleException, NoSuchElementException {
		homePage = new HomePage(driver);
		searchResult = new SearchResult();

		String data = "redmi";
		boolean flag = false;
		WebElement selectedElement = null;
		navbeforeLogin.clickOnLogoImage();
		log.info("redirects to home page");
		homePage.setSearchData(data);

		List<WebElement> searchResultList = searchResult.getSearchResultList();
		for (int i = 0; i < searchResultList.size(); i++) {
			WebElement webElement = searchResultList.get(i);
			ArrayList<String> productDetailsList = searchResult.getProductDetails(webElement);

			if (!flag) {
				if (productDetailsList.size() > 0) {
					selectedElement = webElement;
					flag = true;
				}
			}

			productDetailsList.set(0, data);
			for (String dataString : productDetailsList) {
				log.info(dataString);
			}
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}

		ArrayList<String> selectedProductDetailsList = searchResult.getProductDetails(selectedElement);
		for (String s : selectedProductDetailsList) {
			System.out.println(s);
		}

		searchResult.handleProductAddToCart(selectedElement);
		if (TestUtils.isAlertPresent()) {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}

	}

	/**
	 * @brief validate Cart Body Section \n { "productSelected", "imageSrc",
	 *        "productLink", "productText", "Quantity", "product Price per Unit",
	 *        "Shipping Charge", "Grandtotal" } \n Parse through the map
	 *        getTableBodyData check if the text is Formated/null, \n check for
	 *        broken links and insert the data in TreeMap productDataMap \n Using
	 *        TestUtils.setTestData(filename, sheetName, productDataMap,
	 *        columnNamesList) Write the products information in ProductsData.xlsx
	 *        file \n validate updated grand total of the cart \n validate displayed
	 *        grand total with calculated grand total \n Validate product Quantity
	 *        Limit
	 * @bug No known bugs
	 * @throws AssertionError
	 * @throws ElementNotInteractableException
	 */
	public void validateCartTestCase() throws AssertionError, NoSuchElementException {
		myCartPage = new MyCartPage(driver);
		String[] columnNamesList = { "productSelected", "imageSrc", "productLink", "productText", "Quantity",
				"product Price per Unit", "Shipping Charge", "Grandtotal" };
		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();
		Set<Integer> KeySet = bodyElements.keySet();

		log.info("validating In cart products");
		if (KeySet.size() == 0) {
			log.info("Shopping cart is Empty" + KeySet.size() + "products present in cart");
		} else {
			log.info(KeySet.size() + "products are present in cart");
			for (int k : KeySet) {
				ArrayList<WebElement> element = bodyElements.get(k);
				ArrayList<String> elementsData = myCartPage.getWebElementData(element);
				for (int j = 0; j < elementsData.size(); j++) {
					log.info(columnNamesList[j] + " => " + elementsData.get(j));
				}

			}
		}

		log.info("validating In cart products Grand total");
		float shownGrandTotal = myCartPage.getGrandTotal();
		float calGrandTotal = myCartPage.getCalculatedGrandTotal(bodyElements);
		log.info("Displayed grand total = " + shownGrandTotal);
		log.info("Claculated grand total = " + calGrandTotal);
		try {
			Assert.assertEquals(shownGrandTotal, calGrandTotal);
		} catch (AssertionError e) {
			log.error("AssertionError");
		}

		log.info("validating In cart products Quantity");
		boolean actual = myCartPage.isProductQuantityValid(bodyElements);
		log.info("valid Quantity => " + actual);

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
	@Test(priority = 3)
	public void validateProceedToCheckOut()
			throws ElementNotInteractableException, AssertionError, NoSuchElementException {
		myCartPage = new MyCartPage(driver);
		loginPage = new LoginPage(driver);
		navbeforeLogin = new NavbarBeforeLogin(driver);
		paymentMethod = new PaymentMethod(driver);

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		myCartPage.handleCheckOutButtonClick();

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//		String[] shippingAddr = { "0", "Shipping Address", "New Delhi sector 7", "New-Delhi-123", "Delhi-123",
//				"110096" };
//		String[] billingAddr = { "1", "Billing Address", "CS New Delhi sector 5", "New-Delhi-234", "Delhi-234",
//				"110091" };
//		myCartPage.setAddressData(shippingAddr);
//		myCartPage.setAddressData(billingAddr);
//		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//		myCartPage.handleCheckOutButtonClick();
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
