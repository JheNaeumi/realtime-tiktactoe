package com.example.TikTacToe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Entity
@Setter
@Getter
public class GameState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> board;

    private char currentPlayer;
    private String statusMessage;

    public GameState() {
        this.board = Arrays.asList(
                "\0", "\0", "\0",
                "\0", "\0", "\0",
                "\0", "\0", "\0"
        );
        this.currentPlayer = 'X';
        this.statusMessage = "Game in progress";
    }

    public char[][] getBoardArray() {
        char[][] boardArray = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardArray[i][j] = board.get(i * 3 + j).charAt(0);
            }
        }
        return boardArray;
    }

    public void setBoardArray(char[][] boardArray) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.set(i * 3 + j, String.valueOf(boardArray[i][j]));
            }
        }
    }
}