package com.project.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

public class HomePage extends CommonBase {


	
	@FindBy(name="product")
	WebElement searchBar;

	@FindBy(xpath = "//div[@id='brand-slider']//div[@class='owl-item']")
	List<WebElement> brandList;

	@FindBy(xpath = "//ul[@class='nav navbar-nav']//li")
	List<WebElement> homePageDropDwnElements;
	
	@FindBy(xpath = "//div[@class=\"contact-info\"]//div[@class=\"social-icons\"]//a")
	List<WebElement> socialMediaIconElements;

	
	public HomePage() {
		PageFactory.initElements(driver, this);
	}

	public String title() {
		return driver.getTitle();
	}
	

	public void searchFunctionality(String productName)
	{
		searchBar.sendKeys(productName);
		//incomplete
		
	}

	public List<WebElement> getBrandWebElements() {
		return brandList;
	}
	
	public List<WebElement> getHomePageDropDwnElements() {
		return homePageDropDwnElements;
	}
	
	public List<WebElement> getsocialMediaIconElements() {
		return socialMediaIconElements;
	}
	
	public String getAnchorTagLink(WebElement webElement) {
		javascriptExecutor = (JavascriptExecutor) driver;

		WebElement element = webElement.findElement(By.tagName("a"));
		javascriptExecutor.executeScript("arguments[0].scrollIntoView();", element);
		return element.getAttribute("href");
	}
	


}
