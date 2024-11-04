package msa.devmix.repository.query;

import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    @Override
    public List<BoardQueryDto> findBoardWithUserQueryDtos() {
        return List.of();
    }

    @Override
    public List<BoardQueryDto> findBoardByUserQueryDtos(List<BoardQueryDto> result) {
        return List.of();
    }
}
