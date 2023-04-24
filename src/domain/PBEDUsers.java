package src.domain;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PBEDUsers {

    private final String USERS = "users.cif";
    private final int INTERATION_COUNT = 20;
    private final byte[] SALT = {(byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2, (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2};
    private SecretKey key;
    private Cipher encrypt;
    private Cipher decrypt;
    private String password;

    public PBEDUsers(String password, String keystorePath, String keystorePass) {

        try {

            this.password = password;

            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            FileInputStream fis = new FileInputStream(keystorePath);
            keyStore.load(fis, keystorePass.toCharArray());

            try {
                this.key = (SecretKey) keyStore.getKey("pbe", keystorePass.toCharArray());
            } catch (UnrecoverableKeyException e) {
                throw new RuntimeException(e);
            }

            if(this.key == null) {
                PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT, INTERATION_COUNT, 128);
                SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
                this.key = kf.generateSecret(keySpec);

                KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(this.key);
                KeyStore.ProtectionParameter keyPassword = new KeyStore.PasswordProtection(keystorePass.toCharArray());
                keyStore.setEntry("pbe", keyEntry, keyPassword);

                FileOutputStream fos = new FileOutputStream(keystorePath);
                keyStore.store(fos, password.toCharArray());
            }

            this.encrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128/CBC/PKCS5Padding");
            this.encrypt.init(Cipher.ENCRYPT_MODE, key);

            this.decrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128/CBC/PKCS5Padding");
            this.decrypt.init(Cipher.DECRYPT_MODE, this.key, this.encrypt.getParameters());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void encrypt(String newLine) {
        try {
            CipherOutputStream out = new CipherOutputStream(new FileOutputStream("users.cif", true), this.encrypt);

            out.write(newLine.getBytes());

            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<String> decrypt() {

        try {
            CipherInputStream in = new CipherInputStream(new FileInputStream("users.cif"), this.decrypt);

            String fileContent = new String(in.readAllBytes());

            in.close();

            System.out.println(fileContent);

            List<String> result = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            for(char c : fileContent.toCharArray())
                if(c == '\n') {
                    result.add(sb.toString());
                    sb.delete(0, sb.length());
                }
                else
                    sb.append(c);

            return result;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
