package com.example.TikTacToe.Mapper;

import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.entity.GameState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameStateMapper {

    default GameStateDto toGameStateDto(GameState gameState) {
        return new GameStateDto(gameState.getBoardArray(), gameState.getCurrentPlayer(), gameState.getStatusMessage());
    }
}
