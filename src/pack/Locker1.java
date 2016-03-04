package pack;

import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.JPasswordField;

public class Locker1 {
	private JFrame frame;
	private JTextField textField;
	JPanel Locker = new JPanel();
	JButton btnUnlock = new JButton("Unlock");
	JLabel lblUnlockedSuccessfully = new JLabel("Unlocked Successfully!!");
	JLabel lblLockUnsuccessfull = new JLabel("Lock Unsuccessfull!!");
	JLabel lblLockedSuccessfully = new JLabel("Locked Successfully!!");
	JLabel lblUnlockUnsuccessfull = new JLabel("Unlock Unsuccessfull!!");
	JFileChooser chooser;
	String path;
	BufferedWriter out=null;
	BufferedReader in=null;
	String mtr;
	static String name;
	boolean recorded=false;
	static boolean userExists=false;
	Authentify a;
	private int tries=2;
	private final JButton btnBack = new JButton("Back");
	String nm;
	Connection c;
	Statement stmt;
	int id;
	

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
					Locker1 window = new Locker1("");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Locker1(String name) {
		initialize(name);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	Thread recrd;
	private void initialize(String name) {
		long count=0;
		nm=name;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("VLock Beta");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		try{
			Class.forName("org.sqlite.JDBC");
			c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
			stmt=c.createStatement();
			String sql="select USERID from USER where UNAME='"+nm+"';";
			ResultSet rs=stmt.executeQuery(sql);
			id=rs.getInt(1);
			sql="select count(*) from FILEPATH where UID="+id+";";
			rs=stmt.executeQuery(sql);
			count=rs.getLong(1);
			stmt.close();
			c.close();
			System.out.println("Con close");
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Locker.setBounds(10, 13, 424, 250);
		frame.getContentPane().add(Locker);
		Locker.setLayout(null);
		Locker.setVisible(true);
		
		JLabel lblNewLabel = new JLabel("Locate Folder :");
		lblNewLabel.setBounds(16, 27, 81, 16);
		Locker.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(109, 21, 207, 28);
		Locker.add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblLockedSuccessfully.setVisible(false);
				lblLockUnsuccessfull.setVisible(false);
				lblUnlockedSuccessfully.setVisible(false);
				lblUnlockUnsuccessfull.setVisible(false);
				chooser=new JFileChooser("user.home");
				chooser.setDialogTitle("Select your folder");
				chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(frame.getContentPane()) == JFileChooser.APPROVE_OPTION) 
					path=chooser.getSelectedFile().getAbsolutePath();
				textField.setText(path);
			}
		});
		btnBrowse.setBounds(324, 21, 81, 28);
		Locker.add(btnBrowse);
		
		JButton btnLock = new JButton("Lock");
		btnLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals("")){
					if(FileCrypt.encrypt(textField.getText())){
						lblLockedSuccessfully.setVisible(true);
						try {
							Class.forName("org.sqlite.JDBC");
							c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
							c.setAutoCommit(false);
							stmt=c.createStatement();
							String sql="select ID from FILEPATH;";
							ResultSet rs=stmt.executeQuery(sql);
							int num=0;
							while(rs.next()){
									num=rs.getInt(1)+1;
							}
							rs.close();
							stmt.close();
							System.out.println("select done");
							stmt=c.createStatement();
							sql="insert into FILEPATH values("+num+",'"+textField.getText()+"',"+id+");";
							stmt.executeUpdate(sql);
							System.out.println("Input val");
							stmt.close();
							c.commit();
							System.out.println("commit ... Con close");
							c.close();
							System.out.println("Locked");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						btnUnlock.setEnabled(true);
					}
					else
						lblLockUnsuccessfull.setVisible(true);
				}
				textField.setText("");
			}
		});
		btnLock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblLockedSuccessfully.setVisible(false);
				lblLockUnsuccessfull.setVisible(false);
				lblUnlockedSuccessfully.setVisible(false);
				lblUnlockUnsuccessfull.setVisible(false);
			}
		});
		btnLock.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnLock.setBounds(167, 61, 90, 28);
		Locker.add(btnLock);
		btnUnlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag=false;
				Path toLocked=null;
				File file=null;
				long count=0;
				try {
					Class.forName("org.sqlite.JDBC");
					c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					stmt=c.createStatement();
					String sql="select FILEPATH from FILEPATH where UID="+id+";";
					ResultSet rs=stmt.executeQuery(sql);
					toLocked=Files.createTempDirectory(new File(".").toPath(), "Locked");
					Runtime.getRuntime().exec("attrib +h +s "+"\""+toLocked.toFile().getAbsolutePath()+"\"");
					while(rs.next()){
						path=rs.getString(1);
						String temp= toLocked.toString()+"/"+path.substring(path.lastIndexOf("\\")+1);
						Files.createDirectory(new File(temp).toPath());
					}
					stmt.close();
					c.close();
					System.out.println("Con close");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try{
					chooser=new JFileChooser(toLocked.toFile());
					chooser.setDialogTitle("Select your folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(frame.getContentPane()) == JFileChooser.APPROVE_OPTION) 
					file=chooser.getSelectedFile();
				else{
					throw(new Exception());
				}
				try{
					Class.forName("org.sqlite.JDBC");
					c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
					c.setAutoCommit(false);
					stmt=c.createStatement();
					String sql="select ID,FILEPATH from FILEPATH where UID="+id+";";
					ResultSet rs=stmt.executeQuery(sql);
					String path = null;
					String p="";
					int id1=1;
					while(rs.next()){
						p=rs.getString(2);
						if(p.endsWith(file.getName())){
							path=p;
							id1=rs.getInt(1);
							break;
						}
					}
					System.out.println(path);
					rs.close();
					stmt.close();
					c.close();
					if(FileCrypt.decrypt(path)){
						flag=true;
						try{
							Class.forName("org.sqlite.JDBC");
							c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
							c.setAutoCommit(false);
							stmt=c.createStatement();
							sql="delete from FILEPATH where ID="+id1+";";
							stmt.executeUpdate(sql);
							sql="select count(ID) from FILEPATH where UID="+id+";";
							ResultSet rst=stmt.executeQuery(sql);
							count=rst.getLong(1);
							rs.close();
							stmt.close();
							c.commit();
							c.close();
							System.out.println(path);
						} catch(Exception e2){
							e2.printStackTrace();
						}
					}
					else
						flag=false;
				} catch (Exception a){					
				} 
				if(flag){
					if(count==0)
						btnUnlock.setEnabled(false);
					else
						btnUnlock.setEnabled(true);
					lblUnlockedSuccessfully.setVisible(true);
				}
				else{
					if(count==0)
						btnUnlock.setEnabled(false);
					else
						btnUnlock.setEnabled(true);
					lblUnlockUnsuccessfull.setVisible(true);
				}
				String s="";
				for(File f : toLocked.toFile().listFiles()){
					s=f.getAbsolutePath();
					Runtime.getRuntime().exec("cmd.exe /C del "+"\""+s+"\"");
				}
				Runtime.getRuntime().exec("cmd.exe /C del "+"\""+toLocked.toFile().getAbsolutePath()+"\"");
				} catch(Exception a){}
			}
		});
		btnUnlock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblLockedSuccessfully.setVisible(false);
				lblLockUnsuccessfull.setVisible(false);
				lblUnlockedSuccessfully.setVisible(false);
				lblUnlockUnsuccessfull.setVisible(false);
			}
		});
		
		btnUnlock.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnUnlock.setBounds(167, 102, 90, 28);
		if(count==0)
			btnUnlock.setEnabled(false);
		else
			btnUnlock.setEnabled(true);
		Locker.add(btnUnlock);
		
		
		lblUnlockedSuccessfully.setHorizontalAlignment(SwingConstants.CENTER);
		lblUnlockedSuccessfully.setForeground(new Color(50, 205, 50));
		lblUnlockedSuccessfully.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUnlockedSuccessfully.setBounds(120, 145, 188, 47);
		lblUnlockedSuccessfully.setVisible(false);
		Locker.add(lblUnlockedSuccessfully);
		
		
		lblLockUnsuccessfull.setHorizontalAlignment(SwingConstants.CENTER);
		lblLockUnsuccessfull.setForeground(new Color(220, 20, 60));
		lblLockUnsuccessfull.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLockUnsuccessfull.setBounds(120, 145, 188, 47);
		lblLockUnsuccessfull.setVisible(false);
		Locker.add(lblLockUnsuccessfull);
		
		
		lblLockedSuccessfully.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLockedSuccessfully.setForeground(new Color(50, 205, 50));
		lblLockedSuccessfully.setHorizontalAlignment(SwingConstants.CENTER);
		lblLockedSuccessfully.setBounds(120, 145, 188, 47);
		lblLockedSuccessfully.setVisible(false);
		Locker.add(lblLockedSuccessfully);
		
		
		lblUnlockUnsuccessfull.setHorizontalAlignment(SwingConstants.CENTER);
		lblUnlockUnsuccessfull.setForeground(new Color(220, 20, 60));
		lblUnlockUnsuccessfull.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUnlockUnsuccessfull.setBounds(120, 145, 188, 47);
		lblUnlockUnsuccessfull.setVisible(false);
		Locker.add(lblUnlockUnsuccessfull);
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblLockedSuccessfully.setVisible(false);
				lblLockUnsuccessfull.setVisible(false);
				lblUnlockedSuccessfully.setVisible(false);
				lblUnlockUnsuccessfull.setVisible(false);
			}
		});
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locker.setVisible(false);
				frame.dispose();
				useroper usr=new useroper(nm);
				usr.setVisible(true);
			}
		});
		btnBack.setBounds(167, 204, 90, 28);
		
		Locker.add(btnBack);
	}
}
