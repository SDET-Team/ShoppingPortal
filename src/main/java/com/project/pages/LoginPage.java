package com.project.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class LoginPage extends CommonBase {
	
	@FindBy(name="email")
	WebElement loginEmail;
	
	@FindBy(id="exampleInputPassword1")
	WebElement loginPass;
	
	@FindBy(name="login")
	WebElement loginBtn;
	
	
	@FindBy(id="fullname")
	WebElement fullName;
	
	@FindBy(name="emailid")
	WebElement regEmail;
	
	@FindBy(name="contactno")
	WebElement contactNo;
	
	@FindBy(id="password")
	WebElement newPass;
	
	@FindBy(name="confirmpassword")
	WebElement confirmPass;
	
	@FindBy(id="submit")
	WebElement signupBtn;
		
	@FindBy(linkText="Forgot your Password?")
	WebElement fpassLink;
	
	@FindBy(xpath="//span[contains(text(),'Invalid email id or Password')]")
	WebElement invalidLogin;
	
	@FindBy(xpath="//span[contains(text(),'Email already exists .')]")
	WebElement duplicateEmail;
	
	public LoginPage()
	{
		PageFactory.initElements(driver,this);
	}
	
	
	
	
	//Login 
	public String loginpageTitle()
	{
		return driver.getTitle();
	}
	
	public boolean loginOperation(String email,String password,String expected) 
	{
		loginEmail.sendKeys(email);
		loginPass.sendKeys(password);
		loginBtn.click();
		boolean status=false;
		if(expected.equals("fail"))
			status=invalidLogin.isDisplayed();
		return status;
	}
	
	//forgot
	
	public String forgotpassTitle()
	{
		return driver.getTitle();
	}
	
	public void navigateToForgotpass()
	{
		fpassLink.click();
	}

	
	//registration
	
	public void regFormclear()
	{
		fullName.clear();
		regEmail.clear();
		contactNo.clear();
		newPass.clear();
		confirmPass.clear();
	}
	
	public boolean regOperation(String fname,String email,String contact,String pass,String cpass,String expected)
	{
		
		Alert alert;
		boolean status=false,btnStatus=true;
		
		fullName.sendKeys(fname);
		regEmail.sendKeys(email);
		contactNo.sendKeys(contact);
		newPass.sendKeys(pass);
		confirmPass.sendKeys(cpass);
		if(expected.equals("Duplicate email") && !(signupBtn.isEnabled()) && duplicateEmail.isDisplayed())
		{
			regFormclear();
			return true;
		}
		signupBtn.click();
		WebDriverWait wait = new WebDriverWait(driver,10);
		
		if(expected.equals("Password and Confirm Password Field do not match  !!"))
		{
			wait.until(ExpectedConditions.alertIsPresent());
			alert=TestUtils.switchToAlert();
			String msg=alert.getText();
			alert.accept();
			if(msg.equals(expected))
				status=true;
			else 
				status= false;
			
		}
		else
		{
			wait.until(ExpectedConditions.alertIsPresent());
			alert=TestUtils.switchToAlert();
			String msg=alert.getText();
			alert.accept();
			if(msg.equals(expected))
				status=true;
			else 
				status= false;
			
		}
		
		
		regFormclear();
		return status;
	}
	

}
