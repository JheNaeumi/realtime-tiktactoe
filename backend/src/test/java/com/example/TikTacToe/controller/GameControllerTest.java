package com.example.TikTacToe.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.TikTacToe.dto.GameStateDto;
import com.example.TikTacToe.dto.MoveMessageDto;
import com.example.TikTacToe.entity.Game;
import com.example.TikTacToe.service.impl.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    public void testMakeMoveValid() throws Exception {
        String gameId = "game1";
        String playerId = "player1";
        char[][] board = {
                {'X', 'O', 'X'},
                {'O', 'X', 'O'},
                {'\0', '\0', '\0'}
        };
        GameStateDto gameStateDto = new GameStateDto(board, 'X', "");
        Game game = new Game(gameId, playerId, "player2", gameStateDto);
        MoveMessageDto moveMessage = new MoveMessageDto(gameId, playerId, 2, 2, 'X');

        when(gameService.getGameById(gameId)).thenReturn(Optional.of(game));

        mockMvc.perform(post("/move/{gameId}", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(moveMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.board[2][2]").value('X'))
                .andExpect(jsonPath("$.currentPlayer").value('O'))
                .andDo(print());

        verify(gameService, times(1)).getGameById(gameId);
    }

    @Test
    public void testMakeMoveInvalid() throws Exception {
        String gameId = "game1";
        MoveMessageDto moveMessage = new MoveMessageDto(gameId, "player1", 0, 0, 'X');

        when(gameService.getGameById(gameId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/move/{gameId}", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(moveMessage)))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(gameService, times(1)).getGameById(gameId);
    }

    @Test
    public void testGetGameState() throws Exception {
        String gameId = "game1";
        char[][] board = {
                {'X', 'O', 'X'},
                {'O', 'X', 'O'},
                {'\0', '\0', '\0'}
        };
        GameStateDto gameStateDto = new GameStateDto(board, 'X', "");
        Game game = new Game(gameId, "player1", "player2", gameStateDto);

        when(gameService.getGameById(gameId)).thenReturn(Optional.of(game));

        mockMvc.perform(get("/api/gameState")
                        .param("gameId", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.board[0][0]").value('X'))
                .andExpect(jsonPath("$.board[2][2]").value('\0'))
                .andDo(print());

        verify(gameService, times(1)).getGameById(gameId);
    }

    @Test
    public void testJoinGame() throws Exception {
        String gameId = "game1";
        Game game = new Game(gameId, null, null, new GameStateDto());

        when(gameService.getAvailableGame()).thenReturn(game);

        mockMvc.perform(get("/api/joinGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value(gameId))
                .andExpect(jsonPath("$.playerId").isNotEmpty())
                .andExpect(jsonPath("$.symbol").value("X"))
                .andDo(print());

        verify(gameService, times(1)).getAvailableGame();
    }
}
