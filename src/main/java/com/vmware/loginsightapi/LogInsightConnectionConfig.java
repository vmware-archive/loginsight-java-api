/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

public class LogInsightConnectionConfig {
	private static final String DEFAULT_SCHEME = "https";
	private static final int DEFAULT_PORT = 443;
	private static final int DEFAULT_INGESTION_PORT = 9543;
	
	private final String host;

	private final String userName;

	private final String password;

	private final int ingestionPort;
	
	private final String scheme;
	
	private final int port;
	
	public LogInsightConnectionConfig (String host, String userName, String password) {
		this.host = host;
		this.userName = userName;
		this.password = password;
		this.scheme = DEFAULT_SCHEME;
		this.port = DEFAULT_PORT;
		this.ingestionPort = DEFAULT_INGESTION_PORT;
	}

	public LogInsightConnectionConfig (String host, String userName, String password, String scheme, int port, int ingestionPort) {
		this.host = host;
		this.scheme = scheme;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.ingestionPort = ingestionPort;
	}

	public String getHost() {
		return host;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public int getIngestionPort() {
		return ingestionPort;
	}

	public String getScheme() {
		return scheme;
	}

	public int getPort() {
		return port;
	}

}
