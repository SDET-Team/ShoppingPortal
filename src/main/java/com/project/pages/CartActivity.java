package com.project.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

public class CartActivity extends CommonBase {

	@FindBy(id = "product-tabs-slider")
	WebElement featureProducts;

	@FindBy(xpath = "//h3[normalize-space()='Featured Products']")
	WebElement featuresProductElement;

	@FindBy(id = "new-products-1")
	WebElement featureProductSort;

	@FindBy(xpath = "//ul[@id='new-products-1']//li/a")
	List<WebElement> featureProductSortElements;

	@FindBy(xpath = "//div[@id='all']//div[@class='owl-wrapper']/div")
	List<WebElement> featureProductList;

	@FindBy(className = "name")
	WebElement productName;

	@FindBy(className = "product-price")
	WebElement productPriceSection;

	@FindBy(className = "price")
	WebElement productPrice;

	@FindBy(className = "price-before-discount")
	WebElement priceBeforeDiscount;

	@FindBy(className = "action")
	WebElement addToCartAction;

	@FindBy(tagName = "a")
	WebElement anchorElement;

	@FindBy(tagName = "span")
	WebElement spanElement;

	@FindBy(xpath = "//div[@class='sections prod-slider-small outer-top-small']//div[@class='row']//div[@class=\"owl-wrapper\"]")
	List<WebElement> otherSectionElements;

	public CartActivity() {
		PageFactory.initElements(driver, this);
	}

	public String indexPageTitle() {
		return driver.getTitle();
	}

	public WebElement getWebElement(String method, String tagName, WebElement webElement)
			throws ElementNotVisibleException {
		By methodBy = this.getMethodBy(method, tagName);
		return webElement.findElement(methodBy);
	}

	public List<WebElement> getWebElements(String method, String tagName, WebElement webElement)
			throws ElementNotVisibleException {
		By methodBy = this.getMethodBy(method, tagName);
		return webElement.findElements(methodBy);
	}

	public String getFeatureProductText() {
		return featuresProductElement.getText();
	}

	public List<WebElement> getfeatureProductListElement() {
		return featureProductList;
	}

	public List<WebElement> getOtherSectionListElement() {
		return otherSectionElements;
	}

	public ArrayList<String> getfeatureProductSortElement() {

		ArrayList<String> webArrayList = new ArrayList<>();
		for (WebElement element : featureProductSortElements) {
			webArrayList.add(element.getText());
		}
		return webArrayList;
	}

	public ArrayList<String> getProductDetails(WebElement webElement) {
		javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);

		ArrayList<String> productDetailsList = new ArrayList<>();

		WebElement element1 = this.getWebElement("tagname", "img", webElement);
		productDetailsList.add(element1.getAttribute("src"));

		WebElement element2 = this.getWebElement("classname", "name", webElement);
		WebElement element3 = this.getWebElement("tagname", "a", element2);
		productDetailsList.add(element3.getAttribute("href"));
		productDetailsList.add(element3.getAttribute("innerHTML"));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		List<WebElement> priceList = this.getWebElements("tagname", "span", productPriceSection);
		for (WebElement e : priceList) {
			productDetailsList.add(e.getAttribute("innerHTML").trim());
		}

		WebElement element4 = this.getWebElement("tagname", "a", addToCartAction);
		productDetailsList.add(element4.getAttribute("innerHTML"));

		return productDetailsList;
	}

	public void handleClickActionOnWebElement(WebElement webElement, int i) throws ElementNotInteractableException {

		javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);
		
		WebElement element = webElement.findElement(By.xpath("//div[" + i + "]//div[1]//div[1]//div[1]//div[3]//a[1]"));

		try {

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			javascriptExecutor.executeScript("arguments[0].click();", element);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			alert = driver.switchTo().alert();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			alert.accept();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		} catch (NoAlertPresentException e) {
			log.error("NoAlertPresentException");
		}

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

}
