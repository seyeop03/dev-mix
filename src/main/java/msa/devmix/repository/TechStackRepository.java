package msa.devmix.repository;

import msa.devmix.domain.common.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    TechStack findByTechStackName(String techStackName);

    List<TechStack> findByTechStackNameIn(List<String> techStackNames);

    boolean existsByTechStackNameIn(List<String> techStackNames);
}
