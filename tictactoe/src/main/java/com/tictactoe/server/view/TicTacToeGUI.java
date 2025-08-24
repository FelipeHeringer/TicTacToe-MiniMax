package com.tictactoe.server.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.tictactoe.server.model.GameState;
import com.tictactoe.server.ai.TicTacToeAgent;
import com.tictactoe.server.model.GameBoard;
import com.tictactoe.server.model.Move;

public class TicTacToeGUI extends JFrame {
    private static final int BOARD_SIZE = 3;
    private static final int HUMAN_PLAYER = -1;
    private static final int AI_PLAYER = 1;
    private static final int EMPTY = 0;
    
    private JButton[][] boardButtons;
    private JButton newGameButton;
    private JLabel statusLabel;
    private GameBoard gameBoard;
    private GameState currentState;
    private TicTacToeAgent aiAgent;
    private boolean gameOver;
    
    public TicTacToeGUI() {
        initializeComponents();
        setupLayout();
        initializeGame();
    }
    
    private void initializeComponents() {
        // Initialize the game components
        gameBoard = new GameBoard();
        aiAgent = new TicTacToeAgent();
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        gameOver = false;
        
        // Create the grid of buttons with clean, minimal design
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setFont(new Font("SF Pro Display", Font.PLAIN, 48));
                boardButtons[row][col].setFocusPainted(false);
                boardButtons[row][col].setBackground(new Color(248, 249, 250));
                boardButtons[row][col].setBorder(BorderFactory.createLineBorder(new Color(230, 232, 236), 1));
                boardButtons[row][col].setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Hover effect
                final JButton button = boardButtons[row][col];
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        if (button.getText().isEmpty()) {
                            button.setBackground(new Color(240, 242, 245));
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        if (button.getText().isEmpty()) {
                            button.setBackground(new Color(248, 249, 250));
                        }
                    }
                });
                
                // Add action listener for each button
                final int r = row;
                final int c = col;
                boardButtons[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleHumanMove(r, c);
                    }
                });
            }
        }
        
        // Create new game button with minimal design
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("SF Pro Display",Font.BOLD, 14));
        newGameButton.setBackground(new Color(248, 249, 250));
        newGameButton.setForeground(new Color(60, 66, 87));
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 222, 226), 1),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect for new game button
        newGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newGameButton.setBackground(new Color(240, 242, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newGameButton.setBackground(new Color(248, 249, 250));
            }
        });
        
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        
        // Create status label with minimal styling
        statusLabel = new JLabel("Your turn", JLabel.CENTER);
        statusLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 16));
        statusLabel.setForeground(new Color(107, 114, 128));
    }
    
    private void setupLayout() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Main panel with light background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Game board panel with minimal grid layout
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE, 2, 2));
        boardPanel.setBackground(new Color(255, 255, 255));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardButtons[row][col].setPreferredSize(new Dimension(100, 100));
                boardPanel.add(boardButtons[row][col]);
            }
        }
        
        // Control panel with centered button
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(new Color(255, 255, 255));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        controlPanel.add(newGameButton);
        
        // Status panel with centered text
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(new Color(255, 255, 255));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        statusPanel.add(statusLabel);
        
        // Add panels to main panel
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
    
    private void initializeGame() {
        gameBoard.clearBoard();
        currentState = new GameState(gameBoard, false);
        gameOver = false;
        updateBoardDisplay();
        statusLabel.setText("Your turn");
    }
    
    private void startNewGame() {
        initializeGame();
        statusLabel.setForeground(new Color(107, 114, 128));
    }
    
    private void handleHumanMove(int row, int col) {
        if (gameOver || gameBoard.getBoard()[row][col] != EMPTY) {
            return; // Invalid move
        }
        
        // Apply human move
        Move humanMove = new Move(row, col);
        currentState = currentState.applyMove(humanMove, HUMAN_PLAYER);
        updateBoardDisplay();
        
        // Check if game is over after human move
        if (currentState.isTerminalState()) {
            handleGameEnd();
            return;
        }
        
        // AI's turn
        statusLabel.setText("AI thinking...");
        statusLabel.setForeground(new Color(156, 163, 175));
        
        // Use SwingUtilities.invokeLater to update GUI and then make AI move
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                makeAIMove();
            }
        });
    }
    
    private void makeAIMove() {
        if (gameOver) return;
        
        // Get AI's best move
        Move aiMove = aiAgent.findBestMove(currentState, 9); // Max depth of 9 for perfect play
        
        if (aiMove != null) {
            currentState = currentState.applyMove(aiMove, AI_PLAYER);
            updateBoardDisplay();
            
            // Check if game is over after AI move
            if (currentState.isTerminalState()) {
                handleGameEnd();
            } else {
                statusLabel.setText("Your turn");
                statusLabel.setForeground(new Color(107, 114, 128));
            }
        }
    }
    
    private void updateBoardDisplay() {
        int[][] board = currentState.getGameBoard().getBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                String text = "";
                Color textColor = new Color(75, 85, 99);
                Color bgColor = new Color(248, 249, 250);
                
                if (board[row][col] == HUMAN_PLAYER) {
                    text = "○";
                    textColor = new Color(59, 130, 246);
                    bgColor = new Color(239, 246, 255);
                } else if (board[row][col] == AI_PLAYER) {
                    text = "×";
                    textColor = new Color(239, 68, 68);
                    bgColor = new Color(254, 242, 242);
                }
                
                boardButtons[row][col].setText(text);
                boardButtons[row][col].setForeground(textColor);
                boardButtons[row][col].setBackground(bgColor);
            }
        }
    }
    
    private void handleGameEnd() {
        gameOver = true;
        int utility = currentState.getUtility();
        String message;
        
        if (utility == 1) {
            message = "AI wins";
            statusLabel.setForeground(new Color(239, 68, 68));
        } else if (utility == -1) {
            message = "You win!";
            statusLabel.setForeground(new Color(59, 130, 246));
        } else {
            message = "Draw";
            statusLabel.setForeground(new Color(107, 114, 128));
        }
        
        statusLabel.setText(message);
        
        // Show subtle dialog with game result
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int option = JOptionPane.showConfirmDialog(
                    TicTacToeGUI.this,
                    message + "\n\nPlay again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );
                
                if (option == JOptionPane.YES_OPTION) {
                    startNewGame();
                }
            }
        });
    }
}