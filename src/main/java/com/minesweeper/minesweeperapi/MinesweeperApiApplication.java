package com.minesweeper.minesweeperapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class MinesweeperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinesweeperApiApplication.class, args);
	}

}
