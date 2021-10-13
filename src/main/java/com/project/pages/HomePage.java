package com.project.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;

public class HomePage extends CommonBase {

	public HomePage() {
		PageFactory.initElements(driver, this);
	}

	public String title() {
		return driver.getTitle();
	}

}
