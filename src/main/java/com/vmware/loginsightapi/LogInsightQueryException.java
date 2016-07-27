/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;


// TODO: Auto-generated Javadoc
/**
 * The Class ApiException.
 */
public class LogInsightQueryException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4676962720483909199L;

	/** The stack trace. */
	private String developerMessage;
	
	public LogInsightQueryException() {
		super();
	}

	public LogInsightQueryException(String message, Throwable ex, String developerMessage) {
		super(message, ex);
		this.developerMessage = developerMessage;
	}

	public LogInsightQueryException(String message, String developerMessage) {
		super(message);
		this.developerMessage = developerMessage;
	}

	public LogInsightQueryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, String developerMessage) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.developerMessage = developerMessage;
	}
	
	public String getDeveloperMessage() {
		return this.developerMessage;
	}
	
	public String toString() {
		return "[" + super.toString() + ", developerMessage: " + this.getDeveloperMessage() + "]";	
	}
}
