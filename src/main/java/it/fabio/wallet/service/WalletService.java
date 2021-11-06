package it.fabio.wallet.service;

import it.fabio.wallet.model.Transaction;
import it.fabio.wallet.model.TransactionRequest;

import java.util.List;

public interface WalletService {
    Double getBalance(Long playerId);

    String addTransaction(TransactionRequest transactionRequest);

    List<Transaction> getHistory(Long playerId);

    boolean isValidTransactionRequest(TransactionRequest transactionRequest);
}
