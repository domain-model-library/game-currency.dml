package dml.gamecurrency.entity;

import java.util.HashMap;
import java.util.Map;

public class GameUserCurrencyAccounts {
    private Object userId;
    private Map<String, Object> currencyToAccountIdMap = new HashMap<>();

    public boolean hasAccount(String accountType) {
        return currencyToAccountIdMap.containsKey(accountType);
    }

    public void putAccount(String accountType, Object accountId) {
        currencyToAccountIdMap.put(accountType, accountId);
    }

    public Object getAccount(String currency) {
        return currencyToAccountIdMap.get(currency);
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Map<String, Object> getCurrencyToAccountIdMap() {
        return currencyToAccountIdMap;
    }

    public void setCurrencyToAccountIdMap(Map<String, Object> currencyToAccountIdMap) {
        this.currencyToAccountIdMap = currencyToAccountIdMap;
    }

}
