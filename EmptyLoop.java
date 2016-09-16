
public class EmptyLoop extends Thread {
	

	public static void main(String[] args) {
		// we check if we receive an argument
		 if(args.length != 1) {
				 System.out.println("No arguments were given, exiting.");
				 System.exit(0);
		 }
		 
		 int iterations = 0;
		 // we want to avoid format exception
		 try {
			 iterations = Integer.parseInt(args[0]);
		 } catch (Exception e) {
			 System.out.println("Wrong argument!");
			 System.out.println(e.getMessage());
			 System.exit(0);
		 }
		 
		// we print the number of execution we are going to do
		System.out.println("Number of iterations: "+iterations);
		
		for (int i=0;i<iterations;++i) {
			// doing nothing
		}
		
		System.out.println(iterations+" iterations done!");
		
		

	}

}
