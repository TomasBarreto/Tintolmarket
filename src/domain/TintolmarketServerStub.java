package src.domain;

import java.util.List;

import src.interfaces.ITintolmarketServerStub;

public class TintolmarketServerStub implements ITintolmarketServerStub {
	private List<User> listUsers;
	private SellerList listSeller;
	private WineCatalog winCat;
	
	public TintolmarketServerStub() {
		this.listSeller = new SellerList();
		this.winCat = new WinCatalog();
	}
	public void addWine(String wine, String image, ObjectOutputStream outStream) {
		if(!winCat.containWine(wine)) {
			List<WineSeller> listSeller= new ArrayList<WineSeller>();
			listSeller.put(wine,newSeller);
			winCat.addWine(wine, image);
		}
		else {
			//erro
		}
		
	}
	
	public void sellWine(String wine, int value, int quantity, String  User ObjectOutputStream outStream) {
		
	}
	
	public void viewWine(String wine, ObjectOutputStream outStream) {
		StringBuilder sb = new StringBuilder();
		sb.append(winCat.getInfo(wine));
		
	}
	
	public void buyWine(String wine, User seller, int quantity, ObjectOutputStream outStream) {
		
	}
	
	public void viewWallet(ObjectOutputStream outStream) {
		
	}
	
	public void classifyWine(Wine wine, int stars, ObjectOutputStream outStream) {
		
	}

	public void sendMessage(User user, String message, ObjectOutputStream outStream) {
		
	}
	
	public void readMessages(ObjectOutputStream outStream) {
		
	}
}
