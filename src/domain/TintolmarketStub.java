package src.domain;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.regex.Pattern;

import src.interfaces.ITintolmarketStub;

import javax.imageio.ImageIO;

public class TintolmarketStub implements ITintolmarketStub {

	private Socket clientSocket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String userID;

	public TintolmarketStub(Socket socket, ObjectOutputStream outStream, ObjectInputStream inStream) {
		this.clientSocket = socket;
		this.outStream = outStream;
		this.inStream = inStream;
	}

	public boolean autenticate(String userID, String passWord) throws IOException, ClassNotFoundException {
		outStream.writeObject(userID);
		outStream.writeObject(passWord);
		if ((boolean) inStream.readObject() == false) {
			System.out.println("closed");
			return false;
		}
		this.userID = userID;
		return true;
	}

	@Override
	public void addWine(String wine, String imageUrl){

		BufferedImage buffer;
		System.out.println(imageUrl);

		try {
			buffer = ImageIO.read(new FileInputStream(imageUrl));
			System.out.println(1);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if(buffer == null)
			System.out.println("OLA");

		String[] imageName = imageUrl.split(Pattern.quote(File.separator));

		Command cmd = new Command();
		cmd.setCommand("add");
		cmd.setWine(wine);
		cmd.setImageName(imageName[imageName.length - 1]);
		cmd.setImageBuffer(buffer);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sellWine(String wine, int value, int quantity){
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
	public void viewWine(String wine){
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
	public void buyWine(String wine, String seller, int quantity){
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
	public void viewWallet(){
		Command cmd = new Command();
		cmd.setCommand("wallet");
		cmd.setUserReceiver(userID);
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void classifyWine(String wine, float stars){
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
	public void sendMessage(String user, String message){
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
	public void readMessages(){
		Command cmd = new Command();
		cmd.setCommand("read");
		
		try {
			outStream.writeObject(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() throws IOException {
		Command cmd = new Command();
		cmd.setCommand("stop");
		outStream.writeObject(cmd);
	}
}
