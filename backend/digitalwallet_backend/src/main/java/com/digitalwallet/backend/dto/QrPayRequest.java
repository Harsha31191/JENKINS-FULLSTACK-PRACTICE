package com.digitalwallet.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class QrPayRequest {
    @NotBlank
    private String code;
    @NotNull
    private BigDecimal amount;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
