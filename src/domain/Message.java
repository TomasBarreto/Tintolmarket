package src.domain;

public class Message {
	
	private final String to;
	private final String from;
	private String message;
	
	public Message(String to, String from, String message) {
		this.to = to;
		this.from = from;
		this.message = message;
	}

	public String read() {
		return this.from + ":" + this.message + "\n";
	}
}
