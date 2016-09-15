/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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
    
    CmdExecuter(String cmd)
    {
        initialCmd = cmd;
        cwd = workingDir();
        // Parse cmd.
//        System.out.println("cmd: "+cmd);
        if (cmd.contains("||"))
        {
            String[] pipeParts = cmd.split("||");
            System.out.println("pipeparts: "+pipeParts.length);
            this.cmd = pipeParts[0];
            System.out.println("cmd: "+this.cmd);
            if (pipeParts.length > 1)
                this.pipeCmd = pipeParts[1];
        }
        else
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
            "ls",
            "cd C:/ && ls",
            "ls -l",
            "cd / && ls",
            "pwd",
            "cd",
            "cd /home/ && pwd | ls –all ",
            "uptime", /// uptime and below have not been implemented yet.
            "cat file.txt",
            "who",
            "ps",
            "grep",
            "ifconfig"
        };
        for (int i = 0; i < unitTests.length; ++i)
        {
            CmdExecuter cmd = new CmdExecuter(unitTests[i]);
            cmd.run();
        }
        
        // Get input
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter cmd: ");
        // Process
        
        // Do output
        
    }
    /** Executes the command. The command has been parsed and split up appropriately when reaching here.
     *  Only the arguments relevant to the command should be provided along with the command itself.
    */
    public void execute() 
    {
        cmd = cmd.trim(); // Remove whitespaces before n after as needed.
        if (cmd.startsWith("ls")) // ls
        {
            Path dir = Paths.get(cwd);
            String spacing = "\t";
            if (cmd.contains("-l"))
                spacing = "\n";
//            System.out.println("Path: "+dir.toString());
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*")) 
            {
                for (Path file : stream) {
                    output += file.getFileName() + spacing;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return;
        }
        if (cmd.startsWith("pwd"))
        {
            System.out.println(cwd);
            return;
        }
        if (cmd.startsWith("cd"))
        {
            if (cmd.length() < 3){
                System.out.println("Forgot to provide directory to cd command?");
                return;
            }
            cwd = cmd.substring(3); // from "cd " and to the end of this cmd?
            return;
        }
        output = "Unsupported command: "+cmd;
//        System.out.println();
        return;
       
    }
    /// Runs the command natively in the OS. 
    void RunOSCmd()
    {
        try {
	    // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(this.cmd);
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s = null; 
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
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
    
    
    
}
