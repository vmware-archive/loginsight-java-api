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
	public static final String DEFAULT_SCHEME = "https";
	public static final int DEFAULT_PORT = 443;
	public static final int DEFAULT_INGESTION_PORT = 9543;
	
	//List of properties for loginsight connection
	public static final String LOGINSIGHT_HOST = "loginsight.host";
	public static final String LOGINSIGHT_PORT = "loginsight.port";
	public static final String LOGINSIGHT_USER = "loginsight.user";
	public static final String LOGINSIGHT_CONNECTION_SCHEME = "loginsight.connection.scheme";
	public static final String LOGINSIGHT_INGESTION_PORT = "loginsight.ingestion.port";
	public static final String LOGINSIGHT_PASSWORD = "loginsight.password";
}
