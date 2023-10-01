package com.example.accountmanager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import com.example.accountmanager.AccountService.AccountMapper;
import com.example.accountmanager.persistence.Account;
import com.example.accountmanager.persistence.AccountRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Captor
    private ArgumentCaptor<Example<Account>> exampleAccountCaptor;

    @Test
    void whenValidSearchFilter_thenFindsByFilter() {
        accountService.findAccount(new AccountSearchFilter("Smith", "John", "David", "71234567890", "jsmith@gmail.com"));

        verify(accountRepository, times(1)).findAll(exampleAccountCaptor.capture());
        assertThat(exampleAccountCaptor.getValue().getProbe().getSurname()).isEqualTo("Smith");
    }

    @Test
    void whenEmptySearchFilter_thenReturnsEmptyList() {
        accountService.findAccount(new AccountSearchFilter(null, null, null, null, null));

        verify(accountMapper, times(0)).searchFilterToEntity(any());
        verify(accountRepository, times(0)).findAll(ArgumentMatchers.<Example<Account>>any());
    }
}
