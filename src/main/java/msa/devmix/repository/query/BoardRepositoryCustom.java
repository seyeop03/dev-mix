package msa.devmix.repository.query;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardQueryDto> findBoardWithUserQueryDtos();
    List<BoardQueryDto> findBoardByUserQueryDtos(List<BoardQueryDto> result);
}
