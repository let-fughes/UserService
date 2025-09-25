package com.kirylliuss.shop.dto.respons;

import java.time.LocalDate;

public class CardRespons {

    private Long id;
    private Long userId;
    private String number;
    private String holder;
    private LocalDate expirationDate;

    public CardRespons() {}

    public CardRespons(
            Long id, Long userId, String number, String holder, LocalDate expirationDate) {
        this.id = id;
        this.userId = userId;
        this.holder = holder;
        this.number = number;
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
