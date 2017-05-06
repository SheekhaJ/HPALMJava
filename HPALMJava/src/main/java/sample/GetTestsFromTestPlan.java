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

import com.Excel.XLSXWorkbook;
import com.authentication.Authentication;
import com.parseXML.DOM.ParseXML;
import com.queries.TestFolderQueries;
import com.queries.TestsQueries;

public class GetTestsFromTestPlan {
	public static void main(String[] args) throws ConfigurationException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, EncryptedDocumentException, InvalidFormatException {
		if(Authentication.isAuthenticated())
			System.out.println("User is already authenticated.");
		else 
			Authentication.authenticate();

		String automationRegressionIdResponse=TestFolderQueries.getTestFolderIDFromPath("AutomationRegression");
		ParseXML parseXML = new ParseXML(automationRegressionIdResponse);
		String automationRegressionFolderID=parseXML.getID();
		System.out.println("automationRegressionFolderID: "+automationRegressionFolderID);
		
		String moduleFolderIDNameResponse=TestFolderQueries.getChildFolders(automationRegressionFolderID);
		parseXML.setResponseString(moduleFolderIDNameResponse);
		LinkedHashMap<String, String> moduleIDName = parseXML.getIDName();
		System.out.println(moduleIDName);
		
		
		XLSXWorkbook xlsx = new XLSXWorkbook();
		if(xlsx.isSheetPresent("ModuleFolderIDName"))
			xlsx.initializeGlobalWorkbookSheet("ModuleFolderIDName");
		else
			xlsx.createNewSheet("ModuleFolderIDName");

		xlsx.setColumnHeadings(Arrays.asList("Module ID", "Module Name"));
		
		List<String> modules = new ArrayList<String>(moduleIDName.keySet());
		
		for(int i=0; i<modules.size(); i++){
			String moduleID = modules.get(i);
			xlsx.setCellStringValue((i+1), 0, moduleID);
			xlsx.setCellStringValue((i+1), 1, moduleIDName.get(moduleID));
			
			String moduleTestCasesResponse = TestsQueries.getTests(moduleID);
			parseXML.setResponseString(moduleTestCasesResponse);
			LinkedHashMap<String, String> moduletestCaseIDName = parseXML.getIDName();
			
			List<String> testCaseIDs = new ArrayList<String>(moduletestCaseIDName.keySet());
			
			System.out.println(moduletestCaseIDName);
			
			for(int j=0; j<testCaseIDs.size(); j++){
				String testCaseID = testCaseIDs.get(j);
				
				if(xlsx.isSheetPresent(moduleIDName.get(moduleID)))
					xlsx.initializeGlobalWorkbookSheet(moduleIDName.get(moduleID));
				else
					xlsx.createNewSheet(moduleIDName.get(moduleID));
				
				xlsx.setColumnHeadings(Arrays.asList("Test Case ID", "Test Case Name"));
				xlsx.setCellStringValue((j+1), 0, testCaseID);
				xlsx.setCellStringValue((j+1), 1, moduletestCaseIDName.get(testCaseID));
			}
		}
		
		xlsx.writeWorkbook();
	}
}
