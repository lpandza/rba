package com.lpandza.rba.restclient.cardapi.service;

import com.lpandza.rba.restclient.cardapi.request.NewCardRequest;

public interface CardApiService {
    String createNewCard(NewCardRequest request);
}
