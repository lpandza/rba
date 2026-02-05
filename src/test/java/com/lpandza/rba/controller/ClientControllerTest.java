package com.lpandza.rba.controller;

import com.lpandza.rba.model.Client;
import com.lpandza.rba.model.enums.CardStatus;
import com.lpandza.rba.repository.ClientRepository;
import com.lpandza.rba.request.ClientRequest;
import com.lpandza.rba.restclient.cardapi.service.CardApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @MockitoBean
    private CardApiService cardApiService;


    @Test
    @DisplayName("Should return list of clients when clients exist")
    void shouldReturnListOfClients() throws Exception {
        clientRepository.save(new Client("Ivan", "Horvat", "12345678901", CardStatus.PENDING));
        clientRepository.save(new Client("Marko", "Marić", "12345678902", CardStatus.APPROVED));

        mockMvc.perform(get("/client"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].firstName").value("Ivan"))
               .andExpect(jsonPath("$[0].oib").value("12345678901"))
               .andExpect(jsonPath("$[1].firstName").value("Marko"));
    }

    @Test
    @DisplayName("Should return empty list when no clients exist")
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/client"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should create client successfully with valid data")
    void shouldCreateClientSuccessfully() throws Exception {
        ClientRequest request = new ClientRequest(
                "Ivan",
                "Horvat",
                "12345678901",
                CardStatus.PENDING
        );

        mockMvc.perform(post("/client")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated());

        assert clientRepository.findByOib("12345678901").isPresent();
    }


}
