
package src.domain;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import src.interfaces.ITintolmarketServerStub;

public class TintolmarketServerStub implements ITintolmarketServerStub {
	
	private UserCatalog userCat;
	private SellerList listSeller;
	private WineCatalog wineCat;
	
	public TintolmarketServerStub() {
		this.userCat = new UserCatalog();
		this.listSeller = new SellerList();
		this.wineCat = new WineCatalog();
	}
	

	public void addWine(String wine, String image, ObjectOutputStream outStream) {
		if(winCat.add(wine,image)) {
			listSeller.newWine(wine);
		}
		else {
			//erro
		}
		
	}
	
	public void sellWine(String wine, int value, int quantity, String seller, ObjectOutputStream outStream) {
		if(winCat.containsWine(wine)) {
			listSeller.addSeller(wine,value,quantity,seller);
		}
		else {
			//erro
		}
	}

	public void viewWine(String wine, ObjectOutputStream outStream) {
		StringBuilder sb = new StringBuilder();
		sb.append(winCat.getInfo(wine)+"\n");
		
		
	}
	
	public void buyWine(String wine, String seller, int quantity, ObjectOutputStream outStream) {
		
	}
	

	public void viewWallet(ObjectOutputStream outStream) {
		
	}
	
	public void classifyWine(String wine, int stars, ObjectOutputStream outStream) {
		
	}
	
	public void sendMessage(String user, String message, ObjectOutputStream outStream) {
		userCat.sendMessage(user, message, outStream);
	}

	public void sendMessage(String user, String message, ObjectOutputStream outStream) {
		userCat.sendMessage(user, message, outStream);
	}
	

	public void readMessages(ObjectOutputStream outStream) {
		
	}
}
