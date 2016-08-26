/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vmware.loginsightapi.core.FieldConstraint;

public class MessageQueryBuilderTest {

	@Test
	public void testBasicMsgQuery1() {
		MessageQuery mqb = (MessageQuery) new MessageQuery().limit(10)
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals("/api/v1/events/field_1/EQ+value1?limit=10&content-pack-fields=test", mqb.toUrlString());
	}
	
	@Test
	public void testBasicMsgQueryWithDefaults() {
		MessageQuery mqb = (MessageQuery) new MessageQuery().limit(10).withDefaults()
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals("/api/v1/events/field_1/EQ+value1?limit=10&timeout=30000&content-pack-fields=test", mqb.toUrlString());
	}

}
