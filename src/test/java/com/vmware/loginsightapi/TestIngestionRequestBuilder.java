package com.vmware.loginsightapi;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.Message;

public class TestIngestionRequestBuilder {

	@Test
	public void testIngestionRequestBuilder() {
		IngestionRequest request = new IngestionRequestBuilder().withMessage(new Message("message line 1"))
				.withMessage(new MessageBuilder("message line 2").withField("field1", "content 1").build()).build();
	}

	@Test
	public void testWithMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testWithMessages() {
		fail("Not yet implemented");
	}

}
