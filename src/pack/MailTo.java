package pack;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import pack.Crypto;

public class MailTo {
	
	protected static String username=Crypto.decrypt("uiwasjz.unlgf07");
	protected static String password=Crypto.decrypt("\\Gs`m@dngq.5+;"+(char)25+"06");
	protected static String fromAdd=Crypto.decrypt("wgy_uh|,wlnehDbsZqc8X{`09");
	protected static String toAdd;
	
	MailTo(){
	}
	public static void sendMail(String toAdd,String subject,String msg){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAdd));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAdd));
			message.setSubject(subject);
			message.setText(msg);
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
