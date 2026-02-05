package com.lpandza.rba.restclient.cardapi.request;

import com.lpandza.rba.model.enums.CardStatus;

public record NewCardRequest(String firstName,
                             String lastName,
                             String oib,
                             CardStatus cardStatus) {
}
