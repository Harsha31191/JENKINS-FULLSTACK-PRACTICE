package com.digitalwallet.backend.dto;

import com.digitalwallet.backend.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TxResponse {
    private UUID id;
    private String type;
    private BigDecimal amount;
    private String currency;
    private String status;
    private Instant createdAt;
    private String metadata;

    public TxResponse(Transaction t) {
        this.id = t.getId();
        this.type = t.getType();
        this.amount = t.getAmount();
        this.currency = t.getCurrency();
        this.status = t.getStatus();
        this.createdAt = t.getCreatedAt();
        this.metadata = t.getMetadata();
    }

    public UUID getId() { return id; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public String getMetadata() { return metadata; }
}
