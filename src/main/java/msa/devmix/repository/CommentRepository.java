package msa.devmix.repository;

import msa.devmix.domain.board.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            "SELECT c" +
            " FROM Comment AS c" +
            " JOIN FETCH c.user" +
            " WHERE c.board.id = :boardId" +
            " ORDER BY c.createdAt DESC"
    )
    List<Comment> findByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "JOIN FETCH c.user " +
            "WHERE c.user.id = :userId ")
    Optional<Page<Comment>> findByUserId(@Param("userId") Long userId, Pageable pageable);

    void deleteAllByBoardId(Long boardId);
}
