package src.interfaces;

import src.domain.Wine;

import java.io.ObjectOutputStream;

import src.domain.User;

public interface ITintolmarketServerStub {
	public void addWine(Wine wine, Image image, ObjectOutputStream outStream);
	
	public void sellWine(Wine wine, int value, int quantity, ObjectOutputStream outStream);
	
	public void viewWine(Wine wine, ObjectOutputStream outStream);
	
	public void buyWine(Wine wine, User seller, int quantity, ObjectOutputStream outStream);
	
	public void viewWallet(ObjectOutputStream outStream);
	
	public void classifyWine(Wine wine, int stars, ObjectOutputStream outStream);

	public void sendMessage(User user, String message, ObjectOutputStream outStream);
	
	public void readMessages(ObjectOutputStream outStream);
}
