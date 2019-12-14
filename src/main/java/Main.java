import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class Main extends JFrame{
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
}

class ClockPanel extends JPanel implements ActionListener {
    private JLabel timeField;
    private JButton scanButton;
    private JLabel label;
    private Timer t;
    LoginFrame login;
    public ClockPanel() {
        this.setBackground(Color.white);
        scanButton = new JButton("로그인");
        timeField = new JLabel("오전 00:00:00");
        label = new JLabel("출입 가능 시간입니다. ");
        scanButton.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 24));
        label.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 24));
        scanButton.setBounds(250, 580, 340, 50);
        timeField.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 48));
        timeField.setBounds(20, 380, 340, 50);
        scanButton.setBounds(570, 380, 200, 50);
        label.setBounds(20,20,400,30);
        add(scanButton);
        add(timeField);
        add(label);
        setHourLimit();
        t = new Timer(1000, new ClockListener());
        scanButton.addActionListener(this);
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(login == null){
            login = new LoginFrame();
        }else{
            login.dispose();
            login = new LoginFrame();
        }
    }

    private void setHourLimit() {
        Calendar time = Calendar.getInstance();
        System.out.println(time.get(Calendar.HOUR_OF_DAY));
        if(time.get(Calendar.HOUR_OF_DAY) >= 22 || time.get(Calendar.HOUR_OF_DAY) <= 8) {
            label.setText("출입 가능 시간이 아닙니다.");
            //scanButton.setEnabled(false);
        } else {
            label.setText("카드를 대어 출입하세요.");
            scanButton.setEnabled(true);
        }

    }

    private class ClockListener implements ActionListener {
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
            timeField.setText(""+(ampm == 0 ? "오전 " : "오후 ")+(hour < 10 ? "0"+hour : hour) + ":"
                    +(min < 10 ? "0"+min:min) + ":" + (sec<10?"0"+sec:sec));
        }
    }
}

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
        JTextField id = new JTextField();
        JPasswordField pw = new JPasswordField();
        ActionListener loginQuery = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("아이디: "+id.getText()+", 비밀번호: "+pw.getText());
            }
        };
        id.setBounds(20, 40, 200, 30);
        id_label.setBounds(20, 20, 200, 30);
        pw.setBounds(20, 80, 200, 30);
        pw_label.setBounds(20, 60, 200, 30);
        login.setBounds(230, 40, 70, 70);
        close.setBounds(320, 150, 60, 20);
        add(id);
        add(pw);
        add(login);
        add(close);
        add(id_label);
        add(pw_label);
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