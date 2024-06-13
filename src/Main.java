import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Player {
    public ArrayList<Integer> numbers;
    public String name;

    public Player(String name) {
        this.name = name;
        numbers = new ArrayList<>();
    }
}

class GameFrame extends JFrame {
    Container c = getContentPane();
    Player[] player = new Player[2];
    int[] answer = new int[4];
    int playerTurn = 0;
    JTextArea A, B;
    JLabel chancesLabel;

    public GameFrame(Player playerA, Player playerB) {
        player[0] = playerA;
        player[1] = playerB;

        setTitle("숫자 야구");
        setSize(1000, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PrintPlayerBox();
        RestartButton();
        GameExitButton();
        PrintInputBox();
        makeAnswer();
        showStartMessage();
    }

    void showStartMessage() {
        JOptionPane.showMessageDialog(this, "숫자 야구 게임에 오신 것을 환영합니다!\n4자리 숫자를 입력하여 스트라이크와 볼의 개수를 맞추세요.\n같은 숫자는 중복되지 않습니다.", "게임 시작", JOptionPane.INFORMATION_MESSAGE);
    }

    void PrintInputBox() {
        c.setLayout(null);

        // 숫자를 입력하세요 텍스트
        JLabel inputText = new JLabel("숫자를 입력하세요");
        inputText.setBounds(0, 0, 1000, 50);
        inputText.setHorizontalAlignment(JLabel.CENTER);
        c.add(inputText);

        // 남은 기회 수 텍스트
        chancesLabel = new JLabel("남은 기회: 10");
        chancesLabel.setBounds(0, 30, 1000, 50);
        chancesLabel.setHorizontalAlignment(JLabel.CENTER);
        c.add(chancesLabel);

        // 숫자 입력칸
        JTextField inputBox = new JTextField("", 4);
        c.add(inputBox);
        inputBox.setPreferredSize(new Dimension(250, 50));
        inputBox.setBounds(400, 100, 200, 30); // 적절한 위치와 크기로 설정
        inputBox.setHorizontalAlignment(JTextField.CENTER);
        inputBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JTextField src = (JTextField) e.getSource();
                // 길이 체크
                if (src.getText().length() >= 4)
                    e.consume();

                // 숫자만 있는지 체크
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });

