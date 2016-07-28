package com.vmware.loginsightapi;

import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;

public class AsyncLogInsightConnectionStrategy implements LogInsightConnectionStrategy<CloseableHttpAsyncClient> {
	
	private CloseableHttpAsyncClient asyncHttpClient;

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
