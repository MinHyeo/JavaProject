import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class Player{
    public ArrayList<Integer> numbers;

    public Player(){
        numbers = new ArrayList<Integer>();
    }
}

class GameFrame extends JFrame{
    Player[] player = new Player[2];
    int playerTurn = 0;

    public GameFrame(Player playerA, Player playerB){
        player[0] = playerA;
        player[1] = playerB;

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
                //길이 체크
                if(src.getText().length() >= 4)
                    e.consume();

                //숫자만 있는지 체크
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
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

                //플레이어 턴 확인 및
                int number = Integer.valueOf(inputBox.getText());
                player[playerTurn].numbers.add(number);
                playerTurn = playerTurn + 1 >= 2 ? 0 : playerTurn + 1;
                System.out.println(player[0].numbers);
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
        GameFrame frame = new GameFrame(playerA, playerB);

    }
}