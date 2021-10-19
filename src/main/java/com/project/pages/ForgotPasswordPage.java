package com.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class ForgotPasswordPage extends CommonBase {

	@FindBy(name = "email")
	public WebElement emailTxt;

	@FindBy(id = "contact")
	public WebElement contactTxt;

	@FindBy(name = "password")
	public WebElement passTxt;

	@FindBy(id = "confirmpassword")
	public WebElement confirmTxt;

	@FindBy(name = "change")
	public WebElement changePassbtn;

	@FindBy(xpath = "//span[contains(text(),'Invalid email id or Contact no')]")
	public WebElement invalidUser;
	
	@FindBy(xpath = "//span[contains(text(),'Password Changed Successfully')]")
	public WebElement changeSuccess;

	
	
	public WebElement invaliduserElement()
	{
		return invalidUser;
	}
	
	public WebElement changeSuccessElement()
	{
		return changeSuccess;
	}
	
	public ForgotPasswordPage() {
		PageFactory.initElements(driver, this);
	}

	public String title() {
		return driver.getTitle();
	}
	
	public String changepassword(String email,String contact,String newpass,String cpass)
	{	
		String msg="";
		emailTxt.sendKeys(email);
		contactTxt.sendKeys(contact);
		passTxt.sendKeys(newpass);
		confirmTxt.sendKeys(cpass);
		changePassbtn.click();
		
		if(TestUtils.isVisible(invalidUser))
		{
			msg=invalidUser.getText();
		}
		else if(TestUtils.isVisible(changeSuccess))
			msg=changeSuccess.getText();
		else if(!newpass.equals(cpass))
			msg="New Password and Confirm Password should be same!!!";
		return msg;
	}

}
