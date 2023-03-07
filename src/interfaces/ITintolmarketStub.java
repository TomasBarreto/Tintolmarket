package src.interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;

import src.domain.User;
import src.domain.Wine;

public interface ITintolmarketStub {
	
	public boolean addWine(String wine, String image) throws IOException, ClassNotFoundException;
	
	public boolean sellWine(String wine, int value, int quantity) throws IOException, ClassNotFoundException;
	
	public String viewWine(String wine) throws IOException, ClassNotFoundException;
	
	public String buyWine(String wine, String seller, int quantity) throws IOException, ClassNotFoundException;
	
	public int viewWallet() throws IOException, ClassNotFoundException;
	
	public boolean classifyWine(String wine, int stars) throws IOException, ClassNotFoundException;

	public boolean sendMessage(String user, String message) throws IOException, ClassNotFoundException;
	
	public String readMessages() throws IOException, ClassNotFoundException;
}
