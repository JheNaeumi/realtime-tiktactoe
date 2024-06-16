package com.example.TikTacToe.service.impl;


import com.example.TikTacToe.entity.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private List<Game> games = new ArrayList<>();

    public Game getAvailableGame() {
        for (Game game : games) {
            if (!game.isFull()) {
                return game;
            }
        }
        Game newGame = new Game();
        games.add(newGame);
        return newGame;
    }

    public Optional<Game> getGameById(String gameId) {
        return games.stream()
                .filter(game -> game.getGameId().equals(gameId))
                .findFirst();
    }

    public List<Game> getGames() {
        return games;
    }
}