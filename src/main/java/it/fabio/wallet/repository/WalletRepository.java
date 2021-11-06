package it.fabio.wallet.repository;

import it.fabio.wallet.model.Wallet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findByPlayerId(Long playerId);
}
