package com.minesweeper.minesweeperapi.service;

import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import com.minesweeper.minesweeperapi.controller.request.CreateRequest;
import com.minesweeper.minesweeperapi.controller.request.EventRequest;
import com.minesweeper.minesweeperapi.model.Game;
import com.minesweeper.minesweeperapi.repository.GameRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * GameServiceTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @MockBean
    private GameRepository gameRepository;

    private GameService gameService;

    private Game gameCreated;
    private Game gameFound;

    @Before
    public void setup() {
        this.gameCreated = Game.builder()
            .id("abc-def-1234")
            .created(LocalDateTime.now())
            .status("NEW")
            .username("Sebastian")
            .rows(3)
            .columns(4)
            .mines(10)
            .build();

        this.gameFound = Game.builder()
            .id("qwerty-234")
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .status("NEW")
            .username("Sebastian")
            .rows(8)
            .columns(5)
            .mines(10)
            .build();    
    }

    @Test
    public void createGame_ok() {
        this.gameService = new GameService(gameRepository);

        CreateRequest request = new CreateRequest();
        request.setUsername("Sebastian");
        request.setColumns(4);
        request.setRows(3);
        request.setMines(10);

        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(this.gameCreated);
        Game newGame = this.gameService.create(request);

        assertNotNull(newGame);
        assertEquals(request.getRows(), newGame.getRows());
        assertEquals(request.getUsername(), newGame.getUsername());
    }

    @Test
    public void findGameById_should_find_id_ok() {
        this.gameService = new GameService(gameRepository);

        final String theID = "qwerty-234";
        Optional<Game> optionalGame = Optional.of(this.gameFound);
        Mockito.when(gameRepository.findById(theID)).thenReturn(optionalGame);

        Game aGame = this.gameService.findGameById(theID);
        assertNotNull(aGame);
        assertEquals(aGame.getId(), this.gameFound.getId());
    }

    @Test
    public void findGameById_should_fail_not_valid_id() {
        this.gameService = new GameService(gameRepository);

        final String theID = "qwerty-234";
        Exception ex = new RuntimeException("Game not found");
        Mockito.when(gameRepository.findById(theID)).thenThrow(ex);

        Game aGame = null;
        try {
            aGame = this.gameService.findGameById(theID);
        } catch (Exception e) {
            assertNull(aGame);
            assertEquals(e.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void findGamesByUsername_should_find_a_username_ok() {
        this.gameService = new GameService(gameRepository);

        final String me = "Sebastian";
        List<Game> games = new ArrayList<>();
        games.add(this.gameFound);
    
        Mockito.when(gameRepository.findByUser(me)).thenReturn(games);

        List<Game> gamesFound = this.gameService.findGamesByUsername(me);
        assertNotNull(gamesFound);
        assertEquals(gamesFound.get(0).getUsername(), this.gameFound.getUsername());
    }

    @Test
    public void pause_ok() {
        this.gameService = new GameService(gameRepository);

        final String theID = "qwerty-234";
        Optional<Game> optionalGame = Optional.of(this.gameFound);
        Mockito.when(this.gameRepository.findById(theID)).thenReturn(optionalGame);

        Game gameDB = Game.builder()
            .id("qwerty-234")
            .updated(LocalDateTime.now())
            .status("PAUSED")
            .username("Sebastian")
            .rows(3)
            .columns(4)
            .mines(10)
            .build();
        Mockito.when(this.gameRepository.save(any(Game.class))).thenReturn(gameDB);
        
        Game gameSaved = this.gameService.pause(theID);
        assertNotNull(gameSaved);
        assertEquals(gameDB.getStatus(), gameSaved.getStatus());
    }

    @Test
    public void discover_ok() {
        final String theID = "qwerty-234";
        this.gameService = new GameService(gameRepository);

        this.gameFound.start();

        Optional<Game> optionalGame = Optional.of(this.gameFound);
        Mockito.when(gameRepository.findById(theID)).thenReturn(optionalGame);

        Game gameDB = Game.builder()
            .id("qwerty-234")
            .updated(LocalDateTime.now())
            .status("RESUME")
            .username("Sebastian")
            .moves(10)
            .rows(3)
            .columns(4)
            .mines(10)
            .build();

        Mockito.when(this.gameRepository.save(any(Game.class))).thenReturn(gameDB);

        final EventRequest request = new EventRequest();
        request.setGameId(theID);
        request.setPosX(2);
        request.setPosY (3);
        
        Game gameDiscover = this.gameService.discoverCell(request);
        assertNotNull(gameDiscover);
        assertEquals(gameDB.getId(), gameDiscover.getId());
    }
}