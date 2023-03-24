package src.domain;

/**
 * The Wallet class represents a User's wallet and handles the money management.
 */
public class Wallet {
	private int credit;

	/**
	 * Constructs a new Wallet with a default credit of 200.
	 */
	public Wallet() {
		this.credit = 200;
	}

	/**
	 * Constructs a new Wallet object with the specified credit.
	 * @param credit the initial money balance
	 */
	public Wallet(int credit) {
		this.credit = credit;
	}

	/**
	 * Returns the current money balance.
	 * @return the current money balance
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * Sets the current money balance to the specified value.
	 * @param credit the new money balance
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}

	/**
	 * Returns the current money balance. Equivalent to calling {@link #getCredit()}.
	 * @return the current money balance
	 */
	public int getMoney() {
		return this.credit;
	}

	/**
	 * Reduces the current money balance by the specified wine price.
	 * @param winePrice the price of the wine to be bought
	 * @return the updated money balance
	 */
	public int reduceBalance(int winePrice) {
		this.credit = this.credit - winePrice;
		return this.credit;
	}

	/**
	 * Adds the specified amount of money to the current money balance.
	 * @param money the amount of money to be added
	 * @return the updated money balance
	 */
    public int addMoney(int money) {
		this.credit += money;
		return this.credit;
    }
}