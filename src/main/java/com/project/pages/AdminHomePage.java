package com.project.pages;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.project.base.CommonBase;
public class AdminHomePage extends CommonBase
{
		
		
		public AdminHomePage()
		{
			PageFactory.initElements(driver,this);
		}

		public String title() 
		{
			// TODO Auto-generated method stub
			
			return driver.getTitle();
		}
		
	
		
		
	


}
