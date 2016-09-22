package Part4;

public class DateClient {

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
			port = DateService.DATE_SERVICE_PORT;
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
		// Send a good message.
		m.setType(DateService.DATE_SERVICE_MESSAGE);
		m.setParam("person","george");
		m = conn.call(m);
		System.out.println("Date " + m.getParam("date"));
		// Send a bad message (unsupported)
		m.setType(75);
		m = conn.call(m);
		System.out.println("Bad reply " + m);
		conn.disconnect();
	}
}