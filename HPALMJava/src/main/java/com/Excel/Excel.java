package com.Excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.authentication.ConfigurationProperties;


public abstract class Excel {
	
	protected static String globalExcelPath; 
	
	protected static FileInputStream fileInputStream;
	protected static FileOutputStream fileOutputStream;
	
	static{
		globalExcelPath=ConfigurationProperties.getProperty("GlobalExcelPath");
	}
	
	public enum WorkbookType{
		XLS, XLSX;
	}
	
	public abstract Workbook createNewWorkbook(String workbookName, WorkbookType workbookType) throws IOException, EncryptedDocumentException, InvalidFormatException;
	
	public abstract Sheet createNewSheet(String sheetName);
	
	public abstract boolean isSheetPresent(String sheetName);
	
	public abstract void initializeGlobalWorkbookSheet(String sheetName) throws IOException, EncryptedDocumentException, InvalidFormatException;
	
	public abstract int getNumberOfRows(Sheet sheetName);
	
	public abstract int getNumberOfColumns(Sheet sheetName);
	
	public void setColumnHeadings(List<String> columns){
		for(int i=0; i<columns.size(); i++)
			setCellStringValue(0, i, columns.get(i));
	}
	
	public abstract void setCellStringValue(int row, int col, String value);
	
	public abstract void setCellNumericValue(int row, int col, String value);
	
	public abstract String getCellStringValue(int row, int col);
	
	public abstract void writeWorkbook() throws IOException;
	
	public abstract void writeWorkbook(FileOutputStream fileOutputStream) throws IOException;
	
	public abstract List<String> getAllColumnHeadings();
}
