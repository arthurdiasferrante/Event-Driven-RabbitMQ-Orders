package com.pizzaria.controller;

import com.pizzaria.dto.ClientRequestDTO;
import com.pizzaria.dto.ClientResponseDTO;
import com.pizzaria.model.Client;
import com.pizzaria.repository.ClientRepository;
import com.pizzaria.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// o que isso faz?
// garante que toddo méttodo dentro dessa classe responde a partir desse endereço
@RequestMapping("/clients")
@RestController
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO requestDTO) {
        ClientResponseDTO responseDTO = clientService.createClient(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client newData) {
        Client updatedClient = clientService.updateClient(newData, id);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
