package it.fabio.wallet.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wallet {

    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private Long playerId;
    private Double balance;
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Wallet() {
    }

    public Wallet(Long id, Long playerId, Double balance) {
        this.id = id;
        this.playerId = playerId;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        if (transactions == null){
            transactions = new ArrayList<>();
        }
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        getTransactions().add(transaction);
    }
}
