/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.AggregateResponse;
import com.vmware.loginsightapi.core.AuthInfo;
import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.IngestionResponse;
import com.vmware.loginsightapi.core.MessageQueryResponse;
import com.vmware.loginsightapi.util.AsyncCallback;
import com.vmware.loginsightapi.util.Callback;
//import javax.net.ssl.SSLSocketFactory;

public class LogInsightClient implements AutoCloseable {
	
	public static final String DEFAULT_INGESTION_AGENT_ID = "54947df8-0e9e-4471-a2f9-9af509fb5889";	

	public static final String API_URL_SESSION_PATH = "/api/v1/sessions";
	public static final String API_URL_EVENTS_PATH = "/api/v1/events/";
	public static final String API_URL_AGGREGATED_EVENTS_PATH = "/api/v1/aggregated-events/";
	public static final String API_URL_INGESTION = "/api/v1/messages/ingest/";
	
	private String sessionId;
	
	private final LogInsightConnectionStrategy connectionStrategy;
	private final Properties connectionConfig;
	
	private final CloseableHttpAsyncClient asyncHttpClient;
	
	// private ObjectMapper mapper = new ObjectMapper();

	private final static Logger logger = LoggerFactory.getLogger(LogInsightClient.class);

	public LogInsightClient(LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy, Properties connectionConfig) {
		this.connectionStrategy = connectionStrategy;
		this.connectionConfig = connectionConfig;
		asyncHttpClient = connectionStrategy.getHttpClient();
	}
	
