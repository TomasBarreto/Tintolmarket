
package src.domain;

import src.interfaces.ITintolmarketServerSkel;

public class TintolmarketServerSkel implements ITintolmarketServerSkel {
	
	private UserCatalog userCat;
	private WineCatalog wineCat;
	
	public TintolmarketServerSkel() {
		this.userCat = new UserCatalog();
		this.wineCat = new WineCatalog();
	}

	public synchronized boolean addWine(String wine, String image) {
		return wineCat.addWine(wine, image);
	}

	public synchronized boolean sellWine(String wine, int value, int quantity, String seller) {
		return wineCat.sellWine(wine, value, quantity, seller);
	}

	public String viewWine(String wine) {
		return wineCat.viewWine(wine);
	}
	
	public synchronized String buyWine(String wine, String seller, int quantity, String userID) {
		int balance = userCat.getWalletMoney(userID);
		String result = wineCat.buyWine(wine, seller, quantity, balance);
		if(result.equals("Success! Your order is completed!")){
			int winePrice = wineCat.getWinePrice(wine, seller);
			this.userCat.reduceBalance(userID, winePrice * quantity);
		}
		return result + "\n";
	}

	public int viewWallet(String userID) {
		return this.userCat.getWalletMoney(userID);
	}
	
	public synchronized boolean classifyWine(String wine, int stars) {
		return this.wineCat.classifyWine(wine, stars);
	}
	
	public synchronized boolean sendMessage(String user, String userFrom, String message){
		return userCat.sendMessage(user, userFrom, message);
	}

	public synchronized String readMessages(String userID) {
		return userCat.readMessages(userID);
	}
}
