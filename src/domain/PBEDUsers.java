package src.domain;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class PBEDUsers {

    private final String USERS = "users.cif";
    private final byte[] SALT = { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2 };
    private final int INTERATION_COUNT = 20;
    private SecretKey key;
    private Cipher encrypt;
    private Cipher decrypt;

    public PBEDUsers(String password, String keystorePath, String keystorePass) {
        try {

            KeyStore keystore = KeyStore.getInstance("JCEKS");
            keystore.load(new FileInputStream(keystorePath), keystorePass.toCharArray());

            try {
                this.key = (SecretKey) keystore.getKey("pbe", keystorePass.toCharArray());
            } catch (UnrecoverableKeyException e) {
                throw new RuntimeException(e);
            }

            if(this.key == null) {
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
                PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), this.SALT, this.INTERATION_COUNT);
                this.key = factory.generateSecret(spec);

                KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(keystorePass.toCharArray());
                KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(this.key);
                keystore.setEntry("pbe", keyEntry, keyPassword);

                keystore.store(new FileOutputStream(keystorePath), keystorePass.toCharArray());
            }

            this.encrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            this.decrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");

            this.encrypt.init(Cipher.ENCRYPT_MODE, key);

            System.out.println(this.encrypt.getParameters().getEncoded().length);

            if(!new File("params").exists()) {
                FileOutputStream fos = new FileOutputStream("params");
                fos.write(this.encrypt.getParameters().getEncoded());
                fos.close();
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(String newLine) {
        try {
            return this.encrypt.doFinal(newLine.getBytes());
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> decrypt() {

        try {
            FileInputStream fis = new FileInputStream("params");

            byte[] params = new byte[91];
            fis.read(params);

            AlgorithmParameters p = AlgorithmParameters.getInstance("PBEWithHmacSHA256AndAES_128");
            p.init(params);

            this.decrypt.init(Cipher.DECRYPT_MODE, key, p);

            byte[] fileBytes = fis.readAllBytes();

            byte[] dec = this.decrypt.doFinal(fileBytes);

            String fileContent = new String(dec);

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

        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
