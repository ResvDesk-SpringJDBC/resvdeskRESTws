package com.telappoint.resvdeskrestws.notification.service;

import java.io.InputStream;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.FileUploadResponse;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.notification.model.NotificationResponse;

/**
 * 
 * @author Balaji N
 *
 */
public interface NotifyReservationService {
	public CustomLogger getLogger(String clientCode, String device) throws Exception;
	public BaseResponse lockDialerLock(String clientCode, CustomLogger customLogger, String device) throws Exception;
	public BaseResponse unLockDialerLock(String clientCode, CustomLogger customLogger, String device) throws Exception;
	public NotificationResponse getNotifyList(String clientCode, CustomLogger customLogger, String langCode, String device) throws Exception;
	public FileUploadResponse processThePhoneLogFile(String clientCode, CustomLogger customLogger, String fileSize, String device, String transId, InputStream inputStream, String fileExtension) throws Exception;
}
