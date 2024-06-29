package com.example.TikTacToe.service;

import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.entity.GameState;

import java.util.List;

public interface GameService {

    Game getAvailableGame();

    Game getGameById(String gameId);

    List<Game> getGames();

    void removeGameIfNotInUse();

    void saveGame(Game game);

    boolean checkWin(GameState gameState, int row, int col);

    boolean checkDraw(GameState gameState);
}
