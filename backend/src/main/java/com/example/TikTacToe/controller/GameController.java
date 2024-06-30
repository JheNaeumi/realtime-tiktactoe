package com.example.TikTacToe.controller;

import com.example.TikTacToe.Mapper.GameStateMapper;
import com.example.TikTacToe.dto.PlayerDto;
import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.dto.MoveMessageDto;
import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.entity.GameState;
import com.example.TikTacToe.service.impl.GameServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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


import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameServiceImpl gameServiceImpl;

    private final GameStateMapper gameStateMapper;

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
                if (gameServiceImpl.checkWin(gameState, row, col)) {
                    gameState.setStatusMessage(player + " wins!");
                    gameState.setCurrentPlayer(' ');
                    game.isCompleted();

                } else if (gameServiceImpl.checkDraw(gameState)) {
                    gameState.setStatusMessage("Draw!");
                    gameState.setCurrentPlayer(' ');
                    game.isCompleted();
                } else {
                    gameState.setCurrentPlayer(player == 'X' ? 'O' : 'X');
                }
                game.setGameState(gameState);
                gameServiceImpl.saveGame(game);
                logger.info("Sending game state for game " + gameId + ": " + gameState.getBoard());
                return new ResponseEntity<>(gameStateMapper.toGameStateDto(gameState), HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //TODO: Separation of concern put it in Service
    @GetMapping("/api/gameState")
    public ResponseEntity<GameStateDto> getGameState(@RequestParam String gameId) {
        Game game = gameServiceImpl.getGameById(gameId);
        if (game!=null) {
            GameState gameState = game.getGameState();
            if(gameState.getBoardArray()[0][0] == '\0') System.out.println(gameState.getBoardArray()[0][0]);
            logger.info("Fetching game state for game " + gameId + ": " + gameState.getBoard());
            return new ResponseEntity<>(gameStateMapper.toGameStateDto(gameState), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //TODO: Separation of concern put it in Service
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
            availableGame.setCreatedAt(LocalDateTime.now());
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



}