package com.example.accountmanager;

import java.time.LocalDate;

public record AccountResponse(
        Long id,
        Long bankId,
        String surname,
        String givenName,
        String patronymic,
        LocalDate birthDate,
        String birthPlace,
        String passportNumber,
        String phoneNumber,
        String email,
        String registeredAddress,
        String residentialAddress) {
}
