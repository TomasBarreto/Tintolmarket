package src.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Autentication {

    private ArrayList<String> usersConnected = new ArrayList<>();

    private final String USERS = "users";

    public Autentication(){}

    public boolean autenticate(String userID, String passWord) throws IOException {
        File file = new File(USERS);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String userAndPass [] = line.split(":");

            if(userAndPass[0].equals(userID)){
                if(userAndPass[1].equals(passWord)){
                    if(!this.usersConnected.contains(userID)){
                        this.usersConnected.add(userID);
                        scanner.close();
                        return true;
                    }else{
                        scanner.close();
                        return false;
                    }
                }
                scanner.close();
                return false;
            }
        }

        this.usersConnected.add(userID);
        FileWriter fileWriter = new FileWriter(USERS, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("\n" +userID + ":" + passWord);
        bufferedWriter.close();
        fileWriter.close();
        scanner.close();
        return true;
    }

    public void remove(String userID) {
        for (int i = 0; i < this.usersConnected.size(); i++) {
            if (this.usersConnected.get(i).equals(userID)){
                this.usersConnected.remove(i);
            }
        }
    }
}
