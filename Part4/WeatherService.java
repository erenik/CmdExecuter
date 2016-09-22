package Part4;

import java.util.*;

/// A new service!
public class WeatherService implements Deliverable 
{
	public static final int TEMP_MESSAGE = 101;
	public static final int HUMIDITY_MESSAGE = 102;
	public static final int PORT = 2001;

	/// When a client requests a DATE_SERVICE_MESSAGE, give them the date as response.
	public Message send(Message m) 
	{
		if (m.getType() == TEMP_MESSAGE)
		{
			int temp = -30;
			String loc = m.getParam("location");
			if (loc.equals("Luleå"))
				temp = -25;
			else if (loc.contains("Skellefteå"))
				temp = -20;
			else if (loc.contains("Nancy"))
				temp = 20;
			m.setParam("temp", ""+temp);			
		}
		if (m.getType() == HUMIDITY_MESSAGE)
		{
			m.setParam("humidity", 80+"");
		}
		return m;
	}
	/// Launches a MessagerServer only supporting the DateService on port 1999
	public static void main(String args[]) {
		WeatherService weatherService = new WeatherService();
		MessageServer ms;
		try {
			ms = new MessageServer(PORT);
		} catch(Exception e) {
			System.err.println("Could not start service " + e);
			return;
		}
		Thread msThread = new Thread(ms);
		// Add subscription to the message server, messages of type 100 (DATE_SERVICE_MESSAGE) should get responses from here (DateService).
		ms.subscribe(TEMP_MESSAGE, weatherService); 
		ms.subscribe(HUMIDITY_MESSAGE, weatherService); 
		msThread.start();
	}
}

