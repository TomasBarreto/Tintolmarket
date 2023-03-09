package src.domain;

public class Wallet {
	private int credit;
	
	public Wallet() {
		this.credit = 200;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getMoney() {
		return this.credit;
	}

	public void reduceBalance(int winePrice) {
		this.credit = this.credit - winePrice;
	}
}