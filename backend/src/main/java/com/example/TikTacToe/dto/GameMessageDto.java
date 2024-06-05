package com.example.TikTacToe.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameMessageDto {
    private String lobbyId;
    private String from;
    private int row;
    private int col;


}
