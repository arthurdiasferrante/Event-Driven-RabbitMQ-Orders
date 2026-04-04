package com.pizzaria.service;

import com.pizzaria.exception.ClientNotFoundException;
import com.pizzaria.model.Client;
import com.pizzaria.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client clientBody) {
        return clientRepository.save(clientBody);
    }

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com o ID " + id));
    }

    public Client updateClient(Client clientBody, Long id) {
        Client clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com o ID " + id));

        clientEntity.setAddress(clientBody.getAddress());
        clientEntity.setName(clientBody.getName());

        return clientRepository.save(clientEntity);
    }

    public void deleteClient(Long id) {
        Client clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com o ID " + id));

        clientRepository.delete(clientEntity);
    }
}
