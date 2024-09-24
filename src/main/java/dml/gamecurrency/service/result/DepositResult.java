package dml.gamecurrency.service.result;

import dml.gamecurrency.entity.GameCurrencyAccount;
import dml.gamecurrency.entity.GameCurrencyAccountBillItem;

public class DepositResult {
    private GameCurrencyAccount account;
    private GameCurrencyAccountBillItem billItem;

    public GameCurrencyAccount getAccount() {
        return account;
    }

    public void setAccount(GameCurrencyAccount account) {
        this.account = account;
    }

    public GameCurrencyAccountBillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(GameCurrencyAccountBillItem billItem) {
        this.billItem = billItem;
    }
}
