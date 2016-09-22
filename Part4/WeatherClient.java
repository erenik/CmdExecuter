package Part4;

import java.util.ArrayList;
import java.util.List;

public class WeatherClient {

	/// Client, to test the DateService/MessageServer, uses the MessageClient to communicate easily.
	public static void main(String[] args) 
	{
		if (args.length < 2) {
			System.out.println("Usage: DateClient host port");
		}
		String host = args[0];
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch(Exception e) {
			port = WeatherService.PORT;
		}
		MessageClient conn;
		try {
			/// Connect and set up eased communication functions via MessageClient
			conn = new MessageClient(host,port);
		} catch(Exception e) {
			System.err.println(e);
			return;
		}
		Message m = new Message();
		
		// Test the locations.
		List<String> locations = new ArrayList<String>();
		locations.add("Luleå");
		locations.add("Skellefteå");
		locations.add("Nancy");
		m.setType(WeatherService.TEMP_MESSAGE);
		for (int i = 0; i < locations.size(); ++i)
		{
			String loc = locations.get(i);
			m.setParam("location", loc);
			m = conn.call(m);
			System.out.println("Temp in "+loc+": " + m.getParam("temp"));
		}
		// Send a bad message (unsupported)
		m.setType(75);
		m = conn.call(m);
		System.out.println("Bad reply " + m);
		conn.disconnect();
	}
}