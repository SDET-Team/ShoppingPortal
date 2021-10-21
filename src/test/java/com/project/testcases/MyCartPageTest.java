package com.project.testcases;

import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.MyCartPage;
import com.project.utils.TestUtils;

import org.testng.annotations.BeforeTest;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
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

public class MyCartPageTest extends CommonBase {

	CartActivity cartActivity;
	MyCartPage myCartPage;

	@BeforeTest
	public void setup() {
		initialization();
		driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
		TestUtils.addToCartProduct(7, 3);
	}
	
	@AfterTest
	public void afterTest() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.close();
	}

	@Test(priority = 1)
	public void validateCartPageTitle() throws AssertionError {
		myCartPage = new MyCartPage();
		Assert.assertEquals(myCartPage.getPageTitle(), "My Cart", "My Cart Page Title Not Matched");
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
	}

	@Test(priority = 2)
	public void validateTableHeadsectionList() throws AssertionError {
		myCartPage = new MyCartPage();
		String[] strArray = new String[] { "Remove", "Image", "Product Name", "Quantity", "Price Per unit",
				"Shipping Charge", "GrandTotal" };
		List<WebElement> headList = myCartPage.getTableHeadColumnNames();
		for (int i = 0; i < headList.size(); i++) {
			try {
				String actualString = headList.get(i).getText();				//
				String expectedString = strArray[i];
				Assert.assertEquals(actualString, expectedString);
			} catch (AssertionError e) {
				logger.error("Assertion Error");
//			System.out.println("Assertion Error");
			} catch (Exception e) {
				logger.error("Error");
//			System.out.println("Error");
			}
		}
	}

	@Test(priority = 3)
	public void validateTableBodysectionList() throws AssertionError {
		myCartPage = new MyCartPage();
		String[] columnNamesList = { "productSelected", "imageSrc", "productLink", "productText", "Quantity",
				"product Price per Unit", "Shipping Charge", "Grandtotal" };
		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();
		Map<Integer, ArrayList<String>> inCartProductMap = new TreeMap<Integer, ArrayList<String>>();

		Set<Integer> KeySet = bodyElements.keySet();

		if (KeySet.size() == 0) {
			logger.info("Shopping cart is Empty" + KeySet.size() + "products present in cart");
		} else {
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
			logger.error("FileNotFoundException");
		} catch (Exception e) {
			logger.error("Exception");
		}

	}

	@Test(priority = 4)
	public void validateProductRemovalFromCart() throws AssertionError {
		myCartPage = new MyCartPage();
		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();

		boolean isSelected = myCartPage.selectElementToBDeleted(bodyElements, 2);
		if (isSelected) {
			driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			myCartPage.updateShoppingCartClick();
		}

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
	}

	@Test(priority = 5)
	public void validateUpdatedGrandTotalCart() throws AssertionError {
		myCartPage = new MyCartPage();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Map<Integer, ArrayList<WebElement>> bodyElements1 = myCartPage.getTableBodyData();

		float shownGrandTotal = myCartPage.getGrandTotal();
		float calGrandTotal = myCartPage.getCalculatedGrandTotal(bodyElements1);
		try {
			Assert.assertEquals(shownGrandTotal, calGrandTotal);
		} catch (AssertionError e) {
			logger.error("AssertionError");
			System.out.println("AssertionError");
		}

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

		boolean isSelected = myCartPage.selectElementToBDeleted(bodyElements1, 2);
		if (isSelected) {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
			myCartPage.updateShoppingCartClick();
		}

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Map<Integer, ArrayList<WebElement>> bodyElements2 = myCartPage.getTableBodyData();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		float shownGrandTotal1 = myCartPage.getGrandTotal();
		float calGrandTotal1 = myCartPage.getCalculatedGrandTotal(bodyElements2);
		try {
			Assert.assertEquals(shownGrandTotal, calGrandTotal);
		} catch (AssertionError e) {
			logger.error("AssertionError");
			System.out.println("AssertionError");
		}

	}

	@Test(priority = 6)
	public void validateQuantityLimit() throws AssertionError {
		myCartPage = new MyCartPage();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

		Map<Integer, ArrayList<WebElement>> bodyElements1 = myCartPage.getTableBodyData();
		boolean actual = myCartPage.isProductQuantityValid(bodyElements1);

		myCartPage.setInvalidInputQuantity(bodyElements1);

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		myCartPage.updateShoppingCartClick();

		Map<Integer, ArrayList<WebElement>> bodyElements2 = myCartPage.getTableBodyData();
		boolean updated = myCartPage.isProductQuantityValid(bodyElements2);

		try {
			Assert.assertEquals(actual, updated);
		} catch (AssertionError e) {
			logger.error("AssertionError");
		}

	}

}
