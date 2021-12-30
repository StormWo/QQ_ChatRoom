package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerMessage implements Runnable {
    private Socket socket;
    static ArrayList<Socket> arrayList;
    private DataInputStream dis_message;
    private DataOutputStream dos_message;

    int index = 0;
    boolean bool;


    public ServerMessage(Socket socket_message, ArrayList<Socket> arrayList_message) {
        this.socket = socket_message;
        ServerMessage.arrayList = arrayList_message;
        bool = true;
    }

    public void run() {
        String str = "";
        try {
            dis_message = new DataInputStream(socket.getInputStream());
            while (true) {
                str = dis_message.readUTF();
                if (str.equals("q")){
                    bool=true;
                    index = 0;
                    str = dis_message.readUTF();
                }else {
                    if (str.length() == 1){
                        bool = false;
                        index = Integer.valueOf(str);    //由数字来选取列表第几个socket
                        str = dis_message.readUTF();
                    }
                    else {
                        bool = true;
                    }
                }
              /*  if (str.length() == 1) {        //客户端双击名字时会发出单数字，根据这个数字绝对和谁私聊
                    index = Integer.valueOf(str);
                    bool = false;
                    str = dis_message.readUTF();
                    if (str.equals("q")){
                        index = -1;
                        bool = true;
                    }
                }
               */
                pmEone(str);
            }
        } catch (Exception e) {
            ServerMessage.arrayList.remove(socket);
        }
    }

    public void pmEone(String str) throws Exception {
//        if (!bool){
//            dos_message = new DataOutputStream(arrayList.get(index).getOutputStream());
//            dos_message.writeUTF(str + "\n");
//            dos_message = new DataOutputStream(socket.getOutputStream());
//            dos_message.writeUTF(str + "\n");
//            dos_message.flush();
//        }else {
//            Iterator<Socket> it = arrayList.iterator();
//            while (it.hasNext()) {
//                dos_message = new DataOutputStream(((Socket) it.next()).getOutputStream());
//                dos_message.writeUTF(str + "\n");
//                dos_message.flush();
//            }
//
//        }

        if (bool) {
            Iterator<Socket> it = arrayList.iterator();
            while (it.hasNext()) {
                dos_message = new DataOutputStream(((Socket) it.next()).getOutputStream());
                dos_message.writeUTF(str + "\n");
                dos_message.flush();
            }
        } else {
            dos_message = new DataOutputStream(arrayList.get(index).getOutputStream());
            dos_message.writeUTF(str + "\n");   //给私聊对象发
            dos_message = new DataOutputStream(socket.getOutputStream());
            dos_message.writeUTF(str + "\n");   //给发言者自己发
            dos_message.flush();
        }

    }
}
