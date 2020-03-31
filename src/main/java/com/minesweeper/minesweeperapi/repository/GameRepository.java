package com.minesweeper.minesweeperapi.repository;

import java.util.List;

import com.minesweeper.minesweeperapi.model.Game;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * GameRepository
 */
@Repository
public interface GameRepository extends CrudRepository<Game, String> {
    @Query("{'username': ?0}")
    List<Game> findByUser(final String username);
}