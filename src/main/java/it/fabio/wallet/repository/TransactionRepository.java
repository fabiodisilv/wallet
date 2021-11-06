package it.fabio.wallet.repository;

import it.fabio.wallet.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
