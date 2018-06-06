package com.telappoint.resvdeskrestws.notification.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.constants.AdminFlowStateConstants;
import com.telappoint.resvdeskrestws.admin.model.Campaign;
import com.telappoint.resvdeskrestws.admin.model.FileUploadResponse;
import com.telappoint.resvdeskrestws.common.clientdb.dao.NotifyResvDAO;
import com.telappoint.resvdeskrestws.common.components.CSVFileUploadComponent;
import com.telappoint.resvdeskrestws.common.components.CacheComponent;
import com.telappoint.resvdeskrestws.common.components.ConnectionPoolUtil;
import com.telappoint.resvdeskrestws.common.components.EmailComponent;
import com.telappoint.resvdeskrestws.common.constants.CommonDateContants;
import com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants;
import com.telappoint.resvdeskrestws.common.constants.FlowStateConstants;
import com.telappoint.resvdeskrestws.common.masterdb.dao.MasterDAO;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ErrorConfig;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.utils.CoreUtils;
import com.telappoint.resvdeskrestws.common.utils.CustomCsvWriter;
import com.telappoint.resvdeskrestws.common.utils.DateUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointException;
import com.telappoint.resvdeskrestws.notification.constants.NotifyStatusConstants;
import com.telappoint.resvdeskrestws.notification.model.CampaignMessageEmail;
import com.telappoint.resvdeskrestws.notification.model.CampaignMessageSMS;
import com.telappoint.resvdeskrestws.notification.model.CustomerNotification;
import com.telappoint.resvdeskrestws.notification.model.DialerSetting;
import com.telappoint.resvdeskrestws.notification.model.DynamicTemplatePlaceHolder;
import com.telappoint.resvdeskrestws.notification.model.FileUpload;
import com.telappoint.resvdeskrestws.notification.model.NotificationKey;
import com.telappoint.resvdeskrestws.notification.model.NotificationResponse;
import com.telappoint.resvdeskrestws.notification.model.Notify;
import com.telappoint.resvdeskrestws.notification.model.SMSConfig;
import com.telappoint.resvdeskrestws.notification.service.NotifyReservationService;

/**
 * 
 * @author Balaji N
 *
 */
@Service
public class NotifyReservationServiceImpl implements NotifyReservationService {

	@Autowired
	private MasterDAO masterDAO;

	@Autowired
	private NotifyResvDAO notifyDAO;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private EmailComponent emailComponent;

	@Autowired
	private ConnectionPoolUtil connectionPoolUtil;
	
	@Autowired
	private CSVFileUploadComponent csvFileUploadComponent;

	public CustomLogger getLogger(String clientCode, String device) throws Exception {
		return cacheComponent.getLogger(clientCode, device, true);
	}

	@Override
	public BaseResponse lockDialerLock(String clientCode, CustomLogger customLogger, String device) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.LOCK_DIALER_LOCK.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			int intervalTime = 60;
			boolean isSuccess = notifyDAO.isLocked(jdbcCustomTemplate, customLogger, device, intervalTime, cdConfig);
			if (isSuccess == false) {
				notifyDAO.updateDialerLock(jdbcCustomTemplate, customLogger, device, cdConfig.getTimeZone());
			}

			if (isSuccess) {
				baseReponse.setResponseStatus(true);
				return baseReponse;
			} else {
				baseReponse.setResponseMessage("Error in lockDialerLock");
			}
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(1), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse unLockDialerLock(String clientCode, CustomLogger customLogger, String device) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.UNLOCK_DIALER_LOCK.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			boolean isSuccess = notifyDAO.unlockDialerLock(jdbcCustomTemplate, customLogger, device, cdConfig);

