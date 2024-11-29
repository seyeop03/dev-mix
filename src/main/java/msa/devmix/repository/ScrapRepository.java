package msa.devmix.repository;

import msa.devmix.domain.board.Scrap;
import msa.devmix.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByUserIdAndBoardId(Long userId, Long boardId);

    void deleteAllByBoardId(Long boardId);

    @Query("SELECT s FROM Scrap s WHERE s.board.id IN :boardIds AND s.user = :user")
    List<Scrap> findByBoardIdInAndUser(List<Long> boardIds, User user);
}