        // 입력 버튼
        JButton inputButton = new JButton("입력");
        inputButton.setEnabled(false);
        c.add(inputButton);
        inputButton.setBounds(400, 150, 200, 30); //
        inputButton.setHorizontalAlignment(JTextField.CENTER);
        inputButton.addActionListener(new ActionListener() {
            int chances = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                JFrame frame = (JFrame) button.getTopLevelAncestor();

                // 플레이어 숫자 입력
                String inputText = inputBox.getText(); // 입력된 숫자를 문자열로 가져옴

                // 중복 숫자 체크
                if (hasDuplicateDigits(inputText)) {
                    JOptionPane.showMessageDialog(frame, "중복된 숫자가 있습니다. 다시 입력하세요.", "잘못된 입력", JOptionPane.ERROR_MESSAGE);
                    inputBox.setText("");
                    return;
                }

                if (playerTurn == 0) {
                    A.append(inputText + "\n"); // A 플레이어의 TextArea에 추가
                } else {
                    B.append(inputText + "\n"); // B 플레이어의 TextArea에 추가
                }

                char[] inputChars = inputText.toCharArray(); // 문자열을 문자 배열로 변환

                // 플레이어가 입력한 숫자를 배열에 저장
                int[] input = new int[inputChars.length];
                for (int i = 0; i < inputChars.length; i++) {
                    input[i] = Character.getNumericValue(inputChars[i]); // 문자를 정수로 변환하여 배열에 저장
                }

                inputBox.setText("");

                // 스트라이크와 볼 계산
                int[] stbal = {0, 0};
                for (int i = 0; i < input.length; i++) {
                    for (int j = 0; j < answer.length; j++) {
                        if (input[i] == answer[j]) {
                            if (i == j) { // 스트라이크인 경우
                                stbal[1]++;
                            } else { // 볼인 경우
                                stbal[0]++;
                            }
                        }
                    }
                }

                // 결과 출력
                String resultMessage = "스트라이크: " + stbal[1] + " 볼: " + stbal[0];
                if (playerTurn == 0) {
                    A.append(resultMessage + "\n"); // A 플레이어의 TextArea에 추가
                    if (stbal[1] == 4) {
                        A.append("게임에서 승리하셨습니다!");
                        showRestartDialog("플레이어 A가 승리하였습니다!");
                    }
                } else {
                    B.append(resultMessage + "\n"); // B 플레이어의 TextArea에 추가
                    if (stbal[1] == 4) {
                        B.append("게임에서 승리하셨습니다!");
                        showRestartDialog("플레이어 B가 승리하였습니다!");
                    }
                }

                // 남은 기회 수 감소
                chances--;
                chancesLabel.setText("남은 기회: " + chances);
                if (chances == 0) {
                    showRestartDialog("기회가 모두 소진되었습니다. 게임 종료!");
                }

                // 플레이어 차례 변경
                playerTurn = playerTurn + 1 >= 2 ? 0 : playerTurn + 1;
            }
        });

        inputBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateTextField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateTextField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateTextField();
            }

            // 텍스트 필드의 내용을 검증하는 메소드
            private void validateTextField() {
                if (inputBox.getText().length() == 4) {
                    inputButton.setEnabled(true);
                } else {
                    inputButton.setEnabled(false);
                }
            }
        });
    }

    void PrintPlayerBox() {
        c.setLayout(null);

        // 플레이어 A의 이름을 표시
        JLabel playerALabel = new JLabel(player[0].name);
        playerALabel.setBounds(10, 10, 140, 40);
        playerALabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        playerALabel.setForeground(Color.BLUE); // 파란색으로 설정
        c.add(playerALabel);

        // 플레이어 A의 입력 값 출력 위치
        A = new JTextArea();
        A.setBounds(10, 50, 300, 500);
        A.setBackground(Color.white);
        c.add(A, BorderLayout.CENTER);

        // 플레이어 B의 이름을 표시
        JLabel playerBLabel = new JLabel(player[1].name);
        playerBLabel.setBounds(getWidth() - 300, 10, 140, 40);
        playerBLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        playerBLabel.setForeground(Color.RED); // 빨간색으로 설정
        c.add(playerBLabel);

        // 플레이어 B의 입력 값 출력 위치
        B = new JTextArea();
        B.setBounds(700, 50, 290, 500);
        B.setBackground(Color.white);
        c.add(B, BorderLayout.CENTER);
    }

    void RestartButton()    // 다시 시작 버튼
    {
        JButton restart = new JButton("다시 시작");
        restart.setBounds(400, 500, 200, 30); // 위치와 크기 설정 (중앙 아래쪽에 위치하도록 설정)
        restart.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        restart.setForeground(Color.BLUE); // 파란색으로 설정

        c.add(restart);

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 현재 프레임을 닫음
                new NameInputDialog(); // 새로운 이름 입력 다이얼로그를 엶
            }
        });
    }

    void GameExitButton()    // 게임 종료 버튼
    {
        JButton gameExit = new JButton("게임 종료");
        gameExit.setBounds(400, 600, 200, 30); // 위치와 크기 설정 (중앙 아래쪽에 위치하도록 설정)
        gameExit.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        gameExit.setForeground(Color.RED); // 빨간색으로 설정

        c.add(gameExit);

        gameExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 프로그램 종료
            }
        });
    }

    void makeAnswer()    // 랜덤으로 중복되지 않는 4자리 숫자 생성
    {
        Random r = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int num = r.nextInt(10);
            set.add(num);
        }

        int i = 0;
        for (int num : set) {
            answer[i++] = num;
        }
    }

    void showRestartDialog(String message) {
        int option = JOptionPane.showConfirmDialog(this, message + "\n다시 시작하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new NameInputDialog(); // 새로운 이름 입력 다이얼로그를 엶
        } else {
            System.exit(0); // 프로그램 종료
        }
    }

    boolean hasDuplicateDigits(String input) {
        Set<Character> charSet = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!charSet.add(c)) {
                return true;
            }
        }
        return false;
    }
}

class NameInputDialog extends JDialog {
    JTextField playerANameField, playerBNameField;
    JButton startButton;

    public NameInputDialog() {
        setTitle("플레이어 이름 입력");
        setLayout(new GridLayout(3, 2));

        // 플레이어 A 이름 입력
        add(new JLabel("플레이어 1 이름:"));
        playerANameField = new JTextField();
        add(playerANameField);

        // 플레이어 B 이름 입력
        add(new JLabel("플레이어 2 이름:"));
        playerBNameField = new JTextField();
        add(playerBNameField);

        // 시작 버튼
        startButton = new JButton("게임 시작");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerAName = playerANameField.getText().trim();
                String playerBName = playerBNameField.getText().trim();
                if (playerAName.isEmpty() || playerBName.isEmpty()) {
                    JOptionPane.showMessageDialog(NameInputDialog.this, "두 플레이어의 이름을 모두 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                } else {
                    dispose();
                    new GameFrame(new Player(playerAName), new Player(playerBName));
                }
            }
        });
        add(startButton);

        setSize(400, 200);
        setLocationRelativeTo(null); // 화면 중앙에 다이얼로그 표시
        setModal(true);
        setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        new NameInputDialog();
    }
}

