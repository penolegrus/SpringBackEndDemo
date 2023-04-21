package repo;

import db_models.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query(value = "select g.*\n" +
            "from user_games ug\n" +
            "inner join game g\n" +
            "on ug.games_game_id = g.game_id\n" +
            "where g.game_id = :gameId\n" +
            "and ug.user_id = :userId", nativeQuery = true)
    Game getGameByUserId(@Param("userId") Long userId, @Param("gameId") Long gameId);
}
