package src.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class WineFileHandler {
	private static final String WINE_FILE = "wines.txt";
	private static final String PERMISSIONS = "rw";
	private RandomAccessFile handler;
	
	public WineFileHandler() {
		try {
			this.handler = new RandomAccessFile(new File(WINE_FILE), PERMISSIONS);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void alterFile(Command operation) {
		switch(operation.getCommand()) {
			case "addWine":			
									break;
			case "updateSeller":
									break;
			case "addSeller":
									break;
			case "updateRating":
									break;
			case "deleteSeller":
									break;
			case "updateQuantity":
									break;
									
			default: 				break;
		}
	}
}
