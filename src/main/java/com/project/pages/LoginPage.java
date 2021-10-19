package com.project.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class LoginPage extends CommonBase {

	@FindBy(name = "email")
	WebElement loginEmail;

	@FindBy(id = "exampleInputPassword1")
	WebElement loginPass;

	@FindBy(name = "login")
	WebElement loginBtn;

	@FindBy(id = "fullname")
	WebElement fullName;

	@FindBy(name = "emailid")
	WebElement regEmail;

	@FindBy(name = "contactno")
	WebElement contactNo;

	@FindBy(id = "password")
	WebElement newPass;

	@FindBy(name = "confirmpassword")
	WebElement confirmPass;

	@FindBy(id = "submit")
	WebElement signupBtn;

	@FindBy(linkText = "Forgot your Password?")
	WebElement fpassLink;

	@FindBy(xpath = "//span[contains(text(),'Invalid email id or Password')]")
	WebElement invalidLogin;

	@FindBy(xpath = "//span[contains(text(),'Email already exists .')]")
	WebElement duplicateEmail;

	
	
	public WebElement invalidLoginElement()
	{
		return invalidLogin;
	}
	
	public WebElement duplicateEmailElement()
	{
		return duplicateEmail;
	}
	
	
	
	public LoginPage() {
		PageFactory.initElements(driver, this);
	}

	// Login
	public String loginpageTitle() {
		return driver.getTitle();
	}
	
	public String loginOperation(String email,String password) 
	{	
		String msg="";
		loginEmail.sendKeys(email);
		loginPass.sendKeys(password);
		loginBtn.click();
		if(driver.getTitle().equals("Shopping Portal | Signi-in | Signup") && TestUtils.isVisible(invalidLoginElement()))
			msg=invalidLogin.getText();
		else if(TestUtils.isVisible(navafterLogin.welcomeElement()))
		 msg=navafterLogin.welcomeUser();
		
		return msg;
		
		
	}

	// forgot

	public String forgotpassTitle() {
		return driver.getTitle();
	}

	public void navigateToForgotpass() {
		fpassLink.click();
	}

	
	// registration

	public void regFormclear() {
		fullName.clear();
		regEmail.clear();
		contactNo.clear();
		newPass.clear();
		confirmPass.clear();
	}
 
	
	public String regOperation(String fname,String email,String contact,String pass,String cpass)
	{
		
		String msg="";
		fullName.sendKeys(fname);
		regEmail.sendKeys(email);
		contactNo.sendKeys(contact);
		newPass.sendKeys(pass);
		confirmPass.sendKeys(cpass);
		if(!(signupBtn.isEnabled()) && TestUtils.isVisible(duplicateEmailElement()))
		{	
			msg= duplicateEmail.getText();
			regFormclear();
			return msg;
		}
		signupBtn.click();
		
		if(TestUtils.isAlertPresent())
		{
			
			Alert alert = TestUtils.switchToAlert();
			msg = alert.getText();
			alert.accept();
			regFormclear();
			
		}
		
		return msg;
			
	}

}
