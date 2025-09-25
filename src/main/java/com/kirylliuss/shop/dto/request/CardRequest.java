package com.kirylliuss.shop.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class CardRequest {

    @NotNull(message = "UserId is mandatory")
    private Long userId;

    @NotBlank(message = "Card number is mandatory")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    private String number;

    @NotBlank(message = "Card holder is mandatory")
    @Size(max = 100, message = "Holder name must be less than 100 characters")
    private String holder;

    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    public CardRequest() {}

    public CardRequest(Long userId, String number, String holder, LocalDate expirationDate) {
        this.userId = userId;
        this.number = number;
        this.holder = holder;
        this.expirationDate = expirationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
