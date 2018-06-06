package com.telappoint.resvdeskrestws.common.clientdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.constants.AdminFlowStateConstants;
import com.telappoint.resvdeskrestws.admin.model.Campaign;
import com.telappoint.resvdeskrestws.admin.model.CampaignResponse;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.admin.model.Schedule;
import com.telappoint.resvdeskrestws.common.constants.CommonDateContants;
import com.telappoint.resvdeskrestws.common.constants.FlowStateConstants;
import com.telappoint.resvdeskrestws.common.constants.IVRVxmlConstants;
import com.telappoint.resvdeskrestws.common.constants.ReservataionStatus;
import com.telappoint.resvdeskrestws.common.constants.ReservationMethod;
import com.telappoint.resvdeskrestws.common.constants.ReservationtType;
import com.telappoint.resvdeskrestws.common.externalservice.client.MobileNumberLookup;
import com.telappoint.resvdeskrestws.common.model.AuthResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ConfirmResvResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.EventDate;
import com.telappoint.resvdeskrestws.common.model.HoldResvResponse;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.LoginField;
import com.telappoint.resvdeskrestws.common.model.Options;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;
import com.telappoint.resvdeskrestws.common.model.VerifyResvResponse;
import com.telappoint.resvdeskrestws.common.utils.CoreUtils;
import com.telappoint.resvdeskrestws.common.utils.DateUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.notification.constants.IVRNotifyStatusConstants;

/**
 * 
 * @author Balaji N
 *
 */
public abstract class AbstractDAOImpl {

