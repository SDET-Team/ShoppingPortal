package com.project.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class AdminPage extends CommonBase {
	@FindBy(id = "inputEmail")
	WebElement adminLoginMail;

	@FindBy(name = "password")
	WebElement adminLoginPass;

	@FindBy(name = "submit")
	WebElement adminLoginBtn;

	@FindBy(xpath = "//body/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/a[1]/i[2]")
	WebElement orderManagementList;

	@FindBy(xpath = "//body/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/ul[1]/li[3]/a[1]/b[1]")
	WebElement colorCode;

	@FindBy(xpath = "//body/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/ul[1]/li[3]/a[1]")
	WebElement deliveredOrders;

	public AdminPage() {
		PageFactory.initElements(driver, this);
	}

	// Login
	public String adminloginpageTitle() {
		return driver.getTitle();
	}

	public void adminloginOperation(String email, String password) {
		adminLoginMail.sendKeys(email);
		adminLoginPass.sendKeys(password);
		adminLoginBtn.click();

	}

	public boolean orderManagement() {
		orderManagementList.click();
		boolean status = deliveredOrders.isDisplayed();
		return status;
	}

	public String getColorCode() {
		return colorCode.getCssValue("color");
	}

}
