package com.minesweeper.minesweeperapi.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Cell {
    private int posX;
    private int posY;
    private boolean discovered;
    private boolean mine;
    private boolean flagged;
    private long value;
    private int index;

    public Cell(final int posX, final int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void discovered() {
        this.discovered = true;
    }

    public boolean isAdjacent(final Cell other) {
        boolean x = Math.abs(this.getPosX() - other.getPosX()) <= 1;
        boolean y = Math.abs(this.getPosY() - other.getPosY()) <= 1;
        return this != other && x && y;
    }

    // I need to see cell without mines. So I call again to continue discover cells.
    public void discoverMe(final List<Cell> cells) {
        this.discovered();
        List<Cell> adjacentCells = cells.stream()
                .filter(cell -> this.isAdjacent(cell) && cell.canBeDiscoveredAutomatically())
                .collect(Collectors.toList());

        if (adjacentCells.size() > 0) {
            adjacentCells.forEach(cell -> cell.discoverMe(cells));
        }
    }

    private boolean canBeDiscoveredAutomatically() {
        return !this.isDiscovered() && !this.isMine() && !this.isFlagged() && this.getValue() == 0;
    }
}