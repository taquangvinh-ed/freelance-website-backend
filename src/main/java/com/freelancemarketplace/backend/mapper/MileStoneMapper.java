package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.model.MileStoneModel;
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