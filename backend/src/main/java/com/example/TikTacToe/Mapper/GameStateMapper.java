package com.example.TikTacToe.Mapper;

import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.entity.GameState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameStateMapper {

    GameStateDto toGameStateDto(GameState gameState);
}
