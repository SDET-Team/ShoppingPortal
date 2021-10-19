package com.project.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Verify;
import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyAccountPage;

public class MyAccountPageTest extends CommonBase {
	
	LoginPage loginpage;
	//HomePage homepage;
	//AfterLoginPage afterLoginPage;
	MyAccountPage myAccountPage;
	
	
	public MyAccountPageTest() {
		super();
	}
	
	
	@BeforeClass
	public void setup() {	
		initialization();
		HomePage homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched.");
		navbeforeLogin.navigatetologin();
		
		loginpage=new LoginPage();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched.");
	}
	
	
	@Test(priority=1, dataProvider="login_data")
	public void loginAndNavigateToMyAccount(String email, String password, String expectedResult) {
		boolean actualResult = loginpage.loginOperation(email, password, expectedResult);
		Assert.assertFalse(actualResult);
		
		AfterLoginPage afterLoginPage = new AfterLoginPage();
		Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
		afterLoginPage.goToMyAccountPage();
		
		myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
	}
	
	@Test(priority=2)
	public void checkMyProfileEmailFieldNotEditable() {
		myAccountPage.returnToMyAccount();
		myAccountPage.collapseMyProfileTab();
		Assert.assertEquals(myAccountPage.getMyProfileEmailField().getAttribute("readOnly"), "true", "Email field in My Profile tab should be always inactive, but found active.");
	}
	
	@Test(priority=3)
	public void changeNameAndCheckIfWebpageUpdatesNameInWidgets() {
		myAccountPage.returnToMyAccount();
		myAccountPage.collapseMyProfileTab();
		String prevName = myAccountPage.getMyProfileNameField().getAttribute("value");
		String newName = prevName;
		if(newName.substring( newName.length()-1 ).equals("1")) {
			newName = newName.substring(0, newName.length()-1);
		}
		else {
			newName = newName + "1";
		}
		boolean output = myAccountPage.changeMyProfileName(newName);
		Assert.assertTrue(output, "My Account Name update process failed.");
		String welcomeButtonText = myAccountPage.getWelcomeLinkButton().getText();
		String expectedWelcomeButtonText = "Welcome -" + newName;
		Assert.assertEquals(welcomeButtonText, expectedWelcomeButtonText, "Welcome button text not changed after name update in profile.");
	}
	
	@Test(priority=4, dataProvider="change_password_data")
	public void changePasswordWithValidCurrentPassword(String curPass, String newPass) {
		myAccountPage.returnToMyAccount();
		myAccountPage.collapseChangePasswordTab();
		boolean output = myAccountPage.changeAccountPassword(curPass, newPass);
		Assert.assertTrue(output, "Changing password of user account failed.");
	}
	
	@Test(priority=5)
	public void checkBillingAddressPinCodeOnlyAcceptNumbers() {
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseBillingAddressTab();
		String oldPinCode = myAccountPage.getBillingAddressPinCodeField().getAttribute("value");
		String newPinCode = oldPinCode + "abc";
		boolean output = myAccountPage.changeBillingAddressPinCode(newPinCode);
		Assert.assertTrue(output, "Updating billing address Pin code failed.");
		String updatedPinCode = myAccountPage.getBillingAddressPinCodeField().getAttribute("value");
		String expectedPinCode = newPinCode;
		expectedPinCode = extractStartingNumbers(expectedPinCode);
		Assert.assertEquals(updatedPinCode, expectedPinCode, "New pin code is not updated.");
	}
	
	@Test(priority=5)
	public void checkShippingAddressPinCodeOnlyAcceptNumbers() {
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseShippingAddressTab();
		String oldPinCode = myAccountPage.getShippingAddressPinCodeField().getAttribute("value");
		String newPinCode = oldPinCode + "abc";
		boolean output = myAccountPage.changeShippingAddressPinCode(newPinCode);
		Assert.assertTrue(output, "Updating shipping address Pin code failed.");
		String updatedPinCode = myAccountPage.getShippingAddressPinCodeField().getAttribute("value");
		String expectedPinCode = newPinCode;
		expectedPinCode = extractStartingNumbers(expectedPinCode);
		Assert.assertEquals(updatedPinCode, expectedPinCode, "New pin code is not updated.");
	}
	
	@Test(priority=6)
	public void checkBillingAddressStateCityAcceptOnlyCharacters() {
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseBillingAddressTab();
		
		String oldState = myAccountPage.getBillingAddressStateField().getAttribute("value");
		String oldCity = myAccountPage.getBillingAddressCityField().getAttribute("value");
		String newState = oldState + "123";
		String newCity = oldCity + "123";
		boolean output = myAccountPage.changeBillingAddressStateAndCity(newState, newCity);
		Assert.assertTrue(output, "Updating billing address state and city failed.");
		
		String updatedState = myAccountPage.getBillingAddressStateField().getAttribute("value");
		String updatedCity = myAccountPage.getBillingAddressCityField().getAttribute("value");
		String expectedState = newState;
		expectedState = extractStartingCharacters(expectedState);
		String expectedCity = newCity;
		expectedCity = extractStartingCharacters(expectedCity);
		
		Assert.assertTrue( (updatedState.equals(expectedState) && updatedCity.equals(expectedCity)), "State and city not updated.");
	}
	
	@Test(priority=6)
	public void checkShippingAddressStateCityAcceptOnlyCharacters() {
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseShippingAddressTab();
		
		String oldState = myAccountPage.getShippingAddressStateField().getAttribute("value");
		String oldCity = myAccountPage.getShippingAddressCityField().getAttribute("value");
		String newState = oldState + "123";
		String newCity = oldCity + "123";
		boolean output = myAccountPage.changeShippingAddressStateAndCity(newState, newCity);
		Assert.assertTrue(output, "Updating shipping address state and city failed.");
		
		String updatedState = myAccountPage.getShippingAddressStateField().getAttribute("value");
		String updatedCity = myAccountPage.getShippingAddressCityField().getAttribute("value");
		String expectedState = newState;
		expectedState = extractStartingCharacters(expectedState);
		String expectedCity = newCity;
		expectedCity = extractStartingCharacters(expectedCity);
		
		Assert.assertTrue( (updatedState.equals(expectedState) && updatedCity.equals(expectedCity)), "State and city not updated.");
	}
	
	
	@DataProvider(name="login_data")
	private Object[][] getLoginDetails() {
		return new Object[][] { {"anuj.lpu1@gmail.com", "Test@123", "pass"} };
	}
	
	@DataProvider(name="change_password_data")
	private Object[][] getChangePasswordData() {
		return new Object[][] { {"Test@123", "Test@123"} };
	}
	
	
	@AfterClass
	public void tearDown() {
		//try { Thread.sleep(5*1000); } catch(Exception e) {}
		driver.quit();
	}
	
	
	private String extractStartingNumbers(String str) {
		String res = "0";
		for(int i=0; i<str.length(); i++) {
			if( !Character.isDigit(str.charAt(i)) ) {
				if(i != 0) {
					res = str.substring(0,i);
				}
				break;
			}
		}
		return res;
	}
	
	private String extractStartingCharacters(String str) {
		String res = "";
		for(int i=0; i<str.length(); i++) {
			if( Character.isDigit(str.charAt(i)) ) {
				if(i != 0) {
					res = str.substring(0,i);
				}
				break;
			}
		}
		return res;
	}

}
