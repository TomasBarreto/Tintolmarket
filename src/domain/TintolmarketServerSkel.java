
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
	

	public boolean addWine(String wine, String image, ObjectOutputStream outStream) {
		return wineCat.addWine(wine, image);
	}


	public boolean sellWine(String wine, int value, int quantity, String seller, ObjectOutputStream outStream) {
		return wineCat.sellWine(wine, value, quantity, seller);
	}

	public String viewWine(String wine, ObjectOutputStream outStream) {
		return wineCat.viewWine(wine);
	}
	
	public void buyWine(String wine, String seller, int quantity, ObjectOutputStream outStream) {
		
	}
	

	public void viewWallet(ObjectOutputStream outStream) {
		
	}
	
	public void classifyWine(String wine, int stars, ObjectOutputStream outStream) {
		
	}
	
	public void sendMessage(String user, String message, ObjectOutputStream outStream) throws IOException {
		userCat.sendMessage(user, message, outStream);
	}


	public void readMessages(ObjectOutputStream outStream) {
		
	}
}
