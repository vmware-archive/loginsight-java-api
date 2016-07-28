package com.vmware.loginsightapi;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockLogInsightConnectionStrategy implements LogInsightConnectionStrategy<CloseableHttpAsyncClient> {

	@Mock private CloseableHttpAsyncClient asyncHttpClient;
	@Override
	public CloseableHttpAsyncClient getHttpClient() {
		return asyncHttpClient;
	}

}
