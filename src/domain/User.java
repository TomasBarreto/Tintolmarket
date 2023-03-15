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

	public int getWalletMoney() {
		return this.wallet.getMoney();
	}

	public String readMessages() {
		return mailBox.readMessages();
	}

	public void reduceBalance(int winePrice) {
		this.wallet.reduceBalance(winePrice);
	}

	public void loadMessage(Message newMessage) {
		this.mailBox.loadMessage(newMessage);
	}

    public void increaseWalletMoney(int money) {
		this.wallet.addMoney(money);
    }
}
