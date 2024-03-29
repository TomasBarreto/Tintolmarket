package src.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The WineFileHandler class is responsible for handling the files related to the wine store.
 * It provides methods to add wines and sellers to their respective files, update seller
 statistics and wine ratings.
 */
public class WineFileHandler {
	
	private static final String WINE_CAT_FILE = "wine_cat";
	private static final String WINE_SELLERS_FILE = "wine_sellers";
	private File wineCat;
	private File wineSellers;

	/**
	 * Constructor that initializes the files for wine categories and sellers.
	 */
	public WineFileHandler() {
		this.wineCat = new File(WINE_CAT_FILE);
		this.wineSellers = new File(WINE_SELLERS_FILE);
	}

	/**
	 * Method that alters the file based on the given command.
	 * @param cmd The command to execute.
	 */
	public synchronized void alterFile(Command cmd) {
        try{            
            switch(cmd.getCommand()) {
                case "addWine":			FileWriter fw1 = new FileWriter(WINE_CAT_FILE, true);
                						BufferedWriter bw1 = new BufferedWriter(fw1);

                						bw1.write(cmd.getWine() + ":" +
                						cmd.getImageName() + ":" + "0" + ":" + "0" + "\n");

                						bw1.close();
                						fw1.close();

					                    break;

                case "updateSellerStats":	Scanner sc2 = new Scanner(this.wineSellers);
											List<String> lines = new ArrayList<>();
                
                							while(sc2.hasNextLine()) {
												String line = sc2.nextLine();
                								String[] tokens = line.split(":");
                							
                								if(tokens[0].equals(cmd.getWine()) &&
                							   		tokens[1].equals(cmd.getWineSeller())) {
                									tokens[2] = "" + cmd.getWinePrice();
                									tokens[3] = "" + cmd.getWineQuantity();
													String frase = tokens[0] + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3];
                									lines.add(frase);
                								}
                									else
                										lines.add(line);
                							}

											FileWriter fw2 = new FileWriter(WINE_SELLERS_FILE, false);

											for(int i = 0; i < lines.size(); i++){
												fw2.write(lines.get(i) + "\n");
											}

                							sc2.close();
                							fw2.close();

                    						break;
                    					
                case "addSeller":		FileWriter fw4 = new FileWriter(WINE_SELLERS_FILE, true);
						                BufferedWriter bw4 = new BufferedWriter(fw4);
                	
                						bw4.write(cmd.getWine() + ":" + cmd.getWineSeller() + 
                						":" + cmd.getWinePrice() + ":" + cmd.getWineQuantity()  + "\n");

										bw4.close();
										fw4.close();
                    					break;
                    					
                case "updateRating":	Scanner sc5 = new Scanner(this.wineCat);

						                List<String> lines2 = new ArrayList<>();
						
										while(sc5.hasNextLine()) {
											String line = sc5.nextLine();

											
											String[] tokens = line.split(":");
											
											if(tokens[0].equals(cmd.getWine())) {
												tokens[2] = "" + (Integer.parseInt(tokens[2]) + 1);
												tokens[3] = "" + (Float.parseFloat(tokens[3]) + cmd.getWineStars());
												lines2.add(tokens[0] + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3]);
											} 
											else
												lines2.add(line);
										}

										FileWriter fw5 = new FileWriter(WINE_CAT_FILE, false);

							            for(int i = 0; i < lines2.size(); i++)
							            	fw5.write(lines2.get(i) + "\n");

										fw5.close();
										sc5.close();
										break;
                    					
                default:                break;
            }
        } catch (IOException e){
            System.out.println("File not found");
        }
	}
}
