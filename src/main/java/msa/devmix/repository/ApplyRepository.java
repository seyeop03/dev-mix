package msa.devmix.repository;

import msa.devmix.domain.board.Apply;
import msa.devmix.domain.board.BoardPosition;
import msa.devmix.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Optional<List<Apply>> findByUserId(Long userId);

    void deleteAllByBoardPositionId(Long boardId);

    void deleteAllByBoardPositionIn(List<BoardPosition> boardPositions);

    @Query("SELECT a FROM Apply a JOIN FETCH a.user JOIN FETCH a.boardPosition WHERE a.user = :user")
    Optional<List<Apply>> findByUser(@Param("user") User user);

    @Query("SELECT a FROM Apply a JOIN FETCH a.user WHERE a.boardPosition IN :boardPositions")
    List<Apply> findByBoardPositionIn(@Param("boardPositions") List<BoardPosition> boardPositions);
}
