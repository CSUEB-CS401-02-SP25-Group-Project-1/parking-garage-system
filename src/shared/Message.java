package shared;

import java.io.Serializable;

//Message class is used by Server, EmployeeGUI, and CustomerGUI
//to communicate. `From` is used by the server to determine the 
//level of authority of the client. `messageType` is used to by
//either to understand whether this message is a Request, a 
//successful response, or an unsuccessful response. If the 
//Server or client are Requesting, the specific request is 
//encoded in the text field, according to the following:
//
//The format is "cm:text" where `cm` is short for command (two letters)
//
// Commands	|    Mneumonic
//------------------------------------------
//	`in`	:	init  		: initializes connection 
//	`gt`	:	generate ticket	: Use by both to make a ticket
//	`tg`	:	toggle gate	: Employees raise/lower their gate
//	`vr`	:	view report	: displays stats back to the user
//	`pt`	:	pay ticket	: pays off & closes a ticket
//	`li`	:	log-in		: Employees authorize access
//	`mp`	:	modify password	: Employee changes their password
//	`mg`	:	modify gatetime	: Employee in/decreases gate open time
//	`ot`	:	override ticket	: Employee closes ticket without pay
//	`mr`	: 	modify rate	: Employees modify hourly rate
//	`vl`	:	view log	: Shows a section of the log to Employees
//	`vv`	:	view vehicles	: Displays list of ticket IDs
//	`vc`	:	view cameras	: Sends fake image back to client
//	`va`	:	view amount	: viewer sees # of cars in garage
//

@SuppressWarnings("serial")
public class Message implements Serializable {
	private MessageType messageType;
	private String from;
	private String text;

	public Message(MessageType type, String sender, String contents) {
		messageType = type;
		from = sender;
		text = contents;
	}

	public MessageType getType() {
		return messageType;
	}
	
	public String getSender() {
		return from;
	}
	
	public String getText() {
		return text;
	}
}
