package com.example.accountmanager.persistence;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bankId;
    private String surname;
    private String givenName;
    private String patronymic;
    private LocalDate birthDate;
    private String birthPlace;
    private String passportNumber;
    private String phoneNumber;
    private String email;
    private String registeredAddress;
    private String residentialAddress;
}
