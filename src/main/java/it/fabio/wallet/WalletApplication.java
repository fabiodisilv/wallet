package it.fabio.wallet;

import it.fabio.wallet.model.OperationType;
import it.fabio.wallet.model.Transaction;
import it.fabio.wallet.model.Wallet;
import it.fabio.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

	@Component
	public class ApplicationStartupRunner implements ApplicationRunner {

		@Autowired
		WalletRepository walletRepository;

		@Override
		public void run(ApplicationArguments args) {

			if (walletRepository.count() == 0) {
				Wallet w1 = new Wallet(1L, 1L, 10d);
				Transaction t1 = new Transaction(1L, OperationType.CREDIT, 10d, w1);
				w1.addTransaction(t1);
				walletRepository.save(w1);

				Wallet w2 = new Wallet(2L, 2L, 2d);
				Transaction t2 = new Transaction(2L, OperationType.CREDIT, 10d, w2);
				Transaction t3 = new Transaction(3L, OperationType.DEBIT, 8d, w2);
				w2.addTransaction(t2);
				w2.addTransaction(t3);
				walletRepository.save(w2);

				Wallet w3 = new Wallet(3L, 3L, 20d);
				Transaction t4 = new Transaction(4L, OperationType.CREDIT, 50d, w3);
				Transaction t5 = new Transaction(5L, OperationType.DEBIT, 10d, w3);
				Transaction t6 = new Transaction(6L, OperationType.DEBIT, 20d, w3);
				w3.addTransaction(t4);
				w3.addTransaction(t5);
				w3.addTransaction(t6);
				walletRepository.save(w3);

			}
		}
	}
}
