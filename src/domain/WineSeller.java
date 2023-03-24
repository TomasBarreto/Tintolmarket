package src.domain;

/**
 * The WineSeller class represents a wine seller with a name, price, and quantity of wine
 available for sale.
 */
public class WineSeller {
	private String seller;
	private int price;
	private int quantity;

	/**
	 * Constructor for creating a new WineSeller object with a given seller name, price, and
	 quantity of wine available.
	 * @param seller The name of the wine seller.
	 * @param price The price of the wine being sold by the seller.
	 * @param quantity The quantity of wine available for sale by the seller.
	 */
	public WineSeller(String seller,int price,int quantity) {
        this.seller = seller;
        this.price = price;
        this.quantity = quantity;
    }

	/**
	 * Returns the name of the wine seller.
	 * @return The name of the wine seller.
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * Sets the name of the wine seller.
	 * @param seller The new name of the wine seller.
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}

	/**
	 * Returns the price of the wine being sold by the seller.
	 * @return The price of the wine being sold by the seller.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the price of the wine being sold by the seller.
	 * @param price The new price of the wine being sold by the seller.
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Returns the quantity of wine available for sale by the seller.
	 * @return The quantity of wine available for sale by the seller.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity of wine available for sale by the seller.
	 * @param quantity The new quantity of wine available for sale by the seller.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Removes a specified quantity of wine from the seller's inventory.
	 * @param quantity The quantity of wine to be removed from the seller's inventory.
	 */
	public void removeQuantity(int quantity) {
        this.quantity-=quantity;
    }
}
