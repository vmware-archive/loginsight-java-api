package com.vmware.loginsightapi;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.vmware.loginsightapi.core.FieldConstraint;
import com.vmware.loginsightapi.core.GroupBy;
import com.vmware.loginsightapi.core.GroupByDynamicBinWidth;
import com.vmware.loginsightapi.core.GroupByFixedBinWidth;
import com.vmware.loginsightapi.core.OrderBy;

public class AggregateQueryBuilderTest {

	private static final String SAMPLE_QUERY_PARAMS_1 = "limit=10&timeout=1000";
	private static final String AGGREGATE_URL_PREFIX = "/api/v1/aggregated-events/";

	private static ConstraintBuilder getSampleConstraintBuilder1() {
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
		return cb;
	}

	private static ConstraintBuilder getSampleConstraintBuilder2() {
		ConstraintBuilder cb = new ConstraintBuilder();
		cb.eq("num_field_7", "70");
		cb.contains("text_field_7", "value_7");
		return cb;
	}

	private static String getConstrainString1() {
		return "num_field_1/EQ+10/num_field_2/NE+20/num_field_3/GT+30/"
				+ "num_field_4/GE+40/num_field_5/LT+50/num_field_6/LE+60/"
				+ "text_field_1/CONTAINS+value_1/text_field_2/NOT_CONTAINS+value_2/"
				+ "text_field_3/HAS+value_3/text_field_4/NOT_HAS+value_4/"
				+ "text_field_5/MATCHES_REGEX+value_5/text_field_6/NOT_MATCHES_REGEX+value_6/";
	}

	private static String getConstrainString2() {
		return "num_field_7/EQ+70/" + "text_field_7/CONTAINS+value_7";
	}

	@Test
	public void testBasicAggregateQuery1() {
		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().limit(10)
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals("/api/v1/aggregated-events/field_1/EQ+value1?limit=10&content-pack-fields=test",
				aqb.toUrlString());
	}

	@Test
	public void testBasicAggregateQueryWithDefaults() {
		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().limit(10).withDefaults()
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals(
				"/api/v1/aggregated-events/field_1/EQ+value1?limit=10&timeout=30000&bin-width=5000&aggregation-function=COUNT&content-pack-fields=test",
				aqb.toUrlString());
	}

	@Test
	public void testBasicAggregateQuery2() {
		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().max("field_2").limit(10)
				.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1").addContentPackField("test");
		assertEquals(
				"/api/v1/aggregated-events/field_1/EQ+value1?limit=10&aggregation-function=MAX&aggregation-field=field_2&content-pack-fields=test",
				aqb.toUrlString());
	}

	@Test
	public void testBasicAggregateQuery3() {
		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().max("field_2").limit(10)
				.timeout(1000).addConstraint("field_1", FieldConstraint.Operator.EQ, "value1")
				.addContentPackField("test");
		assertEquals(
				AGGREGATE_URL_PREFIX + "field_1/EQ+value1?" + "limit=10&timeout=1000" + "&aggregation-function=MAX&aggregation-field=field_2&content-pack-fields=test",
				aqb.toUrlString());
	}
	
