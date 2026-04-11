package com.freelancemarketplace.backend.client.api.controller;

import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.freelancemarketplace.backend.client.application.service.ClientService;
import com.freelancemarketplace.backend.client.dto.ClientDTO;

@RestController
@RequestMapping(path = "/api/clients", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ApiResponse<?> createClient(@RequestBody ClientDTO clientDTO){
        ClientDTO newClient = clientService.createClient(clientDTO);
        return ApiResponse.created(newClient);
    }

    @PutMapping("/{clientId}")
    public ApiResponse<?> updateClient(@PathVariable Long clientId,
                                                   @RequestBody ClientDTO clientDTO){
        ClientDTO updatedClient = clientService.updateClient(clientId,clientDTO);
        return ApiResponse.success(updatedClient);
    }

    @DeleteMapping("/{clientId}")
    public ApiResponse<?> deleteClient(@PathVariable Long clientId
                                                   ){
        clientService.deleteClient(clientId);
        return ApiResponse.noContent();
    }


}
