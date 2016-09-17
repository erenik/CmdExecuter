package Part3;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import sun.misc.IOUtils;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;

public class GUIClient extends JFrame {

	private static JPanel contentPane;
	public static JTextField command;
	public static JTextArea result;
	public static JTextField host;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIClient frame = new GUIClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 588, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		command = new JTextField();
		// if we press enter in the command input, we execute
		command.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					GUIClient.ExecuteOnServer();
				}
			}
		});
		command.setBounds(136, 56, 370, 20);
		contentPane.add(command);
		command.setColumns(10);
		
		this.result = new JTextArea();
		JScrollPane scroll = new JScrollPane(result);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		scroll.setBounds(50, 87, 456, 217);
		contentPane.add(scroll);
		// If we press the "execute" button, we send the command to the server
		JButton execbtn = new JButton("Execute");
		execbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUIClient.ExecuteOnServer();
			}
		});
		execbtn.setBounds(417, 23, 89, 23);
		contentPane.add(execbtn);
		
		host = new JTextField();
		host.setBounds(136, 25, 271, 20);
		contentPane.add(host);
		host.setColumns(10);
		
		JLabel lblServer = new JLabel("Server (IP:port)");
		lblServer.setBounds(10, 27, 116, 14);
		contentPane.add(lblServer);
		
		JLabel lblCommand = new JLabel("Command");
		lblCommand.setBounds(10, 59, 116, 14);
		contentPane.add(lblCommand);
		
	}
	
	@SuppressWarnings("deprecation")
	public static void ExecuteOnServer() {
		
		// creating a buffer
		char[] buffer = new char[2048];
		
		// getting the desired command
		String command = GUIClient.command.getText();
		String hostport[] = GUIClient.host.getText().split(":");
		String host = hostport[0];
		int port;
		if (hostport.length > 1) {
			port = Integer.valueOf(hostport[1]);
		} else {
			port = 4032;
		}
		
		// creating streams
		OutputStream out;
		InputStreamReader in;
		
		
		try {
			
			// creating a socket
			Socket s = new Socket(host,port);
		
			// getting our streams
			out = s.getOutputStream();
			in = new InputStreamReader(s.getInputStream());
			
			// sending the command
			out.write(command.getBytes());
			out.flush();
			
			// receiving a reply
			in.read(buffer,0,buffer.length);
			
			s.close();
			//writing the reply in the GUI
			GUIClient.result.setText(buffer.toString());
			
			// closing session and stuff
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
