package src.domain;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Tintolmarket class allows the interaction between the client and the server of the
 Tintolmarket system.
 */
public class Tintolmarket {

    private static Scanner in = new Scanner(System.in);

    /**
     * Initializes the Tintolmarket system by connecting to the server and authenticating the user.
     * @param serverAdress the address of the server to connect to
     * @param userID the user ID to authenticate with
     * @param passWord the password to authenticate with
     * @throws IOException if there's an I/O error while communicating with the server
     * @throws ClassNotFoundException if the TintolmarketStub class is not found
     */
    public Tintolmarket(String serverAdress, String userID, String passWord) throws IOException, ClassNotFoundException {
        String serverAndPort[] = serverAdress.split(":");
        String ip = serverAndPort[0];
        int port = 12345;
        if (serverAndPort.length == 2) {
            port = Integer.parseInt(serverAndPort[1]);
        }

        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (ConnectException e) {
            System.out.println("Server Offline");
            return;
        }

        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

        TintolmarketStub clientStub = new TintolmarketStub(socket, outStream, inStream);
        boolean autenticated = clientStub.autenticate(userID, passWord);
        boolean working = true;

        //verificar se foi autenticado
        if (autenticated) {
            System.out.println("Autentication completed\n");
        } else {
            in.close();
            System.out.println("Autentication failed");
            working = false;
            inStream.close();
            outStream.close();
            socket.close();
        }

        while (working) {
            System.out.println("You can call a command by his entire name or by his initial letter");
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

            String command = in.nextLine();
            String commandSplit[] = command.split(" ");

            try {
                if (commandSplit[0].equals("add") || commandSplit[0].equals("a")) {
                    if (commandSplit.length >= 3) {

                        String url = "";
                        for (int i = 2; i < commandSplit.length; i++) {
                            if (i < commandSplit.length - 1) {
                                url = url + commandSplit[i] + " ";
                            } else {
                                url = url + commandSplit[i];
                            }
                        }

                        clientStub.addWine(commandSplit[1], url);
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("sell") || commandSplit[0].equals("s")) {
                    if (commandSplit.length == 4) {
                        clientStub.sellWine(commandSplit[1], Integer.parseInt(commandSplit[2]), Integer.parseInt(commandSplit[3]));

                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("view") || commandSplit[0].equals("v")) {
                    if (commandSplit.length == 2) {
                        clientStub.viewWine(commandSplit[1]);
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("buy") || commandSplit[0].equals("b")) {
                    if (commandSplit.length == 4) {
                        clientStub.buyWine(commandSplit[1], commandSplit[2], Integer.parseInt(commandSplit[3]));
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("wallet") || commandSplit[0].equals("w")) {
                    if (commandSplit.length == 1) {
                        clientStub.viewWallet();
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("classify") || commandSplit[0].equals("c")) {
                    if (commandSplit.length == 3) {
                        clientStub.classifyWine(commandSplit[1], Float.parseFloat(commandSplit[2]));
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("talk") || commandSplit[0].equals("t")) {
                    if (commandSplit.length >= 3) {
                        String message = "";
                        for (int i = 2; i < commandSplit.length; i++) {
                            message = message + commandSplit[i] + " ";
                        }
                        message = message + "\n";
                        clientStub.sendMessage(commandSplit[1], message);
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("read") || commandSplit[0].equals("r")) {
                    if (commandSplit.length == 1) {
                        clientStub.readMessages();
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("stop")) {
                    working = false;
                    System.out.println("Disconnecting");
                    clientStub.stop();
                    inStream.close();
                } else {
                    System.out.println("Wrong command\n");
                }
            } catch (RuntimeException e) {
                System.out.println("Server Offline");
                working = false;
                System.out.println("Disconnecting");
                inStream.close();
            }
        }
        in.close();
    }

    /**
     * The main method of the Tintolmarket program.
     * @param args the arguments passed to the program
     * @throws IOException if an I/O exception occurs
     * @throws ClassNotFoundException if a class is not found
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        createDirectories();

        if (args.length == 2) {
            System.out.println("Insira a sua password");
            String password = in.next();
            in.nextLine();
            new Tintolmarket(args[0], args[1], password);

        } else {
            new Tintolmarket(args[0], args[1], args[2]);
        }
    }
    /**
     * Creates the necessary directories for the program.
     */
    private static void createDirectories() {
        String clientImgPath = "clientimgs";

        if (!new File(clientImgPath).exists())
            try {
                new File(clientImgPath).mkdir();
            } catch (Exception e) {
                System.out.println("Directory was not created");
            }
    }
}
