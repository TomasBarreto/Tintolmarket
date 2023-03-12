package src.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WineFileHandler {
	
	private static final String WINE_CAT_FILE = "wine_cat.txt";
	private static final String WINE_SELLERS_FILE = "wine_sellers.txt";
	private File wineCat;
	private File wineSellers;

	public WineFileHandler() {
		this.wineCat = new File(WINE_CAT_FILE);
		this.wineSellers = new File(WINE_SELLERS_FILE);
	}
	
	public synchronized void alterFile(Command cmd) {
        try{
            Scanner sc1 = new Scanner(this.wineCat);
            FileWriter fw1 = new FileWriter(WINE_CAT_FILE, true);
            BufferedWriter bw1 = new BufferedWriter(fw1);
            
            Scanner sc2 = new Scanner(this.wineSellers);
            FileWriter fw2 = new FileWriter(WINE_SELLERS_FILE, true);
            BufferedWriter bw2 = new BufferedWriter(fw2);

            switch(cmd.getCommand()) {
                case "addWine":			bw1.write(cmd.getWine() + ":" + 
                						cmd.getImage() + ":" + "0" + ":" + "0" + "\n");
                
					                    break;
                case "updateSeller":	
                    break;
                case "addSeller":		bw2.write(cmd.getWine() + ":" + cmd.getWineSeller() + 
                						":" + cmd.getWineQuantity() + ":" + cmd.getWinePrice() + "\n");
                
                    					break;
                case "updateRating":
                    					break;
                case "deleteSeller":
                    					break;
                case "updateQuantity":
                    					break;

                default:                break;
            }

        } catch (IOException e){
            System.out.println("File not found");
        }

    }
}
