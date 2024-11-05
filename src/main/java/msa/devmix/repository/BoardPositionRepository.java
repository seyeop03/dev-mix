package msa.devmix.repository;

import msa.devmix.domain.board.BoardPosition;
import msa.devmix.domain.common.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPositionRepository extends JpaRepository<BoardPosition, Long> {

    List<BoardPosition> findByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);

    Optional<BoardPosition> findByBoardIdAndPosition(Long boardId, Position position);
}
