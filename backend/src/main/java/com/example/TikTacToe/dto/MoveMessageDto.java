package com.example.TikTacToe.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveMessageDto {
    private int row;
    private int col;
    private char from;
    private String playerId;


}
