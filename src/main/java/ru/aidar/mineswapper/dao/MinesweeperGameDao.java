package ru.aidar.mineswapper.dao;

import ru.aidar.mineswapper.model.MinesweeperGame;

import java.util.UUID;

/**
 * Dao для сущности игра Сапер
 */
public interface MinesweeperGameDao {

    /**
     * Получить игру Сапер по id
     *
     * @param id идентификатор игры
     * @return игра
     */
    MinesweeperGame getById(UUID id);

    /**
     * Сохранить игру Сапер
     *
     * @param game игра
     * @return сохраненная игра
     */
    MinesweeperGame save(MinesweeperGame game);

}
