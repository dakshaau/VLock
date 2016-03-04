package pack;

import java.awt.EventQueue;

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

public class Locker {

	Voice c=new Voice();
	private JFrame frame;
	private JTextField textField;
	JPanel Locker = new JPanel();
	JButton btnUnlock = new JButton("Unlock");
	JButton btnOpenLocker = new JButton("Open Vault");
	JButton btnInputVoice = new JButton("Input Voice");
	JLabel lblUnlockedSuccessfully = new JLabel("Unlocked Successfully!!");
	JLabel lblLockUnsuccessfull = new JLabel("Lock Unsuccessfull!!");
	JLabel lblLockedSuccessfully = new JLabel("Locked Successfully!!");
	JLabel lblUnlockUnsuccessfull = new JLabel("Unlock Unsuccessfull!!");
	JPanel panel = new JPanel();
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
	private final JButton btnBack = new JButton("Lock Vault");
	private final JLabel lblSpeak = new JLabel("Speak");
	private final JLabel lblStop = new JLabel("Stop");
	private static AddSample add;
	private JMenuItem mntmAddSample = new JMenuItem("Add Sample");
	private JButton btnPassword = new JButton("Password");
	private Runnable tr=new Runnable(){
		public void run(){
			while(tries!=0);
			btnPassword.setEnabled(true);
			btnPassword.setVisible(true);
			btnInputVoice.setEnabled(true);
		}
	};
	Thread tried;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
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
						File user=new File("./user.cfg.enc");
						if(user.exists()){
							if(FileCrypt.decryptFile(user.getAbsolutePath())){
							try {
								BufferedReader in =new BufferedReader(new FileReader(new File("./user.cfg")));
								name=in.readLine();
								in.close();
								userExists=true;
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						}
						else
							userExists=false;
					} catch(Exception e){}
					Locker window = new Locker();
					window.frame.setVisible(true);
					if (p.toFile().exists()) {
			            Runtime.getRuntime().addShutdownHook(new Thread() {
			                public void run() {
			                    try {
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
	 * Create the application.
	 */
	public Locker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	Thread recrd;
	private final JLabel lblWelcome = new JLabel("Welcome");
	private final JLabel lblNoMatch = new JLabel("No Match!! .. Access Restricted!");
	private final JButton btnNewUser = new JButton("New User");
	private JPasswordField passwordField;
	private void initialize() {
		frame = new JFrame();
		frame.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentRemoved(ContainerEvent arg0) {
				if(arg0.getComponent().equals(c)){
					mntmAddSample.setEnabled(true);
				}
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
				if(arg0.getComponent().equals(c)){
					mntmAddSample.setEnabled(true);
				}
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				File map=new File("./lf.cfg");
				File user=new File("./user.cfg");
				if(user.exists())
					FileCrypt.encryptFile(user.getAbsolutePath());
				if(map.exists())
					FileCrypt.encryptFile(map.getAbsolutePath());
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				File map=new File("./lf.cfg.enc");
				if(map.exists()){
					FileCrypt.decryptFile(map.getAbsolutePath());
					btnUnlock.setEnabled(true);
				}
				if(userExists){
					btnNewUser.setEnabled(false);
					btnInputVoice.setEnabled(true);
					mntmAddSample.setEnabled(true);
				}
				else
				{
						btnNewUser.setEnabled(true);
						mntmAddSample.setEnabled(false);
				}
				tried=new Thread(tr);
				tried.start();
			}
		});
		frame.setTitle("VLock Beta");
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		btnOpenLocker.setEnabled(false);
		btnOpenLocker.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblStop.setVisible(false);
				lblWelcome.setVisible(false);
				lblNoMatch.setVisible(false);
			}
		});
		
		btnOpenLocker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locker.setVisible(true);
				btnOpenLocker.setEnabled(false);
				btnInputVoice.setEnabled(false);
			}
		});
		try{
		} catch(NullPointerException e){}
		add=new AddSample(name);
		add.setBounds(6, 25, 432, 247);
		frame.getContentPane().add(add);
		add.setVisible(false);
		c.setVisible(false);
		c.setBounds(6, 25, 432, 247);
		frame.getContentPane().add(c);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 444, 23);
		frame.getContentPane().add(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		mntmAddSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add.setVisible(true);
			}
		});
		mnNewMenu.add(mntmAddSample);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File map=new File("./lf.cfg");
				File user=new File("./user.cfg");
				if(user.exists())
					FileCrypt.encryptFile(user.getAbsolutePath());
				if(map.exists())
					FileCrypt.encryptFile(map.getAbsolutePath());
				System.exit(0);
			}
		});
		mnNewMenu.add(mntmClose);
		
		panel.setBounds(10, 31, 428, 235);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		panel.setVisible(false);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(136, 75, 159, 28);
		panel.add(passwordField);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if(Crypto.decrypt(passwordField.getText()).equals(name)){
					btnOpenLocker.setEnabled(true);
					panel.setVisible(false);
					btnPassword.setEnabled(false);
					btnPassword.setVisible(false);
					btnInputVoice.setEnabled(false);
					lblWelcome.setText("Welcome "+name+"!!");
					lblWelcome.setVisible(true);
				}
				else{
					lblNoMatch.setText("Incorrect Password!!");
					lblNoMatch.setVisible(true);
				}
			}
		});
		btnSubmit.setBounds(169, 115, 90, 28);
		panel.add(btnSubmit);
		
		Locker.setBounds(10, 13, 424, 250);
		frame.getContentPane().add(Locker);
		Locker.setLayout(null);
		Locker.setVisible(false);
		
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
					File map=new File("./lf.cfg");
					if(!map.exists()){
						try {
							map.createNewFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if(FileCrypt.encrypt(textField.getText())){
						lblLockedSuccessfully.setVisible(true);
						try {
							out=new BufferedWriter(new FileWriter(map,true));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							out.append(textField.getText()+"/");
							out.close();
							out=null;
						} catch (IOException e1) {
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
				File map=new File("./lf.cfg");
				Path toLocked=null;
				File file=null;
				try {
					in=new BufferedReader(new FileReader(map));
					mtr=in.readLine();
					in.close();
					in=null;
					toLocked=Files.createTempDirectory(new File(".").toPath(), "Locked");
					Runtime.getRuntime().exec("attrib +h +s "+"\""+toLocked.toFile().getAbsolutePath()+"\"");
					for(String path : mtr.split("/")){
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
				chooser=new JFileChooser(toLocked.toFile());
				chooser.setDialogTitle("Select your folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(frame.getContentPane()) == JFileChooser.APPROVE_OPTION) 
					file=chooser.getSelectedFile();
				try{
					out=new BufferedWriter(new FileWriter(map));
					String path = null;
					for(String p : mtr.split("/")){
						if(p.endsWith(file.getName())){
							path=p;
							break;
						}
					}
					System.out.println(path);
					if(FileCrypt.decrypt(path)){
						flag=true;
						String temp=mtr;
						int beg=temp.indexOf(path);
						int end=beg+(path+"/").length();
						if(beg>0)
							temp=mtr.substring(0, beg)+mtr.substring(end);
						else
							temp=mtr.substring(end);
						mtr=temp;
						System.out.println(mtr);
					}
					else
						flag=false;
				} catch (NullPointerException a){
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(flag){
					lblUnlockedSuccessfully.setVisible(true);
					try {
						if(mtr.equals("")){
							out.close();
							Files.deleteIfExists(map.toPath());
							btnUnlock.setEnabled(false);
						}
						else{
							out.write(mtr);
							out.close();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					lblUnlockUnsuccessfull.setVisible(true);
					try {
						out.write(mtr);
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				for(File f : toLocked.toFile().listFiles()){
					f.delete();
				}
				toLocked.toFile().delete();
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
		btnUnlock.setEnabled(false);
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
				btnOpenLocker.setEnabled(false);
				btnInputVoice.setEnabled(true);
				btnPassword.setEnabled(false);
				btnPassword.setVisible(false);
				tries=2;
				tried=new Thread(tr);
				if(!tried.isAlive())
					tried.start();
			}
		});
		btnBack.setBounds(167, 204, 90, 28);
		
		Locker.add(btnBack);
		btnOpenLocker.setBounds(178, 130, 98, 28);
		frame.getContentPane().add(btnOpenLocker);
		btnInputVoice.setEnabled(false);
		btnInputVoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSpeak.setVisible(false);
				lblStop.setVisible(false);
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
						String res=a.compare();
						if(!res.equals("Error")){
							btnOpenLocker.setEnabled(true);
							lblStop.setVisible(false);
							char[] ch=res.toCharArray();
							String name="";
							for(char b:ch){
								if((int)b>32&&(int)b<127)
									name+=b;
							}
							lblWelcome.setText("Welcome "+name+"!!");
							lblWelcome.setVisible(true);
							btnInputVoice.setEnabled(false);
						}
						else{
							lblStop.setVisible(false);
							lblNoMatch.setText("No Match!! ... Try Again");
							lblNoMatch.setVisible(true);
							btnInputVoice.setEnabled(true);
							tries--;
						}
						recorded=false;
					}
				
				});
				if(!recrd.isAlive())
					recrd.start();
			}
		});
		
		btnInputVoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				a=new Authentify();
				if(a.record()){
					recorded=true;
					btnInputVoice.setEnabled(false);
				}
			}
		});
		btnNewUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread done=new Thread(new Runnable(){
					public void run(){
						while(!c.Done);
						File user=new File("./user.cfg");
						if(user.exists()){
							mntmAddSample.setEnabled(true);
							btnInputVoice.setEnabled(true);
							btnNewUser.setEnabled(false);
							try {
								BufferedReader in=new BufferedReader(new FileReader(user));
								name=in.readLine();
								in.close();
								add.name=name;
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
				if(!done.isAlive())
					done.start();
			}
		});
		btnNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.Done=false;
				c.setVisible(true);
			}
		});
		btnNewUser.setBounds(181, 51, 90, 28);
		
		frame.getContentPane().add(btnNewUser);
		btnPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblNoMatch.setVisible(false);
			}
		});
		
		btnPassword.setEnabled(false);
		btnPassword.setVisible(false);
		btnPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(true);
				passwordField.setText("");
			}
		});
		btnPassword.setBounds(182, 89, 90, 28);
		frame.getContentPane().add(btnPassword);
		btnInputVoice.setBounds(182, 89, 88, 28);
		frame.getContentPane().add(btnInputVoice);
		lblStop.setBounds(322, 89, 72, 28);
		frame.getContentPane().add(lblStop);
		lblStop.setHorizontalAlignment(SwingConstants.CENTER);
		lblStop.setForeground(new Color(220, 20, 60));
		lblStop.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStop.setVisible(false);
		lblSpeak.setBounds(322, 89, 72, 28);
		frame.getContentPane().add(lblSpeak);
		lblSpeak.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeak.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSpeak.setForeground(new Color(50, 205, 50));
		lblSpeak.setVisible(false);
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setForeground(new Color(50, 205, 50));
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblWelcome.setBounds(117, 170, 223, 34);
		lblWelcome.setVisible(false);
		
		frame.getContentPane().add(lblWelcome);
		lblNoMatch.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoMatch.setForeground(new Color(220, 20, 60));
		lblNoMatch.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNoMatch.setBounds(94, 170, 265, 34);
		lblNoMatch.setVisible(false);
		
		frame.getContentPane().add(lblNoMatch);
	}
}
