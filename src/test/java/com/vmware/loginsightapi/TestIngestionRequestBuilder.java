package com.vmware.loginsightapi;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.Message;

public class TestIngestionRequestBuilder {

	@Test
	public void testIngestionRequestBuilder() {
		IngestionRequest request = new IngestionRequestBuilder().withMessage(new Message("message line 1"))
				.withMessage(new MessageBuilder("message line 2").withField("field1", "content 1").build()).build();
		List<Message> messages = request.getMessages();
		Assert.assertEquals("Invalid number of message", 2, messages.size());
		Assert.assertEquals("Invalid message text", "message line 2", messages.get(1).getText());
	}

}
