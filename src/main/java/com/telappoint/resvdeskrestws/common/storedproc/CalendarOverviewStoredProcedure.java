package com.telappoint.resvdeskrestws.common.storedproc;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

import com.telappoint.resvdeskrestws.admin.constants.SPConstants;

/**
 * An access class for the reservation_overview stored procedures. This
 * interface expects no args for the store procedures
 *
 * @author Balaji N
 * 
 *         <li>arg5 (out): return code <li>arg6 (out): return message</ul>
 *         </pre>
 */
public class CalendarOverviewStoredProcedure extends StoredProcedure {
	public CalendarOverviewStoredProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, "reservation_overview");

		// Parameters should be declared in same order here that
		// they are declared in the stored procedure.
		RowMapper rowMapper = new CalendarOverviewRowMapper();
		declareParameter(new SqlReturnResultSet(SPConstants.RESULT_LIST.getValue(), rowMapper));
		declareParameter(new SqlParameter(SPConstants.START_DATE.getValue(), Types.VARCHAR));
		declareParameter(new SqlParameter(SPConstants.END_DATE.getValue(), Types.VARCHAR));
		
		declareParameter(new SqlOutParameter(SPConstants.RETURN_CODE.getValue(), Types.INTEGER));
		declareParameter(new SqlOutParameter(SPConstants.RETURN_MESSAGE.getValue(), Types.VARCHAR));
		compile();
	}

	public Map getCalendarView(String startDate, String endDate) {
		Map inParameters = new HashMap();
		inParameters.put(SPConstants.START_DATE.getValue(), startDate);
		inParameters.put(SPConstants.END_DATE.getValue(), endDate);
		Map out = execute(inParameters);
		return out;
	}
}