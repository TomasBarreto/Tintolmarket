package src.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The UserMessagesFileHandler class is responsible for handling the messages file that contains all messages sent between Users.
 * It can add or remove messages from the file, depending on the given Command object.
 */
public class UserMessagesFileHandler {
    private static final String MESSAGES_FILE = "messages";

    private File messages;

    /**
     * Constructs a new UserMessagesFileHandler object and sets the messages file to the default file name.
     */
    public UserMessagesFileHandler() {
        this.messages = new File(MESSAGES_FILE);
    }

    /**
     * Modifies the messages file according to the given Command object.
     * If the Command is "addMsg", the method adds the message to the file.
     * If the Command is "removeMsg", the method removes all messages with the given receiver User from the file.
     * @param cmd the Command object indicating the modification to be made
     */
    public synchronized void alterFile(Command cmd) {
        try{
            switch (cmd.getCommand()){
                case "addMsg":
                    String message = "From:" + cmd.getUser() + ":Receiver:" + cmd.getUserReceiver() + ":Message:" + cmd.getMessage();
                    FileWriter fw1 = new FileWriter(MESSAGES_FILE, true);
                    BufferedWriter bw1 = new BufferedWriter(fw1);
                    bw1.write(message);
                    bw1.close();
                    fw1.close();

                    new HMacHandler().updateHMac(MESSAGES_FILE);

                    break;

                case "removeMsg":
                    Scanner sc = new Scanner(this.messages);
                    List<String> lines = new ArrayList<>();
                    while(sc.hasNextLine()){
                        String msg = sc.nextLine();

                        String [] tokens = msg.split(":");
                        if(tokens.length > 4)
                            if(!tokens[3].equals(cmd.getUserReceiver()))
                                lines.add(msg);
                    }

                    FileWriter fw2 = new FileWriter(MESSAGES_FILE, false);

                    for (int i = 0; i < lines.size(); i++) {
                        fw2.write(lines.get(i) + "\n");
                    }
                    fw2.close();
                    sc.close();

                    new HMacHandler().updateHMac(MESSAGES_FILE);

                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }


    }
}
