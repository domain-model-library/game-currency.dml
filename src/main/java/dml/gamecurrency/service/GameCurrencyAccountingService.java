package dml.gamecurrency.service;

import dml.gamecurrency.entity.GameCurrencyAccount;
import dml.gamecurrency.entity.GameCurrencyAccountBillItem;
import dml.gamecurrency.entity.GameUserCurrencyAccounts;
import dml.gamecurrency.repository.*;
import dml.gamecurrency.service.repositoryset.GameCurrencyAccountingServiceRepositorySet;
import dml.gamecurrency.service.result.DepositResult;
import dml.gamecurrency.service.result.WithdrawResult;

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

    public static DepositResult deposit(GameCurrencyAccountingServiceRepositorySet gameCurrencyAccountingServiceRepositorySet,
                                        Object accountId, String amount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                gameCurrencyAccountingServiceRepositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountBillItemRepository<GameCurrencyAccountBillItem, Object> gameCurrencyAccountBillItemRepository =
                gameCurrencyAccountingServiceRepositorySet.getGameCurrencyAccountBillItemRepository();
        GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
                gameCurrencyAccountingServiceRepositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();

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

    public static WithdrawResult withdraw(GameCurrencyAccountingServiceRepositorySet gameCurrencyAccountingServiceRepositorySet,
                                          Object userId, String currency, String amount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem) {
        GameCurrencyAccountRepository<GameCurrencyAccount, Object> accountRepository =
                gameCurrencyAccountingServiceRepositorySet.getGameCurrencyAccountRepository();
        GameCurrencyAccountBillItemRepository<GameCurrencyAccountBillItem, Object> gameCurrencyAccountBillItemRepository =
                gameCurrencyAccountingServiceRepositorySet.getGameCurrencyAccountBillItemRepository();
        GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
                gameCurrencyAccountingServiceRepositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();
        GameUserCurrencyAccountsRepository userAccountsRepository = gameCurrencyAccountingServiceRepositorySet.getGameUserCurrencyAccountsRepository();

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


}
