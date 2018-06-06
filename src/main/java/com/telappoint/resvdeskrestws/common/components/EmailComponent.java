package com.telappoint.resvdeskrestws.common.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.common.constants.EmailConstants;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.EmailRequest;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;

/**
 * 
 * @author Balaji Nandarapu
 *
 */

@Component
public class EmailComponent {
	
	@Autowired
	CacheComponent cacheComponent;

	@Async("mailExecutor")
	public void sendEmail(CustomLogger customLogger, EmailRequest emailRequest) throws Exception {
		
		String startDate = getStartDate(customLogger, emailRequest);
		String endDate = getEndDate(customLogger, emailRequest);
		customLogger.debug("DateTIme:" + emailRequest.getDateTimeFormat() + ",StartDate:" + startDate + "EndDate:" + endDate + " , Time Zone : " + emailRequest.getTimeZone());
		
		String calendarContent = null;
		if (EmailConstants.RESERVATION_CONFIRM_EMAIL.getValue().equals(emailRequest.getEmailType())) {
			customLogger.debug("Confirmation Mail:" + emailRequest.getToAddress());
			calendarContent = getRevConfirmEmailCalendarMessage(customLogger, startDate, endDate, emailRequest);
		} else if (EmailConstants.RESERVATION_CANCEL_EMAIL.getValue().equals(emailRequest.getEmailType())) {
			customLogger.info("Cancellation Mail:" + emailRequest.getToAddress());
			calendarContent = getRevCancelEmailCalendarMessage(customLogger, startDate, endDate, emailRequest);
		}
		sendEmailICS(customLogger, calendarContent, emailRequest);
	}

