package com.telappoint.resvdeskrestws.common.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;

/**
 * 
 * @author Balaji Nandarapu
 *
 */

@Component
public class ConnectionPoolUtil {

	private static final String DRIVER = "org.mariadb.jdbc.Driver";
	private static final Map<String, JdbcCustomTemplate> datatSourceMap = new HashMap<String, JdbcCustomTemplate>();
	private static final Object lock = new Object();
	
	@Autowired
	private PropertySource propertySource;

	private GenericObjectPool connectionPool = null;

	/**
	 * 1) Creates an instance of GenericObjectPool that holds our pool of
	 * connections object. 2) Creates a connection factory object which will be
	 * use by the pool to create the connection object. We passes the JDBC url
	 * info, username and password. 3) Creates a PoolableConnectionFactory that
	 * will wraps the connection object created by the ConnectionFactory to add
	 * object pooling functionality.
	 * 
	 * 
	 * @param client
	 * @return
	 * @throws Exception
	 */
	public JdbcCustomTemplate getJdbcCustomTemplate(CustomLogger customLogger, Client client) throws Exception {
		if(client == null) {
			throw new Exception("Client not found!");	
		}
		
		String clientCode = client.getClientCode();
		JdbcCustomTemplate jdbcCustomTemplate = datatSourceMap.get(clientCode);
		if (jdbcCustomTemplate != null) {
			printStatus();
			return jdbcCustomTemplate;
		} else {	
			JdbcTemplate jdbcTemplate = new JdbcTemplate();
			try {
				Class.forName(ConnectionPoolUtil.DRIVER).newInstance();
				connectionPool = new GenericObjectPool();
				connectionPool.setMaxActive(Integer.valueOf(PropertyUtils.getValueFromProperties("MAX_ACTIVE",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()))); 
				connectionPool.setMinIdle(Integer.valueOf(PropertyUtils.getValueFromProperties("MIN_IDLE",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName())));
				
				String testOnBorrow = PropertyUtils.getValueFromProperties("testOnBorrow",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				String validationQuery = PropertyUtils.getValueFromProperties("validationQuery",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				String validationInterval = PropertyUtils.getValueFromProperties("validationInterval",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				String removeAbandoned = PropertyUtils.getValueFromProperties("removeAbandoned",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				String removeAbandonedTimeout = PropertyUtils.getValueFromProperties("removeAbandonedTimeout",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				String timeBetweenEvictionRunsMillis = PropertyUtils.getValueFromProperties("timeBetweenEvictionRunsMillis",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				
				if(testOnBorrow == null) testOnBorrow = "true";
				if(validationQuery == null) validationQuery = "select 1 from dual";
				if(validationInterval == null) validationInterval = "34000";
				if(removeAbandoned == null) removeAbandoned = "true";
				if(removeAbandonedTimeout == null) removeAbandonedTimeout = "54";
				if(timeBetweenEvictionRunsMillis == null) timeBetweenEvictionRunsMillis = "34000";
				
				connectionPool.setTestOnBorrow(Boolean.valueOf(testOnBorrow));
				Properties props = new Properties();
			    props.put("user", propertySource.getClientUserName());
			    props.put("password", propertySource.getClientPassword());
			    props.put("autoReconnect", true);
			    props.put("autoReconnectForPools", "true");
			    props.put("validationQuery", validationQuery);
			    props.put("validationInterval",Long.valueOf(validationInterval));
			    props.put("removeAbandoned",Boolean.valueOf(removeAbandoned));
			    props.put("removeAbandonedTimeout",Long.valueOf(removeAbandonedTimeout));	    
			    props.put("timeBetweenEvictionRunsMillis",Long.valueOf(timeBetweenEvictionRunsMillis));
			  	    
				ConnectionFactory cf = new DriverManagerConnectionFactory("jdbc:mysql://" + client.getDbServer() + "/" + client.getDbName(), props);
				new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
				DataSource dataSource = new PoolingDataSource(connectionPool);
				jdbcTemplate.setDataSource(dataSource);
				NamedParameterJdbcTemplate nameParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
				DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
				TransactionTemplate transactionTemplate = new TransactionTemplate(dataSourceTransactionManager);
				jdbcCustomTemplate = new JdbcCustomTemplate(jdbcTemplate, nameParameterJdbcTemplate,dataSourceTransactionManager,transactionTemplate);
				jdbcCustomTemplate.setClientCode(clientCode);
				synchronized (lock) {
					datatSourceMap.put(clientCode, jdbcCustomTemplate);	
				}
				printStatus();
			}catch(Exception e) {
				customLogger.error("Error while prepare the connection pool :",e);
				throw new Exception("Datasource failed: "+((e.getMessage() !=null)?e.getMessage():"") +e.toString());
			}
			return jdbcCustomTemplate;
		}
	}

	public GenericObjectPool getConnectionPool() {
		return connectionPool;
	}

	/**
	 * Prints connection pool status.
	 */
	private void printStatus() {
		System.out.println("Max   : " + getConnectionPool().getMaxActive() + "; " + "Active: " + getConnectionPool().getNumActive() + "; " + "Idle  : "
				+ getConnectionPool().getNumIdle());
	}
}