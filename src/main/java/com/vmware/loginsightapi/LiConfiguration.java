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

/**
 * This class builds configuration from the apache commons
 * properties file. or allow developer to programmatically build the
 * configuration.
 * 
 */
public class LiConfiguration {

	/**
	 * File name for the config file
	 */
	public final static String CONFIG_FILENAME = "config.properties";
	
	/**
	 * LogInsight Host field name
	 */
	public final static String CONFIG_KEY_LI_HOST = "loginsight.host";
	
	/**
	 * LogInsight Port field name
	 */
	public final static String CONFIG_KEY_LI_PORT = "loginsight.port";
	
	/**
	 * LogInsight User field name
	 */
	public final static String CONFIG_KEY_LI_USER = "loginsight.user";
	
	/**
	 * LogInsight password field name
	 */
	public final static String CONFIG_KEY_LI_PASSWORD = "loginsight.password";

	private PropertiesConfiguration config;

	/**
	 * Empty config constructor
	 */
	public LiConfiguration() {

	}

	/**
	 * Builds the configuration from properties file.
	 * 
	 * @throws ConfigurationException configuration exception
	 */
	public void buildConfig() throws ConfigurationException {
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(
				PropertiesConfiguration.class).configure(
						new Parameters().properties().setFileName(CONFIG_FILENAME).setThrowExceptionOnMissing(true)
								.setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
								.setIncludesAllowed(false));
		this.config = builder.getConfiguration();
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
		return this.config.getString(CONFIG_KEY_LI_HOST);
	}

	/**
	 * Getter for Loginsight server port
	 * 
	 * @return Loginsight server port (stringified format)
	 */
	public String getPort() {
		return this.config.getString(CONFIG_KEY_LI_PORT);
	}

	/**
	 * Getter for Loginsight user for authentication
	 * 
	 * @return Loginsight user
	 */
	public String getUser() {
		return this.config.getString(CONFIG_KEY_LI_USER);
	}

	/**
	 * Getter for Loginsight password for authentication
	 * 
	 * @return Loginsight password
	 */
	public String getPassword() {
		return this.config.getString(CONFIG_KEY_LI_PASSWORD);
	}

}
