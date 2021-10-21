package com.project.pages.commonnavbar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavbarAfterLogin {

	WebDriver driver;

	public NavbarAfterLogin(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(linkText = "Logout")
	WebElement logoutBtn;
	
	@FindBy(xpath="//header/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/a[contains(text(),'Welcome -')]")
	WebElement welcomeMsg;
	
	By usernameLocator=By.xpath("//header/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/a[contains(text(),'Welcome -')]");
	
	public WebElement welcomeElement()
	{
		return welcomeMsg;
	}
	public void logout() {
		logoutBtn.click();
	}
	
	public String welcomeUser()
	{
		if(welcomeMsg.isDisplayed())
			return "Welcome";
		else 
			return "User not logged in!!!";
	}

}
