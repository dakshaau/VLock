package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.awt.Color;

public class operation extends JFrame {

	private JPanel contentPane;
	static operation frame;
	Connection c;
	Statement stmt;
	JLabel lblUnlock;
	JButton btnNewButton;
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
					frame = new operation();
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
	public operation() {
		long count=0;
		frame=this;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
			System.out.println("DB Opened");
			stmt=c.createStatement();
			String sql="select count(*) from FILEPATH;";
			ResultSet rs=stmt.executeQuery(sql);
			count=rs.getLong(1);
			stmt.close();
			c.close();
		} catch(Exception e){}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChooseAnOp = new JLabel("Choose An Operation");
		lblChooseAnOp.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblChooseAnOp.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseAnOp.setBounds(128, 24, 175, 23);
		contentPane.add(lblChooseAnOp);
		
		JButton btnUserManagement = new JButton("User Management");
		btnUserManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				susermngmt sr=new susermngmt();
				sr.setVisible(true);
			}
		});
		btnUserManagement.setBounds(144, 86, 143, 23);
		contentPane.add(btnUserManagement);
		
		btnNewButton = new JButton("Unlock Folder");
		if(count!=0)
			btnNewButton.setEnabled(true);
		else
			btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag=false;
				Path toLocked=null;
				try{
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
				stmt=c.createStatement();
				String sql="select ID,FILEPATH from FILEPATH;";
				ResultSet rst=stmt.executeQuery(sql);
				File file=null;
				int id=1;
				try {
					String path;
					toLocked=Files.createTempDirectory(new File(".").toPath(), "Locked");
					Runtime.getRuntime().exec("attrib +h +s "+"\""+toLocked.toFile().getAbsolutePath()+"\"");
					while(rst.next()){
						path=rst.getString(2);
						String temp= toLocked.toString()+"/"+path.substring(path.lastIndexOf("\\")+1);
						Files.createDirectory(new File(temp).toPath());
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JFileChooser chooser=new JFileChooser(toLocked.toFile());
				chooser.setDialogTitle("Select your folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(frame.getContentPane()) == JFileChooser.APPROVE_OPTION) 
					file=chooser.getSelectedFile();
				else
					throw(new Exception());
				try{
					sql="select ID,FILEPATH from FILEPATH;";
					rst=stmt.executeQuery(sql);
					
					String path = null;
					while(rst.next()){
						if(rst.getString(2).endsWith(file.getName())){
							id=rst.getInt(1);
							path=rst.getString(2);
							break;
						}
					}	
					System.out.println(path);
					if(FileCrypt.decrypt(path)){
						flag=true;
						sql="delete from FILEPATH where ID="+id+";";
						stmt.executeUpdate(sql);
					}
					else
						flag=false;
				} catch (NullPointerException a){
					a.printStackTrace();
				}
				if(flag){
					lblUnlock.setText("Unlock Successfull");
					lblUnlock.setForeground(new Color(0,128,0));
					lblUnlock.setVisible(true);
					try {
						sql="select count(*) from FILEPATH;";
						rst=stmt.executeQuery(sql);
						if(rst.getLong(1)==0){
							btnNewButton.setEnabled(false);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					//lblUnlockUnsuccessfull.setVisible(true);
					lblUnlock.setText("Unlock Failed");
					lblUnlock.setForeground(Color.red);
					lblUnlock.setVisible(true);
				}
				stmt.close();
				c.close();
				
				
				for(File f : toLocked.toFile().listFiles()){
					f.delete();
				}
				toLocked.toFile().delete();
				} catch(Exception e2) {}
			}
			
		});
		btnNewButton.setBounds(144, 120, 143, 23);
		contentPane.add(btnNewButton);
		
		JButton btnOpen = new JButton("Lock Vault");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				usernotfound usr=new usernotfound();
				usr.setVisible(true);
			}
		});
		btnOpen.setBounds(144, 154, 143, 23);
		contentPane.add(btnOpen);
		
		lblUnlock = new JLabel("Unlock Successfull");
		lblUnlock.setHorizontalAlignment(SwingConstants.CENTER);
		lblUnlock.setForeground(new Color(0, 128, 0));
		lblUnlock.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblUnlock.setBounds(128, 202, 175, 23);
		lblUnlock.setVisible(false);
		contentPane.add(lblUnlock);
	}
}
