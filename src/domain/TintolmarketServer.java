package src.domain;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TintolmarketServer {

    public static void main(String[] args) {
        if(args.length > 0){
            int port = Integer.parseInt(args[0]);
            TintolmarketServer server = new TintolmarketServer(port);
        }else{
            TintolmarketServer server = new TintolmarketServer(12345);
        }
    }

    class ServerThread extends Thread {
        private Socket socket = null;

        ServerThread(Socket inSoc) {
            socket = inSoc;
        }

        public void run(){
            try {
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                Autentication autenticator = new Autentication();
                TintolmarketServerSkel serverSkel = new TintolmarketServerSkel();

                //CODIGO A EXECUTAR PELO SERVER
                System.out.println("Connected");

                //fazer a autenticacao e manda ao cliente o valor
                String userID = (String) inStream.readObject();
                String passWord = (String) inStream.readObject();
                outStream.writeObject(autenticator.autenticate(userID, passWord));

                boolean working = true;
                while (working){
                    Command cmd = (Command) inStream.readObject();
                    if (cmd.getCommand().equals("add")){
                        outStream.writeObject(serverSkel.addWine(cmd.getWine(), cmd.getImage()));
                    } else if (cmd.getCommand().equals("sell")) {
                        outStream.writeObject(serverSkel.sellWine(cmd.getWine(), cmd.getWinePrice(),cmd.getWineQuantity()));
                    } else if (cmd.getCommand().equals("view")) {
                        outStream.writeObject(serverSkel.viewWine(cmd.getWine()));
                    } else if (cmd.getCommand().equals("buy")) {
                        outStream.writeObject(serverSkel.buyWine(cmd.getWine(), cmd.getWineSeller(),cmd.getWineQuantity()));
                    } else if (cmd.getCommand().equals("wallet")) {
                        outStream.writeObject(serverSkel.viewWallet());
                    } else if (cmd.getCommand().equals("classify")) {
                        outStream.writeObject(serverSkel.classifyWine(cmd.getWine(), cmd.getWineStars()));
                    } else if (cmd.getCommand().equals("talk")) {
                        outStream.writeObject(serverSkel.sendMessage(cmd.getUserReceiver(),cmd.getMessage()));
                    } else if (cmd.getCommand().equals("read")) {
                        outStream.writeObject(serverSkel.readMessages());
                    } else if (cmd.getCommand().equals("stop")) {
                        working = false;
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
        ServerSocket sSoc = null;

        try {
            sSoc = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        while (true) {
            try {
                Socket inSoc = sSoc.accept();
                ServerThread newServerThread = new ServerThread(inSoc);
                newServerThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
