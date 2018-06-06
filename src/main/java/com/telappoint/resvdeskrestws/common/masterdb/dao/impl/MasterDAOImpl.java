package com.telappoint.resvdeskrestws.common.masterdb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.constants.AdminFlowStateConstants;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfig;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfigResponse;
import com.telappoint.resvdeskrestws.common.masterdb.dao.MasterDAO;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ErrorConfig;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.notification.model.SMSConfig;

/**
 * 
 * @author Balaji N
 *
 */
@Repository
public class MasterDAOImpl implements MasterDAO {

	@Autowired
	private JdbcTemplate masterJdbcTemplate;

	public MasterDAOImpl(JdbcTemplate jdbcTemplate) {
		this.masterJdbcTemplate = jdbcTemplate;
	}

	public MasterDAOImpl() {
	}

	@Override
	public void getClients(final String key, final Map<String, Client> clientCacheMap) throws Exception {
		String query = "select * from client c where delete_flag='N'";
		masterJdbcTemplate.query(query.toString(), new ResultSetExtractor<Map<String, Client>>() {
			@Override
			public Map<String, Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Client client = null;
				String clientCode = null;
				while (rs.next()) {
					clientCode = rs.getString("client_code");
					client = new Client();
					client.setClientId(rs.getInt("id"));
					client.setClientCode(clientCode);
					client.setClientName(rs.getString("client_name"));
					client.setWebsite(rs.getString("website"));
					client.setContactEmail(rs.getString("contact_email"));
					client.setFax(rs.getString("fax"));
					client.setAddress(rs.getString("address"));
					client.setAddress2(rs.getString("address2"));
					client.setCity(rs.getString("city"));
					client.setState(rs.getString("state"));
					client.setZip(rs.getString("zip"));
					client.setCountry(rs.getString("country"));
					client.setDbName(rs.getString("db_name"));
					client.setDbServer(rs.getString("db_server"));
					client.setCacheEnabled(rs.getString("cache_enabled"));
					client.setApptLink(rs.getString("appt_link"));
					client.setDirectAccessNumber(rs.getString("direct_access_number"));
					clientCacheMap.put(key + "|" + clientCode, client);
				}
				return clientCacheMap;
			}
		});
	}

	@Override
	public void getClientDeploymentConfig(final String key, final String clientCode, int clientId, final Map<String, Object> cacheObjectMap) throws Exception {
		String query = "select * from client_deployment_config c where client_id = ?";
		masterJdbcTemplate.query(query.toString(), new Object[] { clientId }, new ResultSetExtractor<Map<String, Object>>() {
			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ClientDeploymentConfig clientDeploymentConfig = null;
				if (rs.next()) {
					clientDeploymentConfig = new ClientDeploymentConfig();
					clientDeploymentConfig.setTimeZone(rs.getString("time_zone"));
					clientDeploymentConfig.setDateFormat(rs.getString("date_format"));
					clientDeploymentConfig.setTimeFormat(rs.getString("time_format"));
					clientDeploymentConfig.setDateyyyyFormat(rs.getString("date_yyyy_format"));
					clientDeploymentConfig.setFullDateFormat(rs.getString("full_date_format"));
					clientDeploymentConfig.setFullDatetimeFormat(rs.getString("full_datetime_format"));
					clientDeploymentConfig.setFullTextualdayFormat(rs.getString("full_textualday_format"));
					clientDeploymentConfig.setPhoneFormat(rs.getString("phone_format"));
					clientDeploymentConfig.setPopupCalendardateFormat(rs.getString("popup_calendardate_format"));
					clientDeploymentConfig.setLeadTimeInSeconds(rs.getInt("notify_phone_lead_time"));
					clientDeploymentConfig.setLagTimeInSeconds(rs.getInt("notify_phone_lag_time"));
					
					cacheObjectMap.put(key + "|" + clientCode, clientDeploymentConfig);
				}
				return cacheObjectMap;
			}
		});
	}

	@Override
	public void getErrorConfig(final String key, final Map<String, Object> cacheObject) throws Exception {
		String query = "select * from error";
		masterJdbcTemplate.query(query.toString(), new ResultSetExtractor<Map<String, Object>>() {
			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ErrorConfig errorConfig = null;
				while (rs.next()) {
					String errorCode = rs.getString("error_code");
					errorConfig = new ErrorConfig();
					errorConfig.setErrorId(rs.getInt("id"));
					errorConfig.setErrorCode(errorCode);
					errorConfig.setErrorMessage(rs.getString("error_message"));
					errorConfig.setErrorDescription(rs.getString("error_description"));
					errorConfig.setErrorVXML(rs.getString("error_vxml"));
					errorConfig.setSendAlert(rs.getString("send_alert"));
					cacheObject.put(key + "|" + errorCode, errorConfig);
				}
				return cacheObject;
			}
		});
		
	}
	
	public boolean addReservationReportConfig(CustomLogger customLogger, String langCode, String device, int clientId, ReservationReportConfig resvReportConfig) throws Exception {		
		StringBuilder sql = new StringBuilder();
		sql.append("insert into resv_report_config ").append("(");
		sql.append(" timestamp,username, client_id, report_name, ");
		sql.append(" location_ids,event_ids,seat_ids,");
		sql.append(" report_columns,report_path,");
		sql.append(" email1,email2,email3,email4,");
		sql.append(" email5,email6,sortby1,sortby2,");
		sql.append(" sortby3,sortby4,sortby5,resv_status_fetch");
		sql.append(")").append(" values (now(),?,?,?,");
		sql.append("?,?,?,?,?,?,");
		sql.append("?,?,?,?,");
		sql.append("?,?,?,?,");
		sql.append("?,?,?)");
		
		Object [] dynamic = new Object[20];
		
		dynamic[0] = resvReportConfig.getUserName();
		dynamic[1] = clientId;
		dynamic[2] = resvReportConfig.getReportName();
		dynamic[3] = resvReportConfig.getLocationIds() == null?"ALL":resvReportConfig.getLocationIds();
		dynamic[4] = resvReportConfig.getEventIds()== null?"ALL":resvReportConfig.getEventIds();
		dynamic[5] = resvReportConfig.getSeatIds()== null?"ALL":resvReportConfig.getSeatIds();
		dynamic[6] = resvReportConfig.getReportColumns();
		dynamic[7] = "/appt_report/" + resvReportConfig.getClientCode().toUpperCase() + "/";
		dynamic[8] = resvReportConfig.getEmail1();
		dynamic[9] = resvReportConfig.getEmail2();
		dynamic[10] = resvReportConfig.getEmail3();
		dynamic[11] = resvReportConfig.getEmail4();
		dynamic[12] = resvReportConfig.getEmail5();
		dynamic[13] = resvReportConfig.getEmail6();
		
		dynamic[14] = resvReportConfig.getSortby1();
		dynamic[15] = resvReportConfig.getSortby2();
		dynamic[16] = resvReportConfig.getSortby3();
		dynamic[17] = resvReportConfig.getSortby4();
		dynamic[18] = resvReportConfig.getSortby5();
		dynamic[19] = resvReportConfig.getResvStatusFetch();
		
		try {
			int count = masterJdbcTemplate.update(sql.toString(), dynamic);
			if (count > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new Exception("Failed in save Report Config!", e);
		}
	}

	@Override
	public void getReservationReportConfig(CustomLogger customLogger, String langCode, String device, int clientId,
			final ReservationReportConfigResponse reservationReportConfigResponse) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(" id, concat(DATE_FORMAT(timestamp,'%m/%d/%Y'),' ',DATE_FORMAT(timestamp, '%l:%i %p')) as timestamp, report_name, ");
		sql.append(" report_columns,report_path,");
		sql.append(" email1,email2,email3,email4,");
		sql.append(" email5,email6,username");
		sql.append(" from resv_report_config");
		
		try {
			masterJdbcTemplate.query(sql.toString(), new ResultSetExtractor<ReservationReportConfigResponse>() {
				@Override
				public ReservationReportConfigResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<ReservationReportConfig> resvReportConfigList = new ArrayList<ReservationReportConfig>();
					ReservationReportConfig resvReportConfig = null;
					while (rs.next()) {
						resvReportConfig = new ReservationReportConfig();
						resvReportConfig.setResvReportConfigId(rs.getInt("id"));
						resvReportConfig.setTimestamp(rs.getString("timestamp"));
						resvReportConfig.setReportName(rs.getString("report_name"));
						resvReportConfig.setReportColumns(rs.getString("report_columns"));
						resvReportConfig.setEmail1(rs.getString("email1"));
						resvReportConfig.setEmail2(rs.getString("email2"));
						resvReportConfig.setEmail3(rs.getString("email3"));
						resvReportConfig.setEmail4(rs.getString("email4"));
						resvReportConfig.setEmail5(rs.getString("email5"));
						resvReportConfig.setEmail6(rs.getString("email6"));
						resvReportConfig.setUserName(rs.getString("username"));
						resvReportConfigList.add(resvReportConfig);
					}
					reservationReportConfigResponse.setResvReportConfigList(resvReportConfigList);
					return reservationReportConfigResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_RESV_REPORT_CONFIG.getValue(), e);
		}
		return;		
	}
	
	
	@Override
	public boolean deleteResvReportConfigById(CustomLogger customLogger, String langCode, String device, String reportConfigId) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("delete from resv_report_config where id = ?");
			masterJdbcTemplate.update(sql.toString(), new Object[] {Integer.valueOf(reportConfigId) });
			sql.setLength(0);
			return true;
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.DELETE_REPORT_CONFIG_BY_ID.getValue(), e);
		}
	}

	@Override
	public SMSConfig getSMSConfig(CustomLogger customLogger, Integer clientId) throws Exception {
		String query = "select * from sms_config c where client_id=?";
		return masterJdbcTemplate.query(query.toString(), new Object[]{clientId}, new ResultSetExtractor<SMSConfig>() {
			@Override
			public SMSConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
				SMSConfig smsConfig = null;
				if (rs.next()) {
					smsConfig = new SMSConfig();
					smsConfig.setAccountSID(rs.getString("account_sid"));
					smsConfig.setAuthToken(rs.getString("auth_token"));
					smsConfig.setSmsPhone(rs.getString("sms_phone"));
					smsConfig.setSmsConfigId(rs.getInt("id"));
				}
				return smsConfig;
			}
		});
	}		
}
