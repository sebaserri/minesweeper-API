package com.minesweeper.minesweeperapi.controller;

import com.minesweeper.minesweeperapi.model.Game;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@CrossOrigin
@RestController
@RequestMapping("/minesweeper/api")
@Api(value = "Minesweeper API")
public class MinesweeperController {

    public MinesweeperController() {

    }

    @PostMapping("/")
    @ApiOperation(value = "Create a new game", response = Game.class, 
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game created successfully")})
    public Game create() {
        return null;
    }
}
