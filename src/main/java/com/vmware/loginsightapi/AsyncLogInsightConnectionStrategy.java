/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;

import com.vmware.loginsightapi.util.NonValidatingSSLSocketFactory;

/**
 * Defines the connection strategy with CloseableHttpAsyncClient
 */

public class AsyncLogInsightConnectionStrategy implements LogInsightConnectionStrategy<CloseableHttpAsyncClient> {

	private CloseableHttpAsyncClient asyncHttpClient;

	/**
	 * Initializes and returns the httpClient with NoopHostnameVerifier
	 * 
	 * @return CloseableHttpAsyncClient
	 */
	@Override
	public CloseableHttpAsyncClient getHttpClient() {
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = NonValidatingSSLSocketFactory.getSSLContext();
		// Allow TLSv1 protocol only

		SSLIOSessionStrategy sslSessionStrategy = new SSLIOSessionStrategy(sslcontext, new String[] { "TLSv1" }, null,
				new NoopHostnameVerifier());
		List<Header> headers = LogInsightClient.getDefaultHeaders();

		asyncHttpClient = HttpAsyncClients.custom().setSSLStrategy(sslSessionStrategy).setDefaultHeaders(headers)
				.build();
		asyncHttpClient.start();

		return asyncHttpClient;
	}

}
