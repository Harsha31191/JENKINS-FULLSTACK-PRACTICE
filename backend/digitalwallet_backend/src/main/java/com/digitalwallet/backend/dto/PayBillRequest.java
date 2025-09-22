package com.digitalwallet.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PayBillRequest {
    @NotBlank
    private String biller;
    @NotBlank
    private String account;
    @NotNull
    private BigDecimal amount;

    public String getBiller() { return biller; }
    public void setBiller(String biller) { this.biller = biller; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
