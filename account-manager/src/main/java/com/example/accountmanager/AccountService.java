package com.example.accountmanager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.accountmanager.persistence.Account;
import com.example.accountmanager.persistence.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public long createAccount(AccountCreationRequest dto) {
        return accountRepository.save(accountMapper.creationRequestToEntity(dto)).getId();
    }

    public Optional<AccountResponse> findAccount(long id) {
        return accountRepository.findById(id).map(accountMapper::entityToResponse);
    }

    public List<AccountResponse> findAccount(AccountSearchFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        var example = Example.of(accountMapper.searchFilterToEntity(filter));

        return accountRepository.findAll(example).stream()
                .map(accountMapper::entityToResponse)
                .collect(Collectors.toList());
    }


    @Mapper(componentModel = ComponentModel.SPRING)
    interface AccountMapper {

        Account creationRequestToEntity(AccountCreationRequest request);

        Account searchFilterToEntity(AccountSearchFilter filter);

        AccountResponse entityToResponse(Account entity);
    }
}
