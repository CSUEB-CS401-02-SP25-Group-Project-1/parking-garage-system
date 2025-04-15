package server;
public class Message {
	private MessageType messageType;
	private String from;
	private String text;

	public Message(MessageType type, String sender, String contents) {
		messageType = type;
		from = sender;
		text = contents;
	}

	public MessageType getType() {return messageType;}
	public String getSender() {return from;}
	public String getText() {return text;}

	//define Enum within Message class?
	public enum MessageType {
		Success,
		Fail,
		Request
	};

}