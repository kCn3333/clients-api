package com.kcn.clients_api.controller;

import com.kcn.clients_api.dto.ClientDTO;
import com.kcn.clients_api.entity.Client;
import com.kcn.clients_api.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client REST API Endpoints", description = "Operations related to clients.")
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Get all clients", description = "Retrieve a list of all clients.")
    @ApiResponse(responseCode = "200", description = "List of all clients retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not authenticated")
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.findAll();
    }

    @Operation(summary = "Get single client", description = "Retrieve a single client from database.")
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not authenticated")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/{id}")
    public Client getClientById(@Min(1) @PathVariable int id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Create a new client", description = "Add a new client to the database.")
    @ApiResponse(responseCode = "201", description = "Client created successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed (bad input data)")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not authenticated")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "409", description = "Client with same firstName and lastName already exists")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client postClient(@Valid @RequestBody ClientDTO clientDTO) {
        return clientService.save(clientDTO);
    }

    @Operation(summary = "Update an existing client", description = "Update the details of a current client.")
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed (bad input data)")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not authenticated")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @ApiResponse(responseCode = "409", description = "Client with same firstName and lastName already exists")
    @PutMapping("/{id}")
    public Client putClient(@Min(1) @PathVariable int id, @Valid @RequestBody ClientDTO clientDTO) {
        return clientService.update(id, clientDTO);
    }

    @Operation(summary = "Delete a client", description = "Remove a client from the database.")
    @ApiResponse(responseCode = "204", description = "Client successfully deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not authenticated")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@Min(1) @PathVariable int id) {
        clientService.deleteById(id);
    }
}
