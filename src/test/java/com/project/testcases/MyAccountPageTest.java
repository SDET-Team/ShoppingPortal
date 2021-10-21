package com.project.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyAccountPage;
import com.project.utils.TestUtils;

public class MyAccountPageTest extends CommonBase {
	
	AfterLoginPage afterLoginPage;
	
	
	public MyAccountPageTest() {
		super();
	}
	

	@BeforeSuite(groups="Log")
	public void loginit()
	{
		logConfig();
	}
	
	@BeforeTest
	public void setup() {	
		initialization();
		HomePage homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched.");
		navbeforeLogin.navigatetologin();
		
		LoginPage loginpage=new LoginPage();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched.");
		
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 0);
			String email = data[0][0].toString();
			String password = data[0][1].toString();
			
			loginpage.loginOperation(email, password);
			
			afterLoginPage = new AfterLoginPage();
			Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
		} catch (IOException e) {  }
	}
	
<<<<<<< HEAD
	
	@Test(priority=1, dataProvider="login_data")
	public void loginAndNavigateToMyAccount(String email, String password) {
		
		String msg=loginpage.loginOperation(email, password);
		Assert.assertEquals(msg,"Welcome","Authentication Failed!!!");
		AfterLoginPage afterLoginPage = new AfterLoginPage();
		Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
=======
	@Test
	public void checkMyProfileEmailFieldNotEditable() {
>>>>>>> ae3c0e01f59041a1d8dc7fa6ce503126b573de78
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
		myAccountPage.returnToMyAccount();
		myAccountPage.collapseMyProfileTab();
		Assert.assertEquals(myAccountPage.getMyProfileEmailField().getAttribute("readOnly"), "true", "Email field in My Profile tab should be always inactive, but found active.");
	}
	
	@Test
	public void changeNameAndCheckIfWebpageUpdatesNameInWidgets() {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
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
	
	@Test(dataProvider="change_password_data")
	public void changePasswordWithValidCurrentPassword(String curPass, String newPass) {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
		myAccountPage.returnToMyAccount();
		myAccountPage.collapseChangePasswordTab();
		boolean output = myAccountPage.changeAccountPassword(curPass, newPass);
		Assert.assertTrue(output, "Changing password of user account failed.");
	}
	
	@Test(dataProvider="billing_address_pin_data")
	public void checkBillingAddressPinCodeOnlyAcceptNumbers(String newPin) {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseBillingAddressTab();
		
		boolean output = myAccountPage.changeBillingAddressPinCode(newPin);
		Assert.assertTrue(output, "Updating billing address Pin code failed.");
		String updatedPinCode = myAccountPage.getBillingAddressPinCodeField().getAttribute("value");
		String expectedPinCode = newPin;
		expectedPinCode = extractStartingNumbers(expectedPinCode);
		Assert.assertEquals(updatedPinCode, expectedPinCode, "New pin code with characters got accepted, which was not expected.");
	}
	
	@Test(dataProvider="shipping_address_pin_data")
	public void checkShippingAddressPinCodeOnlyAcceptNumbers(String newPin) {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseShippingAddressTab();
		
		boolean output = myAccountPage.changeShippingAddressPinCode(newPin);
		Assert.assertTrue(output, "Updating shipping address Pin code failed.");
		String updatedPinCode = myAccountPage.getShippingAddressPinCodeField().getAttribute("value");
		String expectedPinCode = newPin;
		expectedPinCode = extractStartingNumbers(expectedPinCode);
		Assert.assertEquals(updatedPinCode, expectedPinCode, "New pin code with characters got accepted, which was not expected.");
	}
	
	@Test(dataProvider="billing_address_state_city_data")
	public void checkBillingAddressStateCityAcceptOnlyCharacters(String newState, String newCity) {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseBillingAddressTab();
		
		boolean output = myAccountPage.changeBillingAddressStateAndCity(newState, newCity);
		Assert.assertTrue(output, "Updating billing address state and city failed.");
		
		String updatedState = myAccountPage.getBillingAddressStateField().getAttribute("value");
		String updatedCity = myAccountPage.getBillingAddressCityField().getAttribute("value");
		String expectedState = newState;
		expectedState = extractStartingCharacters(expectedState);
		String expectedCity = newCity;
		expectedCity = extractStartingCharacters(expectedCity);
		
		Assert.assertTrue( (updatedState.equals(expectedState) && updatedCity.equals(expectedCity)), "State and city got updated with numbers, which is not expected.");
	}
	
	@Test(dataProvider="shipping_address_state_city_data")
	public void checkShippingAddressStateCityAcceptOnlyCharacters(String newState, String newCity) {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		
		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		myAccountPage.collapseShippingAddressTab();
		
		boolean output = myAccountPage.changeShippingAddressStateAndCity(newState, newCity);
		Assert.assertTrue(output, "Updating shipping address state and city failed.");
		
		String updatedState = myAccountPage.getShippingAddressStateField().getAttribute("value");
		String updatedCity = myAccountPage.getShippingAddressCityField().getAttribute("value");
		String expectedState = newState;
		expectedState = extractStartingCharacters(expectedState);
		String expectedCity = newCity;
		expectedCity = extractStartingCharacters(expectedCity);
		
		Assert.assertTrue( (updatedState.equals(expectedState) && updatedCity.equals(expectedCity)), "State and city got updated with numbers, which is not expected.");
	}
	
	
	@DataProvider(name="change_password_data")
	private Object[][] getChangePasswordData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 1);
			String old_password = data[0][0].toString();
			String new_password = data[0][1].toString();
			return new Object[][] { { old_password, new_password } };
		} catch (IOException e) {  }
		
		return new Object[][] { {"", ""} };
	}
	
	@DataProvider(name="billing_address_pin_data")
	private Object[][] getBillingAddressPinData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 2);
			String new_pin = data[0][0].toString();
			return new Object[][] { { new_pin } };
		} catch (IOException e) {  }
		
		return new Object[][] { {""} };
	}
	
	@DataProvider(name="billing_address_state_city_data")
	private Object[][] getBillingAddressStateCityData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 2);
			String new_state = data[0][1].toString();
			String new_city = data[0][2].toString();
			return new Object[][] { { new_state, new_city } };
		} catch (IOException e) {  }
		
		return new Object[][] { {"", ""} };
	}
	
	@DataProvider(name="shipping_address_pin_data")
	private Object[][] getShippingAddressPinData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 3);
			String new_pin = data[0][0].toString();
			return new Object[][] { { new_pin } };
		} catch (IOException e) {  }
		
		return new Object[][] { {""} };
	}
	
	@DataProvider(name="shipping_address_state_city_data")
	private Object[][] getShippingAddressStateCityData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 3);
			String new_state = data[0][1].toString();
			String new_city = data[0][2].toString();
			return new Object[][] { { new_state, new_city } };
		} catch (IOException e) {  }
		
		return new Object[][] { {"", ""} };
	}
	
	
	@AfterTest
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
