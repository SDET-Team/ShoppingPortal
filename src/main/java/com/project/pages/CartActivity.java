package com.project.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
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
	WebElement productPrice;

	@FindBy(className = "action")
	WebElement addToCartAction;

	@FindBy(tagName = "a")
	WebElement anchorElement;

	@FindBy(tagName = "span")
	WebElement spanElement;

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

	public ArrayList<String> getfeatureProductSortElement() {

		ArrayList<String> webArrayList = new ArrayList<>();
		for (WebElement element : featureProductSortElements) {
			webArrayList.add(element.getText());
		}
		return webArrayList;
	}

	public List<WebElement> getfeatureProductListElement() {
		return featureProductList;
	}

	public ArrayList<String> getProductDetails(WebElement webElement) {
		ArrayList<String> productDetailsList = new ArrayList<>();

		WebElement element1 = this.getWebElement("tagname", "img", webElement);
		String productImageSrcString = element1.getAttribute("src");
		productDetailsList.add(productImageSrcString);

		WebElement element2 = this.getWebElement("classname", "name", webElement);
		WebElement element3 = this.getWebElement("tagname", "a", element2);
		String productPageLink = element3.getAttribute("href");
		productDetailsList.add(productPageLink);
		productDetailsList.add(element3.getText());

		List<WebElement> priceList = this.getWebElements("tagname", "span", productPrice);
		for (WebElement e : priceList) {
			productDetailsList.add(e.getText());
		}

//		WebElement element4 = this.getWebElement("tagname", "a", addToCartAction);
//		productDetailsList.add("");

		return productDetailsList;
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
