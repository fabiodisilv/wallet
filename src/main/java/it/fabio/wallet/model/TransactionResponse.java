package it.fabio.wallet.model;

public class TransactionResponse {
    private final Long id;
    private final OperationType operationType;
    private final Double amount;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.operationType = transaction.getOperationType();
        this.amount = transaction.getAmount();
    }

    public Long getId() {
        return id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Double getAmount() {
        return amount;
    }
}
