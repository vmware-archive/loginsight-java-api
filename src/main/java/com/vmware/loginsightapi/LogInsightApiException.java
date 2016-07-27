/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

public class LogInsightApiException extends RuntimeException {

	private static final long serialVersionUID = -4785222347198327783L;

	public LogInsightApiException() {
	}

	public LogInsightApiException(String message) {
		super(message);
	}

	public LogInsightApiException(Throwable cause) {
		super(cause);
	}

	public LogInsightApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogInsightApiException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
