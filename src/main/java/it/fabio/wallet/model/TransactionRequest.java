package it.fabio.wallet.model;

public class TransactionRequest {

    private Long playerId, transactionId;
    private OperationType operationType;
    private Double amount;

    public TransactionRequest() {
    }

    public TransactionRequest(Long playerId, Long transactionId, Double amount) {
        this.playerId = playerId;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
