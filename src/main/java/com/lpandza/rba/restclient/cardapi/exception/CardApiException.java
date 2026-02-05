package com.lpandza.rba.restclient.cardapi.exception;

import com.lpandza.rba.restclient.cardapi.response.ErrorResponse;

public class CardApiException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public CardApiException(String message, ErrorResponse errorResponse) {
        super(message);
        this.errorResponse = errorResponse;
    }

}
