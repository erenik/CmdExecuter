package Part3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CmdExecThread extends Thread {
	
	private Socket sock;
	private BufferedReader in;
	Logger log;
	
	public CmdExecThread(Socket clientSocket) {
		this.sock = clientSocket;
		log = Logger.getLogger("log_file"); // Grab the logger.
	}

	public void run() 
	{		
		log.fine("CmdExecThread::run start"); // Debug when a function starts/stops
		// Set blocking time-out. Ignore after more than 500ms. Reset the connection.
		try {
			sock.setSoTimeout(500);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.fine(e1.toString()); // These exceptions are generally fine, a client closing or something, so just debug it.
		}
		// Read the stuff.
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			while (!sock.isClosed())
	        {
	        	try {
	        		Read();	
	        	}
	        	catch (IOException e)
	        	{
	        		log.warning("Socket failed to read or write. Debug further?");
	        		e.printStackTrace();
	        		sock.close();
	        		return;
	        	}
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.warning(e.toString()); // Problems with BufferedReader/InputStream, or closing the socket, sounds a bit bad, but maybe not app-killing. 
		}
		log.fine("CmdExecThread::run end"); // Debug when a function starts/stops
	}
	/// Try to read, when there are new commands supplied (or the actual command finally arrives).
	void Read() throws IOException
	{
		log.fine("CmdExecThread::Read start"); // Debug when a function starts/stops
		String line;
		try {
	       	line = in.readLine();
	        System.out.println("recv: "+line);
		} catch (Exception e)
		{
			log.fine("Exception while reading input (in.readLine): "+e.toString()); // May be relevant, BufferedReader failing
			throw e;
		}
    	String input = line;
		CmdExecuter cmd = new CmdExecuter(input);
		cmd.run();
		// Sleep until done.
		while(!cmd.hasFinished())
			;		
		try {
			String output = cmd.getOutput();	
			if (output.length() < 1)
				output = "No command sent.";
			/// Reply?
			OutputStream out = sock.getOutputStream();
	        out.write(output.getBytes());
	        System.out.println("Replied "+output.length()+" chars.");
	        out.flush();
		} catch (Exception e)
		{
			log.warning("Error getting output from the command and sending it back to the client: "+e.toString()); // May be relevant, command output failing or socket sending failing.
			throw e;
		}
        sock.close();
		log.fine("CmdExecThread::Read end"); // Debug when a function starts/stops
	}
}
