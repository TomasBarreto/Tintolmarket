package src.domain;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HMacHandler {

    private String pass = "hmacpassword";
    private String hmacFile = "hmac.hmac";
    private Map<String, Integer> indexes;

    private Mac mac;

    public HMacHandler() {
        this.indexes = new HashMap<>();
        this.indexes.put("users.cif", 1);
        this.indexes.put("wine_cat", 2);
        this.indexes.put("wine_sellers", 3);
        this.indexes.put("messages", 4);
        this.indexes.put("currBlk", 5);

        try {
            Key key = new SecretKeySpec(pass.getBytes(), "HmacSHA256");
            this.mac = Mac.getInstance("HmacSHA256");
            this.mac.init(key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateHMac(String filename) {
        try {
            int index = indexes.get(filename);

            List<String> hmacs = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader(hmacFile));

            for(int i = 0; i < this.indexes.size(); i++) {
                if(i == index - 1) {
                    br.readLine();
                    hmacs.add(Base64.getEncoder().encodeToString(this.mac.doFinal(new FileInputStream(filename).readAllBytes())));
                }
                else
                    hmacs.add(br.readLine());
            }

            BufferedWriter bw = bw = new BufferedWriter(new FileWriter(hmacFile, false));

            for(int i = 0; i < this.indexes.size(); i++) {
                bw.write(hmacs.get(i) + "\n");
            }

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
