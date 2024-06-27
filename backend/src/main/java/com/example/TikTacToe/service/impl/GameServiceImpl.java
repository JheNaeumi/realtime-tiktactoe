package com.example.TikTacToe.service.impl;


import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.entity.GameState;
import com.example.TikTacToe.repository.GameRepository;
import com.example.TikTacToe.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
