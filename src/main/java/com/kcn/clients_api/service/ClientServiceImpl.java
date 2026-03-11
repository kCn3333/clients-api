package com.kcn.clients_api.service;

import com.kcn.clients_api.dto.ClientDTO;
import com.kcn.clients_api.entity.Client;
import com.kcn.clients_api.exception.ClientNotFoundException;
import com.kcn.clients_api.exception.InvalidClientDataException;
import com.kcn.clients_api.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(int id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));
    }

    @Transactional
    @Override
    public Client save(ClientDTO clientDTO) {
        validateClient(clientDTO, 0); // 0 for new Client
        return clientRepository.save(convertToClient(0, clientDTO));
    }

    @Transactional
    @Override
    public Client update(int id, ClientDTO clientDTO) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        validateClient(clientDTO, id);
        return clientRepository.save(convertToClient(id, clientDTO));
    }

    @Override
    public Client convertToClient(int id, ClientDTO clientDTO) {
        return new Client(id, clientDTO.getFirstName(), clientDTO.getLastName(), clientDTO.getEmail());
    }

    private void validateClient(ClientDTO clientDTO, int id) {
        List<String> errors = new ArrayList<>();

        if (clientDTO.getFirstName() == null || clientDTO.getFirstName().isBlank()) {
            errors.add("firstName must not be blank");
        }
        if (clientDTO.getLastName() == null || clientDTO.getLastName().isBlank()) {
            errors.add("lastName must not be blank");
        }

        boolean exists;
        if (id == 0) { // save
            exists = clientRepository.existsByFirstNameAndLastName(clientDTO.getFirstName(), clientDTO.getLastName());
        } else { // update
            exists = clientRepository.existsByFirstNameAndLastNameAndIdNot(clientDTO.getFirstName(), clientDTO.getLastName(), id);
        }

        if (exists) {
            errors.add("Client with same firstName and lastName already exists");
        }

        if (!errors.isEmpty()) {
            throw new InvalidClientDataException(errors);
        }
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        clientRepository.deleteById(id);
    }
}
