package src.domain;

public class WineSeller {
	private String seller;
	private int price;
	private int quantity;
	
	public WineSeller(String seller,int price,int quantity) {
        this.seller = seller;
        this.price = price;
        this.quantity = quantity;
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
	
	public void removeQuantity(int quantity) {
        this.quantity-=quantity;
    }
}
