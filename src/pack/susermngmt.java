package pack;

import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 
public class susermngmt extends JFrame {
	
	Connection c=null;
	Statement stmt = null;
	long count=0;
	String username;
	int mode=0;
	static susermngmt frame;

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new susermngmt();
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
	public susermngmt() {
		frame=this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCreateUser = new JLabel("Create User");
		lblCreateUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateUser.setBounds(102, 11, 203, 14);
		contentPane.add(lblCreateUser);
		
		JLabel label_1 = new JLabel("Choose a User  Name To Continue");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(72, 36, 267, 14);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("UserName");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(72, 83, 69, 14);
		contentPane.add(label_2);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(151, 78, 172, 23);
		contentPane.add(textField);
		
		JButton button = new JButton("Proceed");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username=	textField.getText();
				String cnt;
				try
				{
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
				System.out.println("New user entry DB opened!");
				stmt=c.createStatement();
				cnt="SELECT count(USERID) FROM USER;";
				ResultSet rst=stmt.executeQuery(cnt);
				count=rst.getLong(1);
				String sql="insert into USER values("+(count+1)+",'"+username+"','NA','NA','NA','NA','NA');";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
				System.out.println("Username created!");
				frame.dispose();
				registration rg=new registration(count+1,2);
				rg.setVisible(true);
				}
				catch(Exception e1){
				}
			}
		});
		button.setBounds(186, 124, 89, 23);
		contentPane.add(button);
	}
}
	
		
