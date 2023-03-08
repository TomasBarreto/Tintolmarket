
package src.domain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import src.interfaces.ITintolmarketServerSkel;

public class TintolmarketServerSkel implements ITintolmarketServerSkel {
	
	private UserCatalog userCat;
	private WineCatalog wineCat;
	
	public TintolmarketServerSkel() {
		this.userCat = new UserCatalog();
		this.wineCat = new WineCatalog();
	}
	

	public boolean addWine(String wine, String image) {
		return wineCat.addWine(wine, image);
	}


	public boolean sellWine(String wine, int value, int quantity, String seller) {
		return wineCat.sellWine(wine, value, quantity, seller);
	}

	public String viewWine(String wine) {
		return wineCat.viewWine(wine);
	}
	
	public void buyWine(String wine, String seller, int quantity) {
		
	}
	

	public String viewWallet(String userID) {
		return "Saldo na carteira: " + this.userCat.getWalletMoney(userID);
	}
	
	public void classifyWine(String wine, int stars) {
		
	}
	
	public void sendMessage(String user, String message) throws IOException {
		userCat.sendMessage(user, message);
	}


	public void readMessages(String userID) {
		
	}
}
