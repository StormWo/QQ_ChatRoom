package Server;



import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerName implements Runnable
{

    private Socket socket;
    static ArrayList<Socket> arrayList_name;
    static ArrayList<String> name;
    static DataInputStream dataInS_name;
    static OutputStream oS;

    public ServerName(Socket socket, ArrayList<Socket> arrayList_name, ArrayList<String> nam)
    {
        this.socket = socket;
        ServerName.arrayList_name = arrayList_name;
        ServerName.name =nam;
    }

    @Override
    public void run()
    {
        try
        {
            dataInS_name =new DataInputStream(socket.getInputStream());
            name.add(dataInS_name.readUTF());
            pnEone();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void pnEone() throws Exception
    {
        Iterator<Socket> it= arrayList_name.iterator();
        while(it.hasNext())
        {
            oS = (((Socket)it.next()).getOutputStream());
            ObjectOutputStream obj=new ObjectOutputStream(oS);
            obj.writeObject(name);
            obj.flush();
            oS.flush();
        }
    }

}
