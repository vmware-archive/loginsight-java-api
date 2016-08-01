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
 * Auth Failure Exception
 */

public class AuthFailure extends RuntimeException {

	private static final long serialVersionUID = -5203326099531622673L;

	/**
	 * Default constructor
	 */
	public AuthFailure() {
	}

	/**
	 * Constructs AuthFailure with provided error message
	 * 
	 * @param message exception message
	 */
	public AuthFailure(String message) {
		super(message);
	}

	/**
	 * Constructs AuthFailure object from Throwable
	 * 
	 * @param cause cause of the failure (Throwable)
	 */
	public AuthFailure(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs AuthFailure object from error message and Throwable
	 * 
	 * @param message exception message
	 * @param cause   cause of the failure (Throwable)
	 */
	public AuthFailure(String message, Throwable cause) {
		super(message, cause);
	}

}
