package src.domain;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for user authentication in a system.
 * This class performs user authentication based on a previously registered
 users and passwords file. If the user is not registered, it is automatically
 registered.
 * In addition, the class maintains a list of users connected to the system.
 */
public class Autentication {

    private ArrayList<String> usersConnected = new ArrayList<>();

    private final String USERS = "users";

    private SecretKey usersFileKey;
    private PBEDUsers pbedUsers;

    /**
     * Default constructor for the class.
     */
    public Autentication(SecretKey usersFileKey, PBEDUsers pbedUsers){
        this.usersFileKey = usersFileKey;
        this.pbedUsers = pbedUsers;
    }

    /**
     * Performs user authentication in the system.
     * @param userID   The ID of the user to be authenticated.
     * @return true, if the user was successfully authenticated. false, otherwise.
     * @throws IOException If an error occurs while accessing the users file.
     */
    public boolean autenticate(String userID) throws IOException {
        List<String> fileStrings = this.pbedUsers.decrypt();

        if (fileStrings.size() == 0)
            System.out.println("ficheiro vazio");

        for(String line : fileStrings) {

            String userAndPass[] = line.split(":");

            if (userAndPass[0].equals(userID)) {
                if (!this.usersConnected.contains(userID)) {
                    this.usersConnected.add(userID);
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    /**
     * Removes a user from the list of users connected to the system.
     * @param userID The ID of the user to be removed.
     */
    public void remove(String userID) {
        for (int i = 0; i < this.usersConnected.size(); i++) {
            if (this.usersConnected.get(i).equals(userID)){
                this.usersConnected.remove(i);
            }
        }
    }
}
