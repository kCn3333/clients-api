package com.kcn.clients_api.service;

import com.kcn.clients_api.dto.ClientDTO;
import com.kcn.clients_api.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll();
    Client findById(int id);
    Client save(ClientDTO clientDTO);
    Client update(int id, ClientDTO clientDTO);
    Client convertToClient(int id, ClientDTO clientDTO);
    void deleteById(int id);

}
