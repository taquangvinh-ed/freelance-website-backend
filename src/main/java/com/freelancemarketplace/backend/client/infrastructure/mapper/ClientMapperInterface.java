package com.freelancemarketplace.backend.client.infrastructure.mapper;

import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.dto.ClientDTO;

public interface ClientMapperInterface {

    ClientDTO toDto(ClientModel clientModel);

    ClientModel toEntity(ClientDTO clientDTO);
}
