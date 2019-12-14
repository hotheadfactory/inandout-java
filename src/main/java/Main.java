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

    public class Entry {
        public Entry() {
            System.out.println("엔트리 객체 호출");
        }
    }

}

class ClockPanel extends JPanel {
    private JLabel timeField;
    private JButton scanButton;
    private JLabel label;
    private Timer t;
    public ClockPanel() {
        scanButton = new JButton("카드 인식");
        timeField = new JLabel("오전 00:00:00");
        label = new JLabel("출입 신청 시간입니다. ");
        label.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 24));
        scanButton.setBounds(250, 580, 340, 50);
        timeField.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 48));
        timeField.setBounds(20, 380, 340, 50);
        scanButton.setBounds(570, 380, 200, 50);
        label.setBounds(20,20,400,30);
        add(scanButton);
        add(timeField);

        add(label);
        t = new Timer(1000, new ClockListener());
        t.start();
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