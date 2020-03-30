package com.minesweeper.minesweeperapi.controller.request;

import lombok.Data;

@Data
public class EventRequest {
    private String gameId;
    private int posX;
    private int posY;
}
