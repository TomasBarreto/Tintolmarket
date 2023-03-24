package src.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a mailbox that can receive and store messages.
 */
public class Mailbox {
	private List<Message> mailBox;

	/**
	 * Constructs a new Mailbox with an empty list of messages.
	 */
	public Mailbox() {
		this.mailBox = new ArrayList<>();
	}

	/**
	 * Receives a new message and adds it to the list of messages in the mailbox.
	 * @param message the message to be received
	 */
	public void receiveMessage(Message message) {
		mailBox.add(message);
	}

	/**
	 * Reads all the messages stored in the mailbox and returns them as a single string.
	 * After reading the messages, the mailbox is cleared.
	 * If there are no messages, a string indicating so is returned.
	 * @return a string representing all the messages in the mailbox
	 */
    public String readMessages() {
		String message = "";

		if (mailBox.size() == 0) {
			return "There are no messages available\n";
		}

		for (int i = 0; i < mailBox.size(); i++) {
			message = message + mailBox.get(i).read();
		}
		this.mailBox.clear();
		return message;
    }
	/**
	 * Adds a new message to the list of messages in the mailbox.
	 * @param newMessage the new message to be added
	 */
	public void loadMessage(Message newMessage) {
		this.mailBox.add(newMessage);
	}
}