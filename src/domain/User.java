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

	public User(String id, int wallet) {
		this.id = id;
		this.wallet = new Wallet(wallet);
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

	public int reduceBalance(int winePrice) {
		return this.wallet.reduceBalance(winePrice);
	}

	public void loadMessage(Message newMessage) {
		this.mailBox.loadMessage(newMessage);
	}

    public int increaseWalletMoney(int money) {
		return this.wallet.addMoney(money);
    }
}
