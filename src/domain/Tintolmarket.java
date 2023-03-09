package src.domain;

import java.io.IOException;
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
        boolean working = true;

        //verificar se foi autenticado
        if (autenticated){
            System.out.println("Autentication completed\n");
        }else{
            in.close();
            System.out.println("Autentication failed");
            clientStub.stop();
            working = false;
        }

        while(working){
            System.out.println("U can call a command by his entire name or by his initial letter");
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

            System.out.println("Insert a valid command\n");
            String command = in.nextLine();
            String commandSplit [] = command.split(" ");

            if(commandSplit[0].equals("add") || commandSplit[0].equals("a")) {
                if(commandSplit.length == 3){
                    if(clientStub.addWine(commandSplit[1], commandSplit[2])){
                        System.out.println("Wine added successfully\n");
                    }else{
                        System.out.println("Wine already in system\n");
                    }
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if(commandSplit[0].equals("sell") || commandSplit[0].equals("s")){
                if(commandSplit.length == 4){
                    if(clientStub.sellWine(commandSplit[1], Integer.parseInt(commandSplit[2]), Integer.parseInt(commandSplit[3]))){
                        System.out.println("Wine is now for sale\n");
                    }else{
                        System.out.println("Wine doesnt exist\n");
                    }
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("view") || commandSplit[0].equals("v")) {
                if(commandSplit.length == 2){
                    String answer = clientStub.viewWine(commandSplit[1]);
                    System.out.println(answer);
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("buy") || commandSplit[0].equals("b")) {
                if(commandSplit.length == 4){
                    String answer = clientStub.buyWine(commandSplit[1], commandSplit[2], Integer.parseInt(commandSplit[3]));
                    System.out.println(answer);
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("wallet") || commandSplit[0].equals("w")) {
                if(commandSplit.length == 1){
                    System.out.println("Wallet: " + clientStub.viewWallet() + "\n");
                } else{
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("classify") || commandSplit[0].equals("c")) {
                if(commandSplit.length == 3){
                    if(clientStub.classifyWine(commandSplit[1], Integer.parseInt(commandSplit[2]))){
                        System.out.println("Wine classified successfully\n");
                    } else{
                        System.out.println("Wine doesnt exist\n");
                    }
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("talk") || commandSplit[0].equals("t")) {
                if(commandSplit.length >= 3){
                    String message = "";
                    for (int i = 2; i < commandSplit.length; i++) {
                        message = message + commandSplit[i] + " ";
                    }
                    message = message + "\n";
                    boolean sent = clientStub.sendMessage(commandSplit[1], message);
                    if(sent){
                        System.out.println("Message sent!\n");
                    } else {
                        System.out.println("User doesnt exist\n");
                    }
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("read") || commandSplit[0].equals("r")) {
                if(commandSplit.length == 1){
                    System.out.println(clientStub.readMessages());
                } else {
                    System.out.println("Wrong command\n");
                }

            } else if (commandSplit[0].equals("stop")) {
                working = false;
                System.out.println("Disconnecting");
                clientStub.stop();
                System.out.println("Disconnected");
            } else {
                System.out.println("Wrong command\n");
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
