package src.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserMessagesFileHandler {
    private static final String MESSAGES_FILE = "messages";

    private File messages;

    public UserMessagesFileHandler() {
        this.messages = new File(MESSAGES_FILE);
    }

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

                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }


    }
}
