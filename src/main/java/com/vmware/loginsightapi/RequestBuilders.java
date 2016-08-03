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
 * Abstract class with static methods to return the appropriate builder
 * instances.
 */
public abstract class RequestBuilders {

	/**
	 * Returns a new instance of MessageQueryBuilder
	 * 
	 * @return Object of MessageQueryBuilder
	 * @see MessageQueryBuilder
	 */
	public static MessageQueryBuilder messageQuery() {
		return new MessageQueryBuilder();
	}

	/**
	 * Returns a new instance of AggregateQueryBuilder
	 * 
	 * @return Object of AggregateQueryBuilder
	 * @see AggregateQueryBuilder
	 */
	public static AggregateQueryBuilder aggreateQuery() {
		return new AggregateQueryBuilder();
	}

	/**
	 * Returns a new instance of ConstraintBuilder
	 * 
	 * @return Object of ConstraintBuilder
	 * @see ConstraintBuilder
	 */
	public static ConstraintBuilder constraint() {
		return new ConstraintBuilder();
	}
	
	/**
	 * Returns a new instance of IngestionRequestBuilder
	 * 
	 * @return Object of IngestionRequestBuilder
	 * @see IngestionRequestBuilder
	 */
	public static IngestionRequestBuilder ingestionRequest() {
		return new IngestionRequestBuilder();
	}
	
	/**
	 * Returns a new instance of MessageBuilder
	 * 
	 * @return Object of MessageBuilder
	 * @see MessageBuilder
	 */
	public static MessageBuilder message() {
		return new MessageBuilder();
	}
	

}
