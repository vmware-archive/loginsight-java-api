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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.AggregateResponse;
import com.vmware.loginsightapi.core.AuthInfo;
import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.IngestionResponse;
import com.vmware.loginsightapi.core.LogInsightConnectionStrategy;
import com.vmware.loginsightapi.core.MessageQueryResponse;

/**
 * LogInsight client class providing mechanisms to connect to LogInsight, Query
 * loginsight and ingest messages to loginsight
 * 
 */
public class LogInsightClient implements AutoCloseable {

	/**
	 * DEFAULT_INGESTION_AGENT_ID. Required for LogInsight Ingestion API
	 */
	public static final String DEFAULT_INGESTION_AGENT_ID = "54947df8-0e9e-4471-a2f9-9af509fb5889";

	/**
	 * Relative path of the sessions url
	 */
	public static final String API_URL_SESSION_PATH = "/api/v1/sessions";

	/**
	 * Relative path of the event query url
	 */
	public static final String API_URL_EVENTS_PATH = "/api/v1/events/";

	/**
	 * Relative path of the event group query (aggregate query) url
	 */
	public static final String API_URL_AGGREGATED_EVENTS_PATH = "/api/v1/aggregated-events/";

	/**
	 * Relative path of the ingestion url
	 */
	public static final String API_URL_INGESTION = "/api/v1/messages/ingest/";

	private String sessionId;

	private LogInsightConnectionStrategy connectionStrategy;
	private Configuration config;

	private final CloseableHttpAsyncClient asyncHttpClient;

	private final static Logger logger = LoggerFactory.getLogger(LogInsightClient.class);

	/**
	 * Default LogInsightClient constructor
	 * 
	 * @param config
	 *            Configuration object
	 * @see Configuration
	 */
	public LogInsightClient(Configuration config) {
		this.connectionStrategy = new AsyncLogInsightConnectionStrategy();
		this.config = config;
		asyncHttpClient = connectionStrategy.getHttpClient();
		this.connect();
	}

	/**
	 * Builds LogInsightClient object with config object and connection strategy
	 * 
	 * @param config
	 *            Configuration object
	 * @param connectionStrategy
	 *            Connection strategy to use
	 * 
	 */
	public LogInsightClient(Configuration config,
			LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy) {
		this.connectionStrategy = connectionStrategy;
		this.config = config;
		asyncHttpClient = connectionStrategy.getHttpClient();
		this.connect();
	}

	/**
	 * LogInsightClient constructor
	 * 
	 * @param host
	 *            LogInsight hostname
	 * @param user
	 *            user name
	 * @param password
	 *            password
	 * 
	 */
	public LogInsightClient(String host, String user, String password) {
		this.connectionStrategy = new AsyncLogInsightConnectionStrategy();
		this.config = new Configuration(host, user, password);
		asyncHttpClient = connectionStrategy.getHttpClient();
		this.connect();
	}

	/**
	 * Builds LogInsightClient object with basic config parameters and
	 * connection strategy
	 * 
	 * @param host
	 *            LogInsight host name
	 * @param user
	 *            LogInsight user name
	 * @param password
	 *            LogInsight password
	 * @param connectionStrategy
	 *            Connection strategy to use
	 * 
	 */
	public LogInsightClient(String host, String user, String password,
			LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy) {
		this.connectionStrategy = connectionStrategy;
		this.config = new Configuration(host, user, password);
		asyncHttpClient = connectionStrategy.getHttpClient();
		this.connect();
	}

	/**
	 * Constructs and returns the API URL
	 * 
	 * @return api url for query
	 */
	public String apiUrl() {
		return config.getScheme() + "://" + config.getHost() + ":" + config.getPort();
	}

	/**
	 * Compute and return the sessionUrl
	 * 
	 * @return session Url
	 */
	public String sessionUrl() {
		return this.apiUrl() + API_URL_SESSION_PATH;
	}

	/**
	 * Compute and return message query Url
	 * 
	 * @return Message Query URL
	 */
	public String messageQueryUrl() {
		return this.apiUrl();
	}

	/**
	 * Compute and return message query full url
	 * 
	 * @param url
	 *            relativeUrl of the message query
	 * @return full url of the message query
	 */
	public String messageQueryFullUrl(String url) {
		return this.apiUrl() + url;
	}

	/**
	 * Compute and return aggregate query url
	 * 
	 * @return Aggregate Query Url
	 */
	public String aggregateQueryUrl() {
		return this.apiUrl();
	}

	/**
	 * Compute and return aggregate query full url
	 * 
	 * @param url
	 *            relativeUrl of the aggregate query
	 * @return full url of the aggregate query
	 */
	public String aggregateQueryFullUrl(String url) {
		return this.apiUrl() + url;
	}

	/**
	 * Compute and return ingestion api url
	 * 
	 * @return url of the ingestion API
	 */
	public String ingestionApiUrl() {
		return config.getScheme() + "://" + config.getHost() + ":" + config.getIngestionPort() + API_URL_INGESTION
				+ DEFAULT_INGESTION_AGENT_ID;
	}

