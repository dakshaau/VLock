package pack;

import java.awt.BorderLayout;
import java.sql.*;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class authentication extends JFrame {

	private JPanel contentPane;
	String nm;
	passwordgui pswd;
	static authentication frame;
	JButton btnPassword;
	private final JLabel lblStop = new JLabel("Stop");
	private final JLabel lblSpeak = new JLabel("Speak");
	private final JLabel lblNoMatch = new JLabel("No Match!! .. Access Restricted!");
	boolean recorded=false;
	JButton btnNewButton;
	Thread recrd,tried;
	Authentify a;
	Connection c;
	Statement stmt;
	private int tries=2;
	private Runnable tr=new Runnable(){
		public void run(){
			while(tries!=0);
			btnPassword.setEnabled(true);
			btnPassword.setVisible(true);
			btnNewButton.setEnabled(true);
		}
	};
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
					frame = new authentication("daksh");
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
	public authentication(String name) {
		frame=this;
		nm=name;
		tried=new Thread(tr);
		if(!tried.isAlive())
			tried.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWelcome = new JLabel("Welcome "+name);
		lblWelcome.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(120, 33, 232, 14);
		contentPane.add(lblWelcome);
		
		JLabel lblNewLabel = new JLabel("Choose Your Authentication Type");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBackground(UIManager.getColor("List.selectionBackground"));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel.setBounds(96, 60, 256, 23);
		contentPane.add(lblNewLabel);
		
		btnNewButton = new JButton("Audio");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				a=new Authentify();
				if(a.record()){
					recorded=true;
					btnNewButton.setEnabled(false);
				}
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblSpeak.setVisible(false);
				lblStop.setVisible(false);
				lblNoMatch.setVisible(false);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblSpeak.setVisible(true);
				recorded=false;
				recrd=new Thread(new Runnable() {
						@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("\"recrd\" started!");
						while(!recorded);
						lblSpeak.setVisible(false);
						lblStop.setVisible(true);
						a.setHits();
						String re=a.compare();
						String res=re.trim();						
						if(res.equals(nm)){
							frame.dispose();
							try{
								Class.forName("org.sqlite.JDBC");
								c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
								c.setAutoCommit(false);
								stmt=c.createStatement();
								String mail="";
								String sql="select EMAIL,ENOT from USER where UNAME='"+nm+"';";
								ResultSet rs=stmt.executeQuery(sql);
								mail=rs.getString(1);
								if(rs.getString(2).equalsIgnoreCase("YES"))
									MailTo.sendMail(mail, "VLock Account Accessed", "Your Vault has been opened!");
								rs.close();
								stmt.close();
								c.close();
								System.out.println("Con close");
							} catch(Exception e){}
							finally{
							useroper usr=new useroper(nm);
							usr.setVisible(true);}
						}
						else{
							lblStop.setVisible(false);
							lblNoMatch.setText("No Match!! ... Try Again");
							lblNoMatch.setVisible(true);
							btnNewButton.setEnabled(true);
							tries--;
						}
						recorded=false;
					}
				
				});
				if(!recrd.isAlive())
					recrd.start();
			}
		});
		btnNewButton.setBounds(179, 109, 106, 23);
		contentPane.add(btnNewButton);
		
		btnPassword = new JButton("Password");
		btnPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pswd=new passwordgui(nm);
				frame.dispose();
				pswd.setVisible(true);
			}
		});
		btnPassword.setBounds(179, 159, 106, 23);
		contentPane.add(btnPassword);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernotfound ur=new usernotfound();
				frame.dispose();
				ur.setVisible(true);
			}
		});
		btnCancel.setBounds(179, 203, 106, 23);
		contentPane.add(btnCancel);
		
		lblStop.setBounds(319, 106, 72, 28);
		frame.getContentPane().add(lblStop);
		lblStop.setHorizontalAlignment(SwingConstants.CENTER);
		lblStop.setForeground(new Color(220, 20, 60));
		lblStop.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStop.setVisible(false);
		lblSpeak.setBounds(319, 106, 72, 28);
		frame.getContentPane().add(lblSpeak);
		lblSpeak.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeak.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSpeak.setForeground(new Color(50, 205, 50));
		lblSpeak.setVisible(false);
		
		lblNoMatch.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoMatch.setForeground(new Color(220, 20, 60));
		lblNoMatch.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNoMatch.setBounds(96, 222, 265, 34);
		lblNoMatch.setVisible(false);
		
		frame.getContentPane().add(lblNoMatch);
	}
}
