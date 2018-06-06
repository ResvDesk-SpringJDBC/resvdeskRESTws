package com.telappoint.resvdeskrestws.common.storedproc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.telappoint.resvdeskrestws.admin.model.CalendarOverview;
import com.telappoint.resvdeskrestws.common.utils.DateUtils;

/**
 * This class is used to convert ResultSet records to Domain Objects The class
 * is invoked for each Row in the ResultSet. It implements the Spring RowMapper
 * interface
 * 
 * @author Balaji N
 */

public class CalendarOverviewRowMapper implements RowMapper<CalendarOverview> {

	/**
	 * Maps the result set rows to a Claim object
	 */
	public CalendarOverview mapRow(ResultSet rs, int index) throws SQLException {

		CalendarOverview calendarOverview = new CalendarOverview();

		String eventName = StringUtils.trimToEmpty(rs.getString(""));
		String locationName = StringUtils.trimToEmpty(rs.getString(2));
		calendarOverview.setEventName(eventName);
		calendarOverview.setLocationName(locationName);

		String date = rs.getString(3);
		String time = rs.getString(4);
		try {
			calendarOverview.setDate(DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(date));
			calendarOverview.setTime(DateUtils.convert24To12HoursFormat(time));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		Long noOfSeats = rs.getLong(5);
		Long bookedSeats = rs.getLong(6);
		
		calendarOverview.setTotalSeats("" + noOfSeats.longValue());
		calendarOverview.setBookedSeats("" + bookedSeats.longValue());
		calendarOverview.setOpenSeats("" + ((Long)rs.getLong(7)).longValue());
		calendarOverview.setReservedSeats("" + ((Long)rs.getLong(8)).longValue());
		calendarOverview.setEventDateTimeId(rs.getLong(9));
		
		return calendarOverview;
	}
}