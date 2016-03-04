package pack;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JPasswordField;

class AddSample extends JPanel {
	/**
	 * 
	 */
	private JPasswordField txtName;
	private JButton btnDone = new JButton("Done");
	private JLabel lblName = new JLabel("");
	public String name="";
	private JLabel lblSpeak = new JLabel("Speak");
	private JLabel lblStop = new JLabel("Stop");
	private JButton button = new JButton("1");
	private JButton button_1 = new JButton("2");
	private JButton button_2 = new JButton("3");
	private JButton button_3 = new JButton("4");
	private Authentify a;
	private boolean recorded=false,b1=false,b2=false,b3=false,b4=false;

	/**
	 * Create the panel.
	 */
	public AddSample(String n) {
		name=n;
		
		setLayout(null);
		
		final JPanel panel = new JPanel();
		panel.setBounds(10, 11, 430, 242);
		add(panel);
		panel.setLayout(null);
		
		txtName = new JPasswordField();
		txtName.setForeground(new Color(0, 0, 0));
		txtName.setBounds(153, 64, 122, 28);
		panel.add(txtName);
		txtName.setColumns(10);
		
		JButton btnEnterPassword = new JButton("Enter Password");
		btnEnterPassword.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if(Crypto.decrypt(txtName.getText()).equals(name)){
					lblName.setText("Hello "+name+"!");
					button.setEnabled(true);
					button_1.setEnabled(true);
					button_2.setEnabled(true);
					button_3.setEnabled(true);
					panel.setVisible(false);
				}
			}
		});
		btnEnterPassword.setBounds(156, 102, 118, 28);
		panel.add(btnEnterPassword);
		btnDone.setBounds(134, 211, 90, 28);
		add(btnDone);
		
		final JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(true);
				txtName.setForeground(Color.BLACK);
				txtName.setText("");
				recorded=b1=b2=b3=b4=false;
				a=null;
				btnCancel.getParent().setVisible(false);
			}
		});
		btnCancel.setBounds(225, 211, 90, 28);
		add(btnCancel);
		
		JLabel lblNewLabel = new JLabel("For best results it is recommended that you provide");
		lblNewLabel.setBounds(65, 45, 316, 28);
		add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_1 = new JLabel("all types of mentiond samples if possible");
		lblNewLabel_1.setBounds(105, 65, 231, 16);
		add(lblNewLabel_1);
		
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblName.setBounds(127, 18, 194, 28);
		add(lblName);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				a=new Authentify();
				if(a.record()){
					recorded=true;
					button.setEnabled(false);
				}
			}
		});
		
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				lblSpeak.setVisible(true);
				button_1.setEnabled(false);
				button_2.setEnabled(false);
				button_3.setEnabled(false);
				recorded=false;
				Thread recrd=new Thread(new Runnable() {
						@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("\"recrd\" started!");
						while(!recorded);
						lblSpeak.setVisible(false);
						lblStop.setVisible(true);
						b1=true;
						a.setHits();
						a.createSample(name);
						recorded=false;
						if(!b2)
							button_1.setEnabled(true);
						if(!b3)
							button_2.setEnabled(true);
						if(!b4)
							button_3.setEnabled(true);
					}
				
				});
				if(!recrd.isAlive())
					recrd.start();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSpeak.setVisible(false);
				lblStop.setVisible(false);
			}
		});
		button.setBounds(132, 129, 90, 28);
		add(button);
		button.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		button.setToolTipText("Normal Sample 1");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a=new Authentify();
				if(a.record()){
					recorded=true;
					button_1.setEnabled(false);
				}
			}
		});
		
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				lblSpeak.setVisible(true);
				button.setEnabled(false);
				button_2.setEnabled(false);
				button_3.setEnabled(false);
				recorded=false;
				Thread recrd=new Thread(new Runnable() {
						@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("\"recrd\" started!");
						while(!recorded);
						lblSpeak.setVisible(false);
						lblStop.setVisible(true);
						b2=true;
						a.setHits();
						a.createSample(name);
						recorded=false;
						if(!b1)
							button.setEnabled(true);
						if(!b3)
							button_2.setEnabled(true);
						if(!b4)
							button_3.setEnabled(true);
					}
				
				});
				if(!recrd.isAlive())
					recrd.start();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSpeak.setVisible(false);
				lblStop.setVisible(false);
			}
		});
		
		button_1.setBounds(224, 129, 90, 28);
		add(button_1);
		button_1.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		button_1.setToolTipText("Normal Sample 2");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a=new Authentify();
				if(a.record()){
					recorded=true;
					button_2.setEnabled(false);
				}
			}
		});
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				lblSpeak.setVisible(true);
				button_1.setEnabled(false);
				button.setEnabled(false);
				button_3.setEnabled(false);
				recorded=false;
				Thread recrd=new Thread(new Runnable() {
						@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("\"recrd\" started!");
						while(!recorded);
						lblSpeak.setVisible(false);
						lblStop.setVisible(true);
						b3=true;
						a.setHits();
						a.createSample(name);
						recorded=false;
						if(!b1)
							button.setEnabled(true);
						if(!b2)
							button_1.setEnabled(true);
						if(!b4)
							button_3.setEnabled(true);
					}
				
				});
				if(!recrd.isAlive())
					recrd.start();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSpeak.setVisible(false);
				lblStop.setVisible(false);
			}
		});
		
		button_2.setBounds(132, 159, 90, 28);
		add(button_2);
		button_2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		button_2.setToolTipText("Silent Sample");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a=new Authentify();
				if(a.record()){
					recorded=true;
					button_3.setEnabled(false);
				}
			}
		});
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSpeak.setVisible(false);
				lblStop.setVisible(false);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblSpeak.setVisible(true);
				button_1.setEnabled(false);
				button.setEnabled(false);
				button_2.setEnabled(false);
				recorded=false;
				Thread recrd=new Thread(new Runnable() {
						@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("\"recrd\" started!");
						while(!recorded);
						lblSpeak.setVisible(false);
						lblStop.setVisible(true);
						b4=true;
						a.setHits();
						a.createSample(name);
						recorded=false;
						if(!b1)
							button.setEnabled(true);
						if(!b2)
							button_1.setEnabled(true);
						if(!b3)
							button_2.setEnabled(true);
					}
				
				});
				if(!recrd.isAlive())
					recrd.start();
			}
		});
		
		button_3.setBounds(224, 159, 90, 28);
		add(button_3);
		button_3.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		button_3.setToolTipText("Noisy Sample");
		
		lblSpeak.setBounds(335, 141, 62, 20);
		add(lblSpeak);
		lblSpeak.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeak.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSpeak.setForeground(new Color(50, 205, 50));
		lblSpeak.setVisible(false);
		
		lblStop.setBounds(335, 141, 62, 20);
		add(lblStop);
		lblStop.setHorizontalAlignment(SwingConstants.CENTER);
		lblStop.setForeground(new Color(220, 20, 60));
		lblStop.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStop.setVisible(false);
		
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(true);
				name="";
				txtName.setForeground(Color.BLACK);
				txtName.setText("");
				recorded=b1=b2=b3=b4=false;
				a=null;
				btnDone.getParent().setVisible(false);
			}
		});

	}
}
