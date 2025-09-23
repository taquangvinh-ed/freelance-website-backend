package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.OrderDTO;
import com.freelancemarketplace.backend.model.OrderModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderModel toEntity(OrderDTO orderDTO);

    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "productId", source = "product.productId")
    OrderDTO toDto(OrderModel orderModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderModel partialUpdate(OrderDTO orderDTO, @MappingTarget OrderModel orderModel);
}