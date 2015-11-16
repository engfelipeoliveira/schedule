package br.com.system.schedule.service;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class EmailService {

	@Inject
	private Logger logger;

	@Resource(mappedName = "java:jboss/mail/Default")
	Session session;

	public EmailService() {
	}

	@Asynchronous
	public void sendEmail(String to, String from, String subject, String content) {

		logger.info("Sending Email from " + from + " to " + to + " : "+ subject);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);

			logger.info("Email was sent");

		} catch (MessagingException e) {
			logger.severe("Error while sending email : " + e.getMessage());
		}
	}

}