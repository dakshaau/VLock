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

public class useroper extends JFrame {

	private JPanel contentPane;
	static useroper frame;
	JLabel lblUnlock;
	JButton btnNewButton;
	AddSample1 ad;
	String nm="";
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
					frame = new useroper("");
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
	public useroper(String name) {
		nm=name;
		frame=this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ad=new AddSample1(name);
		ad.setBounds(0, 0, 450, 300);
		ad.setVisible(false);
		contentPane.add(ad);
		
		JLabel lblChooseAnOp = new JLabel("Choose An Operation");
		lblChooseAnOp.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblChooseAnOp.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseAnOp.setBounds(128, 24, 175, 23);
		contentPane.add(lblChooseAnOp);
		
		JButton btnUserManagement = new JButton("Add Sample");
		btnUserManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ad.setVisible(true);
			}
		});
		btnUserManagement.setBounds(144, 86, 143, 23);
		contentPane.add(btnUserManagement);
		
		btnNewButton = new JButton("Access Locker");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				Locker1 lck=new Locker1(nm);
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
		btnOpen.setBounds(144, 176, 143, 23);
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
