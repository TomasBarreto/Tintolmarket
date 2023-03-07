package src.domain;

public class WineSeller {
	private String seller;
	private int price;
	private int quantity;
	
	public WineSeller(String seller) {
		this.seller = seller;
		this.price = 0;
		this.quantity = 0;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
