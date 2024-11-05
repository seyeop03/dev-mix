package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import msa.devmix.domain.common.Position;
import msa.devmix.dto.PositionDto;
import msa.devmix.repository.PositionRepository;
import msa.devmix.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;


    @Override
    public List<PositionDto> getPositions() {

        List<Position> positions = positionRepository.findAll();

        List<PositionDto> positionDtos = positions.stream()
                .map(PositionDto::from)
                .toList();

        return positionDtos;
    }
}
