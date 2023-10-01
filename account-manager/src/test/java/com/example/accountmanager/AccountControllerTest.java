package com.example.accountmanager;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Captor
    private ArgumentCaptor<AccountCreationRequest> creationRequestCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Captor
    private ArgumentCaptor<AccountSearchFilter> searchFilterCaptor;

    @Test
    void whenMissingSource_thenReturns400() throws Exception {
        var validRequest = new AccountCreationRequest(
                1453L, "Smith", "John", "David", LocalDate.of(1970, 1, 1), "London", "1100 555666", "71234567890",
                "jsmith@gmail.com", "London SW1P 3PA", "London WC1B 3DG");

        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(0)).createAccount(creationRequestCaptor.capture());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/account-creation-requests.csv", numLinesToSkip = 1)
    void whenInputFromSource_thenReturnsStatus(int status, String source,
                                               @AggregateWith(CreationRequestAggregator.class) AccountCreationRequest request) throws Exception {
        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-Source", source)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(status));

        verify(accountService, times(status == 200 ? 1 : 0)).createAccount(creationRequestCaptor.capture());
    }

    @Test
    void whenPresentAccountId_thenFindsById() throws Exception {
        mockMvc.perform(get("/accounts/find/{id}", "1"));

        verify(accountService, times(1)).findAccount(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(1);
    }

    @Test
    void whenMissingAccountId_thenReturns404() throws Exception {
        mockMvc.perform(get("/accounts/find/{id}", "")).andExpect(status().isNotFound());

        verify(accountService, times(0)).findAccount(any());
    }

    @Test
    void whenValidSearchFilter_thenFindsByFilter() throws Exception {
        var validSearchFilter = new AccountSearchFilter("Smith", "John", "David", "71234567890", "jsmith@gmail.com");

        mockMvc.perform(post("/accounts/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSearchFilter)));

        verify(accountService, times(1)).findAccount(searchFilterCaptor.capture());
    }

    @Test
    void whenInvalidSearchFilter_thenReturns400() throws Exception {
        var invalidSearchFilter = new AccountSearchFilter("Smith", "John", "David", "+71234567890", "jsmith_gmail.com");

        mockMvc.perform(post("/accounts/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSearchFilter)))
                .andExpect(status().isBadRequest());
        verify(accountService, times(0)).findAccount(any());
    }


    private static class CreationRequestAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new AccountCreationRequest(
                    accessor.getLong(2),
                    accessor.getString(3),
                    accessor.getString(4),
                    accessor.getString(5),
                    accessor.get(6, LocalDate.class),
                    accessor.getString(7),
                    accessor.getString(8),
                    accessor.getString(9),
                    accessor.getString(10),
                    accessor.getString(11),
                    accessor.getString(12));
        }
    }
}
