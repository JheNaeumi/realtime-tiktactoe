package com.example.TikTacToe.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveMessageDto {
    private String gameId;
    private String playerId;
    private int row;
    private int col;
    private char from;

}
