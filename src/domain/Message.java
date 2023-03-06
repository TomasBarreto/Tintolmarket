package src.domain;

public class Message {
	
	private final String to;
	private String message;
	
	public Message(String to, String message) {
		this.to = to;
		this.message = message;
	}
}
