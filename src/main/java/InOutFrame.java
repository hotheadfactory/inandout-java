import utils.HttpConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

class InOutFrame extends JFrame implements ActionListener {
    HttpConnection http = new HttpConnection();
    private JLabel dateField;
    private Timer t;
    private Calendar time = Calendar.getInstance();
    public InOutFrame() throws Exception {
        super("입실/퇴실");
        setSize(400,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        dateField = new JLabel("");
        JButton in = new JButton("입실하기");
        JButton out = new JButton("퇴실하기");
        JButton close = new JButton("닫기");
        JLabel status = new JLabel("");
        t = new Timer(1000, new InOutFrame.ClockListener());

        ActionListener getInQuery = e -> {

            int year = time.get(Calendar.YEAR);
            int month = time.get(Calendar.MONTH)+1;
            int date = time.get(Calendar.DATE);
            String parameters = ("date=" + year +"-"+ (month < 10 ? "0"+month : month) +"-"+ (date < 10 ? "0"+date : date) + "&token=" + Main.getToken() + "&option=기타");
            Map<String, String> response = null;
            try {
                response = http.sendPost("http://168.131.30.128:3000/process/reservation/day", parameters);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                status.setText("이미 입실하였습니다.");
            }
            try {
                response = http.sendGet("http://168.131.30.128:3000/token?token="+Main.getToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            status.setText(response.get("username")+" 님 입실하였습니다.");
        };

        ActionListener getOutQuery = e -> {
            int year = time.get(Calendar.YEAR);
            int month = time.get(Calendar.MONTH)+1;
            int date = time.get(Calendar.DATE);
            String parameters = ("date=" + year +"-"+ (month < 10 ? "0"+month : month) +"-"+ (date < 10 ? "0"+date : date) + "&token=" + Main.getToken());
            Map<String, String> response = null;
            try {
                response = http.sendPost("http://168.131.30.128:3000/process/reservation/out", parameters);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                status.setText("이미 퇴실하였습니다.");
            }
            status.setText("퇴실하였습니다.");
        };
        dateField.setBounds(20, 20, 200, 20);
        in.setBounds(70, 60, 100, 100);
        out.setBounds(180, 60, 100, 100);
        close.setBounds(320, 150, 60, 20);
        status.setBounds(20, 40, 200, 30);
        add(in);
        add(out);
        add(close);
        add(status);
        add(dateField);
        in.addActionListener(getInQuery);
        out.addActionListener(getOutQuery);
        close.addActionListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
        t.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        dispose();
    }

    public class ClockListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int year = time.get(Calendar.YEAR);
            int month = time.get(Calendar.MONTH)+1;
            int date = time.get(Calendar.DATE);
            int ampm = 0;
            int hour = time.get(Calendar.HOUR_OF_DAY);
            int min = time.get(Calendar.MINUTE);
            dateField.setText(year+"년 "+month+"월 "+date+"일 "+(ampm == 0 ? "오전 " : "오후 ")+(hour < 10 ? "0"+hour : hour) + "시 "
                    +(min < 10 ? "0"+min:min) + "분");
        }
    }
}