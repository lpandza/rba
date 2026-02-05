package com.lpandza.rba.restclient.cardapi.service.impl;

import com.lpandza.rba.restclient.cardapi.exception.CardApiException;
import com.lpandza.rba.restclient.cardapi.request.NewCardRequest;
import com.lpandza.rba.restclient.cardapi.response.ErrorResponse;
import com.lpandza.rba.restclient.cardapi.response.SuccessResponse;
import com.lpandza.rba.restclient.cardapi.service.CardApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class CardApiServiceImpl implements CardApiService {
    private static final String NEW_CARD_PATH = "card-request";
    private static final String VERSION = "v1";


    private final RestClient restClient;

    public CardApiServiceImpl(@Value("${external-api.card-api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public String createNewCard(NewCardRequest request) {
        try {

            SuccessResponse response =
                    restClient.post()
                              .uri(uriBuilder -> uriBuilder
                                      .pathSegment(NEW_CARD_PATH, VERSION)
                                      .build()).contentType(MediaType.APPLICATION_JSON)
                              .accept(MediaType.APPLICATION_JSON)
                              .body(request)
                              .retrieve()
                              .onStatus(
                                      HttpStatusCode::isError, (req, res) -> {
                                          ErrorResponse error = new ObjectMapper().readValue(
                                                  res.getBody(), ErrorResponse.class);

                                          throw new CardApiException(error.description(), error);
                                      }
                              )
                              .body(SuccessResponse.class);

            log.info("Card request sent for OIB: {}", request.oib());
            assert response != null;
            return response.message();

        } catch (RestClientException e) {
            log.error("Unexpected error calling Card API: {}", e.getMessage(), e);
            throw new CardApiException("Unexpected error: " + e.getMessage(), null);
        }
    }
}
