package com.kcn.clients_api.dao;

import com.kcn.clients_api.entity.Client;

import java.util.List;

public interface ClientDAO {

    List<Client> findAll();
    Client findById(int id);
    Client save(Client client);
    void deleteById(int id);
}
