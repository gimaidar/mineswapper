package ru.aidar.mineswapper.service.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.aidar.mineswapper.model.MinesweeperGame;
import ru.aidar.minesweeper.dto.GameInfoResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringJUnitConfig(MinesweeperMapperImpl.class)
class MinesweeperMapperTest {

    @Autowired
    private MinesweeperMapper mapper;

    @DisplayName("Shoul map MinesweeperGame to GameInfoResponse")
    @Test
    void toGameInfoResponseTest() {
        // Given
        MinesweeperGame game = new MinesweeperGame(4, 4, 1);
        game.getPlayerField()[0][0] = " ";
        game.getPlayerField()[0][1] = "X";
        game.getPlayerField()[0][2] = "M";
        game.getPlayerField()[0][3] = "0";
        game.getPlayerField()[1][0] = "1";
        game.getPlayerField()[1][1] = "2";
        game.getPlayerField()[1][2] = "3";
        game.getPlayerField()[1][3] = "4";
        game.getPlayerField()[2][0] = "5";
        game.getPlayerField()[2][1] = "6";
        game.getPlayerField()[2][2] = "7";
        game.getPlayerField()[2][3] = "8";
        game.getPlayerField()[3][0] = " ";
        game.getPlayerField()[3][1] = " ";
        game.getPlayerField()[3][2] = " ";
        game.getPlayerField()[3][3] = " ";

        // When
        GameInfoResponse response = mapper.toGameInfoResponse(game);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getGameId()).isEqualTo(game.getId());
        assertThat(response.getCompleted()).isEqualTo(game.isCompleted());
        assertThat(response.getWidth()).isEqualTo(game.getWidth());
        assertThat(response.getHeight()).isEqualTo(game.getHeight());

        List<List<GameInfoResponse.FieldEnum>> field = response.getField();
        assertThat(field.get(0).get(0)).isEqualTo(GameInfoResponse.FieldEnum.SPACE);
        assertThat(field.get(0).get(1)).isEqualTo(GameInfoResponse.FieldEnum.X);
        assertThat(field.get(0).get(2)).isEqualTo(GameInfoResponse.FieldEnum.M);
        assertThat(field.get(0).get(3)).isEqualTo(GameInfoResponse.FieldEnum._0);
        assertThat(field.get(1).get(0)).isEqualTo(GameInfoResponse.FieldEnum._1);
        assertThat(field.get(1).get(1)).isEqualTo(GameInfoResponse.FieldEnum._2);
        assertThat(field.get(1).get(2)).isEqualTo(GameInfoResponse.FieldEnum._3);
        assertThat(field.get(1).get(3)).isEqualTo(GameInfoResponse.FieldEnum._4);
        assertThat(field.get(2).get(0)).isEqualTo(GameInfoResponse.FieldEnum._5);
        assertThat(field.get(2).get(1)).isEqualTo(GameInfoResponse.FieldEnum._6);
        assertThat(field.get(2).get(2)).isEqualTo(GameInfoResponse.FieldEnum._7);
        assertThat(field.get(2).get(3)).isEqualTo(GameInfoResponse.FieldEnum._8);
        assertThat(field.get(3).get(0)).isEqualTo(GameInfoResponse.FieldEnum.SPACE);
        assertThat(field.get(3).get(1)).isEqualTo(GameInfoResponse.FieldEnum.SPACE);
        assertThat(field.get(3).get(2)).isEqualTo(GameInfoResponse.FieldEnum.SPACE);
        assertThat(field.get(3).get(3)).isEqualTo(GameInfoResponse.FieldEnum.SPACE);
    }

    @Test
    void toGameInfoResponseWithExceptionTest() {
        // Given
        MinesweeperGame game = new MinesweeperGame(1, 1, 0);
        game.getPlayerField()[0][0] = "?";

        // When-Then
        assertThatThrownBy(() -> mapper.toGameInfoResponse(game))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Неизвестный символ");
    }


}