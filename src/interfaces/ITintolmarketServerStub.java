package src.interfaces;

import src.domain.Wine;

import java.io.ObjectOutputStream;

import src.domain.User;

public interface ITintolmarketServerStub {
	public void addWine(Wine wine, String image, ObjectOutputStream outStream);
	
	public void sellWine(String wine, int value, int quantity, ObjectOutputStream outStream);
	
	public void viewWine(String wine, ObjectOutputStream outStream);
	
	public void buyWine(String wine, String seller, int quantity, ObjectOutputStream outStream);
	
	public void viewWallet(ObjectOutputStream outStream);
	
	public void classifyWine(String wine, int stars, ObjectOutputStream outStream);

	public void sendMessage(String user, String message, ObjectOutputStream outStream);
	
	public void readMessages(ObjectOutputStream outStream);
}
