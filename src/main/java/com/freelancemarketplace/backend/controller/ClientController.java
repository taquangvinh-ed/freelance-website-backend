package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/clients", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO>createClient(@RequestBody ClientDTO clientDTO){
        ClientDTO newClient = clientService.createClient(clientDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newClient
                ));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ResponseDTO>updateClient(@PathVariable Long clientId,
                                                   @RequestBody ClientDTO clientDTO){
        ClientDTO updatedClient = clientService.updateClient(clientId,clientDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedClient
                ));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<ResponseDTO>deleteClient(@PathVariable Long clientId
                                                   ){
        clientService.deleteClient(clientId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                ));
    }


}
