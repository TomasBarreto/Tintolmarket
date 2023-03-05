package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Tintolmarket {
    private static Scanner in = new Scanner(System.in);

    public Tintolmarket(String serverAdress, String userID, String passWord) throws IOException, ClassNotFoundException {
        String serverAndPort[] = serverAdress.split(":");
        String ip = serverAndPort[0];
        int port = 12345;
        if(serverAndPort.length == 2){
            port = Integer.parseInt(serverAndPort[1]);
        }

        //Ligar ao server
        Socket clientSocket = new Socket(ip, port);
        ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());

        //Mandar password e username ao servidor
        outStream.writeObject(userID);
        outStream.writeObject(passWord);

        //verificar se foi autenticado
        if ((boolean) inStream.readObject() == false){
            inStream.close();
            outStream.close();
            System.out.println("Autentication failed");
            System.exit(-1);
        }else{
            System.out.println("Autentication completed\n");
        }

        boolean working = true;
        System.out.println("U can call a command by his entire name or by his initial letter");

        while(working){
            System.out.println("Commands:\n" +
                    "add <wine> <image>\n" +
                    "sell <wine> <value> <quantity>\n" +
                    "view <wine>\n" +
                    "buy <wine> <seller> <quantity>\n" +
                    "wallet\n" +
                    "classify <wine> <stars>\n" +
                    "talk <user> <message>\n" +
                    "read\n" +
                    "stop\n");


            System.out.println("Insert a valid command");
            String command = in.nextLine();
            String commandSplit [] = command.split(" ");

            if(commandSplit[0].equals("add") || commandSplit[0].equals("a")) {
                if(commandSplit.length == 3){

                }
                System.out.println("Wrong command\n");

            } else if(commandSplit[0].equals("sell") || commandSplit[0].equals("s")){
                if(commandSplit.length == 4){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("view") || commandSplit[0].equals("v")) {
                if(commandSplit.length == 2){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("buy") || commandSplit[0].equals("b")) {
                if(commandSplit.length == 4){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("wallet") || commandSplit[0].equals("w")) {
                if(commandSplit.length == 1){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("classify") || commandSplit[0].equals("c")) {
                if(commandSplit.length == 3){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("talk") || commandSplit[0].equals("t")) {
                if(commandSplit.length == 3){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("read") || commandSplit[0].equals("r")) {
                if(commandSplit.length == 1){

                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("stop")) {
                working = false;
                System.out.println("Disconnecting");
            }

        }

        in.close();
        inStream.close();
        outStream.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if(args.length == 2){
            System.out.println("Insira a sua password");
            String password = in.next();
            new Tintolmarket(args[0], args[1], password);

        }else{
            new Tintolmarket(args[0], args[1], args[2]);
        }
    }
}
