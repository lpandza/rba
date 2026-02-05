package com.lpandza.rba.dto;

import com.lpandza.rba.model.enums.CardStatus;

public record ClientDto(
        String firstName,
        String lastName,
        String oib,
        CardStatus cardStatus
) {
}
