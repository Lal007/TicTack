package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameActionListener implements ActionListener {
    private int row;
    private int cell;
    private GameButton button;
    private boolean isExistWinner = false;

    public GameActionListener(int row, int cell, GameButton button) {
        this.row = row;
        this.cell = cell;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();
        if (board.isTurnable(row, cell)){
            updateByPlayersData(board);

            if (board.isFull()){
                board.getGame().showMessage("Ничья");
                board.emptyField();
            }else if (!isExistWinner){
                updateByAiData(board);
            }
        } else board.getGame().showMessage("Некорректный ход!");
        isExistWinner = false;
    }

    private void updateByPlayersData(GameBoard board){
        board.updateGameField(row, cell);

        button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        if (board.checkWin()){
            button.getBoard().getGame().showMessage("Вы выиграли!");
            board.emptyField();
            isExistWinner = true;
        } else {
            board.getGame().passTurn();
        }
    }

    private void updateByAiData(GameBoard board){
        if (board.isDifficult()){
            int x = -1;
            int y = -1;
            char playerSymbol = board.getGame().getCurrentPlayer().getPlayerSign();

            int score = 0;
            for (int i = 0; i < GameBoard.dimension; i++){
                for (int j = 0; j < GameBoard.dimension; j++){
                    int tmp = 0;
                    if (board.isTurnable(i, j)){                                                   //Находим пустую ячейку
                        //if ((i - 1) >= 0 && (j - 1) >= 0 && (i + 1) < SIZE && (j + 1) < SIZE)
                        if ((i - 1) >= 0 && (j - 1) >= 0 && board.isMatching(i - 1, j - 1, playerSymbol)) tmp++;     //Проверка ячейки слева-сверху
                        if ((i - 1) >= 0 && board.isMatching(i - 1, j, playerSymbol)) tmp++;                         //Проверка ячейки сверху
                        if ((j - 1) >= 0 && board.isMatching(i, j - 1, playerSymbol)) tmp++;                         //Проверка ячейки слева
                        if ((i - 1) >= 0 && (j + 1) < GameBoard.dimension && board.isMatching(i - 1, j + 1, playerSymbol)) tmp++;   //Проверка ячейки справа-сверху
                        if ((j + 1) < GameBoard.dimension && board.isMatching(i, j + 1, playerSymbol)) tmp++;                       //Проверка ячейки справа
                        if ((i + 1) < GameBoard.dimension && (j + 1) < GameBoard.dimension && board.isMatching(i + 1, j + 1, playerSymbol)) tmp++; //Проверка ячейки справа-снизу
                        if ((i + 1) < GameBoard.dimension && board.isMatching(i + 1, j, playerSymbol)) tmp++;                       //Проверка ячейки снизу
                        if ((i + 1) < GameBoard.dimension &&(j - 1) >= 0 && board.isMatching(i + 1, j - 1, playerSymbol)) tmp++;    //Проверка ячейки слева-снизу

                        if (tmp > score){  // отбор и сохранение ячейки с наибольшим количеством соседнех ноликов
                            score = tmp;
                            x = i;
                            y = j;
                        }
                    }
                }
            }

            if (x == -1 && y == -1){   // Если ни в одной из соседних ячееек не нашелся нолик генерируем координаты рандомно
                do {
                    Random random = new Random();
                    x = random.nextInt(GameBoard.dimension);
                    y = random.nextInt(GameBoard.dimension);
                }while (!board.isTurnable(x, y));

            }
            board.updateGameField(x, y);
            int cellIndex = GameBoard.dimension * x + y;
            board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
            if (board.checkWin()) {
                button.getBoard().getGame().showMessage("Компьютер выиграл!");
                board.emptyField();
                board.getGame().passTurn();
            } else board.getGame().passTurn();

        }else {
            int x, y;
            Random rnd = new Random();

            do {
                x = rnd.nextInt(GameBoard.dimension);
                y = rnd.nextInt(GameBoard.dimension);
            } while (!board.isTurnable(x, y));

            board.updateGameField(x, y);

            int cellIndex = GameBoard.dimension * x + y;
            board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

            if (board.checkWin()) {
                button.getBoard().getGame().showMessage("Компьютер выиграл!");
                board.emptyField();
                board.getGame().passTurn();
            } else board.getGame().passTurn();
        }
    }
}
