package com.kcn.clients_api.dao;

import com.kcn.clients_api.entity.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDAOJpaImpl implements ClientDAO{

    private EntityManager entityManager;

    public ClientDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Client> findAll() {

        // create a query
        TypedQuery<Client> query=entityManager.createQuery("from Client", Client.class);

        // execute query and get results list
        return query.getResultList();
    }

    @Override
    public Client findById(int id) {
        return entityManager.find(Client.class,id);
    }

    @Override
    public Client save(Client client) {
        return entityManager.merge(client);
    }

    @Override
    public void deleteById(int id) {
        Client clientToRemove=entityManager.find(Client.class,id);
        entityManager.remove(clientToRemove);
    }
}
