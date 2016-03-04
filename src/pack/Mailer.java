package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import pack.MailTo;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;

public class Mailer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @wbp.nonvisual location=217,139
	 */
	private final JScrollPane Message = new JScrollPane();
	/**
	 * @wbp.nonvisual location=202,119
	 */
	private final JTextArea txtrMsg = new JTextArea();
	/**
	 * @wbp.nonvisual location=41,59
	 */
	private final JTextField txtTo = new JTextField();
	/**
	 * @wbp.nonvisual location=41,79
	 */
	private final JTextField txtSubj = new JTextField();
	/**
	 * @wbp.nonvisual location=31,139
	 */
	private final JLabel lblToAddress = new JLabel("To Address :");
	/**
	 * @wbp.nonvisual location=61,179
	 */
	private final JLabel lblSubject = new JLabel("Subject :");
	/**
	 * @wbp.nonvisual location=61,219
	 */
	private final JLabel lblMessage = new JLabel("Message :");
	/**
	 * @wbp.nonvisual location=287,229
	 */
	private final JButton btnSendMail = new JButton("Send Mail");
	/**
	 * @wbp.nonvisual location=197,239
	 */
	private final JButton btnClear = new JButton("Clear");
	/**
	 * @wbp.nonvisual location=81,199
	 */
	private final JLabel lblError = new JLabel("           All fields \r\nare necessary!!");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
					    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					        if ("Nimbus".equals(info.getName())) {
					            UIManager.setLookAndFeel(info.getClassName());
					            break;
					        }
					    }
					} catch (Exception e) {
					    // If Nimbus is not available, you can set the GUI to another look and feel.
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					}
					Mailer frame = new Mailer();
					frame.setVisible(true);
					//System.out.println("ClassPath : "+this.getClass().getResource("").getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Mailer() {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/mailIco.png")));
		setTitle("Mailer");
		setResizable(false);
		lblError.setVerticalAlignment(SwingConstants.BOTTOM);
		lblError.setForeground(new Color(220, 20, 60));
		lblError.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMessage.setForeground(new Color(152, 251, 152));
		lblMessage.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSubject.setForeground(new Color(152, 251, 152));
		lblSubject.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtSubj.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblError.setVisible(false);
			}
		});
		txtSubj.setToolTipText("E-Mail's Subject");
		txtSubj.setColumns(10);
		txtTo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblError.setVisible(false);
			}
		});
		txtTo.setToolTipText("Reciever's E-Mail Address");
		txtTo.setColumns(10);
		txtrMsg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblError.setVisible(false);
			}
		});
		Message.setViewportView(txtrMsg);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setLayout(null);
		lblToAddress.setForeground(new Color(152, 251, 152));
		lblToAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		getContentPane().add(lblToAddress);
		getContentPane().add(lblSubject);
		getContentPane().add(lblMessage);
		getContentPane().add(txtTo);
		getContentPane().add(txtSubj);
		getContentPane().add(lblError);
		getContentPane().add(Message);
		btnSendMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!txtTo.getText().equals("")&&!txtSubj.getText().equals("")&&!txtrMsg.getText().equals("")){
					//System.out.println(txtTo.getText()+" "+txtSubj.getText()+" "+txtrMsg.getText());
					MailTo.sendMail(txtTo.getText(),txtSubj.getText(),txtrMsg.getText());
				}
				else
					lblError.setVisible(true);
					
			}
		});
		getContentPane().add(btnSendMail);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtTo.setText("");
				txtSubj.setText("");
				txtrMsg.setText("");
				lblError.setVisible(false);
			}
		});
		getContentPane().add(btnClear);
		lblError.setBounds(130,94,250,70);
		lblToAddress.setBounds(30,20,90,20);
		lblSubject.setBounds(30, 57, 75, 20);
		lblMessage.setBounds(30,94,75,20);
		Message.setBounds(130,94,250,120);
		txtSubj.setBounds(130,57,250,30);
		txtTo.setBounds(130,20,250,30);
		btnSendMail.setBounds(250,220,100,30);
		btnClear.setBounds(90,220,100,30);
		lblError.setVisible(false);
	}

}
