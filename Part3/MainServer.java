package Part3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainServer 
{
	static void UnitTest()
	{
		CmdExecuter.DoUnitTests();
	}

	public static void main(String[] args) 
	{
		// Do unit-tests first as needed.
		UnitTest();
		
		int port = 4032;
		boolean stop = false;
		if(args.length == 1) {
			port = Integer.valueOf(args[0]);
		}
		
		/// Unit-tests?
		
		try {
			System.out.println("Opening server at port: "+port);
			// Opening the main socket on the port specified
			ServerSocket mainSocket = new ServerSocket(port);
			while (!stop) {
				// we received a connection
				Socket clientSocket = mainSocket.accept();
				System.out.println("New client.");
				// we create a specific thread to handle the connection
				CmdExecThread clientThread = new CmdExecThread(clientSocket);
				clientThread.run();		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Quitting");
	}
}
