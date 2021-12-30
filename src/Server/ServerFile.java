package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerFile implements Runnable {
    private Socket socket = null;

    private String str;
    static ArrayList<Socket> arrayList = new ArrayList<Socket>();
    boolean bool;
    int index;
    ServerSocket server = null;

    @Override
    public void run() {
        try {
            server = new ServerSocket(7462);
            while (true) {
                socket = server.accept();
                arrayList.add(socket);
                FileReadAndWrite a = new FileReadAndWrite(socket);
                a.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


class FileReadAndWrite extends Thread{
        private Socket nowSocket;
        private DataOutputStream output;
        private DataInputStream input;

        public FileReadAndWrite(Socket socket) {
            this.nowSocket = socket;
        }

        public void run(){
            try {
                input = new DataInputStream(nowSocket.getInputStream());//输入流
                while (true){
                    String fileName = input.readUTF();
                    long fileLength = input.readLong();//获得文件名字和文件长度
                    int a = input.readChar();
                    if (a==0){
                        //发送文件名字和文件长度给所有客户端
                        for (Socket socket : ServerFile.arrayList) {
                            output = new DataOutputStream(socket.getOutputStream());
                            if (socket != nowSocket) {    //发送给其他客户端
                                output.writeUTF(fileName);
                                output.flush();
                                output.writeLong(fileLength);
                                output.flush();
                            }
                        }

                        //发送文件内容
                        int length = -1;
                        long curLength = 0;
                        byte[] bytes = new byte[1024];

                        while ((length = input.read(bytes)) > 0) {
                            curLength += length;
                            for (Socket socket : ServerFile.arrayList) {
                                output = new DataOutputStream(socket.getOutputStream());
                                if (socket != nowSocket) {
                                    output.write(bytes, 0, length);
                                    output.flush();
                                }
                            }

                            if (curLength == fileLength) {   //强制退出传文件循环，再开始下一个
                                break;
                            }
                        }
                    }
                    else {
                        Iterator<Socket> it = ServerFile.arrayList.iterator();
                        output = new DataOutputStream(ServerFile.arrayList.get(a-1).getOutputStream());
                        output.writeUTF(fileName);
                        output.flush();
                        output.writeLong(fileLength);
                        output.flush();

                        //发送文件内容
                        int length = -1;
                        long curLength = 0;
                        byte[] bytes = new byte[1024];

                        while ((length = input.read(bytes)) > 0) {
                            curLength += length;
                                output = new DataOutputStream(ServerFile.arrayList.get(a-1).getOutputStream());
                                    output.write(bytes, 0, length);
                                    output.flush();

                            if (curLength == fileLength) {   //强制退出传文件循环，再开始下一个
                                break;
                            }
                        }

                    }

                }

            }catch (Exception e){
                ServerFile.arrayList.remove(nowSocket);
            }
        }

    }

//                    fileOut(fileName, fileLength);
//                }
//            }else {
//                index = a;
//                output = new DataOutputStream(arrayList.get(index).getOutputStream());
//                fileOut(fileName, fileLength);

//    private void fileOut(String fileName, long fileLength) throws IOException {
//
//        output.writeUTF(fileName);
//        output.flush();
//        output.writeLong(fileLength);
//        output.flush();
//
//        int length = -1;
//        long curLength = 0;
//        byte[] bytes = new byte[1024];
//        while ((length=input.read(bytes)) > 0){
//            curLength += length;
//            output.write(bytes,0,length);
//            output.flush();
//
//            if (curLength==fileLength){   //强制退出传文件循环，再开始下一个
//                break;
//            }
//        }
//    }

