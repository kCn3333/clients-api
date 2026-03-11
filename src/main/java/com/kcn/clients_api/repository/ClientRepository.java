package com.kcn.clients_api.repository;

import com.kcn.clients_api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByFirstNameAndLastNameAndIdNot(String firstName, String lastName, int id);
    List<Client> findByLastName(String lastName);

}
