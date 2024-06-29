package com.example.TikTacToe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameStateDto {
    private char[][] board;
    private char currentPlayer;
    private String statusMessage;

}

