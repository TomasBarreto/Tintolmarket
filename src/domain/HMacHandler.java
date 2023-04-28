package src.domain;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * The HMacHandler class is responsible for handling HMac keys and updating HMac files for different data files.
 * It provides methods for updating the HMac file with a new HMac for a given file, and for creating and initializing a new HMac instance.
 */
public class HMacHandler {

    private String pass = "hmacpassword";
    private String hmacFile = "hmac.hmac";
    private Map<String, Integer> indexes;

    private Mac mac;

    /**
     * Constructs a new HMacHandler object with default values for the HMac password and file name.
     * Initializes a new HMac instance with the HMacSHA256 algorithm and the secret key derived from the password.
     */
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

    /**
     * Updates the HMac file with a new HMac for a given file.
     * Reads the current HMac file, replaces the HMac for the specified file with the new HMac, and overwrites the HMac file.
     * @param filename the name of the file for which to update the HMac
     * @throws RuntimeException if there is an error while updating the HMac file
     */
    public void updateHMac(String filename) {
        try {
            int index = indexes.get(filename);

            List<String> hmacs = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader(hmacFile));

            for(int i = 0; i < this.indexes.size(); i++) {
                if(i == index - 1) {
                    br.readLine();

                    byte[] bytes = new byte[(int) new File(filename).length()];
                    DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filename));
                    dataInputStream.readFully(bytes);

                    hmacs.add(Base64.getEncoder().encodeToString(this.mac.doFinal(bytes)));
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
