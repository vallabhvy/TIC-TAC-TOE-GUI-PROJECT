import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends JPanel {

    private char[][] board = new char[3][3];
    private boolean xTurn = true;
    private Font font;
    private JFrame parentFrame;
    private boolean againstComputer;
    private Random random = new Random(); // Random object for random moves

    public TicTacToe(JFrame parentFrame, boolean againstComputer) {
        this.parentFrame = parentFrame;
        this.againstComputer = againstComputer;
        setLayout(new GridLayout(3, 3));
        font = new Font("Arial", Font.BOLD, 60);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setFont(font);
                button.addActionListener(new ButtonListener());
                add(button);
                board[i][j] = '-';
            }
        }
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            int index = getComponentZOrder(button);
            int row = index / 3;
            int col = index % 3;
            if (board[row][col] == '-' && (xTurn || !againstComputer)) {
                button.setText(xTurn ? "X" : "O");
                board[row][col] = xTurn ? 'X' : 'O';
                button.setEnabled(false);
                if (checkWinner()) {
                    return; // Winner has been found and game is reset
                }
                xTurn = !xTurn;
                if (againstComputer && !xTurn) {
                    computerMove();
                }
            }
        }
    }

    private boolean checkWinner() {
        // Check rows and columns for a winner
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '-') {
                showResult("Player " + board[i][0] + " wins!");
                return true;
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '-') {
                showResult("Player " + board[0][i] + " wins!");
                return true;
            }
        }

        // Check diagonals for a winner
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-') {
            showResult("Player " + board[0][0] + " wins!");
            return true;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '-') {
            showResult("Player " + board[0][2] + " wins!");
            return true;
        }

        // Check for a draw
        boolean draw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    draw = false;
                    break;
                }
            }
            if (!draw) break;
        }

        if (draw) {
            showResult("It's a draw!");
            return true;
        }

        return false;
    }

    private void showResult(String message) {
        JOptionPane.showMessageDialog(parentFrame, message);
        resetGame();
    }

    private void computerMove() {
        // Get a list of empty spots
        List<Point> emptySpots = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    emptySpots.add(new Point(i, j));
                }
            }
        }

        if (!emptySpots.isEmpty()) {
            // Pick a random empty spot
            Point move = emptySpots.get(random.nextInt(emptySpots.size()));
            board[move.x][move.y] = 'O';
            JButton button = (JButton) getComponent(move.x * 3 + move.y);
            button.setText("O");
            button.setEnabled(false);
            xTurn = true;
            checkWinner();
        }
    }

    private void resetGame() {
        xTurn = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
                JButton button = (JButton) getComponent(i * 3 + j);
                button.setText("");
                button.setEnabled(true);
            }
        }
    }

    private static void showWelcomeFrame() {
        JFrame welcomeFrame = new JFrame("Best Tic Tac Toe Game Ever");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setSize(600, 600);
        welcomeFrame.setLayout(new BorderLayout());

        JPanel contentPane = (JPanel) welcomeFrame.getContentPane();
        contentPane.setBackground(Color.RED);

        JLabel welcomeLabel = new JLabel("Welcome to the best Tic Tac Toe game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeFrame.add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeFrame.add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose();
                showOptionsFrame();
            }
        });

        welcomeFrame.setVisible(true);
    }

    private static void showOptionsFrame() {
        JFrame optionsFrame = new JFrame("Options");
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsFrame.setSize(600, 600);
        optionsFrame.setLayout(new BorderLayout());

        JPanel contentPane = (JPanel) optionsFrame.getContentPane();
        contentPane.setBackground(Color.RED);

        JLabel optionLabel = new JLabel("Choose an option", SwingConstants.CENTER);
        optionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        optionsFrame.add(optionLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton multiplayerButton = new JButton("Play Multiplayer");
        multiplayerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        multiplayerButton.setBackground(Color.WHITE);
        multiplayerButton.setForeground(Color.BLACK);

        JButton computerButton = new JButton("Play Against Computer");
        computerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        computerButton.setBackground(Color.WHITE);
        computerButton.setForeground(Color.BLACK);

        buttonPanel.add(multiplayerButton);
        buttonPanel.add(computerButton);

        optionsFrame.add(buttonPanel, BorderLayout.SOUTH);

        multiplayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionsFrame.dispose();
                showGameFrame(false);
            }
        });

        computerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionsFrame.dispose();
                showGameFrame(true);
            }
        });

        optionsFrame.setVisible(true);
    }

    private static void showGameFrame(boolean againstComputer) {
        JFrame gameFrame = new JFrame("Tic Tac Toe");
        gameFrame.setSize(600, 600);
        gameFrame.setMinimumSize(new Dimension(600, 600));
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameFrame.dispose();
                showOptionsFrame();
            }
        });

        gameFrame.add(backButton, BorderLayout.NORTH);

        TicTacToe gamePanel = new TicTacToe(gameFrame, againstComputer);
        gameFrame.add(gamePanel, BorderLayout.CENTER);
        gameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showWelcomeFrame();
            }
        });
    }
}
