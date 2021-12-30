package Client;

import java.io.*;
import java.net.Socket;


public class ClientFile extends Thread {
    static DataInputStream fileReader;
    static DataOutputStream fileOut = null;
    static DataInputStream fileIn = null;
    static DataOutputStream dataOutS = null;
    static String ip;
    static String username;

    DataOutputStream fileWriter;
    private Socket socket = null;

    public ClientFile(String userIp, String name, DataOutputStream dataOutS_message) {

        ClientFile.username=name;
        ClientFile.dataOutS = dataOutS_message;
        ClientFile.ip = userIp;
    }

    @Override
    public void run() {
        //客户端接收文件线程
            try {
                socket=new Socket(ip,7462);
                fileIn = new DataInputStream(socket.getInputStream());
                fileOut = new DataOutputStream(socket.getOutputStream());
                //接收文件
                while (true) {
                    String fileName = fileIn.readUTF();
                    long fileLength = fileIn.readLong();

                    int length = -1;
                    byte[] bytes= new byte[1024];
                    long curLength = 0;

                    File userFile=new File("F:\\聊天室接收文件\\"+username);
                    if (!userFile.exists()){
                        userFile.mkdirs();   //新建当前用户文件夹
                    }
                    File file = new File("F:\\聊天室接收文件\\"+username+"\\"+fileName);
                    file.createNewFile();
                    fileWriter= new DataOutputStream(new FileOutputStream(file));
                    while ((length = fileIn.read(bytes)) > 0){
                        //文件写入本地
                        fileWriter.write(bytes,0,length);
                        fileWriter.flush();
                        curLength +=length;

                        if (curLength == fileLength){
                            break;
                        }
                    }
                    dataOutS.writeUTF(username+":> 接收了文件"+fileName);
                    dataOutS.flush();
                    fileWriter.close();
                }

            } catch (IOException e) {
                    e.printStackTrace();
                }

    }
    static void fileUpLoad(String path, int a) {
//通过路径上传文件

        try {
            File file = new File(path);
            fileReader = new DataInputStream(new FileInputStream(file));
            fileOut.writeUTF(file.getName());   //文件名字
            fileOut.flush();
            fileOut.writeLong(file.length());   //文件长度
            fileOut.flush();
            fileOut.writeChar(a);
            fileOut.flush();
//            fileOut.writeChar(number);
//            fileOut.flush();
            int length = -1;
            byte[] bytes = new byte[1024];
            while ((length = fileReader.read(bytes)) > 0 ){
                fileOut.write(bytes,0,length);
                fileOut.flush();
            }
            dataOutS.writeUTF(username +":> 发送文件"+file.getName());
            dataOutS.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
