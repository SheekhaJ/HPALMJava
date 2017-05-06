package com.Excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XLSWorkbook extends Excel{

	private static HSSFWorkbook hssfWorkbook;
	private static HSSFSheet hssfSheet;
	
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	
	public XLSWorkbook() throws FileNotFoundException{
		fileInputStream = new FileInputStream(globalExcelPath);
	}
	
	public XLSWorkbook(String excelPath) throws IOException, EncryptedDocumentException, InvalidFormatException{
		try{
			fileInputStream=new FileInputStream(excelPath);
			if(excelPath.contains(".xls"))
				hssfWorkbook = new HSSFWorkbook(fileInputStream);
		} catch(Exception e){
			System.out.println("Global Excel file mentioned in the properties file doesn't exist.");
			//createNewWorkbook(excelPath, WorkbookType.XLS);
		}
	}
	
	@Override
	public void initializeGlobalWorkbookSheet(String sheetName) throws IOException, EncryptedDocumentException, InvalidFormatException {
		try{
			if(globalExcelPath.contains(".xls")){
				hssfWorkbook = new HSSFWorkbook(fileInputStream);
				hssfSheet = hssfWorkbook.getSheet(sheetName);
			}
		} catch(NullPointerException e) {
			System.out.println("Mentioned Excel Sheet doesn't exist. Thus creating a new sheet with the name "+sheetName);
			hssfSheet=hssfWorkbook.createSheet(sheetName);
			writeWorkbook();
		} catch (FileNotFoundException e) {
			createNewWorkbook(globalExcelPath, WorkbookType.XLS);
		}
	}

	@Override
	public void setCellStringValue(int row, int col, String value) {
		Row r;
		r=hssfSheet.getRow(row);
		if(r==null){
			r=hssfSheet.createRow(row);

			Cell cell =r.createCell(col);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value);
		} else {
			Cell cell = r.getCell(col);

			if(cell==null){
				cell=r.createCell(col);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value);
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value);
			}
		}
		hssfSheet.autoSizeColumn(col);
		/*Row r;
		try{
			r=hssfSheet.getRow(row);
			Cell c=r.getCell(col);
			c.setCellValue(value);
		} catch(Exception e){
			r=hssfSheet.createRow(row);
			Cell c = r.createCell(col);
			c.setCellValue(value);
		}*/
	}

	/**
     * Sets the numeric value passed as parameter in the cell indicated by the parameters. 
     * @param row Row where the value has to be saved. 
     * @param col Column where the value has to be saved. 
     * @param value Value to be saved in the cell indicated by the row and col parameters. 
     * @author 466199
     */
	@Override
	public void setCellNumericValue(int row, int col, String value) {
		Row r;
		r=hssfSheet.getRow(row);
		if(r==null){
			r=hssfSheet.createRow(row);

			Cell cell =r.createCell(col);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(value);
		} else {
			Cell cell = r.getCell(col);

			if(cell==null){
				cell=r.createCell(col);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(value);
			} else {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(value);
			}
		}
		hssfSheet.autoSizeColumn(col);
	}
	
	@Override
	public void writeWorkbook() throws IOException {
		fileOutputStream=new FileOutputStream(globalExcelPath);
		hssfWorkbook.write(fileOutputStream);
	}

	@Override
	public Workbook createNewWorkbook(String workbookName, WorkbookType workbookType) throws IOException, EncryptedDocumentException,
			InvalidFormatException {
		fileOutputStream = new FileOutputStream(workbookName);
		if(workbookName.toString().toLowerCase().contains("xls")){
			fileInputStream = new FileInputStream(workbookName);
			hssfWorkbook = new HSSFWorkbook(fileInputStream);
			HSSFWorkbook.create(InternalWorkbook.createWorkbook());
		} else 
			System.out.println("Invalid File extension.");
		return hssfWorkbook;
	}

	@Override
	public void writeWorkbook(FileOutputStream fileOutputStream) throws IOException {
		hssfWorkbook.write(fileOutputStream);
	}

	@Override
	public Sheet createNewSheet(String sheetName) {
		hssfSheet = hssfWorkbook.createSheet(sheetName);
		return hssfSheet;
	}

	@Override
	public String getCellStringValue(int row, int col) {
		String value=null;
		Row r;
		try{
			r=hssfSheet.getRow(row);
			Cell c=r.getCell(col, MissingCellPolicy.RETURN_NULL_AND_BLANK);
			value = c.getStringCellValue();
		} catch (Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * @return the hssfWorkbook
	 */
	/*public static HSSFWorkbook getHssfWorkbook() {
		return hssfWorkbook;
	}*/

	/**
	 * @return the hssfSheet
	 */
	/*public static HSSFSheet getHssfSheet() {
		return hssfSheet;
	}*/

	@Override
	public int getNumberOfColumns(Sheet sheetName) {
		try{
			return hssfSheet.getRow(0).getPhysicalNumberOfCells();
		} catch(NullPointerException e){
			return 0;
		}
	}
	
	@Override
	public List<String> getAllColumnHeadings() {
		List<String> columns = new ArrayList<String>();
		
		for(int i=0; i<getNumberOfColumns(hssfSheet); i++)
			columns.add(hssfSheet.getRow(0).getCell(i).toString());
		
		return columns;
	}

	@Override
	public int getNumberOfRows(Sheet sheetName) {
		return hssfSheet.getPhysicalNumberOfRows();
	}

	@Override
	public boolean isSheetPresent(String sheetName) {
		hssfSheet = hssfWorkbook.getSheet(sheetName);
		if(hssfSheet==null){
			System.out.println(sheetName+" sheet already exists.");
			return false;
		}
		else
			return true;
	}

/*	@Override
	void setColumnHeadings(List<String> columns) {
			for(int i=0; i<columns.size(); i++)
				setCellStringValue(0, i, columns.get(i));
		
	}*/
}
