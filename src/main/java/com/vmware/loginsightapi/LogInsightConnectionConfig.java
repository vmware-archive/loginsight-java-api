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
