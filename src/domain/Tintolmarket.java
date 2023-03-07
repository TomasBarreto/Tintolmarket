package src.domain;

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

        TintolmarketStub clientStub = new TintolmarketStub(ip, port);
        boolean autenticated = clientStub.autenticate(userID, passWord);

        //verificar se foi autenticado
        if (autenticated){
            System.out.println("Autentication completed\n");
        }else{
            in.close();
            System.out.println("Autentication failed");
            clientStub.stop();
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
                    clientStub.readMessages();
                }
                System.out.println("Wrong command\n");

            } else if (commandSplit[0].equals("stop")) {
                working = false;
                System.out.println("Disconnecting");
                clientStub.stop();
                System.out.println("Disconnected");
            }

        }

        in.close();
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
