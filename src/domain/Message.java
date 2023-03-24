package src.domain;

/**
 * This class represents a message that can be sent from one user to another.
 */
public class Message {
	
	private final String to;
	private final String from;
	private String message;

	/**
	 * Constructs a new Message object with the specified recipient, sender, and message content.
	 * @param to the recipient of the message
	 * @param from the sender of the message
	 * @param message the content of the message
	 */
	public Message(String to, String from, String message) {
		this.to = to;
		this.from = from;
		this.message = message;
	}

	/**
	 * Returns a string representation of the message.
	 * @return a string representation of the message
	 */
	public String read() {
		return "Message sent by: " + this.from + "\ntext: " + this.message + "\n";
	}
}
