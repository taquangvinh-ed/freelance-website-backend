package com.freelancemarketplace.backend.contract.infrastructure.mapper;

import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.contract.domain.model.MileStoneModel;

public interface MileStoneMapperInterface {

    MileStoneDTO toDto(MileStoneModel mileStoneModel);

    MileStoneModel toEntity(MileStoneDTO mileStoneDTO);
}
