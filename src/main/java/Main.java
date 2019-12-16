import utils.HttpConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Map;

public class Main extends JFrame{
    private static String token = "";
    public static void main (String[] args) {
        JFrame frm = new JFrame("In And Out");
        JPanel clock = new ClockPanel();
        clock.setLayout(null);
        frm.getContentPane().add(clock);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setSize(800,480);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }

    public static void setToken (String newToken) {
        token = newToken;
    }

    public static String getToken () {
        return token;
    }
}

class ClockPanel extends JPanel implements ActionListener {
    private JLabel timeField;
    private JButton scanButton;
    private JButton checkLoginButton;
    private JLabel label;
    private JLabel userInfo;
    private JButton logOutButton;
    private Timer t;
    LoginFrame login;
    InOutFrame inout;
    HttpConnection http = new HttpConnection();
    Calendar time = Calendar.getInstance();

    public ClockPanel() {
        this.setBackground(new Color(250, 250, 250));
        scanButton = new JButton("로그인");
        checkLoginButton = new JButton("새로 고침");
        timeField = new JLabel("오전 00:00:00");
        logOutButton = new JButton("로그아웃");
        label = new JLabel("출입 가능 시간입니다. ");
        userInfo = new JLabel("");
        scanButton.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 24));
        label.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 24));
        scanButton.setBounds(250, 580, 340, 50);
        timeField.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 48));
        timeField.setBounds(20, 380, 340, 50);
        checkLoginButton.setBounds(570, 350, 200, 30);
        scanButton.setBounds(570, 380, 200, 50);
        label.setBounds(20, 20, 400, 30);
        userInfo.setBounds(20, 50, 400, 60);
        userInfo.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 18));
        logOutButton.setBounds(700, 10, 70, 20);
        add(scanButton);
        add(checkLoginButton);
        add(timeField);
        add(label);
        add(userInfo);
        add(logOutButton);
        logOutButton.setVisible(false);
        setHourLimit();
        t = new Timer(1000, new ClockListener());
        scanButton.addActionListener(this);
        ActionListener InOutListener = e -> {
            if (inout == null) {
                try {
                    inout = new InOutFrame();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                inout.dispose();
                try {
                    inout = new InOutFrame();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        ActionListener loginListener = e -> {
            Map<String, String> response = null;
            try {
                response = http.sendGet("http://168.131.30.128:3000/token?token=" + Main.getToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            label.setText(response.get("username") + " (" + response.get("memberid") + ") 님, 환영합니다!");
            scanButton.setText("입실 / 퇴실");
            logOutButton.setVisible(true);
            scanButton.removeActionListener(this);
            scanButton.addActionListener(InOutListener);
            HttpConnection http = new HttpConnection();
            int year = time.get(Calendar.YEAR);
            int month = time.get(Calendar.MONTH) + 1;
            int date = time.get(Calendar.DATE);
            String parameters = ("date=" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date) + "&token=" + Main.getToken());
            response = null;
            try {
                response = http.sendPost("http://168.131.30.128:3000/process/reservation/user", parameters);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                userInfo.setText("오늘 출입정보 없음");
            }
            System.out.println(response.toString());
            userInfo.setText((response.get("isout").contains("1") ? "퇴실함" : "재실 중"));
        };
        ActionListener logOutListener = e -> {
            Main.setToken("");
            setHourLimit();
            scanButton.setText("로그인");
            logOutButton.setVisible(false);
            userInfo.setText("");
            scanButton.removeActionListener(InOutListener);
            scanButton.addActionListener(this);
        };
        checkLoginButton.addActionListener(loginListener);
        logOutButton.addActionListener(logOutListener);
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (login == null) {
            login = new LoginFrame();
        } else {
            login.dispose();
            login = new LoginFrame();
        }
    }

    private void setHourLimit() {

        if (time.get(Calendar.HOUR_OF_DAY) >= 22 || time.get(Calendar.HOUR_OF_DAY) <= 8) {
            label.setText("출입 가능 시간이 아닙니다.");
            //scanButton.setEnabled(false);
        } else {
            label.setText("로그인후 출입하세요.");
            scanButton.setEnabled(true);
        }
    }

    public class ClockListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar time = Calendar.getInstance();
            int ampm = 0;
            int hour = time.get(Calendar.HOUR_OF_DAY);
            int min = time.get(Calendar.MINUTE);
            int sec = time.get(Calendar.SECOND);
            if (hour > 12) {
                hour -= 12;
                ampm = 1;
            } else if (hour == 12) {
                ampm = 1;
            }
            timeField.setText("" + (ampm == 0 ? "오전 " : "오후 ") + (hour < 10 ? "0" + hour : hour) + ":"
                    + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec));
        }
    }
}