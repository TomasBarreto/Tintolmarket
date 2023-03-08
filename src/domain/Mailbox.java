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
		for (int i = 0; i < mailBox.size(); i++) {
			message = message + mailBox.get(i).read();
		}
		return message;
    }
}