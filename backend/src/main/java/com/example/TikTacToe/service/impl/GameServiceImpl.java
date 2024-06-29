package com.example.TikTacToe.service.impl;


import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.entity.GameState;
import com.example.TikTacToe.repository.GameRepository;
import com.example.TikTacToe.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Game getAvailableGame() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            if (!game.isFull()) {
                return game;
            }
        }
        return new Game();
    }

    @Override
    public Game getGameById(String gameId) {
        return gameRepository.findByGameId(gameId).orElseThrow(()-> new RuntimeException("error getting gameID"));
    }

    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public void removeGameIfNotInUse() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            if (game.isCompleted()) {
                gameRepository.delete(game);
            }
        }
    }

    @Override
    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    @Override
    public boolean checkWin(GameState gameState, int row, int col) {
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

    @Override
    public boolean checkDraw(GameState gameState) {
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
