package ru.aidar.mineswapper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aidar.mineswapper.dao.MinesweeperGameDao;
import ru.aidar.mineswapper.exception.BadRequestException;
import ru.aidar.mineswapper.model.MinesweeperGame;
import ru.aidar.mineswapper.service.mapper.MinesweeperMapper;
import ru.aidar.minesweeper.dto.GameInfoResponse;
import ru.aidar.minesweeper.dto.GameTurnRequest;
import ru.aidar.minesweeper.dto.NewGameRequest;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class MinesweeperServiceImpl implements MinesweeperService {

    private final MinesweeperMapper minesweeperMapper;
    private final MinesweeperGameDao minesweeperGameDao;

    @Override
    public GameInfoResponse newPost(NewGameRequest request) {
        validateNewGameRequest(request);
        MinesweeperGame game = createGame(request);
        minesweeperGameDao.save(game);
        String[][] matrix = game.getPlayerField();
        for (int i = 0; i < matrix.length; i++) { // Проход по строкам
            for (int j = 0; j < matrix[i].length; j++) { // Проход по столбцам
                System.out.print("'" + matrix[i][j] + "'" + "\t");
            }
            System.out.println(); // Перенос строки после каждой строки матрицы
        }
        System.out.println("______________________________________________________________________________________");
        return minesweeperMapper.toGameInfoResponse(game);
    }

    @Override
    public GameInfoResponse turnPost(GameTurnRequest request) {
        UUID gameId = request.getGameId();
        MinesweeperGame game = minesweeperGameDao.getById(gameId);
        validateGame(game, gameId);

        openCell(game, request.getRow(), request.getCol());

//        String[][] matrix = game.getPlayerField();
//        for (String[] strings : matrix) {
//            for (String string : strings) {
//                System.out.print("'" + string + "'" + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println("______________________________________________________________________________________");
        return minesweeperMapper.toGameInfoResponse(game);
    }

    private static void validateGame(MinesweeperGame game, UUID gameId) {
        if (game == null)
            throw new BadRequestException(format("Игра c id:%s не найдена", gameId));

        if (game.isCompleted())
            throw new BadRequestException(format("Игра c id:%s уже завершена", gameId));
    }

    private static void validateNewGameRequest(NewGameRequest request) {
        int fieldSize = request.getWidth() * request.getHeight();
        if (request.getMinesCount() > fieldSize - 1) {
            throw new BadRequestException((format("Указано больше мин чем поле игры, укажите меньше %s", fieldSize)));
        }
    }

    private MinesweeperGame createGame(NewGameRequest request) {
        MinesweeperGame game = new MinesweeperGame(request.getWidth(), request.getHeight(), request.getMinesCount());
        for (var row : game.getPlayerField()) {
            Arrays.fill(row, " ");
        }
        placeMines(game);
        return game;
    }

    private void openCell(MinesweeperGame game, int row, int col) {
        if (!game.getPlayerField()[row][col].equals(" "))
            throw new BadRequestException("Ячейка уже открыта");

        if (game.getMines()[row][col]) {
            explode(game, row, col);
            return;
        }

        floodFill(game, row, col);

        checkWin(game);
    }

    private void floodFill(MinesweeperGame game, int row, int col) {
        if (row < 0 || col < 0 || row >= game.getHeight() || col >= game.getWidth())
            return;

        if (!game.getPlayerField()[row][col].equals(" "))
            return;

        if (game.getMines()[row][col])
            return;

        int mines = countMines(game, row, col);

        game.getPlayerField()[row][col] = String.valueOf(mines);
        game.setOpenedCells(game.getOpenedCells() + 1);

        if (mines != 0)
            return;

        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++)
                if (!(dr == 0 && dc == 0))
                    floodFill(game, row + dr, col + dc);
    }

    private void explode(MinesweeperGame game, int row, int col) {

        game.setCompleted(true);

        for (int r = 0; r < game.getHeight(); r++) {
            for (int c = 0; c < game.getWidth(); c++) {

                if (game.getMines()[r][c])
                    game.getPlayerField()[r][c] = "X";
                else
                    game.getPlayerField()[r][c] =
                            String.valueOf(countMines(game, r, c));
            }
        }
    }

    private int countMines(MinesweeperGame game, int r, int c) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                int nr = r + dr;
                int nc = c + dc;
                if (nr < 0 || nc < 0 || nr >= game.getHeight() || nc >= game.getWidth())
                    continue;
                if (game.getMines()[nr][nc])
                    count++;
            }
        return count;
    }

    private void checkWin(MinesweeperGame game) {
        int opened = 0;
        for (int r = 0; r < game.getHeight(); r++) {
            for (int c = 0; c < game.getWidth(); c++) {

                if (!game.getPlayerField()[r][c].equals(" "))
                    opened++;
            }
        }
        int safeCells = game.getWidth() * game.getHeight() - game.getMinesCount();
        if (opened != safeCells)
            return;
        game.setCompleted(true);
        for (int r = 0; r < game.getHeight(); r++)
            for (int c = 0; c < game.getWidth(); c++)
                if (game.getMines()[r][c])
                    game.getPlayerField()[r][c] = "M";
    }

    private void placeMines(MinesweeperGame game) {
        Random random = new Random();
        int placed = 0;
        while (placed < game.getMinesCount()) {
            int r = random.nextInt(game.getHeight());
            int c = random.nextInt(game.getWidth());
            if (!game.getMines()[r][c]) {
                game.getMines()[r][c] = true;
                placed++;
            }
        }
    }
}
