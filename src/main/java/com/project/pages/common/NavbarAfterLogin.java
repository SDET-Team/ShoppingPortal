package com.project.pages.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavbarAfterLogin {
	

	WebDriver driver;
	
	public NavbarAfterLogin(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	
	@FindBy(linkText="Logout")
	WebElement logoutBtn;
	
	
	public void logout()
	{
		logoutBtn.click();
	}

}
