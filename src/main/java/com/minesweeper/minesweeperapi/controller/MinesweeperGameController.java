package com.minesweeper.minesweeperapi.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(value = "Minesweeper API")
public class MinesweeperGameController {

    @GetMapping("/")
    public Demo get() {
        return new Demo("1", "Sebastian");
    }
}
