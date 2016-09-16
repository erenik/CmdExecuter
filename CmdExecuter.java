/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
*/
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Emil
 */
public class CmdExecuter implements Runnable 
{
    String initialCmd; // Full cmd at initial loading.
    String cmd; // Command to be executed.
    String pipeCmd; // Command to be piped next.
    String input; // Input from previous command? if piping to file for example.
    String output; // Std output of the command.
    String cwd; // Current working directory.
    boolean failed = false; // For unsupported or failures from exceptions. To halt unit- or repeated tests as needed.
    
    public CmdExecuter(String cmd)
    {
        initialCmd = cmd;
        cwd = workingDir();
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
            "ls"
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
        	System.out.println("os: "+os);
        	if (os.equals("Linux")) {
        		osCmd[0] = "bash ";
            	osCmd[1] = "-c ";
        	}
        		//osCmd = "bash -c "+this.cmd;
            Process p = Runtime.getRuntime().exec(osCmd);
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));
            
            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            
            // read the output from the command
//            System.out.println("Here is the standard output of the command:\n");
            String s = null; 
            while ((s = stdInput.readLine()) != null) 
            {
                output += s +"\n";
                System.out.println(s);
            }

            // read any errors from the attempted command
      //      System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                failed = true;
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }    
    };
    /// Like pwd in Linux, fetches current directory.
    static String workingDir()
    {
        return System.getProperty("user.dir");
    }
    
    /** Runs the CmdExecuter in its own thread, using provided command, next pipe and arguments as parsed and provided earlier in the constructor.
        Schedules the next command to be executed in an own thread afterwards if piping.
    */
    @Override
    public void run() 
    {
        if (cmd.contains("&&"))
        {
            String[] newCmds = cmd.split("&&");
            for (int i = 0; i < newCmds.length; ++i)
            {
                cmd = newCmds[i];
                execute();
            }
        }        
        else
            execute();
        /// Did it work out fine?
        // Any && ?
        // Pipe it?
        if (pipeCmd != null)
        {
            CmdExecuter piped = new CmdExecuter(pipeCmd);
            piped.initialCmd = initialCmd;
            piped.input = output;
            piped.run();
        }
        else if (output != null && output.length() > 0)
        {
            // Print to std out.
            System.out.println("$ "+initialCmd);
            System.out.println(output);
        }
    }
    
    public String getOutput() {
    	return output;
    }
   
}
