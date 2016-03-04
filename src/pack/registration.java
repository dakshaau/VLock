package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.sun.corba.se.pept.transport.Connection;

import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class registration extends JFrame {

	private JPanel contentPane;
	JPasswordField pass1;
	JPasswordField pass2;
	private JTextField textQ;
	private JTextField textA;
	String uname,email,Squestion,Sans,uidentity;
	int uid;
	boolean i;
	java.sql.Connection c=null;
	Statement stmt = null;
	long ind;
	private JTextField textMail;
	JLabel lblStar;
	JCheckBox enot;
	JLabel lblAck;
	usernotfound user;
	static registration frame;
	int mod;
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
					frame = new registration(2,1);
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
	public registration(long index,int mode) {
		frame=this;
		ind=index;
		mod=mode;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 282);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		 
		 
		 
		 final JPanel panel = new JPanel();
		 panel.setBounds(0, 6, 470, 233);
		 contentPane.add(panel);
		 panel.setLayout(null);
		 
		 JLabel lblEnterPassword = new JLabel("Re-Enter Password");
		 lblEnterPassword.setHorizontalAlignment(SwingConstants.CENTER);
		 lblEnterPassword.setBounds(28, 68, 121, 14);
		 panel.add(lblEnterPassword);
		 
		 JLabel lblReenterPassword = new JLabel("Enter password");
		 lblReenterPassword.setHorizontalAlignment(SwingConstants.CENTER);
		 lblReenterPassword.setBounds(25, 42, 107, 14);
		 panel.add(lblReenterPassword);
		 
		 pass1 = new JPasswordField();
		 pass1.addFocusListener(new FocusAdapter() {
		 	@Override
		 	public void focusGained(FocusEvent e) {
		 		lblStar.setVisible(false);
		 		lblStar.setForeground(Color.red);
		 		lblAck.setVisible(false);
		 	}
		 });
		 pass1.setBounds(210, 36, 179, 23);
		 panel.add(pass1);
		 
		 JLabel lblEmailId = new JLabel("Enter E-mail id");
		 lblEmailId.setHorizontalAlignment(SwingConstants.LEFT);
		 lblEmailId.setBounds(34, 94, 121, 14);
		 panel.add(lblEmailId);
		 
		 pass2 = new JPasswordField();
		 pass2.addFocusListener(new FocusAdapter() {
		 	@Override
		 	public void focusGained(FocusEvent e) {
		 		if(pass1.getText().equals(pass2.getText())){
		 			lblStar.setForeground(new Color(0,128,0));
		 		}
		 		else
		 			lblStar.setForeground(Color.red);
		 		lblStar.setVisible(true);
		 		lblAck.setVisible(false);
		 	}
		 });
		 pass2.addKeyListener(new KeyAdapter() {
		 	@Override
		 	public void keyReleased(KeyEvent arg0) {
		 		if(pass1.getText().equals(pass2.getText())){
		 			lblStar.setForeground(new Color(0,128,0));
		 		}
		 		else
		 			lblStar.setForeground(Color.red);
		 		//wwSystem.out.println(pass1.getText()+" "+pass2.getText());
		 	}
		 });
		 pass2.setBounds(210, 65, 179, 23);
		 panel.add(pass2);
		 
		 JButton btnProceed = new JButton("Proceed");
		 btnProceed.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		try{
		 			String sel;
		 			if(enot.isSelected())
		 				sel="yes";
		 			else
		 				sel="no";
		 			String password="";
		 			if(pass1.getText().equals(pass2.getText()))
		 				password=pass1.getText();
		 			if(pass1.getText()!=null&&pass2.getText()!=null&&textMail.getText()!=null&&textA.getText()!=null&&
			 				textQ.getText()!=null&&!password.equals("")){
		 				System.out.println("index : "+ind);
		 				Class.forName("org.sqlite.JDBC");
		 				c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
		 				System.out.println("DB opened for change!");
		 				stmt=c.createStatement();
		 				String sql="update USER set EMAIL='"+textMail.getText()+"',ENOT='"+sel+"',SECANS='"+textA.getText()+"',SECQ='"+textQ.getText()+"',PASSWORD='"+password+"' where USERID="+ind+";";
		 				stmt.executeUpdate(sql);
		 				stmt.close();
		 				c.close();
		 				System.out.println("Username "+ind+" updated!");
		 				lblAck.setText("User Registered Successfully");
		 				lblAck.setForeground(new Color(0,128,0));
		 				lblAck.setVisible(true);
		 				frame.dispose();
		 				if(mod==1){
		 				user=new usernotfound();
		 				user.setVisible(true);}
		 				else if(mod==2){
		 					operation opr=new operation();
		 					opr.setVisible(true);
		 				}
		 			}
		 			else{
		 				lblAck.setText("Complete all fields!");
		 				lblAck.setForeground(Color.red);
		 				lblAck.setVisible(true);
		 			}
		 				
		 		} catch(Exception e){
		 			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		 			lblAck.setText("User Registeration Failed");
		 			lblAck.setForeground(Color.red);
		 			lblAck.setVisible(true);
		 		}
		 	}
		 });
		 
		  lblAck = new JLabel("User registered Successfully");
		  lblAck.setVisible(false);
		  lblAck.setBounds(57, 201, 275, 24);
		  panel.add(lblAck);
		  lblAck.setForeground(new Color(0, 128, 0));
		  lblAck.setFont(new Font("SansSerif", Font.BOLD, 17));
		  lblAck.setHorizontalAlignment(SwingConstants.CENTER);
		 
		 final JLabel lblEnterYourSecurity = new JLabel("Enter your Security Question");
		 lblEnterYourSecurity.setBounds(29, 123, 166, 14);
		 panel.add(lblEnterYourSecurity);
		 lblEnterYourSecurity.setHorizontalAlignment(SwingConstants.CENTER);
		 
		 textQ = new JTextField();
		 textQ.addFocusListener(new FocusAdapter() {
		 	@Override
		 	public void focusGained(FocusEvent e) {
		 		lblAck.setVisible(false);
		 	}
		 });
		 textQ.setBounds(210, 119, 179, 24);
		 panel.add(textQ);
		 textQ.setColumns(10);
		 
		 final JLabel lblEnterAnswer = new JLabel("Enter Answer");
		 lblEnterAnswer.setBounds(36, 152, 93, 14);
		 panel.add(lblEnterAnswer);
		 lblEnterAnswer.setHorizontalAlignment(SwingConstants.LEFT);
		 
		 textA = new JTextField();
		 textA.addFocusListener(new FocusAdapter() {
		 	@Override
		 	public void focusGained(FocusEvent e) {
		 		lblAck.setVisible(false);
		 	}
		 });
		 textA.setBounds(210, 148, 179, 24);
		 panel.add(textA);
		 btnProceed.setBounds(370, 179, 89, 23);
		 panel.add(btnProceed);
		 
		 JLabel label = new JLabel("New User Registration");
		 label.setHorizontalAlignment(SwingConstants.CENTER);
		 label.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		 label.setBounds(82, 6, 275, 19);
		 panel.add(label);
		 
		 textMail = new JTextField();
		 textMail.addFocusListener(new FocusAdapter() {
		 	@Override
		 	public void focusGained(FocusEvent e) {
		 		lblAck.setVisible(false);
		 	}
		 });
		 textMail.setToolTipText("e.g: abc@domain.com");
		 textMail.setBounds(210, 89, 158, 23);
		 panel.add(textMail);
		 textMail.setColumns(10);
		 
		 enot = new JCheckBox("<html><body>Email<br>Notification ");
		 enot.setBounds(375, 81, 91, 39);
		 panel.add(enot);
		 
		 lblStar = new JLabel("*");
		 lblStar.setForeground(Color.BLACK);
		 lblStar.setVisible(false);
		 lblStar.setFont(new Font("SansSerif", Font.BOLD, 19));
		 lblStar.setBounds(395, 71, 55, 12);
		 panel.add(lblStar);
		 
		 JButton btnCancel = new JButton("Cancel");
		 btnCancel.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		frame.dispose();
		 		if(mod==1){
		 			usernotfound usr=new usernotfound();
		 			usr.setVisible(true);
		 		}
		 		else if(mod==2)
		 		{
		 			operation opr=new operation();
		 			opr.setVisible(true);
		 		}
		 	}
		 });
		 btnCancel.setBounds(369, 204, 90, 22);
		 panel.add(btnCancel);
		
	}
}
