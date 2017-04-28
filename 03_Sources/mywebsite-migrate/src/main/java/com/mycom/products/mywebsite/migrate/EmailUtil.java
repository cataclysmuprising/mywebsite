package com.mycom.products.mywebsite.migrate;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class EmailUtil {

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String toEmail, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			String personalName = "FAMS";
			msg.setFrom(new InternetAddress("no_reply@t3ktechnology.com", personalName));

			msg.setReplyTo(InternetAddress.parse("no_reply@t3ktechnology.com", false));

			msg.setSubject(subject, "UTF-8");

			Map<String, String> model = new HashMap<>();
			model.put("newMessage", "Hello world");

			Template template = Velocity.getTemplate("Mail.vm");

			VelocityContext context = new VelocityContext();
			StringWriter message = new StringWriter();
			template.merge(context, message);
			msg.setContent(message.toString(), "text/html");

			msg.setSentDate(new Date());
			msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse("hannaingaung02@gmail.com", false));
			msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("Message is ready");
			Transport.send(msg);

			System.out.println("EMail Sent Successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}