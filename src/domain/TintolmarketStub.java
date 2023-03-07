package src.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import src.interfaces.ITintolmarketStub;

public class TintolmarketStub implements ITintolmarketStub {

	Socket clientSocket;
	ObjectInputStream inStream;
	ObjectOutputStream outStream;

	public TintolmarketStub(String ip, int port) throws IOException {
		this.clientSocket = new Socket(ip, port);
		this.inStream = new ObjectInputStream(clientSocket.getInputStream());
		this.outStream = new ObjectOutputStream(clientSocket.getOutputStream());

	}

	public boolean autenticate(String userID, String passWord) throws IOException, ClassNotFoundException {
		outStream.writeObject(userID);
		outStream.writeObject(passWord);

		if ((boolean) inStream.readObject() == false) {
			inStream.close();
			outStream.close();
			return false;
		}
		return true;
	}

	@Override
	public void addWine(String wine, String image) {
		Command cmd = new Command();
		cmd.setCommand("add");
		cmd.setWine(wine);
		cmd.setImage(image);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sellWine(String wine, int value, int quantity) {
		Command cmd = new Command();
		cmd.setCommand("sell");
		cmd.setWine(wine);
		cmd.setWinePrice(value);
		cmd.setWineQuantity(quantity);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void viewWine(String wine) {
		Command cmd = new Command();
		cmd.setCommand("view");
		cmd.setWine(wine);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void buyWine(String wine, String seller, int quantity) {
		Command cmd = new Command();
		cmd.setCommand("buy");
		cmd.setWine(wine);
		cmd.setWineSeller(seller);
		cmd.setWineQuantity(quantity);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void viewWallet() {
		Command cmd = new Command();
		cmd.setCommand("wallet");
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void classifyWine(String wine, int stars) {
		Command cmd = new Command();
		cmd.setCommand("classify");
		cmd.setWine(wine);
		cmd.setWineStars(stars);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(String user, String message) {
		Command cmd = new Command();
		cmd.setCommand("talk");
		cmd.setUserReceiver(user);
		cmd.setMessage(message);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readMessages() {
		Command cmd = new Command();
		cmd.setCommand("read");
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() throws IOException {
		inStream.close();
		outStream.close();
		clientSocket.close();
	}
}
