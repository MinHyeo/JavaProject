import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Player{

}

class GameFrame extends JFrame{
    public GameFrame(){
        setTitle("숫자 야구");
        setSize(1000, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PrintInputBox();
        PrintPlayerBox();
    }

    void PrintInputBox(){
        Container c = getContentPane();
        c.setLayout(null);

        //숫자를 입력하세요 텍스트
        JLabel inputText = new JLabel("숫자를 입력하세요");
        inputText.setBounds(0, 0, 1000, 50);
        inputText.setHorizontalAlignment(JLabel.CENTER);
        c.add(inputText);

        //숫자 입력칸
        JTextField inputBox = new JTextField("", 4);
        c.add(inputBox);
        inputBox.setPreferredSize(new Dimension(250, 50));
        inputBox.setBounds(400, 50, 200, 30); // 적절한 위치와 크기로 설정
        inputBox.setHorizontalAlignment(JTextField.CENTER);
        inputBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JTextField src = (JTextField)e.getSource();
                if(src.getText().length() >= 4)
                    e.consume();
            }
        });

        //입력 버튼
        JButton inputButton = new JButton("입력");
        c.add(inputButton);
        inputButton.setBounds(400, 100, 200, 30); //
        inputButton.setHorizontalAlignment(JTextField.CENTER);
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                JFrame frame = (JFrame)button.getTopLevelAncestor();
                // 기능 추가
            }
        });
    }
    void PrintPlayerBox(){
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

    }
}

public class Main{
    public static void main(String[] args){
        Player playerA = new Player();
        Player playerB = new Player();
        GameFrame frame = new GameFrame();

    }
}