package com.project.pages.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavbarBeforeLogin {

	WebDriver driver;

	public NavbarBeforeLogin(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(linkText = "Login")
	WebElement loginNavbtn;

	@FindBy(xpath = "//span[contains(text(),'Track Order')]")
	WebElement trackNavbtn;

	public void navigatetologin() {
		loginNavbtn.click();

	}

	public void clickontrackorder() {
		trackNavbtn.click();

	}

}
