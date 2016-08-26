/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.util.Arrays;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;
import org.apache.commons.configuration2.io.ProvidedURLLocationStrategy;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class builds configuration from the apache commons properties file. or
 * allow developer to programmatically build the configuration. <br>
 * 
 * Configuration data will be set on a priority sequence <br>
 * 1. Load from configuration <br>
 * 2. Merge with environment variables <br>
 * 3. Override the values programmatically using "setter" methods <br>
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
	 * Property key for Connection scheme
	 */
	public static final String KEY_AGENT_ID = "loginsight.ingestion.agentId";

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

	String host;
	String user;
	String password;
	String port;
	String ingestionPort;
	String agentId;
	String scheme;

	private final static Logger logger = LoggerFactory.getLogger(Configuration.class);

	/**
	 * Empty config constructor
	 */
	public Configuration() {
		this.port = Integer.toString(DEFAULT_PORT);
		this.ingestionPort = Integer.toString(DEFAULT_INGESTION_PORT);
		this.scheme = DEFAULT_SCHEME;
		this.agentId = UUID.randomUUID().toString();
	}

	/**
	 * Config constructor with host, user and password
	 * 
	 * @param host
	 *            name of the loginsight host
	 * @param user
	 *            user name
	 * @param password
	 *            password
	 * 
	 */
	public Configuration(String host, String user, String password) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.port = Integer.toString(DEFAULT_PORT);
		this.ingestionPort = Integer.toString(DEFAULT_INGESTION_PORT);
		this.scheme = DEFAULT_SCHEME;
		this.agentId = UUID.randomUUID().toString();
	}

	/**
	 * Builds the configuration from a property map
	 * 
	 * @param configData
	 *            Config data in a hashmap
	 * @return Configuration object
	 * 
	 */
	public static Configuration buildConfig(Map<String, String> configData) {

		Configuration liConfig = new Configuration();

		liConfig.port = Integer.toString(DEFAULT_PORT);
		liConfig.ingestionPort = Integer.toString(DEFAULT_INGESTION_PORT);
		liConfig.scheme = DEFAULT_SCHEME;

		if (configData.containsKey(KEY_LI_HOST) && null != configData.get(KEY_LI_HOST)
				&& StringUtils.isNotEmpty(configData.get(KEY_LI_HOST))) {
			liConfig.host = configData.get(KEY_LI_HOST);
		}

		if (configData.containsKey(KEY_LI_PORT) && null != configData.get(KEY_LI_PORT)
				&& StringUtils.isNotEmpty(configData.get(KEY_LI_PORT))) {
			logger.info("setting port as well");
			liConfig.setPort(configData.get(KEY_LI_PORT));
		}

		if (configData.containsKey(KEY_LI_INGESTION_PORT) && null != configData.get(KEY_LI_INGESTION_PORT)
				&& StringUtils.isNotEmpty(configData.get(KEY_LI_INGESTION_PORT))) {
			liConfig.setIngestionPort(configData.get(KEY_LI_INGESTION_PORT));
		}

		if (configData.containsKey(KEY_LI_USER) && null != configData.get(KEY_LI_USER)
				&& StringUtils.isNotEmpty(configData.get(KEY_LI_USER))) {
			liConfig.user = configData.get(KEY_LI_USER);
		}

		if (configData.containsKey(KEY_LI_PASSWORD) && null != configData.get(KEY_LI_PASSWORD)
				&& StringUtils.isNotEmpty(configData.get(KEY_LI_PASSWORD))) {
			liConfig.password = configData.get(KEY_LI_PASSWORD);
		}

		if (configData.containsKey(KEY_CONNECTION_SCHEME) && null != configData.get(KEY_CONNECTION_SCHEME)
				&& StringUtils.isNotEmpty(configData.get(KEY_CONNECTION_SCHEME))) {
			liConfig.setScheme(configData.get(KEY_CONNECTION_SCHEME));
		}
		if (configData.containsKey(KEY_AGENT_ID) && null != configData.get(KEY_AGENT_ID)
				&& StringUtils.isNotEmpty(configData.get(KEY_AGENT_ID))) {
			liConfig.setAgentId(configData.get(KEY_AGENT_ID));
		}
		return liConfig;
	}

	/**
	 * Returns the http or https
	 * 
	 * @return http or https
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * Sets the scheme to provided one http or https
	 * 
	 * @param scheme
	 *            http or https
	 */
	public void setScheme(String scheme) {
		if (null != scheme && !StringUtils.isEmpty(scheme)) {
			if (DEFAULT_SCHEME.equals("https")) {
				this.scheme = scheme;
			} else {
				throw new NotImplementedException("only https scheme is available");
			}
		} else {
			throw new IllegalArgumentException("Invalid http scheme. Should be https");
		}
	}

	/**
	 * Ingestion port
	 * 
	 * @return ingestion port number (string format)
	 */
	public String getIngestionPort() {
		return ingestionPort;
	}

	/**
	 * Updates the ingestion port
	 * 
	 * @param ingestionPort
	 *            ingestion port number (string format)
	 */
	public void setIngestionPort(String ingestionPort) {
		if (null != ingestionPort && !StringUtils.isEmpty(ingestionPort)) {
			this.ingestionPort = ingestionPort;
		} else {
			throw new IllegalArgumentException("Invalid Ingestion port ");
		}
	}

	/**
	 * Agent ID
	 * 
	 * @return agentId (string format)
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * Updates the AgentID
	 * 
	 * @param agentId
	 *            LI agent id
	 * 
	 * @throws IllegalArgumentException
	 *             when the agentId is not a valid UUID
	 */
	public void setAgentId(String agentId) {
		if (null != agentId && !StringUtils.isEmpty(agentId)) {
			UUID uuid = UUID.fromString(agentId);
			this.agentId = uuid.toString();
		} else {
			throw new IllegalArgumentException("Invalid Agent ID");
		}
	}

	/**
	 * Updates the host name of the loginsight
	 * 
	 * @param host
	 *            Host name of the LogInsight server
	 */
	public void setHost(String host) {
		if (null != host && !StringUtils.isEmpty(host)) {
			this.host = host;
		} else {
			throw new IllegalArgumentException("Invalid host name ");
		}
	}

	/**
	 * Updates the user name of the LogInsight user
	 * 
	 * @param user
	 *            user name for LogInsight
	 */
	public void setUser(String user) {
		if (null != user && !StringUtils.isEmpty(user)) {
			this.user = user;
		} else {
			throw new IllegalArgumentException("Invalid user name ");
		}
	}

	/**
	 * Updates the password for LogInsight connection
	 * 
	 * @param password
	 *            password for LogInsight
	 */
	public void setPassword(String password) {
		if (null != password && !StringUtils.isEmpty(password)) {
			this.password = password;
		} else {
			throw new IllegalArgumentException("Invalid password ");
		}
	}

	/**
	 * Updates the LogInsight port number
	 * 
	 * @param port
	 *            Port number (string format)
	 */
	public void setPort(String port) {
		if (null != port && !StringUtils.isEmpty(port)) {
			this.port = port;
		} else {
			throw new IllegalArgumentException("Invalid port ");
		}
	}

	/**
	 * Getter for Loginsight host name
	 * 
	 * @return Loginsight hostname
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Getter for Loginsight server port
	 * 
	 * @return Loginsight server port (stringified format)
	 */
	public String getPort() {
		return this.port;
	}

	/**
	 * Getter for Loginsight user for authentication
	 * 
	 * @return Loginsight user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * Getter for Loginsight password for authentication
	 * 
	 * @return Loginsight password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Merges the values from environment variables into configuration object
	 * (this) Values of the Environment variables takes priority
	 * 
	 * <b>Environment Variables: </b>
	 * 
	 * LOGINSIGHT_HOST: Host of the LogInsight LOGINSIGHT_USERNAME: User name of
	 * the LogInsight LOGINSIGHT_PASSWORD: Password for LogInsight
	 * 
	 * @return Configuration object (this)
	 */
	public Configuration loadFromEnv() {
		if (null != System.getenv("LOGINSIGHT_HOST") && StringUtils.isNotEmpty(System.getenv("LOGINSIGHT_HOST"))) {
			this.host = System.getenv("LOGINSIGHT_HOST");
		}
		if (null != System.getenv("LOGINSIGHT_USERNAME")
				&& StringUtils.isNotEmpty(System.getenv("LOGINSIGHT_USERNAME"))) {
			this.user = System.getenv("LOGINSIGHT_USERNAME");
		}
		if (null != System.getenv("LOGINSIGHT_PASSWORD")
				&& StringUtils.isNotEmpty(System.getenv("LOGINSIGHT_PASSWORD"))) {
			this.password = System.getenv("LOGINSIGHT_PASSWORD");
		}
		return this;
	}

	/**
	 * Builds the Configuration object from the properties file (apache commons
	 * properties file format). <br>
	 * The values provided in the config file will be overwritten environment
	 * variables (if present)
	 * 
	 * List of the properties <br>
	 * loginsight.host = host name <br>
	 * loginsight.port = port number <br>
	 * loginsight.user = User name <br>
	 * loginsight.password = password <br>
	 * loginsight.ingestion.agentId = agentId <br>
	 * loginsight.connection.scheme = http protocol scheme <br>
	 * loginsight.ingestion.port = Ingestion port number <br>
	 * 
	 * @param configFileName
	 *            Name of the config file to read
	 * @return Newly created Configuration object
	 */
	public static Configuration buildFromConfig(String configFileName) {
		try {
			List<FileLocationStrategy> subs = Arrays.asList(new ProvidedURLLocationStrategy(),
					new FileSystemLocationStrategy(), new ClasspathLocationStrategy());
			FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

			FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(
					PropertiesConfiguration.class).configure(
							new Parameters().fileBased().setLocationStrategy(strategy).setFileName(configFileName));
			PropertiesConfiguration propConfig = builder.getConfiguration();
			Map<String, String> propMap = new HashMap<String, String>();
			Iterator<String> keys = propConfig.getKeys();
			keys.forEachRemaining(key -> {
				logger.info(key + ":" + propConfig.getString(key));
				propMap.put(key, propConfig.getString(key));
			});
			Configuration config = Configuration.buildConfig(propMap);
			config.loadFromEnv();
			return config;
		} catch (ConfigurationException e1) {
			throw new RuntimeException("Unable to load config", e1);
		}
	}

}
