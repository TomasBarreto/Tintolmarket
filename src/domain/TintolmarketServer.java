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
import java.nio.ByteBuffer;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The TintolmarketServer class represents a server for a wine market.
 * The server receives requests from clients and performs operations on wine products.
 */
public class TintolmarketServer {

    private final TintolmarketServerSkel serverSkel;
    private final Autentication autenticator;

    private SecretKey usersFileKey;
    private PBEDUsers pbedUsers;

    /**
     * Creates the server object and sets up the port for the server to listen on.
     * If no port is given, the server will use the default port 12345.
     * @param args command line arguments. The first argument is the port number.
     */
    public static void main(String[] args) {
        
        createDirectories();

        if(verifyBlockChain()) {

            if (args.length == 4) {
                int port = Integer.parseInt(args[0]);
                TintolmarketServer server = new TintolmarketServer(port, args[1], args[2], args[3]);
            } else {
                TintolmarketServer server = new TintolmarketServer(12345, args[0], args[1], args[2]);
            }

        }
    }

    /**
     * The ServerThread class represents a thread running on the server to handle a client request.
     */
    class SSLSimpleServer extends Thread {
        private Socket socket;
        private ArrayList<SSLSimpleServer> threadList;
        private PBEDUsers pbedUsers;
        private String keyStorePath;
        private String keyStorePass;

