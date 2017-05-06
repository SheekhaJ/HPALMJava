package com.Excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

public class XLSXWorkbook extends Excel{

	private static XSSFWorkbook xssfWorkbook;
	private static XSSFSheet xssfSheet;
	
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	
	/**
	 * Initializes FileInputStream to the Global Excel Path mentioned in the properties file.
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public XLSXWorkbook() throws EncryptedDocumentException, InvalidFormatException, IOException{
		try{
			fileInputStream = new FileInputStream(globalExcelPath);
			xssfWorkbook=new XSSFWorkbook(fileInputStream);
			
		} catch(FileNotFoundException e){
			System.out.println("Global Excel file mentioned in the properties file doesn't exist.");
			//createNewWorkbook(globalExcelPath, WorkbookType.XLSX);
		}
		
	}
	
	public XLSXWorkbook(String excelPath) throws IOException, EncryptedDocumentException, InvalidFormatException{
		try{
			fileInputStream = new FileInputStream(excelPath);
			globalExcelPath=excelPath;
			if(excelPath.contains(".xlsx"))
				xssfWorkbook = new XSSFWorkbook(fileInputStream);
		} catch(Exception e){
			createNewWorkbook(excelPath, WorkbookType.XLSX);
		}
	}
	
	@Override
	public void initializeGlobalWorkbookSheet(String sheetName) throws IOException, EncryptedDocumentException, InvalidFormatException {
		try{
			if(globalExcelPath.contains(".xlsx")){
				//xssfWorkbook = new XSSFWorkbook(fileInputStream);
				xssfSheet = xssfWorkbook.getSheet(sheetName);
			}
		} /*catch (FileNotFoundException e) {
			createNewWorkbook(globalExcelPath, WorkbookType.XLSX);
		}*/ catch(Exception e) {
			System.out.println("Mentioned Excel Sheet doesn't exist. Thus creating a new sheet with the name "+sheetName);
			xssfSheet=xssfWorkbook.createSheet(sheetName);
			//writeWorkbook();
		} 
	}

	/**
     * Sets the string value passed as parameter in the cell indicated by the parameters. 
     * @param row Row where the value has to be saved. 
     * @param col Column where the value has to be saved. 
     * @param value Value to be saved in the cell indicated by the row and col parameters. 
     * @author 466199
     */
	@Override
	public void setCellStringValue(int row, int col, String value) {
		Row r;
		r=xssfSheet.getRow(row);
		if(r==null){
			r=xssfSheet.createRow(row);

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
			xssfSheet.autoSizeColumn(col);
		}

		/*try{
			r=xssfSheet.getRow(row);
			Cell c=r.getCell(col,MissingCellPolicy.RETURN_NULL_AND_BLANK);
			c.setCellValue(value);
		} catch(Exception e){
			r=xssfSheet.createRow(row);
			Cell c = r.createCell(col);
			c.setCellType(Cell.CELL_TYPE_STRING);
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
		r=xssfSheet.getRow(row);
		if(r==null){
			r=xssfSheet.createRow(row);

			Cell cell =r.createCell(col);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(value));
		} else {
			Cell cell = r.getCell(col);

			if(cell==null){
				cell=r.createCell(col);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(Double.valueOf(value));
			} else {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(value);
			}
		}
		xssfSheet.autoSizeColumn(col);
	}

	@Override
	public void writeWorkbook() throws IOException {
		fileOutputStream=new FileOutputStream(globalExcelPath);
		xssfWorkbook.write(fileOutputStream);
	}

	@Override
	public Workbook createNewWorkbook(String workbookName, WorkbookType workbookType) throws IOException, EncryptedDocumentException,
			InvalidFormatException {
		fileOutputStream = new FileOutputStream(workbookName);
		if(workbookName.toString().toLowerCase().contains("xlsx")){
			xssfWorkbook = new XSSFWorkbook();
			xssfWorkbook.setWorkbookType(XSSFWorkbookType.XLSX);
			xssfWorkbook.createSheet("Sheet1");
			xssfWorkbook.write(fileOutputStream);
		} else 
			System.out.println("Invalid File extension.");
		return xssfWorkbook;
	}

	@Override
	public void writeWorkbook(FileOutputStream fileOutputStream) throws IOException {
		xssfWorkbook.write(fileOutputStream);
	}

	@Override
	public Sheet createNewSheet(String sheetName) {
		xssfSheet = xssfWorkbook.createSheet(sheetName);
		return xssfSheet;
	}
	
	/**
	 * @return the xssfWorkbook
	 */
	/*public static XSSFWorkbook getXssfWorkbook() {
		return xssfWorkbook;
	}*/

	/**
	 * @return the xssfSheet
	 */
	/*public static XSSFSheet getXssfSheet() {
		return xssfSheet;
	}*/

	@Override
	public String getCellStringValue(int row, int col) {
		String value=null;
		Row r;
		try{
			r=xssfSheet.getRow(row);
			Cell c=r.getCell(col,MissingCellPolicy.RETURN_NULL_AND_BLANK);
			value = c.getStringCellValue();
		} catch (Exception e){
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public int getNumberOfColumns(Sheet sheetName) {
		try{
			return xssfSheet.getRow(0).getPhysicalNumberOfCells();
		} catch(NullPointerException e){
			return 0;
		}
	}
	
	@Override
	public List<String> getAllColumnHeadings() {
		List<String> columns = new ArrayList<String>();
		
		for(int i=0; i<getNumberOfColumns(xssfSheet); i++)
			columns.add(xssfSheet.getRow(0).getCell(i,MissingCellPolicy.RETURN_NULL_AND_BLANK).toString());
		
		return columns;
	}

	@Override
	public int getNumberOfRows(Sheet sheetName) {
		return xssfSheet.getPhysicalNumberOfRows();
	}

	@Override
	public boolean isSheetPresent(String sheetName) {
		xssfSheet = xssfWorkbook.getSheet(sheetName);
			if(xssfSheet==null){
				System.out.println(sheetName+" sheet doesn't exist.");
				return false;
			} else
				return true;
	}

/*	@Override
	void setColumnHeadings(List<String> columns) {
		for(int i=0; i<columns.size(); i++)
			setCellStringValue(0, i, columns.get(i));

	}*/
}
