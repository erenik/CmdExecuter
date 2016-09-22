package Part4;

import java.net.*;
import java.io.*;
import java.util.*;

// A message server..
public class MessageServer extends Thread {
	private ServerSocket callListener;
	private Hashtable subscribers; // Message service subscription
	public static final boolean logging = true;
	
	public void log(String s) {
		if (!logging) return;
		System.err.println("MessageServer: " + s);
	}

	public MessageServer(int port) throws IOException {
		log("Simple Messaging Architecture (SMA) version 1.0");

		callListener = new ServerSocket(port);
		subscribers = new Hashtable();
		log("Created MessageServer instance fully!");
	}

	public void subscribe(int messageType, Deliverable d) 
	{
		subscribers.put(messageType + "", d);
	}

	public Deliverable getSubscriber(int messageType) {
		return (Deliverable) subscribers.get(messageType + "");
	}

	// Thread start!
	public void run() {
		log("MessageServer thread started. run() dispatched.");
		while (true) {
			try {
				Socket s=callListener.accept();
				MessageServerDispatcher csd;
				csd = new MessageServerDispatcher(this, s); // For each client, add a MessageServerDispatcher to reply to it?
				csd.setDaemon(false);
				csd.start();
			} catch(Exception e) {
				log("Exception " + e);
				e.printStackTrace();
			}
		}
	}
}