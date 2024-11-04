package msa.devmix.repository;

import msa.devmix.domain.board.Apply;
import msa.devmix.domain.board.BoardPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Optional<List<Apply>> findByUserId(Long userId);

    void deleteAllByBoardPositionId(Long boardId);

    void deleteAllByBoardPositionIn(List<BoardPosition> boardPositions);
}
