package com.example.accountmanager;

import java.time.LocalDate;

import com.example.accountmanager.validation.SourceCondition;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@SourceCondition(source = "MAIL", requiredFields = {"givenName", "email"})
@SourceCondition(source = "MOBILE", requiredFields = {"phoneNumber"})
@SourceCondition(source = "BANK", requiredFields = {"bankId", "surname", "givenName", "patronymic", "birthDate", "passportNumber"})
@SourceCondition(source = "GOSUSLUGI", requiredAllFieldsBut = {"email", "residentialAddress"})
public class AccountCreationRequest {

    @Setter
    private CreationSource source;

    @Positive
    private final Long bankId;

    private final String surname;
    private final String givenName;
    private final String patronymic;

    @Past
    private final LocalDate birthDate;
    private final String birthPlace;

    @Pattern(regexp = "\\d{4} \\d{6}")
    private final String passportNumber;

    @Pattern(regexp = "7\\d{10}")
    private final String phoneNumber;

    @Email
    private final String email;
    private final String registeredAddress;
    private final String residentialAddress;
}
