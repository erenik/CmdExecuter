package Part4;

import java.util.*;

/// A message server returning the current date?
public class DateService implements Deliverable 
{
	public static final int DATE_SERVICE_MESSAGE = 100;
	public static final int DATE_SERVICE_PORT = 1999;

	/// When a client requests a DATE_SERVICE_MESSAGE, give them the date as response.
	public Message send(Message m) {
		Date today = new Date();
		m.setParam("date", today.toString());
		return m;
	}
	/// Launches a MessagerServer only supporting the DateService on port 1999
	public static void main(String args[]) {
		DateService ds = new DateService();
		MessageServer ms;
		try {
			ms = new MessageServer(DATE_SERVICE_PORT);
		} catch(Exception e) {
			System.err.println("Could not start service " + e);
			return;
		}
		Thread msThread = new Thread(ms);
		// Add subscription to the message server, messages of type 100 (DATE_SERVICE_MESSAGE) should get responses from here (DateService).
		ms.subscribe(DATE_SERVICE_MESSAGE, ds); 
		msThread.start();
	}
}