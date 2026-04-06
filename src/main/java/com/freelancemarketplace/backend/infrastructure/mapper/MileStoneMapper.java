package com.freelancemarketplace.backend.infrastructure.mapper;

import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.domain.model.MileStoneModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MileStoneMapper {
    MileStoneModel toEntity(MileStoneDTO mileStoneDTO);

    @Mapping(target = "contractId", source = "contract.contractId")
    MileStoneDTO toDto(MileStoneModel mileStoneModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MileStoneModel partialUpdate(MileStoneDTO mileStoneDTO, @MappingTarget MileStoneModel mileStoneModel);

    List<MileStoneDTO> toDTOs(List<MileStoneModel> mileStones);
}