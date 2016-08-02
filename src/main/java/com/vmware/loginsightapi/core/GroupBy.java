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
 * Abstract Class to hold the GroupBy information required in the query
 *
 */
public abstract class GroupBy {

	protected final String groupByField;
	protected final String bins;
	protected final int binWidth;

	/**
	 * Constructor to initialize the groupBy object with field name Defaults are
	 * set to other parameters.
	 * 
	 * @param groupByField name of the groupBy Field
	 */
	public GroupBy(String groupByField) {
		this.groupByField = groupByField;
		this.binWidth = 0;
		this.bins = null;
	}

	/**
	 * Constructor to initialize the groupBy object with field name, bins and binWidth
	 * 
	 * @param groupByField name of the groupBy field
	 * @param bins         dynamic bins with comma separated bin widths (10,20,50)
	 * @param binWidth     fixed bin width
	 */
	public GroupBy(String groupByField, String bins, int binWidth) {
		this.groupByField = groupByField;
		this.bins = bins;
		this.binWidth = binWidth;
	}
	
	/**
	 * Name of the field on which grouping need to be done.
	 * 
	 * @return name of the groupBy field
	 */
	public String getGroupByField() {
		return groupByField;
	}

	/**
	 * Getter the dynamic bin widths
	 * 
	 * @return a comma separated string with bin widths
	 */
	public String getBins() {
		return bins;
	}

	/**
	 * Getter for fixed bin width
	 * 
	 * @return width of the bin
	 */
	public int getBinWidth() {
		return binWidth;
	}

}
