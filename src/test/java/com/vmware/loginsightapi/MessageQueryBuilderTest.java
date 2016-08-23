package com.vmware.loginsightapi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vmware.loginsightapi.core.FieldConstraint;

public class MessageQueryBuilderTest {

	@Test
	public void testBasicMsgQuery1() {
		MessageQueryBuilder mqb = (MessageQueryBuilder) new MessageQueryBuilder().limit(10)
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals("/api/v1/events/field_1/EQ+value1?limit=10&content-pack-fields=test", mqb.toUrlString());
	}
	
	@Test
	public void testBasicMsgQueryWithDefaults() {
		MessageQueryBuilder mqb = (MessageQueryBuilder) new MessageQueryBuilder().limit(10).withDefaults()
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals("/api/v1/events/field_1/EQ+value1?limit=10&timeout=30000&content-pack-fields=test", mqb.toUrlString());
	}

}
