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

public class LiConfiguration {

	public final static String CONFIG_FILENAME = "config.properties";
	public final static String CONFIG_KEY_LI_HOST = "loginsight.host";
	public final static String CONFIG_KEY_LI_PORT = "loginsight.port";
	public final static String CONFIG_KEY_LI_USER = "loginsight.user";
	public final static String CONFIG_KEY_LI_PASSWORD = "loginsight.password";
	
	private PropertiesConfiguration config;

	public LiConfiguration() {

	}

	public void buildConfig() throws ConfigurationException {
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(
				PropertiesConfiguration.class).configure(
						new Parameters().properties().setFileName(CONFIG_FILENAME).setThrowExceptionOnMissing(true)
								.setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
								.setIncludesAllowed(false));
		this.config = builder.getConfiguration();
	}
	public void setConfig(PropertiesConfiguration config) {
		this.config = config;
	}
	public PropertiesConfiguration getConfig() {
		return this.config;
	}
	
	public String getHost() {
		return this.config.getString(CONFIG_KEY_LI_HOST);
	}
	public String getPort() {
		return this.config.getString(CONFIG_KEY_LI_PORT);
	}
	public String getUser() {
		return this.config.getString(CONFIG_KEY_LI_USER);	
	}
	public String getPassword() {
		return this.config.getString(CONFIG_KEY_LI_PASSWORD);
	}

}
