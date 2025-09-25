package com.kirylliuss.shop.dto.respons;

import java.time.LocalDate;
import java.util.List;

public class UserRespons {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private List<CardRespons> cards;

    public UserRespons() {}

    public UserRespons(
            Long id,
            String name,
            String surname,
            LocalDate birthDate,
            String email,
            List<CardRespons> cards) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.cards = cards;
    }

    public List<CardRespons> getCards() {
        return cards;
    }

    public void setCards(List<CardRespons> cards) {
        this.cards = cards;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
