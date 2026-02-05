package com.lpandza.rba.controller;

import com.lpandza.rba.dto.ClientDto;
import com.lpandza.rba.request.ClientRequest;
import com.lpandza.rba.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAll());
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody ClientRequest clientRequest) {
        clientService.save(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{oib}")
    public ResponseEntity<ClientDto> getByOib(@PathVariable String oib) {
        return ResponseEntity.ok(clientService.getClientByOib(oib));
    }

    @PostMapping("/card-requests/{oib}")
    public ResponseEntity<String> sendNewCardRequest(@PathVariable String oib) {
        return ResponseEntity.ok(clientService.sendNewCardRequest(oib));
    }

    @DeleteMapping("/{oib}")
    public ResponseEntity<Void> deleteByOib(@PathVariable String oib) {
        clientService.deleteByOib(oib);
        return ResponseEntity.ok().build();
    }
}
