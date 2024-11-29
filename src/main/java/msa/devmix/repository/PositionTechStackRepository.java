package msa.devmix.repository;

import msa.devmix.domain.common.PositionTechStack;
import msa.devmix.dto.TechStackDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionTechStackRepository extends JpaRepository<PositionTechStack, Long> {

    @Query("SELECT new msa.devmix.dto.TechStackDto(ts.techStackName, ts.imageUrl) " +
            "FROM PositionTechStack pts " +
            "JOIN pts.position p " +
            "JOIN pts.techStack ts " +
            "WHERE p.positionName = :positionName")
    List<TechStackDto> findTechStacksByPositionName(@Param("positionName") String positionName);
}
