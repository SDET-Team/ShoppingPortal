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

	@BeforeSuite(groups = "Log")
	public void loginit() {
		logConfig();
	}

	@BeforeTest
	public void setup() {
		initialization();
		HomePage homepage = new HomePage(driver);
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched.");
		navbeforeLogin.navigatetologin();

		LoginPage loginpage = new LoginPage(driver);
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup",
				"Login Page Title Not Matched.");

		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 0);
			String email = data[0][0].toString();
			String password = data[0][1].toString();

			loginpage.loginOperation(email, password);

			afterLoginPage = new AfterLoginPage(driver);
			Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
			log.info("Login to website successful.");
		} catch (IOException e) {
			log.error("Login to website failed.");
		}
	}

	@Test
	public void checkMyProfileEmailFieldNotEditable() {
		String testcase = "Verify_My_Profile_Email_Field_Not_Editable :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.collapseMyProfileTab();
		log.info(testcase + "My Profile section expanded.");

		String output = myAccountPage.getMyProfileEmailField().getAttribute("readOnly");
		if (output.equals("true")) {
			log.info(testcase + "Email field found read-only.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase + "Email field found editable.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertEquals(output, "true",
				"Email field in My Profile tab should be always inactive, but found active.");
	}

	@Test
	public void changeNameAndCheckIfWebpageUpdatesNameInWidgets() {
		String testcase = "Verify_My_Profile_Name_Change_Reflects_In_Webpage :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Profile page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.collapseMyProfileTab();
		log.info(testcase + "My Profile section expanded.");

		String prevName = myAccountPage.getMyProfileNameField().getAttribute("value");
		String newName = prevName;
		if (newName.substring(newName.length() - 1).equals("1")) {
			newName = newName.substring(0, newName.length() - 1);
		} else {
			newName = newName + "1";
		}

		boolean output = myAccountPage.changeMyProfileName(newName);
		if (output == true) {
			log.info(testcase + "My Profile name updated.");
		} else {
			log.error(testcase + "My Profile name update failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "My Account Name update process failed.");

		String welcomeButtonText = myAccountPage.getWelcomeLinkButton().getText();
		String expectedWelcomeButtonText = "Welcome -" + newName;
		if (welcomeButtonText.equals(expectedWelcomeButtonText)) {
			log.info(testcase + "Updated My Profile name reflected on website.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase + "Updated My Profile name not reflected on website.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertEquals(welcomeButtonText, expectedWelcomeButtonText,
				"Welcome button text not changed after name update in profile.");
	}

	@Test(dataProvider = "change_password_data")
	public void changePasswordWithValidCurrentPassword(String curPass, String newPass) {
		String testcase = "Verify_Account_Password_Change_Only_With_Valid_Current_Password :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.collapseChangePasswordTab();
		log.info(testcase + "Change Password section in My Account page expanded.");

		boolean output = myAccountPage.changeAccountPassword(curPass, newPass);
		if (output == true) {
			log.info(testcase + "Account password successfully changed.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase + "Account password change failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Changing password of user account failed.");
	}

	@Test(dataProvider = "billing_address_pin_data")
	public void checkBillingAddressPinCodeOnlyAcceptNumbers(String newPin) {
		String testcase = "Verify_Billing_Address_Pin_Code_Field_Accept_Only_Numbers :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		log.info(testcase + "Navigated to Billing/Shipping address page.");
		myAccountPage.collapseBillingAddressTab();
		log.info(testcase + "Billing address section expanded.");

		boolean output = myAccountPage.changeBillingAddressPinCode(newPin);
		if (output == true) {
			log.info(testcase + "Billing address pin code changed successfully.");
		} else {
			log.error(testcase + "Billing address pin code change failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Updating billing address Pin code failed.");

		String updatedPinCode = myAccountPage.getBillingAddressPinCodeField().getAttribute("value");
		String expectedPinCode = newPin;
		expectedPinCode = extractStartingNumbers(expectedPinCode);
		if (updatedPinCode.equals(expectedPinCode)) {
			log.info(testcase + "Billing address pin code field accepted and updated only numbers.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase
					+ "Billing address pin code field accepted and updated one or more characters which are not numbers.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertEquals(updatedPinCode, expectedPinCode,
				"New pin code with characters got accepted, which was not expected.");
	}

	@Test(dataProvider = "shipping_address_pin_data")
	public void checkShippingAddressPinCodeOnlyAcceptNumbers(String newPin) {
		String testcase = "Verify_Shipping_Address_Pin_Code_Field_Accept_Only_Numbers :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		log.info(testcase + "Navigated to Billing/Shipping address page.");
		myAccountPage.collapseShippingAddressTab();
		log.info(testcase + "Shipping address section expanded.");

		boolean output = myAccountPage.changeShippingAddressPinCode(newPin);
		if (output == true) {
			log.info(testcase + "Shipping address pin code changed successfully.");
		} else {
			log.error(testcase + "Shipping address pin code change failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Updating shipping address Pin code failed.");

		String updatedPinCode = myAccountPage.getShippingAddressPinCodeField().getAttribute("value");
		String expectedPinCode = newPin;
		expectedPinCode = extractStartingNumbers(expectedPinCode);
		if (updatedPinCode.equals(expectedPinCode)) {
			log.info(testcase + "Shipping address pin code field accepted and updated only numbers.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase
					+ "Shipping address pin code field accepted and updated one or more characters which are not numbers.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertEquals(updatedPinCode, expectedPinCode,
				"New pin code with characters got accepted, which was not expected.");
	}

	@Test(dataProvider = "billing_address_state_city_data")
	public void checkBillingAddressStateCityAcceptOnlyCharacters(String newState, String newCity) {
		String testcase = "Verify_Billing_Address_State_City_Fields_Accept_Only_Alphabets :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		log.info(testcase + "Navigated to Billing/Shipping address page.");
		myAccountPage.collapseBillingAddressTab();
		log.info(testcase + "Billing address section expanded.");

		boolean output = myAccountPage.changeBillingAddressStateAndCity(newState, newCity);
		if (output == true) {
			log.info(testcase + "Billing address state and city changed successfully.");
		} else {
			log.error(testcase + "Billing address state and city change failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Updating billing address state and city failed.");

		String updatedState = myAccountPage.getBillingAddressStateField().getAttribute("value");
		String updatedCity = myAccountPage.getBillingAddressCityField().getAttribute("value");
		String expectedState = newState;
		expectedState = extractStartingCharacters(expectedState);
		String expectedCity = newCity;
		expectedCity = extractStartingCharacters(expectedCity);
		output = updatedState.equals(expectedState) && updatedCity.equals(expectedCity);
		if (output == true) {
			log.info(testcase + "Billing address state and city fields accepted and updated only alphabets.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase
					+ "Billing address state and city fields accepted and updated one or more characters which are not alphabets.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "State and city got updated with numbers, which is not expected.");
	}

	@Test(dataProvider = "shipping_address_state_city_data")
	public void checkShippingAddressStateCityAcceptOnlyCharacters(String newState, String newCity) {
		String testcase = "Verify_Shipping_Address_State_City_Fields_Accept_Only_Alphabets :: ";

		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");

		myAccountPage.returnToMyAccount();
		myAccountPage.gotoAddressSettings();
		log.info(testcase + "Navigated to Billing/Shipping address page.");
		myAccountPage.collapseShippingAddressTab();
		log.info(testcase + "Shipping address section expanded.");

		boolean output = myAccountPage.changeShippingAddressStateAndCity(newState, newCity);
		if (output == true) {
			log.info(testcase + "Shipping address state and city changed successfully.");
		} else {
			log.error(testcase + "Shipping address state and city change failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Updating shipping address state and city failed.");

		String updatedState = myAccountPage.getShippingAddressStateField().getAttribute("value");
		String updatedCity = myAccountPage.getShippingAddressCityField().getAttribute("value");
		String expectedState = newState;
		expectedState = extractStartingCharacters(expectedState);
		String expectedCity = newCity;
		expectedCity = extractStartingCharacters(expectedCity);
		output = updatedState.equals(expectedState) && updatedCity.equals(expectedCity);
		if (output == true) {
			log.info(testcase + "Shipping address state and city fields accepted and updated only alphabets.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase
					+ "Shipping address state and city fields accepted and updated one or more characters which are not alphabets.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "State and city got updated with numbers, which is not expected.");
	}

	@DataProvider(name = "change_password_data")
	private Object[][] getChangePasswordData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 1);
			String old_password = data[0][0].toString();
			String new_password = data[0][1].toString();
			return new Object[][] { { old_password, new_password } };
		} catch (IOException e) {
		}

		return new Object[][] { { "", "" } };
	}

	@DataProvider(name = "billing_address_pin_data")
	private Object[][] getBillingAddressPinData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 2);
			String new_pin = data[0][0].toString();
			return new Object[][] { { new_pin } };
		} catch (IOException e) {
		}

		return new Object[][] { { "" } };
	}

	@DataProvider(name = "billing_address_state_city_data")
	private Object[][] getBillingAddressStateCityData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 2);
			String new_state = data[0][1].toString();
			String new_city = data[0][2].toString();
			return new Object[][] { { new_state, new_city } };
		} catch (IOException e) {
		}

		return new Object[][] { { "", "" } };
	}

	@DataProvider(name = "shipping_address_pin_data")
	private Object[][] getShippingAddressPinData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 3);
			String new_pin = data[0][0].toString();
			return new Object[][] { { new_pin } };
		} catch (IOException e) {
		}

		return new Object[][] { { "" } };
	}

	@DataProvider(name = "shipping_address_state_city_data")
	private Object[][] getShippingAddressStateCityData() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 3);
			String new_state = data[0][1].toString();
			String new_city = data[0][2].toString();
			return new Object[][] { { new_state, new_city } };
		} catch (IOException e) {
		}

		return new Object[][] { { "", "" } };
	}

	@AfterTest
	public void tearDown() {
		// try { Thread.sleep(5*1000); } catch(Exception e) {}
		// driver.quit();
		driver.close();
	}

	private String extractStartingNumbers(String str) {
		String res = "0";
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if (i != 0) {
					res = str.substring(0, i);
				}
				break;
			}
		}
		return res;
	}

	private String extractStartingCharacters(String str) {
		String res = "";
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				if (i != 0) {
					res = str.substring(0, i);
				}
				break;
			}
		}
		return res;
	}

}
