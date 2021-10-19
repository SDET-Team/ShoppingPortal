package com.project.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class TrackOrderPage extends CommonBase {


	@FindBy(name="orderid")
	WebElement orderId;

	@FindBy(id = "exampleBillingEmail1")
	WebElement emailId;

	@FindBy(name = "submit")
	WebElement submitBtn;

	@FindBy(xpath = "//td[contains(text(),'Registered email id is invalid')]")
	WebElement invalidOrder;
	
	By rowLocator=By.xpath("//body[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/table[1]/tbody[1]/tr[1]/td");
	
	public WebElement invalidOrderElement()
	{
		return invalidOrder;
	}

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

	public int trackingOption(String orderid,String email)
	{

		orderId.sendKeys(orderid);
		emailId.sendKeys(email);
		submitBtn.click();
		List<WebElement> columns=driver.findElements(rowLocator);
		int colSize=columns.size();
		driver.navigate().back();
		clearForm();
		return colSize;
		
	}

}
