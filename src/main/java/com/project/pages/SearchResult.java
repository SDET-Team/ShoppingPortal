package com.project.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

import net.bytebuddy.jar.asm.Handle;

public class SearchResult extends CommonBase {

	@FindBy(xpath = "//div[@id='grid-container']//div//div[@class='row']")
	WebElement productsRow;

	@FindBy(className = "image")
	WebElement imageElement;

	@FindBy(className = "product-info")
	WebElement productInfo;

	@FindBy(className = "product-price")
	WebElement productPrice;

	@FindBy(className = "action")
	WebElement action;

	@FindBy(className = "list-unstyled")
	WebElement listUnstyled;

	public SearchResult() {
		PageFactory.initElements(driver, this);
	}

	/**
	 * 
	 * @param webElement
	 * @return list of search results WebElements
	 * @throws ElementNotVisibleException
	 */
	public List<WebElement> getSearchResultList() throws ElementNotVisibleException {
		List<WebElement> searchResultList = productsRow
				.findElements(By.xpath("//div[@class='col-sm-6 col-md-4 wow fadeInUp animated']"));
		return searchResultList;
	}

	/**
	 * 0 image src 1 link text 2 text 3 product price 4 discount price 5 add-to-cart
	 * 6 wishlist
	 * 
	 * @param webElement
	 * @return ArrayList of productDetails
	 * @throws ElementNotVisibleException
	 */
	public ArrayList<String> getProductDetails(WebElement webElement)
			throws ElementNotVisibleException, NoSuchElementException {
		javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);

		ArrayList<String> productDetails = new ArrayList<>();
		WebElement product = null;

		try {
			String s = webElement.getAttribute("class");
			String xString = "//div[@class='" + s + "']//h3";
			WebElement element = webElement.findElement(By.xpath(xString));
			if (element.isDisplayed()) {
				productDetails.add(element.getText());
			}
		} catch (Exception e) {
			log.error("Product Found");
		}

		try {

			product = webElement.findElement(By.className("product"));
			WebElement imageSrc = product.findElement(By.tagName("img"));
			productDetails.add(imageSrc.getAttribute("src"));

			WebElement productText = product
					.findElement(By.xpath("//div[@class='product-info text-left']//h3[@class='name']//a"));
			productDetails.add(productText.getAttribute("href"));
			productDetails.add(productText.getAttribute("innerHTML"));

			WebElement productPrice = product
					.findElement(By.xpath("//div[@class='product-price']//span[@class='price']"));
			WebElement discountPrice = product
					.findElement(By.xpath("//div[@class='product-price']//span[@class='price-before-discount']"));
			productDetails.add(productPrice.getAttribute("innerHTML"));
			productDetails.add(discountPrice.getAttribute("innerHTML"));

		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException");
		} catch (Exception e) {
			log.error("Exception");
		}

		return productDetails;
	}

	public void handleProductAddToCart(WebElement webElement) {
		WebElement product = webElement.findElement(By.className("product"));
		WebElement addToCartElement = product.findElement(
				By.xpath("//div[@class='cart clearfix animate-effect']/div[@class='action']/ul/li/a/button"));
		log.info(addToCartElement.getTagName());
		try {
			addToCartElement.click();
		} catch (ElementNotInteractableException e) {
			log.error("ElementNotInteractableException");
		}

	}

}
