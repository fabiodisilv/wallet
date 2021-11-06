package it.fabio.wallet;

import it.fabio.wallet.model.OperationType;
import it.fabio.wallet.model.Transaction;
import it.fabio.wallet.model.TransactionRequest;
import it.fabio.wallet.model.Wallet;
import it.fabio.wallet.repository.TransactionRepository;
import it.fabio.wallet.repository.WalletRepository;
import it.fabio.wallet.service.TransactionService;
import it.fabio.wallet.service.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class WalletApplicationTests {
	@Autowired
	WalletService walletService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	TransactionRepository transactionRepository;

	Wallet w1;
	Transaction t1;
	Transaction t2;

	@BeforeEach
	void createTestWallet() {
		w1 = new Wallet(999L, 999L, 5D);
		t1 = new Transaction(998L, OperationType.CREDIT, 10D, w1);
		t2 = new Transaction(999L, OperationType.DEBIT, 5D, w1);
		w1.addTransaction(t1);
		w1.addTransaction(t2);
		walletRepository.save(w1);
	}

	@AfterEach
	void removeTestWallet(){
		walletRepository.deleteById(w1.getId());
	}


	@Test
	void TestIsUniqueTransactionId(){
		Long usedTransactionId = w1.getTransactions().get(0).getId();
		Assertions.assertFalse(transactionService.isUniqueTransactionId(usedTransactionId));
	}

	@Test
	void TestIsValidTransactionRequestPositive(){
		TransactionRequest transactionRequest = new TransactionRequest(1L, 1L, 1D);
		transactionRequest.setOperationType(OperationType.CREDIT);

		Assertions.assertTrue(walletService.isValidTransactionRequest(transactionRequest));

	}

	@Test
	void TestIsValidTransactionRequestNegative(){
		TransactionRequest transactionRequest = new TransactionRequest(1L, 1L, null);
		transactionRequest.setOperationType(OperationType.CREDIT);

		Assertions.assertFalse(walletService.isValidTransactionRequest(transactionRequest));

	}

	@Test
	void TestGetBalance(){
		Assertions.assertEquals(w1.getBalance(), 5d);
	}

	@Test
	void TestGetHistory(){
		List<Transaction> expectedTransactions = new ArrayList<>();
		expectedTransactions.add(t1);
		expectedTransactions.add(t2);

		List<Transaction> actualTransactions = walletService.getHistory(w1.getPlayerId());

		Assertions.assertEquals(expectedTransactions.size(), actualTransactions.size());

		List<Transaction> actualTransactionsOrdered = actualTransactions.stream()
				.sorted((t1, t2)-> t1.getId().compareTo(t2.getId()))
				.collect(Collectors.toList());

		for(int i = 0; i < expectedTransactions.size(); i++){
			Assertions.assertEquals(expectedTransactions.get(i).getId(), actualTransactionsOrdered.get(i).getId());
			Assertions.assertEquals(expectedTransactions.get(i).getOperationType(), actualTransactionsOrdered.get(i).getOperationType());
			Assertions.assertEquals(expectedTransactions.get(i).getAmount(), actualTransactionsOrdered.get(i).getAmount());
			Assertions.assertEquals(expectedTransactions.get(i).getWallet().getId(), actualTransactionsOrdered.get(i).getWallet().getId());
		}
	}

	@Test
	void TestAddTransactionPositive(){

		TransactionRequest transactionRequest = new TransactionRequest(999L, 1000L, 10D);
		transactionRequest.setOperationType(OperationType.CREDIT);

		String expectedTransactionResult = "Transaction Completed";
		String actualTransactionResult = walletService.addTransaction(transactionRequest);

		Assertions.assertEquals(expectedTransactionResult, actualTransactionResult);

		Assertions.assertEquals(w1.getBalance() + 10D, walletRepository.findByPlayerId(999L).get().getBalance());

		Transaction actualTransaction = transactionRepository.findById(1000L).get();
		Assertions.assertEquals(transactionRequest.getTransactionId(), actualTransaction.getId());
		Assertions.assertEquals(transactionRequest.getPlayerId(), actualTransaction.getWallet().getPlayerId());
		Assertions.assertEquals(transactionRequest.getOperationType(), actualTransaction.getOperationType());
		Assertions.assertEquals(transactionRequest.getAmount(), actualTransaction.getAmount());
	}

	@Test
	void TestAddTransactionNegativeNotUniqueTransaction(){
		TransactionRequest transactionRequest = new TransactionRequest(999L, 999L, 10D);
		transactionRequest.setOperationType(OperationType.CREDIT);

		String expectedTransactionResult = "Transaction not processable. Reason: transactionId not unique";
		String actualTransactionResult = walletService.addTransaction(transactionRequest);

		Assertions.assertEquals(expectedTransactionResult, actualTransactionResult);
	}

	@Test
	void TestAddTransactionNegativeNotEnoughBalance(){
		TransactionRequest transactionRequest = new TransactionRequest(999L, 1000L, 1000D);
		transactionRequest.setOperationType(OperationType.DEBIT);

		String expectedTransactionResult = "Transaction not processable. Reason: Balance not enough.";
		String actualTransactionResult = walletService.addTransaction(transactionRequest);

		Assertions.assertEquals(expectedTransactionResult, actualTransactionResult);
	}
}