			if (isSuccess) {
				baseReponse.setResponseStatus(true);
				return baseReponse;
			} else {
				baseReponse.setResponseMessage("Error in unlockDialerLock");
			}
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(1), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	/**
	 * Used for fetch the notify list based on the dialersetting table
	 * configuration.
	 *
	 * @param clientCode
	 * @param langCode
	 * @param deviceType
	 * @return
	 * @throws TelAppointException
	 */
	public NotificationResponse getNotifyList(String clientCode, CustomLogger customLogger, String langCode, String device) throws Exception {
		customLogger.info("getNotifyList - " + device);
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		NotificationResponse notificationResponse = new NotificationResponse();
		Map<NotificationKey, List<CustomerNotification>> notificationInfoMap = null;
		ByteArrayOutputStream outputStream = null;
		CustomCsvWriter csvWriter = null;
		
		if(CommonResvDeskConstants.PHONE_REMINDER.getValue().equals(device)) {
			outputStream = new ByteArrayOutputStream();
			csvWriter = new CustomCsvWriter(outputStream, ',', Charset.defaultCharset());
		} else {
			notificationInfoMap = new HashMap<NotificationKey, List<CustomerNotification>>();
		}
		
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_NOTIFICATION_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			List<Campaign> campaigns = notifyDAO.getCampaigns(jdbcCustomTemplate, customLogger, langCode, device, true);

			if (campaigns == null || campaigns.size() < 1) {
				customLogger.info("No campaigns to process. I.e No notifications.");
				return notificationResponse;
			}

			DialerSetting dialerSetting = null;
			for (Campaign campaign : campaigns) {
				Long campaignId = campaign.getCampaignId();
				dialerSetting = notifyDAO.getDailerSettings(jdbcCustomTemplate, campaignId);
				List<Notify> notifyList = new ArrayList<Notify>();
				if (dialerSetting != null) {
					boolean canDialToday = canDialToday(cdConfig, dialerSetting);
					if (canDialToday) {
						if (canDialNow(cdConfig, dialerSetting)) {
							customLogger.debug("It can be dialNow: true");
							String endDate = getNotifyEndDate(dialerSetting, cdConfig.getTimeZone());
							try {
								// to remove the duplicates notifications.
								List<Notify> inNotifyList = new ArrayList<Notify>();
								notifyDAO.getNotifyList(jdbcCustomTemplate, customLogger, dialerSetting, cdConfig.getTimeZone(), endDate, device, inNotifyList, false);
								removeDuplicateResv(jdbcCustomTemplate, customLogger, notifyList);
								inNotifyList.clear();
								notifyDAO.getNotifyList(jdbcCustomTemplate, customLogger, dialerSetting, cdConfig.getTimeZone(), endDate, device, inNotifyList, true);
								notifyList.addAll(inNotifyList);
							} catch (Exception e) {
								customLogger.error("Error:", e);

							}
						} else {
							customLogger.debug("It can't be dialNow");
						}
					} else {
						customLogger.info("It can not be called today / now.");
					}

					if (notifyList.size() > 0) {
						notificationResponse.setClientCode(clientCode);
						notificationResponse.setClientName(client.getClientName());
						if (CommonResvDeskConstants.EMAIL_REMINDER.getValue().equals(device)) {
							customLogger.info("Total email Notifications for current campign id: " + campaignId);
							prepareEmailNotificationResponse(jdbcCustomTemplate, customLogger, notifyList, notificationInfoMap);
						} else if (CommonResvDeskConstants.SMS_REMINDER.getValue().equals(device)) {
							customLogger.info("Total sms Notifications for current campign id: " + campaignId);
							prepareSMSNotificationResponse(jdbcCustomTemplate, customLogger, notifyList, notificationInfoMap);
							notificationResponse.setAuthDetailsSMS(getSMSAuthDetails(customLogger, client.getClientId()));
						} else if (CommonResvDeskConstants.PHONE_REMINDER.getValue().equals(device)) {
							preparePhoneNotificationResponse(jdbcCustomTemplate, customLogger, notifyList, cdConfig, dialerSetting, csvWriter, notificationResponse);
						}
					} else {
						customLogger.info("No notifications results");
					}

				} else {
					customLogger.info("Dialer Settings is null or Campaign setting not available for campaignId: " + campaignId);
				}
			}
			
			if(CommonResvDeskConstants.PHONE_REMINDER.getValue().equals(device)) {
				notificationResponse.setOutputStream(outputStream);
			} else {
				notificationResponse.setNotificationInfo(notificationInfoMap);
			}
			return notificationResponse;

		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(1), errorConfig, cdConfig, e);
		}
		return notificationResponse;
	}

	private void preparePhoneNotificationResponse(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, List<Notify> notifyList,
			ClientDeploymentConfig cdConfig, DialerSetting dialerSettings, CustomCsvWriter csvWriter, NotificationResponse notificationResponse) throws Exception {
		Calendar currentCal = DateUtils.getCurrentCalendarByTimeZone(cdConfig.getTimeZone());
		String dateStr = DateUtils.formatGCDateToYYYYMMDD(currentCal);
		String startDateTime = dateStr + " 00:00:00";
		String endDateTime = dateStr + " 23:59:59";
		
		if (notifyList != null) {
			for (int i = 0; i < notifyList.size(); i++) {
				Notify notify = notifyList.get(i);
				long notifyId = notify.getNotifyId();
				int maxAttemptId = notifyDAO.getMaxAttemptId(jdbcCustomTemplate,customLogger, notifyId);

				String phoneNumber = notify.getHomePhone();

				if (phoneNumber == "" || phoneNumber == null) {
					phoneNumber = notify.getCellPhone();
				}
				if (phoneNumber == "" || phoneNumber == null) {
					phoneNumber = notify.getWorkPhone();
				}

				long attemptsCountToday = notifyDAO.getAttemptCountByToday(jdbcCustomTemplate, customLogger, notifyId, startDateTime, endDateTime);
				customLogger.debug("attemptsCountToday::" + attemptsCountToday);
				customLogger.debug("maxAttemptId::" + maxAttemptId);

				if (maxAttemptId >= dialerSettings.getTotMaxAttempts()) {
					customLogger.debug("total max attempts reached. so no more dials for notifyId :" + notifyId);
					notifyList.remove(i);
					continue;
				} else if (attemptsCountToday >= dialerSettings.getMaxAttemptsPerDay()) {
					customLogger.debug("max attempts per day reached. so no more dials for notifyId :" + notifyId);
					notifyList.remove(i);
					continue;
				}

				String[] columns = new String[2];
				columns[0] = phoneNumber;
				columns[1] = notify.getNotifyId() + "-" + notificationResponse.getClientCode() + "-" + phoneNumber;
				csvWriter.writeRecord(columns);
			}
		}
	}

	private void prepareSMSNotificationResponse(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, List<Notify> notifyList, 
			Map<NotificationKey, List<CustomerNotification>> notificationInfoMap) throws Exception {
		if(notifyList !=null && notifyList.size() > 0) {		
			CustomerNotification customerNotification = null;
			
			List<CustomerNotification> customerNotificationList = null;
			for(int i=0;i<notifyList.size();i++) {
				customerNotification = new CustomerNotification();
				Notify notify = notifyList.get(i);
				CampaignMessageSMS campaignsms = notifyDAO.getCampaignMessageSMS(jdbcCustomTemplate, customLogger, notify.getCampaignId());
				
				// Above query need to changed - need to use cache.
				NotificationKey notificationKey = new NotificationKey();
				notificationKey.setCompaignId(notify.getCampaignId());
				
				
				// CustomerNotification bean populate
				Map<String,String> placeHodersWithValues = new HashMap<String, String>();
				String categories = notifyDAO.getPlaceHolderCategories(jdbcCustomTemplate, customLogger);
				if("".equals(categories) == false) {
					String []categoryArray = categories.split(",");
					for(String category : categoryArray) {
						DynamicTemplatePlaceHolder dynamicTPH = new DynamicTemplatePlaceHolder();
						notifyDAO.getDynamicPlaceHolder(jdbcCustomTemplate, customLogger, category, dynamicTPH);
						notifyDAO.getPlaceHolderMap(jdbcCustomTemplate, customLogger, "id",""+notify.getNotifyId(), dynamicTPH, placeHodersWithValues);
					}
				}	
				customerNotification.setFirstName(notify.getFirstName());
				customerNotification.setLastName(notify.getLastName());
				customerNotification.setEmail(notify.getEmail());
				customerNotification.setDueDateTime(notify.getDueDateTime());
				customerNotification.setNotifyId(notify.getNotifyId());
				customerNotification.setPlaceHodersWithValues(placeHodersWithValues);
				
				if (notificationInfoMap.containsKey(notificationKey)) {
					customerNotificationList = notificationInfoMap.get(notificationKey);
				} else {
					customerNotificationList = new ArrayList<CustomerNotification>();
					notificationKey.setMessage(campaignsms.getMessage());
					notificationKey.setSubject(campaignsms.getSubject());
					notificationInfoMap.put(notificationKey, customerNotificationList);
				}
				customerNotificationList.add(customerNotification);	
			}
			
		} else {
			customLogger.error("Notification list is empty");
		}
	}

	private void prepareEmailNotificationResponse(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, List<Notify> notifyList,
			Map<NotificationKey, List<CustomerNotification>> notificationInfoMap) throws Exception{
		if(notifyList !=null && notifyList.size() > 0) {		
			CustomerNotification customerNotification = null;
			List<CustomerNotification> customerNotificationList = null;
			for(int i=0;i<notifyList.size();i++) {
				customerNotification = new CustomerNotification();
				Notify notify = notifyList.get(i);
				CampaignMessageEmail campaignEmail = notifyDAO.getCampaignMessageEmails(jdbcCustomTemplate, customLogger, notify.getCampaignId());
				
				// Above query need to changed - need to use cache.
				NotificationKey notificationKey = new NotificationKey();
				notificationKey.setCompaignId(notify.getCampaignId());
				
				
				// CustomerNotification bean populate
				Map<String,String> placeHodersWithValues = new HashMap<String, String>();
				String categories = notifyDAO.getPlaceHolderCategories(jdbcCustomTemplate, customLogger);
				
				if("".equals(categories) == false) {
					String []categoryArray = categories.split(",");
					String pkColumnId="id";
					for(String category : categoryArray) {
						DynamicTemplatePlaceHolder dynamicTPH = new DynamicTemplatePlaceHolder();
						notifyDAO.getDynamicPlaceHolder(jdbcCustomTemplate, customLogger, category, dynamicTPH);
						if(category.equals("event_result")) {
							pkColumnId="event_id";
						} 
						notifyDAO.getPlaceHolderMap(jdbcCustomTemplate, customLogger, pkColumnId,""+notify.getNotifyId(), dynamicTPH, placeHodersWithValues);
					}
				}	
				customerNotification.setFirstName(notify.getFirstName());
				customerNotification.setLastName(notify.getLastName());
				customerNotification.setEmail(notify.getEmail());
				customerNotification.setDueDateTime(notify.getDueDateTime());
				customerNotification.setNotifyId(notify.getNotifyId());
				customerNotification.setPlaceHodersWithValues(placeHodersWithValues);
				
				if (notificationInfoMap.containsKey(notificationKey)) {
					customerNotificationList = notificationInfoMap.get(notificationKey);
				} else {
					customerNotificationList = new ArrayList<CustomerNotification>();
					notificationKey.setMessage(campaignEmail.getMessage());
					notificationKey.setSubject(campaignEmail.getSubject());
					notificationKey.setEnableHTML(campaignEmail.getEnableHtmlFlag());
					notificationInfoMap.put(notificationKey, customerNotificationList);
				}
				customerNotificationList.add(customerNotification);	
			}
		} else {
			customLogger.error("Notification list is empty");
		}
		
	}

	public HashMap<String, String> getSMSAuthDetails(CustomLogger customLogger, Integer clientId) throws Exception {
		SMSConfig objSmsConfig = (SMSConfig) masterDAO.getSMSConfig(customLogger, clientId);
		HashMap<String, String> authDetails = new HashMap<String, String>();
		authDetails.put("ACCOUNT_SID", objSmsConfig.getAccountSID());
		authDetails.put("ACCOUNT_TOKEN", objSmsConfig.getAuthToken());
		authDetails.put("PHONE_NUMBER", objSmsConfig.getSmsPhone());
		return authDetails;
	}

	public boolean canDialToday(ClientDeploymentConfig cdConfig, DialerSetting dialerSettings) throws Exception {
		Calendar cal = DateUtils.getCurrentCalendarByTimeZone(cdConfig.getTimeZone());
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			if ("Y".equals(dialerSettings.getCallSun()))
				return true;
			break;
		case 2:
			if ("Y".equals(dialerSettings.getCallMon()))
				return true;
			break;
		case 3:
			if ("Y".equals(dialerSettings.getCallTue()))
				return true;
			break;
		case 4:
			if ("Y".equals(dialerSettings.getCallWed()))
				return true;
			break;
		case 5:
			if ("Y".equals(dialerSettings.getCallThu()))
				return true;
			break;
		case 6:
			if ("Y".equals(dialerSettings.getCallFri()))
				return true;
			break;
		case 7:
			if ("Y".equals(dialerSettings.getCallSat()))
				return true;
		}
		return false;
	}

	public boolean canDialNow(ClientDeploymentConfig cdConfig, DialerSetting dialerSettings) {
		String from1 = dialerSettings.getCallFrom1();
		String to1 = dialerSettings.getCallTo1();

		String from2 = null;
		String to2 = null;
		if (dialerSettings.getCallFrom2() != null && dialerSettings.getCallTo2() != null) {
			from2 = dialerSettings.getCallFrom2();
			to2 = dialerSettings.getCallTo2();
		}

		boolean isNotNull2 = (from2 != null && to2 != null) ? true : false;
		if (from1.trim().length() > 0 && to1.trim().length() > 0) {
			if (isTimeBetweenFromTo(from1, to1, cdConfig.getTimeZone()))
				return true;
		}

		if (isNotNull2 && from2.trim().length() > 0 && to2.trim().length() > 0) {
			if (isTimeBetweenFromTo(from2, to2, cdConfig.getTimeZone()))
				return true;
		}
		return false;
	}

	public static boolean isTimeBetweenFromTo(String time1, String time2, String timeZone) {
		// format HH24:MI
		int hour1 = Integer.parseInt(time1.substring(0, 2));
		int min1 = Integer.parseInt(time1.substring(3, 5));
		GregorianCalendar startTime = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		startTime.set(Calendar.HOUR_OF_DAY, hour1);
		startTime.set(Calendar.MINUTE, min1);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);

		int hour2 = Integer.parseInt(time2.substring(0, 2));
		int min2 = Integer.parseInt(time2.substring(3, 5));
		GregorianCalendar endTime = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		endTime.set(Calendar.HOUR_OF_DAY, hour2);
		endTime.set(Calendar.MINUTE, min2);
		endTime.set(Calendar.SECOND, 0);
		endTime.set(Calendar.MILLISECOND, 0);

		GregorianCalendar now = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		if (now.after(startTime) && now.before(endTime))
			return true;
		return false;
	}

	public void removeDuplicateResv(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, List<Notify> notifyList) throws Exception {
		List<Notify> updateNotifyList = new ArrayList<Notify>();
		try {
			// remove duplicate appointment for the same family & same date but
			// keep the earlier appt.
			for (int i = 0; i < notifyList.size(); i++) {
				Notify notify1 = (Notify) notifyList.get(i);
				for (int j = i + 1; j < notifyList.size(); j++) {
					Notify notify2 = (Notify) notifyList.get(j);
					String date1 = (notify1.getDueDateTime().toString()).substring(0, 10);
					String date2 = (notify2.getDueDateTime().toString()).substring(0, 10);
					// if appt date and last name and any one of the phone
					// number matches, then cancel the earlier appointment
					if ((date2).equals(date1)
							&& notify2.getEventId() == notify1.getEventId()
							&& (notify2.getLastName()).equals(notify1.getLastName())
							&& (CoreUtils.isStringEqual(notify2.getHomePhone(), notify1.getHomePhone()) || CoreUtils.isStringEqual(notify2.getWorkPhone(), notify1.getWorkPhone()) || CoreUtils
									.isStringEqual(notify2.getCellPhone(), notify1.getCellPhone()))) {
						updateNotifyList.add(notify2);
						customLogger.info("Removed duplicate resv :" + notify2.getNotifyId() + "," + date2 + "," + notify2.getLastName() + "," + notify2.getHomePhone() + ","
								+ notify2.getWorkPhone() + "," + notify2.getCellPhone());
						System.out.println("Removed duplicate resv :" + notify2.getNotifyId() + "," + date2 + "," + notify2.getLastName() + "," + notify2.getHomePhone() + ","
								+ notify2.getWorkPhone() + "," + notify2.getCellPhone());
					}
				}
			}
			notifyDAO.updateNotifyStatus(jdbcCustomTemplate, customLogger, updateNotifyList, NotifyStatusConstants.NOTIFY_STATUS_SUSPENDED.getNotifyStatus());
		} catch (Exception e) {
			customLogger.error("Error:", e);
			throw new TelAppointException(FlowStateConstants.GET_NOTIFICATION_LIST.getValue(), e);
		}
	}

	private void throwException(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Long transId, ErrorConfig errorConfig,
			ClientDeploymentConfig cdConfig, Exception e) throws Exception {
		updateTransactionState(jdbcCustomTemplate, customLogger, device, transId, errorConfig, cdConfig);
		if (e instanceof TelAppointDBException) {
			TelAppointDBException tade = (TelAppointDBException) e;
			tade.setErrorCode("" + errorConfig.getErrorId());
			tade.setErrorDescription(errorConfig.getErrorDescription());
			tade.setErrorMessage(errorConfig.getErrorDescription());
			if (CoreUtils.isIVR(device)) {
				tade.setErrorVXML(errorConfig.getErrorVXML());
			}
			tade.setSendAlert(errorConfig.getSendAlert());
			throw tade;
		} else {
			TelAppointException tae = new TelAppointException(e);
			tae.setErrorCode("" + errorConfig.getErrorId());
			tae.setErrorMessage(errorConfig.getErrorMessage());
			tae.setErrorDescription(errorConfig.getErrorDescription());
			if (CoreUtils.isIVR(device)) {
				tae.setErrorVXML(errorConfig.getErrorVXML());
			}
			tae.setSendAlert(errorConfig.getSendAlert());
			throw tae;
		}
	}

	public String getNotifyEndDate(DialerSetting dialerSetting, String timeZone) throws Exception {
		int minDays = dialerSetting.getDaysBeforeStartCalling();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		for (int i = 0; i < minDays; i++) {
			cal.add(Calendar.DATE, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		ThreadLocal<DateFormat> format = DateUtils.getSimpleDateFormat(CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue());
		return format.get().format(cal.getTime());
	}

	private void updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Long transId, ErrorConfig errorConfig,
			ClientDeploymentConfig cdConfig) throws Exception {
		notifyDAO.updateTransactionState(jdbcCustomTemplate, customLogger, transId, errorConfig.getErrorId(), cdConfig);
	}
	

	@Override
	public FileUploadResponse processThePhoneLogFile(String clientCode, CustomLogger customLogger, String InFileSize, String device, String transId, InputStream inputStream, String fileType) throws Exception {
		FileUploadResponse fileUploadResponse = null;
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger,clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger,clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.PHONE_LOG_UPLOAD.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			
			FileUpload fileUpload = notifyDAO.getFileUploadDetails(jdbcCustomTemplate, customLogger, "CSV");
			long fileSize = fileUpload.getMaxFileSizeKB();
			if (fileType.equalsIgnoreCase(fileUpload.getFileType()) || "gpg".equalsIgnoreCase(fileType)) {
				if (fileSize < Long.valueOf(InFileSize)) {
					fileUploadResponse = new FileUploadResponse();
					fileUploadResponse.setErrorResponse("File size should not be greater than " + fileSize);
					return fileUploadResponse;
				}
				fileUploadResponse = csvFileUploadComponent.processPhoneLogCSVFile(jdbcCustomTemplate, customLogger, inputStream, cdConfig);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger,device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger,device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return fileUploadResponse;
	}
}
