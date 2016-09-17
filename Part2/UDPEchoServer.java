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
				/** Make a new packet each time. DatagramSocket.receive adds data like a queue into the packet,
					making it unmanagable for multiple packets unless you actually create a new one (adjust offset didn't seem to have any effect).
				
					Try commenting the row out and you'll see, after a few iterations it catenates the data weirdly.
				*/
				dp = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
				
				s.receive(dp);
				// print out client's address 
				// we add our name
				byte[] data = dp.getData();
			//	System.out.println(" length: "+dp.getLength());
				String received = new String(data, 0, dp.getLength());
				String toSend = "Emil&Valentin: "+received;
				System.out.print("Message from " + dp.getAddress().getHostAddress()+":"+dp.getPort()+" "+received+". ");
				System.out.println("Echoing with name prepended.");
				
				/// Make a new datagram packet to reset the buffer offset (doesn't go away otherwise, working like a queue pushing more data to be sent).
			//	System.out.println("offset: "+dp.getOffset());
				dp.setData(toSend.getBytes(), 0, toSend.getBytes().length);
			//	System.out.println("offset: "+dp.getOffset());
				// Send it right back 
				s.send(dp); 
//				dp.setLength(BUFSIZE);// avoid shrinking the packet buffer <- wat, really
				
			} 
		} catch (IOException e) {
			System.out.println("Fatal I/O Error !"); 
			System.exit(0);
			
		} 

	}
}