package tests;

import shared.*;
import static org.junit.Assert.*;
import org.junit.*;

public class MessageTest {
	@Test
	public void testMessageType() {
		// should return MessageType.Success
		Message temp = new Message(MessageType.Success, null, null);
		assertEquals(temp.getType(), MessageType.Success);
		
		// should return MessageType.Fail
		temp = new Message(MessageType.Fail, null, null);
		assertEquals(temp.getType(), MessageType.Fail);
		
		// should return MessageType.Request
		temp = new Message(MessageType.Request, null, null);
		assertEquals(temp.getType(), MessageType.Request);
	}
	
	@Test
	public void testMessageSender() {
		// should return same sender
		String sender = "SomeTestClient";
		Message temp = new Message(null, sender, null);
		assertEquals(temp.getSender(), sender);
	}
	
	@Test
	public void testMessageText() {
		// should return same text
		String text = "I should be able to handle carrying a large string of text through transit.";
		Message temp = new Message(null, null, text);
		assertEquals(temp.getText(), text);
	}
}
