package src.domain;

import java.util.ArrayList;
import java.util.List;

public class Mailbox {
	private List<Message> mailBox;
	
	public Mailbox() {
		this.mailBox = new ArrayList<>();
	}

	public void receiveMessage(Message message) {
		mailBox.add(message);
	}

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
}