package com.example.TikTacToe.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameId;
    private String playerX;
    private String playerO;


    @OneToOne(   cascade = CascadeType.ALL)
    @JoinColumn(name = "game_state_id", referencedColumnName = "id")
    private GameState gameState;
    public Game() {
        this.gameId = UUID.randomUUID().toString();
        this.gameState = new GameState();
    }

    public boolean isFull() {
        return playerX != null && playerO != null;
    }

    public boolean isCompleted() {
        return "X wins!".equals(gameState.getStatusMessage()) ||
                "O wins!".equals(gameState.getStatusMessage()) ||
                "Draw!".equals(gameState.getStatusMessage());
    }
}