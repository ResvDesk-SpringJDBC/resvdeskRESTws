package com.telappoint.resvdeskrestws.common.clientdb.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.constants.AdminFlowStateConstants;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.common.clientdb.dao.NotifyResvDAO;
import com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.utils.CoreUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.notification.constants.NotifyStatusConstants;
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
 * @author Balaji N
 *
 */
@Repository
public class NotifyResvDAOImpl extends AbstractDAOImpl implements NotifyResvDAO {

	@Override
	public boolean isLocked(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, int timeInterval, ClientDeploymentConfig cdConfig) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from dialer_lock dl where 1=1");
		sql.append(" and (dl.locked = 'N'");
		sql.append(" and (ADDTIME(dl.start_time, '00:00:" + timeInterval + "') < ");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone());
		sql.append(" or (dl.locked = 'Y' and ADDTIME(dl.end_time, '00:10') < ");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("))");
		sql.append(" and dl.deviceType=?");
		customLogger.debug("sql: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[] { device });
		if (count > 0)
			return false;
		return true;
	}

	@Override
	public boolean unlockDialerLock(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, ClientDeploymentConfig cdConfig) throws Exception {
		return updateDialerLock(jdbcCustomTemplate, customLogger, device, cdConfig.getTimeZone());
	}

	@Override
	public boolean updateDialerLock(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String timeZone) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update dialer_lock dl set dl.start_time=");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(timeZone);
		sql.append(" , dl.end_time=");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(timeZone);
		sql.append(" where device_type=?");
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[] { device });
		if (count > 0)
			return true;
		return false;
	}

	@Override
	public DialerSetting getDailerSettings(JdbcCustomTemplate jdbcCustomTemplate, Long campaignId) throws Exception {
		StringBuilder sql = new StringBuilder("select campaign_id,call_from_1, call_to_1");
		sql.append(",call_from_2, call_to_2,call_mon,call_tue,call_wed,call_thu, call_fri,call_sat, days_before_start_calling, ");
		sql.append("hours_stop_calling,tot_max_attempts, max_attempts_per_day");
		sql.append(" from dialer_settings where campaign_id=?");
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { campaignId }, new ResultSetExtractor<DialerSetting>() {
				@Override
				public DialerSetting extractData(ResultSet rs) throws SQLException, DataAccessException {
					DialerSetting dialerSetting = null;
					if (rs.next()) {
						dialerSetting = new DialerSetting();
						dialerSetting.setCampaignId(rs.getLong("campaign_id"));
						dialerSetting.setCallFrom1(rs.getString("call_from_1"));
						dialerSetting.setCallTo1(rs.getString("call_to_1"));
						dialerSetting.setCallFrom2(rs.getString("call_from_2"));
						dialerSetting.setCallTo2(rs.getString("call_to_2"));
						dialerSetting.setCallMon(rs.getString("call_mon"));
						dialerSetting.setCallTue(rs.getString("call_tue"));
						dialerSetting.setCallWed(rs.getString("call_wed"));
						dialerSetting.setCallThu(rs.getString("call_thu"));
						dialerSetting.setCallFri(rs.getString("call_fri"));
						dialerSetting.setCallSat(rs.getString("call_sat"));
						dialerSetting.setHoursStopCalling(rs.getInt("hours_stop_calling"));
						dialerSetting.setDaysBeforeStartCalling(rs.getInt("days_before_start_calling"));
						dialerSetting.setTotMaxAttempts(rs.getInt("tot_max_attempts"));
						dialerSetting.setMaxAttemptsPerDay(rs.getInt("max_attempts_per_day"));
					}
					return dialerSetting;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_DIALER_SETTING.getValue(), e);
		}
	}

	@Override
	public void getNotifyList(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, DialerSetting dialerSetting, String timeZone, String endDate,
			final String device, final List<Notify> notifyList, final boolean updateInProgress) {

		StringBuilder sql = new StringBuilder();
		sql.append("select * from notify ntf where 1=1");
		sql.append(" and ntf.due_date_time >='" + CoreUtils.getNotifyStartDate(dialerSetting.getHoursStopCalling(), timeZone));
		sql.append("' and date(due_date_time) <= '");
		sql.append(endDate);
		sql.append("' and ntf.do_not_notify = 'N' ");
		sql.append(" and ntf.delete_flag = 'N' ");

		if (CommonResvDeskConstants.EMAIL_REMINDER.getValue().equals(device)) {
			sql.append(" and ntf.notify_by_email = 'Y'");
			sql.append(" and ntf.notify_email_status <= 2");
		} else if (CommonResvDeskConstants.PHONE_REMINDER.getValue().equals(device)) {
			sql.append(" and ntf.notify_by_phone = 'Y'");
			sql.append(" and ntf.notify_phone_status <= 2");
		} else if (CommonResvDeskConstants.SMS_REMINDER.getValue().equals(device)) {
			sql.append(" and ntf.notify_by_sms = 'Y'");
			sql.append(" and ntf.notify_sms_status <= 2");
		}
		sql.append(" and ntf.notify_status in (1,2)");
		sql.append(" and ntf.campaign_id =" + dialerSetting.getCampaignId());
		sql.append(" order by due_date_time ");

		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Long>() {
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					Notify notify = null;
					while (rs.next()) {
						notify = new Notify();
						notify.setCampaignId(rs.getLong("campaign_id"));
						notify.setNotifyId(rs.getLong("id"));
						notify.setFirstName(rs.getString("first_name"));
						notify.setLastName(rs.getString("last_name"));
						notify.setEmail(rs.getString("email"));
						notify.setDueDateTime(rs.getString("due_date_time"));
						notify.setNotifyEmailStatus(rs.getInt("notify_email_status"));
						notify.setNotifySMSstatus(rs.getInt("notify_sms_status"));
						notify.setNotifyPhoneStatus(rs.getInt("notify_phone_status"));
						notify.setNotifyStatus(rs.getInt("notify_status"));
						notify.setHomePhone(rs.getString("home_phone"));
						notify.setCellPhonel(rs.getString("cell_phone"));
						notify.setWorkPhone(rs.getString("work_phone"));
						notifyList.add(notify);
					}
					if (updateInProgress && notifyList.size() > 0) {
						updateNotificationsByBatch(jdbcCustomTemplate, customLogger, notifyList, device);
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_NOTIFY_LIST.getValue(), e);
		}
		customLogger.info("Notify Result:" + sql.toString());
	}

	public void updateNotificationsByBatch(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final List<Notify> notifyList, final String device) {
		final StringBuilder sql = new StringBuilder("update notify set ");

		MapSqlParameterSource mapSQLParameterSource = new MapSqlParameterSource();
		if (CommonResvDeskConstants.EMAIL_REMINDER.getValue().equals(device)) {
			sql.append(" notify_email_status=:EmailStatus");
			mapSQLParameterSource.addValue("EmailStatus", NotifyStatusConstants.NOTIFY_STATUS_IN_PROGRESS.getNotifyStatus());
		} else if (CommonResvDeskConstants.PHONE_REMINDER.getValue().equals(device)) {
			sql.append(" notify_phone_status=:PhoneStatus");
			mapSQLParameterSource.addValue("PhoneStatus", NotifyStatusConstants.NOTIFY_STATUS_IN_PROGRESS.getNotifyStatus());
		} else if (CommonResvDeskConstants.SMS_REMINDER.getValue().equals(device)) {
			sql.append(" notify_sms_status=:SMSStatus");
			mapSQLParameterSource.addValue("SMSStatus", NotifyStatusConstants.NOTIFY_STATUS_IN_PROGRESS.getNotifyStatus());
		}
		sql.append(",notify_status=:NotifyStatus");
		sql.append(" where id in (:NotifyId)");
		mapSQLParameterSource.addValue("NotifyStatus", NotifyStatusConstants.NOTIFY_STATUS_IN_PROGRESS.getNotifyStatus());

		Set<Long> list = new HashSet<Long>();
		for (Notify notify : notifyList) {
			list.add(notify.getNotifyId());
		}
		mapSQLParameterSource.addValue("NotifyId", list);

		try {
			jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), mapSQLParameterSource);
		} catch (Exception e) {
			customLogger.error("Error:" + e, e);
			throw new TelAppointDBException(AdminFlowStateConstants.GET_NOTIFY_LIST.getValue(), e);
		}
	}

	@Override
	public void updateNotifyStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final List<Notify> notifyList, final int status) throws Exception {
		final StringBuilder sql = new StringBuilder("update notify set notify_status=?");
		sql.append(" where id=?");

		jdbcCustomTemplate.getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			final AtomicInteger index = new AtomicInteger(1);

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Notify notify = notifyList.get(i);
				ps.setInt(index.getAndIncrement(), status);
				ps.setLong(index.getAndIncrement(), notify.getNotifyId());
			}

			@Override
			public int getBatchSize() {
				return notifyList.size();
			}
		});
	}

	@Override
	public void getNotifyWithCustomerIdGreaterThanZero(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, NotifyRequest notifyRequest, final List<Notify> notifyList)
			throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select group_concat(distinct customer_id), first_name from notify notify where 1=1 and customer_id>0");
			sql.append(" and notify.do_not_notify = 'N' ");
			sql.append(" and notify.delete_flag = 'N' ");

			boolean showEditNotify = false;
			StringBuilder subSql = new StringBuilder();
			if ("Y".equals(notifyRequest.getNotifyByPhone())) {
				showEditNotify = true;
				subSql.append(" notify.notify_by_phone = 'Y'");
			}
			if ("Y".equals(notifyRequest.getNotifyByEmail()) && !showEditNotify) {
				showEditNotify = true;
				subSql.append(" notify.notify_by_email = 'Y'");
			} else if ("Y".equals(notifyRequest.getNotifyByEmail())) {
				subSql.append(" or notify.notify_by_email = 'Y'");
			}
			if ("Y".equals(notifyRequest.getNotifyBySMS()) && !showEditNotify) {
				showEditNotify = true;
				subSql.append(" notify.notify_by_sms = 'Y'");
			} else if ("Y".equals(notifyRequest.getNotifyBySMS())) {
				subSql.append(" or notify.notify_by_sms = 'Y'");
			}
			if (subSql.length() > 0) {
				sql.append(" and ( ");
				sql.append(subSql);
				sql.append(" ) ");
			}

			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Notify>>() {
				@Override
				public List<Notify> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Notify notify = null;
					while (rs.next()) {
						notify = new Notify();
						notify.setCampaignId(rs.getLong("campaign_id"));
						notify.setNotifyId(rs.getLong("id"));
						notify.setFirstName(rs.getString("first_name"));
						notify.setLastName(rs.getString("last_name"));
						notify.setEmail(rs.getString("email"));
						notify.setDueDateTime(rs.getString("due_date_time"));
						notify.setNotifyEmailStatus(rs.getInt("notify_email_status"));
						notify.setNotifySMSstatus(rs.getInt("notify_sms_status"));
						notify.setNotifyPhoneStatus(rs.getInt("notify_phone_status"));
						notify.setNotifyStatus(rs.getInt("notify_status"));
						notify.setHomePhone(rs.getString("home_phone"));
						notify.setCellPhonel(rs.getString("cell_phone"));
						notify.setWorkPhone(rs.getString("work_phone"));
						notifyList.add(notify);
					}
					return notifyList;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_NOTIFY_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public CampaignMessageEmail getCampaignMessageEmails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long campaignId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from campaign_message_email cme Where 1=1");
		sql.append(" and cme.campaign_id=?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { campaignId }, new ResultSetExtractor<CampaignMessageEmail>() {
			@Override
			public CampaignMessageEmail extractData(ResultSet rs) throws SQLException, DataAccessException {
				CampaignMessageEmail campaignMessageEmail = null;
				while (rs.next()) {
					campaignMessageEmail = new CampaignMessageEmail();
					campaignMessageEmail.setCampaignMessageEmailId(rs.getLong("id"));
					campaignMessageEmail.setCampaignId(rs.getLong("campaign_id"));
					campaignMessageEmail.setLang(rs.getString("lang"));
					campaignMessageEmail.setSubject(rs.getString("subject"));
					campaignMessageEmail.setMessage(rs.getString("message"));
					campaignMessageEmail.setEnableHtmlFlag(rs.getString("enable_html_flag"));
				}
				return campaignMessageEmail;
			}
		});
	}

	@Override
	public CampaignMessageSMS getCampaignMessageSMS(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long campaignId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from campaign_message_sms cme Where 1=1");
		sql.append(" and cme.campaign_id=?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { campaignId }, new ResultSetExtractor<CampaignMessageSMS>() {
			@Override
			public CampaignMessageSMS extractData(ResultSet rs) throws SQLException, DataAccessException {
				CampaignMessageSMS campaignMessageSMS = null;
				while (rs.next()) {
					campaignMessageSMS = new CampaignMessageSMS();
					campaignMessageSMS.setCampaignMessageSMSId(rs.getLong("id"));
					campaignMessageSMS.setCampaignId(rs.getLong("campaign_id"));
					campaignMessageSMS.setLang(rs.getString("lang"));
					campaignMessageSMS.setSubject(rs.getString("subject"));
					campaignMessageSMS.setMessage(rs.getString("message"));
				}
				return campaignMessageSMS;
			}
		});
	}

	@Override
	public CampaignMessagePhone getCampaignMessagePhone(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long campaignId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from campaign_message_sms cme Where 1=1");
		sql.append(" and cme.campaign_id=?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { campaignId }, new ResultSetExtractor<CampaignMessagePhone>() {
			@Override
			public CampaignMessagePhone extractData(ResultSet rs) throws SQLException, DataAccessException {
				CampaignMessagePhone campaignMessagePhone = null;
				while (rs.next()) {
					campaignMessagePhone = new CampaignMessagePhone();
					campaignMessagePhone.setCampaignMessagePhoneId(rs.getLong("id"));
					campaignMessagePhone.setCampaignId(rs.getLong("campaign_id"));
					campaignMessagePhone.setLang(rs.getString("lang"));
					campaignMessagePhone.setTtsOnly(rs.getString("tts_only"));
					campaignMessagePhone.setMessage(rs.getString("message"));
				}
				return campaignMessagePhone;
			}
		});
	}

	@Override
	public void getPlaceHolderMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String pkcolumnId, String pkvalue, DynamicTemplatePlaceHolder dynamicTPH,
			final Map<String, String> map) throws Exception {
		String dynamicSelect = dynamicTPH.getDynamicSelect();
		String dynamicFrom = dynamicTPH.getDynamicFrom();
		final String dynamicAlias = dynamicTPH.getAliasName();
		final String dynamicType = dynamicTPH.getTypes();

		final String placeHolders = dynamicTPH.getDynamicPlaceHolder();
		StringBuilder sql = new StringBuilder();
		sql.append("select ").append(dynamicSelect);
		sql.append(" from ").append(dynamicFrom);
		sql.append(" where ").append(pkcolumnId).append("=").append(pkvalue);

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String alias[] = dynamicAlias.split(",");
				String types[] = dynamicType.split(",");
				String placeHolder[] = placeHolders.split(",");
				while (rs.next()) {
					try {
						for (int i = 0; i < alias.length; i++) {
							if ("int".equalsIgnoreCase(types[i])) {
								map.put(placeHolder[i], "" + rs.getInt(alias[i]));
							}

							if ("long".equalsIgnoreCase(types[i])) {
								map.put(placeHolder[i], "" + rs.getLong(alias[i]));
							}

							if ("varchar".equalsIgnoreCase(types[i])) {
								map.put(placeHolder[i], rs.getString(alias[i]));
							}
						}
					} catch (Exception e) {
						throw new SQLException("Exception in executeQueryDynamicQuery.");
					}
				}
				return null;
			}
		});
	}

	@Override
	public void getDynamicPlaceHolder(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String category, final DynamicTemplatePlaceHolder dynamicTPH)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(distinct table_name order by id) as tableNames, ");
		sql.append("group_concat(concat(table_column,' as ',alias_name) order by id) as columnNames,");
		sql.append(" group_concat(alias_name order by id) as aliasName,");
		sql.append(" group_concat(`type` order by id) as type,");
		sql.append(" group_concat(`place_holder` order by id) as placeHolder");
		sql.append(" from dynamic_template_placeholder where category= ?");

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { category }, new ResultSetExtractor<DynamicTemplatePlaceHolder>() {
			@Override
			public DynamicTemplatePlaceHolder extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					dynamicTPH.setDynamicFrom(rs.getString("tableNames"));
					dynamicTPH.setDynamicSelect(rs.getString("columnNames"));
					dynamicTPH.setDynamicPlaceHolder(rs.getString("placeHolder"));
					dynamicTPH.setAliasName(rs.getString("aliasName"));
					dynamicTPH.setTypes(rs.getString("type"));
				}
				return dynamicTPH;
			}
		});
	}

	@Override
	public String getPlaceHolderCategories(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(distinct category) as categoryStr from dynamic_template_placeholder");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString("categoryStr");
				}
				return "";
			}
		});
	}

	@Override
	public long getAttemptCountByToday(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long notifyId, String startDateTime, String endDateTime) throws Exception {
		StringBuilder sql = new StringBuilder("select count(*) as count from notify_phone_status nps where nps.notify_id=?");
		sql.append(" and nps.call_timestamp >='" + startDateTime);
		sql.append("' and nps.call_timestamp <= '" + endDateTime + "'");
		Integer attemptCountToday = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[] { notifyId });
		if (null == attemptCountToday)
			return 0;

		return attemptCountToday;
	}

	@Override
	public Notify getNotifyById(JdbcCustomTemplate jdbcCustomTemplate, long notifyId) throws Exception {
		String sql = "select id from notify where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql, new Object[] { notifyId }, new ResultSetExtractor<Notify>() {
			@Override
			public Notify extractData(ResultSet rs) throws SQLException, DataAccessException {
				Notify notify = null;
				if (rs.next()) {
					notify = new Notify();
					notify.setNotifyId(rs.getLong("id"));
				}
				return notify;
			}
		});
	}

	@Override
	public void savePhoneRecords(JdbcCustomTemplate jdbcCustomTemplate, final List<OutboundPhoneLogs> outboundPhoneList) throws Exception {
		StringBuilder outboundLogSQL = new StringBuilder();
		outboundLogSQL.append("insert into outbound_phone_logs(timestamp,call_id,phone,call_date, attempts,pickups,reason,duration, notify_id, result, cause)");
		outboundLogSQL.append(" values (now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			jdbcCustomTemplate.getJdbcTemplate().batchUpdate(outboundLogSQL.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					OutboundPhoneLogs obpl = outboundPhoneList.get(i);
					ps.setLong(1, obpl.getCallId());
					ps.setString(2, obpl.getPhone());
					ps.setString(3, obpl.getCallDate());
					ps.setLong(4, obpl.getAttemptId());
					ps.setInt(5, obpl.getPickups());
					ps.setString(6, obpl.getReason());
					ps.setInt(7, obpl.getDuration());
					ps.setLong(8, obpl.getNotifyId());
					ps.setString(9, obpl.getResult());
					ps.setString(10, obpl.getCause());
				}

				@Override
				public int getBatchSize() {
					return outboundPhoneList.size();
				}
			});
			dsTransactionManager.commit(status);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.PHONE_LOG_UPLOAD.getValue(), e);
		}

	}
	
	@Override
	public FileUpload getFileUploadDetails(JdbcCustomTemplate jdbcCustomTemplate,CustomLogger customLogger, String fileType) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select file_type, max_file_size_kb,ignore_rows from file_upload fu where 1=1");
		sql.append(" and fu.file_type=?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] {fileType.toUpperCase()}, new ResultSetExtractor<FileUpload>() {
			FileUpload fileUpload = null;
			@Override
			public FileUpload extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					fileUpload = new FileUpload();
					fileUpload.setFileType(rs.getString("file_type"));
					fileUpload.setMaxFileSizeKB(rs.getLong("max_file_size_kb"));
					fileUpload.setIgnoreRows(rs.getInt("ignore_rows"));
				}
				return fileUpload;
			}
		});
	}

	@Override
	public void saveNotifyPhoneStatusRecords(JdbcCustomTemplate jdbcCustomTemplate, final List<NotifyPhoneStatus> notifyPhoneStatusList) throws Exception {
		StringBuilder npsSQL = new StringBuilder();
		npsSQL.append("insert into notify_phone_status(notify_id,attempt_id,phone, call_status,call_timestamp,seconds)");
		npsSQL.append(" values (?, ?, ?, ?, ?, ?)");

		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			jdbcCustomTemplate.getJdbcTemplate().batchUpdate(npsSQL.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					NotifyPhoneStatus nps = notifyPhoneStatusList.get(i);
					ps.setLong(1, nps.getNotifyId());
					ps.setLong(2, nps.getAttemptId());
					ps.setString(3, nps.getPhone());
					ps.setInt(4, nps.getCallStatus());
					ps.setString(5, nps.getCallTimestamp());
					ps.setLong(6,nps.getSeconds());
				}

				@Override
				public int getBatchSize() {
					return notifyPhoneStatusList.size();
				}
			});
			dsTransactionManager.commit(status);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.NOTIFY_PHONE_STATUS.getValue(), e);
		}
	}

	@Override
	public void updateNotifyPhoneStatusSeconds(JdbcCustomTemplate jdbcCustomTemplate, final List<NotifyPhoneStatus> updatePhoneList) throws Exception {
		StringBuilder npsSQL = new StringBuilder();
		npsSQL.append("update notify_phone_status set seconds=? where notify_id=? and attempt_id=?");
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			jdbcCustomTemplate.getJdbcTemplate().batchUpdate(npsSQL.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					NotifyPhoneStatus nps = updatePhoneList.get(i);
					ps.setLong(1, nps.getSeconds());
					ps.setLong(2, nps.getNotifyId());
					ps.setLong(3, nps.getAttemptId());
				}

				@Override
				public int getBatchSize() {
					return updatePhoneList.size();
				}
			});
			dsTransactionManager.commit(status);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.NOTIFY_PHONE_STATUS.getValue(), e);
		}
	}
}
