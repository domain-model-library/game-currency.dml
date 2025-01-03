package dml.gamecurrency.entity;

public interface GameCurrencyAccountBillItem {
    void setId(Object id);

    Object getId();

    void setAccountId(Object accountId);

    Object getAccountId();

    void setTransactionNumber(long transactionNumber);

    long getAccountTransactionNumber();

    String getTransactionAmount();

    void setTransactionAmount(String amount);
}
