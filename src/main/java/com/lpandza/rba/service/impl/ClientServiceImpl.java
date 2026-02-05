package com.lpandza.rba.service.impl;

import com.lpandza.rba.dto.ClientDto;
import com.lpandza.rba.exception.ClientAlreadyExistsException;
import com.lpandza.rba.model.Client;
import com.lpandza.rba.repository.ClientRepository;
import com.lpandza.rba.request.ClientRequest;
import com.lpandza.rba.restclient.cardapi.request.NewCardRequest;
import com.lpandza.rba.restclient.cardapi.service.CardApiService;
import com.lpandza.rba.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final CardApiService cardApiService;

    public ClientServiceImpl(ClientRepository clientRepository, CardApiService cardApiService) {
        this.clientRepository = clientRepository;
        this.cardApiService = cardApiService;
    }

    @Override
    public ClientDto getClientByOib(String oib) {
        log.info("Getting client with oib {}", oib);
        return clientRepository.findByOib(oib)
                               .map(client -> new ClientDto(
                                       client.getFirstName(),
                                       client.getLastName(),
                                       client.getOib(),
                                       client.getCardStatus()
                               ))
                               .orElseThrow(
                                       () -> {
                                           log.info("Client with oib: {} not found", oib);
                                           return new EntityNotFoundException("Client with oib: " + oib + " not found");
                                       });
    }

    @Override
    public List<ClientDto> getAll() {
        log.info("Getting all clients");
        return clientRepository.findAll()
                               .stream().map(client -> new ClientDto(
                        client.getFirstName(),
                        client.getLastName(),
                        client.getOib(),
                        client.getCardStatus()
                ))
                               .toList();
    }

    @Override
    public void save(ClientRequest clientRequest) {
        log.info("Saving new client {} ", clientRequest);
        validateClientDoesNotExist(clientRequest);

        clientRepository.save(new Client(
                clientRequest.firstName(),
                clientRequest.lastName(),
                clientRequest.oib(),
                clientRequest.cardStatus()
        ));

        log.info("Client saved successfully");
    }

    @Override
    public void deleteByOib(String oib) {
        Client client = clientRepository.findByOib(oib)
                                        .orElseThrow(() -> {
                                            log.info("Attempt to delete non-existing client with oib {}", oib);
                                            return new EntityNotFoundException(
                                                    "Client with OIB: " + oib + " not found");
                                        });

        clientRepository.delete(client);
        log.info("Deleted client {}", client);
    }

    @Override
    public String sendNewCardRequest(String oib) {
        ClientDto clientDto = getClientByOib(oib);

        return cardApiService.createNewCard(new NewCardRequest(
                clientDto.firstName(),
                clientDto.lastName(),
                clientDto.oib(),
                clientDto.cardStatus()
        ));
    }

    private void validateClientDoesNotExist(ClientRequest clientRequest) {
        clientRepository.findByOib(clientRequest.oib())
                        .ifPresent(client -> {
                            log.warn("Attempt to create client with existing oib {}", clientRequest.oib());
                            throw new ClientAlreadyExistsException(clientRequest.oib());
                        });
    }
}
