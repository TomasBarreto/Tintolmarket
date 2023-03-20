
package src.domain;

import src.interfaces.ITintolmarketServerSkel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TintolmarketServerSkel implements ITintolmarketServerSkel {
	
	private UserCatalog userCat;
	private WineCatalog wineCat;
	
	public TintolmarketServerSkel() {
		this.userCat = new UserCatalog();
		this.wineCat = new WineCatalog();
		loadUsers();
		loadWine();
		loadSellers();
		loadMessages();
	}

	@Override
	public String addWine(String wine, String imageName, BufferedImage imageBuffer) {
		boolean value = wineCat.addWine(wine, imageName, imageBuffer);
		if(value){
			return "Wine added successfully\n";
		}else {
			return "Wine already in system\n";
		}
	}

	public synchronized String sellWine(String wine, int value, int quantity, String seller) {
		boolean bool = wineCat.sellWine(wine, value, quantity, seller);
		if (bool){
			return "Wine is now for sale\n";
		}else{
			return "Wine doesnt exist\n";
		}
	}

	public String viewWine(String wine) {
		return wineCat.viewWine(wine);
	}
	
	public synchronized String buyWine(String wine, String seller, int quantity, String userID) {
		int balance = userCat.getWalletMoney(userID);
		String result = wineCat.buyWine(wine, seller, quantity, balance);
		if(result.equals("Success! Your order is completed!")){
			int winePrice = wineCat.getWinePrice(wine, seller);
			this.userCat.reduceBalance(userID, winePrice * quantity);
			this.userCat.increaseBalance(seller, winePrice * quantity);
		}

		return result + "\n";
	}

	public String viewWallet(String userID) {
		return "Wallet: " + this.userCat.getWalletMoney(userID) + "\n";
	}
	
	public synchronized String classifyWine(String wine, int stars) {
		boolean value = this.wineCat.classifyWine(wine, stars);
		if(value){
			return "Wine classified successfully\n";
		} else{
			return "Wine doesnt exist\n";
		}
	}
	
	public synchronized String sendMessage(String user, String userFrom, String message) {
		boolean value = userCat.sendMessage(user, userFrom, message);
		if(value){
			return "Message sent!\n";
		} else{
			return "User not found\n";
		}
	}

	public synchronized String readMessages(String userID) {
		return userCat.readMessages(userID);
	}

	private void loadUsers() {
		try{
			File file = new File("Users");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String userAndPass [] = line.split(":");
				if(userAndPass.length > 1)
					this.userCat.addUser(userAndPass[0]);
			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}

	}

	public void addUser(String userID) {
		this.userCat.addUser(userID);
	}

	private void loadWine() {
		try{
			File file = new File("wine_cat");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String wine [] = line.split(":");
				if(wine.length > 1)
					this.wineCat.loadWine(wine[0], wine[1], wine[2], wine[3]);
			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}
	}

	private void loadSellers() {
		try{
			File file = new File("wine_sellers");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String seller [] = line.split(":");
				if (seller.length > 1)
					this.wineCat.loadSeller(seller);
			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}
	}

	private void loadMessages() {
		try{
			File file = new File("messages");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String msg [] = line.split(":");
				if (msg.length > 1){
					this.userCat.loadMessage(msg[1], msg[3], msg[5]);
				}

			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}
	}

}
