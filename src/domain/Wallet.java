package src.domain;

public class Wallet {
	private int credit;
	
	public Wallet() {
		this.credit = 200;
	}

	public Wallet(int credit) {
		this.credit = credit;
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

	public int reduceBalance(int winePrice) {
		this.credit = this.credit - winePrice;
		return this.credit;
	}

    public int addMoney(int money) {
		this.credit += money;
		return this.credit;
    }
}