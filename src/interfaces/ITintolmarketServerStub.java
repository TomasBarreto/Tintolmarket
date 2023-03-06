package src.interfaces;

import java.io.ObjectOutputStream;

public interface ITintolmarketServerStub {
	public void addWine(String wine, String image, ObjectOutputStream outStream);
	
	public void sellWine(String wine, int value, int quantity, ObjectOutputStream outStream);
	
	public void viewWine(String wine, ObjectOutputStream outStream);
	
	public void buyWine(String wine, String seller, int quantity, ObjectOutputStream outStream);
	
	public void viewWallet(ObjectOutputStream outStream);
	
	public void classifyWine(String wine, int stars, ObjectOutputStream outStream);

	public void sendMessage(String user, String message, ObjectOutputStream outStream);
	
	public void readMessages(ObjectOutputStream outStream);
}
