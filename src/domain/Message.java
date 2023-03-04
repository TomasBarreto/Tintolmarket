package src.domain;

public class Message {
	
	private final User from;
	private final User to;
	private String message;
	
	public Message(User from, User to, String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}
}
