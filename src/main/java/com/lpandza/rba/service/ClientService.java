package com.lpandza.rba.service;

import com.lpandza.rba.dto.ClientDto;
import com.lpandza.rba.request.ClientRequest;

import java.util.List;

public interface ClientService {
    List<ClientDto> getAll();

    void save(ClientRequest clientRequest);

    ClientDto getClientByOib(String oib);

    void deleteByOib(String oib);

    String sendNewCardRequest(String oib);

}
