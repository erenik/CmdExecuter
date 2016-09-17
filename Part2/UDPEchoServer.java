package Part2;


import java.net.*;// need this for InetAddress, Socket, ServerSocket 
import java.io.*;// need this for I/O stuff

public class UDPEchoServer { 
	static final int BUFSIZE=1024;
	
	static public void main(String args[]) throws SocketException 
	{ 
		if (args.length != 1) {
			throw new IllegalArgumentException("Must specify a port!"); 						
		}
		int port = Integer.parseInt(args[0]);
		DatagramSocket s = new DatagramSocket(port);
		DatagramPacket dp = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
		System.out.println("Hosted. entering main loop");
		try { 
			while (true) 
			{
				s.receive(dp);
				byte[] data = dp.getData();
				/// .setData appends to the buffer, so need to get only the exact bytes that we need.
				// Optionally make a new packet each time. 
				String received = new String(data, 0, dp.getLength()); // Change this row to only new String(data) and you'll see the queue effect take place.
				String toSend = "Emil&Valentin: "+received;
				// print out client's address, port and incoming (assumed) String contents. 
				System.out.print("Message from " + dp.getAddress().getHostAddress()+":"+dp.getPort()+" "+received+". ");
				System.out.println("Echoing with name prepended.");				
				/// Make a new datagram packet to reset the buffer offset (doesn't go away otherwise, working like a queue pushing more data to be sent).
				dp.setData(toSend.getBytes(), 0, toSend.getBytes().length);
				// Send it back 
				s.send(dp); 
		//		dp.setLength(BUFSIZE);// avoid shrinking the packet buffer <- generates IllegalArgumentException after setting data with .setData.
				
			} 
		} catch (IOException e) {
			System.out.println("Fatal I/O Error !"); 
			System.exit(0);
			
		} 

	}
}