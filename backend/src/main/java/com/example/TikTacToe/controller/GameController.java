package com.example.TikTacToe.controller;

import com.example.TikTacToe.dto.PlayerDto;
import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.dto.MoveMessageDto;
import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.entity.GameState;
import com.example.TikTacToe.repository.GameRepository;
import com.example.TikTacToe.service.GameService;
import com.example.TikTacToe.service.impl.GameServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);



    private  final GameServiceImpl gameServiceImpl;

    private final GameRepository gameRepository;

    @Transactional
    @MessageMapping("/move/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public ResponseEntity<GameStateDto> makeMove(@DestinationVariable String gameId, MoveMessageDto message) {
        String playerId = message.getPlayerId();
        int row = message.getRow();
        int col = message.getCol();
        char player = message.getFrom();


        Game game = gameServiceImpl.getGameById(gameId);
        if (game!=null) {
            GameState gameState = game.getGameState();
            if ((playerId.equals(game.getPlayerX()) || playerId.equals(game.getPlayerO())) &&
                    (gameState.getBoardArray()[row][col] == '\0') &&
                    (gameState.getCurrentPlayer() == player)) {
                char[][] boardArray = gameState.getBoardArray();
                boardArray[row][col] = player;
                gameState.setBoardArray(boardArray);
                if (checkWin(gameState, row, col)) {
                    gameState.setStatusMessage(player + " wins!");
                    gameState.setCurrentPlayer(' ');
                } else if (checkDraw(gameState)) {
                    gameState.setStatusMessage("Draw!");
                    gameState.setCurrentPlayer(' ');
                } else {
                    gameState.setCurrentPlayer(player == 'X' ? 'O' : 'X');
                }
                game.setGameState(gameState);
                gameServiceImpl.saveGame(game);
                logger.info("Sending game state for game " + gameId + ": " + gameState.getBoard());
                return new ResponseEntity<>(GameStateDto.fromEntity(gameState), HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/api/gameState")
    public ResponseEntity<GameStateDto> getGameState(@RequestParam String gameId) {
        Game game = gameServiceImpl.getGameById(gameId);
        if (game!=null) {
            GameState gameState = game.getGameState();
            if(gameState.getBoardArray()[0][0] == '\0') System.out.println(gameState.getBoardArray()[0][0]);
            logger.info("Fetching game state for game " + gameId + ": " + gameState.getBoard() + "array" + gameState.getBoardArray());
            return new ResponseEntity<>(GameStateDto.fromEntity(gameState), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/api/joinGame")
    public ResponseEntity<PlayerDto> joinGame() {
        String playerId = UUID.randomUUID().toString();
        PlayerDto playerDTO = new PlayerDto();
        playerDTO.setPlayerId(playerId);

        Game availableGame = gameServiceImpl.getAvailableGame();
        GameState gameState = new GameState();
        if (availableGame.getPlayerX() == null) {
            availableGame.setPlayerX(playerId);
            playerDTO.setSymbol("X");
            availableGame.setGameState(gameState);
            gameServiceImpl.saveGame(availableGame);
        } else if (availableGame.getPlayerO() == null) {
            availableGame.setPlayerO(playerId);
            playerDTO.setSymbol("O");
            gameServiceImpl.saveGame(availableGame);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        playerDTO.setGameId(availableGame.getGameId()); // Set the gameId in the PlayerDto
        logger.info("Player joined: " + playerId + " as " + playerDTO.getSymbol() + " in game " + availableGame.getGameId());
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    private boolean checkWin(GameState gameState, int row, int col) {
        char[][] board = gameState.getBoardArray();
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

    private boolean checkDraw(GameState gameState) {
        char[][] board = gameState.getBoardArray();
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