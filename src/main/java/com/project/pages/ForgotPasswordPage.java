package com.project.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

public class ForgotPasswordPage extends CommonBase {
	
	
	@FindBy(name="email")
	WebElement emailTxt;
	
	@FindBy(id="contact")
	WebElement contactTxt;
	
	
	@FindBy(name="password")
	WebElement passTxt;
	
	
	@FindBy(id="confirmpassword")
	WebElement confirmTxt;
	
	@FindBy(name="change")
	WebElement changePassbtn;
	
	@FindBy(xpath="//span[contains(text(),'Invalid email id or Contact no')]")
	WebElement invalidUser;
	
	@FindBy(xpath="//span[contains(text(),'Password Changed Successfully')]")
	WebElement changeSuccess;
	
	
	public ForgotPasswordPage()
	{
		PageFactory.initElements(driver,this);
	}
	
	public String title()
	{
		return driver.getTitle();
	}
	
	public boolean changepassword(String email,String contact,String newpass,String cpass,String expected)
	{
		emailTxt.sendKeys(email);
		contactTxt.sendKeys(contact);
		passTxt.sendKeys(newpass);
		confirmTxt.sendKeys(cpass);
		changePassbtn.click();
		boolean status;
		if(expected.equals("Valid"))
			status=changeSuccess.isDisplayed();
		else
			status=invalidUser.isDisplayed();
		return status;
		
	}
	
	
	
	
	

}
