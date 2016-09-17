package Part3;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;

public class GUIClient extends JFrame {

	private static JPanel contentPane;
	public static JTextField command;
	public static JTextArea result;

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
		command.setBounds(33, 39, 377, 20);
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
		execbtn.setBounds(462, 38, 89, 23);
		contentPane.add(execbtn);
		
	}
	
	public static void ExecuteOnServer() {
		
		// getting the desired command
		String command = GUIClient.command.getText();
		// getting the address
		
		// creating an INetAddress
		
		// connecting to the server
		
		// sending data
		
		// receiving a reply
		
		//writing the reply in the GUI
		GUIClient.result.setText("someresult");
		
		// closing session and stuff
	}
	

}
