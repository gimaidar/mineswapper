package ru.aidar.mineswapper.service;

import ru.aidar.minesweeper.dto.GameInfoResponse;
import ru.aidar.minesweeper.dto.GameTurnRequest;
import ru.aidar.minesweeper.dto.NewGameRequest;

/**
 * Сервис для работы с игрой Сапер
 */
public interface MinesweeperService {

    /**
     * Создать игру Сапер
     *
     * @param newGameRequest запрос на создание игры
     * @return Ответ на запрос на создание игры
     */
    GameInfoResponse newPost(NewGameRequest newGameRequest);

    /**
     * Выполнить ход в игре Сапер
     * @param gameTurnRequest запрос на выполнение хода в игре Сапер
     * @return Ответ на запрос на выполнение хода в игре Сапер
     */
    GameInfoResponse turnPost(GameTurnRequest gameTurnRequest);

}
