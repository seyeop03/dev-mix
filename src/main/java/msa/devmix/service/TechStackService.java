package msa.devmix.service;

import msa.devmix.dto.TechStackDto;

import java.util.List;

public interface TechStackService {

    List<TechStackDto> getTechStacksViaPositionName(String positionName);
}
