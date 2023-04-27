package src.domain;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Class responsible for encrypting and decrypting users data using PBE encryption.
 */
public class PBEDUsers {

    private final String USERS = "users.cif";
    private final int INTERATION_COUNT = 20;
    private final byte[] SALT = {(byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2};
    private SecretKey key;

    private final byte[] IV = {(byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2, (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2};


    /**
     * Constructor for PBEDUsers. Initializes the secret key from keystore if it exists, or generates a new one if it doesn't.
     * Also initializes the PBEParameterSpec, FileWriter and BufferedWriter.
     *
     * @param password     The password for the secret key.
     * @param keystorePath The path of the keystore file.
     * @param keystorePass The password for the keystore file.
     */
    public PBEDUsers(String password, String keystorePath, String keystorePass) {

        try {

            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            FileInputStream fis = new FileInputStream(keystorePath);
            keyStore.load(fis, keystorePass.toCharArray());

            try {
                this.key = (SecretKey) keyStore.getKey("pbe", keystorePass.toCharArray());
            } catch (UnrecoverableKeyException e) {
                throw new RuntimeException(e);
            }

            if (this.key == null) {
                PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT, INTERATION_COUNT, 128);
                SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
                this.key = kf.generateSecret(keySpec);

                KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(this.key);
                KeyStore.ProtectionParameter keyPassword = new KeyStore.PasswordProtection(keystorePass.toCharArray());
                keyStore.setEntry("pbe", keyEntry, keyPassword);

                FileOutputStream fos = new FileOutputStream(keystorePath);
                keyStore.store(fos, password.toCharArray());
            }
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypts the given string and writes it to the "users.cif" file.
     *
     * @param newLine The string to be encrypted and written to the file.
     * @throws RuntimeException if there is an error during the encryption or file writing process.
     */
    public void encrypt(String newLine) {

        try {
            List<String> allLines = decrypt();

            Cipher encrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            encrypt.init(Cipher.ENCRYPT_MODE, this.key);

            CipherOutputStream out = new CipherOutputStream(new FileOutputStream("users.cif"), encrypt);

            String fileContent = "";

            for(String line : allLines)
                fileContent += line + '\n';

            fileContent += newLine + '\n';

            out.write(fileContent.getBytes());

            out.close();

            if (!new File("params").exists()) {
                new File("params").createNewFile();
                FileOutputStream fos = new FileOutputStream("params");
                fos.write(encrypt.getParameters().getEncoded());
                fos.close();
            }
            else {
                FileOutputStream fos = new FileOutputStream("params");
                fos.write(encrypt.getParameters().getEncoded());
                fos.close();
            }

            new HMacHandler().updateHMac("users.cif");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Decrypts the strings in the "users.cif" file and returns them as a list of strings.
     *
     * @return A list of decrypted strings read from the "users.cif" file.
     * @throws RuntimeException if there is an error during the decryption or file reading process.
     */
    public List<String> decrypt() {

        try {
            List<String> result = new ArrayList<>();

            if (new File("params").exists()) {
                FileInputStream fis = new FileInputStream("params");
                byte[] params = fis.readAllBytes();
                fis.close();

                AlgorithmParameters p = AlgorithmParameters.getInstance("PBEWithHmacSHA256AndAES_128");
                p.init(params);
                Cipher decrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
                decrypt.init(Cipher.DECRYPT_MODE, this.key, p);

                CipherInputStream in = new CipherInputStream(new FileInputStream("users.cif"), decrypt);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] b = new byte[16];
                int numberOfBytedRead;
                while ((numberOfBytedRead = in.read(b)) >= 0) {
                    baos.write(b, 0, numberOfBytedRead);
                }

                String fileContent = new String(baos.toByteArray());

                in.close();

                System.out.println(fileContent);

                StringBuilder sb = new StringBuilder();

                for(char c : fileContent.toCharArray())
                    if(c == '\n') {
                        result.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                    else {
                        sb.append(c);
                    }

            }

            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}