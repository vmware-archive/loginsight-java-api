package com.vmware.loginsightapi;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConstraintBuilderTest {

	@Test
	public void testBasicConstraintBuilder() {
		ConstraintBuilder cb = new ConstraintBuilder().eq("field_1", "value_1");
		assertEquals("field_1/EQ+value_1", cb.buildPathSegment());
	}

	@Test
	public void testFullConstraintBuilder() {
		ConstraintBuilder cb = new ConstraintBuilder().eq("num_field_1", "10");
		cb.ne("num_field_2", "20");
		cb.gt("num_field_3", "30");
		cb.ge("num_field_4", "40");
		cb.lt("num_field_5", "50");
		cb.le("num_field_6", "60");
		cb.contains("text_field_1", "value_1");
		cb.notContains("text_field_2", "value_2");
		cb.has("text_field_3", "value_3");
		cb.notHas("text_field_4", "value_4");
		cb.matchesRegex("text_field_5", "value_5");
		cb.notMatchesRegex("text_field_6", "value_6");
		cb.exists("field_exits");
		assertEquals("num_field_1/EQ+10/num_field_2/NE+20/" + "num_field_3/GT+30/num_field_4/GE+40/num_field_5/"
				+ "LT+50/num_field_6/LE+60/" + "text_field_1/CONTAINS+value_1/text_field_2/NOT_CONTAINS+value_2/"
				+ "text_field_3/HAS+value_3/text_field_4/NOT_HAS+value_4/"
				+ "text_field_5/MATCHES_REGEX+value_5/text_field_6/NOT_MATCHES_REGEX+value_6/"
				+ "field_exits/EXISTS", cb.buildPathSegment());
	}

	@Test
	public void testEmptyConstraintBuilder() {
		ConstraintBuilder cb = new ConstraintBuilder();
		String path = cb.buildPathSegment();
		assertTrue(path.isEmpty());
	}

}
