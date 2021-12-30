package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *@author: wyl
 *@date: 2021/10/22 下午8:23
 *@description: 客户端
 */
public class ClientLogin implements ActionListener {
    JFrame frame;
    JTextField username;
    JTextField IP;
    JButton button;
    JLabel title;
    JLabel lname;
    JLabel lIP;

    public ClientLogin() {
        frame = new JFrame("登录窗口");
        username = new JTextField();
        IP = new JTextField();
        button = new JButton("开始");
        button.addActionListener(this);     //产生聊天窗口

        title = new JLabel("请输入用户名和IP地址");
        title.setFont(new Font("Serif", Font.BOLD, 40));

        lname = new JLabel("用户名:");
        lname.setFont(new Font("Serif", Font.PLAIN, 24));

        lIP = new JLabel("IP地址ַ:");
        lIP.setFont(new Font("Serif", Font.PLAIN, 24));

        JPanel jp = new JPanel();
        jp.add(title);
        jp.add(username);
        jp.add(IP);
        jp.add(lname);
        jp.add(lIP);
        jp.add(button);
        title.setBounds(30, 20, 280, 80);
        lname.setBounds(20, 100, 250, 60);
        lIP.setBounds(20, 200, 250, 60);
        username.setBounds(50, 150, 150, 30);
        IP.setBounds(50, 250, 150, 30);
        button.setBounds(70, 300, 90, 30);
        frame.add(jp);
        jp.setLayout(null);
        frame.setSize(280, 400);
        frame.setLocation(550, 220);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent arg0) {
        String name = "";
        String ip = "";
        try {
            name = username.getText();
            ip = IP.getText();
            if (ip.matches("^\\d+[.]+\\d+[.]+\\d+[.]+\\d$")) {
                frame.dispose();

                new ClienWindow(name, ip);
            } else {
                IP.setText("IP格式错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new ClientLogin();

    }

}
