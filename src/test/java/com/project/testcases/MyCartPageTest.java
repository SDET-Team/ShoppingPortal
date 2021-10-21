package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.MyCartPage;
import com.project.utils.TestUtils;

import org.testng.annotations.BeforeTest;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
//import java.lang.ProcessHandle.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

public class MyCartPageTest extends CommonBase {

	CartActivity cartActivity;
	MyCartPage myCartPage;
	

	@BeforeSuite(groups="Log")
	public void loginit()
	{
		logConfig();
	}

	@BeforeTest
	public void setup() {
		
		initialization();
		log.info("Driver initialization");
		myCartPage = new MyCartPage(driver);
		driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
		TestUtils.addToCartProduct(7, 3);
		log.info("7 Products are already present in the cart as pre-condition");
	}

	@AfterTest
	public void afterTest() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.close();
		log.info("Driver closed");
	}

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

	@Test(priority = 2)
	public void validateTableHeadSectionList() throws AssertionError {
		String[] strArray = new String[] { "Remove", "Image", "Product Name", "Quantity", "Price Per unit",
				"Shipping Charge", "GrandTotal" };
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
		log.info("validate Table Head Section List test case execution completed");
	}

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

			String fileString = filePath +"\\src\\resources\\testdata\\" + "InCartProductsData.xlsx";
			TestUtils.setTestData(fileString, "In Cart Products Details", inCartProductMap, columnNamesList);

		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (Exception e) {
			log.error("Exception");
		}

		log.info("validate cart page table test case execution completed");
	}

	@Test(priority = 4)
	public void validateProductRemovalFromCart() throws AssertionError {
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

	@Test(priority = 6)
	public void validateQuantityLimit() throws AssertionError {
	
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

		log.info("validate Quantity Limit test case execution completed");
	}

}
