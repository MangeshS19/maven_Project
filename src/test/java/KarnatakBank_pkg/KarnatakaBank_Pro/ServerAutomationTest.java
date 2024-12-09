package KarnatakBank_pkg.KarnatakaBank_Pro;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;

public class ServerAutomationTest {

	ServerAutomation automation = new ServerAutomation();

    @Test
    public void testShellScriptExecution() throws Exception {
    	
    	
	FileInputStream fs = new FileInputStream("/home/mangesh.sutar/eclipse-workspace/KarnatakaBank_Pro/DATA/DATASHEET.xlsx");
        
		//open excel sheet
		Workbook w1 = WorkbookFactory.create(fs);
        
		//go to actual sheet		
		Sheet s1  =w1.getSheet("userdetails");
 
		//username 
		Row row =s1.getRow(1);
		Cell c1 =row.getCell(0);
  		String username = c1.getStringCellValue();
  		
  		//password
		Row row2 =s1.getRow(1);
		Cell c2 =row.getCell(1);
  		String password = c2.getStringCellValue();
  		
  	
    	automation.connectToServer("10.151.110.208", 22, username, password);

        // Step 2: Navigate to the path and run the shell script
       automation.executeCommand("cd /var/www/karnataka_bank && ./start.sh");

        // Step 3: Verify Excel file
        String filePath = "cd /var/www/karnataka_bank/OUT";
        automation.verifyExcelFile(filePath, filePath);

        
        
        
        // Step 4: Disconnect
        automation.disconnectFromServer();
}
    
}
