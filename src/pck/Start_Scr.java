package pck;
import static pck.SystemUtil.ERR;
import static pck.SystemUtil.STD;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComboBox;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.CardLayout;

public class Start_Scr implements ActionListener{
	
	private JFrame frame;
	private JButton btnDrives;
	private JButton btnClient;
	private JButton btnVpns;
	private JButton btnSettings;
	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Start_Scr window = new Start_Scr();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
//	public static void main(String[] args) throws IOException, InterruptedException {
//		//String[] ret = SystemUtil.exec("cmd /c ipconfig|find/i USU && rasdial USU /disconnect || rasdial USU aptuser1 Password1");
//		String[] ret = SystemUtil.exec(new String[] {"cmd", "/c", "ipconfig|find/i", "\"USU\"", "&&", "rasdial", "USU", "/disconnect", "||", "rasdial", "USU", "aptuser1", "Password1"});
//		if (ret[ERR].isEmpty()) {
//			System.out.println(ret[STD]);
//		} else {
//			System.out.println("ERROR");
//			System.out.println(ret[ERR]);
//		}
//	}

//	public static void main(String[] args) {
//		System.out.println(System.getProperty("user.home"));
//	}
	
	/**
	 * Create the application.
	 */
	public Start_Scr() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 333, 79);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		FlowLayout panelLayout = new FlowLayout();
		panel.setLayout(panelLayout);
		
		//Setup buttons
		btnDrives = new JButton();
		btnClient = new JButton();
		btnVpns = new JButton();
		btnSettings = new JButton();	
		
		//Set button Text
		btnDrives.setText("Drives");
		btnClient.setText("Client");
		btnVpns.setText("VPNS");
		btnSettings.setText("Settings");
	
		setActionListeners();
		
		panel.add(btnDrives);
		panel.add(btnClient);
		panel.add(btnVpns);
		panel.add(btnSettings);
		
	}
	
	private void driveButton(){
		Drives_Scr drivesScr = new Drives_Scr();
				
	}
	private void clientButton(){
		
		
	}
	private void vpnButton(){
		
	}
	private void settingButton(){
		
	}
	
	private ActionListener driveActionListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){	
			driveButton();
		}
	};
	
	private void setActionListeners(){
		btnDrives.addActionListener(driveActionListener);
		btnClient.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				 clientButton();
			}
		});
		btnVpns.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				vpnButton();
			}
		}); 
		btnSettings.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				settingButton();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
