package com.project.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class MyCartPage extends CommonBase {

	CartActivity cartActivity;

	@FindBy(className = "shopping-cart-table")
	WebElement cartTable;

	@FindBy(name = "cart")
	WebElement mainCartSection;

	@FindBy(xpath = "//form[@name='cart']//thead//th")
	List<WebElement> tableHead;

	@FindBy(className = "shopping-cart-btn")
	WebElement shoppingCartBtn;

	@FindBy(xpath = "//form[@name='cart']//tbody")
	WebElement shoppingBodySection;

	@FindBy(xpath = "//div[@class=\"shopping-cart\"]//form//tfoot//a")
	WebElement continueShoppingBtn;

	@FindBy(xpath = "//div[@class=\"shopping-cart\"]//form//tfoot//input")
	WebElement updateCartBtn;

	@FindBy(xpath = "//div[@class='shopping-cart']//div[4]//div//span")
	WebElement grandTotalElement;

	@FindBy(xpath = "//div[@class='shopping-cart']//div[4]//tbody//button")
	WebElement proccedToCheckoutBtn;

	@FindBy(xpath = "//div[@class='shopping-cart']//div[@class='col-md-4 col-sm-12 estimate-ship-tax']")
	List<WebElement> addressElements;

	public MyCartPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public List<WebElement> getTableHeadColumnNames() {
		return tableHead;
	}

	public float getGrandTotal() {
		String gtString = grandTotalElement.getText();
		float grandTotal = Float.parseFloat(gtString);
		return grandTotal;
	}

	public WebElement getWebElement(String method, String tagName, WebElement webElement)
			throws ElementNotVisibleException {
		By methodBy = this.getMethodBy(method, tagName);
		return webElement.findElement(methodBy);
	}

	public List<WebElement> getWebElements(String method, String tagName, WebElement webElement)
			throws ElementNotVisibleException {
		List<WebElement> list = new ArrayList<>();
		By methodBy = this.getMethodBy(method, tagName);
		try {
			list = webElement.findElements(methodBy);
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException");
		}
		return list;
	}

	public Map<Integer, ArrayList<WebElement>> getTableBodyData() {
//		All <tr></tr> WebElements
		List<WebElement> trElements = this.getWebElements("tagName", "tr", shoppingBodySection);
		Map<Integer, ArrayList<WebElement>> productDataMap = new TreeMap<Integer, ArrayList<WebElement>>();

		if (trElements.size() != 0) {
			for (int i = 0; i < trElements.size(); i++) {
				WebElement tr = trElements.get(i);

//				All <td></td> WebElements
				List<WebElement> tdElements = this.getWebElements("tagName", "td", tr);
				ArrayList<WebElement> tdList = new ArrayList<>();
				tdList.addAll(tdElements);
				productDataMap.put(i, tdList);
			}
			return productDataMap;
		}
		return productDataMap;
	}

//		e0 checkBox WebElement
//		e1 product image WebElement		
//		e2 product text WebElement		
//		e3 product quantity WebElement
//		e4 product Price Per unit WebElement		
//		e5 product Shipping Charge WebElement		
//		e6 product Grand total WebElement
	public ArrayList<String> getWebElementData(ArrayList<WebElement> eData) {
		ArrayList<String> list = new ArrayList<>();

		WebElement e0 = eData.get(0);
		By methodBy0 = this.getMethodBy("xpath", "//input[@type='checkbox']");
		WebElement productChecked = e0.findElement(methodBy0);
		boolean isChecked = productChecked.isSelected();
		list.add(String.valueOf(isChecked));

		WebElement e1 = eData.get(1);
		By methodBy1 = this.getMethodBy("tagName", "img");
		WebElement productImage = e1.findElement(methodBy1);
		list.add(productImage.getAttribute("src"));

		WebElement e2 = eData.get(2);
		By methodBy2 = this.getMethodBy("tagName", "a");
		WebElement productText = e2.findElement(methodBy2);
		list.add(productText.getAttribute("href"));
		list.add(productText.getAttribute("innerHTML"));

		WebElement e3 = eData.get(3);
		By methodBy3 = this.getMethodBy("xpath", "//input[@type='text']");
		WebElement productQuantity = e3.findElement(methodBy3);
		list.add(productQuantity.getAttribute("value"));

		for (int i = 4; i < eData.size(); i++) {
			WebElement e4 = eData.get(i);
			By methodBy4 = this.getMethodBy("tagName", "span");
			WebElement productPrice = e4.findElement(methodBy4);
			list.add(productPrice.getAttribute("innerHTML"));
		}

		return list;
	}

	public float getCalculatedGrandTotal(Map<Integer, ArrayList<WebElement>> bodyElements) {
		float gTotal = 0;
		javascriptExecutor = (JavascriptExecutor) driver;
		Set<Integer> KeySet = bodyElements.keySet();
		List<Integer> keyList = new ArrayList<Integer>(KeySet);

		if (keyList.size() == 0) {
			log.info("Shopping cart is Empty" + keyList.size() + "products present in cart");
		} else {
			for (int i = 0; i < keyList.size(); i++) {
				int key = keyList.get(i);

//				All td WebElement	
				ArrayList<WebElement> tdElements = bodyElements.get(key);
				WebElement grandTotal = tdElements.get(6);
				javascriptExecutor.executeScript("arguments[0].scrollIntoView();", grandTotal);
				By methodBy = this.getMethodBy("tagName", "span");
				WebElement productGrandTotalElement = grandTotal.findElement(methodBy);
				String gtString = productGrandTotalElement.getText();
//				System.out.println(i + " => "+  gtString);
				gTotal += Float.parseFloat(gtString);
			}

		}

		return gTotal;
	}

	public void setInvalidInputQuantity(Map<Integer, ArrayList<WebElement>> bodyElements) {

		javascriptExecutor = (JavascriptExecutor) driver;
		Random random = new Random();
		Set<Integer> KeySet = bodyElements.keySet();
		List<Integer> keyList = new ArrayList<Integer>(KeySet);

		if (keyList.size() == 0) {
			log.info("Shopping cart is Empty" + keyList.size() + "products present in cart");
		} else {
			for (int i = 0; i < keyList.size(); i++) {
				int key = keyList.get(i);

//				All td WebElement	
				ArrayList<WebElement> tdElements = bodyElements.get(key);
				WebElement quant = tdElements.get(3);
				javascriptExecutor.executeScript("arguments[0].scrollIntoView();", quant);
				By methodBy = this.getMethodBy("tagName", "input");
				WebElement qElement = quant.findElement(methodBy);

				int randomnNumber = random.nextInt(200) - 100;
				qElement.clear();
				qElement.sendKeys("" + randomnNumber);

			}

		}

	}

	public boolean isProductQuantityValid(Map<Integer, ArrayList<WebElement>> bodyElements) {

		Set<Integer> KeySet = bodyElements.keySet();
		List<Integer> keyList = new ArrayList<Integer>(KeySet);

		if (keyList.size() == 0) {
			log.info("Shopping cart is Empty" + keyList.size() + "products present in cart");
			return false;
		} else {
			for (int i = 0; i < keyList.size(); i++) {
				int key = keyList.get(i);

//				All td WebElement	
				ArrayList<WebElement> tdElements = bodyElements.get(key);
				WebElement quant = tdElements.get(3);
				By methodBy = this.getMethodBy("tagName", "input");
				WebElement qElement = quant.findElement(methodBy);
				String qString = qElement.getAttribute("value");
				int quantity = Integer.parseInt(qString);
				if (quantity <= 0) {
					return false;
				}
			}
		}
		return true;

	}

	public boolean selectElementToBDeleted(Map<Integer, ArrayList<WebElement>> bodyElements, int count) {

		if (count >= bodyElements.size()) {
			count = bodyElements.size();
		} else if (count <= 0) {
			count = 0;
		}

		Set<Integer> KeySet = bodyElements.keySet();
		List<Integer> keyList = new ArrayList<Integer>(KeySet);

		if (keyList.size() == 0) {
			log.info("Shopping cart is Empty" + keyList.size() + "products present in cart");
		} else {
			for (int i = 0; i < count; i++) {
				int key = keyList.get(i);

//				All td WebElement	
				ArrayList<WebElement> tdElements = bodyElements.get(key);
				WebElement firstElement = tdElements.get(0);
				By methodBy = this.getMethodBy("tagName", "input");
				WebElement checkBoxElement = firstElement.findElement(methodBy);

				if (!checkBoxElement.isSelected()) {
					try {
						checkBoxElement.click();
					} catch (ElementNotInteractableException e) {
						log.error("ElementNotInteractableException");
						return false;
					}

				}

			}

		}

		return true;
	}

	public void updateShoppingCartClick() {
		updateCartBtn.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		if (TestUtils.isAlertPresent(driver)) {
			alert.accept();
		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public void clickOnCheckOut() throws ElementNotInteractableException {
		proccedToCheckoutBtn.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public By getMethodBy(String methodString, String tagName) {
		methodString = methodString.toLowerCase().trim();
		switch (methodString) {
		case "id":
			return By.id(tagName);
		case "classname":
			return By.className(tagName);
		case "cssselector":
			return By.cssSelector(tagName);
		case "name":
			return By.name(tagName);
		case "tagname":
			return By.tagName(tagName);
		case "xpath":
			return By.xpath(tagName);
		case "linktext":
			return By.linkText(tagName);
		case "partiallinktext":
			return By.partialLinkText(tagName);
		}
		return null;
	}

	/**
	 * \bug No known bugs
	 * 
	 * @param numberOfproducts number of products to be added to cart
	 * @param numberOfTimes    number of times the product should added to cart
	 * @throws ElementNotInteractableException
	 * @throws NoAlertPresentException
	 */
	public void addToCartProduct(int numberOfproducts, int numberOfTimes)
			throws ElementNotInteractableException, NoAlertPresentException {
		cartActivity = new CartActivity(driver);
		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();

		if (numberOfproducts >= featureProductList.size()) {
			numberOfproducts = featureProductList.size();
		} else if (numberOfproducts <= 0) {
			numberOfproducts = 0;
		}

		if (numberOfTimes >= featureProductList.size()) {
			numberOfTimes = featureProductList.size();
		} else if (numberOfTimes <= featureProductList.size()) {
			numberOfTimes = 1;
		}

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
		log.info(numberOfproducts + "Products added to cart");
	}

	/**
	 * \brief select checkbox of the products and update the cart
	 * 
	 * \bug No known bugs
	 * 
	 * @param count number of products to be removed from cart
	 * @throws AssertionError
	 * @throws ElementNotInteractableException
	 */
	public void removeProductFromCart(int count) throws AssertionError, ElementNotInteractableException {
		Map<Integer, ArrayList<WebElement>> bodyElements = this.getTableBodyData();

		boolean isSelected = this.selectElementToBDeleted(bodyElements, count);
		if (isSelected) {
			driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			this.updateShoppingCartClick();
		}

	}

	public void handleCheckOutButtonClick() throws ElementNotInteractableException {
		proccedToCheckoutBtn.click();
	}

	public void setAddressData(String[] addressData) throws AssertionError {
		int j = Integer.parseInt(addressData[0]);
		WebElement addrElement = addressElements.get(j);
		WebElement addrTagElement = null;

		try {
			addrTagElement = addrElement.findElement(By.xpath("//table//thead//tr//th//span[@class='estimate-title']"));
			Assert.assertEquals(addrTagElement.getText().toLowerCase(), addressData[1].toLowerCase());
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException");
		} catch (AssertionError e) {
			log.error(addressData[1] + " Assertion Error");
		} catch (Exception e) {
			log.error("Error");
		}

		try {
			List<WebElement> dataList = addrElement.findElements(
					By.xpath("//table//tbody//tr//td/div[@class='form-group']//div[@class='form-group']"));

			for (int i = 0; i < dataList.size(); i++) {
				WebElement element = null;
				if (i == 0) {
					element = dataList.get(i).findElement(By.tagName("textarea"));
					element.clear();
					element.sendKeys(addressData[i + 2]);
					continue;
				}
				element = dataList.get(i).findElement(By.tagName("input"));
				element.clear();
				element.sendKeys(addressData[i + 2]);
			}
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException");
		} catch (Exception e) {
			log.error("Error");
		}

	}

}
