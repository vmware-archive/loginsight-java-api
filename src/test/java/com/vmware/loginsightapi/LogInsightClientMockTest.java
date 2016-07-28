package com.vmware.loginsightapi;

import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.MessageQueryResponse;

@RunWith(MockitoJUnitRunner.class)
public class LogInsightClientMockTest {
	
	private LogInsightClient client;
	private final static Logger logger = LoggerFactory.getLogger(LogInsightClientMockTest.class);
	
	private final static String SERVER_RESPONSE_EXPECTED = "{\"userId\":\"7506ecf5-cd7a-4ae3-88b7-f72fc1955c73\",\"sessionId\":\"qyOLWEe7f/GjdM1WnczrCeQure97B/NpTbWTeqqYPBd1AYMf9cMNfQYqltITI4ffPMx822Sz9i/X47t8VwsDb0oGckclJUdn83cyIPk6WlsOpI4Yjw6WpurAnv9RhDsYSzKhAMzskzhTOJKfDHZjWR5v576WwtJA71wqI7igFrG91LG5c/3GfzMb68sUHF6hV+meYtGS4A1y/lUItvfkqTTAxBtTCZNoKrvCJZ4R+b6vuAAYoBNSWL7ycIy2LsALrVFxftAkA8n9DBAZYA9T5A==\",\"ttl\":1800}";
	@Mock
	private LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy;
	@Mock
	private CloseableHttpAsyncClient asyncHttpClient;
	String ip;
	String user;
	String password;

	@Before
	public void setUp() {
		ip = System.getenv("ip");
		user = System.getenv("user");
		password = System.getenv("password");
		LogInsightConnectionConfig connectionConfig = new LogInsightConnectionConfig(ip, user, password);
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
			client.connect();
			assertEquals("Invalid session id!!", "qyOLWEe7f/GjdM1WnczrCeQure97B/NpTbWTeqqYPBd1AYMf9cMNfQYqltITI4ffPMx822Sz9i/X47t8VwsDb0oGckclJUdn83cyIPk6WlsOpI4Yjw6WpurAnv9RhDsYSzKhAMzskzhTOJKfDHZjWR5v576WwtJA71wqI7igFrG91LG5c/3GfzMb68sUHF6hV+meYtGS4A1y/lUItvfkqTTAxBtTCZNoKrvCJZ4R+b6vuAAYoBNSWL7ycIy2LsALrVFxftAkA8n9DBAZYA9T5A==", client.getSessionId());
		} catch (Exception e) {
			System.out.println("Exception raised " + ExceptionUtils.getStackTrace(e));
		}
	}


}
