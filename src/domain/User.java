package domain;

public class User {
	private final String id;
	private Wallet wallet;
	private Mailbox mailBox;
	
	public User(String id) {
		this.id = id;
		this.wallet = new Wallet();
		this.mailBox = new MailBox();
	}
}
