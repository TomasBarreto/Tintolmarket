package src.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WineFileHandler {
	
	private static final String WINE_CAT_FILE = "wine_cat";
	private static final String WINE_SELLERS_FILE = "wine_seller";
	private File wineCat;
	private File wineSellers;

	public WineFileHandler() {
		this.wineCat = new File(WINE_CAT_FILE);
		this.wineSellers = new File(WINE_SELLERS_FILE);
	}
	
	public synchronized void alterFile(Command cmd) {
        try{            
            switch(cmd.getCommand()) {
                case "addWine":			FileWriter fw1 = new FileWriter(WINE_CAT_FILE, true);
                						BufferedWriter bw1 = new BufferedWriter(fw1);
                						
                						bw1.write(cmd.getWine() + ":" + 
                						cmd.getImage() + ":" + "0" + ":" + "0" + "\n");
                
                						bw1.close();
                						fw1.close();
                						
					                    break;
					                    
                case "updateQuantity":	Scanner sc2 = new Scanner(this.wineSellers);
						                FileWriter fw2 = new FileWriter(WINE_SELLERS_FILE, true);
						                BufferedWriter bw2 = new BufferedWriter(fw2);
						                
						                List<String> lines = new ArrayList<>();
                
                						while(sc2.hasNextLine()) {
                							String line = sc2.nextLine();
                							
                							String[] tokens = line.split(":");
                							
                							if(tokens[0].equals(cmd.getWine()) && 
                							   tokens[1].equals(cmd.getWineSeller())) {
                								tokens[2] = "" + cmd.getWineQuantity();
                								tokens[3] = "" + cmd.getWinePrice();
                								
                								lines.add(tokens[0] + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3]);
                							} 
                							else
                								lines.add(line);
                						}
                						
                						sc2.close();
                						bw2.close();
                						fw2.close();
                						wineSellers.delete();
                						
                						this.wineSellers = new File(WINE_SELLERS_FILE);
                						
                			            FileWriter fw3 = new FileWriter(WINE_SELLERS_FILE, true);
                			            BufferedWriter bw3 = new BufferedWriter(fw3);
                			            
                			            for(int i = 0; i < lines.size(); i++)
                			            	bw3.write(lines.get(i) + "\n");
                			            
                			            bw3.close();
                			            fw3.close();
                			            
                    					break;
                    					
                case "addSeller":		FileWriter fw4 = new FileWriter(WINE_SELLERS_FILE, true);
						                BufferedWriter bw4 = new BufferedWriter(fw4);
                	
                						bw4.write(cmd.getWine() + ":" + cmd.getWineSeller() + 
                						":" + cmd.getWineQuantity() + ":" + cmd.getWinePrice() + "\n");
                
                    					break;
                    					
                case "updateRating":	Scanner sc5 = new Scanner(this.wineCat);
						                FileWriter fw5 = new FileWriter(WINE_CAT_FILE, true);
						                BufferedWriter bw5 = new BufferedWriter(fw5);
						                
						                List<String> lines2 = new ArrayList<>();
						
										while(sc5.hasNextLine()) {
											String line = sc5.nextLine();
											
											String[] tokens = line.split(":");
											
											if(tokens[0].equals(cmd.getWine())) {
												tokens[2] = "" + (Integer.parseInt(tokens[2]) + 1);
												tokens[3] = "" + (Integer.parseInt(tokens[3]) + cmd.getWineStars());
												
												lines2.add(tokens[0] + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3]);
											} 
											else
												lines2.add(line);
										}
										
										sc5.close();
										bw5.close();
										fw5.close();
										wineCat.delete();
										
										this.wineCat = new File(WINE_CAT_FILE);
										
							            FileWriter fw6 = new FileWriter(WINE_CAT_FILE, true);
							            BufferedWriter bw6 = new BufferedWriter(fw6);
							            
							            for(int i = 0; i < lines2.size(); i++)
							            	bw6.write(lines2.get(i) + "\n");
							            
							            bw6.close();
							            fw6.close();
							            
										break;
                    					
                case "deleteSeller":	Scanner sc7 = new Scanner(this.wineSellers);
						                FileWriter fw7 = new FileWriter(WINE_SELLERS_FILE, true);
						                BufferedWriter bw7 = new BufferedWriter(fw7);
						                
						                List<String> lines3 = new ArrayList<>();
						
										while(sc7.hasNextLine()) {
											String line = sc7.nextLine();
											
											String[] tokens = line.split(":");
											
											if(!tokens[0].equals(cmd.getWine()))
												lines3.add(line);
										}
										
										sc7.close();
										bw7.close();
										fw7.close();
										wineSellers.delete();
										
										this.wineSellers = new File(WINE_SELLERS_FILE);
										
							            FileWriter fw8 = new FileWriter(WINE_SELLERS_FILE, true);
							            BufferedWriter bw8 = new BufferedWriter(fw8);
							            
							            for(int i = 0; i < lines3.size(); i++)
							            	bw8.write(lines3.get(i) + "\n");
							            
							            bw8.close();
							            fw8.close();
							            
										break;
                    					
                default:                break;
            }

        } catch (IOException e){
            System.out.println("File not found");
        }
	}
}
