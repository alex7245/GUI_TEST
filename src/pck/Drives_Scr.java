package pck;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import java.awt.BorderLayout;


public class Drives_Scr {


	JButton btnDriveUpdate = new JButton("Update Drives");
	private JFrame frame;
	private String str_driveList = "C:\\xfer\\TEST.txt";
	
	private String userName = System.getProperty("user.name");
	private String userFile = "userDrives.txt";
	private String str_userList = "C:\\users\\"+userName+"\\"+userFile;
	
	private Integer numDrives = 10;
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JPanel panel_3 = new JPanel();
	
	private char startDrive = 'G';
	
	public Drives_Scr() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		System.out.println(str_userList);
		setActionListeners();
		populateDrives();
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 328, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JTextPane txtpnTest = new JTextPane();
		txtpnTest.setText("Drive Mappings");
		frame.getContentPane().add(txtpnTest, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		sl_panel.putConstraint(SpringLayout.NORTH, panel_2, 0, SpringLayout.NORTH, panel_1);
		sl_panel.putConstraint(SpringLayout.WEST, panel_2, 6, SpringLayout.EAST, panel_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_2, 430, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_2, 302, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, 26, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 1, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, 430, SpringLayout.NORTH, panel);
		
		
		panel.add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		
		panel.add(panel_2);
		panel.setLayout(sl_panel);
	
		for (int i = 0;i < numDrives;i++){
			JComboBox cmbBox = new JComboBox();
			Integer numLetter = (int)(startDrive);
			char newChar = (char) (numLetter+i);
			
			JLabel lblG = new JLabel(""+newChar);
			int pos = (5+(i*25));
			sl_panel_2.putConstraint(SpringLayout.NORTH, cmbBox, pos, SpringLayout.NORTH, panel_2);
			sl_panel_2.putConstraint(SpringLayout.WEST, cmbBox, 5, SpringLayout.WEST, panel_2);
			sl_panel_1.putConstraint(SpringLayout.NORTH, lblG, pos, SpringLayout.NORTH, panel_1);
			sl_panel_1.putConstraint(SpringLayout.WEST, lblG, 5, SpringLayout.WEST, panel_1);
			panel_1.add(lblG);
			panel_2.add(cmbBox);
		}

		panel_2.setLayout(sl_panel_2);		
		frame.getContentPane().add(panel_3, BorderLayout.SOUTH);
	
	}
	
	private void populateDrives(){
		ReadInFile driveList = new ReadInFile();
		ReadInFile userList = new ReadInFile();
		
		Hashtable rtnDriveList = new Hashtable(); 
		Hashtable rtnUserList = new Hashtable();
		Hashtable dispTable = new Hashtable();
		
		rtnDriveList = driveList.readFile(str_driveList);
		rtnUserList = driveList.readFile(str_userList);
		
		if (rtnUserList == null) {
			dispTable = rtnDriveList;
		} else {
			dispTable = rtnUserList;
		}
	
		Set<String> keys = rtnDriveList.keySet();
		for(String key: keys){
	//		cmb_F.addItem(rtnDriveList.get(key));	
		}
		if (dispTable.get("f") !=null ){
	//		cmb_F.setSelectedItem(dispTable.get("f"));
		}
		

		
	}
	private void updateDrives(){
		
	}
	private void setActionListeners(){
		panel_3.add(btnDriveUpdate);
		btnDriveUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){	
				updateDrives();
			}
		});
	}
	
}
