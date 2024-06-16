package com.example.TikTacToe.entity;

import com.example.TikTacToe.dto.GameStateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Game {
    private String gameId;
    private String playerX;
    private String playerO;
    private GameStateDto gameState;

    public Game() {
        this.gameId = UUID.randomUUID().toString();
        this.gameState = new GameStateDto();
    }

    public boolean isFull() {
        return playerX != null && playerO != null;
    }
}
