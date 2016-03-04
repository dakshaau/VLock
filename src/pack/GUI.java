package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	Connection c=null;
	private final JLabel label = new JLabel("Username not found! Please request access to vault");
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try{
						for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
							if(info.getName().equals("Nimbus")){
								UIManager.setLookAndFeel(info.getClassName());
								break;
							}
						}
					} catch (Exception e){
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					}
					GUI frame = new GUI();
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
	public GUI() {
		boolean nu=true;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
			System.out.println("con suc");
			Statement s=c.createStatement();
			ResultSet rs=s.executeQuery("SELECT UNAME FROM USER");
			while(rs.next()){
				if(!rs.getString("UNAME").equals("MASTER")){
					nu=false;
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setVisible(false);
		if(nu)
			panel.setVisible(true);
			
		panel.setBounds(6, 6, 422, 249);
		contentPane.add(panel);
		panel.setLayout(null);
		
		
		JLabel lblWelcomeFirstUser = new JLabel("Welcome First User!");
		lblWelcomeFirstUser.setHorizontalTextPosition(SwingConstants.CENTER);
		lblWelcomeFirstUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeFirstUser.setBounds(146, 39, 154, 16);
		panel.add(lblWelcomeFirstUser);
		
		textField_1 = new JTextField();
		textField_1.setBounds(164, 79, 122, 28);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Proceed");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(174, 119, 90, 28);
		panel.add(btnNewButton);
		
		JLabel lblUserName = new JLabel("USER NAME");
		lblUserName.setBounds(110, 139, 76, 16);
		contentPane.add(lblUserName);
		
		textField = new JTextField();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				label.setVisible(false);
			}
		});
		textField.setBounds(198, 133, 122, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnProceed = new JButton("PROCEED");
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sr=textField.getText();
				boolean found=false;
				try{
					Statement stmt=c.createStatement();
					ResultSet rst=stmt.executeQuery("SELECT UNAME FROM USER");
					while(rst.next()){
						if(sr.equals(rst.getString("UNAME"))){
							found=true;
							break;
						}
					}
					if(!found){
						label.setVisible(true);
					}
					if(found)
						System.out.println(sr+ " found in databse!");
				}catch(Exception e){}
			}});
		btnProceed.setBounds(208, 173, 90, 28);
		contentPane.add(btnProceed);
		label.setVisible(false);
		label.setBounds(84, 51, 344, 31);
		contentPane.add(label);
	}
}
