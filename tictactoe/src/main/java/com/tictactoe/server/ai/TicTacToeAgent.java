package com.tictactoe.server.ai;

import com.tictactoe.server.model.GameState;
import com.tictactoe.server.model.Move;

public class TicTacToeAgent {
    
    // AI
    private static final int MAX_PLAYER = 1;

    // Human
    private static final int MIN_PLAYER = -1;

    public Integer minimax(GameState state, int depth, int player) {
        if (state.isTerminalState() || depth == 0) {
            return state.getUtility();
        }

        if (player == MAX_PLAYER) {
            int bestValue = Integer.MIN_VALUE;
            for (Move move : state.getAvailableMoves()) {
                GameState newState = state.applyMove(move, MAX_PLAYER);
                int value = minimax(newState, depth - 1, MIN_PLAYER);
                bestValue = Math.max(bestValue, value);
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (Move move : state.getAvailableMoves()) {
                GameState newState = state.applyMove(move, MIN_PLAYER);
                int value = minimax(newState, depth - 1, MAX_PLAYER);
                bestValue = Math.min(bestValue, value);
            }
            return bestValue;
        }
    }

    public Move findBestMove(GameState state, int maxDepth) {
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : state.getAvailableMoves()) {
            GameState newState = state.applyMove(move, MAX_PLAYER);
            int value = minimax(newState, maxDepth - 1, MIN_PLAYER);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }

}
