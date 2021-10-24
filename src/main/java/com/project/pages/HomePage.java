package com.project.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

public class HomePage extends CommonBase {

	@FindBy(name = "product")
	WebElement searchBar;

	@FindBy(xpath = "//div[@id='brand-slider']//div[@class='owl-item']")
	List<WebElement> brandList;

	@FindBy(xpath = "//ul[@class='nav navbar-nav']//li")
	List<WebElement> homePageDropDwnElements;

	@FindBy(xpath = "//div[@class=\"contact-info\"]//div[@class=\"social-icons\"]//a")
	List<WebElement> socialMediaIconElements;

	@FindBy(className = "control-group")
	WebElement searchControlGroup;

	/**
	 * WebElement Initialization
	 */
	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	/**
	 * 
	 * @return String WebPage Title
	 */
	public String title() {
		return driver.getTitle();

	}

	/**
	 * 
	 * @return list of Supported Brands WebElements
	 */
	public List<WebElement> getBrandWebElements() {
		return brandList;
	}

	/**
	 * { "HOME", "BOOKS", "ELECTRONICS", "FURNITURE", "FASHION" }
	 * 
	 * @return list of home page dropDowns WebElements
	 */
	public List<WebElement> getHomePageDropDwnElements() {
		return homePageDropDwnElements;
	}

	/**
	 * 
	 * @return list of social-media-Icon WebElements
	 */
	public List<WebElement> getsocialMediaIconElements() {
		return socialMediaIconElements;
	}

	/**
	 * 
	 * @param webElement
	 * @return string href
	 * @throws ElementNotVisibleException
	 */
	public String getAnchorTagLink(WebElement webElement) throws ElementNotVisibleException {
		javascriptExecutor = (JavascriptExecutor) driver;

		WebElement element = webElement.findElement(By.tagName("a"));
		javascriptExecutor.executeScript("arguments[0].scrollIntoView();", element);
		return element.getAttribute("href");
	}

	/**
	 * 
	 * @param dataString
	 * @throws ElementNotVisibleException
	 * @throws ElementNotInteractableException
	 */
	public void setSearchData(String dataString) throws ElementNotVisibleException, ElementNotInteractableException {
		javascriptExecutor = (JavascriptExecutor) driver;
		WebElement searchElement = searchControlGroup.findElement(By.tagName("input"));
		javascriptExecutor.executeScript("arguments[0].scrollIntoView();", searchElement);
		searchElement.clear();
		searchElement.sendKeys(dataString);
		WebElement searchButton = searchControlGroup.findElement(By.tagName("button"));
		searchButton.click();
	}

}
