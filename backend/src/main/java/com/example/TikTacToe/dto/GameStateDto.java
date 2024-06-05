package com.example.TikTacToe.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStateDto {
    private char[][] board;
    private char currentPlayer;
    private String statusMessage;

    public GameStateDto() {
        this.board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\0';
            }
        }
        this.currentPlayer = 'X';
        this.statusMessage = "Game in progress";
    }

}
