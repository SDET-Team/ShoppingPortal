package com.project.pages;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.project.base.CommonBase;

public class PaymentMethod extends CommonBase {

	@FindBy(id = "collapseOne")
	WebElement paymentBodyElement;

	public PaymentMethod(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public Map<Integer, String> setPaymantMethod() {
		Random random = new Random();
		Map<Integer, String> paymentMap = new TreeMap<Integer, String>();
		String[] paymentString = { "COD", "Internet Banking", "Debit/Credit card" };
		List<WebElement> paymentModes = paymentBodyElement.findElements(By.tagName("input"));

		int modeNum = random.nextInt(3);
		WebElement mode = paymentModes.get(modeNum);
		paymentMap.put(modeNum, paymentString[modeNum]);

		if (mode.isSelected()) {
			return paymentMap;
		} else {
			mode.click();
		}

		return paymentMap;
	}

	public void handlePaymentSubmitBtn() {
		List<WebElement> paymentModes = paymentBodyElement.findElements(By.tagName("input"));
		WebElement element = paymentModes.get(paymentModes.size()-1);
		paymentModes.clear();
		paymentModes.add(element);

		try {
			element.click();
		} catch (ElementNotInteractableException e) {
			log.error("ElementNotInteractableException");
		}

	}

}
