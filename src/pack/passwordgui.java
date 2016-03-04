package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.Panel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class passwordgui extends JFrame {

	private JPanel contentPane;
	private JLabel label;
	private JLabel label_1;
	private JTextField textField;
	private JButton button;
	private JLabel label_2;
	private JLabel label_3;
	private JButton button_1;
	private Panel panel_1;
	private JLabel label_4;
	private JPasswordField passwordField;
	private JLabel label_5;
	private JButton button_2;
	private JButton button_3;
	private Panel panel;
	Connection c;
	Statement stmt;
	String nm;
	static passwordgui frame;

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
					frame = new passwordgui("kaushal");
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
	public passwordgui(String name) {
		frame=this;
		nm=name;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel_1 = new Panel();
		panel_1.setBounds(0, 0, 434, 251);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		
		label_4 = new JLabel("    Enter Password");
		label_4.setBounds(45, 55, 105, 14);
		panel_1.add(label_4);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(160, 44, 146, 28);
		panel_1.add(passwordField);
		
		label_5 = new JLabel("Forgot Password ?");
		label_5.setForeground(Color.BLACK);
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try{
					Class.forName("org.sqlite.JDBC");
					c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					stmt=c.createStatement();
					String sql="select SECANS,SECQ from USER where UNAME='"+nm+"';";
					ResultSet rs=stmt.executeQuery(sql);
					label_1.setText(rs.getString(2));
					rs.close();
					stmt.close();
					c.close();
				} catch(Exception e1){}
				panel.setVisible(true);
				panel_1.setVisible(false);
				//panel.requestFocus(true);
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				label_5.setForeground(Color.blue);
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				label_5.setForeground(Color.black);
			}
		});
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(160, 95, 159, 14);
		panel_1.add(label_5);
		
		button_2 = new JButton("Proceed");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					stmt=c.createStatement();
					String sql="select count(*),PASSWORD,EMAIL,ENOT from USER where UNAME='"+nm+"';";
					ResultSet rst=stmt.executeQuery(sql);
					if(rst.getLong(1)==0){
						System.out.println("Error!");
					}
					else if(passwordField.getText().equals(rst.getString(2))){
						System.out.println("Success!");
						String mail="";
						mail=rst.getString(3);
						try{if(rst.getString(4).equalsIgnoreCase("YES"))
							MailTo.sendMail(mail, "VLock Account Accessed", "Your Vault has been opened!");}
						catch(Exception e){}
						finally{
						rst.close();
						stmt.close();
						c.close();
						System.out.println("Con close");
						frame.dispose();
						useroper usr=new useroper(nm);
						usr.setVisible(true);}
					}
					else
						System.out.println("Invalid Password");
					rst.close();
					stmt.close();
					c.close();
				} catch(Exception e){}
				if(textField.getText().equals("")){}
			}
		});
		button_2.setBounds(196, 138, 89, 23);
		panel_1.add(button_2);
		
		button_3 = new JButton("Cancel");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				usernotfound usr=new usernotfound();
				usr.setVisible(true);
			}
		});
		button_3.setBounds(196, 190, 89, 23);
		panel_1.add(button_3);
		
		panel = new Panel();
		panel.setVisible(false);
		panel.setBounds(0, 0, 414, 251);
		contentPane.add(panel);
		panel.setLayout(null);
		
		label = new JLabel("    Password Recovery");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		label.setBounds(110, 11, 208, 28);
		panel.add(label);
		
		label_1 = new JLabel("Security Question");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(23, 50, 385, 14);
		panel.add(label_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(120, 75, 198, 28);
		panel.add(textField);
		
		button = new JButton("Proceed");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					stmt=c.createStatement();
					String pass,mail;
					String sql="select count(*),SECANS,EMAIL,PASSWORD from USER where UNAME='"+nm+"';";
					ResultSet rst=stmt.executeQuery(sql);
					if(rst.getLong(1)==0){
						System.out.println("Error!");
					}
					else if(textField.getText().equals(rst.getString(2))){
						System.out.println("Success!");
						mail=rst.getString(3);
						pass=rst.getString(4);
						MailTo.sendMail(mail,"VLock Password Recovery",pass);
						label_3.setVisible(true);
					}
					else{
						System.out.println("wrong answer");
						label_2.setVisible(true);
					}
					rst.close();
					stmt.close();
					c.close();
					System.out.println("Con close");
				} catch(Exception e){}
				if(textField.getText().equals("")){}
			}
		});
		button.setBackground(UIManager.getColor("ArrowButton.background"));
		button.setBounds(170, 106, 89, 23);
		panel.add(button);
		
		label_2 = new JLabel("Wrong Answer");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(120, 140, 198, 14);
		label_2.setVisible(false);
		panel.add(label_2);
		
		label_3 = new JLabel("Password sent to register Email ID");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(120, 158, 198, 14);
		label_3.setVisible(false);
		panel.add(label_3);
		
		button_1 = new JButton("Back");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				panel.setVisible(false);
				panel_1.setVisible(true);
			}
		});
		button_1.setBackground(UIManager.getColor("ArrowButton.background"));
		button_1.setBounds(170, 183, 89, 23);
		panel.add(button_1);
	}
}
