package Lab4;

import java.net.Socket;

public class HeavyComputing implements Runnable  {
	
	private Socket client;
	
	public HeavyComputing(Socket nClient) {
		this.client = nClient;
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

	@Override
	public void run() {
		
		// check the number of prime numbers below 1000
		int nb = primeNumber(1000);
		
		
		
	}

}
