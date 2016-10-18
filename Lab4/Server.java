package Lab4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

public class Server 
{


	public static void main(String[] args) 
	{

		
		int port = 4032;
		boolean stop = false;
		String logLevel;
		
		if(args.length < 1) {
			logLevel = "debug";
		} else {
			logLevel = args[0];
		}
		if (args.length > 1)
		{
			port = Integer.valueOf(args[1]);
		}
		
		
		System.out.println("logLevel: "+logLevel);
		Logger log;
		try {
			FileHandler fh = new FileHandler("serv.log");
			log = Logger.getLogger("log_file");
			log.addHandler(fh);			
		} catch (SecurityException | IOException e1) {
			e1.printStackTrace();
			return;
		}
		Level level;
		if (logLevel.contains("warn"))
			level = Level.WARNING;
		else if (logLevel.contains("info"))
			level = Level.INFO;
		else if (logLevel .contains("error"))
			level = Level.SEVERE;
		else if (logLevel .contains("debug"))
			level = Level.FINE;
		else
		{
			System.out.println("Provide a logging level: error/warn/info/debug");
			System.exit(1);
			return;
		}
		log.setLevel(level);
		
		
		/// Unit-tests?
		ServerSocket mainSocket;
		try {
			log.info("Opening server at port: "+port); // General info,
			System.out.println("Opening server at port: "+port);
			// Opening the main socket on the port specified
			mainSocket = new ServerSocket(port);
			while (!stop) {
				// we received a connection
				Socket clientSocket = mainSocket.accept();
				System.out.println("New client.");
				// we create a specific thread to handle the connection
				HeavyComputing clientThread = new HeavyComputing(clientSocket);
				clientThread.run();		
			}
			mainSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.severe(e.toString());  // Exceptions causing crashes are severe.
		}
		
		log.info("Quitting"); // To inform that the exception or action actually triggered quitting.
		System.out.println("Quitting");
	}
}
