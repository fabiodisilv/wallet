package it.fabio.wallet.service.impl;

import it.fabio.wallet.repository.TransactionRepository;
import it.fabio.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    /**
     * Checks if the transaction id is already present
     *
     * @param transactionId the transactionId
     * @return true if the transactionId is unique
     */
    @Override
    public boolean isUniqueTransactionId(Long transactionId) {
        return !transactionRepository.existsById(transactionId);
    }
}
