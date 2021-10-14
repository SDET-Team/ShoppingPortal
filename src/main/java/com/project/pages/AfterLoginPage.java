package com.project.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.project.base.CommonBase;

public class AfterLoginPage extends CommonBase{
	public AfterLoginPage() {
		Assert.assertEquals(driver.getTitle(), "My Cart", "Page title is not as expected.");
		Assert.assertEquals(driver.getCurrentUrl(), "http://localhost/shopping/my-cart.php", "Page URL is not as expected.");
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(css = "body > header > div.top-bar.animate-dropdown > div > div > div.cnt-account > ul > li:nth-child(2) > a")
	WebElement myAccountButton;
	
	public String getTitle() {
		return driver.getTitle();
	}
	
	public void goToMyAccountPage() {
		myAccountButton.click();
	}

}
