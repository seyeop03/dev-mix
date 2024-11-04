package msa.devmix.repository;

import msa.devmix.domain.common.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByPositionName(String positionName);

    List<Position> findByPositionNameIn(List<String> positionNames);

    boolean existsByPositionNameIn(List<String> positionList);
}
