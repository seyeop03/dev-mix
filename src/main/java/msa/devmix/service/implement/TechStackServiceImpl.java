package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import msa.devmix.domain.common.TechStack;
import msa.devmix.dto.TechStackDto;
import msa.devmix.repository.PositionRepository;
import msa.devmix.repository.PositionTechStackRepository;
import msa.devmix.repository.TechStackRepository;
import msa.devmix.service.TechStackService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TechStackServiceImpl implements TechStackService {

    private final TechStackRepository techStackRepository;
    private final PositionTechStackRepository positionTechStackRepository;

    @Override
    public List<TechStackDto> getTechStacksViaPositionName(String positionName) {

        if (Strings.isEmpty(positionName)) {
            return techStackRepository.findAll()
                    .stream()
                    .map(TechStackDto::from)
                    .toList();
        }

        return positionTechStackRepository.findTechStacksByPositionName(positionName);
    }
}
