/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;

/**
 * This class builds configuration from the apache commons
 * properties file. or allow developer to programmatically build the
 * configuration.
 * 
 */
public class Configuration {

	/**
	 * File name for the config file
	 */
	public final static String FILENAME = "config.properties";
	
	/**
	 * Property key for host name
	 */
	public final static String KEY_LI_HOST = "loginsight.host";
	
	/**
	 * Property key for port
	 */
	public final static String KEY_LI_PORT = "loginsight.port";
	
	/**
	 * Property key for ingestion port
	 */
	public final static String KEY_LI_INGESTION_PORT = "loginsight.ingestion.port";
	
	/**
	 * Property key for user name
	 */
	public final static String KEY_LI_USER = "loginsight.user";
	
	/**
	 * Property key for password
	 */
	public final static String KEY_LI_PASSWORD = "loginsight.password";
	
	/**
	 * Property key for Connection scheme
	 */
	public static final String KEY_CONNECTION_SCHEME = "loginsight.connection.scheme";
	
	
	/**
	 * Default protocol scheme
	 */
	public static final String DEFAULT_SCHEME = "https";
		
	/**
	 * Default query port
	 */
	public static final int DEFAULT_PORT = 443;
	
	/**
	 * Default Ingestion Port
	 */
	public static final int DEFAULT_INGESTION_PORT = 9543;

	private PropertiesConfiguration config;
	
	private String host;
	private String user;
	private String password;
	private String port;
	private String ingestionPort;
	

	/**
	 * Empty config constructor
	 */
	public Configuration() {

	}

	/**
	 * Builds the configuration from properties file.
	 * 
	 * @param configFile Name of the configuration (full path or class path)
	 * @throws ConfigurationException configuration exception
	 */
	public static Configuration buildConfig(String configFile) throws ConfigurationException {
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(
				PropertiesConfiguration.class).configure(
						new Parameters().properties().setFileName(FILENAME).setThrowExceptionOnMissing(true)
								.setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
								.setIncludesAllowed(false));
		Configuration liConfig = new Configuration();
		liConfig.config = builder.getConfiguration();
		
		if(null != liConfig.config.getString(KEY_LI_HOST) && StringUtils.isNotEmpty(liConfig.config.getString(KEY_LI_HOST))) {
			liConfig.host = liConfig.config.getString(KEY_LI_HOST);
		} else {
			throw new ConfigurationException("Not a valid host name on the configuration");
		}
		
		if(null != liConfig.config.getString(KEY_LI_PORT) && StringUtils.isNotEmpty(liConfig.config.getString(KEY_LI_PORT))) {
			liConfig.port = liConfig.config.getString(KEY_LI_PORT);
		} else {
			liConfig.port = Integer.toString(DEFAULT_PORT);
		}
		
		if(null != liConfig.config.getString(KEY_LI_INGESTION_PORT) && StringUtils.isNotEmpty(liConfig.config.getString(KEY_LI_INGESTION_PORT))) {
			liConfig.ingestionPort = liConfig.config.getString(KEY_LI_INGESTION_PORT);
		} else {
			liConfig.ingestionPort = Integer.toString(DEFAULT_INGESTION_PORT);
		}
		
		if(null != liConfig.config.getString(KEY_LI_USER) && StringUtils.isNotEmpty(liConfig.config.getString(KEY_LI_USER))) {
			liConfig.user = liConfig.config.getString(KEY_LI_USER);
		} else {
			throw new ConfigurationException("Not a valid user name in the configuration");
		}
		
		if(null != liConfig.config.getString(KEY_LI_PASSWORD) && StringUtils.isNotEmpty(liConfig.config.getString(KEY_LI_PASSWORD))) {
			liConfig.password = liConfig.config.getString(KEY_LI_PASSWORD);
		} else {
			throw new ConfigurationException("Not a valid password in the configuration");
		}
		
		return liConfig;
	}

	public String getIngestionPort() {
		return ingestionPort;
	}

	public void setIngestionPort(String ingestionPort) {
		this.ingestionPort = ingestionPort;
	}

	public void setHost(String host) {
		
		this.host = host;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Sets the configuration using the supplied PropertiesConfiguration
	 * 
	 * @param config PropertiesConfiguration object
	 */
	public void setConfig(PropertiesConfiguration config) {
		this.config = config;
	}

	/**
	 * Returns the config object
	 * 
	 * @return PropertiesConfiguration object
	 */
	public PropertiesConfiguration getConfig() {
		return this.config;
	}

	/**
	 * Getter for Loginsight host name
	 * 
	 * @return Loginsight hostname
	 */
	public String getHost() {
		if(null == this.config.getString(KEY_LI_HOST) || StringUtils.isEmpty(this.config.getString(KEY_LI_HOST))) {
			 throw new LogInsightApiException("No host provided");
		} else {
			return this.config.getString(KEY_LI_HOST);
		}
	}

	/**
	 * Getter for Loginsight server port
	 * 
	 * @return Loginsight server port (stringified format)
	 */
	public String getPort() {
		return this.config.getString(KEY_LI_PORT);
	}

	/**
	 * Getter for Loginsight user for authentication
	 * 
	 * @return Loginsight user
	 */
	public String getUser() {
		return this.config.getString(KEY_LI_USER);
	}

	/**
	 * Getter for Loginsight password for authentication
	 * 
	 * @return Loginsight password
	 */
	public String getPassword() {
		return this.config.getString(KEY_LI_PASSWORD);
	}

}
