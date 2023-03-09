package src.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import src.interfaces.ITintolmarketStub;

public class TintolmarketStub implements ITintolmarketStub {

	private Socket clientSocket;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private String userID;

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
		this.userID = userID;
		
		return true;
	}

	@Override
	public boolean addWine(String wine, String image) throws IOException, ClassNotFoundException {
		Command cmd = new Command();
		cmd.setCommand("add");
		cmd.setWine(wine);
		cmd.setImage(image);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (boolean) inStream.readObject();
	}

	@Override
	public boolean sellWine(String wine, int value, int quantity) throws IOException, ClassNotFoundException {
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

		return (boolean) inStream.readObject();
	}

	@Override
	public String viewWine(String wine) throws IOException, ClassNotFoundException {
		Command cmd = new Command();
		cmd.setCommand("view");
		cmd.setWine(wine);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (String) inStream.readObject();
	}

	@Override
	public String buyWine(String wine, String seller, int quantity) throws IOException, ClassNotFoundException {
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

		return (String) inStream.readObject();
	}

	@Override
	public int viewWallet() throws IOException, ClassNotFoundException {
		Command cmd = new Command();
		cmd.setCommand("wallet");
		cmd.setUserReceiver(userID);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (int) inStream.readObject();
	}

	@Override
	public boolean classifyWine(String wine, int stars) throws IOException, ClassNotFoundException {
		Command cmd = new Command();
		cmd.setCommand("classify");
		cmd.setWine(wine);
		cmd.setWineStars(stars);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (boolean) inStream.readObject();
	}

	@Override
	public boolean sendMessage(String user, String message) throws IOException, ClassNotFoundException {
		Command cmd = new Command();
		cmd.setCommand("talk");
		cmd.setUserReceiver(user);
		cmd.setMessage(message);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (boolean) inStream.readObject();
	}

	@Override
	public String readMessages() throws IOException, ClassNotFoundException {
		Command cmd = new Command();
		cmd.setCommand("read");
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) inStream.readObject();
	}
	
	public void stop() throws IOException {
		inStream.close();
		outStream.close();
		clientSocket.close();
	}
}
