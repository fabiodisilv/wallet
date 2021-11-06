package it.fabio.wallet.model;

import javax.persistence.*;

@Entity
public class Transaction {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private Double amount;
    @ManyToOne
    private Wallet wallet;

    public Transaction() {
    }

    public Transaction(Long id, OperationType operationType, Double amount, Wallet wallet) {
        this.id = id;
        this.operationType = operationType;
        this.amount = amount;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
