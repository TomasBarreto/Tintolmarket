package src.domain;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Scanner;

/**
 * Tintolmarket class allows the interaction between the client and the server of the
 Tintolmarket system.
 */
public class Tintolmarket {

    private static Scanner in = new Scanner(System.in);

    /**
     * Initializes the Tintolmarket system by connecting to the server and authenticating the user.
     * @param serverAdress The address of the server to connect to.
     * @param trustStore The path of the truststore file.
     * @param keyStore The path of the keystore file.
     * @param passwordKeyStore The password for the keystore file.
     * @param userID The user ID to authenticate with.
     * @throws IOException If there's an I/O error while communicating with the server.
     * @throws ClassNotFoundException If the TintolmarketStub class is not found.
     */
    public Tintolmarket(String serverAdress, String trustStore, String keyStore, String passwordKeyStore, String userID) throws IOException, ClassNotFoundException {
        String serverAndPort[] = serverAdress.split(":");
        String ip = serverAndPort[0];
        int port = 12345;
        if (serverAndPort.length == 2) {
            port = Integer.parseInt(serverAndPort[1]);
        }

        String trustStorePath = "storesClient/" + trustStore;
        String keyStorePath = "storesClient/" + keyStore;

        System.setProperty("javax.net.ssl.trustStoreType", "JCEKS");
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", passwordKeyStore);

        System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", passwordKeyStore);

        SocketFactory sf = SSLSocketFactory.getDefault();
        SSLSocket socket = null;

        try {
            socket = (SSLSocket) sf.createSocket(ip, port);
        } catch (ConnectException e) {
            System.out.println("Server Offline");
            return;
        }

        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

        TintolmarketStub clientStub = new TintolmarketStub(socket, outStream, inStream);
        boolean autenticated = clientStub.autenticate(userID, keyStore, passwordKeyStore);
        boolean working = true;

        //verificar se foi autenticado
        if (!autenticated) {
            in.close();
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
                    "list\n"+
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
                        clientStub.sellWine(commandSplit[1], Integer.parseInt(commandSplit[2]), Integer.parseInt(commandSplit[3]), keyStorePath, passwordKeyStore);

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
                        clientStub.buyWine(commandSplit[1], commandSplit[2], Integer.parseInt(commandSplit[3]), userID, keyStorePath, passwordKeyStore);
                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("list") || commandSplit[0].equals("l")) {
                    if (commandSplit.length ==1) {
                        clientStub.getList();
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
                        try{
                            clientStub.sendMessage(commandSplit[1], message, trustStorePath, passwordKeyStore);
                        } catch (NullPointerException e){
                            System.out.println("User does not exists in the system \n");
                        }

                    } else {
                        System.out.println("Wrong command\n");
                    }

                } else if (commandSplit[0].equals("read") || commandSplit[0].equals("r")) {
                    if (commandSplit.length == 1) {
                        clientStub.readMessages(keyStorePath, passwordKeyStore);
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

        new Tintolmarket(args[0], args[1], args[2], args[3], args[4]);

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
