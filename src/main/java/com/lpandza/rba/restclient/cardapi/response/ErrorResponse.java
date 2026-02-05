package com.lpandza.rba.restclient.cardapi.response;

public record ErrorResponse(
        String code,
        String id,
        String description
) {
}
