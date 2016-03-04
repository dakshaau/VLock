package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class masteruser extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	Connection c;
	Statement stmt;
	static masteruser frame;
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
					frame = new masteruser();
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
	public masteruser() {
		frame=this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWelcomeMasterUser = new JLabel("Welcome Master User!!");
		lblWelcomeMasterUser.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblWelcomeMasterUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeMasterUser.setBounds(131, 28, 188, 25);
		contentPane.add(lblWelcomeMasterUser);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(111, 82, 72, 14);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(193, 78, 136, 25);
		contentPane.add(passwordField);
		
		JButton btnProceed = new JButton("Proceed");
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String pass;
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
					stmt=c.createStatement();
					String sql="select PASSWORD from USER where UNAME='MASTER';";
					ResultSet rst=stmt.executeQuery(sql);
					pass=rst.getString(1);
					if(pass.equals(passwordField.getText())){
						System.out.println("Success");
						frame.dispose();
						operation opr=new operation();
						opr.setVisible(true);
					}
					else
						System.out.println("Wrong Password");
					stmt.close();
					c.close();
				} catch(Exception e){
					
				}
			}
		});
		btnProceed.setBackground(new Color(153, 204, 255));
		btnProceed.setBounds(183, 119, 89, 23);
		contentPane.add(btnProceed);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				usernotfound usr=new usernotfound();
				usr.setVisible(true);
			}
		});
		btnCancel.setBackground(new Color(153, 204, 255));
		btnCancel.setBounds(183, 154, 89, 23);
		contentPane.add(btnCancel);
	}
}
