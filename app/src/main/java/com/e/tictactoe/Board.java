package com.e.tictactoe;

import java.util.ArrayList;

/**
 * Internal Board Logic
 * -- board numbers --
 * 0 = empty
 * 1 = 'X'
 * 2 = 'O'
 */


public class Board {
    private int[][] board;
    private int playerMarker;

    public Board(int[][] grid, int playermarker) {
        this.playerMarker = playermarker;
        board = new int[3][3];
        for (int row = 0; row < 3; row++)
            for (int column = 0; column < 3; column++)
                board[row][column] = grid[row][column];
    }

    public Board(int playerMarker) {
        this.playerMarker = playerMarker;
        board = new int[3][3];
    }

    public int[][] getBoard() {
        return board;
    }

    // makes move for opponent, returns it


    /**
     * May have to fix this
     * @return whether there are available moves left.
     */
    public boolean hasMovesLeft() {
        for (int[] row : board)
            for (int val : row)
                if (val == 0)
                    return true;
        return false;
    }

    public int getScore() {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == getOpposingMarker())
                    return 10;
                if (board[0][i] == playerMarker)
                    return -10;
            }

            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == getOpposingMarker())
                    return 10;
                if (board[i][0] == playerMarker)
                    return -10;
            }
        }

        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[0][2] == board[1][1] && board[1][1] == board[2][0])) {
            if (board[1][1] == getOpposingMarker())
                return 10;
            if (board[1][1] == playerMarker)
                return -10;
        }
        return 0;
    }

    public int minimax(int depth, boolean isMaximizing) {
        int score = getScore();

        if (score == 10 || score == -10)
            return score;
        if (!hasMovesLeft())
            return 0;

        int best = isMaximizing ? -1000 : 1000;

        for (int row = 0; row < 3; row++)
            for (int column = 0; column < 3; column++)
                if (board[row][column] == 0) {
                    board[row][column] = isMaximizing ? getOpposingMarker() : playerMarker;
                    best = isMaximizing ? Math.max(best, minimax(depth + 1, !isMaximizing)) : Math.min(best, minimax(depth + 1, !isMaximizing));
                    board[row][column] = 0;
                }
        return best;
    }

    public Move getBestMove() {
        int bestValue = -1000;
        Move bestMove = new Move(-1,-1);
        for (int row = 0; row < 3; row++)
            for (int column = 0; column < 3; column++)
                if (board[row][column] == 0) {
                    board[row][column] = getOpposingMarker();
                    int moveValue = minimax(0, false);
                    board[row][column] = 0;
                    if (moveValue > bestValue) {
                        bestMove = new Move(row, column);
                        bestValue = moveValue;
                    }
                }
        return bestMove;
    }

    public void makeSmartOpponentMove() {
        Move best = getBestMove();
        makeMove(best, getOpposingMarker());
    }


    public void makeDumbOpponentMove() {
        ArrayList<Move> openSpaces = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (board[row][column] == 0) {
                    openSpaces.add(new Move(row, column));
                }
            }
        }
        Move randomMove = openSpaces.get((int)(Math.random() * openSpaces.size()));
        makeMove(randomMove,getOpposingMarker());
    }





    public boolean makePlayerMove(int row, int column) {
        Move move = new Move(row, column);
        if (makeMove(move, playerMarker))
            return true;
        return false;
    }

    public boolean makeMove(Move move, int marker) {
        if (board[move.getRow()][move.getColumn()] == 0) {
            board[move.getRow()][move.getColumn()] = marker;
            return true;
        }
        return false;
    }

    public int getOpposingMarker() {
        return playerMarker == 2 ? 1 : 2;
    }

    public int getWinner() {
        return getScore();
    }


}
