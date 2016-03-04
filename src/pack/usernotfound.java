package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.*;
import java.nio.*;
import java.nio.file.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class usernotfound extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	Connection c = null;
	Statement stmt = null;
	Panel panel_1;
	Panel panel; 
	JLabel lblUserNotFound;
	registration reg;
	static usernotfound frame;
	authentication auth;
	masteruser mst;
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				boolean userExists=false;
				final File inst=new File(".");
				if(inst.isDirectory()){
					for(File ins : inst.listFiles()){
						if(ins.isFile()){
							if(ins.getName().lastIndexOf("instance")!=-1){
								JOptionPane.showMessageDialog(null,"There is another instance of this application running.","Error!",JOptionPane.ERROR_MESSAGE);
								System.exit(0);
							}
						}
					}
				}
				try {
					final Path p= File.createTempFile("instance" , ".tmp", inst).toPath();
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
					try{
						File user=new File("./VLD.db.enc");
						if(user.exists()){
							if(FileCrypt.decryptFile(user.getAbsolutePath())){
								userExists=true;
						}	
						}
						else{
							Class.forName("org.sqlite.JDBC");
							Connection c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
							Statement stmt=c.createStatement();
							String sql="CREATE TABLE USER(USERID INT NOT NULL PRIMARY KEY" +
									",UNAME VARCHAR(20) NOT NULL UNIQUE," +
									"EMAIL VARCHAR(50) NOT NULL,ENOT VARCHAR(3) NOT NULL," +
									" SECANS VARCHAR(20) NOT NULL," +
									"SECQ VARCHAR(100) NOT NULL,"+" PASSWORD VARCHAR(20) NOT NULL )";
							stmt.executeUpdate(sql);
							sql="CREATE TABLE AUDIOSAMPLE "+"(ID INT NOT NULL,"+
									"UID INT NOT NULL,"+
									"SAMPLEPATH VARCHAR(50) NOT NULL" +
									",FOREIGN KEY (UID) REFERENCES USER(USERID) )";

									stmt.executeUpdate(sql);
									
							
							 sql = "CREATE TABLE FILEPATH" +
									"(ID INT PRIMARY KEY NOT NULL," +
									" FILEPATH VARCHAR(50) NOT NULL, " +
									" UID INT NOT NULL," +
									"FOREIGN KEY (UID) REFERENCES USER(USERID) )";
							stmt.executeUpdate(sql);
							sql="insert into USER " +
									"VALUES (1,'MASTER', 'NA','NA','NA','NA','flinstones');";
							stmt.executeUpdate(sql);
							stmt.close();
							c.close();
							System.out.println("DB Created");
							userExists=false;
						}
					} catch(Exception e){}
					frame = new usernotfound();
					frame.setVisible(true);
					if (p.toFile().exists()) {
			            Runtime.getRuntime().addShutdownHook(new Thread() {
			                public void run() {
			                    try {
			                    	File user=new File("./VLD.db");
									if(user.exists())
										if(FileCrypt.encryptFile(user.getAbsolutePath()))
											System.out.println("Encrypted!");
			                        Files.deleteIfExists(p);
			                    } catch (Exception e) {
			                       System.err.println("Instance not deleted!!");
			                    }
			                }
			            });
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public usernotfound() {
		frame=this;
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setVerifyInputWhenFocusTarget(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new Panel();
		panel.setVisible(false);
		panel.setBounds(0, 0, 434, 261);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(212, 5, 0, 0);
		panel.add(label);
		
		JLabel label_1 = new JLabel("Welcome First User!!");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(81, 22, 267, 14);
		panel.add(label_1);
		
		JLabel lblChooseYour = new JLabel("Choose a User  Name To Continue");
		lblChooseYour.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseYour.setBounds(81, 47, 267, 14);
		panel.add(lblChooseYour);
		
		JLabel lblUsername = new JLabel("UserName");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(81, 94, 69, 14);
		panel.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(160, 89, 172, 23);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnProceed = new JButton("Proceed");
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					System.out.println("New user entry DB opened!");
					stmt=c.createStatement();
					String sql="insert into USER values(2,'"+textField.getText()+"','NA','NA','NA','NA','NA');";
					stmt.executeUpdate(sql);
					stmt.close();
					System.out.println("Con close");
					c.commit();
					c.close();
					System.out.println("Username created!");
					frame.dispose();
					reg=new registration(2,1);
					reg.setVisible(true);
				}
				catch(Exception e){
					
				}
			}
		});
		btnProceed.setBounds(195, 135, 89, 23);
		panel.add(btnProceed);
		
		panel_1 = new Panel();
		panel_1.setBounds(10, 0, 424, 261);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setVisible(false);
		
		JLabel label_2 = new JLabel("");
		label_2.setBounds(207, 5, 0, 0);
		panel_1.add(label_2);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(39, 62, 109, 14);
		panel_1.add(lblUserName);
		
		textField_1 = new JTextField();
		textField_1.setBounds(158, 57, 180, 23);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnProceed_1 = new JButton("Proceed");
		btnProceed_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					System.out.println("Old User DB opened!");
					stmt=c.createStatement();
					String sql="select count(*) from USER where UNAME='"+textField_1.getText()+"';";
					ResultSet rs=stmt.executeQuery(sql);
					long count=rs.getLong(1);
					rs.next();
					rs.close();
					stmt.close();
					System.out.println("Con close");
					c.close();
					if(count==0)
						lblUserNotFound.setVisible(true);
					else{
						frame.dispose();
						if(textField_1.getText().equals("MASTER")){
							mst=new masteruser();
							mst.setVisible(true);
						}
						else{
							auth=new authentication(textField_1.getText());
							auth.setVisible(true);
						}
					}
				}
				catch(Exception e){
					
				}
			}
		});
		
		btnProceed_1.setBounds(174, 108, 89, 23);
		panel_1.add(btnProceed_1);
		
		lblUserNotFound = new JLabel("User Not Found!!");
		lblUserNotFound.setForeground(Color.RED);
		lblUserNotFound.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblUserNotFound.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserNotFound.setVisible(false);
		lblUserNotFound.setBounds(134, 30, 174, 14);
		panel_1.add(lblUserNotFound);
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			String sql="select count(USERID) from USER where USERID not in (1);";
			ResultSet rst=stmt.executeQuery(sql);
			long count=rst.getLong(1);
			if(count==0){
				panel.setVisible(true);
				panel_1.setVisible(false);
			}
			else{
				panel_1.setVisible(true);
				panel.setVisible(false);
			}
			rst.close();
			System.out.println("Con close");
			stmt.close();
			c.close();
				
	}
	catch(Exception e){
		
	}
	}
}
