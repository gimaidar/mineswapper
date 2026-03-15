package ru.aidar.mineswapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.aidar.mineswapper.service.MinesweeperService;
import ru.aidar.minesweeper.api.MinesweeperApi;
import ru.aidar.minesweeper.dto.GameInfoResponse;
import ru.aidar.minesweeper.dto.GameTurnRequest;
import ru.aidar.minesweeper.dto.NewGameRequest;

@RestController
@AllArgsConstructor
@CrossOrigin
public class MinesweeperRestController implements MinesweeperApi {

    private final MinesweeperService minesweeperService;

    @Override
    public ResponseEntity<GameInfoResponse> newPost(NewGameRequest newGameRequest) {
        return ResponseEntity.ok(minesweeperService.newPost(newGameRequest));
    }

    @Override
    public ResponseEntity<GameInfoResponse> turnPost(GameTurnRequest gameTurnRequest) {
        return ResponseEntity.ok(minesweeperService.turnPost(gameTurnRequest));
    }

}
