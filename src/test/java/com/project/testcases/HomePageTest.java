package com.project.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import com.project.base.CommonBase;
import com.project.pages.HomePage;

public class HomePageTest extends CommonBase {
	HomePage homepage;

	public HomePageTest() {
		super();
	}

	@BeforeTest
	public void setup() {
		initialization();
		homepage = new HomePage();

	}

	@Test
	public void validateTitle() {
		String title = homepage.title();
		Assert.assertEquals(title, "Shopping Portal Home Page", "Home Page Title Not Matched");
	}

	@AfterTest
	public void tearDown() {
		driver.close();
	}

}
