package src.domain;

public class Wallet {
	private int credit;
	
	public Wallet() {
		this.credit = 200;
	}

	public int getMoney() {
		return this.credit;
	}
}