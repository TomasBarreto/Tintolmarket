package src.domain;

public class Message {
	
	private final String to;
	private String message;

	private final String from;
	
	public Message(String to, String from, String message) {
		this.to = to;
		this.from = from;
		this.message = message;
	}

	public String read() {
		return "Message sent by: " + this.from + "\ntext: " + this.message + "\n";
	}
}
