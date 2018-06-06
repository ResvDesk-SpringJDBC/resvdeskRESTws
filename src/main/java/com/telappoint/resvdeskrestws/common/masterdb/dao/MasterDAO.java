package com.telappoint.resvdeskrestws.common.masterdb.dao;

import java.util.Map;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfig;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfigResponse;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.notification.model.SMSConfig;

public interface MasterDAO {
	public  void getClients(final String key, final Map<String, Client> clientCacheMap) throws Exception;
	public void getClientDeploymentConfig(final String key, String clientCode, int clientId,final Map<String, Object> cacheMap) throws Exception;
	public void getErrorConfig(String value, Map<String, Object> cacheObject) throws Exception;
	
	public boolean addReservationReportConfig(CustomLogger customLogger, String langCode, String device, int clientId, ReservationReportConfig resvReportConfig) throws Exception;
	public void getReservationReportConfig(CustomLogger customLogger, String langCode, String device, int clientId,
			ReservationReportConfigResponse reservationReportConfigResponse) throws Exception;
	
	public boolean deleteResvReportConfigById(CustomLogger customLogger, String langCode, String device, String reportConfigId) throws Exception;
	public SMSConfig getSMSConfig(CustomLogger customLogger, Integer clientId) throws Exception;
}
