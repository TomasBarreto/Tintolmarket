package src.domain;

public class User {
	private static String id;
	private Wallet wallet;
	private Mailbox mailBox;
	
	public User(String id) {
		this.id = id;
		this.wallet = new Wallet();
		this.mailBox = new Mailbox();
	}

	public void receiveMessage(Message message) {
		mailBox.receiveMessage(message);
	}
}
