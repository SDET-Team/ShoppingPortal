package com.project.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.project.base.CommonBase;

public class MyAccountPage extends CommonBase {

	public MyAccountPage(WebDriver driver) {
		Assert.assertEquals(driver.getTitle(), "My Account", "Page title is not as expected.");
		Assert.assertEquals(driver.getCurrentUrl(), "http://localhost/shopping/my-account.php",
				"Page URL is not as expected.");
		PageFactory.initElements(driver, this);

		this.driver = driver;

	}

	@FindBy(css = "body > header > div.top-bar.animate-dropdown > div > div > div.cnt-account > ul > li:nth-child(1) > a")
	WebElement welcomeLinkButton;

	@FindBy(css = "body > header > div.top-bar.animate-dropdown > div > div > div.cnt-account > ul > li:nth-child(2) > a")
	WebElement myAccountButton;

	@FindBy(css = "#accordion > div.panel.panel-default.checkout-step-01 > div.panel-heading > h4 > a")
	WebElement myProfileTab;

	@FindBy(id = "collapseOne")
	WebElement myProfileTabCollapse;

	@FindBy(id = "name")
	WebElement myProfileNameField;

	@FindBy(id = "exampleInputEmail1")
	WebElement myProfileEmailField;

	@FindBy(id = "contactno")
	WebElement myProfileContactNoField;

	@FindBy(css = "#collapseOne > div > div > div > form > button")
	WebElement myProfileUpdateButton;

	@FindBy(css = "#accordion > div.panel.panel-default.checkout-step-02 > div.panel-heading > h4 > a")
	WebElement changePasswordTab;

	@FindBy(id = "collapseTwo")
	WebElement changePasswordTabCollapse;

	@FindBy(id = "cpass")
	WebElement changePasswordCurrentPasswordField;

	@FindBy(id = "newpass")
	WebElement changePasswordNewPasswordField1;

	@FindBy(id = "cnfpass")
	WebElement changePasswordNewPasswordField2;

	@FindBy(css = "#collapseTwo > div > form > button")
	WebElement changePasswordChangeButton;

	@FindBy(css = "body > div.body-content.outer-top-bd > div > div.checkout-box.inner-bottom-sm > div > div.col-md-4 > div > div > div > div.panel-body > ul > li:nth-child(2) > a")
	WebElement addressSettingsButton;

	@FindBy(css = "#accordion > div.panel.panel-default.checkout-step-01 > div.panel-heading > h4 > a")
	WebElement billingAddressTab;

	@FindBy(id = "collapseOne")
	WebElement billingAddressTabCollapse;

	@FindBy(css = "#collapseOne > div > div > div > form > button")
	WebElement billingAddressUpdateButton;

	@FindBy(id = "bilingstate")
	WebElement billingAddressStateField;

	@FindBy(id = "billingcity")
	WebElement billingAddressCityField;

	@FindBy(id = "billingpincode")
	WebElement billingAddressPinCodeField;

	@FindBy(css = "#accordion > div.panel.panel-default.checkout-step-02 > div.panel-heading > h4 > a")
	WebElement shippingAddressTab;

	@FindBy(id = "collapseTwo")
	WebElement shippingAddressTabCollapse;

	@FindBy(css = "#collapseTwo > div > form > button")
	WebElement shippingAddressUpdateButton;

	@FindBy(id = "shippingstate")
	WebElement shippingAddressStateField;

	@FindBy(id = "shippingcity")
	WebElement shippingAddressCityField;

	@FindBy(id = "shippingpincode")
	WebElement shippingAddressPinCodeField;

	@FindBy(css = ".nav-checkout-progress > li:nth-child(3) > a:nth-child(1)")
	WebElement orderHistoryButton;

	public String getTitle() {
		return driver.getTitle();
	}

	public void returnToMyAccount() {
		myAccountButton.click();
	}

