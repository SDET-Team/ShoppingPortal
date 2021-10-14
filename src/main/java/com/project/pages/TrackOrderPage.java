package com.project.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

public class TrackOrderPage extends CommonBase {


	@FindBy(name="orderid")
	WebElement orderId;

	@FindBy(id = "exampleBillingEmail1")
	WebElement emailId;

	@FindBy(name = "submit")
	WebElement submitBtn;

	@FindBy(xpath = "//td[contains(text(),'Registered email id is invalid')]")
	WebElement invalidOrder;

	public TrackOrderPage() {
		PageFactory.initElements(driver, this);
	}

	public String title() {
		return driver.getTitle();
	}

	public void clearForm() {
		orderId.clear();
		emailId.clear();

	}

	public boolean trackingOption(String orderid,String email,String expected)
	{
		orderId.sendKeys(orderid);
		emailId.sendKeys(email);
		submitBtn.click();
		boolean status=false;

		
		if(expected.equals("Invalid") && invalidOrder.isDisplayed() && invalidOrder.getText().equals("Either order id or Registered email id is invalid"))
		{	
			status=true;
		}

		driver.navigate().back();
		clearForm();
		return status;

	}

}
