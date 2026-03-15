package ru.aidar.mineswapper.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.aidar.mineswapper.exception.BadRequestException;
import ru.aidar.mineswapper.service.MinesweeperService;
import ru.aidar.minesweeper.dto.GameInfoResponse;
import ru.aidar.minesweeper.dto.GameTurnRequest;
import ru.aidar.minesweeper.dto.NewGameRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MinesweeperRestController.class)
class MinesweeperRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MinesweeperService minesweeperService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Should create new game and return valid response")
    @Test
    void newPostTest() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        GameInfoResponse response = new GameInfoResponse()
                .gameId(id)
                .width(5)
                .height(5)
                .minesCount(3);
        when(minesweeperService.newPost(any())).thenReturn(response);
        NewGameRequest request = new NewGameRequest()
                .width(5)
                .height(5)
                .minesCount(3);

        // When
        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game_id").value(id.toString()))
                .andExpect(jsonPath("$.width").value(5))
                .andExpect(jsonPath("$.height").value(5))
                .andExpect(jsonPath("$.mines_count").value(3));
    }

    @DisplayName("Should turn post and return valid response")
    @Test
    void turnPostTest() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        GameInfoResponse response = new GameInfoResponse()
                .gameId(id)
                .width(5)
                .height(5)
                .minesCount(3);
        when(minesweeperService.turnPost(any())).thenReturn(response);
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(1)
                .col(2);

        // When
        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game_id").value(id.toString()))
                .andExpect(jsonPath("$.width").value(5))
                .andExpect(jsonPath("$.height").value(5));
    }

    @DisplayName("Should return error response status when new post return error")
    @Test
    void newPostWithErrorTest() throws Exception {
        // Given
        when(minesweeperService.newPost(any())).thenThrow(new BadRequestException("Ошибка"));

        NewGameRequest request = new NewGameRequest()
                .width(5)
                .height(5)
                .minesCount(30);

        // When
        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Should return error response status when turn post return error")
    @Test
    void turnPostWithErrorTest() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        when(minesweeperService.turnPost(any())).thenThrow(new BadRequestException("Игра не найдена"));
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(0)
                .col(0);

        // When
        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isBadRequest());
    }
}