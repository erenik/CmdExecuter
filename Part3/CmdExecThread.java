package Part3;

import java.net.Socket;
import part1.CmdExecuter;

public class CmdExecThread extends Thread {
	
	private Socket s;
	
	public CmdExecThread(Socket clientSocket) {
		this.s = clientSocket;
	}

	public void run() {
		CmdExecuter cmd;
		
	}

}
