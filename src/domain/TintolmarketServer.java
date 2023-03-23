package src.domain;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TintolmarketServer {

    private final TintolmarketServerSkel serverSkel = new TintolmarketServerSkel();
    private final Autentication autenticator = new Autentication();

    public static void main(String[] args) {
        
        createDirectories();
        
        if (args.length > 0) {
            int port = Integer.parseInt(args[0]);
            TintolmarketServer server = new TintolmarketServer(port);
        } else {
            TintolmarketServer server = new TintolmarketServer(12345);
        }
    }

    class ServerThread extends Thread {
        private Socket socket;
        private ArrayList<ServerThread> threadList;

        ServerThread(Socket inSoc, ArrayList<ServerThread> threadList) {
            this.socket = inSoc;
            this.threadList = threadList;
        }

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

    public TintolmarketServer(int port) {
        ArrayList<ServerThread> threadList = new ArrayList<>();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();
            }
        }catch (IOException e){
            System.out.print("Error Connecting");
        }
    }

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
}