	public void collapseMyProfileTab() {
		String collapseAttributeValue = myProfileTabCollapse.getAttribute("class");
		if (collapseAttributeValue.equals("panel-collapse collapse in")) {
			return;
		} else {
			myProfileTab.click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(myProfileUpdateButton));
		}
	}

	public boolean changeMyProfileName(String newName) {
		if (myProfileNameField.getAttribute("required").equals("true")) {
			Assert.assertNotEquals(newName, "", "New name is empty which is not allowed.");
			if (newName.equals(""))
				return false;
		}

		myProfileNameField.clear();
		myProfileNameField.sendKeys(newName);
		myProfileUpdateButton.click();
		Alert updateMsg = driver.switchTo().alert();
		String output = updateMsg.getText();
		updateMsg.accept();
		if (output.equals("Your info has been updated"))
			return true;
		return false;
	}

	public WebElement getWelcomeLinkButton() {
		return welcomeLinkButton;
	}

	public WebElement getMyProfileNameField() {
		return myProfileNameField;
	}

	public WebElement getMyProfileEmailField() {
		return myProfileEmailField;
	}

	public void collapseChangePasswordTab() {
		String collapseAttributeValue = changePasswordTabCollapse.getAttribute("class");
		if (collapseAttributeValue.equals("panel-collapse collapse in")) {
			return;
		} else {
			changePasswordTab.click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(changePasswordChangeButton));
		}
	}

	public boolean changeAccountPassword(String curPass, String newPass) {
		if (changePasswordCurrentPasswordField.getAttribute("required").equals("true")
				&& changePasswordNewPasswordField2.getAttribute("required").equals("true")) {
			Assert.assertNotEquals(curPass, "", "Current password is empty which is not allowed.");
			Assert.assertNotEquals(newPass, "", "New password is empty which is not allowed.");
			if (curPass.equals("") || newPass.equals(""))
				return false;
		}

		changePasswordCurrentPasswordField.clear();
		changePasswordCurrentPasswordField.sendKeys(curPass);
		changePasswordNewPasswordField1.clear();
		changePasswordNewPasswordField1.sendKeys(newPass);
		changePasswordNewPasswordField2.clear();
		changePasswordNewPasswordField2.sendKeys(newPass);

		changePasswordChangeButton.click();

		Alert updateMsg = driver.switchTo().alert();
		String output = updateMsg.getText();
		updateMsg.accept();
		if (output.equals("Password Changed Successfully !!"))
			return true;
		return false;
	}

	public void gotoAddressSettings() {
		addressSettingsButton.click();
	}

	public void collapseBillingAddressTab() {
		String collapseAttributeValue = billingAddressTabCollapse.getAttribute("class");
		if (collapseAttributeValue.equals("panel-collapse collapse in")) {
			return;
		} else {
			billingAddressTab.click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(billingAddressUpdateButton));
		}
	}

	public boolean changeBillingAddressPinCode(String newPinCode) {
		if (billingAddressPinCodeField.getAttribute("required").equals("true")) {
			Assert.assertNotEquals(newPinCode, "", "New pin code is empty which is not allowed.");
			if (newPinCode.equals(""))
				return false;
		}

		billingAddressPinCodeField.clear();
		billingAddressPinCodeField.sendKeys(newPinCode);
		billingAddressUpdateButton.click();

		Alert updateMsg = driver.switchTo().alert();
		String output = updateMsg.getText();
		updateMsg.accept();
		if (output.equals("Billing Address has been updated")) {
			return true;
		}
		return false;
	}

	public boolean changeBillingAddressStateAndCity(String state, String city) {
		if (billingAddressStateField.getAttribute("required").equals("true")
				&& billingAddressCityField.getAttribute("required").equals("true")) {
			Assert.assertNotEquals(state, "", "New state entry is empty which is not allowed.");
			if (state.equals(""))
				return false;
			Assert.assertNotEquals(city, "", "New city entry is empty which is not allowed.");
			if (city.equals(""))
				return false;
		}

		billingAddressStateField.clear();
		billingAddressStateField.sendKeys(state);
		billingAddressCityField.clear();
		billingAddressCityField.sendKeys(city);
		billingAddressUpdateButton.click();

		Alert updateMsg = driver.switchTo().alert();
		String output = updateMsg.getText();
		updateMsg.accept();
		if (output.equals("Billing Address has been updated")) {
			return true;
		}
		return false;
	}

	public WebElement getBillingAddressPinCodeField() {
		return billingAddressPinCodeField;
	}

	public WebElement getBillingAddressStateField() {
		return billingAddressStateField;
	}

	public WebElement getBillingAddressCityField() {
		return billingAddressCityField;
	}

	public void collapseShippingAddressTab() {
		String collapseAttributeValue = shippingAddressTabCollapse.getAttribute("class");
		if (collapseAttributeValue.equals("panel-collapse collapse in")) {
			return;
		} else {
			shippingAddressTab.click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(shippingAddressUpdateButton));
		}
	}

	public boolean changeShippingAddressPinCode(String newPinCode) {
		if (shippingAddressPinCodeField.getAttribute("required").equals("true")) {
			Assert.assertNotEquals(newPinCode, "", "New pin code is empty which is not allowed.");
			if (newPinCode.equals(""))
				return false;
		}

		shippingAddressPinCodeField.clear();
		shippingAddressPinCodeField.sendKeys(newPinCode);
		shippingAddressUpdateButton.click();

		Alert updateMsg = driver.switchTo().alert();
		String output = updateMsg.getText();
		updateMsg.accept();
		if (output.equals("Shipping Address has been updated")) {
			return true;
		}
		return false;
	}

	public boolean changeShippingAddressStateAndCity(String state, String city) {
		if (shippingAddressStateField.getAttribute("required").equals("true")
				&& shippingAddressCityField.getAttribute("required").equals("true")) {
			Assert.assertNotEquals(state, "", "New state entry is empty which is not allowed.");
			if (state.equals(""))
				return false;
			Assert.assertNotEquals(city, "", "New city entry is empty which is not allowed.");
			if (city.equals(""))
				return false;
		}

		shippingAddressStateField.clear();
		shippingAddressStateField.sendKeys(state);
		shippingAddressCityField.clear();
		shippingAddressCityField.sendKeys(city);
		shippingAddressUpdateButton.click();

		Alert updateMsg = driver.switchTo().alert();
		String output = updateMsg.getText();
		updateMsg.accept();
		if (output.equals("Shipping Address has been updated")) {
			return true;
		}
		return false;
	}

	public WebElement getShippingAddressPinCodeField() {
		return shippingAddressPinCodeField;
	}

	public WebElement getShippingAddressStateField() {
		return shippingAddressStateField;
	}

	public WebElement getShippingAddressCityField() {
		return shippingAddressCityField;
	}

	public void gotoOrderHistoryPageButton() {
		orderHistoryButton.click();
	}

}
