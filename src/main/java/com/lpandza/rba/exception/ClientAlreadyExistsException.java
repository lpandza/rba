package com.lpandza.rba.exception;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String oib) {
        super("Client with OIB " + oib + " already exists");
    }
}
