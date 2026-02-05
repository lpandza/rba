package com.lpandza.rba.service;

import com.lpandza.rba.dto.ClientDto;
import com.lpandza.rba.model.Client;
import com.lpandza.rba.model.enums.CardStatus;
import com.lpandza.rba.repository.ClientRepository;
import com.lpandza.rba.request.ClientRequest;
import com.lpandza.rba.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client testClient;
    private ClientRequest testRequest;

    @BeforeEach
    void setUp() {
        testClient = new Client(1L, "Ivan", "Horvat", "12345678901", CardStatus.PENDING);
        testRequest = new ClientRequest("Ivan", "Horvat", "12345678901", CardStatus.PENDING);
    }

    @Nested
    @DisplayName("getAll()")
    class GetAllTests {

        @Test
        @DisplayName("should return list of clients when clients exist")
        void shouldReturnListOfClients() {
            Client client2 = new Client(2L, "Marko", "Marić", "12345678902", CardStatus.APPROVED);
            when(clientRepository.findAll()).thenReturn(List.of(testClient, client2));

            List<ClientDto> result = clientService.getAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).firstName()).isEqualTo("Ivan");
            assertThat(result.get(1).firstName()).isEqualTo("Marko");
            verify(clientRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("should return empty list when no clients exist")
        void shouldReturnEmptyList() {
            when(clientRepository.findAll()).thenReturn(Collections.emptyList());

            List<ClientDto> result = clientService.getAll();

            assertThat(result).isEmpty();
            verify(clientRepository, times(1)).findAll();
        }
    }
}
