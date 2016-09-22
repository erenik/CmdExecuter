package Part4;

import java.io.*;
import java.net.*;

/// Represents a client. Takes a host and port, tries to connect and fetches input/output streams to easily communicate with it.
public class MessageClient extends Thread 
{

	Socket socket;
	OutputStreamWriter out;
	InputStreamReader in;

	public MessageClient(String host, int port)
			throws IOException {
		socket = new Socket(host, port);
		out = new OutputStreamWriter(socket.getOutputStream());
		in = new InputStreamReader(socket.getInputStream());
	}

	// Tries to send a message and get the reply.
	public Message call(Message message) {
		try {
			message.putMessage(out);
		} catch(Exception e) {
			System.err.println("MessageClient (call): " + e);
			return null;
		}
		try {
			Message m = Message.getMessage(in);
			m.unpack();
			return m;
		} catch(Exception e) {
			System.err.println("MessageClient (reply): " + e);
			return new Message();
		}
	}
	// Disconnects, first sends a message informing of the disconnect to the server.
	public void disconnect() {
		Message m = new Message();
		m.setType(0);
		m.setParam("$disconnect","$disconnect");
		call(m);
		try {
			socket.close();
		} catch(Exception e) {
			System.err.println("MessageClient (disconnect): "+e);
		}
	}	
}