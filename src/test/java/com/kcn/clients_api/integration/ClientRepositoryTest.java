package com.kcn.clients_api.integration;

import com.kcn.clients_api.entity.Client;
import com.kcn.clients_api.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Test
    void shouldFindByLastName() {
        List<Client> results = repository.findByLastName("Messi");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("lionel.messi@fcbarcelona.com");
    }
}
