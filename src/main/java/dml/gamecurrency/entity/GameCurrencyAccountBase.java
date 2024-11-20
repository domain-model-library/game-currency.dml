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

    @Override
    public boolean isBalanceInRange(String minBalance, String maxBalance) {
        BigDecimal balance = new BigDecimal(this.balance);
        BigDecimal min = new BigDecimal(minBalance);
        BigDecimal max = new BigDecimal(maxBalance);
        return balance.compareTo(min) >= 0 && balance.compareTo(max) <= 0;
    }

    @Override
    public boolean isBalanceGreaterThanOrEqualTo(String value) {
        BigDecimal balance = new BigDecimal(this.balance);
        BigDecimal targetBalance = new BigDecimal(value);
        return balance.compareTo(targetBalance) >= 0;
    }


}
