package com.example.TikTacToe.service;

import com.example.TikTacToe.entity.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {

    Game getAvailableGame();

    Optional<Game> getGameById(String gameId);

    List<Game> getGames();

    void removeGameIfNotInUse();

    void saveGame(Game game);
}
