package com.example.TikTacToe.controller;

import com.example.TikTacToe.dto.PlayerDto;
import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.dto.MoveMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private GameStateDto gameState = new GameStateDto();
    private String playerX;
    private String playerO;

    @MessageMapping("/move")
    @SendTo("/topic/game")
    public ResponseEntity<GameStateDto> makeMove(MoveMessageDto message) {
        String playerId = message.getPlayerId();
        int row = message.getRow();
        int col = message.getCol();
        char player = message.getFrom();

        if (playerId.equals(playerX) || playerId.equals(playerO)) {
            if (gameState.getBoard()[row][col] == '\0' && gameState.getCurrentPlayer() == player) {
                gameState.getBoard()[row][col] = player;
                if (checkWin(row, col)) {
                    gameState.setStatusMessage(player + " wins!");
                } else if (checkDraw()) {
                    gameState.setStatusMessage("Draw!");
                } else {
                    gameState.setCurrentPlayer(player == 'X' ? 'O' : 'X');
                }
            }
            logger.info("Sending game state: " + gameState);
            return new ResponseEntity<>(gameState, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/api/gameState")
    public ResponseEntity<GameStateDto> getGameState() {
        logger.info("Fetching initial game state: " + gameState);
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @GetMapping("/api/joinGame")
    public ResponseEntity<PlayerDto> joinGame() {
        String playerId = UUID.randomUUID().toString();
        PlayerDto playerDTO = new PlayerDto();
        playerDTO.setPlayerId(playerId);

        if (playerX == null) {
            playerX = playerId;
            playerDTO.setSymbol("X");
        } else if (playerO == null) {
            playerO = playerId;
            playerDTO.setSymbol("O");
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        logger.info("Player joined: " + playerId + " as " + playerDTO.getSymbol());
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }
    private boolean checkWin(int row, int col) {
        char[][] board = gameState.getBoard();
        char player = board[row][col];

        // Check row
        if (board[row][0] == player && board[row][1] == player && board[row][2] == player) return true;
        // Check column
        if (board[0][col] == player && board[1][col] == player && board[2][col] == player) return true;
        // Check diagonals
        if (row == col && board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (row + col == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;

        return false;
    }

    private boolean checkDraw() {
        char[][] board = gameState.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }
}


