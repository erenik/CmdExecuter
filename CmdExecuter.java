/**
 * Command-executer class. Usage: CmdExecuter cmd = new CmdExecuter(osCmdLine); cmd.run();
 * Output is saved in cmd.output as a string.
 * Unit-tests in its own main-function.
 * @author Emil, Valentin
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdExecuter implements Runnable 
{
    String cmd; // Command to be executed.
    String output = ""; // Std output of the command.
    boolean failed = false; // For unsupported or failures from exceptions. To halt unit- or repeated tests as needed.
    
    public CmdExecuter(String cmd)
    {
        // Parse cmd.
        this.cmd = cmd;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        System.out.println("Running unit tests of CmdExecuter.");
        /// Unit test of commands right here, yo.
        String[] unitTests = {
            "ls -l",
            "cd .. && ls",
            "cat GUIcmd.java | grep public",
            "galapagos"
        };
        for (int i = 0; i < unitTests.length; ++i)
        {
            CmdExecuter cmd = new CmdExecuter(unitTests[i]);
            cmd.run();
            if (cmd.failed)
                break;
        }        
    }
    /** Executes the command. The command has been parsed and split up appropriately when reaching here.
     *  Only the arguments relevant to the command should be provided along with the command itself.
    */
    public void execute() 
    {
        cmd = cmd.trim(); // Remove whitespaces before n after as needed.
        /** Run the command in the OS. If you would wanna extend the program, you could
         * handle other expressions here as well. 
         * E.g. if (cmd.startsWith("ownCmd")) { doStuff; return; }
         */
        RunOSCmd(); 
        return;       
    }
    /// Runs the command natively in the OS. 
    void RunOSCmd()
    {
        try {
            // using the Runtime exec method:
        	String[] osCmd = new String[3];
        	osCmd[0] = "cmd ";
        	osCmd[1] = "/C ";
        	osCmd[2] = this.cmd;
        	String os = System.getProperty("os.name");
        	if (os.equals("Linux")) {
        		osCmd[0] = "bash";
            	osCmd[1] = "-c";
        	}
        	// Run the command.
            Process p = Runtime.getRuntime().exec(osCmd);            

            /// Fetch output and errors (input to java from the process).
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
        
            String s = ""; 
            while ((s = stdInput.readLine()) != null) 
            {
            	output += s +"\n";
            }
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                failed = true;
            }
            /// Exit on error.
            if (failed)
            	System.exit(1);
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }    
    }; 

    /** Runs the CmdExecuter in its own thread, using provided command.
    */
    @Override
    public void run() 
    {
        execute();
        /// Did it work out fine?
        if (output != null && output.length() > 0)
        {
            // Print to std out.
            System.out.println("$ "+cmd);
            System.out.println(output);
        }
    }
    
    public String getOutput() {
    	return output;
    }
   
}
