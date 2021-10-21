package com.project.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.project.base.CommonBase;

public class AfterLoginPage extends CommonBase{
	public AfterLoginPage(WebDriver driver) {
		Assert.assertEquals(driver.getTitle(), "My Cart", "Page title is not as expected.");
		Assert.assertEquals(driver.getCurrentUrl(), "http://localhost/shopping/my-cart.php", "Page URL is not as expected.");
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	@FindBy(css = "body > header > div.top-bar.animate-dropdown > div > div > div.cnt-account > ul > li:nth-child(2) > a")
	WebElement myAccountButton;
	
	@FindBy(css = ".cnt-account > ul:nth-child(1) > li:nth-child(3) > a:nth-child(1)")
	WebElement myWishlistButton;
	
	@FindBy(css = "li.dropdown:nth-child(2) > a:nth-child(1)")
	WebElement booksCategoryButton;
	
	public String getTitle() {
		return driver.getTitle();
	}
	
	public void goToMyAccountPage() {
		myAccountButton.click();
	}
	
	public void goToWishlistPage() {
		myWishlistButton.click();
	}
	
	public void goToBooksCategory() {
		booksCategoryButton.click();
	}

}
