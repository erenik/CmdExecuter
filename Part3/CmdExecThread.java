package Part3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import part1.CmdExecuter;

public class CmdExecThread extends Thread {
	
	private Socket sock;
	private BufferedReader in;
	
	public CmdExecThread(Socket clientSocket) {
		this.sock = clientSocket;
	}

	public void run() 
	{		
		// Set blocking time-out. Ignore after more than 500ms. Reset the connection.
		try {
			sock.setSoTimeout(500);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Read the stuff.
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			while (!sock.isClosed())
	        {
				System.out.println("Loop");
	        	try {
	        		Read();	
	        	}
	        	catch (IOException e)
	        	{
	        		e.printStackTrace();
	        		sock.close();
	        		return;
	        	}
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/// Try to read, when there are new commands supplied (or the actual command finally arrives).
	void Read() throws IOException
	{
       	String line = in.readLine();
        System.out.println("recv: "+line);
				
    	String input = line;
		CmdExecuter cmd = new CmdExecuter(input);
		cmd.run();
		// Sleep until done.
		while(!cmd.hasFinished())
			;		
		String output = cmd.getOutput();	
		if (output.length() < 1)
			output = "No command sent.";
		/// Reply?
		OutputStream out = sock.getOutputStream();
        out.write(output.getBytes());
        System.out.println("Replied "+output.length()+" chars.");
        out.flush();
        sock.close();
	}
}
