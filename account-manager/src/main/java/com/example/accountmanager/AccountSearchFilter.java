package com.example.accountmanager;

import java.util.Objects;
import java.util.stream.Stream;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record AccountSearchFilter(
        String surname,
        String givenName,
        String patronymic,

        @Pattern(regexp = "7\\d{10}")
        String phoneNumber,

        @Email
        String email) {

        public boolean isEmpty() {
                return Stream.of(surname, givenName, patronymic, phoneNumber, email).allMatch(Objects::isNull);
        }
}
