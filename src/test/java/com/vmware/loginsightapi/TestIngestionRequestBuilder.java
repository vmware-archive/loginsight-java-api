package com.vmware.loginsightapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vmware.loginsightapi.core.Field;
import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.Message;

public class TestIngestionRequestBuilder {

	@Test
	public void testIngestionRequestWithBasicMessage1() {
		IngestionRequest request = new IngestionRequestBuilder().message(new Message("message line 1"))
				.message(new MessageBuilder("message line 2").field("field1", "content 1").build()).build();
		List<Message> messages = request.getMessages();
		assertEquals("Invalid number of message", 2, messages.size());
		assertEquals("Invalid message text", "message line 2", messages.get(1).getText());
	}

	@Test
	public void testIngestionRequestWithBasicMessage2() {
		IngestionRequest request = new IngestionRequestBuilder().message(new Message("message line 1"))
				.message(new MessageBuilder("message line 2").field("field1", 8, 4).build()).build();
		List<Message> messages = request.getMessages();
		List<Field> fields = messages.get(1).getFields();
		assertEquals(fields.get(0).getStartPosition(), 8);
		assertEquals(fields.get(0).getLength(), 4);
		assertEquals("Invalid number of message", 2, messages.size());
		assertEquals("Invalid message text", "message line 2", messages.get(1).getText());
	}

	@Test
	public void testIngestionRequestWithMessages1() {
		List<Message> messages = new ArrayList<Message>();
		Long timestamp = 1471886543205L;
		messages.add(new MessageBuilder("Sample message text1").build());
		messages.add(new MessageBuilder("Sample message text2").timestamp(timestamp).build());
		messages.add(new MessageBuilder("Sample message text3").currentTimestamp().build());
		messages.add(new MessageBuilder("Sample message text4").field("field_1", "content_1").build());
		messages.add(new MessageBuilder("Sample message text5").field("field2", 3, 4).build());
		messages.add(new MessageBuilder("Sample message text6").field("field_1", "content_1").field("field2", 3, 4).build());
		messages.add(new MessageBuilder("Sample message text7").field("field_1", "content_1").field("field2", 3, 4)
				.timestamp(timestamp).build());
		messages.add(new MessageBuilder("Sample message text8").field("field_1", "content_1").field("field2", 3, 4)
				.currentTimestamp().build());
		
		IngestionRequest request = new IngestionRequestBuilder().messages(messages).build();
		assertEquals(8, request.count());
		assertEquals("Sample message text2", request.getMessages().get(1).getText());
		assertEquals(timestamp, request.getMessages().get(1).getTimestamp());
		assertNull(request.getMessages().get(3).getTimestamp());
	}
}