	public ResvSysConfig getResvSysConfig(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception {
		StringBuilder sql = new StringBuilder("select max_appt_duration_days,display_company,display_procedure,display_location,display_department,display_event,");
		sql.append(" display_seat,login_first,enforce_login,send_conf_email,send_cancel_email,display_comments, comments_rows, comments_cols,");
		sql.append("ivr_time_batch_size,record_msg,scheduler_closed, no_funding,run_phone_type_lookup");
		sql.append(" from resv_sys_config ");

		final ResvSysConfig resvSysConfig = new ResvSysConfig();
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<ResvSysConfig>() {
			@Override
			public ResvSysConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					resvSysConfig.setDisplayCompany(rs.getString("display_company"));
					resvSysConfig.setDisplayProcedure(rs.getString("display_procedure"));
					resvSysConfig.setDisplayLocation(rs.getString("display_location"));
					resvSysConfig.setDisplayDepartment(rs.getString("display_department"));
					resvSysConfig.setDisplayEvent(rs.getString("display_event"));
					resvSysConfig.setDisplaySeat(rs.getString("display_seat"));
					resvSysConfig.setLoginFirst(rs.getString("login_first"));
					resvSysConfig.setEnforceLogin(rs.getString("enforce_login"));
					resvSysConfig.setSendConfirmEmail(rs.getString("send_conf_email"));
					resvSysConfig.setSendCancelEmail(rs.getString("send_cancel_email"));
					resvSysConfig.setIvrTimeBatchSize("" + rs.getInt("ivr_time_batch_size"));
					resvSysConfig.setRecordMsg(rs.getString("record_msg"));
					resvSysConfig.setSchedulerClosed(rs.getString("scheduler_closed"));
					resvSysConfig.setNoFunding(rs.getString("no_funding"));
					resvSysConfig.setDisplayComments(rs.getString("display_comments"));
					resvSysConfig.setCommentsNoOfCols(""+rs.getInt("comments_cols"));
					resvSysConfig.setCommentsNoOfRows(""+rs.getInt("comments_rows"));
					resvSysConfig.setMaxApptDurationDays(""+rs.getInt("max_appt_duration_days"));
					resvSysConfig.setRunPhoneTypeLookup(rs.getString("run_phone_type_lookup"));

					// TODO: add it more if required.
				}
				return resvSysConfig;
			}
		});
	}
	
	/**
	 * This method used to get the next placement number for
	 * location/event/ etc.
	 * 
	 * @param table
	 * @param baseDAO
	 * @return
	 */
	public int getMaxPlacementVal(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String table) {
		int maxPlacementVal = 1;
		try {
			String sql = "select max(placement)+1 from " + table;
			Integer val = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql);
			if(val != null && val != 0) {
				maxPlacementVal = val;
			}
			
		} catch (Exception e) {
			customLogger.error("Error: " + e, e);
		}
		return maxPlacementVal;
	}
	
	public int getMaxAttemptId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long notifyId) {
		String sql = "select max(nps.attempt_id) from notify_phone_status nps where nps.notify_id= ?";
		Integer attemptId = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql, new Object[]{notifyId});
		if (null == attemptId)
			return 0;
		return attemptId;
	}
	
	public boolean insertNotifyEmailStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long notifyId, int statusTO, int numberOfEmails) throws Exception {
		customLogger.debug("updateNotifyEmailStatus");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO notify_email_status");
		sql.append(" (notify_id,attempt_id,email,email_status,email_timestamp,number_of_email) ");
		sql.append(" select " + notifyId + " as notify_id, ");
		sql.append(" case when max(emstatus.attempt_id)+1 is null then 1 when max(emstatus.attempt_id)+1 >0 then max(emstatus.attempt_id)+1 ");
		sql.append(" end as attempt_id , (select email from notify nt where nt.id=");
		sql.append(notifyId + "), " + statusTO + " as sms_status ,CURRENT_TIMESTAMP() as email_timestamp ," + numberOfEmails);
		sql.append(" as number_of_email from notify_email_status emstatus where emstatus.notify_id=" + notifyId);

		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString());
		if (count > 0)
			return true;
		return false;
	}

	public void insertPhoneStatusRecord(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, NotifyRequest notifyRequest) throws Exception {
		long seconds = Calendar.getInstance().getTime().getTime() / 1000 - notifyRequest.getStartTime().getTime() / 1000 + notifyRequest.getLagTime();
		Date callTimeStamp = Calendar.getInstance().getTime();
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO notify_phone_status (notify_id,attempt_id,phone,call_status,call_timestamp,seconds) ");
		sql.append(" values (:notifyId, :attemptId, :phone, :callstatus, :calltimestamp,:seconds)");
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("notifyId", notifyRequest.getNotifyId());
		paramMap.addValue("attemptId", notifyRequest.getAttemptId());
		paramMap.addValue("phone", notifyRequest.getPhoneNumber());
		paramMap.addValue("callstatus",IVRNotifyStatusConstants.NOTIFY_PHONE_STATUS_NO_ANSWER.getNotifyStatus());
		paramMap.addValue("calltimestamp", callTimeStamp);
		paramMap.addValue("seconds", seconds);

		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramMap);

	}

	public boolean insertNotifySMSStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long notifyId, int statusTO, int numberOfSMS) throws Exception {
		customLogger.debug("updateNotifySMSStatus");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO notify_sms_status (notify_id,attempt_id,phone,sms_status,sms_timestamp,number_of_sms) ");
		sql.append(" select " + notifyId + " as notify_id, ");
		sql.append(" case when max(innt.attempt_id)+1 	is null then 1 when max(innt.attempt_id)+1 >0 then max(innt.attempt_id)+1 ");
		sql.append(" end as attempt_id , (select cell_phone from notify nt where nt.id=");
		sql.append(notifyId + "), " + statusTO + " as sms_status ,CURRENT_TIMESTAMP() as sms_timestamp ," + numberOfSMS);
		sql.append(" as number_of_sms from notify_sms_status innt  where innt.notify_id=" + notifyId);

		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString());
		if (count > 0)
			return true;
		return false;
	}

	public void authenticateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Map<String, String> paramsMap, Map<String, LoginField> loginFieldMap, final Object response)
			throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from customer c where ");
			int i = 0;
			String[] columnValue = new String[paramsMap.size()];
			LoginField loginField = null;
			for (String paramColumn : paramsMap.keySet()) {
				sql.append("c.").append(paramColumn).append(" = ?");
				if (paramsMap.size() > i++) {
					String paramValue = paramsMap.get(paramColumn) == null ? "" : paramsMap.get(paramColumn);
					if ("first_name".equals(paramColumn) || "last_name".equals(paramColumn)) {
						paramValue = (paramValue.length() > 0) ? CoreUtils.capitalizeString(paramValue) : "";
					} 
					if("account_number".equals(paramColumn)) {
						loginField = loginFieldMap.get(paramColumn);
						int storageSize = loginField.getStorageSize();
						String storageType = loginField.getStorageType();
						int paramValueLength = paramValue.length();
						if(storageSize > 0 && paramValueLength >= storageSize) {
							if("last".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(paramValueLength - storageSize);
							} else if("first".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(0,storageSize);
							} else if("prefix0".equalsIgnoreCase(storageType)) {
								paramValue = "0"+paramValue;
							} else if("postfix0".equalsIgnoreCase(storageType)) {
								paramValue = paramValue+"0";
							}
						}
					}
					
					if("dob".equals(paramColumn)) {
						paramValue = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(paramValue);
					}
					columnValue[i - 1] = paramValue;
				}
				sql.append(" and ");
			}
			sql.append(" c.delete_flag ='N' order by id desc");
			customLogger.debug(device, "authenticate Customer SQL: " + sql.toString());
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), columnValue, new ResultSetExtractor<Object>() {
				@Override
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						if (response instanceof AuthResponse) {
							AuthResponse authResponse = (AuthResponse) response;
							authResponse.setAuthSuccess(true);
							authResponse.setCustomerId(rs.getLong("id"));
							authResponse.setFirstName(rs.getString("first_name"));
							authResponse.setLastName(rs.getString("last_name"));
							return authResponse;
						}
					}
					return response;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.AUTH_CUSTOMER.getValue(), e);
		}
	}

	public boolean validateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Map<String, String> paramsMap,Map<String, LoginField> loginFieldMap) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(1) from customer c where ");
			int i = 0;
			String[] columnValue = new String[paramsMap.size()];
			LoginField loginField = null;
			for (String paramColumn : paramsMap.keySet()) {
				sql.append("c.").append(paramColumn).append(" = ?");
				if (paramsMap.size() > i++) {
					String paramValue = paramsMap.get(paramColumn) == null ? "" : paramsMap.get(paramColumn);
					if ("first_name".equals(paramColumn) || "last_name".equals(paramColumn)) {
						paramValue = (paramValue.length() > 0) ? CoreUtils.capitalizeString(paramValue) : "";
					} 
					if("account_number".equals(paramColumn)) {
						loginField = loginFieldMap.get(paramColumn);
						int storageSize = loginField.getStorageSize();
						String storageType = loginField.getStorageType();
						int paramValueLength = paramValue.length();
						if(storageSize > 0 && paramValueLength >= storageSize) {
							if("last".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(paramValueLength - storageSize);
							} else if("first".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(0,storageSize);
							} else if("prefix0".equalsIgnoreCase(storageType)) {
								paramValue = "0"+paramValue;
							} else if("postfix0".equalsIgnoreCase(storageType)) {
								paramValue = paramValue+"0";
							}
						}
					}
					columnValue[i - 1] = paramValue;
				}
				sql.append(" and ");
			}
			sql.append(" c.delete_flag ='N' order by id desc");
			customLogger.debug(device, "authenticate Customer SQL: " + sql.toString());
			int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), columnValue);
			boolean isValid = true;
			if (count > 1) {
				isValid = false;
			}
			return isValid;
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.AUTH_CUSTOMER.getValue(), e);
		}
	}

	public long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Customer customer, ClientDeploymentConfig cdConfig) throws Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("insert into customer (account_number, first_name, middle_name,  last_name,  ");
		sql.append("home_phone, work_phone, contact_phone, email, create_datetime, update_datetime, attrib1, attrib2, attrib3, dob)");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')").append(",");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(",?,?,?,?)");
		customLogger.debug("saveCustomer SQL: " + sql.toString());
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
			int i = 1;
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(i++, customer.getAccountNumber());
				ps.setString(i++, customer.getFirstName());
				ps.setString(i++, customer.getMiddleName());
				ps.setString(i++, customer.getLastName());
				ps.setString(i++, customer.getHomePhone());
				ps.setString(i++, customer.getWorkPhone());
				ps.setString(i++, customer.getContactPhone());
				ps.setString(i++, customer.getEmail());
				ps.setString(i++, customer.getAttrib1());
				ps.setString(i++, customer.getAttrib2());
				ps.setString(i++, customer.getAttrib3());
				ps.setString(i++, customer.getDob());
				return ps;
			}
		}, holder);
		Long customerId = holder.getKey().longValue();
		return customerId;
	}

	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Customer customer, ClientDeploymentConfig cdConfig) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update customer set account_number= ?, first_name= ?, middle_name=?,  last_name=?, ");
		sql.append(" home_phone=?, work_phone=?, contact_phone=?, email=?, dob=?, attrib2=?, attrib3=?,");
		sql.append(" update_datetime = ");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(" where id = ?");
		customLogger.debug("updateCustomer SQL: " + sql.toString());
		Object columnValue[] = new Object[] { customer.getAccountNumber(), customer.getFirstName(), customer.getMiddleName(), customer.getLastName(), customer.getHomePhone(),
				customer.getWorkPhone(), customer.getContactPhone(), customer.getEmail(), customer.getDob(), customer.getAttrib2(),customer.getAttrib3(),  customer.getCustomerId() };

		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), columnValue);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public Customer getCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from customer customer where customer.id=");
			sql.append(customerId);
			sql.append(" and customer.delete_flag ='N' ");
			customLogger.debug("getCustomer SQL: " + sql.toString());
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Customer>() {
				@Override
				public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Customer customer = null;
					if (rs.next()) {
						customer = new Customer();
						customer.setCustomerId(rs.getLong("id"));
						customer.setAccountNumber(rs.getString("account_number"));
						customer.setFirstName(rs.getString("first_name"));
						customer.setMiddleName(rs.getString("middle_name"));
						customer.setLastName(rs.getString("last_name"));
						customer.setHomePhone(rs.getString("home_phone"));
						customer.setWorkPhone(rs.getString("work_phone"));
						customer.setContactPhone(rs.getString("contact_phone"));
						customer.setEmail(rs.getString("email"));
						customer.setDob(rs.getString("dob"));
						customer.setAttrib2(rs.getString("attrib2"));
						customer.setAttrib3(rs.getString("attrib3"));
						customer.setCustomerBlocked(rs.getString("blocked_flag"));
					}
					return customer;
				}
			});
		} catch (Exception e) {
			throw new Exception("Error while fetching the customer data!");
		}
	}

	public Customer getCustomerIncludingDeletedCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from customer customer where customer.id=");
			sql.append(customerId);
			customLogger.debug("getCustomer SQL: " + sql.toString());
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Customer>() {
				@Override
				public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Customer customer = null;
					if (rs.next()) {
						customer = new Customer();
						customer.setCustomerId(rs.getLong("id"));
						customer.setAccountNumber(rs.getString("account_number"));
						customer.setFirstName(rs.getString("first_name"));
						customer.setMiddleName(rs.getString("middle_name"));
						customer.setLastName(rs.getString("last_name"));
						customer.setHomePhone(rs.getString("home_phone"));
						customer.setWorkPhone(rs.getString("work_phone"));
						customer.setContactPhone(rs.getString("contact_phone"));
						customer.setEmail(rs.getString("email"));
					}
					return customer;
				}
			});
		} catch (Exception e) {
			throw new Exception("Error while fetching the customer data!");
		}
	}

	public void holdReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final String transId, final String companyId,
			final String procedureId, final String locationId, final String departmentId, final String eventId, final String eventDateTimeId, final String seatId,
			final String customerId, final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, final HoldResvResponse holdResponse) throws Exception {

		holdResponse.setResponseStatus(true);
		boolean isAvailable = isSeatAvailable(jdbcCustomTemplate, customLogger, eventDateTimeId, seatId, cdConfig);

		if (isAvailable) {
			if (customerId == null || (customerId != null && customerId.trim().length() == 0)) {
				customLogger.error("customerId is Empty or NULL - customerId:" + customerId);
				throw new Exception(FlowStateConstants.HOLD_RESERVATION.getValue());
			}

			jdbcCustomTemplate.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
					try {
						final StringBuilder sql = new StringBuilder();
						sql.append("insert into schedule (timestamp,status,trans_id,");
						if (Long.valueOf(companyId) > 0) {
							sql.append("company_id,");
						}
						if (Long.valueOf(procedureId) > 0) {
							sql.append("procedure_id,");
						}

						if (Long.valueOf(departmentId) > 0) {
							sql.append("department_id,");
						}
						sql.append("location_id,event_id,");
						sql.append(" event_date_time_id,seat_id,customer_id) values (");
						sql.append(" CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
						sql.append(",?,?,?,");

						if (Long.valueOf(companyId) > 0) {
							sql.append("?,");
						}
						if (Long.valueOf(procedureId) > 0) {
							sql.append("?,");
						}

						if (Long.valueOf(departmentId) > 0) {
							sql.append("?,");
						}

						sql.append("?,?,?,?)");

						KeyHolder holder = new GeneratedKeyHolder();
						int scCount = jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
							int i = 1;

							@Override
							public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
								PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
								ps.setInt(i++, ReservataionStatus.HOLD.getStatus());
								ps.setLong(i++, Long.valueOf(transId));
								if (Long.valueOf(companyId) > 0) {
									ps.setLong(i++, Long.valueOf(companyId));
								}

								if (Long.valueOf(procedureId) > 0) {
									ps.setLong(i++, Long.valueOf(procedureId));
								}

								if (Long.valueOf(departmentId) > 0) {
									ps.setLong(i++, Long.valueOf(departmentId));
								}
								ps.setLong(i++, Long.valueOf(locationId));
								ps.setLong(i++, Long.valueOf(eventId));
								ps.setLong(i++, Long.valueOf(eventDateTimeId));
								ps.setLong(i++, Long.valueOf(seatId));
								ps.setLong(i++, Long.valueOf(customerId));
								return ps;
							}
						}, holder);
						Long scheduleId = holder.getKey().longValue();
						sql.setLength(0);
						sql.append("update seat set schedule_id = ? where id = ? ");
						int seatUpdateCount = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { scheduleId, Long.valueOf(seatId) });

						if (scCount > 0 && seatUpdateCount > 0) {
							sql.setLength(0);
							holdResponse.setScheduleId(String.valueOf(scheduleId));
						} else {
							customLogger.warn("Something wrong while doing hold reservation!");
							throw new Exception("Hold Reservation failed!");
						}
					} catch (Exception e) {
						transactionStatus.setRollbackOnly();
						throw new TelAppointDBException(FlowStateConstants.HOLD_RESERVATION.getValue(), e);
					}
				}
			});
		} else if ("Y".equals(resvSysConfig.getDisplaySeat())) {
			holdResponse.setResponseStatus(false);
			holdResponse.setResponseMessage(IVRVxmlConstants.ONE_SEAT_NOT_AVAILABLE.getPageKey());
		} else {
			Long nextSeatId = getNextSeatId(jdbcCustomTemplate, customLogger, eventDateTimeId);
			if (nextSeatId.longValue() == 0) {
				holdResponse.setResponseStatus(false);
				holdResponse.setResponseMessage(IVRVxmlConstants.ALL_SEATS_NOT_AVAILABLE.getPageKey());
			} else {
				holdReservation(jdbcCustomTemplate, customLogger, device, transId, companyId, procedureId, locationId, departmentId, eventId, eventDateTimeId,
						String.valueOf(nextSeatId), customerId, resvSysConfig, cdConfig, holdResponse);
			}
		}
	}

	public void confirmReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final String langCode, final String token,
			final String transId, final String scheduleId, final String customerId, final String comment, final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig,
			final ConfirmResvResponse confirmResponse) throws Exception {

		jdbcCustomTemplate.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
				customLogger.debug("scheduleId1:" + scheduleId);

				try {
					if (scheduleId != null && scheduleId.length() > 0 && Long.valueOf(scheduleId) <= 0) {
						customLogger.error("scheduleId is less then or equals to zero");
						throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
					}

					if (customerId == null || (customerId != null && customerId.trim().length() == 0)) {
						customLogger.error("customerId is Empty or NULL - customerId:" + customerId);
						throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
					}

					if (Long.valueOf(customerId) <= 0) {
						customLogger.error("customerId not passed from front end to update the schedule table, Passed customerId:" + customerId);
						throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
					}

					Schedule schedule = getSchedule(jdbcCustomTemplate, customLogger, scheduleId);
					int resvStatus = schedule.getStatus();
					String reminderStatus = schedule.getReminderStatus();

					if (ReservataionStatus.HOLD.getStatus() != resvStatus) {
						customLogger.error("Reservation status is not in HOLD state - scheduleId:" + scheduleId);
						throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
					}

					if (ReservataionStatus.RELEASE_HOLD.getStatus() == resvStatus) {
						customLogger.error("Reservation status in relase state, Please try again");
						throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
					}

					final StringBuilder sql = new StringBuilder();
					sql.append("update schedule set status = ? , customer_id = ?, comments = ? where id = ?");
					int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(),
							new Object[] { ReservataionStatus.CONFIRM.getStatus(), Long.valueOf(customerId), comment, Long.valueOf(scheduleId) });

					if (count > 0) {
						sql.setLength(0);
						sql.append("insert into reservation (timestamp,trans_id,schedule_id,appt_method,appt_type,lang_code,outlook_google_sync)  values (");
						sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
						sql.append(" ,?,?,?,?,?,?)");

						KeyHolder holder = new GeneratedKeyHolder();
						count = jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
							int i = 1;

							@Override
							public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
								PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
								ps.setLong(i++, Long.valueOf(transId));
								ps.setLong(i++, Long.valueOf(scheduleId));
								if (CoreUtils.isOnline(device)) {
									ps.setInt(i++, ReservationMethod.ONLINE.getMethod());
								} else if (CoreUtils.isAdmin(device)) {
									ps.setInt(i++, ReservationMethod.ADMIN.getMethod());
								} else if (CoreUtils.isIVR(device)) {
									ps.setInt(i++, ReservationMethod.IVR.getMethod());
								}
								ps.setInt(i++, ReservationtType.MAKE.getType());
								ps.setString(i++, langCode);
								ps.setString(i++, "N");
								return ps;
							}
						}, holder);
						Long confNumber = holder.getKey().longValue();
						if (count > 0) {
							customLogger.debug("Preparing the confirmation response object here!");
							Object whereClause[] = null;
							Campaign campaign = null;
							boolean isReminderStatus = reminderStatus.equals("Y");
							if (isReminderStatus) {
								campaign = new Campaign();
								whereClause = new Object[27];
								getCampaignById(jdbcCustomTemplate, customLogger, langCode, device, 1, true, campaign);
							}
							populateResponse(jdbcCustomTemplate, customLogger, device, confNumber, cdConfig, resvSysConfig, confirmResponse);

							if (isReminderStatus) {
								prepareNotify(whereClause, customLogger, campaign, schedule, Long.valueOf(customerId), resvSysConfig, confirmResponse);
								addTONotify(jdbcCustomTemplate, customLogger, whereClause, resvSysConfig);
							}
							updateBookedSeat(jdbcCustomTemplate, customLogger, schedule.getEventDateTimeId(),true);
							expireTheToken(jdbcCustomTemplate, customLogger, jdbcCustomTemplate.getClientCode(), device, token, cdConfig);
							confirmResponse.setDbReservationDate(null);
							confirmResponse.setDbReservationTime(null);
						} else {
							customLogger.error("Failed to save in Reservation table.");
							throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
						}
					} else {
						customLogger.error("Failed to update reservation status in schedule table.");
						throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
					}
				} catch (Exception e) {
					customLogger.error("Error: so rollback the transaction!!", e);
					System.out.println(":::BOOKED_SEATS:::booked reservation");
					paramTransactionStatus.setRollbackOnly();
					throw new TelAppointDBException(FlowStateConstants.CONFIRM_RESERVATION.getValue(), e);
				}
			}
		});
	}
	
	public void updateBookedSeat(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long eventDateTimeId, boolean isIncrease) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("update event_date_time set booked_seats = ");
			if(isIncrease) {
				sql.append("booked_seats+1 ");
			} else {
				sql.append("booked_seats-1 ");
			}
			sql.append(" where id=?");
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] {eventDateTimeId});
		} catch (Exception e) {
			throw new Exception("Error while updating the booked seats count!");
		}
	}

	private long getNextSeatId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String eventDateTimeId) {
		String sql = "select id from seat where event_date_time_id = ? and schedule_id = 0 and reserved = 'N' order by id asc";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventDateTimeId) }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					return rs.getLong("id");
				}
				return new Long(0);
			}
		});
	}

	public String getReservationStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select s.status as status, e.send_reminder as sendReminder from schedule s, event e where id=? and s.event_id= e.event_id");
			customLogger.debug("scheduleId:" + scheduleId);
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(scheduleId) }, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getInt("status") + "|" + rs.getString("sendReminder");
					}
					return "";
				}
			});
		} catch (Exception e) {
			throw new Exception("Error while fetching the schedule data!");
		}
	}

	public Schedule getSchedule(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select s.id, s.status as status, e.send_reminder as sendReminder, s.event_id as eventId, s.customer_id as customerId,");
			sql.append("s.location_id as locationId, s.seat_id as seatId, event_date_time_id as eventDateTimeId");
			sql.append(" from schedule s, event e where id=? and s.event_id= e.event_id");
			customLogger.debug("scheduleId:" + scheduleId);

			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(scheduleId) }, new ResultSetExtractor<Schedule>() {
				@Override
				public Schedule extractData(ResultSet rs) throws SQLException, DataAccessException {
					Schedule schedule = null;
					if (rs.next()) {
						schedule = new Schedule();
						schedule.setScheduleId(rs.getLong("id"));
						schedule.setStatus(rs.getInt("status"));
						schedule.setReminderStatus(rs.getString("sendReminder"));
						schedule.setEventId(rs.getLong("eventId"));
						schedule.setSeatId(rs.getLong("seatId"));
						schedule.setLocationId(rs.getInt("locationId"));
						schedule.setEventDateTimeId(rs.getLong("eventDateTimeId"));
					}
					return schedule;
				}
			});
		} catch (Exception e) {
			throw new Exception("Error while fetching the schedule data!");
		}
	}

	private synchronized boolean isSeatAvailable(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String eventDateTimeId, String seatId, ClientDeploymentConfig cdConfig)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from seat s");
		sql.append(" where s.id=? and s.event_date_time_id=? and s.schedule_id = 0 and s.reserved = 'N'");
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[] { Long.valueOf(seatId), Long.valueOf(eventDateTimeId) });
		if (count > 0) {
			sql.setLength(0);
			sql.append("update seat s set s.schedule_id=1 ");
			sql.append(" where s.id=? and s.event_date_time_id=? and s.schedule_id = 0 and s.reserved = 'N'");
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { Long.valueOf(seatId), Long.valueOf(eventDateTimeId) });
			return true;
		}
		customLogger.debug("There is no seats available for the seat:" + seatId + ", eventDateTimeId:" + eventDateTimeId);
		return false;
	}

	public void expireTheToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device, String token, ClientDeploymentConfig cdConfig)
			throws Exception {
		String sql = "update tokens set expiry_stamp=CONVERT_TZ(now(),'US/Central','" + cdConfig.getTimeZone() + "') where  token=? and client_code=? and device=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[] { token, clientCode, device });
	}

    // not in use
	public int getReservationStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId, String customerId) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select status from schedule where id= ? and customer_id= ?");
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(scheduleId), Long.valueOf(customerId) },
					new ResultSetExtractor<Integer>() {
						@Override
						public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								return rs.getInt("status");
							}
							return -1;
						}
					});
		} catch (Exception e) {
			throw new Exception("Error while fetching the schedule data!");
		}
	}

	public void populateResponse(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, Long confNumber,
			final ClientDeploymentConfig cdConfig, final ResvSysConfig resvSysConfig, final ConfirmResvResponse confirmResponse) throws Exception {

		String displayDateFormat = getMariaDBDateFormat(cdConfig.getFullTextualdayFormat());
		
		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_HHMMZZZZ.getValue());

		StringBuilder sql = new StringBuilder();
		sql.append("select r.conf_number, ");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(displayDateFormat).append("')").append("as displayDate,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time, edt.date as dbDate, edt.time as dbTime,");
		sql.append("c.company_name_online,c.company_name_ivr_tts,c.company_name_ivr_audio,");
		sql.append("p.procedure_name_online,p.procedure_name_ivr_tts,p.procedure_name_ivr_audio,");
		sql.append("l.location_name,l.location_name_ivr_tts,l.location_name_ivr_audio,l.time_zone,");
		sql.append("d.department_name_online,d.department_name_ivr_tts,department_name_ivr_audio,");
		sql.append("e.event_name,e.duration,e.event_name_ivr_tts,e.event_name_ivr_audio,");
		sql.append("s.display_seat_number,s.tts,s.audio,");
		sql.append("cu.first_name,cu.last_name, cu.email,cu.contact_phone");
		sql.append(" from reservation r,schedule sc, company c, `procedure` p, location l,");
		sql.append(" department d, event e, seat s, event_date_time edt,customer cu");
		sql.append(" where r.conf_number = ? and r.schedule_id = sc.id");
		sql.append(" and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id ");
		sql.append(" and sc.department_id=d.id and sc.event_id =e.event_id ");
		sql.append(" and sc.event_date_time_id = edt.id and sc.seat_id=s.id");
		sql.append(" and sc.customer_id = cu.id");

		customLogger.debug("Confirm Response SQL: " + sql.toString());
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { confNumber }, new ResultSetExtractor<ConfirmResvResponse>() {
			@Override
			public ConfirmResvResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					String displayDate = rs.getString("displayDate");
					String time = rs.getString("time");
					String date = rs.getString("date");

					confirmResponse.setConfNumber("" + rs.getLong("conf_number"));
					confirmResponse.setFirstName(rs.getString("first_name"));
					confirmResponse.setLastName(rs.getString("last_name"));
					confirmResponse.setEmail(rs.getString("email"));
					confirmResponse.setHomePhone(rs.getString("contact_phone"));
					String timeZone = rs.getString("time_zone");
					confirmResponse.setDuration(rs.getInt("duration"));
					timeZone = (timeZone == null) ? cdConfig.getTimeZone() : timeZone;

					confirmResponse.setTimeZone(timeZone);
					confirmResponse.setSeatNumber(rs.getString("display_seat_number"));

					if (CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
						confirmResponse.setDisplayDate(displayDate);
						confirmResponse.setDisplayTime(time);
						confirmResponse.setReservationDate(date);
						confirmResponse.setReservationTime(time);
					} else if (CoreUtils.isIVR(device)) {
						confirmResponse.setReservationDate(displayDate);
						confirmResponse.setReservationTime(time);
					}
					// this two fields required for add the date in notify table - due_date_time
					confirmResponse.setDbReservationDate(rs.getString("dbDate"));
					confirmResponse.setDbReservationTime(rs.getString("dbTime"));

					if (CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
						confirmResponse.setCompanyName(rs.getString("company_name_online"));
						confirmResponse.setProcedure(rs.getString("procedure_name_online"));
						confirmResponse.setLocation(rs.getString("location_name"));
						confirmResponse.setDepartment(rs.getString("department_name_online"));
						confirmResponse.setEvent(rs.getString("event_name"));
					} else if (CoreUtils.isIVR(device)) {
						confirmResponse.setCompanyName(rs.getString("company_name_ivr_tts"));
						confirmResponse.setProcedure(rs.getString("procedure_name_ivr_tts"));
						confirmResponse.setLocation(rs.getString("location_name_ivr_tts"));
						confirmResponse.setDepartment(rs.getString("department_name_ivr_tts"));
						confirmResponse.setEvent(rs.getString("event_name_ivr_tts"));
					}
					customLogger.debug("date:" + confirmResponse.getReservationDate() + " " + confirmResponse.getReservationTime());
				}
				return confirmResponse;
			}
		});

	}

	public boolean updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long transId, Integer state, ClientDeploymentConfig cdConfig)
			throws Exception {
		StringBuilder sql = new StringBuilder("insert into trans_state(trans_id,timestamp,state) values (?, ");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("'),?)");
		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { transId, state });
		boolean stateStatus = false;
		if (count > 0) {
			stateStatus = true;
		}
		return stateStatus;
	}
	
	public void getEventDates(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, String eventId, String locationId,
			final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, final Map<String, EventDate> availableDateMap) throws Exception {

		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct");
		sql.append(" DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as availDate");
		sql.append(" from event_date_time edt, seat s");
		sql.append(" where edt.event_id = ? and edt.location_id = ? and edt.enable = 'Y' and edt.num_seats > 0");
		sql.append(" and edt.date > CURRENT_DATE and edt.date < DATE_ADD(CURRENT_DATE, INTERVAL ").append(resvSysConfig.getMaxApptDurationDays()).append(" DAY)");
		sql.append(" and edt.id = s.event_date_time_id and s.schedule_id = 0 and s.reserved = 'N' order by edt.date asc");
		customLogger.debug("getEventDates SQL: " + sql.toString());
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventId), Long.valueOf(locationId) },
					new ResultSetExtractor<Map<String, EventDate>>() {
						@Override
						public Map<String, EventDate> extractData(ResultSet rs) throws SQLException, DataAccessException {
							EventDate availableDate = null;
							while (rs.next()) {
								availableDate = new EventDate();
								if(CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
									availableDate.setStatus("0");
								}
								String date = rs.getString("availDate");
								availableDate.setDate(date);
								availableDate.setTimeZone(cdConfig.getTimeZone());
								availableDateMap.put(date, availableDate);
							}
							return availableDateMap;
						}
					});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_DATES_LIST.getValue(), e);
		}
		return;
	}

	public void getBookedDates(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, String eventId, String locationId, final Map<String, EventDate> bookedDateMap)
			throws Exception {
		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct ");
		sql.append(" DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as bookedDate");
		sql.append(" from event e, event_date_time edt, seat s");
		sql.append(" where e.event_id = ? and edt.location_id= ? and e.event_id = edt.event_id and edt.id = s.event_date_time_id");
		sql.append(" and edt.date > CURRENT_DATE and edt.date NOT IN");
		sql.append("(select distinct edt.date from event e, event_date_time edt, seat s where ");
		sql.append(" e.event_id = ? and e.event_id = edt.event_id and edt.date > CURRENT_DATE");
		sql.append(" and edt.id = s.event_date_time_id and s.schedule_id = 0 and s.reserved = 'N' )");
		sql.append(" order by edt.date");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventId), Long.valueOf(locationId), Long.valueOf(eventId) },
					new ResultSetExtractor<Map<String, EventDate>>() {
						@Override
						public Map<String, EventDate> extractData(ResultSet rs) throws SQLException, DataAccessException {
							EventDate availableDate = null;
							while (rs.next()) {
								availableDate = new EventDate();
								if(CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
									availableDate.setStatus("1");
								}
								String date = rs.getString("bookedDate");
								availableDate.setDate(date);
								bookedDateMap.put(date, availableDate);
							}
							return bookedDateMap;
						}
					});

		} catch (Exception e) {
			throw new Exception("Failed in booked dates!", e);
		}
		return;
	}

	public String getMariaDBDateFormat(String dateFormat) {
		if (CommonDateContants.FULLTEXTUAL_DAY_FORMAT.getValue().equals(dateFormat)) {
			return "%W, %b %d, %Y";
		} else if (CommonDateContants.DATE_FORMAT_YYYYMMDD.getValue().equals(dateFormat)) {
			return "%Y-%m-%d";
		} else {
			return "%m/%d/%Y";
		}
	}

	public String getMariaDBTimeFormat(String timeFormat) {
		if (CommonDateContants.TIME_FORMAT_HMMA.getValue().equals(timeFormat)) {
			return "%l:%i %p";
		} else if (CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue().equals(timeFormat)) {
			return "%h:%i %p";
		} else if (CommonDateContants.TIME_FORMAT_HHMMZZZZ.getValue().equals(timeFormat)) {
			return "%h:%i %p";
		} else {
			return "%h:%i %p";
		}
	}

	public void populateAlias(Map<String, String> aliasMap, Object responseObject, String device) {
		if (aliasMap != null && !aliasMap.isEmpty() && CoreUtils.isOnline(device)) {
			if (responseObject instanceof VerifyResvResponse) {
				VerifyResvResponse verifyResvResponse = (VerifyResvResponse) responseObject;
				String companyNameAliasValue = aliasMap.get(verifyResvResponse.getCompanyName());
				String procedureNameAliasValue = aliasMap.get(verifyResvResponse.getProcedure());
				String locationNameAliasValue = aliasMap.get(verifyResvResponse.getLocation());
				String departmentAliasValue = aliasMap.get(verifyResvResponse.getDepartment());
				String eventNameAliasValue = aliasMap.get(verifyResvResponse.getEvent());
				if (companyNameAliasValue != null) {
					verifyResvResponse.setCompanyName(companyNameAliasValue.trim());
				}
				if (procedureNameAliasValue != null) {
					verifyResvResponse.setProcedure(procedureNameAliasValue.trim());
				}
				if (locationNameAliasValue != null) {
					verifyResvResponse.setLocation(locationNameAliasValue.trim());
				}

				if (departmentAliasValue != null) {
					verifyResvResponse.setDepartment(departmentAliasValue.trim());
				}

				if (eventNameAliasValue != null) {
					verifyResvResponse.setEvent(eventNameAliasValue.trim());
				}
			} else if (responseObject instanceof ConfirmResvResponse) {
				ConfirmResvResponse confirmResvResponse = (ConfirmResvResponse) responseObject;
				String companyNameAliasValue = aliasMap.get(confirmResvResponse.getCompanyName());
				String procedureNameAliasValue = aliasMap.get(confirmResvResponse.getProcedure());
				String locationNameAliasValue = aliasMap.get(confirmResvResponse.getLocation());
				String departmentAliasValue = aliasMap.get(confirmResvResponse.getDepartment());
				String eventNameAliasValue = aliasMap.get(confirmResvResponse.getEvent());
				if (companyNameAliasValue != null) {
					confirmResvResponse.setCompanyName(companyNameAliasValue.trim());
				}
				if (procedureNameAliasValue != null) {
					confirmResvResponse.setProcedure(procedureNameAliasValue.trim());
				}
				if (locationNameAliasValue != null) {
					confirmResvResponse.setLocation(locationNameAliasValue.trim());
				}

				if (departmentAliasValue != null) {
					confirmResvResponse.setDepartment(departmentAliasValue.trim());
				}
				if (eventNameAliasValue != null) {
					confirmResvResponse.setEvent(eventNameAliasValue.trim());
				}
			}
		}
	}

	public void populateAlias(Map<String, String> aliasMap, List<Options> options) throws Exception {
		String fieldName = "optionValue";
		for (Options option : options) {
			Object key = CoreUtils.getPropertyValue(option, fieldName);
			if (aliasMap != null) {
				String aliasValue = aliasMap.get((String) key);
				if (key != null && aliasValue != null) {
					CoreUtils.setPropertyValue(option, fieldName, aliasValue.trim());
				}
			}
		}
	}

	public List<Integer> getListFromCommaSeperatedString(String scheduleIds) {
		List<Integer> list = new ArrayList<Integer>();
		for (String s : scheduleIds.split(","))
			list.add(Integer.valueOf(s));

		return list;
	}

	public void getCampaigns(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			final CampaignResponse campaignResponse) throws Exception {
		List<Campaign> campaignList = getCampaigns(jdbcCustomTemplate, customLogger, langCode, device, isFullData);
		campaignResponse.setCampaignList(campaignList);
	}

	public List<Campaign> getCampaigns(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData) throws Exception {
		StringBuilder sql = new StringBuilder("select id,name, notify_by_phone,notify_by_phone_confirm,notify_by_sms,");
		sql.append("notify_by_sms_confirm,notify_by_email,notify_by_email_confirm, notify_by_push_notification, active from campaign where id > 0");
		try {
			final List<Campaign> campaignList = new ArrayList<Campaign>();
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Campaign>>() {

				@Override
				public List<Campaign> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Campaign campaign = null;
					while (rs.next()) {
						campaign = new Campaign();
						campaign.setCampaignId(rs.getLong("id"));
						campaign.setCampaignName(rs.getString("name"));
						if (isFullData) {
							campaign.setNotifyByEmail(rs.getString("notify_by_email"));
							campaign.setNotifyBySMS(rs.getString("notify_by_sms"));
							campaign.setNotifyByPhone(rs.getString("notify_by_phone"));
							campaign.setNotifyByEmailConfirm(rs.getString("notify_by_email_confirm"));
							campaign.setNotifyBySMSConfirm(rs.getString("notify_by_sms_confirm"));
							campaign.setNotifyByPhoneConfirm(rs.getString("notify_by_phone_confirm"));
							campaign.setNotifyByPushNotification(rs.getString("notify_by_push_notification"));
							campaign.setActive(rs.getString("active"));
						} else {
							campaign.setActive(null);
						}
						campaignList.add(campaign);
					}
					return campaignList;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_CAMPAIGNS.getValue(), e);
		}

	}

	public void getCampaignById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, long campaignId, final boolean isFullData,
			final Campaign campaign) {
		StringBuilder sql = new StringBuilder("select id,name, notify_by_phone,notify_by_phone_confirm,notify_by_sms,");
		sql.append("notify_by_sms_confirm,notify_by_email,notify_by_email_confirm, notify_by_push_notification, active from campaign where id=?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { campaignId }, new ResultSetExtractor<Campaign>() {
				@Override
				public Campaign extractData(ResultSet rs) throws SQLException, DataAccessException {
					while (rs.next()) {
						campaign.setCampaignId(rs.getLong("id"));
						campaign.setCampaignName(rs.getString("name"));
						if (isFullData) {
							campaign.setNotifyByEmail(rs.getString("notify_by_email"));
							campaign.setNotifyBySMS(rs.getString("notify_by_sms"));
							campaign.setNotifyByPhone(rs.getString("notify_by_phone"));
							campaign.setNotifyByEmailConfirm(rs.getString("notify_by_email_confirm"));
							campaign.setNotifyBySMSConfirm(rs.getString("notify_by_sms_confirm"));
							campaign.setNotifyByPhoneConfirm(rs.getString("notify_by_phone_confirm"));
							campaign.setNotifyByPushNotification(rs.getString("notify_by_push_notification"));
							campaign.setActive(rs.getString("active"));
						} else {
							campaign.setActive(null);

						}
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_CAMPAIGN_BY_ID.getValue(), e);
		}
		return;
	}

	private void prepareNotify(Object[] obj, CustomLogger customLogger, Campaign campaign, Schedule schedule, long customerId, ResvSysConfig resvSysConfig, ConfirmResvResponse confirmResponse) throws Exception {
		int i = 0;
		String notes = "";
		// obj[i++] = new Timestamp(System.currentTimeMillis());
		obj[i++] = campaign.getCampaignId(); // compaignId
		// obj[i++] = Character.valueOf('N'); // call_now
		// obj[i++] = Character.valueOf('N'); // emergency_notify
		// obj[i++] = Character.valueOf('N'); // broadcast_mode
		obj[i++] = 1; // notify_status
		obj[i++] = schedule.getLocationId();
		obj[i++] = schedule.getEventId();
		obj[i++] = schedule.getSeatId();
		obj[i++] = customerId;// customerId
		obj[i++] = schedule.getScheduleId();// schedule_id
		obj[i++] = confirmResponse.getFirstName();// firstName
		obj[i++] = confirmResponse.getLastName();// lastName
		
		if("Y".equals(resvSysConfig.getRunPhoneTypeLookup())) {
			//TODO: need to write method to get the accountSid and authtoken from master-db - smsconfig table.
			String type = null;
			try {
				type = MobileNumberLookup.getMobileType(confirmResponse.getHomePhone(), "AC4458b0b635416cba4238effa76e78e84", "31056ad9bdd97443ec55231d6c966881");
				if("mobile".equals(type)) {
					obj[i++] = "";// home_phone
					obj[i++] = confirmResponse.getHomePhone();// cell_phone
				} else {
					obj[i++] = confirmResponse.getHomePhone();// home_phone
					obj[i++] = "";// cell_phone
				}
			} catch(Exception e) {
				customLogger.error("Error while mobile lookup : "+e,e);
				notes = "Invalid Number: "+confirmResponse.getHomePhone();
				obj[i++] = "";// home_phone
				obj[i++] = "";// cell_phone
			}
			
		} else {
			obj[i++] = confirmResponse.getHomePhone();// home_phone
			obj[i++] = "";// cell_phone
		}
		obj[i++] = confirmResponse.getEmail();// email
		obj[i++] = "";// email_cc
		// obj[i++] = "";// lang_id
		// obj[i++] = Character.valueOf('N');// notify_preference

		// all below should be based on comapign settings.
		obj[i++] = campaign.getNotifyByPhone();// notify_by_phone
		obj[i++] = campaign.getNotifyByPhoneConfirm();// notify_by_phone_confirm
		obj[i++] = campaign.getNotifyBySMS();// notify_by_sms
		obj[i++] = campaign.getNotifyBySMSConfirm();// notify_by_sms_confirm
		obj[i++] = campaign.getNotifyByEmail();// notify_by_email
		obj[i++] = campaign.getNotifyByEmailConfirm();// notify_by_email_confirm

		obj[i++] = campaign.getNotifyByPushNotification();// notify_by_push_notif
		obj[i++] = 0;// notify_phone_status
		obj[i++] = 0;// notify_sms_status
		obj[i++] = 0;// notify_email_status
		obj[i++] = 0;// notify_push_notification_status
		// obj[i++] = Character.valueOf('N');// not not notify
		// obj[i++] = Character.valueOf('N');// delete flag

		String date = confirmResponse.getDbReservationDate();
		String time = confirmResponse.getDbReservationTime();
		obj[i++] = date + " " + time;// due_date_time
		obj[i++] = notes;
		obj[i++] = schedule.getEventDateTimeId();
	}

	public boolean addTONotify(final JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Object[] whereClause, ResvSysConfig resvSysConfig) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into notify ").append("(");
		sql.append(" timestamp,campaign_id,");
		sql.append(" notify_status,location_id, event_id, seat_id, customer_id,schedule_id,first_name,");
		sql.append(" last_name,home_phone,cell_phone,email,email_cc,");
		sql.append(" notify_by_phone,notify_by_phone_confirm,notify_by_sms,notify_by_sms_confirm,");
		sql.append(" notify_by_email,notify_by_email_confirm,notify_by_push_notif,notify_phone_status,");
		sql.append(" notify_sms_status,notify_email_status,notify_push_notification_status, due_date_time,notes,event_date_time_id");
		sql.append(")").append(" values (now(),?,?,?,?,");
		sql.append("?,?,?,?,?,");
		sql.append("?,?,?,?,?,");
		sql.append("?,?,?,?,?,");
		sql.append("?,?,?,?,?,?,?,?)");

		try {
			int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), whereClause);
			if (count > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new Exception("Failed in notify table save!", e);
		}
	}
	
	public boolean isGreaterThenTodayDate(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String date, String time, ClientDeploymentConfig cdConfig) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append("'").append(date).append(" ").append(time).append("'");
		sql.append(" > ").append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("') as isGreaterThenToday");
		int isGreater = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString());
		if(isGreater > 0) {
			return true;
		}
		return false;
	}

	
	public boolean isAllowDuplicateResv(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId,long eventId) throws Exception {
		Customer customer = getCustomer(jdbcCustomTemplate, customLogger, customerId);
		if (customer == null) {
			customLogger.info("Customer is not found!. So not allowing the appt");
			return false;
		}

		if ("Y".equals(customer.getCustomerBlocked())) {
			customLogger.info("We should not allow the appointment for blocked customer");
			return false;
		}
		return isDuplicateResv(jdbcCustomTemplate, customLogger, eventId, customerId);
	}

	public boolean isDuplicateResv(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long eventId, long customerId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(sh.id) from resv_sys_config a, event e, schedule sh,event_date_time edt");
		sql.append(" where a.term_start_date > '2000-01-01' and e.event_id = ?");
		sql.append(" and e.allow_duplicates = 'N' and a.one_appt_per_term = 'Y'");
		sql.append(" and sh.customer_id = ?");
		sql.append(" and sh.status = " + ReservataionStatus.CONFIRM.getStatus());
		sql.append(" and sh.event_id in (select event_id from event where allow_duplicates = 'N')");
		sql.append(" and sh.event_id=e.event_id and e.event_id = edt.event_id");
		sql.append(" and edt.date between a.term_start_date and a.term_end_date");
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[] { eventId, customerId });
		if (count > 0) {
			return false;
		}
		return true;
	}
}
