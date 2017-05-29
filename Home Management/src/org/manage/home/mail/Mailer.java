package org.manage.home.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	private  Message message;

	public Mailer(Session session, int command, String videoUrl, String imageUrl, String fileName, int quality) {
		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Messages.mailer_from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Messages.mailer_to));
			message.setSubject("command");
			message.setText("{ \"command\": \"" + command + "\" ," + "\"V\":\"" + videoUrl + "\","
					+ "\"P\":\"https://i.ytimg.com/vi/" + imageUrl + "/hqdefault.jpg\" ," + "\"F\":\"" + fileName
					+ "\",\"Q\":\"" + quality + "\"}");
			message.saveChanges();
			
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendMail() {

		try {

			Transport.send(message);

			System.out.println("Sent");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
