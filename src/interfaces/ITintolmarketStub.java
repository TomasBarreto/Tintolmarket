package src.interfaces;

import java.io.ObjectOutputStream;

import src.domain.User;
import src.domain.Wine;

public interface ITintolmarketStub {
	
	public void addWine(Wine wine, String image, ObjectOutputStream outStream);
	
	public void sellWine(Wine wine, int value, int quantity, ObjectOutputStream outStream);
	
	public void viewWine(Wine wine, ObjectOutputStream outStream);
	
	public void buyWine(Wine wine, User seller, int quantity, ObjectOutputStream outStream);
	
	public void viewWallet(ObjectOutputStream outStream);
	
	public void classifyWine(Wine wine, int stars, ObjectOutputStream outStream);

	public void sendMessage(User user, String message, ObjectOutputStream outStream);
	
	public void readMessages(ObjectOutputStream outStream);
}
