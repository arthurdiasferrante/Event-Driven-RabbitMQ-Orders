package com.pizzaria.service;

import com.pizzaria.dto.ClientRequestDTO;
import com.pizzaria.dto.ClientResponseDTO;
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

    public ClientResponseDTO createClient(ClientRequestDTO requestDTO) {
        Client clientEntity = new Client();

        clientEntity.setName(requestDTO.name());
        clientEntity.setAddress(requestDTO.address());

        Client savedClient = clientRepository.save(clientEntity)
        return new ClientResponseDTO(savedClient.getId(), savedClient.getName(), savedClient.getAddress());
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
