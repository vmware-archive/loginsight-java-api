/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Properties;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.FieldConstraint;
import com.vmware.loginsightapi.core.MessageQueryResponse;

@RunWith(MockitoJUnitRunner.class)
public class LogInsightClientMockTest {
	
	private LogInsightClient client;
	private final static Logger logger = LoggerFactory.getLogger(LogInsightClientMockTest.class);
	
	private final static String SERVER_RESPONSE_EXPECTED = "{\"userId\":\"7506ecf5-cd7a-4ae3-88b7-f72fc1955c73\",\"sessionId\":\"qyOLWEe7f/GjdM1WnczrCeQure97B/NpTbWTeqqYPBd1AYMf9cMNfQYqltITI4ffPMx822Sz9i/X47t8VwsDb0oGckclJUdn83cyIPk6WlsOpI4Yjw6WpurAnv9RhDsYSzKhAMzskzhTOJKfDHZjWR5v576WwtJA71wqI7igFrG91LG5c/3GfzMb68sUHF6hV+meYtGS4A1y/lUItvfkqTTAxBtTCZNoKrvCJZ4R+b6vuAAYoBNSWL7ycIy2LsALrVFxftAkA8n9DBAZYA9T5A==\",\"ttl\":1800}";
	private final static String SERVER_EXPECTED_QUERY_RESPONSE = "{\"complete\":true,\"duration\":57,\"events\":"
			+ "[{\"text\":\"2015-05-20T15:31:01.783+02:00 [10108 trivia 'commonvpxLro' opID=939CF9EC-00000B7B-88] "
			+ "[VpxLRO] Work item scheduled, total threads 8, max threads 640, running threads 7, queued size 1\","
			+ "\"timestamp\":1432135888000,\"fields\":[{\"name\":\"vclap_product\",\"content\":\"vcenter\"},"
			+ "{\"name\":\"vclap_source\",\"content\":\"vcenter\"},{\"name\":\"vclap_caseid\",\"content\":\"1423244\"},"
			+ "{\"name\":\"hostname\",\"content\":\"loganalyzer-web.eng.vmware.com\"},{\"name\":\"event_type\","
			+ "\"content\":\"v4_e7aecfc7\"},{\"name\":\"appname\",\"content\":\"vpxd\"},"
			+ "{\"name\":\"filepath\",\"content\":\"vpxd-47457.log\"},{\"name\":\"source\","
			+ "\"content\":\"10.152.215.4\"},{\"name\":\"bundle\",\"content\":\"VC\"},{\"name\":\"tenant\",\"content\":\"cpd\"}]}]}";
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
	}
	
	@Test
	public void testConnect() {
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
		//TODO Junit does not support ordering of tests. testConnect() below is called so
		// that sessionId is populated
		testConnect();
		long startTime = System.nanoTime();
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
		long duration = System.nanoTime() - startTime;
		logger.info("duration=" + duration);
	}



}
