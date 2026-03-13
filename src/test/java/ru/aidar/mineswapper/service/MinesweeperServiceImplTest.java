package ru.aidar.mineswapper.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.aidar.mineswapper.dao.MinesweeperGameDao;
import ru.aidar.mineswapper.exception.BadRequestException;
import ru.aidar.mineswapper.model.MinesweeperGame;
import ru.aidar.mineswapper.service.mapper.MinesweeperMapperImpl;
import ru.aidar.minesweeper.dto.GameInfoResponse;
import ru.aidar.minesweeper.dto.GameTurnRequest;
import ru.aidar.minesweeper.dto.NewGameRequest;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {
        MinesweeperServiceImpl.class,
        MinesweeperMapperImpl.class
})
class MinesweeperServiceImplTest {

    @Autowired
    private MinesweeperServiceImpl service;

    @MockitoBean
    private MinesweeperGameDao minesweeperGameDao;

    @DisplayName("Should save MinesweeperGame")
    @Test
    void newPostTest() {
        // Given
        NewGameRequest request = new NewGameRequest()
                .width(5)
                .height(5)
                .minesCount(3);

        // When
        GameInfoResponse response = service.newPost(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getWidth()).isEqualTo(5);
        assertThat(response.getHeight()).isEqualTo(5);
        assertThat(response.getMinesCount()).isEqualTo(3);
        verify(minesweeperGameDao).save(any());
    }

    @DisplayName("Should throw exception when request.minesCount is invalid")
    @Test
    void newPostWithErrorTest() {
        // Given
        NewGameRequest request = new NewGameRequest()
                .width(2)
                .height(2)
                .minesCount(4);

        // When-Then
        assertThatThrownBy(() -> service.newPost(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("больше мин");
        verify(minesweeperGameDao, never()).save(any());
    }

    @DisplayName("Should throw exception when game not found by id")
    @Test
    void turnPostNotFoundTest() {
        // Given
        UUID id = UUID.randomUUID();
        when(minesweeperGameDao.getById(id)).thenReturn(null);
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(0)
                .col(0);

        // When-Then
        assertThatThrownBy(() -> service.turnPost(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("не найдена");
    }

    @DisplayName("Should throw exception when game already completed")
    @Test
    void turnPostAlreadyCompletedTest() {
        // Given
        MinesweeperGame game = new MinesweeperGame(3, 3, 0);
        UUID id = game.getId();
        game.setCompleted(true);

        when(minesweeperGameDao.getById(id)).thenReturn(game);
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(0)
                .col(0);

        // When-Then
        assertThatThrownBy(() -> service.turnPost(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже завершена");
    }

    @DisplayName("Should return player field with opened cell")
    @Test
    void turnPostTest() {
        // Given
        MinesweeperGame game = new MinesweeperGame(3, 3, 0);
        for (var row : game.getPlayerField()) {
            Arrays.fill(row, " ");
        }
        UUID id = game.getId();
        when(minesweeperGameDao.getById(id)).thenReturn(game);
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(1)
                .col(1);

        // When
        GameInfoResponse response = service.turnPost(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(game.getPlayerField()[1][1]).isNotEqualTo(" ");
    }

    @DisplayName("Should return player field with opened mine")
    @Test
    void turnPostMineOpenedTest() {
        // Given
        MinesweeperGame game = new MinesweeperGame(2, 2, 1);
        for (var row : game.getPlayerField()) {
            Arrays.fill(row, " ");
        }
        game.getMines()[0][0] = true;
        UUID id = game.getId();
        when(minesweeperGameDao.getById(id)).thenReturn(game);
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(0)
                .col(0);

        // When
        GameInfoResponse response = service.turnPost(request);

        // Then
        assertThat(game.isCompleted()).isTrue();
        assertThat(game.getPlayerField()[0][0]).isEqualTo("X");
        assertThat(response).isNotNull();
    }

    @DisplayName("Should throw exception when cell already opened")
    @Test
    void turnPostCellAlreadyOpenedTest() {
        // Given
        MinesweeperGame game = new MinesweeperGame(3, 3, 0);
        for (var row : game.getPlayerField()) {
            Arrays.fill(row, " ");
        }
        game.getPlayerField()[1][1] = "1";
        UUID id = game.getId();
        when(minesweeperGameDao.getById(id)).thenReturn(game);

        // When
        GameTurnRequest request = new GameTurnRequest()
                .gameId(id)
                .row(1)
                .col(1);

        // Then
        assertThatThrownBy(() -> service.turnPost(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже открыта");
    }

}