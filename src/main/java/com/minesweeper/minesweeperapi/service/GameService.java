package com.minesweeper.minesweeperapi.service;

import java.util.List;

import com.minesweeper.minesweeperapi.controller.request.CreateRequest;
import com.minesweeper.minesweeperapi.controller.request.EventRequest;
import com.minesweeper.minesweeperapi.model.Game;
import com.minesweeper.minesweeperapi.repository.GameRepository;

import org.springframework.stereotype.Service;

/**
 * GameService
 */
@Service
public class GameService {
    
    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game create(final CreateRequest aRequest) {
        final Game newGame = Game.builder()
            .status("NEW")
            .username(aRequest.getUsername())
            .rows(aRequest.getRows())
            .columns(aRequest.getColumns())
            .mines(aRequest.getMines())
            .build();
        newGame.start();
        return this.gameRepository.save(newGame);
    }

    public Game findGameById(final String id) {
        return this.gameRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public List<Game> findGamesByUsername(final String username) {
        return this.gameRepository.findByUser(username);
    }

    public Game pause(final String id) {
        Game game = findGameById(id);
        if (game.isGameOver()) {
            throw new RuntimeException("The Game finished, can not update it.");
        }
        game.togglePause();
        return this.gameRepository.save(game);
    }

    public Game recognize(final EventRequest request) {
        Game game = findGameById(request.getGameId());
        if (game.isPaused()) {
            throw new RuntimeException("The Game is paused, can not update it.");
        }
        if (game.isGameOver()) {
            throw new RuntimeException("The Game finished, can not update it.");
        }
        game.recognize(request.getPosX(), request.getPosY());
        return this.gameRepository.save(game);
    }

    public Game flagCell(EventRequest request) {
        Game game = findGameById(request.getGameId());
        if (game.isPaused()) {
            throw new RuntimeException("The Game is paused, can not update it.");
        }
        if (game.isGameOver()) {
            throw new RuntimeException("The Game finished, can not update it.");
        }
        game.flagCell(request.getPosX(), request.getPosY());
        return this.gameRepository.save(game);
    }
}