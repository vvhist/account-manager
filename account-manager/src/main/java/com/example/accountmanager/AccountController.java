package com.example.accountmanager;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public long create(@RequestBody @Valid AccountCreationRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable long id) {
        return ResponseEntity.of(accountService.findAccount(id));
    }

    @PostMapping("/find")
    public List<AccountResponse> find(@RequestBody @Valid AccountSearchFilter request) {
        return accountService.findAccount(request);
    }
}
