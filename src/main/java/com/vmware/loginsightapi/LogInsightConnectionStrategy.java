package com.vmware.loginsightapi;

public interface LogInsightConnectionStrategy<T> {
	T getHttpClient();

}
