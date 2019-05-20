package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3;
    static int cellSize = 150;
    private char[][] gameField;
    private GameButton[] gameButtons;
    private boolean difficulty = false;

    private Game game;

    static char nullSymbol = '\u0000';

    public GameBoard(Game currentGame){
        this.game = currentGame;
        initField();
    }

    private void initField(){
        setBounds(cellSize * dimension, cellSize * dimension, 400, 300);
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("Новая игра");

        JTextField text = new JTextField(10);
        JButton easy = new JButton("Легко");
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDifficulty(false);
                text.setText("Легко");
            }
        });
        JButton hard = new JButton("Сложно");
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDifficulty(true);
                text.setText("Сложно");
            }
        });

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
            }
        });

        //controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize*dimension, 150);
        controlPanel.add(easy);
        controlPanel.add(hard);
        controlPanel.add(text);

        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(dimension,dimension));
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);

        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension * dimension];

        for (int i = 0; i < (dimension * dimension); i++){
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    void emptyField(){
        for (int i = 0; i < (dimension * dimension); i++){
            gameButtons[i].setText("");

            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;

            gameField[x][y] = nullSymbol;
        }
    }

    Game getGame (){
        return game;
    }

    boolean isTurnable(int x, int y){
        boolean result = false;

        if (gameField[y][x] == nullSymbol)
            result = true;

        return result;
    }

    boolean isMatching(int x, int y, char playerSymbol){
        boolean result = false;

        if (gameField[y][x] == playerSymbol)
            result = true;

        return result;
    }

    void updateGameField(int x, int y){
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    boolean checkWin(){
        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();

        return (checkDiag(playerSymbol) || checkLines(playerSymbol));
    }

    private boolean checkDiag(char playerSymbol){
        boolean leftRight = true;
        boolean rightLeft = true;

        for (int i = 0; i < dimension; i++){
            leftRight &= (gameField[i][i] == playerSymbol);
            rightLeft &= (gameField[i][dimension - 1 - i] == playerSymbol);
        }

        return (leftRight || rightLeft);
    }

    private boolean checkLines(char playerSymbol){
        boolean line = true;
        boolean raw = true;

        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++){
                line &= (gameField[i][j] == playerSymbol);
                raw &= (gameField[j][i] == playerSymbol);
            }

            if (line || raw) return true;
            line = true;
            raw = true;
        }

        return false;
    }

    boolean isFull(){
        boolean result = true;

        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension;j++){
                if (gameField[i][j] == nullSymbol){
                    result = false;
                    break;
                }
                if (result = false) break;
            }
        }

        return result;
    }

    public GameButton getButton(int buttonIndex){
        return gameButtons[buttonIndex];
    }

    public void setDifficulty(boolean difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDifficult() {
        return difficulty;
    }
}
