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

                //CODIGO A EXECUTAR PELO SERVER
                System.out.println("Connected");

                //fazer a autenticacao e manda ao cliente o valor
                String userID = (String) inStream.readObject();
                String passWord = (String) inStream.readObject();
                outStream.writeObject(autenticator.autenticate(userID, passWord));






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
