package com.pizzaria.controller;

import com.pizzaria.model.Client;
import com.pizzaria.repository.ClientRepository;
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

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        Client newClient = clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        var clientEntity = clientRepository.findById(id);

        if (clientEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Client client = clientEntity.get();
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        var clientEntity = clientRepository.findById(id);
        if (clientEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client newData) {
        var clientEntity = clientRepository.findById(id);
        if (clientEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Client newClient = clientEntity.get();
        newClient.setName(newData.getName());
        newClient.setAddress(newData.getAddress());

        clientRepository.save(newClient);

        return ResponseEntity.ok(newClient);
    }


}
