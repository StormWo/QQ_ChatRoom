package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class ClientName implements Runnable {

    DataOutputStream dataOutS_name;
    DefaultListModel model;
    DataInputStream dataInS_name;
    String name, listName;
    ArrayList arrayList_names = new ArrayList();
    ObjectInputStream ois;
    int i = 0;

    public ClientName(DataOutputStream dataOutS_name, DefaultListModel listModel,
                      DataInputStream dataInS_name, String nam)
    {
        this.dataOutS_name = dataOutS_name;
        this.model = listModel;
        this.dataInS_name = dataInS_name;
        this.name = nam;
    }

    public void run()
    {
        try
        {
            dataOutS_name.writeUTF(name);

            while(true)
            {
                ois=new ObjectInputStream(dataInS_name);
                arrayList_names =(ArrayList)ois.readObject();
                if(i>0)
                {
                    model.clear();
                }
                Iterator it= arrayList_names.iterator();
                while(it.hasNext())
                {
                    listName =(String) it.next();
                    i++;
                    model.addElement(listName);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
