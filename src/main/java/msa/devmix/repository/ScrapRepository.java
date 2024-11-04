package msa.devmix.repository;

import msa.devmix.domain.board.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByUserIdAndBoardId(Long userId, Long boardId);

    void deleteAllByBoardId(Long boardId);
}
