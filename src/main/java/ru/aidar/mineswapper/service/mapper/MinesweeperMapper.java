package ru.aidar.mineswapper.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.aidar.mineswapper.model.MinesweeperGame;
import ru.aidar.minesweeper.dto.GameInfoResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.aidar.minesweeper.dto.GameInfoResponse.FieldEnum.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MinesweeperMapper {

    @Mapping(target = "gameId", source = "id")
    @Mapping(target = "field", source = "playerField", qualifiedByName = "toField")
    GameInfoResponse toGameInfoResponse(MinesweeperGame game);

    @Named("toField")
    default List<List<GameInfoResponse.FieldEnum>> toField(String[][] field) {
        return Arrays.stream(field)
                .map(row -> Arrays.stream(row)
                        .map(this::mapToEnumString)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    default GameInfoResponse.FieldEnum mapToEnumString(String val) {
        return switch (val) {
            case " " -> SPACE;
            case "0" -> _0;
            case "1" -> _1;
            case "2" -> _2;
            case "3" -> _3;
            case "4" -> _4;
            case "5" -> _5;
            case "6" -> _6;
            case "7" -> _7;
            case "8" -> _8;
            case "M" -> M;
            case "X" -> X;
            default -> throw new IllegalStateException("Неизвестный символ на поле: " + val);
        };
    }

}
