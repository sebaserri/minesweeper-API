package com.minesweeper.minesweeperapi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Document
@AllArgsConstructor
@Builder
public class Game {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    private String username;
    private String status;
    private int moves;
    private int rows;
    private int columns;
    private int mines;
    private final List<Cell> cells = new ArrayList<>();
    
    
    public void start() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                this.getCells().add(new Cell(i, j));
            }
        }

        // Let's mix it !! 
        Collections.shuffle(this.getCells());

        // THE MINES!!! :O
        this.getCells().stream().limit(this.getMines()).forEach(cellMine -> cellMine.setMine(true));

        // Sets value for each cell (how many mines has near)
       final Stream<Cell> aStreamOfCells = this.getCells().stream().filter((cell) -> !cell.isMine());
       aStreamOfCells.forEach((cell) -> cell.setValue(this.setTheValue(cell)));
    }

    public void recognize(final int posX, final int posY) {
        final Cell theCell = this.findMeTheCellIn(posX, posY);

        if (theCell.isFlagged()) {
            return;
        }

        this.moves++;

        // Sorry buddy, GAME OVERR!
        if (theCell.isMine()) {
            this.setStatus("GAME OVER");
            return;
        }

        theCell.recognizeMe(this.getCells());

        // NO more cell available! YOU WON!!!!
        if (this.areCellUntouched()) {
            this.status = "YOU WON";
        }
    }

    public void flagCell(final int posX, final int posY) {
        final Cell selectedCell = this.findMeTheCellIn(posX, posY);

        // Flag the clicked cell.
        selectedCell.setFlagged(true);
    }

    public boolean isGameOver() {
        return "YOU WON".equals(this.status) || "GAME OVER".equals(this.status);
    }

    public boolean isPaused() {
        return "PAUSED".equals(this.status);
    }

    public void togglePause() {
        if (this.isPaused()) {
            this.setStatus("RESUME");
        } else {
            this.setStatus("PAUSED");
        }
    }

    private long setTheValue(final Cell aCell) {
        Stream<Cell> filtered = this.getCells().stream().filter(other -> aCell.isAdjacent(other) && other.isMine());
        return filtered.count();
    }

    private Cell findMeTheCellIn(final int posX, final int posY) {
        final Stream<Cell> streamCell = this.getCells().stream().filter(cell -> cell.getPosX() == posX && cell.getPosY() == posY);
        return streamCell.findFirst()
            .orElseThrow(() -> new RuntimeException("Cell not found for the given coordinates"));
    }

    private boolean areCellUntouched() {
        return this.getCells().stream().filter(cell -> !cell.isMine()).allMatch(Cell::isRecognized);
    }
}