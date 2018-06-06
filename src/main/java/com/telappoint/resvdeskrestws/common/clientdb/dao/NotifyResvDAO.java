package com.telappoint.resvdeskrestws.common.clientdb.dao;

import java.util.List;
import java.util.Map;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.Campaign;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.notification.model.CampaignMessageEmail;
import com.telappoint.resvdeskrestws.notification.model.CampaignMessagePhone;
import com.telappoint.resvdeskrestws.notification.model.CampaignMessageSMS;
import com.telappoint.resvdeskrestws.notification.model.DialerSetting;
import com.telappoint.resvdeskrestws.notification.model.DynamicTemplatePlaceHolder;
import com.telappoint.resvdeskrestws.notification.model.FileUpload;
import com.telappoint.resvdeskrestws.notification.model.Notify;
import com.telappoint.resvdeskrestws.notification.model.NotifyPhoneStatus;
import com.telappoint.resvdeskrestws.notification.model.OutboundPhoneLogs;

/**
 * 
 * @author Balaji
 *
 */
public interface NotifyResvDAO {

	public boolean unlockDialerLock(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, ClientDeploymentConfig cdConfig) throws Exception;

	public boolean updateDialerLock(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String timeZone) throws Exception;

	public boolean isLocked(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, int timeInterval, ClientDeploymentConfig cdConfig);

	public boolean updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long transId, Integer state, ClientDeploymentConfig cdConfig)
			throws Exception;

	public List<Campaign> getCampaigns(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData) throws Exception;

	public DialerSetting getDailerSettings(JdbcCustomTemplate jdbcCustomTemplate, Long campaignId) throws Exception;

	public void getNotifyList(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, DialerSetting dialerSetting, String timeZone, String endDate,
			final String device, final List<Notify> notifyList, boolean updateInProgress) throws Exception;

	public void getNotifyWithCustomerIdGreaterThanZero(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, NotifyRequest notifyRequest, final List<Notify> notifyList)
			throws Exception;

	public void updateNotifyStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final List<Notify> notifyList, int status) throws Exception;

	public CampaignMessageEmail getCampaignMessageEmails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long campaignId) throws Exception;

	public CampaignMessageSMS getCampaignMessageSMS(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long campaignId) throws Exception;

	public CampaignMessagePhone getCampaignMessagePhone(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long campaignId) throws Exception;

	public String getPlaceHolderCategories(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception;

	public void getDynamicPlaceHolder(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String category, final DynamicTemplatePlaceHolder dynamicTPH)
			throws Exception;

	public void getPlaceHolderMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String pkcolumnId, String pkvalue, DynamicTemplatePlaceHolder dynamicTPH,
			Map<String, String> placeHodersWithValues) throws Exception;

	public int getMaxAttemptId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long notifyId) throws Exception;

	public long getAttemptCountByToday(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long notifyId, String startDateTime, String endDateTime) throws Exception;

	public Notify getNotifyById(JdbcCustomTemplate jdbcCustomTemplate, long notifyId) throws Exception;

	public void savePhoneRecords(JdbcCustomTemplate jdbcCustomTemplate, List<OutboundPhoneLogs> outboundPhoneList) throws Exception;

	public void saveNotifyPhoneStatusRecords(JdbcCustomTemplate jdbcCustomTemplate, List<NotifyPhoneStatus> notifyPhoneList) throws Exception;
	
	public FileUpload getFileUploadDetails(JdbcCustomTemplate jdbcCustomTemplate,CustomLogger customLogger, String fileType) throws Exception;

	public void updateNotifyPhoneStatusSeconds(JdbcCustomTemplate jdbcCustomTemplate, List<NotifyPhoneStatus> updatePhoneList) throws Exception;
}
