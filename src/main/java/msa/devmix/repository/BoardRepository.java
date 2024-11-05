package msa.devmix.repository;

import msa.devmix.domain.board.Board;
import msa.devmix.repository.query.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Optional<Board> findByIdAndUserId(Long id, Long userId);

    Optional<List<Board>> findByUserId(Long userId, Pageable pageable);
}
