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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            /// below have not been implemented yet.
            "cat src/README.md",
            "uptime", 
            "who",
            "ps",
            "grep",
            "ifconfig"
        };
        for (int i = 0; i < unitTests.length; ++i)
        {
            CmdExecuter cmd = new CmdExecuter(unitTests[i]);
            cmd.run();
            if (cmd.failed)
                break;
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
        if (cmd.startsWith("cat"))
        {
            // Read file.
            try{
                String file = cmd.substring(4);
                String path = cwd+"/"+file;
                System.out.println("File: "+path);
                BufferedReader br = new BufferedReader(new FileReader(path));
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    String everything = sb.toString();
                    output = everything;
                } 
                finally {
                    br.close();
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                e.printStackTrace();
                failed = true;
            } 
            return;
        }
        /// Not working yet.
        if (cmd.startsWith("uptime"))
        {
            uptime();
            return;
        }
        output = "Unsupported command: "+cmd;
        failed = true;
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
    
    
    /// Detailed/longer functions to implement some commands.
    void uptime()
    {
        try 
        {
            long uptime = -1;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Process uptimeProc = Runtime.getRuntime().exec("net stats srv");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("Statistics since")) 
                    {
                        SimpleDateFormat format = new SimpleDateFormat("'Statistics since' yyyy-MM-dd hh:mm:ss");
                        Date boottime = format.parse(line);
                        uptime = System.currentTimeMillis() - boottime.getTime();
                        break;
                    }
                }
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                Process uptimeProc = Runtime.getRuntime().exec("uptime");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                String line = in.readLine();
                if (line != null) {
                    Pattern parse = Pattern.compile("((\\d+) days,)? (\\d+):(\\d+)");
                    Matcher matcher = parse.matcher(line);
                    if (matcher.find()) {
                        String _days = matcher.group(2);
                        String _hours = matcher.group(3);
                        String _minutes = matcher.group(4);
                        int days = _days != null ? Integer.parseInt(_days) : 0;
                        int hours = _hours != null ? Integer.parseInt(_hours) : 0;
                        int minutes = _minutes != null ? Integer.parseInt(_minutes) : 0;
                        uptime = (minutes * 60000) + (hours * 60000 * 60) + (days * 6000 * 60 * 24);
                    }
                }
            }
            output = "Uptime: "+uptime;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    
    
    
    
}
