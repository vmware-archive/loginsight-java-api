/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import com.vmware.loginsightapi.core.AbstractQuery;

/**
 * MessageQuery extends from QueryBuilder and overwrites toUrlString.
 */
public class MessageQuery extends AbstractQuery<MessageQuery> {

	/**
	 * Relative URL path for events query
	 */
	public static final String API_URL_EVENTS_PATH = "/api/v1/events/";

	/**
	 * Builds the url segment based on the field constraints and other url
	 * parameters supplied.
	 * 
	 * @return partial url string
	 */
	@Override
	public String toUrlString() {
		String path = buildPathSegment();
		String params = buildUrlParameters();
		String url = API_URL_EVENTS_PATH;
		if (!path.isEmpty()) {
			url += path;
		}
		if (!params.isEmpty()) {
			url += "?" + params;
		}
		return url;
	}

}
