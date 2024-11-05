package msa.devmix.repository.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final EntityManager em;

    // Board 조회
    @Override
    public List<BoardQueryDto> findBoards(int pageNumber, int pageSize) {

        return em.createQuery("SELECT new msa.devmix.repository.query.BoardQueryDto(" +
                "b.id, b.title, b.createdBy, b.viewCount,  b.commentCount, b.recruitEndDate)" +
                " FROM Board b", BoardQueryDto.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

    }

    @Override
    public Long countBoards() {

        return em.createQuery("SELECT count(b) FROM Board b", Long.class)
                .getSingleResult();
    }

    @Override
    public Map<Long, List<BoardPositionQueryDto>> findBoardPositionQueryDtos(List<Long> boardIds) {

        List<BoardPositionQueryDto> boardPositionQueryDtos = em.createQuery(
                 " SELECT new msa.devmix.repository.query.BoardPositionQueryDto(" +
                        " bp.board.id, p.positionName, bp.requiredCount, bp.currentCount)" +
                        " FROM BoardPosition bp " +
                        " JOIN bp.position p" +
                        " WHERE bp.board.id IN :boardIds", BoardPositionQueryDto.class)
                .setParameter("boardIds", boardIds)
                .getResultList();

        return boardPositionQueryDtos.stream().collect(Collectors.groupingBy(BoardPositionQueryDto::getBoardId));

    }

    @Override
    public Map<Long, List<BoardTechStackQueryDto>> findBoardTechStackQueryDtos(List<Long> boardIds) {

        List<BoardTechStackQueryDto> boardTechStackQueryDtos = em.createQuery("SELECT new msa.devmix.repository.query.BoardTechStackQueryDto(bt.board.id, t.imageUrl)" +
                        " FROM BoardTechStack bt" +
                        " JOIN bt.techStack t WHERE bt.board.id IN :boardIds", BoardTechStackQueryDto.class)
                .setParameter("boardIds", boardIds)
                .getResultList();

        return boardTechStackQueryDtos.stream().collect(Collectors.groupingBy(BoardTechStackQueryDto::getBoardId));
    }




}
