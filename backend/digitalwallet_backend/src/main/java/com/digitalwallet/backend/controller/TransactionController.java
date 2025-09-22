package com.digitalwallet.backend.controller;

import com.digitalwallet.backend.dto.*;
import com.digitalwallet.backend.model.Transaction;
import com.digitalwallet.backend.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService txService;

    public TransactionController(TransactionService txService) {
        this.txService = txService;
    }

    @PostMapping("/transactions/send")
    public ResponseEntity<TxResponse> sendMoney(Authentication authentication, @RequestBody SendRequest req) {
        UUID userId = UUID.fromString(authentication.getName());
        Transaction tx = txService.sendMoney(userId, req.getTo(), req.getAmount());
        return ResponseEntity.ok(new TxResponse(tx));
    }

    @PostMapping("/bills/pay")
    public ResponseEntity<TxResponse> payBill(Authentication authentication, @RequestBody PayBillRequest req) {
        UUID userId = UUID.fromString(authentication.getName());
        Transaction tx = txService.payBill(userId, req.getBiller(), req.getAccount(), req.getAmount());
        return ResponseEntity.ok(new TxResponse(tx));
    }

    @PostMapping("/qr/pay")
    public ResponseEntity<TxResponse> payQr(Authentication authentication, @RequestBody QrPayRequest req) {
        UUID userId = UUID.fromString(authentication.getName());
        Transaction tx = txService.payQr(userId, req.getCode(), req.getAmount());
        return ResponseEntity.ok(new TxResponse(tx));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TxResponse>> list(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<Transaction> list = txService.listForUser(userId);
        List<TxResponse> resp = list.stream().map(TxResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }
}
