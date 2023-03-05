package src;

import java.io.*;
import java.util.Scanner;

public class Autentication {
    public Autentication(){}

    public boolean autenticate(String userID, String passWord) throws IOException {
        File file = new File("https://github.com/TOXIK523/Tintolmarket/blob/bceda67b88dcd05a1977496100e391298722ede8/Users");
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String userAndPass [] = line.split(":");

            if(userAndPass[0].equals(userID)){
                if(userAndPass[1].equals(passWord)){
                    scanner.close();
                    return true;
                }

                scanner.close();
                return false;
            }
        }

        FileWriter fileWriter = new FileWriter("Users", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("\n" +userID + ":" + passWord);
        bufferedWriter.close();
        fileWriter.close();
        scanner.close();
        return true;
    }
}
