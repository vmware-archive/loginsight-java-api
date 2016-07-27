/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class LogInsightApiError {

	private String message;
	private String stackTrace;
	private boolean isError;
	
	public LogInsightApiError(String error, String stackTrace) {
		this.message = error;
		this.stackTrace = stackTrace;
		this.isError = true;
	}
	public LogInsightApiError(String error, Throwable th) {
		this.message = error;
		this.stackTrace = ExceptionUtils.getStackTrace(th);
		this.isError = true;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public LogInsightApiError() {
		isError = false;
	}
	public static LogInsightApiError create() {
		return new LogInsightApiError();
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
}
