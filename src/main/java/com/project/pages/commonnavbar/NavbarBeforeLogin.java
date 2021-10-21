package com.project.pages.commonnavbar;

import org.openqa.selenium.ElementNotInteractableException;
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

	@FindBy(className = "logo")
	WebElement logoElement;

	@FindBy(linkText = "My Cart")
	WebElement myCartElement;
	
	public void navigatetologin() throws ElementNotInteractableException {
		loginNavbtn.click();

	}

	public void clickontrackorder() throws ElementNotInteractableException {
		trackNavbtn.click();

	}

	public void clickOnLogoImage() throws ElementNotInteractableException {
		logoElement.click();

	}

	public void clickOnMyCartImage() throws ElementNotInteractableException {
		myCartElement.click();
		
	}

}
