package com.telappoint.resvdeskrestws.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;

/**
 * 
 * @author Balaji N
 *
 */
public class RestTimeTakenFilter implements javax.servlet.Filter {

	private static Map<String, CustomLogger> map = new HashMap<String, CustomLogger>();

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		try {
			String isEnabledLoggingStr = PropertyUtils.getValueFromProperties("IS_ACCESS_LOG_ENABLED", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
			String threshold = PropertyUtils.getValueFromProperties("THRESHOLD", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
			String url = "unknown";
			boolean isEnableLogging = (isEnabledLoggingStr != null && "Y".equalsIgnoreCase(isEnabledLoggingStr))?true:false;
			if ( isEnableLogging) {
				if (request instanceof HttpServletRequest) {
					url = ((HttpServletRequest) request).getRequestURL().toString();
					String queryString = ((HttpServletRequest) request).getQueryString();
					if (queryString != null)
						url += "?" + queryString;
				}
			}
			
			filterChain.doFilter(request, response);
			
			if (isEnableLogging) {
				String fileName = "resvdesk-accesslogger";
				CustomLogger customLogger = null;
				if (map.containsKey(fileName)) {
					customLogger = map.get(fileName);
				} else {
					customLogger = new CustomLogger("resvdesk-accesslog", false, "ReservationDesk", "INFO");
				}	
				long stopTime = System.currentTimeMillis();
				
				if(threshold != null && (stopTime-startTime) > Integer.parseInt(threshold)) {
					customLogger.info(getLine(startTime, stopTime, url));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in logger creation");
			e.printStackTrace();
		}
	}

	public String getLine(long startTime, long endTime, String url) {
		long timems = endTime - startTime;
		StringBuilder sb = new StringBuilder();
		sb.append(url).append(" ");
		sb.append(timems / 1000).append("s").append(" ");// "time-taken"
		sb.append(timems).append("ms");// x-time-ms
		return sb.toString();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