	@Test
	public void testCountAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().count().limit(10)
				.timeout(1000).withDefaults();
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());

		///api/v1/aggregated-events/num_field_1/EQ+10/num_field_2/NE+20/num_field_3/GT+30/num_field_4/GE+40/num_field_5/LT+50/num_field_6/LE+60/text_field_1/CONTAINS+value_1/text_field_2/NOT_CONTAINS+value_2/text_field_3/HAS+value_3/text_field_4/NOT_HAS+value_4/text_field_5/MATCHES_REGEX+value_5/text_field_6/NOT_MATCHES_REGEX+value_6/num_field_7/EQ+70/text_field_7/CONTAINS+value_7?limit=10&timeout=1000&content-pack-fields=test1&content-pack-fields=test2&content-pack-fields=test3&content-pack-fields=test5
		///api/v1/aggregated-events/num_field_1/EQ+10/num_field_2/NE+20/num_field_3/GT+30/num_field_4/GE+40/num_field_5/LT+50/num_field_6/LE+60/text_field_1/CONTAINS+value_1/text_field_2/NOT_CONTAINS+value_2/text_field_3/HAS+value_3/text_field_4/NOT_HAS+value_4/text_field_5/MATCHES_REGEX+value_5/text_field_6/NOT_MATCHES_REGEX+value_6/num_field_7/EQ+70/text_field_7/CONTAINS+value_7?limit=10&timeout=1000&bin-width=5000&aggregation-function=COUNT&content-pack-fields=test1&content-pack-fields=test2&content-pack-fields=test3&content-pack-fields=test5
		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "bin-width=5000&aggregation-function=COUNT"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5", aqb.toUrlString());
	}
	
	@Test
	public void testSampleAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().sample().binWidth(100).limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());
		
		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + "limit=10&timeout=1000&bin-width=100" + "&"
				+ "aggregation-function=SAMPLE"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5" , aqb.toUrlString());
	}

	@Test
	public void testMaxAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().max("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=MAX&aggregation-field=field_2"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5", aqb.toUrlString());
	}
	
	@Test
	public void testMaxAggregateQueryGroupByDynamic() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().max("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());
		aqb.groupByDynamicBins("field_3", "10,20,50");

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=MAX&aggregation-field=field_2"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5"
				+ "&group-by-field=field_3&bins=10,20,50", aqb.toUrlString());
	}
	
	@Test
	public void testMaxAggregateQueryGroupByFixed() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().max("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());
		aqb.groupByFixedBinWidth("field_3", 100);
		aqb.orderBy(OrderBy.OrderByFunction.MAX, "field_2", OrderBy.Direction.ASC);
		aqb.orderBy(OrderBy.OrderByFunction.MAX, "field_3", OrderBy.Direction.DESC);
		
		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=MAX&aggregation-field=field_2"
				+ "&order-by-function=MAX&order-by-field=field_2&order-by-direction=ASC&order-by-function=MAX&order-by-field=field_3&order-by-direction=DESC"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5"
				+ "&group-by-field=field_3&bin-width=100", aqb.toUrlString());
	}
	
	@Test
	public void testMinAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().min("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());
		
		List<OrderBy> orderBys = new ArrayList<OrderBy>();
		orderBys.add(new OrderBy(OrderBy.OrderByFunction.MIN, "field_2", OrderBy.Direction.ASC));
		orderBys.add(new OrderBy(OrderBy.OrderByFunction.MIN, "field_3", OrderBy.Direction.DESC));
		List<GroupBy> groupBys = new ArrayList<GroupBy>();
		groupBys.add(new GroupByFixedBinWidth("field_4", 100));
		groupBys.add(new GroupByDynamicBinWidth("field_5", "10,20,30"));
		aqb.setGroupBy(groupBys);
		aqb.setOrderBy(orderBys);

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=MIN&aggregation-field=field_2"
				+ "&order-by-function=MIN&order-by-field=field_2&order-by-direction=ASC&order-by-function=MIN&order-by-field=field_3&order-by-direction=DESC"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5"
				+ "&group-by-field=field_4&bin-width=100&group-by-field=field_5&bins=10,20,30", aqb.toUrlString());
	}
	
	@Test
	public void testSumAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().sum("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=SUM&aggregation-field=field_2"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5", aqb.toUrlString());
	}
	
	@Test
	public void testStdevAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().stdev("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=STDEV&aggregation-field=field_2"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5", aqb.toUrlString());
	}
	
	@Test
	public void testVarianceAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().variance("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=VARIANCE&aggregation-field=field_2"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5", aqb.toUrlString());
	}
	
	@Test
	public void testUcountAggregateQuery() {

		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().ucount("field_2").limit(10)
				.timeout(1000);
		aqb.setContentPackFields(new ArrayList<String>(Arrays.asList(new String[] { "test1", "test2" })));
		aqb.addContentPackFields(Arrays.asList(new String[] { "test3", "test5" }));
		ConstraintBuilder cb = AggregateQueryBuilderTest.getSampleConstraintBuilder1();
		aqb.setConstraints(cb.build());
		ConstraintBuilder cb2 = AggregateQueryBuilderTest.getSampleConstraintBuilder2();
		aqb.addConstraints(cb2.build());

		assertEquals(AGGREGATE_URL_PREFIX + AggregateQueryBuilderTest.getConstrainString1()
				+ AggregateQueryBuilderTest.getConstrainString2() + "?" + SAMPLE_QUERY_PARAMS_1 + "&"
				+ "aggregation-function=UCOUNT&aggregation-field=field_2"
				+ "&content-pack-fields=test1&content-pack-fields=test2"
				+ "&content-pack-fields=test3&content-pack-fields=test5", aqb.toUrlString());
	}
}
