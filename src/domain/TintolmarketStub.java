package src.domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
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

		File file = new File(imageUrl);
		try {
			FileInputStream fs = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fs.read(bytes);

			String[] imageName = imageUrl.split(Pattern.quote(File.separator));

			Command cmd = new Command();
			cmd.setCommand("add");
			cmd.setWine(wine);
			cmd.setImageName(imageName[imageName.length - 1]);
			cmd.setImageBuffer(bytes);

			try {
				outStream.writeObject(cmd);
				System.out.println((String)inStream.readObject());

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
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
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void viewWine(String wine){
		Command cmd = new Command();
		cmd.setCommand("view");
		cmd.setWine(wine);
		
		try {
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

			String imageUrl = (String) inStream.readObject();
			byte[] imageBuffer = (byte[]) inStream.readObject();

			imageUrl = "client" + imageUrl;

			try {
				ByteArrayInputStream bs = new ByteArrayInputStream(imageBuffer);

				bs.read(imageBuffer);

				File newFile = new File(imageUrl);
				FileOutputStream fo = new FileOutputStream(newFile);
				fo.write(imageBuffer);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Scanner sc = new Scanner(System.in);
			System.out.println("Open image? (y/n)\n");
			String input = sc.nextLine();

			if(input.equals("y")) {
				File file = new File(imageUrl);
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
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
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void viewWallet(){
		Command cmd = new Command();
		cmd.setCommand("wallet");
		cmd.setUserReceiver(userID);
		
		try {
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
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
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
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
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void readMessages(){
		Command cmd = new Command();
		cmd.setCommand("read");
		
		try {
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void stop() throws IOException {
		Command cmd = new Command();
		cmd.setCommand("stop");
		outStream.writeObject(cmd);
	}
}
