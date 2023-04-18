package src.domain;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

/**
 * The TintolmarketServer class represents a server for a wine market.
 * The server receives requests from clients and performs operations on wine products.
 */
public class TintolmarketServer {

    private final TintolmarketServerSkel serverSkel = new TintolmarketServerSkel();
    private final Autentication autenticator;

    private SecretKey usersFileKey;

    /**
     * Creates the server object and sets up the port for the server to listen on.
     * If no port is given, the server will use the default port 12345.
     * @param args command line arguments. The first argument is the port number.
     */
    public static void main(String[] args) {
        
        createDirectories();
        
        if (args.length == 4) {
            int port = Integer.parseInt(args[0]);
            TintolmarketServer server = new TintolmarketServer(port, args[1], args[2], args[3]);
        } else {
            TintolmarketServer server = new TintolmarketServer(12345, args[0], args[1], args[2]);
        }
    }

    /**
     * The ServerThread class represents a thread running on the server to handle a client request.
     */
    class SSLSimpleServer extends Thread {
        private Socket socket;
        private ArrayList<SSLSimpleServer> threadList;

        /**
         * Creates a new ServerThread object with the given socket and list of threads.
         * @param inSoc the socket to communicate with the client.
         * @param threadList the list of threads.
         */
        SSLSimpleServer(Socket inSoc, ArrayList<SSLSimpleServer> threadList) {
            this.socket = inSoc;
            this.threadList = threadList;
        }


        /**
         * Executes the code to handle the client request.
         */
        public void run() {
            try {
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                //CODIGO A EXECUTAR PELO SERVER


                //fazer a autenticacao e manda ao cliente o valor
                String userID = (String) inStream.readObject();
                String passWord = (String) inStream.readObject();
                boolean autenticated = autenticator.autenticate(userID, passWord);
                outStream.writeObject(autenticated);
                if(autenticated){
                    System.out.println("Client connected");
                    serverSkel.addUser(userID);
                }

                boolean working = true;
                while (working && autenticated) {
                    try{
                        Command cmd = (Command) inStream.readObject();

                        if (cmd.getCommand().equals("add")) {
                            outStream.writeObject(serverSkel.addWine(cmd.getWine(), cmd.getImageName(), cmd.getImageBuffer()));
                        } else if (cmd.getCommand().equals("sell")) {
                            outStream.writeObject(serverSkel.sellWine(cmd.getWine(), cmd.getWinePrice(), cmd.getWineQuantity(), userID));
                        } else if (cmd.getCommand().equals("view")) {
                            outStream.writeObject(serverSkel.viewWine(cmd.getWine()));
                            outStream.writeObject(serverSkel.getImageUrl(cmd.getWine()));
                            outStream.writeObject(serverSkel.getImage(cmd.getWine()));
                        } else if (cmd.getCommand().equals("buy")) {
                            outStream.writeObject(serverSkel.buyWine(cmd.getWine(), cmd.getWineSeller(), cmd.getWineQuantity(), userID));
                        } else if (cmd.getCommand().equals("wallet")) {
                            outStream.writeObject(serverSkel.viewWallet(userID));
                        } else if (cmd.getCommand().equals("classify")) {
                            outStream.writeObject(serverSkel.classifyWine(cmd.getWine(), cmd.getWineStars()));
                        } else if (cmd.getCommand().equals("talk")) {
                            outStream.writeObject(serverSkel.sendMessage(cmd.getUserReceiver(), userID, cmd.getMessage()));
                        } else if (cmd.getCommand().equals("read")) {
                            outStream.writeObject(serverSkel.readMessages(userID));
                        } else if (cmd.getCommand().equals("stop")) {
                            working = false;
                            autenticator.remove(userID);
                            System.out.println("Client disconnected");
                        }
                    } catch(SocketException e){
                        System.out.println("Client disconnected");
                        working = false;
                        autenticator.remove(userID);
                    } catch (EOFException e) {
                        System.out.println("Client disconnected");
                        working = false;
                        autenticator.remove(userID);
                    }
                }

                outStream.close();
                inStream.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Constructor for TintolmarketServer class.
     * @param port the port number for the server socket to listen on.
     */
    public TintolmarketServer(int port, String passwordCifra ,String keyStore, String passKeystore) {

        getUsersFileKey(passwordCifra);

        this.autenticator = new Autentication(this.usersFileKey);

        ArrayList<SSLSimpleServer> threadList = new ArrayList<>();

        String keyStorePath = "keystoreServer/" + keyStore;

        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", passKeystore);

        ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();

        try(SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(port)){
            while (true){
                SSLSimpleServer serverThread = new SSLSimpleServer(ss.accept(), threadList);
                threadList.add(serverThread);
                serverThread.start();
            }
        }catch (IOException e){
            System.out.print("Error Connecting");
        }
    }

    /**
     * This method creates the necessary directories for the server.
     */
    private static void createDirectories() {

        String users = "users";
        String wine_cat = "wine_cat";
        String wine_sellers = "wine_sellers";
        String messages = "messages";
        String imgsDir = "imgs";

        if(!new File(users).exists()) {
            try {
                new File(users).createNewFile();
            } catch (IOException e) {
                System.out.println("File was not created");
            }
        }

        if(!new File(wine_cat).exists()) {
            try {
                new File(wine_cat).createNewFile();
            } catch (IOException e) {
                System.out.println("File was not created");
            }
        }

        if(!new File(wine_sellers).exists()) {
            try {
                new File(wine_sellers).createNewFile();
            } catch (IOException e) {
                System.out.println("File was not created");
            }
        }

        if(!new File(messages).exists()) {
            try {
                new File(messages).createNewFile();
            } catch (IOException e) {
                System.out.println("File was not created");
            }
        }

        if(!new File(imgsDir).exists())
            try {
                new File(imgsDir).mkdir();
            } catch (Exception e) {
                System.out.println("Directory was not created");
            }
    }

    /* Gets the key for encrypting and decrypting the users file
     *  @param password - password for PBE
     */
    private void getUsersFileKey(String password) {
        try {
            byte[] salt = new byte[8];
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 20);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
            this.usersFileKey = kf.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SecretKeyFactory Algorithm Not Found");
        } catch (InvalidKeySpecException e) {
            System.out.println("Invalid KeySpec");
        }
    }
}
