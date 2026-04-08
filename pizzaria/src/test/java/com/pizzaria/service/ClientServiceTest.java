package com.pizzaria.service;

import com.pizzaria.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void getClientsReturnsEmptyWhenRepositoryHasNothing() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(clientService.getClients().isEmpty());
    }
}
