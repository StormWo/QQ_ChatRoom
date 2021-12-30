package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;

import java.io.*;
import java.net.Socket;
import java.awt.Font;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ClienWindow extends WindowAdapter implements ActionListener
{
    private String name = "";
    private JFrame frame;

    private JTextArea jTextArea_text;   //对话框
    private JTextArea JTextArea_message;    //聊天输入框
//    private JTextArea JTextArea_filePath;  //文件路径
    private JTextArea jText_fileTarget;

    private JButton send;   //发送按钮
    private JButton file;   //上传文件按钮
    private JList list; //群成员列表
    private DefaultListModel listModel;    //接口模型列表框

    private Socket socket_message,  //文字流套接字
            socket_name,    //名字流套接字
            socket_file;    //文件流套接字

    private DataInputStream dataInS_message;
    private DataOutputStream dataOutS_message;
    private DataOutputStream dataOutS_name;
    private DataInputStream dataInS_name;

    DataOutputStream fileOut;
    DataInputStream fileIn;

    private FileInputStream fis;
    private OutputStream os;


    public ClienWindow(String nam, String ip) throws Exception
    {

        this.name = nam;
        frame = new JFrame(nam+":的聊天窗口:");
        frame.setSize(800,620);
        frame.setLocation(200,200);

        JPanel panel= new JPanel();

        JTextArea_message = new JTextArea();
        //发消息前的窗口，文字输入框
        JTextArea_message.setBackground(Color.PINK);
        JTextArea_message.setFont(new Font("Serif",Font.PLAIN,24));
        panel.add(JTextArea_message);
        JTextArea_message.setBounds(10,350,600,150);

        //    JTextArea_filePath = new JTextArea();
        // //获取文件路径
        // JTextArea_filePath.setText("格式：f:\\upload\\pig.mp4");
        // JTextArea_filePath.setBackground(Color.WHITE);
        // JTextArea_filePath.setFont(new Font("Serif",Font.PLAIN,24));
        // panel.add(JTextArea_filePath);
        // JTextArea_filePath.setBounds(10,600,500,50);

        jText_fileTarget = new JTextArea();
        //群聊还是私聊
        jText_fileTarget.setText("0群发，1，2，3~私发文件");
        jText_fileTarget.setBackground(Color.WHITE);
        jText_fileTarget.setFont(new Font("Serif",Font.PLAIN,24));
        panel.add(jText_fileTarget);
        jText_fileTarget.setBounds(10,520,340,50);

        file = new JButton("上传");
        file.addActionListener(this);
        panel.add(file);
        file.setBounds(390,520,99,50);

        jTextArea_text = new JTextArea();
        //聊天记录窗口
        jTextArea_text.setEditable(false);
        jTextArea_text.setBackground(Color.YELLOW);
        jTextArea_text.setFont(new Font("Serif",Font.PLAIN,24));
        panel.add(jTextArea_text);
        JScrollPane scroll= new JScrollPane(jTextArea_text);
        panel.add(scroll);
        scroll.setBounds(10,10,600,300);

        send = new JButton("发送");
        //发送按钮
        send.addActionListener(this);
        panel.add(send);
        send.setBounds(500,520,100,50);


        listModel = new DefaultListModel();
        //聊天成员列表名单
        list = new JList(listModel);
        list.setFont(new Font("Serif",Font.PLAIN,24));
        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane);
        scrollPane.setBounds(620,10,130,550);
        panel.add(scrollPane);

        panel.setLayout(null);
        frame.add(panel);
        frame.setVisible(true);     //显性
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出

        socket_message = new Socket(ip,7456);
        socket_name = new Socket(ip,7459);
//        socket_file = new Socket(ip,7462);

        dataInS_message = new DataInputStream(socket_message.getInputStream()); //输入文字流
        //实例化
        dataOutS_message = new DataOutputStream(socket_message.getOutputStream());  //输出消息流

        dataInS_name = new DataInputStream(socket_name.getInputStream());       //输入名字流
        dataOutS_name = new DataOutputStream(socket_name.getOutputStream());    //输出名字流

//        fileOut = new DataOutputStream(socket_file.getOutputStream());  //输出文件流
//        fileIn = new DataInputStream(socket_file.getInputStream());     //输入文件流

        dataOutS_message.writeUTF(nam +":> 进入聊天室");

        list.addMouseListener(new MouseAdapter()
        {//名字双击私聊,三次点击恢复群聊

            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount() == 2)
                {
                    int index = list.locationToIndex(e.getPoint());
                    try
                    {
                        dataOutS_message.writeUTF(new Integer(index).toString());
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                if (e.getClickCount()==3)
                {
                    try
                    {
                        dataOutS_message.writeUTF("q");
                    } catch (IOException Exception)
                    {
                        Exception.printStackTrace();
                    }

                }
            }
        });
        ClientName un=new ClientName(dataOutS_name, listModel, dataInS_name,nam);
        //客户端名字排列，接收名字
        Thread tn=new Thread(un);
        tn.start();

        ClientMessage cm=new ClientMessage(dataInS_message, jTextArea_text);
        //收消息
        Thread tt=new Thread(cm);
        tt.start();

        ClientFile cf = new ClientFile(ip,name,dataOutS_message);
        Thread tf = new Thread(cf);
        tf.start();
    }

    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == send)
        {//点击发送按钮后
            String str = name+":>"+JTextArea_message.getText();
            JTextArea_message.setText(null);
            try
            {
                dataOutS_message.writeUTF(str); //传到服务器
                dataOutS_message.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        else if(event.getSource() == file){
            //点击上传按钮后
            String str = jText_fileTarget.getText();
            if (str.length()==1){
                int a = Integer.parseInt(jText_fileTarget.getText());
                FileCatch(frame,a);
            }
            jText_fileTarget.setText("格式错误，输入数字");
            /*
            String path = JTextArea_filePath.getText();
            String num = jText_fileTarget.getText();

            JTextArea_filePath.setText("");
            jText_fileTarget.setText("-1代表群聊，从0开始成员");
//            int number =Integer.valueOf(num);
            if (!(new File(path).exists())){
                JTextArea_filePath.setText("格式错误");
            }else {
                fileUpLoad(path,name,1);
            }
*/
        }
    }


    void FileCatch(JFrame frame, int a) {
        //通过窗口上传文件
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹
        fileChooser.setCurrentDirectory(new File("F:"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("(多媒体)","jpg","jpeg","png","mp3","mp4","wav"));
        //显示在聊天框中间；
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            //获取路径
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            ClientFile.fileUpLoad(path,a);
        }

    }




}
