package com.tictactoe.server.model;

import java.util.ArrayList;

public class GameState {
    private GameBoard gameBoard;
    private boolean isTerminalState;

    public GameState(GameBoard board, boolean isTerminalState) {
        this.gameBoard = board;
        this.isTerminalState = isTerminalState;
    }

    public GameState applyMove(Move move, int player) {
        
        GameBoard newBoard = gameBoard.copy();
        int[][] board = newBoard.getBoard();
        int row = move.getRow();
        int col = move.getColumn();

        if (player == 1) {
            board[row][col] = 1;
        } else {
            board[row][col] = -1;
        }

        return new GameState(newBoard, isTerminalState());
    }

    public ArrayList<Move> getAvailableMoves() {
        int[][] board = gameBoard.getBoard();
        ArrayList<Move> moves = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    moves.add(new Move(row, col));
                }
            }
        }
        return moves;
    }

    public boolean isTerminalState() {

        int winner = checkWinner();
        if (winner != 0 || getAvailableMoves().size() == 0) {
            return true;
        }

        return false;
    }

    private int checkWinner() {
        int[][] board = gameBoard.getBoard();

        // check horizontal
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][0] == board[row][1] && board[row][1] == board[row][2] && board[row][0] != 0) {
                    return board[row][0];
                }
            }
        }

        // check vertical
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[0][col] == board[1][col] && board[1][col] == board[2][col] && board[0][col] != 0) {
                    return board[0][col];
                }
            }
        }

        // check diagonal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return 0;
    }

    public int getUtility(){
        int winner = checkWinner();

        if(winner != 0 ) {
            // Verify who is the winner
            if (winner == 1) {
                return 1;
            }else {
                return -1;
            }
        }

        return 0;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

}