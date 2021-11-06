package it.fabio.wallet.service.impl;

import it.fabio.wallet.model.*;
import it.fabio.wallet.repository.TransactionRepository;
import it.fabio.wallet.repository.WalletRepository;
import it.fabio.wallet.service.TransactionService;
import it.fabio.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class WalletServiceImpl implements WalletService {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionService transactionService;

    /**
     * Returns the current balance for the given playerId
     *
     * @param playerId the id of the player
     * @return the balance of the user
     */
    @Override
    public Double getBalance(Long playerId) {
        Double result = null;
        Optional<Wallet> optionalWallet = walletRepository.findByPlayerId(playerId);
        if(optionalWallet.isPresent()){
            result = optionalWallet.get().getBalance();
        }
        return result;
    }

    /**
     * Add a Transaction for a given playerId if the transaction is admissible
     *
     * @param transactionRequest the id of the player
     * @return the description of the transaction result
     */
    @Transactional
    @Override
    public String addTransaction(TransactionRequest transactionRequest){
        String result = "";
        Optional<Wallet> optionalWallet = walletRepository.findByPlayerId(transactionRequest.getPlayerId());
        if(optionalWallet.isEmpty()){
            result = "Transaction not processable. Reason: Invalid playerId";
        }else if(!transactionService.isUniqueTransactionId(transactionRequest.getTransactionId())){
                result = "Transaction not processable. Reason: transactionId not unique";
            }else if(transactionRequest.getOperationType() == OperationType.DEBIT && !isBalanceEnough(optionalWallet.get(), transactionRequest.getAmount())){
                result = "Transaction not processable. Reason: Balance not enough.";
            }else {
            Wallet wallet = optionalWallet.get();
                Transaction transaction = new Transaction(transactionRequest.getTransactionId(), transactionRequest.getOperationType(), transactionRequest.getAmount(), wallet);
                wallet = processTransaction(wallet, transaction);
                result = "Transaction Completed";
            }
        return result;
    }

    /**
     * Returns the transaction history for the given playerId
     *
     * @param playerId the id of the player
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getHistory(Long playerId) {
        List<Transaction> transactions = null;
        Optional<Wallet> optionalWallet = walletRepository.findByPlayerId(playerId);
        if(optionalWallet.isPresent()){
            transactions = optionalWallet.get().getTransactions();
        }
        return transactions;
    }

    /**
     * Returns the transaction history for the given playerId
     *
     * @param transactionRequest the id of the player
     * @return the list of transactions
     */
    @Override
    public boolean isValidTransactionRequest(TransactionRequest transactionRequest) {
        boolean result = transactionRequest.getTransactionId() != null &&
                transactionRequest.getOperationType() != null &&
                transactionRequest.getPlayerId() != null &&
                transactionRequest.getAmount() != null &&
                transactionRequest.getAmount() > 0;

        return result;
    }

    /**
     * Checks if the balance is enough for the given amount
     *
     * @param wallet the wallet object
     * @param debitAmount the amount
     * @return true if the balance is enough
     */
    private boolean isBalanceEnough(Wallet wallet, Double debitAmount){
        return wallet.getBalance()-debitAmount >= 0;
    }

    /**
     * Process the transaction updating the balance and adding the transaction to the list
     *
     * @param wallet the wallet object
     * @param transaction the transaction object
     * @return the updated wallet object
     */
    private Wallet processTransaction(Wallet wallet, Transaction transaction){
        if(transaction.getOperationType() == OperationType.CREDIT){
            wallet.setBalance(wallet.getBalance() + transaction.getAmount());
        }else{
            wallet.setBalance(wallet.getBalance() - transaction.getAmount());
        }
        wallet.addTransaction(transaction);

        return wallet;
    }
}
