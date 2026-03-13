package ru.aidar.mineswapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Игра Сапер
 */
@AllArgsConstructor
@Getter
@Setter
public class MinesweeperGame {

    public MinesweeperGame(int width, int height, int minesCount) {
        this.width = width;
        this.height = height;
        this.minesCount = minesCount;
        this.mines = new boolean[height][width];
        this.playerField = new String[height][width];
    }

    /**
     * Идентификатор игры
     */
    private final UUID id = UUID.randomUUID();

    /**
     * Ширина поля игры
     */
    private final int width;

    /**
     * Высота поля игры
     */
    private final int height;

    /**
     * Количество мин
     */
    private final int minesCount;

    /**
     * Поле с минами
     */
    private final boolean[][] mines;

    /**
     * Поле игрока
     */
    private final String[][] playerField;

    /**
     * Признак завершения игры
     */
    private boolean completed = false;

}