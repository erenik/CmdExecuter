package Part3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
	

	public static void main(String[] args) {
		
		int port = 4032;
		boolean stop = false;
		if(args.length == 1) {
			port = Integer.valueOf(args[0]);
		}
		
		try {
			
			// Opening the main socket on the port specified
			ServerSocket mainSocket = new ServerSocket(port);
			
			while (!stop) {
				// we received a connection
				Socket clientSocket = mainSocket.accept();
				// we create a specific thread to handle the connection
				CmdExecThread clientThread = new CmdExecThread(clientSocket);
				clientThread.start();
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
