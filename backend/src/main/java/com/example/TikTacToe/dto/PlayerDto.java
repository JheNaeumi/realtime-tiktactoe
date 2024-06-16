package com.example.TikTacToe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDto {
    private String playerId;
    private String symbol;
    private String gameId; // Add this field to include the game ID
}
