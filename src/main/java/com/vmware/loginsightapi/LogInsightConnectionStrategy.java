/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

/**
 * LogInsightConnectionStrategy defines the connection implementation to be
 * used. Implement this class to the return the custom build HttpClient for use
 * in LogInsightClient
 * 
 * @param <T>
 *            Customized CloseableHttpAsyncClient or CloseableHttpClient class
 */
public interface LogInsightConnectionStrategy<T extends CloseableHttpAsyncClient> {
	
	/**
	 * Return an instance of HttpClient Implement this method to return a custom
	 * HttpClient instance.
	 * 
	 * @return Instance of custom configured HttpClient
	 */
	T getHttpClient();

}
