package com.example.TikTacToe.controller;

import com.example.TikTacToe.dto.PlayerDto;
import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.dto.MoveMessageDto;
import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.service.GameService;
import com.example.TikTacToe.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);


    @Autowired
    private GameService gameServiceImpl;

    @MessageMapping("/move/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public ResponseEntity<GameStateDto> makeMove(@DestinationVariable String gameId, MoveMessageDto message) {
        String playerId = message.getPlayerId();
        int row = message.getRow();
        int col = message.getCol();
        char player = message.getFrom();

        Optional<Game> gameOpt = gameServiceImpl.getGameById(gameId);
        if (gameOpt.isPresent()) {
            Game game = gameOpt.get();
            GameStateDto gameState = game.getGameState();

            if ((playerId.equals(game.getPlayerX()) || playerId.equals(game.getPlayerO())) &&
                    gameState.getBoard()[row][col] == '\0' &&
                    gameState.getCurrentPlayer() == player) {

                gameState.getBoard()[row][col] = player;
                if (checkWin(gameState, row, col)) {
                    gameState.setStatusMessage(player + " wins!");
                    gameState.setCurrentPlayer(' ');
                } else if (checkDraw(gameState)) {
                    gameState.setStatusMessage("Draw!");
                    gameState.setCurrentPlayer(' ');
                } else {
                    gameState.setCurrentPlayer(player == 'X' ? 'O' : 'X');
                }

                logger.info("Sending game state for game " + gameId + ": " + gameState);
                return new ResponseEntity<>(gameState, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/api/gameState")
    public ResponseEntity<GameStateDto> getGameState(@RequestParam String gameId) {
        Optional<Game> gameOpt = gameServiceImpl.getGameById(gameId);
        if (gameOpt.isPresent()) {
            GameStateDto gameState = gameOpt.get().getGameState();
            logger.info("Fetching game state for game " + gameId + ": " + gameState);
            return new ResponseEntity<>(gameState, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/api/joinGame")
    public ResponseEntity<PlayerDto> joinGame() {
        String playerId = UUID.randomUUID().toString();
        PlayerDto playerDTO = new PlayerDto();
        playerDTO.setPlayerId(playerId);

        Game availableGame = gameServiceImpl.getAvailableGame();

        if (availableGame.getPlayerX() == null) {
            availableGame.setPlayerX(playerId);
            gameServiceImpl.saveGame(availableGame);
            playerDTO.setSymbol("X");
        } else if (availableGame.getPlayerO() == null) {
            availableGame.setPlayerO(playerId);
            gameServiceImpl.saveGame(availableGame);
            playerDTO.setSymbol("O");
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        playerDTO.setGameId(availableGame.getGameId()); // Set the gameId in the PlayerDto
        logger.info("Player joined: " + playerId + " as " + playerDTO.getSymbol() + " in game " + availableGame.getGameId());
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    private boolean checkWin(GameStateDto gameState, int row, int col) {
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

    private boolean checkDraw(GameStateDto gameState) {
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