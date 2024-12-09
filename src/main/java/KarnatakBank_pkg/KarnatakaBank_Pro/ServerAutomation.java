package KarnatakBank_pkg.KarnatakaBank_Pro;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import com.jcraft.jsch.*;

public class ServerAutomation {

    private Session session;

    public void connectToServer(String host, int port, String user, String password) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        System.out.println("Connected to server: " + host);
    }

    public String executeCommand(String command) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        InputStream input = channelExec.getInputStream();
        channelExec.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        channelExec.disconnect();
        return output.toString().trim();
    }

    public void disconnectFromServer() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            System.out.println("Disconnected from server");
        }
    }

    public void verifyExcelFile(String folderPath, String filePattern) throws JSchException, IOException {
        // Find the file dynamically
    	filePattern ="KBankChatHistory_20241202_to_20241208";
        String command = "ls -ltr " + folderPath + " | grep '" + filePattern + "' | tail -n 1 | awk '{print $NF}'";
        String dynamicFileName = executeCommand(command);

        if (dynamicFileName == null || dynamicFileName.isEmpty()) {
            throw new FileNotFoundException("No file matching pattern " + filePattern + " found in " + folderPath);
        }

        String filePath = folderPath + "/" + dynamicFileName;
        System.out.println("Dynamic File Found: " + filePath);
    }
    
    public void runShellScriptWithDateRange(String scriptPath, String startDate, String endDate) throws JSchException, IOException {
        // Form the command with date range
        String command = "cd " + scriptPath + " && ./script.sh \"" + startDate + "\" \"" + endDate + "\"";

        // Execute the command
        String output = executeCommand(command);
        System.out.println("Shell Script Output:\n" + output);
    }
}

