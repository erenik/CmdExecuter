package Lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import Part3.CmdExecuter;

public class HeavyComputing implements Runnable  {
	
	private Socket client;
	private BufferedReader in;
	private Logger log;
	
	public HeavyComputing(Socket nClient) {
		this.client = nClient;
		log = Logger.getLogger("log_file"); // Grab the logger.
	}
	
	public int primeNumber(int n) {

	    // initially assume all integers are prime
	    boolean[] isPrime = new boolean[n+1];
	    for (int i = 2; i <= n; i++) {
	        isPrime[i] = true;
	    }
	
	    // mark non-primes <= n using Sieve of Eratosthenes
	    for (int factor = 2; factor*factor <= n; factor++) {
	
	        // if factor is prime, then mark multiples of factor as nonprime
	        // suffices to consider mutiples factor, factor+1, ...,  n/factor
	        if (isPrime[factor]) {
	            for (int j = factor; factor*j <= n; j++) {
	                isPrime[factor*j] = false;
	            }
	        }
	    }
	
	    // count primes
	    int primes = 0;
	    for (int i = 2; i <= n; i++) {
	        if (isPrime[i]) primes++;
	    }
	    System.out.println("The number of primes <= " + n + " is " + primes);
	    
	    return primes;
	}
	
	public int fibonacci(int n) {
		if(n == 0)
	        return 0;
	    else if(n == 1)
	      return 1;
	   else
	      return fibonacci(n - 1) + fibonacci(n - 2);
	}
	
	void Read() throws IOException
	{
		log.fine("HeavyComputation::Read start"); // Debug when a function starts/stops
		String line;
		try {
	       	line = in.readLine();
	        System.out.println("recv: "+line);
		} catch (Exception e)
		{
			log.fine("Exception while reading input (in.readLine): "+e.toString()); // May be relevant, BufferedReader failing
			throw e;
		}
    	String input = line;

    	
    	int nb = fibonacci(Integer.valueOf(input));
    	
		try {
			
			OutputStream out = client.getOutputStream();
	        out.write(String.valueOf(nb).getBytes());
	        System.out.println("Replied.");
	        out.flush();
		} catch (Exception e)
		{
			log.warning("Error sending it back to the client: "+e.toString()); // May be relevant, command output failing or socket sending failing.
			throw e;
		}
        client.close();
		log.fine("HC::Read end"); // Debug when a function starts/stops
	}

	@Override
	public void run() {
		
		log.fine("CmdExecThread::run start"); // Debug when a function starts/stops
		// Set blocking time-out. Ignore after more than 500ms. Reset the connection.
		try {
			//client.setSoTimeout(50000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.fine(e1.toString()); // These exceptions are generally fine, a client closing or something, so just debug it.
		}
		// Read the stuff.
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while (!client.isClosed())
	        {
	        	try {
	        		Read();	
	        	}
	        	catch (IOException e)
	        	{
	        		log.warning("Socket failed to read or write. Debug further?");
	        		e.printStackTrace();
	        		client.close();
	        		return;
	        	}
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.warning(e.toString()); // Problems with BufferedReader/InputStream, or closing the socket, sounds a bit bad, but maybe not app-killing. 
		}
		log.fine("CmdExecThread::run end"); // Debug when a function starts/stops	


	}


}
