package Server;


import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 *@author: wyl
 *@date: 2021/10/22 下午8:23
 *@description: 服务端
 */
public class ServerStart {

    private ServerSocket ss,dd,ff;
    private ArrayList<Socket> arrayList_name =new ArrayList<Socket>();
    private ArrayList<Socket> arrayList_message =new ArrayList<Socket>();
    private ArrayList<Socket> arrayList_file =new ArrayList<Socket>();

    private ArrayList<String> names =new ArrayList<String>();

    public ServerStart() throws Exception
    {
        ss=new ServerSocket(7456);
        dd=new ServerSocket(7459);
//        ff=new ServerSocket(7462);

        while(true)
        {
            Socket socket_message = ss.accept();      //收到的信息
            Socket socket_name = dd.accept();         //
//            Socket socket_file = ff.accept();   //收到的文件

            arrayList_message.add(socket_message);
            arrayList_name.add(socket_name);
//            arrayList_file.add(socket_file);

            ServerMessage sm=new ServerMessage(socket_message, arrayList_message);
            Thread tm=new Thread(sm);
            tm.start();

            ServerName sn=new ServerName(socket_name, arrayList_name, names);
            Thread tn=new Thread(sn);
            tn.start();


            ServerFile sf= new ServerFile();
                    //导入arrayList_message是为了对列表排列数，私发文件
            Thread tf = new Thread(sf);
            tf.start();

        }
    }
    public static void main(String[] args) throws Exception
    {
        new ServerStart();
    }
}