	public void sendEmailICS(CustomLogger customLogger, String calendarContent, EmailRequest emailRequest) throws Exception {
		//calendar part
		BodyPart calendarPart = new MimeBodyPart();
		calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=" + emailRequest.getMethod());
		Multipart multipart = new MimeMultipart("alternative");
		multipart.addBodyPart(calendarPart);
		sendBaseEmail(customLogger, emailRequest, multipart);
	}	
	
	
	private void sendBaseEmail(CustomLogger customLogger,EmailRequest emailRequest, Multipart multipart) throws Exception {
		String mailHost = PropertyUtils.getValueFromProperties("internal.mail.hostname", PropertiesConstants.EMAIL_PROP.getPropertyFileName());
		
		if(multipart == null) {
			// email body part
			multipart = new MimeMultipart("alternative");
		} 
		MimeBodyPart emailBodyPart = new MimeBodyPart();
		emailBodyPart.setContent(emailRequest.getEmailBody(), "text/html; charset=utf-8");
		multipart.addBodyPart(emailBodyPart);
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(mailHost);
		if(emailRequest.isEmailThroughInternalServer() == false) {
			sender.setHost(PropertyUtils.getValueFromProperties("mail.smtp.hostname", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
			sender.setPort(Integer.valueOf(PropertyUtils.getValueFromProperties("mail.smtp.port", PropertiesConstants.EMAIL_PROP.getPropertyFileName())));
			sender.setUsername(PropertyUtils.getValueFromProperties("mail.smtp.user", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
			sender.setPassword(PropertyUtils.getValueFromProperties("mail.smtp.password", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
			sender.setJavaMailProperties(getSMTPMailProperties());
		}
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(emailRequest.getToAddress());
		helper.setFrom(emailRequest.getFromAddress());
		helper.setSubject(emailRequest.getSubject());
		helper.setText(emailRequest.getEmailBody());
		message.setContent(multipart);
		sender.send(message);
	}
	
	private String getRevConfirmEmailCalendarMessage(CustomLogger customLogger, String startDate, String endDate, EmailRequest emailRequest) {	
		StringBuilder calendarContent = new StringBuilder("BEGIN:VCALENDAR").append("\n");
		calendarContent.append("METHOD:").append(emailRequest.getMethod()).append("\n");
		calendarContent.append("PRODID: BCP - Meeting").append("\n");
		calendarContent.append("VERSION:2.0").append("\n");
		calendarContent.append("BEGIN:VEVENT").append("\n");
		calendarContent.append("DTSTART:").append(startDate).append("\n");
		calendarContent.append("DTEND:").append(endDate).append("\n");	
		calendarContent.append("DTSTAMP:").append(startDate).append("\n" );
		calendarContent.append("ORGANIZER:MAILTO:").append(emailRequest.getFromAddress()).append("\n");
		calendarContent.append("UID:").append(emailRequest.getScheduleId()).append("\n");			
		calendarContent.append("CREATED:").append(startDate).append("\n");
		calendarContent.append("DESCRIPTION:").append(emailRequest.getEmailBody()).append("\n");
		calendarContent.append("LOCATION:").append(emailRequest.getEventName()).append(",");
		calendarContent.append(emailRequest.getClientName()).append("\n");
		calendarContent.append("SUMMARY:").append(emailRequest.getSubject()).append("\n");
		calendarContent.append("SEQUENC.E:0").append("\n");
		calendarContent.append("PRIORITY:5").append("\n");
		calendarContent.append("CLASS:PUBLIC").append("\n");
		calendarContent.append("STATUS:").append(emailRequest.getStatus()).append("\n");
		calendarContent.append("TRANSP:OPAQUE").append("\n");
		calendarContent.append("END:VEVENT").append("\n");
		calendarContent.append("END:VCALENDAR");
		return calendarContent.toString();
	}
	
	private String getRevCancelEmailCalendarMessage(CustomLogger customLogger, String startDate, String endDate, EmailRequest emailRequest) {
		StringBuilder calendarContent = new StringBuilder("BEGIN:VCALENDAR").append("\n");
		calendarContent.append("PRODID: BCP - Meeting").append("\n");
		calendarContent.append("VERSION:2.0").append("\n");
		calendarContent.append("CALSCALE:GREGORIAN").append("\n");
		calendarContent.append("METHOD:CANCEL").append("\n");
		calendarContent.append("BEGIN:VEVENT").append("\n");
		calendarContent.append("DTSTART:").append(startDate).append("\n");
		calendarContent.append("DTEND:").append(endDate).append("\n");
		calendarContent.append("DTSTAMP:").append(startDate).append("\n");
		calendarContent.append("ORGANIZER:MAILTO:").append(emailRequest.getFromAddress()).append("\n");
		calendarContent.append("UID:").append(emailRequest.getFromAddress()).append("\n");
		calendarContent.append("CREATED:").append(startDate).append("\n");
		calendarContent.append("DESCRIPTION:").append(emailRequest.getEmailBody()).append("\n");
		calendarContent.append("LOCATION:").append(emailRequest.getEventName()).append(",");
		calendarContent.append(emailRequest.getClientName()).append("\n");
		calendarContent.append("SEQUENCE:0").append("\n");
		calendarContent.append("STATUS:").append(emailRequest.getStatus()).append("\n");
		calendarContent.append("SUMMARY:").append(emailRequest.getSubject()).append("\n");
		calendarContent.append("TRANSP:OPAQUE").append("\n");
		calendarContent.append("END:VEVENT").append("\n");
		calendarContent.append("END:VCALENDAR");
		return calendarContent.toString();
	}
	
	
	private String getEndDate(CustomLogger customLogger, EmailRequest emailRequest) throws Exception {
		DateFormat sdfFormator = getSimpleDateFormat(customLogger, emailRequest.getLangCode());
		DateFormat sdfParsor = getSimpleDateParser(customLogger, emailRequest);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(emailRequest.getTimeZone()));
		String endDate = "";
		if (emailRequest.getDateTime() != null) {
			cal.setTime(sdfParsor.parse(emailRequest.getDateTime()));
			cal.add(Calendar.MINUTE, emailRequest.getEventDuration());
			endDate = sdfFormator.format(cal.getTime());
		} else {
			endDate = "";
		}
		return endDate;
	}

	private String getStartDate(CustomLogger customLogger, EmailRequest emailRequest) throws Exception {
		DateFormat sdfFormator = getSimpleDateFormat(customLogger, emailRequest.getLangCode());
		DateFormat sdfParsor = getSimpleDateParser(customLogger, emailRequest);
		String startDate = "";
		try {
			if (emailRequest.getDateTime() != null) {
				Date dateStr = sdfParsor.parse(emailRequest.getDateTime());
				startDate = sdfFormator.format(dateStr);
			} else {
				startDate = emailRequest.getDateTime();
			}
			return startDate;
		} catch (Exception e) {
			customLogger.error("Date format or Parse is failed:");
			throw new Exception("Date format or Parse is failed");
		}
	}

	private DateFormat getSimpleDateFormat(CustomLogger customLogger, String langCode) {
		SimpleDateFormat sdfFormator = null;
		if ("us-es".equals(langCode)) {
			Locale locale = new Locale("es", "ES");
			sdfFormator = new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'", locale);
			sdfFormator.setTimeZone(TimeZone.getTimeZone("UTC"));
		} else {
			sdfFormator = new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'");
			sdfFormator.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		return sdfFormator;
	}

	private DateFormat getSimpleDateParser(CustomLogger customLogger, EmailRequest emailRequest) {
		SimpleDateFormat sdfParsor = null;
		if ("us-es".equals(emailRequest.getLangCode())) {
			Locale locale = new Locale("es", "ES");
			sdfParsor = new SimpleDateFormat(emailRequest.getDateTimeFormat(), locale);
			sdfParsor.setTimeZone(TimeZone.getTimeZone(emailRequest.getTimeZone()));
		} else {
			sdfParsor = new SimpleDateFormat(emailRequest.getDateTimeFormat());
			sdfParsor.setTimeZone(TimeZone.getTimeZone(emailRequest.getTimeZone()));
		}
		return sdfParsor;
	}
	
	private Properties getSMTPMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.debug", "false");
		return properties;
	}
	
	public void prepareEmailRequest(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String token, String transId, String emailSubjectKey,
			String emailBodyKey, ClientDeploymentConfig cdConfig, ResvSysConfig resvConfig, EmailRequest emailRequest, boolean cache) throws Exception {
		try {
			Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

			String emailSubjectTemplate = (String) emailTemplateMap.get(emailSubjectKey);
			String emailBodyTemplate = (String) emailTemplateMap.get(emailBodyKey);

			String emailSubject = getEmailSubject(emailSubjectTemplate, aliasMap, emailRequest);
			String emailBody = getEmailBody(customLogger, emailBodyTemplate, aliasMap, emailRequest);
			emailRequest.setSubject(emailSubject);
			emailRequest.setEmailBody(emailBody);
			emailRequest.setTimeZone(cdConfig.getTimeZone());
			emailRequest.setDateTimeFormat("MM/dd/yyyy hh:mm a");

			emailRequest.setMethod("REQUEST");
			emailRequest.setStatus("CONFIRMED");
			boolean isEmailThroughInternalServer = "true".equals(PropertyUtils.getValueFromProperties("EMAIL_THROUGH_INTERNAL_SERVER", PropertiesConstants.EMAIL_PROP.getPropertyFileName())) ? true
					: false;
			if (isEmailThroughInternalServer) {
				emailRequest.setFromAddress(PropertyUtils.getValueFromProperties("internal.mail.fromaddress", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
				emailRequest.setReplyAddress(PropertyUtils.getValueFromProperties("internal.mail.replyaddress", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
			} else {
				emailRequest.setFromAddress(PropertyUtils.getValueFromProperties("mail.fromaddress", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
				emailRequest.setReplyAddress(PropertyUtils.getValueFromProperties("mail.replyaddress", PropertiesConstants.EMAIL_PROP.getPropertyFileName()));
			}
			emailRequest.setEmailThroughInternalServer(isEmailThroughInternalServer);
			emailRequest.setLangCode(langCode);
			sendEmail(customLogger, emailRequest);
		} catch (Exception e) {
			customLogger.error("Error in getEmailRequest method. ", e);
			e.printStackTrace();
		}
	}

	private String getEmailBody(CustomLogger customLogger, String emailBodyTemplate, Map<String, String> aliasMap, EmailRequest emailRequest) {
		try {
			emailBodyTemplate = emailBodyTemplate.replace("%COMPANY%", emailRequest.getCompanyName() != null ? emailRequest.getCompanyName() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%PROCEDURE%", emailRequest.getProcedureName() != null ? emailRequest.getProcedureName() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%LOCATION%", emailRequest.getLocationName() != null ? emailRequest.getLocationName() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%DEPARTMENT%", emailRequest.getDepartmentName() != null ? emailRequest.getDepartmentName() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%EVENT%", emailRequest.getEventName() != null ? emailRequest.getEventName() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%SEAT%", emailRequest.getSeatNumber() != null ? emailRequest.getSeatNumber() : "");
			String firstName = emailRequest.getFirstName() != null ? emailRequest.getFirstName() : "";
			String lastName = emailRequest.getLastName() != null ? emailRequest.getLastName() : "";
			emailBodyTemplate = emailBodyTemplate.replace("%CUST_FIRSTNAME%", firstName);
			emailBodyTemplate = emailBodyTemplate.replace("%CUST_LASTNAME%", lastName);
			emailBodyTemplate = emailBodyTemplate.replace("%CUSTOMERNAME%", firstName + " " + lastName);
			emailBodyTemplate = emailBodyTemplate.replace("%RESVDATE%", emailRequest.getDate());
			emailBodyTemplate = emailBodyTemplate.replace("%RESVTIME%", emailRequest.getTime());
			emailBodyTemplate = emailBodyTemplate.replace("%CONFNUM%", emailRequest.getConfNumber());
			emailBodyTemplate = emailBodyTemplate.replace("%CLIENTNAME%", emailRequest.getClientName() != null ? emailRequest.getClientName() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%CLIENTADDRESS%", emailRequest.getClientAddress() != null ? emailRequest.getClientAddress() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%CLIENTWEBSITE%", emailRequest.getClientWebsite() != null ? emailRequest.getClientWebsite() : "");
			emailBodyTemplate = emailBodyTemplate.replace("%CLIENTRESVLINK%", emailRequest.getClientResvLink() != null ? emailRequest.getClientResvLink() : "");
		} catch (Exception e) {
			customLogger.error("Error in getEmailBody method", e);
		}
		return emailBodyTemplate;
	}

	private String getEmailSubject(String emailSubjectTemplate, Map<String, String> aliasMap, EmailRequest emailRequest) {
		String firstName = emailRequest.getFirstName() != null ? emailRequest.getFirstName() : "";
		String lastName = emailRequest.getLastName() != null ? emailRequest.getLastName() : "";

		emailSubjectTemplate = emailSubjectTemplate.replace("%CUST_FIRSTNAME%", firstName);
		emailSubjectTemplate = emailSubjectTemplate.replace("%CUST_FIRSTNAME%", lastName);
		return emailSubjectTemplate;
	}
}
