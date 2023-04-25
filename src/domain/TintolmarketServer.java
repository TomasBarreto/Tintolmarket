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
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

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
        TintolmarketServer server = null;

            if (args.length == 4) {
                if(verifyBlockChain("keystoreServer/" + args[2], args[3])) {
                    int port = Integer.parseInt(args[0]);
                    server = new TintolmarketServer(port, args[1], args[2], args[3]);
                }
                else
                    System.out.println("Blockchain is corrupted");
            } else {
                if(verifyBlockChain("keystoreServer/" + args[1], args[2]))
                    server = new TintolmarketServer(12345, args[0], args[1], args[2]);
                else
                    System.out.println("Blockchain is corrupted");
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

                    if(!new File(cert).exists())
                        try {
                            new File(cert).createNewFile();
                            FileOutputStream fos = new FileOutputStream(cert);
                            fos.write(certificate.getEncoded());
                            fos.close();

                            String toWrite = userID + ":" + "certs/" + userID + ".cer:200";
                            this.pbedUsers.encrypt(toWrite);
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
                        } else if (cmd.getCommand().equals("list")) {
                            outStream.writeObject(serverSkel.getAllTransactions(this.keyStorePath, this.keyStorePass));
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

        this.serverSkel = new TintolmarketServerSkel(this.pbedUsers);

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
                FileWriter fw = new FileWriter(currentBlk);
                fw.write("1");
                fw.close();
            } catch (Exception e) {
                System.out.println("File was not created");
            }

        if(!new File(logs).exists())
            try {
                new File(logs).mkdir();
                new File(logs + "/block_1.blk").createNewFile();

                Block newBlock = new Block("00000000".getBytes(), 1, 0);

                FileOutputStream fos2 = new FileOutputStream(logs + "/block_1.blk");
                ObjectOutputStream out3 = new ObjectOutputStream(fos2);
                out3.writeObject(newBlock);

            } catch (Exception e) {
                System.out.println("Directory was not created");
            }
    }

    private static boolean verifyBlockChain(String keyStorePath, String keyStorePass) {

        int lastBlock = 0;
        String currBlkFile = "currBlk";

        Scanner sc = null;
        try {
            sc = new Scanner(new File(currBlkFile));

            lastBlock = sc.nextInt();

            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for(int i = 1; i < lastBlock; i++)
            if (!verifyBlock(i, lastBlock, keyStorePath, keyStorePass))
                return false;

        return true;
    }

    private static boolean verifyBlock(int blockIndex, int lastBlock, String keyStorePath, String keyStorePass) {

        String alias = "server";

        try {
            SignedObject signedObject1 = null;
            SignedObject signedObject2 = null;
            byte[] hash1 = new byte[0];
            byte[] hash2 = new byte[0];

            FileInputStream fis = new FileInputStream("logs/block_" + blockIndex + ".blk");
            ObjectInputStream in = new ObjectInputStream(fis);

            FileInputStream fis2 = new FileInputStream("logs/block_" + (blockIndex + 1) + ".blk");
            ObjectInputStream in2 = new ObjectInputStream(fis2);

            if (blockIndex + 1 != lastBlock) {
                signedObject1 = (SignedObject) in.readObject();

                signedObject2 = (SignedObject) in2.readObject();

                hash1 = ((Block) signedObject1.getObject()).getHash();
                hash2 = ((Block) signedObject2.getObject()).getHash();
            }
            else {
                signedObject1 = (SignedObject) in.readObject();

                Block block2 = (Block) in2.readObject();

                hash1 = ((Block) signedObject1.getObject()).getHash();
                hash2 = block2.getHash();
            }

            fis.close();
            fis2.close();
            in.close();
            in2.close();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(signedObject1);
            byte[] blockBytes = bos.toByteArray();


            MessageDigest hash1ToCompare = MessageDigest.getInstance("SHA-256");
            hash1ToCompare.update(blockBytes);

            if(blockIndex == 1)  {
                if(!Arrays.equals(hash1, "00000000".getBytes()))
                    return false;
            }

            if(!Arrays.equals(hash1ToCompare.digest(), hash2))
                return false;

            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            FileInputStream fis3 = new FileInputStream(keyStorePath);

            keyStore.load(fis3, keyStorePass.toCharArray());

            Certificate certificate = keyStore.getCertificate(alias);

            PublicKey pk = certificate.getPublicKey();

            Signature signature = Signature.getInstance("MD5withRSA");

            signature.initVerify(pk);

            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            ObjectOutputStream out2 = new ObjectOutputStream(bos2);
            out2.writeObject(signedObject1.getObject());
            byte[] blockBytesNoSig = bos2.toByteArray();

            signature.update(blockBytesNoSig);

            byte[] signatureCurrBlock = signedObject1.getSignature();

            return signature.verify(signatureCurrBlock);

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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
