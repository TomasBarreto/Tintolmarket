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

public class PBEDUsers {

    private final String USERS = "users.cif";
    private final int INTERATION_COUNT = 20;
    private final byte[] SALT = {(byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2};
    private SecretKey key;
    private String password;
    private PBEParameterSpec pbeParameterSpec;

    private final byte[] IV = {(byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2, (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2};

    private FileWriter fileWriter;
    private BufferedWriter writer;

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

            this.pbeParameterSpec = new PBEParameterSpec(SALT, INTERATION_COUNT, new IvParameterSpec(IV));

            this.fileWriter = new FileWriter("users.cif", true);
            this.writer = new BufferedWriter(this.fileWriter);

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

    public void encrypt(String newLine) {

        try {
            Cipher encrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            encrypt.init(Cipher.ENCRYPT_MODE, this.key, this.pbeParameterSpec);

            String result = Base64.getEncoder().encodeToString(encrypt.doFinal(newLine.getBytes()));

            FileWriter fw = new FileWriter("users.cif", true);
            fw.write(result + '\n');
            fw.close();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> decrypt() {

        try {
            List<String> result = new ArrayList<>();

            Cipher decrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            decrypt.init(Cipher.DECRYPT_MODE, this.key, this.pbeParameterSpec);

            BufferedReader br = new BufferedReader(new FileReader("users.cif"));
            String line = "";

            while ((line = br.readLine()) != null) {
                byte [] lineBytes = Base64.getDecoder().decode(line.getBytes(StandardCharsets.UTF_8));
                result.add(new String(decrypt.doFinal(lineBytes), StandardCharsets.UTF_8));
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
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }
}