import utils.HttpConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

class LoginFrame extends JFrame implements ActionListener {

    public LoginFrame(){
        super("로그인");
        setSize(400,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        JButton login = new JButton("로그인");
        JButton close = new JButton("닫기");
        JLabel id_label = new JLabel("아이디");
        JLabel pw_label = new JLabel("비밀번호");
        JLabel status = new JLabel("");
        JTextField id = new JTextField();
        JPasswordField pw = new JPasswordField();

        ActionListener loginQuery = e -> {
            HttpConnection http = new HttpConnection();
            String parameters = ("id=" + id.getText() + "&password=" + pw.getText() + "&type=member");
            Map<String, String> response = null;
            try {
                response = http.sendPost("http://168.131.30.128:3000/process/login", parameters);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                status.setText("아이디나 비밀번호를 다시 확인해주세요");
            }
            Main.setToken(response.get("data"));
            try {
                response = http.sendGet("http://168.131.30.128:3000/token?token="+Main.getToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            status.setText(response.get("username")+" 님, 환영합니다!");
        };
        id.setBounds(20, 40, 200, 30);
        id_label.setBounds(20, 20, 200, 30);
        pw.setBounds(20, 80, 200, 30);
        pw_label.setBounds(20, 60, 200, 30);
        login.setBounds(230, 40, 70, 70);
        close.setBounds(320, 150, 60, 20);
        status.setBounds(20, 100, 200, 30);
        add(id);
        add(pw);
        add(login);
        add(close);
        add(id_label);
        add(pw_label);
        add(status);
        login.addActionListener(loginQuery);
        close.addActionListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        dispose();
    }
}