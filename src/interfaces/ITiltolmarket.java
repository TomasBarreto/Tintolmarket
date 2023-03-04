package interfaces;

public interface ITiltolmarket {
	
	public void addWine(Wine wine, Image image);
	
	public void sellWine(Wine wine, int value, int quantity);
	
	public void viewWine(Wine wine);
	
	public void buyWine(Wine wine, User seller, int quantity);
	
	public void viewWallet();
	
	public void classifyWine(Wine wine, int stars);

	public void sendMessage(User user, String message);
	
	public void readMessages();
}
