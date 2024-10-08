package dml.gamecurrency.entity;

public interface GameCurrencyAccount {
    void setId(Object id);

    Object getId();

    void setCurrency(String currency);

    long deposit(String amount);

    String getBalance();

    long withdraw(String amount);
}
