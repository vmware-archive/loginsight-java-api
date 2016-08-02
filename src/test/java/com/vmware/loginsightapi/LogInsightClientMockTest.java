/**
 * Copyright 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.AggregateResponse;
import com.vmware.loginsightapi.core.MessageQueryResponse;
import com.vmware.loginsightapi.util.Callback;

@RunWith(MockitoJUnitRunner.class)
public class LogInsightClientMockTest {
	
	private LogInsightClient client;
	private final static Logger logger = LoggerFactory.getLogger(LogInsightClientMockTest.class);
	
	private final static String SERVER_RESPONSE_EXPECTED = "{\"userId\":\"7506ecf5-cd7a-4ae3-88b7-f72fc1955c73\","
			+ "\"sessionId\":\"qyOLWEe7f/GjdM1WnczrCeQure97B/NpTbWTeqqYPBd1AYMf9cMNfQYqltITI4ffPMx822Sz9i/X47t8VwsDb0oGckclJUdn83cyIPk6WlsOpI4Yjw6WpurAnv9RhDsYSzKhAMzskzhTOJKfDHZjWR5v576WwtJA71wqI7igFrG91LG5c/3GfzMb68sUHF6hV+meYtGS4A1y/lUItvfkqTTAxBtTCZNoKrvCJZ4R+b6vuAAYoBNSWL7ycIy2LsALrVFxftAkA8n9DBAZYA9T5A==\",\"ttl\":1800}";
	private final static String SERVER_EXPECTED_QUERY_RESPONSE = "{\"complete\":true,\"duration\":57,\"events\":"
			+ "[{\"text\":\"2015-05-20T15:31:01.783+02:00 [10108 trivia 'commonvpxLro' opID=939CF9EC-00000B7B-88] "
			+ "[VpxLRO] Work item scheduled, total threads 8, max threads 640, running threads 7, queued size 1\","
			+ "\"timestamp\":1432135888000,\"fields\":[{\"name\":\"vclap_product\",\"content\":\"vcenter\"},"
			+ "{\"name\":\"vclap_source\",\"content\":\"vcenter\"},{\"name\":\"vclap_caseid\",\"content\":\"1423244\"},"
			+ "{\"name\":\"hostname\",\"content\":\"loganalyzer-web.eng.vmware.com\"},{\"name\":\"event_type\","
			+ "\"content\":\"v4_e7aecfc7\"},{\"name\":\"appname\",\"content\":\"vpxd\"},"
			+ "{\"name\":\"filepath\",\"content\":\"vpxd-47457.log\"},{\"name\":\"source\","
			+ "\"content\":\"10.152.215.4\"},{\"name\":\"bundle\",\"content\":\"VC\"},{\"name\":\"tenant\",\"content\":\"cpd\"}]}]}";
	
	private final static String SERVER_EXPECTED_AGGREGATE_QUERY_RESPONSE = "{\"complete\":true,\"duration\":52,"
			+ "\"bins\":[{\"minTimestamp\":1432135885000,\"maxTimestamp\":1432135889999,\"value\":208515}]}";
	@Mock
	private LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy;
	@Mock
	private CloseableHttpAsyncClient asyncHttpClient;
	String host;
	String user;
	String password;
	
	Properties connectionConfig = null;

	@Before
	public void setUp() {
		//Load the properties file for loginsight connection.
		connectionConfig = new Properties();
		try {
			connectionConfig.load(getClass().getResourceAsStream("/config.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		host = connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST);
		user = connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_USER);
		password = connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_PASSWORD);
		if (StringUtils.isEmpty(password)) {
			password = System.getenv(LogInsightConnectionConfig.LOGINSIGHT_PASSWORD);
		}
		when(connectionStrategy.getHttpClient()).thenReturn(asyncHttpClient);
		client = new LogInsightClient(connectionStrategy, connectionConfig);

		HttpResponse response = mock(HttpResponse.class);
		Future<HttpResponse> future = ConcurrentUtils.constantFuture(response);
		when(asyncHttpClient.execute(any(HttpUriRequest.class), any(FutureCallback.class))).thenReturn(future, null);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(httpEntity);
		StatusLine statusLine = mock(StatusLine.class);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);
		try {
			InputStream inputStream = IOUtils.toInputStream(SERVER_RESPONSE_EXPECTED, "UTF-8");
			when(httpEntity.getContent()).thenReturn(inputStream);			
			client.connect(user, password);
			assertEquals("Invalid session id!!", "qyOLWEe7f/GjdM1WnczrCeQure97B/NpTbWTeqqYPBd1AYMf9cMNfQYqltITI4ffPMx822Sz9i/X47t8VwsDb0oGckclJUdn83cyIPk6WlsOpI4Yjw6WpurAnv9RhDsYSzKhAMzskzhTOJKfDHZjWR5v576WwtJA71wqI7igFrG91LG5c/3GfzMb68sUHF6hV+meYtGS4A1y/lUItvfkqTTAxBtTCZNoKrvCJZ4R+b6vuAAYoBNSWL7ycIy2LsALrVFxftAkA8n9DBAZYA9T5A==", client.getSessionId());
		} catch (Exception e) {
			logger.error("Exception raised " + ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Test
	public void testMessageQuery() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100)
				.setConstraints(constraints);
		
		
		HttpGet getRequest = client.getHttpRequest(mqb.toUrlString(), false);
		try {
			Assert.assertTrue("Request URI is malformed", getRequest.getURI().equals( new URI("https://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":443/api/v1/events/vclap_caseid/EQ+1423244/timestamp/GT+0")));
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Header[] headers = getRequest.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			String headerName = headers[i].getName();
			String headerValue = headers[i].getValue();
			if (headerName.equals("Content-Type")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("Accept")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("x-li-timestamp")) {
				Assert.assertNotNull("x-li-timestamp header is not set", headerValue);
			}
			
			if (headerName.equals("X-li-session-id")) {
				Assert.assertNotNull("X-li-session-id header is not set", headerValue);
			}
		}
		
		HttpResponse response = mock(HttpResponse.class);
		Future<HttpResponse> future = ConcurrentUtils.constantFuture(response);
		when(asyncHttpClient.execute(any(HttpUriRequest.class), any(FutureCallback.class))).thenReturn(future, null);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(httpEntity);
		StatusLine statusLine = mock(StatusLine.class);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);
		
		try {
			InputStream inputStream = IOUtils.toInputStream(SERVER_EXPECTED_QUERY_RESPONSE, "UTF-8");
			when(httpEntity.getContent()).thenReturn(inputStream);			
			MessageQueryResponse messages = client.messageQuery(mqb.toUrlString());
			Assert.assertTrue("Invalid number of messages", messages.getEvents().size() <= 100);
		} catch (Exception e) {
			logger.error("Exception raised " + ExceptionUtils.getStackTrace(e));
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testAggregateQuery() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().limit(100)
				.setConstraints(constraints);
		
		HttpGet getRequest = client.getHttpRequest(aqb.toUrlString(), true);
		try {
			Assert.assertTrue("Request URI is malformed", getRequest.getURI().equals( new URI("https://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":443/api/v1/aggregated-events/vclap_caseid/EQ+1423244/timestamp/GT+0")));
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Header[] headers = getRequest.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			String headerName = headers[i].getName();
			String headerValue = headers[i].getValue();
			if (headerName.equals("Content-Type")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("Accept")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("x-li-timestamp")) {
				Assert.assertNotNull("x-li-timestamp header is not set", headerValue);
			}
			
			if (headerName.equals("X-li-session-id")) {
				Assert.assertNotNull("X-li-session-id header is not set", headerValue);
			}
		}
		
		HttpResponse response = mock(HttpResponse.class);
		Future<HttpResponse> future = ConcurrentUtils.constantFuture(response);
		when(asyncHttpClient.execute(any(HttpUriRequest.class), any(FutureCallback.class))).thenReturn(future, null);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(httpEntity);
		StatusLine statusLine = mock(StatusLine.class);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);
		
		try {
			InputStream inputStream = IOUtils.toInputStream(SERVER_EXPECTED_AGGREGATE_QUERY_RESPONSE, "UTF-8");
			when(httpEntity.getContent()).thenReturn(inputStream);			
			AggregateResponse message = client.aggregateQuery(aqb.toUrlString());
			Assert.assertTrue("Invalid number of bins", message.getBins().size() <= 100);
			Assert.assertTrue("Invalid duration in the response", message.getDuration() > 0);
		} catch (Exception e) {
			logger.error("Exception raised " + ExceptionUtils.getStackTrace(e));
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testAggregateQueryCallback() {
		final CountDownLatch latch = new CountDownLatch(1);
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().limit(100)
				.setConstraints(constraints);
		
		HttpGet getRequest = client.getHttpRequest(aqb.toUrlString(), true);
		try {
			Assert.assertTrue("Request URI is malformed", getRequest.getURI().equals( new URI("https://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":443/api/v1/aggregated-events/vclap_caseid/EQ+1423244/timestamp/GT+0")));
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Header[] headers = getRequest.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			String headerName = headers[i].getName();
			String headerValue = headers[i].getValue();
			if (headerName.equals("Content-Type")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("Accept")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("x-li-timestamp")) {
				Assert.assertNotNull("x-li-timestamp header is not set", headerValue);
			}
			
			if (headerName.equals("X-li-session-id")) {
				Assert.assertNotNull("X-li-session-id header is not set", headerValue);
			}
		}
		
		HttpResponse response = mock(HttpResponse.class);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(httpEntity);
		StatusLine statusLine = mock(StatusLine.class);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);

		doAnswer(new Answer<Future<HttpResponse>>() {
			  @Override
		      public Future<HttpResponse> answer(InvocationOnMock invocation) {
		          FutureCallback<HttpResponse> responseCallback = invocation.getArgumentAt(1, FutureCallback.class);
		          responseCallback.completed(response);
		          return null;
		      }})
		  .when(asyncHttpClient).execute(any(HttpUriRequest.class), any(FutureCallback.class));
		
		try {
			InputStream inputStream = IOUtils.toInputStream(SERVER_EXPECTED_AGGREGATE_QUERY_RESPONSE, "UTF-8");
			when(httpEntity.getContent()).thenReturn(inputStream);

			client.aggregateQuery(aqb.toUrlString(), (AggregateResponse queryResponse, LogInsightApiError error) -> {
				if (error.isError()) {
					Assert.assertTrue(false);
					latch.countDown();
				} else {
					logger.info("Call completed");
					logger.info("Returned " + queryResponse.getBins().size() + " messages");
					Assert.assertTrue("Invalid number of bins", queryResponse.getBins().size() <= 100);
					Assert.assertTrue("Invalid duration in the response", queryResponse.getDuration() > 0);
					latch.countDown();
				}
			});			
		} catch (Exception e) {
			logger.error("Exception raised " + ExceptionUtils.getStackTrace(e));
			Assert.assertTrue(false);
		}
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			logger.info("Test Finished completely!!!");
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testMessageQueryAsync() {
		final CountDownLatch latch = new CountDownLatch(1);
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100)
				.setConstraints(constraints);
		
		HttpGet getRequest = client.getHttpRequest(mqb.toUrlString(), false);
		try {
			Assert.assertTrue("Request URI is malformed", getRequest.getURI().equals( new URI("https://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":443/api/v1/events/vclap_caseid/EQ+1423244/timestamp/GT+0")));
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Header[] headers = getRequest.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			String headerName = headers[i].getName();
			String headerValue = headers[i].getValue();
			if (headerName.equals("Content-Type")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("Accept")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("x-li-timestamp")) {
				Assert.assertNotNull("x-li-timestamp header is not set", headerValue);
			}
			
			if (headerName.equals("X-li-session-id")) {
				Assert.assertNotNull("X-li-session-id header is not set", headerValue);
			}
		}
		
		HttpResponse response = mock(HttpResponse.class);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(httpEntity);
		StatusLine statusLine = mock(StatusLine.class);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);

		doAnswer(new Answer<Future<HttpResponse>>() {
			  @Override
		      public Future<HttpResponse> answer(InvocationOnMock invocation) {
		          FutureCallback<HttpResponse> responseCallback = invocation.getArgumentAt(1, FutureCallback.class);
		          responseCallback.completed(response);
		          return null;
		      }})
		  .when(asyncHttpClient).execute(any(HttpUriRequest.class), any(FutureCallback.class));
		
		try {
			InputStream inputStream = IOUtils.toInputStream(SERVER_EXPECTED_QUERY_RESPONSE, "UTF-8");
			when(httpEntity.getContent()).thenReturn(inputStream);

			client.messageQueryAsync(mqb.toUrlString(), new Callback<MessageQueryResponse>() {
				@Override
				public void completed(MessageQueryResponse response) {
					logger.info("Call completed");
					logger.info("Returned " + response.getEvents().size() + " messages");
					Assert.assertTrue("Invalid number of messages", response.getEvents().size() <= 100);
					latch.countDown();
				}

				@Override
				public void failed(LogInsightApiException e) {
					System.out.println("Call failed" + ExceptionUtils.getStackTrace(e));
					latch.countDown();
				}

				@Override
				public void cancelled() {
					System.out.println("Call cancelled");
					latch.countDown();
				}

			});			
		} catch (Exception e) {
			logger.error("Exception raised " + ExceptionUtils.getStackTrace(e));
			Assert.assertTrue(false);
		}
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			logger.info("Test Finished completely!!!");
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testMessageQueryLambda() {
		final CountDownLatch latch = new CountDownLatch(1);
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100)
				.setConstraints(constraints);
		
		HttpGet getRequest = client.getHttpRequest(mqb.toUrlString(), false);
		try {
			Assert.assertTrue("Request URI is malformed", getRequest.getURI().equals( new URI("https://" + connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_HOST) + ":443/api/v1/events/vclap_caseid/EQ+1423244/timestamp/GT+0")));
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Header[] headers = getRequest.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			String headerName = headers[i].getName();
			String headerValue = headers[i].getValue();
			if (headerName.equals("Content-Type")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("Accept")) {
				Assert.assertEquals("Wrong request header value", "application/json", headerValue);
			}
			
			if (headerName.equals("x-li-timestamp")) {
				Assert.assertNotNull("x-li-timestamp header is not set", headerValue);
			}
			
			if (headerName.equals("X-li-session-id")) {
				Assert.assertNotNull("X-li-session-id header is not set", headerValue);
			}
		}
		
		HttpResponse response = mock(HttpResponse.class);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(httpEntity);
		StatusLine statusLine = mock(StatusLine.class);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);

		doAnswer(new Answer<Future<HttpResponse>>() {
			  @Override
		      public Future<HttpResponse> answer(InvocationOnMock invocation) {
		          FutureCallback<HttpResponse> responseCallback = invocation.getArgumentAt(1, FutureCallback.class);
		          responseCallback.completed(response);
		          return null;
		      }})
		  .when(asyncHttpClient).execute(any(HttpUriRequest.class), any(FutureCallback.class));
		
		try {
			InputStream inputStream = IOUtils.toInputStream(SERVER_EXPECTED_QUERY_RESPONSE, "UTF-8");
			when(httpEntity.getContent()).thenReturn(inputStream);

			client.messageQuery(mqb.toUrlString(), (MessageQueryResponse queryResponse, LogInsightApiError error) -> {
				if (error.isError()) {
					System.out.println("Call failed" + error.getMessage());
				} else {
					logger.info("Call completed");
					logger.info("Returned " + queryResponse.getEvents().size() + " messages");
					Assert.assertTrue("Invalid number of messages", queryResponse.getEvents().size() <= 100);
					latch.countDown();
				}
			});			
		} catch (Exception e) {
			logger.error("Exception raised " + ExceptionUtils.getStackTrace(e));
			Assert.assertTrue(false);
		}		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			logger.info("Test Finished completely!!!");
			e1.printStackTrace();
		}
	}



}
