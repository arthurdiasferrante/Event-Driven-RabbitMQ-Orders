package com.pizzaria.controller;

import com.pizzaria.model.Client;
import com.pizzaria.repository.ClientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// o que isso faz?
// garante que toddo méttodo dentro dessa classe responde a partir desse endereço
@RequestMapping("/clients")
@RestController
public class ClientController {

    private ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public Client saveClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @GetMapping
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return "Client " + id + " Deleted";
    }

    @PutMapping("/{id}")
    public String updateClient(@PathVariable Long id, @RequestBody Client newData) {
        var clientBox = clientRepository.findById(id);
        if (clientBox.isEmpty()) {
            return "Client not found";
        }

        Client client = clientBox.get();
        client.setName(newData.getName());
        client.setAddress(newData.getAddress());

        clientRepository.save(client);

        return "User with ID " + id + " updated successfully!";
    }


}
