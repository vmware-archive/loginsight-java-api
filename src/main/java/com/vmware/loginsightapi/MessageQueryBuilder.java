/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

public class MessageQueryBuilder extends QueryBuilder {
	
	public static final String API_URL_EVENTS_PATH = "/api/v1/events/";

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
