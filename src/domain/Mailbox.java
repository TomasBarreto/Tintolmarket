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
}