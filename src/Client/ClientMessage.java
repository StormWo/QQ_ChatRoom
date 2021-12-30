package Client;

import javax.swing.*;
import java.io.DataInputStream;

public class ClientMessage implements Runnable {

    DataInputStream dataInS;
    JTextArea jTextA;

    public ClientMessage(DataInputStream dataInS_message, JTextArea jTextA) {
        this.dataInS = dataInS_message;
        this.jTextA = jTextA;
    }

    public void run() {
        String str = "";
        while (true) {
            try {
                str = dataInS.readUTF();
                jTextA.append(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
