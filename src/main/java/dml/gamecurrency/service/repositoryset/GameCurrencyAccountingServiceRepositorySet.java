package dml.gamecurrency.service.repositoryset;

import dml.gamecurrency.repository.GameCurrencyAccountBillItemRepository;
import dml.gamecurrency.repository.GameCurrencyAccountRepository;
import dml.gamecurrency.repository.GameUserCurrencyAccountsRepository;

public interface GameCurrencyAccountingServiceRepositorySet {
    GameCurrencyAccountRepository getGameCurrencyAccountRepository();

    GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository();

    GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository();
}
