package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.WeeklyReportDTO;
import com.freelancemarketplace.backend.dto.WeeklyReportItemDTO;
import com.freelancemarketplace.backend.model.WeeklyReportItemModel;
import com.freelancemarketplace.backend.model.WeeklyReportModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeeklyReportMapper {
    WeeklyReportModel toEntity(WeeklyReportDTO weeklyReportDTO);

    @AfterMapping
    default void linkItems(@MappingTarget WeeklyReportModel weeklyReportModel) {
        weeklyReportModel.getItems().forEach(item -> item.setWeeklyReport(weeklyReportModel));
    }

    WeeklyReportItemDTO itemToDto(WeeklyReportItemModel item);

    WeeklyReportDTO toDto(WeeklyReportModel weeklyReportModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    WeeklyReportModel partialUpdate(WeeklyReportDTO weeklyReportDTO, @MappingTarget WeeklyReportModel weeklyReportModel);

    List<WeeklyReportDTO> toDTOs(List<WeeklyReportModel> models);
}