	public String apiUrl() {
		return connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_CONNECTION_SCHEME) + "://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_PORT);
	}

	public String sessionUrl() {
		return this.apiUrl() + API_URL_SESSION_PATH;
	}

	public String messageQueryUrl() {
		return this.apiUrl();
	}

	public String messageQueryFullUrl(String url) {
		return this.apiUrl() + url;
	}

	public String aggregateQueryUrl() {
		return this.apiUrl();
	}

	public String aggregateQueryFullUrl(String url) {
		return this.apiUrl() + url;
	}

	public String ingestionApiUrl() {
		int logInsightIngestionPort = 0;
		if (StringUtils.isEmpty(connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_INGESTION_PORT))) {
			logInsightIngestionPort = LogInsightConnectionConfig.DEFAULT_INGESTION_PORT;
		}
		return connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_CONNECTION_SCHEME) + "://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":" + logInsightIngestionPort + API_URL_INGESTION + DEFAULT_INGESTION_AGENT_ID;
	}

	public static List<Header> getDefaultHeaders() {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Content-Type", "application/json"));
		headers.add(new BasicHeader("Accept", "application/json"));
		String timestamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
		headers.add(new BasicHeader("x-li-timestamp", timestamp));
		return headers;
	}
	
	/**
	 * Connects to LogInsight and initialize AsyncHttpClient with LogInsight
	 * session Id. This method should be called after a successful
	 * authentication with LogInsight, so that {@code getSessionId} returns a
	 * proper session id.
	 *
	 */
	public void connect(String userName, String password) throws AuthFailure {
		
		String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", userName, password);

		HttpPost httpPost = new HttpPost(sessionUrl());
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type", "application/json");
		HttpResponse response = null;
		httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
		try {
			Future<HttpResponse> future = asyncHttpClient.execute(httpPost, null);
			response = future.get();
			String serverResponse = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			logger.info("Auth response = " + serverResponse);
			if (response.getStatusLine().getStatusCode() == 200) {
				AuthInfo authInfo = AuthInfo.fromJsonString(serverResponse);
				sessionId = authInfo.getSessionId();
			} else {
				logger.error("Unable to authenticate. StatusCode=" + response.getStatusLine().getStatusCode());
				logger.error("Unable to authenticate. " + serverResponse);
				throw new AuthFailure("Connection to LogInsight failed. " + serverResponse);
			}
		} catch (InterruptedException ie) {
			throw new AuthFailure("Connection to LogInsight failed", ie);
		} catch (ExecutionException ee) {
			throw new AuthFailure("Connection to LogInsight failed", ee);
		} catch (IOException e) {
			throw new AuthFailure("Connection to LogInsight failed", e);
		}
	}

	/**
	 * Stop the async http client.
	 */
	public void stopAsyncHttpClient() {
		logger.debug("Stopping the AsyncHttpClient");
		try {
			asyncHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method uses CloseableHttpAsyncClient to execute the remote query.
	 * Callers should ensure that {@code connect()} is invoked before calling
	 * this method.
	 *
	 * @param apiUrl
	 *            for LogInsight 3.0 API. The URL looks like
	 *            text/CONTAINS+Test/timestamp/GT+0
	 *
	 * @return a JSONObject representing the LI response
	 */
	public MessageQueryResponse messageQuery(String apiUrl) throws LogInsightApiException {
		HttpGet request = null;
		try {
			request = getHttpRequest(apiUrl, false);
			Future<HttpResponse> future = asyncHttpClient.execute(request, null);
			HttpResponse httpResponse = future.get();
			logger.debug("Response: " + httpResponse.getStatusLine());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				InputStream responseBody = httpResponse.getEntity().getContent();
				String responseString = IOUtils.toString(responseBody, "UTF-8");
				logger.warn("Response: " + responseString);
				return MessageQueryResponse.fromJsonString(responseString);
			}
			if ((httpResponse.getStatusLine().getStatusCode() == 401)
					|| (httpResponse.getStatusLine().getStatusCode() == 440)) {
				logger.warn("Session expired, retrying the request after authentication");
				sessionId = null;
				throw new AuthFailure("Invalid session id. Message query failed.");
			} else {
				throw new LogInsightApiException(
						"Unable to query the response from LogInsight " + httpResponse.getStatusLine());
			}
		} catch (InterruptedException ie) {
			throw new LogInsightApiException("Message query failed", ie);
		} catch (ExecutionException ee) {
			throw new LogInsightApiException("Message query failed", ee);
		} catch (IOException e) {
			throw new LogInsightApiException("Message query failed", e);
		}
	}

	public void messageQueryAsync(String apiUrl, Callback<MessageQueryResponse> callback)
			throws LogInsightApiException {
		HttpGet request = null;
//		final CountDownLatch latch = new CountDownLatch(1);
		try {
			request = getHttpRequest(apiUrl, false);
			asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse httpResponse) {

					try {
						InputStream responseBody = httpResponse.getEntity().getContent();
						String responseString = IOUtils.toString(responseBody, "UTF-8");
						logger.warn("Response: " + responseString);
						callback.completed(MessageQueryResponse.fromJsonString(responseString));
						
					} catch (IOException e) {
						e.printStackTrace();
						callback.failed(new LogInsightApiException(e));
					}

				}

				@Override
				public void failed(Exception ex) {
					callback.failed(new LogInsightApiException(ex));
				}

				@Override
				public void cancelled() {
					callback.cancelled();
				}

			});
			logger.info("Finished completely!!!");
		} catch (Exception ie) {
			throw new LogInsightApiException("Message query failed", ie);
		}
	}
	
	public void messageQuery(String apiUrl, AsyncCallback<MessageQueryResponse, LogInsightApiError> callback)
			throws LogInsightApiException {
		HttpGet request = null;
		try {
			request = getHttpRequest(apiUrl, false);
			asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse httpResponse) {

					try {
						InputStream responseBody = httpResponse.getEntity().getContent();
						String responseString = IOUtils.toString(responseBody, "UTF-8");
						logger.warn("Response: " + responseString);						
						callback.completed(MessageQueryResponse.fromJsonString(responseString), LogInsightApiError.create());
						
					} catch (IOException e) {
						e.printStackTrace();
						callback.completed(null, new LogInsightApiError("Unable to process the query response", e));
					}

				}

				@Override
				public void failed(Exception ex) {
					callback.completed(null, new LogInsightApiError("Failed message Query", ex));
				}

				@Override
				public void cancelled() {
					callback.completed(null, new LogInsightApiError("Cancelled message Query", ""));
				}

			});
			logger.info("Finished completely!!!");
		} catch (Exception ie) {
			callback.completed(null, new LogInsightApiError("Message query failed", ie));
		}
	}

	public AggregateResponse aggregateQuery(String apiUrl) throws LogInsightApiException {
		HttpGet request = null;
		try {
			request = getHttpRequest(apiUrl, true);
			System.out.println("Querying " + aggregateQueryUrl() + apiUrl);
			Future<HttpResponse> future = asyncHttpClient.execute(request, null);
			HttpResponse httpResponse = future.get();
			logger.debug("Aggregate Response: " + httpResponse.getStatusLine());
			System.out.println("Aggregate Response: " + httpResponse.getStatusLine());
			
			if ((httpResponse.getStatusLine().getStatusCode() == 401)
					|| (httpResponse.getStatusLine().getStatusCode() == 440)) {
				logger.warn("Session expired, retrying the request after authentication");
				sessionId = null;
				throw new AuthFailure("Session expired. Received " + httpResponse.getStatusLine() + " from LogInsight");
			} else {
				InputStream responseBody = httpResponse.getEntity().getContent();
				String responseString = IOUtils.toString(responseBody, "UTF-8");
				logger.warn("Response: " + responseString);
				return AggregateResponse.fromJsonString(responseString);
			}
		} catch (InterruptedException ie) {
			throw new LogInsightApiException("Aggregation query failed", ie);
		} catch (ExecutionException ee) {
			throw new LogInsightApiException("Aggregation query failed", ee);
		} catch (IOException e) {
			throw new LogInsightApiException("Aggregation query failed", e);
		}
	}
	
	public void aggregateQuery(String apiUrl, AsyncCallback<AggregateResponse, LogInsightApiError> callback) {
		HttpGet request = null;
		try {
			request = getHttpRequest(apiUrl, true);
			logger.debug("Querying " + aggregateQueryUrl() + apiUrl);
			asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse httpResponse) {

					try {
						String responseString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
						logger.warn("Response: " + responseString);						
						callback.completed(AggregateResponse.fromJsonString(responseString), LogInsightApiError.create());
						
					} catch (IOException e) {
						e.printStackTrace();
						callback.completed(null, new LogInsightApiError("Unable to process the query response", e));
					}

				}

				@Override
				public void failed(Exception ex) {
					callback.completed(null, new LogInsightApiError("Failed message Query", ex));
				}

				@Override
				public void cancelled() {
					callback.completed(null, new LogInsightApiError("Cancelled message Query", ""));
				}

			});
		} catch (Exception ie) {
			callback.completed(null, new LogInsightApiError("Message query failed", ie));
		}
	}

	public IngestionResponse injest(IngestionRequest messages) throws LogInsightApiException {

		// IngestionResponse response = null;
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(ingestionApiUrl());

			httpPost.setEntity(new StringEntity(messages.toJson(), ContentType.APPLICATION_JSON));
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("Accept", "application/json");
			logger.info("Sending : " + messages.toJson());
			Future<HttpResponse> future = asyncHttpClient.execute(httpPost, null);
			HttpResponse httpResponse = future.get();
			logger.debug("Response: " + httpResponse.getStatusLine());
			InputStream responseBody = httpResponse.getEntity().getContent();
			String responseString = IOUtils.toString(responseBody, "UTF-8");
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// String responseString =
				// convertStreamToString(httpResponse.getEntity().getContent());
				logger.warn("Response: " + responseString);
				return IngestionResponse.fromJsonString(responseString);
			} else {
				// String responseString =
				// convertStreamToString(httpResponse.getEntity().getContent());
				throw new LogInsightApiException("Unable to send messages to LogInsight. Received "
						+ httpResponse.getStatusLine() + " from LogInsight. Response = " + responseString);
			}
		} catch (InterruptedException ie) {
			throw new LogInsightApiException("Ingestion failed", ie);
		} catch (ExecutionException ee) {
			throw new LogInsightApiException("Ingestion failed", ee);
		} catch (IOException e) {
			throw new LogInsightApiException("Ingestion failed", e);
		}
	}

	public String getSessionId() throws AuthFailure {
		if (sessionId == null) {
			throw new AuthFailure("Invalid session id");
		}
		return sessionId;
	}

	@Override
	public void close() throws Exception {
		this.stopAsyncHttpClient();
	}
	
	public List<Header> getSessionHeaders() {
		List<Header> headers = new ArrayList<>();		
		headers.add(new BasicHeader("X-li-session-id", getSessionId()));
		return headers;
	}
	
	public void addHeaders(HttpGet request, List<Header> headers) {
		for (Header header : headers) {
			request.addHeader(header);
		}
	}
	
	/**
	 * Returns a properly created instance of HttPGet based on the provided URL
	 * @param apiUrl
	 * @param isAggregateQuery Is it is normal query or aggregate query
	 * @return
	 */
	public HttpGet getHttpRequest(String apiUrl, boolean isAggregateQuery) {
		HttpGet request = null;
		try {
			if (isAggregateQuery) {
				request = new HttpGet(aggregateQueryUrl() + apiUrl);
			} else {
				request = new HttpGet(messageQueryUrl() + apiUrl);
			}
		} catch (IllegalArgumentException e) {
			throw e;
		}
		addHeaders(request, getDefaultHeaders());
		addHeaders(request, getSessionHeaders());
		return request;
	}

}