	/**
	 * Get the default list of headers for queries
	 * 
	 * @return list of headers
	 */
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
	 * @throws AuthFailure
	 *             authentication failure exception
	 */
	protected void connect() throws AuthFailure {

		String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", config.getUser(),
				config.getPassword());
		System.out.println("auth body " + body);
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
	 * Performs message query. Returns a CompletableFuture for
	 * MessageQueryResponse
	 * 
	 * @param apiUrl
	 *            relative url of the API
	 * @return MessageQueryResponse CompletableFuture object
	 * @throws LogInsightApiException
	 *             Exception
	 */
	public CompletableFuture<MessageQueryResponse> messageQuery(String apiUrl) {
		HttpGet request = null;
		CompletableFuture<MessageQueryResponse> completableFuture = new CompletableFuture<MessageQueryResponse>();
		try {
			request = getHttpRequest(apiUrl, false);
			asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse httpResponse) {

					try {
						InputStream responseBody = httpResponse.getEntity().getContent();
						String responseString = IOUtils.toString(responseBody, "UTF-8");
						logger.warn("Response: " + responseString);
						completableFuture.complete(MessageQueryResponse.fromJsonString(responseString));
					} catch (IOException e) {
						e.printStackTrace();
						completableFuture.completeExceptionally(e);
					}
				}

				@Override
				public void failed(Exception ex) {
					completableFuture.completeExceptionally(new LogInsightApiException("Failed message Query", ex));
				}

				@Override
				public void cancelled() {
					completableFuture.completeExceptionally(new LogInsightApiException("Cancelled message Query"));
				}

			});
		} catch (Exception ie) {
			completableFuture.completeExceptionally(new LogInsightApiException("Message query failed", ie));
		}
		return completableFuture.thenApply(response -> response);
	}

	/**
	 * Performs aggregate query. Accepts callback
	 * 
	 * @param apiUrl
	 *            relative url of the API
	 * @return AggregateResponse CompletableFuture
	 * 
	 */
	public CompletableFuture<AggregateResponse> aggregateQuery(String apiUrl) {
		HttpGet request = null;
		CompletableFuture<AggregateResponse> completableFuture = new CompletableFuture<AggregateResponse>();
		try {
			request = getHttpRequest(apiUrl, true);
			logger.debug("Querying " + aggregateQueryUrl() + apiUrl);
			asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse httpResponse) {

					try {
						String responseString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
						logger.warn("Response: " + responseString);
						completableFuture.complete(AggregateResponse.fromJsonString(responseString));

					} catch (IOException e) {
						e.printStackTrace();
						completableFuture.completeExceptionally(
								new LogInsightApiException("Unable to process the query response", e));
					}

				}

				@Override
				public void failed(Exception ex) {
					completableFuture.completeExceptionally(new LogInsightApiException("Failed message Query", ex));
				}

				@Override
				public void cancelled() {
					completableFuture.completeExceptionally(new LogInsightApiException("Cancelled message Query"));
				}

			});
		} catch (Exception ie) {
			completableFuture.completeExceptionally(new LogInsightApiException("Message query failed", ie));
		}
		return completableFuture.thenApply(response -> response);
	}

	/**
	 * Ingest messages to loginsight
	 * 
	 * @param messages
	 *            IngestionRequest object with list of messages
	 * @return IngestionResponse CompletableFuture object
	 * @see IngestionRequest
	 * @see IngestionResponse
	 */
	public CompletableFuture<IngestionResponse> ingest(IngestionRequest messages) {
		HttpPost httpPost = null;
		CompletableFuture<IngestionResponse> completableFuture = new CompletableFuture<IngestionResponse>();
		try {
			httpPost = getIngestionHttpRequest(messages);
			logger.info("Sending : " + messages.toJson());
			asyncHttpClient.execute(httpPost, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse httpResponse) {

					try {
						String responseString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
						logger.warn("Response: " + responseString);
						completableFuture.complete(IngestionResponse.fromJsonString(responseString));

					} catch (IOException e) {
						e.printStackTrace();
						completableFuture.completeExceptionally(
								new LogInsightApiException("Unable to process the query response", e));
					}
				}

				@Override
				public void failed(Exception ex) {
					completableFuture.completeExceptionally(new LogInsightApiException("Failed message Query", ex));
				}

				@Override
				public void cancelled() {
					completableFuture.completeExceptionally(new LogInsightApiException("Cancelled message Query"));
				}

			});
		} catch (Exception e) {
			completableFuture.completeExceptionally(new LogInsightApiException("Ingestion failed", e));
		}
		return completableFuture.thenApply(response -> response);
	}

	/**
	 * Returns sessionId if available. Throws AuthFailure in case sessionId not
	 * available.
	 * 
	 * @return session id
	 * @throws AuthFailure
	 *             authentication failure
	 */
	public String getSessionId() throws AuthFailure {
		if (sessionId == null) {
			throw new AuthFailure("Invalid session id");
		}
		return sessionId;
	}

	/**
	 * Close the httpclient connection
	 */
	@Override
	public void close() throws Exception {
		this.stopAsyncHttpClient();
	}

	/**
	 * Get the session headers
	 * 
	 * @return list of headers
	 */
	public List<Header> getSessionHeaders() {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("X-li-session-id", getSessionId()));
		return headers;
	}

	/**
	 * Add headers to HttpGet
	 * 
	 * @param request
	 *            HttpGet Object
	 * @param headers
	 *            adds the list of headers to be added
	 */
	public void addHeaders(HttpGet request, List<Header> headers) {
		for (Header header : headers) {
			request.addHeader(header);
		}
	}

	/**
	 * Returns a properly created instance of {@code HttpGet} based on the
	 * provided URL
	 * 
	 * @param apiUrl
	 *            base url of the api
	 * @param isAggregateQuery
	 *            Is it is normal query or aggregate query
	 * @return HttpGet request
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

	/**
	 * Returns a properly formed {@code HttpPost} for the given
	 * {@code IngestionRequest}
	 * 
	 * @param ingestionRequest
	 *            Ingestion request body
	 * @return HttpPost object
	 */
	public HttpPost getIngestionHttpRequest(IngestionRequest ingestionRequest) {
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(ingestionApiUrl());

			httpPost.setEntity(new StringEntity(ingestionRequest.toJson(), ContentType.APPLICATION_JSON));
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("Accept", "application/json");
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return httpPost;
	}

}
