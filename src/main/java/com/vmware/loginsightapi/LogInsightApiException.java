/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

/**
 * Generic API Exception for LogInsight API
 */
public class LogInsightApiException extends RuntimeException {

	private static final long serialVersionUID = -4785222347198327783L;

	/**
	 * Constructs an empty LogInsightApiException object
	 */
	public LogInsightApiException() {
	}

	/**
	 * Constructs LogInsightApiException with provided message
	 * @param message error message
	 */
	public LogInsightApiException(String message) {
		super(message);
	}

	/**
	 * Constructs LogInsightApiException with provided Throwable
	 * @param cause Throwable instance
	 */
	public LogInsightApiException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs LogInsightApiException with provided message and Throwable
	 * 
	 * @param message Error message
	 * @param cause   Throwable instance
	 */
	public LogInsightApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
