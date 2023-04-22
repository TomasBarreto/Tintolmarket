package src.domain;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class PBEDUsers {

    private final String USERS = "users";
    private final byte[] SALT = { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2 };
    private final int INTERATION_COUNT = 20;
    private SecretKey key;
    private Cipher encrypt;
    private Cipher decrypt;

    public PBEDUsers(String password) {
        try {
            this.encrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            this.decrypt = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT, INTERATION_COUNT);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
            this.key = kf.generateSecret(keySpec);
            byte[] params = this.encrypt.getParameters().getEncoded();

            AlgorithmParameters p = AlgorithmParameters.getInstance("PBEWithHmacSHA256AndAES_128");

            p.init(params);
            this.decrypt.init(Cipher.DECRYPT_MODE, key, p);

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
        } catch (InvalidAlgorithmParameterException e) {
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
            FileInputStream fis = new FileInputStream(USERS);

            byte[] fileBytes = fis.readAllBytes();

            byte [] dec = this.decrypt.doFinal(fileBytes);

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
        }
    }
}
