package com.vmware.loginsightapi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vmware.loginsightapi.core.FieldConstraint;

public class AggregateQueryBuilderTest {

	@Test
	public void testBasicAggregateQuery1() {
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) new AggregateQueryBuilder().limit(10)
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals("/api/v1/aggregated-events/field_1/EQ+value1?limit=10&content-pack-fields=test", aqb.toUrlString());
	}

}
