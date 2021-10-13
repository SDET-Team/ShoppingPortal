package com.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;

import com.project.base.CommonBase;

public class TestUtils extends CommonBase {

	public static long IMPLICIT_WAIT = 5;

	static HttpURLConnection huc = null;
	static int respCode = 200;

	public static Object[][] getTestData(String filepath) throws IOException {

		File src = new File(filepath);
		FileInputStream fileinput = new FileInputStream(src);
		Workbook wb = new XSSFWorkbook(fileinput);
		XSSFSheet sheet1 = (XSSFSheet) wb.getSheetAt(0);

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

	public static Alert switchToAlert() {
		Alert alert = driver.switchTo().alert();
		return alert;
	}

	public static boolean isLinkValid(String urlString) {
		String baseUrl = config.getProperty("url");
		boolean isValid = false;

		if (!isTextFormated(urlString)) {
			String errorString = "Either url is not configured or it is empty";
			logger.error(urlString + " => " + errorString);
			return isValid;
		}

		if (!urlString.startsWith(baseUrl)) {
			String errorString = "belongs to another domain, skipping it";
			logger.error(urlString + " => " + errorString);
			return isValid;
		}

		try {
			huc = (HttpURLConnection) (new URL(urlString).openConnection());
			huc.setRequestMethod("HEAD");
			huc.connect();
			respCode = huc.getResponseCode();
			if (respCode >= 400) {
				String errorString = "is a broken link";
				logger.error(urlString + " => " + errorString);
				return isValid;
			} else {
				String infoString = "is a valid link";
				logger.error(urlString + " => " + infoString);
				isValid = true;
				return isValid;
			}
		} catch (MalformedURLException e) {
			logger.fatal("MalformedURLException");
		} catch (IOException e) {
			logger.fatal("IOException");
		} catch (Exception e) {
			logger.fatal("Error");
		}
		return isValid;
	}

	public static boolean isTextFormated(String string) {
		if (string == null || string.isEmpty() || string == "#") {
			return false;
		}
		return true;
	}

	public static void setTestData(String fileName, String sheetName, Map<Integer, ArrayList<String>> dataMap,
			String[] columnNames) throws FileNotFoundException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet(sheetName);
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
			out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException");
		} catch (IOException e) {
			logger.error("IOException");
		}

	}

}
