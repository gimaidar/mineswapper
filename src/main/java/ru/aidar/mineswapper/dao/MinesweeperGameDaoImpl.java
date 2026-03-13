package ru.aidar.mineswapper.dao;

import org.springframework.stereotype.Component;
import ru.aidar.mineswapper.model.MinesweeperGame;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MinesweeperGameDaoImpl implements MinesweeperGameDao {

    private final Map<UUID, MinesweeperGame> games = new ConcurrentHashMap<>();

    @Override
    public MinesweeperGame getById(UUID id) {
        return games.get(id);
    }

    @Override
    public MinesweeperGame save(MinesweeperGame game) {
        games.put(game.getId(), game);
        return game;
    }

}
