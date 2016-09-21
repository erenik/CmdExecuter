package Part3;
/**
 * Command-executer class. Usage: CmdExecuter cmd = new CmdExecuter(osCmdLine); cmd.run();
 * Output is saved in cmd.output as a string.
 * Unit-tests in its own main-function.
 * @author Emil, Valentin
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CmdExecuter implements Runnable 
{
    String cmd; // Command to be executed.
    String output = ""; // Std output of the command.
    Logger log;
    boolean failed = false; // For unsupported or failures from exceptions. To halt unit- or repeated tests as needed.
    boolean finished = false;
    boolean printOutput = true; // Default, false for unitTests without errors.
    
    public CmdExecuter(String cmd)
    {
        // Parse cmd.
        this.cmd = cmd;
		log = Logger.getLogger("log_file"); // Grab the logger.
    }
    static void DoUnitTests()
    {
        System.out.print("Running unit tests of CmdExecuter.. ");
        /// Unit test of commands right here, yo.
        List<String> tests = new ArrayList<String>();
		String os = System.getProperty("os.name");
    	if (os.equals("Linux")) {
    		tests.add("ls -l");
    		tests.add("cd .. && ls");
    		tests.add("cat GUIcmd.java | grep public");
    		//tests.add("ls");
    	}
    	else  // Presumes Win32
    	{
    		tests.add("dir");
    		tests.add("ipconfig");
    		tests.add("dir -a");    		
    		tests.add("help dir");
    	}
        for (int i = 0; i < tests.size(); ++i)
        {
            CmdExecuter cmd = new CmdExecuter(tests.get(i));
            cmd.printOutput = false;
            cmd.run();
            if (cmd.failed)
            {
            	cmd.printOutput = true;
            	System.out.println("Unit test failed.");
                System.exit(1);;
            }
        }        
        System.out.println("All working");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	DoUnitTests();
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
        finished = true;
        return;       
    }
    /// Runs the command natively in the OS. 
    void RunOSCmd()
    {
        try {
        	log.fine("CmdExecuter::RunOSCmd start");
            // using the Runtime exec method:
        	String[] osCmd = new String[3];
        	osCmd[0] = "cmd ";
        	osCmd[1] = "/C ";        	
        	osCmd[2] = this.cmd;
        	String os = System.getProperty("os.name");
//        	System.out.println("os: "+os);
        	/// Linux config of args.
        	if (os.equals("Linux")) {
        		osCmd[0] = "bash";
            	osCmd[1] = "-c";
        	}
        	/// Windows config of args.
        	if (os.contains("Windows"))
        	{
        		String[] split = this.cmd.split(" ");
        		osCmd = new String[2 + split.length];
        		osCmd[0] = "cmd";
        		osCmd[1] = "/C";
        		for (int i = 0; i < split.length; ++i)
        		{
        			osCmd[2+i] = split[i];
        		}
        	}
        	// Run the command.
            Process p = Runtime.getRuntime().exec(osCmd);            

            /// Fetch output and errors (input to java from the process).
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            
            log.fine("Process started, waiting for output now"); // debug debug
            String s = ""; 
            while ((s = stdInput.readLine()) != null) 
            {
            	output += s +"\n";
            }
            while ((s = stdError.readLine()) != null) {
            	output += s +"\n";
            }
            /// Exit on error.
            if (failed)
            {
            	output += "Unknown command. Failed to launch process.";
            	log.warning("Unknown command. Failed to launch process: "+this.cmd); // Unknown command, warn the server admins!
            	failed = true;
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
            log.severe(e.toString()); // Severe on exceptions!
        }    
    	log.fine("CmdExecuter::RunOSCmd end");
    }; 

    /** Runs the CmdExecuter in its own thread, using provided command.
    */
    @Override
    public void run() 
    {
        execute();
        /// Did it work out fine?
        if (output != null && output.length() > 0 && printOutput)
        {
            // Print to std out.
        	log.fine("CmdExecuter::run result: \n$ "+cmd+"\n"+output);
            System.out.println("$ "+cmd);
            System.out.println(output);
        }
    }
    
    public String getOutput() {
    	return output;
    }
    public boolean hasFinished(){ return finished; }
}
