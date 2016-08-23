package com.vmware.loginsightapi;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.vmware.loginsightapi.core.LogInsightApiException;
import com.vmware.loginsightapi.core.Message;

public class MessageBuilderTest {

	@Test
	public void testCreatingEmptyMessage() {
		Message msg = new MessageBuilder().build();
		assertNull(msg.getText());
		assertTrue(msg.getFields().isEmpty());
		assertNull(msg.getTimestamp());
		assertTrue(msg.checkIsEmpty());
		assertTrue(msg.checkIsValid());
	}

	@Test
	public void testCreatingMessageWithMessageText() {
		String messageLine = "Sample Message line";
		Message msg = new MessageBuilder(messageLine).build();
		assertEquals(msg.getText(), messageLine);
		assertTrue(msg.getFields().isEmpty());
		assertNull(msg.getTimestamp());
		assertTrue(!msg.checkIsEmpty());
		assertTrue(msg.checkIsValid());
	}

	@Test
	public void testCreatingMessageWithMessageTextAndOneFieldWithContent() {
		String messageText = "Sample Message line";
		Message msg = new MessageBuilder(messageText).field("field_1", "content_1").build();
		assertTrue(msg.getFields().size() == 1);
		assertEquals(msg.getText(), messageText);
		assertNull(msg.getTimestamp());
		assertTrue(!msg.checkIsEmpty());
		assertTrue(msg.checkIsValid());
	}

	@Test
	public void testCreatingMessageWithMessageTextAndOneFieldWithPosition() {
		String messageText = "Sample Error Message line";
		Message msg = new MessageBuilder(messageText).field("field_1", 7, 5).build();
		assertTrue(msg.getFields().size() == 1);
		assertEquals(msg.getText(), messageText);
		assertNull(msg.getTimestamp());
		assertTrue(!msg.checkIsEmpty());
		assertTrue(msg.checkIsValid());
	}

	@Test
	public void testCreatingMessageWithMessageTextAndGivenTimestamp() {
		String messageText = "Sample Error Message line";
		Long timestamp = 1471877543743L;
		MessageBuilder mb = new MessageBuilder(messageText).field("field_1", 7, 5).timestamp(timestamp);
		mb.field("field_2", "content_2");
		Message msg = mb.build();
		assertTrue(msg.getFields().size() == 2);
		assertEquals(msg.getText(), messageText);
		assertEquals(msg.getTimestamp(), timestamp);
		assertTrue(msg.getTimestamp() == 1471877543743L);
		assertTrue(!msg.checkIsEmpty());
		assertTrue(msg.checkIsValid());
	}

	@Test
	public void testCreatingMessageWithMessageTextAndCurrentTimestamp() {
		String messageText = "Sample Error Message line";
		Message msg = new MessageBuilder(messageText).field("field_1", 7, 5).currentTimestamp().build();
		assertTrue(msg.getFields().size() == 1);
		assertEquals(msg.getText(), messageText);
		assertNotNull(msg.getTimestamp());
		assertTrue(!msg.checkIsEmpty());
		assertTrue(msg.checkIsValid());
	}

	@Test
	public void testCreatingInvalidFieldLengthInMessage() {
		try {
			String messageText = "Sample Error Message line";
			Message msg = new MessageBuilder(messageText).field("field_1", 7, messageText.length() + 2)
					.currentTimestamp().build();
			assertTrue(false);
		} catch (LogInsightApiException ex) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreatingInvalidFieldStartPosInMessage() {
		try {
			String messageText = "Sample Error Message line";
			Message msg = new MessageBuilder(messageText).field("field_1", -2, 7 + 2)
					.currentTimestamp().build();
			assertTrue(false);
		} catch (LogInsightApiException ex) {
			assertTrue(true);
		}
	}
}
