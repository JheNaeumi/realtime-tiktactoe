package com.example.TikTacToe.repository;



import com.example.TikTacToe.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game,Long> {

    @Query(value = "SELECT * FROM game WHERE game_id = :gameId", nativeQuery = true)
    Optional<Game> findByGameId(@Param("gameId") String gameId);
}
