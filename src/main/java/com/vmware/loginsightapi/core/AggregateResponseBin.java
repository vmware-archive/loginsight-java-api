/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.core;

/**
 * Class representing the each bin of aggregate response.
 *
 */
public class AggregateResponseBin {
	
	private Long minTimestamp;
	private Long maxTimestamp;
	private Long value;
	
	/**
	 * Bin's minimum (starting) Timestamp
	 * @return timestamp in millis
	 */
	public Long getMinTimestamp() {
		return minTimestamp;
	}
	
	/**
	 * Sets minimum (starting) Timestamp for bin.
	 * @param minTimestamp minimum (starting) time stamp
	 */
	public void setMinTimestamp(Long minTimestamp) {
		this.minTimestamp = minTimestamp;
	}
	
	/**
	 * Bin's maximum (end) Timestamp
	 * 
	 * @return timestamp in millis
	 */
	public Long getMaxTimestamp() {
		return maxTimestamp;
	}
	
	/**
	 * Sets maximum (end) Timestamp for bin.
	 * 
	 * @param maxTimestamp maximum (end) time stamp
	 */
	public void setMaxTimestamp(Long maxTimestamp) {
		this.maxTimestamp = maxTimestamp;
	}
	
	/**
	 * Bin value
	 * @return value in the bin
	 */
	public Long getValue() {
		return value;
	}
	
	/**
	 * Setter for Bin value
	 * 
	 * @param value value in the bin
	 */
	public void setValue(Long value) {
		this.value = value;
	}

}