        /**
         * Creates a new ServerThread object with the given socket and list of threads.
         * @param inSoc the socket to communicate with the client.
         * @param threadList the list of threads.
         */
        SSLSimpleServer(Socket inSoc, ArrayList<SSLSimpleServer> threadList, PBEDUsers pbedUsers, String keyStorePath, String keyStorePass) {
            this.socket = inSoc;
            this.threadList = threadList;
            this.pbedUsers = pbedUsers;
            this.keyStorePath = keyStorePath;
            this.keyStorePass = keyStorePass;
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
                Random rd = new Random();

                String userID = (String) inStream.readObject();
                boolean clientExists = autenticator.autenticate(userID);

                outStream.writeObject(clientExists);

                Long nonce = rd.nextLong();

                outStream.writeObject(nonce);

                boolean isTrueClient = false;

                if(clientExists) {
                    byte[] signedNonce = (byte[]) inStream.readObject();

                    Certificate certificate = getUserCerticate("certs/" + userID + ".cer");

                    PublicKey pk = certificate.getPublicKey();

                    Signature signature = Signature.getInstance("MD5withRSA");
                    signature.initVerify(pk);
                    signature.update(nonce.byteValue());

                    isTrueClient = signature.verify(signedNonce);

                    System.out.println(nonce.toString());
                    System.out.println(ByteBuffer.wrap(signedNonce).getLong());

                    if (isTrueClient) {
                        System.out.println("Client connected");
                        serverSkel.addUser(userID);
                    }

                    outStream.writeObject(isTrueClient);
                }
                else {
                    byte[] signedNonce = new byte[0];
                    signedNonce = (byte[]) inStream.readObject();

                    Certificate certificate = (Certificate) inStream.readObject();

                    PublicKey pk = certificate.getPublicKey();

                    Signature signature = Signature.getInstance("MD5withRSA");
                    signature.initVerify(pk);
                    signature.update(nonce.byteValue());

                    isTrueClient = signature.verify(signedNonce);

                    outStream.writeObject(isTrueClient);

                    // ESCREVER CERTIFICADO NA PASTA DO SERVER

                    String cert = "certs/" + userID + ".cer";

                    if(!new File(userID + ".cer").exists())
                        try {
                            new File(cert).createNewFile();
                            FileOutputStream fos = new FileOutputStream(cert);
                            fos.write(certificate.getEncoded());
                            fos.close();

                            FileOutputStream fos2 = new FileOutputStream("users.cif", true);
                            fos2.write(this.pbedUsers.encrypt(userID + ":" + "certs/" + userID + ".cer\n"));
                            fos2.close();
                        } catch (Exception e) {
                            System.out.println("File was not created");
                        }

                    if (isTrueClient) {
                        System.out.println("Client connected");
                        serverSkel.addUser(userID);
                    }

                    clientExists = true;
                }

                boolean working = true;
                while (working && clientExists && isTrueClient) {
                    try{
                        Command cmd = null;

                        int isSignedCommand = (int) inStream.readObject();

                        boolean isValidSignature = false;
                        SignedObject signedObject = null;

                        if(isSignedCommand == 1) {
                            signedObject = (SignedObject) inStream.readObject();

                            Certificate certificate = getUserCerticate("certs/" + userID + ".cer");
                            PublicKey pk = certificate.getPublicKey();

                            Signature s = Signature.getInstance("MD5withRSA");

                            isValidSignature = signedObject.verify(pk, s);
                            cmd = (Command) signedObject.getObject();
                        }
                        else {
                            cmd = (Command) inStream.readObject();
                        }

                        if (cmd.getCommand().equals("add")) {
                            outStream.writeObject(serverSkel.addWine(cmd.getWine(), cmd.getImageName(), cmd.getImageBuffer()));
                        } else if (cmd.getCommand().equals("sell")) {
                            if(isValidSignature)
                                outStream.writeObject(serverSkel.sellWine(cmd.getWine(), cmd.getWinePrice(), cmd.getWineQuantity(), userID, this.keyStorePath, this.keyStorePass));
                            else
                                outStream.writeObject("Invalid Signature");
                        } else if (cmd.getCommand().equals("view")) {
                            outStream.writeObject(serverSkel.viewWine(cmd.getWine()));
                            outStream.writeObject(serverSkel.getImageUrl(cmd.getWine()));
                            outStream.writeObject(serverSkel.getImage(cmd.getWine()));
                        } else if (cmd.getCommand().equals("buy")) {
                            if(isValidSignature)
                                outStream.writeObject(serverSkel.buyWine(cmd.getWine(), cmd.getWineSeller(), cmd.getWineQuantity(), userID, this.keyStorePath, this.keyStorePass));
                            else
                                outStream.writeObject("Invalid Signature");
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
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }

        private Certificate getUserCerticate(String certificatePath) {
            try {
                FileInputStream fis = new FileInputStream(certificatePath);
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate certificate = certificateFactory.generateCertificate(fis);
                fis.close();

                return certificate;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Constructor for TintolmarketServer class.
     * @param port the port number for the server socket to listen on.
     */
    public TintolmarketServer(int port, String passwordCifra ,String keyStore, String keyStorePass) {

        String keyStorePath = "keystoreServer/" + keyStore;

        this.pbedUsers = new PBEDUsers(passwordCifra, keyStorePath, keyStorePass);

        this.serverSkel = new TintolmarketServerSkel();

        this.autenticator = new Autentication(this.usersFileKey, this.pbedUsers);

        ArrayList<SSLSimpleServer> threadList = new ArrayList<>();

        System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePass);

        ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();

        try {
            SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(port);
            while (true){
                SSLSimpleServer serverThread = new SSLSimpleServer(ss.accept(), threadList, this.pbedUsers, keyStorePath, keyStorePass);
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

        String users = "users.cif";
        String wine_cat = "wine_cat";
        String wine_sellers = "wine_sellers";
        String messages = "messages";
        String imgsDir = "imgs";
        String logs = "logs";
        String currentBlk = "currBlk";

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

        if(!new File(currentBlk).exists())
            try {
                new File(currentBlk).createNewFile();
                FileOutputStream fos = new FileOutputStream(currentBlk);
                fos.write(1);
                fos.close();
            } catch (Exception e) {
                System.out.println("File was not created");
            }

        if(!new File(logs).exists())
            try {
                new File(logs).mkdir();
                new File(logs + "/block_1.blk");
                FileWriter fw = new FileWriter(logs + "/block_1.blk");
                fw.write("00000000\n");
                fw.write("1\n");
                fw.write("0\n");

                fw.close();
            } catch (Exception e) {
                System.out.println("Directory was not created");
            }
    }

    private static boolean verifyBlockChain() {

        int lastBlock = 0;
        String currBlkFile = "currBlk";

        try {
            FileInputStream fs = new FileInputStream(currBlkFile);
            lastBlock = fs.read();

            fs.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i = 1; i < lastBlock; i++)
            if (!verifyBlock(i))
                return false;

        return true;
    }

    private static boolean verifyBlock(int blockIndex) {

        String alias = "server";

        try {
            BufferedReader br1 = new BufferedReader(new FileReader("logs/block_" + blockIndex + ".blk"));
            BufferedReader br2 = new BufferedReader(new FileReader("logs/block_" + (blockIndex + 1) + ".blk"));

            String hash1 = br1.readLine();
            String hash2 = br2.readLine();
            br1.close();
            br2.close();

            FileInputStream fis1 = new FileInputStream("logs/block_" + blockIndex + ".blk");

            byte[] fileBytes1 = fis1.readAllBytes();

            MessageDigest messageDigest1 = MessageDigest.getInstance("SHA-256");
            messageDigest1.update(fileBytes1);

            byte[] hash1ToCompare = messageDigest1.digest();

            if(blockIndex == 1)  {
                if(!hash1.equals("00000000"))
                    return false;
            }

            if(!hash2.equals(hash1ToCompare.toString()))
                return false;

            String keyStorePath = System.getProperty("javax.net.ssl.keyStore");
            char[] keyStorePass = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();

            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            FileInputStream fis2 = new FileInputStream(keyStorePath);

            keyStore.load(fis2, keyStorePass);

            Certificate certificate = keyStore.getCertificate(alias);

            PublicKey pk = certificate.getPublicKey();

            Signature signature = Signature.getInstance("MD5withRSA");

            BufferedReader br3 = new BufferedReader(new FileReader("logs/block_" + blockIndex + ".blk"));

            signature.initVerify(pk);

            for(int i = 0; i < 8; i++)
                signature.update(br3.readLine().getBytes());

            if(signature.verify(br3.readLine().getBytes())) {
                br3.close();
                fis2.close();
                fis1.close();

                return true;
            }

            br3.close();
            fis2.close();
            fis1.close();

            return false;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }

    }
}
