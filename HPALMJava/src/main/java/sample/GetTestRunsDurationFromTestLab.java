package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.xml.sax.SAXException;

import com.Excel.Excel;
import com.Excel.XLSXWorkbook;
import com.authentication.Authentication;
import com.parseXML.DOM.ParseXML;
import com.queries.TestRunsQueries;
import com.queries.TestSetFoldersQueries;
import com.queries.TestSetsQueries;
import com.queries.TestsQueries;

public class GetTestRunsDurationFromTestLab {
	public static void main(String[] args) throws ConfigurationException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, EncryptedDocumentException, InvalidFormatException {
		if(Authentication.isAuthenticated())
			System.out.println("User is already authenticated.");
		else 
			Authentication.authenticate();
		
		String folderPath="R.16.1/TSG/Regression- Automation";
		String resp=TestSetFoldersQueries.getTestSetFolderIDFromPath(folderPath);
		ParseXML parseXML = new ParseXML(resp);
		String regressionTestSetFolderID=parseXML.getID();
		
		resp=TestSetFoldersQueries.getChildTestSetFolderID(regressionTestSetFolderID);
		parseXML.setResponseString(resp);
		LinkedHashMap<String, String> childTestSetFoldersIDName=parseXML.getIDName();
		
		List<String> childTestSetFolderIDs=new ArrayList<String>(childTestSetFoldersIDName.keySet());
		
		LinkedHashMap<String, String> moduleTestSetIDName=new LinkedHashMap<String, String>();
		
		for(int i=0; i<childTestSetFoldersIDName.size(); i++){
			String childTestSetFolderID=childTestSetFolderIDs.get(i);
			resp=TestSetsQueries.getChildTestSetsByParentID(childTestSetFolderID);
			parseXML.setResponseString(resp);
			moduleTestSetIDName.put(parseXML.getID(), parseXML.getName());
		}
		
		Excel excel1=new XLSXWorkbook("R.16.1ExecutionTimeDuration.xlsx");
		
		List<String> moduleTestSetIDs=new ArrayList<String>(moduleTestSetIDName.keySet());
		
		for(int i=0; i<moduleTestSetIDs.size(); i++){
			String testSetID=moduleTestSetIDs.get(i);
			String moduleResponse=TestRunsQueries.getPassedTestRuns(testSetID);
			parseXML.setResponseString(moduleResponse);
			
			int numOfRuns=parseXML.getNumberOfFieldsNodes();
			System.out.println("Number of runs in a module: "+numOfRuns);
			System.out.println(moduleResponse+"\n");
			List<String> runID=parseXML.getNodesByXpath("//Field[@Name='id']/Value");
			
			String sheetName=moduleTestSetIDName.get(testSetID).replace("Regression_", "");
			
			if(excel1.isSheetPresent(sheetName))
				excel1.initializeGlobalWorkbookSheet(sheetName);
			else 
				excel1.createNewSheet(sheetName);
			
			excel1.setColumnHeadings(Arrays.asList("Test-ID","Test-Name","Owner","Test-Set Name","Status", "Duration"));
			
			ParseXML parseXML2 = new ParseXML(moduleResponse);
			
			for(int j=0; j<numOfRuns; j++){
				
				String testID=parseXML.getNodesByXpath("//Field[@Name='id']/Value[text()='"+runID.get(j)+"']/parent::Field/following-sibling::Field[@Name='test-id']/Value").get(0);
				excel1.setCellStringValue((j+1), 0, testID);
				
				String testNameResponse=TestsQueries.getTestName(testID);
				parseXML2.setResponseString(testNameResponse);
				excel1.setCellStringValue((j+1), 1, parseXML2.getName());
				
				excel1.setCellStringValue((j+1), 2, parseXML.getNodesByXpath("//Field[@Name='id']/Value[text()='"+runID.get(j)+"']/parent::Field/following-sibling::Field[@Name='owner']/Value").get(0));
				
				excel1.setCellStringValue((j+1), 3, moduleTestSetIDName.get(testSetID));
				
				excel1.setCellStringValue((j+1), 4, parseXML.getNodesByXpath("//Field[@Name='id']/Value[text()='"+runID.get(j)+"']/parent::Field/following-sibling::Field[@Name='status']/Value").get(0));
				
				excel1.setCellNumericValue((j+1), 5, parseXML.getNodesByXpath("//Field[@Name='id']/Value[text()='"+runID.get(j)+"']/parent::Field/following-sibling::Field[@Name='duration']/Value").get(0));
			}			
		}
		
		excel1.writeWorkbook();
	}
}
