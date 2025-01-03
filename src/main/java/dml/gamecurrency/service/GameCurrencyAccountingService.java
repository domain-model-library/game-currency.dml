package dml.gamecurrency.service;

import dml.gamecurrency.entity.GameCurrencyAccount;
import dml.gamecurrency.entity.GameCurrencyAccountBillItem;
import dml.gamecurrency.entity.GameUserCurrencyAccounts;
import dml.gamecurrency.repository.*;
import dml.gamecurrency.service.repositoryset.GameCurrencyAccountingServiceRepositorySet;
import dml.gamecurrency.service.result.DepositResult;
import dml.gamecurrency.service.result.WithdrawResult;

import java.util.ArrayList;
import java.util.List;

public class GameCurrencyAccountingService {
    public static GameCurrencyAccount createAccount(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                                    Object userId, String currency, GameCurrencyAccount newAccount) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository = repositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountIdGeneratorRepository accountIdGeneratorRepository = repositorySet.getGameCurrencyAccountIdGeneratorRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = new GameUserCurrencyAccounts();
        userAccounts.setUserId(userId);
        userAccounts = userAccountsRepository.takeOrPutIfAbsent(userId, userAccounts);
        if (userAccounts.hasAccount(currency)) {
            throw new RuntimeException("Account already exists");
        }
        newAccount.setId(accountIdGeneratorRepository.take().generateId());
        newAccount.setCurrency(currency);
        accountRepository.put(newAccount);
        userAccounts.putAccount(currency, newAccount.getId());
        return newAccount;
    }

    public static GameCurrencyAccount getOrCreateAccount(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                                         Object userId, String currency, GameCurrencyAccount newAccount) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository = repositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountIdGeneratorRepository accountIdGeneratorRepository = repositorySet.getGameCurrencyAccountIdGeneratorRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = new GameUserCurrencyAccounts();
        userAccounts.setUserId(userId);
        userAccounts = userAccountsRepository.takeOrPutIfAbsent(userId, userAccounts);
        Object accountId = userAccounts.getAccount(currency);
        if (accountId == null) {
            newAccount.setId(accountIdGeneratorRepository.take().generateId());
            newAccount.setCurrency(currency);
            accountRepository.put(newAccount);
            userAccounts.putAccount(currency, newAccount.getId());
            return newAccount;
        } else {
            return accountRepository.find(accountId);
        }
    }

    public static GameCurrencyAccount getAccount(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                                 Object userId, String currency) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository = repositorySet.getGameCurrencyAccountRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = userAccountsRepository.find(userId);
        if (userAccounts == null) {
            return null;
        }
        Object accountId = userAccounts.getAccount(currency);
        if (accountId == null) {
            return null;
        }
        return accountRepository.find(accountId);
    }

    public static List<GameCurrencyAccount> getAllAccountsForUser(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                                                  Object userId) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository = repositorySet.getGameCurrencyAccountRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = userAccountsRepository.find(userId);
        if (userAccounts == null) {
            return null;
        }
        List<GameCurrencyAccount> accounts = new ArrayList<>();
        List<Object> accountIds = userAccounts.getAllAccountIds();
        for (Object accountId : accountIds) {
            accounts.add(accountRepository.find(accountId));
        }
        return accounts;
    }

    public static DepositResult deposit(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                        Object accountId, String amount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                repositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountBillItemRepository<GameCurrencyAccountBillItem, Object> gameCurrencyAccountBillItemRepository =
                repositorySet.getGameCurrencyAccountBillItemRepository();
        GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
                repositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();

        GameCurrencyAccount account = accountRepository.take(accountId);
        long transactionNumber = account.deposit(amount);
        newGameCurrencyAccountBillItem.setId(gameCurrencyAccountBillItemIdGeneratorRepository.take().generateId());
        newGameCurrencyAccountBillItem.setAccountId(accountId);
        newGameCurrencyAccountBillItem.setTransactionNumber(transactionNumber);
        newGameCurrencyAccountBillItem.setTransactionAmount(amount);
        gameCurrencyAccountBillItemRepository.put(newGameCurrencyAccountBillItem);

        DepositResult depositResult = new DepositResult();
        depositResult.setAccount(account);
        depositResult.setBillItem(newGameCurrencyAccountBillItem);
        return depositResult;

    }

    public static DepositResult deposit(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                        Object userId, String currency, String amount, GameCurrencyAccount newAccount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                repositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountBillItemRepository<GameCurrencyAccountBillItem, Object> gameCurrencyAccountBillItemRepository =
                repositorySet.getGameCurrencyAccountBillItemRepository();
        GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
                repositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();
        GameCurrencyAccountIdGeneratorRepository accountIdGeneratorRepository = repositorySet.getGameCurrencyAccountIdGeneratorRepository();

        GameUserCurrencyAccounts userAccounts = new GameUserCurrencyAccounts();
        userAccounts.setUserId(userId);
        userAccounts = userAccountsRepository.takeOrPutIfAbsent(userId, userAccounts);
        GameCurrencyAccount account;
        if (userAccounts.getAccount(currency) == null) {
            account = newAccount;
            account.setId(accountIdGeneratorRepository.take().generateId());
            account.setCurrency(currency);
            accountRepository.put(account);
            userAccounts.putAccount(currency, account.getId());
        } else {
            account = accountRepository.take(userAccounts.getAccount(currency));
        }
        long transactionNumber = account.deposit(amount);
        newGameCurrencyAccountBillItem.setId(gameCurrencyAccountBillItemIdGeneratorRepository.take().generateId());
        newGameCurrencyAccountBillItem.setAccountId(account.getId());
        newGameCurrencyAccountBillItem.setTransactionNumber(transactionNumber);
        newGameCurrencyAccountBillItem.setTransactionAmount(amount);
        gameCurrencyAccountBillItemRepository.put(newGameCurrencyAccountBillItem);

        DepositResult depositResult = new DepositResult();
        depositResult.setAccount(account);
        depositResult.setBillItem(newGameCurrencyAccountBillItem);
        return depositResult;
    }

    public static WithdrawResult withdraw(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                          Object userId, String currency, String amount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                repositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountBillItemRepository<GameCurrencyAccountBillItem, Object> gameCurrencyAccountBillItemRepository =
                repositorySet.getGameCurrencyAccountBillItemRepository();
        GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
                repositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = userAccountsRepository.find(userId);
        Object accountId = userAccounts.getAccount(currency);
        GameCurrencyAccount account = accountRepository.take(accountId);
        long transactionNumber = account.withdraw(amount);
        newGameCurrencyAccountBillItem.setId(gameCurrencyAccountBillItemIdGeneratorRepository.take().generateId());
        newGameCurrencyAccountBillItem.setAccountId(accountId);
        newGameCurrencyAccountBillItem.setTransactionNumber(transactionNumber);
        newGameCurrencyAccountBillItem.setTransactionAmount(amount);
        gameCurrencyAccountBillItemRepository.put(newGameCurrencyAccountBillItem);

        WithdrawResult withdrawResult = new WithdrawResult();
        withdrawResult.setAccount(account);
        withdrawResult.setBillItem(newGameCurrencyAccountBillItem);
        return withdrawResult;
    }

    public static WithdrawResult withdraw(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                          Object accountId, String amount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                repositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountBillItemRepository<GameCurrencyAccountBillItem, Object> gameCurrencyAccountBillItemRepository =
                repositorySet.getGameCurrencyAccountBillItemRepository();
        GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
                repositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();

        GameCurrencyAccount account = accountRepository.take(accountId);
        long transactionNumber = account.withdraw(amount);
        newGameCurrencyAccountBillItem.setId(gameCurrencyAccountBillItemIdGeneratorRepository.take().generateId());
        newGameCurrencyAccountBillItem.setAccountId(accountId);
        newGameCurrencyAccountBillItem.setTransactionNumber(transactionNumber);
        newGameCurrencyAccountBillItem.setTransactionAmount(amount);
        gameCurrencyAccountBillItemRepository.put(newGameCurrencyAccountBillItem);

        WithdrawResult withdrawResult = new WithdrawResult();
        withdrawResult.setAccount(account);
        withdrawResult.setBillItem(newGameCurrencyAccountBillItem);
        return withdrawResult;
    }

    public static boolean isBalanceInRange(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                           Object userId, String currency, String minBalance, String maxBalance) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                repositorySet.getGameCurrencyAccountRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = userAccountsRepository.find(userId);
        if (userAccounts == null) {
            return false;
        }
        Object accountId = userAccounts.getAccount(currency);
        if (accountId == null) {
            return false;
        }
        GameCurrencyAccount account = accountRepository.find(accountId);
        return account.isBalanceInRange(minBalance, maxBalance);
    }

    /**
     * 余额是否大于等于指定值
     */
    public static boolean isBalanceGreaterThanOrEqualTo(GameCurrencyAccountingServiceRepositorySet repositorySet,
                                                        Object userId, String currency, String balance) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                repositorySet.getGameCurrencyAccountRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = repositorySet.getGameUserCurrencyAccountsRepository();

        GameUserCurrencyAccounts userAccounts = userAccountsRepository.find(userId);
        if (userAccounts == null) {
            return false;
        }
        Object accountId = userAccounts.getAccount(currency);
        if (accountId == null) {
            return false;
        }
        GameCurrencyAccount account = accountRepository.find(accountId);
        return account.isBalanceGreaterThanOrEqualTo(balance);
    }


}
