package com.telappoint.resvdeskrestws.common.utils;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.TelAppointLogger;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;

/**
 * @author Balaji
 * 
 * 
 */
public class EmailUtils {

	public static void sendErrorEmail(String subject, String body) throws Exception {
		TelAppointLogger.logDebug("sendEmail method entered.");
		Properties mailProperties = PropertyUtils.getProperties(PropertiesConstants.EMAIL_PROP.getPropertyFileName());
		String sendMail = mailProperties == null ? null : mailProperties.getProperty("error.mail.send");
		if (mailProperties != null && sendMail != null && "yes".equalsIgnoreCase(sendMail)) {
			String userName = mailProperties.getProperty("mail.smtp.user");
			String passWord = mailProperties.getProperty("mail.smtp.password");
			String host = mailProperties.getProperty("mail.smtp.hostname");

			String fromAddress = mailProperties.getProperty("mail.fromaddress");
			String toAddress = mailProperties.getProperty("error.mail.to");
			String ccAddress = mailProperties.getProperty("error.mail.cc");
			String replyAddress = mailProperties.getProperty("mail.replayaddress");

			try {
				Session session = Session.getDefaultInstance(mailProperties, null);
				MimeMessage msg = new MimeMessage(session);
				msg.setSubject(subject);
				msg.setFrom(new InternetAddress(fromAddress));
				InternetAddress[] replyAddresses = new InternetAddress[1];
				replyAddresses[0] = new InternetAddress(replyAddress);
				msg.setReplyTo(replyAddresses);

				// This part for CC Address Details
				if (ccAddress != null && !"".equals(ccAddress) && ccAddress.length() > 0) {
					InternetAddress[] inetAddress = getInetAddress(ccAddress);
					msg.setRecipients(Message.RecipientType.CC, inetAddress);
				}
				// This part for TO Address Details
				if (toAddress != null && !"".equals(toAddress) && toAddress.length() > 0) {
					InternetAddress[] inetAddress = getInetAddress(toAddress);
					msg.setRecipients(Message.RecipientType.TO, inetAddress);
				}

				msg.setContent(body, "text/html");
				msg.saveChanges();
				Transport transport = session.getTransport("smtp");
				transport.connect(host, userName, passWord);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
			} catch (AddressException ade) {
				TelAppointLogger.logError("Error:", ade);
				throw ade;
			} catch (MessagingException me) {
				TelAppointLogger.logError("Error:", me);
				throw me;
			}
		}
	}

	private static InternetAddress[] getInetAddress(String address) throws AddressException {
		InternetAddress[] inetAddress = null;
		StringTokenizer tokens = new StringTokenizer(address, ",");
		if (tokens != null && tokens.hasMoreTokens()) {
			inetAddress = new InternetAddress[tokens.countTokens()];
			int i = 0;
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				inetAddress[i++] = new InternetAddress(token.trim());
			}
		}
		return inetAddress;
	}

}
