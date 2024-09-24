package dml.gamecurrency.entity;

public abstract class GameCurrencyAccountBillItemBase implements GameCurrencyAccountBillItem {

    protected Object accountId;
    protected long transactionNumber;
    protected String transactionAmount;

    @Override
    public void setAccountId(Object accountId) {
        this.accountId = accountId;
    }

    @Override
    public Object getAccountId() {
        return accountId;
    }

    @Override
    public void setTransactionNumber(long transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    @Override
    public long getAccountTransactionNumber() {
        return transactionNumber;
    }

    @Override
    public String getTransactionAmount() {
        return transactionAmount;
    }

    @Override
    public void setTransactionAmount(String amount) {
        this.transactionAmount = amount;
    }
}
