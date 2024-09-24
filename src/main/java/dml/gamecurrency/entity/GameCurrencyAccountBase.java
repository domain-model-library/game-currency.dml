package dml.gamecurrency.entity;

import java.math.BigDecimal;

public abstract class GameCurrencyAccountBase implements GameCurrencyAccount {

    protected String currency;
    protected String balance = "0";
    protected long transactionNumber;

    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public long deposit(String amount) {
        BigDecimal balance = new BigDecimal(this.balance);
        BigDecimal depositAmount = new BigDecimal(amount);
        this.balance = balance.add(depositAmount).toString();
        transactionNumber++;
        return transactionNumber;
    }

    @Override
    public String getBalance() {
        return balance;
    }

    @Override
    public long withdraw(String amount) {
        BigDecimal balance = new BigDecimal(this.balance);
        BigDecimal withdrawAmount = new BigDecimal(amount);
        this.balance = balance.subtract(withdrawAmount).toString();
        transactionNumber++;
        return transactionNumber;
    }

    
}
