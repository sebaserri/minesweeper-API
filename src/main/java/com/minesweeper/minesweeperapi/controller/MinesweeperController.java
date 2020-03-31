package com.minesweeper.minesweeperapi.controller;

import java.util.List;

import com.minesweeper.minesweeperapi.controller.request.CreateRequest;
import com.minesweeper.minesweeperapi.controller.request.EventRequest;
import com.minesweeper.minesweeperapi.model.Game;
import com.minesweeper.minesweeperapi.service.GameService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@CrossOrigin
@RestController
@RequestMapping("/minesweeper/api")
@Api(value = "Minesweeper API")
public class MinesweeperController {

    private final GameService gameService;

    public MinesweeperController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = "/load/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Load a game by Id", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Loaded successfully") })
    public Game loadGame(@PathVariable final String id) {
        try {
            return this.gameService.findGameById(id);
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/load/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return all the games by username", response = Game.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Games retrieved successfully") })
    public List<Game> getGamesByUser(@PathVariable final String username) {
        try {
            return this.gameService.findGamesByUsername(username);
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new game", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Game created successfully") })
    public Game create(@RequestBody final CreateRequest aRequest) {
        try {
            return this.gameService.create(aRequest);
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping(value = "/recognize", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Recognize a cell by GameId and X&Y position", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Game updated successfully") })
    public Game recognize(@RequestBody final EventRequest request) {
        try {
            return this.gameService.recognize(request);
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping(value = "/flag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Flag a cell by game ID given X&Y position", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Game updated successfully") })
    public Game flagCell(@RequestBody final EventRequest request) {
        try {
            return this.gameService.flagCell(request);
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping(value = "/pause/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Pause or Resume a game by ID", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Game updated successfully") })
    public Game pauseGame(@PathVariable final String id) {
        try {
            return this.gameService.pause(id);
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
