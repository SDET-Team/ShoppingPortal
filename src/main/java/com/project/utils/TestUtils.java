package com.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.ElementNotVisibleException;
import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.MyCartPage;

/**
 * \file TestUtils.java \author nikhil varavadekar \author shivam k \author
 * nishant nair \author nikhil pawar \data 20/10/2021
 */
public class TestUtils extends CommonBase {

	public static long PAGE_LOAD_TIMEOUT = 30;
	public static long IMPLICIT_WAIT = 5;
	static HttpURLConnection huc = null;
	static int respCode = 200;
	static CartActivity cartActivity;
	static MyCartPage myCartPage;

	public static Object[][] getTestData(String filepath, String type) throws IOException {

		File src = new File(filepath);
		FileInputStream fileinput = new FileInputStream(src);
		Workbook wb = new XSSFWorkbook(fileinput);
		XSSFSheet sheet1;
		if (type.equals("Positive"))
			sheet1 = (XSSFSheet) wb.getSheetAt(0);
		else
			sheet1 = (XSSFSheet) wb.getSheetAt(1);

		if (type.equals("ReviewAutoFillName") || type.equals("ValidateUserName"))
			sheet1 = (XSSFSheet) wb.getSheetAt(1);
		else if (type.equals("AddReview"))
			sheet1 = (XSSFSheet) wb.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();

		int row_count = sheet1.getLastRowNum();
		int column_count = sheet1.getRow(0).getLastCellNum();

		Object data[][] = new Object[row_count][column_count];
		for (int i = 0; i < row_count; i++) {
			for (int j = 0; j < column_count; j++) {

				data[i][j] = sheet1.getRow(i + 1).getCell(j).toString();
			}

		}
		return data;

	}

	/**
	 * @return Alert
	 * @throws NoAlertPresentException
	 */
	public static Alert switchToAlert(WebDriver driver) {
		Alert alert = driver.switchTo().alert();
		return alert;
	}

	/**
	 * \brief logs and returns if the link is broken or not
	 * 
	 * \bug No known bugs
	 * 
	 * @param urlString
	 * @return boolean
	 * @throws NullPointerException
	 * @throws HttpTimeoutException
	 */
	public static boolean isLinkValid(String urlString) {
		String baseUrl = config.getProperty("url");
		boolean isValid = false;

		if (!isTextFormated(urlString)) {
			log.error(urlString + " => " + "Either url is not configured or it is empty");
			return isValid;
		}
		if (!urlString.startsWith(baseUrl)) {
			log.error(urlString + " => " + "belongs to another domain, skipping it");
			return isValid;
		}

		try {
			huc = (HttpURLConnection) (new URL(urlString).openConnection());
			huc.setRequestMethod("HEAD");
			huc.connect();
			respCode = huc.getResponseCode();
			if (respCode >= 400) {
				log.error(urlString + " => " + "is a broken link");
				return isValid;
			} else {
				log.info(urlString + " => " + "is a valid link");
				isValid = true;
				return isValid;
			}
		} catch (MalformedURLException e) {
			log.error("MalformedURLException");
		} catch (IOException e) {
			log.error("IOException");
		} catch (Exception e) {
			log.error("Error");
		}
		return isValid;
	}

	/**
	 * \bug No known bugs
	 * 
	 * @param string
	 * @return boolean
	 * @throws NullPointerException
	 */
	public static boolean isTextFormated(String string) throws NullPointerException {
		if (string == null || string.isEmpty() || string == "#") {
			return false;
		}
		return true;
	}

	/**
	 * \bug No known bugs
	 * 
	 * @param fileName
	 * @param sheetName
	 * @param dataMap
	 * @param columnNames
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static void setTestData(String fileName, String sheetName, Map<Integer, ArrayList<String>> dataMap,
			String[] columnNames) throws FileNotFoundException, IOException, NullPointerException {

		File file = new File(fileName);
		XSSFWorkbook workbook = null;
		if (file.exists() == false) {
			workbook = new XSSFWorkbook();
		} else {
			try {
				InputStream inputStream = new FileInputStream(file);
				workbook = new XSSFWorkbook(inputStream);
			} catch (IOException e) {
				log.error("IOException");
			}
		}

		XSSFSheet spreadsheet = workbook.getSheet(sheetName);
		if (workbook.getNumberOfSheets() != 0) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				if (workbook.getSheetName(i).equals(sheetName)) {
					spreadsheet = workbook.getSheet(sheetName);
				} else {
					spreadsheet = workbook.createSheet(sheetName);
				}
			}
		} else {
			spreadsheet = workbook.createSheet(sheetName);
		}

		XSSFRow row;
		row = spreadsheet.createRow(0);
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
		}

		Set<Integer> keyid = dataMap.keySet();
		int rowid = 1;
		for (Integer key : keyid) {
			row = spreadsheet.createRow(rowid++);
			ArrayList<String> dataArrayList = dataMap.get(key);
			int cellid = 0;

			for (String sData : dataArrayList) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue(sData);
			}
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (IOException e) {
			log.error("IOException");
		}

	}

	/**
	 * \bug No known bugs
	 * 
	 * @return boolean
	 * @throws NoAlertPresentException
	 * @throws TimeoutException
	 */
	public static boolean isAlertPresent() throws NoAlertPresentException, TimeoutException {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException e) {
			return false;
		}

	}

	public static boolean isAlertPresent(WebDriver driver) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException e) {
			return false;
		}

	}

	/**
	 * \bug No known bugs
	 * 
	 * @param WebElement
	 * @return boolean
	 * @throws ElementNotVisibleException
	 */
	public static boolean isVisible(WebElement element) throws ElementNotVisibleException {
		try {
			if (element.isDisplayed()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static Object[][] getTestData(String filepath, int sheetIndex) throws IOException {
		File src = new File(filepath);
		FileInputStream fileinput = new FileInputStream(src);
		Workbook wb = new XSSFWorkbook(fileinput);
		XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(sheetIndex);

		// DataFormatter formatter = new DataFormatter();

		int row_count = sheet.getLastRowNum();
		int column_count = sheet.getRow(0).getLastCellNum();
		Object data[][] = new Object[row_count][column_count];
		for (int i = 0; i < row_count; i++) {
			for (int j = 0; j < column_count; j++) {
				data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
			}
		}
		wb.close();
		return data;
	}

	public static String getscreenShot(WebDriver driver, String filename) {

		TakesScreenshot scrshot = (TakesScreenshot) driver;

		File srcfile = scrshot.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat(" yyyy.MM.dd.HH.mm.ss").format(new Date());
		String currDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		dir1 = currDate;
		String newfilename = filename + " " + timeStamp + ".png";
		String filepath = System.getProperty("user.dir") + "\\automation test output\\screenshots\\" + dir1
				+ "\\Testscreenshots " + timeStamp + "\\" + newfilename;
		try {
			FileUtils.copyFile(srcfile, new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filepath;
	}
}
