package it.fabio.wallet.controller;

import it.fabio.wallet.model.OperationType;
import it.fabio.wallet.model.Transaction;
import it.fabio.wallet.model.TransactionRequest;
import it.fabio.wallet.model.TransactionResponse;
import it.fabio.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @GetMapping("/{playerId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long playerId){
        ResponseEntity<Double> responseEntity;

        Double result = walletService.getBalance(playerId);

        if(result == null){
            responseEntity = new ResponseEntity<Double>(HttpStatus.NOT_FOUND);
        }else {
            responseEntity = new ResponseEntity<Double>(result, HttpStatus.OK);
        }
        return responseEntity;
    }

    @GetMapping("/{playerId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getHistory(@PathVariable Long playerId){
        ResponseEntity<List<TransactionResponse>> responseEntity;
        List<Transaction> result = walletService.getHistory(playerId);

        if(result == null){
            responseEntity = new ResponseEntity<List<TransactionResponse>>(HttpStatus.NOT_FOUND);
        }else {
            responseEntity = new ResponseEntity<List<TransactionResponse>>(result.stream()
                    .map(transaction -> new TransactionResponse(transaction))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest transactionRequest){
        transactionRequest.setOperationType(OperationType.DEBIT);
        return processTransactionRequest(transactionRequest);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest transactionRequest){
        transactionRequest.setOperationType(OperationType.CREDIT);
        return processTransactionRequest(transactionRequest);
    }

    private ResponseEntity<String> processTransactionRequest(TransactionRequest transactionRequest){
        ResponseEntity<String> responseEntity;
        if(!walletService.isValidTransactionRequest(transactionRequest)){
            responseEntity = new ResponseEntity<String>("Invalid Transaction request", HttpStatus.BAD_REQUEST);
        }else {
            responseEntity = new ResponseEntity<String>(walletService.addTransaction(transactionRequest), HttpStatus.OK);
        }
        return responseEntity;
    }

}
